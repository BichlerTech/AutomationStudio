package com.bichler.astudio.connections.wizards;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;

import com.bichler.astudio.filesystem.DataHubFileSystem;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.filesystem.SimpleFileSystem;
import com.bichler.astudio.filesystem.SshFileSystem;
import com.bichler.astudio.connections.ConnectionsActivator;
import com.bichler.astudio.connections.enums.ConnectionType;
import com.bichler.astudio.connections.utils.ConnectionsSharedImages;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.swt.NumericText;

public class HostConnectionWizardPage extends WizardPage
{
  public IFileSystem getFilesystem()
  {
    return filesystem;
  }

  public void setFilesystem(IFileSystem filesystem)
  {
    this.filesystem = filesystem;
  }
  private IWorkbench workbench = null;
  // private IStructuredSelection selection = null;
  private Combo cmb_type;
  private Text txt_host;
  private Text txt_username;
  private IFileSystem filesystem = null;
  private Label lblBgcolor;
  private Text txt_password;
  private Text txt_rootpath;
  private Label lblrootpath;
  private Text txt_connectionName;
  private Text txt_timeout;
  private Button button_1;
  private Label lblFileSeparator;
  private Text txt_fileseparator;

  /**
   * private int connectionType = 0; private String connectionName = ""; private
   * int connectionTimeout = 1000; private String hostName = ""; private String
   * userName = ""; private String password = ""; private String javaPath = "";
   * private String javaArg = "-jar"; private String rootPath = "";
   */
  /**
   * Create the wizard.
   */
  public HostConnectionWizardPage(IWorkbench workbench, IStructuredSelection selection)
  {
    this();
    this.setWorkbench(workbench);
    // this.selection = selection;
    this.filesystem = new SimpleFileSystem();
  }

