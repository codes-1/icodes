package cn.com.codes.singleTestTaskManager.dto;

public class TestLeaderVo {

	private String id;
	private String loginName;
	private String name;
	private String taskId;

	public TestLeaderVo(){
		
	}
	
	public TestLeaderVo(String id,String name,String loginName,String taskId){
		this.id = id;
		this.name = name;
		this.loginName = loginName;
		this.taskId = taskId;
		
	}
	public TestLeaderVo(String id,String name,String loginName){
		this.id = id;
		this.name = name;
		this.loginName = loginName;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

}