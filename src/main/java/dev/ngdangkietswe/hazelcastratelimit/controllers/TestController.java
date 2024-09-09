package dev.ngdangkietswe.hazelcastratelimit.controllers;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import dev.ngdangkietswe.hazelcastratelimit.constants.HazelcastConstant;
import dev.ngdangkietswe.hazelcastratelimit.enums.EndPointRatelimitEnum;
import dev.ngdangkietswe.hazelcastratelimit.models.ConfigRateLimit;
import dev.ngdangkietswe.hazelcastratelimit.payloads.responses.HazelcastResponse;
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
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api")
public class TestController {

    private final HazelcastResponse hazelcastResponse;

    @GetMapping("/mock-test")
    public ResponseEntity<?> mockTest(@RequestHeader Map<String, String> headers) {
        return hazelcastResponse.asResponse(headers, EndPointRatelimitEnum.MOCK_TEST.getValue(), Map.of("message", "Hello World"));
    }
}
