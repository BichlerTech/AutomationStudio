package com.bichler.astudio.opcua.addressspace.model.binary;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;

//import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.compiler.CompilationProgress;
import org.eclipse.jdt.core.compiler.batch.BatchCompiler;

import com.bichler.astudio.opcua.addressspace.model.Activator;
import com.bichler.astudio.opcua.addressspace.model.InformationModelCompilationProgress;

import opc.sdk.core.application.operation.ICancleOperation;

import com.bichler.astudio.opcua.libs.OPCLibsActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class CompileFactory {
	protected static final String EXTENSION_JAR = "jar";
	protected static final String EXTENSION_CLASS = "class";
	protected static final String EXTENSION_JAVA = "java";
	protected static final String EXTENSION_VALUE = "v";
	protected static final int MAX_BYTE_LIMIT_METHOD = 60000;
	protected static final int MAX_BYTE_LIMIT_VALUE = Integer.MAX_VALUE;

	public CompileFactory() {
	}

	public static boolean compile(ICancleOperation monitor, String destFolder, String jarPath, String jarName, /*
																												 * String
																												 * workspace,
																												 */
			String packageName/* , boolean compile */) throws IOException {
		CompilationProgress progress = new InformationModelCompilationProgress(monitor); // instantiate
		boolean isCompiled = batchCompile(progress, destFolder/* , compile */);
		if (isCompiled) {
			// create jar
			File root = new File(destFolder);
			File[] files = root.listFiles(new FileFilter() {
				@Override
				public boolean accept(File file) {

					String extension = "";

					int i = file.getPath().lastIndexOf('.');
					if (i >= 0) {
						extension = file.getPath().substring(i + 1);
					}

					if (EXTENSION_CLASS.equals(extension)) {
						return true;
					} else if (EXTENSION_VALUE.equals(extension)) {
						return true;
					} else if (EXTENSION_JAVA.equals(extension)) {
						return true;
					}
					return false;
				}
			});
			if (files == null) {
				return false;
			}
			createJar(packageName, jarPath, jarName, files);
			return true;
		} else {
			throw new IOException(
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "monitor.error.compileinfomodel"));
		}
	}

	private static void createJar(String packageName, String jarPath, String jarName, File[] files) {
		// jar location path
		String location = new File(jarPath, jarName + "." + EXTENSION_JAR).getPath();
		// jar file
		File jar = new File(location);
		// delete existing
		if (jar.exists()) {
			jar.delete();
		}
		byte buffer[] = new byte[10240];
		JarOutputStream out = null;
		try {
			// manifest
			Manifest manifest = new Manifest();
			manifest.getMainAttributes().put(Attributes.Name.MANIFEST_VERSION, "1.0");
			// output stream
			FileOutputStream stream = new FileOutputStream(location);
			out = new JarOutputStream(stream, manifest);
			// create package structure
			String[] packages = packageName.split("\\.");
			String pPath = "";
			for (String p : packages) {
				pPath += p + "/";
			}
			// creates package jar entry
			JarEntry folderEntry = new JarEntry(pPath);
			out.putNextEntry(folderEntry);
			out.closeEntry();
			// create file jar entries
			for (int i = 0; i < files.length; i++) {
				if (files[i] == null || !files[i].exists() || files[i].isDirectory()) {
					continue;
				}
				String name = files[i].getName();
				String extension = "";

				int index = name.lastIndexOf('.');
				if (index >= 0) {
					extension = name.substring(index + 1);
				}

				JarEntry jarAdd = null;
				if (EXTENSION_VALUE.equalsIgnoreCase(extension)) {
					jarAdd = new JarEntry(name);
					jarAdd.setTime(files[i].lastModified());
					out.putNextEntry(jarAdd);
				} else if (EXTENSION_CLASS.equalsIgnoreCase(extension) || EXTENSION_JAVA.equalsIgnoreCase(extension)) {// ||
																														// EXTENSION_JAVA.equalsIgnoreCase(extension))
																														// {
																														// create
																														// class
																														// jar
																														// entry
					jarAdd = new JarEntry(pPath + name);
					jarAdd.setTime(files[i].lastModified());
					out.putNextEntry(jarAdd);
				}
				// Write file to archive
				FileInputStream in = null;
				try {
					in = new FileInputStream(files[i]);
					int nRead = -1;
					while ((nRead = in.read(buffer, 0, buffer.length)) > 0) {
						out.write(buffer, 0, nRead);
					}
					out.flush();
					out.closeEntry();
				} catch (IOException e) {
					Logger.getLogger(CompileFactory.class.getName()).log(Level.SEVERE,
							CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "monitor.error.upload"), e);
				} finally {
					if (in != null) {
						try {
							in.close();
						} catch (IOException e) {
							Logger.getLogger(CompileFactory.class.getName()).log(Level.SEVERE, CustomString
									.getString(Activator.getDefault().RESOURCE_BUNDLE, "monitor.error.upload"), e);
						}
					}
				}
			}
		} catch (IOException e) {
			Logger.getLogger(CompileFactory.class.getName()).log(Level.SEVERE,
					CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "monitor.error.upload"), e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					Logger.getLogger(CompileFactory.class.getName()).log(Level.SEVERE,
							CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "monitor.error.upload"), e);
				}
			}
		}
	}

	private static boolean batchCompile(CompilationProgress progress, String destFolder/* , boolean doCompile */) {
		boolean isCompiled = false;
		// stack path
		String jStack = OPCLibsActivator.getLibraryPath(OPCLibsActivator.JAR_STACK);
		// core path
		String jCore = OPCLibsActivator.getLibraryPath(OPCLibsActivator.JAR_CORE);
		// server path
		String jServer = OPCLibsActivator.getLibraryPath(OPCLibsActivator.JAR_SERVER);
		String jComDRV = OPCLibsActivator.getLibraryPath(OPCLibsActivator.JAR_COMDRV);
		String jBaseDriver = OPCLibsActivator.getLibraryPath(OPCLibsActivator.JAR_DRVBASE);
		// if (doCompile) {
		if (isOSWindows()) {
			// compile with windows, because of whitespaces in a path
			isCompiled = BatchCompiler.compile(
					"-source 1.7 -target 1.7 " + "-classpath rt.jar;\"" + jStack + "\";\"" + jCore + "\";\"" + jServer
							+ "\";\"" + jComDRV + "\";\"" + jBaseDriver + "\" \"" + destFolder + "\"",
					new PrintWriter(System.out), new PrintWriter(System.err), progress);
		} else {
			isCompiled = BatchCompiler
					.compile(
							"-source 1.7 -target 1.7 " + "-classpath rt.jar;" + jStack + ";" + jCore + ";" + jServer
									+ " " + destFolder,
							new PrintWriter(System.out), new PrintWriter(System.err), progress);
		}
		// }
		return isCompiled;
	}

	private static boolean isOSWindows() {
		String OS = System.getProperty("os.name");
		return OS.startsWith("Windows");
	}
}
