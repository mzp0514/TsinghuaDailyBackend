package com.mobilecourse.backend.controllers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mobilecourse.backend.WebSocketServer;
import com.mobilecourse.backend.dao.UserDao;
import com.mobilecourse.backend.model.Test;
import com.mobilecourse.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Hashtable;
import java.util.List;

@RestController
@EnableAutoConfiguration
//设置此参数可以给这个类的所有接口都加上前缀
@RequestMapping("/user")
public class UserController extends CommonController {

	@Autowired
	private UserDao userMapper;

	@RequestMapping(value = "/register", method = { RequestMethod.POST })
	public String register(@RequestParam(value = "username")String username,
	                       @RequestParam(value = "password")String password) {
		User u = new User();
		u.setUsername(username);
		u.setPassword(password);
		try {
			userMapper.insert(u);
		}
		catch (Exception e){
			return wrapperMsg(404, "username already exists");
		}
		return wrapperMsg(200, "register success");
	}

	@RequestMapping(value = "/login", method = { RequestMethod.POST })
	public String login(@RequestParam(value = "username")String username,
	                       @RequestParam(value = "password")String password,
	                    HttpServletRequest request) {
		User u = userMapper.get(username);
		if(u == null){
			return wrapperMsg(404, "user not exist");
		}
		else if(u.getPassword().equals(password)){
			putInfoToSession(request, "username", u.getUsername());
			return wrapperMsg(200, "login success");
		}
		else{
			return wrapperMsg(404, "wrong password");
		}

	}

	@RequestMapping(value = "/logout", method = { RequestMethod.POST })
	public String logout(HttpServletRequest request) {
		removeInfoFromSession(request, "username");
		return wrapperMsg(200, "logout success");
	}



}
