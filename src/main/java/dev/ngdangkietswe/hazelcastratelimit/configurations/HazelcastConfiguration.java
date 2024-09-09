package dev.ngdangkietswe.hazelcastratelimit.configurations;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author ngdangkietswe
 * @since 9/8/2024
 */

@Configuration
@RequiredArgsConstructor
public class HazelcastConfiguration {

    private final HazelcastConfigurationProperties hazelcastConfigurationProperties;

    @Bean
    public HazelcastInstance hazelcastInstance() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName(hazelcastConfigurationProperties.getClusterName());
        clientConfig.getNetworkConfig().addAddress(hazelcastConfigurationProperties.getAddress());
        clientConfig.getNetworkConfig().setConnectionTimeout(50000);
        return HazelcastClient.newHazelcastClient(clientConfig);
    }
}
