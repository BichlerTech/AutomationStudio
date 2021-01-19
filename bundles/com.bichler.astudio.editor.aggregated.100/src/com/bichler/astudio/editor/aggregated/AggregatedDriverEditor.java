package com.bichler.astudio.editor.aggregated;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
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
import org.opcfoundation.ua.core.AnonymousIdentityToken;
import org.opcfoundation.ua.core.UserNameIdentityToken;
import org.opcfoundation.ua.core.X509IdentityToken;
import org.opcfoundation.ua.transport.security.Cert;
import org.opcfoundation.ua.transport.security.KeyPair;
import org.opcfoundation.ua.transport.security.PrivKey;
import org.opcfoundation.ua.utils.CertificateUtils;

import com.bichler.astudio.editor.aggregated.driver.AggregatedDriverUtil;
import com.bichler.astudio.editor.aggregated.model.AggregatedDriverConfigurationType;
import com.bichler.astudio.editor.aggregated.util.AggregatedUtil;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.AbstractOPCConfigDriverViewLinkEditorPart;
import com.bichler.astudio.opcua.dialog.discovery.dialog.OPCUAConnectionDialog;
import com.bichler.astudio.opcua.editor.input.OPCUADPEditorInput;
import com.bichler.astudio.opcua.editor.input.OPCUADriverEditorInput;
import com.bichler.astudio.opcua.handlers.opcua.OPCUAUtil;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.security.wizard.util.CertGenUtil;
import com.bichler.astudio.opcua.widget.NodeToTrigger;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.view.drivermodel.handler.util.DriverBrowserUtil;

import opc.client.application.core.ApplicationConfiguration;
import opc.client.application.core.ClientApplicationConfiguration;
import opc.client.application.core.ClientSecurityConfiguration;
import opc.sdk.core.node.Node;

public class AggregatedDriverEditor extends AbstractOPCConfigDriverViewLinkEditorPart
{
  public final static String ID = "com.bichler.astudio.editor.aggregated.AggregatedDriverEditor"; //$NON-NLS-1$
  private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
  private boolean dirty = false;
  private Text txtInfomodelpath;
  // tabfolder for aggregated configuration
  private CTabFolder tabFolder;
  private CTabItem tabRemote;
  private Text txt_servername;
  private Text txt_serverUri;
  private Text lst_securityPolicies;
  private Text cmb_securityMode;
  private Text txt_securityType;
  private Button btnNewButton;
  private Label lbl_state;
  private Text txt_username;
  private Text txt_password;
  private CTabItem tabModel;
  private Button btnCheckModel;
  private Button btnCheckRemoteServer;
  private Button btnOpenModel;
  private Button btnClearModel;
  private ScrolledComposite sc_certrtificate;
  private Composite cmp_cert;
  private Text txt_issuer;
  private Text txt_validafter;
  private Text txt_validbefore;
  private Text txt_algorithm;
  private Button btnRenew;
  private KeyPair newKeyPair;
  private Form frmAggregatedDriver;
  private AggregatedEditorDevice device;

  public AggregatedDriverEditor()
  {
  }

