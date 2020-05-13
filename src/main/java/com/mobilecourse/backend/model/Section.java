package com.mobilecourse.backend.model;

public class Section {
	private int section_id;
	private String section_name;
	private String category;
	private int follower_cnt;
	private int article_cnt;

	public int getSection_id() {
		return section_id;
	}

	public void setSection_id(int section_id) {
		this.section_id = section_id;
	}

	public String getSection_name() {
		return section_name;
	}

	public void setSection_name(String section_name) {
		this.section_name = section_name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}


	@Override
	public String toString() {
		return "Section{" +
				"section_id=" + section_id +
				", section_name='" + section_name + '\'' +
				", follower_cnt=" + follower_cnt +
				", article_cnt=" + article_cnt +
				'}';
	}

	public int getFollower_cnt() {
		return follower_cnt;
	}

	public void setFollower_cnt(int follower_cnt) {
		this.follower_cnt = follower_cnt;
	}

	public int getArticle_cnt() {
		return article_cnt;
	}

	public void setArticle_cnt(int article_cnt) {
		this.article_cnt = article_cnt;
	}
}
