package store.baegopa.delivery.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 배송 정보 엔티티
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
@Table(name = "delivery_info")
public class DeliveryInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deliveryInfoId;

    @ManyToOne
    @JoinColumn(name = "delivery_driver_id")
    private DeliveryDriverEntity deliveryDriverEntity;

    @NotNull
    @Size(max = 255)
    private String deliveryAddress;

    @NotNull
    @Size(max = 255)
    private String reqStoreAddress;

    @NotNull
    private LocalDateTime prepDatetime;

    @NotNull
    private Integer price;

    @Size(max = 255)
    private String reqMemo;

    @NotNull
    @Size(max = 255)
    private String reqStore;

    public void setDeliveryDriverEntity(DeliveryDriverEntity deliveryDriverEntity) {
        this.deliveryDriverEntity = deliveryDriverEntity;
    }
}
