package cn.com.codes.framework.app.blh;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import cn.com.codes.common.util.StringUtils;
import cn.com.codes.framework.app.view.UniversalView;
import cn.com.codes.framework.app.view.View;
import cn.com.codes.framework.common.JsonUtil;
import cn.com.codes.framework.common.util.Context;
import cn.com.codes.framework.common.util.MypmBean;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.hibernate.HibernateGenericController;
import cn.com.codes.framework.security.filter.SecurityContextHolder;
import cn.com.codes.framework.transmission.JsonInterface;
import cn.com.codes.framework.transmission.dto.BaseDto;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.framework.transmission.events.BusiResponseEvent;
import cn.com.codes.framework.transmission.events.RequestEvent;
import cn.com.codes.framework.transmission.events.ResponseEvent;
import cn.com.codes.userManager.blh.PasswordTool;
import cn.com.codes.framework.app.blh.BaseBizLogicHandler;
import cn.com.codes.framework.app.blh.BlhProxy;
import cn.com.codes.framework.app.blh.BusinessBlh;

public abstract class BusinessBlh extends BaseBizLogicHandler {

	private static boolean loadRepInfo = false;
	private static long reptVc=0L;
	private static long reptLimetVc =999+1;
	public BusinessBlh() {
		
	}

	public ResponseEvent performTask(RequestEvent req) throws BaseException {
		BusiResponseEvent resp = null;
		BusiRequestEvent ReqEvent = (BusiRequestEvent) req;
		//删除DOC中SEESION里的数据
//		SecurityContextHolderHelp.getSession().removeAttribute("docResult");
//		SecurityContextHolderHelp.getSession().removeAttribute("docInClipboard");
		View view = this._do(ReqEvent);
		if (view == null) {
			throw new BaseException("blh_01");
		}
		resp = view.toResponse();

		return resp;
	}

	
	public String addJsonPre(String preExpless,Object jsonObj) {
		return "{\"" + preExpless + "\":"+ JsonUtil.toJson(jsonObj) + "}";
	}
	public View _do(BusiRequestEvent req) throws BaseException {
		if("goAnalysisMain".equals(req.getDealMethod())){
			return reportViewProcess(req);
		}
		return BlhProxy.invokeBlh(req, this, this.getClass());
	}

	private View reportViewProcess(BusiRequestEvent req) throws BaseException{
		if(!secChk()){
			View result = new UniversalView();
			result.displayData("forward", "reptView");	
			return result;
		}
		return BlhProxy.invokeBlh(req, this, this.getClass());
	}
	
	private  boolean secChk(){
//		if (reptVc>reptLimetVc&&reptLimetVc!=Long.valueOf("9876543219").longValue()) {
//			return false;
//		}
		return true;
	}
	
	private long  getReptBroCount(){
		HibernateGenericController hibernate = (HibernateGenericController) Context.getInstance().getBean(
		"hibernateGenericController");
		Long count=null;;
		try {
			count = new Long(hibernate.findByFreePara("select count(*) from t_syslog where ACCESS_IP='1289630248984' ").get(0).toString());
		}catch (NumberFormatException e) {
			return 0;
		}catch (Exception e) {
			return 0;
		}
		return  count;
		//return  1000;
	}

	/**
	 * 用可变长参数就是为了不强制传入参数，没参数时 直接调用getView，虽然是可变长参数，
	 * 但只取第一个参数作为forward,第二个参数是提信信息或提示信息代码
	 * 
	 * @author liuyg 功能：
	 * @param result
	 * @return
	 */

	public UniversalView getView(String... resultMsg) {
		UniversalView view = new UniversalView();
		if (resultMsg.length >= 1 && resultMsg != null) {
			view.displayData("forward", resultMsg[0]);
			if (resultMsg.length >= 2 && resultMsg[1] != null) {
				view.displayData("message", resultMsg[1]);
			}
		}
		return view;
	}

	protected void writeResult(String resStr) {
		SecurityContextHolder.getContext().writeResponse(resStr);
	}

	protected String listToJson(List<JsonInterface> list) {
		StringBuffer sb = new StringBuffer(getPageInfo());
		int i = 0;
		if (list != null && list.size() > 0) {
			sb.append("{rows: [");
			for (JsonInterface obj : list) {
				i++;
				if (i != list.size()) {
					sb.append(obj.toStrList()).append(",");
				} else {
					sb.append(obj.toStrList());
				}

			}
			sb.append("]}");
		}
		return sb.toString();
	}

