package com.mobilecourse.backend.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.sql.Timestamp;

public class Article implements Comparable<Article> {
	private int article_id;
	private int section_id;
	private String title;
	private String author_name;
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Timestamp publish_time;
	private String content;
	private int view_num;
	private int fav_num;
	private int like_num;
	private String reader; //'All', 'Staff', 'Member', 'MemberStaff'
	private String category;


	public int getArticle_id() {
		return article_id;
	}

	public void setArticle_id(int article_id) {
		this.article_id = article_id;
	}

	public int getSection_id() {
		return section_id;
	}

	public void setSection_id(int section_id) {
		this.section_id = section_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor_name() {
		return author_name;
	}

	public void setAuthor_name(String author_name) {
		this.author_name = author_name;
	}

	public Timestamp getPublish_time() {
		return publish_time;
	}

	public void setPublish_time(Timestamp publish_time) {
		this.publish_time = publish_time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getReader() {
		return reader;
	}

	public void setReader(String reader) {
		this.reader = reader;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}


	@Override
	public int compareTo(Article a) {
		return this.publish_time.compareTo(a.getPublish_time());
	}

	public int getView_num() {
		return view_num;
	}

	public void setView_num(int view_num) {
		this.view_num = view_num;
	}

	public int getFav_num() {
		return fav_num;
	}

	public void setFav_num(int fav_num) {
		this.fav_num = fav_num;
	}

	public int getLike_num() {
		return like_num;
	}

	public void setLike_num(int like_num) {
		this.like_num = like_num;
	}

	@Override
	public String toString() {
		return "Article{" +
				"article_id=" + article_id +
				", section_id=" + section_id +
				", title='" + title + '\'' +
				", author_name='" + author_name + '\'' +
				", publish_time=" + publish_time +
				", view_num=" + view_num +
				", fav_num=" + fav_num +
				", like_num=" + like_num +
				", reader='" + reader + '\'' +
				'}';
	}
}
