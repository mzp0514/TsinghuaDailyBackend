package com.mobilecourse.backend.model;

import java.sql.Timestamp;

public class Article {
	private int article_id;
	private int section_id;
	private String title;
	private String author_name;
	private Timestamp publish_time;
	private String content;
	private int view;
	private String reader; //'All', 'Staff', 'Member', 'MemberStaff'

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

	public int getView() {
		return view;
	}

	public void setView(int view) {
		this.view = view;
	}

	public String getReader() {
		return reader;
	}

	public void setReader(String reader) {
		this.reader = reader;
	}
}
