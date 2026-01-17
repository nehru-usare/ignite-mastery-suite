package com.ignite.mastery.config;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cluster.ClusterState;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

/**
 * Apache Ignite Configuration Class
 * 
 * Purpose: This class tells Spring Boot how to start Apache Ignite.
 * Think of this as the "Brain" setup for our distributed system.
 */
@Configuration
public class IgniteConfig {

    @Bean
    public Ignite igniteInstance() {
        // 1. Create a new configuration object
        IgniteConfiguration cfg = new IgniteConfiguration();

        // 2. Set node name
        cfg.setIgniteInstanceName("IgniteClientNode");

        // 3. MANDATORY FOR STANDALONE: Client Mode = true
        // This tells this app: "Don't store data, just connect to the cluster".
        cfg.setClientMode(true);

        // 4. Discovery Configuration
        // This tells the app WHERE the standalone server is.
        TcpDiscoverySpi spi = new TcpDiscoverySpi();
        TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();

        // We look for the server on localhost:47500 (default port)
        ipFinder.setAddresses(Collections.singletonList("127.0.0.1:47500..47509"));

        spi.setIpFinder(ipFinder);
        cfg.setDiscoverySpi(spi);

        // 5. Start the client!
        Ignite ignite = Ignition.start(cfg);

        // 6. ACTIVATE THE CLUSTER (Only needed if Persistence is enabled)
        ignite.cluster().state(ClusterState.ACTIVE);

        // 7. ENABLE STATISTICS EXPLICITLY
        // We use getOrCreateCache to ensure the client node is aware of the cache
        // definition.
        ignite.getOrCreateCache("productCache");
        ignite.cluster().enableStatistics(Collections.singletonList("productCache"), true);

        return ignite;
    }
}
