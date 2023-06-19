package cn.com.codes.impExpManager.blh;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.opensymphony.webwork.ServletActionContext;

import cn.com.codes.caseManager.dto.CaseManagerDto;
import cn.com.codes.caseManager.service.CaseManagerService;
import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.common.util.StringUtils;
import cn.com.codes.framework.app.blh.BusinessBlh;
import cn.com.codes.framework.app.view.UniversalView;
import cn.com.codes.framework.app.view.View;
import cn.com.codes.framework.common.ConvertObjArrayToVo;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.impExpManager.dto.ExpCaseInfo;
import cn.com.codes.impExpManager.dto.ImpCaseInfo;
import cn.com.codes.impExpManager.service.ImpExpManagerService;
import cn.com.codes.object.CasePri;
import cn.com.codes.object.CaseType;
import cn.com.codes.object.OutlineInfo;
import cn.com.codes.object.SingleTestTask;
import cn.com.codes.object.TestCaseInfo;
import cn.com.codes.object.TypeDefine;
import cn.com.codes.outlineManager.blh.OutLineManagerBlh;
import cn.com.codes.outlineManager.service.OutLineManagerService;
import cn.com.codes.testBaseSet.service.TestBaseSetService;
import cn.com.codes.testTaskManager.service.TestTaskDetailService;
import cn.com.codes.impExpManager.blh.CaseImpExpBlh;

//public class CaseImpExpBlh extends BusinessBlh {
public class CaseImpExpBlh extends BusinessBlh {
	private static Logger logger = Logger.getLogger(CaseImpExpBlh.class);

	private TestTaskDetailService testTaskService;
	private ImpExpManagerService impExpManagerService;
	private TestBaseSetService testBaseSetService;
	private OutLineManagerService outLineService;
	private OutLineManagerBlh outLineBlh;
	private CaseManagerService caseService;

	@SuppressWarnings("unchecked")
	public View expCase(BusiRequestEvent req) {
		CaseManagerDto dto = super.getDto(CaseManagerDto.class, req);
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		dto.setTaskId(taskId);
		impExpManagerService.buildCaseWhereSql(dto);
		int pageSize = 2000;
		dto.setPageSize(pageSize);
		int totalRows = impExpManagerService
				.getHibernateGenericController()
				.getResultCountBySqlWithValuesMap(dto.getHql(),
						dto.getHqlParamMaps()).intValue();
		// System.out.println(dto.getHql());
		// System.out.println(dto.getHqlParamMaps());
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
					+ "template" + File.separator + "caseExport.xls");
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
		HSSFCell cellSumary = rowSumary.getCell((short) 0);
		cellSumary.setCellValue((impExpManagerService.getCaseCountStr(taskId)
				+ " 导出用例数：" + totalRows));
		HSSFRow row = sheet.getRow(3);

