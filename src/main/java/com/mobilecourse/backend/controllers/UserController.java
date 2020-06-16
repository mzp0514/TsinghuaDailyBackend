package com.mobilecourse.backend.controllers;



import com.mobilecourse.backend.WebSocketServer;
import com.mobilecourse.backend.dao.UserDao;
import com.mobilecourse.backend.model.User;
import net.sf.json.JSONObject;
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
			wrapperMsg.put("user_id", u.getUser_id());
			return wrapperMsg.toString();

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
	public String getInfo(@RequestParam(value = "id_num", defaultValue = "self")String id_num,
	                      HttpServletRequest request) {
		User u = null;

		if(id_num.equals("self")){
			u = userMapper.getById((int) request.getSession().getAttribute("user_id"));
		}
		else {
			try {
				u = userMapper.getByIdNum(id_num);
			} catch (Exception e){
				return wrapperMsg(404, "user not exist");
			}

		}
		JSONObject userinfo = JSONObject.fromObject(u);
		userinfo.remove("password");
		JSONObject wrapperMsg = new JSONObject();
		wrapperMsg.put("code", 200);
		wrapperMsg.put("info", userinfo);
		return wrapperMsg.toString();
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
