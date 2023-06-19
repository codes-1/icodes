package cn.com.codes.object;

import cn.com.codes.framework.transmission.JsonInterface;


public class ConcernOtherMission implements JsonInterface {

	/**  
	* 字段:      字段名称
	* @Fields serialVersionUID : TODO 
	*/
	private static final long serialVersionUID = 1L;
	private String concernMissionId;
	private String userId;
	private String missionId;
	
   
	public ConcernOtherMission() {
	}
	
	
	


	public String getUserId() {
		return userId;
	}





	public void setUserId(String userId) {
		this.userId = userId;
	}





	public String getMissionId() {
		return missionId;
	}





	public void setMissionId(String missionId) {
		this.missionId = missionId;
	}





	public String getConcernMissionId() {
		return concernMissionId;
	}





	public void setConcernMissionId(String concernMissionId) {
		this.concernMissionId = concernMissionId;
	}





	@Override
	public String toStrUpdateInit() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String toStrList() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String toStrUpdateRest() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void toString(StringBuffer bf) {
		// TODO Auto-generated method stub
		
	}
}