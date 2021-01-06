package cn.jboost.base.starter.limiter;

import cn.jboost.base.starter.limiter.lock.DistributedLockAspect;
import cn.jboost.base.starter.limiter.lock.DistributedLock;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @Author ronwxy
 * @Date 2020/6/22 15:10
 * @Version 1.0
 */
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
public class RedisRateLimiterAutoConfiguration {

    @Bean
    public DistributedLock distributedLock(StringRedisTemplate stringRedisTemplate) {
        return new DistributedLock(stringRedisTemplate);
    }

    @Bean
    public DistributedLockAspect syncLockHandle(DistributedLock distributedLock) {
        return new DistributedLockAspect(distributedLock);
    }

    @Bean
    @ConditionalOnBean(RedisConnectionFactory.class)
    public PermitsRedisTemplate permitsRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        PermitsRedisTemplate template = new PermitsRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    public RedisRateLimiterFactory redisRateLimiterFactory(PermitsRedisTemplate permitsRedisTemplate, StringRedisTemplate stringRedisTemplate, DistributedLock distributedLock) {
        return new RedisRateLimiterFactory(permitsRedisTemplate, stringRedisTemplate, distributedLock);
    }

    @Bean
    public RedisCountLimiter redisCountLimiter(StringRedisTemplate stringRedisTemplate) {
        return new RedisCountLimiter(stringRedisTemplate);
    }

    @Bean
    public RedisLimitAspect redisLimitAspect(RedisCountLimiter redisCountLimiter, RedisRateLimiterFactory redisRateLimiterFactory) {
        return new RedisLimitAspect(redisCountLimiter, redisRateLimiterFactory);
    }

}
