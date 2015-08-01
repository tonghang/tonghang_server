package com.tonghang.web.topic.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tonghang.web.common.util.Constant;
import com.tonghang.web.topic.dao.TopicDao;
import com.tonghang.web.topic.pojo.Topic;
import com.tonghang.web.user.pojo.User;

@Repository("topicDao")
public class ITopicDao implements TopicDao{

	private SessionFactory sessionFactory;
	@Resource(name="sessionFactory")
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	/**
	 * 新建话题
	 */
	@Override
	@Transactional
	public void save(Topic topic) {
		// TODO Auto-generated method stub
		System.out.println(topic.getUsers());
		Session session = sessionFactory.getCurrentSession();
		System.out.println("开始插入"+topic.getLabel().getLabel_name());
		session.getTransaction().begin();
		session.save(topic);
		session.getTransaction().commit();
	}
	
	/**
	 * 通过标签名模糊查找话题
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Topic> findTopicByLabelName(String label_name,int nowpage) {
		// TODO Auto-generated method stub
		if(!sessionFactory.getCurrentSession().getTransaction().isActive()){
			sessionFactory.getCurrentSession().getTransaction().begin();
		}
		return (List<Topic>) sessionFactory.getCurrentSession().createQuery("from Topic as topic where topic.label.label_name like '%"+label_name+"%'")
													.setFirstResult(Constant.PAGESIZE*(nowpage-1)).setMaxResults(Constant.PAGESIZE).list();
	
	}

	/**
	 * 按照subject查找话题
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Topic> findTopicBySubject(String subject,int nowpage) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().getTransaction().begin();
		System.out.println(subject);
		return (List<Topic>) sessionFactory.getCurrentSession().createQuery("from Topic as topic where topic.subject like '%"+subject+"%'")
													.setFirstResult(Constant.PAGESIZE*(nowpage-1)).setMaxResults(Constant.PAGESIZE).list();
	}
	
	/**
	 * 查看指定用户创建的话题
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Topic> findTopicByUserId(String client_id,int nowpage) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().getTransaction().begin();
		return (List<Topic>) sessionFactory.getCurrentSession().createQuery("from Topic as topic where topic.user.client_id = :client_id")
				.setParameter("client_id", client_id).setFirstResult(Constant.PAGESIZE*(nowpage-1)).setMaxResults(Constant.PAGESIZE).list();
	}

	/**
	 * 用户加入话题
	 */
	@Override
	public void userJoinTopic(User user, Topic topic) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.getTransaction();
		if(!tx.isActive()){
			tx.begin();
		}
		Topic t =findTopicById(topic.getHuanxin_group_id());	
		user = (User) session.get(User.class, user.getClient_id());
		user.setTopic(t);
		session.save(user);
		tx.commit();
	}

	/**
	 * 
	 * 用户离开话题
	 */
	@Override
	public void userLeaveTopic(User user, Topic topic) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.getCurrentSession();
		Transaction tx = session.getTransaction();
		tx.begin();
		Topic t =findTopicById(topic.getHuanxin_group_id());	
		user = (User) session.get(User.class, user.getClient_id());
		user.setTopic(null);
		tx.commit();
	}
	
	/**
	 * 按照huanxin_group_id 查找话题
	 */
	@Override
	public Topic findTopicById(String id) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().getTransaction().begin();
		return (Topic) sessionFactory.getCurrentSession().get(Topic.class, id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Topic> recommendTopics(String label, int nowpage) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().getTransaction().begin();
		return (List<Topic>) sessionFactory.getCurrentSession().createQuery("from Topic as topic where ")
													.setFirstResult(Constant.PAGESIZE*(nowpage-1)).setMaxResults(Constant.PAGESIZE).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<User> checkMemberFromTopic(String topic_id,int nowpage) {
		// TODO Auto-generated method stub
		sessionFactory.getCurrentSession().getTransaction().begin();
		Topic topic = (Topic) sessionFactory.getCurrentSession().get(Topic.class, topic_id);
		List<User> users = new ArrayList<User>();
		users.addAll(Constant.PAGESIZE*(nowpage-1),topic.getUsers());
		return users;
	}

	
}
