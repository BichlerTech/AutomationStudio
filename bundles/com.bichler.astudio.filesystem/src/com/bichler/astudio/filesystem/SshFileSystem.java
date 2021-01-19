package com.bichler.astudio.filesystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

/**
 * 
 * @author hannes
 * 
 */
public abstract class SshFileSystem implements IFileSystem
{
  private InputStream inputstream = null;
  private InputStream errorstream = null;
  private JSch ssh = new JSch();
  Session session = null;
  ChannelSftp chsftp = null;
  ChannelExec chexec = null;
  private String rootPath = "";
  private String connectionName = "";
  private String hostName = "";
  private String user = "";
  private String passwd = "";
  private int port = 22;
  private int timeout = 10000;
  private int processId = -1;
  private String javaPath = "";
  private String javaArg = "";
  private String separator;

  @Override
  public boolean connect()
  {
    try
    {
      Properties config = new Properties();
      config.put("StrictHostKeyChecking", "no");
      config.put("compression.s2c", "zlib,none");
      config.put("compression.c2s", "zlib,none");
      session = ssh.getSession(this.user, this.hostName);
      session.setConfig(config);
      session.setPort(port);
      session.setPassword(passwd);
      // set timeout for connect to server
      session.setTimeout(timeout);
      session.connect();
      chsftp = (ChannelSftp) session.openChannel("sftp");
      chsftp.connect();
    }
    catch (JSchException e1)
    {
      Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Verbindung zu dataHUB nicht möglich, timeout!");
      return false;
    }
    return true;
  }

