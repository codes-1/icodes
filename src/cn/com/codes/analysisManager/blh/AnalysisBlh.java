package cn.com.codes.analysisManager.blh;

import org.apache.log4j.Logger;

import cn.com.codes.analysisManager.dto.AnalysisDto;
import cn.com.codes.analysisManager.service.AnalysisService;
import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.framework.app.blh.BusinessBlh;
import cn.com.codes.framework.app.view.View;
import cn.com.codes.framework.common.JsonUtil;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.jms.CommonMessageListener;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.analysisManager.blh.AnalysisBlh;

public class AnalysisBlh extends BusinessBlh {
	
	private static Logger log = Logger.getLogger(AnalysisBlh.class);
	private AnalysisService analysisService;
	//获取  --分析度量页面
	public View goAnalysisMain(BusiRequestEvent req) throws BaseException {
		return super.getView();
	}
	
	//获取 日提交bug趋势页面
	public View devDayFixTrend(BusiRequestEvent req) throws BaseException {
		return super.getView();
	}
	//获取日编写用例趋势页面
	public View writeCaseDayTrend(BusiRequestEvent req) throws BaseException {
		return super.getView();
	}
	//获取 日提交bug总数页面
	public View commitExistBugDayStat(BusiRequestEvent req) throws BaseException {
		return super.getView();
	}
	//获取 日执行用例趋势以及页面
	public View testerExeCaseDayTrend(BusiRequestEvent req) throws BaseException {
		return super.getView();
	}
	//获取测试人员提交、关闭bug统计页面
	public View testerBugStat(BusiRequestEvent req) throws BaseException {
		return super.getView();
	}
	//获取已关闭bug按周龄统计页面
	public View bugExistWeekStat(BusiRequestEvent req) throws BaseException {
		return super.getView();
	}
	//获取待处理bug按周龄统计页面
	public View bugExistWeek4NoFixStat(BusiRequestEvent req) throws BaseException {
		return super.getView();
	}
	//获取bug等级统计页面 
	public View bugGradeStat(BusiRequestEvent req) throws BaseException {
		return super.getView();
	}
	//获取遗留bug分析页面
	public View bugBequeathStat(BusiRequestEvent req) throws BaseException {
		return super.getView();
	}
	//获取bug密度分析页面
	public View bugDensityStat(BusiRequestEvent req) throws BaseException {
		return super.getView();
	}
	
	public View analysisMainList(BusiRequestEvent req) throws BaseException {
		AnalysisDto analysisDto = (AnalysisDto) req.getDto();
		analysisService.goAnalysisMain(analysisDto);
		
		super.writeResult(JsonUtil.toJson(analysisDto.getTreeStr()));
		return super.globalAjax();
	}

	public AnalysisService getAnalysisService() {
		return analysisService;
	}

	public void setAnalysisService(AnalysisService analysisService) {
		this.analysisService = analysisService;
	}
	
	public static boolean canViewRept(){
		if((CommonMessageListener.getReptViewCount())>=(CommonMessageListener.getReptLimitCount())){
			return false;
		}
		return true;
	} 
	//日修复bug趋势
	public View getDevDayFixTrend(BusiRequestEvent req) throws BaseException {
		AnalysisDto analysisDto = (AnalysisDto) req.getDto();
		analysisService.getDevDayFixTrend(analysisDto);
		/*Map<String, String> result = new HashMap<String, String>();*/
		super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
		return super.globalAjax();
	} 
	//日编写用例趋势
	public View getWriteCaseDayTrend(BusiRequestEvent req) throws BaseException {
		AnalysisDto analysisDto = (AnalysisDto) req.getDto();
		analysisService.getWriteCaseDayTrend(analysisDto); 
		super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
		return super.globalAjax();
	}
	//日执行用例趋势以及明细
		public View getTesterExeCaseDayTrend(BusiRequestEvent req) throws BaseException {
			AnalysisDto analysisDto = (AnalysisDto) req.getDto();
			analysisService.getTesterExeCaseDayTrend(analysisDto); 
			super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
			return super.globalAjax();
	}
    //日提交bug及总数趋势01
		public View getCommitExistBugAll(BusiRequestEvent req) throws BaseException {
			AnalysisDto analysisDto = (AnalysisDto) req.getDto();
			analysisService.getCommitExistBugAll(analysisDto); 
			super.writeResult(JsonUtil.toJson(analysisDto.getExistAllBugData()));
			return super.globalAjax();
	}
		
