package store.baegopa.delivery.exception;

/**
 * 키 가져오는 api 호출 에러
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
public class KeyMangerException extends RuntimeException {
    public KeyMangerException(String message) {
        super(message);
    }
}
