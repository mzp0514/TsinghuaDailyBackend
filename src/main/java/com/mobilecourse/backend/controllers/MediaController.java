package com.mobilecourse.backend.controllers;


import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.net.FileNameMap;
import java.net.HttpURLConnection;
import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;


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
			String uid = (String) request.getSession().getAttribute("user_id");
			String path = "./media" + sep + uid;
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

	@RequestMapping(value = "/get",method = RequestMethod.GET)
	@ResponseBody
	public HttpServletResponse getImage(@RequestParam(value = "filename") String filename,
	                                    HttpServletRequest request, HttpServletResponse response) {
		try {
			String sep = System.getProperty("file.separator");
			String uid = (String) request.getSession().getAttribute("user_id");
			String url= "./media" + sep + uid + sep + filename;
			File file = new File(url);
			FileInputStream fis = new FileInputStream(file);
			response.setContentType(URLConnection.guessContentTypeFromName(filename));
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.addHeader("Content-Length", "" + file.length());
			OutputStream out = response.getOutputStream();
			IOUtils.copy(fis, out);
			out.flush();
			fis.close();
			return null;
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}

	}

}
