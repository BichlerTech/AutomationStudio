package com.bichler.astudio.editor.allenbradley.driver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.bichler.astudio.editor.allenbradley.AllenBradleyModelLabelProvider;
import com.bichler.astudio.editor.allenbradley.AllenBradleyModelTreeContentProvider;
import com.bichler.astudio.editor.allenbradley.datenbaustein.AllenBradleyDBResourceManager;
import com.bichler.astudio.editor.allenbradley.model.AbstractAllenBradleyNode;
import com.bichler.astudio.editor.allenbradley.model.AllenBradleyNodeFactory;
import com.bichler.astudio.editor.allenbradley.wizard.AllenBradleyImportWizardPage;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.opcua.driver.enums.DriverConfigProperties;
import com.bichler.astudio.view.drivermodel.browser.listener.IDriverModelListener;
import com.bichler.astudio.view.drivermodel.handler.util.DriverBrowserUtil;

public class AllenBradleyDriverUtil
{
  public static void openDriverView(final IFileSystem filesystem, String driverConfig,
      final AllenBradleyDBResourceManager structManager)
  {
    AllenBradleyModelLabelProvider labelProvider = new AllenBradleyModelLabelProvider();
    AllenBradleyModelTreeContentProvider contentProvider = new AllenBradleyModelTreeContentProvider();
    Map<DriverConfigProperties, IDriverModelListener> listeners = new LinkedHashMap<>();
    // listener for structure
    listeners.put(DriverConfigProperties.pathdriverstruct, new IDriverModelListener()
    {
      @Override
      public void loadModel(String path)
      {
        if (!filesystem.isDir(path))
        {
          return;
        }
        File directory = new File(path);
        String[] files = directory.list(new FilenameFilter()
        {
          @Override
          public boolean accept(File file, String filename)
          {
            if (filename != null)
            {
              int index = filename.lastIndexOf(".");
              String extension = filename.substring(index + 1);
              // allenbradley stucture
              // extension
              if (AllenBradleyImportWizardPage.EXT_L5X.equalsIgnoreCase(extension))
              {
                return true;
              }
            }
            return false;
          }
        });
        Map<String, AbstractAllenBradleyNode> structures = new HashMap<>();
        for (String file : files)
        {
          String csv = directory + File.separator + file;
          if (filesystem.isFile(csv))
          {
            InputStream symboTableFile = null;
            try
            {
              symboTableFile = filesystem.readFile(csv);
              List<AbstractAllenBradleyNode> structs = new AllenBradleyNodeFactory().parseL5X(symboTableFile,
                  structManager);
              for (AbstractAllenBradleyNode arn : structs)
              {
                structures.put(arn.getName(), arn);
              }
            }
            catch (Exception e1)
            {
              Logger.getLogger(getClass().getName()).log(Level.SEVERE, e1.getMessage(), e1);
            }
            finally
            {
              if (symboTableFile != null)
              {
                try
                {
                  symboTableFile.close();
                }
                catch (IOException e1)
                {
                  Logger.getLogger(getClass().getName()).log(Level.SEVERE, e1.getMessage(), e1);
                }
              }
            }
          }
        }
        structManager.addStructures(structures);
      }
    });
    // listener for csv
    listeners.put(DriverConfigProperties.pathdrivermodel, new IDriverModelListener()
    {
      @Override
      public void loadModel(String path)
      {
        if (!filesystem.isFile(path))
        {
          return;
        }
        InputStream symboTableFile;
        try
        {
          symboTableFile = filesystem.readFile(path);
          AbstractAllenBradleyNode root = new AllenBradleyNodeFactory()
              .parseCSV(new BufferedReader(new InputStreamReader(symboTableFile)), structManager);
          DriverBrowserUtil.updateDriverModelView(root);
        }
        catch (IOException e1)
        {
          Logger.getLogger(getClass().getName()).log(Level.SEVERE, e1.getMessage(), e1);
        }
      }
    });
    DriverBrowserUtil.openDriverModelView(contentProvider, labelProvider, filesystem, driverConfig, listeners,
        new AllenBradleyDriverDragSupport(), structManager);
  }
}
