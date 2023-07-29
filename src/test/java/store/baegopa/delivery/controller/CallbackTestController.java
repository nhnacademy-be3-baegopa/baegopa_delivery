package store.baegopa.delivery.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import store.baegopa.delivery.dto.response.CallbackResponse;

/**
 * 콜백이 잘 되는지 보기위한 테스트용 컨트롤러
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
@RestController
@RequestMapping("/test/callback")
@Slf4j
public class CallbackTestController {
    @PostMapping
    public CallbackResponse returnCallbackResponse(@RequestBody CallbackResponse callbackResponse) {
        log.info("callbackResponse : {}", callbackResponse.toString());
        return callbackResponse;
    }

}
