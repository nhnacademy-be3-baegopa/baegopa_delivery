package store.baegopa.delivery.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import store.baegopa.delivery.config.RestDocsUtil;
import store.baegopa.delivery.dto.request.DeliveryRequestRequest;
import store.baegopa.delivery.dto.response.CallbackResponse;
import store.baegopa.delivery.entity.DeliveryInfoEntity;
import store.baegopa.delivery.entity.DeliveryStateHistoryEntity;
import store.baegopa.delivery.entity.code.DeliveryStateCode;
import store.baegopa.delivery.repository.DeliveryDriverRepository;
import store.baegopa.delivery.repository.DeliveryInfoRepository;
import store.baegopa.delivery.repository.DeliveryStateHistoryRepository;

import static org.awaitility.Awaitility.await;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 통합 테스트
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = "classpath:driver_insert.sql")
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DeliveryControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    DeliveryDriverRepository deliveryDriverRepository;
    @Autowired
    DeliveryStateHistoryRepository deliveryStateHistoryRepository;
    @Autowired
    DeliveryInfoRepository deliveryInfoRepository;
    @Autowired
    WebApplicationContext webApplicationContext;

    RestDocumentationResultHandler handler = RestDocsUtil.handler();

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup() {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Test
    @Order(1)
    void getDeliveryRequest() throws Exception {
        log.info("port : {}", port);

        DeliveryRequestRequest deliveryRequestRequest = new DeliveryRequestRequest();
        ReflectionTestUtils.setField(deliveryRequestRequest, "deliveryAddress", "배달 주소");
        ReflectionTestUtils.setField(deliveryRequestRequest, "price", 5000);
        ReflectionTestUtils.setField(deliveryRequestRequest, "reqStore", "가게1");
        ReflectionTestUtils.setField(deliveryRequestRequest, "reqStoreAddress", "가게 주소");
        ReflectionTestUtils.setField(deliveryRequestRequest, "prepDatetime", LocalDateTime.now());
        ReflectionTestUtils.setField(deliveryRequestRequest, "reqMemo", "메모");
        ReflectionTestUtils.setField(deliveryRequestRequest, "callbackId", "baegopa-123-456");
        ReflectionTestUtils.setField(deliveryRequestRequest, "callbackUrl", "http://local.baegopa.store:" + port + "/test/callback");

        mockMvc.perform(post("/delivery")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deliveryRequestRequest)))
                .andDo(print())
                .andDo(handler)
                .andExpect(status().isOk());

        await().until(() -> deliveryInfoRepository.count() == 1L);

        await()
                .atMost(Duration.ofMinutes(1)) // 최대 1분까지
                .pollInterval(Duration.ofSeconds(5)) // 5초 간격으로 검사
                .until(() -> { // true 가 뜰 때까지 검사
                    DeliveryInfoEntity deliveryInfoEntity = deliveryInfoRepository.findAll().get(0);

                    DeliveryStateCode stateCode = deliveryStateHistoryRepository.findAll(Example.of(DeliveryStateHistoryEntity.builder()
                                    .deliveryInfoEntity(deliveryInfoEntity)
                                    .build()), Sort.by(Sort.Direction.DESC, "createDatetime"))
                            .get(0)
                            .getDeliveryStateCode();

                    log.info("stateCode : {}, {}", stateCode, stateCode.getName());

                    return DeliveryStateCode.A6.equals(stateCode) // 거절이거나
                            || DeliveryStateCode.A5.equals(stateCode) // 취소이거나
                            || DeliveryStateCode.A4.equals(stateCode); // 배송완료이면
                });
    }

    @Test
    @Order(2)
    void getDeliveryRequestForTest() throws Exception {
        log.info("port : {}", port);

        DeliveryRequestRequest deliveryRequestRequest = new DeliveryRequestRequest();
        ReflectionTestUtils.setField(deliveryRequestRequest, "deliveryAddress", "배달 주소");
        ReflectionTestUtils.setField(deliveryRequestRequest, "price", 5000);
        ReflectionTestUtils.setField(deliveryRequestRequest, "reqStore", "가게1");
        ReflectionTestUtils.setField(deliveryRequestRequest, "reqStoreAddress", "가게 주소");
        ReflectionTestUtils.setField(deliveryRequestRequest, "prepDatetime", LocalDateTime.now());
        ReflectionTestUtils.setField(deliveryRequestRequest, "reqMemo", "메모");
        ReflectionTestUtils.setField(deliveryRequestRequest, "callbackId", "baegopa-123-456");
        ReflectionTestUtils.setField(deliveryRequestRequest, "callbackUrl", "http://local.baegopa.store:" + port + "/test/callback");

        ReflectionTestUtils.setField(deliveryRequestRequest, "acceptMinSeconds", 0);
        ReflectionTestUtils.setField(deliveryRequestRequest, "acceptMaxSeconds", 10);
        ReflectionTestUtils.setField(deliveryRequestRequest, "deliveryMinSeconds", 0);
        ReflectionTestUtils.setField(deliveryRequestRequest, "deliveryMaxSeconds", 10);
        ReflectionTestUtils.setField(deliveryRequestRequest, "finishMinSeconds", 0);
        ReflectionTestUtils.setField(deliveryRequestRequest, "finishMaxSeconds", 10);
        ReflectionTestUtils.setField(deliveryRequestRequest, "acceptPercent", 100);

        mockMvc.perform(post("/delivery")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deliveryRequestRequest)))
                .andDo(print())
                .andDo(handler)
                .andExpect(status().isOk());

        await().until(() -> deliveryInfoRepository.count() == 2L);

        await()
                .atMost(Duration.ofMinutes(1)) // 최대 1분까지
                .pollInterval(Duration.ofSeconds(5)) // 5초 간격으로 검사
                .until(() -> { // true 가 뜰 때까지 검사
                    DeliveryInfoEntity deliveryInfoEntity = deliveryInfoRepository.findAll().get(1);

                    DeliveryStateCode stateCode = deliveryStateHistoryRepository.findAll(Example.of(DeliveryStateHistoryEntity.builder()
                                    .deliveryInfoEntity(deliveryInfoEntity)
                                    .build()), Sort.by(Sort.Direction.DESC, "createDatetime"))
                            .get(0)
                            .getDeliveryStateCode();

                    log.info("stateCode : {}, {}", stateCode, stateCode.getName());

                    return DeliveryStateCode.A6.equals(stateCode) // 거절이거나
                            || DeliveryStateCode.A5.equals(stateCode) // 취소이거나
                            || DeliveryStateCode.A4.equals(stateCode); // 배송완료이면
                });
    }

    @Test
    @Order(3)
    void callback() throws Exception {
        CallbackResponse callbackResponse = CallbackResponse.builder()
                .callbackId("baegopa-123-456")
                .driverName("김기사")
                .deliveryStateCode(DeliveryStateCode.A2)
                .build();

        mockMvc.perform(post("/test/callback")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(callbackResponse)))
                .andDo(print())
                .andDo(handler)
                .andDo(handler.document(
                        responseFields(
                                fieldWithPath("callbackId").description("호출 할 때 넣은 콜백 아이디"),
                                fieldWithPath("driverName").description("배달 기사 이름, 해당 값은 수락(A2) 일 때만 응답합니다.").optional(),
                                fieldWithPath("deliveryStateCode").description("배달 상태 코드 +\n"
                                        + Arrays.stream(DeliveryStateCode.values())
                                        .map(code -> code.name() + " : " + code.getName())
                                        .collect(Collectors.joining(" +\n"))))
                ))
                .andExpect(status().isOk());
    }
}