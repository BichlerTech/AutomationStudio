package com.bichler.astudio.device.opcua.editor;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.IStreamListener;
import org.eclipse.debug.core.model.IStreamMonitor;
import org.eclipse.debug.internal.core.OutputStreamMonitor;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.wb.swt.SWTResourceManager;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import com.bichler.astudio.device.core.preference.DevicePreferenceConstants;
import com.bichler.astudio.device.core.preference.DevicePreferenceManager;
import com.bichler.astudio.device.opcua.DeviceActivator;
import com.bichler.astudio.device.opcua.DeviceSharedImages;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.filesystem.SimpleFileSystem;
import com.bichler.astudio.filesystem.SshFileSystem;
import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
import com.bichler.astudio.opcua.nodes.OPCUAServerModelNode;
import com.bichler.astudio.utils.internationalization.CustomString;

public class SimulationEditor extends EditorPart
{
  public static final String ID = "com.bichler.astudio.device.opcua.editor.simulation.SimulationEditor"; //$NON-NLS-1$
  private final Logger logger = Logger.getLogger(getClass().getName());
  private static final String APPLICATION_NAME = "Automation Studio Console";
  /** scrolled composite */
  private ScrolledComposite scrolledComposite;
  private Composite composite;
  private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
  private Section sctnConfiguration;
  private Button btnStart;
  private Combo cmbDevice;
  private Section sctnVMArguments;
  private Button btn_minHeap;
  private Spinner spinner_minHeap;
  private Button btn_maxHeap;
  private Spinner spinnerMaxHeap;
  private Button btnPermGen;
  private Spinner spinner_permGen;
  private Text txt_connectionname;
  private Text txt_host;
  private Text txt_timeout;
  private Text txt_opcuarootpath;
  private Section sctnLaunch;
  private ILaunch launch;
  private IFileSystem filesystem = null;
  private Composite composite_launch;
  private Button btnStop;
  private boolean isDirty = false;
  private IDebugEventSetListener debugListener;
  // private Form form;
  // private Form frmSimulation;
  private Button cb_javaVm;
  private Text txt_javaVmPath;
  private Button btn_javaVm;

  public SimulationEditor()
  {
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
    // application section
    createPartApplication();
    // createPartScripts();
    // vm-arguments section
    createPartVMArguments();
    // launch section
    createPartLaunch();
    // displays initial values
    fillEditor();
    // set handler for swt controls
    setHandler();
    selectControls();
    // compute size for editor
    computeSize();
  }

