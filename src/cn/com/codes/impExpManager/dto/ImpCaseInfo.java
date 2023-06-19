package cn.com.codes.impExpManager.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.codes.object.TestCaseInfo;


public class ImpCaseInfo {
	
	private List<TestCaseInfo> caseList;
	private Map<String, String> caseTypemap ;
	private Map<String, String> casePrimap;
	private Map<String, String> modeMap ;
	public List<TestCaseInfo> getCaseList() {
		if(caseList==null){
			caseList = new ArrayList<TestCaseInfo>();
		}
		return caseList;
	}
	public void setCaseList(List<TestCaseInfo> caseList) {
		
		this.caseList = caseList;
	}
	public Map<String, String> getCaseTypemap() {
		if(caseTypemap==null){
			caseTypemap = new HashMap<String, String>();
		}
		return caseTypemap;
	}
	public void setCaseTypemap(Map<String, String> caseTypemap) {
		
		this.caseTypemap = caseTypemap;
	}
	public Map<String, String> getCasePrimap() {
		if(casePrimap==null){
			casePrimap = new HashMap<String, String>();
		}
		return casePrimap;
	}
	public void setCasePrimap(Map<String, String> casePrimap) {
		this.casePrimap = casePrimap;
	}
	public Map<String, String> getModeMap() {
		if(modeMap==null){
			modeMap = new HashMap<String, String>();
		}
		return modeMap;
	}
	public void setModeMap(Map<String, String> modeMap) {
		this.modeMap = modeMap;
	}

}
