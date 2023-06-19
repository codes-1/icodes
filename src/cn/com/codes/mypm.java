package cn.com.codes;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import cn.com.codes.bugManager.blh.BugManagerBlh;

public class mypm {

	public static void main(String[] args) {
		ApplicationContext ac = new FileSystemXmlApplicationContext(
				"file:D:/mypmInstalDir/mypm/webapps/mypm/WEB-INF/classes/applicationContext.xml");
		BugManagerBlh blh = (BugManagerBlh) ac.getBean("bugManagerBlh");
		System.out.println(blh);  
	} 
}
