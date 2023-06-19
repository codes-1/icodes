package cn.com.codes.object;

import java.lang.reflect.Field;

import cn.com.codes.object.JsonInterfaceTool;

public class JsonInterfaceTool {

	public static void printJsonImpl(String className,String idProName,String classPath) {
		Class c = null;
		try {
			if(classPath==null||"".equals(classPath.trim())){
				c = Class.forName("cn.com.codes.object." + className);
			}else{
				c = Class.forName(classPath + className);
			}
			//c = Class.forName("cn.com.codes.object." + className);
			//c = Class.forName("cn.com.codes.caseManager.dto." + className);
			Field[]  fields = c.getDeclaredFields();
			for(int i=0; i<4; i++){
				if(i==0){//生成toStrList
					printToStrList(fields,idProName);
				}
				if(i==1){//生成toStrUpdateInit
					printToStrUpdateInit(fields,idProName);
				}
				if(i==2){//生成toStrUpdateRest
					printToStrUpdateRest(fields,idProName);
				}
				if(i==3){//生成toString
					printToStrIng(fields,idProName);
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
	}
	public static void printViewJsonImpl(String className,String idProName) {
		Class c = null;
		try {
			c = Class.forName("cn.com.codes.object.view." + className);
			Field[]  fields = c.getDeclaredFields();
			for(int i=0; i<4; i++){
				if(i==0){//生成toStrList
					printToStrList(fields,idProName);
				}
				if(i==1){//生成toStrUpdateInit
					printToStrUpdateInit(fields,idProName);
				}
				if(i==2){//生成toStrUpdateRest
					printToStrUpdateRest(fields,idProName);
				}
				if(i==3){//生成toString
					printToStrIng(fields,idProName);
				}
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} 
	}	
	//对于复回主键，等决定如何处里后再调spliCompositeId的实现，只要idProName不是string 就调spliCompositeId处理
	public static void printToStrList(Field[]  fields,String idProName){
		StringBuffer sb = new StringBuffer();
		sb.append("   public String toStrList(){\n");
		sb.append("        StringBuffer sbf = new StringBuffer();\n");
		sb.append("        sbf.append(\"{\");\n");
		sb.append("        sbf.append(\"id:'\");\n");
		sb.append("        sbf.append(get"+upperFirstChar(idProName)+"().toString());\n");
		sb.append("        sbf.append(\"',data: [0,'','\");\n");
		int i=1;
		for(Field field :fields){
			if(!field.getName().equals(idProName)){
				if(field.getType()==java.util.Date.class){
					sb.append("        sbf.append("+field.getName()+" == null ? \"\" : StringUtils.formatShortDate("+field.getName()+" ));\n");
				}else{
					sb.append("        sbf.append("+field.getName()+" == null ? \"\" : "+field.getName()+" );\n");
				}
				if(i==fields.length-1){
					sb.append("        sbf.append(\"'\");\n");
				}else{
					sb.append("        sbf.append(\"','\");\n");
				}
				i++;
			}
		}
		sb.append("        sbf.append(\"]\");\n");
		sb.append("        sbf.append(\"}\");\n");
		sb.append("        return sbf.toString();\n");
		sb.append("    }\n");
		System.out.println(sb);
	}
	//对于复回主键，等决定如何处里后再调spliCompositeId的实现，只要idProName不是string 就调spliCompositeId处理
	public static void printToStrIng(Field[]  fields,String idProName){
		StringBuffer sb = new StringBuffer();
		sb.append("   public void toString(StringBuffer sbf){\n");
		sb.append("        sbf.append(\"{\");\n");
		sb.append("        sbf.append(\"id:'\");\n");
		sb.append("        sbf.append(get"+upperFirstChar(idProName)+"().toString());\n");
		sb.append("        sbf.append(\"',data: [0,'','\");\n");
		int i=1;
		for(Field field :fields){
			if(!field.getName().equals(idProName)){
				if(field.getType()==java.util.Date.class){
					sb.append("        sbf.append("+field.getName()+" == null ? \"\" : StringUtils.formatShortDate("+field.getName()+" ));\n");
				}else{
					sb.append("        sbf.append("+field.getName()+" == null ? \"\" : "+field.getName()+" );\n");
				}
				if(i==fields.length-1){
					sb.append("        sbf.append(\"'\");\n");
				}else{
					sb.append("        sbf.append(\"','\");\n");
				}
				i++;
			}
		}
		sb.append("        sbf.append(\"]\");\n");
		sb.append("        sbf.append(\"}\");\n");
		sb.append("    }\n");
		System.out.println(sb);
	}
	
	public static void printToStrUpdateInit(Field[]  fields,String idProName){
		StringBuffer sb = new StringBuffer();
		sb.append("    public String toStrUpdateInit(){\n");
		sb.append("        StringBuffer sbf = new StringBuffer();\n");
		sb.append("        sbf.append(\""+idProName+"=\");\n");
		sb.append("        sbf.append(get"+upperFirstChar(idProName)+"().toString());\n");
		for(Field field :fields){
			if(!field.getName().equals(idProName)){
				if(field.getType()==java.util.Date.class){
					sb.append("        sbf.append(\"^\");\n");
					sb.append("        sbf.append(\""+field.getName()+"=\").append("+field.getName()+" == null ? \"\" : StringUtils.formatShortDate("+field.getName()+"));\n");
				}else{
					sb.append("        sbf.append(\"^\");\n");
					sb.append("        sbf.append(\""+field.getName()+"=\").append("+field.getName()+" == null ? \"\" : "+field.getName()+");\n");
				}
			}
		}
		sb.append("        return sbf.toString();\n");
		sb.append("    }\n");
		System.out.println(sb);
	}
	
	public static void printToStrUpdateRest(Field[]  fields,String idProName){
		StringBuffer sb = new StringBuffer();
		sb.append("   public String toStrUpdateRest(){\n");
		sb.append("        StringBuffer sbf = new StringBuffer();\n");
		sb.append("        sbf.append(get"+upperFirstChar(idProName)+"().toString());\n");
		sb.append("        sbf.append(\"^\");\n");
		sb.append("        sbf.append(\"0,,\");\n");
		for(Field field :fields){
			if(!field.getName().equals(idProName)){
				if(field.getType()==java.util.Date.class){
					sb.append("        sbf.append("+field.getName()+" == null ? \"\" : StringUtils.formatShortDate("+field.getName()+" ));\n");
					sb.append("        sbf.append(\",\");\n");
				}else{
					sb.append("        sbf.append("+field.getName()+" == null ? \"\" : "+field.getName()+" );\n");
					sb.append("        sbf.append(\",\");\n");
				}
			}
		}
		System.out.println(sb.substring(0,sb.lastIndexOf("sbf.append"))+"return sbf.toString();\n     }\n");
	}

	public static void main(String[] args){
		JsonInterfaceTool.printJsonImpl("BugCaseCycleRela", "id","cn.com.codes.object.");
		//JsonInterfaceTool.printViewJsonImpl("VCaseBaseInfo", "caseId");
		
	}
	
	public static String upperFirstChar(String string) {
		if (string == null){
			return null;
		}
		if (string.length() <= 1){
			return string.toLowerCase();
		}
		StringBuffer sb = new StringBuffer(string);
		sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
		return sb.toString();
	}
}