    //日提交bug及总数趋势02
		public View getCommitExistBugCom(BusiRequestEvent req) throws BaseException {
			AnalysisDto analysisDto = (AnalysisDto) req.getDto();
			analysisService.getCommitExistBugCom(analysisDto); 
			super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
			return super.globalAjax();
	} 
	//获取测试人员
	public View getTester(BusiRequestEvent req) throws BaseException {
		AnalysisDto analysisDto = (AnalysisDto) req.getDto();
		analysisService.getTester(analysisDto); 
		super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
		return super.globalAjax();
	}

	//获取测试人员提交关闭bug统计
	public View getTesterBugStat(BusiRequestEvent req) throws BaseException {
			AnalysisDto analysisDto = (AnalysisDto) req.getDto();
			analysisService.getTesterBugStat(analysisDto); 
			super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
			return super.globalAjax();
		}
	//获取测试人员提交关闭bug统计02
		public View getTesterBugStatClose(BusiRequestEvent req) throws BaseException {
				AnalysisDto analysisDto = (AnalysisDto) req.getDto();
				analysisService.getTesterBugStatClose(analysisDto); 
				super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
				return super.globalAjax();
			}
	//获取已关闭bug按周龄统计
	public View getBugExistWeekStat(BusiRequestEvent req) throws BaseException {
			AnalysisDto analysisDto = (AnalysisDto) req.getDto();
			analysisService.getBugExistWeekStat(analysisDto); 
			super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
			return super.globalAjax();
		}
	//获取待处理bug按周龄统计01
	public View getBugExistWeek4NoFixStat(BusiRequestEvent req) throws BaseException {
			AnalysisDto analysisDto = (AnalysisDto) req.getDto();
			analysisService.getBugExistWeek4NoFixStat(analysisDto); 
			super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
			return super.globalAjax();
		}
	//获取待处理bug按周龄统计02
	public View getBugExistWeek4NoFixStatAbsolute(BusiRequestEvent req) throws BaseException {
			AnalysisDto analysisDto = (AnalysisDto) req.getDto();
			analysisService.getBugExistWeek4NoFixStatAbsolute(analysisDto); 
			super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
			return super.globalAjax();
		}
	//获取bug等级统计
	public View getBugGradeStat(BusiRequestEvent req) throws BaseException {
			AnalysisDto analysisDto = (AnalysisDto) req.getDto();
			analysisService.getBugGradeStat(analysisDto); 
			super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
			return super.globalAjax();
		}
	//获取遗留bug分析
	public View getBugBequeathStat(BusiRequestEvent req) throws BaseException {
			AnalysisDto analysisDto = (AnalysisDto) req.getDto();
			analysisService.getBugBequeathStat(analysisDto); 
			super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
			return super.globalAjax();
		}
	//获取bug密度分析
	public View getBugDensityStat(BusiRequestEvent req) throws BaseException {
			AnalysisDto analysisDto = (AnalysisDto) req.getDto();
			analysisService.getBugDensityStat(analysisDto); 
			super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
			return super.globalAjax();
		}
	//获取bug密度分析02 
		public View getBugDensityStatType(BusiRequestEvent req) throws BaseException {
				AnalysisDto analysisDto = (AnalysisDto) req.getDto();
				analysisService.getBugDensityStatType(analysisDto); 
				super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
				return super.globalAjax();
			}
	//获取bug密度分析03 
		public View getBugDensityStatBugType(BusiRequestEvent req) throws BaseException {
				AnalysisDto analysisDto = (AnalysisDto) req.getDto();
				analysisService.getBugDensityStatBugType(analysisDto); 
				super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
				return super.globalAjax();
			}
	//获取--提交|打开|待处理|修改|关闭BUG趋势
	public View reptFixCloseDayTrend(BusiRequestEvent req) throws BaseException {
		return super.getView();
	}
	
	public View getReptFixCloseDayTrend(BusiRequestEvent req) throws BaseException {
		AnalysisDto analysisDto = (AnalysisDto) req.getDto();
		analysisService.getReptFixCloseDayTrend(analysisDto);
		super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
		return super.globalAjax();
	}
	
