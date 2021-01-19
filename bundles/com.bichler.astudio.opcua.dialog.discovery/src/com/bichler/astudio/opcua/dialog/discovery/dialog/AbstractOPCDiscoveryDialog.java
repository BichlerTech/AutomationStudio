package com.bichler.astudio.opcua.dialog.discovery.dialog;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.prefs.Preferences;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.opcfoundation.ua.builtintypes.ByteString;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.common.ServiceFaultException;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AnonymousIdentityToken;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.EndpointDescription;
import org.opcfoundation.ua.core.UserIdentityToken;
import org.opcfoundation.ua.core.UserNameIdentityToken;
import org.opcfoundation.ua.core.UserTokenPolicy;
import org.opcfoundation.ua.core.UserTokenType;
import org.opcfoundation.ua.core.X509IdentityToken;
import org.opcfoundation.ua.encoding.binary.BinaryEncoder;
import org.opcfoundation.ua.transport.UriUtil;
import org.opcfoundation.ua.transport.security.Cert;

import com.bichler.astudio.images.common.CommonImagesActivator;
import com.bichler.astudio.opcua.dialog.discovery.DialogDiscoveryActivator;
import com.bichler.astudio.opcua.dialog.discovery.provider.ConnectionDialogContentProvider;
import com.bichler.astudio.opcua.dialog.discovery.provider.ConnectionDialogLabelProvider;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.swt.CheckBoxButton;

import opc.client.application.UAClient;
import opc.client.application.UADiscoveryClient;

public abstract class AbstractOPCDiscoveryDialog extends Dialog
{
  private static final String[] FILTER_CERT = { "*.der" };
  // current selection of the tree viewer
  private EndpointDescription endpoint = null;
  private HashMap<UserTokenType, Boolean> allowedTokens = null;
  // connected client instance
  // private UAClient client = null;
  // Viewer for discovered servers and endpoints
  private TreeViewer treeview = null;
  private Label errorMessages = null;
  private Button radio_annonymous = null;
  private Button radio_userpassword = null;
  private Button radio_cert = null;
  private Text txt_username;
  private Text txt_password;
  private Text txt_cert;
  private String displayName;
  private String dialogTitel;
  private Job discoverJob;
  private Button btn_openCert;
  private UserIdentityToken userauth;
  private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
  private Combo combo_protocol;
  private Text txt_port;
  private Combo combo_hostUri;
  /**
   * Preferences! this are our preferences we will be using as the
   * IPreferenceStore is not available yet
   */
  private static Preferences _preferences = Preferences.userNodeForPackage(AbstractOPCDiscoveryDialog.class);
  private static final String _KeyLastUsedLocations = "lastlocations";
  private static final int _MaxHistory = 10;
  private static final String _SplitRow = "#";
  private static final String _SplitChar = "##";
  private List<String> _lastUsedLocations = new ArrayList<>();
  private CheckBoxButton useIP;
  private static final int ENTRIES = 5;

  private void loadPreferences()
  {
    String lastUsed = _preferences.get(_KeyLastUsedLocations, "");
    _lastUsedLocations = new ArrayList<String>();
    if (lastUsed != null)
    {
      String[] all = lastUsed.split(_SplitChar);
      for (String str : all)
      {
        _lastUsedLocations.add(str);
      }
      for (String row : _lastUsedLocations)
      {
        String[] rowEntries = row.split(_SplitRow);
        if (rowEntries.length >= ENTRIES)
        {
          StringBuilder builder = new StringBuilder();
          builder.append(rowEntries[0]);
          builder.append(" ");
          builder.append(rowEntries[1]);
          builder.append("://");
          builder.append(rowEntries[2]);
          builder.append(":");
          builder.append(rowEntries[3]);
          this.combo_hostUri.add(builder.toString());
        }
      }
    }
  }

