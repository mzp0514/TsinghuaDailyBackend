package com.mobilecourse.backend.dao;

import com.mobilecourse.backend.model.Test;
import com.mobilecourse.backend.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDao {

	void insert(User u);

	User getById(int user_id);

	User getByName(String username);

	User getByIdNum(String id_num);

	User getBySectionId(int section_id);

	User getAdminBySectionId(int section_id);

	void updateInfo(int user_id, String avatar, String status);

	void updateInfoAuth(int user_id, int verified, String dept_name,
	                    String user_type, String id_num, int section_id);

	void updateAuthStatus(int user_id, int verified);
}
