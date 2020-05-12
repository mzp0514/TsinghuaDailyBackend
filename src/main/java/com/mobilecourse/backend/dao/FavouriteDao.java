package com.mobilecourse.backend.dao;

import com.mobilecourse.backend.model.Favourite;

import java.util.List;


public interface FavouriteDao {

	void insert(Favourite f);

	void delete(int user_id, int article_id);

	Favourite get(int user_id, int article_id);

	List<Favourite> select(int user_id);
}
