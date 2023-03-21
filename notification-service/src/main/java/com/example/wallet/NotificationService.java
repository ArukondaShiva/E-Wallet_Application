package com.example.wallet;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

	@Autowired
	SimpleMailMessage simpleMailMessage;
	
	@Autowired
	JavaMailSender javaMailSender;
	
	@KafkaListener(topics = {CommonConstants.TRANSACTION_COMPLETION_TOPIC}, groupId = "grp123")
	public void sendNotification(String msg) throws ParseException {
		
		JSONObject data = (JSONObject) new JSONParser().parse(msg);
		String email = (String) data.get("email");
		String emailMsg = (String) data.get("msg");
		
		simpleMailMessage.setFrom("EnterMail.com");
		simpleMailMessage.setTo(email);
		simpleMailMessage.setText(emailMsg);
		simpleMailMessage.setSubject("E-Wallet Payment Updates");
		
		javaMailSender.send(simpleMailMessage);
	}
	
	
}
