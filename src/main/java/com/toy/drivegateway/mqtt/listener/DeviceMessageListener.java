package com.toy.drivegateway.mqtt.listener;

import com.toy.drivegateway.mqtt.common.MessageFormatter;
import com.toy.drivegateway.mqtt.handler.DeviceMessageHandler;
import com.toy.drivegateway.mqtt.publisher.MqttPublisherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceMessageListener {

    private final MqttPublisherService publisherService;
    private final DeviceMessageHandler deviceMessageHandler;
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

            // 즉시 응답 (ACK)
            publisherService.sendSuccess(deviceNo, messageId);

            // 비동기 처리(업무 로직)
            if (messageFormatter.extractMessageType(payload).isReport()) {
                deviceMessageHandler.processAsync(payload, message.getHeaders());
            }

        } catch (Exception e) {
            publisherService.sendFail(deviceNo, messageId);
            log.error("[ERROR Subscribe] {}", e.getMessage(), e);
        }
    }
}
