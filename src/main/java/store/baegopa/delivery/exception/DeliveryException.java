package store.baegopa.delivery.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 배송 시스템 오류
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
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DeliveryException extends RuntimeException {
    public DeliveryException(String message) {
        super(message);
    }
}