  @Override
  public String[] listFiles(String url) throws IOException
  {
    if (this.chsftp == null)
    {
      Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Can not list files on url: {0}, because no connection!",
          url);
      return new String[0];
    }
    List<String> fs = new ArrayList<>();
    try
    {
      Vector<?> files = this.chsftp.ls(url);
      for (int i = 0; i < files.size(); i++)
      {
        if (this.isFile(url + separator + ((LsEntry) files.get(i)).getFilename()))
        {
          fs.add(((LsEntry) files.get(i)).getFilename());
        }
      }
    }
    catch (SftpException e)
    {
      Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
    }
    return fs.toArray(new String[fs.size()]);
  }

  @Override
  public String[] listDirs(String url) throws IOException
  {
    List<String> fs = new ArrayList<>();
    try
    {
      Vector<?> files = this.chsftp.ls(url);
      for (int i = 0; i < files.size(); i++)
      {
        if (this.isDir(url + separator + ((LsEntry) files.get(i)).getFilename()))
        {
          fs.add(((LsEntry) files.get(i)).getFilename());
        }
      }
    }
    catch (SftpException e)
    {
      Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
    }
    return fs.toArray(new String[fs.size()]);
  }

  @Override
  public InputStream readFile(String url) throws IOException
  {
    InputStream stream = null;
    try
    {
      stream = this.chsftp.get(url);
    }
    catch (SftpException e)
    {
      throw new IOException(e.getMessage());
    }
    return stream;
  }

  public OutputStream writeFile(String url) throws IOException
  {
    OutputStream stream;
    try
    {
      stream = this.chsftp.put(url);
    }
    catch (SftpException e)
    {
      throw new IOException(e.getMessage());
    }
    return stream;
  }
  
  public OutputStream writeFile(String url, boolean append) throws IOException
  {
    OutputStream stream;
    try
    {
    	stream = this.chsftp.put(url, 1);
    }
    catch (SftpException e)
    {
      throw new IOException(e.getMessage());
    }
    return stream;
  }

  @Override
  public boolean addFile(String url) throws IOException
  {
    try
    {
      // create a new File on ssh server, and get teh outputstream for it
      OutputStream stream = this.chsftp.put(url);
      stream.close();
    }
    catch (SftpException e)
    {
      throw new IOException(e.getMessage());
    }
    return true;
  }

  @Override
  public boolean addDir(String url) throws IOException
  {
    try
    {
      this.chsftp.mkdir(url);
    }
    catch (SftpException e)
    {
      throw new IOException(e.getMessage());
    }
    return true;
  }

  @Override
  public boolean removeFile(String url) throws IOException
  {
    try
    {
      this.chsftp.rm(url);
    }
    catch (SftpException e)
    {
      throw new IOException(e.getMessage());
    }
    return true;
  }

  @Override
  public boolean removeDir(String url) throws IOException
  {
    try
    {
      this.chsftp.rmdir(url);
    }
    catch (SftpException e)
    {
      throw new IOException(e.getMessage());
    }
    return true;
  }

  @Override
  public boolean isFile(String url)
  {
    SftpATTRS attr;
    try
    {
      attr = this.chsftp.stat(url);
      if (attr == null)
      {
        return false;
      }
      return !attr.isDir();
    }
    catch (SftpException e)
    {
      return false;
    }
  }

  @Override
  public boolean isDir(String url)
  {
    SftpATTRS attr;
    try
    {
      attr = this.chsftp.stat(url);
      return attr.isDir();
    }
    catch (SftpException e)
    {
      return false;
    }
  }

  @Override
  public String getHostName()
  {
    return this.hostName;
  }

  @Override
  public void setHostName(String hostName)
  {
    this.hostName = hostName;
  }

  @Override
  public void setRootPath(String path)
  {
    this.rootPath = path;
  }

  @Override
  public String getRootPath()
  {
    return rootPath;
  }

  @Override
  public boolean renameFile(String oldName, String newName) throws IOException
  {
    try
    {
      this.chsftp.rename(oldName, newName);
    }
    catch (SftpException e)
    {
      throw new IOException(e.getMessage());
    }
    return true;
  }

  @Override
  public String getUser()
  {
    return this.user;
  }

  @Override
  public void setUser(String user)
  {
    this.user = user;
  }

  @Override
  public String getPassword()
  {
    return passwd;
  }

  @Override
  public void setPassword(String pwd)
  {
    this.passwd = pwd;
  }

  @Override
  public boolean isconnected()
  {
    if (this.session != null)
    {
      return this.session.isConnected();
    }
    return false;
  }

  @Override
  public boolean startProcess(String... args)
  {
    try
    {
      String exe = "";
      for (int i = 1; i < args.length; i++)
      {
        exe += exe + " " + args[i];
      }
      this.chexec.setCommand("cd \"" + args[0] + "\" \n  \"" + exe + "\"");
      this.chexec.connect();
    }
    catch (JSchException e)
    {
      Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
    }
    return true;
  }

  @Override
  public boolean stopProcess()
  {
    try
    {
      String command = "kill " + this.processId;
      this.chexec = (ChannelExec) session.openChannel("exec");
      inputstream = this.chexec.getInputStream();
      errorstream = this.chexec.getErrStream();
      this.chexec.setCommand(command);
      this.chexec.connect();
      // now read line
      BufferedReader reader = new BufferedReader(new InputStreamReader(errorstream));
      // String firstpid =
      reader.readLine();
      this.chexec.disconnect();
    }
    catch (JSchException | IOException e)
    {
      Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
    }
    return true;
  }

  @Override
  public InputStream getActualInputStream()
  {
    return inputstream;
  }

  @Override
  public InputStream getActualErrorStream()
  {
    return errorstream;
  }

  public int getTimeOut()
  {
    return timeout;
  }

  public void setTimeOut(int timeout)
  {
    this.timeout = timeout;
  }

  public boolean execCommand(String command)
  {
    try
    {
      this.chexec = (ChannelExec) session.openChannel("exec");
      this.chexec.setCommand(command);
      this.chexec.connect();
      this.chexec.disconnect();
    }
    catch (JSchException e)
    {
      Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
    }
    return true;
  }

  public boolean connectToProcess(String processName)
  {
    String command = "ps ax | grep '" + processName + "' | awk ' { print $1;}'";
    try
    {
      this.chexec = (ChannelExec) session.openChannel("exec");
      inputstream = this.chexec.getInputStream();
      errorstream = this.chexec.getErrStream();
      this.inputstream = this.chexec.getInputStream();
      this.chexec.setCommand(command);
      this.chexec.connect();
      // now read line
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputstream));
      String firstpid = reader.readLine();
      // inputstream.markSupported()
      this.chexec.disconnect();
      // now try one again
      this.chexec = (ChannelExec) session.openChannel("exec");
      inputstream = this.chexec.getInputStream();
      errorstream = this.chexec.getErrStream();
      this.chexec.setCommand(command);
      this.chexec.connect();
      // now read line
      reader = new BufferedReader(new InputStreamReader(inputstream));
      String secondpid = reader.readLine();
      this.chexec.disconnect();
      if (firstpid.compareTo(secondpid) == 0)
      {
        try
        {
          this.processId = Integer.parseInt(secondpid);
        }
        catch (NumberFormatException ex)
        {
          Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage());
        }
        return true;
      }
    }
    catch (Exception e)
    {
      Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
    }
    return false;
  }

  @Override
  public String getConnectionName()
  {
    return connectionName;
  }

  @Override
  public void setConnectionName(String name)
  {
    this.connectionName = name;
  }

  @Override
  public void setJavaPath(String path)
  {
    this.javaPath = path;
  }

  @Override
  public String getJavaPath()
  {
    return this.javaPath;
  }

  @Override
  public void setJavaArg(String value)
  {
    this.javaArg = value;
  }

  @Override
  public String getJavaArg()
  {
    return this.javaArg;
  }

  @Override
  public String getTargetFileSeparator()
  {
    return separator;
  }

  @Override
  public void setTargetFileSeparator(String separator)
  {
    this.separator = separator;
  }

  @Override
  public boolean removeDir(String url, boolean subfile) throws IOException
  {
    if (!subfile)
    {
      this.removeDir(url);
    }
    else
    {
      if (isDir(url))
      {
        String[] files = listFiles(url);
        String[] dirs = listDirs(url);
        if (!url.endsWith(separator))
        {
          url = url + separator;
        }
        // try to delete all subfiles
        for (String file : files)
        {
          if (file.compareTo(".") == 0 || file.compareTo("..") == 0)
          {
            continue;
          }
          removeFile(url + file);
        }
        // try to delete all subdirs
        for (String dir : dirs)
        {
          if (dir.compareTo(".") == 0 || dir.compareTo("..") == 0)
          {
            continue;
          }
          removeDir(url + dir, subfile);
        }
        removeDir(url);
      }
    }
    return false;
  }

  public void disconnect()
  {
    if (this.session == null)
    {
      return;
    }
    this.session.disconnect();
  }
}
