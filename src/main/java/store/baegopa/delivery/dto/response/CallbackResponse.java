package store.baegopa.delivery.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import store.baegopa.delivery.entity.code.DeliveryStateCode;

/**
 * 콜백 할 때 담아 보낼 객체
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
@Builder
@Getter
public class CallbackResponse {
    private String callbackId;
    private DeliveryStateCode deliveryStateCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String driverName;
}
