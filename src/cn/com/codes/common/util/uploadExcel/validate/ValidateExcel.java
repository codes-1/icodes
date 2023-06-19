package cn.com.codes.common.util.uploadExcel.validate;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import cn.com.codes.common.util.uploadExcel.match.RegExp;
import cn.com.codes.common.util.uploadExcel.parsexml.ParseXmlTool;
import cn.com.codes.common.util.uploadExcel.parsexml.XmlEntity;
import cn.com.codes.common.util.uploadExcel.parsexml.XmlEntity.CellConfig;
import cn.com.codes.framework.common.ClassUtil;
import cn.com.codes.framework.common.LogWrap;
import cn.com.codes.common.util.uploadExcel.validate.ValidateExcel;

public class ValidateExcel {
	private static Logger log = Logger.getLogger(ValidateExcel.class);
	private final int HOLD_DECIMAL_TWO = 2;// 小数点后保留两位
	private final int ROUNDING_VALUE = 5;// 四舍五入
	private final int EXCEL_START_ROW = 3;// excel表数据有效开始行
	private POIFSFileSystem fs;
	private XmlEntity xmlEntity;
	@SuppressWarnings("unchecked")
	private List entityList;// 通过验证后的数据集

	@SuppressWarnings("unchecked")
	public ValidateExcel(POIFSFileSystem fs, XmlEntity xmlEntity) {
		this.fs = fs;
		this.xmlEntity = xmlEntity;
	}

	@SuppressWarnings( { "deprecation", "unchecked" })
	public String validateExcel() {
		List<XmlEntity.CellConfig> cellList = xmlEntity.getCellConfigList();
		StringBuffer errorStr = new StringBuffer();
		try {
			HSSFWorkbook workBook = new HSSFWorkbook(fs);
			HSSFSheet aSheet = workBook.getSheetAt(0);// 只做一个sheet
			// 设置list容量
			setEntityList(new ArrayList(aSheet.getLastRowNum()));
			LogWrap.info(log, "excel表的总行数:" + aSheet.getLastRowNum());
			if (aSheet.getLastRowNum() < 3)
				errorStr.append("请输入数据");
			else {
				for (int i = EXCEL_START_ROW; i <= aSheet.getLastRowNum(); i++) {
					String className = xmlEntity.getClassName();
					Object entity = ClassUtil.newInstance(className);
					HSSFRow row = aSheet.getRow(i);
					String error = "";
					for (short j = 0; j < xmlEntity.getLength(); j++) {
						HSSFCell cell = row.getCell(j);
						CellConfig cellConfig = cellList.get(j);
						String cellValue = "";
						// 先做组内数据的判断
						if (cellConfig.getGroup() != null) {
							checkGroupData(cellConfig, row, errorStr, i);
						}
						if (cell != null) {
							// 得到Excel单元格值
							cellValue = getCellValue(cell, cellConfig);
							cellValue = cellValue.trim();
							if (cellValue.equalsIgnoreCase("")) {
								if (cellConfig.isNoNull()) {
									error = (i + 1) + "行" + (j + 1) + "列数据不能为空" + "<br>";
									errorStr.append(error);
									continue;
								} else
									continue;
							}
							// 数据不为空
							else {
								// 不需要校验
								if (cellConfig.getMatch() == null && cellConfig.getMethod() == null) {
									LogWrap.info(log, "无需验证的数据:" + cellValue);
									BeanUtils.copyProperty(entity, cellConfig.getPropetyName(), cellValue);
									continue;

								}// 正则校验
								else if (cellConfig.getMatch() != null
										&& cellConfig.getMatch().length >= 0) {
									error = checkCellValueByMatchAndCopyData(
											cellValue, cellConfig, entity, i, j);
									if (!error.equalsIgnoreCase("")) {
										errorStr.append(error);
									}
									continue;
								}
								// 方法校验
								else if (cellConfig.getMethod() != null) {
									error = checkCellValueByMethodAndCopyData(cellValue, cellConfig, entity, i, j);
									if (!error.equalsIgnoreCase("")) {
										errorStr.append(error);
									}
									continue;
								}
							}
						} else if (cellConfig.isNoNull()) {
							error = (i + 1) + "行" + (j + 1) + "列数据不能为空"
									+ "<br>";
							errorStr.append(error);
							continue;
						}
					}
					// 将通过验证的数据放到list中
					if (error.equalsIgnoreCase(""))
						entityList.add(entity);
				}
			}
		} catch (Exception e) {
			LogWrap.error(log, e.getMessage());
			errorStr.append(e.getMessage());
			e.printStackTrace();
		}
		setEntityList(entityList);
		LogWrap.info(log, "数据验证结果:" + errorStr.toString());
		LogWrap.info(log, "有效数据数量:" + entityList.size());
		return errorStr.toString();
	}

