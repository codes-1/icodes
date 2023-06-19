package cn.com.codes.common.util;

public class Convert2Name {

	public static String convCase(Integer testRest){
		if(testRest==0){
			return "ReView" ;
		}else if(testRest==1){
			return "未测试" ;
		}else if(testRest==2){
			return "通过" ;
		}else if(testRest==3){
			return "未通过" ;
		}else if(testRest==4){
			return "不适用" ;
		}else if(testRest==5){
			return "阻塞" ;
		}else if(testRest==6){
			return "待修正" ;
		}
		return "";
	}
	public static String convCase2(Integer testRest){
		if(testRest==0){
			return "ReView" ;
		}else if(testRest==1){
			return "审核通过" ;
		}else if(testRest==2){
			return "审核通过" ;
		}else if(testRest==3){
			return "审核通过" ;
		}else if(testRest==4){
			return "不适用" ;
		}else if(testRest==5){
			return "阻塞" ;
		}else if(testRest==6){
			return "待修正" ;
		}
		return "";
	}
}
