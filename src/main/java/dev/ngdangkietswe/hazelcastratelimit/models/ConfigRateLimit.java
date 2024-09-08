package dev.ngdangkietswe.hazelcastratelimit.models;

import lombok.Data;

/**
 * @author ngdangkietswe
 * @since 9/8/2024
 */

@Data
public class ConfigRateLimit {

    private int limit;
    private int second;
}
