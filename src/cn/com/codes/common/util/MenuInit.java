package cn.com.codes.common.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class MenuInit {

	public static String getInitMenuSql(String pathAndFile, String compId)
			throws Exception {

		InputStreamReader fr = new InputStreamReader(new FileInputStream(
				pathAndFile), "utf-8");
		BufferedReader br = new BufferedReader(fr);
		String line = br.readLine();
		int i = 1;
		int level1 = 0;
		int level2 = 0;
		int level3 = 0;
		int level4 = 0;
		int level5 = 0;
		int level6 = 0;
		String url = "";
		String secuUrl = "";
		StringBuffer sb = new StringBuffer("\n");
		while (line != null) {
			url = "";
			secuUrl = "";
			String sql = null;
			String myLine = line.trim();
			String page = "0";
			if (myLine.split(",").length > 2) {
				url = myLine.split(",")[2];
			}
			if (myLine.split(",").length > 3) {
				secuUrl = myLine.split(",")[3];
			}
			if (myLine.split(",").length > 4) {
				page = myLine.split(",")[4];
			}

			if (!myLine.equals("")) {
				if (i % 10 == 0) {
					i = i + 10;
				}
				sql = "insert into  T_FUNCTION(FUNCTIONID,FUNCTIONNAME,SEQ,PARENTID,LEVELNUM,ISLEAF,URL,SECURITY_URL,PAGE) values('"
						+ String.valueOf(i)
						+ "','"
						+ myLine.substring(1, myLine.length()).split(",")[0]
						+ "'," + i + ",'";
				if (myLine.startsWith("1")) {
					level1 = i;
					sql += String.valueOf(-1)
							+ "',1,"
							+ myLine.substring(1, myLine.length()).split(",")[1]
							+ ",'" + url + "','" + secuUrl + "','" + page
							+ "')";
				}
				if (myLine.startsWith("2")) {
					level2 = i;
					sql += String.valueOf(level1)
							+ "',2,"
							+ myLine.substring(1, myLine.length()).split(",")[1]
							+ ",'" + url + "','" + secuUrl + "','" + page
							+ "')";
				}
				if (myLine.startsWith("3")) {
					level3 = i;
					sql += String.valueOf(level2)
							+ "',3,"
							+ myLine.substring(1, myLine.length()).split(",")[1]
							+ ",'" + url + "','" + secuUrl + "','" + page
							+ "')";
				}
				if (myLine.startsWith("4")) {
					level4 = i;
					sql += String.valueOf(level3)
							+ "',4,"
							+ myLine.substring(1, myLine.length()).split(",")[1]
							+ ",'" + url + "','" + secuUrl + "','" + page
							+ "')";
				}
				if (myLine.startsWith("5")) {
					level5 = i;
					sql += String.valueOf(level4)
							+ "',4,"
							+ myLine.substring(1, myLine.length()).split(",")[1]
							+ ",'" + url + "','" + secuUrl + "','" + page
							+ "')";
				}
				if (myLine.startsWith("6")) {
					level6 = i;
					sql += String.valueOf(level5)
							+ "',4,"
							+ myLine.substring(1, myLine.length()).split(",")[1]
							+ ",'" + url + "','" + secuUrl + "','" + page
							+ "')";
				}
				if (myLine.startsWith("7")) {
					sql += String.valueOf(level6)
							+ "',4,"
							+ myLine.substring(1, myLine.length()).split(",")[1]
							+ ",'" + url + "','" + secuUrl + "','" + page
							+ "')";
				}
				String sql2 = "insert into T_ROLE_FUNCTION_REAL(ROLEID,FUNCTIONID)  values('"
						+ compId + "','" + i + "');";
				sb.append(sql).append(";\n");
				sb.append(sql2 + "\n");
				i++;
			}
			line = br.readLine();
		}
		return sb.toString();
	}

	private static void printUpgradeSql(String upgSqlScrPath) {
		StringBuffer sb = null;
		;
		try {
			InputStreamReader fr = new InputStreamReader(new FileInputStream(
					upgSqlScrPath), "utf-8");
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			while (line != null) {
				if ("".equals(line) || line.indexOf("--") != -1) {
					line = br.readLine();
					continue;
				}

				if (line.endsWith(";")) {
					line = line.substring(0, line.length() - 1);
				}
				//System.out.println("st.execute(\"" + line + "\");");
				line = br.readLine();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// System.out.println(sb.toString());
	}

	// public static void main(String[] args) {
	// printUpgradeSql("E:\\mypm10_new\\mypmdoc\\patchs.txt");
	// }
//	public static void main(String[] args) {
//		try {
//			String sql = MenuInit.getInitMenuSql("E:\\mypm10_new\\patchs.txt",
//					"402880ea25b62b6a0125b66873290012");
//			// 402880ea25b62b6a0125b66873290012
//			System.out.println(sql);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

//	public static void main(String[] args) {
//		HttpClient httpClient = new HttpClient();
//		InetAddress ip = null;
//		GetMethod getMethod = null;
//		try {
//			ip = InetAddress.getLocalHost();
//			getMethod= new GetMethod("http://www.mypm.cc/mypm/commonAction!regisInfo.action?dto.operCmd="+getMac()+ ip.getHostAddress()+"_"+(new Date()).getTime());
//			getMethod.getParams().setParameter("test", "value");
//			getMethod.addRequestHeader("Content-Type", "charset=UTF-8");
//			int statusCode = httpClient.executeMethod(getMethod);
//			if (statusCode != HttpStatus.SC_OK) {
//				System.err.println("Method failed: "
//						+ getMethod.getStatusLine());
//			} else {
//				byte[] responseBody = getMethod.getResponseBody();
//				// 处理内容
//				System.out.println(new String(responseBody));
//			}
//		} catch (HttpException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (Exception e) {
//
//		} finally {
//			getMethod.releaseConnection();
//		}
//		
//		
//		try {
//			System.out.println("Current IP address : " + ip.getHostAddress());
//
//			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
//
//			byte[] mac = network.getHardwareAddress();
//
//			System.out.print("Current MAC address : ");
//
//			StringBuilder sb = new StringBuilder();
//			for (int i = 0; i < mac.length; i++) {
//				sb.append(String.format("%02X%s", mac[i],
//						(i < mac.length - 1) ? "-" : ""));
//			}
//			System.out.println(sb.toString());
//
//		}catch (SocketException e) {
//			// e.printStackTrace();
//		} catch (Exception e) {
//
//		}
//
//	}

	public static String  getMac() {
		try {
			StringBuilder sb = new StringBuilder();
			Enumeration<NetworkInterface> el = NetworkInterface
					.getNetworkInterfaces();
			while (el.hasMoreElements()) {
				byte[] mac = el.nextElement().getHardwareAddress();
				if (mac == null)
					continue;
				StringBuilder builder = new StringBuilder();
				for (byte b : mac) {
					builder.append(hexByte(b));
					builder.append("-");
				}
				builder.deleteCharAt(builder.length() - 1);
				sb.append(builder + "_");

			}
			//System.out.println(sb);
			return sb.toString();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	    return "";
	}

	static String hexByte(byte b) {
		String s = "000000" + Integer.toHexString(b);
		return s.substring(s.length() - 2);
	}
}
