package com.xuanke.dao;

import com.xuanke.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * Created by lzq on 2017/4/19.
 */

public interface UserDao {
    @Select("select * from sec_user where id=#{id}")
    public User findUser(@Param("id") int id);
}
