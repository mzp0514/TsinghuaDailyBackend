package com.mobilecourse.backend.controllers;

import com.mobilecourse.backend.WebSocketServer;
import com.mobilecourse.backend.dao.UserDao;
import com.mobilecourse.backend.model.Test;
import com.mobilecourse.backend.model.User;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Hashtable;
import java.util.List;

@RestController
@EnableAutoConfiguration

@RequestMapping("/media")
public class MediaController extends CommonController {

	private String getFileType(String filename){
		return filename.substring(filename.lastIndexOf(".") + 1);
	}

	@RequestMapping(value = "/upload", method = { RequestMethod.POST })
	public String upload(@RequestParam(value = "file") MultipartFile file,
	                     HttpServletRequest request) {

		try {
			InputStream in = file.getInputStream();

			String filename = System.currentTimeMillis() + "." + getFileType(file.getOriginalFilename());
			String sep = System.getProperty("file.separator");
			String username = (String) request.getSession().getAttribute("username");
			String path = "./media" + sep + username;
			File f = new File(path);
			if (!f.exists() && !f.isDirectory()) {
				f.mkdirs();
			}
			file.transferTo(Paths.get(path + sep + filename));
			return wrapperMsg(200, filename);
		}
		catch (Exception e){
			e.printStackTrace();
			return wrapperMsg(404, "upload failed");
		}

	}

	@RequestMapping(value = "/get-image",method = RequestMethod.GET)
	@ResponseBody
	public HttpServletResponse getImage(@RequestParam(value = "filename") String filename,
	                                    HttpServletRequest request, HttpServletResponse response) {
		try {
			String sep = System.getProperty("file.separator");
			String username = (String) request.getSession().getAttribute("username");
			String url= "./media" + sep + username + sep + filename;
			File file = new File(url);
			FileInputStream fis = new FileInputStream(file);
			//response.setContentType("multipart/form-data"); //设置返回的文件类型
			response.setContentType("image/" + getFileType(filename));
			response.setHeader("Access-Control-Allow-Origin", "*");//设置该图片允许跨域访问
			IOUtils.copy(fis, response.getOutputStream());
			// response.addHeader("Content-Length", "" + file.length());
			return response;
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}

	}

}
