package store.baegopa.delivery.util;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Component;

/**
 * 배송 정보 아이디 저장소
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
@Component
public class DeliveryInfoIdStorage {
    private static final Map<Long, Long> deliveryInfoIdMap = new HashMap<>();

    /**
     * 해당 threadId에 맞는 deliveryInfoId를 가져온다.
     *
     * @param threadId threadId
     * @return long deliveryInfoId
     * @author 김현준
     */
    public long getId(long threadId) {
        Long result = deliveryInfoIdMap.get(threadId);

        if (result == null) {
            throw new NoSuchElementException();
        }

        return result;
    }

    /**
     * 해당 threadId에 맞는 deliveryInfoId를 저장한다.
     *
     * @param threadId       threadId
     * @param deliveryInfoId deliveryInfoId
     * @author 김현준
     */
    public void setId(long threadId, long deliveryInfoId) {
        deliveryInfoIdMap.put(threadId, deliveryInfoId);
    }


    /**
     * 해당 threadId에 맞는 deliveryInfoId를 삭제한다.
     *
     * @param threadId threadId
     * @author 김현준
     */
    public void remove(long threadId) {
        deliveryInfoIdMap.remove(threadId);
    }
}
