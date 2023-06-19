package cn.com.codes.framework.common;

import java.util.Random;



// 以下的常量和EncodePasswd(),DecodePasswd()用于系统口令的加密和解密
public class EncipherConst{

	   public final static String m_strKey1 = "zxcvbnm,./asdfg";
		//public final static String m_strKey1 = "liuyougenjiang";
	    // public final static String m_strKey1 = "zxcvbnml./asdfg";

	    public final static String m_strKeya = "cjk;";

	    // public final static String m_strKey2 = "hjkl;'qwertyuiop";
	    public final static String m_strKey2 = "hjkl;qwertyuiop";

	    public final static String m_strKeyb = "cai2";

	    public final static String m_strKey3 = "[]\\1234567890-";

	    // public final static String m_strKey3 = "[]ll1234567890-";

	    public final static String m_strKeyc = "%^@#";

	    public final static String m_strKey4 = "=` ZXCVBNM<>?:LKJ";

	    public final static String m_strKeyd = "*(N";

	    public final static String m_strKey5 = "HGFDSAQWERTYUI";

	    public final static String m_strKeye = "%^HJ";

	    public final static String m_strKey6 = "OP{}|+_)(*&^%$#@!~";
	    
	    // public final static String m_strKey6 = "OP{}|+_)(*l^%$#@!~";
	    
	    public static String  getKey(){
	    	String keyArr [] = new String[7];
	    	String keyInitArr [] = new String[]{null,m_strKey1,m_strKey2,m_strKey3,m_strKey4,m_strKey5,m_strKey6};
	    	Random rand = new Random();
	    	int i = 1;
	    	while(!isFull(keyArr)){
	    		int exeFlg = rand.nextInt(7);
	    		if(!isContain(keyArr,keyInitArr[exeFlg])){
	    			keyArr[i] = keyInitArr[exeFlg];
	    			i++;
	    		}
	    	}
	    	
	    	StringBuffer keySb = new StringBuffer();
	    	
	    	for(String key :keyArr){
	    		if(key!=null){
	    			keySb.append(key);
	    		}
	    	}
	    	
	    	return keySb.toString();
	    }
	    

	    public static  boolean isFull(String[] keyArr){
	    	for(int i=1; i<7;i++){
	    		if(keyArr[i]==null){
	    			return false;
	    		}
	    	}
	    	return true;
	    }
	    
	    
	    public static  boolean isContain(String[] keyArr,String keyVal){
	    	for(int i=1; i<7;i++){
	    		if(keyArr[i]==keyVal){
	    			return true;
	    		}
	    	}
	    	return false;
	    }
	    
}