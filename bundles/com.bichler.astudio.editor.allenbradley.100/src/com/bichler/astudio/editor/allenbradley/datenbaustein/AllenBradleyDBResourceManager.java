package com.bichler.astudio.editor.allenbradley.datenbaustein;

import java.util.HashMap;
import java.util.Map;

import com.bichler.astudio.editor.allenbradley.model.AbstractAllenBradleyNode;
import com.bichler.astudio.view.drivermodel.handler.util.IDriverStructResourceManager;

public class AllenBradleyDBResourceManager implements IDriverStructResourceManager
{
  private static Map<String, AbstractAllenBradleyNode> allenbradley_db_structures = new HashMap<>();

  public AllenBradleyDBResourceManager()
  {
  }

  // public static allenbradleyDBResourceManager getInstance() {
  // if (instance == null) {
  // instance = new allenbradleyDBResourceManager();
  // }
  // return instance;
  // }
  public void addStructures(Map<String, AbstractAllenBradleyNode> structures)
  {
    allenbradley_db_structures.putAll(structures);
  }

  public AbstractAllenBradleyNode getStructure(String key)
  {
    return allenbradley_db_structures.get(key);
  }
}
