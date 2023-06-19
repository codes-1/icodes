package cn.com.codes.framework.jms;

import java.io.Serializable;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.command.ActiveMQObjectMessage;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import cn.com.codes.framework.security.SysLog;
import cn.com.codes.msgManager.dto.MailBean;



public class CommonMessageConverter implements MessageConverter {

	
	/* (non-Javadoc)
	 * @see org.springframework.jms.support.converter.MessageConverter#fromMessage(javax.jms.Message)
	 */
	public Object fromMessage(Message msg) throws JMSException,
			MessageConversionException {
		if (msg instanceof ObjectMessage) {
			return ((ObjectMessage) msg).getObject();
		} else {
			throw new JMSException("Msg:[" + msg + "] is not Map");
		}
	}

	/* (non-Javadoc)
	 * @see org.springframework.jms.support.converter.MessageConverter#toMessage(java.lang.Object, javax.jms.Session)
	 */
	public Message toMessage(Object obj, Session session) throws JMSException,
			MessageConversionException {
		if (obj instanceof SysLog ||obj instanceof MailBean) {
			ActiveMQObjectMessage objMsg = (ActiveMQObjectMessage) session.createObjectMessage();
			objMsg.setObject((Serializable)obj);
			return objMsg;
		} else {
			throw new JMSException("Object:[" + obj + "] is not Log");
		}
	
	}


}