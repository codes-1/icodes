package cn.com.codes.common.util.uploadExcel.match;

import java.util.Calendar;

import cn.com.codes.common.util.uploadExcel.match.RegExp;

public final class RegExp {
	// 匹配数字
	public static final String NUM = "^([0-9])+$";
	// 匹配电子邮箱
	public static final String EMAIL = "^(\\S)+[@]{1}(\\S)+[.]{1}(\\S)+$";
	// 任何非空白字符
	public static final String BLANK = "^(\\S)+$";
	// 匹配包括数字和字母的非空白字符
	public static final String CHAR = "^(\\w)+$";
	// 匹配首位不能为0的数字
	public static final String NOZERO = "^([1-9]){1}[0-9]*$";
	// 验证是否是日期格式
	public static final String DATE1 = "^([1-9]){1}([0-9]){3}[-]{1}([0-9]){1,2}[-]{1}([0-9]){1,2}$";
	// 验证日期格式 闰年,非闰年,大月,小月
	public static final String DATE = "^((([0-9]{2})(([02468][048])|([13579][26])))(-)(2|02)(-)((([1-9])|([0][1-9]))|([1-2][0-9])))"
			+ "|"
			+ "((([0-9]{2})(([02468][1235679])|([13579][01345789])))(-)(2|02)(-)((([1-9])|([0][1-9]))|([1][0-9])|([2][0-8])))"
			+ "|"
			+ "(([0-9]{4})(-)((1|01|3|03|5|05|7|07|8|08)|(10|12))(-)((([1-9])|([0][1-9]))|([1-2][0-9])|30|31))"
			+ "|"
			+ "(([0-9]{4})(-)(((4|04|6|06|9|09)|11))(-)((([1-9])|([0][1-9]))|([1-2][0-9])|30))$";
	// 匹配只能是1或0
	public static final String ZEROORONE = "^(0|1)$";
	// 校验用户名必须是由4-20个英文字母、数字和符号组成（可以使用中文，一个汉字占用2个字符），字母不区分大小写
	public static final String ACCOUNTNAME = "^((\\S)|([\u4e00-\u9fa5]))+$";
	// 校验是中文
	public static final String CHECKCHINESE = "^([\u4e00-\u9fa5])$";

	// 校验固定电话号码
	public static final String PHONENUMBER = "^(\\d)+$";

	// 校验学员档案号:格式为(2008070256)
	public static final String ARCHIVERNUMBER = "^(\\d){10}$";

	// 校验所有成绩的格式:格式(100/120)
	public static final String GRADEFORMAT = "^(\\d){1,3}(/)(\\d){1,3}$";

	// 校验只能输入两位数字
	public static final String TWOBITNUM = "^(\\d){2}$";

	// 校验年份
	public static final String YEAR = "^(\\d){4}$";

	// 填写是/否
	public static final String YESORNO = "^(是|否){1}$";
	
	public static final String SEX="^(男|女){1}$";

	// 校验只输入年份和月份(格式:2008-2)
	public static final String YEARORMONTH = "^((\\d){4}(-)([1-9]){1})" + "|"
			+ "((\\d){4}(-)(1)([012]))$";
	public static final String YEARORMONTHDOT="^((\\d){4}(\\.)(([1-9])|(0[1-9])){1})" + "|"
			+ "((\\d){4}(\\.)(1)([012]))$";
	// 验证日期格式 闰年,非闰年,大月,小月 年月日以"."隔开
	public static final String DATEDOT = "^((([0-9]{2})(([02468][048])|([13579][26])))(\\.)(2|02)(.)((([1-9])|([0][1-9]))|([1-2][0-9])))"
			+ "|"
			+ "((([0-9]{2})(([02468][1235679])|([13579][01345789])))(\\.)(2|02)(\\.)((([1-9])|([0][1-9]))|([1][0-9])|([2][0-8])))"
			+ "|"
			+ "(([0-9]{4})(\\.)((1|01|3|03|5|05|7|07|8|08)|(10|12))(\\.)((([1-9])|([0][1-9]))|([1-2][0-9])|30|31))"
			+ "|"
			+ "(([0-9]{4})(\\.)(((4|04|6|06|9|09)|11))(\\.)((([1-9])|([0][1-9]))|([1-2][0-9])|30))$";
	//上课周期
	public static final String ATTEND_CLASS_CYCLE="^(每天|星期一|星期二|星期三|星期四|星期五|星期六|星期日){1}$";

	public static boolean checkLoginName(String loginName) {
		if (loginName.matches(RegExp.ACCOUNTNAME)) {
			int len = 0;
			for (int i = 0; i < loginName.length(); i++) {
				len += loginName.substring(i, i + 1).matches(RegExp.CHECKCHINESE) ? 2 : 1;
			}
			if (len > 20 || len < 4)
				return false;
			else
				return true;
		} else
			return false;
	}
	
	public static boolean checkPassword(String password){
		if(password.length() < 6)
			return false;
		return true;
	}

	public static void main(String[] args) {
		System.out.println("每天".matches(RegExp.ATTEND_CLASS_CYCLE));
		Calendar today=Calendar.getInstance();
		System.out.println(today.get(Calendar.DAY_OF_WEEK));
	}
}
