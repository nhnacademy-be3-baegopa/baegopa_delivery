package store.baegopa.delivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import store.baegopa.delivery.entity.DeliveryInfoEntity;

/**
 * 배송 정보 레파지토리
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
public interface DeliveryInfoRepository extends JpaRepository<DeliveryInfoEntity, Long> {
}
