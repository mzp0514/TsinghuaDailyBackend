package com.mobilecourse.backend.controllers;

import com.mobilecourse.backend.dao.*;
import com.mobilecourse.backend.model.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;


@RestController
@EnableAutoConfiguration
//设置此参数可以给这个类的所有接口都加上前缀
@RequestMapping("/comment")
public class CommentController extends CommonController {

	@Autowired
	private CommentDao commentMapper;

	@Autowired
	private UserDao userMapper;

	@Autowired
	private ArticleDao articleMapper;

	@RequestMapping(value = "/add", method = { RequestMethod.POST })
	public String add(@RequestParam(value = "article_id") int article_id,
	                      @RequestParam(value = "content") String content,
	                      HttpServletRequest request) {
		int uid = (int) request.getSession().getAttribute("user_id");
		Comment c = new Comment();
		c.setArticle_id(article_id);
		c.setContent(content);
		c.setUser_id(uid);
		c.setAdd_time(Timestamp.valueOf(LocalDateTime.now()));
		commentMapper.insert(c);
		return wrapperMsg(200, "success");
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST })
	public String delete(@RequestParam(value = "comment_id") int comment_id,
	                     @RequestParam(value = "article_id") int article_id,
	                  HttpServletRequest request) {
		int uid = (int) request.getSession().getAttribute("user_id");
		User u = userMapper.getById(uid);
		int section_id = articleMapper.getById(article_id).getSection_id();
		if(u.getAdmin() && section_id == u.getSection_id()) {
			commentMapper.delete(comment_id);
			return wrapperMsg(200, "success");
		}
		else{
			return wrapperMsg(404, "no authority");
		}
	}

	@RequestMapping(value = "/get-comments", method = { RequestMethod.GET })
	public String getComments(@RequestParam(value = "article_id") int article_id,
	                  HttpServletRequest request) {
		List<Comment> comments = commentMapper.select(article_id);
		JSONArray cmt = JSONArray.parseArray(JSON.toJSONString(comments));
		for(int i = 0; i < comments.size(); i++){
			User u = userMapper.getById(comments.get(i).getUser_id());
			JSONObject obj = cmt.getJSONObject(i);
			obj.put("username", u.getUsername());
			obj.put("avatar", u.getAvatar());
		}
		JSONObject wrapperMsg = new JSONObject();
		wrapperMsg.put("code", 200);
		wrapperMsg.put("comments", cmt);
		return wrapperMsg.toJSONString();
	}


}
