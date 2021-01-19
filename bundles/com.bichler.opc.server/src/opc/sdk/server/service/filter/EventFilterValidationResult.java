package opc.sdk.server.service.filter;

import java.util.ArrayList;
import java.util.List;

import opc.sdk.core.context.StringTable;
import opc.sdk.core.enums.DiagnosticsMasks;

import org.opcfoundation.ua.builtintypes.ServiceResult;
// import org.opcfoundation.ua.core.EventFilterResult;
import org.opcfoundation.ua.core.MonitoringFilterResult;

/**
 * 
 * @author Thomas Z&ouml;chbauer
 *
 */
public class EventFilterValidationResult {
	private ServiceResult status = null;
	private List<ServiceResult> selecteClauseResults = null;
	private ContentFilterValidationResult whereClauseResults = null;

	public EventFilterValidationResult() {
		this.selecteClauseResults = new ArrayList<ServiceResult>();
	}

	public List<ServiceResult> getSelecteClauseResults() {
		return selecteClauseResults;
	}

	public ServiceResult getStatus() {
		return status;
	}

	public ContentFilterValidationResult getWhereClauseResults() {
		return whereClauseResults;
	}

	/**
	 * ADD NOT SET
	 * 
	 * public void setSelecteClauseResults( List<ServiceResult>
	 * mSelecteClauseResults) { m_selecteClauseResults = mSelecteClauseResults; }
	 */
	public void setStatus(ServiceResult mStatus) {
		status = mStatus;
	}

	public void setWhereClauseResults(ContentFilterValidationResult mWhereClauseResults) {
		whereClauseResults = mWhereClauseResults;
	}

	public void addSelecteClauseResults(ServiceResult clauseResult) {
		this.selecteClauseResults.add(clauseResult);
	}

	public MonitoringFilterResult toEventFilterResult(DiagnosticsMasks diagnosticsMask, StringTable stringTable) {
		// EventFilterResult eventFilterResult = new EventFilterResult();
		return null;
	}
}
