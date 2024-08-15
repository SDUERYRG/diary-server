package com.yrg.springboot.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yrg.springboot.entity.User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService extends IService<User> {

        /**
        * 分页获取
        *
        * @Author yrg
        * @Date 2024/7/19 15:33
        */
        IPage<User> getPage(int current, int pageSize);

        IPage<User> getPage(int current, int pageSize, User user);

        IPage<User> searchUsers(int current, int pageSize, String keyword);

        /**
        * 修改头像
        *
        * @Author yrg
        * @Date 2024/7/19 15:33
        */
        boolean changeTx(MultipartFile file, @PathVariable String userId);

        /**
        * 登录验证
        *
        * @Author yrg
        * @Date 2024/7/19 15:33
        */
        User userAuthentication(User user);
        String getPower(String account);
        /**
        * 判断账号是否重复
        *
        * @Author yrg
        * @Date 2024/7/19 15:33
        */
        User getByAccount(String account);

        /**
        * 发送邮件验证码
        *
        * @Author yrg
        * @Date 2024/7/19 15:33
        */
        void sendMail(String email);

        /**
        * 邮件验证码验证
        *
        * @Author yrg
        * @Date 2024/7/19 15:33
        */
        boolean checkCode(String mail, String code);
        /**
         * (忘记密码)修改密码
         *
         * @Author yrg
         * @Date 2024/7/19 16:02
         */
        Integer modifyPassword(User user);
        /**
         * 查询全部用户
         *
         * @Author yrg
         * @Date 2024/8/3 15:56
         */
        public IPage<User> getAllUsersPage(int current, int pageSize) ;

        /**
         * 通过用户名获取用户Id
         *
         * @Author yrg
         * @Date 2024/8/15 16:24
         */
        String getUserIdByUserName(String userName);
}
