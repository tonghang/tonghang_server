package com.tonghang.web.common.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tonghang.web.common.dao.LabelDao;
import com.tonghang.web.common.pojo.Label;
import com.tonghang.web.topic.dao.TopicDao;
import com.tonghang.web.topic.pojo.Topic;
import com.tonghang.web.topic.service.TopicService;
import com.tonghang.web.user.pojo.User;

/**
 * 测试用controller
 * @author Administrator
 *
 */
@RestController("labelController1")
public class TestController {

//	private LabelDao labelDao;
//	private TopicDao topicDao;
//	private TopicService topicService;
//	@Resource(name="topicService")
//	public void setTopicService(TopicService topicService) {
//		this.topicService = topicService;
//	}
//	@Resource(name="topicDao")
//	public void setTopicDao(TopicDao topicDao){
//		this.topicDao = topicDao;
//	}
//	@Resource(name="labelDao")
//	public void setLabelDao(LabelDao labelDao) {
//		this.labelDao = labelDao;
//	}
//	
//	@RequestMapping("label")
//	public ResponseEntity<List<Label>> findLabel(@RequestParam String labelname){
//		List<Label> labels = labelDao.findLabelByName(labelname);
//		return new ResponseEntity<List<Label>>(labels,HttpStatus.OK);
//	}
//	@RequestMapping("add")
//	public String addLabe(@RequestParam String labelname){
//		Label label = new Label();
//		label.setLabel_name(labelname);
//		label.setCreated_at(new Date());
//		label.setId(202);
//		labelDao.save(label);
//		return "SUCCESS";
//	}
//	@RequestMapping("find")
//	public ResponseEntity<List<Label>> findLabelByUser(@RequestParam String client_id){
//		User user = new User();
//		user.setClient_id(client_id);
//		List<Label> labels = labelDao.findLabelByUser(user);
//		System.out.println(labels.get(0).getLabel_name());
//		return new ResponseEntity<List<Label>>(labels,HttpStatus.OK);
//	}
//	
//	@RequestMapping("setTopic")
//	public String joinTopic(@RequestParam String client_id,@RequestParam String huanxin_group_id){
//		User user = new User();
//		Topic topic = new Topic();
//		topic.setHuanxin_group_id(huanxin_group_id);
//		user.setClient_id(client_id);
//		topicDao.userJoinTopic(user, topic);
//		return "SUCCESS";
//	}
//	@RequestMapping("leaveTopic")
//	public String leaveTopic(@RequestParam String client_id,@RequestParam String huanxin_group_id){
//		User user = new User();
//		Topic topic = new Topic();
//		topic.setHuanxin_group_id(huanxin_group_id);
//		user.setClient_id(client_id);
//		topicDao.userLeaveTopic(user, topic);
//		return "SUCCESS";
//	}
//	
//	@RequestMapping("addTopic")
//	public String addTopic(HttpServletRequest request,@RequestParam String client_id,@RequestParam String huanxin_group_id
//			,@RequestParam String subject,@RequestParam String label_name) throws Exception{
//		request.setCharacterEncoding("UTF-8");
//		User user = new User();
//		user.setClient_id(client_id);
//		Label label = new Label();
//		label.setLabel_name(label_name);
//		Topic topic = new Topic();
//		topic.setHuanxin_group_id(huanxin_group_id);
//		topic.setLabel(label);
//		topic.setUser(user);
//		topicDao.save(topic);
//		return "success";
//	}
//	
//	/**
//	 * 测试完成达，到基本数据的显示，目前缺省响应参数包括(etag,status)	2015-6-26
//	 * @param request
//	 * @param subject
//	 * @return
//	 * @throws Exception
//	 */
///*	@RequestMapping("bySubject")
//	public ResponseEntity<Map<String,Object>> findTopics(HttpServletRequest request,
//															@RequestParam String subject) throws Exception{
//		subject = new String(subject.getBytes("iso-8859-1"),"utf-8");
//		Map<String, Object> result = topicService.checkTopicBySubject(subject, 1);
//		return new ResponseEntity<Map<String, Object>>(result,HttpStatus.OK);
//	}*/
//	
//	/**
//	 * 测试完成达，到基本数据的显示，目前缺省响应参数包括(etag,status)	2015-6-26
//	 * @param request
//	 * @param labelname
//	 * @return
//	 * @throws Exception
//	 */
///*	@RequestMapping("byLabel")
//	public ResponseEntity<Map<String,Object>> findTopicsbyLabel(HttpServletRequest request,
//															@RequestParam String labelname) throws Exception{
//		labelname = new String(labelname.getBytes("iso-8859-1"),"utf-8");
//		Map<String, Object> result = topicService.checkTopicByLabel(labelname, 1);
//		return new ResponseEntity<Map<String,Object>>(result,HttpStatus.OK);
//	}*/
//	
//	/**
//	 * 测试完成达，到基本数据的显示，目前缺省响应参数包括(etag,status)	2015-6-26
//	 * @param request
//	 * @param client_id
//	 * @return
//	 * @throws Exception
//	 */
///*	@RequestMapping("byUser")
//	public ResponseEntity<Map<String,Object>> findTopicsbyUserl(HttpServletRequest request,
//															@RequestParam String client_id) throws Exception{
//		Map<String,Object> topics = topicService.checkTopicInUser(client_id,1);
//		return new ResponseEntity<Map<String,Object>>(topics,HttpStatus.OK);
//	}
//	*/
//	/**
//	 * 测试完成达，到基本数据的显示，目前缺省响应参数包括(etag,status)	2015-6-26
//	 * @param client_id
//	 * @return
//	 */
///*	@RequestMapping("recommend")
//	public ResponseEntity<Map<String,Object>> recommendTopic(@RequestParam String client_id){
//		User user = new User();
//		user.setClient_id(client_id);
//		Map<String,Object> result = topicService.recommendTopics(user,1);
//		return new ResponseEntity<Map<String,Object>>(result,HttpStatus.OK);
//	}*/
//	
///*	@RequestMapping("checkMember")
//	public ResponseEntity<Map<String,Object>> checkMember(@RequestParam String topic_id){
//		Topic topic = new Topic();
//		Map<String,Object> result = topicService.checkTopicMember(topic_id, 1);
//		return new ResponseEntity<Map<String,Object>>(result,HttpStatus.OK);
//	}*/
////	
////	@RequestMapping("getUserById")
////	public ResponseEntity<Map<String,Object>> findUserByLabel(@RequestParam String ){
////		
////	}
}
