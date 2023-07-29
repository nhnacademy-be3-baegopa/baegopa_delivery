package store.baegopa.delivery.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import store.baegopa.delivery.dto.response.CallbackResponse;
import store.baegopa.delivery.entity.DeliveryDriverEntity;
import store.baegopa.delivery.entity.DeliveryInfoEntity;
import store.baegopa.delivery.entity.DeliveryStateHistoryEntity;
import store.baegopa.delivery.entity.code.DeliveryStateCode;
import store.baegopa.delivery.repository.DeliveryDriverRepository;
import store.baegopa.delivery.repository.DeliveryInfoRepository;
import store.baegopa.delivery.repository.DeliveryStatusHistoryRepository;

/**
 * 배송 상태가 변경 될 때 응답 콜백 서비스
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
@Slf4j
public class DeliveryCallbackService {
    private final RestTemplate restTemplate;
    private final DeliveryDriverRepository deliveryDriverRepository;
    private final DeliveryInfoRepository deliveryInfoRepository;
    private final DeliveryStatusHistoryRepository deliveryStatusHistoryRepository;

    /**
     * 기사를 매칭한다.
     *
     * @author 김현준
     */
    // 콜백 받을 서버에 문제가 있어도 롤백을 하지 않는다.
    @Transactional(noRollbackFor = RestClientException.class)
    public void matchDriver(long deliveryInfoId,
                            String callbackUrl,
                            String callbackId,
                            long driverId) {
        DeliveryInfoEntity deliveryInfoEntity = deliveryInfoRepository.findById(deliveryInfoId)
                .orElseThrow();

        DeliveryDriverEntity deliveryDriverEntity = deliveryDriverRepository.findById(driverId)
                .orElseThrow();

        deliveryInfoEntity.setDeliveryDriverEntity(deliveryDriverEntity);

        deliveryStatusHistoryRepository.save(DeliveryStateHistoryEntity.builder()
                .deliveryInfoEntity(deliveryInfoEntity)
                .deliveryStateCode(DeliveryStateCode.A2)
                .build());

        RequestEntity<CallbackResponse> requestEntity = RequestEntity
                .post(callbackUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(CallbackResponse.builder()
                        .callbackId(callbackId)
                        .deliveryStateCode(DeliveryStateCode.A2)
                        .driverName(deliveryDriverEntity.getName())
                        .build());

        restTemplate.exchange(requestEntity, Void.class);
    }

    /**
     * 배송 상태를 바꾸고 콜백해준다.
     *
     * @param deliveryInfoId    deliveryInfoId
     * @param callbackUrl       callbackUrl
     * @param callbackId        callbackId
     * @param deliveryStateCode deliveryStateCode
     * @author 김현준
     */
    @Transactional(noRollbackFor = RestClientException.class)
    public void changeStateAndCallback(long deliveryInfoId,
                                       String callbackUrl,
                                       String callbackId,
                                       DeliveryStateCode deliveryStateCode) {
        deliveryStatusHistoryRepository.save(DeliveryStateHistoryEntity.builder()
                .deliveryInfoEntity(deliveryInfoRepository.getReferenceById(deliveryInfoId))
                .deliveryStateCode(deliveryStateCode)
                .build());

        RequestEntity<CallbackResponse> requestEntity = RequestEntity
                .post(callbackUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(CallbackResponse.builder()
                        .callbackId(callbackId)
                        .deliveryStateCode(deliveryStateCode)
                        .build());

        restTemplate.exchange(requestEntity, Void.class);
    }
}