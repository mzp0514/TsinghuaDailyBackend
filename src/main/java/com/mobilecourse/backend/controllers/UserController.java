package com.mobilecourse.backend.controllers;



import com.mobilecourse.backend.WebSocketServer;
import com.mobilecourse.backend.dao.UserDao;
import com.mobilecourse.backend.model.User;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
			JSONObject wrapperMsg = new JSONObject();
			wrapperMsg.put("code", 200);
			wrapperMsg.put("info", JSONObject.parseObject(JSON.toJSONString(u)));
			return wrapperMsg.toJSONString();
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


	@RequestMapping(value = "/get-info", method = { RequestMethod.GET })
	public String getInfo(@RequestParam(value = "id_num", required = false)String id_num,
	                      @RequestParam(value = "user_id", required = false)Integer user_id,
	                      HttpServletRequest request) {
		int uid = 0;
		try {
			uid = (int) request.getSession().getAttribute("user_id");
		} catch (Exception e){
			e.printStackTrace();
			return wrapperMsg(404, "please log in");
		}

		User u = null;

		if(id_num == null){
			if(user_id == null) {
				u = userMapper.getById(uid);
			}
			else{
				u = userMapper.getById(user_id);
			}
		}
		else {
			u = userMapper.getByIdNum(id_num);
		}

		if(u == null){
			return wrapperMsg(404, "user not exist");
		}
		JSONObject userinfo = JSONObject.parseObject(JSON.toJSONString(u));
		userinfo.remove("password");
		JSONObject wrapperMsg = new JSONObject();
		wrapperMsg.put("code", 200);
		wrapperMsg.put("info", userinfo);
		return wrapperMsg.toJSONString();
	}



	@RequestMapping(value = "/modify-info", method = { RequestMethod.POST })
	public String updateInfo(@RequestParam(value = "avatar", defaultValue = "")String avartar,
	                     @RequestParam(value = "status",  defaultValue = "")String status,
	                     HttpServletRequest request) {
		int uid = 0;
		try {
			uid = (int) request.getSession().getAttribute("user_id");
		} catch (Exception e){
			e.printStackTrace();
			return wrapperMsg(404, "please log in");
		}

		userMapper.updateInfo(uid, avartar, status);
		return wrapperMsg(200, "success");
	}

}
