package com.xuanke.service.impl;


import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import com.xuanke.dao.UserDao;
import com.xuanke.entity.User;
import com.xuanke.service.UserService;
@Repository
public class UserServiceImpl implements UserService{
	private static final Logger LOGGER=LoggerFactory.getLogger(UserServiceImpl.class);
	@Autowired
	private UserDao userDao;
	@Autowired
	private RedisTemplate redisTemplate;
	@Override
	public User findUserById(int id) {
		//从redis 缓存里获取城市信息
		String key="user_"+id;
		ValueOperations<String, User> opsForValue = redisTemplate.opsForValue();
		//缓存是否存在
		boolean haskey=redisTemplate.hasKey(key);
		User user;
		if(haskey){
				user=opsForValue.get(key);
		}else
		{
			//否则从db中取
			user = userDao.findUser(id);
	}

		//插入到缓存里
		opsForValue.set(key, user,10,TimeUnit.SECONDS);
		LOGGER.info("UserServiceImpl.findUserById():用户插入缓存",user.toString());
		return user;
	}
}
