package cn.com.codes.object;

import cn.com.codes.framework.transmission.JsonInterface;


public class IterationTaskReal implements JsonInterface {
	private static final long serialVersionUID = 1L;
	
	private String iterationMissionId;
	private String iterationId;
	private String missionId;


	/**  
	 * @return iterationMissionId 
	 */
	public String getIterationMissionId() {
		return iterationMissionId;
	}


	/**  
	 * @param iterationMissionId iterationMissionId 
	 */
	public void setIterationMissionId(String iterationMissionId) {
		this.iterationMissionId = iterationMissionId;
	}


	/**  
	 * @return missionId 
	 */
	public String getMissionId() {
		return missionId;
	}


	/**  
	 * @param missionId missionId 
	 */
	public void setMissionId(String missionId) {
		this.missionId = missionId;
	}


	/**  
	 * @return iterationId 
	 */
	public String getIterationId() {
		return iterationId;
	}


	/**  
	 * @param iterationId iterationId 
	 */
	public void setIterationId(String iterationId) {
		this.iterationId = iterationId;
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
