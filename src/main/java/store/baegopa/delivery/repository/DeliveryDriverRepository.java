package store.baegopa.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.baegopa.delivery.entity.DeliveryDriverEntity;

/**
 * 배송 기사 레파지토리
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
public interface DeliveryDriverRepository extends JpaRepository<DeliveryDriverEntity, Long> {
}
