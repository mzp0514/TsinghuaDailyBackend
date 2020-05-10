package com.mobilecourse.backend.dao;

import com.mobilecourse.backend.model.Test;
import com.mobilecourse.backend.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserDao {

	void insert(User u);

	User get(String username);

}
