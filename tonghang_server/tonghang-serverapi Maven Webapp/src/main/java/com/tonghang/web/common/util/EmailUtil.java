package com.tonghang.web.common.util;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.tonghang.web.user.pojo.User;

public class EmailUtil {
	public static void sendEmail(User user) {
		JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
		senderImpl.setHost("smtp.qq.com");
		senderImpl.setUsername("tonghangtonghang@126.com");
		senderImpl.setPassword("xiaot2015");
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(user.getEmail());
		mailMessage.setFrom("tonghangtonghang@126.com");
		mailMessage.setSubject("【同行】密码已重置");
		mailMessage.setText("尊敬的" + user.getUsername() + "，您好！\n\n"
				+ "您的同行帐户密码已更改，当前密码是：" + user.getPassword()
				+ "\n请及时更改成您熟悉的密码。\n\n" + "祝您使用快乐！");
		senderImpl.send(mailMessage);
	}
}
