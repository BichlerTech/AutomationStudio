package com.bichler.astudio.language.handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.FrameworkUtil;

import com.bichler.astudio.utils.activator.InternationalActivator;
import com.bichler.astudio.utils.ui.UtilsUIActivator;
import com.bichler.astudio.utils.ui.dialogs.PickWorkspaceDialog;

public class LanguageUtil {
	public static void initializeLanguage() {
		// fetch locale
		Locale locale = PickWorkspaceDialog.getProductLanguage();
		do {
			if (locale == null) {
				locale = LanguageUtil.openLanguageDialog();
			}
		} while (locale == null);
		LanguageUtil.setProductLanguage(locale);
	}

	public static Locale openLanguageDialog() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench == null) {
			throw new NullPointerException();
		}
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		if (window == null) {
			throw new NullPointerException();
		}
		Shell shell = window.getShell();
		if (shell == null) {
			throw new NullPointerException();
		}
		SelectLanguageDialog dialog = new SelectLanguageDialog(shell);
		dialog.open();
		return dialog.getApplicationLocale();
	}

	public static void setProductLanguage(Locale language) {
		Location location = Platform.getInstallLocation();
		URL url = location.getURL();
		if (url != null) {
			URL url2 = null;
			try {
				url2 = FileLocator.toFileURL(url);
			} catch (IOException e) {
				Logger.getLogger(LanguageUtil.class.getName()).log(Level.SEVERE, e.getMessage());
			}
			if (url2 != null) {
				File installFile = new File(url2.getFile());
				FilenameFilter filter = new FilenameFilter() {
					private final String EXTENSION = "ini";

					@Override
					public boolean accept(File parent, String name) {
						String ext = new Path(name).getFileExtension();
						if (EXTENSION.equals(ext)) {
							return true;
						}
						return false;
					}
				};
				File[] children = installFile.listFiles(filter);
				if (children != null && children.length > 0) {
					File asIni = children[0];
					String bufferedString = null;
					BufferedReader reader = null;
					try {
						FileInputStream fis = new FileInputStream(asIni);
						reader = new BufferedReader(new InputStreamReader(fis));
						String line = null;
						StringBuilder buffer = new StringBuilder();
						String nlString = null;
						boolean nlfound = false;
						boolean vmargsfound = false;
						while ((line = reader.readLine()) != null) {
							if ("-nl".equals(line)) {
								// nl
								nlfound = true;
								PickWorkspaceDialog.appendLine(buffer, "%nativelanguage%");
								nlString = line + "\n";
								// skip language
								reader.readLine();
								line = language.getLanguage();
								nlString += line;
								continue;
							}
							if ("-vmargs".equals(line)) {
								vmargsfound = true;
								if (!nlfound)
									PickWorkspaceDialog.appendLine(buffer, "%nativelanguage%");
							}
							PickWorkspaceDialog.appendLine(buffer, line);
						}
						if (nlString == null) {
							nlString = "-nl\n" + language.getLanguage();
						}
						bufferedString = buffer.toString();
						if (nlfound || vmargsfound)
							bufferedString = bufferedString.replace("%nativelanguage%", nlString);
						else {
							bufferedString += nlString;
						}
					} catch (IOException e) {
						Logger.getLogger(LanguageUtil.class.getName()).log(Level.SEVERE, e.getMessage());
					} finally {
						if (reader != null) {
							try {
								reader.close();
							} catch (IOException e) {
								Logger.getLogger(LanguageUtil.class.getName()).log(Level.SEVERE, e.getMessage());
							}
						}
					}
					BufferedWriter writer = null;
					try {
						FileOutputStream fos = new FileOutputStream(asIni);
						writer = new BufferedWriter(new OutputStreamWriter(fos));
						writer.write(bufferedString);
						writer.flush();
					} catch (IOException e) {
						Logger.getLogger(LanguageUtil.class.getName()).log(Level.SEVERE, e.getMessage());
					} finally {
						if (writer != null) {
							try {
								writer.close();
							} catch (IOException e) {
								Logger.getLogger(LanguageUtil.class.getName()).log(Level.SEVERE, e.getMessage());
							}
						}
					}
				}
			}
		}
		BundleContext ctx = FrameworkUtil.getBundle(UtilsUIActivator.class).getBundleContext();
		Bundle[] bundles = ctx.getBundles();
		for (Bundle bundle : bundles) {
			BundleContext context = bundle.getBundleContext();
			if (context == null) {
				continue;
			}
			Bundle activator = context.getBundle();
			if (activator == null) {
				continue;
			}
			String activatorClass = bundle.getHeaders().get(Constants.BUNDLE_ACTIVATOR);
			if (activatorClass == null) {
				continue;
			}
			try {
				Class<?> plugin = activator.loadClass(activatorClass);
				Method method = plugin.getMethod("getDefault");
				Object activatorInstance = method.invoke(null);
				if (activatorInstance instanceof InternationalActivator) {
					((InternationalActivator) activatorInstance).setLanguageResourceBundle(language);
				}
			} catch (NoSuchMethodException e) {
				// do not log exception
			} catch (Exception e) {
				Logger.getLogger(LanguageUtil.class.getName()).log(Level.SEVERE, e.getMessage());
			}
		}
	}
}
