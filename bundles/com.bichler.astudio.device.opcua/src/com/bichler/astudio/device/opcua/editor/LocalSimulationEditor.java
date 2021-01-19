package com.bichler.astudio.device.opcua.editor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
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
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
// import org.eclipse.ui.internal.console.ConsoleManager;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

import com.bichler.astudio.device.opcua.DeviceActivator;
import com.bichler.astudio.device.opcua.DeviceSharedImages;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.filesystem.SimpleFileSystem;
import com.bichler.astudio.navigation.nodes.StudioModelNode;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
import com.bichler.opc.comdrv.ComDRVManager;
import com.bichler.scriptmanager.EcmaScriptManager;

public class LocalSimulationEditor extends EditorPart
{
  public static final String ID = "com.bichler.astudio.device.opcua.editor.simulation.LocalSimulationEditor"; //$NON-NLS-1$
  private final Logger logger = Logger.getLogger(getClass().getName());
  public static final String APPLICATION_NAME = "SimulatingOPCServer";
  /** scrolled composite */
  private ScrolledComposite scrolledComposite;
  private Composite composite;
  private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
  private Composite composite_configuration;
  private Button btnStart;
  // private Combo cmb_device;
  private Section sctnVMArguments;
  private Composite composite_vmarguments;
  private Button btn_minHeap;
  private Spinner spinner_minHeap;
  private Button btn_maxHeap;
  private Spinner spinner_maxHeap;
  private Button btn_permGen;
  private Spinner spinner_permGen;

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
  private List<Button> selectedScripts = new ArrayList<>();
  private List<String> selectedScriptNames = new ArrayList<>();
  private Button btnRunScript;
  private String serverpath = "";

  public LocalSimulationEditor()
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
    // application section
    // createPartApplication();
    createPartScripts();
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

