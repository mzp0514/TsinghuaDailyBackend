package com.mobilecourse.backend.controllers;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mobilecourse.backend.dao.*;
import com.mobilecourse.backend.model.*;
import net.sf.json.JSONObject;
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

import net.sf.json.JSONArray;

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


	@RequestMapping(value = "/cateory-articles", method = { RequestMethod.GET })
	public String getCategoryArticles(@RequestParam(value = "category") String category,
	                                  @RequestParam(value = "page_num") int page_num,
	                                  @RequestParam(value = "page_size", defaultValue = "10") int page_size,
	                                  HttpServletRequest request) {
		int uid = (int) request.getSession().getAttribute("user_id");
		User u = userMapper.getById(uid);
		PageHelper.startPage(page_num, page_size, "article_id desc");
		List<Article> articles = articleMapper.selectByCategory(category, uid,
				u.getSection_id(), u.getType().equals("Staff"));
		PageInfo pageInfo = new PageInfo(articles);
		JSONArray js = JSONArray.fromObject(pageInfo.getList());
		for(int i = 0; i < js.size(); i++){
			js.getJSONObject(i).discard("content");
		}
		JSONObject wrapperMsg = new JSONObject();
		wrapperMsg.put("code", 200);
		wrapperMsg.put("articles", js);
		return wrapperMsg.toString();
	}

	@RequestMapping(value = "/section-articles", method = { RequestMethod.GET })
	public String getSectionArticles(@RequestParam(value = "section_id") int section_id,
	                                 @RequestParam(value = "page_num") int page_num,
	                                 @RequestParam(value = "page_size", defaultValue = "10") int page_size,
	                          HttpServletRequest request) {
		int uid = (int) request.getSession().getAttribute("user_id");
		User u = userMapper.getById(uid);
		System.out.println(page_num);
		PageHelper.startPage(page_num, page_size, "article_id desc");
		List<Article> articles = articleMapper.selectBySectionId(section_id, u.getSection_id(),
																u.getType().equals("Staff"));
		System.out.println(articles.toString());
		PageInfo pageInfo = new PageInfo(articles);
		JSONArray js = JSONArray.fromObject(pageInfo.getList());
		for(int i = 0; i < js.size(); i++){
			js.getJSONObject(i).discard("content");
		}
		JSONObject wrapperMsg = new JSONObject();
		wrapperMsg.put("code", 200);
		wrapperMsg.put("articles", js);
		return wrapperMsg.toString();
	}

	@RequestMapping(value = "/article", method = { RequestMethod.GET })
	public String getArticle(@RequestParam(value = "article_id") int article_id,
	                                 HttpServletRequest request) {
		try {
			int uid = (int) request.getSession().getAttribute("user_id");
			Article a = articleMapper.getById(article_id);

			articleMapper.updateViewCnt(article_id, 1);

			Favourite f = favouriteMapper.get(uid, article_id);
			Boolean fav = (f != null);

			JSONObject wrapperMsg = new JSONObject();
			wrapperMsg.put("code", 200);
			wrapperMsg.put("info", JSONObject.fromObject(a));
			wrapperMsg.put("favoured", fav);
			return wrapperMsg.toString();
		}
		catch (Exception e){
			return wrapperMsg(404, "article not found");
		}
	}

	@RequestMapping(value = "/publish", method = { RequestMethod.POST })
	public String publish(@RequestParam(value = "title") String title,
	                         @RequestParam(value = "author") String author,
	                         @RequestParam(value = "content") String content,
	                         @RequestParam(value = "reader") String reader,
	                         HttpServletRequest request) {
		int uid = (int) request.getSession().getAttribute("user_id");
		User u = userMapper.getById(uid);
		if(!u.getAdmin()){
			return wrapperMsg(404, "no authority");
		}

		Article a = new Article();
		a.setAuthor_name(author);
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
		int uid = (int) request.getSession().getAttribute("user_id");
		User u = userMapper.getById(uid);
		if(!u.getAdmin()){
			return wrapperMsg(404, "no authority");
		}
		articleMapper.delete(article_id);
		return wrapperMsg(200, "success");
	}

	@RequestMapping(value = "/favour", method = { RequestMethod.POST })
	public String favour(@RequestParam(value = "article_id") int article_id,
	                         HttpServletRequest request) {
		int uid = (int) request.getSession().getAttribute("user_id");
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
		int uid = (int) request.getSession().getAttribute("user_id");

		favouriteMapper.delete(uid, article_id);

		articleMapper.updateFavCnt(article_id, -1);
		return wrapperMsg(200, "success");
	}

}