	private String checkCellValueByMethodAndCopyData(final String cellValue,
			final CellConfig cellConfig, Object entity, final int i, final int j)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		String methodStr = cellConfig.getMethod();
		Method methodCheck = RegExp.class.getMethod(methodStr,
				new Class[] { String.class });
		Object[] args = { new String(cellValue) };
		Boolean matchResult = (Boolean) methodCheck.invoke(null, args);
		if (!matchResult.booleanValue()) {
			String error = (i + 1) + "行" + (j + 1) + "列,错误数据为:" + cellValue + cellConfig.getErrorInformation() + "<br>";
			return error;
		}
		// 数据通过验证,将数据放入到实体对象中
		else {
			LogWrap.info(log, "通过方法验证的数据:" + cellValue);
			BeanUtils.copyProperty(entity, cellConfig.getPropetyName(), cellValue);
			return "";
		}

	}

	private String checkCellValueByMatchAndCopyData(final String cellValue,
			final CellConfig cellConfig, Object entity, final int i, final int j)
			throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException, ParseException {
		String[] matchStr = cellConfig.getMatch();
		boolean bool = false;
		for (int m = 0; m < matchStr.length; m++) {
			if (matchStr[m] != null) {
				Field field = RegExp.class.getField(matchStr[m]);
				String match = (String) field.get(matchStr);
				if (cellValue.matches(match)) {
					bool = true;
					break;
				}
			}
		}
		if (!bool) {
			String error = (i + 1) + "行" + (j + 1) + "列数据格式错误,错误数据为:" + cellValue + "<br>";
			return error;
		} else {
			LogWrap.info(log, "通过正则验证的数据" + cellValue);
			if (cellConfig.getDataType() != null) {
				if (cellConfig.getDataType().equalsIgnoreCase("DATE")) {
					Date tempDate = null;
					String temp;
					if (cellValue.indexOf(".") > 0) {
						if (cellValue.split("\\.").length == 2) {
							temp = cellValue + ".1";
						} else {
							temp = cellValue;
						}
						tempDate = new SimpleDateFormat("yyyy.MM.dd").parse(temp);
					} else if (cellValue.indexOf("-") > 0) {
						if (cellValue.split("-").length == 2) {
							temp = cellValue + "-1";
						} else {
							temp = cellValue;
						}
						tempDate = new SimpleDateFormat("yyyy-MM-dd").parse(temp);
					}
					BeanUtils.copyProperty(entity, cellConfig.getPropetyName(), tempDate);
				}
			} else {
				BeanUtils.copyProperty(entity, cellConfig.getPropetyName(), cellValue);
			}
			return "";
		}
	}

	@SuppressWarnings("deprecation")
	private String getCellValue(final HSSFCell cell, final CellConfig cellConfig) {
		String cellValue = "";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_BLANK:
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			break;
		case HSSFCell.CELL_TYPE_ERROR:
			break;
		case HSSFCell.CELL_TYPE_FORMULA:
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				cellValue = new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
			} else {
				Double num = cell.getNumericCellValue();
				if (cellConfig.getDataType() != null && cellConfig.getDataType().equalsIgnoreCase("Double")) {
					BigDecimal b = new BigDecimal(num);
					cellValue = b.setScale(HOLD_DECIMAL_TWO, ROUNDING_VALUE).toString();
				} else {
					cellValue = String.valueOf(num.longValue());
				}
			}
			break;
		case HSSFCell.CELL_TYPE_STRING:
			cellValue = cell.getStringCellValue();
			break;
		}
		return cellValue;
	}

	private void checkGroupData(final CellConfig cellConfig, final HSSFRow row, final StringBuffer errorStr, final int i) {
		String group = cellConfig.getGroup();
		if (group.indexOf(";") > 0) {
			boolean result = false;
			String[] semicolonGroup = group.split(";");
			boolean[] checkoutResult = new boolean[semicolonGroup.length];
			for (int m = 0; m < semicolonGroup.length; m++) {
				checkoutResult[m] = true;
				if (semicolonGroup[m].indexOf(",") > 0) {
					String[] commaGroup = semicolonGroup[m].split(",");
					for (int n = 0; n < commaGroup.length; n++) {
						Short index = Short.valueOf(commaGroup[n]);
						HSSFCell groupCell = row.getCell(index.shortValue());
						if (groupCell == null) {
							checkoutResult[m] = false;
							break;
						} else {
							String temp = getCellValue(groupCell, cellConfig);
							if (temp.equalsIgnoreCase("")) {
								checkoutResult[m] = false;
								break;
							}
						}
					}
				}
			}
			for (int m = 0; m < checkoutResult.length; m++) {
				if (checkoutResult[m] == true) {
					result = true;
					break;
				}
			}
			if (!result) {
				String error = (i + 1) + "行" + cellConfig.getErrorInformation() + "<br>";
				errorStr.append(error);
			}
		} else if (group.indexOf(",") > 0) {
			String[] commaGroup = group.split(",");
			boolean checkoutResult = false;
			for (int m = 0; m < commaGroup.length; m++) {
				Short index = Short.valueOf(commaGroup[m]);
				HSSFCell groupCell = row.getCell(index.shortValue());
				if (groupCell != null) {
					String temp = getCellValue(groupCell, cellConfig);
					if (!temp.equalsIgnoreCase("")) {
						checkoutResult = true;
						break;
					}
				}
			}
			if (!checkoutResult) {
				String error = (i + 1) + "行" + cellConfig.getErrorInformation() + "<br>";
				errorStr.append(error);
			}
		}
	}

	public static void main(String[] args) {
		POIFSFileSystem fs = null;
		try {
			fs = new POIFSFileSystem(new FileInputStream(
					"D://webplace//hy//WebRoot//exceltemplete//ambow.xls"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ParseXmlTool test = new ParseXmlTool();
		XmlEntity xmlEntity = test.parseXml("student.xml");
		ValidateExcel v = new ValidateExcel(fs, xmlEntity);
		if (v.validateExcel().toString().equalsIgnoreCase("")) {
		}
	}

	@SuppressWarnings("unchecked")
	public List getEntityList() {
		return entityList;
	}

	@SuppressWarnings("unchecked")
	public void setEntityList(List entityList) {
		this.entityList = entityList;
	}
}
