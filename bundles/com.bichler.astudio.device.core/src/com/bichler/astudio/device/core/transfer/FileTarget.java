package com.bichler.astudio.device.core.transfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bichler.astudio.filesystem.DataHubFileSystem;
import com.bichler.astudio.filesystem.IFileSystem;

public class FileTarget extends AbstractTarget {
	protected static final String FILEPOLICY_SEPARATOR = "/";
	private Logger logger = Logger.getLogger(getClass().getName());

	public FileTarget(String execution) {
		super(execution);
	}

	@Override
	public boolean execute(TargetExecutor executor, IFileSystem targetFileSystem) throws IOException {
		String[] filetransfer = this.execution.split(DeviceTargetUtil.FILE_SPLITTER);
		if (filetransfer.length != 2) {
			logger.log(Level.SEVERE, getClass().getName() + " Source and target destination missing");
			return true;
		}
		File root = executor.getRoot();
		String sourcePath = filetransfer[0];

		File copy = null;
		for (File file : root.listFiles()) {
			if (sourcePath.equals(file.getName())) {
				copy = file;
				break;
			}
		}

		if (copy == null) {
			// error in the files.module configuration (path of key entry)
			throw new IllegalArgumentException("Source file missing");
		}

		try {
			String targetPath = checkOrCreateFolder(targetFileSystem, filetransfer[1]);
			copyFile(targetFileSystem, copy, targetPath);
		} catch (IOException e) {
			logger.log(Level.SEVERE, getClass().getName() + " Error transfering files " + e.getMessage());
			throw e;
		}
		return true;
	}

	private static int MAX_TRY = 10;
	
	private void copyFile(IFileSystem targetFileSystem, File file, String targetPath) throws IOException {
		if(!targetPath.endsWith(targetFileSystem.getTargetFileSeparator())) {
			targetPath = targetPath+targetFileSystem.getTargetFileSeparator();
		}
		
		String filePath = targetPath + file.getName();
		if (file.isDirectory()) {
			addDirectoryTransfer(targetFileSystem, filePath);
			// find sub folders and files
			for (File subFiles : file.listFiles()) {
				copyFile(targetFileSystem, subFiles, filePath);
			}
		} else {
			// add file
			boolean finished = false;
			int countTry = 0;
			
			while (!finished || countTry < MAX_TRY) {
				addFileTransfer(targetFileSystem, filePath);

				logger.log(Level.INFO, "Start Upload file - Src: {0} Target: {1}",
						new String[] { file.getAbsolutePath(), filePath });
				// copy all files
				
				/*try (OutputStream out = targetFileSystem.writeFile(filePath);
						InputStream in = new FileInputStream(file);) {

					int read = -1;
					byte[] buffer = new byte[1024];
					while ((read = in.read(buffer, 0, buffer.length)) != -1) {
						out.write(buffer, 0, read);
					}
					out.flush();
					finished = true;
				} catch (IOException e) {
					//throw e;
					targetFileSystem.disconnect();
					targetFileSystem.connect();
				}*/
				
				InputStream in = null;
				OutputStream out = null;
				try {
					in = new FileInputStream(file);
					out = targetFileSystem.writeFile(filePath); 
					
					int read = -1;
					byte[] buffer = new byte[1024];
					while ((read = in.read(buffer, 0, buffer.length)) != -1) {
						out.write(buffer, 0, read);
						if(countTry > 0) {
							try {
								Thread.sleep(25);
							} catch (InterruptedException ie) {
								ie.printStackTrace();
							}
						}
					}
					
					try {
						Thread.sleep(10);
					} catch (InterruptedException ie) {
						ie.printStackTrace();
					}
					
					out.flush();
					finished = true;
					countTry = MAX_TRY;
				}catch(IOException e) {
					if(countTry >= MAX_TRY) {
						throw e;	
					}
					// increase counter
					countTry++;
					// reconnect
					targetFileSystem.disconnect();
					try {
						Thread.sleep(50);
					} catch (InterruptedException ie) {
						ie.printStackTrace();
					}
					targetFileSystem.connect();					
				} finally {
					if(in != null) {
						try {
							in.close();
						}catch(IOException e) {
							e.printStackTrace();
						}
					}
					if(out != null) {
						try {
							out.close();
						}catch(IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			/*
			 * logger.log(Level.INFO, "Start Upload file - Src: {0} Target: {1}", new
			 * String[] { file.getAbsolutePath(), targetPath });
			 */
		}
	}

	private void addDirectoryTransfer(IFileSystem targetFileSystem, String targetDirectoryPath) throws IOException {
		if (!targetFileSystem.isDir(targetDirectoryPath)) {
			targetFileSystem.addDir(targetDirectoryPath);
		}
	}

	private void addFileTransfer(IFileSystem targetFileSystem, String targetFilePath) throws IOException {
		// remove file if exists
		if (targetFileSystem.isFile(targetFilePath)) {
			targetFileSystem.removeFile(targetFilePath);
		}

		if (!targetFileSystem.isFile(targetFilePath)) {
			targetFileSystem.addFile(targetFilePath);
		}
	}

	private String getRootPathTarget(IFileSystem targetFileSystem) {
		String rootPathTarget = null;
		// check for separator
		if (targetFileSystem.getRootPath().endsWith(targetFileSystem.getTargetFileSeparator())) {
			rootPathTarget = targetFileSystem.getRootPath();
		} else {
			rootPathTarget = targetFileSystem.getRootPath() + targetFileSystem.getTargetFileSeparator();
		}
		return rootPathTarget;
	}

	/**
	 * Create folder structure for file.txt
	 * 
	 * @param targetFileSystem
	 * @param mapping
	 * @return
	 * @throws IOException
	 */
	private String checkOrCreateFolder(IFileSystem targetFileSystem, String targetPath) throws IOException {
		// target root
		String path = getRootPathTarget(targetFileSystem);
		String[] canonical = targetPath.split(FILEPOLICY_SEPARATOR);

		for (int i = 0; i < canonical.length; i++) {
			String part = canonical[i];
			// append path
			if (!part.isEmpty()) {
				if(!part.endsWith(targetFileSystem.getTargetFileSeparator())) {
					part = part + targetFileSystem.getTargetFileSeparator();
				}
				
				path = path + part;
				addDirectoryTransfer(targetFileSystem, path);
			}
			// append target separator
			else {
				if(!path.endsWith(targetFileSystem.getTargetFileSeparator())) {
					path += targetFileSystem.getTargetFileSeparator();
				}
			}
		}
		return path;
	}

}
