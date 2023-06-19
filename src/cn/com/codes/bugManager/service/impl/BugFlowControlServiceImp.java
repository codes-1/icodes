package cn.com.codes.bugManager.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.codes.bugManager.blh.BugFlowConst;
import cn.com.codes.bugManager.dto.BugManagerDto;
import cn.com.codes.bugManager.dto.ControlInfo;
import cn.com.codes.bugManager.service.BugCommonService;
import cn.com.codes.bugManager.service.BugFlowControlService;
import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.common.service.DrawHtmlListDateService;
import cn.com.codes.framework.app.services.BaseServiceImpl;
import cn.com.codes.framework.common.HtmlListComponent;
import cn.com.codes.framework.common.ListObject;
import cn.com.codes.framework.exception.DataBaseException;
import cn.com.codes.object.BugBaseInfo;
import cn.com.codes.object.TypeDefine;
import cn.com.codes.object.User;
import cn.com.codes.testTaskManager.dto.CurrTaskInfo;
import cn.com.codes.testTaskManager.service.TestTaskDetailService;

public class BugFlowControlServiceImp extends BaseServiceImpl implements BugFlowControlService {

	private BugCommonService bugCommonService;
	private TestTaskDetailService testTaskService ;
	private DrawHtmlListDateService drawHtmlListDateService;

	public void upInitContl(CurrTaskInfo currTaskInfo,BugBaseInfo bug){
		Integer nextFlowCd = bug.getNextFlowCd();
		String hql="select new User(id,name ,loginName) from User where id in (:ids)";
		Map praValues = new HashMap(1);
		String myId = SecurityContextHolderHelp.getUserId();
		List<User> nextFlowUser = null;
		if((nextFlowCd==1||nextFlowCd==8)&&(bug.getCurrHandlerId().equals(myId)
				||currTaskInfo.getRoleInTask().indexOf("8")>=0)){
			praValues.put("ids", this.strArr2List(currTaskInfo.getTestAndLdStr().split(",")));
			nextFlowUser = this.findByHqlWithValuesMap(hql, praValues, false);
			List<ListObject> actorList = drawHtmlListDateService.convertUser2ListObj(nextFlowUser);
			bug.setTestSelStr(HtmlListComponent.toSelectStr(actorList, "$"));
		}else if(nextFlowCd==2&&(bug.getCurrHandlerId().equals(myId)
				||currTaskInfo.getRoleInTask().indexOf("8")>=0)){
			praValues.put("ids", this.strArr2List(currTaskInfo.getTestChkAndLdStr().split(",")));
			nextFlowUser = this.findByHqlWithValuesMap(hql, praValues, false);
			List<ListObject> actorList = drawHtmlListDateService.convertUser2ListObj(nextFlowUser);
			bug.setTestSelStr(HtmlListComponent.toSelectStr(actorList, "$"));
		}else if(nextFlowCd==3&&bug.getCurrHandlerId().equals(myId)){
			praValues.put("ids", this.strArr2List(currTaskInfo.getAnalysIdStr().split(",")));
			nextFlowUser = this.findByHqlWithValuesMap(hql, praValues, false);
			List<ListObject> actorList = drawHtmlListDateService.convertUser2ListObj(nextFlowUser);
			bug.setAnalySelStr(HtmlListComponent.toSelectStr(actorList, "$"));
		}else if(nextFlowCd==4&&bug.getCurrHandlerId().equals(myId)){
			praValues.put("ids", this.strArr2List(currTaskInfo.getAssinIdStr().split(",")));
			nextFlowUser = this.findByHqlWithValuesMap(hql, praValues, false);
			List<ListObject> actorList = drawHtmlListDateService.convertUser2ListObj(nextFlowUser);
			bug.setAssignSelStr(HtmlListComponent.toSelectStr(actorList, "$"));
		}else if(nextFlowCd==5&&bug.getCurrHandlerId().equals(myId)){
			praValues.put("ids", this.strArr2List(currTaskInfo.getDevIdStr().split(",")));
			nextFlowUser = this.findByHqlWithValuesMap(hql, praValues, false);
			List<ListObject> actorList = drawHtmlListDateService.convertUser2ListObj(nextFlowUser);
			bug.setDevStr(HtmlListComponent.toSelectStr(actorList, "$"));
		}else if(nextFlowCd==6&&bug.getCurrHandlerId().equals(myId)){
			praValues.put("ids", this.strArr2List(currTaskInfo.getDevChkIdStr().split(",")));
			nextFlowUser = this.findByHqlWithValuesMap(hql, praValues, false);
			List<ListObject> actorList = drawHtmlListDateService.convertUser2ListObj(nextFlowUser);
			bug.setDevStr(HtmlListComponent.toSelectStr(actorList, "$"));
		}else if(nextFlowCd==7&&bug.getCurrHandlerId().equals(myId)){
			praValues.put("ids", this.strArr2List(currTaskInfo.getIntercsIdStr().split(",")));
			nextFlowUser = this.findByHqlWithValuesMap(hql, praValues, false);
			List<ListObject> actorList = drawHtmlListDateService.convertUser2ListObj(nextFlowUser);
			bug.setInterCesSelStr(HtmlListComponent.toSelectStr(actorList, "$"));
		}
	
	}


