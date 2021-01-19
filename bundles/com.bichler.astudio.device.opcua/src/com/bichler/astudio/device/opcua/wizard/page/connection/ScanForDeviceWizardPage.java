package com.bichler.astudio.device.opcua.wizard.page.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import com.bichler.astudio.device.opcua.DeviceActivator;
import com.bichler.astudio.filesystem.DataHubFileSystem;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.filesystem.SimpleFileSystem;
import com.bichler.astudio.images.common.CommonImagesActivator;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.zebra.handler.PrintEvent;
import com.bichler.astudio.zebra.handler.PrintLabelHandler;
import com.hbsoft.comet.broadcast.BroadcastClient;
import com.hbsoft.comet.broadcast.BroadcastConstants;
import com.hbsoft.comet.broadcast.BroadcastEntry;
import com.hbsoft.comet.broadcast.BroadcastMessage;

public class ScanForDeviceWizardPage extends WizardPage
{
  @Override
  public boolean canFlipToNextPage()
  {
    return true;
  }
  private IFileSystem filesystem = null;
  private Button btnScan;
  private Button btnActivateHUB;
  private TreeViewer treeViewer;
  private IStructuredSelection selection;
  private Text txtIp;
  private Text txtPort;
  private Text txtTimeout;

  /**
   * Create the wizard.
   * 
   * @wbp.parser.constructor
   */
  public ScanForDeviceWizardPage()
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
    btnScan = new Button(container, SWT.NONE);
    GridData gdbtnScan = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1);
    gdbtnScan.widthHint = 48;
    gdbtnScan.heightHint = 48;
    btnScan.setLayoutData(gdbtnScan);
    btnScan.setToolTipText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.handler.wizard.devicescan.page.scan"));
    btnScan.setImage(
        CommonImagesActivator.getDefault().getRegisteredImage(CommonImagesActivator.IMG_24, CommonImagesActivator.LOG));
    /**
     * only available if master dongle is connected
     */
    boolean masterdongle = true;
    if (masterdongle)
    {
      btnActivateHUB = new Button(container, SWT.NONE);
      GridData gd_btn_activateHUB = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 2, 1);
      gd_btn_activateHUB.widthHint = 48;
      gd_btn_activateHUB.heightHint = 48;
      btnActivateHUB.setLayoutData(gd_btn_activateHUB);
      btnActivateHUB.setToolTipText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
          "com.bichler.astudio.device.opcua.handler.wizard.devicescan.page.activateregister"));
      btnActivateHUB.setEnabled(false);
      btnActivateHUB.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          try
          {
            if (treeViewer.getInput() != null)
            {
              BroadcastMessage message = ((ArrayList<BroadcastMessage>) treeViewer.getInput()).get(0);
              message.getAddress();
              DataHubFileSystem fs = new DataHubFileSystem();
              fs.setHostName(message.getAddress());
              fs.setPassword(message.getPassword());
              fs.setUser(message.getUsername());
              fs.setTimeOut(Integer.parseInt(message.getConnectionTimeout()));
              fs.connect();
              // if (treeViewer.getInput() instanceof BroadcastMessage)
              // {
              URL serialurl = new URL("http://www.hb-softsolution.com/dataHUB/devicestateserialnumber.php");
              URLConnection urlc = serialurl.openConnection();
              InputStream stream = urlc.getInputStream();
              InputStreamReader reader = new InputStreamReader(stream);
              BufferedReader in = new BufferedReader(reader);
              String inputLine;
              inputLine = in.readLine();
              if (inputLine != null && !inputLine.isEmpty())
              {
                // create next serialnumber
                int serial = Integer.parseInt(inputLine);
                serial++;
                // set serialnumber to datahub hostname
                fs.execCommand("hostname dataHUB" + serial);
                OutputStream out = fs.writeFile("/etc/hostname\n");
                out.write(("dataHUB" + serial).getBytes());
                out.flush();
                out.close();
                String eth0_mac = message.getKeyValuePairs().get("eth0-mac").getValue();
                String eth1_mac = message.getKeyValuePairs().get("eth1-mac").getValue();
                // read dataHUB version
                InputStream input = fs.readFile("/hbin/version.txt");
                byte[] inbyte = new byte[1024];
                int read = input.read(inbyte);
                input.close();
                String version = new String(inbyte, 0, read);
                fs.execCommand("/hbin/javaOPC -version");
                String insertstring = "http://www.hb-softsolution.com/dataHUB/devicestateinsert.php?devicetype=1001&serialnumber="
                    + serial + "&modulenr=1" + "&mac1=" + eth0_mac + "&mac2=" + eth1_mac + "&def_ip1=172.17.0.111"
                    + "&def_ip2=10.0.0.111" + "&firmware=" + version.split("\\n")[0] + "&java=1.7_0_45-b45"
                    + "&webfrontend=v.1.0.0.1_0.0.0.2" + "&testperson=HB" + "&customer=K001"
                    + "&customer_ordernr=4788600" + "&license=testlicense";
                URL inserturl = new URL(insertstring);
                urlc = inserturl.openConnection();
                stream = urlc.getInputStream();
                reader = new InputStreamReader(stream);
                in = new BufferedReader(reader);
                inputLine = in.readLine();
                /**
                 * print label
                 */
                DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
                DateFormat mf = new SimpleDateFormat("MM.yyyy");
                Date today = Calendar.getInstance().getTime();
                PrintLabelHandler handler = new PrintLabelHandler();
                PrintEvent trigger = new PrintEvent(serial + "", eth0_mac, eth1_mac, df.format(today),
                    mf.format(today));
                ExecutionEvent event = new ExecutionEvent(new HashMap<>(), trigger, null);
                try
                {
                  handler.execute(event);
                  treeViewer.setInput(null);
                  btnActivateHUB.setEnabled(false);
                }
                catch (ExecutionException e1)
                {
                  Logger.getLogger(getClass().getName()).log(Level.SEVERE, e1.getMessage());
                }
              }
              in.close();
            }
          }
          catch (IOException e1)
          {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e1.getMessage(), e1);
          }
        }
      });
    }
    setHandler();
    fillSettings();
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

  private void createSettings(Composite container)
  {
    Label lbl_ip = new Label(container, SWT.NONE);
    lbl_ip.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lbl_ip.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.handler.wizard.devicescan.page.ip"));
    txtIp = new Text(container, SWT.BORDER);
    txtIp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    Label lbl_port = new Label(container, SWT.NONE);
    lbl_port.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lbl_port.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.handler.wizard.devicescan.page.port"));
    txtPort = new Text(container, SWT.BORDER);
    txtPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    Label lbl_timeout = new Label(container, SWT.NONE);
    lbl_timeout.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lbl_timeout.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.property.timeout"));
    txtTimeout = new Text(container, SWT.BORDER);
    txtTimeout.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
  }

  private void createTreeViewer(Composite container)
  {
    this.treeViewer = new TreeViewer(container, SWT.BORDER);
    this.treeViewer.setContentProvider(new ScanForDeviceContentProvider());
  
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

  private void fillSettings()
  {
    this.txtIp.setText(BroadcastConstants.BC_IP);
    this.txtPort.setText("" + BroadcastConstants.BC_PORT);
    this.txtTimeout.setText("" + BroadcastConstants.BC_TIMEOUT);
  }

  private void setHandler()
  {
    btnScan.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        final String ip = txtIp.getText();
        final int port = Integer.parseInt(txtPort.getText());
        int cachedtimeout = Integer.parseInt(txtTimeout.getText());
        // 0 or lower is infinite to wait
        if (cachedtimeout <= 0)
        {
          cachedtimeout = 1;
        }
        final int timeout = cachedtimeout;
        ProgressMonitorDialog dialog = new ProgressMonitorDialog(getShell());
        try
        {
          dialog.run(true, true, new IRunnableWithProgress()
          {
            @Override
            public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
            {
              ScanForCombox scan = new ScanForCombox(monitor, ip, port, timeout);
              scan.start();
              while (scan.isAlive())
              {
                if (monitor.isCanceled())
                {
                  monitor.done();
                  btnActivateHUB.setEnabled(false);
                  return;
                }
                Thread.sleep(100);
              }
            }
          });
        }
        catch (InvocationTargetException | InterruptedException e1)
        {
          Logger.getLogger(getClass().getName()).log(Level.SEVERE, e1.getMessage(), e1);
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
            btnActivateHUB.setEnabled(true);
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
      // default dispose function, do nothing
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
        return ((List) parentElement).toArray();
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
}
