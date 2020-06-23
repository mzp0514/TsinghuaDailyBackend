package com.mobilecourse.backend.controllers;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mobilecourse.backend.dao.*;
import com.mobilecourse.backend.model.*;
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

@RequestMapping("/article")
public class ArticleController extends CommonController {

	@Autowired
	private ArticleDao articleMapper;

	@Autowired
	private UserDao userMapper;

	@Autowired
	private SectionDao sectionMapper;

	@Autowired
	private FavouriteDao favouriteMapper;

	@Autowired
	private LikesDao likeMapper;


	@RequestMapping(value = "/cateory-articles", method = { RequestMethod.GET })
	public String getCategoryArticles(@RequestParam(value = "category") String category,
	                                  @RequestParam(value = "page_num") int page_num,
	                                  @RequestParam(value = "page_size", defaultValue = "10") int page_size,
	                                  HttpServletRequest request) {
		int uid = (int) request.getSession().getAttribute("user_id");
		User u = userMapper.getById(uid);
		if(u.getVerified() != 2){
			return wrapperMsg(404, "not verified");
		}
		PageHelper.startPage(page_num, page_size, "article_id desc");
		List<Article> articles = articleMapper.selectByCategory(category, uid,
				u.getSection_id(), u.getType().equals("Staff"));
		PageInfo pageInfo = new PageInfo(articles);
		JSONArray js = JSONArray.parseArray(JSON.toJSONString(pageInfo.getList()));
		for(int i = 0; i < js.size(); i++){
			JSONObject obj = js.getJSONObject(i);
			Favourite f = favouriteMapper.get(uid, (Integer) obj.get("article_id"));
			Boolean fav = (f != null);

			Likes l = likeMapper.get(uid, (Integer) obj.get("article_id"));
			Boolean lik = (l != null);

			obj.put("favoured", fav);
			obj.put("liked", lik);
			obj.remove("content");
		}
		JSONObject wrapperMsg = new JSONObject();
		wrapperMsg.put("code", 200);
		wrapperMsg.put("articles", js);
		return wrapperMsg.toJSONString();
	}

	@RequestMapping(value = "/section-articles", method = { RequestMethod.GET })
	public String getSectionArticles(@RequestParam(value = "section_id") int section_id,
	                                 @RequestParam(value = "page_num") int page_num,
	                                 @RequestParam(value = "page_size", defaultValue = "10") int page_size,
	                          HttpServletRequest request) {
		int uid = 0;
		try {
			uid = (int) request.getSession().getAttribute("user_id");
		} catch (Exception e){
			e.printStackTrace();
			return wrapperMsg(404, "please log in");
		}
		User u = userMapper.getById(uid);
		if(u.getVerified() != 2){
			return wrapperMsg(404, "not verified");
		}
		PageHelper.startPage(page_num, page_size, "article_id desc");
		List<Article> articles = articleMapper.selectBySectionId(section_id, u.getSection_id(),
																u.getType().equals("Staff"));
		PageInfo pageInfo = new PageInfo(articles);
		JSONArray js = JSONArray.parseArray(JSON.toJSONString(pageInfo.getList()));
		for(int i = 0; i < js.size(); i++){
			JSONObject obj = js.getJSONObject(i);
			Favourite f = favouriteMapper.get(uid, (Integer) obj.get("article_id"));
			Boolean fav = (f != null);

			Likes l = likeMapper.get(uid, (Integer) obj.get("article_id"));
			Boolean lik = (l != null);

			obj.put("favoured", fav);
			obj.put("liked", lik);
			obj.remove("content");
		}
		JSONObject wrapperMsg = new JSONObject();
		wrapperMsg.put("code", 200);
		wrapperMsg.put("articles", js);
		return wrapperMsg.toJSONString();
	}

	@RequestMapping(value = "/search", method = { RequestMethod.POST })
	public String search(@RequestParam(value = "query") String query,
	                     @RequestParam(value = "page_num") int page_num,
	                     @RequestParam(value = "page_size", defaultValue = "10") int page_size,
	                                 HttpServletRequest request) {
		int uid = 0;
		try {
			uid = (int) request.getSession().getAttribute("user_id");
		} catch (Exception e){
			e.printStackTrace();
			return wrapperMsg(404, "please log in");
		}
		User u = userMapper.getById(uid);

		if(u.getVerified() != 2){
			return wrapperMsg(404, "not verified");
		}

		PageHelper.startPage(page_num, page_size);
		List<Article> articles = articleMapper.search(query, u.getSection_id(),
				u.getType().equals("Staff"));
		PageInfo pageInfo = new PageInfo(articles);
		JSONArray js = JSONArray.parseArray(JSON.toJSONString(pageInfo.getList()));
		for(int i = 0; i < js.size(); i++){
			JSONObject obj = js.getJSONObject(i);
			Favourite f = favouriteMapper.get(uid, (Integer) obj.get("article_id"));
			Boolean fav = (f != null);

			Likes l = likeMapper.get(uid, (Integer) obj.get("article_id"));
			Boolean lik = (l != null);

			obj.put("favoured", fav);
			obj.put("liked", lik);
			obj.remove("content");
		}
		JSONObject wrapperMsg = new JSONObject();
		wrapperMsg.put("code", 200);
		wrapperMsg.put("articles", js);
		return wrapperMsg.toJSONString();
	}

