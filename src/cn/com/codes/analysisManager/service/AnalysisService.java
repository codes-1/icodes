package cn.com.codes.analysisManager.service;

import cn.com.codes.analysisManager.dto.AnalysisDto;
import cn.com.codes.framework.app.services.BaseService;

public interface AnalysisService extends BaseService {
	
	public void goAnalysisMain(AnalysisDto analysisDto);
	
	//public void goAnalysisMain4Mysql(AnalysisDto analysisDto);
	
	public void getTesterExeCaseDayTrend(AnalysisDto analysisDto);//chy
	
	public void getDevDayFixTrend(AnalysisDto analysisDto);//chy
	
	public void getWriteCaseDayTrend(AnalysisDto analysisDto);//chy
	
	public void getTester(AnalysisDto analysisDto);//chy
	
	public void getCommitExistBugCom(AnalysisDto analysisDto);//chy
	
	public void getCommitExistBugAll(AnalysisDto analysisDto);//chy
	
	public void getReptFixCloseDayTrend(AnalysisDto analysisDto);
	
	public void getTesterDayCommitTrend(AnalysisDto analysisDto);
	
	public void getTesterDayCommitTrendForClosed(AnalysisDto analysisDto);
	
	public void getCommitExistBugBuildStat(AnalysisDto analysisDto);
	
	public void getBugFixPersonStat(AnalysisDto analysisDto);
	
	public void getBugExistDayStat(AnalysisDto analysisDto);
	
	public void getBugExistDay4NoFixStatAbsolute(AnalysisDto analysisDto);
	
	public void getBugExistDay4NoFixStat(AnalysisDto analysisDto);
	
	public void getBugTypeStat(AnalysisDto analysisDto);
	
	public void getBugImpPhaseStat(AnalysisDto analysisDto);
	
	public void getTesterBugQuality(AnalysisDto analysisDto);
	
	public void getBugStatusDistbuStat(AnalysisDto analysisDto);
	
	//chy
	public void getTesterBugStat(AnalysisDto analysisDto);
	
	public void getTesterBugStatClose(AnalysisDto analysisDto);
	
	public void getBugExistWeekStat(AnalysisDto analysisDto);
	
	public void getBugExistWeek4NoFixStat(AnalysisDto analysisDto);
	
	public void getBugExistWeek4NoFixStatAbsolute(AnalysisDto analysisDto);
	
	public void getBugGradeStat(AnalysisDto analysisDto);
	
	public void getBugBequeathStat(AnalysisDto analysisDto);
	
	public void getBugDensityStat(AnalysisDto analysisDto);
	
	public void getBugDensityStatType(AnalysisDto analysisDto);
	
	public void getBugDensityStatBugType(AnalysisDto analysisDto);
	
	public void getBugModuleDistbuForNum(AnalysisDto analysisDto);
	
	public void getBugModuleDistbuForLevel(AnalysisDto analysisDto);
	
	public void getBugModuleDistbuForType(AnalysisDto analysisDto);
	
	public void getDevFixDataSet(AnalysisDto analysisDto);
	
	public void getChargeOwner(AnalysisDto analysisDto);
	
	public void getImportCaseByProject(AnalysisDto analysisDto);
	
	public void getBeforeOpenBugSummary(AnalysisDto analysisDto);
	
	public void getNewFixCloseBugSummary(AnalysisDto analysisDto);
	
}