	protected String listToJsonBySB(boolean pageFlag, List<JsonInterface> list) {
		StringBuffer sb = new StringBuffer();
		if (pageFlag)
			sb.append(getPageInfo());
		int i = 0;
		if (list != null && list.size() > 0) {
			sb.append("{rows: [");
			for (JsonInterface obj : list) {
				i++;
				if (i != 1)
					obj.toString(sb.append(","));
				else
					obj.toString(sb);
			}
			sb.append("]}");
		}
		return sb.toString();
	}

	protected String listToStringBySB(List<String> list) {
		StringBuffer sb = new StringBuffer();
		int i = 0;
		if (list != null && list.size() > 0) {
			for (String obj : list) {
				i++;
				if (i != 1)
					sb.append("^").append(obj);
				else
					sb.append(obj);
			}
		}
		return sb.toString();
	}

	protected String getPageInfo() {
		Object pageInfo = SecurityContextHolder.getContext()
				.getAttr("pageInfo");
		if (pageInfo != null) {
			return pageInfo.toString();
		}
		return " $";
	}

	protected <T extends BaseDto> T getDto(Class<T> clasz, BusiRequestEvent req) {
		return (T) req.getDto();
	}

	protected Integer getPageNo() {
		return (Integer) SecurityContextHolder.getContext().getAttr("pageNo");
	}

	protected Integer getPageCount() {
		return (Integer) SecurityContextHolder.getContext()
				.getAttr("pageCount");
	}

	protected String getPageStr() {
		return getPageInfo();
		// return ((Integer)SecurityContextHolder.getContext().getAttr("pageNo")
		// + "/" +
		// (Integer)SecurityContextHolder.getContext().getAttr("pageCount"));
	}

	protected View globalAjax() {

		return getView("globalAjaxRest");
	}

	public String ObjArrList2Json(List<Object[]> list, StringBuffer sb,
			int idIndex) {
		if (sb == null) {
			sb = new StringBuffer();
		}
		int i = 0;
		if (list != null && list.size() > 0) {
			sb.append("{rows: [");
			for (Object[] values : list) {
				i++;
				if (i != list.size()) {
					this.ObjArr2Json(values, sb, idIndex, true, i);
				} else {
					this.ObjArr2Json(values, sb, idIndex, false, i);
				}
			}
			sb.append("]}");
		}
		Object pageInfo = SecurityContextHolder.getContext()
				.getAttr("pageInfo");
		if (pageInfo != null) {
			return sb.insert(0, pageInfo.toString()).toString();
		} else {
			return sb.toString();
		}
	}

	public String ObjArrList2Json(List<Object[]> list, StringBuffer sb,
			int idIndex, boolean deleteIdFlag) {
		if (sb == null) {
			sb = new StringBuffer();
		}
		int i = 0;
		if (list != null && list.size() > 0) {
			sb.append("{rows: [");
			for (Object[] values : list) {
				i++;
				if (i != list.size()) {
					this
							.ObjArr2Json(values, sb, idIndex, true, i,
									deleteIdFlag);
				} else {
					this.ObjArr2Json(values, sb, idIndex, false, i,
							deleteIdFlag);
				}
			}
			sb.append("]}");
		}
		Object pageInfo = SecurityContextHolder.getContext()
				.getAttr("pageInfo");
		if (pageInfo != null) {
			return sb.insert(0, pageInfo.toString()).toString();
		} else {
			return sb.toString();
		}
	}

	public String ObjArrList2JsonLongDate(List<Object[]> list, StringBuffer sb,
			int idIndex) {
		if (sb == null) {
			sb = new StringBuffer();
		}
		int i = 0;
		if (list != null && list.size() > 0) {
			sb.append("{rows: [");
			for (Object[] values : list) {
				i++;
				if (i != list.size()) {
					this.ObjArr2JsonLongDate(values, sb, idIndex, true, i);
				} else {
					this.ObjArr2JsonLongDate(values, sb, idIndex, false, i);
				}
			}
			sb.append("]}");
		}
		Object pageInfo = SecurityContextHolder.getContext()
				.getAttr("pageInfo");
		if (pageInfo != null) {
			return sb.insert(0, pageInfo.toString()).toString();
		} else {
			return sb.toString();
		}
	}

