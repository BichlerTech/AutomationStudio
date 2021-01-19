package com.bichler.astudio.opcua.handlers.opcua;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.handlers.HandlerUtil;

public class OPCUAImportAddressspaceClassesHandler extends AbstractHandler {

	public static final String ID = "com.bichler.astudio.opcua.addressspace.importclasses";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		FileDialog dialog = new FileDialog(HandlerUtil.getActiveShell(event), SWT.OPEN);
		dialog.setFilterExtensions(new String[] { "*.jar" });
		String path = dialog.open();

		if (path != null) {
			try {
				importJar(path);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	private void importJar(String path) throws MalformedURLException {
		URL jarURL = new File(path).toURI().toURL();

		// Entweder so
		URLClassLoader classLoader = new URLClassLoader(new URL[] { jarURL });

		URL[] urls = classLoader.getURLs();

//		classLoader.loadClass("");

		// Oder so

		// ClassLoader classLoader = Thread.currentThread()
		// .getContextClassLoader();
		// if (classLoader != null && (classLoader instanceof URLClassLoader)) {
		// URLClassLoader urlClassLoader = (URLClassLoader) classLoader;
		// Method addURL = URLClassLoader.class.getDeclaredMethod("addURL",
		// new Class[] { URL.class });
		// addURL.setAccessible(true);
		// addURL.invoke(urlClassLoader, new Object[] { jarURL });
		// }
		//
		// Class testRunnerClass = classLoader
		// .loadClass("junit.swingui.TestRunner");
		// testRunnerClass.getMethod("main", new Class[] { String[].class })
		// .invoke(null, new Object[] { new String[0] });

	}

}
