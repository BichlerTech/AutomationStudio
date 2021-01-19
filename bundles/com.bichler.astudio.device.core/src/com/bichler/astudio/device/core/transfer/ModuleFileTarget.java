package com.bichler.astudio.device.core.transfer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import com.bichler.astudio.filesystem.DataHubFileSystem;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.filesystem.SimpleFileSystem;

public class ModuleFileTarget extends AbstractTarget {
	protected static final String FILEPOLICY_SEPARATOR = "/";
	private Logger logger = Logger.getLogger(getClass().getName());

	public ModuleFileTarget(String execution) {
		super(execution);
		this.execution = this.execution.replaceFirst("#zip#", "");
	}

	/**
	 * Extracts a zip entry to destination
	 * 
	 * @param filesystem
	 * @param destinationDir
	 * @param zipEntry
	 * @return
	 * @throws IOException
	 */
	private String makeZip(IFileSystem filesystem, String destinationDir, ZipEntry zipEntry) throws IOException {
		String rootPath = getRootPathTarget(filesystem);
		String targetDir = destinationDir;
		if (targetDir.startsWith(FILEPOLICY_SEPARATOR)) {
			targetDir = targetDir.replaceFirst(FILEPOLICY_SEPARATOR, "");
		}
		targetDir = targetDir.replace(FILEPOLICY_SEPARATOR, filesystem.getTargetFileSeparator());

		String path = rootPath + targetDir;
		String sysFileName = zipEntry.getName().replace(FILEPOLICY_SEPARATOR, filesystem.getTargetFileSeparator());
		String targetPath = targetDir + filesystem.getTargetFileSeparator() + sysFileName;
		String fullPath = getDirectoryPathTarget(filesystem, path) + sysFileName;

		boolean isFile = filesystem.isFile(fullPath);

		if (!isFile) {
			// make file structure
			boolean targetIsDirectory = makeDir(filesystem, zipEntry.isDirectory(), rootPath, targetPath);
			if (targetIsDirectory) {
				return null;
			}
		}
		// create file
		addFileTransfer(filesystem, fullPath);

		return fullPath;
	}

