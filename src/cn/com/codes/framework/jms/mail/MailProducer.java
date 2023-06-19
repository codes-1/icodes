package cn.com.codes.framework.jms.mail;

import javax.jms.Queue;

import org.springframework.jms.core.JmsTemplate;

import cn.com.codes.msgManager.dto.MailBean;



public class MailProducer {

	private JmsTemplate template;

	private Queue destination;

	public void setTemplate(JmsTemplate template) {
		this.template = template;
	}

	public void setDestination(Queue destination) {
		this.destination = destination;
	}

	public void sendMail(MailBean mailBean) {
		template.convertAndSend(this.destination, mailBean);
	}

}