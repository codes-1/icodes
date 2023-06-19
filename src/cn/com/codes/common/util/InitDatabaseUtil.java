package cn.com.codes.common.util;

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
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import cn.com.codes.framework.common.config.PropertiesBean;
import cn.com.codes.framework.common.util.Context;
import cn.com.codes.userManager.blh.PasswordTool;
import cn.com.codes.common.util.InitDatabaseUtil;

public class InitDatabaseUtil {
	private static Connection conn = null;
	static Statement st = null;
	private static Logger loger = Logger.getLogger(InitDatabaseUtil.class);

	public InitDatabaseUtil() {
		try {
			PropertiesBean conf = (PropertiesBean) Context.getInstance()
					.getBean("ContextProperties");
			String driver = conf.getProperty("config.db.driver");
			String url = conf.getProperty("config.db.url");
			String userName = PasswordTool.DecodePasswd(conf
					.getProperty("config.db.user"));
			String password = PasswordTool.DecodePasswd(conf
					.getProperty("config.db.password"));
			Class.forName(driver);
			conn = DriverManager.getConnection(url, userName, password);
			conn.setAutoCommit(false);
			st = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static synchronized void initDatas() throws SQLException {

	}

	public int  exeUpgrade(boolean fileFlg) {
		PropertiesBean conf = (PropertiesBean) Context.getInstance().getBean("ContextProperties");
		int exeFlg = 0;
		if (conf.getProperty("haveUpgrade") == null) {
			if (fileFlg) {
				exeFlg = upgradeDbFromeFile();
				try {
					if(exeFlg==1){
						conf.setProperty("haveUpgrade", "true");
						conf.write();
					}
				} catch (IOException e) {
					loger.error(e);
					e.printStackTrace();
				}
			}else {
				try {
					initDatas();
					exeFlg=1;
					conf.setProperty("haveUpgrade", "true");
					conf.write();
				} catch (SQLException e) {
					loger.error(e);
					e.printStackTrace();
				}catch (IOException e) {
					loger.error(e);
					e.printStackTrace();
				}finally{
					if (st != null) {
						try {
							st.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					if (conn != null) {
						try {
							conn.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}

				}
			}
		}
		return exeFlg ;
	}

	public static  synchronized int upgradeDbFromeFile() {
		int exeFlg = 0;
		PropertiesBean conf = (PropertiesBean) Context.getInstance().getBean("ContextProperties");
		String pathAndFile = conf.getContextPath().split("WEB-INF")[0]+ "WEB-INF/classes/upgrade.sql";
		OutputStreamWriter sqlFileWrite = null;
		InputStreamReader fr = null;
		try {
			st.execute("update t_function set FUNCTIONNAME='测试需求项BUG分布明细' where FUNCTIONNAME='BUG模块分布统计'");
			
			st.execute("update t_function set FUNCTIONNAME='开发人员待改|BUG修改统计' where FUNCTIONNAME='开发人员待改BUG统计'");
			st.execute("update t_function set FUNCTIONNAME='测试人员提交|关闭BUG统计' where FUNCTIONNAME='测试人员提交BUG统计'");
			st.execute("update t_function set FUNCTIONNAME='日执行用例趋势及明细' where FUNCTIONNAME='日执行用例趋势'");
			st.execute("update t_bugbaseinfo t ,t_outlineinfo f set t.MODULENUM =f.MODULENUM where t.moduleid=f.moduleid");
			 fr = new InputStreamReader(new FileInputStream(
					pathAndFile), "utf-8");
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			
			StringBuffer sql = new StringBuffer();
			while (line != null) {
				//System.out.println(line);
				if (!line.equals("") && !line.startsWith("#")) {
					if(line.endsWith(";")){
						sql.append(line);
						if(sql.indexOf("delete")<4){
							System.out.println("===========================sql");
							System.out.println(sql.toString().substring(0, sql.length()-1));
							st.execute(sql.toString().substring(0, sql.length()-1));
							loger.error(sql.toString().substring(0, sql.length()-1));
						}					
						sql.delete(0, sql.length());
					}else{
						sql.append(line);
					}
				}
				line = br.readLine();
			}
			exeFlg=1;
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
		return exeFlg;
	}
	public static  synchronized int initDatasFromeFile() {
		int exeFlg = 0;
		PropertiesBean conf = (PropertiesBean) Context.getInstance().getBean("ContextProperties");
		String pathAndFile = conf.getContextPath().split("WEB-INF")[0]+ "WEB-INF/classes/upgrade.sql";
		OutputStreamWriter currCompanyRemarkFileWrite = null;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(pathAndFile), "UTF-8"));
			String line = br.readLine();
			while (line != null) {
				if ("".equals(line) || !line.startsWith("--")||!line.startsWith("#")) {
					//line = br.readLine();
					continue;
				}
				line = line.substring(0, line.length() - 1);
				st.execute(line);

				line = br.readLine();
			}
			exeFlg=1;

		} catch (IOException e) {
			loger.error(e);
			e.printStackTrace();
		} catch (SQLException e) {
			loger.error(e);
			e.printStackTrace();
		} catch (Exception e) {
			loger.error(e);
			e.printStackTrace();
		} finally {
			if (currCompanyRemarkFileWrite != null) {
				try {
					currCompanyRemarkFileWrite.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (st != null) {
				try {
					st.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		}
		return exeFlg;
	}
	

}
