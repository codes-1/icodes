package cn.com.codes.object;

import cn.com.codes.framework.transmission.JsonInterface;


public class IterationBugReal implements JsonInterface {
	private static final long serialVersionUID = 1L;
	
	private String iterationBugId;
	private String iterationId;
	private String bugCardId;
	

	/**  
	 * @return iterationBugId 
	 */
	public String getIterationBugId() {
		return iterationBugId;
	}


	/**  
	 * @param iterationBugId iterationBugId 
	 */
	public void setIterationBugId(String iterationBugId) {
		this.iterationBugId = iterationBugId;
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


	/**  
	* @return bugCardId 
	*/
	public String getBugCardId() {
		return bugCardId;
	}


	/**  
	* @param bugCardId bugCardId 
	*/
	public void setBugCardId(String bugCardId) {
		this.bugCardId = bugCardId;
	}

}
