package cn.com.codes.object;

import cn.com.codes.framework.transmission.JsonInterface;


public class IterationTestcasepackageReal implements JsonInterface {
	private static final long serialVersionUID = 1L;
	
	private String iterationPackageId;
	private String iterationId;
	private String packageId;
	


	/**  
	 * @return iterationPackageId 
	 */
	public String getIterationPackageId() {
		return iterationPackageId;
	}


	/**  
	 * @param iterationPackageId iterationPackageId 
	 */
	public void setIterationPackageId(String iterationPackageId) {
		this.iterationPackageId = iterationPackageId;
	}


	/**  
	 * @return packageId 
	 */
	public String getPackageId() {
		return packageId;
	}


	/**  
	 * @param packageId packageId 
	 */
	public void setPackageId(String packageId) {
		this.packageId = packageId;
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
