package cn.com.codes.bugManager.blh;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.codes.bugManager.dto.BugManagerDto;
import cn.com.codes.bugManager.dto.ControlInfo;
import cn.com.codes.bugManager.service.BugCommonService;
import cn.com.codes.bugManager.service.BugFlowControlService;
import cn.com.codes.common.SecurityContextHolderHelp;
import cn.com.codes.common.service.DrawHtmlListDateService;
import cn.com.codes.framework.app.blh.BusinessBlh;
import cn.com.codes.framework.app.view.View;
import cn.com.codes.framework.common.HtmlListComponent;
import cn.com.codes.framework.common.JsonUtil;
import cn.com.codes.framework.common.ListObject;
import cn.com.codes.framework.exception.DataBaseException;
import cn.com.codes.framework.jms.mail.MailProducer;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.msgManager.service.CommonMsgService;
import cn.com.codes.object.BugBaseInfo;
import cn.com.codes.object.BugHandHistory;
import cn.com.codes.object.OutlineInfo;
import cn.com.codes.object.SoftwareVersion;
import cn.com.codes.object.TypeDefine;
import cn.com.codes.testTaskManager.dto.CurrTaskInfo;
import cn.com.codes.testTaskManager.service.TestTaskDetailService;

public class BugFlowControlBlh extends BusinessBlh {

	private BugCommonService bugCommonService;
	private TestTaskDetailService testTaskService ;
	private CommonMsgService mypmCommonMsgService;
	private BugFlowControlService bugFlowControlService ;
	private DrawHtmlListDateService drawHtmlListDateService;
	
	private MailProducer mailProducer;

	public View handSub(BusiRequestEvent req){
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		BugBaseInfo bug = dto.getBug();
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		bug.setCurrHandlerId(SecurityContextHolderHelp.getUserId());
		bug.setCurrHandlDate(new Date());
		dto.getBug().setTaskId(taskId);
		String hql ="select new OutlineInfo(moduleNum,moduleId)  from OutlineInfo where taskId=? and moduleId=?";
		List<OutlineInfo> outLineList = bugCommonService.findByHql(hql, bug.getTaskId(),bug.getModuleId());
		bug.setModuleNum(outLineList.get(0).getModuleNum());
		//如果BUG是关闭状态，还要写解决版本
		if("批量分配".equals(bug.getCurrRemark())){//批量分配后，再处理BUG时，还是原来的备注，这里强修改过来s
			bug.setCurrRemark("处理问题");
		}
		BugHandHistory history = this.buildHistory(dto);
		bugCommonService.updatHandSub(dto,history);
		//this.sendMail(history,bug);
		bug.setModelName(dto.getModuleName());
		//System.out.println(dto.getModuleName());
		bugCommonService.sendBugNotice(history, bug,null);
		super.writeResult("success");
		return super.globalAjax();
	}

	private BugHandHistory buildHistory(BugManagerDto dto){
		BugBaseInfo bug = dto.getBug();
		if(bug.getProcessLog()==null||bug.getProcessLog().trim().equals("")){
			return null;
		}
		BugHandHistory bugHistory = new BugHandHistory();
		bugHistory.setInitState(bug.getInitState());
		bugHistory.setBugState(bug.getCurrStateId());
		//bugHistory.setHandlerName(bug.getModelName());
		bugHistory.setHandlerId(bug.getCurrHandlerId());
		bugHistory.setInsDate(bug.getCurrHandlDate());
		bugHistory.setTestFlowCd(bug.getCurrFlowCd());
		bugHistory.setBugId(bug.getBugId());
		bugHistory.setTaskId(bug.getTaskId());
		bugHistory.setModuleId(bug.getModuleId());
		bugHistory.setRemark(bug.getCurrRemark());
		bugHistory.setHandResult(bug.getProcessLog());
		//bugHistory.setTestSeq(bug.getTestSeq());
		bugHistory.setCurrVer(dto.getCurrVer());
		bugHistory.setCurrDayFinal(1);
		return bugHistory;
	}
	public View viewDetal(BusiRequestEvent req){
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		BugBaseInfo bug = dto.getBug();
		//先查出测试流程及当前操作人在项目中的角色
		CurrTaskInfo currTaskInfo = testTaskService.getCurrTaskDetalInfo();
		dto.setTestFlow(currTaskInfo.getTestFlow());
		dto.setRoleInTask(currTaskInfo.getRoleInTask());
		String hql = "from BugBaseInfo where bugId=? and taskId=?";
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		List bugList = bugCommonService.findByHql(hql, bug.getBugId(),taskId);
		ControlInfo controlInfo = null;
		if(bugList==null||bugList.size()==0){
			dto.setIsAjax("true");
			throw new DataBaseException("当前记录己被删除");
		}
		bug = (BugBaseInfo)bugList.get(0);
		//bug.setInitState(bug.getCurrStateId());
		dto.setBug(bug);
		dto.setStateName(BugFlowConst.getStateName(dto.getBug().getCurrStateId()));
		bugCommonService.setRelaUser(dto.getBug());
		this.loadRelaData(dto, controlInfo,currTaskInfo,bug.getNextFlowCd());
		currTaskInfo = null;
		controlInfo = null;
		return super.getView("detail");
	}
	public View bugHand(BusiRequestEvent req){
		BugManagerDto dto = super.getDto(BugManagerDto.class, req);
		BugBaseInfo bug = dto.getBug();
		if(dto.getTaskId()!=null&&!"".equals(dto.getTaskId().trim())){
			SecurityContextHolder.getContext().getVisit().setTaskId(dto.getTaskId());	
		}
		//先查出测试流程及当前操作人在项目中的角色
		CurrTaskInfo currTaskInfo = testTaskService.getCurrTaskDetalInfo();
		dto.setTestFlow(currTaskInfo.getTestFlow());
		dto.setRoleInTask(currTaskInfo.getRoleInTask());
		String hql = "from BugBaseInfo where bugId=? and taskId=?";
		String taskId = SecurityContextHolderHelp.getCurrTaksId();
		List bugList = bugCommonService.findByHql(hql, bug.getBugId(),taskId);
		ControlInfo controlInfo = null;
		if(bugList==null||bugList.size()==0){
			throw new DataBaseException("当前记录己被删除");
		}
		bug = (BugBaseInfo)bugList.get(0);
		dto.setBug(bug);
		dto.setStateName(BugFlowConst.getStateName(dto.getBug().getCurrStateId()));
		bugCommonService.setRelaUser(dto.getBug());
		String myUserId = SecurityContextHolderHelp.getUserId();
		if("".equals(dto.getRoleInTask())){
			this.loadRelaData(dto, controlInfo,currTaskInfo,bug.getNextFlowCd());
			dto.getBug().setTestCases(null);
			dto.setCurrTaskInfo(null);
			dto.setIsDetailFlag("1");
			super.writeResult(JsonUtil.toJson(dto));
			return super.globalAjax();
//			return super.getView("detail");
		}else{
			if(bug.getCurrHandlerId().equals(myUserId)){
				controlInfo = correctMyHandDataInit(currTaskInfo,bug);
				if(controlInfo.getOptnalStateList().size()>0){
					controlInfo.getOptnalStateList().remove(new ListObject(bug.getCurrStateId().toString(), ""));
				}
			}else{
				controlInfo = flowCtrlHandDataInit(currTaskInfo,bug);
				bug.setInitState(bug.getCurrStateId());
			}
		}
		bug.setStaFlwMemStr(controlInfo.getStaFlwMemStr());
		dto.setStateList(controlInfo.getOptnalStateList());
		if(bug.getCurrHandlerId().equals(myUserId)){
			this.loadRelaData(dto, controlInfo,currTaskInfo,bug.getCurrFlowCd());
		}else{
			this.loadRelaData(dto, controlInfo,currTaskInfo,bug.getNextFlowCd());
		}
		if(controlInfo.isViewFlag()&&(controlInfo.getOptnalStateList().size()==0
				&&(currTaskInfo.getRoleInTask().indexOf("7")<0
				||currTaskInfo.getRoleInTask().indexOf("8")<0))){
			controlInfo = null;
			dto.getBug().setTestCases(null);
			dto.setCurrTaskInfo(null);
			dto.setIsDetailFlag("1");
			super.writeResult(JsonUtil.toJson(dto));
			return super.globalAjax();
//			return super.getView("detail");
		}
		controlInfo = null;
		currTaskInfo = null;
		dto.getBug().setTestCases(null);
		dto.setCurrTaskInfo(null);
		dto.setIsDetailFlag("0");
		super.writeResult(JsonUtil.toJson(dto));
		return super.globalAjax();
//		return super.getView();
		//处理问题时，要把处理前的nextFlowCd 的值存入currFowCd
	}
	private void loadRelaData(BugManagerDto dto,ControlInfo controlInfo,CurrTaskInfo currTaskInfo,int flowCd){
		
		bugCommonService.setRelaType(dto.getBug());
		bugCommonService.setActorListData(currTaskInfo, dto.getBug());
		
		if(dto.getLoadType()==1&&controlInfo!=null&&!controlInfo.isViewFlag()){
			List<TypeDefine> typeList = bugCommonService.getBugListData();
			dto.setTypeList(typeList);	
			List<ListObject> verList = testTaskService.getVerSelList();
			dto.getBug().setVerSelStr(HtmlListComponent.toSelectStr(verList, "$"));
		}
		Long verInfoId = dto.getBug().getCurrVerInfo();
		if(verInfoId==null){
			if(dto.getBug().getBugReptVer()!=null){
				String hql = "select new SoftwareVersion(versionId,versionNum) from SoftwareVersion where versionId=? ";
				List<SoftwareVersion> list = bugCommonService.findByHql(hql, dto.getBug().getBugReptVer());
				if(list!=null&&!list.isEmpty()){
					SoftwareVersion reptVer = list.get(0);
					dto.getBug().setReptVersion(reptVer);
				}	
			}
		}else{
			String hql = "select new SoftwareVersion(versionId,versionNum) from SoftwareVersion where versionId=? or versionId=? ";
			List<SoftwareVersion> list = bugCommonService.findByHql(hql, dto.getBug().getBugReptVer(),verInfoId);
			if(list!=null||!list.isEmpty()){
				for(SoftwareVersion sv :list){
					if(verInfoId.toString().equals(sv.getVersionId().toString())){
						dto.getBug().setCurrVersion(sv);
					}
					if(dto.getBug().getBugReptVer().toString().equals(sv.getVersionId().toString())){
						dto.getBug().setReptVersion(sv);
					}
				}
			}
		}

		bugCommonService.initBugListDate(dto);
		dto.setModuleName(bugCommonService.getMdPathName(dto.getBug().getModuleId(),""));
		dto.getBug().setModelName(dto.getModuleName());
		this.setCurrOwner(dto,flowCd);
		if(controlInfo!=null&&controlInfo.getFixFlwName()!=null){
			dto.getBug().setModelName(controlInfo.getFixFlwName());
		}
		if(SecurityContextHolderHelp.getUserId().equals(dto.getBug().getCurrHandlerId())){
//			dto.getBug().setModelName("处理人");
		}
	}
	


