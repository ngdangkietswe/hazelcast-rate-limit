package dev.ngdangkietswe.hazelcastratelimit.controllers;

import dev.ngdangkietswe.hazelcastratelimit.enums.EndPointRatelimitEnum;
import dev.ngdangkietswe.hazelcastratelimit.payloads.responses.HazelcastResponse;
import dev.ngdangkietswe.hazelcastratelimit.services.MockCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author ngdangkietswe
 * @since 9/8/2024
 */

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Log4j2
public class MockCardController {

    private final MockCardService mockCardService;
    private final HazelcastResponse hazelcastResponse;

    @GetMapping("/mock-cards")
    public ResponseEntity<?> mockCards(@RequestHeader Map<String, String> headers) {
        return hazelcastResponse.asResponse(headers, EndPointRatelimitEnum.MOCK_CARDS.getValue(), mockCardService.mockCards());
    }
}
