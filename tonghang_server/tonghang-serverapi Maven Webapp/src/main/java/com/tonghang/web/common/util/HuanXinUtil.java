package com.tonghang.web.common.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.tonghang.web.user.pojo.User;

/**
 * 业务逻辑中需要到环信的工具类
 * @author Administrator
 *
 */
public class HuanXinUtil {
	public static String HUANXINtoken;
	static {
		HUANXINtoken = HuanXinUtil.getHuangXinServerToken();
	}
	/**
	 * 获得环信管理员的TOKEN(定时更新尚未实现)
	 * @return
	 */
	protected static String getHuangXinServerToken(){
		HttpHeaders header = new HttpHeaders();
		Map<String,Object> parts = new HashMap<String, Object>();
		header.add("Content-Type","application/json");
		parts.put("grant_type", "client_credentials");
		parts.put("client_id", Constant.CLIENT_ID);
		parts.put("client_secret", Constant.CLIENT_SECRET);
		HttpEntity<Map<String,Object>> requestEntity=
				new HttpEntity<Map<String,Object>>(parts,header);
		ResponseEntity<Map> response = DataUtil.postEntity(
							Constant.HUANXIN_URL+"token",requestEntity,Map.class);
		Map map = response.getBody();
		String token = (String) map.get("access_token");
		return token;
	}
	
	public static void registUser(User user){
		HttpHeaders header = new HttpHeaders();
		Map<String,Object> parts = new HashMap<String, Object>();
		header.add("Content-Type","application/json");
		parts.put("username", user.getClient_id().replaceAll("-", ""));
		parts.put("password", user.getPassword());
		parts.put("nickname", user.getUsername());
		HttpEntity<Map<String,Object>> requestEntity=
				new HttpEntity<Map<String,Object>>(parts,header);
		ResponseEntity<Map> result = DataUtil.postEntity(Constant.HUANXIN_URL+"users",requestEntity,Map.class);
	}
	
	/**
	 * 用户修改昵称
	 * @param username
	 * @param huanxinusername
	 */
	public static void changeUsername(String username,String huanxinusername){
		HttpHeaders header = new HttpHeaders();
		Map<String,Object> parts = new HashMap<String, Object>();
		header.add("Authorization", "Bearer "+HUANXINtoken);
		parts.put("nickname", username);
		HttpEntity<Map<String,Object>> requestEntity=
				new HttpEntity<Map<String,Object>>(parts,header);
		System.out.println(huanxinusername);
		DataUtil.putEntity(Constant.HUANXIN_URL+"users/"+huanxinusername.replaceAll("-", ""),requestEntity);
	}
	
	/**
	 * 用户修改密码
	 * @param password
	 * @param username
	 */
	public static void changePassword(String password,String username){
		HttpHeaders header = new HttpHeaders();
		System.out.println("username:"+username);
		Map<String,Object> parts = new HashMap<String, Object>();
		header.add("Authorization","Bearer "+HUANXINtoken);
		parts.put("newpassword", password);
		HttpEntity<Map<String,Object>> requestEntity=
				new HttpEntity<Map<String,Object>>(parts,header);
		DataUtil.putEntity(Constant.HUANXIN_URL+"users/"+username.replaceAll("-", "")+"/password",requestEntity);
	}
	
	/**
	 * 用户好友关系管理（环信API中 POST加好友DELETE删好友）
	 * @param my
	 * @param friend
	 * @param httpmethod
	 */
	public static void operateFriends(String my,String friend,String httpmethod){
		HttpHeaders header = new HttpHeaders();
		Map<String,Object> parts = new HashMap<String, Object>();
		header.add("Authorization","Bearer "+HUANXINtoken);
		HttpEntity<Map<String,Object>> requestEntity=
				new HttpEntity<Map<String,Object>>(parts,header);
		if ("POST".equalsIgnoreCase(httpmethod)) {
			DataUtil.postEntity(Constant.HUANXIN_URL+"users/"+my+"/contacts/users/"+friend,requestEntity, Map.class);
		}else{
			DataUtil.deleteEntity(Constant.HUANXIN_URL+"users/"+my+"/contacts/users/"+friend);
		}
	}
	
	/**
	 * 在环信中创建群组（创建话题）
	 * @param groupname
	 * @param owner
	 * @return
	 */
	public static String createGroup(String groupname,String owner){
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		Map<String,Object> parts = new HashMap<String, Object>();
		header.add("Authorization","Bearer "+HUANXINtoken);
		System.out.println("!!!!!!!!:     " + HUANXINtoken);
		parts.put("groupname", groupname);
		parts.put("desc", Constant.CREATEGROUP);
		parts.put("public", true);
		parts.put("maxusers", 100);
		parts.put("approval", true);
		parts.put("owner", owner);
		HttpEntity<Map<String,Object>> requestEntity=
				new HttpEntity<Map<String,Object>>(parts,header);
		ResponseEntity<Map> result = DataUtil.postEntity(Constant.HUANXIN_URL+"chatgroups",requestEntity,Map.class);
		Map<String,Map> msg = result.getBody();
		LogUtil.printLog("环信返回的group_id:"+(String) msg.get("data").get("groupid"));
		return (String) msg.get("data").get("groupid");
	}


}
