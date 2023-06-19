package cn.com.codes.analysisManager.web;

import java.util.HashMap;

import cn.com.codes.analysisManager.blh.AnalysisBlh;
import cn.com.codes.analysisManager.dto.AnalysisDto;
import cn.com.codes.framework.exception.BaseException;
import cn.com.codes.framework.transmission.events.BusiRequestEvent;
import cn.com.codes.framework.web.action.BaseAction;

@SuppressWarnings("unchecked")
public class AnalysisAction extends BaseAction<AnalysisBlh> {
	
	public static final long serialVersionUID = 1l;
	private AnalysisBlh analysisBlh;
	private AnalysisDto analysisDto = new AnalysisDto();

	protected String _getCustomBlhControlFlow(BusiRequestEvent reqEvent) {
		return null;
	}

	protected void _prepareRequest(BusiRequestEvent reqEvent)throws BaseException {
		reqEvent.setDto(analysisDto);
	}

	protected String _processResponse() throws BaseException {
		HashMap<?, ?> displayData = (HashMap<?, ?>) _getDisplayData();
		return super.forwardPage(displayData);
	}

	public AnalysisBlh getAnalysisBlh() {
		return analysisBlh;
	}

	public void setAnalysisBlh(AnalysisBlh analysisBlh) {
		this.analysisBlh = analysisBlh;
	}

	public AnalysisDto getAnalysisDto() {
		return analysisDto;
	}

	public void setAnalysisDto(AnalysisDto analysisDto) {
		this.analysisDto = analysisDto;
	}
}
