package store.baegopa.delivery.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void getDeliveryRequest(@RequestBody DeliveryRequestRequest deliveryRequestRequest) {
        deliveryService.deliveryRequest(deliveryRequestRequest);
    }
}
