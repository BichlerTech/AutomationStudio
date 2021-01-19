package opc.sdk.server.core.config;

import java.io.IOException;
import java.io.InputStream;

import opc.sdk.core.application.ApplicationConfiguration;

public class ServerConfigurationParser {
	public ServerConfigurationParser() {
	}

	public ApplicationConfiguration parseConfiguration(InputStream inputStream) throws IOException {
		return new ApplicationConfiguration(inputStream);
	}
}
