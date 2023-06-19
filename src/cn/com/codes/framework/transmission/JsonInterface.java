package cn.com.codes.framework.transmission;

import java.io.Serializable;

public interface JsonInterface extends Serializable{
	
	public String toStrUpdateInit();
	public String toStrList();
	public String toStrUpdateRest();
	public void toString(StringBuffer bf);
}