	public void setCurrOwner(BugManagerDto dto,int flowCd){
		BugBaseInfo bug = dto.getBug();
		if(bug.getCurrStateId()==3||bug.getCurrStateId()==4||bug.getCurrStateId()==5||bug.getCurrStateId()==14
				||bug.getCurrStateId()==15||bug.getCurrStateId()==22||bug.getCurrStateId()==23
				){
			StringBuffer sb  = new StringBuffer(" 处理人");
			sb.append("$");
			sb.append(bug.getCurrHander().getLoginName()).append("(").append(bug.getCurrHander().getName()).append(")");
			dto.setCurrOwner(sb.toString());
			return;
		}
		StringBuffer sb  = new StringBuffer("等待"); 
		//if(bug.getNextFlowCd()==1){
		flowCd = bug.getNextFlowCd();
		if(flowCd==1){
			dto.getBug().setModelName("处理问题");
			sb.append("$");
			sb.append(bug.getTestOwner().getLoginName()).append("(").append(bug.getTestOwner().getName()).append(")处理");
			dto.setCurrOwner(sb.toString());
		}else if(flowCd==2){
			dto.getBug().setModelName("测试互验");
			sb.append("$");
			sb.append(bug.getTestOwner().getLoginName()).append("(").append(bug.getTestOwner().getName()).append(")互验");
			dto.setCurrOwner(sb.toString());
		}else if(flowCd==3){
			dto.getBug().setModelName("分析问题");
			sb.append("$");
			sb.append(bug.getAnalysOwner().getLoginName()).append("(").append(bug.getAnalysOwner().getName()).append(")分析");
			dto.setCurrOwner(sb.toString());
		}else if(flowCd==4){
			dto.getBug().setModelName("分配问题");
			sb.append("$");
			sb.append(bug.getAssinOwner().getLoginName()).append("(").append(bug.getAssinOwner().getName()).append(")分配");
			dto.setCurrOwner(sb.toString());
		}else if(flowCd==5){
			dto.getBug().setModelName("修改问题");
			sb.append("$");
			sb.append(bug.getDevOwner().getLoginName()).append("(").append(bug.getDevOwner().getName()).append(")处理");
			dto.setCurrOwner(sb.toString());
		}else if(flowCd==6){
			dto.getBug().setModelName("开发互检");
			sb.append("$");
			sb.append(bug.getDevOwner().getLoginName()).append("(").append(bug.getDevOwner().getName()).append(")处理");
			dto.setCurrOwner(sb.toString());
		}else if(flowCd==7){
			dto.getBug().setModelName("负责人仲裁");
			sb.append("$");
			sb.append(bug.getIntecesOwner().getLoginName()).append("(").append(bug.getIntecesOwner().getName()).append(")处理");
			dto.setCurrOwner(sb.toString());
		}else if(flowCd==8){
			dto.getBug().setModelName("测试确认");
			sb.append("$");
			sb.append(bug.getTestOwner().getLoginName()).append("(").append(bug.getTestOwner().getName()).append(")确认");
			dto.setCurrOwner(sb.toString());
		}		
	}