	//获取--日提交|关闭BUG趋势
	public View testerDayCommitTrend(BusiRequestEvent req) throws BaseException {
		return super.getView();
	}
	
	public View getTesterDayCommitTrend(BusiRequestEvent req) throws BaseException {
		AnalysisDto analysisDto = (AnalysisDto) req.getDto();
		analysisService.getTesterDayCommitTrend(analysisDto);
		super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
		return super.globalAjax();
	}
	
	public View getTesterDayCommitTrendForClosed(BusiRequestEvent req) throws BaseException {
		AnalysisDto analysisDto = (AnalysisDto) req.getDto();
		analysisService.getTesterDayCommitTrendForClosed(analysisDto);
		super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
		return super.globalAjax();
	}
	
	//获取--版本间提交及BUG总数趋势
	public View commitExistBugBuildStat(BusiRequestEvent req) throws BaseException {
		return super.getView();
	}
	
	public View getCommitExistBugBuildStat(BusiRequestEvent req) throws BaseException {
		AnalysisDto analysisDto = (AnalysisDto) req.getDto();
		analysisService.getCommitExistBugBuildStat(analysisDto);
		super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
		return super.globalAjax();
	} 
	
	//开发人员待改|BUG修改统计
		public View bugFixPersonStat(BusiRequestEvent req) throws BaseException {
			return super.getView();
		}
		//开发人员待修BUG统计
		public View getBugFixPersonStat(BusiRequestEvent req) throws BaseException {
			AnalysisDto analysisDto = (AnalysisDto) req.getDto();
			analysisService.getBugFixPersonStat(analysisDto);
			super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
			return super.globalAjax();
		}
		
		//开发人员修改BUG分析
		public View getDevFixDataSet(BusiRequestEvent req) throws BaseException {
			AnalysisDto analysisDto = (AnalysisDto) req.getDto();
			analysisService.getDevFixDataSet(analysisDto);
			super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
			return super.globalAjax();
		}
		
		//已关闭BUG按天龄期统计
		public View bugExistDayStat(BusiRequestEvent req) throws BaseException {
			return super.getView();
		}
		
		public View getBugExistDayStat(BusiRequestEvent req) throws BaseException {
			AnalysisDto analysisDto = (AnalysisDto) req.getDto();
			analysisService.getBugExistDayStat(analysisDto);
			super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
			return super.globalAjax();
		}
		
		//待处理BUG按天龄期统计
		public View bugExistDay4NoFixStat(BusiRequestEvent req) throws BaseException {
			return super.getView();
		}
		//待处理BUG按天龄期统计
		public View getBugExistDay4NoFixStat(BusiRequestEvent req) throws BaseException {
			AnalysisDto analysisDto = (AnalysisDto) req.getDto();
			analysisService.getBugExistDay4NoFixStat(analysisDto);
			super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
			return super.globalAjax();
		}
		//待处理BUG按天绝对龄期统计
		public View getBugExistDay4NoFixStatAbsolute(BusiRequestEvent req) throws BaseException {
			AnalysisDto analysisDto = (AnalysisDto) req.getDto();
			analysisService.getBugExistDay4NoFixStatAbsolute(analysisDto);
			super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
			return super.globalAjax();
		}
		
		
		//BUG类型统计
		public View bugTypeStat(BusiRequestEvent req) throws BaseException {
			return super.getView();
		}
		
		public View getBugTypeStat(BusiRequestEvent req) throws BaseException {
			AnalysisDto analysisDto = (AnalysisDto) req.getDto();
			analysisService.getBugTypeStat(analysisDto);
			super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
			return super.globalAjax();
		}
		
		//BUG引入阶段分析
		public View bugImpPhaseStat(BusiRequestEvent req) throws BaseException {
			return super.getView();
		}
		
		public View getBugImpPhaseStat(BusiRequestEvent req) throws BaseException {
			AnalysisDto analysisDto = (AnalysisDto) req.getDto();
			analysisService.getBugImpPhaseStat(analysisDto);
			super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
			return super.globalAjax();
		}
		
		//测试人员BUG质量分析
		public View testerBugQuality(BusiRequestEvent req) throws BaseException {
			return super.getView();
		}
		
