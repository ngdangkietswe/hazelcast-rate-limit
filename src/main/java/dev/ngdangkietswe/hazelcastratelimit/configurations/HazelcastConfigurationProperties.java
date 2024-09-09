package dev.ngdangkietswe.hazelcastratelimit.configurations;

import dev.ngdangkietswe.hazelcastratelimit.models.ConfigRateLimit;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ngdangkietswe
 * @since 9/9/2024
 */

@Configuration
@ConfigurationProperties(prefix = "hazelcast.ngdangkietswe")
@Getter
@Setter
public class HazelcastConfigurationProperties {

    private String[] address;
    private String clusterName;
    private int defaultLimit;
    private int defaultSecond;
    private Map<String, Map<String, Integer>> api;

    @Bean
    public ConfigRateLimit configRateLimit() {
        ConfigRateLimit configRateLimit = new ConfigRateLimit();
        configRateLimit.setDefaultLimit(defaultLimit);
        configRateLimit.setDefaultSecond(defaultSecond);

        Map<String, ConfigRateLimit.ApiSpecified> rateLimitMap = new HashMap<>();
        api.forEach((key, value) -> {
            ConfigRateLimit.ApiSpecified apiSpecified = new ConfigRateLimit.ApiSpecified();
            apiSpecified.setLimit(value.get("limit"));
            apiSpecified.setSecond(value.get("second"));
            rateLimitMap.put(key, apiSpecified);
        });

        configRateLimit.setApiSpecified(rateLimitMap);

        return configRateLimit;
    }
}