  private void createButtonGroup()
  {
    Group applicationLevels = new Group(composite, SWT.BORDER);
    applicationLevels.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
    applicationLevels.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
    applicationLevels.setLayout(new GridLayout(2, false));
    btnStart = new Button(applicationLevels, SWT.NONE);
    btnStart.setToolTipText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.simulation.editor.tooltip.start"));
    btnStart.setImage(DeviceSharedImages.getImage(DeviceSharedImages.ICON_SERVER_START));
    GridData gd_btnStart = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_btnStart.heightHint = 64;
    gd_btnStart.widthHint = 64;
    btnStart.setLayoutData(gd_btnStart);
    formToolkit.adapt(btnStart, true, true);
    btnStop = new Button(applicationLevels, SWT.NONE);
    btnStop.setToolTipText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.simulation.editor.tooltip.stop"));
    btnStop.setImage(DeviceSharedImages.getImage(DeviceSharedImages.ICON_SERVER_START));
    GridData gd_btnStop = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_btnStop.heightHint = 64;
    gd_btnStop.widthHint = 64;
    btnStop.setLayoutData(gd_btnStop);
    formToolkit.adapt(btnStop, true, true);
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
  public void dispose()
  {
    // ask closing editor
    if (this.isDirty)
    {
      MessageDialog.openInformation(getSite().getShell(),
          CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
              "com.bichler.astudio.device.opcua.simulation.editor.dialog.simulation.title"),
          CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
              "com.bichler.astudio.device.opcua.simulation.editor.dialog.simulation.message.stop"));
    }
    if (this.isDirty)
    {
      logger.log(Level.INFO, CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
          "com.bichler.astudio.device.opcua.simulation.editor.dialog.simulation.log.info.simulation.stop"));
      try
      {
        terminateApplication();
      }
      catch (DebugException e)
      {
        logger.log(Level.SEVERE, CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
            "com.bichler.astudio.device.opcua.simulation.editor.dialog.simulation.log.error.simulation.stop"), e);
      }
    }
    super.dispose();
  }

  @Override
  public void init(IEditorSite site, IEditorInput input) throws PartInitException
  {
    // Initialize the editor part
    setSite(site);
    setInput(input);
    setPartName(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.simulation.editorinput.name"));
  }

  @Override
  public boolean isDirty()
  {
    // no dirty flag
    return false;
  }

  protected void setDirty(boolean isDirty)
  {
    this.isDirty = isDirty;
    firePropertyChange(IEditorPart.PROP_DIRTY);
  }

  @Override
  public boolean isSaveAsAllowed()
  {
    return false;
  }

  protected void launchApplication() throws Exception
  {
    if (this.filesystem == null)
    {
      throw new RuntimeException("No target filesystem selected!");
    }
    // listen to simulation processes
    this.debugListener = new IDebugEventSetListener()
    {
      @Override
      public void handleDebugEvents(final DebugEvent[] events)
      {
        Display.getDefault().syncExec(new Runnable()
        {
          @Override
          public void run()
          {
            for (DebugEvent evt : events)
            {
              // process is terminated
              if (DebugEvent.TERMINATE == evt.getKind())
              {
                setControlsApplicationStopped();
                try
                {
                  terminateApplication();
                }
                catch (DebugException e)
                {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
                }
              }
              // creation is terminated
              else if (DebugEvent.CREATE == evt.getKind())
              {
                setControlsApplicationRunning();
              }
            }
          }
        });
      }
    };
    DebugPlugin.getDefault().addDebugEventListener(this.debugListener);
    // remote or local debugging
    int fsType = -1;
    String launchConfig = "";
    // launchLocal();
    if (this.filesystem instanceof SimpleFileSystem)
    {
      fsType = 0;
    }
    else if (this.filesystem instanceof SshFileSystem)
    {
      fsType = 1;
    }
    // application running level type
    switch (fsType)
    {
    case 0:
      launchConfig = IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION;
      break;
    case 1:
      // TODO: REMOTE DEBUGGING
      launchConfig = IJavaLaunchConfigurationConstants.ID_REMOTE_JAVA_APPLICATION;
      break;
    default:
      launchConfig = IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION;
      break;
    }
    // boolean useJavaVm = cb_javaVm.getSelection();
    boolean maxHeap = btn_maxHeap.getSelection();
    boolean minHeap = btn_minHeap.getSelection();
    boolean permGen = btnPermGen.getSelection();
    String vmArgs = "";
    if (minHeap)
    {
      vmArgs += "-Xms" + spinner_minHeap.getSelection() + "m";
    }
    if (maxHeap)
    {
      if (!vmArgs.isEmpty())
      {
        vmArgs += " ";
      }
      vmArgs += "-Xmx" + spinnerMaxHeap.getSelection() + "m";
    }
    if (permGen)
    {
      if (!vmArgs.isEmpty())
      {
        vmArgs += " ";
      }
      vmArgs += "-XX:MaxPermSize=" + spinner_permGen.getSelection() + "m";
    }
    /**
     * create launch configuration
     */
    ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
    ILaunchConfigurationType type = manager.getLaunchConfigurationType(launchConfig);
    // fetch current running launch configuration
    ILaunchConfiguration[] configurations = manager.getLaunchConfigurations(type);
    // terminate running configurations
    for (int i = 0; i < configurations.length; i++)
    {
      ILaunchConfiguration configuration = configurations[i];
      if (configuration.getName().equals(APPLICATION_NAME))
      {
        configuration.delete();
        break;
      }
    }
    // new launch
    ILaunchConfigurationWorkingCopy workingCopy = type.newInstance(null, APPLICATION_NAME);
    /**
     * Filesystem from targhet
     */
    if (filesystem instanceof SshFileSystem)
    {
      // Set JVM debugger connection parameters
      Map<String, String> connectionParameters = new HashMap<String, String>();
      connectionParameters.put("hostname", ((SshFileSystem) this.filesystem).getHostName());
      connectionParameters.put("port", "" + 22);
      workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CONNECT_MAP, connectionParameters);
      workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_CONNECTOR,
          "org.eclipse.jdt.launching.socketAttachConnector");
    }
    /**
     * OPC Server jar
     */
    String mainJarPath = getPathFromDevice();
    try
    {
      boolean isFile = this.filesystem.isFile(mainJarPath);
      if (!isFile)
      {
        throw new Exception("Cannot simulate OPC UA Server on target device! No file on Path " + mainJarPath);
      }
    }
    catch (Exception e)
    {
      throw new Exception("Cannot simulate OPC UA Server on target device!", e);
    }
    IPath jarPath = new Path(mainJarPath);
    IRuntimeClasspathEntry toolsEntry = JavaRuntime.newArchiveRuntimeClasspathEntry(jarPath);
    toolsEntry.setClasspathProperty(IRuntimeClasspathEntry.USER_CLASSES);
    /**
     * Java system library
     */
    IPath systemLibsPath = new Path(JavaRuntime.JRE_CONTAINER);
    IRuntimeClasspathEntry systemLibsEntry = JavaRuntime.newRuntimeContainerClasspathEntry(systemLibsPath,
        IRuntimeClasspathEntry.STANDARD_CLASSES);
    /**
     * Classpath entries
     */
    List<String> classpath = new ArrayList<>();
    classpath.add(toolsEntry.getMemento());
    classpath.add(systemLibsEntry.getMemento());
    workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, classpath);
    workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, false);
    /**
     * vm args
     */
    if (!vmArgs.isEmpty())
    {
      workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, vmArgs);
    }
    /** Main Type */
    workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME,
        "com.bichler.opcua.server.serverruntime.ServerRuntime_Server");
    /**
     * Working directory
     */
    String workingDirPath = getWorkingDirFromDevice();
    File workingDir = new Path(workingDirPath).toFile();
    workingCopy.setAttribute(IJavaLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY, workingDir.getAbsolutePath());
    // launch
    ILaunchConfiguration configuration = workingCopy.doSave();
    
    this.launch = configuration.launch(ILaunchManager.RUN_MODE, null);
    if (launch.getProcesses() == null || launch.getProcesses().length <= 0)
    {
      terminateApplication();
      throw new Exception("Target launch application has been canceled!");
    }
    IStreamListener l = new IStreamListener()
    {
      @Override
      public void streamAppended(String text, IStreamMonitor monitor)
      {
        System.out.println(text);
      }
    };
    ((OutputStreamMonitor) launch.getProcesses()[0].getStreamsProxy().getOutputStreamMonitor()).addListener(l);
    ((OutputStreamMonitor) launch.getProcesses()[0].getStreamsProxy().getErrorStreamMonitor()).addListener(l);
    // DebugUITools.launch(configuration, ILaunchManager.RUN_MODE);
    setDirty(true);
  }

  protected void terminateApplication() throws DebugException
  {
    if (this.launch == null)
    {
      setDirty(false);
      return;
    }
    this.launch.terminate();
    this.launch = null;
    DebugPlugin.getDefault().removeDebugEventListener(this.debugListener);
    // clear console view
    setDirty(false);
  }

  private void computeSection()
  {
    Point minSize = computeSize();
    this.scrolledComposite.setMinSize(minSize);
    this.composite.layout(true);
  }

  private Point computeSize()
  {
    this.sctnConfiguration.computeSize(SWT.DEFAULT, SWT.DEFAULT);
    this.sctnVMArguments.computeSize(SWT.DEFAULT, SWT.DEFAULT);
    this.sctnLaunch.computeSize(SWT.DEFAULT, SWT.DEFAULT);
    return this.composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
  }

  private void createPartApplication()
  {
    // section users
    sctnConfiguration = formToolkit.createSection(composite, Section.TWISTIE | Section.TITLE_BAR);
    sctnConfiguration.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
    formToolkit.paintBordersFor(sctnConfiguration);
    sctnConfiguration.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.simulation.form.application"));
    sctnConfiguration.setExpanded(true);
    // composite users
    Composite compositeConfiguration = new Composite(sctnConfiguration, SWT.NONE);
    formToolkit.adapt(compositeConfiguration);
    formToolkit.paintBordersFor(compositeConfiguration);
    sctnConfiguration.setClient(compositeConfiguration);
    compositeConfiguration.setLayout(new GridLayout(2, false));
    Label lblDevice = formToolkit.createLabel(compositeConfiguration,
        CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
            "com.bichler.astudio.device.opcua.simulation.form.application.device"),
        SWT.NONE);
    lblDevice.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    this.cmbDevice = new Combo(compositeConfiguration, SWT.BORDER);
    cmbDevice.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    formToolkit.adapt(cmbDevice, true, true);
    /**
     * Device info
     */
    Group deviceinfo = new Group(compositeConfiguration, SWT.NONE);
    deviceinfo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
    deviceinfo.setLayout(new GridLayout(2, false));
    // connection name
    Label lbl_connectionname = new Label(deviceinfo, SWT.NONE);
    lbl_connectionname.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    formToolkit.adapt(lbl_connectionname, true, true);
    lbl_connectionname.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.simulation.form.application.connection"));
    txt_connectionname = new Text(deviceinfo, SWT.BORDER | SWT.READ_ONLY);
    txt_connectionname.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    formToolkit.adapt(txt_connectionname, true, true);
    // host
    Label lbl_host = new Label(deviceinfo, SWT.NONE);
    lbl_host.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    formToolkit.adapt(lbl_host, true, true);
    lbl_host.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.simulation.form.application.host"));
    txt_host = new Text(deviceinfo, SWT.BORDER | SWT.READ_ONLY);
    txt_host.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    formToolkit.adapt(txt_host, true, true);
    // timeout
    Label lbl_timeout = new Label(deviceinfo, SWT.NONE);
    lbl_timeout.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.simulation.form.application.timeout"));
    lbl_timeout.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    formToolkit.adapt(lbl_timeout, true, true);
    txt_timeout = new Text(deviceinfo, SWT.BORDER | SWT.READ_ONLY);
    txt_timeout.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    formToolkit.adapt(txt_timeout, true, true);
    // rootpath
    Label lbl_rootpath = new Label(deviceinfo, SWT.NONE);
    lbl_rootpath.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.simulation.form.application.path"));
    lbl_rootpath.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    formToolkit.adapt(lbl_rootpath, true, true);
    txt_opcuarootpath = new Text(deviceinfo, SWT.BORDER | SWT.READ_ONLY);
    txt_opcuarootpath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    formToolkit.adapt(txt_opcuarootpath, true, true);
  }

  private void createPartLaunch()
  {
    // section users
    sctnLaunch = formToolkit.createSection(composite, Section.TWISTIE | Section.TITLE_BAR);
    sctnLaunch.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
    formToolkit.paintBordersFor(sctnLaunch);
    sctnLaunch.setText("Launch");
    sctnLaunch.setExpanded(true);
    // composite users
    composite_launch = new Composite(sctnLaunch, SWT.NONE);
    formToolkit.adapt(composite_launch);
    formToolkit.paintBordersFor(composite_launch);
    sctnLaunch.setClient(composite_launch);
    composite_launch.setLayout(new GridLayout(2, false));
  }

  private void createPartVMArguments()
  {
    // section users
    sctnVMArguments = formToolkit.createSection(composite, Section.TWISTIE | Section.TITLE_BAR);
    sctnVMArguments.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
    formToolkit.paintBordersFor(sctnVMArguments);
    sctnVMArguments.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.simulation.form.vmargs"));
    sctnVMArguments.setExpanded(true);
    // composite users
    Composite compositeVmarguments = new Composite(sctnVMArguments, SWT.NONE);
    formToolkit.adapt(compositeVmarguments);
    formToolkit.paintBordersFor(compositeVmarguments);
    sctnVMArguments.setClient(compositeVmarguments);
    compositeVmarguments.setLayout(new GridLayout(3, false));
    cb_javaVm = new Button(compositeVmarguments, SWT.CHECK);
    cb_javaVm.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.simulation.form.javavm"));
    formToolkit.adapt(cb_javaVm, true, true);
    txt_javaVmPath = new Text(compositeVmarguments, SWT.BORDER | SWT.READ_ONLY);
    txt_javaVmPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    formToolkit.adapt(txt_javaVmPath, true, true);
    btn_javaVm = new Button(compositeVmarguments, SWT.NONE);
    formToolkit.adapt(btn_javaVm, true, true);
    btn_javaVm.setText("...");
    btn_minHeap = new Button(compositeVmarguments, SWT.CHECK);
    formToolkit.adapt(btn_minHeap, true, true);
    btn_minHeap.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.simulation.form.vmargs.initheap"));
    spinner_minHeap = new Spinner(compositeVmarguments, SWT.BORDER);
    spinner_minHeap.setMaximum(10000);
    spinner_minHeap.setMinimum(1);
    spinner_minHeap.setSelection(40);
    spinner_minHeap.setPageIncrement(1);
    spinner_minHeap.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
    formToolkit.adapt(spinner_minHeap);
    formToolkit.paintBordersFor(spinner_minHeap);
    /**
     * Max heap space
     */
    btn_maxHeap = new Button(compositeVmarguments, SWT.CHECK);
    formToolkit.adapt(btn_maxHeap, true, true);
    btn_maxHeap.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.simulation.form.vmargs.maxheap"));
    spinnerMaxHeap = new Spinner(compositeVmarguments, SWT.BORDER);
    spinnerMaxHeap.setMaximum(10000);
    spinnerMaxHeap.setMinimum(1);
    spinnerMaxHeap.setSelection(256);
    spinnerMaxHeap.setPageIncrement(1);
    spinnerMaxHeap.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
    formToolkit.adapt(spinnerMaxHeap);
    formToolkit.paintBordersFor(spinnerMaxHeap);
    /**
     * PermGen size
     */
    btnPermGen = new Button(compositeVmarguments, SWT.CHECK);
    formToolkit.adapt(btnPermGen, true, true);
    btnPermGen.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.simulation.form.vmargs.permgen"));
    spinner_permGen = new Spinner(compositeVmarguments, SWT.BORDER);
    spinner_permGen.setMaximum(10000);
    spinner_permGen.setMinimum(1);
    spinner_permGen.setSelection(128);
    spinner_permGen.setPageIncrement(1);
    spinner_permGen.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
    formToolkit.adapt(spinner_permGen);
    formToolkit.paintBordersFor(spinner_permGen);
  }

  private void fillEditor()
  {
    DevicePreferenceManager devicemanager = new DevicePreferenceManager();
    Preferences input = devicemanager.getRoot();
    try
    {
      String[] children = input.childrenNames();
      for (String child : children)
      {
        this.cmbDevice.add(child);
      }
    }
    catch (BackingStoreException e)
    {
      e.printStackTrace();
    }
  }

  private String getWorkingDirFromDevice()
  {
    // find server model node
    OPCNavigationView view = (OPCNavigationView) getEditorSite().getPage().findView(OPCNavigationView.ID);
    // server name
    OPCUAServerModelNode rootNode = (OPCUAServerModelNode) view.getViewer().getInput();
    // device to run
    DevicePreferenceManager devicemanager = new DevicePreferenceManager();
    Preferences root = devicemanager.getRoot();
    Preferences child = root.node(this.cmbDevice.getText());
    // append path
    String rootPath = child.get(DevicePreferenceConstants.PREFERENCE_DEVICE_ROOTPATH, "");
    return new Path(rootPath).append("servers").append(rootNode.getServerName()).toOSString();
  }

  private String getPathFromDevice()
  {
    DevicePreferenceManager devicemanager = new DevicePreferenceManager();
    Preferences root = devicemanager.getRoot();
    Preferences child = root.node(this.cmbDevice.getText());
    String rootPath = child.get(DevicePreferenceConstants.PREFERENCE_DEVICE_ROOTPATH, "");
    return new Path(rootPath).append("runtime").append("OPC_Server.jar").toOSString();
  }

  private void selectControls()
  {
    // -Xms32m -Xmx2048m -XX:MaxPermSize=512M
    this.cmbDevice.select(0);
    this.cmbDevice.notifyListeners(SWT.Selection, new Event());
    this.cb_javaVm.notifyListeners(SWT.Selection, new Event());
    this.btn_maxHeap.notifyListeners(SWT.Selection, new Event());
    this.btn_minHeap.notifyListeners(SWT.Selection, new Event());
    this.btnPermGen.notifyListeners(SWT.Selection, new Event());
    if (launch != null)
    {
      setControlsApplicationRunning();
    }
    else
    {
      setControlsApplicationStopped();
    }
  }

  private void setHandler()
  {
    // sections
    this.sctnConfiguration.addExpansionListener(new ExpansionAdapter()
    {
      @Override
      public void expansionStateChanged(ExpansionEvent e)
      {
        computeSection();
      }
    });
    this.sctnVMArguments.addExpansionListener(new ExpansionAdapter()
    {
      @Override
      public void expansionStateChanged(ExpansionEvent e)
      {
        computeSection();
      }
    });
    this.sctnLaunch.addExpansionListener(new ExpansionAdapter()
    {
      @Override
      public void expansionStateChanged(ExpansionEvent e)
      {
        computeSection();
      }
    });
    // device
    this.cmbDevice.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        btnStart.setEnabled(false);
        String devicename = ((Combo) cmbDevice).getText();
        if (devicename == null || devicename.isEmpty())
        {
          return;
        }
        btnStart.setEnabled(true);
        DevicePreferenceManager devicemanager = new DevicePreferenceManager();
        IFileSystem filesystem = devicemanager.getFilesystem(devicename);
        if (filesystem == null)
        {
          return;
        }
        txt_connectionname.setText(filesystem.getConnectionName());
        txt_host.setText(filesystem.getHostName());
        txt_timeout.setText("" + filesystem.getTimeOut());
        txt_opcuarootpath.setText(filesystem.getRootPath());
        SimulationEditor.this.filesystem = filesystem;
      }
    });
    // simulation start
    this.btnStart.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        try
        {
          launchApplication();
          btnStart.setEnabled(false);
        }
        catch (Exception e1)
        {
          logger.log(Level.SEVERE,
              CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
                  "com.bichler.astudio.device.opcua.simulation.editor.dialog.simulation.log.error.simulation.launch"),
              e1);
          MessageDialog.openError(getSite().getShell(),
              CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
                  "com.bichler.astudio.device.opcua.simulation.editor.dialog.simulation.title"),
              CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
                  "com.bichler.astudio.device.opcua.simulation.editor.dialog.simulation.log.error.simulation.launch")
                  + e1.getMessage());
        }
      }
    });
    this.btnStop.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        try
        {
          terminateApplication();
          setControlsApplicationStopped();
        }
        catch (DebugException e1)
        {
          logger.log(Level.SEVERE,
              CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
                  "com.bichler.astudio.device.opcua.simulation.editor.dialog.simulation.log.error.simulation.stop"),
              e1);
        }
      }
    });
    this.btn_javaVm.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        DirectoryDialog dialog = new DirectoryDialog(getSite().getShell(), SWT.OPEN);
        String path = dialog.open();
        if (path == null)
        {
          return;
        }
        txt_javaVmPath.setText(path);
      }
    });
    // vm arguments
    this.btnPermGen.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        boolean enabled = ((Button) e.getSource()).getSelection();
        spinner_permGen.setEnabled(enabled);
      }
    });
    this.cb_javaVm.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        boolean enable = ((Button) e.getSource()).getSelection();
        txt_javaVmPath.setEnabled(enable);
        btn_javaVm.setEnabled(enable);
      }
    });
    this.btn_minHeap.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        boolean enabled = ((Button) e.getSource()).getSelection();
        spinner_minHeap.setEnabled(enabled);
      }
    });
    this.btn_maxHeap.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        boolean enabled = ((Button) e.getSource()).getSelection();
        spinnerMaxHeap.setEnabled(enabled);
      }
    });
  }

  private void setControlsApplicationRunning()
  {
    btnStart.setEnabled(false);
    btnStop.setEnabled(true);
    this.sctnConfiguration.setEnabled(false);
    this.sctnVMArguments.setEnabled(false);
  }

  private void setControlsApplicationStopped()
  {
    btnStart.setEnabled(true);
    btnStop.setEnabled(false);
    this.sctnConfiguration.setEnabled(true);
    this.sctnVMArguments.setEnabled(true);
    // ConsoleView view = (ConsoleView)
    // getSite().getPage().findView(IConsoleConstants.ID_CONSOLE_VIEW);
    // if (view != null)
    // {
    // IConsole console = view.getConsole();
    // ConsolePlugin.getDefault().getConsoleManager().removeConsoles(new IConsole[]
    // { console });
    // }
  }
}
