package com.bichler.opcua.statemachine.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static Activator activator;
	private static BundleContext context;
	private static String FOLDER_TESTCASEUML = "testcaseUML";
	private static String FOLDER_TRANSFORMATION = "transformation";
	private static String FOLDER_REVERSE = "reverse";
	
	static BundleContext getContext() {
		return context;
	}
	
	public static Activator getDefault() {
		return Activator.activator;
	}

	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		Activator.activator = this;
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
		Activator.activator = null;
	}
	
	public File getFolderTestcaseTransformation() {
		return getFile(getContext().getBundle(), new Path(FOLDER_TESTCASEUML).append(FOLDER_TRANSFORMATION));
	}
	
	public File getFolderTestcaseReverse() {
		return getFile(getContext().getBundle(), new Path(FOLDER_TESTCASEUML).append(FOLDER_REVERSE));
	}

	public File getFile(Bundle bundle, IPath path) {
		URL url = FileLocator.find(bundle, path, null);
		if (url != null) {
			try {
				URL url2 = FileLocator.toFileURL(url);
				// remove '/' beginning of path if possible
				return new File(url2.getFile());
			} catch (IOException e) {
				Logger.getLogger(Activator.class.getName()).log(Level.SEVERE, e.getMessage());
			}
		}
		return null;
	}
}
