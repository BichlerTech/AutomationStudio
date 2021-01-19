package com.bichler.astudio.utils.ui.dialogs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.prefs.Preferences;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.bichler.astudio.utils.constants.HBStudioUtils;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.UtilsUIActivator;

public class PickWorkspaceDialog extends TitleAreaDialog
{
  // the name of the file that tells us that the workspace directory belongs
  // to our application
  public static final String WS_IDENTIFIER = ".csmetadata";
  // you would probably normally define these somewhere in your Preference
  // Constants
  public static final String _KeyWorkspaceRootDir = "wsRootDir";
  private static final String _KeyRememberWorkspace = "wsRemember";
  private static final String _KeyLastUsedWorkspaces = "wsLastUsedWorkspaces";
  // this are our preferences we will be using as the IPreferenceStore is not
  // available yet
  private static Preferences _preferences = Preferences.userNodeForPackage(PickWorkspaceDialog.class);
  // our controls
  private Combo _workspacePathCombo;
  private List<String> _lastUsedWorkspaces;
  private Button _RememberWorkspaceButton;
  // used as separator when we save the last used workspace locations
  private static final String _SplitChar = "#";
  // max number of entries in the history box
  private static final int _MaxHistory = 20;
  private boolean _switchWorkspace;
  // whatever the user picks ends up on this variable
  private String _selectedWorkspaceRootLocation;
  private boolean hasChanged = false;

  /**
   * Creates a new workspace dialog with a specific image as title-area image.
   * 
   * @param switchWorkspace
   *          true if we're using this dialog as a switch workspace dialog
   * @param wizardImage
   *          Image to show
   */
  public PickWorkspaceDialog(boolean switchWorkspace, Image wizardImage)
  {
    super(Display.getDefault().getActiveShell());
    this._switchWorkspace = switchWorkspace;
    if (wizardImage != null)
    {
      setTitleImage(wizardImage);
    }
  }