  @Override
  public void createPartControl(Composite parent)
  {
    parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
    parent.setLayout(new GridLayout(1, false));
    this.frmAggregatedDriver = formToolkit.createForm(parent);
    frmAggregatedDriver.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    formToolkit.paintBordersFor(frmAggregatedDriver);
    formToolkit.decorateFormHeading(frmAggregatedDriver);
    frmAggregatedDriver.setText(CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.aggregated.drivereditor.form.title"));
    frmAggregatedDriver.getBody().setLayout(new FillLayout(SWT.HORIZONTAL));
    ScrolledComposite root_scrolled = new ScrolledComposite(frmAggregatedDriver.getBody(),
        SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
    formToolkit.adapt(root_scrolled);
    formToolkit.paintBordersFor(root_scrolled);
    root_scrolled.setExpandHorizontal(true);
    root_scrolled.setExpandVertical(true);
    Composite composite_root = new Composite(root_scrolled, SWT.BORDER);
    root_scrolled.setContent(composite_root);
    GridLayout gl_composite_root = new GridLayout(1, false);
    gl_composite_root.verticalSpacing = 0;
    gl_composite_root.marginWidth = 0;
    gl_composite_root.marginHeight = 0;
    gl_composite_root.horizontalSpacing = 0;
    composite_root.setLayout(gl_composite_root);
    // model section
    createOPCDriverModelSection(composite_root);
    Section sctnNewSection = formToolkit.createSection(composite_root, Section.TWISTIE | Section.TITLE_BAR);
    sctnNewSection.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1));
    formToolkit.paintBordersFor(sctnNewSection);
    sctnNewSection.setText(
        CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE, "aggregated.editor.driver.settings"));
    sctnNewSection.setExpanded(true);
    Composite sectionParent = formToolkit.createComposite(sctnNewSection, SWT.NONE);
    formToolkit.paintBordersFor(sectionParent);
    sectionParent.setLayout(new GridLayout(1, false));
    sctnNewSection.setClient(sectionParent);
    // remote server
    createRemoteServerTab(sectionParent);
    // information model
    createModelTab(sectionParent);
    // certificate section
    createCertificateSection(composite_root);
    fillControls();
    // root_scrolled.setMinSize(SWT.DEFAULT, SWT.DEFAULT);
    addHandler();
  }

  @Override
  public void computeSection()
  {
  }

  @Override
  public void doSave(IProgressMonitor monitor)
  {
    // create connection attributes
    String driverconfig = "driverid\n\n\n" + "drivertype\naggregated\n\n" + "connection properties\n\n"
        + "redundancy no\n\n" + "servername\n" + txt_servername.getText() + "\n\nserveruri\n" + txt_serverUri.getText()
        + "\n\nsecuritypolicy\n" + lst_securityPolicies.getText() + "\n\nsecuritymode\n" + cmb_securityMode.getText()
        + "\n\nsecuritytype\n";
    if (txt_securityType.getText().compareTo(AnonymousIdentityToken.class.getSimpleName()) == 0)
    {
      driverconfig += "0";
    }
    else if (txt_securityType.getText().compareTo(UserNameIdentityToken.class.getSimpleName()) == 0)
    {
      driverconfig += "1";
    }
    else if (txt_securityType.getText().compareTo(X509IdentityToken.class.getSimpleName()) == 0)
    {
      driverconfig += "2";
    }
    driverconfig += "\n\nusername\n" + txt_username.getText() + "\n\npassword\n" + txt_password.getText();
    if (!txtInfomodelpath.getText().isEmpty())
    {
      driverconfig += "\n\ninformationmodel\n" + txtInfomodelpath.getText();
    }
    // aggregated config type
    AggregatedDriverConfigurationType configType = (AggregatedDriverConfigurationType) tabFolder.getData();
    if (configType != null)
    {
      driverconfig += "\n\neditorconfigtype\n" + configType.name();
    }
    boolean isDrvStatusModel = isDriverStatusModel();
    driverconfig += "\n\ndrvstatusflag\n" + isDrvStatusModel;
    // driverstatus nodes
    StringBuilder builder = new StringBuilder();
    if (!this.device.driverstatusnodes.isEmpty())
    {
      driverconfig += "\n\ndriverstatus\n";
      for (NodeId driverstatusNode : this.device.driverstatusnodes)
      {
        Node node = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
            .getNodeById(driverstatusNode);
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
      driverconfig += builder.toString();
    }
    OutputStream output = null;
    try
    {
      output = getEditorInput().getFileSystem().writeFile(getEditorInput().getDriverConfigPath());
      output.write(driverconfig.getBytes());
      output.flush();
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
      if (output != null)
      {
        try
        {
          output.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
    }
    if (this.newKeyPair != null)
    {
      String driverpath = getEditorInput().getDriverPath();
      IFileSystem fs = getEditorInput().getFileSystem();
      String certFolder = new Path(driverpath).append("cert").toOSString();
      if (!fs.isDir(driverpath) || !fs.isDir(certFolder))
      {
        return;
      }
      final String cert = new Path(certFolder).append("clientcertificate.der").toOSString();
      if (!fs.isFile(cert))
      {
        return;
      }
      final String key = new Path(certFolder).append("clientkey.pfx").toOSString();
      if (!fs.isFile(key))
      {
        return;
      }
      CertGenUtil.store(this.newKeyPair, new File(cert), new File(key));
      this.newKeyPair = null;
    }
    setDirty(false);
    updateEditorIfNeeded();
    OPCUAUtil.validateOPCUADriver(getEditorInput().getFileSystem(), getEditorInput().getNode());
  }

  @Override
  public void doSaveAs()
  {
  }

  @Override
  public void init(IEditorSite site, IEditorInput input) throws PartInitException
  {
    this.setSite(site);
    this.setInput(input);
    setPartName(getEditorInput().getServerName() + " - " + getEditorInput().getDriverName() + " - "
        + CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
            "com.bichler.astudio.editor.aggregated.drivereditor.form.driver"));
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

  @Override
  public OPCUADriverEditorInput getEditorInput()
  {
    return (OPCUADriverEditorInput) super.getEditorInput();
  }

  @Override
  public void setFocus()
  {
    super.setFocus();
    this.frmAggregatedDriver.setFocus();
  }

  @Override
  public void setDirty(boolean dirty)
  {
    this.dirty = dirty;
    firePropertyChange(IEditorPart.PROP_DIRTY);
  }

  @Override
  public void refreshDatapoints()
  {
    // no datapoints to refresh
  }

  protected void handleCheckConfig(AggregatedDriverConfigurationType type, CTabItem tab, CTabItem... others)
  {
    Image imgCheck = AggregatedSharedImages.getImage(AggregatedSharedImages.ICON_CHECKED_1);
    Image imgUncheck = AggregatedSharedImages.getImage(AggregatedSharedImages.ICON_CHECKED_0);
    tab.setImage(imgCheck);
    boolean dirty = false;
    if (tab.getData() == null)
    {
      dirty = true;
    }
    tab.setData(type);
    this.tabFolder.setData(type);
    for (CTabItem other : others)
    {
      other.setImage(imgUncheck);
      other.setData(null);
    }
    setDirty(dirty);
  }

  private void createCertificateSection(Composite parent)
  {
    Section section_certificates = formToolkit.createSection(parent, Section.TWISTIE | Section.TITLE_BAR);
    section_certificates.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    section_certificates.setBounds(0, 0, 98, 21);
    formToolkit.paintBordersFor(section_certificates);
    section_certificates.setText(CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.aggregated.drivereditor.form.certificate"));
    section_certificates.setExpanded(true);
    sc_certrtificate = new ScrolledComposite(section_certificates, SWT.H_SCROLL | SWT.V_SCROLL);
    formToolkit.adapt(sc_certrtificate);
    formToolkit.paintBordersFor(sc_certrtificate);
    section_certificates.setClient(sc_certrtificate);
    sc_certrtificate.setExpandHorizontal(true);
    sc_certrtificate.setExpandVertical(true);
    cmp_cert = new Composite(sc_certrtificate, SWT.NONE);
    formToolkit.adapt(cmp_cert);
    formToolkit.paintBordersFor(cmp_cert);
    cmp_cert.setLayout(new GridLayout(2, false));
    Label lblIssuer = new Label(cmp_cert, SWT.NONE);
    lblIssuer.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    formToolkit.adapt(lblIssuer, true, true);
    lblIssuer.setText(CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.aggregated.drivereditor.form.certificate"));
    txt_issuer = new Text(cmp_cert, SWT.BORDER | SWT.READ_ONLY);
    txt_issuer.setText("");
    txt_issuer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    formToolkit.adapt(txt_issuer, true, true);
    Label lblAlgorithm = new Label(cmp_cert, SWT.NONE);
    lblAlgorithm.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblAlgorithm.setText(CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.aggregated.drivereditor.form.algorithm"));
    formToolkit.adapt(lblAlgorithm, true, true);
    txt_algorithm = new Text(cmp_cert, SWT.BORDER | SWT.READ_ONLY);
    txt_algorithm.setText("");
    txt_algorithm.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    formToolkit.adapt(txt_algorithm, true, true);
    Label lblValidAfter = new Label(cmp_cert, SWT.NONE);
    lblValidAfter.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, true, 1, 1));
    lblValidAfter.setText(CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.aggregated.drivereditor.form.validafter"));
    formToolkit.adapt(lblValidAfter, true, true);
    txt_validafter = new Text(cmp_cert, SWT.BORDER | SWT.READ_ONLY);
    txt_validafter.setText("");
    txt_validafter.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    formToolkit.adapt(txt_validafter, true, true);
    Label lblValidBefor = new Label(cmp_cert, SWT.NONE);
    lblValidBefor.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblValidBefor.setText(CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.aggregated.drivereditor.form.validbefore"));
    lblValidBefor.setBounds(0, 0, 52, 15);
    formToolkit.adapt(lblValidBefor, true, true);
    txt_validbefore = new Text(cmp_cert, SWT.BORDER | SWT.READ_ONLY);
    txt_validbefore.setText("");
    txt_validbefore.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    formToolkit.adapt(txt_validbefore, true, true);
    this.btnRenew = new Button(cmp_cert, SWT.NONE);
    btnRenew.setBounds(0, 0, 75, 25);
    formToolkit.adapt(btnRenew, true, true);
    btnRenew.setText(CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.aggregated.drivereditor.form.renew"));
    new Label(cmp_cert, SWT.NONE);
    sc_certrtificate.setContent(cmp_cert);
    sc_certrtificate.setMinSize(cmp_cert.computeSize(SWT.DEFAULT, SWT.DEFAULT));
  }

  private void createModelTab(Composite parent)
  {
    this.tabModel = new CTabItem(tabFolder, SWT.NONE);
    tabModel.setText(CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.aggregated.drivereditor.form.informationmodel"));
    tabModel.setToolTipText(CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.aggregated.drivereditor.form.tooltip.informationmodel"));
    ScrolledComposite scrolledComposite_1 = new ScrolledComposite(tabFolder, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
    scrolledComposite_1.setExpandVertical(true);
    scrolledComposite_1.setExpandHorizontal(true);
    tabModel.setControl(scrolledComposite_1);
    formToolkit.paintBordersFor(scrolledComposite_1);
    Composite composite_2 = new Composite(scrolledComposite_1, SWT.NONE);
    formToolkit.adapt(composite_2);
    formToolkit.paintBordersFor(composite_2);
    GridLayout gl_composite_2 = new GridLayout(2, false);
    gl_composite_2.horizontalSpacing = 0;
    gl_composite_2.marginWidth = 0;
    gl_composite_2.marginHeight = 0;
    gl_composite_2.verticalSpacing = 0;
    composite_2.setLayout(gl_composite_2);
    Composite composite_3 = new Composite(composite_2, SWT.NONE);
    composite_3.setLayout(new GridLayout(3, false));
    composite_3.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false, 2, 1));
    this.btnCheckModel = new Button(composite_3, SWT.NONE);
    GridData gd_btnNewButton_2 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_btnNewButton_2.widthHint = 48;
    gd_btnNewButton_2.heightHint = 48;
    btnCheckModel.setLayoutData(gd_btnNewButton_2);
    btnCheckModel.setToolTipText(CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.aggregated.drivereditor.form.tooltip.configureinformationmodel"));
    formToolkit.adapt(btnCheckModel, true, true);
    // btnCheckModel.setText("Check");
    btnCheckModel.setImage(AggregatedSharedImages.getImage(AggregatedSharedImages.ICON_CHECK));
    this.btnOpenModel = new Button(composite_3, SWT.NONE);
    GridData gd_btnNewButton_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_btnNewButton_1.widthHint = 48;
    gd_btnNewButton_1.heightHint = 48;
    btnOpenModel.setLayoutData(gd_btnNewButton_1);
    formToolkit.adapt(btnOpenModel, true, true);
    btnOpenModel.setImage(AggregatedSharedImages.getImage(AggregatedSharedImages.ICON_OPEN));
    this.btnClearModel = new Button(composite_3, SWT.NONE);
    GridData gd_btnClearModel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_btnClearModel.widthHint = 48;
    gd_btnClearModel.heightHint = 48;
    btnClearModel.setLayoutData(gd_btnClearModel);
    formToolkit.adapt(btnOpenModel, true, true);
    // btnClearModel.setText("Löschen");
    btnClearModel.setToolTipText(CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.aggregated.drivereditor.form.tooltip.closeinformationmodel"));
    btnClearModel.setImage(AggregatedSharedImages.getImage(AggregatedSharedImages.ICON_CLOSE));
    formToolkit.adapt(composite_3);
    formToolkit.paintBordersFor(composite_3);
    Label lblNewLabel_2 = new Label(composite_2, SWT.NONE);
    lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    formToolkit.adapt(lblNewLabel_2, true, true);
    lblNewLabel_2.setText(CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.aggregated.drivereditor.form.path"));
    lblNewLabel_2.setToolTipText(CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.aggregated.drivereditor.form.tooltip.path"));
    txtInfomodelpath = new Text(composite_2, SWT.BORDER);
    txtInfomodelpath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    formToolkit.adapt(txtInfomodelpath, true, true);
    scrolledComposite_1.setContent(composite_2);
    scrolledComposite_1.setMinSize(composite_2.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    scrolledComposite_1.setMinSize(new Point(447, 187));
  }

  private void createRemoteServerTab(Composite parent)
  {
    this.tabFolder = new CTabFolder(parent, SWT.BORDER);
    tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    // section_model.setClient(tabFolder);
    tabFolder.setSize(474, 312);
    formToolkit.adapt(tabFolder);
    formToolkit.paintBordersFor(tabFolder);
    tabFolder.setSelectionBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_BACKGROUND));
    this.tabRemote = new CTabItem(tabFolder, SWT.NONE);
    tabRemote.setText(CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.aggregated.drivereditor.form.tab.remote.title"));
    tabRemote.setToolTipText(CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.aggregated.drivereditor.form.tab.remote.tooltip.title"));
    ScrolledComposite scrolledComposite = new ScrolledComposite(tabFolder, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
    tabRemote.setControl(scrolledComposite);
    scrolledComposite.setExpandHorizontal(true);
    scrolledComposite.setExpandVertical(true);
    Composite composite = new Composite(scrolledComposite, SWT.NONE);
    composite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
    scrolledComposite.setContent(composite);
    GridLayout gl_composite = new GridLayout(2, false);
    gl_composite.verticalSpacing = 0;
    gl_composite.marginWidth = 0;
    gl_composite.marginHeight = 0;
    gl_composite.horizontalSpacing = 0;
    composite.setLayout(gl_composite);
    Composite composite_1 = new Composite(composite, SWT.NONE);
    GridData gd_composite_1 = new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1);
    gd_composite_1.widthHint = 144;
    composite_1.setLayoutData(gd_composite_1);
    composite_1.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
    GridLayout gl_composite_1 = new GridLayout(3, false);
    gl_composite_1.marginWidth = 0;
    gl_composite_1.verticalSpacing = 0;
    gl_composite_1.marginHeight = 0;
    gl_composite_1.horizontalSpacing = 0;
    composite_1.setLayout(gl_composite_1);
    this.btnCheckRemoteServer = new Button(composite_1, SWT.NONE);
    GridData gd_btnCheck = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_btnCheck.heightHint = 60;
    gd_btnCheck.widthHint = 60;
    btnCheckRemoteServer.setLayoutData(gd_btnCheck);
    formToolkit.adapt(btnCheckRemoteServer, true, true);
    btnCheckRemoteServer.setToolTipText(CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.aggregated.drivereditor.form.tab.remote.tooltip.activate"));
    btnCheckRemoteServer.setImage(AggregatedSharedImages.getImage(AggregatedSharedImages.ICON_CHECK));
    btnNewButton = new Button(composite_1, SWT.NONE);
    btnNewButton.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        // now try to connect to browse an opc ua server
        OPCUAConnectionDialog dialog = new OPCUAConnectionDialog(getSite().getShell());
        int dialogOpenResult = dialog.open();
        if (dialogOpenResult == Dialog.OK)
        {
          // now we take content of connection dialog
          txt_servername.setText(dialog.getDisplayName());
          txt_serverUri.setText(dialog.getEndpoint().getEndpointUrl());
          lst_securityPolicies.setText(dialog.getEndpoint().getSecurityPolicyUri());
          cmb_securityMode.setText(dialog.getEndpoint().getSecurityMode().name());
          txt_securityType.setText(dialog.getIdentity().getClass().getSimpleName());
          txt_username.setText("");
          txt_password.setText("");
          if (dialog.getIdentity() instanceof UserNameIdentityToken)
          {
            txt_username.setText(((UserNameIdentityToken) dialog.getIdentity()).getUserName());
            txt_password.setText(new String(((UserNameIdentityToken) dialog.getIdentity()).getPassword().getValue()));
          }
          setDirty(true);
        }
      }
    });
    GridData gd_btnNewButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_btnNewButton.widthHint = 60;
    gd_btnNewButton.heightHint = 60;
    btnNewButton.setLayoutData(gd_btnNewButton);
    btnNewButton.setText("");
    btnNewButton.setToolTipText(CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.aggregated.drivereditor.form.connect"));
    btnNewButton.setImage(AggregatedSharedImages.getImage(AggregatedSharedImages.ICON_BROWSE));
    lbl_state = new Label(composite_1, SWT.NONE);
    lbl_state.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
    lbl_state.setText("");
    // lbl_state.setImage(AggregatedSharedImages
    // .getImage(AggregatedSharedImages.ICON_BUTTON_SERVER_STOPPED));
    Label lblServername = new Label(composite, SWT.NONE);
    lblServername.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
    lblServername.setAlignment(SWT.RIGHT);
    lblServername.setText(CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.aggregated.drivereditor.form.servername"));
    txt_servername = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
    GridData gd_txt_servername = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
    gd_txt_servername.widthHint = 270;
    txt_servername.setLayoutData(gd_txt_servername);
    Label lblNewLabel = new Label(composite, SWT.NONE);
    lblNewLabel.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
    lblNewLabel.setText(CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.aggregated.drivereditor.form.serveruri"));
    txt_serverUri = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
    txt_serverUri.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    Label lblSecuritypolicy = new Label(composite, SWT.NONE);
    lblSecuritypolicy.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
    lblSecuritypolicy.setText(CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.aggregated.drivereditor.form.securitypolicy"));
    lst_securityPolicies = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
    lst_securityPolicies.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    Label lblSecuritymode = new Label(composite, SWT.NONE);
    lblSecuritymode.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
    lblSecuritymode.setText(CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.aggregated.drivereditor.form.securitymode"));
    cmb_securityMode = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
    cmb_securityMode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    Label lblSecuritytype = new Label(composite, SWT.NONE);
    lblSecuritytype.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
    lblSecuritytype.setText(CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.aggregated.drivereditor.form.securitytype"));
    txt_securityType = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
    txt_securityType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    Label lbl_username = new Label(composite, SWT.NONE);
    lbl_username.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
    lbl_username.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, false, false, 1, 1));
    lbl_username.setText(CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.aggregated.drivereditor.form.username"));
    txt_username = new Text(composite, SWT.BORDER | SWT.READ_ONLY);
    txt_username.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    Label lbl_password = new Label(composite, SWT.NONE);
    lbl_password.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
    lbl_password.setText(CustomString.getString(AggregatedActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.aggregated.drivereditor.form.password"));
    txt_password = new Text(composite, SWT.BORDER | SWT.READ_ONLY | SWT.PASSWORD);
    txt_password.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
  }

  private void fillControls()
  {
    this.device = this.loadOPCUAServerConnection();
    this.txt_servername.setText(device.getUaServerName());
    this.txt_serverUri.setText(device.getUaServerUri());
    this.lst_securityPolicies.setText(device.getUaSecurityPolicy());
    this.cmb_securityMode.setText(device.getUaSecurityMode());
    switch (device.getUaSecurityType())
    {
    case 0:
      this.txt_securityType.setText(AnonymousIdentityToken.class.getSimpleName());
      break;
    case 1:
      this.txt_securityType.setText(UserNameIdentityToken.class.getSimpleName());
      this.txt_username.setText(device.getUaUserName());
      this.txt_password.setText(device.getUaPassword());
      break;
    case 2:
      this.txt_securityType.setText(X509IdentityToken.class.getSimpleName());
      break;
    }
    String infoModel = device.getUaInformationModel();
    if (infoModel != null)
    {
      this.txtInfomodelpath.setText(infoModel);
    }
    AggregatedDriverConfigurationType configType = device.getAggregatedConfigType();
    this.tabFolder.setData(configType);
    switch (configType)
    {
    case remote:
      tabFolder.setSelection(0);
      this.tabRemote.setData(configType);
      handleCheckConfig(configType, this.tabRemote, this.tabModel);
      break;
    case model:
      tabFolder.setSelection(1);
      this.tabModel.setData(configType);
      handleCheckConfig(configType, this.tabModel, this.tabRemote);
      break;
    }
    String driverpath = getEditorInput().getDriverPath();
    IFileSystem fs = getEditorInput().getFileSystem();
    String certFolder = new Path(driverpath).append("cert").toOSString();
    if (!fs.isDir(driverpath) || !fs.isDir(certFolder))
    {
      return;
    }
    ApplicationConfiguration config = new ApplicationConfiguration();
    //
    ClientApplicationConfiguration app = new ClientApplicationConfiguration();
    // app.setDefaultSessionTimeout(1000);
    config.setClientApplicationConfiguration(app);
    config.setApplicationType("Client_1");
    config.setApplicationName("HBS OPC UA Client");
    config.setApplicationUri("urn:User-VAIO:UASDK:HBS-Client");
    config.setProductUri("http://hb-softsolution.com/UASDK/UAClient");
    config.setDefaultSessionTimeout(10001);
    ClientSecurityConfiguration cconf = new ClientSecurityConfiguration();
    cconf.setCommonName("HBS OPC Client");
    cconf.setOrganisation("HB-Softsolution");
    cconf.setPrivateKeyPassword("com.hbsoft.comet.client");
    //cconf.setNonceLength("32");
    String hostName;
    try
    {
      hostName = InetAddress.getLocalHost().getHostName();
      KeyPair keyPair = CertificateUtils.createApplicationInstanceCertificate(cconf.getCommonName(),
          cconf.getOrganisation(), config.getApplicationUri(), 365, hostName);
      // final String cert = new
      // Path(certFolder).append("clientcertificate.der").toOSString();
      // if (!fs.isFile(cert))
      // {
      // return;
      // }
      // final String key = new Path(certFolder).append("clientkey.pfx").toOSString();
      // if (!fs.isFile(key))
      // {
      // return;
      // }
      // KeyPair keyPair = loadCertificate(cert, key);
      fillCertificate(keyPair);
      // this.btnRenew.addSelectionListener(new SelectionAdapter()
      // {
      // @Override
      // public void widgetSelected(SelectionEvent e)
      // {
      // KeyPair kp = loadCertificate(cert, key);
      // newKeyPair = CertGenUtil.openRenewWizard(kp, new File(cert), new File(key));
      // if (newKeyPair == null)
      // {
      // return;
      // }
      // fillCertificate(newKeyPair);
      // setDirty(true);
      // }
      // });
      fillDriverStatus(device.driverstatusflag, device.driverstatusnodes);
    }
    catch (UnknownHostException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (GeneralSecurityException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private KeyPair loadCertificate(String cert, String key)
  {
    // certificate
    Cert certificate = null;
    File fis = null;
    try
    {
      fis = new File(cert);
      certificate = Cert.load(fis);
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (CertificateEncodingException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    catch (CertificateException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    PrivKey privKey = null;
    try
    {
      fis = new File(key);
      privKey = PrivKey.loadFromKeyStore(fis, null);
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
    catch (NoSuchAlgorithmException e)
    {
      e.printStackTrace();
    }
    catch (UnrecoverableKeyException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (CertificateException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (KeyStoreException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    if (certificate == null || privKey == null)
    {
      return null;
    }
    final KeyPair keyPair = new KeyPair(certificate, privKey);
    return keyPair;
  }

  private void fillCertificate(KeyPair keyPair)
  {
    X509Certificate c = keyPair.getCertificate().certificate;
    this.txt_issuer.setText(c.getIssuerX500Principal().getName());
    this.txt_algorithm.setText(c.getSigAlgName());
    this.txt_validafter.setText(c.getNotBefore().toString());
    this.txt_validbefore.setText(c.getNotAfter().toString());
  }

  private AggregatedEditorDevice loadOPCUAServerConnection()
  {
    AggregatedEditorDevice device = new AggregatedEditorDevice();
    String config = getEditorInput().getDriverConfigPath();
    IFileSystem fs = getEditorInput().getFileSystem();
    if (fs != null)
    {
      InputStream stream = null;
      try
      {
        stream = fs.readFile(config);
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
    }
    return device;
  }

  private void addHandler()
  {
    btnCheckRemoteServer.addSelectionListener(
        new CheckConfigButtonListener(AggregatedDriverConfigurationType.remote, tabRemote, tabModel));
    btnCheckModel.addSelectionListener(
        new CheckConfigButtonListener(AggregatedDriverConfigurationType.model, tabModel, tabRemote));
    this.btnOpenModel.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        String path = AggregatedUtil.openModel();
        if (path == null)
        {
          return;
        }
        txtInfomodelpath.setText(path);
        setDirty(true);
      }
    });
    this.btnClearModel.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        txtInfomodelpath.setText("");
        setDirty(true);
      }
    });
    super.addHandler(getEditorInput().getDriverName(), this.device.driverstatusnodes);
  }

  private void updateEditorIfNeeded()
  {
    OPCUADriverEditorInput input = getEditorInput();
    String name = input.getDriverName();
    String path = input.getDriverPath();
    IEditorReference[] editors = getEditorSite().getWorkbenchWindow().getActivePage().getEditorReferences();
    for (IEditorReference editor : editors)
    {
      String editorId = editor.getId();
      // refresh particular aggregateddp editors
      if (!AggregatedDPEditor.ID.equals(editorId))
      {
        continue;
      }
      IEditorPart editorSite = editor.getEditor(true);
      // get editor informations
      if (editorSite instanceof AggregatedDPEditor)
      {
        OPCUADPEditorInput dpInput = ((AggregatedDPEditor) editorSite).getEditorInput();
        String name2 = dpInput.getDriverName();
        String path2 = dpInput.getDriverPath();
        // check for driver and refresh its editor
        if (name != null && name2 != null && name.compareTo(name2) == 0 && path != null && path2 != null
            && path.compareTo(path2) == 0)
        {
          ((AggregatedDPEditor) editorSite).refreshEditor();
        }
      }
    }
  }


  class CheckConfigButtonListener extends SelectionAdapter
  {
    private AggregatedDriverConfigurationType type;
    // private AggregatedDriverConfigurationType othersType;
    private CTabItem tab;
    private CTabItem[] others;

    public CheckConfigButtonListener(AggregatedDriverConfigurationType type, CTabItem tab, CTabItem... others)
    {
      this.type = type;
      this.tab = tab;
      this.others = others;
    }

    @Override
    public void widgetSelected(SelectionEvent e)
    {
      handleCheckConfig(type, tab, others);
    }
  }

  @Override
  public void onFocusRemoteView()
  {
    AggregatedDriverUtil.openDriverView(getEditorInput().getFileSystem(), getEditorInput().getDriverConfigPath(),
        getEditorInput().getDriverPath());
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
