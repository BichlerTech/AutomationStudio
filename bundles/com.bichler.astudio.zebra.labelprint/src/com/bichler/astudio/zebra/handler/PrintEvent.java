package com.bichler.astudio.zebra.handler;

import org.eclipse.swt.widgets.Event;

public class PrintEvent extends Event
{
  private String sn;
  private String x1;
  private String x2;
  private String date;
  private String month;

  public PrintEvent()
  {
    super();
  }

  public PrintEvent(String sn, String x1, String x2, String date, String month)
  {
    this.setSn(sn);
    this.setX1(x1);
    this.setX2(x2);
    this.setDate(date);
    this.setMonth(month);
  }

  public String getSn()
  {
    return sn;
  }

  public void setSn(String sn)
  {
    this.sn = sn;
  }

  public String getX1()
  {
    return x1;
  }

  public void setX1(String x1)
  {
    this.x1 = x1;
  }

  public String getX2()
  {
    return x2;
  }

  public void setX2(String x2)
  {
    this.x2 = x2;
  }

  public String getDate()
  {
    return date;
  }

  public void setDate(String date)
  {
    this.date = date;
  }

  public String getMonth()
  {
    return month;
  }

  public void setMonth(String month)
  {
    this.month = month;
  }
}
