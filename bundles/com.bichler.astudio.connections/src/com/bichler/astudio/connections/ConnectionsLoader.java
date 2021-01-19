package com.bichler.astudio.connections;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Path;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.bichler.astudio.filesystem.DataHubFileSystem;
import com.bichler.astudio.filesystem.HBStudioConnections;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.filesystem.SimpleFileSystem;

public class ConnectionsLoader extends DefaultHandler
{
  private ConnectionsHostManager hostManager = null;
  private IFileSystem filesystem = null;

  public ConnectionsLoader(ConnectionsHostManager hostManager)
  {
    this.hostManager = hostManager;
    hostManager.setStudioConnections(new HBStudioConnections());
  }

  @Override
  public void startElement(String uri, String localname, String name, Attributes attrs) throws SAXException
  {
    if (name.compareTo("ConnectionType") == 0)
    {
      String prop = attrs.getValue(0);
      if (prop.compareTo("ssh") == 0)
      {
        this.filesystem = new DataHubFileSystem();
      }
      else if (prop.compareTo("simple") == 0)
      {
        this.filesystem = new SimpleFileSystem();
      }
    }
    else if (name.compareTo("Connectionname") == 0)
    {
      hostManager.getStudioConnections().addConnection(attrs.getValue(0), filesystem);
      filesystem.setConnectionName(attrs.getValue(0));
    }
    else if (name.compareTo("ConnectionTimeOut") == 0)
    {
      try
      {
        filesystem.setTimeOut(Integer.parseInt(attrs.getValue(0)));
      }
      catch (NumberFormatException e)
      {
        // do nothing, we take the default value
      }
    }
    else if (name.compareTo("Host") == 0)
    {
      filesystem.setHostName(attrs.getValue(0));
    }
    else if (name.compareTo("User") == 0)
    {
      filesystem.setUser(attrs.getValue(0));
    }
    else if (name.compareTo("Password") == 0)
    {
      filesystem.setPassword(attrs.getValue(0));
    }
    else if (name.compareTo("JavaPath") == 0)
    {
      filesystem.setJavaPath(attrs.getValue(0));
    }
    else if (name.compareTo("JarArg") == 0)
    {
      filesystem.setJavaArg(attrs.getValue(0));
    }
    else if (name.compareTo("RootPath") == 0)
    {
      filesystem.setRootPath(attrs.getValue(0));
    }
    else if (name.compareTo("FileSeparator") == 0)
    {
      filesystem.setTargetFileSeparator(attrs.getValue(0));
    }
  }

  public void importWorkspaceHosts(String path, String servers)
  {
    File runtimeStructure = new Path(path).append(servers).toFile();
    if (runtimeStructure.exists() && runtimeStructure.isDirectory())
    {
      List<File> serverFiles = new ArrayList<File>();
      File[] files = runtimeStructure.listFiles();
      if (files != null)
      {
        for (File file : files)
        {
          if (!file.isHidden() && file.isDirectory())
          {
            serverFiles.add(file);
          }
        }
      }
      for (File f : serverFiles)
      {
        IFileSystem fs = new SimpleFileSystem();
        fs.setConnectionName(f.getName());
        fs.setTargetFileSeparator(File.separator);
        fs.setTimeOut(20000);
        fs.setRootPath(f.getPath());
        hostManager.getStudioConnections().addConnection(f.getName(), fs);
      }
    }
  }
}
