package store.baegopa.delivery.dto.response;

import lombok.Getter;
import lombok.ToString;

/**
 * 키 응답 DTO
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
public class KeyResponse {
    private Header header;
    private Body body;

    public String getSecret() {
        return body.secret;
    }

    /**
     * 헤더 클래스.
     * <br>
     * 성공여부, 결과코드, 결과 메세지를 필드로 갖는다.
     *
     * @author 김현준
     */
    @Getter
    @ToString
    public static class Header {
        private boolean isSuccessful;
        private int resultCode;
        private String resultMessage;
    }

    /**
     * 시크릿 결과 값
     *
     * @author 김현준
     */
    @ToString
    @Getter
    public static class Body {
        private String secret;
    }
}
