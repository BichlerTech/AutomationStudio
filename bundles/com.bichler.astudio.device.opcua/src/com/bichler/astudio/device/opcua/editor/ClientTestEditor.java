package com.bichler.astudio.device.opcua.editor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.transport.security.KeyPair;
import org.opcfoundation.ua.utils.CertificateUtils;

import com.bichler.astudio.device.opcua.DeviceActivator;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.filesystem.SimpleFileSystem;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.opc.comdrv.ComDRVManager;
import com.bichler.scriptmanager.EcmaScriptManager;

import opc.client.application.UAClient;
import opc.client.application.UADiscoveryClient;
import opc.client.application.core.ApplicationConfiguration;
import opc.client.application.core.ClientApplicationConfiguration;
import opc.client.application.core.ClientSecurityConfiguration;
import opc.client.application.core.TransportQuotas;
import opc.client.service.ClientSession;
import opc.client.service.ProfileManager;
import opc.sdk.core.session.AllowNoneCertificateValidator;

public class ClientTestEditor extends EditorPart
{
  public static final String ID = "com.bichler.astudio.device.opcua.editor.simulation.ClientTestEditorPart"; //$NON-NLS-1$
  /** scrolled composite */
  private ScrolledComposite scrolledComposite;
  private Composite composite;
  private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
  private Composite composite_configuration;
  private IFileSystem filesystem = null;
  private List<Button> selectedScripts = new ArrayList<>();
  private List<String> selectedScriptNames = new ArrayList<>();
  private Button btnRunScript;
  private String serverpath = "";
  private Text btnStart;
  private Text btnStop;
  private Logger logger = Logger.getLogger(getClass().getName());

  public ClientTestEditor()
  {
    // set the internal server instance to the driver manager
    ComDRVManager.getDRVManager().setServer(ServerInstance.getInstance().getServerInstance());
    IViewPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
        .findView(OPCNavigationView.ID);
    if (view instanceof OPCNavigationView)
    {
      OPCNavigationView navView = (OPCNavigationView) view;
      StudioModelNode selectedElement = (StudioModelNode) navView.getViewer().getInput();
      serverpath = selectedElement.getFilesystem().getRootPath();
    }
  }