  private void savePreferences()
  {
    IStructuredSelection selection = (IStructuredSelection) this.treeview.getSelection();
    Object bo = selection.getFirstElement();
    if (bo instanceof EndpointDescription)
    {
      String protocol = this.combo_protocol.getText();
      String host = this.combo_hostUri.getText();
      String port = this.txt_port.getText();
      int securityLevel = -1;
      boolean isIP = this.useIP.isChecked();
      String servername = "";
      if (this.radio_annonymous.getSelection())
      {
        securityLevel = 0;
      }
      else if (this.radio_userpassword.getSelection())
      {
        securityLevel = 1;
      }
      else if (this.radio_cert.getSelection())
      {
        securityLevel = 2;
      }
      ApplicationDescription server = ((EndpointDescription) bo).getServer();
      if (server != null)
      {
        LocalizedText name = server.getApplicationName();
        if (name != null && name.getText() != null)
        {
          servername = name.getText();
        }
      }
      // create current entry
      StringBuilder row = new StringBuilder();
      row.append(servername);
      row.append(_SplitRow);
      row.append(protocol);
      row.append(_SplitRow);
      row.append(host);
      row.append(_SplitRow);
      row.append(port);
      row.append(_SplitRow);
      row.append("" + isIP);
      row.append(_SplitRow);
      row.append("" + securityLevel);
      String newRow = row.toString();
      // add entry
      this._lastUsedLocations.remove(newRow);
      boolean exist = false;
      String existingRow = null;
      for (String entry : this._lastUsedLocations)
      {
        boolean isHost = entry.contains(host);
        boolean isPort = entry.contains(port);
        if (isHost && isPort)
        {
          exist = true;
          existingRow = entry;
          break;
        }
      }
      // host-port combiniation exist
      if (exist)
      {
        this._lastUsedLocations.remove(existingRow);
      }
      this._lastUsedLocations.add(0, newRow);
      // deal with max history
      // deal with the max history
      if (_lastUsedLocations.size() > _MaxHistory)
      {
        List<String> remove = new ArrayList<String>();
        for (int i = _MaxHistory; i < _lastUsedLocations.size(); i++)
        {
          remove.add(_lastUsedLocations.get(i));
        }
        _lastUsedLocations.removeAll(remove);
      }
      // create a string concatenation of all our last used workspaces
      StringBuffer buf = new StringBuffer();
      for (int i = 0; i < _lastUsedLocations.size(); i++)
      {
        buf.append(_lastUsedLocations.get(i));
        if (i != _lastUsedLocations.size() - 1)
        {
          buf.append(_SplitChar);
        }
      }
      // save them onto our preferences
      _preferences.put(_KeyLastUsedLocations, buf.toString());
    }
  }

  /**
   * Constructor
   * 
   * @param parentShell
   */
  public AbstractOPCDiscoveryDialog(Shell parentShell)
  {
    super(parentShell);
    this.allowedTokens = new HashMap<UserTokenType, Boolean>();
    initializeAllowedTokens();
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    super.createButtonsForButtonBar(parent);
    parent.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
  }

