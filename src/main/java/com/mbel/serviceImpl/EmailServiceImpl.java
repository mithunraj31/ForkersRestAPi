package com.mbel.serviceImpl;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service("EmailServiceImpl")
public class EmailServiceImpl  {

	@Autowired
	private JavaMailSender javaMailSender;
	String url="http://18.182.8.74/"; 



	void sendEmail(String email, String password, String name) {

		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setTo(email);
		msg.setSubject("Registration Completed");
		msg.setText(name+"様,"+"\n"
				+"\n"+"この度は、MBEL FORCASTをご利用いただき、誠にありがとうございます。"
				+ "\n"+"アカウントの登録が完了いたしましたので、登録内容について下記のとおりご連絡申し上げます。"
				+ "\n"+"Eメール :"+email
				+"\n"+"パスワード :"+password
				+ "\n"+"ログイン後にパスワードを変更してください。"
				+ "\n"+"url :"+url);
		javaMailSender.send(msg);

	}

	void sendEmailWithAttachment() throws MessagingException, IOException {

		MimeMessage msg = javaMailSender.createMimeMessage();

		// true = multipart message
		MimeMessageHelper helper = new MimeMessageHelper(msg, true);
		helper.setTo("1@gmail.com");

		helper.setSubject("Testing from Spring Boot");

		// default = text/plain
		//helper.setText("Check attachment for image!");

		// true = text/html
		helper.setText("<h1>Check attachment for image!</h1>", true);

		helper.addAttachment("my_photo.png", new ClassPathResource("android.png"));

		javaMailSender.send(msg);

	}

}