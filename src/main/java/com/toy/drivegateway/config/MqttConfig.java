package com.toy.drivegateway.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

@Configuration
public class MqttConfig {

    @Value("${mqtt.broker-url}")
    private String brokerUrl;

    @Value("${mqtt.username}")
    private String username;

    @Value("${mqtt.password}")
    private String password;

    @Value("${mqtt.ca-path}")
    private String caPath;

    @Value("${mqtt.client-id}")
    private String clientId;

    @Value("${mqtt.topic}")
    private String topic;

    /**
     * MQTT Client Factory (TLS 설정 포함)
     */
    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();

        try {
            // MQTT 연결 옵션
            MqttConnectOptions options = new MqttConnectOptions();
            options.setServerURIs(new String[]{brokerUrl});
            options.setUserName(username);
            options.setPassword(password.toCharArray());
            options.setCleanSession(true);
            options.setAutomaticReconnect(true);

            // TLS/SSL 설정
            // SSL 환경일 때만 TLS 설정 적용
            if (brokerUrl.startsWith("ssl://")) {
                options.setSocketFactory(getSocketFactory(caPath));
            }

            factory.setConnectionOptions(options);
        } catch (Exception e) {
            throw new RuntimeException("TLS 설정 실패", e);
        }

        return factory;
    }

    /**
     * 루트 CA 인증서 기반의 TLS Socket Factory 생성
     */
    private SSLSocketFactory getSocketFactory(String caFilePath) throws Exception {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        try (FileInputStream fis = new FileInputStream(caFilePath)) {
            X509Certificate caCert = (X509Certificate) cf.generateCertificate(fis);

            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(null, null);
            ks.setCertificateEntry("ca", caCert);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ks);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), new SecureRandom());

            return sslContext.getSocketFactory();
        }
    }

    /**
     * Subscriber 설정
     * 구독용 채널 (Inbound (subscribe))
     * 브로커 → mqttInbound() → mqttInboundChannel() → 애플리케이션(비즈니스 로직으로 전달)
     * 수신한 메시지를 mqttInboundChannel()로 보냄 → 이후 다른 @ServiceActivator에서 처리 가능
     */
    @Bean
    public MessageChannel mqttInboundChannel() {
        return new DirectChannel();
    }

    /**
     * 구독 어댑터 (Subscriber)
     */
    @Bean
    public MessageProducer inbound(MqttPahoClientFactory factory) {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter(clientId + "-sub", factory, topic);
        adapter.setCompletionTimeout(5000);
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInboundChannel());

        return adapter;
    }

    /**
     * Publisher 설정
     * 발행용 채널 (Outbound (publish))
     * 애플리케이션 → mqttOutboundChannel() → mqttOutbound() → 브로커
     * mqttOutboundChannel()에 메시지를 넣으면 자동으로 MQTT 브로커로 전송
     */
    @Bean
    public MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    /**
     * 발행 어댑터 (Publisher)
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    public MessageHandler outbound(MqttPahoClientFactory factory) {
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(clientId + "-pub", factory);
        messageHandler.setAsync(true); // 비동기 발행 (성능 향상)

        return messageHandler;
    }
}
