package kopring.micrometer;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;

@Service
public class CustomMetricsService {

    private final Counter requestCounter;
    private final Timer requestTimer;
    private final CustomGauge customGauge;

    // 생성자에서 MeterRegistry를 주입받아 필요한 메트릭을 등록합니다.
    public CustomMetricsService(MeterRegistry meterRegistry) {
        // HTTP 요청 총 건수를 세는 Counter (태그로 엔드포인트 구분)
        this.requestCounter = meterRegistry.counter("custom.requests.total", "endpoint", "/api/metric");

        // HTTP 요청 처리 시간을 측정하는 Timer (태그로 엔드포인트 구분)
        this.requestTimer = meterRegistry.timer("custom.request.duration", "endpoint", "/api/metric");

        // Gauge: 예를 들어, 현재 활성 세션 수를 측정하기 위한 커스텀 객체를 등록
        this.customGauge = new CustomGauge();
        Gauge.builder("custom.active.sessions", customGauge, CustomGauge::getActiveSessions)
                .tag("region", "us-east")
                .register(meterRegistry);
    }

    /**
     * 실제 비즈니스 로직을 실행할 때 요청 카운트와 처리 시간을 측정합니다.
     *
     * @param requestLogic 실제 처리할 로직 (예: HTTP 요청 처리)
     */
    public void processRequest(Runnable requestLogic) {
        // 요청 수 증가
        requestCounter.increment();
        // 요청 처리 시간 측정
        requestTimer.record(requestLogic);
    }

    /**
     * 활성 세션 수 업데이트 (예를 들어, 로그인/로그아웃 이벤트에서 호출)
     *
     * @param activeSessions 현재 활성 세션 수
     */
    public void updateActiveSessions(int activeSessions) {
        customGauge.setActiveSessions(activeSessions);
    }

    /**
     * 커스텀 Gauge의 값을 저장하는 내부 클래스.
     */
    private static class CustomGauge {
        // 현재 활성 세션 수를 저장 (volatile을 사용해 스레드 안정성 확보)
        private volatile double activeSessions = 0;

        public double getActiveSessions() {
            return activeSessions;
        }

        public void setActiveSessions(double activeSessions) {
            this.activeSessions = activeSessions;
        }
    }
}
