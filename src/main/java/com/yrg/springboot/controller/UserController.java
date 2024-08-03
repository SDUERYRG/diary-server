package com.yrg.springboot.controller;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yrg.springboot.entity.User;
import com.yrg.springboot.service.UserService;
import com.yrg.springboot.utils.MD5;
import com.yrg.springboot.utils.MyException;
import com.yrg.springboot.utils.Result;
import com.yrg.springboot.utils.ResultCode;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;

@RestController
public class UserController {
    @Resource
    private UserService userService;

    @GetMapping("/all/{current}/{pageSize}")
    @ResponseBody
    @Transactional(propagation = Propagation.SUPPORTS)
    public Result getAllUsersPage(@PathVariable int current, @PathVariable int pageSize) {
        IPage<User> page = userService.getAllUsersPage(current, pageSize);
        if (current > page.getPages()) // 当前页大于总页数
            page = userService.getAllUsersPage((int) page.getPages(), pageSize);
        System.out.println("查询成功");
        return new Result(true, page);
    }


    @GetMapping("/{current}/{pageSize}")
    @ResponseBody
    @Transactional(propagation = Propagation.SUPPORTS)
    public Result getPage(@PathVariable int current, @PathVariable int pageSize, @RequestParam(required = false) User user) {
        System.out.println(current + " " + pageSize + " ");
        IPage<User> page;
        if (user == null) {
            page = userService.getPage(current, pageSize);
        }else {
            page = userService.getPage(current, pageSize, user);
        }
        if (current > page.getPages()) //当前页大于总页数
            page = userService.getPage((int) page.getPages(), pageSize, user);
        return new Result(true, page);
    }


    @GetMapping("/{userId}")
    @ResponseBody
    @Transactional(propagation = Propagation.SUPPORTS)
    public Result editShow(@PathVariable String userId) {
        User user = userService.getById(userId);
        if(user != null)
            return Result.success(user);
        else
            return Result.error();
    }

    @PutMapping
    @ResponseBody
    @Transactional
    public Result updateUser(@RequestBody User user) {
        User oldUser = userService.getById(user.getUserId());
        //如果密码没更改
        if(!user.getPassword().equals(oldUser.getPassword())){
            System.out.println(oldUser.getPassword());
            user.setPassword(MD5.md5PlusSalt(user.getPassword()));
        }
        if (userService.updateById(user))
            return Result.success(ResultCode.SUCCESS.code(), "修改成功(●'◡'●)");
        else
            return Result.error(ResultCode.ERROR.code(), "修改失败(＞﹏＜)");
    }

    @PostMapping
    @ResponseBody
    @Transactional
    public Result addUser(@RequestBody User user) {
        User byAccount = userService.getByAccount(user.getAccount());
        if (byAccount != null)
            throw new MyException(ResultCode.USER_HAS_EXISTED.code(), ResultCode.USER_HAS_EXISTED.message());
        //md5加密
        user.setPassword(MD5.md5PlusSalt(user.getPassword()));
        if (userService.save(user))
            return Result.success(ResultCode.SUCCESS.code(), "添加成功(●'◡'●)");
        else
            return Result.error(ResultCode.ERROR.code(), "添加失败(＞﹏＜)");
    }

    @DeleteMapping("/{userId}")
    @ResponseBody
    @Transactional
    public Result delUserById(@PathVariable String userId) {
        if (userService.removeById(userId))
            return Result.success(ResultCode.SUCCESS.code(), "删除成功(●'◡'●)");
        else
            return Result.error();
    }

    @DeleteMapping("/delByIdList")
    @ResponseBody
    @Transactional
    public Result delByIdList(@RequestBody JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("idArr");
        ArrayList<String> idList = new ArrayList<>();
        for (Object id : jsonArray) {
            idList.add((String) id);
        }
        if (userService.removeByIds(idList))
            return Result.success(ResultCode.SUCCESS.code(), "删除成功(●'◡'●)");
        else
            return Result.error();
    }

    /**
     * 用户头像修改
     *
     * @Author liuzhi
     * @Date 2022/10/10 18:47
     */
    @PostMapping("/changeTx/{userId}")
    @ResponseBody
    @Transactional
    public Result changeTx(MultipartFile file, @PathVariable String userId) {
        if (userService.changeTx(file, userId)) {
            User user = userService.getById(userId);
            return Result.success(ResultCode.SUCCESS.code(), "修改成功(≧∇≦)ﾉ", user);
        } else {
            return Result.error(ResultCode.ERROR.code(), "修改失败つ﹏⊂");
        }
    }
}
