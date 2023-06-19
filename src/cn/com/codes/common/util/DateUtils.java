package cn.com.codes.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class DateUtils {
	
	private static Log logger = LogFactory.getLog(DateUtils.class);
	
	private static final String TIDY_DATE_FORMAT = "yyyyMMdd";

	private static final String SHORT_DATE_FORMAT = "yyyy-MM-dd";

	private static final String YEAR_MONTH_FORMAT = "yyyy-MM";

	private static final String BRIEF_DATE_FORMAT = "MM-dd HH:mm";

	private static final String MIDDLE_DATE_FORMAT = "yyyy-MM-dd HH:mm";

	private static final String LONG_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private static final String COMPACT_DATE_FORMAT = "yyyyMMddHHmmss";

	public static String formatTidyDate(Date date) {
		return formatDate(TIDY_DATE_FORMAT, date);
	}

	public static Date parseTidyDate(String value) throws ParseException {
		return parseDate(TIDY_DATE_FORMAT, value);
	}

	public static Date parseTidyDate(String value, Date defaultValue) {
		return parseDate(TIDY_DATE_FORMAT, value, defaultValue);
	}

	public static Date parseTidyDateLast(String value, Date defaultValue) {
		return parseDate(TIDY_DATE_FORMAT, value, defaultValue, 86400000 - 1000);
	}

	public static String formatShortDate(Date date) {
		return formatDate(SHORT_DATE_FORMAT, date);
	}

	public static Date parseShortDate(String value) throws ParseException {
		return parseDate(SHORT_DATE_FORMAT, value);
	}

	public static Date parseShortDate(String value, Date defaultValue) {
		return parseDate(SHORT_DATE_FORMAT, value, defaultValue);
	}

	public static Date parseShort59Date(Date tempDate) {
		return new Date(parseShortDate(formatShortDate(tempDate), tempDate).getTime() + 86399999);
	}

	public static String formatYearMonthDate(Date date) {
		return formatDate(YEAR_MONTH_FORMAT, date);
	}

	public static String formatBriefDate(Date date) {
		return formatDate(BRIEF_DATE_FORMAT, date);
	}

	public static String formatMiddleDate(Date date) {
		return formatDate(MIDDLE_DATE_FORMAT, date);
	}

	public static Date parseMiddleDate(String value) throws ParseException {
		return parseDate(MIDDLE_DATE_FORMAT, value);
	}

	public static Date parseMiddleDate(String value, Date defaultValue) {
		return parseDate(MIDDLE_DATE_FORMAT, value, defaultValue);
	}

	public static String formatLongDate(Date date) {
		return formatDate(LONG_DATE_FORMAT, date);
	}

	public static Date parseLongDate(String value) throws ParseException {
		return parseDate(LONG_DATE_FORMAT, value);
	}

	public static Date parseLongDate(String value, Date defaultValue) {
		return parseDate(LONG_DATE_FORMAT, value, defaultValue);
	}

	public static String formatCompactDate(Date date) {
		return formatDate(COMPACT_DATE_FORMAT, date);
	}

	public static Date parseCompactDate(String value) throws ParseException {
		return parseDate(COMPACT_DATE_FORMAT, value);
	}

	public static Date parseCompactDate(String value, Date defaultValue) {
		return parseDate(COMPACT_DATE_FORMAT, value, defaultValue);
	}

	public static Date parseDate(String value) {
		return parseDate(value, (Date) null);
	}

	public static Date parseDate(String value, Date defaultValue) {
		if (value == null)
			return defaultValue;

		String format = "yyyyMMdd";
		if (value.indexOf("-") > 0) {
			format = "yyyy-MM-dd";
		} else if (value.indexOf("/") > 0) {
			if (value.length() == 8)
				value = "20" + value;
			format = "yyyy/MM/dd";
		}
		return parseDate(format, value, defaultValue);
	}

	public static String formatDate(String format, Date date) {
		if (date == null)
			return CalendaConstants.ANY_STRING;
		return new SimpleDateFormat(format).format(date);
	}

	public static Date parseDate(String format, String value) {
		return parseDate(format, value, null);
	}

	public static Date parseDate(String format, String value, Date defaultValue) {
		try {
			if (value == null)
				return defaultValue;
			return new SimpleDateFormat(format).parse(value);
		} catch (ParseException e) {
			logger.debug("incorrect date, default used.", e);
			return defaultValue;
		}
	}

	public static Date parseDate(String format, String value, Date defaultValue, Integer offset) {
		try {
			if (value == null)
				return defaultValue;
			return new Date(new SimpleDateFormat(format).parse(value).getTime() + offset);
		} catch (ParseException e) {
			logger.debug("incorrect date, default used.", e);
			return defaultValue;
		}
	}
	
	public static Date regularizeDate(Date date, int type) {
		if (date == null)
			return null;

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(new Date().getTime());

		if (type == 1) {
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
		} else if (type == 2) {
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			calendar.set(Calendar.MILLISECOND, 999);
		}

		return date;
	}
	
	public static String getSeason(int month) {
		if (month > 0 && month <= 3) {
			return "Q1";
		} else if (month >= 4 && month <= 6) {
			return "Q2";
		} else if (month >= 7 && month <= 9) {
			return "Q3";
		} else if (month >= 10 && month <= 12) {
			return "Q4";
		}
		return "";
	}
}
