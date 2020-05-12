package com.mobilecourse.backend.dao;


import com.mobilecourse.backend.model.Comment;

import java.util.List;

public interface CommentDao {

	void insert(Comment f);

	List<Comment> select(int article_id);

	void delete(int comment_id);
}
