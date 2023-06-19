package cn.com.codes.testTaskManager.dto;

public class VTestkTaskActor {

	private String id;
	private String taskId;
	private String userId;
	private String userName;
	private Integer actor;
	
	public VTestkTaskActor(String userId,String userName,Integer actor){
		this.userId = userId;
		this.userName = userName;
		this.actor = actor;
	}

	public VTestkTaskActor(){
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Integer getActor() {
		return actor;
	}
	public void setActor(Integer actor) {
		this.actor = actor;
	}
}
