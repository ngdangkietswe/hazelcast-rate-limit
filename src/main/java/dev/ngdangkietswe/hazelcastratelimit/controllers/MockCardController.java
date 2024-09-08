package dev.ngdangkietswe.hazelcastratelimit.controllers;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.IAtomicLong;
import com.hazelcast.map.IMap;
import dev.ngdangkietswe.hazelcastratelimit.models.ConfigRateLimit;
import dev.ngdangkietswe.hazelcastratelimit.services.MockCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Objects;

/**
 * @author ngdangkietswe
 * @since 9/8/2024
 */

@RestController
@RequiredArgsConstructor
@Log4j2
public class MockCardController {

    private final MockCardService mockCardService;
    private final ConfigRateLimit configRateLimit;
    private final HazelcastInstance hazelcastInstance;

    private static final String MAPS_USER = "USERS";
    private static final String AUTHENTICATED_USER = "hazelcast-users";

    @GetMapping("/api/mock-cards")
    public ResponseEntity<?> mockCards(@RequestHeader Map<String, String> headers) {
        IMap<String/*user*/, Long/*requestCounter*/> hazelcastMap = hazelcastInstance.getMap(MAPS_USER);
        var userRequest = getUserFromHeaders(headers);
        if (isAllowAccessResource(userRequest, hazelcastMap)) {
            saveAndIncrementCounter(userRequest, hazelcastMap);
            return ResponseEntity.ok(mockCardService.mockCards());
        }
        log.info("User:[{}] was rejected because too many requests in [{}] s", userRequest, configRateLimit.getSecond());
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(Map.of("error_message", String.format("Too many request in a period. Limit %s request per %s(s)", configRateLimit.getLimit(), configRateLimit.getSecond())));
    }

    // Check if user is allowed to access resource
    private boolean isAllowAccessResource(String userRequest, Map<String, Long> hazelcastMap) {
        var currentCounter = getCurrentCounterOfUser(userRequest, hazelcastMap);
        return currentCounter < configRateLimit.getLimit();
    }

    // Get current counter of user
    private long getCurrentCounterOfUser(String userRequest, Map<String, Long> hazelcastMap) {
        var currentCounter = hazelcastMap.get(userRequest);
        if (Objects.isNull(currentCounter)) {
            resetCounter(userRequest);
            return 0;
        }
        return currentCounter;
    }

    // Reset counter for user
    private void resetCounter(String userRequest) {
        IAtomicLong counter = hazelcastInstance.getCPSubsystem().getAtomicLong(userRequest);
        counter.set(0);
        log.info("Reset counter for user: {}, counter: {}", userRequest, counter.get());
    }

    // Save and increment counter for user
    private void saveAndIncrementCounter(String userRequest, IMap<String, Long> hazelcastMap) {
        IAtomicLong counter = hazelcastInstance.getCPSubsystem().getAtomicLong(userRequest);
        counter.incrementAndGet();
        hazelcastMap.put(userRequest, counter.get(), configRateLimit.getSecond(), java.util.concurrent.TimeUnit.SECONDS);
        log.info("User: {}, counter: {}", userRequest, counter.get());
    }

    // Get user from headers, ex: "hazelcast-users: ngdangkietswe"
    private String getUserFromHeaders(Map<String, String> headers) {
        return headers.getOrDefault(AUTHENTICATED_USER, "anonymous");
    }
}