	/**
	 * Makes a target file or directory.
	 * 
	 * @param filesystem
	 * @param isPathDirectory
	 * @param fullPath
	 * 
	 * @return true if file is directory, false if file is a file
	 */
	private boolean makeDir(IFileSystem filesystem, boolean isPathDirectory, String rootPath, String targetPath) {
		// target file separator
		try {
			String[] splitPath = targetPath.split(splitSeparator(filesystem));
			if (splitPath == null) {
				throw new IllegalArgumentException();
			}
			String separator = filesystem.getTargetFileSeparator();

			String iPath = "";
			for (int i = 0; i < splitPath.length; i++) {
				// append path
				iPath += splitPath[i];
				// make file (last entry)
				if (!isPathDirectory && i == splitPath.length - 1) {
					// file gets created later on
					return false;
				}
				// make directory
				rMakeDir(filesystem, rootPath + iPath);
				// append directory separator
				iPath += separator;
			}
		} catch (IOException e) {
			// could not create file structure
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * Makes a directory to extract zip.
	 * 
	 * @param filesystem
	 * @param path
	 */
	private void rMakeDir(IFileSystem filesystem, String path) throws IOException {
		if (filesystem.isDir(path)) {
			// directory exists
			return;
		}
		filesystem.addDir(path);
	}

	/**
	 * Windows needs to add another \ because of charset
	 * 
	 * @param filesystem
	 * @return
	 */
	private String splitSeparator(IFileSystem filesystem) {
		String separator = filesystem.getTargetFileSeparator();
		if (separator.equals("\\")) {
			separator = separator + filesystem.getTargetFileSeparator();
		}
		return separator;
	}

	@Override
	public boolean execute(TargetExecutor executor, IFileSystem targetFileSystem) throws IOException {
		String[] filetransfer = this.execution.split(DeviceTargetUtil.FILE_SPLITTER);
		if (filetransfer.length != 2) {
			logger.log(Level.SEVERE, getClass().getName() + " Source and target destination missing");
			return true;
		}

		// zip target file
		if (targetFileSystem instanceof SimpleFileSystem) {
			String targetPath = DeviceTargetUtil.FILE_MODULES;
			String path = getRootPathTarget(targetFileSystem) + targetPath;
			String zipFilter = filetransfer[0];
			executeSimpleFilesystem(targetFileSystem, path, zipFilter, filetransfer[1]);
		} else if (targetFileSystem instanceof DataHubFileSystem) {
			String targetPath = DeviceTargetUtil.FOLDER_UNZIP_MODULES;
			String path = FILEPOLICY_SEPARATOR + /*getRootPathTarget(targetFileSystem) + */ targetPath;
			String zipFilter = filetransfer[0];
			executeSshFilesystem(targetFileSystem, path, zipFilter, filetransfer[1]);
		}

		return true;
	}

	private boolean executeSshFilesystem(IFileSystem filesystem, String path, String zipFilter, String zipTarget)
			throws IOException {

		moveDir(filesystem, path, zipFilter, zipTarget);

		return true;
	}

	/**
	 * Extracts a zip entry to destination
	 * 
	 * @param filesystem
	 * @param destinationDir path of unzipped directory on target
	 * @param zipEntry
	 * @return
	 * @throws IOException
	 */
	private boolean moveDir(IFileSystem filesystem, String destinationDir, String zipFilter, String zipTarget)
			throws IOException {

		String rootPath = FILEPOLICY_SEPARATOR; // getRootPathTarget(filesystem);
		String unzipDir = destinationDir;
		if (unzipDir.startsWith(FILEPOLICY_SEPARATOR)) {
			unzipDir = unzipDir.replaceFirst(FILEPOLICY_SEPARATOR, "");
		}

		unzipDir = unzipDir + FILEPOLICY_SEPARATOR + zipFilter;
		unzipDir = unzipDir.replace(FILEPOLICY_SEPARATOR, filesystem.getTargetFileSeparator());

		unzipDir = rootPath + unzipDir;

		String unzipDirTarget = zipTarget;// +FILEPOLICY_SEPARATOR+zipFilter;
		unzipDirTarget = unzipDirTarget.replace(FILEPOLICY_SEPARATOR, filesystem.getTargetFileSeparator());
		String unzipDirTargetFolder = unzipDirTarget + FILEPOLICY_SEPARATOR + zipFilter;
		unzipDirTargetFolder = unzipDirTargetFolder.replace(FILEPOLICY_SEPARATOR, filesystem.getTargetFileSeparator());

		makeDir(filesystem, true, rootPath, unzipDirTarget);

		filesystem.execCommand("rm -r " + unzipDirTargetFolder);

		filesystem.execCommand("mv " + unzipDir + " " + unzipDirTarget);
		logger.log(Level.SEVERE, getClass().getName() + " mv -f " + unzipDir + " " + unzipDirTarget);
		return true;
	}

	private boolean executeSimpleFilesystem(IFileSystem targetFileSystem, String path, String zipFilter,
			String zipTarget) throws IOException {
		ZipInputStream zis = null;
		ZipEntry zipEntry = null;

		try {
			// open zip
			ZipFile zipFile = new ZipFile(new File(path));
			zis = new ZipInputStream(targetFileSystem.readFile(path));
			// read zip entries
			zipEntry = zis.getNextEntry();
			while (zipEntry != null) {
				// filter
				if (!zipEntry.getName().startsWith(zipFilter)) {
					zipEntry = zis.getNextEntry();
					continue;
				}
				OutputStream fos = null;
				try {
					// extract zip entry
					String targetFile = makeZip(targetFileSystem, zipTarget, zipEntry);
					// continue if is directory
					if (targetFile == null) {
						zipEntry = zis.getNextEntry();
						continue;
					}
					// write file content
					fos = targetFileSystem.writeFile(targetFile);
					int len;
					byte[] buffer = new byte[1024];
					while ((len = zis.read(buffer, 0, buffer.length)) > 0) {
						fos.write(buffer, 0, len);
					}
					fos.flush();
					zipEntry = zis.getNextEntry();
				} finally {
					if (fos != null) {
						fos.close();
						fos = null;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (zis != null) {
				zis.closeEntry();
				zis.close();
			}
		}
		return true;
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
		String rootPathTarget = getDirectoryPathTarget(targetFileSystem, targetFileSystem.getRootPath());
		return rootPathTarget;
	}

	private String getDirectoryPathTarget(IFileSystem targetFileSystem, String path) {
		String directoryPath = path;
		// check for separator
		if (!directoryPath.endsWith(targetFileSystem.getTargetFileSeparator())) {
			directoryPath = directoryPath + targetFileSystem.getTargetFileSeparator();
		}
		return directoryPath;
	}

	private String checkOrCreateFolder(IFileSystem targetFileSystem, String targetPath, String separator)
			throws IOException {
		// target root
		String path = getRootPathTarget(targetFileSystem);
		String[] canonical = targetPath.split(separator);

		for (int i = 0; i < canonical.length; i++) {
			String part = canonical[i];
			// append path
			if (!part.isEmpty()) {
				if (!part.endsWith(targetFileSystem.getTargetFileSeparator())) {
					part = part + targetFileSystem.getTargetFileSeparator();
				}

				path = path + part;
				addDirectoryTransfer(targetFileSystem, path);
			}
			// append target separator
			else {
				if (!path.endsWith(targetFileSystem.getTargetFileSeparator())) {
					path += targetFileSystem.getTargetFileSeparator();
				}
			}
		}
		return path;
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
		return checkOrCreateFolder(targetFileSystem, targetPath, FILEPOLICY_SEPARATOR);
	}

}
