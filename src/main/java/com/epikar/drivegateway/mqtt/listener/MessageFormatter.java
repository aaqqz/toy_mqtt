package com.epikar.drivegateway.mqtt.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.epikar.drivegateway.mqtt.common.MqttConstants.DEVICE_NO;
import static com.epikar.drivegateway.mqtt.common.MqttConstants.MESSAGE_ID;

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
        return extractJsonField(payload, DEVICE_NO);
    }

    public String extractMessageId(String payload) throws JsonProcessingException {
        return extractJsonField(payload, MESSAGE_ID);
    }

    public String extractJsonField(String payload, String fieldName) throws JsonProcessingException {
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
