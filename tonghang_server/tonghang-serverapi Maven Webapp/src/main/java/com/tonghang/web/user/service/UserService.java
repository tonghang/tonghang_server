package com.tonghang.web.user.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tonghang.web.common.dao.LabelDao;
import com.tonghang.web.common.exception.BaseException;
import com.tonghang.web.common.exception.LoginException;
import com.tonghang.web.common.pojo.Label;
import com.tonghang.web.common.util.CommonMapUtil;
import com.tonghang.web.common.util.EmailUtil;
import com.tonghang.web.common.util.HuanXinUtil;
import com.tonghang.web.common.util.SecurityUtil;
import com.tonghang.web.common.util.StringUtil;
import com.tonghang.web.friend.dao.FriendDao;
import com.tonghang.web.topic.dao.TopicDao;
import com.tonghang.web.topic.pojo.Topic;
import com.tonghang.web.topic.util.TopicUtil;
import com.tonghang.web.user.dao.UserDao;
import com.tonghang.web.user.pojo.User;
import com.tonghang.web.user.util.UserUtil;

@Service("userService")
public class UserService {
	
	@Resource(name="userDao")
	private UserDao userDao;
	@Resource(name="labelDao")
	private LabelDao labelDao;
	@Resource(name="topicDao")
	private TopicDao topicDao;
	@Resource(name="friendDao")
	private FriendDao friendDao;
	
	@Resource(name="userUtil")
	private UserUtil userUtil;
	
	/**
	 * 用户登录
	 * @param 
	 * @return
	 * @throws Exception 
	 * 自己和自己肯定不是好友，调用带ignore参数的userToMapConvertor方法
	 */
	public Map<String,Object> login(String email,String password) throws BaseException{
		Map<String,Object> result = new HashMap<String, Object>();
		User user = userDao.findUserByEmail(email);
		if(user==null){
//			result.put("error", userUtil.messageToMapConvertor(0, "登录失败，用户不存在！"));
			throw new LoginException("登录失败，该用户不存在！");
		}else{
			if(user.getStatus().equals(0)){
//				result.put("error", userUtil.messageToMapConvertor(0, "登录失败，用户被封号！"));
				throw new LoginException("登录失败，用户被封号！");
			}else{
				if(user.getPassword().equals(password)){
					Map<String,Object> usermap = userUtil.userToMapConvertor(user,false);
					usermap.putAll(CommonMapUtil.baseMsgToMapConvertor());
					result.put("success", usermap);
				}else{
//					result.put("error", userUtil.messageToMapConvertor(0, "登录失败，密码不正确！"));
					throw new LoginException("登录失败，用户名或密码错误！");
				}
			}
		}
		return result;
	}
	
	/**
	 * 找回密码
	 * @param 
	 * @return
	 */
	public Map<String,Object> forgetPassword(String email){
		Map<String,Object> result = new HashMap<String, Object>();
		User user = userDao.findUserByEmail(email);
		if(user==null){
			result.put("error", userUtil.messageToMapConvertor(0, "发送失败，用户不存在！"));
		}else{
			user.setPassword(StringUtil.randomCode(8));
//调修改密码方法
			EmailUtil.sendEmail(user);
			userDao.save(user);
			result.put("success", userUtil.messageToMapConvertor(200, "密码重置请求成功!"));
		}
		return result;
	}
	/**
	 * 业务功能：用户注册
	 * @param user User对象(新注册的user对象)
	 * @return
	 * 自己和自己肯定不是好友，调用带ignore参数的userToMapConvertor方法
	 */
	public Map<String,Object> regist(User user){
		Map<String,Object> result = new HashMap<String, Object>();
		Iterator<Label> it = user.getLabellist().iterator();
		while(it.hasNext()){
			Label label = it.next();
			System.out.println("regist:"+label.getLabel_name());
			if(labelDao.findLabelById(label.getLabel_name())==null)
				labelDao.save(label);
		}
		if(userDao.findUserByEmail(user.getEmail())==null){
			user.setClient_id(SecurityUtil.getUUID());
			userDao.save(user);
			HuanXinUtil.registUser(user);
			Map<String,Object> usermap = userUtil.userToMapConvertor(user,false);
			usermap.putAll(CommonMapUtil.baseMsgToMapConvertor());
			result.put("success", usermap);
		}
		else{
			result.put("error", userUtil.messageToMapConvertor(0, "注册失败，邮箱已被注册！"));
		}
		return result;
	}
	/**
	 * 查看用户详细信息
	 * @param client_id	前台请求中的参数	表示用户的唯一标识	client_id
	 * @return
	 */
	public Map<String,Object> checkUserMessage(String client_id){
		User user =userDao.findUserById(client_id);
		Map<String,Object> usermap = userUtil.userToMapConvertor(user,client_id);
		Map<String,Object> result = new HashMap<String, Object>();
		usermap.putAll(CommonMapUtil.baseMsgToMapConvertor());
		result.put("success", usermap);
		return result;
	}

