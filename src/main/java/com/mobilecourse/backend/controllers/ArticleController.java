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
	private FollowDao followMapper;

	@Autowired
	private MemberDao memberMapper;

	@Autowired
	private UserDao userMapper;

	@Autowired
	private SectionDao sectionMapper;

	private ArrayList<Article> filter(int user_id, int section_id, ArrayList<Article> articles){

		ArrayList<Article> res = new ArrayList<>();
		User u = userMapper.getById(user_id);
		Boolean isStaff = false;
		Boolean isMember = false;
		if(section_id != -1){
			Member m = memberMapper.get(user_id, section_id);
			if(m != null){
				isMember = true;
			}
		}
		if(u.getType().equals("Staff")){
			isStaff = true;
		}

		for(int i = 0; i < articles.size(); i++){
			Article a = articles.get(i);
			if(a.getReader().equals("All")){
				res.add(a);
			}
			else if(a.getReader().equals("AllStaff")){
				if(isStaff){
					res.add(a);
				}
			}
			else{
				if(section_id == -1) {
					Member m = memberMapper.get(user_id, section_id);
					if(m != null){
						isMember = true;
					}
				}
				if(a.getReader().equals("Member")){
					if(isMember){
						res.add(a);
					}
				}
				else if(a.getReader().equals("MemberStaff")){
					if(isMember && isStaff){
						res.add(a);
					}
				}
			}

		}

		return res;
	}

	@RequestMapping(value = "/cateory-articles", method = { RequestMethod.GET })
	public String getCategoryArticles(@RequestParam(value = "category") String category,
	                                  @RequestParam(value = "page_num") int page_num,
	                                  @RequestParam(value = "page_size", defaultValue = "10") int page_size,
	                                  HttpServletRequest request) {
		int uid = (int) request.getSession().getAttribute("user_id");
		ArrayList<Article> articles = new ArrayList<>();
		PageHelper.startPage(page_num, page_size);
		if(category.equals("follow")){

			List<Follow> follows = followMapper.selectByUserId(uid);

			for(int i = 0; i < follows.size(); i++){
				int section_id = follows.get(i).getSection_id();
				articles.addAll(filter(uid, section_id,
						(ArrayList<Article>) articleMapper.selectBySectionId(section_id)));
			}
			Collections.sort(articles);
		}
		else {
			articles = filter(uid, -1,
					(ArrayList<Article>) articleMapper.selectByCategory(category));
			Collections.reverse(articles);
		}
		PageInfo pageInfo = new PageInfo(articles);
		JSONArray js = JSONArray.fromObject(pageInfo.getList());
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
		PageHelper.startPage(page_num, page_size);
		ArrayList<Article> articles = (ArrayList<Article>) articleMapper.selectBySectionId(section_id);
		articles = filter(uid, section_id, articles);
		Collections.reverse(articles);
		PageInfo pageInfo = new PageInfo(articles);
		JSONArray js = JSONArray.fromObject(pageInfo.getList());
		JSONObject wrapperMsg = new JSONObject();
		wrapperMsg.put("code", 200);
		wrapperMsg.put("articles", js);
		return wrapperMsg.toString();
	}

	@RequestMapping(value = "/article", method = { RequestMethod.GET })
	public String getArticle(@RequestParam(value = "article_id") int article_id,
	                                 HttpServletRequest request) {
		Article a = articleMapper.getById(article_id);
		// TODO 浏览量、浏览记录

		JSONObject wrapperMsg = new JSONObject();
		wrapperMsg.put("code", 200);
		wrapperMsg.put("info", JSONObject.fromObject(a));
		wrapperMsg.put("content", a.getContent());
		return wrapperMsg.toString();
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

}