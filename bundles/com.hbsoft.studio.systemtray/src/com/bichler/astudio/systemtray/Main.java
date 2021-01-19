package com.bichler.astudio.systemtray;

import java.awt.TrayIcon;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import com.bichler.astudio.systemtray.icons.Resources;
import com.sun.javafx.application.PlatformImpl;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

// Java 8 code
public class Main extends Application
{
  public static TrayIcon trayIcon;
  /**
   * system tray and window icon
   */
  private URL imageLoc = null;
  private ScrollPane hmiScrollPane;
  private ScrollPane opcScrollPane;
  private ScrollPane iecScrollPane;
  private Stage hmiStage = new Stage();
  private Stage opcStage = new Stage();
  private Stage iecStage = new Stage();
  private String studioPath = "";
  private ScrollPane configScrollPane;
  private Stage configStage = new Stage();
  private String opcuaServerPath;
  private String hmiServerPath;
  private String iecProjectPath;

  // sets up the javafx application.
  // a tray icon is setup for the icon, but the main stage remains invisible until
  // the user interacts with the tray icon.
  @Override
  public void start(final Stage stage)
  {
    imageLoc = Resources.class.getResource("btech.png");
    // instructs the javafx system not to exit implicitly when the last application
    // window is shut.
    Platform.setImplicitExit(false);
    // sets up the tray icon (using awt code run on the swing thread).
    javax.swing.SwingUtilities.invokeLater(this::addAppToTray);
    createOPCStage();
    createHMIStage();
    createIECStage();
    createConfigStage();
  }

  /**
   *
   * @return the main window application content.
   */
  private Node createHMIContent()
  {
    File directory = new File(hmiServerPath);
    VBox content = new VBox();
    if (directory.isDirectory())
    {
      for (File dir : directory.listFiles())
      {
        HMIServerEntry entry = new HMIServerEntry(hmiServerPath, dir.getName());
        content.getChildren().add(entry);
      }
    }
    return content;
  }

  /**
   *
   * @return the main window application content.
   */
  private Node createOPCUAContent()
  {
    File directory = new File(opcuaServerPath);
    VBox content = new VBox();
    if (directory.isDirectory())
    {
      for (File dir : directory.listFiles())
      {
        OPCUAServerEntry entry = new OPCUAServerEntry(opcuaServerPath, dir.getName());
        content.getChildren().add(entry);
      }
    }
    return content;
  }
  
  /**
  *
  * @return the main window application content.
  */
 private Node createIECContent()
 {
   File directory = new File(iecProjectPath);
   VBox content = new VBox();
   if (directory.isDirectory())
   {
     for (File dir : directory.listFiles())
     {
       IECProjectEntry entry = new IECProjectEntry(iecProjectPath, dir.getName());
       content.getChildren().add(entry);
     }
   }
   return content;
 }
  
  /**
  *
  * @return the main window application content.
  */
 private Node createConfigContent()
 {
   File directory = new File(opcuaServerPath);
   VBox content = new VBox();
   if (directory.isDirectory())
   {
     //for (File dir : directory.listFiles())
     //{
       ConfigEntry entry = new ConfigEntry(opcuaServerPath, "OPC UA Server Path");
       entry.setContent(opcuaServerPath);
       content.getChildren().add(entry);
       entry = new ConfigEntry(opcuaServerPath, "HMI Server Path");
       entry.setContent(hmiServerPath);
       content.getChildren().add(entry);
       entry = new ConfigEntry(opcuaServerPath, "AS Server Path");
       entry.setContent(studioPath);
       content.getChildren().add(entry);
     //}
   }
   return content;
 }

