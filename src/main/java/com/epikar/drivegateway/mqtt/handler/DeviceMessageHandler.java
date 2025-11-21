package com.epikar.drivegateway.mqtt.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceMessageHandler {

    @Async
    public void processAsync(String payload, MessageHeaders headers) {
        log.info("process 처리 시작: {}, {}", payload, headers);

        // todo
        // 여기에 실제 처리 로직 구현
        // JSON 파싱, 데이터베이스 저장, 다른 서비스 호출 등

        log.info("process 처리 완료");
    }
}
