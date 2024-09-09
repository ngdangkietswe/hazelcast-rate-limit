package dev.ngdangkietswe.hazelcastratelimit.utils;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.IAtomicLong;
import com.hazelcast.map.IMap;
import dev.ngdangkietswe.hazelcastratelimit.constants.HazelcastConstant;
import dev.ngdangkietswe.hazelcastratelimit.models.ConfigRateLimit;
import lombok.extern.log4j.Log4j2;

import java.util.Map;
import java.util.Objects;

/**
 * @author ngdangkietswe
 * @since 9/9/2024
 */

@Log4j2
public class HazelcastUtil {

    // Check if user is allowed to access resource
    public static boolean isAllowAccessResource(HazelcastInstance hazelcastInstance, String userRequest, Map<String, Long> hazelcastMap, ConfigRateLimit.ApiSpecified rateLimit) {
        var currentCounter = getCurrentCounterOfUser(hazelcastInstance, userRequest, hazelcastMap);
        return currentCounter < rateLimit.getLimit();
    }

    // Get current counter of user
    public static long getCurrentCounterOfUser(HazelcastInstance hazelcastInstance, String userRequest, Map<String, Long> hazelcastMap) {
        var currentCounter = hazelcastMap.get(userRequest);
        if (Objects.isNull(currentCounter)) {
            resetCounter(hazelcastInstance, userRequest);
            return 0;
        }
        return currentCounter;
    }

    // Reset counter for user
    public static void resetCounter(HazelcastInstance hazelcastInstance, String userRequest) {
        IAtomicLong counter = hazelcastInstance.getCPSubsystem().getAtomicLong(userRequest);
        counter.set(0);
        log.info("Reset counter for user: {}, counter: {}", userRequest, counter.get());
    }

    // Save and increment counter for user
    public static void saveAndIncrementCounter(HazelcastInstance hazelcastInstance, String userRequest, IMap<String, Long> hazelcastMap, ConfigRateLimit.ApiSpecified rateLimit) {
        IAtomicLong counter = hazelcastInstance.getCPSubsystem().getAtomicLong(userRequest);
        counter.incrementAndGet();
        hazelcastMap.put(userRequest, counter.get(), rateLimit.getSecond(), java.util.concurrent.TimeUnit.SECONDS);
        log.info("User: {}, counter: {}", userRequest, counter.get());
    }

    // Get user from headers, ex: "hazelcast-users: ngdangkietswe"
    public static String getUserRequest(Map<String, String> headers, String endpoint) {
        return headers.getOrDefault(HazelcastConstant.AUTHENTICATED_USER, "anonymous") + "-" + endpoint;
    }
}
