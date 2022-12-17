package net.bookdevcontainer.todolist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import lombok.extern.slf4j.Slf4j;

// リトライを設定する場合は次の行を有効化してください
import io.github.resilience4j.retry.annotation.Retry;


@Slf4j
@Service
@Qualifier("retryService")
public class ScheduleServiceImpl implements ScheduleService {

    public ScheduleServiceImpl() {
    }

    @Value("${schedule.api.url}")
    private String API_URL;

    @Autowired
    private RestTemplate restTemplate;
    private int i;

    @Override
    // リトライを設定する場合は次の行を有効化してください
    @Retry(name = "scheduleRetry", fallbackMethod = "retryFallback")
    public ResponseEntity<String> schedule() {
        
        log.info("Invoke Schedule API:  count= " + i++);

        return new ResponseEntity<>(
            restTemplate.getForObject( API_URL, String.class), 
            HttpStatus.OK
            );
    }

    public ResponseEntity<String> retryFallback(Throwable t) {
        log.error("Fallback Execution for Retry, cause - {}", t.toString());
        return new ResponseEntity<>("Fallback Execution for Retry", HttpStatus.SERVICE_UNAVAILABLE);
    }

    public ResponseEntity<String> limitFallback(Throwable t) {
        log.error("Fallback Execution for RateLimit, cause - {}", t.toString());
        return new ResponseEntity<>("Fallback Execution for for RateLimit", HttpStatus.SERVICE_UNAVAILABLE);
    }

    public ResponseEntity<String>  circuitFallback(Throwable t) {
        log.error("Fallback Execution for CircuitBreaker, cause - {}", t.toString());
        return new ResponseEntity<>("Fallback Execution for for CircuitBreaker", HttpStatus.SERVICE_UNAVAILABLE);
    }

}
