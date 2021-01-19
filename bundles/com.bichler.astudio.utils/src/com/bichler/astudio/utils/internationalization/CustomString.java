package com.bichler.astudio.utils.internationalization;

import java.util.ResourceBundle;

/**
 * 
 * @author hannes bichler
 * 
 */
public class CustomString
{
  private CustomString()
  {
  }

  public static String getString(ResourceBundle rb, String key)
  {
    try
    {
      return new String(rb.getString(key).getBytes("ISO-8859-1"), "UTF-8").replace("&auml;", "�").replace("&ouml;", "�")
          .replace("&uuml;", "�").replace("&Auml;", "�").replace("&Ouml;", "�").replace("&Uuml;", "�");
    }
    catch (Exception e)
    {
      return key;
    }
  }
}
