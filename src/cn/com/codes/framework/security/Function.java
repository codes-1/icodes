package cn.com.codes.framework.security;



import java.util.List;
import java.util.Set;

import cn.com.codes.framework.security.Function;

public abstract class Function implements java.io.Serializable {

	private String functionId;
	private String functionName;
	private Integer level;
	private Integer isleaf;
	private String methods;
	private Integer seq;
	private String url;
	private String parentId;
	private String secutiryUrl ;
	private Function parentFunction;
	private String[] methodArray ;
	private List<String> methodList ;
	private Set<Function> childFunctions ;
	
	public Function() {
	}
	public Function(String secutiryUrl) {
		this.secutiryUrl = secutiryUrl;
	}
	public Function(String functionId,String functionName,String parentId) {
		this.functionId = functionId;
		this.functionName = functionName;
		this.parentId = parentId;
	}

	public Function(String functionId,String parentId,String functionName,Integer level) {
		this.functionId = functionId;
		this.parentId = parentId;
		this.functionName = functionName;
		this.level = level;

	}
	public Function (String functionId,String parentId,String functionName,Integer level,String url ,Integer seq ){
		this.functionId = functionId;
		this.parentId = parentId;
		this.functionName = functionName;
		this.level = level;
		this.url = url;
		this.seq = seq;
	}
	public Integer getLevel() {
		return this.level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getIsleaf() {
		return this.isleaf;
	}

	public void setIsleaf(Integer isleaf) {
		this.isleaf = isleaf;
	}

	public String getMethods() {
		return this.methods;
	}

	public void setMethods(String methods) {
		this.methods = methods;
	}

	public Integer getSeq() {
		return this.seq;
	}

	public void setSeq(Integer seq) {
		this.seq = seq;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public List<String> getMethodList() {
		return methodList;
	}

	public void setMethodList(List<String> methodList) {
		this.methodList = methodList;
	}

	public String[] getMethodArray() {
		return methodArray;
	}

	public void setMethodArray(String[] methodArray) {
		this.methodArray = methodArray;
	}

	public String getFunctionId() {
		return functionId;
	}

	public void setFunctionId(String functionId) {
		this.functionId = functionId;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public Function getParentFunction() {
		return parentFunction;
	}

	public void setParentFunction(Function parentFunction) {
		this.parentFunction = parentFunction;
	}

	public Set getChildFunctions() {
		return childFunctions;
	}

	public void setChildFunctions(Set childFunctions) {
		this.childFunctions = childFunctions;
	}

	public String getSecutiryUrl() {
		return secutiryUrl;
	}

	public void setSecutiryUrl(String secutiryUrl) {
		this.secutiryUrl = secutiryUrl;
	}
	public static void main(String[] args){
		System.out.println("测");
	}



}
