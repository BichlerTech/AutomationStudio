package com.bichler.astudio.log.server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ASLogHandler implements Runnable
{
  /** socket to receive message */
  private Socket socket = null;

  /**
   * Handler for incoming log4j messages.
   * 
   * @param Socket
   *          Socket for incoming message.
   */
  public ASLogHandler(Socket socket)
  {
    this.socket = socket;
  }

  @Override
  public void run()
  {
    ObjectInputStream input = null;
    try
    {
      BufferedInputStream bis = new BufferedInputStream(this.socket.getInputStream());
      BufferedReader reader = new BufferedReader(new InputStreamReader(bis));
     // input = new ObjectInputStream(bis);
      // read message
      while (!this.socket.isClosed())
      {
        System.out.println(reader.readLine());
//        LoggingEvent event = (LoggingEvent) input.readObject();
//        send(event);
      }
    }
    catch (SocketException e)
    {
      Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
//    catch (ClassNotFoundException e)
//    {
//      e.printStackTrace();
//    }
    finally
    {
      if (input != null)
      {
        try
        {
          input.close();
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
    }
  }
}
