package com.bichler.astudio.systemtray;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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

public class IECProjectEntry extends HBox
{
  @FXML
  private HBox topbox;
  @FXML
  private Button btn_start;
  @FXML
  private Button btn_stop;
  @FXML
  private Label lbl_project;
  @FXML
  private TextArea message;
  @FXML
  private Button btn_browser;
  private String project = "";
  private Comet comet;
  private String serverpath = "";
  private Comet_ResourceManager manager;

  public IECProjectEntry(String serverpath, String project)
  {
    this.serverpath = serverpath;
    this.project = project;
    try
    {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("OPCUAServerEntry.fxml"));
      fxmlLoader.setRoot(this);
      fxmlLoader.setController(this);
      fxmlLoader.load();
      lbl_project.setText(project);
      this.setPrefHeight(96.0);
      topbox.setMinHeight(96.0);
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
      serverpath = serverpath + project;
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
      btn_browser.setDisable(false);
      message.appendText("Server: " + project + " successfuly started!\n");
    }
    catch (Exception ex)
    {
      message.appendText("Server: " + project + " on Port: " + manager.getProject().getPort()
          + " couldn't be started!\n" + ex.getMessage() + "\n");
    }
  }

  @FXML
  private void stopServer(ActionEvent event)
  {
    comet.stop();
    btn_start.setDisable(false);
    btn_stop.setDisable(true);
    btn_browser.setDisable(true);
    message.appendText("Server: " + project + " successfuly stopped!\n");
  }

  @FXML
  private void browse(ActionEvent event)
  {
    try
    {
      if (Desktop.isDesktopSupported())
      {
        Desktop.getDesktop().browse(new URI(manager.getProject().getProtocoltype() + "://localhost:"
            + manager.getProject().getPort() + manager.getProject().getContextPath()));
      }
    }
    catch (IOException | URISyntaxException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
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
