package com.bichler.astudio.editor.calculation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wb.swt.SWTResourceManager;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.AbstractOPCConfigDriverViewLinkEditorPart;
import com.bichler.astudio.opcua.editor.input.OPCUADriverEditorInput;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.widget.NodeToTrigger;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.swt.CheckBoxButton;
import com.bichler.astudio.utils.ui.swt.NumericText;
import com.bichler.astudio.view.drivermodel.handler.util.DriverBrowserUtil;

import opc.sdk.core.node.Node;

public class CalculationDriverEditor extends AbstractOPCConfigDriverViewLinkEditorPart
{
  public final static String ID = "com.bichler.astudio.editor.calculation.CalculationDriverEditor"; //$NON-NLS-1$
  private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
  private boolean dirty = false;
  private String drvConfigString = "";
  private Label lblScanCyclic;
  private NumericText txt_scanCyclic;
  private CheckBoxButton cb_scanCyclic;
  protected boolean scanCyclic = false;
  protected int scanCyclicInterval = 100;
  private Form frmCalculation;
  private List<NodeId> driverstatusnodes = new ArrayList<>();
  private boolean driverstatusflag = false;

  public CalculationDriverEditor()
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
    parent.setLayout(new FillLayout(SWT.HORIZONTAL));
    this.frmCalculation = formToolkit.createForm(parent);
    formToolkit.paintBordersFor(frmCalculation);
    formToolkit.decorateFormHeading(frmCalculation);
    frmCalculation.setText(CustomString.getString(CalculationActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.calculation.editor.driver.form.title"));
    frmCalculation.getBody().setLayout(new GridLayout(3, false));
    createOPCDriverModelSection(frmCalculation.getBody());
    createOPCDriverSettings(frmCalculation.getBody());
    this.fillControls();
    this.addHandler();
  }

  private void createOPCDriverSettings(Composite body)
  {
    Section sctnNewSection = formToolkit.createSection(body, Section.TWISTIE | Section.TITLE_BAR);
    sctnNewSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1));
    formToolkit.paintBordersFor(sctnNewSection);
    sctnNewSection.setText(
        CustomString.getString(CalculationActivator.getDefault().RESOURCE_BUNDLE, "calculation.driver.settings"));
    sctnNewSection.setExpanded(true);
    Composite sectionParent = formToolkit.createComposite(sctnNewSection, SWT.NONE);
    formToolkit.paintBordersFor(sectionParent);
    sectionParent.setLayout(new GridLayout(3, false));
    sctnNewSection.setClient(sectionParent);
    lblScanCyclic = new Label(sectionParent, SWT.NONE);
    lblScanCyclic.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
    lblScanCyclic.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblScanCyclic.setText(CustomString.getString(CalculationActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.calculation.editor.property.interval"));
    txt_scanCyclic = new NumericText(sectionParent, SWT.BORDER);
    txt_scanCyclic.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    cb_scanCyclic = new CheckBoxButton(sectionParent, SWT.BOLD);
    GridData gd_cbt_scanCyclic = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_cbt_scanCyclic.widthHint = 39;
    cb_scanCyclic.setLayoutData(gd_cbt_scanCyclic);
    cb_scanCyclic.setAlignment(SWT.LEFT);
    cb_scanCyclic.setBackground(SWTResourceManager.getColor(255, 255, 255));
  }

  @Override
  public void doSave(IProgressMonitor monitor)
  {
    this.setFocus();
    String scanCyclic = this.cb_scanCyclic.isChecked() + ";" + this.txt_scanCyclic.getText();
    IFileSystem fs = getEditorInput().getFileSystem();
    String config = getEditorInput().getDriverConfigPath();
    String tmpoutput = "";
    try
    {
      if (!fs.isFile(config))
      {
        fs.addFile(config);
      }
      if (fs.isFile(config))
      {
        // if(file.exists()) {
        try
        {
          OutputStream output = fs.writeFile(config);
          if (this.drvConfigString.indexOf("%address%") == -1)
          {
            tmpoutput = this.drvConfigString.replace("deviceaddress", "deviceaddress\n%address%");
          }
          if (tmpoutput.contains("%scancyclic%"))
          {
            tmpoutput = tmpoutput.replace("%scancyclic%", scanCyclic);
          }
          else
          {
            tmpoutput = tmpoutput + "\nscancyclic\n" + scanCyclic + "\n";
          }
          if (tmpoutput.contains("%drvstatusflag%"))
          {
            tmpoutput = tmpoutput.replace("%drvstatusflag%", "" + isDriverStatusModel());
          }
          else
          {
            tmpoutput = tmpoutput + "\ndrvstatusflag\n" + isDriverStatusModel() + "\n";
          }
          if (tmpoutput.contains("%driverstatus%"))
          {
            StringBuilder builder = new StringBuilder();
            for (NodeId driverstatusNode : driverstatusnodes)
            {
              Node node = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
                  .getNodeById(driverstatusNode);
              if (node == null)
              {
                continue;
              }
              String name = node.getBrowseName().getName();
              String uri = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
                  .getUri(driverstatusNode.getNamespaceIndex());
              String value = driverstatusNode.toString();
              if (value.contains(";"))
              {
                value = value.split(";")[1];
                value = ";" + value;
              }
              builder.append(name + "=" + uri + value + "\n");
            }
            tmpoutput = tmpoutput.replace("%driverstatus%", builder.toString());
          }
          else
          {
            if (!this.driverstatusnodes.isEmpty())
            {
              StringBuilder builder = new StringBuilder();
              for (NodeId driverstatusNode : driverstatusnodes)
              {
                Node node = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
                    .getNodeById(driverstatusNode);
                if (node == null)
                {
                  // TODO: HANDLE NO MODEL
                  continue;
                }
                String name = node.getBrowseName().getName();
                String uri = ServerInstance.getInstance().getServerInstance().getNamespaceUris()
                    .getUri(driverstatusNode.getNamespaceIndex());
                String value = driverstatusNode.toString();
                if (value.contains(";"))
                {
                  value = value.split(";")[1];
                  value = ";" + value;
                }
                builder.append(name + "=" + uri + value + "\n");
              }
              tmpoutput = tmpoutput + "\ndriverstatus\n" + builder.toString() + "\n";
            }
          }
          output.write(tmpoutput.getBytes());
          output.flush();
          output.close();
          setDirty(false);
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
    }
    catch (IOException ex)
    {
      ex.printStackTrace();
    }
  }

  @Override
  public void doSaveAs()
  {
    // Do the Save As operation
  }

  @Override
  public OPCUADriverEditorInput getEditorInput()
  {
    return (OPCUADriverEditorInput) super.getEditorInput();
  }

  @Override
  public void init(IEditorSite site, IEditorInput input) throws PartInitException
  {
    setSite(site);
    setInput(input);
    setPartName(getEditorInput().getServerName() + " - " + CustomString.getString(
        CalculationActivator.getDefault().RESOURCE_BUNDLE, "com.bichler.astudio.editor.calculation.editor.driver.name"));
    ProgressMonitorDialog dialog = new ProgressMonitorDialog(site.getShell());
    try
    {
      dialog.run(true, false, new IRunnableWithProgress()
      {
        public void run(IProgressMonitor monitor)
        {
          monitor.beginTask(
              CustomString.getString(CalculationActivator.getDefault().RESOURCE_BUNDLE, "monitor.init.task"),
              IProgressMonitor.UNKNOWN);
          monitor.subTask(
              CustomString.getString(CalculationActivator.getDefault().RESOURCE_BUNDLE, "monitor.init.message"));
          IFileSystem fs = getEditorInput().getFileSystem();
          String config = getEditorInput().getDriverConfigPath();
          if (fs.isFile(config))
          {
            BufferedReader bfr = null;
            try
            {
              bfr = new BufferedReader(new InputStreamReader(fs.readFile(config)));
              String line = "";
              while ((line = bfr.readLine()) != null)
              {
                drvConfigString += line + "\n";
                if (line.compareTo("deviceaddress") == 0)
                {
                  line = bfr.readLine();
                  if (line != null)
                  {
                    // get all configured address parts
                    String[] items = line.split(";");
                    if (items == null || items.length == 0)
                    {
                      continue;
                    }
                    drvConfigString += "%address%\n";
                  }
                }
                else if (line.compareTo("scancyclic") == 0)
                {
                  String cyclic = bfr.readLine();
                  String[] cyclicLine = cyclic.split(";");
                  if (cyclicLine.length > 1)
                  {
                    try
                    {
                      scanCyclic = Boolean.parseBoolean(cyclicLine[0]);
                      scanCyclicInterval = Integer.parseInt(cyclicLine[1]);
                    }
                    catch (NumberFormatException ex)
                    {
                      // TODO logg exception
                    }
                    drvConfigString += "%scancyclic%\n";
                  }
                }
                else if (line.compareTo("drvstatusflag") == 0)
                {
                  line = bfr.readLine();
                  if (line != null)
                  {
                    driverstatusflag = Boolean.parseBoolean(line);
                  }
                  drvConfigString += "%drvstatusflag%\n";
                }
                else if (line.compareTo("driverstatus") == 0)
                {
                  String driverstatus = null;
                  driverstatusnodes = new ArrayList<>();
                  while ((driverstatus = bfr.readLine()) != null)
                  {
                    if (driverstatus.trim().isEmpty())
                    {
                      break;
                    }
                    int start = driverstatus.indexOf("=");
                    String nodeid = driverstatus.substring(start + 1);
                    String[] nodeid2parse = nodeid.split(";");
                    NodeId parsedId = NodeId.NULL;
                    try
                    {
                      if (nodeid2parse.length <= 0)
                      {
                        parsedId = NodeId.parseNodeId(nodeid);
                      }
                      else
                      {
                        String ns = nodeid2parse[0];
                        String idValue = nodeid2parse[1];
                        int index = ServerInstance.getInstance().getServerInstance().getNamespaceUris().getIndex(ns);
                        parsedId = NodeId.parseNodeId("ns=" + index + ";" + idValue);
                      }
                      driverstatusnodes.add(parsedId);
                    }
                    catch (ArrayIndexOutOfBoundsException e)
                    {
                      // e.printStackTrace();
                    }
                    catch (IllegalArgumentException e)
                    {
                    }
                  }
                  drvConfigString += "%driverstatus%\n";
                }
              }
            }
            catch (IOException ex)
            {
              ex.printStackTrace();
            }
            finally
            {
              if (bfr != null)
              {
                try
                {
                  bfr.close();
                }
                catch (IOException e)
                {
                  e.printStackTrace();
                }
              }
            }
          }
          monitor.done();
        }
      });
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  @Override
  public boolean isDirty()
  {
    return dirty;
  }

  @Override
  public boolean isSaveAsAllowed()
  {
    return false;
  }

  public String removeEscapes(String builder)
  {
    return builder.replace(" ", "").replace("\n", "").replace("\t", "").replace("\r", "");
  }

  public void setDirty(boolean dirty)
  {
    this.dirty = dirty;
    firePropertyChange(IEditorPart.PROP_DIRTY);
  }

  @Override
  public void setFocus()
  {
    super.setFocus();
    this.frmCalculation.setFocus();
  }

  private void fillControls()
  {
    this.cb_scanCyclic.setChecked(this.scanCyclic);
    this.txt_scanCyclic.setText("" + this.scanCyclicInterval);
    fillDriverStatus(this.driverstatusflag, this.driverstatusnodes);
  }

  private void addHandler()
  {
    txt_scanCyclic.addModifyListener(new ModifyListener()
    {
      @Override
      public void modifyText(ModifyEvent e)
      {
        setDirty(true);
      }
    });
    cb_scanCyclic.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        setDirty(true);
      }
    });
    super.addHandler(getEditorInput().getDriverName(), this.driverstatusnodes);
  }

  @Override
  public void refreshDatapoints()
  {
  }

  @Override
  public void computeSection()
  {
  }

  @Override
  public void onFocusRemoteView()
  {
    DriverBrowserUtil.openEmptyDriverModelView();
  }

  @Override
  public void onDisposeRemoteView()
  {
    IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
    if (window != null)
    {
      IWorkbenchPage page = window.getActivePage();
      if (page != null)
      {
        IEditorReference[] references = page.getEditorReferences();
        if (references == null || references.length == 0)
        {
          DriverBrowserUtil.openEmptyDriverModelView();
        }
      }
    }
  }

  @Override
  public boolean isTriggerNodeValid(NodeToTrigger obj)
  {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public List<NodeToTrigger> getPossibleTriggerNodes()
  {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setPossibleTriggerNodes(List<NodeToTrigger> possibleTriggerNodes)
  {
    // TODO Auto-generated method stub
  }
}
