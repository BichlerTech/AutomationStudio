package com.bichler.astudio.systemtray;

import java.io.IOException;

import com.hbsoft.visu.Comet_ResourceManager;
import com.hbsoft.visu.IControlListener;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class ConfigEntry extends HBox
{
  @FXML
  private HBox topbox;
  @FXML
  private Label lbl_config;
  @FXML
  private TextField content;
  private String project = "";
  private String serverpath = "";
  private Comet_ResourceManager manager;

  public ConfigEntry(String serverpath, String project)
  {
    this.serverpath = serverpath;
    this.project = project;
    try
    {
      FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ConfigEntry.fxml"));
      fxmlLoader.setRoot(this);
      fxmlLoader.setController(this);
      fxmlLoader.load();
      lbl_config.setText(project);
      this.setPrefHeight(35.0);
      topbox.setMinHeight(35.0);
    }
    catch (IOException e1)
    {
      // TODO Auto-generated catch block
      e1.printStackTrace();
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

  public void setContent(String content)
  {
    this.content.appendText(content);
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
      content.appendText("HMI Client connected from: " + ip + "\n");
    }

    @Override
    public void clientDisconnected(String ip)
    {
      javax.swing.SwingUtilities
          .invokeLater(() -> Main.trayIcon.displayMessage("Automation Studio", "HMI Client disconnected from: " + ip,
              // null
              java.awt.TrayIcon.MessageType.INFO));
      content.appendText("HMI Client disconnected from: " + ip + "\n");
    }
  }
}
