package opc.sdk.server.service.filter;

import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.ServiceResult;
import org.opcfoundation.ua.builtintypes.StatusCode;

/**
 * 
 * @author Thomas Z&ouml;chbauer
 *
 */
public class ElementResult {
	private ServiceResult status = null;
	private List<ServiceResult> operandResults = null;

	public ElementResult() {
		this.operandResults = new ArrayList<ServiceResult>();
	}

	/**
	 * Initializes the object with a result code.
	 * 
	 * @param status
	 */
	public ElementResult(ServiceResult status) {
		this();
		this.status = status;
		if (status == null) {
			this.status = new ServiceResult(StatusCode.GOOD);
		}
	}

	public void setStatus(ServiceResult status) {
		this.status = status;
	}

	public void addOperandResult(ServiceResult operandResult) {
		this.operandResults.add(operandResult);
	}

	public List<ServiceResult> getOperandResults() {
		return this.operandResults;
	}

	public ServiceResult getStatus() {
		return this.status;
	}
}