  @Override
  protected void configureShell(Shell newShell)
  {
    super.configureShell(newShell);
    // set window title
    if (_switchWorkspace)
    {
      newShell.setText(CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
          "com.bichler.astudio.dialog.pickworkspace.title.switch"));
    }
    else
    {
      if (UtilsUIActivator.getDefault().RESOURCE_BUNDLE == null)
      {
        Locale locale = getProductLanguage();
        if (locale == null)
        {
          locale = Locale.ENGLISH;
        }
        UtilsUIActivator.getDefault().setLanguageResourceBundle(locale);
      }
      newShell.setText(CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
          "com.bichler.astudio.dialog.pickworkspace.title.new"));
    }
  }

  public static Locale getProductLanguage()
  {
    Location location = Platform.getInstallLocation();
    URL url = location.getURL();
    Locale productLanguage = null;
    if (url != null)
    {
      try
      {
        URL url2 = FileLocator.toFileURL(url);
        FilenameFilter filter = new FilenameFilter()
        {
          private final String EXTENSION = "ini";

          @Override
          public boolean accept(File parent, String name)
          {
            String ext = new Path(name).getFileExtension();
            if (EXTENSION.equals(ext))
            {
              return true;
            }
            return false;
          }
        };
        File installFile = new File(url2.getFile());
        File[] children = installFile.listFiles(filter);
        if (children != null && children.length > 0)
        {
          File csOneIni = children[0];
          BufferedReader reader = null;
          try
          {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(csOneIni)));
            StringBuilder buffer = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null)
            {
              if ("-nl".equals(line))
              {
                // append nl line
                appendLine(buffer, line);
                line = reader.readLine();
                productLanguage = new Locale(line);
              }
              appendLine(buffer, line);
            }
          }
          finally
          {
            if (reader != null)
            {
              reader.close();
            }
          }
        }
        if (productLanguage == null)
        {
          FilenameFilter filter2 = new FilenameFilter()
          {
            private final String EXTENSION = "lhb";

            @Override
            public boolean accept(File parent, String name)
            {
              String ext = new Path(name).getFileExtension();
              if (EXTENSION.equals(ext))
              {
                return true;
              }
              return false;
            }
          };
          File[] language = installFile.listFiles(filter2);
          if (language != null && language.length > 0)
          {
            File languageFile = language[0];
            String lang = languageFile.getName();
            int pos = lang.lastIndexOf(".");
            productLanguage = new Locale(lang.substring(0, pos));
          }
        }
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    // default locale english
    if (productLanguage == null)
    {
      productLanguage = Locale.ENGLISH;
    }
    return productLanguage;
  }

  public static void appendLine(StringBuilder buffer, String line)
  {
    // add to buffer
    buffer.append(line);
    buffer.append("\n");
  }

  /**
   * Returns whether the user selected "remember workspace" in the preferences
   * 
   * @return
   */
  public static boolean isRememberWorkspace()
  {
    return _preferences.getBoolean(_KeyRememberWorkspace, false);
  }

  /**
   * Returns the last set workspace directory from the preferences
   * 
   * @return null if none
   */
  public static String getLastSetWorkspaceDirectory()
  {
    return _preferences.get(_KeyWorkspaceRootDir, null);
  }

  @Override
  protected Control createDialogArea(Composite parent)
  {
    // parent.setLayout(new FillLayout());
    setTitle(CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.dialog.pickworkspace.title.new"));
    setMessage(CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE, "com.bichler.astudio.noworkspace"));
    try
    {
      Composite inner = new Composite(parent, SWT.BORDER);
      // double[][] layout = new double[][] { { 5,
      // LatticeLayout.PREFERRED, 5, 250, 5, LatticeLayout.PREFERRED, 5 },
      // { 5, LatticeLayout.PREFERRED, 5, LatticeLayout.PREFERRED, 40 } };
      // inner.setLayout(new LatticeLayout(layout));
      inner.setLayout(new GridLayout(3, false));
      // inner.setLayoutData(
      // new GridData(GridData.FILL_VERTICAL | GridData.VERTICAL_ALIGN_END
      // | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));
      inner.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
      // label on left
      CLabel label = new CLabel(inner, SWT.NONE);
      label.setText(CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
          "com.bichler.astudio.dialog.pickworkspace.swt.path.label"));
      // label.setLayoutData(new LatticeData("1, 1"));
      label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
      // combo in middle
      _workspacePathCombo = new Combo(inner, SWT.BORDER);
      _workspacePathCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
      // _workspacePathCombo.setLayoutData(new LatticeData("3, 1"));
      label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
      String wsRoot = _preferences.get(_KeyWorkspaceRootDir, "");
      if (wsRoot == null || wsRoot.length() == 0)
      {
        wsRoot = getWorkspacePathSuggestion();
      }
      _workspacePathCombo.setText(wsRoot == null ? "" : wsRoot);
      // CommonImagesActivator cma =
      // com.bichler.astudio.images.common.CommonImagesActivator.getDefault();
      // browse button on right
      Button browse = new Button(inner, SWT.PUSH);
      browse.setToolTipText(CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
          "com.bichler.astudio.dialog.pickworkspace.swt.path.browse"));
      browse.setImage(UtilsUIActivator.getDefault().getImage("icons/look.png"));
      browse.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
      browse.addListener(SWT.Selection, new Listener()
      {
        @Override
        public void handleEvent(Event event)
        {
          DirectoryDialog dd = new DirectoryDialog(getParentShell());
          dd.setText(CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
              "com.bichler.astudio.dialog.pickworkspace.swt.path.browse.title"));
          dd.setMessage(CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
              "com.bichler.astudio.dialog.pickworkspace.info"));
          dd.setFilterPath(_workspacePathCombo.getText());
          String pick = dd.open();
          if (pick == null && _workspacePathCombo.getText().length() == 0)
          {
            setMessage(CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
                "com.bichler.astudio.dialog.pickworkspace.error"), IMessageProvider.ERROR);
          }
          else if (pick != null)
          {
            setMessage(
                CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE, "com.bichler.astudio.noworkspace"));
            _workspacePathCombo.setText(pick);
          }
        }
      });
      // checkbox below
      _RememberWorkspaceButton = new Button(inner, SWT.CHECK);
      _RememberWorkspaceButton.setText(CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
          "com.bichler.astudio.dialog.pickworkspace.swt.path.remember"));
      // _RememberWorkspaceButton.setLayoutData(new LatticeData("3, 3, 5,
      // 3"));
      _RememberWorkspaceButton.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 3, 1));
      _RememberWorkspaceButton.setSelection(_preferences.getBoolean(_KeyRememberWorkspace, false));
      new Label(inner, SWT.NONE);
      new Label(inner, SWT.NONE);
      new Label(inner, SWT.NONE);
      String lastUsed = _preferences.get(_KeyLastUsedWorkspaces, "");
      _lastUsedWorkspaces = new ArrayList<String>();
      if (lastUsed != null)
      {
        String[] all = lastUsed.split(_SplitChar);
        for (String str : all)
          _lastUsedWorkspaces.add(str);
      }
      for (String last : _lastUsedWorkspaces)
        _workspacePathCombo.add(last);
      return inner;
    }
    catch (Exception err)
    {
      err.printStackTrace();
      return null;
    }
  }

  /**
   * Returns whatever path the user selected in the dialog.
   * 
   * @return Path
   */
  public String getSelectedWorkspaceLocation()
  {
    return _selectedWorkspaceRootLocation;
  }

  // suggests a path based on the user.home/temp directory location
  private String getWorkspacePathSuggestion()
  {
    StringBuffer buf = new StringBuffer();
    String uHome = System.getProperty("user.home");
    if (uHome == null)
    {
      uHome = "c:" + File.separator + "temp";
    }
    buf.append(uHome);
    buf.append(File.separator);
    buf.append("workspace");
    // buf.append("_Workspace");
    return buf.toString();
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent)
  {
    // clone workspace needs a lot of checks    
    createButton(parent, IDialogConstants.OK_ID, CustomString.getString(
    		UtilsUIActivator.getDefault().RESOURCE_BUNDLE, "dialog.btn.ok"), true);
    createButton(parent, IDialogConstants.CANCEL_ID, CustomString.getString(
    		UtilsUIActivator.getDefault().RESOURCE_BUNDLE, "dialog.btn.cancel"), false);
  }

  private void createCloneButton(Composite parent) {
	  Button clone = createButton(parent, IDialogConstants.IGNORE_ID, CustomString.getString(
			  UtilsUIActivator.getDefault().RESOURCE_BUNDLE, "com.bichler.astudio.dialog.pickworkspace.swt.clone"), false);
		    clone.addListener(SWT.Selection, new Listener()
		    {
		      @Override
		      public void handleEvent(Event arg0)
		      {
		        try
		        {
		          String txt = _workspacePathCombo.getText();
		          File workspaceDirectory = new File(txt);
		          if (!workspaceDirectory.exists())
		          {
		            // The currently entered workspace path does not exist.
		            // Please enter a valid path.
		            MessageDialog.openError(Display.getDefault().getActiveShell(),
		                CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
		                    "com.bichler.astudio.dialog.pickworkspace.swt.clone.error.title"),
		                CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
		                    "com.bichler.astudio.dialog.pickworkspace.swt.clone.error.exist"));
		            return;
		          }
		          if (!workspaceDirectory.canRead())
		          {
		            // The currently entered workspace path is not readable.
		            // Please check file system permissions.
		            MessageDialog.openError(Display.getDefault().getActiveShell(),
		                CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
		                    "com.bichler.astudio.dialog.pickworkspace.swt.clone.error.title"),
		                CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
		                    "com.bichler.astudio.dialog.pickworkspace.swt.clone.error.readable"));
		            return;
		          }
		          // check for workspace file (empty indicator that it's a
		          // workspace)
		          File wsFile = new File(txt + File.separator + WS_IDENTIFIER);
		          if (!wsFile.exists())
		          {
		            // The currently entered workspace path does not contain
		            // a valid workspace.
		            MessageDialog.openError(Display.getDefault().getActiveShell(),
		                CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
		                    "com.bichler.astudio.dialog.pickworkspace.swt.clone.error.title"),
		                CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
		                    "com.bichler.astudio.dialog.pickworkspace.swt.clone.error.invalid"));
		            return;
		          }
		          DirectoryDialog dd = new DirectoryDialog(Display.getDefault().getActiveShell());
		          dd.setFilterPath(txt);
		          String directory = dd.open();
		          if (directory == null)
		          {
		            return;
		          }
		          File targetDirectory = new File(directory);
		          if (targetDirectory.getAbsolutePath().equals(workspaceDirectory.getAbsolutePath()))
		          {
		            // Source and target workspaces are the same
		            MessageDialog.openError(Display.getDefault().getActiveShell(),
		                CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
		                    "com.bichler.astudio.dialog.pickworkspace.swt.clone.error.title"),
		                CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
		                    "com.bichler.astudio.dialog.pickworkspace.swt.clone.error.samesource"));
		            return;
		          }
		          // recursive check, if new directory is a subdirectory of
		          // our workspace, that's a big no-no or we'll
		          // create directories forever
		          if (isTargetSubdirOfDir(workspaceDirectory, targetDirectory))
		          {
		            // Target folder is a subdirectory of the current
		            // workspace
		            MessageDialog.openError(Display.getDefault().getActiveShell(),
		                CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
		                    "com.bichler.astudio.dialog.pickworkspace.swt.clone.error.title"),
		                CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
		                    "com.bichler.astudio.dialog.pickworkspace.swt.clone.error.ischild"));
		            return;
		          }
		          try
		          {
		            copyFiles(workspaceDirectory, targetDirectory);
		          }
		          catch (Exception err)
		          {
		            // There was an error cloning the workspace:
		            MessageDialog.openError(Display.getDefault().getActiveShell(),
		                CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
		                    "com.bichler.astudio.dialog.pickworkspace.swt.clone.error.title"),
		                CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
		                    "com.bichler.astudio.dialog.pickworkspace.swt.clone.error.copy") + " " + err.getMessage());
		            return;
		          }
		          // Would you like to set the newly cloned workspace to be
		          // the active one?
		          boolean setActive = MessageDialog.openConfirm(Display.getDefault().getActiveShell(),
		              CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
		                  "com.bichler.astudio.dialog.pickworkspace.title.copy"),
		              CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
		                  "com.bichler.astudio.dialog.pickworkspace.swt.clone.info.cloneactive"));
		          if (setActive)
		          {
		            _workspacePathCombo.setText(directory);
		          }
		        }
		        catch (Exception err)
		        {
		          // There was an internal error, please check the logs
		          MessageDialog.openError(Display.getDefault().getActiveShell(),
		              CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
		                  "com.bichler.astudio.dialog.pickworkspace.swt.clone.error.title"),
		              CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
		                  "com.bichler.astudio.dialog.pickworkspace.swt.clone.error.internal"));
		          err.printStackTrace();
		        }
		      }
		    });
  }
  
  // checks whether a target directory is a subdirectory of ourselves
  private boolean isTargetSubdirOfDir(File source, File target)
  {
    List<File> subdirs = new ArrayList<File>();
    getAllSubdirectoriesOf(source, subdirs);
    return subdirs.contains(target);
  }

  // helper for above
  private void getAllSubdirectoriesOf(File target, List<File> buffer)
  {
    File[] files = target.listFiles();
    if (files == null || files.length == 0)
      return;
    for (File f : files)
    {
      if (f.isDirectory())
      {
        buffer.add(f);
        getAllSubdirectoriesOf(f, buffer);
      }
    }
  }

  /**
   * This function will copy files or directories from one location to another.
   * note that the source and the destination must be mutually exclusive. This
   * function can not be used to copy a directory to a sub directory of itself.
   * The function will also have problems if the destination files already exist.
   * 
   * @param src
   *          -- A File object that represents the source for the copy
   * @param dest
   *          -- A File object that represents the destination for the copy.
   * @throws IOException
   *           if unable to copy.
   */
  public static void copyFiles(File src, File dest) throws IOException
  {
    // Check to ensure that the source is valid...
    if (!src.exists())
    {
      throw new IOException("Kann keine Quelle finden: " + src.getAbsolutePath());
    }
    else if (!src.canRead())
    { // check to ensure we have rights to the
      // source...
      throw new IOException("Kein Lesezugriff: " + src.getAbsolutePath() + ". Überprüfen sie die Dateiberechtigung.");
    }
    // is this a directory copy?
    if (src.isDirectory())
    {
      if (!dest.exists())
      { // does the destination already exist?
        // if not we need to make it exist if possible (note this is
        // mkdirs not mkdir)
        if (!dest.mkdirs())
        {
          throw new IOException("Kann Ordner nicht anlegen: " + dest.getAbsolutePath());
        }
      }
      // get a listing of files...
      String list[] = src.list();
      // copy all the files in the list.
      for (int i = 0; i < list.length; i++)
      {
        File dest1 = new File(dest, list[i]);
        File src1 = new File(src, list[i]);
        copyFiles(src1, dest1);
      }
    }
    else
    {
      // This was not a directory, so lets just copy the file
      FileInputStream fin = null;
      FileOutputStream fout = null;
      byte[] buffer = new byte[4096]; // Buffer 4K at a time (you can
      // change this).
      int bytesRead;
      try
      {
        // open the files for input and output
        fin = new FileInputStream(src);
        fout = new FileOutputStream(dest);
        // while bytesRead indicates a successful read, lets write...
        while ((bytesRead = fin.read(buffer)) >= 0)
        {
          fout.write(buffer, 0, bytesRead);
        }
      }
      catch (IOException e)
      { // Error copying file...
        IOException wrapper = new IOException(
            "Kann die Datein nicht kopieren: " + src.getAbsolutePath() + "zu" + dest.getAbsolutePath());
        wrapper.initCause(e);
        wrapper.setStackTrace(e.getStackTrace());
        throw wrapper;
      }
      finally
      { // Ensure that the files are closed (if they were open).
        if (fin != null)
        {
          fin.close();
        }
        if (fout != null)
        {
          fout.close();
        }
      }
    }
  }

  @Override
  protected void okPressed()
  {
    final String str = _workspacePathCombo.getText();
    if (str.length() == 0)
    {
      setMessage(CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
          "com.bichler.astudio.dialog.pickworkspace.error"), IMessageProvider.ERROR);
      return;
    }
    String ret = checkWorkspaceDirectory(getParentShell(), str, true, true);
    if (ret != null)
    {
      setMessage(ret, IMessageProvider.ERROR);
      return;
    }
    // save it so we can show it in combo later
    _lastUsedWorkspaces.remove(str);
    if (!_lastUsedWorkspaces.contains(str))
    {
      _lastUsedWorkspaces.add(0, str);
    }
    // deal with the max history
    if (_lastUsedWorkspaces.size() > _MaxHistory)
    {
      List<String> remove = new ArrayList<String>();
      for (int i = _MaxHistory; i < _lastUsedWorkspaces.size(); i++)
      {
        remove.add(_lastUsedWorkspaces.get(i));
      }
      _lastUsedWorkspaces.removeAll(remove);
    }
    // create a string concatenation of all our last used workspaces
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < _lastUsedWorkspaces.size(); i++)
    {
      buf.append(_lastUsedWorkspaces.get(i));
      if (i != _lastUsedWorkspaces.size() - 1)
      {
        buf.append(_SplitChar);
      }
    }
    // save them onto our preferences
    _preferences.putBoolean(_KeyRememberWorkspace, _RememberWorkspaceButton.getSelection());
    _preferences.put(_KeyLastUsedWorkspaces, buf.toString());
    String wsRoot = _preferences.get(_KeyWorkspaceRootDir, "");
    this.hasChanged = !str.equals(wsRoot);
    // now create it
    boolean ok = checkAndCreateWorkspaceRoot(str);
    if (!ok)
    {
      // The workspace could not be created, please check the error
      // log
      setMessage(CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
          "com.bichler.astudio.dialog.pickworkspace.workspace.error"));
      return;
    }
    // here we set the location so that we can later fetch it again
    _selectedWorkspaceRootLocation = str;
    // and on our preferences as well
    _preferences.put(_KeyWorkspaceRootDir, str);
    finishChangeWorkspace(getParentShell(), str, true);
    super.okPressed();
  }

  public boolean hasChanged()
  {
    return this.hasChanged;
  }

  public static void finishChangeWorkspace(final Shell shell, final String path, boolean longTime)
  {
    if (longTime)
    {
      ProgressMonitorDialog pDialog = new ProgressMonitorDialog(shell);
      try
      {
        pDialog.run(true, false, new IRunnableWithProgress()
        {
          @Override
          public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
          {
            try
            {
              monitor.beginTask(CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
                  "com.bichler.astudio.dialog.pickworkspace.workspace.monitor"), IProgressMonitor.UNKNOWN);
              Map<String, Map<String, String>> csWorkspacePreferences = HBStudioUtils.createWorkspaceRuntime(path);
              HBStudioUtils.initWorkspacePreferences(csWorkspacePreferences);
            }
            finally
            {
              monitor.done();
            }
          }
        });
      }
      catch (InvocationTargetException e)
      {
        e.printStackTrace();
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      Map<String, Map<String, String>> csWorkspacePreferences = HBStudioUtils.createWorkspaceRuntime(path);
      HBStudioUtils.initWorkspacePreferences(csWorkspacePreferences);
    }
  }

  /**
   * Ensures a workspace directory is OK in regards of reading/writing, etc. This
   * method will get called externally as well.
   * 
   * @param parentShell
   *          Shell parent shell
   * @param workspaceLocation
   *          Directory the user wants to use
   * @param askCreate
   *          Whether to ask if to create the workspace or not in this location if
   *          it does not exist already
   * @param fromDialog
   *          Whether this method was called from our dialog or from somewhere
   *          else just to check a location
   * @return null if everything is ok, or an error message if not
   */
  public static String checkWorkspaceDirectory(Shell parentShell, String workspaceLocation, boolean askCreate,
      boolean fromDialog)
  {
    File f = new File(workspaceLocation);
    if (!f.exists())
    {
      if (askCreate)
      {
        // The directory does not exist. Would you like to create it?
        boolean create = MessageDialog.openConfirm(parentShell,
            CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
                "com.bichler.astudio.dialog.pickworkspace.workspace.check.info.new.title"),
            CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
                "com.bichler.astudio.dialog.pickworkspace.workspace.check.info.new.message"));
        if (create)
        {
          try
          {
            f.mkdirs();
            File wsDot = new File(workspaceLocation + File.separator + WS_IDENTIFIER);
            wsDot.createNewFile();
          }
          catch (Exception err)
          {
            // Error creating directories, please check folder
            // permissions
            return CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
                "com.bichler.astudio.dialog.pickworkspace.workspace.error.check.create");
          }
        }
        if (!f.exists())
        {
          // The selected directory does not exist
          return CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
              "com.bichler.astudio.dialog.pickworkspace.workspace.error.check.exist");
        }
      }
    }
    if (!f.canRead())
    {
      return CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
          "com.bichler.astudio.dialog.pickworkspace.workspace.error.check.readable");
    }
    if (!f.isDirectory())
    {
      return CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
          "com.bichler.astudio.dialog.pickworkspace.workspace.error.check.directory");
    }
    File wsTest = new File(workspaceLocation + File.separator + WS_IDENTIFIER);
    if (fromDialog)
    {
      if (!wsTest.exists())
      {
        boolean create = MessageDialog.openConfirm(parentShell,
            CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
                "com.bichler.astudio.dialog.pickworkspace.workspace.check.info.location.title"),
            CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
                "com.bichler.astudio.dialog.pickworkspace.workspace.check.info.location.message"));
        if (create)
        {
          try
          {
            f.mkdirs();
            File wsDot = new File(workspaceLocation + File.separator + WS_IDENTIFIER);
            wsDot.createNewFile();
          }
          catch (Exception err)
          {
            return CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
                "com.bichler.astudio.dialog.pickworkspace.workspace.error.check.create");
          }
        }
        else
        {
          return CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
              "com.bichler.astudio.dialog.pickworkspace.workspace.check.info.select");
        }
        if (!wsTest.exists())
        {
          return CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
              "com.bichler.astudio.dialog.pickworkspace.workspace.error.check.createexist");
        }
        return null;
      }
    }
    else
    {
      if (!wsTest.exists())
      {
        return CustomString.getString(UtilsUIActivator.getDefault().RESOURCE_BUNDLE,
            "com.bichler.astudio.dialog.pickworkspace.workspace.error.check.workspace");
      }
    }
    return null;
  }

  /**
   * Checks to see if a workspace exists at a given directory string, and if not,
   * creates it. Also puts our identifying file inside that workspace.
   * 
   * @param wsRoot
   *          Workspace root directory as string
   * @return true if all checks and creations succeeded, false if there was a
   *         problem
   */
  public static boolean checkAndCreateWorkspaceRoot(String wsRoot)
  {
    try
    {
      File fRoot = new File(wsRoot);
      if (!fRoot.exists())
        return false;
      File dotFile = new File(wsRoot + File.separator + PickWorkspaceDialog.WS_IDENTIFIER);
      if (!dotFile.exists() && !dotFile.createNewFile())
        return false;
      return true;
    }
    catch (Exception err)
    {
      // as it might need to go to some other error log too
      err.printStackTrace();
      return false;
    }
  }
}
