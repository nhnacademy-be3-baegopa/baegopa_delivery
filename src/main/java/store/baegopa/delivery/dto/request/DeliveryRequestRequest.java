package store.baegopa.delivery.dto.request;

import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

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
@ToString
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


    private Integer acceptMinSeconds;
    private Integer acceptMaxSeconds;

    private Integer deliveryMinSeconds;
    private Integer deliveryMaxSeconds;

    private Integer finishMinSeconds;
    private Integer finishMaxSeconds;

    private Integer acceptPercent;


    /**
     * 시간, 확률을 설정 했는지 ?
     *
     * @return boolean true 설정함 false 한개라도 빈 값이 있음
     * @author 김현준
     */
    public boolean isSetTime() {
        return acceptMinSeconds != null
                && acceptMaxSeconds != null
                && deliveryMinSeconds != null
                && deliveryMaxSeconds != null
                && finishMinSeconds != null
                && finishMaxSeconds != null
                && acceptPercent != null;
    }
}
