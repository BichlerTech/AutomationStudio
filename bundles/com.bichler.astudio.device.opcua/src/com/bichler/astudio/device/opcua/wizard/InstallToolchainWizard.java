package com.bichler.astudio.device.opcua.wizard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;

import com.bichler.astudio.device.opcua.DeviceActivator;
import com.bichler.astudio.device.opcua.wizard.page.InstallToolchainWizardPage;

public class InstallToolchainWizard extends Wizard {

	private InstallToolchainWizardPage pageOne;

	public InstallToolchainWizard() {
		setWindowTitle("Install C-Toolchain");
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		this.pageOne = new InstallToolchainWizardPage();
		addPage(this.pageOne);
	}

	@Override
	public boolean performFinish() {
		final String path = this.pageOne.getPath();

		// install zip
		try {
			getContainer().run(true, false, new IRunnableWithProgress() {
				
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					File zipFile = new File(path);
					ZipFile zf = null;
					try {
						zf = new ZipFile(zipFile);
						int size = zf.size();
	
						monitor.beginTask("Copy files"+"...", size);
						copyToolchainFile(monitor, path);
					} catch (ZipException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}finally {
						if(zf != null) {
							try {
								zf.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
					monitor.done();
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return true;
	}

	
	private void copyToolchainFile(IProgressMonitor monitor, String zipPath) {
		File destToolchain = DeviceActivator.getDefault().getToolchain();

		File zipFile = new File(zipPath);
		ZipInputStream zis = null;
		try {
			zis = new ZipInputStream(new FileInputStream(zipFile));
			ZipEntry zipEntry = zis.getNextEntry();
			while (zipEntry != null) {
				monitor.worked(1);
				File newFile = newFile(destToolchain, zipEntry);
				copyZip(newFile, zis, zipEntry);
				zipEntry = zis.getNextEntry();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (zis != null) {
				try {
					zis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static void copyZip(File newFile, ZipInputStream zis, ZipEntry zipEntry) throws IOException {
		if (zipEntry.isDirectory()) {
			if (!newFile.isDirectory() && !newFile.mkdirs()) {
				throw new IOException("Failed to create directory " + newFile);
			}
		} else {
			// fix for Windows-created archives
			File parent = newFile.getParentFile();
			if (!parent.isDirectory() && !parent.mkdirs()) {
				throw new IOException("Failed to create directory " + parent);
			}

			// write file content
			FileOutputStream fos = new FileOutputStream(newFile);
			int len;
			byte[] buffer = new byte[1024];
			while ((len = zis.read(buffer)) > 0) {
				fos.write(buffer, 0, len);
			}
			fos.close();
		}
	}

	private static File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
		File destFile = new File(destinationDir, zipEntry.getName());

		String destDirPath = destinationDir.getCanonicalPath();
		String destFilePath = destFile.getCanonicalPath();

		if (!destFilePath.startsWith(destDirPath + File.separator)) {
			throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
		}

		return destFile;
	}
}