	private String getNextHandler(BugBaseInfo bug ){
		int flowCd = bug.getNextFlowCd();
		if(flowCd==1||flowCd==2||flowCd==8){
			return bug.getTestOwnerId();
		}else if(flowCd==3){
			return bug.getAnalyseOwnerId();
		}else if(flowCd==4){
			return bug.getAssinOwnerId();
		}else if(flowCd==5||flowCd==6){		
			return bug.getDevOwnerId();
		}else if(flowCd==7){
			return bug.getIntercessOwnerId();
		}				
		return null;
	}
	private int correctFlwCd(String testFlw,int currFlwCd){
		
		if(testFlw.indexOf(String.valueOf(currFlwCd))>=0){
			return currFlwCd;
		}
		String[] testFlwArr = testFlw.split(" ");
		int i=0;
		for(String flw :testFlwArr){
			if("9".equals(flw)){
				continue;
			}
			if(Integer.parseInt(flw)>currFlwCd){
				break;
			}
			i++;
		}
		return Integer.parseInt(testFlwArr[i]);		
	}
	//处理问题，当前处理人是本人
	private ControlInfo correctMyHandDataInit(CurrTaskInfo currTaskInfo,BugBaseInfo bug){
		
		//当流程变动时设置的状态，这里初始状态为null 反回一个什么都没有的ControlInfo
		if(bug.getInitState()==null){
			return new ControlInfo();
		}
		String myUserId = SecurityContextHolderHelp.getUserId();
		int currState = bug.getCurrStateId();
		int initState = bug.getInitState();
		int currFlwCd = bug.getCurrFlowCd();
		currFlwCd = this.correctFlwCd(currTaskInfo.getTestFlow(), currFlwCd);
		
		ControlInfo controlInfo = new ControlInfo();
		List<ListObject>  optnalState = new ArrayList<ListObject>();
		controlInfo.setOptnalStateList(optnalState);
		boolean handByme =myUserId.equals(bug.getCurrHandlerId());
		boolean isAuthor = myUserId.equals(bug.getBugReptId())||myUserId.equals(bug.getTestOwnerId());
		//if((initState==1||currState==2)&&(handByme||isAuthor)){
		if((currState==1||currState==2)&&(handByme||isAuthor)){
			StringBuffer staFlwMemSb = new StringBuffer();
			if(currFlwCd==1){
				optnalState.add(new ListObject("3", "重复"));
				optnalState.add(new ListObject("4", "无效"));
				optnalState.add(new ListObject("5", "撤销"));	
				optnalState.add(new ListObject("15", "关闭/不再现"));	
				staFlwMemSb.append("3,4,5,15=1^0");				
			}else if(currFlwCd==2||currFlwCd==3||currFlwCd==4){
				if(currTaskInfo.getTestFlow().indexOf("2")>=0){
					optnalState.add(new ListObject("6", "不再现/需提供更多信息"));
					optnalState.add(new ListObject("2", "修正/描述不当"));
					staFlwMemSb.append("$2,6=2^testSelStr");
					optnalState.add(new ListObject("7", "待改/再现"));
					optnalState.add(new ListObject("8", "待改/不再现"));
					staFlwMemSb.append("$7,8=5^devStr");
				}else if(currTaskInfo.getTestFlow().indexOf("3")>=0){
					optnalState.add(new ListObject("24", "分析"));
					staFlwMemSb.append("$24=3^analySelStr");
				}else if (currTaskInfo.getTestFlow().indexOf("4")>=0){
					optnalState.add(new ListObject("25", "分配"));
					staFlwMemSb.append("$25=4^assignSelStr");
				}else{
					optnalState.add(new ListObject("10", "待改"));
					//optnalState.add(new ListObject("8", "待改/不再现"));
					staFlwMemSb.append("$10=5^devStr");
				}
			}
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());
		}else if(currState==3&&(handByme||isAuthor)){
			//else if(initState==3&&(handByme||isAuthor)){
			StringBuffer staFlwMemSb = new StringBuffer();
			if(currFlwCd==1){
				optnalState.add(new ListObject("4", "无效"));
				optnalState.add(new ListObject("5", "撤销"));
				staFlwMemSb.append("3,4,5=1^0");
				if(currTaskInfo.getTestFlow().indexOf("2")>=0){
					optnalState.add(new ListObject("1", "待置"));
					staFlwMemSb.append("$1=2^testChkIdSelStr");
				}else if(currTaskInfo.getTestFlow().indexOf("3")>=0){
					optnalState.add(new ListObject("24", "分析"));
					staFlwMemSb.append("$24=3^analySelStr");
				}else if(currTaskInfo.getTestFlow().indexOf("4")>=0){
					optnalState.add(new ListObject("25", "分配"));;
					staFlwMemSb.append("$25=4^assignSelStr");
				}else{
					if(currTaskInfo.getTestFlow().indexOf("2")>=0){
						optnalState.add(new ListObject("7", "待改/再现"));
						optnalState.add(new ListObject("8", "待改/不再现"));
						staFlwMemSb.append("$7,8=5^devStr");
					}else{
						optnalState.add(new ListObject("10", "待改"));
						staFlwMemSb.append("$10=5^devStr");
					}
				}				
			}else if(currFlwCd==2){
				optnalState.add(new ListObject("2", "修正/描述不当"));
				optnalState.add(new ListObject("4", "无效"));
				optnalState.add(new ListObject("5", "撤销"));
				optnalState.add(new ListObject("6", "不再现/需提供更多信息"));
				staFlwMemSb.append("3,4,5=1^0");
				staFlwMemSb.append("$6=1^testSelStr");
				if(currTaskInfo.getTestFlow().indexOf("3")>=0){
					optnalState.add(new ListObject("24", "分析"));
					staFlwMemSb.append("$24=3^analySelStr");
				}else if(currTaskInfo.getTestFlow().indexOf("4")>=0){
					optnalState.add(new ListObject("25", "分配"));;
					staFlwMemSb.append("$25=4^assignSelStr");
				}else{
					if(currTaskInfo.getTestFlow().indexOf("2")>=0){
						optnalState.add(new ListObject("7", "待改/再现"));
						optnalState.add(new ListObject("8", "待改/不再现"));
						staFlwMemSb.append("$7,8=5^devStr");
					}else{
						optnalState.add(new ListObject("10", "待改"));
						staFlwMemSb.append("$10=5^devStr");
					}
				}
			}else if(currFlwCd==3||currFlwCd==4||currFlwCd==5){
				optnalState.add(new ListObject("5", "撤销"));
				if(currTaskInfo.getTestFlow().indexOf("4")>=0){
					optnalState.add(new ListObject("25", "分配"));
					staFlwMemSb.append("25=4^assignSelStr$5=1^0");
				}else{
					if(currTaskInfo.getTestFlow().indexOf("2")>=0){
						optnalState.add(new ListObject("7", "待改/再现"));
						optnalState.add(new ListObject("8", "待改/不再现"));
					}else{
						optnalState.add(new ListObject("10", "待改"));
					}
					staFlwMemSb.append("7,8,10=5^devStr");
				}
			}
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());
		}else if(currState==4&&(handByme||isAuthor)){
			//else if(initState==4&&(handByme||isAuthor)){
			StringBuffer staFlwMemSb = new StringBuffer();
			if(currFlwCd==1){
				optnalState.add(new ListObject("3", "重复"));
				optnalState.add(new ListObject("4", "无效"));
				optnalState.add(new ListObject("5", "撤销"));	
				optnalState.add(new ListObject("15", "关闭/不再现"));	
				staFlwMemSb.append("3,4,5,15=1^0");		
				if(currTaskInfo.getTestFlow().indexOf("2")>=0){
					optnalState.add(new ListObject("1", "待置"));
					staFlwMemSb.append("$1=2^testSelStr");
				}else if(currTaskInfo.getTestFlow().indexOf("3")>=0){
					optnalState.add(new ListObject("24", "分析"));
					staFlwMemSb.append("$24=3^analySelStr");
				}else if (currTaskInfo.getTestFlow().indexOf("4")>=0){
					optnalState.add(new ListObject("25", "分配"));
					staFlwMemSb.append("$25=4^assignSelStr");
				}else{
					optnalState.add(new ListObject("10", "待改"));
					//optnalState.add(new ListObject("8", "待改/不再现"));
					staFlwMemSb.append("$10=5^devStr");
				}
			}else if(currFlwCd==2){
				optnalState.add(new ListObject("2", "修正/描述不当"));
				optnalState.add(new ListObject("3", "重复"));
				optnalState.add(new ListObject("5", "撤销"));
				if(currTaskInfo.getTestFlow().indexOf("2")>=0){
						optnalState.add(new ListObject("6", "不再现/需提供更多信息"));
				}
				staFlwMemSb.append("2,3,5=1^0");
				staFlwMemSb.append("$6=1^testSelStr");
				if(currTaskInfo.getTestFlow().indexOf("3")>=0){
					optnalState.add(new ListObject("24", "分析"));
					staFlwMemSb.append("$24=3^analySelStr");
				}else if (currTaskInfo.getTestFlow().indexOf("4")>=0){
					optnalState.add(new ListObject("25", "分配"));
					staFlwMemSb.append("$25=4^assignSelStr");
				}else{
					if(currTaskInfo.getTestFlow().indexOf("2")>=0){
						optnalState.add(new ListObject("7", "待改/再现"));
						optnalState.add(new ListObject("8", "待改/不再现"));
					}else{
						optnalState.add(new ListObject("10", "待改"));
					}
					staFlwMemSb.append("$7,8,10=5^devStr");
				}
			}else if(currFlwCd==3||currFlwCd==4||currFlwCd==5){
				optnalState.add(new ListObject("5", "撤销"));
				staFlwMemSb.append("5=1^0");
				if (currTaskInfo.getTestFlow().indexOf("4")>=0){
					optnalState.add(new ListObject("25", "分配"));
					staFlwMemSb.append("$25=4^assignSelStr");
				}else{
					if(currTaskInfo.getTestFlow().indexOf("2")>=0){
						optnalState.add(new ListObject("7", "待改/再现"));
						optnalState.add(new ListObject("8", "待改/不再现"));
					}else{
						optnalState.add(new ListObject("10", "待改"));
					}
					staFlwMemSb.append("$7,8,10=5^devStr");
				}
				staFlwMemSb.append("5=1^testSelStr");
			}
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());
		}else if(currState==5&&(handByme||isAuthor)){
			//else if(initState==5&&(handByme||isAuthor)){
			StringBuffer staFlwMemSb = new StringBuffer();
			if(currFlwCd==1){
				optnalState.add(new ListObject("3", "重复"));
				optnalState.add(new ListObject("15", "关闭/不再现"));	
				staFlwMemSb.append("3,15=1^0");
			}else if(currFlwCd==2){
				optnalState.add(new ListObject("2", "修正/描述不当"));
				optnalState.add(new ListObject("3", "重复"));
				optnalState.add(new ListObject("6", "不再现/需提供更多信息"));
				staFlwMemSb.append("3,4,5,6=1^testSelStr");
			}else{
				staFlwMemSb.append("3,15=1^0");
			}
			if(bug.getInitState()==16){ 
				optnalState.add(new ListObject("12", "分歧"));
				staFlwMemSb.append("$12=7^interCesSelStr");
			}
			if(currTaskInfo.getTestFlow().indexOf("2")>=0){
				optnalState.add(new ListObject("1", "待置"));
				staFlwMemSb.append("$1=2^testSelStr");
				controlInfo.setStaFlwMemStr(staFlwMemSb.toString());
			}else if(currTaskInfo.getTestFlow().indexOf("3")>=0){
				optnalState.add(new ListObject("24", "分析"));
				staFlwMemSb.append("$24=3^analySelStr");
			}else if(currTaskInfo.getTestFlow().indexOf("4")>=0){
				optnalState.add(new ListObject("25", "分配"));
				staFlwMemSb.append("$25=4^assignSelStr");
			}else if(currTaskInfo.getTestFlow().indexOf("5")>=0){
				optnalState.add(new ListObject("10", "待改"));
				staFlwMemSb.append("$10=5^devStr");
			}
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());
		}else if(currState==6&&(handByme||isAuthor)){
			//else if(initState==6&&(handByme||isAuthor)){
			StringBuffer staFlwMemSb = new StringBuffer();
			optnalState.add(new ListObject("2", "修正/描述不当"));
			optnalState.add(new ListObject("3", "重复"));
			optnalState.add(new ListObject("4", "无效"));
			optnalState.add(new ListObject("5", "撤销"));
			staFlwMemSb.append("3,4,5=1^0");
			if(currTaskInfo.getTestFlow().indexOf("3")>=0){
				optnalState.add(new ListObject("24", "分析"));
				staFlwMemSb.append("$24=3^analySelStr");
			}else if(currTaskInfo.getTestFlow().indexOf("4")>=0){
				optnalState.add(new ListObject("25", "分配"));
				staFlwMemSb.append("$25=4^assignSelStr");
			}else{
				if(currTaskInfo.getTestFlow().indexOf("2")>=0){
					optnalState.add(new ListObject("7", "待改/再现"));
					optnalState.add(new ListObject("8", "待改/不再现"));
					staFlwMemSb.append("$7,8=5^devStr");
				}else{
					optnalState.add(new ListObject("10", "待改"));
					staFlwMemSb.append("$10=5^devStr");
				}
			}
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());
		}else if((currState==7||currState==8||currState==10)&&handByme){
			//else if((initState==7||initState==8||initState==10)&&handByme){
			StringBuffer staFlwMemSb = new StringBuffer();
			if(currFlwCd==1){
				optnalState.add(new ListObject("3", "重复"));
				optnalState.add(new ListObject("5", "撤销"));
				optnalState.add(new ListObject("15", "关闭/不再现"));	
				staFlwMemSb.append("3,5,15=1^0");
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
			}else if(currFlwCd==5){
				optnalState.add(new ListObject("3", "重复"));
				optnalState.add(new ListObject("11", "挂起/需提供更多信息"));
				if(currTaskInfo.getTestFlow().indexOf("6")>=0){
					optnalState.add(new ListObject("18", "交叉验证"));
					staFlwMemSb.append("3,11,16=1^testSelStr");
					staFlwMemSb.append("$18=6^devStr");
				}else{
					optnalState.add(new ListObject("13", "已改"));
					optnalState.add(new ListObject("26", "己改/同步到测试环境"));
					staFlwMemSb.append("3,11,13,16=1^testSelStr");
					staFlwMemSb.append("$26=1^testSelStr");
				}
				optnalState.add(new ListObject("17", "重分配"));
				if(initState!=12&&initState!=19&&initState!=20){
					optnalState.add(new ListObject("16", "非错"));
					optnalState.add(new ListObject("19", "挂起/不计划修改"));
					optnalState.add(new ListObject("20", "挂起/下版本修改"));
				}
				if(currTaskInfo.getTestFlow().indexOf("4")>=0){
					staFlwMemSb.append("$17=4^assignSelStr");
					staFlwMemSb.append("$19,20=7^interCesSelStr");
				}else{
					staFlwMemSb.append("$17,19,20=7^interCesSelStr");
				}
			}else if(currFlwCd==6){
				optnalState.add(new ListObject("13", "已改"));
				optnalState.add(new ListObject("26", "己改/同步到测试环境"));
				//staFlwMemSb.append("13=5^devStr");
				staFlwMemSb.append("13=1^testSelStr");
				staFlwMemSb.append("$26=1^testSelStr");
			}else if(currFlwCd==7){
				//if(initState==7){
				//	
				//}else 
				if(initState==12){
					optnalState.add(new ListObject("22", "关闭/撤销"));
					optnalState.add(new ListObject("23", "关闭/遗留"));
					controlInfo.setStaFlwMemStr("$22,23=1^0");
				}else if(initState==19){
					optnalState.add(new ListObject("21", "待改/下版本修改"));
					optnalState.add(new ListObject("23", "关闭/遗留"));
					controlInfo.setStaFlwMemStr("$21=5^devStr$23=1^0");
				}else if(initState==20){
					optnalState.add(new ListObject("21", "待改/下版本修改"));
					controlInfo.setStaFlwMemStr("$21=5^devStr");
				}
				//optnalState.add(new ListObject("13", "已改"));
				//staFlwMemSb.append("13=5^devStr");
			}
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());
		}else if(currState==9&&handByme){
			//else if(initState==9&&handByme){
			if(currFlwCd==6){
				optnalState.add(new ListObject("13", "已改"));
				controlInfo.setStaFlwMemStr("13,26=1^testSelStr");
				optnalState.add(new ListObject("26", "己改/同步到测试环境"));
			}else{
				optnalState.add(new ListObject("14", "关闭/己解决"));
				controlInfo.setStaFlwMemStr("14=1^0");
			}
		}else if(currState==11&&handByme){
			//else if(initState==11&&handByme){
			StringBuffer staFlwMemSb = new StringBuffer();
			optnalState.add(new ListObject("3", "重复"));
			staFlwMemSb.append("3=1^0");
			if(currTaskInfo.getTestFlow().indexOf("6")>=0){
				optnalState.add(new ListObject("18", "交叉验证"));
				staFlwMemSb.append("$18=6^devStr");
			}else{
				optnalState.add(new ListObject("13", "已改"));
				optnalState.add(new ListObject("26", "己改/同步到测试环境"));
			}
			staFlwMemSb.append("$13,16,26=1^testSelStr");
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
		}else if(currState==12&&handByme){
			//else if(initState==12&&handByme){
			optnalState.add(new ListObject("5", "撤销"));
			controlInfo.setStaFlwMemStr("5=1^0");
		}else if(currState==13&&handByme){

			
			StringBuffer staFlwMemSb = new StringBuffer();
			if(currFlwCd==6){
				optnalState.add(new ListObject("9", "待改/未解决"));
				staFlwMemSb.append("9=5^devStr");
				optnalState.add(new ListObject("26", "己改/同步到测试环境"));
				staFlwMemSb.append("$26=1^testSelStr");
			}else{
				optnalState.add(new ListObject("26", "己改/同步到测试环境"));
				optnalState.add(new ListObject("3", "重复"));
				staFlwMemSb.append("$3,13,16,26=1^testSelStr");
				optnalState.add(new ListObject("16", "非错"));
				optnalState.add(new ListObject("17", "重分配"));
				optnalState.add(new ListObject("19", "挂起/不计划修改"));
				if(currTaskInfo.getTestFlow().indexOf("4")>=0){
					staFlwMemSb.append("$17=4^assignSelStr");
					staFlwMemSb.append("$19=7^interCesSelStr");
				}else{
					staFlwMemSb.append("$17,19=7^interCesSelStr");
				}
			}
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());			
		}else if(currState==26&&handByme){
			StringBuffer staFlwMemSb = new StringBuffer();
			if(currFlwCd==6){
				optnalState.add(new ListObject("9", "待改/未解决"));
				staFlwMemSb.append("9=5^devStr");
				optnalState.add(new ListObject("13", "己改"));
				staFlwMemSb.append("$13=1^testSelStr");
			}else{
				optnalState.add(new ListObject("13", "己改"));
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
			}
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());			
		}else if(currState==14&&(handByme||isAuthor)){
			//else if(initState==14&&(handByme||isAuthor)){
			optnalState.add(new ListObject("9", "待改/未解决"));
			controlInfo.setStaFlwMemStr("9=5^devStr");
		}else if(currState==15&&(handByme||isAuthor)){
			//else if(initState==15&&(handByme||isAuthor)){
			if(currTaskInfo.getTestFlow().indexOf("2")>=0){
				optnalState.add(new ListObject("1", "待置"));
				controlInfo.setStaFlwMemStr("1=2^testSelStr");
			}else if(currTaskInfo.getTestFlow().indexOf("5")>=0){
				optnalState.add(new ListObject("10", "待改"));
				controlInfo.setStaFlwMemStr("$10=5^devStr");
			}
		}else if(currState==16&&handByme){
			//else if(initState==16&&handByme){
			StringBuffer staFlwMemSb = new StringBuffer();
			optnalState.add(new ListObject("3", "重复"));
			optnalState.add(new ListObject("11", "挂起/需提供更多信息"));
			if(currFlwCd==4){
				optnalState.add(new ListObject("10", "待改"));
				staFlwMemSb.append("3,10,11=1^testSelStr");
			}else{
				staFlwMemSb.append("3,11=1^testSelStr");
			}
			
			if(currTaskInfo.getTestFlow().indexOf("6")>=0){
				optnalState.add(new ListObject("18", "交叉验证"));
				staFlwMemSb.append("$18=6^devStr");
			}else{
				optnalState.add(new ListObject("13", "已改"));
				optnalState.add(new ListObject("26", "己改/同步到测试环境"));
			}
			staFlwMemSb.append("$13,16,26=1^testSelStr");
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
		}else if(currState==17&&handByme){
			//if(initState==17&&handByme){
			StringBuffer staFlwMemSb = new StringBuffer();
			optnalState.add(new ListObject("3", "重复"));
			optnalState.add(new ListObject("11", "挂起/需提供更多信息"));
			staFlwMemSb.append("3,11=1^testSelStr");
			if(currTaskInfo.getTestFlow().indexOf("6")>=0){
				optnalState.add(new ListObject("18", "交叉验证"));
				staFlwMemSb.append("$18=6^devStr");
			}else{
				optnalState.add(new ListObject("13", "已改"));
				optnalState.add(new ListObject("26", "己改/同步到测试环境"));
			}
			staFlwMemSb.append("$13,16,26=1^testSelStr");
			optnalState.add(new ListObject("16", "非错"));
			optnalState.add(new ListObject("19", "挂起/不计划修改"));
			optnalState.add(new ListObject("20", "挂起/下版本修改"));
			if(currTaskInfo.getTestFlow().indexOf("4")>=0){
				staFlwMemSb.append("$17=4^assignSelStr");
				staFlwMemSb.append("$19,20=7^interCesSelStr");
			}else{
				staFlwMemSb.append("$17,19,20=7^interCesSelStr");
			}
			//如本人是分配人(或无分配流程且是开发仲裁)还可以再分配
			if(currTaskInfo.getRoleInTask().indexOf("4")>=0
					||(currTaskInfo.getRoleInTask().indexOf("7")>=0&&currTaskInfo.getTestFlow().indexOf("4")<0)){
				if(currTaskInfo.getTestFlow().indexOf("2")>=0){
					optnalState.add(new ListObject("7", "待改/再现"));
					optnalState.add(new ListObject("8", "待改/不再现"));
					staFlwMemSb.append("$7,8=5^devStr");
				}else{
					optnalState.add(new ListObject("10", "待改"));
					staFlwMemSb.append("$10=5^devStr");
				}	
			}
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());
		}else if(currState==18&&handByme){
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
		}else if(currState==19&&handByme){
			StringBuffer staFlwMemSb = new StringBuffer();
			optnalState.add(new ListObject("3", "重复"));
			optnalState.add(new ListObject("11", "挂起/需提供更多信息"));
			staFlwMemSb.append("3,11=1^testSelStr");
			staFlwMemSb.append("$13,16,26=1^testSelStr");
			if(currTaskInfo.getTestFlow().indexOf("6")>=0){
				optnalState.add(new ListObject("18", "交叉验证"));
				staFlwMemSb.append("$18=6^devStr");
			}else{
				optnalState.add(new ListObject("13", "已改"));
				optnalState.add(new ListObject("26", "己改/同步到测试环境"));
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
		}else if(currState==20&&handByme){
			StringBuffer staFlwMemSb = new StringBuffer();
			optnalState.add(new ListObject("3", "重复"));
			optnalState.add(new ListObject("11", "挂起/需提供更多信息"));
			staFlwMemSb.append("3,11,26=1^testSelStr");
			staFlwMemSb.append("$13,16=1^testSelStr");
			if(currTaskInfo.getTestFlow().indexOf("6")>=0){
				optnalState.add(new ListObject("18", "交叉验证"));
				staFlwMemSb.append("$18=6^devStr");
			}else{
				optnalState.add(new ListObject("13", "已改"));
				optnalState.add(new ListObject("26", "己改/同步到测试环境"));
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
		}else if(currState==21&&handByme){
			StringBuffer staFlwMemSb = new StringBuffer();
			optnalState.add(new ListObject("3", "重复"));
			staFlwMemSb.append("3=1^testSelStr");
			optnalState.add(new ListObject("10", "待改"));
			staFlwMemSb.append("$10=5^devStr");
		}else if(currState==22&&handByme){
			StringBuffer staFlwMemSb = new StringBuffer();
			if(currTaskInfo.getTestFlow().indexOf("2")>=0){
				optnalState.add(new ListObject("7", "待改/再现"));
				optnalState.add(new ListObject("8", "待改/不再现"));
				staFlwMemSb.append("$7,8=5^devStr");
			}else{
				optnalState.add(new ListObject("10", "待改"));
				staFlwMemSb.append("$10=5^devStr");
			}
			if(initState==12){
				optnalState.add(new ListObject("23", "关闭/遗留"));
				staFlwMemSb.append("$23=1^0");
			}else if(initState==19){
				optnalState.add(new ListObject("21", "待改/下版本修改"));
				optnalState.add(new ListObject("23", "关闭/遗留"));
				staFlwMemSb.append("$21=5^devStr$23=1^0");
			}else if(initState==20){
				optnalState.add(new ListObject("21", "待改/下版本修改"));
				optnalState.add(new ListObject("23", "关闭/遗留"));
				staFlwMemSb.append("$21=5^devStr$23=1^0");
			}
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());
		}else if(currState==23&&handByme){
			optnalState.add(new ListObject("10", "待改"));
			if(initState==12){
				optnalState.add(new ListObject("22", "关闭/撤销"));
				optnalState.add(new ListObject("23", "关闭/遗留"));
				controlInfo.setStaFlwMemStr("$22,23=1^0");
			}else if(initState==19){
				optnalState.add(new ListObject("21", "待改/下版本修改"));
				optnalState.add(new ListObject("23", "关闭/遗留"));
				controlInfo.setStaFlwMemStr("$21=5^devStr$23=1^0");
			}else if(initState==20){
				optnalState.add(new ListObject("21", "待改/下版本修改"));
				controlInfo.setStaFlwMemStr("$21=5^devStr");
			}
			optnalState.add(new ListObject("21", "待改/下版本修改"));
			controlInfo.setStaFlwMemStr("10,21=5^devStr");
		}else if(currState==24&&handByme){
			StringBuffer staFlwMemSb = new StringBuffer();
			if(currFlwCd==1){
				optnalState.add(new ListObject("3", "重复"));
				optnalState.add(new ListObject("5", "撤销"));
				optnalState.add(new ListObject("15", "关闭/不再现"));	
				controlInfo.setStaFlwMemStr("3,5,15=1^0");
			}else if(currFlwCd==2){
				optnalState.add(new ListObject("3", "重复"));
				optnalState.add(new ListObject("2", "修正/描述不当"));
				optnalState.add(new ListObject("4", "无效"));
				optnalState.add(new ListObject("5", "撤销"));
				staFlwMemSb.append("3,4,5=0^0");
				if(currTaskInfo.getTestFlow().indexOf("4")>=0){
					optnalState.add(new ListObject("25", "分配"));
					staFlwMemSb.append("$25=4^assignSelStr");
				}else{
					if(currTaskInfo.getTestFlow().indexOf("2")>=0){
						optnalState.add(new ListObject("7", "待改/再现"));
						optnalState.add(new ListObject("8", "待改/不再现"));
						staFlwMemSb.append("$7,8=5^devStr");
					}else{
						optnalState.add(new ListObject("10", "待改"));
						staFlwMemSb.append("$10=5^devStr");
					}
				}				
			}
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());
		}else if(currState==25&&handByme){
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
		flowCtrlHandDataInit2(currTaskInfo,bug,controlInfo);
		return controlInfo;
	}
	private void flowCtrlHandDataInit2(CurrTaskInfo currTaskInfo,BugBaseInfo bug,ControlInfo controlInfo){
		
		String myUserId = SecurityContextHolderHelp.getUserId();
		boolean testOwn =(myUserId.equals(bug.getTestOwnerId())||currTaskInfo.getRoleInTask().indexOf("8")>=0);
		boolean devOwn = myUserId.equals(bug.getDevOwnerId());
		boolean intecrOwn = (myUserId.equals(bug.getIntercessOwnerId())||currTaskInfo.getRoleInTask().indexOf("7")>=0);
		boolean asngnOwn = (myUserId.equals(bug.getAssinOwnerId())||currTaskInfo.getRoleInTask().indexOf("4")>=0);
		boolean analysOwn = (myUserId.equals(bug.getAnalyseOwnerId())||currTaskInfo.getRoleInTask().indexOf("3")>=0);
		if(!testOwn&&!devOwn&&!intecrOwn&&!asngnOwn&&!analysOwn){
			return;
		}
		controlInfo.setFixFlwName("处理问题");//这时不知用户是向下走，还是反悔所以统称为"处理问题"
		StringBuffer staFlwMemSb = new StringBuffer();
		if(controlInfo.getStaFlwMemStr()!=null&&!"".equals(controlInfo.getStaFlwMemStr())){
			staFlwMemSb.append(controlInfo.getStaFlwMemStr());
			staFlwMemSb.append("$");
		}
		List<ListObject>  optnalState = controlInfo.getOptnalStateList();
		int currState = bug.getCurrStateId();
		if(currState==1&&testOwn){
			optnalState.add(new ListObject("2", "修正/描述不当"));
			optnalState.add(new ListObject("3", "重复"));
			optnalState.add(new ListObject("4", "无效"));
			optnalState.add(new ListObject("5", "撤销"));
			optnalState.add(new ListObject("6", "不再现/需提供更多信息"));
			staFlwMemSb.append("2,3,4,5,6=1^testSelStr");
			if(currTaskInfo.getTestFlow().indexOf("3")>=0){
				optnalState.add(new ListObject("24", "分析"));
				staFlwMemSb.append("$24=3^analySelStr");
			}else if(currTaskInfo.getTestFlow().indexOf("4")>=0){
				optnalState.add(new ListObject("25", "分配"));
				staFlwMemSb.append("$25=4^assignSelStr");
			}else if(currTaskInfo.getTestFlow().indexOf("5")>=0){
				optnalState.add(new ListObject("7", "待改/再现"));
				optnalState.add(new ListObject("8", "待改/不再现"));
				staFlwMemSb.append("$7,8=5^devStr");
			}
		}else if(currState==2&&testOwn){
			if(currTaskInfo.getTestFlow().indexOf("2")>=0){
				optnalState.add(new ListObject("1", "待置"));
			}else if(currTaskInfo.getTestFlow().indexOf("3")>=0){
				optnalState.add(new ListObject("24", "分析"));
				staFlwMemSb.append("24=3^analySelStr");
			}else if(currTaskInfo.getTestFlow().indexOf("4")>=0){
				optnalState.add(new ListObject("25", "分配"));
				staFlwMemSb.append("25=4^assignSelStr");
			}else if(currTaskInfo.getTestFlow().indexOf("5")>=0){
				optnalState.add(new ListObject("7", "待改/再现"));
				optnalState.add(new ListObject("8", "待改/不再现"));
				staFlwMemSb.append("7,8=4^devStr");
			}
			staFlwMemSb.append("$4,5=1^0$1=2^testSelStr");
			optnalState.add(new ListObject("4", "无效"));
			optnalState.add(new ListObject("5", "撤销"));
		}else if(currState==3&&testOwn){
			int befFlwCd = bug.getCurrFlowCd();
			befFlwCd= this.correctFlwCd(currTaskInfo.getTestFlow(), befFlwCd);
			if(befFlwCd==2){
				optnalState.add(new ListObject("1", "待置"));
				optnalState.add(new ListObject("5", "撤销"));
				staFlwMemSb.append("5=1^0$1=2^testChkIdSelStr");
			}else if(befFlwCd==3){
				optnalState.add(new ListObject("5", "撤销"));
				optnalState.add(new ListObject("24", "分析"));
				staFlwMemSb.append("5=1^0$24=2^analySelStr");
			}else if(befFlwCd==4){
				optnalState.add(new ListObject("5", "撤销"));
				optnalState.add(new ListObject("25", "分配"));
				staFlwMemSb.append("5=1^0$25=4^assignSelStr");
			}else if(befFlwCd==5){
				optnalState.add(new ListObject("5", "撤销"));
				if(currTaskInfo.getTestFlow().indexOf("2")>=0){
					optnalState.add(new ListObject("7", "待改/再现"));
					optnalState.add(new ListObject("8", "待改/不再现"));
				}else{
					optnalState.add(new ListObject("10", "待改"));
				}	
				staFlwMemSb.append("5=1^0$7,8,10=5^devStr");
			}
		}else if(currState==4&&testOwn){
			if(currTaskInfo.getTestFlow().indexOf("2")>=0){
				optnalState.add(new ListObject("1", "待置"));
			}else if(currTaskInfo.getTestFlow().indexOf("3")>=0){
				optnalState.add(new ListObject("24", "分析"));
				staFlwMemSb.append("24=3^analySelStr");
			}else if(currTaskInfo.getTestFlow().indexOf("4")>=0){
				optnalState.add(new ListObject("25", "分配"));
				staFlwMemSb.append("25=4^assignSelStr");
			}else if(currTaskInfo.getTestFlow().indexOf("5")>=0){
				optnalState.add(new ListObject("7", "待改/再现"));
				optnalState.add(new ListObject("8", "待改/不再现"));
				staFlwMemSb.append("7,8=4^devStr");
			}
			staFlwMemSb.append("$1=2^testChkIdSelStr$5=0^0");
			optnalState.add(new ListObject("5", "撤销"));
		}else if(currState==6&&testOwn){
			if(currTaskInfo.getTestFlow().indexOf("2")>=0){
				optnalState.add(new ListObject("1", "待置"));
			}else if(currTaskInfo.getTestFlow().indexOf("3")>=0){
				optnalState.add(new ListObject("24", "分析"));
				staFlwMemSb.append("24=3^analySelStr");
			}else if(currTaskInfo.getTestFlow().indexOf("4")>=0){
				optnalState.add(new ListObject("25", "分配"));
				staFlwMemSb.append("25=4^assignSelStr");
			}else if(currTaskInfo.getTestFlow().indexOf("5")>=0){
				optnalState.add(new ListObject("7", "待改/再现"));
				optnalState.add(new ListObject("8", "待改/不再现"));
				staFlwMemSb.append("7,8=4^devStr");
			}
			optnalState.add(new ListObject("5", "撤销"));
			optnalState.add(new ListObject("15", "关闭/不再现"));
			staFlwMemSb.append("$1=2^testChkIdSelStr$5,15=0^0");
		}else if((currState==7||currState==8||currState==10)&&devOwn){
			optnalState.add(new ListObject("3", "重复"));
			optnalState.add(new ListObject("11", "挂起/需提供更多信息"));
			if(currTaskInfo.getTestFlow().indexOf("6")>=0){
				optnalState.add(new ListObject("18", "交叉验证"));
				staFlwMemSb.append("3,11,16=1^testSelStr");
				staFlwMemSb.append("$18=6^devStr");
			}else{
				optnalState.add(new ListObject("13", "已改"));
				optnalState.add(new ListObject("26", "己改/同步到测试环境"));
				staFlwMemSb.append("3,11,13,16,26=1^testSelStr");
			}
			int initState = bug.getInitState();
			if(initState!=12&&initState!=19&&initState!=20){
				optnalState.add(new ListObject("16", "非错"));
				optnalState.add(new ListObject("17", "重分配"));
				optnalState.add(new ListObject("19", "挂起/不计划修改"));
				optnalState.add(new ListObject("20", "挂起/下版本修改"));
			}
			if(currTaskInfo.getTestFlow().indexOf("4")>=0){
				staFlwMemSb.append("$17=4^assignSelStr");
				staFlwMemSb.append("$19,20=7^interCesSelStr");
			}else{
				staFlwMemSb.append("$17,19,20=7^interCesSelStr");
			}
		}else if(currState==9&&devOwn){
			optnalState.add(new ListObject("11", "挂起/需提供更多信息"));
			if(currTaskInfo.getTestFlow().indexOf("6")>=0){
				optnalState.add(new ListObject("18", "交叉验证"));
				staFlwMemSb.append("11=1^testSelStr$18=6^devChkIdSelStr");
			}else{
				optnalState.add(new ListObject("13", "已改"));
				optnalState.add(new ListObject("26", "己改/同步到测试环境"));
				staFlwMemSb.append("11,13,26=1^testSelStr");
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
		}else if(currState==11&&testOwn){
				optnalState.add(new ListObject("15", "关闭/不再现"));
				if(currTaskInfo.getTestFlow().indexOf("2")>=0){
					optnalState.add(new ListObject("7", "待改/再现"));
					optnalState.add(new ListObject("8", "待改/不再现"));
				}else{
					optnalState.add(new ListObject("10", "待改"));
				}	
			staFlwMemSb.append("15=1^0$7,8,10=5^devStr");
		}else if(currState==12&&intecrOwn){
			optnalState.add(new ListObject("10", "待改"));
			optnalState.add(new ListObject("22", "关闭/撤销"));
			staFlwMemSb.append("10=5^devStr$22=7^0");
		}else if(currState==13&&testOwn){
			//else if(currState==13&&testOwn&&!bug.getCurrVer().equals(currTaskInfo.getCurrentVersion())){//已改
			optnalState.add(new ListObject("9", "待改/未解决"));
			optnalState.add(new ListObject("14", "关闭/己解决"));
			staFlwMemSb.append("14=1^0$9=5^devStr");
		}else if(currState==26&&testOwn){
			//else if(currState==13&&testOwn&&!bug.getCurrVer().equals(currTaskInfo.getCurrentVersion())){//已改
			optnalState.add(new ListObject("9", "待改/未解决"));
			optnalState.add(new ListObject("14", "关闭/己解决"));
			staFlwMemSb.append("14=1^0$9=5^devStr");
		}else if(currState==16&&testOwn){
			optnalState.add(new ListObject("5", "撤销"));
			optnalState.add(new ListObject("12", "分歧"));
			staFlwMemSb.append("5=1^0$12=7^interCesSelStr");
		}else if(currState==17&&intecrOwn){
			optnalState.add(new ListObject("10", "待改"));
			staFlwMemSb.append("10=5^devStr");
		}else if(currState==18&&devOwn){
			optnalState.add(new ListObject("9", "待改/未解决"));
			optnalState.add(new ListObject("13", "已改"));
			optnalState.add(new ListObject("26", "己改/同步到测试环境"));
			staFlwMemSb.append("13,26=1^testSelStr$9=5^devStr");
		}else if(currState==19&&intecrOwn){
			optnalState.add(new ListObject("10", "待改"));
			optnalState.add(new ListObject("21", "待改/下版本修改"));
			optnalState.add(new ListObject("22", "关闭/撤销"));
			optnalState.add(new ListObject("23", "关闭/遗留"));
			staFlwMemSb.append("10,21=5^devStr$22,23=0^0");
		}else if(currState==20&&intecrOwn){
			optnalState.add(new ListObject("10", "待改"));
			optnalState.add(new ListObject("21", "待改/下版本修改"));
			staFlwMemSb.append("10,21=5^devStr");
		}else if(currState==24&&analysOwn){
			optnalState.add(new ListObject("3", "重复"));
			if(currTaskInfo.getTestFlow().indexOf("2")<0){
				optnalState.add(new ListObject("2", "修正/描述不当"));
				optnalState.add(new ListObject("4", "无效"));
				optnalState.add(new ListObject("5", "撤销"));
				optnalState.add(new ListObject("6", "不再现/需提供更多信息"));
				staFlwMemSb.append("2,3,4,5,6=1^0");
			}else{
				staFlwMemSb.append("3=1^0");
			}
			if(currTaskInfo.getTestFlow().indexOf("4")>=0){
				optnalState.add(new ListObject("25", "分配"));
				staFlwMemSb.append("$25=4^assignSelStr");
			}else{
				if(currTaskInfo.getTestFlow().indexOf("2")>=0){
					optnalState.add(new ListObject("7", "待改/再现"));
					optnalState.add(new ListObject("8", "待改/不再现"));
					staFlwMemSb.append("$7,8=5^devStr");
				}else{
					optnalState.add(new ListObject("10", "待改"));
					staFlwMemSb.append("$10=5^devStr");
				}	
			}
		}else if(currState==25&&asngnOwn){
			optnalState.add(new ListObject("3", "重复"));
			optnalState.add(new ListObject("16", "非错"));
			if(currTaskInfo.getTestFlow().indexOf("2")<0&&currTaskInfo.getTestFlow().indexOf("3")<0){
				optnalState.add(new ListObject("2", "修正/描述不当"));
				optnalState.add(new ListObject("4", "无效"));
				optnalState.add(new ListObject("5", "撤销"));
				optnalState.add(new ListObject("6", "不再现/需提供更多信息"));
				staFlwMemSb.append("2,3,4,5,6,16=1^0");
			}else{
				staFlwMemSb.append("3,16=1^0");
			}
			if(currTaskInfo.getTestFlow().indexOf("2")>=0){
				optnalState.add(new ListObject("7", "待改/再现"));
				optnalState.add(new ListObject("8", "待改/不再现"));
				staFlwMemSb.append("$7,8=5^devStr");
			}else{
				optnalState.add(new ListObject("10", "待改"));
				staFlwMemSb.append("$10=5^devStr");
			}
		}else{
		}
		if(!staFlwMemSb.toString().equals("")){
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());
		}
		this.sortOptState(controlInfo.getOptnalStateList());
	}
	private void sortOptState(List<ListObject> optnalState){
		if(optnalState==null||optnalState.isEmpty()||optnalState.size()==1){
			return;
		}
		Map stateMap = new HashMap();
		for(ListObject obj :optnalState){
			stateMap.put(obj.getKeyObj(), obj);
		}
		optnalState.clear();
		optnalState.addAll(stateMap.values());
		ListObject[] states = new ListObject[optnalState.size()];
		java.util.Arrays.sort(optnalState.toArray(states), new ListObjectComparator());
		optnalState.clear();
		for(ListObject obj :states){
			optnalState.add(obj);
		}
	}
	
	class ListObjectComparator implements Comparator{
		
	    public int compare(Object o1, Object o2) {
	        Integer key1;
	        Integer key2;
	        if (o1 instanceof ListObject) {
	            key1 = Integer.parseInt(((ListObject)o1).getKeyObj());
	        }else{
	        	key1 = o1.hashCode();
	        }
	        if (o2 instanceof ListObject) {
	            key2 =Integer.parseInt(((ListObject)o2).getKeyObj());
	        }else{
	        	key2 = o2.hashCode();
	        }
	        return key1.compareTo(key2);
	    }
	}
