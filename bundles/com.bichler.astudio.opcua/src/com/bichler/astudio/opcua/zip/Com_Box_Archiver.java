package com.bichler.astudio.opcua.zip;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.bichler.astudio.filesystem.IFileSystem;

public class Com_Box_Archiver {

	private IFileSystem filesystem = null;

	public Com_Box_Archiver() {
	}

	public IFileSystem getFilesystem() {
		return filesystem;
	}

	public void setFilesystem(IFileSystem filesystem) {
		this.filesystem = filesystem;
	}

	public void zipDir(String dirName, String nameZipFile) throws IOException {
		ZipOutputStream zip = null;
		FileOutputStream fW = null;
		fW = new FileOutputStream(nameZipFile);
		zip = new ZipOutputStream(fW);
		addFolderToZip("", dirName, zip);
		zip.close();
		fW.close();
	}

	private String lastElementofPath(String path) {
		int index = path.lastIndexOf(filesystem.getTargetFileSeparator());
		return path.substring(index + 1);
	}

	private void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) throws IOException {

		// File folder = new File(srcFolder);
		if (filesystem.listDirs(srcFolder).length == 0 && filesystem.listFiles(srcFolder).length == 0) {
			addFileToZip(path, srcFolder, zip, true);
		} else {
			for (String fileName : filesystem.listFiles(srcFolder)) {
				if (path.equals("")) {
					addFileToZip(lastElementofPath(srcFolder), srcFolder + "/" + fileName, zip, false);
				} else {
					addFileToZip(path + "/" + lastElementofPath(srcFolder), srcFolder + "/" + fileName, zip, false);
				}
			}
			for (String fileName : filesystem.listDirs(srcFolder)) {
				if (path.equals("")) {
					addFileToZip(lastElementofPath(srcFolder), srcFolder + "/" + fileName, zip, false);
				} else {
					addFileToZip(path + "/" + lastElementofPath(srcFolder), srcFolder + "/" + fileName, zip, false);
				}
			}
		}
	}

	private void addFileToZip(String path, String srcFile, ZipOutputStream zip, boolean flag) throws IOException {

		// File folder = new File(srcFile);
		if (flag) {
			zip.putNextEntry(new ZipEntry(path + "/" + lastElementofPath(srcFile) + "/"));
		} else {
			if (filesystem.isDir(srcFile)) {
				addFolderToZip(path, srcFile, zip);
			} else {
				byte[] buf = new byte[1024];
				int len;
				FileInputStream in = new FileInputStream(srcFile);
				zip.putNextEntry(new ZipEntry(path + "/" + lastElementofPath(srcFile)));
				while ((len = in.read(buf)) > 0) {
					zip.write(buf, 0, len);
				}
			}
		}
	}

	/**
	 * Unzip it
	 * 
	 * @param zipFile input zip file
	 * @param output  zip file output folder
	 */
	public void unZipArchive(String zipFile, String outputFolder) throws IOException {

		byte[] buffer = new byte[1024];

		try {

			// create output directory is not exists
			// File folder = new File("");
			if (!filesystem.isDir(outputFolder)) {
				filesystem.addDir(outputFolder);
			}

			// get the zip file content
			ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
			// get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();

			while (ze != null) {

				String fileName = ze.getName();

				OutputStream out = filesystem.writeFile(outputFolder + filesystem.getTargetFileSeparator() + fileName);

				if (ze.isDirectory()) {
					// now we create a new directory
					filesystem.addDir(outputFolder + filesystem.getTargetFileSeparator() + fileName);
				} else {
					filesystem.addFile(outputFolder + filesystem.getTargetFileSeparator() + fileName);
					OutputStream fos = filesystem
							.writeFile(outputFolder + filesystem.getTargetFileSeparator() + fileName);

					int len;
					while ((len = zis.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}

					fos.close();
				}
				ze = zis.getNextEntry();
			}

			zis.closeEntry();
			zis.close();

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
