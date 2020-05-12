package com.mobilecourse.backend.dao;

import com.mobilecourse.backend.model.Article;

import java.util.List;

public interface ArticleDao {

	List<Article> selectBySectionId(int section_id);

	List<Article> selectByCategory(String category);

	Article getById(int article_id);

	void insert(Article a);

	void delete(int article_id);

	void updateViewCnt(int article_id, int update_num);

	void updateFavCnt(int article_id, int update_num);
}
