package com.epikar.drivegateway.mqtt.event.inbound;

import com.epikar.drivegateway.mqtt.event.dto.OutboundDto;
import com.epikar.drivegateway.mqtt.event.outbound.OutboundService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import static com.epikar.drivegateway.mqtt.event.MqttConstants.DEVICE_NO;
import static com.epikar.drivegateway.mqtt.event.MqttConstants.MESSAGE_ID;

@Slf4j
@Component
@RequiredArgsConstructor
public class InboundGateway {

    private final ObjectMapper objectMapper;
    private final OutboundService outboundService;


    // subscribe
    @ServiceActivator(inputChannel = "mqttInboundChannel")
    public void mqttSubscribe(Message<?> message) {
        log.debug("mqttSubscribe {}", message);
        String payload = convertPayloadToString(message.getPayload());
        String deviceNo = null;
        String messageId = null;

        try {
            deviceNo = extractDeviceNo(payload);
            messageId = extractMessageId(payload);

            process(payload, message.getHeaders());

            sendSuccess(deviceNo, messageId);
        } catch (Exception e) {
            log.error("[ERROR mqttSubscribe] {}", e.getMessage(), e);
            sendFail(deviceNo, messageId);
        }
    }

    private void process(String payload, MessageHeaders headers) {
        log.info("process 처리 시작: {}, {}", payload, headers);

        // todo
        // 여기에 실제 처리 로직 구현
        // JSON 파싱, 데이터베이스 저장, 다른 서비스 호출 등

        log.info("process 처리 완료");
    }

    private String convertPayloadToString(Object payload) {
        if (payload instanceof byte[]) {
            return new String((byte[]) payload);
        }

        return String.valueOf(payload);
    }

    private String extractDeviceNo(String payload) throws JsonProcessingException {
        return extractJsonField(payload, DEVICE_NO);
    }

    private String extractMessageId(String payload) throws JsonProcessingException {
        return extractJsonField(payload, MESSAGE_ID);
    }

    private String extractJsonField(String payload, String fieldName) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(payload);
        JsonNode fieldNode = jsonNode.get(fieldName);

        if (fieldNode != null) {
            String value = fieldNode.asText();
            log.debug("extracted field {} >> {}", fieldName, value);
            return value;
        }

        throw new IllegalArgumentException(fieldName + " 필드를 찾을 수 없습니다.");
    }

    private void sendSuccess(String deviceNo, String messageId) {
        sendToMqtt(deviceNo, messageId, true);
    }

    private void sendFail(String deviceNo, String messageId) {
        sendToMqtt(deviceNo, messageId, false);
    }

    private void sendToMqtt(String deviceNo, String messageId, boolean isSuccess) {
        if (deviceNo == null || deviceNo.isEmpty()) {
            log.warn("device_no가 없어서 응답 메시지를 발송하지 않습니다.");
            return;
        }

        OutboundDto outboundDto = OutboundDto.builder()
                .device_no(deviceNo)
                .message_id(messageId)
                .isSuccess(isSuccess)
                .build();

        outboundService.sendToMqtt(deviceNo, outboundDto);
    }
}
