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
public class ContentFilterValidationResult {
	private ServiceResult status = null;
	private List<ElementResult> elementResults = null;

	public ContentFilterValidationResult(ServiceResult status) {
		this.status = status;
		this.elementResults = new ArrayList<ElementResult>();
		if (status == null) {
			this.status = new ServiceResult(StatusCode.GOOD);
		}
	}

	public List<ElementResult> getElementResults() {
		return this.elementResults;
	}

	public ServiceResult getStatus() {
		return this.status;
	}

	public void setStatus(ServiceResult status) {
		this.status = status;
	}
}
