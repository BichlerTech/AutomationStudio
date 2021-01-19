package com.bichler.astudio.utils.opcua;

public class OpenServerParameter
{
  public static final String PARAMETER_ID = "startserverextern";
  private String servername = "";
  private String configuration = "";
  private String certificates = "";
  private String localization = "";
  private String model = "";

  public OpenServerParameter()
  {
  }

  public String getServername()
  {
    return servername;
  }

  public String getConfiguration()
  {
    return configuration;
  }

  public String getCertificates()
  {
    return certificates;
  }

  public String getLocalization()
  {
    return localization;
  }

  public String getModel()
  {
    return model;
  }

  public void setServername(String servername)
  {
    this.servername = servername;
  }

  public void setConfiguration(String configuration)
  {
    this.configuration = configuration;
  }

  public void setCertificates(String certificates)
  {
    this.certificates = certificates;
  }

  public void setLocalization(String localization)
  {
    this.localization = localization;
  }

  public void setModel(String model)
  {
    this.model = model;
  }
}