	public void bugHand(BugManagerDto dto){
		BugBaseInfo bug = dto.getBug();
		//先查出测试流程及当前操作人在项目中的角色
		CurrTaskInfo currTaskInfo = testTaskService.getCurrTaskDetalInfo();
		dto.setTestFlow(currTaskInfo.getTestFlow());
		dto.setRoleInTask(currTaskInfo.getRoleInTask());
		String hql = "from BugBaseInfo where bugId=? and taskId=?";
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		List bugList = bugCommonService.findByHqlRefresh(hql, bug.getBugId(),taskId);
		ControlInfo controlInfo = null;
		if(bugList==null||bugList.size()==0){
			throw new DataBaseException("当前记录己被删除");
		}
		bug = (BugBaseInfo)bugList.get(0);
		bug.setInitState(bug.getCurrStateId());
		dto.setBug(bug);
		dto.setStateName(BugFlowConst.getStateName(dto.getBug().getCurrStateId()));
		bugCommonService.setRelaUser(dto.getBug());
		if("".equals(dto.getRoleInTask())){
			return ;
		}else{
			String myUserId = SecurityContextHolderHelp.getUserId();
			if(bug.getCurrHandlerId().equals(myUserId)){//当前处理人是自己，当前状态为本人没处理前的状态，
				controlInfo = correctMyHandDataInit(currTaskInfo,bug);
			}else{
				controlInfo = flowCtrlHandDataInit(currTaskInfo,bug);
			}
		}
		bug.setStaFlwMemStr(controlInfo.getStaFlwMemStr());
		dto.setStateList(controlInfo.getOptnalStateList());
		this.loadRelaData(dto, controlInfo,currTaskInfo);
		currTaskInfo = null;
		controlInfo = null;
		//处理问题时，要把处理前的nextFlowCd 的值存入currFowCd
	}
	private void loadRelaData(BugManagerDto dto,ControlInfo controlInfo,CurrTaskInfo currTaskInfo){
		bugCommonService.setRelaType(dto.getBug());
		bugCommonService.setActorListData(currTaskInfo, dto.getBug());
		if(dto.getLoadType()==1&&controlInfo!=null&&!controlInfo.isViewFlag()){
			List<TypeDefine> typeList = bugCommonService.getBugListData();
			dto.setTypeList(typeList);		
		}
		bugCommonService.initBugListDate(dto);
		dto.setModuleName(bugCommonService.getMdPathName(dto.getBug().getModuleId(),""));
		this.setCurrOwner(dto);
	}

	private void setCurrOwner(BugManagerDto dto){
		BugBaseInfo bug = dto.getBug();
		if(bug.getCurrStateId()==4||bug.getCurrStateId()==5||bug.getCurrStateId()==14
				||bug.getCurrStateId()==15||bug.getCurrStateId()==22||bug.getCurrStateId()==23
				){
			StringBuffer sb  = new StringBuffer("<font color=\"blue\">处理人</font>:");
			sb.append(bug.getCurrHander().getName()).append("(").append(bug.getCurrHander().getLoginName()).append(")");
			dto.setCurrOwner(sb.toString());
		}
		//String hql="select new User(id,name ,loginName) from User where id ? and companyId=?";
		//String compId = SecurityContextHolderHelp.getCompanyId();
		//String myUserId = SecurityContextHolderHelp.getUserId();
		StringBuffer sb  = new StringBuffer(" 	<font color=\"blue\">等待"); 
		if(bug.getNextFlowCd()==1&&!bug.getCurrHandlerId().equals(bug.getTestOwnerId())){
			dto.setModuleName("处理问题");
			sb.append("</font>: </td>\n");
			sb.append(" <td  class=\"tdtxt\" width=\"160\" style=\"padding:2 0 0 4;\">\n");
			sb.append(bug.getTestOwner().getName()).append("(").append(bug.getTestOwner().getLoginName()).append(")处理");
			dto.setCurrOwner(sb.toString());
		}else if(dto.getBug().getNextFlowCd()==2&&!bug.getCurrHandlerId().equals(bug.getTestOwnerId())){
			dto.setModuleName("测试互验");
			sb.append("</font>:</td>");
			sb.append(" <td  class=\"tdtxt\" width=\"160\" style=\"padding:2 0 0 4;\">\n");
			sb.append(bug.getTestOwner().getName()).append("(").append(bug.getTestOwner().getLoginName()).append(")互验");
			dto.setCurrOwner(sb.toString());
		}else if(dto.getBug().getNextFlowCd()==3&&!bug.getCurrHandlerId().equals(bug.getAnalyseOwnerId())){
			dto.setModuleName("分析问题");
			sb.append("</font>:</td>\n");
			sb.append(" <td  class=\"tdtxt\" width=\"160\" style=\"padding:2 0 0 4;\">\n");
			sb.append(bug.getAnalysOwner().getName()).append("(").append(bug.getAnalysOwner().getLoginName()).append(")分析");
			dto.setCurrOwner(sb.toString());
		}else if(dto.getBug().getNextFlowCd()==4&&!bug.getCurrHandlerId().equals(bug.getAssinOwnerId())){
			dto.setModuleName("分配问题");
			sb.append("</font>:</td>\n");
			sb.append(" <td  class=\"tdtxt\" width=\"160\" style=\"padding:2 0 0 4;\">\n");
			sb.append(bug.getAssinOwner().getName()).append("(").append(bug.getAssinOwner().getLoginName()).append(")分配");
			dto.setCurrOwner(sb.toString());
		}else if(dto.getBug().getNextFlowCd()==5&&!bug.getCurrHandlerId().equals(bug.getDevOwnerId())){
			dto.setModuleName("修改问题");
			sb.append("</font>:</td>\n");
			sb.append(" <td  class=\"tdtxt\" width=\"160\" style=\"padding:2 0 0 4;\">\n");
			sb.append(bug.getDevOwner().getName()).append("(").append(bug.getDevOwner().getLoginName()).append(")处理");
			dto.setCurrOwner(sb.toString());
		}else if(dto.getBug().getNextFlowCd()==6&&!bug.getCurrHandlerId().equals(bug.getDevOwnerId())){
			dto.setModuleName("开发互检");
			sb.append("</font>:</td>\n");
			sb.append(" <td  class=\"tdtxt\" width=\"160\" style=\"padding:2 0 0 4;\">\n");
			sb.append(bug.getDevOwner().getName()).append("(").append(bug.getDevOwner().getLoginName()).append(")处理");
			dto.setCurrOwner(sb.toString());
		}else if(dto.getBug().getNextFlowCd()==7&&!bug.getCurrHandlerId().equals(bug.getIntercessOwnerId())){
			dto.setModuleName("开发负责人仲裁");
			sb.append("</font>:</td>\n");
			sb.append(" <td  class=\"tdtxt\" width=\"160\" style=\"padding:2 0 0 4;\">\n");
			sb.append(bug.getIntecesOwner().getName()).append("(").append(bug.getIntecesOwner().getLoginName()).append(")处理");
			dto.setCurrOwner(sb.toString());
		}else if(dto.getBug().getNextFlowCd()==8&&!bug.getCurrHandlerId().equals(bug.getTestOwnerId())){
			dto.setModuleName("测试确认");
			sb.append("</font>:</td>\n");
			sb.append(" <td  class=\"tdtxt\" width=\"160\" style=\"padding:2 0 0 4;\">\n");
			sb.append(bug.getTestOwner().getName()).append("(").append(bug.getTestOwner().getLoginName()).append(")确认");
			dto.setCurrOwner(sb.toString());
		}		
	}
	//如果下一流程编码小于当前流程编码，处理后，他们互换
	private int getNextFlwCd(BugBaseInfo bug,String[] testFlw){
		
		String myUserId = SecurityContextHolderHelp.getUserId();
		String currHandId = bug.getCurrHandlerId();
		if(bug.getCurrFlowCd()>bug.getNextFlowCd()){//踢皮球发生
			if(currHandId.equals(myUserId)){
				int i=0;
				for(String flw :testFlw){
					if(flw.equals(bug.getCurrFlowCd().toString())){
						break;
					}
					i++;
				}
				return Integer.parseInt(testFlw[i+1]);
			}else{
				return bug.getCurrFlowCd();
			}
		}else{
			if(currHandId.equals(myUserId)){//本人置状态后，再修改状态
				return bug.getNextFlowCd();
			}else{//别人处理，流转到自己名字7的BUG
				int i=0;
				for(String flw :testFlw){
					if(flw.equals(bug.getNextFlowCd().toString())){
						break;
					}
					i++;
				}
				return Integer.parseInt(testFlw[i+1]);
			}			
		}
	}
	

