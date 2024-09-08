package dev.ngdangkietswe.hazelcastratelimit.configurations;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import dev.ngdangkietswe.hazelcastratelimit.models.ConfigRateLimit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ngdangkietswe
 * @since 9/8/2024
 */

@Configuration
public class HazelcastConfiguration {

    @Value("#{'${hazelcast.ngdangkietswe.address}'.split(',')}")
    private String[] address;
    @Value("${hazelcast.ngdangkietswe.cluster_name}")
    private String clusterName;
    @Value("${hazelcast.ngdangkietswe.limit}")
    private int limit;
    @Value("${hazelcast.ngdangkietswe.second}")
    private int second;

    @Bean
    public HazelcastInstance hazelcastInstance() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName(clusterName);
        clientConfig.getNetworkConfig().addAddress(address);
        clientConfig.getNetworkConfig().setConnectionTimeout(50000);
        return HazelcastClient.newHazelcastClient(clientConfig);
    }

    @Bean
    public ConfigRateLimit configRateLimit() {
        ConfigRateLimit configDefault = new ConfigRateLimit();
        configDefault.setLimit(limit);
        configDefault.setSecond(second);
        return configDefault;
    }
}
