package com.bichler.astudio.device.core.transfer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import com.bichler.astudio.device.core.DevCoreActivator;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.utils.internationalization.CustomString;

public class DeviceTargetUtil {

	public static final String FILE_FILES = "files.module";
	public static final String FILE_MODULES = "module.zip";
	public static final String FOLDER_UNZIP_MODULES = "unzip_module";
	public static final String FILE_VERSION = "version.info";
	public static final String FILE_SPLITTER = ":";
	public static final String FOLDER_SSH_UNZIP_MODULE = "/unzip_module";

	/**
	 * Reads zip files.module
	 * 
	 * @param version
	 * @return
	 */
	public static TargetExecutor readTargetFileZip(IFileSystem targetFileSystem, File zipFileFile) {
		TargetExecutor tExecutor = null;
		List<ITarget> targets = new ArrayList<>();
		if (zipFileFile.exists()) {
			// for (File f : zipFileFile.listFiles()) {
			// if (f.getName().compareTo(FILE_MODULES) == 0) {
			tExecutor = new TargetExecutor(zipFileFile);
			tExecutor.setTargetFileSystem(targetFileSystem);
			ZipInputStream zis = null;
			ZipEntry zipEntry = null;
			try {
				ZipFile zipFile = new ZipFile(zipFileFile);
				zis = new ZipInputStream(new FileInputStream(zipFileFile));
				zipEntry = zis.getNextEntry();

				while (zipEntry != null) {
					if (FILE_FILES.equals(zipEntry.getName())) {
						try (InputStreamReader fr = new InputStreamReader(zipFile.getInputStream(zipEntry));
								BufferedReader br = new BufferedReader(fr);) {
							String line = null;
							// read all lines
							while ((line = br.readLine()) != null) {
								targets.add(createTarget(zipFileFile, line));
							}
						} catch (Exception e) {
							Logger.getLogger(DeviceTargetUtil.class.getName()).log(Level.SEVERE, e.getMessage(), e);
						}
						break;
					}
					zipEntry = zis.getNextEntry();
				}
				zis.closeEntry();
				zipFile.close();
				zis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			// exit loop after reading zip file
			// break;
		}
		// }
		// }

		tExecutor.setTargets(targets.toArray(new ITarget[0]));
		return tExecutor;
	}

	/**
	 * Reads zip files.module
	 * 
	 * @param version
	 * @return
	 */
	public static TargetExecutor readTargetFileZip(IFileSystem targetFileSystem, File parent, String version) {
		TargetExecutor tExecutor = null;
		List<ITarget> targets = new ArrayList<>();
		if (parent.exists()) {
			for (File folder : parent.listFiles()) {
				// check version
				if (!version.equals(folder.getName())) {
					continue;
				}

				for (File f : folder.listFiles()) {
					if (f.getName().compareTo(FILE_MODULES) == 0) {
						tExecutor = new TargetExecutor(f);
						tExecutor.setTargetFileSystem(targetFileSystem);
						ZipInputStream zis = null;
						ZipEntry zipEntry = null;
						try {
							ZipFile zipFile = new ZipFile(f);
							zis = new ZipInputStream(new FileInputStream(f));
							zipEntry = zis.getNextEntry();

							while (zipEntry != null) {
								if (FILE_FILES.equals(zipEntry.getName())) {
									try (InputStreamReader fr = new InputStreamReader(zipFile.getInputStream(zipEntry));
											BufferedReader br = new BufferedReader(fr);) {
										String line = null;
										// read all lines
										while ((line = br.readLine()) != null) {
											targets.add(createTarget(folder, line));
										}
									} catch (Exception e) {
										Logger.getLogger(DeviceTargetUtil.class.getName()).log(Level.SEVERE,
												e.getMessage(), e);
									}
									break;
								}
								zipEntry = zis.getNextEntry();
							}
							zis.closeEntry();
							zipFile.close();
							zis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						// exit loop after reading zip file
						break;
					}
				}
			}
		}
		tExecutor.setTargets(targets.toArray(new ITarget[0]));
		return tExecutor;
	}

	/**
	 * Reads files.module
	 * 
	 * @param version
	 * @return
	 */
	public static TargetExecutor readTargetFile(File parent, String version) {
		TargetExecutor tExecutor = null;
		List<ITarget> targets = new ArrayList<>();
		if (parent.exists()) {
			for (File folder : parent.listFiles()) {
				// check version
				if (!version.equals(folder.getName())) {
					continue;
				}

				for (File f : folder.listFiles()) {
					if (f.getName().compareTo(FILE_FILES) == 0) {
						tExecutor = new TargetExecutor(folder);

						try (FileReader fr = new FileReader(f); BufferedReader br = new BufferedReader(fr);) {
							String line = null;
							// read all lines
							while ((line = br.readLine()) != null) {
								targets.add(createTarget(folder, line));
							}
						} catch (Exception e) {
							Logger.getLogger(DeviceTargetUtil.class.getName()).log(Level.SEVERE, e.getMessage(), e);
						}
						break;
					}
				}
				break;
			}
		}
		tExecutor.setTargets(targets.toArray(new ITarget[0]));
		return tExecutor;
	}

	/**
	 * Reads version folders from a directory
	 * 
	 * @param version
	 * @return
	 */
	public static String[] readTargetVersion(File root) {
		List<String> versions = new ArrayList<>();
		if (root.exists()) {
			for (File folder : root.listFiles()) {
				versions.add(folder.getName());
			}
		}
		return versions.toArray(new String[0]);
	}

	private static ITarget createTarget(File root, String line) {
		ITarget target = null;

		String[] execution = line.split(FILE_SPLITTER);
		if (execution.length != 2) {
			throw new IllegalArgumentException(CustomString.getString(DevCoreActivator.getDefault().RESOURCE_BUNDLE,
					"com.bichler.astudio.device.core.error.parsing") + line);
		}

		if (execution[0].startsWith("#DatahubCmd")) {
			target = new CommandTarget(execution[1]);
		} else if (execution[0].startsWith("#zip#")) {
			target = new ModuleFileTarget(line.replaceFirst("#zip#", ""));
		} else {
			target = new FileTarget(line);
		}

		return target;
	}

}
