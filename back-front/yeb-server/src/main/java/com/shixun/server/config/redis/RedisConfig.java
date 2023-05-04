package com.shixun.server.config.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
/*序列化配置*/
    @Bean
    public RedisTemplate<String,Object> redisTemplate(LettuceConnectionFactory connectionFactory){
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        /*String类型 key序列器*/
        template.setKeySerializer(new StringRedisSerializer());
        /*String类型 value序列器*/
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        /*Hash类型 key序列器*/
        template.setHashKeySerializer(new StringRedisSerializer());
        /*Hash类型 value序列器*/
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(connectionFactory);
        return template;
    }
}