	private void ObjArr2Json(Object[] values, StringBuffer sb, int idIndex,
			boolean appendComma, int index) {
		sb.append("{");
		sb.append("id:'");
		sb.append(values[idIndex].toString());
		sb.append("',data: [0,");
		sb.append(index);
		sb.append(",'");
		int i = 0;
		for (Object obj : values) {
			if (i != idIndex) {
				if (i < values.length - 1) {
					if (obj != null && obj instanceof Date) {
						sb.append(StringUtils.formatShortDate((Date) obj))
								.append("','");
					} else {
						sb.append(obj == null ? "" : obj.toString()).append(
								"','");
					}
				} else {
					if (obj != null && obj instanceof Date) {
						sb.append(StringUtils.formatShortDate((Date) obj))
								.append("'");
					} else {
						sb.append(obj == null ? "" : obj.toString())
								.append("'");
					}
				}
				i++;
			} else if (idIndex == 0 && i == 0) {
				i++;
			}
		}
		sb.append("]");
		sb.append("}");
		if (appendComma) {
			sb.append(",");
		}
	}

	private void ObjArr2JsonLongDate(Object[] values, StringBuffer sb,
			int idIndex, boolean appendComma, int index) {
		sb.append("{");
		sb.append("id:'");
		sb.append(values[idIndex].toString());
		sb.append("',data: [0,");
		sb.append(index);
		sb.append(",'");
		int i = 0;
		for (Object obj : values) {
			if (i != idIndex) {
				if (i < values.length - 1) {
					if (obj != null && obj instanceof Date) {
						sb.append(StringUtils.formatLongDate((Date) obj))
								.append("','");
					} else {
						sb.append(obj == null ? "" : obj.toString()).append(
								"','");
					}
				} else {
					if (obj != null && obj instanceof Date) {
						sb.append(StringUtils.formatLongDate((Date) obj))
								.append("'");
					} else {
						sb.append(obj == null ? "" : obj.toString())
								.append("'");
					}
				}
				i++;
			} else if (idIndex == 0 && i == 0) {
				i++;
			}
		}
		sb.append("]");
		sb.append("}");
		if (appendComma) {
			sb.append(",");
		}
	}

	private void ObjArr2Json(Object[] values, StringBuffer sb, int idIndex,
			boolean appendComma, int index, boolean deleteIdFlag) {
		sb.append("{");
		sb.append("id:'");
		if (deleteIdFlag)
			sb.append(index);
		else
			sb.append(values[idIndex].toString());
		sb.append("',data: [0,");
		sb.append(index);
		sb.append(",'");
		int i = 0;
		for (Object obj : values) {
			if (i != idIndex) {
				if (i < values.length - 1) {
					if (obj != null && obj instanceof Date) {
						sb.append(StringUtils.formatShortDate((Date) obj))
								.append("','");
					} else {
						sb.append(obj == null ? "" : obj.toString()).append(
								"','");
					}
				} else {
					if (obj != null && obj instanceof Date) {
						sb.append(StringUtils.formatShortDate((Date) obj))
								.append("'");
					} else {
						sb.append(obj == null ? "" : obj.toString())
								.append("'");
					}
				}
				i++;
			} else if (idIndex == 0 && i == 0) {
				i++;
			}
		}
		sb.append("]");
		sb.append("}");
		if (appendComma) {
			sb.append(",");
		}
	}

	public String listConvertStr(List objectList, String appendComma){
		if(objectList == null || objectList.size() == 0){
			return "";
		}
		StringBuffer bf = new StringBuffer();
		Iterator it = objectList.iterator();
		int i = 0;
		while(it.hasNext()){
			if(i != 0)
				bf.append(appendComma);
			bf.append(it.next().toString());
			i++;
		}
		return bf.toString();
	}
	
	public String getReportReciveKeyValue(List reportReciveList){
		if(reportReciveList == null || reportReciveList.size() == 0){
			return "";
		}
		Object[] object = null;
		int i = 0;
		StringBuffer bfId = new StringBuffer();
		StringBuffer bfName = new StringBuffer();
		Iterator it = reportReciveList.iterator();
		while(it.hasNext()){
			object = (Object[])it.next();
			if(i >= 1){
				bfId.append(",");
				bfName.append(",");
			}
			bfId.append(object[0]);
			bfName.append(object[1]);
			i++;
		}
		return bfId.toString() + "$" + bfName.toString();
	}

	public static long getCurrVc() {
		return reptVc;
	}

	public static void setCurrVc(long currVc) {
		BusinessBlh.reptVc = currVc;
	}
	public static void setCurrVc() {
		BusinessBlh.reptVc = reptVc +1;
	}
}
