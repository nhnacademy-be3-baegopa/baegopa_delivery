package store.baegopa.delivery.dto.request;

import javax.validation.constraints.NotNull;
import lombok.Getter;

/**
 * 배달 요청 취소
 * <pre>
 * ===========================================================
 * DATE             AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/08/06       김현준                 최초 생성
 * </pre>
 *
 * @author 김현준
 * @since 2023/08/06
 */
@Getter
public class DeliveryCancelRequest {
    @NotNull
    private String callbackId;
}
