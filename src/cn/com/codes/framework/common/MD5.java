package cn.com.codes.framework.common;

import   java.security.MessageDigest;   


public   class   MD5{   
        /**   
          *   Constructor   
          *   
          */   
        public   MD5()   {   
        }   
          
        public   static   String   md5(String   plainText)   {   
                String   encryptText   =   null;   
                try   {   
                        MessageDigest   m   =   MessageDigest.getInstance("MD5");   
                        m.update(plainText.getBytes("UTF8"));   
                        byte   s[]   =   m.digest();   
                        String   result   =   "";   
                        for   (int   i   =   0;   i   <   s.length;   i++)   {   
                                result   +=   Integer.toHexString((0x000000FF   &   s[i])   |   0xFFFFFF00)   
                                                .substring(6);   
                        }   
                        return   result;   
                }   
                catch   (Exception   e)   {   
                }   
                  
                return   encryptText;   
        }   
}   

