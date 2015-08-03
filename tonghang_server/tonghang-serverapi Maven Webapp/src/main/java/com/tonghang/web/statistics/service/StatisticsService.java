package com.tonghang.web.statistics.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.tonghang.web.statistics.dao.StatisticsDao;
import com.tonghang.web.statistics.pojo.ActiveUser;

@Service("statisticsService")
public class StatisticsService {

	@Resource(name="statisticsDao")
	private StatisticsDao statisticsDao;
	
	public void addActiveUser(ActiveUser auser){
		statisticsDao.addActiveUser(auser);
	}
	
	public int getActiveUserCount(){
		return statisticsDao.getActiveUserNumber();
	}
}
