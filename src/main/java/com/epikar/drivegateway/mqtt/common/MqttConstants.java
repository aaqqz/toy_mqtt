package com.epikar.drivegateway.mqtt.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MqttConstants {

    public static final String DEVICE_NO = "device_no";
    public static final String MESSAGE_ID = "message_id";
    public static final String MESSAGE_TYPE = "message_type";
    public static final String TOPIC_PREFIX = "seda/server/";
    public static final String MESSAGE_TYPE_RESPONSE = "response";
}
