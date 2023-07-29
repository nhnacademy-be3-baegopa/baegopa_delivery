package store.baegopa.delivery.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * 모든 API 응답에서 사용될 클래스.
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
@Builder
public class ResultResponse<T> {

    private Header header;
    private List<T> result;

    /**
     * <p>리스트 결과 생성하는 메소드.</p>
     *
     * @param <T>        result List에 담을 타입
     * @param resultList 결과 리스트
     * @param httpStatus http 상태 타입
     * @return ResponseEntity로 감싼 ResultResponse
     * @author 김현준
     */
    public static <T> ResponseEntity<ResultResponse<T>> responseEntity(HttpStatus httpStatus, List<T> resultList) {
        return ResponseEntity.status(httpStatus)
                .body(newResult(httpStatus, resultList));
    }

    /**
     * <p>단건 결과 생성하는 메소드.</p>
     *
     * @param <T>        result List에 담을 타입
     * @param result     결과 단건
     * @param httpStatus http 상태 타입
     * @return ResponseEntity로 감싼 ResultResponse
     * @author 김현준
     */
    public static <T> ResponseEntity<ResultResponse<T>> responseEntity(HttpStatus httpStatus, T result) {
        return ResponseEntity.status(httpStatus)
                .body(newResult(httpStatus, result));
    }

    /**
     * <p>void 결과 생성하는 메소드.</p>
     *
     * @param httpStatus http 상태 타입
     * @return ResponseEntity로 감싼 ResultResponse
     * @author 김현준
     */
    public static ResponseEntity<ResultResponse<Void>> responseEntity(HttpStatus httpStatus) {
        return ResponseEntity.status(httpStatus)
                .body(newResult(httpStatus));
    }

    /**
     * <p>리스트 결과 생성하는 메소드.</p>
     *
     * @param <T>        result List에 담을 타입
     * @param resultList 결과 리스트
     * @param httpStatus http 상태 타입
     * @param message    결과 메시지
     * @return ResponseEntity로 감싼 ResultResponse
     * @author 김현준
     */
    public static <T> ResponseEntity<ResultResponse<T>> responseEntity(HttpStatus httpStatus, List<T> resultList, String message) {
        return ResponseEntity.status(httpStatus)
                .body(newResult(httpStatus, resultList, message));
    }

    /**
     * <p>단건 결과 생성하는 메소드.</p>
     *
     * @param <T>        result List에 담을 타입
     * @param result     결과 단건
     * @param httpStatus http 상태 타입
     * @param message    결과 메시지
     * @return ResponseEntity로 감싼 ResultResponse
     * @author 김현준
     */
    public static <T> ResponseEntity<ResultResponse<T>> responseEntity(HttpStatus httpStatus, T result, String message) {
        return ResponseEntity.status(httpStatus)
                .body(newResult(httpStatus, result, message));
    }

    /**
     * <p>void 결과 생성하는 메소드.</p>
     *
     * @param httpStatus http 상태 타입
     * @param message    결과 메시지
     * @return ResponseEntity로 감싼 ResultResponse
     * @author 김현준
     */
    public static ResponseEntity<ResultResponse<Void>> responseEntity(HttpStatus httpStatus, String message) {
        return ResponseEntity.status(httpStatus)
                .body(newResult(httpStatus, message));
    }

    /**
     * <p>리스트 결과 생성하는 메소드.</p>
     *
     * @param <T>        result List에 담을 타입
     * @param resultList 결과 리스트
     * @param httpStatus http 상태 타입
     * @return header와 결과가 담긴 ResultResponse
     * @author 김현준
     */
    public static <T> ResultResponse<T> newResult(HttpStatus httpStatus, List<T> resultList) {
        return ResultResponse.<T>builder()
                .header(Header.builder()
                        .successful(!httpStatus.isError())
                        .resultCode(httpStatus.value())
                        .build())
                .result(resultList)
                .build();
    }

    /**
     * <p>단건 결과 생성하는 메소드.</p>
     *
     * @param <T>        result List에 담을 타입
     * @param result     결과 단건
     * @param httpStatus http 상태 타입
     * @return header 와 결과가 담긴 ResultResponse
     * @author 김현준
     */
    public static <T> ResultResponse<T> newResult(HttpStatus httpStatus, T result) {
        return ResultResponse.<T>builder()
                .header(Header.builder()
                        .successful(!httpStatus.isError())
                        .resultCode(httpStatus.value())
                        .build())
                .result(List.of(result))
                .build();
    }

    /**
     * <p>void 결과 생성하는 메소드.</p>
     *
     * @param httpStatus http 상태 타입
     * @return header와 결과가 담긴 ResultResponse
     * @author 김현준
     */
    public static ResultResponse<Void> newResult(HttpStatus httpStatus) {
        return ResultResponse.<Void>builder()
                .header(Header.builder()
                        .successful(!httpStatus.isError())
                        .resultCode(httpStatus.value())
                        .build())
                .build();
    }

    /**
     * <p>리스트 결과 생성하는 메소드.</p>
     *
     * @param <T>        result List에 담을 타입
     * @param resultList 결과 리스트
     * @param httpStatus http 상태 타입
     * @param message    결과 메시지
     * @return header와 결과가 담긴 ResultResponse
     * @author 김현준
     */
    public static <T> ResultResponse<T> newResult(HttpStatus httpStatus, List<T> resultList, String message) {
        return ResultResponse.<T>builder()
                .header(Header.builder()
                        .successful(!httpStatus.isError())
                        .resultCode(httpStatus.value())
                        .resultMessage(message)
                        .build())
                .result(resultList)
                .build();
    }

    /**
     * <p>단건 결과 생성하는 메소드.</p>
     *
     * @param <T>        result List에 담을 타입
     * @param result     결과 단건
     * @param httpStatus http 상태 타입
     * @param message    결과 메시지
     * @return header와 결과가 담긴 ResultResponse
     * @author 김현준
     */
    public static <T> ResultResponse<T> newResult(HttpStatus httpStatus, T result, String message) {
        return ResultResponse.<T>builder()
                .header(Header.builder()
                        .successful(!httpStatus.isError())
                        .resultCode(httpStatus.value())
                        .resultMessage(message)
                        .build())
                .result(List.of(result))
                .build();
    }

    /**
     * <p>void 결과 생성하는 메소드.</p>
     *
     * @param httpStatus http 상태 타입
     * @param message    결과 메시지
     * @return header와 결과가 담긴 ResultResponse
     * @author 김현준
     */
    public static ResultResponse<Void> newResult(HttpStatus httpStatus, String message) {
        return ResultResponse.<Void>builder()
                .header(Header.builder()
                        .successful(!httpStatus.isError())
                        .resultCode(httpStatus.value())
                        .resultMessage(message)
                        .build())
                .build();
    }

    public long getTotalCount() {
        return result == null ? 0 : result.size();
    }

    /**
     * 공통 응답형식에서 헤더를 표시하기위한 클래스.
     *
     * @author 김현준
     */
    @Getter
    @Builder
    public static class Header {

        private boolean successful;
        private int resultCode;
        private String resultMessage;
    }

}
