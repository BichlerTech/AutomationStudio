package opc.client.application.core;

/**
 * A clients trace configuration to set all Parameters for Logging.
 * 
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 */
public class TraceConfiguration {
	/** Logged Output File Path */
	private String outputFilePath = null;
	/** Delete on Load */
	private String deleteOnLoad = null;
	/** Trace Mask */
	private String traceMask = null;

	/**
	 * Trace Configuration to Configure a Clients Loggin settings.
	 * 
	 * @param outputFilePath
	 * @param deleteOnLoad
	 * @param traceMask
	 */
	public TraceConfiguration(String outputFilePath, String deleteOnLoad, String traceMask) {
		this.outputFilePath = outputFilePath;
		this.deleteOnLoad = deleteOnLoad;
		this.traceMask = traceMask;
	}

	public String getOutputFilePath() {
		return outputFilePath;
	}

	public String getDeleteOnLoad() {
		return deleteOnLoad;
	}

	public String getTraceMask() {
		return traceMask;
	}

	/**
	 * Sets the Output File Path.
	 * 
	 * @param OutputFilePath String path from the OutputFile
	 */
	public void setOutputFilePath(String outputFilePath) {
		this.outputFilePath = outputFilePath;
	}

	/**
	 * Sets the Delete On Load.
	 * 
	 * @param deleteOnLoad
	 */
	public void setDeleteOnLoad(String deleteOnLoad) {
		this.deleteOnLoad = deleteOnLoad;
	}

	/**
	 * Sets the Trace Mask.
	 * 
	 * @param traceMask
	 */
	public void setTraceMask(String traceMask) {
		this.traceMask = traceMask;
	}
}
