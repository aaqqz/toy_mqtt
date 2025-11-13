package com.epikar.drivegateway.mqtt.listener;

import com.epikar.drivegateway.mqtt.service.MqttPublisherService;
import com.epikar.drivegateway.mqtt.service.dto.PublishDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceMessageListener {

    private final MqttPublisherService publisherService;
    private final MessageFormatter messageFormatter;

    @ServiceActivator(inputChannel = "mqttInboundChannel")
    public void handleMessage(Message<?> message) {
        log.debug("[Subscribe] message: {}", message);

        String payload = messageFormatter.convertPayloadToString(message.getPayload());
        String deviceNo = null;
        String messageId = null;

        try {
            deviceNo = messageFormatter.extractDeviceNo(payload);
            messageId = messageFormatter.extractMessageId(payload);

            process(payload, message.getHeaders());

            publisherService.sendSuccess(deviceNo, messageId);
        } catch (Exception e) {
            publisherService.sendFail(deviceNo, messageId);
            log.error("[ERROR Subscribe] {}", e.getMessage(), e);
        }
    }

    private void process(String payload, MessageHeaders headers) {
        log.info("process 처리 시작: {}, {}", payload, headers);

        // todo
        // 여기에 실제 처리 로직 구현
        // JSON 파싱, 데이터베이스 저장, 다른 서비스 호출 등

        log.info("process 처리 완료");
    }
}
