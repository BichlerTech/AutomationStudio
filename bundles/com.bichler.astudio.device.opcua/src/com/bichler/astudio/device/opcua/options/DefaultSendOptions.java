package com.bichler.astudio.device.opcua.options;

import org.eclipse.core.runtime.Path;

public class DefaultSendOptions extends SendOptions {

	public DefaultSendOptions() {
		super();
	}

	@Override
	public OS_Startup getOs() {
		if (isHBDatahubUpload()) {
			return OS_Startup.LINUX;
		}
		return super.getOs();
	}

	@Override
	public String getJavaPath() {
		String separator = getFilesystem().getTargetFileSeparator();
		if (isHBDatahubUpload()) {
			return separator + "hbin" + separator + "javaOPC";
		}

		return super.getJavaPath() + "\\java";
	}

	@Override
	public String buildStartup(String path, String servername) {
		StringBuffer buffer = new StringBuffer();
		String separator = getFilesystem().getTargetFileSeparator();

		if (isHBDatahubUpload()) {
			buffer.append("#!/bin/sh\n");
			buffer.append("cd /hbs/comet/opc_ua_server/servers/HBSServer" + "\n");
			buffer.append(".."
					+separator
					+ ".."
					+ separator
					+ "runtime"
					+ separator
					+ "jre_1.8.0"
					+ separator
					+ "bin"
					+ separator
					+ "javaOPC"
					+ " -Djava.library.path=\".."
					+ separator
					+ ".."
					+ separator
					+ "runtime"
					+ separator
					+ "ext;%PATH%\" -Xms32m -Xmx256m -XX:MaxPermSize=192m -jar .."
					+ separator + ".." + separator + "runtime"
					+ separator + "OPC_Server.jar\n");

		} else {
			// buffer.append("#!/bin/sh\n");
			buffer.append("cd " + path
					+ separator + "servers"
					+ getFilesystem().getTargetFileSeparator() + servername
					+ "\n");
			buffer.append("\""
					+ getJavaPath()
					+ "\""
					+ " -Djava.library.path=\"../../runtime/ext\" -Xms32m -Xmx256m -XX:MaxPermSize=192m -jar ../../runtime/OPC_Server.jar\n");
		}
		return buffer.toString();
	}

	@Override
	public String nameScriptFile(String startupScriptPath) {
		String extension = new Path(startupScriptPath).getFileExtension();
		int extIndex = startupScriptPath.lastIndexOf("." + extension);
		String startupScript = startupScriptPath.substring(0, extIndex);

		if (isHBDatahubUpload()) {
			return startupScript + ".sh";
		}

		OS_Startup operatingSystem = getOs();
		switch (operatingSystem) {
		case WINDOWS:
			return startupScript + ".bat";
		case LINUX:
			return startupScript + ".sh";
		case MAC:
			return startupScript + ".sh";
		case UNDEFINED:
		default:
			return startupScript + ".sh";
		}
	}
}
