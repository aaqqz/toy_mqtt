package com.epikar.drivegateway.mqtt.controller;

import com.epikar.drivegateway.mqtt.controller.request.PublishRequest;
import com.epikar.drivegateway.mqtt.service.MqttPublisherService;
import com.epikar.drivegateway.mqtt.service.dto.PublishDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MqttPublishController {

    private final MqttPublisherService publisherService;

    @PostMapping("/mqtt/publish")
    public void publish(@RequestParam String topic, @RequestBody PublishRequest request) {

        PublishDto publishDto = PublishDto.builder()
                .device_no(request.getDeviceNo())
                .message_id(request.getMessage())
                .isSuccess(true)
                .build();

        publisherService.publish(topic, publishDto);
        log.info("publish >> {}", publishDto);
    }
}
