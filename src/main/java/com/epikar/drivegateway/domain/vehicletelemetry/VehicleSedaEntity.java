package com.epikar.drivegateway.domain.vehicletelemetry;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@Getter
//@Entity
//@Table(name = "vehicleSeda")
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VehicleSedaEntity {

//    @Id
//    private String carId; // 차량 PK (seda)
//
//    @Column(unique = true, nullable = false)
//    private String carNo; // 차량 번호
//    private String carNm; // 차량 명 (데이터 안넘어옴)
//
//    @JoinColumn(name = "vehicleId")
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Vehicle vehicle;
//
//    private String deviceNo; // 단말 번호
//
//    private String messageId; // message 고유 ID
//    private String messagedAt; // message 생성 시간
//
//    private String reportType; // 리포트 유형
//    private String eventType; // 차량의 이벤트가 발생한 경우
//
//    private String engine; // 시동 상태
//    private String door; // 도어 열림 상태
//    private String doorLock; // 도어 잠금 상태
//
//    private String firstDt; // 최초 보고 시간 (장착시작 대응)
//    private String lastKeyOnTime; // 마지막 Key On 시간
//    private String lastKeyOffTime; // 마지막 Key Off 시간
//
//    private String drivingStatus; // 주행 상태
//    private String odometer; // 차량 주행거리
//    private String tripDistance; // 트립 단위 주행거리
//    private String dayDistance; // 일일주행거리
//    private String remainingDrivingDistance; // 주행 가능 거리
//
//    private String chargeState; // 충전 상태
//    private String fuelLevel; // 연료잔량(L)
//    private String chargePercent; // 전기차 충전 잔량
//    private String batteryVoltage; // 차량 베터리
//
//    private String address; // 주소
//    private String longitude; // 위도
//    private String latitude; // 경도
//
//    private String vspeed; // 주행속도
//    private String maxVspeed; // 최고 속도 (트립 최고)
//    private String avrVspeed; // 평균 속도 (트립 평균)
//
//    private String qstartCnt; // 급출발 횟수
//    private String raccelCnt; // 급가속 횟수
//    private String qstopCnt; // 급정지 횟수
//    private String rdecelCnt; // 급감속 횟수
//
//    private String drivingId;
//    private String obdOutYn; // obdOutYn : OBD 탈거(상태) 여부  (탈거:“Y”, 정상:"N")
//
//    @Enumerated(EnumType.STRING)
//    private StatusYN abnormalDrivingStatus;
}
