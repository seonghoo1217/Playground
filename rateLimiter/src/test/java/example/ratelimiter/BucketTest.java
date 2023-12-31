package example.ratelimiter;

import example.ratelimiter.controller.BucketTestController;
import example.ratelimiter.service.BucketService;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BucketTest {
    @Autowired
    BucketTestController bucketTestController;

    @MockBean
    BucketService bucketService;

    private MockHttpServletRequest request;
    private Bucket mockBucket;

    @BeforeEach
    public void setUp() {
        request = new MockHttpServletRequest();
        request.addHeader("Host", "localhost");

        mockBucket = mock(Bucket.class);
        when(bucketService.resolveBucket(any(HttpServletRequest.class))).thenReturn(mockBucket);
    }

    @Test
    public void bucketConsume_isOk() {
        when(mockBucket.tryConsume(1)).thenReturn(true);
        ResponseEntity<?> responseEntity = bucketTestController.bucketAccess(request);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void bucketConsume_isReject() {
        when(mockBucket.tryConsume(1)).thenReturn(false);
        ResponseEntity<?> responseEntity = bucketTestController.bucketAccess(request);
        assertEquals(HttpStatus.TOO_MANY_REQUESTS, responseEntity.getStatusCode());
    }

    @Test
    public void bucketConsume_retryAfterDelay() throws InterruptedException {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

        // 첫 번째 요청 - 실패
        when(mockBucket.tryConsume(1)).thenReturn(false);
        ResponseEntity<?> responseEntity = bucketTestController.bucketAccess(request);
        assertEquals(HttpStatus.TOO_MANY_REQUESTS, responseEntity.getStatusCode());

        // 10초 후에 다시 요청
        executorService.schedule(() -> {
            when(mockBucket.tryConsume(1)).thenReturn(true);
            ResponseEntity<?> response = bucketTestController.bucketAccess(request);
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }, 10, TimeUnit.SECONDS);

        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(15, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}
