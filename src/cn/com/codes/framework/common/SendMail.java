package cn.com.codes.framework.common;



import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;





public class SendMail {

  private MimeMessage mimeMsg; 
  private Session session;
  private Properties props;
  private Multipart mp;
  private boolean needAuth = false;
  private String username = "";
  private String password = "";
  
  public SendMail() {
//setSmtpHost(getConfig.mailHost);�getConfig���л�ȡ
    createMimeMessage();
  }

  public SendMail(String smtp) {
    setSmtpHost(smtp);
    createMimeMessage();
  }

  /**
   * @param hostName String
   */
  public void setSmtpHost(String hostName) {
    
    //System.out.println("mail.smtp.host =="+hostName);
    if (props == null) {
      props = System.getProperties(); 

    }
    props.put("mail.smtp.host", hostName);
  }

  /**
   * @return boolean
   */
  public boolean createMimeMessage() {
    try {
      session = Session.getDefaultInstance(props, null);
    }
    catch (Exception e) {
      System.err.println("��ȡ�ʼ��Ự����ʱ�������" + e);
      return false;
    }

    try {
    	mimeMsg = new MimeMessage(session);
    	mp = new MimeMultipart();

      return true;
    }
    catch (Exception e) {
      System.err.println("����MIME�ʼ�����ʧ�ܣ�" + e);
      return false;
    }
  }

  /**
   * @param need boolean
   */
  public void setNeedAuth(boolean need) {

    //System.out.println("mail.smtp.auth  ==="+need);
    if (props == null) {
      props = System.getProperties();

    }
    if (need) {
      props.put("mail.smtp.auth", "true");
    }
    else {
      props.put("mail.smtp.auth", "false");
    }
  }

  /**
   * @param name String
   * @param pass String
   */
  public void setNamePass(String name, String pass) {
    username = name;
    password = pass;
  }

  /**
   * @param mailSubject String
   * @return boolean
   */
  public boolean setSubject(String mailSubject) {
    try {
      mimeMsg.setSubject(mailSubject);
      return true;
    }
    catch (Exception e) {
      System.err.println("�����ʼ����ⷢ�����");
      return false;
    }
  }

  /**
   * @param mailBody String
   */
  public boolean setBody(String mailBody) {
    try {
      BodyPart bp = new MimeBodyPart();
      bp.setContent(
          "<meta http-equiv=Content-Type content=text/html; charset=utf-8>" +
          mailBody, "text/html;charset=utf-8");
      mp.addBodyPart(bp);

      return true;
    }
    catch (Exception e) {
      System.err.println("�����ʼ�����ʱ�������" + e);
      return false;
    }
  }

  /**
   * @param name String
   * @param pass String
   */
  public boolean addFileAffix(String filename) {

    try {
      BodyPart bp = new MimeBodyPart();
      FileDataSource fileds = new FileDataSource(filename);
      bp.setDataHandler(new DataHandler(fileds));
      bp.setFileName(fileds.getName());

      mp.addBodyPart(bp);

      return true;
    }
    catch (Exception e) {
      System.err.println("����ʼ�������" + filename + "�������" + e);
      return false;
    }
  }

  /**
   * @param name String
   * @param pass String
   */
  public boolean setFrom(String from) {
    try {
      mimeMsg.setFrom(new InternetAddress(from)); //���÷�����
      return true;
    }
    catch (Exception e) {
      return false;
    }
  }

  /**
   * @param name String
   * @param pass String
   */
  public boolean setTo(String to) {
    if (to == null) {
      return false;
    }

    try {
      mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
      return true;
    }
    catch (Exception e) {
      return false;
    }

  }

  /**
   * @param name String
   * @param pass String
   */
  public boolean setCopyTo(String copyto) {
    if (copyto == null) {
      return false;
    }
    try {
      mimeMsg.setRecipients(Message.RecipientType.CC,
                            (Address[]) InternetAddress.parse(copyto));
      return true;
    }
    catch (Exception e) {
      return false;
    }
  }

  /**
   * @param name String
   * @param pass String
   */
  public boolean sendout() {
    try {
      mimeMsg.setContent(mp);
      mimeMsg.saveChanges();

      Session mailSession = Session.getInstance(props, null);
      Transport transport = mailSession.getTransport("smtp");
      String host=(String)props.get("mail.smtp.host");
      transport.connect(host,username,password);
      transport.sendMessage(mimeMsg,mimeMsg.getRecipients(Message.RecipientType.TO));
//transport.send(mimeMsg);

   
      transport.close();

      return true;
    }
    catch (Exception e) {
      System.err.println(e);
      return false;
    }
  }

  /**
   * Just do it as this
 * @throws IOException 
   */
  
}



