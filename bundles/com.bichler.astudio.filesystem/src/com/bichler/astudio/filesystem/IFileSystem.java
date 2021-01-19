package com.bichler.astudio.filesystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface IFileSystem
{
  /**
   * Executes a external process on a local or remote machine
   * 
   * @param args
   *          1. working directory where the process should run 2. additional
   *          process specific parameters
   * @return true if start was successfully otherwise false.
   */
  boolean startProcess(String... args);

  InputStream getActualInputStream();

  InputStream getActualErrorStream();

  boolean stopProcess();

  /**
   * set the path to the java runtime directory
   * 
   * @param path
   *          Directory to java runtime system.
   */
  void setJavaPath(String path);

  /**
   * get the path to the java runtime directory
   * 
   * @return Directory to java runtime system.
   */
  String getJavaPath();

  /**
   * set the path to the root directory
   * 
   * @param path
   *          Root directory of a servers.
   */
  void setRootPath(String path);

  /**
   * get the path to the root directory
   * 
   * @return Root directory of a servers.
   */
  String getRootPath();

  String getConnectionName();

  void setConnectionName(String name);

  /**
   * get host name where the comet studio is located.
   * 
   * @return host name.
   */
  String getHostName();

  String getUser();

  void setUser(String user);

  String getPassword();

  void setPassword(String pwd);

  /**
   * set host name where the comet studio is located.
   * 
   * @param host
   *          name.
   */
  void setHostName(String hostName);

  /**
   * get a list with all files for a given directory.
   * 
   * @param url
   *          Directory where to look for all files.
   * 
   * @return List with all files and directories.
   * @throws FileNotFoundException
   * @throws IOException
   */
  String[] listFiles(String url) throws IOException;

  /**
   * get a list with all directories for a given directory.
   * 
   * @param url
   *          Directory where to look for all files.
   * 
   * @return List with all files and directories.
   * @throws FileNotFoundException
   * @throws IOException
   */
  String[] listDirs(String url) throws IOException;

  /**
   * Check if the given url points to a file.
   * 
   * @param url
   * @return
   * @throws FileNotFoundException
   * @throws IOException
   */
  boolean isFile(String url);

  /**
   * Check if the given url points to a directory.
   * 
   * @param url
   * @return
   * @throws FileNotFoundException
   * @throws IOException
   */
  boolean isDir(String url);

  /**
   * opens a requested file and returns a input stream for that.
   * 
   * @param url
   * @return
   * @throws FileNotFoundException
   * @throws IOException
   */
  InputStream readFile(String url) throws IOException;

  OutputStream writeFile(String url) throws IOException;

  OutputStream writeFile(String url, boolean append) throws IOException;
  
  boolean addFile(String url) throws IOException;

  boolean addDir(String url) throws IOException;

  boolean removeFile(String url) throws IOException;

  boolean removeDir(String url) throws IOException;

  /**
   * added 04.11.2013 HB deletes also each subfiles and subfolders
   * 
   * @param url
   * @param subfile
   * @return
   * @throws FileNotFoundException
   * @throws IOException
   */
  boolean removeDir(String url, boolean subfile) throws IOException;

  boolean renameFile(String oldName, String newName) throws IOException;

  boolean connect();

  boolean isconnected();

  void setTimeOut(int timeout);

  int getTimeOut();

  boolean connectToProcess(String processName);

  void setJavaArg(String value);

  String getJavaArg();

  void setTargetFileSeparator(String separator);

  String getTargetFileSeparator();

  boolean execCommand(String command);

  void disconnect();
}
