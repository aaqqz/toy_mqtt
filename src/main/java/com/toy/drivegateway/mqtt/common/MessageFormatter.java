package com.toy.drivegateway.mqtt.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.toy.drivegateway.mqtt.common.MqttConstants.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageFormatter {

    private final ObjectMapper objectMapper;

    public String convertPayloadToString(Object payload) {
        if (payload instanceof byte[]) {
            return new String((byte[]) payload);
        }

        return String.valueOf(payload);
    }

    public String extractDeviceNo(String payload) throws JsonProcessingException {
        return extractJsonFieldToString(payload, DEVICE_NO);
    }

    public String extractMessageId(String payload) throws JsonProcessingException {
        return extractJsonFieldToString(payload, MESSAGE_ID);
    }

    public MessageType extractMessageType(String payload) throws JsonProcessingException {
        return extractJsonFieldToMessageType(payload, MESSAGE_TYPE);
    }

    private MessageType extractJsonFieldToMessageType(String payload, String messageType) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(payload);
        JsonNode fieldNode = jsonNode.get(messageType);

        if (fieldNode != null) {
            MessageType value = MessageType.valueOf(fieldNode.asText().toUpperCase());
            log.debug("extracted field {} >> {}", messageType, value.name());
            return value;
        }

        throw new IllegalArgumentException(messageType + " 필드를 찾을 수 없습니다.");
    }

    private String extractJsonFieldToString(String payload, String fieldName) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(payload);
        JsonNode fieldNode = jsonNode.get(fieldName);

        if (fieldNode != null) {
            String value = fieldNode.asText();
            log.debug("extracted field {} >> {}", fieldName, value);
            return value;
        }

        throw new IllegalArgumentException(fieldName + " 필드를 찾을 수 없습니다.");
    }
}
