package cn.com.codes.framework.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.apache.log4j.Logger;

import cn.com.codes.framework.common.config.PropertiesBean;
import cn.com.codes.framework.common.util.Context;

public class UpgradeTool {

	private PreparedStatement ps = null;
	public Connection conn = null;
	public String compId = "";
	Statement st = null;
	private static Logger loger = Logger.getLogger(UpgradeTool.class);

	public UpgradeTool() {
		PropertiesBean conf = (PropertiesBean) Context.getInstance().getBean("ContextProperties");
		String driver = conf.getProperty("config.db.driver");
		String url = conf.getProperty("config.db.url");
		String userName = conf.getProperty("config.db.user");
		String password = conf.getProperty("config.db.password");
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, userName, password);
			conn.setAutoCommit(false);
			st = conn.createStatement();
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		} catch (SQLException e) {
			loger.error(e);
			e.printStackTrace();
		}
	}

	public UpgradeTool(String dbType) {
		compId = String.valueOf(new Date().getTime());
		try {
			if (dbType.equals("oracle")) {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				conn = DriverManager.getConnection(
						"jdbc:oracle:thin:@127.0.0.1:1521:orcl", "mypm10",
						"mypm10");
			} else {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager
						.getConnection(
								"jdbc:mysql://127.0.0.1:6033/mypmbugtrace?useUnicode=true&characterEncoding=utf-8",
								"mypm22", "345");
			}
			conn.setAutoCommit(false);
			st = conn.createStatement();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void upgradeDb(String pathAndFile) {
		OutputStreamWriter sqlFileWrite = null;
		InputStreamReader fr = null;
		try {
			 fr = new InputStreamReader(new FileInputStream(
					pathAndFile), "utf-8");
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			StringBuffer sql = new StringBuffer();
			while (line != null) {
				if (!line.equals("") && !line.startsWith("#")) {
					if(line.endsWith(";")){
						sql.append(line);
						st.execute(sql.toString().substring(0, sql.length()-1));
						sql.delete(0, sql.length()-1);
					}else{
						sql.append(line);
					}
				}
				line = br.readLine();
			}
			fr.close();
			File sqlFile = new File(pathAndFile);
			sqlFileWrite = new OutputStreamWriter(
					new FileOutputStream(sqlFile), "utf-8");
			sqlFileWrite.write("");
			conn.commit();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(st!=null){
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}	
			if(fr!=null){
				try {
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(sqlFileWrite!=null){
				try {
					sqlFileWrite.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}






	public static void main(String[] args) {
		// 初始化还要补充下面的表
		// ld_menu,ld_user,ld_group,ld_menugroup,ld_usergroup
		// MenuAdmin menuAdmin = new MenuAdmin("oracle");
		UpgradeTool menuAdmin = new UpgradeTool("mysql");
		try {
			for(int i =0; i<1500; i++){
				//menuAdmin.st.execute("insert into t_helper(id) values("+i +")");
				System.out.println("insert into t_helper(id) values("+i +");");
			}
			//menuAdmin.conn.commit();
		} catch (Exception e) {
			try {
				menuAdmin.conn.rollback();
			} catch (SQLException e1) {
				
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if (menuAdmin.conn != null) {
				try {
					menuAdmin.conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}


}
