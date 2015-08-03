package com.tonghang.web.user.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.tonghang.web.common.util.CommonMapUtil;
import com.tonghang.web.common.util.Constant;
import com.tonghang.web.common.util.TimeUtil;
import com.tonghang.web.label.pojo.Label;
import com.tonghang.web.user.pojo.User;
import com.tonghang.web.user.service.UserService;

@Component("userUtil")
public class UserUtil {

	@Resource(name="userService")
	private UserService userService;
	
	/**
	 * 该包装方式目前已废弃
	 * @param users
	 * @return
	 */
	@Deprecated
	public Map<String,Object> usersToMapConvertor(List<User> users){
		List<Map<String,Object>> usersmsg = new ArrayList<Map<String,Object>>();
		Map<String,Object> usermap = CommonMapUtil.baseMsgToMapConvertor();
		Map<String,Object> result = new HashMap<String, Object>();
		for(User u:users){
			List<String> labels = new ArrayList<String>();
			Map<String,Object> msg = new HashMap<String, Object>();
			for(Label l:u.getLabellist()){
				labels.add(l.getLabel_name());
			}
			msg.put("labels", labels);
			msg.put("email", u.getEmail());
			msg.put("username", u.getUsername());
			msg.put("sex", u.getSex());
			msg.put("phone", u.getPhone());
			msg.put("city", u.getCity());
			msg.put("client_id", u.getClient_id());
			msg.put("image", Constant.IMAGE_PATH+u.getClient_id()+"/"+Constant.IMAGE_NAME);
			msg.put("created_at", TimeUtil.getFormatString(u.getCreated_at()));
			msg.put("birth", u.getBirth());
			usersmsg.add(msg);
		}
		
		usermap.put("users", usersmsg);
		result.put("success", usermap);
		return result;
	}
	
	/**
	 * 
	 * @param users(要包装的目标对象List)
	 * @param client_id(当前用户的client_id,用来判断List中每一个用户对象和当前用户是不是好友关系，关系用is_friend来表示)
	 * @return
	 */
	public Map<String,Object> usersToMapConvertor(List<User> users,String client_id){
		List<Map<String,Object>> usersmsg = new ArrayList<Map<String,Object>>();
		Map<String,Object> usermap = CommonMapUtil.baseMsgToMapConvertor();
		Map<String,Object> result = new HashMap<String, Object>();
		for(User u:users){
			List<String> labels = new ArrayList<String>();
			Map<String,Object> msg = new HashMap<String, Object>();
			for(Label l:u.getLabellist()){
				labels.add(l.getLabel_name());
			}
			msg.put("labels", labels);
			msg.put("email", u.getEmail());
			msg.put("username", u.getUsername());
			msg.put("sex", u.getSex());
			msg.put("phone", u.getPhone());
			msg.put("city", u.getCity());
			msg.put("client_id", u.getClient_id());
			msg.put("image", Constant.IMAGE_PATH+u.getClient_id()+"/"+Constant.IMAGE_NAME);
			msg.put("created_at", u.getCreated_at());
			msg.put("birth", u.getBirth());
			msg.put("is_friend", userService.isFriend(client_id, u.getClient_id()));
			System.out.println("usersToMapConvertor: "+usermap.get("pic_server")+Constant.IMAGE_PATH+u.getClient_id()+"/"+Constant.IMAGE_NAME);
			usersmsg.add(msg);
		}
		
		usermap.put("users", usersmsg);
		result.put("success", usermap);
		return result;
	}
	
	public Map<String,Object> userToMapConvertor(User user,String client_id){
		List<String> labels = new ArrayList<String>();
		Map<String,Object> msg = new HashMap<String, Object>();
		Map<String,Object> usermap = new HashMap<String, Object>();
		for(Label l:user.getLabellist()){
			labels.add(l.getLabel_name());
		}
		msg.put("labels", labels);
		msg.put("email", user.getEmail());
		msg.put("sex", user.getSex());
		msg.put("username", user.getUsername());
		msg.put("phone", user.getPhone());
		msg.put("city", user.getCity());
		msg.put("client_id", user.getClient_id());
		msg.put("image", Constant.IMAGE_PATH+user.getClient_id()+"/"+Constant.IMAGE_NAME);
		msg.put("created_at", user.getCreated_at());
		msg.put("birth", user.getBirth());
		msg.put("is_friend", userService.isFriend(client_id, user.getClient_id()));
		usermap.put("user", msg);
		return usermap;
	}
	/**
	 * 重载userToMapConvertor方法，ignore表示忽略好友关系（因为有可能已知对方肯定是或者不是好友关系）
	 * @param user
	 * @param ignore
	 * @return
	 */
	public Map<String,Object> userToMapConvertor(User user,boolean ignore){
		List<String> labels = new ArrayList<String>();
		Map<String,Object> msg = new HashMap<String, Object>();
		Map<String,Object> usermap = new HashMap<String, Object>();
		for(Label l:user.getLabellist()){
			labels.add(l.getLabel_name());
		}
		msg.put("labels", labels);
		msg.put("email", user.getEmail());
		msg.put("sex", user.getSex());
		msg.put("username", user.getUsername());
		msg.put("phone", user.getPhone());
		msg.put("city", user.getCity());
		msg.put("client_id", user.getClient_id());
		msg.put("image", Constant.IMAGE_PATH+user.getClient_id()+"/"+Constant.IMAGE_NAME);
		msg.put("created_at", user.getCreated_at());
		msg.put("birth", user.getBirth());
		msg.put("is_friend", ignore);
		usermap.put("user", msg);
		return usermap;
	}
	
	public Map<String,Object> messageToMapConvertor(int code,String message){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("code",code);
		map.put("message",message);
		return map;
	}
}