  private void createPartScripts()
  {
    // section users
    Section sctnConfiguration = formToolkit.createSection(composite, Section.TWISTIE | Section.TITLE_BAR);
    sctnConfiguration.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
    formToolkit.paintBordersFor(sctnConfiguration);
    sctnConfiguration.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.server.form.scripts"));
    sctnConfiguration.setExpanded(true);
    // composite users
    composite_configuration = new Composite(sctnConfiguration, SWT.NONE);
    formToolkit.adapt(composite_configuration);
    formToolkit.paintBordersFor(composite_configuration);
    sctnConfiguration.setClient(composite_configuration);
    composite_configuration.setLayout(new GridLayout(2, false));
    btnRunScript = new Button(composite_configuration, SWT.NONE);
    btnRunScript.setToolTipText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.server.editor.tooltip.startscript"));
    btnRunScript
        .setImage(ResourceManager.getPluginImage("com.bichler.astudio.device.opcua", "icons/editor/48/simulation_start.png"));
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
      // LogActivator.getDefault().getLogManager()
      // .logInfo("Closing OPC UA Server");
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
    setControlsApplicationRunning();
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
    // this.sctnConfiguration.computeSize(SWT.DEFAULT, SWT.DEFAULT);
    this.sctnVMArguments.computeSize(SWT.DEFAULT, SWT.DEFAULT);
    this.sctnLaunch.computeSize(SWT.DEFAULT, SWT.DEFAULT);
    return this.composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
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
    composite_vmarguments = new Composite(sctnVMArguments, SWT.NONE);
    formToolkit.adapt(composite_vmarguments);
    formToolkit.paintBordersFor(composite_vmarguments);
    sctnVMArguments.setClient(composite_vmarguments);
    composite_vmarguments.setLayout(new GridLayout(3, false));
    cb_javaVm = new Button(composite_vmarguments, SWT.CHECK);
    cb_javaVm.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.simulation.form.javavm"));
    formToolkit.adapt(cb_javaVm, true, true);
    txt_javaVmPath = new Text(composite_vmarguments, SWT.BORDER | SWT.READ_ONLY);
    txt_javaVmPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    formToolkit.adapt(txt_javaVmPath, true, true);
    btn_javaVm = new Button(composite_vmarguments, SWT.NONE);
    formToolkit.adapt(btn_javaVm, true, true);
    btn_javaVm.setText("...");
    btn_minHeap = new Button(composite_vmarguments, SWT.CHECK);
    formToolkit.adapt(btn_minHeap, true, true);
    btn_minHeap.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.simulation.form.vmargs.initheap"));
    spinner_minHeap = new Spinner(composite_vmarguments, SWT.BORDER);
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
    btn_maxHeap = new Button(composite_vmarguments, SWT.CHECK);
    formToolkit.adapt(btn_maxHeap, true, true);
    btn_maxHeap.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.simulation.form.vmargs.maxheap"));
    spinner_maxHeap = new Spinner(composite_vmarguments, SWT.BORDER);
    spinner_maxHeap.setMaximum(10000);
    spinner_maxHeap.setMinimum(1);
    spinner_maxHeap.setSelection(256);
    spinner_maxHeap.setPageIncrement(1);
    spinner_maxHeap.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
    formToolkit.adapt(spinner_maxHeap);
    formToolkit.paintBordersFor(spinner_maxHeap);
    /**
     * PermGen size
     */
    btn_permGen = new Button(composite_vmarguments, SWT.CHECK);
    formToolkit.adapt(btn_permGen, true, true);
    btn_permGen.setText(CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.device.opcua.simulation.form.vmargs.permgen"));
    spinner_permGen = new Spinner(composite_vmarguments, SWT.BORDER);
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
        e.printStackTrace();
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
            e.printStackTrace();
          }
        }
      }
    }
  }

  private void selectControls()
  {
    this.cb_javaVm.notifyListeners(SWT.Selection, new Event());
    this.btn_maxHeap.notifyListeners(SWT.Selection, new Event());
    this.btn_minHeap.notifyListeners(SWT.Selection, new Event());
    this.btn_permGen.notifyListeners(SWT.Selection, new Event());
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
          logger.log(Level.SEVERE, CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
              "com.bichler.astudio.device.opcua.simulation.editor.dialog.simulation.log.error.simulation.launch"), e1);
          MessageDialog.openError(getSite().getShell(),
              CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
                  "com.bichler.astudio.device.opcua.simulation.editor.dialog.simulation.title"),
              CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
                  "com.bichler.astudio.device.opcua.simulation.editor.dialog.simulation.log.error.simulation.launch")
                  + e1.getMessage());
        }
      }
    });
    this.btnRunScript.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        try
        {
          /** here we execute java scripts */
          EcmaScriptManager script = new EcmaScriptManager();
          /**
           * set the server interface to communicate with that in the ecmascript
           */
          List<String> scripts = new ArrayList<>();
          /**
           * add internal script functions
           */
          IPath scriptpath = new Path(serverpath).append("ecmascripts").append("internal").append("internal.js");
          scripts.add(scriptpath.toOSString());
          for (int i = 0; i < selectedScripts.size(); i++)
          {
            Button check = selectedScripts.get(i);
            if (check.getSelection())
            {
              System.out.println("run script: " + selectedScriptNames.get(i));
              scriptpath = new Path(serverpath).append("ecmascripts").append(selectedScriptNames.get(i) + ".js");
              scripts.add(scriptpath.toOSString());
            }
          }
          script.execScripts(scripts);
        }
        catch (Exception e1)
        {
          e1.printStackTrace();
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
          logger.log(Level.SEVERE, CustomString.getString(DeviceActivator.getDefault().RESOURCE_BUNDLE,
              "com.bichler.astudio.device.opcua.simulation.editor.dialog.simulation.log.error.simulation.stop"), e1);
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
    this.btn_permGen.addSelectionListener(new SelectionAdapter()
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
        spinner_maxHeap.setEnabled(enabled);
      }
    });
  }

  private void setControlsApplicationRunning()
  {
    ServerInstance.getInstance().start();
    btnStart.setEnabled(false);
    btnStop.setEnabled(true);
    // this.sctnConfiguration.setEnabled(false);
    this.sctnVMArguments.setEnabled(false);
  }

  private void setControlsApplicationStopped()
  {
    ServerInstance.getInstance().stop();
    btnStart.setEnabled(true);
    btnStop.setEnabled(false);
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
