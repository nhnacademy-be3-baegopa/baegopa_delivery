package store.baegopa.delivery.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import store.baegopa.delivery.dto.request.DeliveryRequestRequest;
import store.baegopa.delivery.entity.DeliveryInfoEntity;
import store.baegopa.delivery.repository.DeliveryInfoRepository;

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
    private final DummyDeliveryService dummyDeliveryService;

    /**
     * 배송 요청을 받아 배송을 진행한다.
     *
     * @param deliveryRequestRequest deliveryRequestRequest
     * @author 김현준
     */
    @Transactional
    public void deliveryRequest(DeliveryRequestRequest deliveryRequestRequest) {
        Long deliveryInfoId = deliveryInfoRepository.save(DeliveryInfoEntity.builder()
                .deliveryAddress(deliveryRequestRequest.getDeliveryAddress())
                .prepDatetime(deliveryRequestRequest.getPrepDatetime())
                .price(deliveryRequestRequest.getPrice())
                .reqStore(deliveryRequestRequest.getReqStore())
                .reqStoreAddress(deliveryRequestRequest.getReqStoreAddress())
                .reqMemo(deliveryRequestRequest.getReqMemo())
                .build()).getDeliveryInfoId();

        // 더미 배송 시스템을 실행시킨다.
        dummyDeliveryService.startDummyDelivery(
                deliveryInfoId,
                deliveryRequestRequest.getCallbackUrl(),
                deliveryRequestRequest.getCallbackId(),
                deliveryRequestRequest.getPrepDatetime());
    }
}
