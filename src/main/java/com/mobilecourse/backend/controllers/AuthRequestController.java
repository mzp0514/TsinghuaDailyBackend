package com.mobilecourse.backend.controllers;

import com.mobilecourse.backend.dao.AuthRequestDao;
import com.mobilecourse.backend.dao.MemberDao;
import com.mobilecourse.backend.dao.SectionDao;
import com.mobilecourse.backend.dao.UserDao;
import com.mobilecourse.backend.model.AuthRequest;
import com.mobilecourse.backend.model.Member;
import com.mobilecourse.backend.model.User;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import net.sf.json.JSONArray;

@RestController
@EnableAutoConfiguration
//设置此参数可以给这个类的所有接口都加上前缀
@RequestMapping("/auth-request")
public class AuthRequestController extends CommonController {

	@Autowired
	private AuthRequestDao AuthRequestMapper;

	@Autowired
	private UserDao UserMapper;

	@Autowired
	private SectionDao SectionMapper;

	@Autowired
	private MemberDao MemberMapper;

	@RequestMapping(value = "/request", method = { RequestMethod.POST })
	public String request(@RequestParam(value = "id_num")String id_num,
	                      @RequestParam(value = "dept_name")String dept_name,
	                      @RequestParam(value = "user_type")String user_type,
	                      @RequestParam(value = "id_card")String id_card,
	                      HttpServletRequest request) {
		AuthRequest rq = new AuthRequest();
		rq.setDept_name(dept_name);
		rq.setId_card(id_card);
		rq.setType(user_type);
		rq.setId_num(id_num);
		User receiver = UserMapper.getBySectionId(SectionMapper.getBySectionName(dept_name).getSection_id());
		rq.setReceiver_id(receiver.getUser_id());
		rq.setSender_id((Integer) request.getSession().getAttribute("user_id"));
		AuthRequestMapper.insert(rq);
		return wrapperMsg(200, "success");
	}

	@RequestMapping(value = "/get-requests", method = { RequestMethod.GET })
	public String get(HttpServletRequest request) {
		int uid = (Integer) request.getSession().getAttribute("user_id");
		List<AuthRequest> requests = AuthRequestMapper.get(uid);
		JSONArray js = JSONArray.fromObject(requests);
		JSONObject wrapperMsg = new JSONObject();
		wrapperMsg.put("code", 200);
		wrapperMsg.put("requests", js);
		return wrapperMsg.toString();
	}

	@RequestMapping(value = "/approve", method = { RequestMethod.POST })
	public String approve(@RequestParam(value = "request_id")int request_id,
	                      HttpServletRequest request) {
		AuthRequest rq = AuthRequestMapper.getById(request_id);
		UserMapper.updateInfoAuth(rq.getSender_id(), true, rq.getDept_name(),
										rq.getType(), rq.getId_num());
		AuthRequestMapper.delete(request_id);
		Member m = new Member();
		m.setSection_id(SectionMapper.getBySectionName(rq.getDept_name()).getSection_id());
		m.setUser_id(rq.getSender_id());
		MemberMapper.insert(m);
		return wrapperMsg(200, "success");
	}
}
