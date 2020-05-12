package com.mobilecourse.backend.controllers;


import com.alibaba.fastjson.JSONObject;
import com.mobilecourse.backend.WebSocketServer;
import com.mobilecourse.backend.dao.UserDao;
import com.mobilecourse.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


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
		User u = userMapper.getByName(username);
		if(u == null){
			return wrapperMsg(404, "user not exist");
		}
		else if(u.getPassword().equals(password)){
			putInfoToSession(request, "user_id", u.getUser_id());
			return wrapperMsg(200, "login success");
		}
		else{
			return wrapperMsg(404, "wrong password");
		}

	}

	@RequestMapping(value = "/logout", method = { RequestMethod.POST })
	public String logout(HttpServletRequest request) {
		removeInfoFromSession(request, "user_id");
		return wrapperMsg(200, "logout success");
	}

	@RequestMapping(value = "/get-self-info", method = { RequestMethod.GET })
	public String getSelfInfo(HttpServletRequest request) {
		User u = userMapper.getById((int) request.getSession().getAttribute("user_id"));
		JSONObject wrapperMsg = new JSONObject();
		wrapperMsg.put("code", 200);
		wrapperMsg.put("avatar", u.getAvatar());
		wrapperMsg.put("status", u.getStatus());
		wrapperMsg.put("verified", u.getVerified());
		wrapperMsg.put("dept_name", u.getDept_name());
		wrapperMsg.put("user_type", u.getType());
		if(u.getAdmin()){
			wrapperMsg.put("section_id", u.getSection_id());
		}
		else{
			wrapperMsg.put("id_num", u.getId_num());
		}
		return wrapperMsg.toJSONString();
	}

	@RequestMapping(value = "/get-info", method = { RequestMethod.GET })
	public String getInfo(@RequestParam(value = "id_num")String id_num,
	                      HttpServletRequest request) {
		try {
			User u = userMapper.getByIdNum(id_num);
			JSONObject wrapperMsg = new JSONObject();
			wrapperMsg.put("code", 200);
			wrapperMsg.put("avatar", u.getAvatar());
			wrapperMsg.put("status", u.getStatus());
			wrapperMsg.put("verified", u.getVerified());
			if(u.getAdmin()){
				wrapperMsg.put("section_id", u.getSection_id());
			}
			else{
				wrapperMsg.put("dept_name", u.getDept_name());
				wrapperMsg.put("id_num", u.getId_num());
				wrapperMsg.put("user_type", u.getType());
			}
			return wrapperMsg.toJSONString();
		}
		catch (Exception e){
			return wrapperMsg(404, "user not exist");
		}
	}

	@RequestMapping(value = "/modify-info", method = { RequestMethod.POST })
	public String updateInfo(@RequestParam(value = "avatar", defaultValue = "")String avartar,
	                     @RequestParam(value = "status",  defaultValue = "")String status,
	                     HttpServletRequest request) {
		userMapper.updateInfo((int) request.getSession().getAttribute("user_id"),
								avartar, status);
		return wrapperMsg(200, "success");
	}

}
