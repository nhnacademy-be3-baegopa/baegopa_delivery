package store.baegopa.delivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import store.baegopa.delivery.dto.request.DeliveryRequestRequest;
import store.baegopa.delivery.entity.DeliveryInfoEntity;
import store.baegopa.delivery.entity.DeliveryStateHistoryEntity;
import store.baegopa.delivery.entity.code.DeliveryStateCode;
import store.baegopa.delivery.repository.DeliveryDriverRepository;
import store.baegopa.delivery.repository.DeliveryInfoRepository;
import store.baegopa.delivery.repository.DeliveryStateHistoryRepository;

import static org.awaitility.Awaitility.await;
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
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(scripts = "classpath:driver_insert.sql")
@Slf4j
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

    @LocalServerPort
    private int port;

    @Test
    void getDeliveryRequest() throws Exception {
        log.info("port : {}", port);

        DeliveryRequestRequest deliveryRequestRequest = new DeliveryRequestRequest();
        ReflectionTestUtils.setField(deliveryRequestRequest, "deliveryAddress", "배달 주소");
        ReflectionTestUtils.setField(deliveryRequestRequest, "price", 5000);
        ReflectionTestUtils.setField(deliveryRequestRequest, "reqStore", "가게1");
        ReflectionTestUtils.setField(deliveryRequestRequest, "reqStoreAddress", "가게 주소");
        ReflectionTestUtils.setField(deliveryRequestRequest, "prepDatetime", LocalDateTime.now());
        ReflectionTestUtils.setField(deliveryRequestRequest, "reqMemo", "메모");
        ReflectionTestUtils.setField(deliveryRequestRequest, "callbackId", "123");
        ReflectionTestUtils.setField(deliveryRequestRequest, "callbackUrl", "http://local.baegopa.store:" + port + "/test/callback");

        mockMvc.perform(post("/delivery")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(deliveryRequestRequest)))
                .andDo(print())
                .andExpect(status().isOk());

        await().until(() -> {
            List<DeliveryInfoEntity> all = deliveryInfoRepository.findAll();

            return all.size() == 1;
        });

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
}