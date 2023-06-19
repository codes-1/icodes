package cn.com.codes.bugManager.dto;

import java.util.List;

import cn.com.codes.framework.common.ListObject;

public class ControlInfo {

	private List<ListObject> optnalStateList;
	private String fixFlwName;//固定流程名，把动态设置的忽略 
	private boolean viewFlag;// 如没任何可处理权限，且标识为查看的标记
	// 如多个状态对应的FLW和MEM相同，写成sta1,sta2=flw1^MemStrId1
	// sta1=flw1^MemStrId1$sta2=flw2^MemStrId2
	private String staFlwMemStr;

	public List<ListObject> getOptnalStateList() {
		return optnalStateList;
	}

	public void setOptnalStateList(List<ListObject> optnalStateList) {
		this.optnalStateList = optnalStateList;
	}

	public boolean isViewFlag() {
		return viewFlag;
	}

	public void setViewFlag(boolean viewFlag) {
		this.viewFlag = viewFlag;
	}

	public String getStaFlwMemStr() {
		return staFlwMemStr;
	}

	public void setStaFlwMemStr(String staFlwMemStr) {
		this.staFlwMemStr = staFlwMemStr;
	}

	public String getFixFlwName() {
		return fixFlwName;
	}

	public void setFixFlwName(String fixFlwName) {
		this.fixFlwName = fixFlwName;
	}

}
