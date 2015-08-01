package com.tonghang.web.topic.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tonghang.web.common.dao.LabelDao;
import com.tonghang.web.common.pojo.Label;
import com.tonghang.web.common.util.CommonMapUtil;
import com.tonghang.web.common.util.HuanXinUtil;
import com.tonghang.web.topic.dao.TopicDao;
import com.tonghang.web.topic.pojo.Topic;
import com.tonghang.web.topic.util.TopicUtil;
import com.tonghang.web.user.dao.UserDao;
import com.tonghang.web.user.pojo.User;
import com.tonghang.web.user.util.UserUtil;

@Service("topicService")
public class TopicService {

	@Resource(name="topicDao")
	private TopicDao topicDao;
	@Resource(name="labelDao")
	private LabelDao labelDao;
	@Resource(name="userDao")
	private UserDao userDao;
	@Resource(name="userUtil")
	private UserUtil userUtil;
	
	public LabelDao getLabelDao() {
		return labelDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public TopicDao getTopicDao() {
		return topicDao;
	}

	/**
	 * 根据关键字查找话题(调试通过)
	 * 
	 * @param subject 前端请求中的参数 表示话题关键字
	 * @param nowpage 前端请求中的参数 表示当前页数 做分页用
	 * @return
	 */
	public Map<String,Object> checkTopicBySubject(String subject/*,String etag*/,int nowpage){
		List<Topic> topics = topicDao.findTopicBySubject(subject, nowpage);
		return TopicUtil.topicsToMapConvertor(topics/*,etag*/);
	}
	
	/**
	 * 根据标签（行业）查找话题(调试通过)
	 * @param labelname 前端请求中的参数	表示标签
	 * @param nowpage	  前端请求中的参数	表示当前页数 做分页用
	 * @return
	 */
	public Map<String,Object> checkTopicByLabel(String labelname/*,String etag*/,int nowpage){
		List<Topic> topics = topicDao.findTopicByLabelName(labelname, nowpage);
		return TopicUtil.topicsToMapConvertor(topics/*,etag*/);
	}
	
	/**
	 * 首页话题推荐(调试通过)
	 * @param user controller中 根据前端请求client_id构造的User对象	表示当前使用用户
	 * @param nowpage 前端请求中的参数	 表示当前页数 做分页用
	 * @return
	 */
	public Map<String,Object> recommendTopics(User user,/*String etag,*/int nowpage){
		List<Label> labels = labelDao.findLabelByUser(user);
		List<Topic> topics = new ArrayList<Topic>();
		for(Label label : labels){
			topics.addAll(topicDao.findTopicByLabelName(label.getLabel_name(), nowpage));
		}
		return TopicUtil.topicsToMapConvertor(topics/*,etag*/);
	}
	
	/**
	 * 添加话题
	 * @param client_id	 前端请求中的参数	表示用户唯一标识client_id
	 * @param subject	前端请求中的参数	表示每个话题必填的subject
	 * @param label_name
	 * @return
	 */
	@Transactional
	public Map<String,Object> addTopic(String client_id,String subject,String label_name){
		Map<String,Object> result = new HashMap<String, Object>();
		User user = userDao.findUserById(client_id);
		Label label = new Label();
		label.setLabel_name(label_name);
		Topic topic = new Topic();
		topic.setSubject(subject);
		System.out.println("addTopic -- client_id:"+client_id);
		topic.setHuanxin_group_id(HuanXinUtil.createGroup(subject, client_id));
		topic.setLabel(label);
		topic.setUser(user);
		topicDao.save(topic);
		Map<String,Object> message = CommonMapUtil.baseMsgToMapConvertor();
		message.put("topics", TopicUtil.topicToMapConvertor(topic, user));
		result.put("success", message);
		return result;
	}
	
	/**
	 * 查看某用户的话题
	 * @param client_id 前端请求中的参数	表示用户唯一标识client_id
	 * @param nowpage 前端请求中的参数	 表示当前页数 做分页用
	 * @return
	 */
	public Map<String,Object> checkTopicInUser(String client_id/*,String etag*/,int nowpage){
		List<Topic> topics = topicDao.findTopicByUserId(client_id, nowpage);
		return TopicUtil.topicsToMapConvertor(topics);
	}
	
	/**
	 * 查看话题中的成员
	 * @param topic_id	前端请求中的参数	表示话题的唯一标识huanxin_group_id
	 * @param nowpage	前端请求中的参数	 表示当前页数 做分页用
	 * @return
	 */
	public Map<String,Object> checkTopicMember(String client_id,String topic_id, /*String etag,*/int nowpage){
		List<User> users = topicDao.checkMemberFromTopic(topic_id, nowpage);
		System.out.println(users.size());
		return userUtil.usersToMapConvertor(users,client_id);
	}

	/**
	 * * 用户加入或退出话题
	 * @param huanxin_group_id	前端请求中的参数	表示话题的唯一标识huanxin_group_id
	 * @param client_id	前端请求中的参数	表示用户的唯一标识client_id
	 * @param isJoin	判断用户加入话题还是退出话题
	 * @return
	 */
	public Map<String,Object> joinOrLeaveTopic(String huanxin_group_id,String client_id,boolean isJoin){
		User user = new User();
		Topic topic = new Topic();
		Map<String,Object> result = new HashMap<String, Object>();
		Map<String,Object> topicmap = new HashMap<String, Object>();
		topic.setHuanxin_group_id(huanxin_group_id);
		user.setClient_id(client_id);
		if(isJoin){
			topicDao.userLeaveTopic(user);
			topicDao.userJoinTopic(user, topic);	
		}else 
			topicDao.userLeaveTopic(user);
		topic = topicDao.findTopicById(huanxin_group_id);
		user = userDao.findUserById(client_id);
		Map<String,Object> usermap = userUtil.userToMapConvertor(user,client_id);
		topicmap.putAll(TopicUtil.topicToMapConvertor(topic, user));
		topicmap.putAll(usermap);
		result.put("success", topicmap);
		return result;
	}
}
