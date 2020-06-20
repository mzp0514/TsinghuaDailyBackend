package com.mobilecourse.backend.model;

public class AuthRequest {
	private int request_id;
	private int sender_id;
	private int receiver_id;
	private String username;
	private String dept_name;
	private String id_num;
	private String user_type;//(type in ('Undergraduate', 'Graduate', 'Staff'))
	private String id_card;


	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getRequest_id() {
		return request_id;
	}

	public void setRequest_id(int request_id) {
		this.request_id = request_id;
	}

	public int getSender_id() {
		return sender_id;
	}

	public void setSender_id(int sender_id) {
		this.sender_id = sender_id;
	}

	public int getReceiver_id() {
		return receiver_id;
	}

	public void setReceiver_id(int receiver_id) {
		this.receiver_id = receiver_id;
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

	public String getId_card() {
		return id_card;
	}

	public void setId_card(String id_card) {
		this.id_card = id_card;
	}

	public String getId_num() {
		return id_num;
	}

	public void setId_num(String id_num) {
		this.id_num = id_num;
	}

	@Override
	public String toString() {
		return "AuthRequest [request_id=" + request_id + ", sender_id=" + sender_id +
				", receiver_id=" + receiver_id + ", dept_name=" + dept_name + ", id_num=" +
				id_num + ", user_type=" + user_type + ", id_card=" + id_card + "]";
	}
}
