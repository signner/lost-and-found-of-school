package com.shiro.web;

import com.shiro.myEnum.UPFILEPATH;
import com.shiro.pojo.UserInfo;
import com.shiro.pojo.re.ReturnMessage;
import com.shiro.service.UserInfoService;
import com.shiro.utils.FileUploadUtil;
import com.shiro.utils.PasswordUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Api(description = "公共开放的接口")
@RestController
public class NormalController {
    @Autowired
    UserInfoService userService;
    @Value("${salt}")
    private String salt;
    @Value("${urlPath.online}")
    private String online;
    @Value("${urlPath.local}")
    private String local;

    /**
     * 登录
     *
     * @param users
     * @return
     */
    @ApiOperation(value = "登录", notes="通过用户名和密码进行登录，将token写入数据库token字段，返回当前用户对象")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "用户密码", paramType = "query", required = true, dataType = "String")
    })
    @PostMapping("/login")
    public ReturnMessage doLogin(UserInfo users) {
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token =
                new UsernamePasswordToken(users.getUsername(), users.getPassword());
        ReturnMessage returnMessage = new ReturnMessage();
        try {
            subject.login(token);
//            System.out.println(subject.getPrincipal());
//            token.setRememberMe(remeberMe);
            Session session = subject.getSession();
            String tokenId = String.valueOf(session.getId());
            UserInfo users1 = userService.findByUsername(users.getUsername());
            userService.setToken(tokenId, Math.toIntExact(users1.getUid()));
            users1.setToken(tokenId);
            returnMessage.setData(users1);
            returnMessage.setCode(200);
            returnMessage.setMessage("登录成功");
        } catch (IncorrectCredentialsException e) {
            returnMessage.setData("");
            returnMessage.setCode(400);
            returnMessage.setMessage("密码错误");
        } catch (LockedAccountException e) {
            returnMessage.setMessage("登录失败，该用户已被冻结");
            returnMessage.setData("");
            returnMessage.setCode(401);
        } catch (AuthenticationException e) {
            returnMessage.setData("");
            returnMessage.setCode(402);
            returnMessage.setMessage("该用户不存在");
        } catch (Exception e) {
            returnMessage.setData("");
            returnMessage.setCode(404);
            returnMessage.setMessage("该用户不存在");
        }
        return returnMessage;
    }

    /**
     * 未登录，shiro应重定向到登录界面，此处返回未登录状态信息由前端控制跳转页面
     *
     * @return
     */
    @RequestMapping(value = "/unauth")
    public Object unauth() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", "1000000");
        map.put("msg", "未登录");
        return map;
    }

    /**
     * 权限不足匹配的页面
     *
     * @return
     */
    @ApiOperation(value = "无权限接口", notes="如果执意访问一些非此角色的权限接口，会跳转到此接口")
    @RequestMapping("/403")
    public ReturnMessage _403() {
//        System.out.println("------------->403,权限不足");
        ReturnMessage r = new ReturnMessage();
        r.setCode(403);
        r.setData("当前角色权限不足");
        r.setMessage("您没有访问的权限！或许是token过期了");
        return r;
    }

    /**
     * 登出
     *
     * @return
     */
    @ApiOperation(value = "退出登录", notes="退出登录会清楚当前subject以及会向数据库的token字段写入uuid")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户id", paramType = "query", required = true, dataType = "Integer"),
            @ApiImplicitParam(name = "token", value = "token", paramType = "query", required = true, dataType = "String")
    })
    @PostMapping("/logout")
    public ReturnMessage logout(Integer uid, String token) throws Exception {
//        System.out.println("------------->logout");
        ReturnMessage r = new ReturnMessage();
        String token1 = userService.getToken(Math.toIntExact(uid));
        String msg = "";
        System.out.println(token.equals(token1) || token == token1);
        if (token.equals(token1) || token == token1) {
            //每次退出都往其中写入一个无序无规律不重复的字符串
            userService.setToken(String.valueOf(UUID.randomUUID()), uid);
            SecurityUtils.getSubject().logout();
            r.setCode(200);
            r.setData("");
            r.setMessage("退出登录成功！");
        } else {
            r.setCode(500);
            r.setData("");
            r.setMessage("你不是本用户，别擅自使用这个功能！");
        }
        return r;
    }

    /**
     * 自带头像
     *
     * @param user
     * @param avatarFile
     * @return
     */
    @ApiOperation(value = "注册", notes="进行注册操作")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "user", value = "用户", paramType = "query", required = true, dataType = "com.shiro.pojo.UserInfo"),
            @ApiImplicitParam(name = "avatarFile", value = "用户上传的自定义头像", paramType = "query", required = false, dataType = "org.springframework.web.multipart.MultipartFile")
    })
    @PostMapping("/zhuce")
    public ReturnMessage zhuce(UserInfo user, @RequestParam(name = "avatarFile", required = false) MultipartFile avatarFile) throws Exception {
        ReturnMessage r = new ReturnMessage();
        if (avatarFile != null) {
            String avatarUrl = FileUploadUtil.up(avatarFile, UPFILEPATH.avatar, online, local);
            user.setAvatar(avatarUrl);
        }
        user.setPassword(PasswordUtil.convertMD5(user.getPassword(), salt));
        userService.logonUser(user);
        r.setMessage("注册成功");

        return r;
    }
    @ApiOperation(value = "校验", notes="进行修改操作的时候对一些字段进行校核")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "field", value = "字段", paramType = "query", required = true, dataType = "String"),
            @ApiImplicitParam(name = "param", value = "字段的值", paramType = "query", required = true, dataType = "String")
    })
    @RequestMapping("none/check")
    public ReturnMessage check(String field, String param) {
        ReturnMessage r = new ReturnMessage();
        try {
            userService.checkUser(field, param);
            r.setMessage("此用户未被注册");
        } catch (Exception e) {
            r.setCode(300);
            r.setMessage(e.getMessage());
        }
        return r;
    }
    @ApiOperation(value = "获取角色", notes="在前端访问一些管理员界面时调用此接口获取当前角色")
    @RequestMapping("none/getRole")
    public ReturnMessage getRole() {
        ReturnMessage r = new ReturnMessage();
        Subject subject = SecurityUtils.getSubject();
        String[] roles = {"user", "admin", "guest"};
        String role = Arrays.asList(roles).stream().filter(s -> {
            return subject.hasRole(s);
        }).collect(Collectors.joining());
        if (role == null || role.equals(""))
            role = "none";
        r.setData(role);
        return r;
    }

}
