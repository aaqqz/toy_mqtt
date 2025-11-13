package com.epikar.drivegateway.mqtt.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.epikar.drivegateway.mqtt.common.MqttConstants.*;

@Getter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PublishDto {

    private final String device_no;
    private final String message_type;
    private final Integer message_id;
    private final String messaged_at;
    private final Body body;

    @Builder
    public PublishDto(String device_no, String message_id, boolean isSuccess) {
        this.device_no = device_no;
        this.message_type = MESSAGE_TYPE_RESPONSE;
        this.message_id = Integer.parseInt(message_id);
        this.messaged_at = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.body = new Body(isSuccess);
    }

    @Getter
    @ToString
    private static class Body {
        private final String result;

        public Body(boolean isSuccess) {
            this.result = isSuccess ? "ok" : "fail";
        }
    }
}
