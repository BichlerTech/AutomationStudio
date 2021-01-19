package com.hbsoft.studio.systemtray;

import java.io.IOException;

import com.hbsoft.visu.Comet;
import com.hbsoft.visu.Comet_ResourceManager;
import com.hbsoft.visu.IControlListener;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;

public class HMIServerEntry extends HBox
{
  @FXML
  private Button btn_start;
  @FXML
  private Button btn_stop;
  @FXML
  private Label lbl_project;
  @FXML
  private TextArea message;
  @FXML
  private Button btn_refresh;
  private String project = "";
  private Comet comet;
  String protocolType = "http";
  String contextPath = "/Comet";
  int port = 8081;
  private Comet_ResourceManager manager;

  public HMIServerEntry(String project)
  {
    this.project = project;
    try
    {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("HMIServerEntry.fxml"));
      fxmlLoader.setRoot(this);
      fxmlLoader.setController(this);
      fxmlLoader.load();
      lbl_project.setText(project);
      this.setHeight(110);
    }
    catch (IOException e1)
    {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
  }

  @FXML
  private void startServer(ActionEvent event)
  // private void startServer()
  {
    if (comet == null)
    {
      String serverpath = "C:\\HBS\\Programmierung\\Testprojekte\\testplan_neu\\hmi_server\\servers\\" + project;
      manager = Comet_ResourceManager.getManager();
      /**
       * set runtime path to manager
       */
      manager.setRuntimePath(serverpath + "\\..\\..\\runtime");
      // manager.setDefaultProjectName("");
      manager.setRootPath(serverpath);
      manager.loadMainProjectsettings();
      // start the comet environment
      comet = new Comet(manager);
      comet.setProtocolType(manager.getProject().getProtocoltype());
      comet.setContextPath(manager.getProject().getContextPath());
      try
      {
        comet.setPort(Integer.parseInt(manager.getProject().getPort()));
      }
      catch (NumberFormatException ex)
      {
        message.appendText("Portnumer from project setting couldn't be parsed!\nSet default 8080!\n");
        comet.setPort(8080);
      }
      comet.setControlListener(new ControlHandler());
    }
    try
    {
      comet.start();
      btn_start.setDisable(true);
      btn_stop.setDisable(false);
      btn_refresh.setDisable(false);
      message.appendText("Server: " + project + " successfuly started!\n");
    }
    catch (Exception ex)
    {
      message
          .appendText("Server: " + project + " on Port: " + port + " couldn't be started!\n" + ex.getMessage() + "\n");
    }
  }

  @FXML
  private void stopServer(ActionEvent event)
  {
    comet.stop();
    btn_start.setDisable(false);
    btn_stop.setDisable(true);
    btn_refresh.setDisable(true);
    message.appendText("Server: " + project + " successfuly stopped!\n");
  }

  @FXML
  private void reloadConfig(ActionEvent event)
  {
    if (manager != null)
    {
      manager.loadMainProjectsettings();
    }
  }

  public String getProject()
  {
    return project;
  }

  public void setProject(String project)
  {
    this.project = project;
  }


  /**
   * control handler implementation for reacting on client connect and disconnect
   * 
   * @author hannes
   *
   */
  class ControlHandler implements IControlListener
  {
    @Override
    public void clientConnected(String ip)
    {
      javax.swing.SwingUtilities
          .invokeLater(() -> Main.trayIcon.displayMessage("Automation Studio", "HMI Client connected from: " + ip,
              // null
              java.awt.TrayIcon.MessageType.INFO));
      message.appendText("HMI Client connected from: " + ip + "\n");
    }

    @Override
    public void clientDisconnected(String ip)
    {
      javax.swing.SwingUtilities
          .invokeLater(() -> Main.trayIcon.displayMessage("Automation Studio", "HMI Client disconnected from: " + ip,
              // null
              java.awt.TrayIcon.MessageType.INFO));
      message.appendText("HMI Client disconnected from: " + ip + "\n");
    }
  }
}
