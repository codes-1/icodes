package cn.com.codes.userManager.blh;

//以下的常量和EncodePasswd(),DecodePasswd()用于系统口令的加密和解密

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import cn.com.codes.userManager.blh.PasswordTool;


public class PasswordTool {

//	private final static String m_strKey1 = "zxcvbnm,./asdfg";
//	private final static String m_strKey2 = "hjkl;qwertyuiop";
//	private final static String m_strKey3 = "[]\\1234567890-";
//	private final static String m_strKey4 = "=` ZXCVBNM<>?:LKJ";
//	private final static String m_strKey5 = "HGFDSAQWERTYUI";
//	private final static String m_strKey6 = "OP{}|+_)(*&^%$#@!~";
	private static String  keyStr = "zxcvbnm,./asdfg" +"hjkl;qwertyuiop" +"[]\\1234567890-" +"+=` ZXCVBNM<>?:LKJ"+"HGFDSAQWERTYUI"+"OP{}|+_)(*&^%$#@!~";
	public static String EncodePasswd(String strPasswd) {
		String strEncodePasswd = new String("");

		int n;
		char code;
		String des = new String();
		String strKey = new String(keyStr);
		if ((strPasswd == null) | strPasswd.length() == 0) {
			strEncodePasswd = "";
			return strEncodePasswd;
		}

//		strKey = m_strKey1 + m_strKey2 + m_strKey3 + m_strKey4 + m_strKey5
//				+ m_strKey6;

		while (strPasswd.length() < 8) {
			strPasswd = strPasswd + (char) 1;
		}

		des = "";
		for (n = 0; n < strPasswd.length(); n++) {
			while (true) {
				code = (char) Math.rint(Math.random() * 100);

				while ((code > 0)
						&& (((code ^ strPasswd.charAt(n)) < 0) || ((code ^ strPasswd
								.charAt(n)) > 90))) {
					code = (char) ((int) code - 1);
				}

				char mid = 0;
				int flag = code ^ strPasswd.charAt(n);
				if (flag > 93) {
					mid = 0;
				} else {
					mid = strKey.charAt(flag); // Asc(Mid(strKey, (code Xor
					// Asc(Mid(strPasswd, n, 1))) +
					// 1, 1))
				}
				if ((code > 35) & (code < 122) & (code != 124) & (mid != 39)
						& (code != 44) & (mid != 124) & (mid != 39)
						& (mid != 44)) {
					break;
				}
				// 确保生成的字符是可见字符并且在SQL语句中有效
			}
			char temp = strKey.charAt(code ^ strPasswd.charAt(n));
			des = des + (char) code + temp;
		}
		strEncodePasswd = des;

		// 确保生成的字符是可见字符并且在SQL语句中有效
		while (strEncodePasswd.indexOf("&") > -1
				|| strEncodePasswd.indexOf("'") > -1
				|| strEncodePasswd.indexOf(",") > -1
				|| strEncodePasswd.indexOf("\\") > -1) {
			strEncodePasswd = PasswordTool.EncodePasswd(strPasswd);
		}
		// add end.
		return strEncodePasswd;
	}

	public static String DecodePasswd(String varCode) {
		int n;
		String des = new String();
		String strKey = new String(keyStr);
		if ((varCode == null) || (varCode.length() == 0)) {
			return "";
		}
//		strKey = m_strKey1 + m_strKey2 + m_strKey3 + m_strKey4 + m_strKey5
//				+ m_strKey6;

		if (varCode.length() % 2 == 1) {
			varCode = varCode + "?";
		}

		des = "";
		for (n = 0; n <= varCode.length() / 2 - 1; n++) {
			char b;
			b = varCode.charAt(n * 2);
			int a;
			a = (int) strKey.indexOf(varCode.charAt(n * 2 + 1));
			des = des + (char) ((int) b ^ a);
		}

		n = des.indexOf(1);
		if (n > 0) {
			return des.substring(0, n);
		} else {
			return des;
		}
	}

	private PreparedStatement ps = null;
	public Connection conn = null;
	public String compId = "";
	Statement st = null;
	public void MenuAdmin(String dbType) {
		compId =String.valueOf(new Date().getTime()) ;
		try {
			if(dbType.equals("oracle")){
				Class.forName("oracle.jdbc.driver.OracleDriver");
				conn = DriverManager
						.getConnection(
								"jdbc:oracle:thin:@127.0.0.1:1522:orcl",
								"mypm10", "mypm10");
			}else{
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager
						.getConnection(
								"jdbc:mysql://127.0.0.1:6033/mypmbugtrace?useUnicode=true&characterEncoding=utf-8",
								"mypm", "mypm10");
			}
			conn.setAutoCommit(false);
			st = conn.createStatement();
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	

}