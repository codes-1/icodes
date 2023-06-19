package cn.com.codes.caseManager.dto;

import java.util.ArrayList;
import java.util.List;


public class TreeJsonVo {
	private String id;
	private String text;
	private String iconCls;
	private String checked;
	private String state;
	public Boolean leaf;//是否为叶子节点，true表示是叶子节点，false表示不是叶子节点  
	private Boolean rootNode;//是否是root节点
	private Integer currLevel;
	private Integer moduleState;
	private String moduleNum;
	private List<TreeJsonVo> children = new ArrayList<TreeJsonVo>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getIconCls() {
		return iconCls;
	}
	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}
	public String getChecked() {
		return checked;
	}
	public void setChecked(String checked) {
		this.checked = checked;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Boolean getLeaf() {
		return leaf;
	}
	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}
	public Boolean getRootNode() {
		return rootNode;
	}
	public void setRootNode(Boolean rootNode) {
		this.rootNode = rootNode;
	}
	public Integer getCurrLevel() {
		return currLevel;
	}
	public void setCurrLevel(Integer currLevel) {
		this.currLevel = currLevel;
	}
	public Integer getModuleState() {
		return moduleState;
	}
	public void setModuleState(Integer moduleState) {
		this.moduleState = moduleState;
	}
	public List<TreeJsonVo> getChildren() {
		return children;
	}
	public void setChildren(List<TreeJsonVo> children) {
		this.children = children;
	}
	public String getModuleNum() {
		return moduleNum;
	}
	public void setModuleNum(String moduleNum) {
		this.moduleNum = moduleNum;
	} 
	
	
}
