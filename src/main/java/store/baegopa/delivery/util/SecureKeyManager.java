package store.baegopa.delivery.util;


import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Objects;
import javax.net.ssl.SSLContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import store.baegopa.delivery.dto.response.KeyResponse;
import store.baegopa.delivery.exception.KeyMangerException;

/**
 * 암호화 된 키를 가져오는 서비스.
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
@Slf4j
@Component
public class SecureKeyManager {

    private final RestTemplate restTemplate;
    private final HttpEntity<Void> httpEntity;
    @Value("${nhn.cloud.security.url}")
    private String url;
    @Value("${nhn.cloud.security.appkey}")
    private String appKey;

    /**
     * 생성자.
     *
     * @param password 키 비밀번호
     * @throws KeyStoreException         KeyStoreException
     * @throws CertificateException      CertificateException
     * @throws IOException               IOException
     * @throws NoSuchAlgorithmException  NoSuchAlgorithmException
     * @throws UnrecoverableKeyException UnrecoverableKeyException
     * @throws KeyManagementException    KeyManagementException
     * @author 김현준
     */
    public SecureKeyManager(@Value("${nhn.cloud.security.password}") String password) throws KeyStoreException,
            CertificateException, IOException, NoSuchAlgorithmException,
            UnrecoverableKeyException, KeyManagementException {
        KeyStore clientStore = KeyStore.getInstance("PKCS12");

        InputStream result;
        result = new ClassPathResource("baegopa-project.p12").getInputStream();

        clientStore.load(result, password.toCharArray());

        SSLContext sslContext = SSLContextBuilder.create()
                .setProtocol("TLS")
                .loadKeyMaterial(clientStore, password.toCharArray())
                .loadTrustMaterial(new TrustSelfSignedStrategy())
                .build();

        SSLConnectionSocketFactory sslConnectionSocketFactory =
                new SSLConnectionSocketFactory(sslContext);
        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        restTemplate = new RestTemplate(requestFactory);
        httpEntity = new HttpEntity<>(headers);
    }

    /**
     * KeyID에 맞는 Data 를 가져온다.
     *
     * @param keyId keyId
     * @return keyId로 가져온 복호화 된 데이터.
     */
    public String keyStore(String keyId) {
        try {
            ResponseEntity<KeyResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    httpEntity,
                    KeyResponse.class,
                    appKey,
                    keyId
            );

            log.info("response : {}", response);

            return Objects.requireNonNull(response.getBody())
                    .getSecret();

        } catch (Exception e) {
            log.error("keyStore ERROR : {}", e.getMessage(), e);
            throw new KeyMangerException(e.getMessage());
        }
    }
}
