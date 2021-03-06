package opc.sdk.core.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LicenseManager {
	public LicenseManager() {
		File dataHUBlicense = new File("/cometintern/licensing/license.dat");
		if (dataHUBlicense.exists()) {
			FileInputStream stream;
			try {
				stream = new FileInputStream(dataHUBlicense);
				Map<String, String> params = initializeTXT(stream);
				if (params != null) {
					// now create serialnumber for all params
				}
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}

	/**
	 * new initialize function to read from textfile HB 18.08.2016
	 * 
	 * @param serverConfigFile
	 * @throws IOException
	 */
	private Map<String, String> initializeTXT(InputStream serverConfigFile) throws IOException {
		BufferedReader breader = null;
		String line = "";
		Map<String, String> parameters = null;
		try {
			breader = new BufferedReader(new InputStreamReader(serverConfigFile));
			parameters = new HashMap<>();
			while ((line = breader.readLine()) != null) {
				if (line.startsWith("#"))
					continue; // we found an comment
				String[] content = line.split("=");
				if (content != null && content.length == 2)
					parameters.put(content[0].trim().toUpperCase(), content[1]);
			}
			breader.close();
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
			try {
				breader.close();
			} catch (IOException e1) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e1);
			}
		}
		return parameters;
	}
}
