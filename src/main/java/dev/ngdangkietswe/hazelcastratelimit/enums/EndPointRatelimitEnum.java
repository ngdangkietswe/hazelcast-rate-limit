package dev.ngdangkietswe.hazelcastratelimit.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ngdangkietswe
 * @since 9/9/2024
 */

@Getter
@AllArgsConstructor
public enum EndPointRatelimitEnum {

    MOCK_CARDS("mock-cards"),
    MOCK_USERS("mock-users");

    private final String value;
}
