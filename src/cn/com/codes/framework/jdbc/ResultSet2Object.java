package cn.com.codes.framework.jdbc;

import java.sql.ResultSet;
import java.util.List;

public interface ResultSet2Object {

	/**
	 * 
	 *@author liuyg
	 *功能： 把resultSet转换成ＶＯ或ＰＯＪＯ
	 *@param resultSet 用ＳＱＬ果询到的对像数组List
	 *@return
	 */
	@SuppressWarnings("rawtypes")
	public void convert(ResultSet rs,List list);
}
