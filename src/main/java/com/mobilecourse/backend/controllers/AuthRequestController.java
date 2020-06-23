package com.mobilecourse.backend.controllers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mobilecourse.backend.dao.AuthRequestDao;
import com.mobilecourse.backend.dao.SectionDao;
import com.mobilecourse.backend.dao.UserDao;
import com.mobilecourse.backend.model.AuthRequest;
import com.mobilecourse.backend.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.List;



@RestController
@EnableAutoConfiguration
//设置此参数可以给这个类的所有接口都加上前缀
@RequestMapping("/auth-request")
public class AuthRequestController extends CommonController {

	@Autowired
	private AuthRequestDao authRequestMapper;

	@Autowired
	private UserDao userMapper;

	@Autowired
	private SectionDao sectionMapper;


	@RequestMapping(value = "/request", method = { RequestMethod.POST })
	public String request(@RequestParam(value = "id_num")String id_num,
	                      @RequestParam(value = "dept_name")String dept_name,
	                      @RequestParam(value = "user_type")String user_type,
	                      @RequestParam(value = "id_card")String id_card,
	                      HttpServletRequest request) {
		int uid = 0;
		try {
			uid = (int) request.getSession().getAttribute("user_id");
		} catch (Exception e){
			e.printStackTrace();
			return wrapperMsg(404, "please log in");
		}
		AuthRequest rq = new AuthRequest();
		rq.setDept_name(dept_name);
		rq.setId_card(id_card);
		rq.setType(user_type);
		rq.setId_num(id_num);
		rq.setUsername(userMapper.getById(uid).getUsername());
		User receiver = userMapper.getAdminBySectionId(sectionMapper.getBySectionName(dept_name).getSection_id());
		rq.setReceiver_id(receiver.getUser_id());
		rq.setSender_id(uid);
		authRequestMapper.insert(rq);
		userMapper.updateAuthStatus(uid, 1);
		return wrapperMsg(200, "success");
	}

	@RequestMapping(value = "/get-requests", method = { RequestMethod.GET })
	public String get(HttpServletRequest request) {
		int uid = 0;
		try {
			uid = (int) request.getSession().getAttribute("user_id");
		} catch (Exception e){
			e.printStackTrace();
			return wrapperMsg(404, "please log in");
		}
		List<AuthRequest> requests = authRequestMapper.get(uid);
		JSONArray req = JSONArray.parseArray(JSON.toJSONString(requests));
		JSONObject wrapperMsg = new JSONObject();
		wrapperMsg.put("code", 200);
		wrapperMsg.put("requests", req);
		return wrapperMsg.toJSONString();
	}

	@RequestMapping(value = "/approve", method = { RequestMethod.POST })
	public String approve(@RequestParam(value = "request_id")int request_id,
	                      HttpServletRequest request) {
		int uid = 0;
		try {
			uid = (int) request.getSession().getAttribute("user_id");
		} catch (Exception e){
			e.printStackTrace();
			return wrapperMsg(404, "please log in");
		}
		AuthRequest rq = authRequestMapper.getById(request_id);
		int section_id = sectionMapper.getBySectionName(rq.getDept_name()).getSection_id();
		userMapper.updateInfoAuth(rq.getSender_id(), 2, rq.getDept_name(),
										rq.getType(), rq.getId_num(), section_id);
		authRequestMapper.delete(request_id);
		
		return wrapperMsg(200, "success");
	}

	@RequestMapping(value = "/refuse", method = { RequestMethod.POST })
	public String refuse(@RequestParam(value = "request_id")int request_id,
	                      HttpServletRequest request) {
		int uid = 0;
		try {
			uid = (int) request.getSession().getAttribute("user_id");
		} catch (Exception e){
			e.printStackTrace();
			return wrapperMsg(404, "please log in");
		}
		AuthRequest rq = authRequestMapper.getById(request_id);
		authRequestMapper.delete(request_id);
		userMapper.updateAuthStatus(rq.getSender_id(), 3);
		return wrapperMsg(200, "success");
	}
}
