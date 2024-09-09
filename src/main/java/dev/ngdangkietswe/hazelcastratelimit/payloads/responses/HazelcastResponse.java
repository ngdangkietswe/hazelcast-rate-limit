package dev.ngdangkietswe.hazelcastratelimit.payloads.responses;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import dev.ngdangkietswe.hazelcastratelimit.constants.HazelcastConstant;
import dev.ngdangkietswe.hazelcastratelimit.models.ConfigRateLimit;
import dev.ngdangkietswe.hazelcastratelimit.utils.HazelcastUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author ngdangkietswe
 * @since 9/9/2024
 */

@Component
@RequiredArgsConstructor
@Log4j2
public class HazelcastResponse {

    private final ConfigRateLimit configRateLimit;
    private final HazelcastInstance hazelcastInstance;

    public ResponseEntity<?> asResponse(Map<String, String> headers, String endpoint, Object data) {
        IMap<String/*user*/, Long/*requestCounter*/> hazelcastMap = hazelcastInstance.getMap(HazelcastConstant.MAPS_USER);
        var userRequest = HazelcastUtil.getUserRequest(headers, endpoint);
        ConfigRateLimit.ApiSpecified rateLimit = configRateLimit.getRateLimit(endpoint);
        if (HazelcastUtil.isAllowAccessResource(hazelcastInstance, userRequest, hazelcastMap, rateLimit)) {
            HazelcastUtil.saveAndIncrementCounter(hazelcastInstance, userRequest, hazelcastMap, rateLimit);
            return ResponseEntity.ok(data);
        }
        log.info("User:[{}] was rejected because too many requests in [{}] s", userRequest, rateLimit.getSecond());
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(Map.of("error_message", String.format("Too many request in a period. Limit %s request per %s(s)", rateLimit.getLimit(), rateLimit.getSecond())));
    }
}