  /**
   * Create contents of the editor part.
   * 
   * @param parent
   */
  @Override
  public void createPartControl(Composite parent)
  {
    scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
    scrolledComposite.setMinWidth(700);
    scrolledComposite.setMinHeight(750);
    scrolledComposite.setExpandHorizontal(true);
    scrolledComposite.setExpandVertical(true);
    // main composite
    composite = new Composite(scrolledComposite, SWT.NONE);
    composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_LIST_BACKGROUND));
    scrolledComposite.setMinSize(new Point(300, 600));
    scrolledComposite.setContent(this.composite);
    composite.setLayout(new GridLayout(1, false));
    createButtonGroup();
    createPartScripts();
    // displays initial values
    fillEditor();
    // set handler for swt controls
    setHandler();
    // compute size for editor
    computeSize();
  }

  private void createButtonGroup()
  {
    Section sctnConfiguration = formToolkit.createSection(composite, Section.TWISTIE | Section.TITLE_BAR);
    sctnConfiguration.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 2));
    formToolkit.paintBordersFor(sctnConfiguration);
    sctnConfiguration.setText("Verbindungseinstellungen");
    sctnConfiguration.setExpanded(true);
    Composite composite_configuration = new Composite(sctnConfiguration, SWT.NONE);
    formToolkit.adapt(composite_configuration);
    formToolkit.paintBordersFor(composite_configuration);
    sctnConfiguration.setClient(composite_configuration);
    composite_configuration.setLayout(new GridLayout(2, false));
    GridData gd_uri = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
    gd_uri.widthHint = 60;
    Label uri = new Label(composite_configuration, SWT.NONE);
    uri.setText("Uri:");
    uri.setLayoutData(gd_uri);
    formToolkit.adapt(uri, true, true);
    GridData gd_btnStart = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
    btnStart = new Text(composite_configuration, SWT.NONE);
    btnStart.setText("opc.tcp://localhost:48010");
    btnStart.setLayoutData(gd_btnStart);
    btnStart.setToolTipText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.simulation.editor.tooltip.start"));
    formToolkit.adapt(btnStart, true, true);
    //btnStart.setImage(DeviceSharedImages.getImage(DeviceSharedImages.ICON_SERVER_START));
    GridData gd_sec = new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1);
    gd_sec.widthHint = 60;
    Label sec = new Label(composite_configuration, SWT.NONE);
    sec.setText("Security:");
    sec.setLayoutData(gd_sec);
    formToolkit.adapt(sec, true, true);
    btnStop = new Text(composite_configuration, SWT.NONE);
    btnStop.setToolTipText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.simulation.editor.tooltip.stop"));
    //btnStop.setImage(DeviceSharedImages.getImage(DeviceSharedImages.ICON_SERVER_START));
    GridData gd_btnStop = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_btnStop.heightHint = 15;
    gd_btnStop.widthHint = 64;
    btnStop.setLayoutData(gd_btnStop);
    formToolkit.adapt(btnStop, true, true);
  }

  private void createPartScripts()
  {
    // section users
    Section sctnConfiguration = formToolkit.createSection(composite, Section.TWISTIE | Section.TITLE_BAR);
    sctnConfiguration.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
    formToolkit.paintBordersFor(sctnConfiguration);
    sctnConfiguration.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.server.form.scripts"));
    sctnConfiguration.setExpanded(true);
    // composite users
    composite_configuration = new Composite(sctnConfiguration, SWT.NONE);
    formToolkit.adapt(composite_configuration);
    formToolkit.paintBordersFor(composite_configuration);
    sctnConfiguration.setClient(composite_configuration);
    composite_configuration.setLayout(new GridLayout(2, false));
    btnRunScript = new Button(composite_configuration, SWT.NONE);
    btnRunScript.setToolTipText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.server.editor.tooltip.startscript"));
    btnRunScript
        .setImage(ResourceManager.getPluginImage("com.bichler.astudio.device", "icons/editor/48/simulation_start.png"));
    GridData gd_btnStart = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
    gd_btnStart.heightHint = 64;
    gd_btnStart.widthHint = 64;
    btnRunScript.setLayoutData(gd_btnStart);
    formToolkit.adapt(btnRunScript, true, true);
  }

  @Override
  public void setFocus()
  {
    // Set the focus
  }

  @Override
  public void doSave(IProgressMonitor monitor)
  {
    // Do the Save operation
  }

  @Override
  public void doSaveAs()
  {
    // Do the Save As operation
  }

  @Override
  public void init(IEditorSite site, IEditorInput input) throws PartInitException
  {
    // Initialize the editor part
    setSite(site);
    setInput(input);
  }

  @Override
  public boolean isSaveAsAllowed()
  {
    return false;
  }

  private Point computeSize()
  {
    return this.composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
  }

  private void fillEditor()
  {
    if (!serverpath.isEmpty())
    {
      IPath scriptpath = new Path(serverpath).append("ecmascripts").append("start.conf");
      BufferedReader reader = null;
      try
      {
        filesystem = new SimpleFileSystem();
        if (!filesystem.isFile(scriptpath.toString()))
        {
          return;
        }
        InputStream stream = filesystem.readFile(scriptpath.toString());
        InputStreamReader streamreader = new InputStreamReader(stream);
        reader = new BufferedReader(streamreader);
        String line = "";
        while ((line = reader.readLine()) != null)
        {
          if (line.startsWith("#single"))
          {
            continue;
          }
          if (line.startsWith("#interval"))
          {
            break;
          }
          if (line.startsWith("#"))
          {
            continue;
          }
          if (line.trim().isEmpty())
          {
            continue;
          }
          if (!line.isEmpty())
          {
            Button cb_javaVm = new Button(composite_configuration, SWT.CHECK);
            this.selectedScripts.add(cb_javaVm);
            formToolkit.adapt(cb_javaVm, true, true);
            Label script = new Label(composite_configuration, SWT.BORDER | SWT.READ_ONLY);
            script.setText(line.replace(".js", ""));
            this.selectedScriptNames.add(line.replace(".js", ""));
          }
        }
        reader.close();
        streamreader.close();
        stream.close();
      }
      catch (IOException e)
      {
        logger.log(Level.SEVERE, e.getMessage());
      }
      finally
      {
        if (reader != null)
        {
          try
          {
            reader.close();
          }
          catch (IOException e)
          {
            logger.log(Level.SEVERE, e.getMessage());
          }
        }
      }
    }
  }

  private void setHandler()
  {
    this.btnRunScript.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        try
        {
          /** here we execute java scripts */
          EcmaScriptManager script = new EcmaScriptManager();
          List<String> scripts = new ArrayList<>();
          for (int i = 0; i < selectedScripts.size(); i++)
          {
            Button check = selectedScripts.get(i);
            if (check.getSelection())
            {
              logger.log(Level.INFO, "start run script: " + selectedScriptNames.get(i));
              IPath scriptpath = new Path(serverpath).append("ecmascripts").append(selectedScriptNames.get(i) + ".js");
              scripts.add(scriptpath.toOSString());
            }
          }
          if (scripts.isEmpty())
          {
            logger.log(Level.INFO, "No script selected, so skipt test!");
            return;
          }
          /**
           * set the client interface to communicate with that in the ecmascript
           */
          logger.log(Level.INFO, "Try to connect to server! ");
          UADiscoveryClient discovery = new UAClient().createDiscoveryClient();
          EndpointDescription[] endpoints;
          endpoints = discovery.getEndpoints(false, btnStart.getText());
          // select proper endpoint
          EndpointDescription endpoint = discovery.selectEndpointNone(endpoints);
          UAClient uaclient = createClient();
          /**
           * create a new session for test client
           */
          ClientSession session = uaclient.createSession(endpoint, "Testpannel");
          session.setKeepAliveInterval(10000);
          session.setReconnectionPeriod(10000);
          uaclient.activateSession(session, null);
          /**
           * add client instance to script engine
           */
          script.getEngine().put("client", uaclient);
          script.getEngine().eval("load(\"nashorn:mozilla_compat.js\");");
          script.getEngine().eval("importPackage(java.io);");
          script.getEngine().eval("importPackage(java.nio);");
          script.getEngine().eval("importPackage(java.nio.file);");
          script.getEngine().eval("importPackage(java.lang);");
          script.getEngine().eval("importPackage(java.util.logging);");
          script.getEngine().eval("importPackage(org.opcfoundation.ua.builtintypes);");
          script.getEngine().eval("importPackage(org.opcfoundation.ua.core);");
          /**
           * execute the inserted scripts
           */
          script.execScripts(scripts);
          /**
           * script execution was successfully, so close all opened sessions and client
           * connection
           */
          uaclient.closeSession(session, true);
          uaclient.close();
        }
        catch (Exception e1)
        {
          logger.log(Level.SEVERE, e1.getMessage());
        }
      }
    });
  }

  /**
   * create an opc ua client instance with default values
   *
   * @return
   */
  private UAClient createClient()
  {
    UAClient client = new UAClient();
    try
    {
      /*
       * here we set client configuration by code
       */
      ApplicationConfiguration config = new ApplicationConfiguration();
      //
      //config.setApplication(new Application());
      ClientApplicationConfiguration app = new ClientApplicationConfiguration();
      config.setClientApplicationConfiguration(app);
      config.setApplicationType("Client_1");
      config.setApplicationName("Automation Studio Test Client");
      config.setApplicationUri("urn:User-VAIO:UASDK:HBS-Client");
      config.setProductUri("http://centauro.com/UASDK/UAClient");
      config.setDefaultSessionTimeout(30000);
      ClientSecurityConfiguration cconf = new ClientSecurityConfiguration();
      cconf.setCommonName("HBS OPC Client");
      cconf.setOrganisation("HB-Softsolution");
      cconf.setPrivateKeyPassword("com.hbsoft.comet.client");
      // cconf.setNonceLength("32");
      config.setCertificateValidator(new AllowNoneCertificateValidator(null));
      // create client certificate on the fly
      String hostName = InetAddress.getLocalHost().getHostName();
      KeyPair clientApplicationInstanceCertificate = CertificateUtils.createApplicationInstanceCertificate(
          cconf.getCommonName(), cconf.getOrganisation(), config.getApplicationUri(), 365, hostName);
      cconf.setApplicationCertificate(clientApplicationInstanceCertificate);
      config.setClientSecurityConfig(cconf);
      TransportQuotas transp = new TransportQuotas();
      transp.setOperationTimeout(10000);
      transp.setMaxStringLength(1048576);
      transp.setMaxByteStringLength(1048576);
      transp.setMaxArrayLength(65535);
      transp.setMaxMessageSize(4194304);
      transp.setMaxBufferSize(65535);
      transp.setChannelLifetime(300000);
      transp.setOperationTimeout(10000);
      transp.setSecurityTokenLifetime(3600000);
      config.setTransportQuotas(transp);
      client.setApplicationConfiguration(config);
      client.setProfileManager(new ProfileManager());
      return client;
    }
    catch (NullPointerException | IOException | GeneralSecurityException npe)
    {
      logger.log(Level.SEVERE, null, npe);
    }
    return null;
  }

  @Override
  public boolean isDirty()
  {
    return false;
  }
}
