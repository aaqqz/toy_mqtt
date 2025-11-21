package com.toy.drivegateway.mqtt.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MessageType {
    COMMAND("command"),
    REPORT("report"),
    RESPONSE("response")
    ;

    private final String description;

    public boolean isReport() {
        return this == REPORT;
    }
}


