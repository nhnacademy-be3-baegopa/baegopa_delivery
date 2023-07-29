package store.baegopa.delivery.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import store.baegopa.delivery.dto.request.DeliveryRequestRequest;
import store.baegopa.delivery.service.DeliveryService;

/**
 * 배송 컨트롤러
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
@RequiredArgsConstructor
@RestController
@RequestMapping("/delivery")
public class DeliveryController {
    private final DeliveryService deliveryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public void getDeliveryRequest(DeliveryRequestRequest deliveryRequestRequest) {
        deliveryService.deliveryRequest(deliveryRequestRequest);
    }
}