  /**
   * Sets up a system tray icon for the application.
   */
  private void addAppToTray()
  {
    try
    {
      /**
       * load servers path from file
       */
      File conf = new File("config.dat");
      if (conf.exists())
      {
        try (FileReader freader = new FileReader(conf); BufferedReader reader = new BufferedReader(freader);)
        {
          String line = "";
          while ((line = reader.readLine()) != null)
          {
            if (line != null && !line.isEmpty())
            {
              String[] items = line.split("=");
              if (items != null && items.length > 1)
              {
                if (items[0].compareTo("opcserver") == 0)
                {
                  opcuaServerPath = items[1];
                }
                else if (items[0].compareTo("hmiservers") == 0)
                {
                  hmiServerPath = items[1];
                }
                else if (items[0].compareTo("studio") == 0)
                {
                  studioPath = items[1];
                }
                else if (items[0].compareTo("iecprojects") == 0)
                {
                  iecProjectPath = items[1];
                }
              }
            }
          }
        }
        catch (IOException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      // ensure awt toolkit is initialized.
      java.awt.Toolkit.getDefaultToolkit();
      // app requires system tray support, just exit if there is no support.
      if (!java.awt.SystemTray.isSupported())
      {
        System.out.println("No system tray support, application exiting.");
        Platform.exit();
      }
      // set up a system tray icon.
      java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
      // java.awt.Image image = ImageIO.read(imageLoc);
      java.awt.Image image = javax.imageio.ImageIO.read(Resources.class.getResourceAsStream("btech16.png"));
      // BufferedImage img = ImageIO.read(imageLoc);
      trayIcon = new TrayIcon(image, "Automation Studio");
      // java.awt.Image image = Toolkit.getDefaultToolkit().getImage();
      // trayIcon = new java.awt.TrayIcon(image);
      // if the user double-clicks on the tray icon, show the main app stage.
      // trayIcon.addActionListener(event -> Platform.runLater(this::showOPCStage));
      // if the user selects the default menu item (which includes the app name),
      // show the main app stage.
      java.awt.MenuItem aboutStudioItem = new java.awt.MenuItem("Über Automation Studio");
      aboutStudioItem.addActionListener(event -> Platform.runLater(this::showAutomationStudio));
      java.awt.MenuItem openHMIItem = new java.awt.MenuItem("HMI Control");
      openHMIItem.addActionListener(event -> Platform.runLater(this::showHMIStage));
      java.awt.MenuItem openOPCUAItem = new java.awt.MenuItem("OPC UA Control");
      openOPCUAItem.addActionListener(event -> Platform.runLater(this::showOPCStage));
      java.awt.MenuItem openIECItem = new java.awt.MenuItem("IEC 61131 Control");
      openIECItem.addActionListener(event -> Platform.runLater(this::showIECStage));
      java.awt.MenuItem openStudioItem = new java.awt.MenuItem("Automation Studio Öffnen");
      openStudioItem.addActionListener(event -> Platform.runLater(this::showAutomationStudio));
      java.awt.MenuItem openConfigItem = new java.awt.MenuItem("Konfiguration");
      openConfigItem.addActionListener(event -> Platform.runLater(this::showAutomationConfig));
      // the convention for tray icons seems to be to set the default icon for opening
      // the application stage in a bold font.
      java.awt.Font defaultFont = java.awt.Font.decode(null);
      defaultFont = defaultFont.deriveFont(14.0f);
      java.awt.Font boldFont = defaultFont.deriveFont(java.awt.Font.BOLD);
      aboutStudioItem.setFont(boldFont);
      openHMIItem.setFont(boldFont);
      openOPCUAItem.setFont(boldFont);
      openIECItem.setFont(boldFont);
      openStudioItem.setFont(defaultFont);
      openConfigItem.setFont(defaultFont);
      // to really exit the application, the user must go to the system tray icon
      // and select the exit option, this will shutdown JavaFX and remove the
      // tray icon (removing the tray icon will also shut down AWT).
      java.awt.MenuItem exitItem = new java.awt.MenuItem("Beenden");
      exitItem.addActionListener(event -> {
        // notificationTimer.cancel();
        Platform.exit();
        tray.remove(trayIcon);
      });
      exitItem.setFont(defaultFont);
      // setup the popup menu for the application.
      final java.awt.PopupMenu popup = new java.awt.PopupMenu();
      popup.add(aboutStudioItem);
      popup.addSeparator();
      popup.add(openHMIItem);
      popup.add(openOPCUAItem);
      popup.add(openIECItem);
      popup.add(openStudioItem);
      popup.add(openConfigItem);
      popup.addSeparator();
      popup.add(exitItem);
      trayIcon.setPopupMenu(popup);
      tray.add(trayIcon);
    }
    catch (java.awt.AWTException e)
    {
      System.out.println("Unable to init system tray");
      e.printStackTrace();
    }
    catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void createHMIStage()
  {
    hmiScrollPane = new ScrollPane();
    hmiScrollPane.setFitToHeight(true);
    hmiScrollPane.setStyle("-fx-background-color: rgba(255, 255, 255, 1);");
    StackPane layout = new StackPane(hmiScrollPane);
    layout.setStyle("-fx-background-color: rgba(255, 255, 255, 1);");
    Scene scene = new Scene(layout, 970, 400);
    scene.setFill(Color.TRANSPARENT);
    hmiStage.setScene(scene);
    hmiStage.setTitle("Automation Studio HMI Control");
    try
    {
      hmiStage.getIcons().add(new Image(imageLoc.openStream()));
    }
    catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void createOPCStage()
  {
    opcScrollPane = new ScrollPane();
    opcScrollPane.setFitToHeight(true);
    opcScrollPane.setStyle("-fx-background-color: rgba(255, 255, 255, 1);");
    StackPane layout = new StackPane(opcScrollPane);
    layout.setStyle("-fx-background-color: rgba(255, 255, 255, 1);");
    Scene scene = new Scene(layout, 970, 400);
    scene.setFill(Color.TRANSPARENT);
    opcStage.setScene(scene);
    opcStage.setTitle("Automation Studio OPCUA Control");
    try
    {
      opcStage.getIcons().add(new Image(imageLoc.openStream()));
    }
    catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  private void createIECStage()
  {
    iecScrollPane = new ScrollPane();
    iecScrollPane.setFitToHeight(true);
    iecScrollPane.setStyle("-fx-background-color: rgba(255, 255, 255, 1);");
    StackPane layout = new StackPane(iecScrollPane);
    layout.setStyle("-fx-background-color: rgba(255, 255, 255, 1);");
    Scene scene = new Scene(layout, 970, 400);
    scene.setFill(Color.TRANSPARENT);
    iecStage.setScene(scene);
    iecStage.setTitle("Automation Studio IEC61131 Control");
    try
    {
      iecStage.getIcons().add(new Image(imageLoc.openStream()));
    }
    catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void createConfigStage()
  {
    configStage = new Stage();
    configScrollPane = new ScrollPane();
    configScrollPane.setFitToHeight(true);
    configScrollPane.setStyle("-fx-background-color: rgba(255, 255, 255, 1);");
    StackPane layout = new StackPane(configScrollPane);
    layout.setStyle("-fx-background-color: rgba(255, 255, 255, 1);");
    Scene scene = new Scene(layout, 700, 200);
    scene.setFill(Color.TRANSPARENT);
    configStage.setScene(scene);
    configStage.setTitle("Automation Studio Service Konfiguration");
    try
    {
      configStage.getIcons().add(new Image(imageLoc.openStream()));
    }
    catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void showHMIStage()
  {
    if (hmiStage != null)
    {
      if (hmiScrollPane.getContent() == null)
        hmiScrollPane.setContent(createHMIContent());
      // hmiStage.show();
      hmiStage.toFront();
    }
    hmiStage.show();
  }

  private void showOPCStage()
  {
    if (opcStage != null)
    {
      if (opcScrollPane.getContent() == null)
        opcScrollPane.setContent(createOPCUAContent());
      // opcStage.show();
      opcStage.toFront();
    }
    opcStage.show();
  }
  
  private void showIECStage()
  {
    if (iecStage != null)
    {
      if (iecScrollPane.getContent() == null)
        iecScrollPane.setContent(createIECContent());
      // opcStage.show();
      iecStage.toFront();
    }
    iecStage.show();
  }
  
  /**
   * Shows the application stage and ensures that it is brought out the front of
   * all stages.
   */
  private void showAutomationStudio()
  {
    Runtime runtime = Runtime.getRuntime();
    try
    {
      runtime.exec(this.studioPath);
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private void showAutomationConfig()
  {
    if (configStage != null)
    {
      configScrollPane.setContent(createConfigContent());
      // opcStage.show();
      configStage.toFront();
    }
    configStage.show();
  }

  public static void main(String[] args) throws IOException, java.awt.AWTException
  {
    PlatformImpl.setTaskbarApplication(false);
    Application.launch(Main.class, args);
  }
}
