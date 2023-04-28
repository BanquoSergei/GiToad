package org.example.configuration;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import org.example.data.accounts.AccountService;
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
    private final AccountService accountService;

    @Autowired
    public CacheConfiguration(AccountService accountService) {
        this.accountService = accountService;
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
                                            return null;

                                        return switch (name) {
                                            case "jwt" -> accountService.findJwtById((String)key);
                                            default -> null;
                                        };
                                    }
                                }).asMap(),
                        true
                );
            }
        };
    }

}
