package com.bichler.astudio.editor.aggregated.driver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import opc.client.application.listener.ReconnectListener;
import opc.client.service.ClientSession;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.progress.UIJob;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.core.BrowseDirection;
import org.opcfoundation.ua.core.BrowseResult;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;

import com.bichler.astudio.editor.aggregated.AggregatedEditorDevice;
import com.bichler.astudio.editor.aggregated.clientbrowser.model.AbstractCCModel;
import com.bichler.astudio.editor.aggregated.clientbrowser.model.RootCCModel;
import com.bichler.astudio.editor.aggregated.clientbrowser.provider.ClientConnectionContentProvider;
import com.bichler.astudio.editor.aggregated.clientbrowser.provider.ClientConnectionLabelProvider;
import com.bichler.astudio.editor.aggregated.clientbrowser.util.OPCConnectionAdapter;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.components.ui.serverbrowser.providers.UAServerModelNode;
import com.bichler.astudio.opcua.driver.enums.DriverConfigProperties;
import com.bichler.astudio.view.drivermodel.browser.listener.IDriverModelListener;
import com.bichler.astudio.view.drivermodel.handler.util.DriverBrowserUtil;

public class AggregatedDriverUtil
{
  public static AbstractCCModel openServer(IFileSystem filesystem, final AggregatedEditorDevice device,
      String driverpath)
  {
    final UAServerModelNode modelNode = new UAServerModelNode();
    modelNode.setDevice(device);
    if (device != null)
    {
      modelNode.setDisplayName(device.getUaServerName());
      modelNode.setServerUrl(device.getUaServerUri());
      String path_clientConfig = new Path(driverpath).append("clientconfig").append("clientconfig.xml").toOSString();
      String path_clientCert = new Path(driverpath).append("cert").append("clientcertificate.der").toOSString();
      String path_clientKey = new Path(driverpath).append("cert").append("clientkey.pfx").toOSString();
      File configurationFile = null, certFile = null, privKeyFile = null;
      String modelpath = device.getUaInformationModel();
      if (filesystem.isFile(path_clientConfig) && filesystem.isFile(path_clientCert)
          && filesystem.isFile(path_clientKey))
      {
        // try {
        configurationFile = new File(path_clientConfig);
        certFile = new File(path_clientCert);
        privKeyFile = new File(path_clientKey);
        device.connect2Server(filesystem, configurationFile, certFile, privKeyFile, modelpath);
        // close all input streams
        if (device.isConnected()
        // device.getUaclient() != null
        // && device.getUaclient().getActiveSession() != null
        )
        {
          modelNode.setConnected(true);
        }
        // only add reconnect listener if connection to an ua server
        // is ok
        if (device.getUaclient() != null && device.getUaclient().getActiveSession() != null)
        {
          device.getUaclient().getActiveSession().addReconnectListener(new ReconnectListener()
          {
            @Override
            public void onReconnectStarted(ClientSession session)
            {
              UIJob job = new UIJob("")
              {
                @Override
                public IStatus runInUIThread(IProgressMonitor monitor)
                {
                  // TODO Auto-generated method
                  // stub
                  modelNode.setConnected(false);
                  // tableViewer.refresh();
                  modelNode.setReconnection(true);
                  device.getUaclient().getActiveSession().reconnect();
                  return Status.OK_STATUS;
                }
              };
              job.schedule();
            }

            @Override
            public void onReconnectFinished(ClientSession session, boolean successfull)
            {
              UIJob job = new UIJob("")
              {
                @Override
                public IStatus runInUIThread(IProgressMonitor monitor)
                {
                  // TODO Auto-generated method
                  // stub
                  modelNode.setConnected(true);
                  // if (tableViewer != null
                  // && !tableViewer
                  // .getControl()
                  // .isDisposed()) {
                  // tableViewer.refresh();
                  // }
                  return Status.OK_STATUS;
                }
              };
              job.schedule();
            }

            @Override
            public void onConnectionLost(ClientSession session)
            {
              UIJob job = new UIJob("")
              {
                @Override
                public IStatus runInUIThread(IProgressMonitor monitor)
                {
                  // TODO Auto-generated method
                  // stub
                  modelNode.setConnected(false);
                  // tableViewer.refresh();
                  device.getUaclient().getActiveSession().reconnect();
                  return Status.OK_STATUS;
                }
              };
              job.schedule();
            }

			@Override
			public void onReconnectStopped(ClientSession session) {
				// TODO Auto-generated method stub
				
			}
          });
        }
      }
    }
    return new RootCCModel("root", NodeClass.Object, new OPCConnectionAdapter()
    {
      @Override
      public boolean isServerActive()
      {
        return device.isConnected();
      }

      @Override
      public BrowseResult browse(NodeId parentId, BrowseDirection forward, boolean includeSubtyes,
          UnsignedInteger nodeClass, NodeId referenceId, UnsignedInteger resultMask)
      {
        return device.browse(parentId, forward, includeSubtyes, nodeClass, referenceId, resultMask);
      }
    });
  }

  private static AggregatedEditorDevice loadOPCUAServerConnection(IFileSystem filesystem, String driverConfigPath)
  {
    AggregatedEditorDevice device = new AggregatedEditorDevice();
    InputStream stream = null;
    try
    {
      stream = filesystem.readFile(driverConfigPath);
      device.loadOPCUAServerConnection(stream);
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    finally
    {
      if (stream != null)
      {
        try
        {
          stream.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
    }
    return device;
  }

  public static void openDriverView(final IFileSystem filesystem, final String driverConfig, final String driverPath)
  {
    ClientConnectionLabelProvider labelProvider = new ClientConnectionLabelProvider();
    ClientConnectionContentProvider contentProvider = new ClientConnectionContentProvider();
    Map<DriverConfigProperties, IDriverModelListener> listeners = new HashMap<>();
    // // listener for csv
    listeners.put(DriverConfigProperties.editorconfigtype, new IDriverModelListener()
    {
      @Override
      public void loadModel(String modeltype)
      {
        AggregatedEditorDevice device = loadOPCUAServerConnection(filesystem, driverConfig);
        AbstractCCModel root = openServer(filesystem, device, driverPath);
        DriverBrowserUtil.updateDriverModelView(root);
      }
    });
    DriverBrowserUtil.openDriverModelView(contentProvider, labelProvider, filesystem, driverConfig, listeners,
        new AggregatedDriverDragSupport(), null);
  }
}
