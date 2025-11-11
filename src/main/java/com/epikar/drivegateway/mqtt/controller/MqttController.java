package com.epikar.drivegateway.mqtt.controller;

import com.epikar.drivegateway.mqtt.event.dto.OutboundDto;
import com.epikar.drivegateway.mqtt.event.outbound.OutboundService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MqttController {

    private final OutboundService outboundService;

    @PostMapping("/mqtt/publish")
    public void publishToTopic(@RequestBody PublishRequest request) {
        log.info("publishToTopic >> {}", request);
    }
}
