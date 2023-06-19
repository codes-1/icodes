package cn.com.codes.framework.common.util;

import java.security.Key;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;

import cn.com.codes.framework.common.util.DESPlus;

/**
 * @note 定义指定字符串生成密钥 加密和解密方法
 * @author jiangke 2008-5-16
 */
public class DESPlus {
	private static String strDefaultKey = "lygdgzyhfczxyzhjkrqambowanbo";

	private static Cipher encryptCipher = null;

	private static Cipher decryptCipher = null;

	/**
	 * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[]
	 * hexStr2ByteArr(String strIn) 互为可逆的转换过程
	 * 
	 * @param arrB
	 *            需要转换的byte数组
	 * @return 转换后的字符串
	 * @throws Exception
	 *             本方法不处理任何异常，所有异常全部抛出
	 */
	public static String byteArr2HexStr(byte[] arrB) throws Exception {
		int iLen = arrB.length;
		StringBuffer sb = new StringBuffer(iLen * 2);
		for (int i = 0; i < iLen; i++) {
			int intTmp = arrB[i];
			while (intTmp < 0) {
				intTmp = intTmp + 256;
			}
			if (intTmp < 16) {
				sb.append("0");
			}
			sb.append(Integer.toString(intTmp, 16));
		}
		return sb.toString();
	}

	public static byte[] hexStr2ByteArr(String strIn) throws Exception {
		byte[] arrB = strIn.getBytes();
		int iLen = arrB.length;
		byte[] arrOut = new byte[iLen / 2];
		for (int i = 0; i < iLen; i = i + 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
		}
		return arrOut;
	}

	public DESPlus() throws Exception {
		init(strDefaultKey);
	}

	/**
	 * 指定密钥构造方法
	 * 
	 * @param strKey
	 *            指定的密钥
	 * @throws Exception
	 */
	public static void init(String strKey) throws Exception {
		//Security.addProvider(new com.sun.crypto.provider.SunJCE());
		Key key = getKey(strKey.getBytes());

		synchronized (Cipher.class) {
			encryptCipher = Cipher.getInstance("DES");
		}
		encryptCipher.init(Cipher.ENCRYPT_MODE, key);

		synchronized (Cipher.class) {
			decryptCipher = Cipher.getInstance("DES");
		}
		decryptCipher.init(Cipher.DECRYPT_MODE, key);
	}

	/**
	 * 加密字节数组
	 * 
	 * @param arrB
	 *            需加密的字节数组
	 * @return 加密后的字节数组
	 * @throws Exception
	 */
	public static byte[] encrypt(byte[] arrB) throws Exception {
		return encryptCipher.doFinal(arrB);
	}

	/**
	 * 加密字符串
	 * 
	 * @param strIn
	 *            需加密的字符串
	 * @return 加密后的字符串
	 * @throws Exception
	 */
	public static String encrypt(String strKey, String strIn) {
		if (strIn == null || "".equals(strIn)) {

			return "";
		}
		String encryptStr = null;
		try {
			if (strKey == null || "".equals(strKey)) {
				init(strDefaultKey);
			} else {
				init(strKey);
			}
			encryptStr = byteArr2HexStr(encrypt(strIn.getBytes()));
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return encryptStr;
	}

	/**
	 * 解密字节数组
	 * 
	 * @param arrB
	 *            需解密的字节数组
	 * @return 解密后的字节数组
	 * @throws Exception
	 */
	public static byte[] decrypt(byte[] arrB) throws Exception {
		return decryptCipher.doFinal(arrB);
	}

	/**
	 * 解密字符串
	 * 
	 * @param strIn
	 *            需解密的字符串
	 * @return 解密后的字符串
	 * @throws Exception
	 */
	public static String decrypt(String strKey, String strIn) {
		if (strIn == null || "".equals(strIn)) {

			return "";
		}
		String decryptStr = null;
		try {
			if (strKey == null || "".equals(strKey)) {
				init(strDefaultKey);
			} else {
				init(strKey);
			}
			decryptStr = new String(decrypt(hexStr2ByteArr(strIn)));
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return decryptStr;
	}

	/**
	 * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位
	 * 
	 * @param arrBTmp
	 *            构成该字符串的字节数组
	 * @return 生成的密钥
	 * @throws java.lang.Exception
	 */
	private static Key getKey(byte[] arrBTmp) throws Exception {
		byte[] arrB = new byte[8];
		for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
			arrB[i] = arrBTmp[i];
		}

		// 生成密钥
		Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");

		return key;
	}

	/**
	 * 测试
	 */
	public static void main(String[] args) {
		// try {
		String test = "123!jklfff";
		// DESPlus des = new DESPlus();//默认密钥
		// DESPlus des = new DESPlus("leemenz");//自定义密钥
		System.out.println("加密前的字符：" + test);
		test = DESPlus.encrypt("1234567890abcdefghijklmn", test);
		System.out.println("加密后的字符：" + test);
		System.out.println("解密后的字符：" + DESPlus.decrypt("1234567890abcdefghijklmn", test));
		// }
		// catch (BadPaddingException be) {
		// System.out.println("密钥错误");
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}
}
