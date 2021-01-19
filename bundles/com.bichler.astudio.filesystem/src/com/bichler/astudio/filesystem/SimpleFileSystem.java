package com.bichler.astudio.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleFileSystem implements IFileSystem {
	private InputStream inputstream = null;
	private InputStream errorstream = null;
	private String hostName = "";
	private String rootPath = "";
	private String connectionName = "";
	private String javaArg = "";
	private String javaPath = "";
	private String userName = "";
	private int timeout = -1;
	private Process process;
	private String separator = "";

	@Override
	public String[] listFiles(String url) throws IOException {
		File file = new File(url);
		List<String> files = new ArrayList<>();
		File[] fs = file.listFiles();
		if (fs != null) {
			for (File f : fs) {
				if (f.isFile()) {
					files.add(f.getName());
				}
			}
		}
		return files.toArray(new String[files.size()]);
	}

	@Override
	public String[] listDirs(String url) throws IOException {
		File file = new File(url);
		List<String> files = new ArrayList<>();
		if (file.exists()) {
			for (File f : file.listFiles()) {
				if (f.isDirectory()) {
					files.add(f.getName());
				}
			}
		}
		return files.toArray(new String[files.size()]);
	}

	@Override
	public FileInputStream readFile(String url) throws IOException {
		return new FileInputStream(new File(url));
	}

	public OutputStream writeFile(String url) throws IOException {
		File file = new File(url);
		if (!file.exists() && !file.createNewFile())
			return null;
		return new FileOutputStream(new File(url));
	}

	public OutputStream writeFile(String url, boolean append) throws IOException {
		File file = new File(url);
		if (!file.exists() && !file.createNewFile())
			return null;
		return new FileOutputStream(new File(url), append);
	}

	@Override
	public boolean addFile(String url) throws IOException {
		File file = new File(url);
		return file.createNewFile();
	}

	@Override
	public boolean addDir(String url) throws IOException {
		return Files.createDirectories(Paths.get(url)) != null;
	}

	@Override
	public boolean removeFile(String url) throws IOException {
		Files.delete(Paths.get(url));
		return true;
	}

	@Override
	public boolean removeDir(String url) throws IOException {
		Files.delete(Paths.get(url));
		return true;
	}

	@Override
	public boolean isFile(String url) {
		File file = new File(url);
		return file.exists() && file.isFile();
	}

	@Override
	public boolean isDir(String url) {
		File file = new File(url);
		return file.exists() && file.isDirectory();
	}

	@Override
	public String getHostName() {
		return this.hostName;
	}

	@Override
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	@Override
	public void setRootPath(String path) {
		this.rootPath = path;
	}

	@Override
	public String getRootPath() {
		return rootPath;
	}

	@Override
	public boolean renameFile(String oldName, String newName) throws IOException {
		return new File(oldName).renameTo(new File(newName));
	}

	@Override
	public String getUser() {
		return this.userName;
	}

	@Override
	public void setUser(String user) {
		this.userName = user;
	}

	@Override
	public String getPassword() {
		return "";
	}

	@Override
	public void setPassword(String pwd) {
		// not used on local file system
	}

	@Override
	public boolean connect() {
		// do nothing on local file system
		return true;
	}

	@Override
	public boolean isconnected() {
		if (this.rootPath != null) {
			File rootpath = new File(rootPath);
			if (rootpath.exists() && rootpath.isDirectory()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean startProcess(String... args) {
		/**
		 * we need to eliminate first argument
		 */
		String[] arguments = new String[args.length - 1];
		for (int i = 0; i < arguments.length; i++) {
			arguments[i] = args[i + 1];
		}
		ProcessBuilder builder = new ProcessBuilder(arguments);
		builder.directory(new File(args[0]));
		try {
			process = builder.start();
			this.inputstream = process.getInputStream();
			this.errorstream = process.getErrorStream();
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
		}
		return true;
	}

	@Override
	public boolean stopProcess() {
		if (process != null) {
			process.destroy();
			try {
				process.waitFor();
			} catch (InterruptedException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
			}
		}
		return false;
	}

	@Override
	public InputStream getActualInputStream() {
		return this.inputstream;
	}

	@Override
	public InputStream getActualErrorStream() {
		return this.errorstream;
	}

	@Override
	public void setTimeOut(int timeout) {
		this.timeout = timeout;
	}

	@Override
	public int getTimeOut() {
		return this.timeout;
	}

	public boolean connectToProcess(String processName) {
		return false;
	}

	@Override
	public String getConnectionName() {
		return connectionName;
	}

	@Override
	public void setConnectionName(String name) {
		this.connectionName = name;
	}

	@Override
	public void setJavaPath(String path) {
		this.javaPath = path;
	}

	@Override
	public String getJavaPath() {
		return this.javaPath;
	}

	@Override
	public void setJavaArg(String value) {
		this.javaArg = value;
	}

	@Override
	public String getJavaArg() {
		return this.javaArg;
	}

	@Override
	public String getTargetFileSeparator() {
		return separator;
	}

	@Override
	public void setTargetFileSeparator(String separator) {
		this.separator = separator;
	}

	@Override
	public boolean removeDir(String url, boolean subfile) throws IOException {
		if (!subfile) {
			this.removeDir(url);
		} else {
			if (isDir(url)) {
				String[] files = listFiles(url);
				String[] dirs = listDirs(url);
				if (!url.endsWith(separator)) {
					url = url + separator;
				}
				// try to delete all subfiles
				for (String file : files) {
					removeFile(url + file);
				}
				// try to delete all subdirs
				for (String dir : dirs) {
					removeDir(url + dir, subfile);
				}
				removeDir(url);
			}
		}
		return false;
	}

	@Override
	public boolean execCommand(String command) {
		try {
			// Execute command
			String cmd = "cmd /c start cmd.exe";
			Process child = Runtime.getRuntime().exec(cmd);
			// Get output stream to write from it
			OutputStream out = child.getOutputStream();
			out.write(command.getBytes());
			out.flush();
			out.close();
		} catch (IOException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
		}
		return false;
	}

	public void disconnect() {
		// do nothing on local file system
	}

}
