package com.mobilecourse.backend.dao;

import com.mobilecourse.backend.model.Follow;

import java.util.List;

public interface FollowDao {

	void insert(Follow f);

	void delete(Follow f);

	List<Follow> selectByUserId(int user_id);

	Follow get(int user_id, int section_id);
}
