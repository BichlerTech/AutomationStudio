package com.bichler.astudio.zebra.labelprint;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.zebra.sdk.comm.Connection;
import com.zebra.sdk.comm.ConnectionException;
import com.zebra.sdk.comm.TcpConnection;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import com.zebra.sdk.printer.ZebraPrinterLanguageUnknownException;

public class ZebraPrintBTech
{
  public final static String LBLName = "label/etikette_btech.prn";
  public final static String PrinterIP = "192.168.8.220";
  
  private ZebraPrintBTech() {
    
  }
  public static void printLabel(String sn, String x1, String x2, String date, String month)
  {
    String file = ZebraActivator.getDefault().getResourceFile(LBLName);
    Connection connection = new TcpConnection(PrinterIP, TcpConnection.DEFAULT_ZPL_TCP_PORT);
    StringBuilder buffer = new StringBuilder();
   
    try (InputStream stream = new FileInputStream(file);
        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader breader = new BufferedReader(reader);)
    {
      // print label for device
      connection.open();
      String line = "";
      while ((line = breader.readLine()) != null)
      {
        buffer.append(line);
      }
      String content = buffer.toString();
      content = content.replace("%SN%", sn);
      content = content.replace("%X1%", x1);
      content = content.replace("%X2%", x2);
      content = content.replace("%DATE%", date);
      ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);
      printer.sendCommand(content);
    }
    catch (ConnectionException | ZebraPrinterLanguageUnknownException | IOException e)
    {
      Logger.getLogger(ZebraPrintHBS.class.getName()).log(Level.SEVERE, e.getMessage());
    }
    // print label for box
    String filebox = ZebraActivator.getDefault().getResourceFile("label/etikette_aussen_btech.prn");
    try (InputStream stream = new FileInputStream(filebox);
        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader breader = new BufferedReader(reader);)
    {
      buffer = new StringBuilder();
      String line = "";
      while ((line = breader.readLine()) != null)
      {
        buffer.append(line);
      }
      String content = buffer.toString();
      content = content.replace("%SN%", sn);
      content = content.replace("%MONTH%", month);
      ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);
      printer.sendCommand(content);
      connection.close();
    }
    catch (ConnectionException | ZebraPrinterLanguageUnknownException | IOException e)
    {
      Logger.getLogger(ZebraPrintHBS.class.getName()).log(Level.SEVERE, e.getMessage());
    }
  }
}
