package AI.hotel.conf;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

        @Bean
        public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
            RedisTemplate<Object, Object> template = new RedisTemplate<>();
            template.setConnectionFactory(redisConnectionFactory);
            FastJsonRedisSerializer<Object> serializer = new FastJsonRedisSerializer<Object>(Object.class);
            // value值的序列化采用fastJsonRedisSerializer
            template.setValueSerializer(serializer);
            template.setHashValueSerializer(serializer);
            // key的序列化采用StringRedisSerializer
            template.setKeySerializer(new StringRedisSerializer());
            template.setHashKeySerializer(new StringRedisSerializer());
            template.setConnectionFactory(redisConnectionFactory);
            return template;
        }


        @Bean
        public RedisCacheManager redisCacheManager(RedisTemplate<Object, Object> redisTemplate) {
            RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisTemplate.getConnectionFactory());
            RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                    .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer()));
            return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
        }
}
