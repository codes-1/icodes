package cn.com.codes.impExpManager.blh;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.opensymphony.webwork.ServletActionContext;

import cn.com.codes.bugManager.blh.BugFlowConst;
import cn.com.codes.bugManager.dto.BugManagerDto;
import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.common.util.StringUtils;
import cn.com.codes.framework.app.blh.BusinessBlh;
import cn.com.codes.framework.app.view.UniversalView;
import cn.com.codes.framework.app.view.View;
import cn.com.codes.framework.common.ConvertObjArrayToVo;
import cn.com.codes.framework.exception.DataBaseException;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.impExpManager.dto.ExpBugInfo;
import cn.com.codes.impExpManager.service.ImpExpManagerService;
import cn.com.codes.object.OutlineInfo;
import cn.com.codes.object.SingleTestTask;
import cn.com.codes.testTaskManager.dto.CurrTaskInfo;
import cn.com.codes.testTaskManager.service.TestTaskDetailService;
import cn.com.codes.impExpManager.blh.BugImpExpBlh;

public class BugImpExpBlh extends BusinessBlh {

	private static Logger logger = Logger.getLogger(BugImpExpBlh.class);

	private TestTaskDetailService testTaskService;
	private ImpExpManagerService impExpManagerService;

	public View expBug(BusiRequestEvent req) {

		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		CurrTaskInfo currTaskInfo = testTaskService.getCurrTaskInfo();
		dto.setTestFlow(currTaskInfo.getTestFlow());
		dto.setRoleInTask(currTaskInfo.getRoleInTask());
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		dto.setTaskId(taskId);
		if("treeView".equals(dto.getModuleName())){//树形显示模示下的导出
			Long moduleId = dto.getBug().getModuleId();
			//System.out.println(moduleId);
			if(moduleId!=null){
				String hql = "select new OutlineInfo(moduleNum,moduleId) from OutlineInfo where moduleId=? and taskId=?";
				List<OutlineInfo> list = impExpManagerService.findByHql(hql, moduleId,taskId);
				if(list==null||list.isEmpty()){
					throw new DataBaseException("测试需求不存在");
				}
				OutlineInfo outLine = list.get(0);
				//System.out.println("==="+outLine.getModuleNum());
				if(outLine.getModuleNum()!=null){
					dto.getBug().setModuleNum(outLine.getModuleNum());
				}				
			}
			//设置测试需求ID为空，这时在查询时，用测试需求编号来查
			dto.getBug().setModuleId(null);
		}
		impExpManagerService.buildBugWhereSql(dto);
		int pageSize = 2000;
		dto.setPageSize(pageSize);
		int totalRows = impExpManagerService.getHibernateGenericController()
				.getResultCountBySqlWithValuesMap(dto.getHql(),
						dto.getHqlParamMaps()).intValue();
		//System.out.println(dto.getHql());
		//System.out.println(dto.getHqlParamMaps());
		int pageCount = (totalRows + (pageSize - (totalRows % pageSize == 0 ? pageSize
				: totalRows % pageSize)))
				/ pageSize;
		HSSFWorkbook wb = null;
		String path = null;
		try {
			path = ServletActionContext.getServletContext().getRealPath(
					File.separator);
			FileInputStream templateInputStream = new FileInputStream(path
					+ File.separator + "mypmUserFiles" + File.separator
					+ "template" + File.separator + "bugExport.xls");
			if (totalRows == 0) {
				UniversalView view = new UniversalView();
				view.displayData("excel", templateInputStream);
				return view;
			}
			POIFSFileSystem fs = new POIFSFileSystem(templateInputStream);
			wb = new HSSFWorkbook(fs);
		} catch (FileNotFoundException e1) {
			logger.error(e1);
		} catch (IOException e1) {
			logger.error(e1);
		}
		wb.setSheetName(0, "sheet1");
		HSSFSheet sheet = wb.getSheetAt(0);
		HSSFRow rowSumary = sheet.getRow(2);
		HSSFCell cellSumary  = rowSumary.getCell((short) 0);
		cellSumary.setCellValue((impExpManagerService.getBugCountStr(taskId)+" 导出BUG数："+totalRows+"(状态为撤销,无效 ,重复 ,非错 ,关闭/撤销 ,修正/描述不当的BUG不算有效BUG)"));
		HSSFRow row = sheet.getRow(3);
		
		SingleTestTask task = testTaskService.getProNameAndPmName(taskId);
		if(task!=null){
			HSSFCell cell = row.getCell((short) 1);
			cell.setCellValue(task.getProName());
			HSSFCell cellPm = row.getCell((short) 8);
			cellPm.setCellValue(task.getPsmName());
		}
		Integer startRow = 5;
		for (int i = 1; i < pageCount + 1; i++) {
			dto.setPageNo(i);
			List<ExpBugInfo> exportList = impExpManagerService
					.findBySqlWithValuesMap(dto, new ConvertObjArrayToVo() {
						public List<?> convert(List<?> resultSet) {
							if (resultSet == null || resultSet.isEmpty()) {
								return null;
							}
							Iterator it = resultSet.iterator();
							List<ExpBugInfo> list = new ArrayList<ExpBugInfo>(
									resultSet.size());
							while (it.hasNext()) {
								ExpBugInfo expBugInfo = new ExpBugInfo();
								Object values[] = (Object[]) it.next();
								expBugInfo.setBugcardId(values[0].toString());
								expBugInfo.setBugDesc((String) values[1]);
								expBugInfo.setReproductTxt((String) values[2]);
								expBugInfo.setCurrState(Integer.parseInt(values[3].toString()));
								expBugInfo.setLevelName((String) values[4]);
								expBugInfo.setTypeName((String) values[5]);
								expBugInfo.setOccaName((String) values[6]);
								expBugInfo.setReptName((String) values[7]);
								expBugInfo.setReptDate((Date) values[8]);
								expBugInfo.setDisVer((String) values[9]);
								expBugInfo.setFixName(values[10] == null ? null: (String) values[10]);
								expBugInfo.setFixDate(values[11] == null ? null: (Date) values[11]);
								expBugInfo.setClsName((String) values[12]);
								expBugInfo.setClsDate((Date) values[13]);
								expBugInfo.setReslVer(values[14] == null ? null: (String) values[14]);
								expBugInfo.setModulename((String) values[15]);
								expBugInfo.setSuperModuleName((String) values[16]);
								expBugInfo.setCurrName((String) values[17]);
								list.add(expBugInfo);
							}
							return list;
						}
					});
			writeExportExcel(exportList, sheet, startRow);
			exportList.clear();
		}
		FileOutputStream exportExcelFile = null;
		int userInt = SecurityContextHolderHelp.getUserId().hashCode();
		// 将Excel工作簿存盘
		String time = String.valueOf((new Date()).getTime());
		try {
			// 输出文件
			exportExcelFile = new FileOutputStream(path + File.separator
					+ "mypmUserFiles" + File.separator + "expFile"
					+ File.separator + "bugExport_" + time + userInt+".xls");
			wb.write(exportExcelFile);
		} catch (IOException e) {
			logger.error(e);
		} finally {
			if (exportExcelFile != null) {
				try {
					exportExcelFile.flush();
					exportExcelFile.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
		InputStream exportStuStream = getExpFileInputStream(time,userInt);
		UniversalView view = new UniversalView();
		view.displayData("excel", exportStuStream);
		File file=new File(path + File.separator
				+ "mypmUserFiles" + File.separator + "expFile"
				+ File.separator + "bugExport_" + time +userInt+ ".xls");
		if(file.exists()&&file.isFile()) {
			file.delete();
		}
		return view;
	}

	private void writeExportExcel(List<ExpBugInfo> bugList, HSSFSheet sheet,
			Integer startRow) {
			for (ExpBugInfo bug : bugList) {
					HSSFRow row = sheet.getRow(startRow);
					row = sheet.getRow(startRow);
					if (row == null) {
						row = sheet.createRow(startRow.shortValue());
					}
					sheet.addMergedRegion(new Region(startRow, (short)(1), startRow, (short)2));
					for (int celIndex = 0; celIndex < 11; celIndex++) {
						HSSFCell cell = row.getCell((short) celIndex);
						if (cell == null){
							cell = row.createCell((short) celIndex);
						}
						this.writeCell(bug,cell,celIndex);
					}
					 startRow++;
			}
	}

	private void writeCell(ExpBugInfo bug, HSSFCell cell, int celIndex) {
		cell.setCellType(HSSFCell.ENCODING_UTF_16);
		//cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		if (celIndex == 0) {
			cell.setCellValue(bug.getBugcardId()+"/("+bug.getSuperModuleName()+"/"+bug.getModulename()+")");
		} else if (celIndex == 1) {
			// HSSFCellStyle cellStyle1 = cell.getCellStyle();
			// cellStyle1.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			cell.setCellValue(bug.getBugDesc() + ":\n" + bug.getReproductTxt());
		} else if (celIndex == 3) {
			cell.setCellValue(BugFlowConst.getStateName(bug.getCurrState()));
		} else if (celIndex == 4) {
			cell.setCellValue(bug.getLevelName());
		} else if (celIndex == 5) {
			cell.setCellValue(bug.getTypeName());
		} else if (celIndex == 6) {
			cell.setCellValue(bug.getOccaName());
		} else if (celIndex == 7) {
			cell.setCellValue(bug.getReptName() + "/"
					+ StringUtils.formatShortDate(bug.getReptDate()));
		} else if (celIndex == 8) {
			if (bug.getFixDate() != null) {
				cell.setCellValue(bug.getFixName() + "/"
						+ StringUtils.formatShortDate(bug.getFixDate()));
			}
		} else if (celIndex == 9) {
			if(bug.getClsName()!=null) {
				cell.setCellValue(bug.getClsName() + "/"
						+ StringUtils.formatShortDate(bug.getClsDate()));
			}else {
				cell.setCellValue(bug.getCurrName()+ "/"
						+ StringUtils.formatShortDate(bug.getClsDate()));
			}


		} else if (celIndex == 10) {
			if (bug.getReslVer() != null) {
				cell.setCellValue(bug.getDisVer() + "/" + bug.getReslVer());
			} else {
				cell.setCellValue(bug.getDisVer()+ "/未解决" );
			}

		}
	}

	private InputStream getExpFileInputStream(String time ,int userInt) {

		InputStream studentInfoStream = ServletActionContext
				.getServletContext().getResourceAsStream(
						File.separator + "mypmUserFiles" + File.separator
								+ "expFile" + File.separator + "bugExport_"
								+ time +userInt+ ".xls");
		return studentInfoStream;
	}

	public TestTaskDetailService getTestTaskService() {
		return testTaskService;
	}

	public void setTestTaskService(TestTaskDetailService testTaskService) {
		this.testTaskService = testTaskService;
	}

	public ImpExpManagerService getImpExpManagerService() {
		return impExpManagerService;
	}

	public void setImpExpManagerService(
			ImpExpManagerService impExpManagerService) {
		this.impExpManagerService = impExpManagerService;
	}

//	public static void main(String[] args) {
//
//		try {
//			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(
//					"e:\\bugExport.xls"));
//			HSSFWorkbook wb = new HSSFWorkbook(fs);
//			HSSFSheet sheet = wb.getSheetAt(0);
//			System.out.println(sheet.getLastRowNum());
//			HSSFRow row = sheet.getRow(3);
//			HSSFCell cell = row.getCell((short) 1);
//			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//			cell.setCellValue("MYPM20/V2011");
//			for (int i = 5; i < 10; i++) {
//				row = sheet.getRow(i);
//				if (row == null) {
//					row = sheet.createRow((short) i);
//				}
//				sheet.addMergedRegion(new Region(i, (short) (1), i, (short) 2));
//				for (int c = 0; c < 11; c++) {
//					cell = row.getCell((short) c);
//					if (cell == null)
//						cell = row.createCell((short) c);
//					// System.out.println((i+1) +"_"+(c+1));
//					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
//					if (c == 1) {
//						HSSFCellStyle cellStyle1 = cell.getCellStyle();
//						cellStyle1.setAlignment(HSSFCellStyle.ALIGN_LEFT);
//						cell.setCellValue((i + 1) + "_" + (c + 1)
//								+ "BUG描述及再现步骤\n BUG描述及再现步骤\n BUG描述及再现步骤");
//					} else {
//						cell.setCellValue((i + 1) + "_" + (c + 1));
//					}
//				}
//			}
//			// 输出文件
//			FileOutputStream fileOut = new FileOutputStream(
//					"e:\\questionexport.xls");
//			wb.write(fileOut);
//			fileOut.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
}