		SingleTestTask task = testTaskService.getProNameAndPmName(taskId);
		if (task != null) {
			HSSFCell cell = row.getCell((short) 1);
			cell.setCellValue(task.getProName());
			HSSFCell cellPm = row.getCell((short) 8);
			cellPm.setCellValue(task.getPsmName());
		}
		Integer startRow = 5;
		for (int i = 1; i < pageCount + 1; i++) {
			dto.setPageNo(i);
			List<ExpCaseInfo> exportList = impExpManagerService
					.findBySqlWithValuesMap(dto, new ConvertObjArrayToVo() {
						@SuppressWarnings("rawtypes")
						public List<?> convert(List<?> resultSet) {
							if (resultSet == null || resultSet.isEmpty()) {
								return null;
							}
							Iterator it = resultSet.iterator();
							List<ExpCaseInfo> list = new ArrayList<ExpCaseInfo>(
									resultSet.size());
							while (it.hasNext()) {
								ExpCaseInfo expCaseInfo = new ExpCaseInfo();
								Object values[] = (Object[]) it.next();
								expCaseInfo.setTestCaseId(values[0].toString());
								expCaseInfo.setSuperMname(values[1].toString());
								expCaseInfo.setModuleName(values[2].toString());
								expCaseInfo.setTestCaseDes(values[3].toString());
								expCaseInfo.setTestCaseOperData(values[4]
										.toString());
								expCaseInfo.setExpResult(values[5] == null ? ""
										: values[5].toString());
								expCaseInfo.setStatus(Integer
										.parseInt(values[6].toString()));
								expCaseInfo.setAuthor(values[7] == null ? ""
										: values[7].toString());
								expCaseInfo.setExeName(values[8] == null ? ""
										: values[8].toString());
								expCaseInfo.setTypeName(values[9] == null ? ""
										: values[9].toString());
								expCaseInfo.setPriName(values[10] == null ? ""
										: values[10].toString());
								expCaseInfo.setExeDate(values[11] == null ? null
										: (Date)values[11]);
								list.add(expCaseInfo);
							}
							return list;
						}
					});
			writeExportExcel(exportList, sheet, startRow);
			exportList.clear();
		}
		FileOutputStream exportExcelFile = null;
		// 将Excel工作簿存盘
		String time = String.valueOf((new Date()).getTime());
		int userInt = SecurityContextHolderHelp.getUserId().hashCode();
		try {
			// 输出文件
			
			exportExcelFile = new FileOutputStream(path + File.separator
					+ "mypmUserFiles" + File.separator + "expFile"
					+ File.separator + "caseExport_" + time + userInt+".xls");
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
				+ File.separator + "caseExport_" + time +userInt+ ".xls");
		if(file.exists()&&file.isFile()) {
			file.delete();
		}
		return view;
	}

	private void writeExportExcel(List<ExpCaseInfo> caseList, HSSFSheet sheet,
			Integer startRow) {
		for (ExpCaseInfo caseInfo : caseList) {
			HSSFRow row = sheet.getRow(startRow);
			row = sheet.getRow(startRow);
			if (row == null) {
				row = sheet.createRow(startRow.shortValue());
			}
			sheet.addMergedRegion(new Region(startRow, (short) (1), startRow,
					(short) 2));
			for (int celIndex = 0; celIndex < 11; celIndex++) {
				HSSFCell cell = row.getCell((short) celIndex);
				if (cell == null) {
					cell = row.createCell((short) celIndex);
				}
				this.writeCell(caseInfo, cell, celIndex);
			}
			startRow++;
		}
	}

	private void writeCell(ExpCaseInfo caseInfo, HSSFCell cell, int celIndex) {
		// cell.setEncoding(HSSFCell.ENCODING_UTF_16);
		cell.setCellType(HSSFCell.ENCODING_UTF_16);
		if (celIndex == 0) {
			cell.setCellValue(caseInfo.getTestCaseId() + "/("
					+ caseInfo.getSuperMname() + "/" + caseInfo.getModuleName()
					+ ")");
		} else if (celIndex == 1) {
			cell.setCellValue(caseInfo.getTestCaseDes());
		} else if (celIndex == 3) {
			cell.setCellValue(caseInfo.getTestCaseOperData());
		} else if (celIndex == 4) {
			cell.setCellValue(caseInfo.getExpResult());
		} else if (celIndex == 5) {
			cell.setCellValue(convCase(caseInfo.getStatus()));
		} else if (celIndex == 6) {
			cell.setCellValue(caseInfo.getTypeName());
		} else if (celIndex == 7) {
			cell.setCellValue(caseInfo.getPriName());
		} else if (celIndex == 8) {
			cell.setCellValue(caseInfo.getExeName() + "/"
					+ StringUtils.formatShortDate(caseInfo.getExeDate()));
		}
	}

	public static String convCase(Integer testStatus) {
		if (testStatus == 0) {
			// return "ReView" ;
			return "待审核";
		} else if (testStatus == 1) {
			return "未测试";
		} else if (testStatus == 2) {
			return "通过";
		} else if (testStatus == 3) {
			return "未通过";
		} else if (testStatus == 4) {
			return "不适用";
		} else if (testStatus == 5) {
			return "阻塞";
		} else if (testStatus == 6) {
			return "待修正";
		}
		return "";
	}

	@SuppressWarnings("unchecked")
	private List<OutlineInfo> loadAllOutLine() {

		String hql = "select new OutlineInfo(moduleId,superModuleId,isleafNode, moduleName,moduleState,moduleNum,moduleLevel) from OutlineInfo where taskId=? and superModuleId !=0 order by seq ";
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		return outLineService.findByNoCache(hql, taskId);
	}

	public View impCase(BusiRequestEvent req) throws Exception {
		CaseManagerDto dto = super.getDto(CaseManagerDto.class, req);
		//String impFilePath = dto.getImpFilePath();
		StringBuffer sb = new StringBuffer();
		ImpCaseInfo impCaseInfo = this.buildImpCaseInfo(dto, sb);
		if (impCaseInfo == null) {
			/*SecurityContextHolderHelp.setAttr("operaFlg", sb.toString());
			return super.getView("success",sb.toString().substring(0, sb.length()-3));*/
			super.writeResult(sb.toString());
			return super.globalAjax();
		}
		this.impCaseExexute(impCaseInfo);
		/*SecurityContextHolderHelp.setAttr("operaFlg", "success");
		return super.getView("success","success");*/
		super.writeResult("success");
		return super.globalAjax();
	}

	public static void main(String[] args) {
		CaseImpExpBlh cimplBlh = new CaseImpExpBlh();
		StringBuffer sb = new StringBuffer();
		ImpCaseInfo info = null;
		try {
			info = cimplBlh.buildImpCaseInfo("d:\\caseImp_07.xlsx", sb);
			System.out.println("====6666666666666"+sb);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private ImpCaseInfo buildImpCaseInfo(CaseManagerDto dto, StringBuffer sb)
			throws Exception {
		String officeVersion = null;
		Workbook wb = null;
		InputStream stream = null;
		if (dto.getImportFileFileName().endsWith(".xls")) {
			officeVersion = "2003";
			stream = new FileInputStream(dto.getImportFile());
		} else if (dto.getImportFileFileName().endsWith(".xlsx")) {
			officeVersion = "2007";
			stream = new FileInputStream(dto.getImportFile());
		}

		if (officeVersion.equals("2003")) {
			wb = (Workbook) new HSSFWorkbook(stream);
		} else if (officeVersion.equals("2007")) {
			wb = (Workbook) new XSSFWorkbook(stream);
		}
		Sheet sheet = null;
		sheet = wb.getSheetAt(0);
		int countRow = sheet.getLastRowNum();
		// int countCell=sheet.getRow(0).getPhysicalNumberOfCells();
		Row row = null;
		Cell cell = null;
		int t = 0;
		for (int i = 2; i <= countRow; i++) {
			row = sheet.getRow(i);
			if(t>=5000){
				return null;
			}
			for (int j = 0; j < 6; j++) {
				cell = row.getCell(j);
				String content = null;
				try {
					if(cell!=null) {
						content = cell.getStringCellValue();
					}
					
				} catch (IllegalStateException e) {
					
					logger.error("导入数据格式不对，被强制转为Stirng");
					content =String.valueOf(cell.getNumericCellValue());
				}
				if (content == null || content.trim().equals("")) {
					sb.append("第" + (i + 1) + "行 第" + (j + 1) + "列不能为空  <br>");
					t++;
				}
				content =null;
			}
			
		}

		if (sb.length() > 5) {
			return null;
		}
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		//String taskId = "40288083473efe2501473eff11020002";
		boolean caseAduitFlg  = this.isReview(taskId);
		//boolean caseAduitFlg  = false;
		Integer testStatus = null;
		if(!caseAduitFlg){
			testStatus = 1;
		}else{
			testStatus = 0;
		}
		ImpCaseInfo info = new ImpCaseInfo();
		Date createDate = new Date();
		for (int i = 2; i <= countRow; i++) {
			row = sheet.getRow(i);
			TestCaseInfo tc = new TestCaseInfo();
			tc.setTestStatus(testStatus);
			tc.setRemark("导入用例");
			tc.setIsReleased(0);
			tc.setCreaterId(SecurityContextHolderHelp.getUserId());
			//tc.setCreaterId("tc.setCreaterId(SecurityContextHolderHelp.getUserId());");
			tc.setCreatdate(createDate);
			tc.setUpddate(createDate);
			for (int j = 0; j < 6; j++) {
				cell = row.getCell(j);
				String content=null;
				try {
					//在上面己校为为空了，这里就不再校验为空 
					content = cell.getStringCellValue();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					logger.error("导入数据格式不对，被强制转为Stirng");
					content =String.valueOf(cell.getNumericCellValue());
				}
				content = this.jsonProtect(content);
				if(j==0){
					info.getModeMap().put(content, null);
					tc.setModuleName(content);
				}else if(j==1){
					tc.setTestCaseDes(content);
				}else if(j==2){
					tc.setOperDataRichText(content);
					tc.setTestData(content);
				}else if(j==3){
					tc.setExpResult(content);
				}else if(j==4){
					info.getCaseTypemap().put(content, null);
					tc.setTypeName(content);
				}else if(j==5){
					info.getCasePrimap().put(content, null);
					tc.setPriName(content);
				}
			}
			info.getCaseList().add(tc);
		}
		return info;
	}

	
	private ImpCaseInfo buildImpCaseInfo(String impFilePath, StringBuffer sb)
			throws Exception {
		String officeVersion = null;
		Workbook wb = null;
		InputStream stream = null;
		if (impFilePath.endsWith(".xls")) {
			officeVersion = "2003";
			stream = new FileInputStream(impFilePath);
		} else if (impFilePath.endsWith(".xlsx")) {
			officeVersion = "2007";
			stream = new FileInputStream(impFilePath);
		}

		if (officeVersion.equals("2003")) {
			wb = (Workbook) new HSSFWorkbook(stream);
		} else if (officeVersion.equals("2007")) {
			wb = (Workbook) new XSSFWorkbook(stream);
		}
		Sheet sheet = null;
		sheet = wb.getSheetAt(0);
		int countRow = sheet.getLastRowNum();
		// int countCell=sheet.getRow(0).getPhysicalNumberOfCells();
		Row row = null;
		Cell cell = null;
		int t = 0;
		for (int i = 2; i < countRow; i++) {
			row = sheet.getRow(i);
			if(t>=5000){
				return null;
			}
			for (int j = 0; j < 6; j++) {
				cell = row.getCell(j);
				String content = null;
				try {
					content = cell.getStringCellValue();
				} catch (Exception e) {
					logger.error("导入数据格式不对，被强制转为Stirng");
					content =String.valueOf(cell.getNumericCellValue());
				}
				if (content == null || content.trim().equals("")) {
					sb.append("第" + (i + 1) + "行 第" + (j + 1) + "列不能为空  \n");
					t++;
				}
			}
			
		}

		if (sb.length() > 5) {
			return null;
		}
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		//String taskId = "40288083473efe2501473eff11020002";
		boolean caseAduitFlg  = this.isReview(taskId);
		//boolean caseAduitFlg  = false;
		Integer testStatus = null;
		if(!caseAduitFlg){
			testStatus = 1;
		}else{
			testStatus = 0;
		}
		ImpCaseInfo info = new ImpCaseInfo();
		Date createDate = new Date();
		for (int i = 2; i < countRow; i++) {
			row = sheet.getRow(i);
			TestCaseInfo tc = new TestCaseInfo();
			tc.setTestStatus(testStatus);
			tc.setRemark("导入用例");
			tc.setIsReleased(0);
			tc.setCreaterId(SecurityContextHolderHelp.getUserId());
			//tc.setCreaterId("tc.setCreaterId(SecurityContextHolderHelp.getUserId());");
			tc.setCreatdate(createDate);
			tc.setUpddate(createDate);
			for (int j = 0; j < 6; j++) {
				cell = row.getCell(j);
				String content = cell.getStringCellValue();
				content = this.jsonProtect(content);
				if(j==0){
					info.getModeMap().put(content, null);
					tc.setModuleName(content);
				}else if(j==1){
					tc.setTestCaseDes(content);
				}else if(j==2){
					tc.setOperDataRichText(content);
					tc.setTestData(content);
				}else if(j==3){
					tc.setExpResult(content);
				}else if(j==4){
					info.getCaseTypemap().put(content, null);
					tc.setTypeName(content);
				}else if(j==5){
					info.getCasePrimap().put(content, null);
					tc.setPriName(content);
				}
			}
			info.getCaseList().add(tc);
		}
		return info;
	}


	private String jsonProtect(String value) {
		value = value.replaceAll("=", "\uff1d");
		value = value.replaceAll("\\$", "\uff04");
		value = value.replaceAll("\\^", "\uff3e");
		value = value.replaceAll("'", "\uff07");
		value = value.replaceAll("\\[", "\uff3b");
		value = value.replaceAll("\\]", "\uff3d");
		value = value.replaceAll("\\{", "\uff5b");
		value = value.replaceAll("\\}", "\uff5d");
		value = value.replaceAll("<", "\uff1c");
		value = value.replaceAll(">", "\uff1e");
		value = value.replaceAll("\"", "\u201d");
		//System.out.println(value);
		return value;
	}

	private boolean isReview(String taskId) {
		//return true;
		String flwHql = "select new TestFlowInfo(testFlowCode) from TestFlowInfo where taskId=? and testFlowCode=9";
		List flwList = caseService.findByHql(flwHql, taskId);
		if (flwList == null || flwList.size() == 0) {
			return false;
		} else {
			return true;
		}

	}

	public void impCaseExexute(ImpCaseInfo impCaseInfo) {

		OutlineInfo rootNode = this.getRootNode();
		List<CaseType> caseTypeList = this.buildCaseNewBaseData(
				impCaseInfo.getCaseTypemap(), CaseType.class);
		List<CasePri> casePriList = this.buildCaseNewBaseData(
				impCaseInfo.getCasePrimap(), CasePri.class);
		Map<String, Map<String, OutlineInfo>> allNodemap = this.prepareImpCase(
				impCaseInfo.getModeMap(), rootNode.getModuleName());
		Map<String, CasePri> casePriMap = new HashMap<String, CasePri>(
				casePriList.size());

		Map<String, CaseType> caseTypeMap = new HashMap<String, CaseType>(
				casePriList.size());
		for (CaseType ct : caseTypeList) {
			caseTypeMap.put(ct.getTypeName(), ct);
		}
		for (CasePri cp : casePriList) {
			casePriMap.put(cp.getTypeName(), cp);
		}
		outLineBlh.addImportNodes(allNodemap, rootNode, casePriMap,
				caseTypeMap, impCaseInfo.getCaseList());

	}

	private OutlineInfo isExistsNode(List<OutlineInfo> list,
			String currNodeName, int level,String superNodeName,Map<Long ,OutlineInfo> parentMap) {
		 
		for (OutlineInfo out : list) {
			if (out.getModuleLevel() == level
					&& currNodeName.equals(out.getModuleName())) {
				OutlineInfo currParent = parentMap.get(out.getSuperModuleId());
				if(currParent==null){
					currParent = this.getParentNode(out.getSuperModuleId());
					 parentMap.put(out.getSuperModuleId(),currParent);
				}
				if(currParent.getModuleName().equals(superNodeName)){
					return out;
				}
				
			}
		}

		return null;
	}

	private OutlineInfo getParentNode(Long parentmoduleId) {

		String hql = " from OutlineInfo where moduleId=? ";
		return (OutlineInfo) outLineService.findByNoCache(hql, parentmoduleId).get(0);

	}
	private OutlineInfo getRootNode() {

		String hql = "select new OutlineInfo(moduleId,superModuleId,isleafNode, moduleName,moduleState,moduleNum) from OutlineInfo where taskId=? and superModuleId =0 ";
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		return (OutlineInfo) outLineService.findByNoCache(hql, taskId).get(0);

	}

	private Map<String, Map<String, OutlineInfo>> prepareImpCase(
			Map<String, String> modeMap, String rootName) {
		List<OutlineInfo> list = loadAllOutLine();
		Map<String, Map<String, OutlineInfo>> allNodemap = new HashMap<String, Map<String, OutlineInfo>>(
				8);
		Map<String, OutlineInfo> map2 = new HashMap<String, OutlineInfo>();
		Map<String, OutlineInfo> map3 = new HashMap<String, OutlineInfo>();
		Map<String, OutlineInfo> map4 = new HashMap<String, OutlineInfo>();
		Map<String, OutlineInfo> map5 = new HashMap<String, OutlineInfo>();
		Map<String, OutlineInfo> map6 = new HashMap<String, OutlineInfo>();
		Map<String, OutlineInfo> map7 = new HashMap<String, OutlineInfo>();
		Map<String, OutlineInfo> map8 = new HashMap<String, OutlineInfo>();
		Map<String, OutlineInfo> map9 = new HashMap<String, OutlineInfo>();
		allNodemap.put("2", map2);
		allNodemap.put("3", map3);
		allNodemap.put("4", map4);
		allNodemap.put("5", map5);
		allNodemap.put("6", map6);
		allNodemap.put("7", map7);
		allNodemap.put("8", map8);
		allNodemap.put("9", map9);
		Iterator<?> it = modeMap.entrySet().iterator();
		Map<Long ,OutlineInfo> parentMap = new HashMap<Long ,OutlineInfo>();
		while (it.hasNext()) {
			@SuppressWarnings("unchecked")
			Map.Entry<String, String> me = (Map.Entry<String, String>) it
					.next();
			String caseNode = me.getKey();
			String[] caseNodeArr = caseNode.split("/");
			String superNodeName = null;
			for (int i = 0; i < (caseNodeArr.length < 9 ? caseNodeArr.length
					: 9); i++) {
				if (i == 0) {
					superNodeName = rootName;
				} else {
					superNodeName = caseNodeArr[i - 1];
				}
				OutlineInfo out = this
						.isExistsNode(list, caseNodeArr[i], i + 2,superNodeName,parentMap);
				if (out == null) {
					out = new OutlineInfo();
					out.setModuleLevel(i + 2);
					out.setModuleName(caseNodeArr[i]);
				}
				out.setSuperModuleName(superNodeName);
				this.setOutlineInfo(allNodemap, i + 2, out, me.getKey());
			}

		}
		return allNodemap;
	}

	private void setOutlineInfo(
			Map<String, Map<String, OutlineInfo>> allNodemap, int level,
			OutlineInfo out, String caseModelInfo) {

		Map<String, OutlineInfo> map = allNodemap.get(String.valueOf(level));
		map.put(caseModelInfo, out);
	}

	private <T> List<T> buildCaseNewBaseData(Map<String, String> map,
			Class<T> clasz) {
		String hql = "select new " + clasz.getSimpleName()
				+ "(typeId, typeName) from " + clasz.getSimpleName()
				+ " where (compId=? or compId=1) ";
		String companyId = SecurityContextHolderHelp.getCompanyId();
		List<T> currList = testBaseSetService.findByHql(hql, companyId);
		List<T> newList = new ArrayList<T>();
		Iterator it = map.entrySet().iterator();
		Map.Entry<String, String> me = null;
		while (it.hasNext()) {
			boolean haveAdd = false;
			me = (Map.Entry<String, String>) it.next();
			for (TypeDefine caseType : (List<TypeDefine>) currList) {
				if (me.getKey().equals(caseType.getTypeName())) {
					haveAdd = true;
					break;
				}
			}
			if (!haveAdd) {
				if (clasz.getSimpleName().equals("CaseType")) {
					CaseType ct = new CaseType();
					ct.setCompId(companyId);
					ct.setRemark("导入用例自动生成 ");
					ct.setIsDefault(0);
					ct.setStatus("1");
					ct.setTypeName(me.getKey());
					newList.add((T) ct);
				} else {
					CasePri ct = new CasePri();
					ct.setCompId(companyId);
					ct.setRemark("导入用例自动生成");
					ct.setIsDefault(0);
					ct.setStatus("1");
					ct.setTypeName(map.get(me.getKey()));
					newList.add((T) ct);
				}

			}
		}
		newList.addAll(currList);
		return newList;
	}


	private InputStream getExpFileInputStream(String time,int userInt) {

		InputStream studentInfoStream = ServletActionContext
				.getServletContext().getResourceAsStream(
						File.separator + "mypmUserFiles" + File.separator
								+ "expFile" + File.separator + "caseExport_"
								+ time + userInt+".xls");
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

	public TestBaseSetService getTestBaseSetService() {
		return testBaseSetService;
	}

	public void setTestBaseSetService(TestBaseSetService testBaseSetService) {
		this.testBaseSetService = testBaseSetService;
	}

	public OutLineManagerService getOutLineService() {
		return outLineService;
	}

	public void setOutLineService(OutLineManagerService outLineService) {
		this.outLineService = outLineService;
	}



	public CaseManagerService getCaseService() {
		return caseService;
	}

	public void setCaseService(CaseManagerService caseService) {
		this.caseService = caseService;
	}

	public OutLineManagerBlh getOutLineBlh() {
		return outLineBlh;
	}

	public void setOutLineBlh(OutLineManagerBlh outLineBlh) {
		this.outLineBlh = outLineBlh;
	}

}
