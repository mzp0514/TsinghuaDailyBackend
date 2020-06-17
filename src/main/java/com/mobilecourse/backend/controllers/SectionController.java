package com.mobilecourse.backend.controllers;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mobilecourse.backend.dao.FollowDao;
import com.mobilecourse.backend.dao.SectionDao;

import com.mobilecourse.backend.model.Follow;
import com.mobilecourse.backend.model.Section;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@EnableAutoConfiguration
//设置此参数可以给这个类的所有接口都加上前缀
@RequestMapping("/section")
public class SectionController extends CommonController {

	@Autowired
	private SectionDao sectionMapper;

	@Autowired
	private FollowDao followMapper;

	@RequestMapping(value = "/get-sections", method = { RequestMethod.GET })
	public String getCategorySections(@RequestParam(value = "category", defaultValue = "") String category,
	                                  HttpServletRequest request) {
		ArrayList<Section> sections = null;
		if(category.equals("Follow")){
			int uid = (int) request.getSession().getAttribute("user_id");
			List<Follow> follows = followMapper.selectByUserId(uid);
			for(int i = 0; i < follows.size(); i++){
				sections.add(sectionMapper.getBySectionId(follows.get(i).getSection_id()));
			}
		}
		else {
			sections = (ArrayList<Section>) sectionMapper.selectByCategory(category);
		}

		JSONArray sec = JSONArray.parseArray(JSON.toJSONString(sections));
		JSONObject wrapperMsg = new JSONObject();
		wrapperMsg.put("code", 200);
		wrapperMsg.put("sections", sec);
		return wrapperMsg.toJSONString();
	}


	@RequestMapping(value = "/follow", method = { RequestMethod.POST })
	public String follow(@RequestParam(value = "section_id") int section_id,
	                     HttpServletRequest request) {
		Follow f = new Follow();
		f.setSection_id(section_id);
		f.setUser_id((Integer) request.getSession().getAttribute("user_id"));
		followMapper.insert(f);
		sectionMapper.updateFollowerCnt(section_id, 1);
		return wrapperMsg(200, "follow success");
	}

	@RequestMapping(value = "/unfollow", method = { RequestMethod.POST })
	public String unfollow(@RequestParam(value = "section_id") int section_id,
	                     HttpServletRequest request) {
		Follow f = new Follow();
		f.setSection_id(section_id);
		f.setUser_id((Integer) request.getSession().getAttribute("user_id"));
		followMapper.delete(f);
		sectionMapper.updateFollowerCnt(section_id, -1);
		return wrapperMsg(200, "unfollow success");
	}

	@RequestMapping(value = "/section-info", method = { RequestMethod.GET })
	public String sectionInfo(@RequestParam(value = "section_id") int section_id,
	                       HttpServletRequest request) {
		int uid = (int) request.getSession().getAttribute("user_id");
		Follow f = followMapper.get(uid, section_id);
		Boolean followed = true;
		if(f == null){
			followed = false;
		}
		Section s = sectionMapper.getBySectionId(section_id);
		JSONObject wrapperMsg = new JSONObject();
		wrapperMsg.put("code", 200);
		wrapperMsg.put("followed", followed);
		wrapperMsg.put("info", JSON.toJSON(s));
		return wrapperMsg.toJSONString();
	}

}
