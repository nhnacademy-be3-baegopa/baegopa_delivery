package store.baegopa.delivery.dto.request;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Getter;

/**
 * 배달 요청
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
@Getter
public class DeliveryRequestRequest {
    @NotNull
    private String deliveryAddress;

    @NotNull
    private int price;

    @NotNull
    private String reqStore;

    @NotNull
    private String reqStoreAddress;

    @NotNull
    private LocalDateTime prepDatetime;

    private String reqMemo;

    private String callbackId;

    private String callbackUrl;
}
