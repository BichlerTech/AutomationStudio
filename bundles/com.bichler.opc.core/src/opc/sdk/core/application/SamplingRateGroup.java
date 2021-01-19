package opc.sdk.core.application;

/**
 * Sampling Rate Group
 * 
 * @author Thomas Z&ouml;bauer
 *
 */
public class SamplingRateGroup {
	private String start = "";
	private String increment = "";
	private String count = "";

	public String getStart() {
		return start;
	}

	public String getIncrement() {
		return increment;
	}

	public String getCount() {
		return count;
	}

	public void setStart(String value) {
		this.start = value;
	}

	public void setIncrement(String value) {
		this.increment = value;
	}

	public void setCount(String value) {
		this.count = value;
	}
}
