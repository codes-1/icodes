package cn.com.codes.framework.jms.log;

import javax.jms.Queue;

import org.springframework.jms.core.JmsTemplate;

import cn.com.codes.framework.security.SysLog;




public class LogProducer {

	private JmsTemplate template;

	private Queue destination;

	public void setTemplate(JmsTemplate template) {
		this.template = template;
	}

	public void setDestination(Queue destination) {
		this.destination = destination;
	}

	public void log(SysLog log) {
		template.convertAndSend(this.destination, log);
	}

}