package cn.com.codes.log;
///**
// * (C) 2010-2011 Alibaba Group Holding Limited.
// *
// * This program is free software; you can redistribute it and/or
// * modify it under the terms of the GNU General Public License 
// * version 2 as published by the Free Software Foundation. 
// * 
// */
//
//package cn.com.codes.log;
//
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Timestamp;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Properties;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.TimeUnit;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.log4j.Logger;
//import org.apache.log4j.PropertyConfigurator;
//
//import com.dhcc.core.DateUtil;
//import com.dhcc.core.FileUtil;
//import com.dhcc.core.task.Task;
//import com.dhcc.dfis.client.service.rest.DataxClient;
//import com.dhcc.dfis.dto.syncData.updateSycnDto;
//import com.taobao.datax.common.constants.Constants;
//import com.taobao.datax.common.exception.DataExchangeException;
//import com.taobao.datax.common.plugin.PluginParam;
//import com.taobao.datax.common.plugin.Pluginable;
//import com.taobao.datax.common.plugin.Reader;
//import com.taobao.datax.common.plugin.Writer;
//import com.taobao.datax.common.util.DealTools;
//import com.taobao.datax.engine.conf.EngineConf;
//import com.taobao.datax.engine.conf.GroupConf;
//import com.taobao.datax.engine.conf.JobConf;
//import com.taobao.datax.engine.conf.JobPluginConf;
//import com.taobao.datax.engine.conf.ParseXMLUtil;
//import com.taobao.datax.engine.conf.PluginConf;
//import com.taobao.datax.engine.plugin.BufferedLineExchanger;
//import com.taobao.datax.engine.storage.Storage;
//import com.taobao.datax.engine.storage.StoragePool;
//import com.taobao.datax.plugins.common.DBRecord;
//import com.taobao.datax.plugins.common.DBRecordBakcup;
//import com.taobao.datax.plugins.common.DBTestUtil;
//import com.taobao.datax.plugins.common.MapUtil;
//import com.taobao.datax.plugins.reader.oraclereader.ParamKey;
//
///**
// * Core class of DataX, schedule {@link Reader} & {@link Writer}.
// * 
// * */
//public class Engine {
//	private static final Logger logger = Logger.getLogger(Engine.class);
//
//	public static final Map<String,java.sql.Timestamp> DATE_MAP ;
//	/**
//	 * 日志格式化配置文件路径
//	 */
//	private static final String logFormatFilePath;
//	
//	//public static final Map<String,String> TIME_MAP = new HashMap<String,String>();
//	
//	private static final int PERIOD = 10;
//
//	private static final int MAX_CONCURRENCY = 64;
//
//	private EngineConf engineConf;
//
//	private Map<String, PluginConf> pluginReg;
//
//	private MonitorPool readerMonitorPool;
//
//	private MonitorPool writerMonitorPool;
//	
//	// 错误信息
//	private static String errormsg = "failed";
//	
//	/**
//	 * 根据groupId存放是否超时的标识
//	 */
//	public static Map<String,Boolean>timeoutMap;
//	
//	static {
//		timeoutMap = new ConcurrentHashMap<String,Boolean>();
//		DATE_MAP = new ConcurrentHashMap<String,java.sql.Timestamp>();
//		logFormatFilePath = findLogFormatFilePath();
//	}
//
//	/**
//	 * Constructor for {@link Engine}
//	 * 
//	 * @param engineConf
//	 *            Configuration for {@link Engine}
//	 * 
//	 * @param pluginReg
//	 *            Configurations for {@link Pluginable}
//	 * 
//	 * */
//	public Engine(EngineConf engineConf, Map<String, PluginConf> pluginReg) {
//		this.engineConf = engineConf;
//		this.pluginReg = pluginReg;
//		this.writerMonitorPool = new MonitorPool();
//		this.readerMonitorPool = new MonitorPool();
//
//	}
//	
//
//	/**
//	 * Start a DataX job.
//	 *
//	 * @param jobConf
//	 *            Configuration for the DataX Job.
//	 * 
//	 * @return 0 for success, others for failure.
//	 * @throws InterruptedException
//	 *
//	 * @throws Exception
//       *
//       */
//
// 	public int start(JobConf jobConf,String groupid,int timeout)  {
//		logger.info('\n' + engineConf.toString() + '\n');
//		logger.info('\n' + jobConf.toString() + '\n');
//		logger.info("DataX startups .");
//
//		StoragePool storagePool = new StoragePool(jobConf, engineConf, PERIOD);
//		NamedThreadPoolExecutor readerPool = null;
//		List<NamedThreadPoolExecutor> writerPool =  null;
//        try{
//        	readerPool = initReaderPool(jobConf,
//    				storagePool,groupid,timeout);
//        	writerPool = initWriterPool(jobConf,
//    				storagePool,timeout,groupid);
//        }catch(Exception ex) {
//        	MapUtil.addException(ex.getMessage());
//        	ex.printStackTrace();
//        	// throw new DataExchangeException("false","200");
//        	 // return Engine.Constant.RETURN_CODE;
//        }
//		//NamedThreadPoolExecutor 
//		//List<NamedThreadPoolExecutor> 
//
//		logger.info("DataX starts to exchange data .");
////		Boolean isTimeout = timeoutMap.get(groupid);
////		if(null != isTimeout && isTimeout) {
////			// 存在任务超时
////			readerPool.shutdownNow();
////			for (NamedThreadPoolExecutor dp : writerPool) {
////				dp.shutdownNow();
////			}
////			timeoutMap.remove(groupid);
////			return 200;
////		}
//		readerPool.shutdown();
//		for (NamedThreadPoolExecutor dp : writerPool) {
//			dp.shutdown();
//		}
//		
//		int sleepCnt = 0;
//		int retcode = 0;
//        try{
//    		while (true) {
//    			//System.out.println(1);
//    			/* check reader finish? */
//    			/* 如果关闭后所有任务都已完成，则返回 true。*/
//    			boolean readerFinish = readerPool.isTerminated();
//    			//logger.info("--------------------------------print readerFinish--------------------------------------"+readerFinish);
//    			if (readerFinish) {
//    				storagePool.closeInput();
//    			}
//    			//System.out.println(2);
//    			boolean writerAllFinish = true;
//    			//System.out.println(3);
//    			NamedThreadPoolExecutor[] dps = writerPool
//    					.toArray(new NamedThreadPoolExecutor[0]);//?
//    			//System.out.println(4);
//    			/* check each DumpPool */
//    			for (NamedThreadPoolExecutor dp : dps) {
//    				if (!readerFinish && dp.isTerminated()) {
//    					logger.error(String.format("DataX Writer %s failed .",
//    							dp.getName()));
//    					writerPool.remove(dp);
//    				} else if (!dp.isTerminated()) {
//    					writerAllFinish = false;
//    				}
//    			}
//
//    			if (readerFinish && writerAllFinish) {
//    				logger.info("DataX Reader post work begins .");
//    				readerPool.doPost();
//    				logger.info("DataX Reader post work ends .");
//
//    				logger.info("DataX Writers post work begins .");
//    				for (NamedThreadPoolExecutor dp : writerPool) {
//    					dp.getParam().setOppositeMetaData(
//    							readerPool.getParam().getMyMetaData());
//    					dp.doPost();
//    				}
//    				logger.info("DataX Writers post work ends .");
//
//    				logger.info("DataX job succeed .");
//    				break;
//    			} else if (!readerFinish && writerAllFinish) {
//    				logger.error("DataX Writers finished before reader finished.");
//    				logger.error("DataX job failed.");
//    				readerPool.shutdownNow();
//    				readerPool.awaitTermination(3, TimeUnit.SECONDS);
//    				
//    				break;
//    			}
//    			Thread.sleep(1000);
//    			
//    			sleepCnt++;
//
//    			if (sleepCnt % PERIOD == 0) {
//    				/* reader&writer count num of thread */
//    				StringBuilder sb = new StringBuilder();
//    				sb.append(String.format("ReaderPool %s: Active Threads %d .",
//    						readerPool.getName(), readerPool.getActiveCount()));
//    				logger.info(sb.toString());
//
//    				sb.setLength(0);
//    				for (NamedThreadPoolExecutor perWriterPool : writerPool) {
//    					sb.append(String.format(
//    							"WriterPool %s: Active Threads %d .",
//    							perWriterPool.getName(),
//    							perWriterPool.getActiveCount()));
//    					logger.info(sb.toString());
//    					sb.setLength(0);
//    				}
//    				logger.info(storagePool.getPeriodState());
//    			}
//    		}
//        }catch(Exception ex){
//        	MapUtil.addException(ex.getMessage());
//        	return Constant.RETURN_CODE;
//        }
//
//		StringBuilder sb = new StringBuilder();
//
//		sb.append(storagePool.getTotalStat());
//		long discardLine = this.writerMonitorPool.getDiscardLine();
//		sb.append(String.format("%-26s: %19d\n", "Total discarded records",
//				discardLine));
//
//		logger.info(sb.toString());
//
//		//Reporter.stat.put("DISCARD_RECORDS", String.valueOf(discardLine));
//		//Reporter reporter = Reporter.instance();
//		//reporter.report(jobConf);
//
//		long total = -1;
//		boolean writePartlyFailed = false;
//		for (Storage s : storagePool.getStorageForReader()) {
//			String[] lineCounts = s.info().split(":");
//			long lineTx = Long.parseLong(lineCounts[0]);
//			if (total != -1 && total != lineTx) {
//				writePartlyFailed = true;
//				logger.error("Writer partly failed, for " + total + "!="
//						+ lineTx);
//			}
//			total = lineTx;
//		}
//		//System.out.println(writePartlyFailed+"-------------------------");
//		if(writePartlyFailed){
//			
//			return Constant.RETURN_CODE;
//		}else{
//			//如果处理成功，那么去更新excle里面的时间点
//			//获取该job的id号,调用excle接口，更新该job的时间点
//			//String jobId = jobConf.getId();
//			return retcode;
//		}
//		//return writePartlyFailed ? 200 : retcode;
//	}
// 	
// 	// 输入datax日志信息到指定目录
//	public static void confLog(String logFileName) {
//		Calendar calendar = Calendar.getInstance();
//		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
//		String logDir = "logs/" + sd.format(calendar.getTime());
//		System.setProperty("log.dir", logDir);
//		// f = new java.text.SimpleDateFormat("HHmmss");
//		// String logFile = jobId + "." + f.format(c.getTime()) + ".log";
//		System.setProperty("log.file", logFileName);
//		//String path = initpath();
//		PropertyConfigurator.configure(logFormatFilePath);
//	}
//
//    private static String OS = System.getProperty("os.name").toLowerCase();
//    
//    /**
//    * 方法名:  findLogFormatFilePath
//    * 方法功能描述: 获取日志格式化文件的路径
//    * @return 文件路径
//    * @Author: 张飞
//    * @Create Date:  2015年1月12日 下午4:38:57
//     */
//	public static String findLogFormatFilePath() {
//		String logFormatFilePath = Thread.currentThread()
//				.getContextClassLoader().getResource("").toString();
//		logger.info("logFormatFilePath = "+logFormatFilePath);
//		logFormatFilePath = logFormatFilePath.substring(
//				logFormatFilePath.indexOf("/") + 1,
//				logFormatFilePath.indexOf("webapps"));
//		logger.info("logFormatFilePath = "+logFormatFilePath);
//		logFormatFilePath = logFormatFilePath + "bin/conf/log4j.properties";
//		// 判断操作系统类型
//		String os = System.getProperty("os.name").toLowerCase();
//		if (!os.contains("win")) {
//			logFormatFilePath = "/" + logFormatFilePath;
//		}
//		logger.info("logFormatFilePath = "+logFormatFilePath);
//		return logFormatFilePath;
//	}
//    
//	
//	public static String initpath() {
//		Properties p = new Properties();
//		InputStream input = Thread.currentThread().getContextClassLoader()
//				.getResourceAsStream("app.properties");
//		try {
//			p.load(input);
//			if (OS.contains("win")) {
//				return p.get("log4jpath") != null ? (String) p.get("log4jpath")
//						: null;
//			} else {
//				return p.get("linuxlog4j") != null ? (String) p
//						.get("linuxlog4j") : null;
//			}
//		} catch (IOException e) {
//			MapUtil.addException(e.getMessage());
//			e.printStackTrace();
//		} finally {
//			try {
//				input.close();
//			} catch (IOException e) {
//				MapUtil.addException(e.getMessage());
//				e.printStackTrace();
//			}
//		}
//		return null;
//	}
//	
//
//	
//	/**
//	 * 新增代码，将table字符串封装到到一个字符串数组里面
//	 * @param tables tables字符串
//	 * @return
//	 */
//	private String [] praseToArray(String tables) {
//		//判断tables是否为空，tables有可能是多个表又,拼接而成的
//		if(!StringUtils.isBlank(tables)){
//			return tables.split(Constant.SPLIT_CHAR);
//		}
//		return null;
//	}
//	
//   /**
//    * 重新拼接sql，
//    * @param name 数据库类型，根据读取的插件类型来判断有oracle，mysql，server
//    * @param sql sqlyuj
//    * @param oracleReadTag
//    * @param tag 是否是首次执行任务
//    * @param groupid xml文件配置的jobGroupId
//    * @param dbNow 源库当前时间
//    * @param JobConf
//    * @return string
//    */
//	private String getSql(String name, String sql, String oracleReadTag,
//			boolean tag,String groupid,String dbNow,JobConf jobConf) {
//		if (StringUtils.isBlank(sql)) {
//            MapUtil.addException("[ "+sql+" ] is null .");
//			throw new NullPointerException(sql + "is not null");
//		}
//		String beforeForm = null;// from关键字前的字符串
//		String endForm = null;// from关键字后的字符串
//		if (sql.contains(Constant.SQL_CONSTANT_FROM)
//				|| sql.contains((Constant.SQL_CONSTANT_FROM)
//						.toUpperCase())) {
//			// 从from关键那里进行截取
//			String[] sqlArrays = sql.split(Constant.SQL_CONSTANT_FROM);
//			if (1 == sqlArrays.length) { // add by zf 判断对大写的FROM的分割
//				sqlArrays = sql.split("FROM");
//			}
//			beforeForm = sqlArrays[0];
//			// 添加DATAX_TASKGROUPID字段，回滚时精确删除数据
//			if (FileUtil.isRollBack) {
//				beforeForm = beforeForm + " ,'" + groupid + "' as " + "\""
//						+ Constants.DATAX_TASKGROUPID + "\" ";
//				if (tag) {
//					// 自动添加精确回滚的字段
//					EngineFuntion.autoCreateRollbackFiled(getConnectionByJobConf(jobConf), jobConf);
//				}
//			}
//			endForm = sqlArrays[1];
//		}
//		String makeSql = beforeForm + Constant.SQL_CONSTANT_FROM + " "
//				+ endForm;
//		if (StringUtils.trimToEmpty(name).equals(
//				Constant.SQL_CONSTANT_ORACLE)) {
//			if("3".equals(oracleReadTag)) {
//				// 社保视图没有插入时间和更新时间，不做任何处理
//			} else if ("2".equals(oracleReadTag)) {
//				// 第一次更新执行的时候 不执行更新数据，因此 where 1>2
//				beforeForm = beforeForm + Constant.SQL_CONSTANT_COMMA
//						+ " to_char(sysdate,'yyyy-MM-dd HH24:mi:ss') as "
//						+ Constant.SQL_FIELD_VALUE + " ";
//				makeSql = beforeForm + Constant.SQL_CONSTANT_FROM + " "
//						+ endForm;
//				if (tag) {
//					makeSql = makeSql + " AND 1>2 ";
//				}
//			} else {
//				beforeForm = beforeForm + Constant.SQL_CONSTANT_COMMA
//						+ " to_char(sysdate,'yyyy-MM-dd HH24:mi:ss') as "
//						+ Constant.SQL_FIELD_VALUE + " ";
//				makeSql = beforeForm + Constant.SQL_CONSTANT_FROM + " "
//						+ endForm;
//				if (tag) {
//					makeSql = makeSql + " and create_time <= to_date('"+dbNow+"','yyyy-mm-dd hh24:mi:ss')";
//				}
//			}
//
//		} else if (StringUtils.trimToEmpty(name).equals(
//				Constant.SQL_CONSTANT_MYSQL)) {
//			if ("3".equals(oracleReadTag)) {
//				// 社保视图没有插入时间和更新时间，不做任何处理
//			} else if ("2".equals(oracleReadTag)) { // 更新插件
//				// 第一次更新执行的时候 不执行更新数据，因此 where 1>2
//				beforeForm = beforeForm + Constant.SQL_CONSTANT_COMMA
//						+ " NOW() as " + Constant.SQL_FIELD_VALUE + " ";
//				makeSql = beforeForm + Constant.SQL_CONSTANT_FROM + " "
//						+ endForm ;
//				if (tag) {
//					makeSql = makeSql + " and (1>2) ";
//				}
//			} else {
//				beforeForm = beforeForm + Constant.SQL_CONSTANT_COMMA
//						+ " NOW() as " + Constant.SQL_FIELD_VALUE + " ";
//				makeSql = beforeForm + Constant.SQL_CONSTANT_FROM + " "
//						+ endForm ;
//				if (tag) { // 首次执行任务直接添加过滤条件,否则在后面进行组织SQL
//					if (Task.isOpenUpdateSyncFlg) {
//						makeSql = makeSql + " and (insert_datetime<='" + dbNow + "' or sync_flg = 0) ";
//					}else {
//						makeSql = makeSql + " and (insert_datetime<='" + dbNow + "') ";
//					}
//				}
//			}
//		}
//		return makeSql;
//	}
//   
//	// 根据不同的数据库获取当前时间
//	private static String getSql(String type) {
//		String sql = "";
//		if ("oracle".equalsIgnoreCase(type)) {
//			sql = "SELECT SYSDATE FROM DUAL";
//		} else if ("mysql".equalsIgnoreCase(type)) {
//			sql = "select now()";
//		} else if ("sqlserver".equalsIgnoreCase(type)) {
//			sql = "select gettime()";
//		}
//		return sql;
//	}
//   
//	// 通过JobConf获取数据库链接
//	private static Connection getConnectionByJobConf(JobConf jobConf) {
//		// 插件类型
//		JobPluginConf pluConf = jobConf.getWriterConfs().get(0);
//		// =============获取数据库链接信息===============
//		String type = pluConf.getName();
//		// 根据插件类型获取数据库类型
//		if (type.startsWith("mysql")) {
//			type = "mysql";
//		} else if (type.startsWith("oracle")) {
//			type = "oracle";
//		}
//		String dbIp = pluConf.getPluginParams().getValue("ip");
//		String dbPort = pluConf.getPluginParams().getValue("port");
//		String dbName = pluConf.getPluginParams().getValue("dbname");
//		String dbUser = pluConf.getPluginParams().getValue("username");
//		String dbPassword = pluConf.getPluginParams().getValue("password");
//		String rack = pluConf.getPluginParams().getValue("rack");
//		// 获取数据库链接
//		return DBTestUtil.getConnection(type, dbIp, dbPort, dbName, dbUser,
//				dbPassword, rack);
//	}
//   
//	public static Timestamp getSqlDate(JobConf jobConf) throws SQLException {
//		Connection conn = null;
//		PreparedStatement pstm = null;
//		ResultSet rs = null;
//		String type = jobConf.getWriterConfs().get(0).getName();
//		// 根据插件类型获取数据库类型
//		if (type.startsWith("mysql")) {
//			type = "mysql";
//		} else if (type.startsWith("oracle")) {
//			type = "oracle";
//		}
//		try {
//			conn = getConnectionByJobConf(jobConf);
//			pstm = conn.prepareStatement(getSql(type));
//			rs = pstm.executeQuery();
//			if (rs.next()) {
//				java.sql.Timestamp sqlDate = rs.getTimestamp(1);
//				DATE_MAP.put(jobConf.getId(), sqlDate);
//				return sqlDate;
//			}
//		} catch (SQLException e) {
//			MapUtil.addException(e.getMessage());
//			e.printStackTrace();
//		} finally {
//			DBTestUtil.closeAll(conn, pstm, rs);
//		}
//		return null;
//	}
//   
//	
//	public static boolean delete(JobConf jobConf, String sql)
//			throws SQLException {
//		Connection conn = null;
//		PreparedStatement pstm = null;
//		ResultSet rs = null;
//		// 插件类型
//		JobPluginConf pluConf = jobConf.getWriterConfs().get(0);
//		String type = pluConf.getName();
//		// 根据插件类型获取数据库类型
//		if (type.startsWith("mysql")) {
//			type = "mysql";
//		} else if (type.startsWith("oracle")) {
//			type = "oracle";
//		}
//		try {
//			conn = getConnectionByJobConf(jobConf);
//			conn.setAutoCommit(false);
//			pstm = conn.prepareStatement(sql);
//			pstm.executeUpdate(sql);
//			conn.commit();
//			return true;
//		} catch (SQLException e) {
//			MapUtil.addException(e.getMessage());
//			e.printStackTrace();
//			conn.rollback();
//			return false;
//		} finally {
//			DBTestUtil.closeAll(conn, pstm, rs);
//		}
//
//	}
//	
//	/**
//	 * 拼接sql
//	 * @param sql
//	 * @return
//	 */
//	private String spliceSql(String sql) {
//		if (DealTools.isNull(sql)) {
//			MapUtil.addException("[ "+sql+" ] is null .");
//			throw new NullPointerException(" sql  is  null");
//		}
//		if (sql.contains("&lt;")) {
//			sql = sql.replaceAll("&lt;", "<");
//		}
//		if (sql.contains("&gt;")) {
//			sql = sql.replaceAll("&gt;", ">");
//		}
//		return sql;
//	}
//   
//	@SuppressWarnings({ "resource"})
//	private NamedThreadPoolExecutor initReaderPool(JobConf jobConf,
//			StoragePool sp,String groupid,int timeout) throws Exception  {
//        //从job配置文件里面获取plugin标签属性值
//		JobPluginConf readerJobConf = jobConf.getReaderConf();
//		
//		PluginConf readerConf = pluginReg.get(readerJobConf.getName());
//        //产生路径
//		if (readerConf.getPath() == null) {
//			readerConf.setPath(engineConf.getPluginRootPath() + "reader/"
//					+ readerConf.getName());
//		}
//
//		logger.info(String.format("DataX Reader %s try to load path %s .",
//				readerConf.getName(), readerConf.getPath()));
//		JarLoader jarLoader = new JarLoader(
//				new String[] { readerConf.getPath() });
//		
//		Class<?> myClass = jarLoader.loadClass(readerConf.getClassName());
//        
//		ReaderWorker readerWorkerForPreAndPost = new ReaderWorker(readerConf,
//				myClass,jobConf,timeout,groupid);
//		PluginParam sparam = jobConf.getReaderConf().getPluginParams();
//        //增加代码
//      //也可以从这里去从外部获取上次的查询时间点
//		String sql = sparam.getValue(ParamKey.sql);
//		/**
//		 * 新增加的代码
//		 */
//		String tables = sparam.getValue(ParamKey.tables);//可以获取tables
//		
//		String [] tableArray = this.praseToArray(tables);
//		//获取job id
//      if(null == tableArray){
//			MapUtil.addException("[tables] 标签值不能为空！");
//			throw new DataExchangeException("false", "tables标签不能为空!");
//      }
//      // 判断是否是多表查询
//      boolean isMultitable = checkMultitable(tables);
//      sql = inertMultitaField(groupid, readerConf, sql, isMultitable);
//      /**
//       * 获取job id
//       */
//      String jobId =  jobConf.getId();
//      logger.info("get job id:"+jobId);
//      if(StringUtils.isBlank(jobId)) {
//			MapUtil.addException("[job]标签值不能为空！");
//			throw new DataExchangeException("false", "job标签名称不能为空!");
//      }
//      // 获取源库当前时间_20150325_ZF
//      String dbNow = DateUtil.timestampToString(getSqlDate(jobConf));
//      
//      //获取<plugin>标签名称
//      String name = jobConf.getReaderConf().getName();
//      //是否是前置机
//      String isCenter = jobConf.getReaderConf().getPluginParams().getValue("iscenter");
//      // oracle 读取标识0-正常读取 1-插入数据读取 2-更新数据读取3-社保卡（没有更新时间和插入时间）
//      String oracleReadTag = "";
//      try {
//    	  oracleReadTag = jobConf.getReaderConf().getPluginParams().getValue("readtag");
//      }catch(Exception e) {
//    	  oracleReadTag = "0";
//      }
//      if (null == oracleReadTag || "".equals(oracleReadTag)) {
//    	  oracleReadTag = "1";
//      }
//      logger.info("plugin name = "+name +" oracleReadTag = "+oracleReadTag);
//       //根据jobid 去外界表查询该job的上一次时间点
//        //判断sql语句里面是否含有where 关键字
//        if(sql.contains(StringUtils.trimToEmpty(Constant.SQL_CONSTANT_WHERE)) || 
//        		sql.contains(StringUtils.trimToEmpty(Constant.SQL_CONSTANT_WHERE).toUpperCase())){
//        	String native_sql = sql ;
//        	//去job表里面去查询获取job对象
//        	String mysql = "select id, DATE_FORMAT(MAX(DATE),'%Y-%c-%d %H:%i:%s')  AS DATE, jobid, state, isRollBack,srcTable,distTable FROM optbakcup WHERE jobId='"+jobConf.getId()+"' and state='1' and isRollBack ='3'";
//        	DBRecord dbrecord = DBRecordBakcup.selectData(mysql);
//        	if(null != dbrecord && !DealTools.isNull(dbrecord.getQueryDate())){
//        		logger.info("========== optbakcup value ========== "+dbrecord);
//        		//获取该jobib执行成功时的最大maxdate，然后向后退一天
//        		//读取excle,获取jobid，然后根据该id去获取其它字段进行业务sql拼写
//        		String endSql = "";
//				if (!isMultitable) {
//					endSql = this.getSql(name, native_sql,oracleReadTag,false,groupid,dbNow,jobConf);
//				}else {
//					endSql = sql;
//				}
//            	if(null == endSql) {
//					MapUtil.addException("[ " + endSql + " ]sql语句语法错误！");
//            		throw new DataExchangeException("false",endSql+":sql grammar error!");
//            	}
//            	endSql = spliceSql(endSql);
//            	String groupStr = "";//group的后半部分
//            	String beforStr = "";//group的前 半部分
//            	if(endSql.contains("group by")){
//    				String gropus [] = endSql.split("group by");
//    				groupStr = gropus[1];
//    				beforStr = gropus[0];
//    			}
//            	if("".equals(beforStr)) {
//            		beforStr = endSql;
//            	}
//            	String dates = dbrecord.getQueryDate();
//            	logger.info(dates.split(" ")[0] + " ========== " + dates);
//        		if(name.equals(Constant.SQL_CONSTANT_ORACLE)) {
//					String str = Constant.SQL_CONSTANT_AND + assemblySqlQueryCondion(tableArray, dates, name, true,oracleReadTag,dbNow);
//					beforStr = beforStr + str;
//					if (!"".equals(groupStr)) {
//						beforStr = beforStr + "group by " + groupStr;
//					}
//        		}else if(name.equals(Constant.SQL_CONSTANT_MYSQL)) {
//        			//如果是前置机
//        			if(isCenter.equals(Constant.FONTMATHINE_TO_DATACENTER_CODE)) {
//        				String str = " " + assemblySqlQueryCondion(tableArray,dates,name,true,oracleReadTag,dbNow);
//        				beforStr =  beforStr +str;
//        				if(!"".equals(groupStr)) {
//        					beforStr = beforStr + "group by " +groupStr;
//        				}
//        			}
//        		}else if(name.equals(Constant.SQL_CONSTANT_SQLSERVER)){
//        			//TODO sqlServer暂时未做开发
//        		}
////        		 if(-1 == beforStr.lastIndexOf("AND")) {
////        			 beforStr = beforStr.substring(0, beforStr.lastIndexOf("AND"));
////        		 }
//        		logger.info(" ========== final select query sql ========== "+beforStr);
//            	sparam.putValue(ParamKey.sql, beforStr);
//        		
//			} else {
//				String finalsql = "";
//				if (!isMultitable) {
//					finalsql = this.getSql(name, native_sql,oracleReadTag,true,groupid,dbNow,jobConf);
//				}else {
//					finalsql = native_sql;
//				}
//				String endSql = spliceSql(finalsql);
//				logger.info("========== The first time synchronization query sql ========== "
//						+ endSql);
//				sparam.putValue(ParamKey.sql, endSql);
//			}
//        }
//		readerWorkerForPreAndPost.setParam(sparam);
//		readerWorkerForPreAndPost.init();
//		logger.info("DataX Reader prepare work begins .");
//		int code = readerWorkerForPreAndPost.prepare(sparam);
//		if (code != 0) {
//			MapUtil.addException("datax reader prepare work failed.");
//			throw new DataExchangeException("DataX Reader prepare work failed!");
//		}
//		logger.info("DataX Reader prepare work ends .");
//
//		logger.info("DataX Reader split work begins .");
//		List<PluginParam> readerSplitParams = readerWorkerForPreAndPost
//				.doSplit(sparam);
//		logger.info(String.format(
//				"DataX Reader splits this job into %d sub-jobs",
//				readerSplitParams.size()));
//		logger.info("DataX Reader split work ends .");
//
//		int concurrency = readerJobConf.getConcurrency();
//		if (concurrency <= 0 || concurrency > MAX_CONCURRENCY) {
//			throw new IllegalArgumentException(String.format(
//					"Reader concurrency set to be %d, make sure it must be between [%d, %d] .",
//					concurrency, 1, MAX_CONCURRENCY));
//		}
//
//		concurrency = Math.min(concurrency,
//				readerSplitParams.size());
//		if (concurrency <= 0) {
//			concurrency = 1;
//		}
//		readerJobConf.setConcurrency(concurrency);
//
//		NamedThreadPoolExecutor readerPool = new NamedThreadPoolExecutor(
//				readerJobConf.getId(), readerJobConf.getConcurrency(),
//				readerJobConf.getConcurrency(), 1L, TimeUnit.SECONDS,
//				new LinkedBlockingQueue<Runnable>());
//
//		readerPool.setPostWorker(readerWorkerForPreAndPost);
//		readerPool.setParam(sparam);
//
//		readerPool.prestartAllCoreThreads();
//
//		logger.info("DataX Reader starts to read data ");
//		for (PluginParam param : readerSplitParams) {
//			ReaderWorker readerWorker = new ReaderWorker(readerConf, myClass,jobConf,timeout,groupid);
//			readerWorker.setParam(param);
//			readerWorker.setLineSender(new BufferedLineExchanger(null, sp
//					.getStorageForReader(), this.engineConf
//					.getStorageBufferSize()));
//			readerPool.execute(readerWorker);
//			readerMonitorPool.monitor(readerWorker);
//		}
//
//		return readerPool;
//	}
//	
//	// 添加多表查询时的字段
//	private String inertMultitaField(String groupid, PluginConf readerConf,
//			String sql, boolean isMultitable) {
//		if (isMultitable) {
//			    StringBuffer addField = new StringBuffer();
//				if (FileUtil.isRollBack) {
//					// datax GroupId 字段
//					addField.append(",'" + groupid + "' as " + "\""+ Constants.DATAX_TASKGROUPID + "\" ");
//				}
//				StringBuffer sb = new StringBuffer(sql);
//				if(readerConf.getName().startsWith("mysql")) {
//					// mysql同步时间字段
//					addField.append(",now() as sync_date ");
//				}else {
//					// oracle同步时间字段
//					addField.append(",to_char(sysdate,'"+Constant.DATE_FORMAT+"') as sync_date ");
//				}
//				// 组装datax GroupId 字段和时间同步的字段
//				sb.insert(wherePosition(sql), addField.toString());
//				sql = sb.toString();
//		  }
//		return sql;
//	}
//	
//	// 判断是不是多表级联查询
//	private static boolean checkMultitable(String tables) {
//		if (tables.contains("#")) {
//			return true;
//		}
//		return false;
//	}
//	
//	// 判断查询语句首个where或者WHERE
//	private static int wherePosition(String sql) {
//		int position = sql.indexOf("from");
//		if (-1 == position) {
//			return sql.indexOf("FROM");
//		}
//		return position;
//	}
//	
//	/**
//	 * 如果是多表级联查询的话，拼接查询条件，是二级库到三级库
//	 * @param tableArray
//	 * @param dates 多个表，其中表明和别名用#拼接例如admin#a,test#t
//	 * @param dbtype
//	 * @param iscenter 是否是前置机
//	 * @param oracleReadTag oracle读取标识
//	 * @param dbNow 源库当前的时间
//	 * @return
//	 */
//	private static String assemblySqlQueryCondion(String [] tableArray,String dates,String dbtype,boolean iscenter,String oracleReadTag,String dbNow) {
//		StringBuffer sb = new StringBuffer();
//		String condionSql = "";
//		String tempWhereSql = "";
//		String tb = "";
//	    if(tableArray.length >= 1) {
//	    	  for(int i=0;i<tableArray.length;i++) {
//	    		  tb = tableArray[i];
//	    		  if(tb.contains("#")) {
//	    			  // 多表查询条件组织
//	    			  String [] s = tb.split("#");
//	    			  tempWhereSql = queryCondtion(dates,oracleReadTag,dbNow,dbtype,true,s[1]);
//					if (i != 0 && "mysqlreader".equals(dbtype)) { // 第一次执行不截取and
//						if (tempWhereSql.toString().contains("and")) {
//							tempWhereSql = tempWhereSql.substring(tempWhereSql
//									.toString().indexOf("and") + 3,
//									tempWhereSql.length());
//						}
//					}
//	    			  sb.append(tempWhereSql);
//	    			  tempWhereSql = "";
//	    		  }else {
//	    			  // 单表查询过滤条件组装
//	    			  return queryCondtion(dates,oracleReadTag,dbNow,dbtype,false,null);
//	    		  }
//	    	  }
//	      }
//         if(sb.toString().contains("and")) {
//        	 condionSql = sb.substring(0, sb.toString().lastIndexOf("and"));
//         }
//         return condionSql;
//	}
//	
//	/**
//	* 方法名:  queryCondtion
//	* 方法功能描述: 组装查询的SQL过滤条件
//	* @param bakDate 记录库中的最大时间
//	* @param readTag 读取标识
//	* @param dbNow 源库当前时间
//	* @param dbtype 数据库类型
//	* @param isMulTable 是否是多表
//	* @param tableAlias 多表时 表的别名
//	* @return 组织好的过滤条件
//	* @Author: 张飞
//	* @Create Date:  2015年3月26日 下午2:09:27
//	 */
//	public static String queryCondtion(String bakDate, String readTag,String dbNow,String dbtype,boolean isMulTable,String tableAlias) {
//		StringBuffer sb = new StringBuffer();
//		if (Constant.SQL_CONSTANT_ORACLE.equals(dbtype)) {
//			assemblyQueryConditionByOracle(bakDate, readTag, dbNow, isMulTable,
//					tableAlias, sb);
//		}else if(Constant.SQL_CONSTANT_MYSQL.equals(dbtype)) {
//			 if(!"3".equals(readTag)) {
//				assemblyQueryCondtionByMysql(bakDate, dbNow, isMulTable,
//						tableAlias, sb);
//			  }
//		}
//		return sb.toString();
//	}
//	
//	// 组织Oracle的查询条件语句
//	private static void assemblyQueryConditionByOracle(String bakDate,
//			String readTag, String dbNow, boolean isMulTable,
//			String tableAlias, StringBuffer sb) {
//		if("1".equals(readTag)) { 
//			  // 新增数据 【 create_time >= 记录库中的最大时间】并且【create_time <= 源库的当前时间】
//			readTagOneCondition(bakDate, dbNow, isMulTable, tableAlias, sb);
//		  }else if("2".equals(readTag)) {
//			  // 更新数据 【update_time >= 记录库中最大的时间 并且 update_time > create_time 在并且update_time <=源库的当前时间 】
//			  readTagTwoCondition(bakDate, dbNow, isMulTable, tableAlias, sb);
//		  }else if("3".equals(readTag)) {
//			  // 社保视图,没有更新和插入字段，每次全部插入，因此不做任务处理
//			
//		  }else {
//			  // (create_time >= 记录库中的最大时间 and create_time<=dbNow) || (update_time >= 记录库中的最大时间 and update_time <= dbNow)
//			readTagOtherCondition(bakDate, dbNow, isMulTable, tableAlias, sb);
//		  }
//	}
//	
//	// 读取标识为其它的查询条件 
//	private static void readTagOtherCondition(String bakDate, String dbNow,
//			boolean isMulTable, String tableAlias, StringBuffer sb) {
//		if (isMulTable) {
//			sb.append(" (("+tableAlias+".");
//			sb.append("create_time >= to_date('"+bakDate+"','"+Constant.DATE_FORMAT+"')");
//			sb.append(" and "+tableAlias+".");
//			sb.append("create_time <= to_date('"+dbNow+"','"+Constant.DATE_FORMAT+"'))");
//			sb.append(" or ");
//			sb.append("("+tableAlias+".");
//			sb.append("update_time >= to_date('"+bakDate+"','"+Constant.DATE_FORMAT+"')");
//			sb.append(" and "+tableAlias+".");
//			sb.append("update_time <= to_date('"+dbNow+"','"+Constant.DATE_FORMAT+"')))");
//			sb.append(" and");
//		}else {					
//			sb.append(" ((create_time >= to_date('"+bakDate+"','"+Constant.DATE_FORMAT+"')");
//			sb.append(" and create_time <= to_date('"+dbNow+"','"+Constant.DATE_FORMAT+"'))");
//			sb.append(" or ");
//			sb.append("(update_time >= to_date('"+bakDate+"','"+Constant.DATE_FORMAT+"')");
//			sb.append(" and update_time <= to_date('"+dbNow+"','"+Constant.DATE_FORMAT+"')))");
//		}
//	}
//
//	// 读取标识oracleReadTag=2的查询条件
//	private static void readTagTwoCondition(String bakDate, String dbNow,
//			boolean isMulTable, String tableAlias, StringBuffer sb) {
//		if(isMulTable) {
//			  sb.append(" ("+tableAlias+".");
//			  sb.append("update_time >= to_date('"+bakDate+"','"+Constant.DATE_FORMAT+"')");
//			  sb.append(" and "+tableAlias+".");
//			  sb.append("update_time <= to_date('"+dbNow+"','"+Constant.DATE_FORMAT+"')");
//			  sb.append(" and "+tableAlias+".");
//			  sb.append("update_time >= ");
//			  sb.append(tableAlias+".");
//			  sb.append("create_time)");
//			  sb.append(" and");
//		  }else {					  
//			  sb.append(" (update_time >= to_date('"+bakDate+"','"+Constant.DATE_FORMAT+"')");
//			  sb.append(" and update_time <= to_date('"+dbNow+"','"+Constant.DATE_FORMAT+"')");
//			  sb.append(" and update_time >= create_time)");
//		  }
//	}
//	
//	// 读取标识oracleReadTag=1的查询条件
//	private static void readTagOneCondition(String bakDate, String dbNow,
//			boolean isMulTable, String tableAlias, StringBuffer sb) {
//		if (isMulTable) {
//			sb.append(" ("+tableAlias+".");
//			sb.append("create_time >= to_date('"+bakDate+"','"+Constant.DATE_FORMAT+"')");
//			sb.append(" and "+tableAlias+".");
//			sb.append("create_time <= to_date('"+dbNow+"','"+Constant.DATE_FORMAT+"'))");
//			sb.append(" and");
//		}else {					
//			sb.append(" (create_time >= to_date('"+bakDate+"','"+Constant.DATE_FORMAT+"')");
//			sb.append(" and create_time <= to_date('"+dbNow+"','"+Constant.DATE_FORMAT+"'))");
//		}
//	}
//	// 组织MySql的查询条件语句
//	private static void assemblyQueryCondtionByMysql(String bakDate,
//			String dbNow, boolean isMulTable, String tableAlias, StringBuffer sb) {
//		if (isMulTable) {
//			sb.append(" and (("+tableAlias+".");
//			sb.append("insert_datetime >= '" +bakDate +"'");
//			sb.append(" and "+tableAlias+".");
//			sb.append("insert_datetime <= '"+dbNow+"')");
//			if (Task.isOpenUpdateSyncFlg) {
//				sb.append(" or ");
//				sb.append(tableAlias + ".");
//				sb.append("sync_flg = 0)");
//			}
//			sb.append(" and");
//		}else {				
//			sb.append(" and ((insert_datetime >= '" +bakDate +"'");
//			sb.append(" and insert_datetime <= '"+dbNow+"')");
//			if (Task.isOpenUpdateSyncFlg) {
//				sb.append(" or sync_flg = 0)");
//			}else {
//				sb.append(")");
//			}
//		}
//	}
//	
//	
//	@SuppressWarnings("resource")
//	private List<NamedThreadPoolExecutor> initWriterPool(JobConf jobConf,
//			StoragePool sp,int timeout,String groupId)  throws Exception {
//		List<NamedThreadPoolExecutor> writerPoolList = new ArrayList<NamedThreadPoolExecutor>();
//		List<JobPluginConf> writerJobConfs = jobConf.getWriterConfs();
//		for (JobPluginConf dpjc : writerJobConfs) {
//			PluginConf writerConf = pluginReg.get(dpjc.getName());
//			if (writerConf.getPath() == null) {
//				writerConf.setPath(engineConf.getPluginRootPath() + "writer/"
//						+ writerConf.getName());
//			}
//
//			logger.info(String.format(
//					"DataX Writer %s try to load path %s .",
//					writerConf.getName(), writerConf.getPath()));
//			JarLoader jarLoader = new JarLoader(
//					new String[] { writerConf.getPath() });
//			Class<?> myClass = jarLoader.loadClass(writerConf.getClassName());
//			WriterWorker writerWorkerForPreAndPost = new WriterWorker(
//					writerConf, myClass,jobConf,timeout,groupId);
//			//logger.info("jobConf:"+jobConf);
//			
//			PluginParam writerParam = dpjc.getPluginParams();
//			writerWorkerForPreAndPost.setParam(writerParam);
//			writerWorkerForPreAndPost.init();
//
//			logger.info("DataX Writer prepare work begins .");
//			int code = writerWorkerForPreAndPost.prepare(writerParam);
//			if (code != 0) {
//				MapUtil.addException("datax writer prepare work failed.");
//				throw new DataExchangeException(
//						"DataX Writer prepare work failed!");
//			}
//			logger.info("DataX Writer prepare work ends .");
//			logger.info("DataX Writer split work begins .");
//			List<PluginParam> writerSplitParams = writerWorkerForPreAndPost
//					.doSplit(writerParam);
//			logger.info(String.format(
//					"DataX Writer splits this job into %d sub-jobs .",
//					writerSplitParams.size()));
//			logger.info("DataX Writer split work ends .");
//
//			int concurrency = dpjc.getConcurrency();
//			if (concurrency <= 0 || concurrency > MAX_CONCURRENCY) {
//				throw new IllegalArgumentException(String.format(
//						"Writer concurrency set to be %d, make sure it must be between [%d, %d] .",
//						concurrency, 1, MAX_CONCURRENCY));
//			}
//	
//			concurrency = Math.min(dpjc.getConcurrency(),
//					writerSplitParams.size());
//			if (concurrency <= 0) {
//				concurrency = 1;
//			}
//			dpjc.setConcurrency(concurrency);
//
//			NamedThreadPoolExecutor writerPool = new NamedThreadPoolExecutor(
//					dpjc.getName() + "-" + dpjc.getId(), dpjc.getConcurrency(),
//					dpjc.getConcurrency(), 1L, TimeUnit.SECONDS,
//					new LinkedBlockingQueue<Runnable>());
//
//			writerPool.setPostWorker(writerWorkerForPreAndPost);
//			writerPool.setParam(writerParam);
//
//			writerPool.prestartAllCoreThreads();
//			writerPoolList.add(writerPool);
//			logger.info("DataX Writer starts to write data .");
//			for (PluginParam pp : writerSplitParams) {
//				WriterWorker writerWorker = new WriterWorker(writerConf,
//						myClass,jobConf,timeout,groupId);
//				writerWorker.setParam(pp);
//				writerWorker.setLineReceiver(new BufferedLineExchanger(sp
//						.getStorageForWriter(dpjc.getId()), null,
//						this.engineConf.getStorageBufferSize()));
//				writerPool.execute(writerWorker);
//				writerMonitorPool.monitor(writerWorker);
//			}
//		}
//		return writerPoolList;
//	}
//	
//	
////	/**
////	 * 获取每个jobxml，然后根据group id获取每个job，然后根据每个job里面的sql语句进行count，在进行分页查询，重新拼写job
////	 * @param filepath
////	 * @return
////	 */
////	public Map<String,List<JobConf>> productJob(final String filepath){
////		List<String> xmls = ParseXMLUtil.loadFiles(filepath);
////		Map<String,List<JobConf>> gruopMap = new HashMap<String,List<JobConf>>();
////		for (String path : xmls) 
////		{
////			List<GroupConf> groupConfLists = ParseXMLUtil.loadGroupConfigIntoArrays(path);
////			if (!groupConfLists.isEmpty()) {
////				for (GroupConf groupConf : groupConfLists) {
////					List<JobConf> lists = groupConf.getJobConfs();
////					String groupId = groupConf.getGroupId();
////					for(JobConf jobConf : lists){
////						String type = jobConf.getReaderConf().getName();
////                        if(Engine.Constant.SQL_CONSTANT_ORACLE.equals(type)){
////                        	
////                        }						
////					}
////					
////				}
////			}
////		}
////		return null;
////	}
//	
//	
//	/**
//	 * 可以根据groupid  去执行job
//	 * @param groupid
//	 * @param path
//	 * @param frontMachineIp 前置机IP
//	 * @param logFileName
//	 * @param timeout 超时时间
//	 * @throws SQLException
//	 * @throws ClassNotFoundException
//	 * @throws IOException
//	 */
//	public static Map<String,String> runJobByGroupId (String  groupid,String path,String frontMachineIp,Object logFileName,int timeout) throws Exception{
//		
//		//update by zhangfei 增加返回日志文件名称
//		Map<String,String> retMap = new HashMap<String, String>();
//		// update by zhangfei 修改datax日志文件名称从调度系统获
//		if (null == logFileName || "".equals(logFileName)) {
//			// 处理没有传递过来的日志文件名称
//			logFileName = groupid + "_" + DateUtil.getNow("HHmmssSSS");
//		}
//		// 输入datax日志文件
//		confLog(logFileName.toString());
//		// 打印系统版本号到日志文件
//		logger.info("datax system version = ["+FileUtil.findValueByKey("conf.system.version")+"]");
//		logger.info("datax read jobxml file path = ["+path+"]");
//		//retMap.put("logName", logFileName);
//		EngineConf engineConf = ParseXMLUtil.loadEngineConfig();
//		//解析plugins配置文件
//		Map<String, PluginConf> pluginConfs = ParseXMLUtil.loadPluginConfig();
//
//		Engine engine = new Engine(engineConf, pluginConfs);
//		DBRecord dbrecord = null;
//		//DataxJobResoult jobResult = null;
//		//回滚map，其中list里面是状态码集合
//		Map<String,List<String>> rollBackMap = new HashMap<String,List<String>>();
//		//gruopMap 
//		Map<String,List<JobConf>> gruopMap = new HashMap<String,List<JobConf>>();
//		
//		//前置机集合
//		Map<String,List<String>> fepMap = new HashMap<String,List<String>>();
//		//前置机源表集合
//		Map<String,List<String>> feptbMap = new HashMap<String,List<String>>();
//		
//		//该map是每一组group里面所有job的时间，其中key为groupid，value为每个job的时间戳集合
//		Map<String,List<Timestamp>> timeMap = new HashMap<String,List<Timestamp>>();
//		List<String> xmls = null;
//        try{
//        	xmls = ParseXMLUtil.loadFiles(path);
//		}catch(Exception ex) {
//            MapUtil.addException(ex.getMessage());
//            ex.printStackTrace();
//			throw new DataExchangeException("false","系统异常!");
//		}
//		//List<GroupConf> groupConfLists = ParseXMLUtil.loadGroupConfigIntoArrays(path);
//		//组集合，key为组id，list为jobconf集合
//		Map<String, List<JobConf>> jobXmpMap = new HashMap<String, List<JobConf>>();
//		try{
//			for (String p : xmls) {
//				List<GroupConf> groupConfList = ParseXMLUtil.loadGroupConfigIntoArrays(p);
//				if (!groupConfList.isEmpty()) {
//					for (GroupConf groupConf : groupConfList) {
//						List<JobConf> lists = groupConf.getJobConfs();
//						String groupId = groupConf.getGroupId();
//						if(groupId.equals(groupid)){
//							jobXmpMap.put(groupId, lists);
//						}
//						//jobXmpMap.put(groupId, lists);
//						
//					}
//				}
//			}
//		}catch(Exception e1) {
//			MapUtil.addException(e1.getMessage());
//			throw new DataExchangeException("false","系统异常!");
//		}
//		
//		//for(String id : groupid)
//		{
//			List<JobConf> confs = jobXmpMap.get(groupid);
//			if(null != confs){
//				gruopMap.put(groupid, confs);
//				List<Timestamp> timeList = new ArrayList<Timestamp>();
//				for (JobConf jobConf : confs) {
//					//获取每个jobconf的时间点
//					timeList.add(Engine.getSqlDate(jobConf));
//				}
//				Collections.sort(timeList);
//				timeMap.put(groupid, timeList);
//				//timeMap.put(id, timeList);
//				
//			}
//		}
//		int rolkBackFlg = 0;
//		int retcode = 0;
//		//混滚状态，1为回滚，0为不回滚
//		//for(String id : groupid)
//		//{
//			String id = groupid;
//			List<JobConf> confs = jobXmpMap.get(id);
//			
//			//List<DataxJobResoult> jobList = new ArrayList<DataxJobResoult>();
//			List<String> list = new ArrayList<String>();
//			if(null != confs){
//				//前置机错误码集合
//				List<String> fepList = new ArrayList<String>();
//				//前置机的表明和时间点
//				List<String> tbNameList = new ArrayList<String>();
//				//前置机
//				Set<String> set = new HashSet<String>();
//				for(JobConf jobConf :confs){
//					try {
//						dbrecord = new DBRecord();
//						logger.info("---------------------------------- Engine start------------------------------------");
//						/**************************************************************************************/
//						retcode = engine.start(jobConf,groupid,timeout);
//						logger.info("---------------------------------- Engine end------------------------------------");
//						//读取jobconfxml配置文件
//						String iscenter = jobConf.getReaderConf().getPluginParams().getValue("iscenter");//是否是前置机
//						if(Constant.FONTMATHINE_TO_DATACENTER_CODE.equals(iscenter)){
//							set.add(iscenter);
//						}
//						
//						String writeTable = jobConf.getWriterConfs().get(0).getPluginParams().getValue(
//								com.taobao.datax.plugins.writer.oraclewriter.ParamKey.table);// 目标表
//				        String readTable = jobConf.getReaderConf().getPluginParams().getValue("tables");// 源表
//				        String jobId = jobConf.getId();
//						/********************************* 根据retcode执行插入操作 *******************************/
//						if(ReaderWorker.ERRORMAP.get(groupid) != null) {
//							list.add(String.valueOf(Constant.RETURN_CODE));// 加入每个job的状态码
//							//前置机
//							if(Constant.FONTMATHINE_TO_DATACENTER_CODE.equals(iscenter)){
//								fepList.add("200");
//								tbNameList.add(readTable+"$"+DATE_MAP.get(jobConf.getId()));
//							}
//							dbrecord.setState(String.valueOf(0));// 执行失败
//						}else{
//							list.add(String.valueOf(retcode));// 加入每个job的状态码
//							//如果是前置机
//							if(Constant.FONTMATHINE_TO_DATACENTER_CODE.equals(iscenter)){
//								//fepList.add("0");
//								fepList.add(String.valueOf(retcode));
//								tbNameList.add(readTable+"$"+DATE_MAP.get(jobConf.getId()));
//							}
//							dbrecord.setState(String.valueOf(1));// 执行成功
//						}
//						ReaderWorker.ERRORMAP.remove(groupid);// 移除错误
//						EngineFuntion.doTimeOutTask(groupid); // 移除超时的标识
//						// 执行时间点
//						java.sql.Timestamp date = DATE_MAP.get(jobConf.getId());
//						// 执行增加记录
//						dbrecord.setDate(date);
//						dbrecord.setDistTable(writeTable);
//						dbrecord.setIsRollBack(String.valueOf(3));// 没有执行回滚操作
//						dbrecord.setSrcTable(readTable);
//						dbrecord.setJobId(jobId);
//						// 向记录表里面写入数据
//						DBRecordBakcup.saveDbRecord(dbrecord);
//						
//						Thread.sleep(2*1000);
//					} catch (Exception e) {
//					MapUtil.addException(e.getMessage());
//					e.printStackTrace();
//					rolkBackFlg = 1;
//					list.add(String.valueOf(Constant.RETURN_CODE));// 加入每个job的状态码
//					}
//				}
//				rollBackMap.put(id, list);
//				logger.info("rollBackMap:"+rollBackMap.toString());
//				List<String> rollBackList = rollBackMap.get(id);
//				logger.info("rollBackList:"+rollBackList+"---id---"+id);
//				//进行对前置机的更新
//				fepMap.put(id, fepList);
//				feptbMap.put(id, tbNameList);
//				List<String> feps = fepMap.get(id);
//				List<String> tbs = feptbMap.get(id);
//				logger.info("[feps] value = "+feps);
//				logger.info("[tbs] value = "+tbs);
//				logger.info("[!feps.contains('200')] value = "+!feps.contains("200"));
//				logger.info("[!feps.isEmpty()] value = "+!feps.isEmpty());
//				logger.info("[!tbs.isEmpty()] value = "+!tbs.isEmpty());
//				logger.info("[set.contains(Engine.Constant.IS_CENTER_TRUE)] value = "+set.contains(Constant.FONTMATHINE_TO_DATACENTER_CODE));
//			if (Task.isOpenUpdateSyncFlg) {                  
//				if(!feps.contains("200") && !feps.isEmpty() && !tbs.isEmpty() && set.contains(Constant.FONTMATHINE_TO_DATACENTER_CODE)) {
//					logger.info("----------------------------------entry rest--------------------------------");
//					//调用前置机接口
//					Map<String,List<String>> map = new HashMap<String,List<String>>();
//					List<String> li = new ArrayList<String>();
//					for(String assyas : tbs){
//						String syncdate = assyas.split("[$]")[1];
//						String tables = assyas.split("[$]")[0];
//						if(tables.contains("#")){
//							tables = tables.split("#")[0];
//						}
//						if(map.containsKey(tables)){
//							List<String> lis = map.get(tables);
//							lis.add(syncdate);
//							Collections.sort(lis);
//							map.put(tables, lis);
//						}else{
//							li.add(syncdate);
//							Collections.sort(li);
//							map.put(tables, li);
//						}					
//					}
//					//调用rest服务
//					String restsys = FileUtil.findValueByKey("conf.frontend.rest");
//					if (null != restsys && "dfis".equals(restsys.trim())) { // 前置机是直报系统
//						updateSycnDto updateSycnDto = new updateSycnDto();
//						updateSycnDto.setDataMap(map);
//						updateSycnDto.setType("1");
//						logger.info("============== update difs system ==============");
//						try {
//							// 如果没有配置前置机IP,则默认取datax配置的前置机信息[conf.dfis.address=http://192.168.5.5:7001/dfis]
//							if (null == frontMachineIp || "".equals(frontMachineIp)) {
//								logger.info("call datax configration frontMachine infomation");
//								DataxClient.updateSycnRest(updateSycnDto);
//							} else {
//								logger.info("call quartz configration frontMachine infomation; [frontMachineIp] = "
//										+ frontMachineIp);
//								DataxClient.updateSycnRest(updateSycnDto,
//										frontMachineIp);
//							}
//						} catch (Exception e) {
//							MapUtil.addException("call dfis client error " + e.getMessage());
//							logger.error("call dfis client error : "
//									+ e.getMessage());
//							e.printStackTrace();
//						}
//					} else { // 前置机是ishare
//						com.dhcc.ishare.dto.syncData.updateSycnDto updateSycnDto = new com.dhcc.ishare.dto.syncData.updateSycnDto();
//						updateSycnDto.setDataMap(map);
//						updateSycnDto.setType("1");
//						
//						logger.info("============== update ishare system ==============");
//						
//						try {
//							// 如果没有配置前置机IP,则默认取datax配置的前置机信息[conf.ishare.address=http://192.168.5.5:7001/ishare]
//							if (null == frontMachineIp || "".equals(frontMachineIp)) {
//								logger.info("call datax configration frontMachine infomation");
//								com.dhcc.ishare.client.service.rest.DataxClient
//								.updateSycnRest(updateSycnDto);
//							} else {
//								logger.info("call quartz configration frontMachine infomation; [frontMachineIp] = "
//										+ frontMachineIp);
//								com.dhcc.ishare.client.service.rest.DataxClient
//								.updateSycnRest(updateSycnDto,
//										frontMachineIp);
//							}
//						} catch (Exception e) {
//							MapUtil.addException("call ishare client error " + e.getMessage());
//							logger.error("call ishare client error : "
//									+ e.getMessage());
//							e.printStackTrace();
//						}
//					}
//					logger.info("----------------------------------out rest--------------------------------");
//				}
//			}
//			// 执行回滚操作
//			if (FileUtil.isRollBack) {
//				logger.info("=================== run task failed rollback data ===================");
//				rolkBackFlg = rollbackdata(groupid, dbrecord, gruopMap,
//						timeMap, rolkBackFlg, retcode, id, rollBackList);
//			}   
//			  }
//			
//		   //根据情况决定 是否删除 
//		  if(null != confs){
//			  for(JobConf jobConf :confs){
//				DATE_MAP.remove(jobConf.getId());
//			  }
//		  }else{
//			MapUtil.addException("not find GroupId file : "+groupid);
//			// new Throwable("no groupid find in job file:" +groupid);
//			//logger.error("no groupid find in job file:" +groupid);
//		  }
//		  if(rolkBackFlg==1){
//			  retcode = -1;
//		  }
//		if (null != MapUtil.getException()) {
//			retMap.put("retCode", "-1");
//			retMap.put("failed", MapUtil.getException());
//			MapUtil.clearAll();
//		} else {
//			retMap.put("retCode", String.valueOf(retcode));
//			// retMap.put("failed", errormsg);
//		}
//		//throw new Exception("测试异常！==============");
//		  return retMap;
//	}
//
//	// 根据条件进行回滚删除数据
//	private static int rollbackdata(String groupid, DBRecord dbrecord,
//			Map<String, List<JobConf>> gruopMap,
//			Map<String, List<Timestamp>> timeMap, int rolkBackFlg, int retcode,
//			String id, List<String> rollBackList) throws SQLException,
//			ClassNotFoundException, IOException {
//		// 数据回滚
//		//int n = 0;
//		//rollBackList.add("200");
//		if (rollBackList.contains(String.valueOf(Constant.RETURN_CODE))) {
//			List<JobConf> JobConfList = gruopMap.get(id);
//			Timestamp minTime = timeMap.get(id).get(0);// 获取最早时间点
//			if(null != JobConfList){
//				for (JobConf job : JobConfList) {
//					rolkBackFlg = 1;
//					//n++;
//					// 目标表
//					String targetTable = job.getWriterConfs().get(0).getPluginParams().getValue("table");
//					// 源表
//					String sourceTable = job.getReaderConf().getPluginParams().getValue("tables");
//					String deleteSql = "";
//					// 插件名称
//					String plugName = job.getWriterConfs().get(0).getName();
//					if (Constants.MYSQLWRITER.equals(plugName)) {
//						deleteSql = "delete from " + targetTable
//								+ " where sync_date >= '" + minTime + "' and "
//								+ Constants.DATAX_TASKGROUPID + " = '"
//								+ groupid + "'";
//					} else if (Constants.ORACLEWRITER.equals(plugName)) {
//						// Oracle回滚删除语句
//						deleteSql = "DELETE FROM " + targetTable
//								+ " WHERE TO_CHAR(SYNC_DATE) >= '" + minTime
//								+ "' AND " + Constants.DATAX_TASKGROUPID
//								+ " = '" + groupid + "'";
//					} else if (Constants.MYSQLUPDATEWRITER.equals(plugName)
//							|| Constants.ORACLEUPDATEWRITER.equals(plugName)) {
//						// 不管是什么类型的更新插件都不进行回滚
//						return 0;
//					}
//					
//					logger.info("[Roll back delete sql statment ] = " + deleteSql);
//					boolean isTrue = Engine.delete(job, deleteSql);
//					saveRollbackRecord(dbrecord, retcode, job, targetTable,
//							sourceTable, isTrue);
//				}
//			}
//		}
//		return rolkBackFlg;
//	}
//
//	// 保存回滚的记录
//	private static void saveRollbackRecord(DBRecord dbrecord, int retcode,
//			JobConf job, String targetTable, String sourceTable, boolean isTrue)
//			throws ClassNotFoundException, IOException {
//		if (isTrue) {
//			// 回滚删除成功
//			dbrecord.setIsRollBack(String.valueOf(0));
//		} else {
//			// 回滚删除失败
//			dbrecord.setIsRollBack(String.valueOf(1));
//		}
//		String jobId = job.getId();
//		// 执行时间点
//		java.sql.Timestamp date = DATE_MAP.get(job.getId());
//		dbrecord.setState(String.valueOf(1));// 执行成功
//		if (retcode != 0) {
//			dbrecord.setState(String.valueOf(0));// 执行失败
//		}
//		// 执行增加记录
//		dbrecord.setDate(date);
//		dbrecord.setDistTable(targetTable);
//		dbrecord.setSrcTable(sourceTable);
//		dbrecord.setJobId(jobId);
//		// 向记录表里面写入数据
//		DBRecordBakcup.saveDbRecord(dbrecord);
//	}
//
//}