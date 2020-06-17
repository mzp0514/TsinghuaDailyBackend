package com.mobilecourse.backend.model;

import com.alibaba.fastjson.annotation.JSONField;

import java.sql.Timestamp;
public class Message {
	private int sender_id;
	private int receiver_id;

	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Timestamp send_time;
	private String content;

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

	public Timestamp getSend_time() {
		return send_time;
	}

	public void setSend_time(Timestamp send_time) {
		this.send_time = send_time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
