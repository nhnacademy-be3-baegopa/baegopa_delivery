package store.baegopa.delivery.config;

import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Collection;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.operation.OperationRequest;
import org.springframework.restdocs.operation.OperationRequestPart;
import org.springframework.restdocs.operation.OperationResponse;
import org.springframework.restdocs.operation.Parameters;
import org.springframework.restdocs.operation.RequestCookie;
import org.springframework.restdocs.operation.preprocess.OperationPreprocessor;
import org.springframework.web.util.UriComponentsBuilder;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

/**
 * 테스트코드에서 Rest Docs 작성 설정
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
public class RestDocsUtil {

    public static RestDocumentationResultHandler handler() {
        final URIUpdaterPreprocessor preprocessor = new URIUpdaterPreprocessor();

        return MockMvcRestDocumentation.document(
                "{class-name}/{method-name}",
                preprocessRequest(
                        prettyPrint(),
                        preprocessor),
                preprocessResponse(
                        prettyPrint())
        );
    }

    private static final class URIUpdaterPreprocessor implements OperationPreprocessor {

        @Override
        public OperationRequest preprocess(OperationRequest request) {
            return new URIUpdaterOperationRequest(request);
        }

        @Override
        public OperationResponse preprocess(OperationResponse response) {
            return response;
        }

    }

    private static final class URIUpdaterOperationRequest implements OperationRequest {

        private static final int PORT = 8087;
        private static final String HOST = "180.210.81.55"; // baegopa delivery
        private final OperationRequest delegate;

        public URIUpdaterOperationRequest(OperationRequest request) {
            delegate = request;
        }

        @Override
        public byte[] getContent() {
            return delegate.getContent();
        }

        @Override
        public String getContentAsString() {
            return delegate.getContentAsString();
        }

        @Override
        public HttpHeaders getHeaders() {
            HttpHeaders headers = new HttpHeaders();

            headers.addAll(delegate.getHeaders());

            headers.setHost(new InetSocketAddress(HOST, PORT));
            return headers;
        }

        @Override
        public HttpMethod getMethod() {
            return delegate.getMethod();
        }

        @Override
        public Parameters getParameters() {
            return delegate.getParameters();
        }

        @Override
        public Collection<OperationRequestPart> getParts() {
            return delegate.getParts();
        }

        @Override
        public URI getUri() {
            URI sourceUri = delegate.getUri();
            UriComponentsBuilder builder = UriComponentsBuilder.fromUri(sourceUri);
            return builder
                    .port(PORT)
                    .host(HOST)
                    .replacePath(sourceUri.getPath())
                    .build().toUri();
        }

        @Override
        public Collection<RequestCookie> getCookies() {
            return delegate.getCookies();
        }
    }
}
