package com.xuanke;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.xuanke.dao.UserDao;
import com.xuanke.entity.User;

@SpringBootApplication
/*@EnableEurekaClient*/
@RestController
@MapperScan("com.xuanke.dao")
public class XuankeApplication {
	@Autowired
	private UserDao userDao;
	@Autowired
	private EurekaClient eurekaClient;

	@RequestMapping("/hello/{id}")
	@ResponseBody
	public User hello(@PathVariable("id") int id){
		User userInfo = userDao.findUser(1);
		return userInfo;
	}
	@GetMapping("/eureka-instance")
	public String serviceUrl(){
		InstanceInfo instanceInfo = this.eurekaClient.getNextServerFromEureka("localhost", false);
		return  instanceInfo.getHomePageUrl();
	}
	public static void main(String[] args) {
		SpringApplication.run(XuankeApplication.class, args);
	}
}