  /**
   * Creates the controls for the dialog
   */
  @Override
  protected Control createDialogArea(Composite parent)
  {
    parent.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
    Composite composite = (Composite) super.createDialogArea(parent);
    composite.setLayout(new FillLayout(SWT.HORIZONTAL));
    Form frmNewForm = formToolkit.createForm(composite);
    formToolkit.paintBordersFor(frmNewForm);
    formToolkit.decorateFormHeading(frmNewForm);
    frmNewForm
        .setText(CustomString.getString(DialogDiscoveryActivator.getDefault().RESOURCE_BUNDLE, "dialog.discovery"));
    GridLayoutFactory gridLayoutFactory = GridLayoutFactory.swtDefaults().numColumns(5);// .margins(10,
    // 5);
    gridLayoutFactory.applyTo(frmNewForm.getBody());
    GridDataFactory gridDataFactory = GridDataFactory.fillDefaults().grab(false, false);
    /**
     * DISCOVER URI SECTION
     */
    Label endpointUrlLabel = new Label(frmNewForm.getBody(), SWT.NONE);
    endpointUrlLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
    endpointUrlLabel
        .setText(CustomString.getString(DialogDiscoveryActivator.getDefault().RESOURCE_BUNDLE, "dialog.protocol"));
    gridDataFactory.span(1, 1).applyTo(endpointUrlLabel);
    this.combo_protocol = new Combo(frmNewForm.getBody(), SWT.READ_ONLY);
    String[] transportOptions = { UriUtil.SCHEME_OPCTCP, UriUtil.SCHEME_HTTP, UriUtil.SCHEME_HTTPS };
    for (String uri : transportOptions)
    {
      combo_protocol.add(uri);
    }
    combo_protocol.select(0);
    // text_serverUri.setText("opc.tcp://80.200.242.120:4840");
    gridDataFactory.span(4, 1).applyTo(combo_protocol);
    Label hostUrlLabel = new Label(frmNewForm.getBody(), SWT.NONE);
    hostUrlLabel.setText(CustomString.getString(DialogDiscoveryActivator.getDefault().RESOURCE_BUNDLE, "dialog.host"));
    hostUrlLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
    gridDataFactory.span(1, 1).applyTo(hostUrlLabel);
    this.combo_hostUri = new Combo(frmNewForm.getBody(), SWT.BORDER);
    // text_serverUri.setText("opc.tcp://80.200.242.120:4840");
    gridDataFactory.span(4, 1).applyTo(combo_hostUri);
    this.combo_hostUri.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        int index = ((Combo) e.getSource()).getSelectionIndex();
        if (index < 0 && _lastUsedLocations.size() > index)
        {
          return;
        }
        String row = _lastUsedLocations.get(index);
        String[] entries = row.split(_SplitRow);
        if (entries.length >= ENTRIES)
        {
          String protocol = entries[1];
          String host = entries[2];
          String port = entries[3];
          String isIp = entries[4];
          // set protocol
          String[] items = combo_protocol.getItems();
          if (items != null)
          {
            for (int i = 0; i < items.length; i++)
            {
              if (items[i].equals(protocol))
              {
                combo_protocol.select(i);
                break;
              }
            }
          }
          // set host
          combo_hostUri.setText(host);
          // set port
          txt_port.setText(port);
          // set useip
          useIP.setChecked(Boolean.parseBoolean(isIp));
        }
      }
    });
    Label portLabel = new Label(frmNewForm.getBody(), SWT.NONE);
    portLabel.setText(CustomString.getString(DialogDiscoveryActivator.getDefault().RESOURCE_BUNDLE, "dialog.port"));
    gridDataFactory.span(1, 1).applyTo(portLabel);
    portLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
    this.txt_port = new Text(frmNewForm.getBody(), SWT.BORDER);
    // text_serverUri.setText("opc.tcp://Hannes-VAIO:6006/Test");
    // this.client = OPCUAClient.getInstance();
    // text_serverUri.setText("opc.tcp://80.200.242.120:4840");
    gridDataFactory.span(4, 1).applyTo(txt_port);
    Label useIPLabel = new Label(frmNewForm.getBody(), SWT.NONE);
    useIPLabel.setText(CustomString.getString(DialogDiscoveryActivator.getDefault().RESOURCE_BUNDLE, "dialog.useip"));
    gridDataFactory.span(1, 1).applyTo(useIPLabel);
    useIPLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
    this.useIP = new CheckBoxButton(frmNewForm.getBody(), SWT.CHECK);
    gridDataFactory.span(1, 4).applyTo(useIP);
    useIP.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
    // Label useValidLabel = new Label(frmNewForm.getBody(), SWT.NONE);
    // useValidLabel.setText(CustomString
    // .getString("CONNECTIONDIALOG.USEVALID"));
    // gridDataFactory.span(1, 1).applyTo(useValidLabel);
    // useValidLabel.setBackground(ColorConstants.white);
    // final CheckBoxButton useValid = new CheckBoxButton(
    // frmNewForm.getBody(), SWT.CHECK);
    // gridDataFactory.span(2, 1).applyTo(useValid);
    // useValid.setBackground(ColorConstants.white);
    final Button btn_discover = new Button(frmNewForm.getBody(), SWT.BUTTON1);
    btn_discover
        .setText(CustomString.getString(DialogDiscoveryActivator.getDefault().RESOURCE_BUNDLE, "dialog.discovery"));
    gridDataFactory.span(5, 1).applyTo(btn_discover);
    btn_discover.addSelectionListener(new SelectionListener()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        // errorMessages.setVisible(false);
        String discoveryUrl = combo_protocol.getText() + "://" + combo_hostUri.getText() + ":" + txt_port.getText();
        boolean alwaysIp = useIP.isChecked();
        boolean alwaysValid = isValidDiscovery();
        discover(discoveryUrl, alwaysIp, alwaysValid);
        validate();
      }

      @Override
      public void widgetDefaultSelected(SelectionEvent e)
      {
      }
    });
    combo_protocol.addSelectionListener(new SelectionListener()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        Combo source = (Combo) e.getSource();
        if (UriUtil.SCHEME_OPCTCP.equals(source.getText()))
        {
          btn_discover.setEnabled(true);
        }
        else
        {
          btn_discover.setEnabled(false);
        }
      }

      @Override
      public void widgetDefaultSelected(SelectionEvent e)
      {
        // TODO Auto-generated method stub
      }
    });
    /**
     * DISCOVER URI SECTION END
     */
    /**
     * TREE VIEWER SECTION
     */
    this.treeview = new TreeViewer(frmNewForm.getBody(), SWT.SINGLE | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
    gridDataFactory.grab(true, true).span(5, 1).applyTo(this.treeview.getControl());
    this.treeview.addSelectionChangedListener(new ISelectionChangedListener()
    {
      @Override
      public void selectionChanged(SelectionChangedEvent event)
      {
        TreeSelection treeSelection = (TreeSelection) event.getSelection();
        // selection is an endpoint
        if (treeSelection.getFirstElement() instanceof EndpointDescription)
        {
          endpoint = (EndpointDescription) treeSelection.getFirstElement();
          UserTokenPolicy[] tokens = endpoint.getUserIdentityTokens();
          enableAllowedTokens(tokens);
          tokenSelection();
          validate();
        }
        else
        {
          validate();
        }
      }
    });
    /**
     * TREE VIEWER SECTION END
     */
    /**
     * LOGIN USER AUTHENTIFICATION SECTION
     */
    /*
     * Group authentification = new Group(composite, SWT.SHADOW_ETCHED_IN);
     * authentification.setText("Authentification");
     * authentification.setVisible(true); gridDataFactory.applyTo(authentification);
     */
    this.radio_annonymous = new Button(frmNewForm.getBody(), SWT.RADIO);
    this.radio_annonymous
        .setText(CustomString.getString(DialogDiscoveryActivator.getDefault().RESOURCE_BUNDLE, "dialog.anonymous"));
    this.radio_annonymous.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
    gridDataFactory.grab(false, false).span(5, 1).applyTo(this.radio_annonymous);
    this.radio_userpassword = new Button(frmNewForm.getBody(), SWT.RADIO);
    this.radio_userpassword.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
    this.radio_userpassword
        .setText(CustomString.getString(DialogDiscoveryActivator.getDefault().RESOURCE_BUNDLE, "dialog.login"));
    gridDataFactory.grab(false, false).span(5, 1).applyTo(this.radio_userpassword);
    Label usernameLabel = new Label(frmNewForm.getBody(), SWT.NONE);
    usernameLabel.setText(CustomString.getString(DialogDiscoveryActivator.getDefault().RESOURCE_BUNDLE, "dialog.user"));
    usernameLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
    gridDataFactory.span(2, 1).applyTo(usernameLabel);
    this.txt_username = new Text(frmNewForm.getBody(), SWT.BORDER);
    gridDataFactory.span(3, 1).applyTo(this.txt_username);
    this.txt_username.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
    Label passwordLabel = new Label(frmNewForm.getBody(), SWT.NONE);
    passwordLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
    passwordLabel
        .setText(CustomString.getString(DialogDiscoveryActivator.getDefault().RESOURCE_BUNDLE, "dialog.password"));
    gridDataFactory.span(2, 1).applyTo(passwordLabel);
    this.txt_password = new Text(frmNewForm.getBody(), SWT.BORDER);
    this.txt_password.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
    gridDataFactory.span(3, 1).applyTo(this.txt_password);
    this.radio_cert = new Button(frmNewForm.getBody(), SWT.RADIO);
    this.radio_cert.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
    this.radio_cert
        .setText(CustomString.getString(DialogDiscoveryActivator.getDefault().RESOURCE_BUNDLE, "dialog.security"));
    gridDataFactory.grab(false, false).span(5, 1).applyTo(this.radio_cert);
    Label certLabel = new Label(frmNewForm.getBody(), SWT.NONE);
    certLabel.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
    certLabel
        .setText(CustomString.getString(DialogDiscoveryActivator.getDefault().RESOURCE_BUNDLE, "dialog.certificate"));
    gridDataFactory.span(2, 1).applyTo(certLabel);
    txt_cert = new Text(frmNewForm.getBody(), SWT.BORDER);
    txt_cert.setEditable(false);
    txt_cert.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
    gridDataFactory.span(2, 1).grab(true, false).applyTo(txt_cert);
    GridData gd_btn_openKey = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_btn_openKey.widthHint = 24;
    btn_openCert = new Button(frmNewForm.getBody(), SWT.PUSH);
    Image certImage = CommonImagesActivator.getDefault().getRegisteredImage(CommonImagesActivator.IMG_16,
        CommonImagesActivator.SECURITY_CERTIFICATION);
    btn_openCert.setImage(certImage);
    btn_openCert.setLayoutData(gd_btn_openKey);
    /**
     * LOGIN USER AUTHENTIFICATION SECTION END
     */
    /**
     * LOGIN USER AUTHENtiFICATION LISTENER
     */
    radio_annonymous.addSelectionListener(new SelectionListener()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        tokenSelection();
        validate();
      }

      @Override
      public void widgetDefaultSelected(SelectionEvent e)
      {
      }
    });
    radio_userpassword.addSelectionListener(new SelectionListener()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        tokenSelection();
        validate();
      }

      @Override
      public void widgetDefaultSelected(SelectionEvent e)
      {
        // TODO Auto-generated method stub
      }
    });
    radio_cert.addSelectionListener(new SelectionListener()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        tokenSelection();
        validate();
      }

      @Override
      public void widgetDefaultSelected(SelectionEvent e)
      {
        // TODO Auto-generated method stub
      }
    });
    btn_openCert.addSelectionListener(new SelectionListener()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        FileDialog dlg = new FileDialog(getShell(), SWT.SINGLE);
        // dlg.setFilterNames(FILTER_NAMES);
        dlg.setText(
            CustomString.getString(DialogDiscoveryActivator.getDefault().RESOURCE_BUNDLE, "dialog.certificate"));
        dlg.setFilterExtensions(FILTER_CERT);
        String fn = dlg.open();
        if (fn != null)
        {
          // Append all the selected files. Since getFileNames()
          // returns only
          // the names, and not the path, prepend the path,
          // normalizing
          // if necessary
          StringBuffer buf = new StringBuffer();
          String[] files = dlg.getFileNames();
          for (int i = 0, n = files.length; i < n; i++)
          {
            buf.append(dlg.getFilterPath());
            if (buf.charAt(buf.length() - 1) != File.separatorChar)
            {
              buf.append(File.separatorChar);
            }
            buf.append(files[i]);
            buf.append(" ");
          }
          txt_cert.setText(buf.toString());
        }
      }

      @Override
      public void widgetDefaultSelected(SelectionEvent e)
      {
        // TODO Auto-generated method stub
      }
    });
    txt_username.addModifyListener(new ModifyListener()
    {
      @Override
      public void modifyText(ModifyEvent e)
      {
        validate();
      }
    });
    txt_cert.addModifyListener(new ModifyListener()
    {
      @Override
      public void modifyText(ModifyEvent e)
      {
        validate();
      }
    });
    /**
     * LOGIN USER AUTHENtiFICATION LISTENER END
     */
    // error message label
    this.errorMessages = new Label(frmNewForm.getBody(), SWT.WRAP);
    this.errorMessages.setVisible(false);
    this.errorMessages.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
    this.errorMessages.setForeground(new Color(null, 255, 0, 0));
    this.errorMessages.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 5, 1));
    this.treeview.setContentProvider(new ConnectionDialogContentProvider(errorMessages));
    this.treeview.setLabelProvider(new ConnectionDialogLabelProvider(errorMessages));
    loadPreferences();
    return composite;
  }

  @Override
  protected Control createContents(Composite parent)
  {
    Control c = super.createContents(parent);
    this.getButton(IDialogConstants.OK_ID).setEnabled(false);
    return c;
  }

  @Override
  protected Point getInitialSize()
  {
    return new Point(500, 600);
  }

  @Override
  protected void configureShell(Shell newShell)
  {
    super.configureShell(newShell);
    // newShell.setText(CustomString.getString("NEW_UA_SERVER_DIALOG_TIGLE"));
    this.dialogTitel = CustomString.getString(DialogDiscoveryActivator.getDefault().RESOURCE_BUNDLE, "dialog.title");
  }

  @Override
  protected void okPressed()
  {
    createUserAuthentification();
    savePreferences();
    super.okPressed();
  }

  @Override
  protected void cancelPressed()
  {
    super.cancelPressed();
  }

  protected abstract boolean isValidDiscovery();

  public String getDisplayName()
  {
    return displayName;
  }

  public EndpointDescription getEndpoint()
  {
    return endpoint;
  }

  public UserIdentityToken getIdentity()
  {
    return this.userauth;
  }

  private void createUserAuthentification()
  {
    this.userauth = null;
    // anonymous
    if (radio_annonymous.getSelection())
    {
      this.userauth = new AnonymousIdentityToken();
    }
    // usr pw
    else if (radio_userpassword.getSelection())
    {
      this.userauth = new UserNameIdentityToken(null, txt_username.getText(),
          ByteString.valueOf(txt_password.getText().getBytes(BinaryEncoder.UTF8)), null);
    }
    // cert
    else if (radio_cert.getSelection())
    {
      try
      {
        File certFile = new File(txt_cert.getText());
        if (certFile == null || !certFile.isFile())
        {
          return;
        }
        Cert cert = Cert.load(certFile);
        this.userauth = new X509IdentityToken(null, ByteString.valueOf(cert.certificate.getEncoded()));
      }
      catch (IOException e)
      {
        // IStatus status = new Status(IStatus.ERROR,
        // "ConnectionDialog",
        // e.getMessage(), e);
        this.errorMessages.setText(e.getMessage());
        // Activator.getDefault().getLog().log(status);
        return;
      }
      catch (CertificateEncodingException e)
      {
        // IStatus status = new Status(IStatus.ERROR,
        // "ConnectionDialog",
        // e.getMessage(), e);
        this.errorMessages.setText(e.getMessage());
        // Activator.getDefault().getLog().log(status);
        return;
      }
      catch (CertificateException e)
      {
        this.errorMessages.setText(e.getMessage());
        // Activator.getDefault().getLog().log(status);
        return;
      }
    }
    if (this.userauth == null)
    {
      return;
    }
  }

  /**
   * Discovers the current Server Uri in the text field
   * 
   * @param discoverURI
   *          to discover a server
   * @param alwaysIp
   * @param alwaysValid
   * @param string2
   * @param string
   */
  private void discover(final String discoverURI, final boolean alwaysIp, final boolean alwaysValid)
  {
    // wait to finish
    if (this.discoverJob != null)
    {
      return;
      // this.discoverJob.cancel();
      // this.discoverJob = null;
    }
    ((ConnectionDialogContentProvider) this.treeview.getContentProvider()).setAlwaysIP(alwaysIp);
    ((ConnectionDialogContentProvider) this.treeview.getContentProvider()).setAlwaysValid(alwaysValid);
    ((ConnectionDialogContentProvider) this.treeview.getContentProvider()).setDiscoveryURL(discoverURI);
    this.discoverJob = new Job("DiscoveryJob")
    {
      @Override
      protected IStatus run(IProgressMonitor monitor)
      {
        ApplicationDescription[] discoveryResult = null;
        URI uri = null;
        try
        {
          uri = new URI(discoverURI);
          if (alwaysIp)
          {
            String host = uri.getHost();
            try
            {
              InetAddress address = InetAddress.getByName(host);
              String alwaysIPAdress = address.getHostAddress();
              uri = new URI(uri.toASCIIString().replaceFirst(host, alwaysIPAdress));
            }
            catch (UnknownHostException e)
            {
              return Status.OK_STATUS;
            }
          }
        }
        catch (URISyntaxException e1)
        {
          return Status.OK_STATUS;
        }
        UADiscoveryClient discoveryClient = null;
        try
        {
          discoveryClient = new UAClient().createDiscoveryClient();
          discoveryResult = discoveryClient.findServer(discoverURI);
          final ApplicationDescription[] servers = discoveryResult;
          /*
           * replace "[ipaddress]" string with real ip address
           */
          for (ApplicationDescription desc : servers)
          {
            String[] urls = desc.getDiscoveryUrls();
            for (int i = 0; i < urls.length; i++)
            {
              urls[i] = urls[i].replace("[ipaddress]", uri.getHost());
            }
            desc.setDiscoveryUrls(urls);
          }
          /** did we get any serverser from discovery? */
          if (discoveryResult != null && discoveryResult.length > 0)
          {
            if (treeview != null && !treeview.getControl().isDisposed())
            {
              treeview.getTree().getDisplay().asyncExec(new Runnable()
              {
                @Override
                public void run()
                {
                  treeview.setInput(servers);
                  errorMessages.setText("");
                }
              });
            }
            displayName = discoveryResult[0].getApplicationName().getText();
          }
          // show an error message that no servers has been discoverd
          // successful
          else
          {
            if (!errorMessages.isDisposed())
            {
              final String errorUri = uri.toASCIIString();
              errorMessages.getDisplay().asyncExec(new Runnable()
              {
                @Override
                public void run()
                {
                  errorMessages.setText(CustomString.getString(DialogDiscoveryActivator.getDefault().RESOURCE_BUNDLE,
                      "error.endpoints.noresult") + " " + errorUri + "!");
                  errorMessages.setVisible(true);
                  treeview.setInput(null);
                }
              });
            }
          }
        }
        catch (final Exception e)
        {
          // error discovering a server with the given uri
          if (!errorMessages.isDisposed())
          {
            errorMessages.getDisplay().asyncExec(new Runnable()
            {
              @Override
              public void run()
              {
                treeview.setInput(null);
                String m = " ";
                if (e instanceof ServiceFaultException)
                {
                  m += ((ServiceFaultException) e).getServiceFault().getResponseHeader().getServiceResult().getName()
                      + "! ";
                  m += ((ServiceFaultException) e).getServiceFault().getResponseHeader().getServiceResult()
                      .getDescription() + " ";
                }
                errorMessages.setText(CustomString.getString(DialogDiscoveryActivator.getDefault().RESOURCE_BUNDLE,
                    "error.endpoints.errorresult") + " " + m);
                errorMessages.setVisible(true);
              }
            });
          }
          try
          {
            EndpointDescription[] endpoints = discoveryClient.getEndpoints(alwaysValid, uri.toASCIIString());
            if (endpoints != null && endpoints.length > 0)
            {
              EndpointDescription endpoint = discoveryClient.selectEndpointNone(endpoints);
              discoveryResult = new ApplicationDescription[] { endpoint.getServer() };
              if (discoveryResult != null && discoveryResult.length > 0)
              {
                final ApplicationDescription[] dres = discoveryResult;
                treeview.getTree().getDisplay().asyncExec(new Runnable()
                {
                  @Override
                  public void run()
                  {
                    treeview.setInput(dres);
                  }
                });
                displayName = discoveryResult[0].getApplicationName().getText();
              }
            }
          }
          catch (final Exception ex)
          {
            if (!errorMessages.isDisposed())
            {
              errorMessages.getDisplay().asyncExec(new Runnable()
              {
                @Override
                public void run()
                {
                  treeview.setInput(null);
                  String m = " ";
                  if (ex instanceof ServiceResultException)
                  {
                    m += ((ServiceResultException) ex).getLocalizedMessage();
                  }
                  errorMessages.setText(CustomString.getString(DialogDiscoveryActivator.getDefault().RESOURCE_BUNDLE,
                      "error.endpoints.errorresult") + " " + m);
                  errorMessages.setVisible(true);
                }
              });
            }
          }
        }
        discoverJob = null;
        return Status.OK_STATUS;
      }
    };
    this.discoverJob.setUser(false);
    this.discoverJob.schedule();
  }

  private void enableAllowedTokens(UserTokenPolicy... allowedTokens)
  {
    if (allowedTokens == null || allowedTokens.length <= 0)
    {
      return;
    }
    initializeAllowedTokens();
    for (UserTokenPolicy policy : allowedTokens)
    {
      UserTokenType type = policy.getTokenType();
      this.allowedTokens.put(type, true);
    }
  }

  private void initializeAllowedTokens()
  {
    for (UserTokenType type : UserTokenType.values())
    {
      this.allowedTokens.put(type, false);
    }
  }

  /**
   * Depends on the isEnabled parameter, do the controls disable or enable
   * 
   * @param isEnabled
   *          to disable or enable controls
   * @param controls
   *          to disable or enable
   */
  private void setEnabledControls(boolean isEnabled, Control... controls)
  {
    for (Control c : controls)
    {
      c.setEnabled(isEnabled);
    }
  }

  private void tokenSelection()
  {
    // anonymous
    if (radio_annonymous.getSelection())
    {
      setEnabledControls(false, txt_username, txt_password, txt_cert, btn_openCert);
    }
    // usr pw
    else if (radio_userpassword.getSelection())
    {
      setEnabledControls(false, txt_cert, btn_openCert);
      if (allowedTokens.get(UserTokenType.UserName))
      {
        setEnabledControls(true, txt_username, txt_password);
      }
    }
    // cert
    else if (radio_cert.getSelection())
    {
      setEnabledControls(false, txt_username, txt_password);
      if (allowedTokens.get(UserTokenType.Certificate))
      {
        setEnabledControls(true, txt_cert, btn_openCert);
      }
    }
  }

  private void validate()
  {
    Button btn_OK = getButton(IDialogConstants.OK_ID);
    if (endpoint == null)
    {
      btn_OK.setEnabled(false);
      return;
    }
    boolean isAllowed = false;
    if (radio_annonymous.getSelection())
    {
      isAllowed = allowedTokens.get(UserTokenType.Anonymous);
    }
    else if (radio_userpassword.getSelection())
    {
      isAllowed = allowedTokens.get(UserTokenType.UserName);
      if (txt_username.getText() == null || txt_username.getText().isEmpty())
      {
        isAllowed = false;
      }
    }
    else if (radio_cert.getSelection())
    {
      isAllowed = allowedTokens.get(UserTokenType.Certificate);
      if (txt_cert.getText() == null || txt_cert.getText().isEmpty())
      {
        isAllowed = false;
      }
    }
    btn_OK.setEnabled(isAllowed);
  }
}
