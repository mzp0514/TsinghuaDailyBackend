package com.mobilecourse.backend.model;

import java.sql.Timestamp;

public class Comment {
	private int article_id;
	private int comment_id;
	private int user_id;
	private String content;
	private Timestamp add_time;

	public int getArticle_id() {
		return article_id;
	}

	public void setArticle_id(int article_id) {
		this.article_id = article_id;
	}

	public int getComment_id() {
		return comment_id;
	}

	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getAdd_time() {
		return add_time;
	}

	public void setAdd_time(Timestamp add_time) {
		this.add_time = add_time;
	}

	@Override
	public String toString() {
		return "Comment{" +
				"article_id=" + article_id +
				", comment_id=" + comment_id +
				", content='" + content + '\'' +
				", add_time=" + add_time +
				'}';
	}
}
