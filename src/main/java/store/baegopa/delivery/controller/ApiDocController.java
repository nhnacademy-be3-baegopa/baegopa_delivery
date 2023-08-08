package store.baegopa.delivery.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * API 문서를 반환한다.
 * <pre>
 * ===========================================================
 * DATE             AUTHOR               NOTE
 * -----------------------------------------------------------
 * 2023/08/08       김현준                 최초 생성
 * </pre>
 *
 * @author 김현준
 * @since 2023/08/08
 */
@Controller
@RequestMapping("/docs")
public class ApiDocController {

    /**
     * 요청이 들어온 docs 문서를 내어준다.
     *
     * @return ModelAndView
     * @author 김현준
     */
    @GetMapping()
    public String getView() {
        return "index";
    }
}
