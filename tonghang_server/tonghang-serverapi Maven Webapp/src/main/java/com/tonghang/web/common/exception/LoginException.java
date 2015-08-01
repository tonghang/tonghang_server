package com.tonghang.web.common.exception;

public class LoginException extends BaseException{

	public int code = 510;
	public String message = "用户名或密码错误";
	
	public LoginException(){
		super();
	}
	public LoginException(String message){
		super(message);
	}
	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return message;
	}
	
	public int getCode(){
		return code;
	}

}
