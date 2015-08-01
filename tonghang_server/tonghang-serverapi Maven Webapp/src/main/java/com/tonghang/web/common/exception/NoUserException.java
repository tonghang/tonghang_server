package com.tonghang.web.common.exception;

public class NoUserException extends BaseException{

	public String message = "该用户不存在";
	public int code = 511;
	
	public NoUserException(){
		super();
	}
	public NoUserException(String message){
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
