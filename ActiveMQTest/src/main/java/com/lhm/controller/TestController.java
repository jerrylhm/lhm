package com.lhm.controller;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

    @Autowired
    private JmsTemplate jmsTemplate;
    @Resource(name="itemAddTopic")
    private Destination destination;
    
    @RequestMapping("index")
    public String index() {
    	return "index";
    }
    
    @RequestMapping("send")
    @ResponseBody
    public String send() {
    	jmsTemplate.send(destination, new MessageCreator() {
			
			public Message createMessage(Session session) throws JMSException {
				return session.createTextMessage("草泥马的");
			}
		});
    	return "ok";
    }
}
