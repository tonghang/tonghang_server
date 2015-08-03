package com.tonghang.web.statistics.dao;

import com.tonghang.web.statistics.pojo.ActiveUser;

public interface StatisticsDao {

	public void addActiveUser(ActiveUser auser);
	
	public int getActiveUserNumber();
}
