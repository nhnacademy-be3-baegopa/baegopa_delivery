package store.baegopa.delivery.config;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import store.baegopa.delivery.util.SecureKeyManager;

/**
 * DB 설정 프로퍼티
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
@Setter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "store.baegopa.delivery.database")
public class DatabaseProperties {
    private final SecureKeyManager secureKeyManager;

    private String url;
    private String username;
    private String password;
    private String driverClassName;
    private int initialSize;
    private int maxTotal;
    private int maxIdle;
    private int minIdle;
    private long maxWaitMillis;
    private String validationQuery;
    private boolean testOnBorrow;

    public String getUrl() {
        return secureKeyManager.keyStore(url);
    }

    public String getUsername() {
        return secureKeyManager.keyStore(username);
    }

    public String getPassword() {
        return secureKeyManager.keyStore(password);
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public long getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }
}
