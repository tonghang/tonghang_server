package com.tonghang.web.user.dao.impl;

import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.tonghang.web.common.util.Constant;
import com.tonghang.web.label.pojo.Label;
import com.tonghang.web.user.dao.UserDao;
import com.tonghang.web.user.pojo.User;

@Repository("userDao")
public class UserDaoImpl implements UserDao {
	@Resource(name="sessionFactory")
	private SessionFactory sessionFactory;

	
	@Override
	public User findUserById(String client_id) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		session.setFlushMode(FlushMode.COMMIT);
		if(!session.getTransaction().isActive()){
			session.getTransaction().begin();
		}
		session.clear();
		User user = (User)session.get(User.class, client_id);
		session.getTransaction().commit();
		session.close();
		return user;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> findUserByLabel(String label_name,int nowpage) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		session.setFlushMode(FlushMode.COMMIT);
		if(!session.getTransaction().isActive()){
			session.getTransaction().begin();
		}
		List<User> user = (List<User>) session.createQuery("select distinct user from User as user left join user.labellist as label where label.label_name like '%"+label_name+"%'")
														.setFirstResult(Constant.PAGESIZE*(nowpage-1)).setMaxResults(Constant.PAGESIZE).list();
		session.getTransaction().commit();
		session.close();
		return user;
	}

	@SuppressWarnings("unchecked")
	@Override
	public User findUserByEmail(String email) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		session.setFlushMode(FlushMode.COMMIT);
		if(!session.getTransaction().isActive()){
			session.getTransaction().begin();
		}
		User user = (User) session.createQuery("from User as user where user.email = '"+email+"'").uniqueResult();
		session.getTransaction().commit();
		session.close();
		return user;
	}

	@Override
	public void save(User user) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		session.setFlushMode(FlushMode.COMMIT);
		if(!session.getTransaction().isActive()){
			session.getTransaction().begin();
		}
		session.save(user);
		session.getTransaction().commit();
		session.close();
	}

	@Override
	public void saveOrUpdate(User user) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		session.setFlushMode(FlushMode.COMMIT);
		if(!session.getTransaction().isActive()){
			session.getTransaction().begin();
		}
		session.saveOrUpdate(user);
		session.getTransaction().commit();
		session.close();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<User> findUserByUsername(String username,int page) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		session.setFlushMode(FlushMode.COMMIT);
		if(!session.getTransaction().isActive()){
			session.getTransaction().begin();
		}
		session.flush(); 
		session.clear();
		List<User> users = session.createQuery("from User as user where user.username like '%"+username+"%'").setFirstResult(Constant.PAGESIZE*(page-1)).setMaxResults(Constant.PAGESIZE).list();
		session.getTransaction().commit();
		session.close();
		return users;
	}

	@Override
	public void update(String client_id, User user) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		if(!session.getTransaction().isActive()){
			session.getTransaction().begin();
		}
		User u =  (User)session.get(User.class, client_id);
		u = user;
		session.getTransaction().commit();
		session.close();
	}

	@Override
	public void deleteLabel(String client_id,Label label) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		if(!session.getTransaction().isActive()){
			session.getTransaction().begin();
		}
		User user = (User) session.get(client_id,User.class);
		user.getLabellist().remove(label);
		session.update(user);
		session.getTransaction().commit();
		session.close();
	}

	@Override
	public void deleteAllLabel(String client_id) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		if(!session.getTransaction().isActive()){
			session.getTransaction().begin();
		}
		User user = (User) session.get(User.class,client_id);
		user.setLabellist(new HashSet<Label>());
		session.update(user);
		session.getTransaction().commit();	
		session.close();
	}

	@Override
	public void addFriend(User my, User friend) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		if(!session.getTransaction().isActive()){
			session.getTransaction().begin();
		}
		my.getFriends().add(friend);
		session.update(my);
		session.getTransaction().commit();	
		session.close();
	}

	@Override
	public void deleteFriend(User my, User friend) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		if(!session.getTransaction().isActive()){
			session.getTransaction().begin();
		}
		my.getFriends().remove(friend);
		session.update(my);
		session.getTransaction().commit();	
		session.close();
	}

}
