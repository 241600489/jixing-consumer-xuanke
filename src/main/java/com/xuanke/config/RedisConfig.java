package com.xuanke.config;

import java.lang.reflect.Method;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
/*@Configuration//声明配置类
@EnableCaching//声明缓存管理类
*//*
 *明确指定高速缓存是如何解决的，以及如何为注释驱动生成密钥*/
public class RedisConfig extends CachingConfigurerSupport{
	/**
	 * 自定义键的生成策略
	 * 
	 */
	@Bean
	@Override
	public KeyGenerator keyGenerator() {
		
		return new KeyGenerator() {
			
			@Override
			public Object generate(Object target, Method method, Object... params) {
				StringBuilder sb=new StringBuilder();
				sb.append(target.getClass().getName());
				sb.append(method.getName());
				for (Object object : params) {
					sb.append(object.getClass().getName());
				}
				return sb.toString();
			}
		};
	}
	/**
	 * 管理缓存bean
	 */
	@Bean
	public CacheManager cacherManage(RedisTemplate redisTemplate){
		return new RedisCacheManager(redisTemplate);
	}
	/**
	 * 建立redis模板
	 */
	@Bean
	public 	RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory factory){
		RedisTemplate<String,Object> template=new RedisTemplate<String, Object>();
		/**
		 *  spring-data-redis提供了多种serializer策略，这对使用jedis的开发者而言，实在是非常便捷。sdr提供了4种内置的serializer：
  			JdkSerializationRedisSerializer：使用JDK的序列化手段(serializable接口，ObjectInputStrean，ObjectOutputStream)，数据以字节流存储
     		StringRedisSerializer：字符串编码，数据以string存储
			JacksonJsonRedisSerializer：json格式存储
			OxmSerializer：xml格式存储
			我们选择JacksonJsonRedisSerializer,速度快，占用内存少
		 */
		@SuppressWarnings("unchecked")
		Jackson2JsonRedisSerializer jackson2JsonRedisSerializer=new Jackson2JsonRedisSerializer<Object>(Object.class);
		/**
		 * RedisTemplate中需要声明4种serializer，默认为“JdkSerializationRedisSerializer”：
         1) keySerializer ：对于普通K-V操作时，key采取的序列化策略
         2) valueSerializer：value采取的序列化策略
         3) hashKeySerializer： 在hash数据结构中，hash-key的序列化策略
         4) hashValueSerializer：hash-value的序列化策略
           我们下面实现valueSerializer
		 */
		ObjectMapper om=new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL,com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		template.setValueSerializer(jackson2JsonRedisSerializer);
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setHashValueSerializer(jackson2JsonRedisSerializer);
		template.setConnectionFactory(factory);
		//
		/**
		 * 下面这个方法继承父类RedisAccessor的方法，又是InitializingBean的接口，
		 * 就是在BeanFactory在设置了所有提供的bean属性之后才调用，
		 * 允许bean实例仅执行初始化，并可抛出bean属性配置异常
		 */
		template.afterPropertiesSet();
		return template;
	}
}
