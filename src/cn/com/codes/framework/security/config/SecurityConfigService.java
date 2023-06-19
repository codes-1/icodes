package cn.com.codes.framework.security.config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;

import cn.com.codes.framework.app.services.BaseServiceImpl;
import cn.com.codes.framework.security.Function;

public abstract class SecurityConfigService extends BaseServiceImpl {



//	public void getfouctions(String fouctionName,BuildPageDataObject bpo) {
//
//		String sql = "";
//		if (!StringUtils.isEmpty(fouctionName)) {
//			sql = "SELECT S.FUNCTION_ID as functionId,CONCAT(M.FUNCTION_NAME,'/',S.FUNCTION_NAME ) as functionName,S.METHODS as methods FROM"
//					+ "(SELECT T.FUNCTION_ID,T.FUNCTION_NAME,T.PARENT_ID ,T.METHODS,T.URL FROM T_FUNCTION T where T.ISLEAF=1 order by SEQ)S,T_FUNCTION M"
//					+ "  WHERE M.FUNCTION_ID =S.PARENT_ID and CONCAT(M.FUNCTION_NAME,'/',S.FUNCTION_NAME ) like '%"
//					+ fouctionName + "%'";
//		} else {
//			sql = "SELECT S.FUNCTION_ID as functionId,CONCAT(M.FUNCTION_NAME,'/',S.FOUCTION_NAME ) as functionName,S.METHODS as methods FROM"
//					+ "(SELECT T.FUNCTION_ID,T.FUNCTION_NAME,T.PARENT_ID ,T.METHODS,T.URL FROM T_FUNCTION T where T.ISLEAF=1 order by SEQ)S,T_FUNCTION M"
//					+ "  WHERE M.FUNCTION_ID =S.PARENT_ID";
//		}
//
//		PagerModel.setDatasBySql(this.getHibernateGenericController(), sql, null, bpo, null);
//	}

	public Function getfouctionByID(String id) {

		String sql = "";

		sql = "SELECT S.FUNCTION_ID as functionId,CONCAT(M.FUNCTION_NAME,'/',S.FUNCTION_NAME ) as functionName,S.METHODS as methods FROM"
				+ "(SELECT T.FUNCTION_ID,T.FUNCTION_NAME,T.PARENT_ID ,T.METHODS,T.URL FROM T_FUNCTION T where T.ISLEAF=1 order by SEQ)S,T_FUNCTION M"
				+ "  WHERE M.FUNCTION_ID =S.PARENT_ID and S.FUNCTION_ID='"
				+ id + "'";

		Session session = this.getHibernateGenericController()
				.getHibernateTemplate().getSessionFactory().openSession();
		Connection con = session.connection();
		Function fouction = null;
		try {
			java.sql.PreparedStatement preSt = con.prepareStatement(sql
					.toString());
			ResultSet rs = preSt.executeQuery();
			fouction = buildFounction(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(session!=null){
				session.close();
			}
		}

		
		return fouction;
	}

	public abstract Function buildFounction(ResultSet rs);
	public void saveFounction(Function founction) {

		String sql = "update T_FUNCTION set METHODS='"
				+ founction.getMethods() + "' where FUNCTION_ID='"
				+ founction.getFunctionId() + "'";
		Session session = this.getHibernateGenericController()
				.getHibernateTemplate().getSessionFactory().openSession();
		Connection con = session.connection();
		try {
			java.sql.PreparedStatement preSt = con.prepareStatement(sql
					.toString());
			preSt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			if(session!=null){
				session.close();
			}
		}
		
	}
	

}
