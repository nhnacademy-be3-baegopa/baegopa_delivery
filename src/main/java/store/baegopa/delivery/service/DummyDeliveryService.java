package store.baegopa.delivery.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import store.baegopa.delivery.config.DummyDeliveryTimeProperties;
import store.baegopa.delivery.entity.DeliveryDriverEntity;
import store.baegopa.delivery.entity.code.DeliveryStateCode;
import store.baegopa.delivery.repository.DeliveryDriverRepository;

/**
 * 더미 배송 서비스. 더미 데이터들로 배송 시스템을 진행시킨다.
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
@Service
@RequiredArgsConstructor
@Slf4j
public class DummyDeliveryService {
    private static final String LOG_FORMAT = "{}초 뒤에 {}합니다.";
    private static final String THREAD_INTERRUPTED = "thread interrupted ! : {}";
    public static final String CALLBACK_SERVER_ERROR = "콜백서버 에러 : {}";
    private final DeliveryCallbackService deliveryCallbackService;
    private final DeliveryDriverRepository deliveryDriverRepository;
    private final DummyDeliveryTimeProperties dummyDeliveryTimeProperties;

    /**
     * 실제 배송이 아니라 더미 데이터들로 랜덤한 시간을 두고 배송 절차를 밟는다.
     *
     * @param deliveryInfoId deliveryInfoId
     * @param callbackUrl    callbackUrl
     * @param callbackId     callbackId
     * @author 김현준
     */
    public void startDummyDelivery(long deliveryInfoId,
                                   String callbackUrl,
                                   String callbackId,
                                   LocalDateTime prepDatetime) {
        new Thread(() -> {
            // 실제 드라이버가 콜을 받는 것이 아니라 임의로 수락 혹은 거절을 한다.
            ThreadLocalRandom current = ThreadLocalRandom.current();

            long sleep = current.nextLong(
                    Duration.ofSeconds(dummyDeliveryTimeProperties.getAcceptMinSeconds()).toMillis(),
                    Duration.ofSeconds(dummyDeliveryTimeProperties.getAcceptMaxSeconds()).toMillis()
            );

            // 10% 확률로 거절함.
            DeliveryStateCode deliveryStateCode = current.nextInt(100) < 90 ? DeliveryStateCode.A2 : DeliveryStateCode.A6;
            log.info(LOG_FORMAT, Duration.ofMillis(sleep).toSeconds(), deliveryStateCode.getName());

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.info(THREAD_INTERRUPTED, e.getMessage(), e);
            }

            // 거절이면 끝
            if (DeliveryStateCode.A6.equals(deliveryStateCode)) {
                try {
                    deliveryCallbackService.changeStateAndCallback(deliveryInfoId, callbackUrl, callbackId, deliveryStateCode);
                } catch (RestClientException e) { // 콜백에서 에러가 나도 배송은 계속 진행한다.
                    log.error(CALLBACK_SERVER_ERROR, e.getMessage(), e);
                }
                return;
            }

            // 수락이면 임의의 드라이버를 찾는다.
            long count = deliveryDriverRepository.count();
            int index = current.nextInt((int) count);

            Page<DeliveryDriverEntity> driverPage = deliveryDriverRepository.findAll(PageRequest.of(index, 1));
            List<DeliveryDriverEntity> content = driverPage.getContent();

            if (driverPage.isEmpty()) {
                log.error("기사가 없습니다.");
                try {
                    deliveryCallbackService.changeStateAndCallback(deliveryInfoId, callbackUrl, callbackId, DeliveryStateCode.A6);
                } catch (RestClientException e) { // 콜백에서 에러가 나도 배송은 계속 진행한다.
                    log.error(CALLBACK_SERVER_ERROR, e.getMessage(), e);
                }
                return;
            }

            DeliveryDriverEntity deliveryDriverEntity = content.get(0);
            long driverId = deliveryDriverEntity.getDeliveryDriverId();

            try {
                deliveryCallbackService.matchDriver(deliveryInfoId, callbackUrl, callbackId, driverId);
            } catch (RestClientException e) { // 콜백에서 에러가 나도 배송은 계속 진행한다.
                log.error(CALLBACK_SERVER_ERROR, e.getMessage(), e);
            }

            // 기사가 음식을 집고 배송
            // 만약 조리 예정 시간이 현재 시간보다 전일때 (이미 요리가 완료 됨)
            if (prepDatetime.isBefore(LocalDateTime.now())) {
                sleep = current.nextLong(
                        Duration.ofSeconds(dummyDeliveryTimeProperties.getDeliveryMinSeconds()).toMillis(),
                        Duration.ofSeconds(dummyDeliveryTimeProperties.getDeliveryMaxSeconds()).toMillis()
                );
            } else {
                Duration between = Duration.between(LocalDateTime.now(), prepDatetime);
                sleep = current.nextLong(
                        between.plusSeconds(dummyDeliveryTimeProperties.getDeliveryMinSeconds()).toMillis(),
                        between.plusSeconds(dummyDeliveryTimeProperties.getDeliveryMaxSeconds()).toMillis()
                );
            }


            deliveryStateCode = DeliveryStateCode.A3;
            log.info(LOG_FORMAT, Duration.ofMillis(sleep).toSeconds(), deliveryStateCode.getName());
            log.info("{}", Duration.ofMillis(sleep).toString());

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.info(THREAD_INTERRUPTED, e.getMessage(), e);
            }

            try {
                deliveryCallbackService.changeStateAndCallback(deliveryInfoId, callbackUrl, callbackId, deliveryStateCode);
            } catch (RestClientException e) { // 콜백에서 에러가 나도 배송은 계속 진행한다.
                log.error(CALLBACK_SERVER_ERROR, e.getMessage(), e);
            }

            // 배송 완료
            sleep = current.nextLong(
                    Duration.ofSeconds(dummyDeliveryTimeProperties.getFinishMinSeconds()).toMillis(),
                    Duration.ofSeconds(dummyDeliveryTimeProperties.getFinishMaxSeconds()).toMillis()
            );

            deliveryStateCode = DeliveryStateCode.A4;
            log.info(LOG_FORMAT, Duration.ofMillis(sleep).toSeconds(), deliveryStateCode.getName());

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.info(THREAD_INTERRUPTED, e.getMessage(), e);
            }

            try {
                deliveryCallbackService.changeStateAndCallback(deliveryInfoId, callbackUrl, callbackId, deliveryStateCode);
            } catch (RestClientException e) { // 콜백에서 에러가 나도 배송은 계속 진행한다.
                log.error(CALLBACK_SERVER_ERROR, e.getMessage(), e);
            }

        }).start();
    }
}
