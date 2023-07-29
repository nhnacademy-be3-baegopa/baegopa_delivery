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
    private final DeliveryCallbackService deliveryCallbackService;
    private final DeliveryDriverRepository deliveryDriverRepository;

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
                    Duration.ofSeconds(30).toMillis(),
                    Duration.ofSeconds(60).toMillis()
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
                deliveryCallbackService.changeStateAndCallback(deliveryInfoId, callbackUrl, callbackId, deliveryStateCode);
                return;
            }

            // 수락이면 임의의 드라이버를 찾는다.
            long count = deliveryDriverRepository.count();
            int index = current.nextInt((int) count);

            Page<DeliveryDriverEntity> driverPage = deliveryDriverRepository.findAll(PageRequest.of(index, 1));
            List<DeliveryDriverEntity> content = driverPage.getContent();

            if (driverPage.isEmpty()) {
                log.error("기사가 없습니다.");
                deliveryCallbackService.changeStateAndCallback(deliveryInfoId, callbackUrl, callbackId, DeliveryStateCode.A6);
                return;
            }

            DeliveryDriverEntity deliveryDriverEntity = content.get(0);
            long driverId = deliveryDriverEntity.getDeliveryDriverId();

            deliveryCallbackService.matchDriver(deliveryInfoId, callbackUrl, callbackId, driverId);

            // 기사가 음식을 집고 배송
            // 만약 조리 예정 시간이 현재 시간보다 전일때 (이미 요리가 완료 됨)
            if (prepDatetime.isBefore(LocalDateTime.now())) {
                sleep = current.nextLong(
                        Duration.ofSeconds(30).toMillis(),
                        Duration.ofSeconds(60).toMillis()
                );
            } else {
                Duration between = Duration.between(LocalDateTime.now(), prepDatetime);
                sleep = current.nextLong(
                        between.plusSeconds(30).toMillis(),
                        between.plusSeconds(60).toMillis()
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

            deliveryCallbackService.changeStateAndCallback(deliveryInfoId, callbackUrl, callbackId, deliveryStateCode);

            // 배송 완료
            sleep = current.nextLong(
                    Duration.ofMinutes(3).toMillis(),
                    Duration.ofMinutes(15).toMillis()
            );

            deliveryStateCode = DeliveryStateCode.A4;
            log.info(LOG_FORMAT, Duration.ofMillis(sleep).toSeconds(), deliveryStateCode.getName());

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.info(THREAD_INTERRUPTED, e.getMessage(), e);
            }

            deliveryCallbackService.changeStateAndCallback(deliveryInfoId, callbackUrl, callbackId, deliveryStateCode);

        }).start();
    }
}
