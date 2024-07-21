package com.yrg.springboot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yrg.springboot.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author yrg
 * @Date 2024/7/19 15:37
 * 用户管理相关操作
 */

@Repository
@Mapper
public interface UserDao extends BaseMapper<User>{

    @Select("select * from user where userId=#{userId}")
    List<User> selectByUserId(Integer userId);

    /**
     * 修改用户头像
     *
     * @Author yrg
     * @Date 2024/7/19 15:39
     */
    @Update("update user set txPicture = #{fileName} where userId = #{userId}")
    boolean changeTx(String userId, String fileName);

    /**
     * 登录验证
     *
     * @Author yrg
     * @Date 2024/7/19 15:40
     */
    @Select("SELECT * FROM user where account = #{account}")
    User userAuthentication(User user);
    /**
     * 判断账号是否重复
     *
     * @Author YRG
     * @Date 2024/7/19 15:41
     */
    @Select("SELECT * FROM user_table where account = #{account}")
    User getByAccount(String account);

    /**
     * 用户注册
     *
     * @Author yrg
     * @Date 2024/7/19 15:41
     */
    @Insert("insert into user_table(userName,account,password,email,introduce) values(#{userName},#{account},#{password},#{email},#{introduce})")
    Integer userRegister(User user);

    /**
     * (忘记密码)修改密码
     *
     * @Author yrg
     * @Date 2024/7/19 15:42
     */
    @Update("update user_table set password = #{password} where account = #{account}")
    Integer modifyPassword(User user);
}
