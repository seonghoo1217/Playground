package example.ratelimiter.controller;


import com.google.common.util.concurrent.RateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/guava")
public class GuavaTestController {

    private final RateLimiter rateLimiter;

    public GuavaTestController() {
        // 0.1초에 한 번만 호출 가능
        rateLimiter = RateLimiter.create(2);
    }

    @GetMapping(value = "/api/test")
    public ResponseEntity<String> test() {
        if (rateLimiter.tryAcquire()) {
            return ResponseEntity.ok("success!");
        }

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
    }
}