	@RequestMapping(value = "/article", method = { RequestMethod.GET })
	public String getArticle(@RequestParam(value = "article_id") int article_id,
	                                 HttpServletRequest request) {
		int uid = 0;
		try {
			uid = (int) request.getSession().getAttribute("user_id");
		} catch (Exception e){
			e.printStackTrace();
			return wrapperMsg(404, "please log in");
		}

		try {
			Article a = articleMapper.getById(article_id);
			if(a == null){
				return wrapperMsg(404, "article not exist");
			}

			articleMapper.updateViewCnt(article_id, 1);

			Favourite f = favouriteMapper.get(uid, article_id);
			Boolean fav = (f != null);

			Likes l = likeMapper.get(uid, article_id);
			Boolean lik = (l != null);

			User u = userMapper.getById(uid);
			Boolean is_author = u.getSection_id() == a.getSection_id() && u.getAdmin();

			JSONObject obj = JSONObject.parseObject(JSON.toJSONString(a));
			obj.put("favoured", fav);
			obj.put("liked", lik);
			obj.put("is_author", is_author);

			JSONObject wrapperMsg = new JSONObject();
			wrapperMsg.put("code", 200);
			wrapperMsg.put("info", obj);
			return wrapperMsg.toJSONString();
		}
		catch (Exception e){
			return wrapperMsg(404, "article not found");
		}
	}

	@RequestMapping(value = "/publish", method = { RequestMethod.POST })
	public String publish(@RequestParam(value = "title") String title,
	                         @RequestParam(value = "content") String content,
	                         @RequestParam(value = "reader") String reader,
	                         HttpServletRequest request) {
		int uid = 0;
		try {
			uid = (int) request.getSession().getAttribute("user_id");
		} catch (Exception e){
			e.printStackTrace();
			return wrapperMsg(404, "please log in");
		}
		User u = userMapper.getById(uid);
		if(!u.getAdmin()){
			return wrapperMsg(404, "no authority");
		}

		Article a = new Article();
		a.setAuthor_name(u.getDept_name());
		a.setPublish_time(Timestamp.valueOf(LocalDateTime.now()));
		a.setContent(content);
		a.setReader(reader);
		a.setSection_id(u.getSection_id());
		a.setTitle(title);
		a.setCategory(sectionMapper.getBySectionId(u.getSection_id()).getCategory());
		articleMapper.insert(a);

		return wrapperMsg(200, "success");
	}

	@RequestMapping(value = "/delete", method = { RequestMethod.POST })
	public String publish(@RequestParam(value = "article_id") int article_id,
	                      HttpServletRequest request) {
		int uid = 0;
		try {
			uid = (int) request.getSession().getAttribute("user_id");
		} catch (Exception e){
			e.printStackTrace();
			return wrapperMsg(404, "please log in");
		}
		User u = userMapper.getById(uid);
		if(!u.getAdmin()){
			return wrapperMsg(404, "no authority");
		}
		try {
			articleMapper.delete(article_id);
		}
		catch (Exception e){
			e.printStackTrace();
			return wrapperMsg(404, "article not exist");
		}
		return wrapperMsg(200, "success");
	}

	@RequestMapping(value = "/favour", method = { RequestMethod.POST })
	public String favour(@RequestParam(value = "article_id") int article_id,
	                         HttpServletRequest request) {
		int uid = 0;
		try {
			uid = (int) request.getSession().getAttribute("user_id");
		} catch (Exception e){
			e.printStackTrace();
			return wrapperMsg(404, "please log in");
		}
		Favourite f = new Favourite();
		f.setArticle_id(article_id);
		f.setUser_id(uid);
		favouriteMapper.insert(f);

		articleMapper.updateFavCnt(article_id, 1);
		return wrapperMsg(200, "success");
	}

	@RequestMapping(value = "/disfavour", method = { RequestMethod.POST })
	public String disfavour(@RequestParam(value = "article_id") int article_id,
	                     HttpServletRequest request) {
		int uid = 0;
		try {
			uid = (int) request.getSession().getAttribute("user_id");
		} catch (Exception e){
			e.printStackTrace();
			return wrapperMsg(404, "please log in");
		}

		favouriteMapper.delete(uid, article_id);

		articleMapper.updateFavCnt(article_id, -1);
		return wrapperMsg(200, "success");
	}

	@RequestMapping(value = "/like", method = { RequestMethod.POST })
	public String like(@RequestParam(value = "article_id") int article_id,
	                     HttpServletRequest request) {
		int uid = 0;
		try {
			uid = (int) request.getSession().getAttribute("user_id");
		} catch (Exception e){
			e.printStackTrace();
			return wrapperMsg(404, "please log in");
		}
		Likes l = new Likes();
		l.setArticle_id(article_id);
		l.setUser_id(uid);
		likeMapper.insert(l);

		articleMapper.updateLikeCnt(article_id, 1);
		return wrapperMsg(200, "success");
	}

	@RequestMapping(value = "/dislike", method = { RequestMethod.POST })
	public String dislike(@RequestParam(value = "article_id") int article_id,
	                        HttpServletRequest request) {
		int uid = 0;
		try {
			uid = (int) request.getSession().getAttribute("user_id");
		} catch (Exception e){
			e.printStackTrace();
			return wrapperMsg(404, "please log in");
		}

		likeMapper.delete(uid, article_id);
		articleMapper.updateLikeCnt(article_id, -1);
		return wrapperMsg(200, "success");
	}



}