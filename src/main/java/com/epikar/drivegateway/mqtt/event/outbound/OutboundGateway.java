package com.epikar.drivegateway.mqtt.event.outbound;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

@Component
@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface OutboundGateway {

    // publish
    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, String payload);
}
