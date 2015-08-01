package com.tonghang.web.common.dao;

import java.util.List;

import com.tonghang.web.common.pojo.Label;
import com.tonghang.web.user.pojo.User;

public interface LabelDao {

	public void save(Label label);
	
	public List<Label> findLabelByName(String label_name);
	public List<Label> findLabelByUser(User user);
	
	public Label findLabelById(String id);
	
	
	
}
