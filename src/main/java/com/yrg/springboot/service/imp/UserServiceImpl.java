package com.yrg.springboot.service.imp;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yrg.springboot.dao.UserDao;
import com.yrg.springboot.entity.User;
import com.yrg.springboot.service.UserService;
import com.yrg.springboot.utils.CodeUtils;
import com.yrg.springboot.utils.MD5;
import com.yrg.springboot.utils.MyException;
import com.yrg.springboot.utils.ResultCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {
    @Resource
    private UserDao userDao;

    @Value("D:/txImg")
    private String txImgPath;

    @Override
    public String getPower(String account) {
        return userDao.getPower(account);
    }
    /**
     * 分页查询
     *
     * @Author yrg
     * @Date 2024/7/19 15:47
     */
    @Override
    public IPage<User> getPage(int current, int pageSize) {
        IPage page = new Page(current, pageSize);
        userDao.selectPage(page, null);
        return page;
    }
    /**
     * 分页+条件查询
     *
     * @Author yrg
     * @Date 2024/7/19 15:48
     */
    @Override
    public IPage<User> getPage(int current, int pageSize, User user) {
        Page page = new Page(current, pageSize);
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.like(user.getUserName() != null, User::getUserName, user.getUserName());
        lqw.like(user.getAccount() != null, User::getAccount, user.getAccount());
        lqw.like(user.getEmail() != null, User::getEmail, user.getEmail());
        userDao.selectPage(page, lqw);
        return page;
    }

    @Override
    public IPage<User> searchUsers(int current, int pageSize, String keyword) {
        Page<User> page = new Page<>(current, pageSize);
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.like(keyword != null, User::getUserName, keyword)
                .or().like(keyword != null, User::getAccount, keyword)
                .or().like(keyword != null, User::getEmail, keyword);
        userDao.selectPage(page, lqw);
        return page;
    }
    /**
     * 修改头像
     *
     * @Author yrg
     * @Date 2024/7/19 15:48
     */
    @Override
    public boolean changeTx(MultipartFile file, @PathVariable String userId) {
        //防止文件名冲突
        String fileName = UUID.randomUUID() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        //更新头像后删除原来的头像
        User userById = userDao.selectById(userId);
        String oldTxName = userById.getTxPicture();
        try {
            file.transferTo(new File(txImgPath + fileName));
        } catch (Exception e) {
            throw new MyException(ResultCode.FILE_UPLOAD_FAIL.code(), ResultCode.FILE_UPLOAD_FAIL.message());
        }
        if (!"defaultTX.gif".equals(oldTxName)) { //不是默认头像则删除
            File file1 = new File(txImgPath + oldTxName);
            file1.delete();
        }
        return userDao.changeTx(userId, fileName);
    }
    /**
     * 登录验证
     *
     * @Author yrg
     * @Date 2024/7/21 14:48
     */
    @Override
    public User userAuthentication(User user) {
        return userDao.userAuthentication(user);
    }
    /**
     * 判断账号是否重复
     *
     * @Author yrg
     * @Date 2024/7/21 14:48
     */
    @Override
    public User getByAccount(String account) {
        return userDao.getByAccount(account);
    }
    /**
     * 发送邮件验证码
     *
     * @Author yrg
     * @Date 2024/7/19 15:52
     */
    @Resource
    private CodeUtils codeUtils;
    @CreateCache(name = "emailCodeCache_", expire = 300, timeUnit = TimeUnit.SECONDS)//本地方案
    private Cache<String, String> emailCodeCache;
    @Resource
    private JavaMailSender javaMailSender;

    @Override
    public void sendMail(String email) {
        String code = codeUtils.generateCode(email);
        emailCodeCache.put(email, code);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("1941456753@qq.com(姚仁广)");    //发件人
        message.setTo(email);        //收件人
        message.setSubject("数据库课设.验证码"); //标题
        message.setText("您的验证码为:" + emailCodeCache.get(email) + ",有效期为5分钟，此邮件为自动发送，请勿回复");    //正文
        javaMailSender.send(message);
    }
    /**
     * 邮件验证码验证
     *
     * @Author yrg
     * @Date 2024/7/21 14:52
     */
    @Override
    public boolean checkCode(String mail, String code) {
        return code.equals(emailCodeCache.get(mail));
    }

    /**
     * (忘记密码)修改密码
     *
     * @Author yrg
     * @Date 2024/7/21 14:52
     */
    @Override
    public Integer modifyPassword(User user) {
        user.setPassword(MD5.md5PlusSalt(user.getPassword()));
        Integer integer = userDao.modifyPassword(user);
        return integer;
    }
    public IPage<User> getAllUsersPage(int current, int pageSize) {
        Page<User> page = new Page<>(current, pageSize);
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        // 不添加任何查询条件，查询所有用户
        userDao.selectPage(page, lqw);
        return page;
    }

}
