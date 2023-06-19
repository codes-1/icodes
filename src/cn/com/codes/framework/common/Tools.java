package cn.com.codes.framework.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class Tools {

	/**   
     *   得到给定日期   
     *     
     *   @param   date   给定日期   
     *   @param   pattern   给定日期格式   
     *   @return   Date   给定日期   
     */
	public static Date getDate(String date,String pattern){
		SimpleDateFormat sdf=new SimpleDateFormat(pattern);
		Date date_o=null;
		try {
			date_o=sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date_o;
	}
	
	
	
	/**   
     *   得到给定日期   
     *     
     *   @param   date   给定日期   
     *   @param   pattern   给定日期格式   
     *   @return   String   给定日期   
     */
	public static String getDate(Date date,String pattern){
		SimpleDateFormat sdf=new SimpleDateFormat(pattern);
		return sdf.format(date);
	}
	
	/**
	 * 根据给定的日期，得到该日是周几
	 * @param date 给定的日期
	 * @return 周几
	 */
	public static String getDayByDate(Date date){
		String [] days={"周日","周一","周二","周三","周四","周五","周六"};
		
		Calendar c=new GregorianCalendar();
		c.setTime(date);
		return days[c.get(Calendar.DAY_OF_WEEK)-1];
	}
	

	
	/**
	 * 根据当前时间取得下一个季节
	 * 
	 * @param d
	 * @return 一个两个元素的数组，第一个元素是年，第二个元素是季节
	 */
	public static String[] getNextSeason(Date d){
		String[] s=new String[2];
		Calendar c2=new GregorianCalendar();
		c2.setTime(d);
		int year=c2.get(Calendar.YEAR);
		int month=c2.get(Calendar.MONTH)+1;
		if(month>=1 && month <=3){
			s[0]=String.valueOf(year);
			s[1]="夏季";
		}else if(month>=4 && month <=6){
			s[0]=String.valueOf(year);
			s[1]="秋季";
		}else if(month>=7 && month <=9){
			s[0]=String.valueOf(year);
			s[1]="冬季";
		}else if(month>=10 && month <=12){
			s[0]=String.valueOf(year+1);
			s[1]="春季";
		}
		return s;
	}

	public static String getInExpress(String[] Ids){
		
		StringBuffer inExpress = new StringBuffer("in(");
		for(String id:Ids){
				inExpress.append(" '" +id +"',");
			
		}
		inExpress.append(")");
		String result=(inExpress.toString()).replace(",)", ")");
		return result;
	}
}
