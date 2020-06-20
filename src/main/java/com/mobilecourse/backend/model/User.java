package com.mobilecourse.backend.model;

public class User {
	private int user_id;
	private String username;
	private String password;
	private String avatar;
	private String status;
	private int verified;
	private Boolean admin;
	private String dept_name;
	private String user_type; //('Undergraduate', 'Graduate', 'Staff'))
	private String id_num;
	private int section_id;

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getVerified() {
		return verified;
	}

	public void setVerified(int verified) {
		this.verified = verified;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}

	public String getDept_name() {
		return dept_name;
	}

	public void setDept_name(String dept_name) {
		this.dept_name = dept_name;
	}

	public String getType() {
		return user_type;
	}

	public void setType(String user_type) {
		this.user_type = user_type;
	}

	public String getId_num() {
		return id_num;
	}

	public void setId_num(String id_num) {
		this.id_num = id_num;
	}

	public int getSection_id() {
		return section_id;
	}

	public void setSection_id(int section_id) {
		this.section_id = section_id;
	}


	@Override
	public String toString() {
		return "User{" +
				"user_id=" + user_id +
				", username='" + username + '\'' +
				", avatar='" + avatar + '\'' +
				'}';
	}
}
