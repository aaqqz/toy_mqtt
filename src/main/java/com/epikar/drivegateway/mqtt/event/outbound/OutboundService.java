package com.epikar.drivegateway.mqtt.event.outbound;

import com.epikar.drivegateway.mqtt.event.dto.OutboundDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.epikar.drivegateway.mqtt.event.MqttConstants.TOPIC_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboundService {

    private final ObjectMapper objectMapper;
    private final OutboundGateway outBoundGateway;

    public void sendToMqtt(String deviceNo, OutboundDto outboundDto) {
        try {
            String jsonPayload = objectMapper.writeValueAsString(outboundDto);

            outBoundGateway.sendToMqtt(TOPIC_PREFIX + deviceNo, jsonPayload);

            log.info("[SUCCESS sendToMqtt] Topic: {}", TOPIC_PREFIX + deviceNo);
            log.info("[SUCCESS sendToMqtt] response: {}", jsonPayload);
        } catch (Exception e) {
            log.error("[ERROR sendToMqtt] Topic: {}, Error: {}", TOPIC_PREFIX + deviceNo, e.getMessage(), e);
        }
    }
}
