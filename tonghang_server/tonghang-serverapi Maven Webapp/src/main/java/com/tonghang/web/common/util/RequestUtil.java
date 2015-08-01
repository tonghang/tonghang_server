package com.tonghang.web.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.tonghang.web.user.pojo.User;


public class RequestUtil {

	/**
	 * 请求流中读取JSON字符串
	 * @param request
	 * @return
	 */
	public static String readRequest(HttpServletRequest request){
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader bufi = request.getReader();
			String line = null;
			while((line=bufi.readLine())!=null){
				sb.append(line);
			}
			return sb.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}	
	}
	
	/**
	 * 从请求中读取用户的头像
	 * @param request
	 * @param user
	 * @param picture
	 */
	public static void UserImageReceiver(HttpServletRequest request,String client_id, CommonsMultipartFile picture){
		if(picture!=null){
			String pictureRealPathDir = request.getSession().getServletContext().getRealPath("image");
			String fileName =pictureRealPathDir+File.separator+client_id+File.separator+Constant.IMAGE_NAME;              
			try {
				File f = new File(fileName);
				File folder = new File(pictureRealPathDir+File.separator+client_id);
				if(!folder.exists())
					folder.mkdirs();
				picture.getFileItem().write(f);
				LogUtil.printLog(f.getAbsolutePath());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
