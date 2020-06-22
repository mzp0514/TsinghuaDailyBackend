package com.mobilecourse.backend.dao;

import com.mobilecourse.backend.model.Article;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.List;

public interface ArticleDao {

	List<Article> selectBySectionId(int section_id, int self_section_id, boolean is_staff);

	List<Article> selectByCategory(String category, int user_id, int section_id, boolean is_staff);

	Article getById(int article_id);

	void insert(Article a);

	void delete(int article_id);

	void updateViewCnt(int article_id, int update_num);

	void updateFavCnt(int article_id, int update_num);

	void updateLikeCnt(int article_id, int update_num);

	List<Article> search(String query, int self_section_id, boolean is_staff);
}
