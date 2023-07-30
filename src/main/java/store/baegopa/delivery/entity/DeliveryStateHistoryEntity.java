package store.baegopa.delivery.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import store.baegopa.delivery.entity.code.DeliveryStateCode;

/**
 * 배송 상태 변경 이력 엔티티
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "delivery_state_history")
public class DeliveryStateHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryStateHistoryId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DeliveryStateCode deliveryStateCode;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "delivery_info_id")
    private DeliveryInfoEntity deliveryInfoEntity;

    @NotNull
    private LocalDateTime createDatetime;

    @PrePersist
    private void setDefault() {
        createDatetime = LocalDateTime.now();
    }
}
