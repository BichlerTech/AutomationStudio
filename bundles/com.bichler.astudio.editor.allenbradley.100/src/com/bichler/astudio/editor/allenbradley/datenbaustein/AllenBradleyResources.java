package com.bichler.astudio.editor.allenbradley.datenbaustein;

import java.util.HashMap;
import java.util.Map;

public class AllenBradleyResources
{
  private Map<String, AllenBradleyDBResourceManager> dbResourceManagers = new HashMap<>();
  private static AllenBradleyResources instance = null;

  public static AllenBradleyResources getInstance()
  {
    if (instance == null)
    {
      instance = new AllenBradleyResources();
    }
    return instance;
  }

  private void addDBResourceManager(String drivername, AllenBradleyDBResourceManager structManager)
  {
    this.dbResourceManagers.put(drivername, structManager);
  }

  public AllenBradleyDBResourceManager getDBResourceManager(String drivername)
  {
    AllenBradleyDBResourceManager manager = this.dbResourceManagers.get(drivername);
    if (manager == null)
    {
      manager = new AllenBradleyDBResourceManager();
      addDBResourceManager(drivername, manager);
    }
    return manager;
  }
}
