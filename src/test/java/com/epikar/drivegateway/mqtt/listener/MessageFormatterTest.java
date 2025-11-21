package com.epikar.drivegateway.mqtt.listener;

import com.epikar.drivegateway.mqtt.common.MessageFormatter;
import com.epikar.drivegateway.mqtt.common.MessageType;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class MessageFormatterTest {

    private final MessageFormatter messageFormatter;

    @Test
    void extractJsonFieldToMessageType() throws JsonProcessingException {
        // todo object fixture
        // given
        String payload = "{\n" +
                "  \"device_no\": \"01241032794\",\n" +
                "  \"message_type\": \"report\",\n" +
                "  \"message_id\": 595177994,\n" +
                "  \"body\": {\n" +
                "    \"report_type\": \"state\"\n" +
                "  },\n" +
                "  \"messaged_at\": \"2025-11-20 05:43:19\"\n" +
                "}";

        // when
        MessageType messageType = messageFormatter.extractMessageType(payload);

        // then
        assertThat(messageType).isEqualTo(MessageType.REPORT);
    }

}