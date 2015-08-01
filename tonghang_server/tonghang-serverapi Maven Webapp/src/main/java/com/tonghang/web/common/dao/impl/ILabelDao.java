package com.tonghang.web.common.dao.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.tonghang.web.common.dao.LabelDao;
import com.tonghang.web.common.pojo.Label;
import com.tonghang.web.user.pojo.User;

@Repository("labelDao")
public class ILabelDao implements LabelDao{
	
	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;

	@Override
	public void save(Label label) {
		// TODO Auto-generated method stub
//		System.out.println("开始插入"+label.getLabel_name());
		if(!sessionFactory.getCurrentSession().getTransaction().isActive()){
			sessionFactory.getCurrentSession().getTransaction().begin();
		}
		sessionFactory.getCurrentSession().save(label);
		sessionFactory.getCurrentSession().getTransaction().commit();
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Label> findLabelByName(String label_name) {
		// TODO Auto-generated method stub
		if(!sessionFactory.getCurrentSession().getTransaction().isActive()){
			sessionFactory.getCurrentSession().getTransaction().begin();
		}
		return sessionFactory.getCurrentSession().createQuery("from Label as label where label.label_name like '%"+label_name+"%' ").list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Label> findLabelByUser(User user) {
		// TODO Auto-generated method stub	
		if(!sessionFactory.getCurrentSession().getTransaction().isActive()){
			sessionFactory.getCurrentSession().getTransaction().begin();
		}
		List<String> labelnames = (List<String>) sessionFactory.getCurrentSession().createQuery("select label.label_name from Label as label join label.userlist as users where users.client_id= :client_id").
										setParameter("client_id", user.getClient_id()).list();
		List<Label> labels = new ArrayList<Label>();
		Set<Label> labelset = new HashSet<Label>();
		for(String names:labelnames){
			Label l = (Label) sessionFactory.getCurrentSession().get(Label.class, names);
			labelset.add(l);
		}
		user.setLabellist(labelset);
		labels.addAll(user.getLabellist());	
		return labels;
	}

	
}
