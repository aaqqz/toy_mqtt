package com.toy.drivegateway.mqtt.handler;

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

        try {
            // TODO: 데이터 파싱 → DB 저장 → 서비스 호출 등 실제 처리
            Thread.sleep(2000); // 테스트용
        } catch (Exception e) {
            log.error("process 중 오류 발생", e);
        }

        log.info("process 처리 완료");
    }
}
