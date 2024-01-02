package example.ratelimiter.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resilience")
public class ResilienceController {

    @Value("${resilience4j.ratelimiter.instances.user-throttling.limitRefreshPeriod}")
    private String limitRefreshPeriod;

    @GetMapping(value = "/api/test")
    @RateLimiter(name = "rateLimiterApi", fallbackMethod = "rateLimiterFallback")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("success");
    }

    public ResponseEntity<String> rateLimiterFallback(Throwable t) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Retry-After", "3s");

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .headers(responseHeaders)
                .body("짧은 시간에 너무 많은 요청을 보냈습니다. 3초 후에 재시도하세요.");
    }
}