	/**
	 * 判断两个人是不是好友
	 * @param my
	 * @param friend
	 * @return
	 */
	public boolean isFriend(String my,String friend){
		return friendDao.isFriend(my, friend);
	}
	/**
	 * 首页推荐
	 * @param 
	 * @return
	 */
	public Map<String, Object> recommend(String client_id, int page) {
		List<User> users = new ArrayList<User>();
		System.out.println("recommend的当前页数："+page);
		User user = new User();
		user.setClient_id(client_id);
		System.out.println("recommend推荐人："+user);
		List<Label> labels = labelDao.findLabelByUser(user);
		System.out.println("recommend的标签："+labels.size());
		for(Label label : labels){
			List<User> us = userDao.findUserByLabel(label.getLabel_name(), page);
			System.out.println("标签 "+label.getLabel_name()+" 对应的用户："+us);
			if(us.contains(user)){
				us.remove(user);
			}
			users.addAll(us);
		}
		return userUtil.usersToMapConvertor(users,client_id);
	}

	public Map<String, Object> searchLabel(String client_id,String label_name, int page) {
		// TODO Auto-generated method stub
		List<Label> labels = labelDao.findLabelByName(label_name);
		List<User> users = new ArrayList<User>();
		for(Label label : labels){
			users.addAll(userDao.findUserByLabel(label.getLabel_name(), page));
		}
		return userUtil.usersToMapConvertor(users,client_id);
	}

	public Map<String, Object> searchNick(String client_id,String username, int page) {
		// TODO Auto-generated method stub
		List<User> users = userDao.findUserByUsername(username, page);
		return userUtil.usersToMapConvertor(users,client_id);
	}

	public Map<String, Object> userMessage(String client_id) {
		// TODO Auto-generated method stub
		Map<String,Object> result = new HashMap<String, Object>();
		User user = userDao.findUserById(client_id);
		System.out.println("查看某用户详细信息："+user.getLabellist());
		System.out.println("查看某用户详细信息："+user);
		Map<String,Object> usermap = userUtil.userToMapConvertor(user,client_id);
		usermap.putAll(CommonMapUtil.baseMsgToMapConvertor());
		result.put("success", usermap);
		return result;
	}

	/**
	 * 现在所有的error都放到success中，因为前台无法区分sucess和error
	 * @param client_id
	 * @param username
	 * @param sex
	 * @param birth
	 * @param city
	 * @return
	 */
	public Map<String, Object> update(String client_id, String username,
			String sex, String birth, String city) {
		// TODO Auto-generated method stub
		Map<String,Object> result = new HashMap<String, Object>();
		User user = userDao.findUserById(client_id);
		System.out.println("新注册的用户的client_id:"+user);
		System.out.println("新注册的用户的client_id:"+client_id);
		if(user==null){
			result.put("success", userUtil.messageToMapConvertor(0, "该用户不存在！"));
			return result;
		}
		if(!birth.equals(user.getBirth()))
			user.setBirth(birth);
		if(!city.equals(user.getCity()))
			user.setCity(city);
		if(!sex.equals(user.getSex()))
			user.setSex(sex);
		if(username!=null&&!username.equals(user.getUsername())){
			user.setUsername(username);
			HuanXinUtil.changeUsername(user.getUsername(),user.getClient_id());
		}
		userDao.saveOrUpdate(user);
		Map<String,Object> usermap = userUtil.userToMapConvertor(user,client_id);
		usermap.putAll(CommonMapUtil.baseMsgToMapConvertor());
		result.put("success", usermap);
		return result;
	}

	public Map<String, Object> updatePassword(String client_id,String old_passwd, String new_passwd) {
		// TODO Auto-generated method stub
		Map<String,Object> result = new HashMap<String, Object>();
		User user = new User();
		user.setClient_id(client_id);
		user = userDao.findUserById(client_id);
		if(user.getPassword().equals(old_passwd)){
			user.setPassword(new_passwd);
			HuanXinUtil.changePassword(user.getPassword(), user.getClient_id());
			userDao.save(user);
			user = userDao.findUserById(client_id);
			Map<String,Object> usermap = userUtil.userToMapConvertor(user,client_id);
			usermap.putAll(CommonMapUtil.baseMsgToMapConvertor());
			result.put("success", usermap);
		}else 
			result.put("success", userUtil.messageToMapConvertor(0, "修改失败，原密码不正确！"));
		return result;
	}

	public Map<String, Object> updateLabel(String client_id, List<String> list) {
		// TODO Auto-generated method stub
		Map<String,Object> result = new HashMap<String, Object>();
		User user = userDao.findUserById(client_id);
		System.out.println("修改人的ID:"+user);
		List<Label> labels = new ArrayList<Label>();
		userDao.deleteAllLabel(client_id);
		for(String label_name : list){
			Label label = labelDao.findLabelById(label_name);
			if(label==null){
				label = new Label();
				label.setLabel_name(label_name);
				labelDao.save(label);				
			}
			labels.add(label);
		}
		Set<Label> labellist = new HashSet<Label>();
		labellist.addAll(labels);
		user.setLabellist(labellist);
		userDao.saveOrUpdate(user);
		Map<String,Object> usermap = userUtil.userToMapConvertor(user,client_id);
		usermap.putAll(CommonMapUtil.baseMsgToMapConvertor());
		result.put("success", usermap);
		return result;
	}

	public Map<String, Object> userTopic(String client_id, int page) {
		// TODO Auto-generated method stub
		List<Topic> topics = topicDao.findTopicByUserId(client_id, page);
		return TopicUtil.topicsToMapConvertor(topics);
	}
	
}
