package com.mobilecourse.backend.controllers;


import com.mobilecourse.backend.dao.CommentDao;
import com.mobilecourse.backend.dao.MessageDao;
import com.mobilecourse.backend.model.Comment;
import com.mobilecourse.backend.model.Message;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@EnableAutoConfiguration

@RequestMapping("/message")
public class MessageController extends CommonController {
	@Autowired
	private MessageDao messageMapper;


	public String getMessages(int uid) {

		List<Message> messages = messageMapper.select(uid);
		JSONArray msg = JSONArray.fromObject(messages);
		JSONObject wrapperMsg = new JSONObject();
		wrapperMsg.put("code", 200);
		wrapperMsg.put("messages", msg);
		return wrapperMsg.toString();
	}


	public String addMessage(String message, int to, int uid) {

		Message m = new Message();
		m.setContent(message);
		m.setReceiver_id(to);
		m.setSender_id(uid);
		m.setSend_time(Timestamp.valueOf(LocalDateTime.now()));
		messageMapper.insert(m);

		return wrapperMsg(200, "success");
	}

}

