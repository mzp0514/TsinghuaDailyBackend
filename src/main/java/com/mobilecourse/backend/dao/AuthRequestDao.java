package com.mobilecourse.backend.dao;

import com.mobilecourse.backend.model.AuthRequest;

import java.util.List;

public interface AuthRequestDao {

	void insert(AuthRequest u);

	List<AuthRequest> get(int receiver_id);

	AuthRequest getById(int request_id);

	void delete(int request_id);

}
