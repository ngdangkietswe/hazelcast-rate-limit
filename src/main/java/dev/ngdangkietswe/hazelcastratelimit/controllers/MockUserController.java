package dev.ngdangkietswe.hazelcastratelimit.controllers;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import dev.ngdangkietswe.hazelcastratelimit.constants.HazelcastConstant;
import dev.ngdangkietswe.hazelcastratelimit.enums.EndPointRatelimitEnum;
import dev.ngdangkietswe.hazelcastratelimit.models.ConfigRateLimit;
import dev.ngdangkietswe.hazelcastratelimit.services.MockUserService;
import dev.ngdangkietswe.hazelcastratelimit.utils.HazelcastUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author ngdangkietswe
 * @since 9/9/2024
 */

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Log4j2
public class MockUserController {

    private final MockUserService mockUserService;
    private final ConfigRateLimit configRateLimit;
    private final HazelcastInstance hazelcastInstance;

    @GetMapping("/mock-users")
    public ResponseEntity<?> mockUsers(@RequestHeader Map<String, String> headers) {
        IMap<String/*user*/, Long/*requestCounter*/> hazelcastMap = hazelcastInstance.getMap(HazelcastConstant.MAPS_USER);
        String endpoint = EndPointRatelimitEnum.MOCK_USERS.getValue();
        var userRequest = HazelcastUtil.getUserRequest(headers, endpoint);
        ConfigRateLimit.ApiSpecified rateLimit = configRateLimit.getRateLimit(EndPointRatelimitEnum.MOCK_USERS.getValue());
        if (HazelcastUtil.isAllowAccessResource(hazelcastInstance, userRequest, hazelcastMap, rateLimit)) {
            HazelcastUtil.saveAndIncrementCounter(hazelcastInstance, userRequest, hazelcastMap, rateLimit);
            return ResponseEntity.ok(mockUserService.mockUsers());
        }
        log.info("User:[{}] was rejected because too many requests in [{}] s", userRequest, rateLimit.getSecond());
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(Map.of("error_message", String.format("Too many request in a period. Limit %s request per %s(s)", rateLimit.getLimit(), rateLimit.getSecond())));
    }
}
