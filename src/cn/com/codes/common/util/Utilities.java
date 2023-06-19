package cn.com.codes.common.util;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.com.codes.common.util.StringUtils;

public final class Utilities {	
	public static Date someDate(Date now, Integer yearBefore,
			Integer monthBefore, Integer dayBefore) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);

		if (yearBefore != null)
			calendar.roll(Calendar.YEAR, yearBefore.intValue());
		if (monthBefore != null)
			calendar.roll(Calendar.MONTH, monthBefore.intValue());
		if (dayBefore != null)
			calendar.roll(Calendar.DAY_OF_MONTH, dayBefore.intValue());

		return calendar.getTime();
	}

	public static Date rollDate(Date now, String mode, int delta) {
		if (now == null)
			return null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);

		if (mode == null || mode.equalsIgnoreCase("year")) {
			calendar.add(Calendar.YEAR, delta);
		} else if (mode == null || mode.equalsIgnoreCase("month")) {
			calendar.add(Calendar.MONTH, delta);
		} else if (mode == null || mode.equalsIgnoreCase("day")) {
			calendar.add(Calendar.DAY_OF_MONTH, delta);
		} else if (mode == null || mode.equalsIgnoreCase("week")) {
			calendar.add(Calendar.DAY_OF_MONTH, delta * 7);
		}

		return calendar.getTime();
	}

	public static Date[] calculateBeginAndEndTime(Date date, String mode) {
		if (date == null)
			return new Date[] { null, null };

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		if (mode == null || mode.equalsIgnoreCase("day")) {
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			Date begin = calendar.getTime();

			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			Date end = calendar.getTime();

			return new Date[] { begin, end };
		} else if (mode.equalsIgnoreCase("week")) {
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			Date begin = calendar.getTime();

			calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
			calendar.add(Calendar.DAY_OF_WEEK, 0);
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			Date end = calendar.getTime();

			return new Date[] { begin, end };
		} else if (mode.equalsIgnoreCase("month")) {
			calendar.set(Calendar.DAY_OF_MONTH, 1);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			Date begin = calendar.getTime();

			calendar.set(Calendar.DAY_OF_MONTH, calendar
					.getActualMaximum(Calendar.DAY_OF_MONTH));
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
			Date end = calendar.getTime();

			return new Date[] { begin, end };
		}

		return new Date[] { null, null };
	}

	public static Date[] getEffectiveDate(Date startSection, Date endSection,
			Date startReference, Date endReference) {
		if (startSection == null)
			startSection = startReference;
		if (endSection == null)
			endSection = endReference;

		if (startReference == null)
			startReference = startSection;
		if (endReference == null)
			endReference = endSection;

		if (startReference == null && endReference == null)
			return new Date[] { null, null };

		if (startSection != null && startReference != null
				&& startSection.after(startReference))
			startReference = startSection;

		if (endSection != null && endReference != null
				&& endSection.before(endReference))
			endReference = endSection;

		if (startReference != null && endReference != null
				&& startReference.after(endReference))
			return new Date[] { null, null };

		return new Date[] { startReference, endReference };
	}

	public static String formatTidyDate(int year, int month, int day) {
		String date = "" + year;
		date += ((month < 10) ? ("0" + month) : month);
		date += ((day < 10) ? ("0" + day) : day);
		return date;
	}

	public static String formatStringDate(Calendar calendar) {
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		
		return "" + year + (month < 10 ? ("0" + month) : month)
				+ (day < 10 ? ("0" + day) : day);
	}

	public static String formatTidyDate(int year, int month) {
		String date = "" + year;
		date += ((month < 10) ? ("0" + month) : month);
		return date;
	}

	public static boolean isNullOrEmpty(List list) {
		return (list == null || list.size() == 0);
	}
	
	public static Integer getNumberNoPoint(double oldNumber){
		Long result = 0l;
		if(oldNumber > 1e-3 || oldNumber < -1e-3){
			result = Math.round(oldNumber);
		}
		return result.intValue();
	}

	public static Integer getIntegerPC(Float f, Float p, Integer scale){
        BigDecimal b = new BigDecimal(Double.toString(f*100.0 / p));
        BigDecimal one = new BigDecimal("1");
		return (b.divide(one,scale,BigDecimal.ROUND_HALF_UP).intValue());
	}
	
	public static String[] getWeekBEStr(java.util.Calendar javaCalendar, Integer customWeekStartDay){
		String[] weekBEStr = new String[2];
		javaCalendar.setFirstDayOfWeek(customWeekStartDay);
		int dayOfWeek = javaCalendar.get(java.util.Calendar.DAY_OF_WEEK) - 1;
		javaCalendar.add(java.util.Calendar.DATE, -dayOfWeek);
		weekBEStr[0] = StringUtils.formatShortDate(javaCalendar.getTime());
		javaCalendar.add(java.util.Calendar.DATE, 7);
		weekBEStr[1] = StringUtils.formatShortDate(javaCalendar.getTime());
		return weekBEStr;
	}
	
	public static String[] getMonthBEStr(java.util.Calendar javaCalendar){
		String[] monthBEStr = new String[2];
        int minValue = javaCalendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        javaCalendar.set(Calendar.DAY_OF_MONTH, minValue);
        monthBEStr[0] = StringUtils.formatShortDate(javaCalendar.getTime());
        int maxValue = javaCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        javaCalendar.set(Calendar.DAY_OF_MONTH, maxValue);
        monthBEStr[1] = StringUtils.formatShortDate(javaCalendar.getTime());
		return monthBEStr;
	}
	
	public static String[] getSeasonBEStr(java.util.Calendar javaCalendar){
		int year = javaCalendar.get(java.util.Calendar.YEAR);
		int month = javaCalendar.get(java.util.Calendar.MONTH);
		String[] seasonBEStr = new String[2];
		int array[][] = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, { 10, 11, 12 } };
		int season = 1;
		if (month >= 1 && month <= 3) {
			season = 1;
		}
		if (month >= 4 && month <= 6) {
			season = 2;
		}
		if (month >= 7 && month <= 9) {
			season = 3;
		}
		if (month >= 10 && month <= 12) {
			season = 4;
		}
		int start_month = array[season - 1][0];
		int end_month = array[season - 1][2];

		int start_days = 1;
		int end_days = getLastDayOfMonth(year, end_month);
		seasonBEStr[0] = year + "-" + start_month + "-" + start_days;
		seasonBEStr[1] = year + "-" + end_month + "-" + end_days;
		return seasonBEStr;
	}

	private static int getLastDayOfMonth(int year, int month) {
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
			return 31;
		}
		if (month == 4 || month == 6 || month == 9 || month == 11) {
			return 30;
		}
		if (month == 2) {
			if (isLeapYear(year)) {
				return 29;
			} else {
				return 28;
			}
		}
		return 0;
	}

	public static boolean isLeapYear(int year) {
		return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
	}
	



}