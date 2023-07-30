package store.baegopa.delivery.entity.code;

import lombok.Getter;

/**
 * 배송 상태 코드
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
public enum DeliveryStateCode {
    A1("요청"),
    A2("수락"),
    A3("배송중"),
    A4("배송완료"),
    A5("취소"),
    A6("거절"),
    ;

    private final String name;

    DeliveryStateCode(String name) {
        this.name = name;
    }
}