		public View getTesterBugQuality(BusiRequestEvent req) throws BaseException {
			AnalysisDto analysisDto = (AnalysisDto) req.getDto();
			analysisService.getTesterBugQuality(analysisDto);
			super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
			return super.globalAjax();
		}
		
		//测试需求项BUG分布明细
		public View bugModuleDistbuStat(BusiRequestEvent req) throws BaseException {
			return super.getView();
		}
		
		//测试需求项BUG分布明细--BUG数
		public View getBugModuleDistbuForNum(BusiRequestEvent req) throws BaseException {
			AnalysisDto analysisDto = (AnalysisDto) req.getDto();
			analysisService.getBugModuleDistbuForNum(analysisDto);
			super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
			return super.globalAjax();
		}
		
		//测试需求项BUG分布明细--BUG等级
		public View getBugModuleDistbuForLevel(BusiRequestEvent req) throws BaseException {
			AnalysisDto analysisDto = (AnalysisDto) req.getDto();
			analysisService.getBugModuleDistbuForLevel(analysisDto);
			super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
			return super.globalAjax();
		}
		
		//测试需求项BUG分布明细--BUG类型
		public View getBugModuleDistbuForType(BusiRequestEvent req) throws BaseException {
			AnalysisDto analysisDto = (AnalysisDto) req.getDto();
			analysisService.getBugModuleDistbuForType(analysisDto);
			super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
			return super.globalAjax();
		}
		
		//BUG状态分布统计
		public View bugStatusDistbuStat(BusiRequestEvent req) throws BaseException {
			return super.getView();
		}
		
		public View getBugStatusDistbuStat(BusiRequestEvent req) throws BaseException {
			AnalysisDto analysisDto = (AnalysisDto) req.getDto();
			analysisService.getBugStatusDistbuStat(analysisDto);
			super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
			return super.globalAjax();
		}
		
		//切换项目时，将【分析度量】查询所需的数据放入session中
		public View putAnanysisSession(BusiRequestEvent req) {
			AnalysisDto dto = super.getDto(AnalysisDto.class, req);
			if(dto.getAnalyProNum()!=null&&!"".equals(dto.getAnalyProNum())){
	    		SecurityContextHolderHelp.setCurrTaksAnalyProNum(dto.getAnalyProNum());
	    	}
			if(dto.getAnalyProjectName()!=null&&!"".equals(dto.getAnalyProjectName())){
	    		SecurityContextHolderHelp.setCurrTaksProName(dto.getAnalyProjectName());
	    	}
			if(dto.getTaskId()!=null&&!"".equals(dto.getTaskId())){
	    		SecurityContextHolderHelp.setCurrTaksId(dto.getTaskId());
	    	}
			super.writeResult("success");
			return super.globalAjax();
		}
		
		//责任人分析
		public View chargeOwner(BusiRequestEvent req) throws BaseException {
			return super.getView();
		}
		
		public View getChargeOwner(BusiRequestEvent req) throws BaseException {
			AnalysisDto analysisDto = (AnalysisDto) req.getDto();
			analysisService.getChargeOwner(analysisDto);
			super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
			return super.globalAjax();
		}
		
		//责任人引入原因分析
		public View importCase(BusiRequestEvent req) throws BaseException {
			return super.getView();
		}
		
		public View getImportCaseByProject(BusiRequestEvent req) throws BaseException {
			AnalysisDto analysisDto = (AnalysisDto) req.getDto();
			analysisService.getImportCaseByProject(analysisDto);
			super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
			return super.globalAjax();
		}
		
		//简要统计
		public View bugSummary(BusiRequestEvent req) throws BaseException {
			return super.getView();
		}
		
		//简要统计--截止到时间末BUG状态分布情况
		public View getBeforeOpenBugSummary(BusiRequestEvent req) throws BaseException {
			AnalysisDto analysisDto = (AnalysisDto) req.getDto();
			analysisService.getBeforeOpenBugSummary(analysisDto);
			super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
			return super.globalAjax();
		}
		
		//简要统计--时间段内新增、修改和关闭BUG概况
		public View getNewFixCloseBugSummary(BusiRequestEvent req) throws BaseException {
			AnalysisDto analysisDto = (AnalysisDto) req.getDto();
			analysisService.getNewFixCloseBugSummary(analysisDto);
			super.writeResult(JsonUtil.toJson(analysisDto.getAlsResult()));
			return super.globalAjax();
		}
}
