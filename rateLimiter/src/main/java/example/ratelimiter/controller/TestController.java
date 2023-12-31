package example.ratelimiter.controller;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
public class TestController {

    /*
    - Refill : 일정 시간마다 충전할 Token의 개수 지정
    - Bandwidth : Bucket의 총 크기를 지정
    - Bucket : 실제 트래픽 제어에 사용
     **/

    // 충전 간격을 5초로 지정하며, 한 번 충전할 때마다 3개의 토큰을 충전
    private final Bucket bucket;

    public TestController() {
        // 충전 간격을 5초로 지정하며, 한 번 충전할 때마다 3개의 토큰을 충전한다.
        Refill refill = Refill.intervally(3, Duration.ofSeconds(5));

        // Bucket의 총 크기는 3
        Bandwidth limit = Bandwidth.classic(3, refill);

        // 총 크기는 3이며 5초마다 3개의 토큰을 충전하는 Bucket
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @GetMapping(value = "/api/test")
    public ResponseEntity<String> test() {
        if (bucket.tryConsume(1)) {
            return ResponseEntity.ok("success!");
        }
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }
}
