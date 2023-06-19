package cn.com.codes.framework.common;

import cn.com.codes.framework.common.Encipher;
import cn.com.codes.framework.common.EncipherConst;

//以下的常量和EncodePasswd(),DecodePasswd()用于系统口令的加密和解密

public class Encipher {

	public static String pubKey =  EncipherConst.getKey();
	
	public static String EncodePasswd(String strPasswd) {
		String strEncodePasswd = new String("");

		int n;
		char code;
		String des = new String();
		String strKey = new String();
		if ((strPasswd == null) | strPasswd.length() == 0) {
			strEncodePasswd = "";
			return strEncodePasswd;
		}

		strKey = EncipherConst.m_strKey1 + EncipherConst.m_strKey2
				+ EncipherConst.m_strKey3 + EncipherConst.m_strKey4
				+ EncipherConst.m_strKey5 + EncipherConst.m_strKey6;

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
			strEncodePasswd = Encipher.EncodePasswd(strPasswd);
		}
		// add end.
		return strEncodePasswd;
	}

	public static String DecodePasswd(String varCode) {
		int n;
		String des = new String();
		String strKey = new String();
		if ((varCode == null) || (varCode.length() == 0)) {
			return "";
		}
		strKey = EncipherConst.m_strKey1 + EncipherConst.m_strKey2
				+ EncipherConst.m_strKey3 + EncipherConst.m_strKey4
				+ EncipherConst.m_strKey5 + EncipherConst.m_strKey6;
		strKey ="zxcvbnm,./asdfghjkl;qwertyuiop=` ZXCVBNM<>?:LKJOP{}|+_)(*&^%$#@!~HGFDSAQWERTYUI[]\1234567890-";
		//System.out.println("key=====" +strKey);
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


}