	//处理问题，当前处理人是本人
	private ControlInfo correctMyHandDataInit(CurrTaskInfo currTaskInfo,BugBaseInfo bug){
		
		int initState = bug.getInitState();
		int nextFlowCd = this.getNextFlwCd(bug, currTaskInfo.getTestFlow().split(""));
		int currFlwCd = bug.getCurrFlowCd();
		String myUserId = SecurityContextHolderHelp.getUserId();
		
		ControlInfo controlInfo = new ControlInfo();
		List<ListObject>  optnalState = new ArrayList<ListObject>();
		controlInfo.setOptnalStateList(optnalState);
		boolean handByme =myUserId.equals(bug.getCurrHandlerId());
		if(initState==1&&handByme){
			optnalState.add(new ListObject("3", "重复"));
			optnalState.add(new ListObject("5", "撤销"));	
			optnalState.add(new ListObject("15", "关闭/不再现"));	
			controlInfo.setStaFlwMemStr("3,5,15=1^0");
		}else if(initState==2&&handByme){
			optnalState.add(new ListObject("3", "重复"));
			optnalState.add(new ListObject("4", "无效"));
			optnalState.add(new ListObject("5", "撤销"));
			optnalState.add(new ListObject("6", "不再现/需提供更多信息"));
			StringBuffer staFlwMemSb = new StringBuffer("3,4,5=0^0");
			staFlwMemSb.append("$6=1^testSelStr");
			if(nextFlowCd==3){
				optnalState.add(new ListObject("24", "分析"));
				staFlwMemSb.append("$24=3^analySelStr");
			}else if (nextFlowCd==4){
				optnalState.add(new ListObject("25", "分配"));
				staFlwMemSb.append("$25=4^assignSelStr");
			}else{
				optnalState.add(new ListObject("7", "待改/再现"));
				optnalState.add(new ListObject("8", "待改/不再现"));
				staFlwMemSb.append("$7,8=5^devStr");
			}
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());
		}else if(initState==3&&handByme){
			StringBuffer staFlwMemSb = new StringBuffer();
				if(currFlwCd==2){
					optnalState.add(new ListObject("2", "修正/描述不当"));
					optnalState.add(new ListObject("4", "无效"));
					optnalState.add(new ListObject("5", "撤销"));
					optnalState.add(new ListObject("6", "不再现/需提供更多信息"));
					staFlwMemSb.append("3,4,5=1^0");
					staFlwMemSb.append("$6=1^testSelStr");
					if(nextFlowCd==3){
						optnalState.add(new ListObject("24", "分析"));
						staFlwMemSb.append("$24=3^analySelStr");
					}else if (nextFlowCd==4){
						optnalState.add(new ListObject("25", "分配"));;
						staFlwMemSb.append("$25=4^assignSelStr");
					}else{
						optnalState.add(new ListObject("7", "待改/再现"));
						optnalState.add(new ListObject("8", "待改/不再现"));
						staFlwMemSb.append("$7,8=5^devStr");
					};
					controlInfo.setStaFlwMemStr(staFlwMemSb.toString());
				}else if(currFlwCd==3){
					optnalState.add(new ListObject("5", "撤销"));
					optnalState.add(new ListObject("24", "分析"));
				}else if(currFlwCd==4){ 
					optnalState.add(new ListObject("5", "撤销"));
					optnalState.add(new ListObject("25", "分配"));				
				}else if(currFlwCd==5){
					optnalState.add(new ListObject("5", "撤销"));
					if(currTaskInfo.getTestFlow().indexOf("2")>=0){
						optnalState.add(new ListObject("7", "待改/再现"));
						optnalState.add(new ListObject("8", "待改/不再现"));
					}else{
						optnalState.add(new ListObject("10", "待改"));
					}					
				}
				controlInfo.setStaFlwMemStr(staFlwMemSb.toString());
		}else if(initState==4&&handByme){
			StringBuffer staFlwMemSb = new StringBuffer();
			if(currFlwCd==2){
				optnalState.add(new ListObject("2", "修正/描述不当"));
				optnalState.add(new ListObject("3", "重复"));
				optnalState.add(new ListObject("5", "撤销"));
				optnalState.add(new ListObject("6", "不再现/需提供更多信息"));
				staFlwMemSb.append("3,4,5=1^0");
				staFlwMemSb.append("$6=1^testSelStr");
				if(nextFlowCd==3){
					optnalState.add(new ListObject("24", "分析"));
					staFlwMemSb.append("$24=3^analySelStr");
				}else if (nextFlowCd==4){
					optnalState.add(new ListObject("25", "分配"));
					staFlwMemSb.append("$25=4^assignSelStr");
				}else{
					optnalState.add(new ListObject("7", "待改/再现"));
					optnalState.add(new ListObject("8", "待改/不再现"));
					staFlwMemSb.append("$7,8=5^devStr");
				}
			}else if(currFlwCd==3){
				optnalState.add(new ListObject("5", "撤销"));
				optnalState.add(new ListObject("24", "分析"));
				optnalState.add(new ListObject("5", "撤销"));
				optnalState.add(new ListObject("25", "分配"));				
			}else if(currFlwCd==5){
				optnalState.add(new ListObject("5", "撤销"));
				if(currTaskInfo.getTestFlow().indexOf("2")>=0){
					optnalState.add(new ListObject("7", "待改/再现"));
					optnalState.add(new ListObject("8", "待改/不再现"));
				}else{
					optnalState.add(new ListObject("10", "待改"));
				}					
			}
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());
		}else if(initState==5&&handByme){
			StringBuffer staFlwMemSb = new StringBuffer();
			if(currFlwCd==1){
				optnalState.add(new ListObject("3", "重复"));
				optnalState.add(new ListObject("15", "关闭/不再现"));	
				controlInfo.setStaFlwMemStr("3,15=1^0");
			}else if(currFlwCd==2){
				optnalState.add(new ListObject("2", "修正/描述不当"));
				optnalState.add(new ListObject("3", "重复"));
				optnalState.add(new ListObject("6", "不再现/需提供更多信息"));
				staFlwMemSb.append("3,4,5,6=1^testSelStr");
			}
			if(nextFlowCd==2){
				optnalState.add(new ListObject("1", "待置"));
				staFlwMemSb.append("$1=2^testSelStr");
				controlInfo.setStaFlwMemStr(staFlwMemSb.toString());
			}else if(nextFlowCd==3){
				optnalState.add(new ListObject("24", "分析"));
				staFlwMemSb.append("$24=3^analySelStr");
			}else if(nextFlowCd==4){
				optnalState.add(new ListObject("25", "分配"));
				staFlwMemSb.append("$25=4^assignSelStr");
			}else if(nextFlowCd==5){
				optnalState.add(new ListObject("7", "待改/再现"));
				optnalState.add(new ListObject("8", "待改/不再现"));
				staFlwMemSb.append("$7,8=5^devStr");
			}
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());
		}else if(initState==6&&handByme){
			StringBuffer staFlwMemSb = new StringBuffer();
			optnalState.add(new ListObject("2", "修正/描述不当"));
			optnalState.add(new ListObject("3", "重复"));
			optnalState.add(new ListObject("4", "无效"));
			optnalState.add(new ListObject("5", "撤销"));
			staFlwMemSb.append("3,4,5=1^0");
			if(nextFlowCd==3){
				optnalState.add(new ListObject("24", "分析"));
				staFlwMemSb.append("$24=3^analySelStr");
			}else if (nextFlowCd==4){
				optnalState.add(new ListObject("25", "分配"));
				staFlwMemSb.append("$25=4^assignSelStr");
			}else{
				optnalState.add(new ListObject("7", "待改/再现"));
				optnalState.add(new ListObject("8", "待改/不再现"));
				staFlwMemSb.append("$7,8=5^devStr");
			}
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());
		}else if((initState==7||initState==8||initState==10)&&handByme){
			StringBuffer staFlwMemSb = new StringBuffer();
			if(currFlwCd==1){
				optnalState.add(new ListObject("3", "重复"));
				optnalState.add(new ListObject("5", "撤销"));
				optnalState.add(new ListObject("15", "关闭/不再现"));	
				controlInfo.setStaFlwMemStr("3,5,15=1^0");
			}else if(currFlwCd==2){
				optnalState.add(new ListObject("2", "修正/描述不当"));
				optnalState.add(new ListObject("3", "重复"));
				optnalState.add(new ListObject("5", "撤销"));
				optnalState.add(new ListObject("6", "不再现/需提供更多信息"));
				optnalState.add(new ListObject("15", "关闭/不再现"));	
				staFlwMemSb.append("2,3,4,5,6,15=1^testSelStr");
			}else if(currFlwCd==3){
				optnalState.add(new ListObject("3", "重复"));
				optnalState.add(new ListObject("4", "无效"));
				staFlwMemSb.append("3,4=1^testSelStr");
			}else if(currFlwCd==4){
				optnalState.add(new ListObject("3", "重复"));
				optnalState.add(new ListObject("4", "无效"));
				staFlwMemSb.append("3,4=1^testSelStr");
			}else if(currFlwCd==6){
				optnalState.add(new ListObject("13", "已改"));
				staFlwMemSb.append("13=5^devStr");
			}
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());
		}else if(initState==9&&handByme){
			optnalState.add(new ListObject("14", "关闭/己解决"));
			controlInfo.setStaFlwMemStr("12=1^0");
		}else if(initState==11&&handByme){
			StringBuffer staFlwMemSb = new StringBuffer();
			optnalState.add(new ListObject("3", "重复"));
			staFlwMemSb.append("3=0^0");
			if(currTaskInfo.getTestFlow().indexOf("6")>=0){
				optnalState.add(new ListObject("18", "交叉验证"));
				staFlwMemSb.append("$18=6^devStr");
			}else{
				optnalState.add(new ListObject("13", "已改"));
			}
			staFlwMemSb.append("$13,16=1^testSelStr");
			optnalState.add(new ListObject("16", "非错"));
			optnalState.add(new ListObject("17", "重分配"));
			optnalState.add(new ListObject("19", "挂起/不计划修改"));
			optnalState.add(new ListObject("20", "挂起/下版本修改"));
			if(currTaskInfo.getTestFlow().indexOf("4")>=0){
				staFlwMemSb.append("$17=4^assignSelStr");
				staFlwMemSb.append("$19,20=7^interCesSelStr");
			}else{
				staFlwMemSb.append("$17,19,20=7^interCesSelStr");
			}
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());
		}else if(initState==12&&handByme){
			optnalState.add(new ListObject("5", "撤销"));
			controlInfo.setStaFlwMemStr("5=1^0");
		}else if(initState==13&&handByme){
			StringBuffer staFlwMemSb = new StringBuffer();
			if(currFlwCd==5){
				optnalState.add(new ListObject("3", "重复"));
				staFlwMemSb.append("$3,13,16=1^testSelStr");
				optnalState.add(new ListObject("16", "非错"));
				optnalState.add(new ListObject("17", "重分配"));
				optnalState.add(new ListObject("19", "挂起/不计划修改"));
				if(currTaskInfo.getTestFlow().indexOf("4")>=0){
					staFlwMemSb.append("$17=4^assignSelStr");
					staFlwMemSb.append("$19=7^interCesSelStr");
				}else{
					staFlwMemSb.append("$17,19=7^interCesSelStr");
				}
			}else if(currFlwCd==6){
				optnalState.add(new ListObject("10", "待改"));
				staFlwMemSb.append("10=5^devStr");
			}
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());			
		}else if(initState==14&&handByme){
			optnalState.add(new ListObject("9", "待改/未解决"));
			controlInfo.setStaFlwMemStr("9=5^devStr");
		}else if(initState==15&&handByme){
			if(nextFlowCd==2){
				optnalState.add(new ListObject("1", "待置"));
				controlInfo.setStaFlwMemStr("1=2^testSelStr");
			}else if(nextFlowCd==5){
				optnalState.add(new ListObject("10", "待改"));
				controlInfo.setStaFlwMemStr("10=5^devStr");
			}
		}else if(initState==16&&handByme){
			StringBuffer staFlwMemSb = new StringBuffer();
			optnalState.add(new ListObject("3", "重复"));
			optnalState.add(new ListObject("11", "挂起/需提供更多信息"));
			staFlwMemSb.append("3,11=1^testSelStr");
			if(currTaskInfo.getTestFlow().indexOf("6")>=0){
				optnalState.add(new ListObject("18", "交叉验证"));
				staFlwMemSb.append("$18=6^devStr");
			}else{
				optnalState.add(new ListObject("13", "已改"));
			}
			staFlwMemSb.append("$13,16=1^testSelStr");
			optnalState.add(new ListObject("17", "重分配"));
			optnalState.add(new ListObject("19", "挂起/不计划修改"));
			optnalState.add(new ListObject("20", "挂起/下版本修改"));
			if(currTaskInfo.getTestFlow().indexOf("4")>=0){
				staFlwMemSb.append("$17=4^assignSelStr");
				staFlwMemSb.append("$19,20=7^interCesSelStr");
			}else{
				staFlwMemSb.append("$17,19,20=7^interCesSelStr");
			}
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());
		}else if(initState==17&&handByme){
			StringBuffer staFlwMemSb = new StringBuffer();
			optnalState.add(new ListObject("3", "重复"));
			optnalState.add(new ListObject("11", "挂起/需提供更多信息"));
			staFlwMemSb.append("3,11=1^testSelStr");
			if(currTaskInfo.getTestFlow().indexOf("6")>=0){
				optnalState.add(new ListObject("18", "交叉验证"));
				staFlwMemSb.append("$18=6^devStr");
			}else{
				optnalState.add(new ListObject("13", "已改"));
			}
			staFlwMemSb.append("$13,16=1^testSelStr");
			optnalState.add(new ListObject("16", "非错"));
			optnalState.add(new ListObject("19", "挂起/不计划修改"));
			optnalState.add(new ListObject("20", "挂起/下版本修改"));
			if(currTaskInfo.getTestFlow().indexOf("4")>=0){
				staFlwMemSb.append("$17=4^assignSelStr");
				staFlwMemSb.append("$19,20=7^interCesSelStr");
			}else{
				staFlwMemSb.append("$17,19,20=7^interCesSelStr");
			}
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());
		}else if(initState==18&&handByme){
			StringBuffer staFlwMemSb = new StringBuffer();
			optnalState.add(new ListObject("3", "重复"));
			optnalState.add(new ListObject("11", "挂起/需提供更多信息"));
			staFlwMemSb.append("3,11=1^testSelStr");
			staFlwMemSb.append("$13,16=1^testSelStr");
			optnalState.add(new ListObject("16", "非错"));
			optnalState.add(new ListObject("17", "重分配"));
			optnalState.add(new ListObject("19", "挂起/不计划修改"));
			optnalState.add(new ListObject("20", "挂起/下版本修改"));
			if(currTaskInfo.getTestFlow().indexOf("4")>=0){
				staFlwMemSb.append("$17=4^assignSelStr");
				staFlwMemSb.append("$19,20=7^interCesSelStr");
			}else{
				staFlwMemSb.append("$17,19,20=7^interCesSelStr");
			}
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());			
		}else if(initState==19&&handByme){
			StringBuffer staFlwMemSb = new StringBuffer();
			optnalState.add(new ListObject("3", "重复"));
			optnalState.add(new ListObject("11", "挂起/需提供更多信息"));
			staFlwMemSb.append("3,11=1^testSelStr");
			staFlwMemSb.append("$13,16=1^testSelStr");
			if(currTaskInfo.getTestFlow().indexOf("6")>=0){
				optnalState.add(new ListObject("18", "交叉验证"));
				staFlwMemSb.append("$18=6^devStr");
			}else{
				optnalState.add(new ListObject("13", "已改"));
			}
			optnalState.add(new ListObject("16", "非错"));
			optnalState.add(new ListObject("17", "重分配"));
			optnalState.add(new ListObject("20", "挂起/下版本修改"));
			optnalState.add(new ListObject("23", "关闭/遗留"));
			if(currTaskInfo.getTestFlow().indexOf("4")>=0){
				staFlwMemSb.append("$17=4^assignSelStr");
				staFlwMemSb.append("$19,20=7^interCesSelStr");
			}else{
				staFlwMemSb.append("$17,19,20=7^interCesSelStr");
			}
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());				
		}else if(initState==20&&handByme){
			StringBuffer staFlwMemSb = new StringBuffer();
			optnalState.add(new ListObject("3", "重复"));
			optnalState.add(new ListObject("11", "挂起/需提供更多信息"));
			staFlwMemSb.append("3,11=1^testSelStr");
			staFlwMemSb.append("$13,16=1^testSelStr");
			if(currTaskInfo.getTestFlow().indexOf("6")>=0){
				optnalState.add(new ListObject("18", "交叉验证"));
				staFlwMemSb.append("$18=6^devStr");
			}else{
				optnalState.add(new ListObject("13", "已改"));
			}
			optnalState.add(new ListObject("16", "非错"));
			optnalState.add(new ListObject("17", "重分配"));
			optnalState.add(new ListObject("19", "挂起/不计划修改"));
			if(currTaskInfo.getTestFlow().indexOf("4")>=0){
				staFlwMemSb.append("$17=4^assignSelStr");
				staFlwMemSb.append("$19=7^interCesSelStr");
			}else{
				staFlwMemSb.append("$17,19=7^interCesSelStr");
			}
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());				
		}else if(initState==21&&handByme){
			StringBuffer staFlwMemSb = new StringBuffer();
			optnalState.add(new ListObject("3", "重复"));
			staFlwMemSb.append("3=1^testSelStr");
			optnalState.add(new ListObject("10", "待改"));
			staFlwMemSb.append("$10=5^devStr");
		}else if(initState==22&&handByme){
			optnalState.add(new ListObject("10", "待改"));
			controlInfo.setStaFlwMemStr("$10=5^devStr");
		}else if(initState==23&&handByme) {
			optnalState.add(new ListObject("10", "待改"));
			optnalState.add(new ListObject("21", "待改/下版本修改"));
			controlInfo.setStaFlwMemStr("$10,21=5^devStr");
		}else if(initState==24&&handByme){
			StringBuffer staFlwMemSb = new StringBuffer();
			if(currFlwCd==1){
				optnalState.add(new ListObject("3", "重复"));
				optnalState.add(new ListObject("5", "撤销"));
				optnalState.add(new ListObject("15", "关闭/不再现"));	
				controlInfo.setStaFlwMemStr("3,5,15=1^0");
			}else if(currFlwCd==2){
				optnalState.add(new ListObject("3", "重复"));
				optnalState.add(new ListObject("2", "修正/描述不当"));
				optnalState.add(new ListObject("3", "重复"));
				optnalState.add(new ListObject("4", "无效"));
				optnalState.add(new ListObject("5", "撤销"));
				staFlwMemSb.append("3,4,5=0^0");
				if (nextFlowCd==4){
					optnalState.add(new ListObject("25", "分配"));
					staFlwMemSb.append("$25=4^assignSelStr");
				}else{
					optnalState.add(new ListObject("7", "待改/再现"));
					optnalState.add(new ListObject("8", "待改/不再现"));
					staFlwMemSb.append("$7,8=5^devStr");
				}				
			}
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());
		}else if(initState==25&&handByme){
			StringBuffer staFlwMemSb = new StringBuffer();
			if(currFlwCd==1){
				optnalState.add(new ListObject("3", "重复"));
				optnalState.add(new ListObject("5", "撤销"));
				optnalState.add(new ListObject("15", "关闭/不再现"));	
				controlInfo.setStaFlwMemStr("3,5,15=0^0");
			}else if(currFlwCd==2){
				optnalState.add(new ListObject("2", "修正/描述不当"));
				optnalState.add(new ListObject("3", "重复"));
				optnalState.add(new ListObject("4", "无效"));
				optnalState.add(new ListObject("5", "撤销"));
				staFlwMemSb.append("2,3,4,5=1^testSelStr");	
			}else if(currFlwCd==3){
				optnalState.add(new ListObject("3", "重复"));
				staFlwMemSb.append("3=1^testSelStr");		
			}
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());
		}else{
			controlInfo.setViewFlag(true);
		}
		return controlInfo;
	}
	//处理问题且当前处理人不是本人
	private ControlInfo flowCtrlHandDataInit(CurrTaskInfo currTaskInfo,BugBaseInfo bug){
		
		int currState = bug.getCurrStateId();
		int nextFlowCd = this.getNextFlwCd(bug, currTaskInfo.getTestFlow().split(" "));
		int currFlwCd = bug.getNextFlowCd();
		int befFlwCd = bug.getCurrFlowCd();
		String myUserId = SecurityContextHolderHelp.getUserId();
		
		ControlInfo controlInfo = new ControlInfo();
		List<ListObject>  optnalState = new ArrayList<ListObject>();
		controlInfo.setOptnalStateList(optnalState);
		boolean testOwn =(myUserId.equals(bug.getTestOwnerId())||currTaskInfo.getRoleInTask().indexOf("8")>=0);
		boolean devOwn = myUserId.equals(bug.getDevOwnerId());
		boolean intecrOwn = (myUserId.equals(bug.getIntercessOwnerId())||currTaskInfo.getRoleInTask().indexOf("7")>=0);
		boolean asngnOwn = (myUserId.equals(bug.getAssinOwnerId())||currTaskInfo.getRoleInTask().indexOf("7")>=0);
		boolean analysOwn = (myUserId.equals(bug.getAnalyseOwnerId())||currTaskInfo.getRoleInTask().indexOf("7")>=0);
		if(currState==1&&testOwn){
			StringBuffer staFlwMemSb = new StringBuffer();
			if(currFlwCd==2){
				optnalState.add(new ListObject("2", "修正/描述不当"));
				optnalState.add(new ListObject("3", "重复"));
				optnalState.add(new ListObject("4", "无效"));
				optnalState.add(new ListObject("5", "撤销"));
				optnalState.add(new ListObject("6", "不再现/需提供更多信息"));
				staFlwMemSb.append("2,3,4,5,6=1^testSelStr");
				if(nextFlowCd==3){
					optnalState.add(new ListObject("24", "分析"));
					staFlwMemSb.append("$24=3^analySelStr");
				}else if(nextFlowCd==4){
					optnalState.add(new ListObject("25", "分配"));
					staFlwMemSb.append("$25=4^assignSelStr");
				}else if(nextFlowCd==5){
					optnalState.add(new ListObject("7", "待改/再现"));
					optnalState.add(new ListObject("8", "待改/不再现"));
					staFlwMemSb.append("$7,8=4^devStr");
				}
			}
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());
		}else if(currState==2&&testOwn){
			optnalState.add(new ListObject("1", "待置"));
			optnalState.add(new ListObject("4", "无效"));
			optnalState.add(new ListObject("5", "撤销"));
			controlInfo.setStaFlwMemStr("4,5=1^0$1=2^testSelStr");
		}else if(currState==3&&testOwn){
			if(befFlwCd==2){
				optnalState.add(new ListObject("1", "待置"));
				optnalState.add(new ListObject("5", "撤销"));
				controlInfo.setStaFlwMemStr("5=1^0$1=2^testChkIdSelStr");
			}else if(befFlwCd==3){
				optnalState.add(new ListObject("5", "撤销"));
				optnalState.add(new ListObject("24", "分析"));
				controlInfo.setStaFlwMemStr("5=1^0$24=2^analySelStr");
			}else if(befFlwCd==4){
				optnalState.add(new ListObject("5", "撤销"));
				optnalState.add(new ListObject("25", "分配"));	
				controlInfo.setStaFlwMemStr("5=1^0$25=4^assignSelStr");
			}else if(befFlwCd==5){
				optnalState.add(new ListObject("5", "撤销"));
				if(currTaskInfo.getTestFlow().indexOf("2")>=0){
					optnalState.add(new ListObject("7", "待改/再现"));
					optnalState.add(new ListObject("8", "待改/不再现"));
				}else{
					optnalState.add(new ListObject("10", "待改"));
				}
				controlInfo.setStaFlwMemStr("5=1^0$7,8,10=5^devStr");;					
			}
		}else if(currState==4&&testOwn){
			optnalState.add(new ListObject("1", "待置"));
			optnalState.add(new ListObject("5", "撤销"));
			controlInfo.setStaFlwMemStr("1=2^testChkIdSelStr$5=0^0");
		}else if(currState==6&&testOwn){
			optnalState.add(new ListObject("1", "待置"));
			optnalState.add(new ListObject("5", "撤销"));
			optnalState.add(new ListObject("15", "关闭/不再现"));
			controlInfo.setStaFlwMemStr("1=2^testChkIdSelStr$5,15=0^0");
		}else if((currState==7||currState==8||currState==10)&&devOwn){
			StringBuffer staFlwMemSb = new StringBuffer();
			optnalState.add(new ListObject("3", "重复"));
			optnalState.add(new ListObject("11", "挂起/需提供更多信息"));
			if(currTaskInfo.getTestFlow().indexOf("6")>=0){
				optnalState.add(new ListObject("18", "交叉验证"));
				staFlwMemSb.append("3,11,16=1^testSelStr");
				staFlwMemSb.append("$18=6^devStr");
			}else{
				optnalState.add(new ListObject("13", "已改"));
				staFlwMemSb.append("3,11,13,16=1^testSelStr");
			}
			optnalState.add(new ListObject("16", "非错"));
			optnalState.add(new ListObject("17", "重分配"));
			optnalState.add(new ListObject("19", "挂起/不计划修改"));
			optnalState.add(new ListObject("20", "挂起/下版本修改"));
			if(currTaskInfo.getTestFlow().indexOf("4")>=0){
				staFlwMemSb.append("$17=4^assignSelStr");
				staFlwMemSb.append("$19,20=7^interCesSelStr");
			}else{
				staFlwMemSb.append("$17,19,20=7^interCesSelStr");
			}
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());
		}else if(currState==9&&devOwn){
			StringBuffer staFlwMemSb = new StringBuffer();
			optnalState.add(new ListObject("11", "挂起/需提供更多信息"));
			if(currTaskInfo.getTestFlow().indexOf("6")>=0){
				optnalState.add(new ListObject("18", "交叉验证"));
				staFlwMemSb.append("11=1^testSelStr$18=6^devChkIdSelStr");
			}else{
				optnalState.add(new ListObject("13", "已改"));
				staFlwMemSb.append("11,13=1^testSelStr");
			}
			optnalState.add(new ListObject("17", "重分配"));
			optnalState.add(new ListObject("19", "挂起/不计划修改"));
			optnalState.add(new ListObject("20", "挂起/下版本修改"));
			if(currTaskInfo.getTestFlow().indexOf("4")>=0){
				staFlwMemSb.append("$17=4^assignSelStr");
				staFlwMemSb.append("$19,20=7^interCesSelStr");
			}else{
				staFlwMemSb.append("$17,19,20=7^interCesSelStr");
			}
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());
		}else if(currState==11&&testOwn){
				optnalState.add(new ListObject("15", "关闭/不再现"));
				if(currTaskInfo.getTestFlow().indexOf("2")>=0){
					optnalState.add(new ListObject("7", "待改/再现"));
					optnalState.add(new ListObject("8", "待改/不再现"));
				}else{
					optnalState.add(new ListObject("10", "待改"));
				}	
			controlInfo.setStaFlwMemStr("15=1^0$7,8,10=5^devStr");
		}else if(currState==12&&intecrOwn){
			optnalState.add(new ListObject("10", "待改"));
			optnalState.add(new ListObject("22", "关闭/撤销"));
		}else if(currState==13&&testOwn){
			optnalState.add(new ListObject("9", "待改/未解决"));
			optnalState.add(new ListObject("14", "关闭/己解决"));
			controlInfo.setStaFlwMemStr("14=1^0$9=5^devStr");
		}else if(currState==16&&testOwn){
			optnalState.add(new ListObject("5", "撤销"));
			optnalState.add(new ListObject("12", "分歧"));	
			controlInfo.setStaFlwMemStr("5=1^0$12=7^interCesSelStr");
		}else if(currState==17&&intecrOwn){
			optnalState.add(new ListObject("10", "待改"));
			controlInfo.setStaFlwMemStr("10=5^devStr");
		}else if(currState==18&&devOwn){
			optnalState.add(new ListObject("9", "待改/未解决"));
			optnalState.add(new ListObject("13", "已改"));
			controlInfo.setStaFlwMemStr("13=1^testSelStr$9=5^devStr");
		}else if(currState==19&&intecrOwn){
			optnalState.add(new ListObject("10", "待改"));
			optnalState.add(new ListObject("21", "待改/下版本修改"));
			optnalState.add(new ListObject("22", "关闭/撤销"));
			optnalState.add(new ListObject("23", "关闭/遗留"));
			controlInfo.setStaFlwMemStr("10,21=5^devStr$22,23=0^0");
		}else if(currState==20&&intecrOwn){
			optnalState.add(new ListObject("10", "待改"));
			optnalState.add(new ListObject("21", "待改/下版本修改"));
			controlInfo.setStaFlwMemStr("10,21=5^devStr");			
		}else if(currState==24&&analysOwn){
			optnalState.add(new ListObject("3", "重复"));
			if(currTaskInfo.getTestFlow().indexOf("4")>=0){
				optnalState.add(new ListObject("25", "分配"));
			}else{
				optnalState.add(new ListObject("10", "待改"));
			}
			controlInfo.setStaFlwMemStr("3=1^0$25=4^assignSelStr$10=5^devStr");
			optnalState.add(new ListObject("3", "重复"));
			optnalState.add(new ListObject("10", "待改"));
			controlInfo.setStaFlwMemStr("3=1^0$10=5^devStr");
		}else if(currState==25&&asngnOwn){
			controlInfo.setStaFlwMemStr("3=1^0$25=4^assignSelStr$10=5^devStr");
			optnalState.add(new ListObject("3", "重复"));
			optnalState.add(new ListObject("10", "待改"));
			controlInfo.setStaFlwMemStr("3=1^0$10=5^devStr");
		}
		else{
			
			controlInfo.setViewFlag(true);
		}
		return controlInfo;
		
	}
	
	public BugCommonService getBugCommonService() {
		return bugCommonService;
	}
	
	public void setBugCommonService(BugCommonService bugCommonService) {
		this.bugCommonService = bugCommonService;
	}


	public TestTaskDetailService getTestTaskService() {
		return testTaskService;
	}
	public void setTestTaskService(TestTaskDetailService testTaskService) {
		this.testTaskService = testTaskService;
	}

	private List strArr2List(String[] strArr){
		List list = new ArrayList();
		for(String str :strArr){
			if(!"".equals(str)&&!list.contains(str)){
				list.add(str);
			}
		}
		return list;
	}

	public DrawHtmlListDateService getDrawHtmlListDateService() {
		return drawHtmlListDateService;
	}

	public void setDrawHtmlListDateService(
			DrawHtmlListDateService drawHtmlListDateService) {
		this.drawHtmlListDateService = drawHtmlListDateService;
	}

}
