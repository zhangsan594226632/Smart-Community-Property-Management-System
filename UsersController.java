package com.ai.doctor.controller;

import cn.hutool.core.date.DateUtil;
import com.ai.doctor.beans.Loginlog;
import com.ai.doctor.beans.Users;
import com.ai.doctor.service.LoginlogService;
import com.ai.doctor.service.UsersService;
import com.ai.doctor.utils.Iputil;
import com.ai.doctor.utils.Results;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
@Slf4j
@Tag(name = "用户接口", description = "用户接口") // 类级注解
public class UsersController {

    @Autowired
    UsersService usersService;


    @Autowired
    LoginlogService loginlogService;

    /**
     * 01-查询
     * @return
     */
    @Operation(summary = "获取用户信息", description = "获取用户信息") // 方法级注解
    @GetMapping("list")
    public Results list(){
        List<Users> list = usersService.list();
        return Results.success(list);
    }


    /**
     * 02-新增
     * @return
     */
    @PostMapping("add")
    @Operation(summary = "新增用户信息", description = "新增用户信息") // 方法级注解
    public Results add(@RequestBody Users u){
        boolean b = usersService.save(u);
        if(b){
            return Results.success();
        }else {
            return Results.error();
        }
    }


    /**
     * 03-更新
     * @return
     */
    @PutMapping("update")
    @Operation(summary = "更新用户信息", description = "更新用户信息") // 方法级注解
    public Results update(@RequestBody Users u){
        boolean b = usersService.updateById(u);
        if(b){
            return Results.success();
        }else {
            return Results.error();
        }
    }

    /**
     * 04-删除
     * @return
     */
    @DeleteMapping("delete/{id}")
    @Operation(summary = "删除用户信息", description = "删除用户信息") // 方法级注解
    public Results delete(@PathVariable("id") Integer id){
        boolean b = usersService.removeById(id);
        if(b){
            return Results.success();
        }else {
            return Results.error();
        }
    }

    /**
     * 05-用户登录
     * @return
     */
    @PostMapping("login")
    @Operation(summary = "用户登录", description = "用户登录") // 方法级注解
    public Results login(@RequestBody Users user){
        log.info("登录的信息是:"+user);
        Users u = usersService.getOne(new QueryWrapper<Users>().eq("name", user.getName()).eq("pass",user.getPass()));
        if(u!=null){

            //记录登录的日志信息
            Loginlog loginlog=new Loginlog();
            loginlog.setName(user.getName());
            String ip = Iputil.getIp();
            loginlog.setIp(ip);
            loginlog.setAddress(Iputil.getAddress(ip));
            loginlog.setLogintime(DateUtil.now());
            //存储到数据库
            loginlogService.save(loginlog);
            log.info("登录人日志信息:"+loginlog);

            return Results.success(u);
        }else{
            return Results.error("账号或密码错误！");
        }
    }

    /**
     * 05-用户注册
     * @return
     */
    @Operation(summary = "注册信息", description = "注册信息") // 方法级注解
    @PostMapping("register")
    public Results register(@RequestBody Users users) {
        Users u = usersService.getOne(new QueryWrapper<Users>().eq("phone", users.getPhone()));
        if(u!=null){
            return Results.error("注册的手机号已存在");
        }else{
            Users one = usersService.getOne(new QueryWrapper<Users>().eq("name", users.getName()));
            if(one==null){
                boolean b = usersService.save(users);
                if(b){
                    return Results.success();
                }else{
                    return Results.error();
                }
            }else{
                return Results.error("注册的账户已存在");
            }
        }
    }
}
