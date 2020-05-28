package com.mobilecourse.backend.dao;


import com.mobilecourse.backend.model.Message;

import java.util.List;

public interface MessageDao {
	void insert(Message m);

	List<Message> select(int user_id);
}