//	static {
//		MypmBean.getInstacne();
//	}
	//处理问题且当前处理人不是本人
	private ControlInfo flowCtrlHandDataInit(CurrTaskInfo currTaskInfo,BugBaseInfo bug){
		
		int currState = bug.getCurrStateId();
		String myUserId = SecurityContextHolderHelp.getUserId();
		ControlInfo controlInfo = new ControlInfo();
		List<ListObject>  optnalState = new ArrayList<ListObject>();
		controlInfo.setOptnalStateList(optnalState);
		
		String nextHandlerId = getNextHandler(bug);
		boolean testOwn = false;
		boolean devOwn = false ;
		boolean intecrOwn = false ;
		boolean asngnOwn = false;
		boolean analysOwn = false;
		if(nextHandlerId!=null&&!currTaskInfo.isTeamMember(nextHandlerId)){
			testOwn =currTaskInfo.getRoleInTask().indexOf("8")>=0;
			devOwn = myUserId.equals(bug.getDevOwnerId());
			intecrOwn = currTaskInfo.getRoleInTask().indexOf("7")>=0;
			asngnOwn = currTaskInfo.getRoleInTask().indexOf("4")>=0;
			analysOwn = currTaskInfo.getRoleInTask().indexOf("3")>=0;
		}else{
			testOwn =(myUserId.equals(bug.getTestOwnerId())||currTaskInfo.getRoleInTask().indexOf("8")>=0);
			devOwn = myUserId.equals(bug.getDevOwnerId());
			intecrOwn = (myUserId.equals(bug.getIntercessOwnerId())||currTaskInfo.getRoleInTask().indexOf("7")>=0);
			asngnOwn = (myUserId.equals(bug.getAssinOwnerId())||currTaskInfo.getRoleInTask().indexOf("4")>=0);
			analysOwn = (myUserId.equals(bug.getAnalyseOwnerId())||currTaskInfo.getRoleInTask().indexOf("3")>=0);
		}

		if(currState==1&&testOwn){
			StringBuffer staFlwMemSb = new StringBuffer();
			optnalState.add(new ListObject("2", "修正/描述不当"));
			optnalState.add(new ListObject("3", "重复"));
			optnalState.add(new ListObject("4", "无效"));
			optnalState.add(new ListObject("5", "撤销"));
			optnalState.add(new ListObject("6", "不再现/需提供更多信息"));
			staFlwMemSb.append("2,3,4,5,6=1^testSelStr");
			if(currTaskInfo.getTestFlow().indexOf("3")>=0){
				optnalState.add(new ListObject("24", "分析"));
				staFlwMemSb.append("$24=3^analySelStr");
			}else if(currTaskInfo.getTestFlow().indexOf("4")>=0){
				optnalState.add(new ListObject("25", "分配"));
				staFlwMemSb.append("$25=4^assignSelStr");
			}else if(currTaskInfo.getTestFlow().indexOf("5")>=0){
				optnalState.add(new ListObject("7", "待改/再现"));
				optnalState.add(new ListObject("8", "待改/不再现"));
				staFlwMemSb.append("$7,8=5^devStr");
			}
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());
		}else if(currState==2&&testOwn){
			StringBuffer staFlwMemSb = new StringBuffer();
			if(currTaskInfo.getTestFlow().indexOf("2")>=0){
				optnalState.add(new ListObject("1", "待置"));
			}else if(currTaskInfo.getTestFlow().indexOf("3")>=0){
				optnalState.add(new ListObject("24", "分析"));
				staFlwMemSb.append("24=3^analySelStr");
			}else if(currTaskInfo.getTestFlow().indexOf("4")>=0){
				optnalState.add(new ListObject("25", "分配"));
				staFlwMemSb.append("25=4^assignSelStr");
			}else if(currTaskInfo.getTestFlow().indexOf("5")>=0){
				optnalState.add(new ListObject("7", "待改/再现"));
				optnalState.add(new ListObject("8", "待改/不再现"));
				staFlwMemSb.append("7,8=4^devStr");
			}
			staFlwMemSb.append("$4,5=1^0$1=2^testSelStr");
			optnalState.add(new ListObject("4", "无效"));
			optnalState.add(new ListObject("5", "撤销"));
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());
		}else if(currState==3&&testOwn){
			int befFlwCd = bug.getCurrFlowCd();
			befFlwCd= this.correctFlwCd(currTaskInfo.getTestFlow(), befFlwCd);
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
			StringBuffer staFlwMemSb = new StringBuffer();
			if(currTaskInfo.getTestFlow().indexOf("2")>=0){
				optnalState.add(new ListObject("1", "待置"));
			}else if(currTaskInfo.getTestFlow().indexOf("3")>=0){
				optnalState.add(new ListObject("24", "分析"));
				staFlwMemSb.append("24=3^analySelStr");
			}else if(currTaskInfo.getTestFlow().indexOf("4")>=0){
				optnalState.add(new ListObject("25", "分配"));
				staFlwMemSb.append("25=4^assignSelStr");
			}else if(currTaskInfo.getTestFlow().indexOf("5")>=0){
				optnalState.add(new ListObject("7", "待改/再现"));
				optnalState.add(new ListObject("8", "待改/不再现"));
				staFlwMemSb.append("7,8=4^devStr");
			}
			staFlwMemSb.append("$1=2^testChkIdSelStr$5=0^0");
			optnalState.add(new ListObject("5", "撤销"));
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());
		}else if(currState==6&&testOwn){
			StringBuffer staFlwMemSb = new StringBuffer();
			if(currTaskInfo.getTestFlow().indexOf("2")>=0){
				optnalState.add(new ListObject("1", "待置"));
			}else if(currTaskInfo.getTestFlow().indexOf("3")>=0){
				optnalState.add(new ListObject("24", "分析"));
				staFlwMemSb.append("24=3^analySelStr");
			}else if(currTaskInfo.getTestFlow().indexOf("4")>=0){
				optnalState.add(new ListObject("25", "分配"));
				staFlwMemSb.append("25=4^assignSelStr");
			}else if(currTaskInfo.getTestFlow().indexOf("5")>=0){
				optnalState.add(new ListObject("7", "待改/再现"));
				optnalState.add(new ListObject("8", "待改/不再现"));
				staFlwMemSb.append("7,8=4^devStr");
			}
			optnalState.add(new ListObject("5", "撤销"));
			optnalState.add(new ListObject("15", "关闭/不再现"));
			staFlwMemSb.append("$1=2^testChkIdSelStr$5,15=0^0");
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());
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
				optnalState.add(new ListObject("26", "己改/同步到测试环境"));
				staFlwMemSb.append("3,11,13,16,26=1^testSelStr");
			}
			optnalState.add(new ListObject("17", "重分配"));
			int initState = bug.getInitState();
			if(initState!=12&&initState!=19&&initState!=20){
				optnalState.add(new ListObject("16", "非错"));
				optnalState.add(new ListObject("19", "挂起/不计划修改"));
				optnalState.add(new ListObject("20", "挂起/下版本修改"));
			}
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
				optnalState.add(new ListObject("26", "己改/同步到测试环境"));
				staFlwMemSb.append("11,13,26=1^testSelStr");
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
			controlInfo.setStaFlwMemStr("10=5^devStr$22=7^0");
		}else if(currState==13&&testOwn){
			//else if(currState==13&&testOwn&&!bug.getCurrVer().equals(currTaskInfo.getCurrentVersion())){//已改
			optnalState.add(new ListObject("9", "待改/未解决"));
			optnalState.add(new ListObject("14", "关闭/己解决"));
			controlInfo.setStaFlwMemStr("14=1^0$9=5^devStr");
		}else if(currState==26&&testOwn){
			//else if(currState==13&&testOwn&&!bug.getCurrVer().equals(currTaskInfo.getCurrentVersion())){//已改
			optnalState.add(new ListObject("9", "待改/未解决"));
			optnalState.add(new ListObject("14", "关闭/己解决"));
			controlInfo.setStaFlwMemStr("14=1^0$9=5^devStr");
		}else if(currState==16&&testOwn){
			optnalState.add(new ListObject("5", "撤销"));
			optnalState.add(new ListObject("12", "分歧"));	
			controlInfo.setStaFlwMemStr("5=1^0$12=7^interCesSelStr");
		}else if(currState==17&&(intecrOwn||asngnOwn)){
			optnalState.add(new ListObject("10", "待改"));
			controlInfo.setStaFlwMemStr("10=5^devStr");
		}else if(currState==18&&devOwn){
			optnalState.add(new ListObject("9", "待改/未解决"));
			optnalState.add(new ListObject("13", "已改"));
			optnalState.add(new ListObject("26", "己改/同步到测试环境"));
			controlInfo.setStaFlwMemStr("13,26=1^testSelStr$9=5^devStr");
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
			StringBuffer staFlwMemSb = new StringBuffer();
			optnalState.add(new ListObject("3", "重复"));
			optnalState.add(new ListObject("16", "非错"));
			if(currTaskInfo.getTestFlow().indexOf("2")<0){
				optnalState.add(new ListObject("2", "修正/描述不当"));
				optnalState.add(new ListObject("4", "无效"));
				optnalState.add(new ListObject("5", "撤销"));
				optnalState.add(new ListObject("6", "不再现/需提供更多信息"));
				staFlwMemSb.append("2,3,4,5,6,16=1^0");
			}else{
				staFlwMemSb.append("3=1^0");
			}
			
			if(currTaskInfo.getTestFlow().indexOf("4")>=0){
				optnalState.add(new ListObject("25", "分配"));
				staFlwMemSb.append("$25=4^assignSelStr");
			}else{
				if(currTaskInfo.getTestFlow().indexOf("2")>=0){
					optnalState.add(new ListObject("7", "待改/再现"));
					optnalState.add(new ListObject("8", "待改/不再现"));
					staFlwMemSb.append("$7,8=5^devStr");
				}else{
					optnalState.add(new ListObject("10", "待改"));
					staFlwMemSb.append("$10=5^devStr");
				}	
			}
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());
			//optnalState.add(new ListObject("10", "待改"));
			//controlInfo.setStaFlwMemStr("3=1^0$10=5^devStr");
		}else if(currState==25&&asngnOwn){
			StringBuffer staFlwMemSb = new StringBuffer();
			optnalState.add(new ListObject("3", "重复"));
			optnalState.add(new ListObject("16", "非错"));
			if(currTaskInfo.getTestFlow().indexOf("2")<0&&currTaskInfo.getTestFlow().indexOf("3")<0){
				optnalState.add(new ListObject("2", "修正/描述不当"));
				optnalState.add(new ListObject("4", "无效"));
				optnalState.add(new ListObject("5", "撤销"));
				optnalState.add(new ListObject("6", "不再现/需提供更多信息"));
				staFlwMemSb.append("2,3,4,5,6,16=1^0");
			}else{
				staFlwMemSb.append("3,16=1^0");
			}
			if(currTaskInfo.getTestFlow().indexOf("2")>=0){
				optnalState.add(new ListObject("7", "待改/再现"));
				optnalState.add(new ListObject("8", "待改/不再现"));
				staFlwMemSb.append("$7,8=5^devStr");
			}else{
				optnalState.add(new ListObject("10", "待改"));
				staFlwMemSb.append("$10=5^devStr");
			}
			//controlInfo.setStaFlwMemStr("3=1^0$25=4^assignSelStr$10=5^devStr");
			//optnalState.add(new ListObject("3", "重复"));
			//optnalState.add(new ListObject("10", "待改"));
			//controlInfo.setStaFlwMemStr("3=1^0$10=5^devStr");
			controlInfo.setStaFlwMemStr(staFlwMemSb.toString());
		}else{

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
	public BugFlowControlService getBugFlowControlService() {
		return bugFlowControlService;
	}
	public void setBugFlowControlService(BugFlowControlService bugFlowControlService) {
		this.bugFlowControlService = bugFlowControlService;
	}
	public DrawHtmlListDateService getDrawHtmlListDateService() {
		return drawHtmlListDateService;
	}
	public void setDrawHtmlListDateService(
			DrawHtmlListDateService drawHtmlListDateService) {
		this.drawHtmlListDateService = drawHtmlListDateService;
	}
	public TestTaskDetailService getTestTaskService() {
		return testTaskService;
	}
	public void setTestTaskService(TestTaskDetailService testTaskService) {
		this.testTaskService = testTaskService;
	}
	public MailProducer getMailProducer() {
		return mailProducer;
	}
	public void setMailProducer(MailProducer mailProducer) {
		this.mailProducer = mailProducer;
	}
	public CommonMsgService getMypmCommonMsgService() {
		return mypmCommonMsgService;
	}
	public void setMypmCommonMsgService(CommonMsgService mypmCommonMsgService) {
		this.mypmCommonMsgService = mypmCommonMsgService;
	}

}
