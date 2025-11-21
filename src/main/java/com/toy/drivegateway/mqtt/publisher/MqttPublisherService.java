package com.toy.drivegateway.mqtt.publisher;

import com.toy.drivegateway.mqtt.publisher.dto.PublishDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Service;

import static com.toy.drivegateway.mqtt.common.MqttConstants.TOPIC_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class MqttPublisherService {

    private final MessageChannel mqttOutboundChannel;
    private final ObjectMapper objectMapper;

    public void publish(String topic, PublishDto publishDto) {
        try {
            String payload = objectMapper.writeValueAsString(publishDto);

            mqttOutboundChannel.send(
                    MessageBuilder
                            .withPayload(payload)
                            .setHeader(MqttHeaders.TOPIC, TOPIC_PREFIX + topic)
                            .build()
            );

            log.info("[SUCCESS publish] Topic: {}", TOPIC_PREFIX + topic);
            log.info("[SUCCESS publish] payload: {}", payload);
        } catch (Exception e) {
            log.error("[ERROR publish] Topic: {}, Error: {}", TOPIC_PREFIX + topic, e.getMessage(), e);
        }

    }

    public void sendSuccess(String deviceNo, String messageId) {
        sendToMqtt(deviceNo, messageId, true);
    }

    public void sendFail(String deviceNo, String messageId) {
        sendToMqtt(deviceNo, messageId, false);
    }

    private void sendToMqtt(String deviceNo, String messageId, boolean isSuccess) {
        if (deviceNo == null || deviceNo.isEmpty()) {
            log.warn("device_no가 없어서 응답 메시지를 발송하지 않습니다.");
            return;
        }

        PublishDto publishDto = PublishDto.builder()
                .device_no(deviceNo)
                .message_id(messageId)
                .isSuccess(isSuccess)
                .build();

        publish(deviceNo, publishDto);
    }
}
