package opc.sdk.core.application;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration for a discovery server
 * 
 * @author Thomas Z&ouml;chbauer
 *
 */
public class DiscoveryServerConfiguration {
	private List<String> serverNames = null;
	private String discoveryServerCacheFile = "";

	public DiscoveryServerConfiguration() {
		initialize();
	}

	private void initialize() {
		this.setServerNames(new ArrayList<String>());
	}

	public void setServerNames(List<String> serverNames) {
		this.serverNames = serverNames;
	}

	public List<String> getServerNames() {
		return serverNames;
	}

	public void setDiscoveryServerCacheFile(String discoveryServerCacheFile) {
		this.discoveryServerCacheFile = discoveryServerCacheFile;
	}

	public String getDiscoveryServerCacheFile() {
		return discoveryServerCacheFile;
	}
}
