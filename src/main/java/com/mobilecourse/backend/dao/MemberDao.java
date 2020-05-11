package com.mobilecourse.backend.dao;

import com.mobilecourse.backend.model.Member;

public interface MemberDao {

	void insert(Member f);

	Member get(int user_id, int section_id);
}
