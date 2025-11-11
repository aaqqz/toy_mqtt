package com.epikar.drivegateway.mqtt.controller;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PublishRequest {
    private String deviceNo;
    private String message;

    public PublishRequest(String deviceNo, String message) {
        this.deviceNo = deviceNo;
        this.message = message;
    }
}
