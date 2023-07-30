package store.baegopa.delivery.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 더미 배송 시스템 시간 설정
 * <pre>
 * ===========================================================
 * DATE             AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/07/29       김현준                 최초 생성
 * </pre>
 *
 * @author 김현준
 * @since 2023/07/29
 */
@Setter
@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "store.baegopa.delivery.dummy-delivery-time")
public class DummyDeliveryTimeProperties {
    private int acceptMinSeconds;
    private int acceptMaxSeconds;

    private int deliveryMinSeconds;
    private int deliveryMaxSeconds;

    private int finishMinSeconds;
    private int finishMaxSeconds;
}
