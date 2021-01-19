package com.bichler.astudio.editor.allenbradley.datatype;

public enum DriverModelExtensions
{
  allenbradley_Model("*.csv"), allenbradley_Structure("*.L5X");
  DriverModelExtensions(String extension)
  {
    this.extensions = new String[] { extension };
  }

  DriverModelExtensions(String[] extensions)
  {
    this.extensions = extensions;
  }
  private String[] extensions;

  public String[] getExtensions()
  {
    return extensions;
  }

  public static String[] getDriverModelExtensions(String drivertype)
  {
    String[] ext = null;
    if ("allenbradley".equals(drivertype))
    {
      ext = allenbradley_Model.getExtensions();
    }
    return ext;
  }

  public static String[] getDriverStructExtensions(String type)
  {
    String[] ext = null;
    if ("allenbradley".equals(type))
    {
      ext = allenbradley_Structure.getExtensions();
    }
    return ext;
  }
}
