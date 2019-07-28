package com.jincou.redislua.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Description: 实例redisTemplate 主要是修改默认的序列化方式
 * 如果这里不指定序列化 而用默认的jdk序列化，那么那么redis存储的value可能会是这样的 "rO0ABXQAAXF0AAF3"
 * @author xub
 * @date 2019/7/27 下午5:48
 */
@Slf4j
@Configuration
public class RedisConfig {

    /**
     * @Description: 这里重新定义redisTemplate非常有必要，不然就算数据存储到布隆过滤器中，你再去判断是否存在也会告诉你不存在
     *
     * @author xub
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(factory);
        // 使用Jackson2JsonRedisSerialize 替换默认序列化
        /**Jackson序列化  json占用的内存最小 */
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        /**Jdk序列化   JdkSerializationRedisSerializer是最高效的*/
//      JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
        /**String序列化*/
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        /**将key value 进行stringRedisSerializer序列化*/
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(stringRedisSerializer);
        /**将HashKey HashValue 进行序列化*/
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        log.info("redisTemplate初始化加载成功==========");
        return redisTemplate;
    }

}