  /**
   * Create the wizard.
   * 
   * @wbp.parser.constructor
   */
  public HostConnectionWizardPage()
  {
    super("hostconnectionwizardPage");
    setTitle(
        CustomString.getString(ConnectionsActivator.getDefault().RESOURCE_BUNDLE, "HostConnectionWizardPage.Title"));
    setDescription(CustomString.getString(ConnectionsActivator.getDefault().RESOURCE_BUNDLE,
        "HostConnectionWizardPage.Description"));
    ImageDescriptor desc = new ImageDescriptor()
    {
      @Override
      public ImageData getImageData()
      {
        return ConnectionsSharedImages.getImage(ConnectionsSharedImages.ICON_WIZARD_PROJECT_ADD).getImageData();
      }
    };
    setImageDescriptor(desc);
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
    container.setLayout(new GridLayout(3, false));
    Label lblServername = new Label(container, SWT.NONE);
    lblServername.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblServername.setText(CustomString.getString(ConnectionsActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.connections.wizard.host.connection.type"));
    cmb_type = new Combo(container, SWT.READ_ONLY);
    cmb_type.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        if (cmb_type.getSelectionIndex() == 0)
        {
          // change filesystem if required
          if (filesystem != null || filesystem instanceof SshFileSystem)
          {
            IFileSystem fs = filesystem;
            filesystem = new SimpleFileSystem();
            filesystem.setConnectionName(fs.getConnectionName());
            filesystem.setTimeOut(fs.getTimeOut());
            filesystem.setHostName(fs.getHostName());
            filesystem.setUser(fs.getUser());
            filesystem.setPassword(fs.getPassword());
            filesystem.setJavaPath(fs.getJavaPath());
            filesystem.setJavaArg(fs.getJavaArg());
            filesystem.setRootPath(fs.getRootPath());
            filesystem.setTargetFileSeparator(fs.getTargetFileSeparator());
          }
          txt_username.setEnabled(false);
          txt_password.setEnabled(false);
          txt_fileseparator.setEnabled(false);
        }
        else
        {
          if (filesystem != null || filesystem instanceof SimpleFileSystem)
          {
            IFileSystem fs = filesystem;
            filesystem = new DataHubFileSystem();
            filesystem.setConnectionName(fs.getConnectionName());
            filesystem.setTimeOut(fs.getTimeOut());
            filesystem.setHostName(fs.getHostName());
            filesystem.setUser(fs.getUser());
            filesystem.setPassword(fs.getPassword());
            filesystem.setJavaPath(fs.getJavaPath());
            filesystem.setJavaArg(fs.getJavaArg());
            filesystem.setRootPath(fs.getRootPath());
            filesystem.setTargetFileSeparator(fs.getTargetFileSeparator());
          }
          txt_username.setEnabled(true);
          txt_password.setEnabled(true);
          txt_fileseparator.setEnabled(true);
        }
      }
    });
    cmb_type
        .setItems(new String[] { ConnectionType.Virtual_Device.getDescription(), ConnectionType.SSH.getDescription() });
    GridData gd_cmb_type = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
    gd_cmb_type.widthHint = 284;
    cmb_type.setLayoutData(gd_cmb_type);
    cmb_type.select(0);
    new Label(container, SWT.NONE);
    Label lblNewLabel_1 = new Label(container, SWT.NONE);
    lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblNewLabel_1.setText(CustomString.getString(ConnectionsActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.connections.wizard.host.connection.name"));
    txt_connectionName = new Text(container, SWT.BORDER);
    txt_connectionName.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyReleased(KeyEvent e)
      {
        filesystem.setConnectionName(txt_connectionName.getText());
      }
    });
    txt_connectionName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    new Label(container, SWT.NONE);
    Label lblNewLabel_2 = new Label(container, SWT.NONE);
    lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblNewLabel_2.setText(CustomString.getString(ConnectionsActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.connections.wizard.host.connection.timeout"));
    txt_timeout = new NumericText(container, SWT.BORDER);
    txt_timeout.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyReleased(KeyEvent e)
      {
        try
        {
          filesystem.setTimeOut(Integer.parseInt(txt_timeout.getText()));
        }
        catch (Exception ex)
        {
        }
      }
    });
    txt_timeout.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    new Label(container, SWT.NONE);
    Label lblNewLabel = new Label(container, SWT.NONE);
    lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblNewLabel.setText("host:");
    txt_host = new Text(container, SWT.BORDER);
    txt_host.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyReleased(KeyEvent e)
      {
        filesystem.setHostName(txt_host.getText());
      }
    });
    txt_host.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    new Label(container, SWT.NONE);
    Label lblHeight = new Label(container, SWT.NONE);
    lblHeight.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblHeight.setText(CustomString.getString(ConnectionsActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.connections.wizard.host.connection.user"));
    txt_username = new Text(container, SWT.BORDER);
    txt_username.setEnabled(false);
    txt_username.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyReleased(KeyEvent e)
      {
        filesystem.setUser(txt_username.getText());
      }
    });
    txt_username.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
    new Label(container, SWT.NONE);
    lblBgcolor = new Label(container, SWT.NONE);
    lblBgcolor.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblBgcolor.setText(CustomString.getString(ConnectionsActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.connections.wizard.host.connection.password"));
    txt_password = new Text(container, SWT.BORDER | SWT.PASSWORD);
    txt_password.setEnabled(false);
    txt_password.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyReleased(KeyEvent e)
      {
        filesystem.setPassword(txt_password.getText());
      }
    });
    txt_password.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    new Label(container, SWT.NONE);
    lblFileSeparator = new Label(container, SWT.NONE);
    lblFileSeparator.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    lblFileSeparator.setText(CustomString.getString(ConnectionsActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.connections.wizard.host.connection.separator"));
    txt_fileseparator = new Text(container, SWT.BORDER);
    txt_fileseparator.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyReleased(KeyEvent e)
      {
        filesystem.setTargetFileSeparator(txt_fileseparator.getText());
      }
    });
    txt_fileseparator.setEnabled(false);
    txt_fileseparator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    new Label(container, SWT.NONE);
    lblrootpath = new Label(container, SWT.NONE);
    lblrootpath.setText(CustomString.getString(ConnectionsActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.connections.wizard.host.connection.path.opc"));
    lblrootpath.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    txt_rootpath = new Text(container, SWT.BORDER);
    txt_rootpath.addKeyListener(new KeyAdapter()
    {
      @Override
      public void keyReleased(KeyEvent e)
      {
        filesystem.setRootPath(txt_rootpath.getText());
      }
    });
    txt_rootpath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    Label lblHmirootpath = new Label(container, SWT.NONE);
    lblHmirootpath.setText(CustomString.getString(ConnectionsActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.connections.wizard.host.connection.path.hmi"));
    lblHmirootpath.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
    button_1 = new Button(container, SWT.NONE);
    button_1.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        DirectoryDialog dialog = new DirectoryDialog(getShell());
        dialog.open();
      }
    });
    button_1.setText("...");
  }

  public void setEdit()
  {
    setTitle(CustomString.getString(ConnectionsActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.connections.wizard.host.connection.edit.title"));
    setDescription(CustomString.getString(ConnectionsActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.connections.wizard.host.connection.edit.description"));
    if (filesystem instanceof SimpleFileSystem)
      this.cmb_type.select(0);
    else
      this.cmb_type.select(1);
    if (this.cmb_type.getSelectionIndex() == 1)
    {
      this.txt_username.setEnabled(true);
      this.txt_password.setEnabled(true);
      this.txt_fileseparator.setEnabled(true);
    }
    this.txt_connectionName.setText(filesystem.getConnectionName());
    this.txt_timeout.setText(filesystem.getTimeOut() + "");
    this.txt_host.setText(filesystem.getHostName());
    this.txt_username.setText(filesystem.getUser());
    this.txt_password.setText(filesystem.getPassword());
    this.txt_rootpath.setText(filesystem.getRootPath());
    this.txt_fileseparator.setText(filesystem.getTargetFileSeparator());
  }

  public IWorkbench getWorkbench()
  {
    return workbench;
  }

  public void setWorkbench(IWorkbench workbench)
  {
    this.workbench = workbench;
  }
}
