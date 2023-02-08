package org.example.configuration;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import org.example.users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private static final long CACHE_MAXIMUM_SIZE = 1024;
    private final UserService userService;

    @Autowired
    public CacheConfiguration(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public CacheManager cacheManager() {

        return new ConcurrentMapCacheManager() {
            @Override
            protected Cache createConcurrentMapCache(String name) {
                return new ConcurrentMapCache(
                        name,
                        CacheBuilder.newBuilder()
                                .maximumSize(CACHE_MAXIMUM_SIZE)
                                .refreshAfterWrite(1, TimeUnit.HOURS)
                                .expireAfterWrite(12, TimeUnit.HOURS)
                                .build(new CacheLoader() {
                                    @Override
                                    public Object load(Object key) {

                                        if(key == null)
                                            throw new IllegalArgumentException("Account ID can't be null");

                                        return switch (name) {
                                            case "login" -> userService.find((String)key);
                                            default -> throw new IllegalArgumentException();
                                        };
                                    }
                                }).asMap(),
                        false
                );
            }
        };
    }

}
