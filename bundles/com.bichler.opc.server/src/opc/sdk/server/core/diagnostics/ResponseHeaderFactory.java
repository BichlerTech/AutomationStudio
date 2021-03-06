package opc.sdk.server.core.diagnostics;

import opc.sdk.core.context.StringTable;
import opc.sdk.core.result.HBResponseHeader;

import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.ServiceResponse;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.core.RequestHeader;
import org.opcfoundation.ua.core.ResponseHeader;

/**
 * DesignPattern: Singleton
 * 
 * Creates a responseheader for a {@link ServiceResponse}.
 * 
 * @author Thomas Z&ouml;chbauer
 */
public class ResponseHeaderFactory {
	/** Singleton instance of the ResponseHeaderFactory */
	private static ResponseHeaderFactory instance = null;

	/**
	 * Returns the Singleton instance of a ResponseHeaderFactory
	 * 
	 * @return ResponseHeaderFactory
	 */
	public static ResponseHeaderFactory getInstance() {
		if (instance == null) {
			instance = new ResponseHeaderFactory();
		}
		return instance;
	}

	/**
	 * Creates a ResponseHeader from a given RequestHeader.
	 * 
	 * @param RequestHeader RequestHeader from a Client. The Information of the
	 *                      RequestHeader is required to create the ResponseHeader.
	 * @return ResponseHeader
	 */
	public ResponseHeader createResponseHeader(RequestHeader requestHeader, StatusCode serviceResult) {
		ResponseHeader responseHeader = new HBResponseHeader();
		responseHeader.setServiceResult(serviceResult);
		responseHeader.setRequestHandle(requestHeader.getRequestHandle());
		responseHeader.setTimestamp(DateTime.currentTime());
		responseHeader.setStringTable(new String[0]);
		return responseHeader;
	}

	/**
	 * Creates a more complex ResponseHeader from a given RequestHeader.
	 * 
	 * @param RequestHeader RequestHeader from a Client. The Information of the
	 *                      RequestHeader is required to create the ResponseHeader.
	 * @param Server        Internal Server instance, which contains all more
	 *                      complex Information.
	 * @return ResponseHeader
	 */
	public ResponseHeader createResponseHeader(RequestHeader requestHeader, StatusCode serviceResult,
			StringTable stringTable) {
		ResponseHeader responseHeader = new ResponseHeader();
		responseHeader.setRequestHandle(requestHeader.getRequestHandle());
		responseHeader.setServiceResult(serviceResult);
		responseHeader.setStringTable(stringTable.toArray());
		responseHeader.setTimestamp(DateTime.currentTime());
		return responseHeader;
	}
}
