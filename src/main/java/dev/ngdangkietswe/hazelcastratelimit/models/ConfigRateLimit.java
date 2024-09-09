package dev.ngdangkietswe.hazelcastratelimit.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author ngdangkietswe
 * @since 9/8/2024
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "set", builderMethodName = "newBuilder")
public class ConfigRateLimit {

    private int defaultLimit;
    private int defaultSecond;
    private Map<String/*api-endpoint*/, ApiSpecified> apiSpecified;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder(setterPrefix = "set", builderMethodName = "newBuilder")
    public static class ApiSpecified {
        private int limit;
        private int second;
    }

    public ApiSpecified getRateLimit(String api) {
        return apiSpecified.getOrDefault(api, ApiSpecified.newBuilder()
                .setLimit(defaultLimit)
                .setSecond(defaultSecond)
                .build());
    }
}
