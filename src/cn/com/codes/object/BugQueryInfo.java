package cn.com.codes.object;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.com.codes.common.util.StringUtils;
import cn.com.codes.object.Queryinfo;

public class BugQueryInfo extends Queryinfo {
	
	//注意因BugQueryInfo中不含taskId 用用库中查出的Queryinfo查询时，需要在praValues中put进去
	private String onlyMyCondSec ;//与我相关的BUG HQL片段
	private  Map currPraValues; 
	//因为可以查询并保存当前查询，如查询为与我相关的时，含有与上面片段的值，且它不用保存库里所以又中些属性
	 
	private  static StringBuffer hqlSelectData = new StringBuffer("select new BugBaseInfo(");
	static{
//		hqlSelectData.append(" bugId,bugDesc,bugGradeId,bugFreqId,bugOccaId,")
//		   .append(" bugTypeId,priId, geneCauseId,reptDate,msgFlag,")
//		   .append(" relaCaseFlag, moduleId, testOwnerId,currFlowCd,")
//		   .append(" currHandlerId,currStateId,nextFlowCd)")
//		   .append(" from BugBaseInfo b  ");
		hqlSelectData.append(" bugId,bugDesc,bugGradeId,bugFreqId,bugOccaId,")
		   .append(" bugTypeId,priId, devOwnerId,reptDate,msgFlag,")
		   .append(" relaCaseFlag, moduleId, testOwnerId,currFlowCd,")
		   .append(" currHandlerId,currStateId,nextFlowCd,bugReptId,taskId)")
		   .append(" from BugBaseInfo b  ");
	}
	
	public BugQueryInfo(){
		
	}
	public BugQueryInfo(Long queryId, Integer onlyMe, String paraValueStr,String taskId,String queryName) {
		super(queryId, onlyMe, paraValueStr,taskId,queryName);
	}
	
	public BugQueryInfo(Long queryId, Integer onlyMe,String taskId,String queryName) {
		super(queryId, onlyMe,taskId,queryName);
	}
	public String getOnlyMyCondSec() {
		return onlyMyCondSec;
	}

	public void setOnlyMyCondSec(String onlyMyCondSec) {
		this.onlyMyCondSec = onlyMyCondSec;
	}

	public Map getCurrPraValues() {
		return currPraValues;
	}

	public void setCurrPraValues(Map currPraValues) {
		this.currPraValues = currPraValues;
	}
	public String getCurrHql(){	
		if(this.getHqlCondiStr().indexOf(":bugRecurDesc")>0){
			this.setHqlCondiStr(this.getHqlCondiStr().replace(":bugRecurDesc", " :bugDesc"));
		}
		return this.getHqlCondiStr() +(onlyMyCondSec==null?" order by currHandlDate desc":onlyMyCondSec);
	}
	
	public String toJson(){
		
		return null;
	}
	
	public static StringBuffer getHqlSelectData() {
		return hqlSelectData;
	}
	
	public String praValues2Str(){
		if(this.getPraValues()==null||this.getPraValues().isEmpty()){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		Iterator it = this.getPraValues().entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, Object> me = (Map.Entry<String, Object>)it.next();
			if(this.getTaskId()!=null){
				if(me.getKey().equals("reptDate")||me.getKey().equals("repEndDate")){
					sb.append("^").append(me.getKey()).append("=").append(StringUtils.formatShortDate((Date)me.getValue()));
				}else{
					sb.append("^").append(me.getKey()).append("=").append(me.getValue().toString());
				}
			}else{
				if(me.getKey().indexOf("taskId")<0&&me.getKey().indexOf("moduleId")<0){
					if(me.getKey().equals("reptDate")||me.getKey().equals("repEndDate")){
						sb.append("^").append(me.getKey()).append("=").append(StringUtils.formatShortDate((Date)me.getValue()));
					}else{
						sb.append("^").append(me.getKey()).append("=").append(me.getValue().toString());
					}
				}				
			}
		}
		return sb.substring(1).toString();
	}
	private Object typeConver(String[] mepEntry ){
		if(!mepEntry[0].equals("bugDesc")&&!mepEntry[0].equals("devOwnerId")
				&&!mepEntry[0].equals("taskId")&&!mepEntry[0].equals("testOwnerId")
				&&!mepEntry[0].equals("reptDate")&&!mepEntry[0].equals("repEndDate")&&!mepEntry[0].equals("nextOwnerId")){
			
			if(mepEntry[0].equals("currStateId")||mepEntry[0].equals("relaCaseFlag")){
				return new Integer((String)mepEntry[1]);
			}
			return new Long((String)mepEntry[1]);
		}else if(mepEntry[0].equals("reptDate")||mepEntry[0].equals("repEndDate")){
			try {
				return StringUtils.parseShortDate(mepEntry[1]);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}else{
			return mepEntry[1];
		}
	}
	public Map praValueStr2Map(){
		if(getParaValueStr()==null||"".equals(getParaValueStr().trim())){
			return null;
		}
		Map praValues = new HashMap();
		String[] praVal = getParaValueStr().split("\\^");
		String[] mepEntry = new String[2];
		for(String keyVal :praVal){
			mepEntry = keyVal.split("=");
			//bugRecurDesc为历史数据
			if(mepEntry[0].equals("bugRecurDesc")){
				mepEntry[0]="bugDesc";
			}
			praValues.put(mepEntry[0], typeConver(mepEntry));
		}
		//String taskId = SecurityContextHolderHelp.getCurrTaksId();
		//praValues.put("taskId", taskId);
		//praValues.put("currHandlerId", SecurityContextHolderHelp.getUserId());
		super.setPraValues(praValues);
		return praValues;
	}

}
