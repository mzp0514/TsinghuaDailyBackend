package com.mobilecourse.backend.dao;
import com.mobilecourse.backend.model.Likes;

import java.util.List;

public interface LikesDao {
	void insert(Likes f);

	void delete(int user_id, int article_id);

	Likes get(int user_id, int article_id);

	List<Likes> select(int user_id);
}
