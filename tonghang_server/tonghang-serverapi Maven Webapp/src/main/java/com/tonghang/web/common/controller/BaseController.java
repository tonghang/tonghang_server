package com.tonghang.web.common.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.tonghang.web.common.exception.BaseException;

@Controller
public class BaseController {

	/**
	 * 处理所有controller触发的异常并把异常信息和异常码返回给客户端
	 * @param e
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String,Object>> exception(Exception e){
		Map<String,Object> result = new HashMap<String, Object>();
		Map<String,Object> map = new HashMap<String, Object>();
		if(e instanceof BaseException){
			result.put("message", e.getMessage());
			result.put("code", ((BaseException)e).getCode());
			e.printStackTrace();
		}else{
			result.put("code", 500);
			result.put("message", "server exception");
			e.printStackTrace();
		}
		map.put("suceess", result);
		return new ResponseEntity<Map<String,Object>>(map,HttpStatus.OK);
	}
}
