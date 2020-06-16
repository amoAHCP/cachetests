package com.example.accessingdatarest.configuration;


import com.example.accessingdatarest.entity.Person;
import com.example.accessingdatarest.serializer.PersonKryoSerializer;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.config.XmlClientConfigBuilder;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import java.util.Optional;

@Configuration
public class CachingConfiguration implements CachingConfigurer {
    private @Autowired
    Environment environment;
    private @Value("${spring.cache.file}")
    String hzConfig;

    @Profile("!devlocal")
    @Bean
    public CacheManager cacheManager() {
        return new HazelcastCacheManager(hazelcastInstance());
    }

    @Override
    public CacheResolver cacheResolver() {
        return null;
    }

    @Override
    public KeyGenerator keyGenerator() {
        return null;
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return null;
    }

    @Bean
    @Profile("devlocal")
    public CacheManager getNoOpCacheManager() {
        return new NoOpCacheManager();
    }

    HazelcastInstance hazelcastInstance() {
        String POD_NAMESPACE = System.getenv("POD_NAMESPACE");
        String HZ_GROUP_NAME = System.getenv("HZ_GROUP_NAME");
        String HZ_CLIENT_CONFIG = System.getenv("HZ_CLIENT_CONFIG");
        String HZ_SERVICE_NAME = System.getenv("HZ_SERVICE_NAME");
        System.out.println("Pod Namespace: " + POD_NAMESPACE);
        SerializerConfig productSerializer = new SerializerConfig()
                .setTypeClass(Person.class)
                .setImplementation(new PersonKryoSerializer(false));

        ClientConfig config = new XmlClientConfigBuilder(CachingConfiguration.class.getClassLoader().getResourceAsStream(Optional.ofNullable(HZ_CLIENT_CONFIG).orElse(hzConfig))).build(); //new ClientConfig();
        //config.getSerializationConfig().addSerializerConfig(productSerializer);


        /**   if(POD_NAMESPACE!=null) {
         //config.getGroupConfig().setName(HZ_GROUP_NAME);
         String serviceName = HZ_SERVICE_NAME + "." + POD_NAMESPACE + ".svc.cluster.local";
         System.out.println("resolve cache service service: "+serviceName);
         config.getNetworkConfig().getKubernetesConfig().setEnabled(true)
         .setProperty("service-dns", serviceName);



         }**/
        // config.getGroupConfig().setName("caching-test");
        // for client HazelcastInstance LocalMapStatistics will not available
        return HazelcastClient.newHazelcastClient(config);
        // return Hazelcast.newHazelcastInstance();
    }


    /** @Bean public PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    return new PropertySourcesPlaceholderConfigurer();
    }**/
}