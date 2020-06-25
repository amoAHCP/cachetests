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

    @Profile("!devlocal & !kube1")
    @Bean(name = "cacheManager")
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

    @Bean(name = "cacheManager")
    @Profile("devlocal")
    public CacheManager getNoOpCacheManager() {
        return new NoOpCacheManager();
    }

    @Bean(name = "cacheManager")
    @Profile("kube1")
    public CacheManager getNoOpCacheManagerKube() {
        System.out.println("Kube 1: cache manager");
        return new NoOpCacheManager();
    }

    HazelcastInstance hazelcastInstance() {

        String HZ_CLIENT_CONFIG = System.getenv("HZ_CLIENT_CONFIG");

        System.out.println("config: " + hzConfig);
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