package com.bichler.astudio.update.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.Version;

public class UpdateUtil {

	private static final String PROP_VERSION = "version=";

	public static void checkForUpdate() {
		connectSSH();
	}


	/*
	 * filesystem.setConnectionName("Automation Studio CheckforUpdates");
	 * filesystem.setTimeOut(50000); filesystem.setHostName("ftp68.world4you.com");
	 * filesystem.setUser("ftp5852803"); filesystem.setPassword("pyi6*bw");
	 * filesystem.setTargetFileSeparator("/");
	 * filesystem.setRootPath("/download/as");
	 * 
	 * boolean connected = filesystem.connect();
	 */
	private static boolean connectSSH() {
		String ftpUrl = "ftp://ftp5852803:pyi6*bw@ftp68.world4you.com:21/download/as/version.properties";

		InputStream in = null;
		try {
			URLConnection urlConnection = new URL(ftpUrl).openConnection();
			urlConnection.connect();
			in = urlConnection.getInputStream();
			String version = readFromStream(in);
			
			if (checkVersion(version)) {
				showUpdate();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			MessageDialog.openError(Display.getDefault().getActiveShell(), "Update",
					"Could not connect to update server!");
			e.printStackTrace();
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return true;
	}
	private static String readFromStream(InputStream in) throws IOException {
		BufferedReader read = new BufferedReader(new InputStreamReader(in));

		String i, version = "";
		while ((i = read.readLine()) != null) {
			String[] property = i.split(PROP_VERSION);
			if (property.length > 1) {
				version = property[1];
			}
		}
		return version;
	}
	
	
	private static boolean checkVersion(String version) {
		Version pluginVersion = getProductVersion();

		String[] v = version.split("\\.");
		if (v.length >= 3) {
			if (Integer.parseInt(v[0]) > pluginVersion.getMajor()) {
				return true;
			}

			else if (Integer.parseInt(v[1]) > pluginVersion.getMinor()) {
				return true;
			}

			else if (Integer.parseInt(v[2]) > pluginVersion.getMicro()) {
				return true;
			}
		}

		return false;
	}

	private static Version getProductVersion() {
	    IProduct product = Platform.getProduct();
	    Version version = product.getDefiningBundle().getVersion();
	    // MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Version", version.toString());
	    return version;
	}


	private static void showUpdate() {
		MessageDialog.openInformation(Display.getDefault().getActiveShell(), "Update",
				"Update available for Automation Studio. Please visit https://www.bichler.tech/downloads");
	}
	

}
