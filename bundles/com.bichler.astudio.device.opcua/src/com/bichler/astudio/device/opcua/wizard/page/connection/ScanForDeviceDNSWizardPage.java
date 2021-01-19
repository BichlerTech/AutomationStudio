package com.bichler.astudio.device.opcua.wizard.page.connection;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import com.hbsoft.comet.broadcast.BroadcastClient;
import com.hbsoft.comet.broadcast.BroadcastConstants;
import com.hbsoft.comet.broadcast.BroadcastEntry;
import com.hbsoft.comet.broadcast.BroadcastMessage;
import com.bichler.astudio.device.opcua.DeviceActivator;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.filesystem.SimpleFileSystem;
import com.bichler.astudio.images.common.CommonImagesActivator;
import com.bichler.astudio.utils.internationalization.CustomString;

public class ScanForDeviceDNSWizardPage extends WizardPage
{
  private IFileSystem filesystem = null;
  private Button btn_Scan;
  private TreeViewer treeViewer;;
  private IStructuredSelection selection;
  // private Text txt_ip;
  // private Text txt_port;
  // private Text txt_timeout;

  /**
   * Create the wizard.
   * 
   * @wbp.parser.constructor
   */
  public ScanForDeviceDNSWizardPage()
  {
    super("scanfordevice");
    setTitle(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.handler.wizard.devicescan.page.title"));
    setDescription(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.handler.wizard.devicescan.page.title"));
    this.filesystem = new SimpleFileSystem();
  }

  /**
   * Create contents of the wizard.
   * 
   * @param parent
   */
  public void createControl(Composite parent)
  {
    Composite container = new Composite(parent, SWT.NULL);
    setControl(container);
    container.setLayout(new GridLayout(2, false));
    createSettings(container);
    Label separator = new Label(container, SWT.SEPARATOR | SWT.HORIZONTAL);
    separator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
    createTreeViewer(container);
    btn_Scan = new Button(container, SWT.NONE);
    GridData gd_btn_Scan = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1);
    gd_btn_Scan.widthHint = 48;
    gd_btn_Scan.heightHint = 48;
    btn_Scan.setLayoutData(gd_btn_Scan);
    btn_Scan.setToolTipText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.handler.wizard.devicescan.page.scan"));
    btn_Scan.setImage(
        CommonImagesActivator.getDefault().getRegisteredImage(CommonImagesActivator.IMG_24, CommonImagesActivator.LOG));
    setHandler();
    // fillSettings();
    // select();
  }

  @Override
  public boolean isPageComplete()
  {
    IStructuredSelection s = getSelection();
    if (s == null)
    {
      return false;
    }
    if (s.isEmpty())
    {
      return false;
    }
    return true;
  }

  public IStructuredSelection getSelection()
  {
    return this.selection;
  }

  public IFileSystem getFilesystem()
  {
    return filesystem;
  }

  public void setFilesystem(IFileSystem filesystem)
  {
    this.filesystem = filesystem;
  }
  // private void select() {
  // this.cmb_type.select(0);
  // int index = this.cmb_type.getSelectionIndex();
  // this.cmb_type.notifyListeners(SWT.Selection, null);
  //
  // }

  private void createSettings(Composite container)
  {
    Label lbl_host = new Label(container, SWT.NONE);
    lbl_host.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lbl_host.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.handler.wizard.devicescan.page.host"));
  }

  private void createTreeViewer(Composite container)
  {
    this.treeViewer = new TreeViewer(container, SWT.BORDER);
    this.treeViewer.setContentProvider(new ScanForDeviceContentProvider());
    // this.treeViewer.setLabelProvider(new ScanForDeviceLabelProvider());
    Tree tree = treeViewer.getTree();
    tree.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
    TreeViewerColumn treeViewerColumn = new TreeViewerColumn(treeViewer, SWT.NONE);
    TreeColumn trclmnKey = treeViewerColumn.getColumn();
    trclmnKey.setWidth(200);
    trclmnKey.setText("Key");
    treeViewerColumn.setLabelProvider(new ColumnLabelProvider()
    {
      @Override
      public Image getImage(Object element)
      {
        if (element instanceof BroadcastMessage)
        {
          return CommonImagesActivator.getDefault().getRegisteredImage(CommonImagesActivator.IMG_16,
              CommonImagesActivator.SERVER_STANDBY);
        }
        return super.getImage(element);
      }

      @Override
      public String getText(Object element)
      {
        if (element instanceof BroadcastMessage)
        {
          String address = ((BroadcastMessage) element).getAddress();
          return address;
        }
        else if (element instanceof Entry)
        {
          return ((Entry<String, BroadcastEntry>) element).getKey();
        }
        return "";
      }
    });
    TreeViewerColumn treeViewerColumn_1 = new TreeViewerColumn(treeViewer, SWT.NONE);
    TreeColumn trclmnNewColumn = treeViewerColumn_1.getColumn();
    trclmnNewColumn.setWidth(200);
    trclmnNewColumn.setText("Value");
    treeViewerColumn_1.setLabelProvider(new ColumnLabelProvider()
    {
      @Override
      public String getText(Object element)
      {
        if (element instanceof Entry)
        {
          BroadcastEntry bc = ((Entry<String, BroadcastEntry>) element).getValue();
          return bc.getValue();
        }
        return "";
      }
    });
  }


  // private void fillSettings() {
  // this.txt_ip.setText(BroadcastConstants.BC_IP);
  // this.txt_port.setText("" + BroadcastConstants.BC_PORT);
  // this.txt_timeout.setText("" + BroadcastConstants.BC_TIMEOUT);
  // }
  static class SampleListener implements ServiceListener
  {
    @Override
    public void serviceAdded(ServiceEvent event)
    {
      System.out.println("Service added   : " + event.getName() + "." + event.getType());
    }

    @Override
    public void serviceRemoved(ServiceEvent event)
    {
      System.out.println("Service removed : " + event.getName() + "." + event.getType());
    }

    @Override
    public void serviceResolved(ServiceEvent event)
    {
      System.out.println("Service resolved: " + event.getInfo());
    }
  }

  /**
   * Retourne toutes les adresses locales à cet ordinateur.
   * 
   * @return Set of InetAddress
   */
  static Set<InetAddress> getAllInetAddresses()
  {
    Set<InetAddress> result = new HashSet<InetAddress>();
    try
    {
      Enumeration<NetworkInterface> nifs = NetworkInterface.getNetworkInterfaces();
      while (nifs.hasMoreElements())
      {
        NetworkInterface nif = nifs.nextElement();
        Enumeration<InetAddress> inas = nif.getInetAddresses();
        while (inas.hasMoreElements())
        {
          InetAddress inetAddress = inas.nextElement();
         
          result.add(inetAddress);
        }
      }
    }
    catch (SocketException se)
    {
      // logger.warning("Error while fetching network interfaces
      // addresses: " + se);
    }
    return result;
  }

  private void setHandler()
  {
    btn_Scan.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        // final String ip = txt_ip.getText();
        //
        // final int port = Integer.parseInt(txt_port.getText());
        //
        // int cachedtimeout = Integer.parseInt(txt_timeout.getText());
        // 0 or lower is infinite to wait
        // if (cachedtimeout <= 0) {
        // cachedtimeout = 1;
        // }
        //
        // final int timeout = cachedtimeout;
        ProgressMonitorDialog dialog = new ProgressMonitorDialog(getShell());
        try
        {
          dialog.run(true, true, new IRunnableWithProgress()
          {
            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
            {
              JmDNS jmdns;
              try
              {
                Set<InetAddress> allInets = getAllInetAddresses();
                for (InetAddress inetAddress : allInets)
                {
                  jmdns = JmDNS.create(inetAddress);
                  jmdns.addServiceListener("_ssh._tcp.local.", new SampleListener());
                  ServiceInfo[] infos = jmdns.list("_ssh._tcp.local.");
                  for (ServiceInfo info : infos)
                  {
                    String server = info.getServer();
                    if (server.endsWith("."))
                    {
                      System.out.println(server.substring(0, server.lastIndexOf(".")));
                    }
                    else
                    {
                      System.out.println(server);
                    }
                  }
                  jmdns.close();
                }
              }
              catch (IOException e2)
              {
                // TODO Auto-generated catch block
                e2.printStackTrace();
              }
            }
          });
        }
        catch (InvocationTargetException e1)
        {
          e1.printStackTrace();
        }
        catch (InterruptedException e1)
        {
          e1.printStackTrace();
        }
      }
    });
    this.treeViewer.addSelectionChangedListener(new ISelectionChangedListener()
    {
      @Override
      public void selectionChanged(SelectionChangedEvent event)
      {
        selection = (IStructuredSelection) event.getSelection();
        boolean isComplete = isPageComplete();
        setPageComplete(isComplete);
      }
    });
  }


  class ScanForCombox extends Thread
  {
    private IProgressMonitor monitor;
    private String ip;
    private int port;
    private int timeout;

    public ScanForCombox(IProgressMonitor monitor, String ip, int port, int timeout)
    {
      this.monitor = monitor;
      this.ip = ip;
      this.port = port;
      this.timeout = timeout;
    }

    @Override
    public void run()
    {
      try
      {
        monitor.beginTask(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
            "com.bichler.astudio.device.opcua.handler.wizard.devicescan.monitor.scan"), IProgressMonitor.UNKNOWN);
        BroadcastClient client = new BroadcastClient();
        client.setIP(ip);
        client.setPort(port);
        client.setTimeout(timeout);
        final List<BroadcastMessage> messages = client.findServerByUDP();
        if (monitor.isCanceled())
        {
          return;
        }
        Display.getDefault().syncExec(new Runnable()
        {
          @Override
          public void run()
          {
            treeViewer.setInput(messages);
          }
        });
      }
      finally
      {
        monitor.done();
      }
    }
  }


  class ScanForDeviceContentProvider implements ITreeContentProvider
  {
    @Override
    public void dispose()
    {
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput)
    {
    }

    @Override
    public Object[] getElements(Object inputElement)
    {
      return getChildren(inputElement);
    }

    @Override
    public Object[] getChildren(Object parentElement)
    {
      if (parentElement instanceof List<?>)
      {
        return ((List<?>) parentElement).toArray();
      }
      else if (parentElement instanceof BroadcastMessage)
      {
        Map<String, BroadcastEntry> keyvals = new LinkedHashMap<>(
            ((BroadcastMessage) parentElement).getKeyValuePairs());
        // remove username/ password entries
        keyvals.remove(BroadcastConstants.ATTR_USER);
        keyvals.remove(BroadcastConstants.ATTR_PASSWORD);
        return keyvals.entrySet().toArray();
      }
      // else if(parentElement instanceof Entry){
      // ((Entry)parentElement)
      // }
      return new Object[0];
    }

    @Override
    public Object getParent(Object element)
    {
      return null;
    }

    @Override
    public boolean hasChildren(Object element)
    {
      return getChildren(element).length > 0;
    }
  }
  // class ScanForDeviceLabelProvider extends LabelProvider implements
  // ITableLabelProvider {
  //
  // @Override
  // public Image getColumnImage(Object element, int columnIndex) {
  //
  // return null;
  // }
  //
  // @Override
  // public String getColumnText(Object element, int columnIndex) {
  //
  // if (element instanceof BroadcastMessage) {
  // String address = ((BroadcastMessage) element).getAddress();
  // switch (columnIndex) {
  // case 0:
  // return address;
  // }
  // }
  //
  // else if (element instanceof Entry) {
  // switch (columnIndex) {
  // case 0:
  // return ((Entry<String, String>) element).getKey();
  // case 1:
  // ((Entry<String, String>) element).getValue();
  // }
  // }
  //
  // return null;
  // }
  // }
}
