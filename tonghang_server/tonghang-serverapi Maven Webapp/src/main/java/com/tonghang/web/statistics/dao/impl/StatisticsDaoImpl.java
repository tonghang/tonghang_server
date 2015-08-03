package com.tonghang.web.statistics.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.tonghang.web.statistics.dao.StatisticsDao;
import com.tonghang.web.statistics.pojo.ActiveUser;

@Repository("statisicsDao")
public class StatisticsDaoImpl implements StatisticsDao {
	
	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;

	@Override
	public void addActiveUser(ActiveUser auser) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		if(!session.getTransaction().isActive()){
			session.getTransaction().begin();
		}
		session.saveOrUpdate(auser);
		session.getTransaction().commit();
	}

	@Override
	public int getActiveUserNumber() {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		if(!session.getTransaction().isActive()){
			session.getTransaction().begin();
		}
		int count = session.createQuery("from ActiveUser au").list().size();
		return count;
	}

}
