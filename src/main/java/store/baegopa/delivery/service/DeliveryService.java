package store.baegopa.delivery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.baegopa.delivery.dto.request.DeliveryRequestRequest;
import store.baegopa.delivery.entity.DeliveryInfoEntity;
import store.baegopa.delivery.entity.DeliveryStateHistoryEntity;
import store.baegopa.delivery.entity.code.DeliveryStateCode;
import store.baegopa.delivery.repository.DeliveryInfoRepository;
import store.baegopa.delivery.repository.DeliveryStateHistoryRepository;

/**
 * 배송 서비스
 * <pre>
 * ===========================================================
 * DATE             AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/07/28       김현준                 최초 생성
 * </pre>
 *
 * @author 김현준
 * @since 2023/07/28
 */
@Service
@RequiredArgsConstructor
public class DeliveryService {
    private final DeliveryInfoRepository deliveryInfoRepository;
    private final DeliveryStateHistoryRepository deliveryStateHistoryRepository;
    private final DummyDeliveryService dummyDeliveryService;

    /**
     * 배송 요청을 받아 배송을 진행한다.
     *
     * @param deliveryRequestRequest deliveryRequestRequest
     * @author 김현준
     */
    @Transactional
    public void deliveryRequest(DeliveryRequestRequest deliveryRequestRequest) {
        DeliveryInfoEntity deliveryInfoEntity = deliveryInfoRepository.save(DeliveryInfoEntity.builder()
                .deliveryAddress(deliveryRequestRequest.getDeliveryAddress())
                .prepDatetime(deliveryRequestRequest.getPrepDatetime())
                .price(deliveryRequestRequest.getPrice())
                .reqStore(deliveryRequestRequest.getReqStore())
                .reqStoreAddress(deliveryRequestRequest.getReqStoreAddress())
                .reqMemo(deliveryRequestRequest.getReqMemo())
                .build());

        deliveryStateHistoryRepository.save(DeliveryStateHistoryEntity.builder()
                .deliveryInfoEntity(deliveryInfoEntity)
                .deliveryStateCode(DeliveryStateCode.A1)
                .build());

        // 더미 배송 시스템을 실행시킨다.
        dummyDeliveryService.startDummyDelivery(
                deliveryInfoEntity.getDeliveryInfoId(),
                deliveryRequestRequest.getCallbackUrl(),
                deliveryRequestRequest.getCallbackId(),
                deliveryRequestRequest.getPrepDatetime());
    }
}
