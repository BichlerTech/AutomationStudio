package com.hbsoft.studio.systemtray;

import java.awt.TrayIcon;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

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
  private Stage stage;
  public static TrayIcon trayIcon;
  /**
   * system tray and window icon
   */
  URL imageLoc = System.class.getResource("/com/hbsoft/studio/systemtray/icons/centauro.png");

  // sets up the javafx application.
  // a tray icon is setup for the icon, but the main stage remains invisible until
  // the user interacts with the tray icon.
  @Override
  public void start(final Stage stage)
  {
    // stores a reference to the stage.
    this.stage = stage;
    // instructs the javafx system not to exit implicitly when the last application
    // window is shut.
    Platform.setImplicitExit(false);
    // sets up the tray icon (using awt code run on the swing thread).
    javax.swing.SwingUtilities.invokeLater(this::addAppToTray);
    // out stage will be translucent, so give it a transparent style.
    // stage.initStyle(StageStyle.TRANSPARENT);
    // create the layout for the javafx stage.
    ScrollPane scrollPane = new ScrollPane(createContent());
    scrollPane.setFitToHeight(true);
    scrollPane.setStyle("-fx-background-color: rgba(255, 255, 255, 1);");
    StackPane layout = new StackPane(scrollPane);
    layout.setStyle("-fx-background-color: rgba(255, 255, 255, 1);");
    Scene scene = new Scene(layout, 910, 400);
    scene.setFill(Color.TRANSPARENT);
    stage.setScene(scene);
    stage.setTitle("Automation Studio Control");
    try
    {
      stage.getIcons().add(new Image(imageLoc.openStream()));
    }
    catch (IOException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * For this dummy app, the (JavaFX scenegraph) content, just says "hello,
   * world". A real app, might load an FXML or something like that.
   *
   * @return the main window application content.
   */
  private Node createContent()
  {
    String serverpath = "C:\\HBS\\Programmierung\\Testprojekte\\testplan_neu\\hmi_server\\servers\\";
    File directory = new File(serverpath);
    VBox content = new VBox();
    if (directory.isDirectory())
    {
      for (File dir : directory.listFiles())
      {
        HMIServerEntry entry = new HMIServerEntry(dir.getName());
        content.getChildren().add(entry);
      }
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
      java.awt.Image image = ImageIO.read(imageLoc);
      trayIcon = new java.awt.TrayIcon(image);
      // if the user double-clicks on the tray icon, show the main app stage.
      trayIcon.addActionListener(event -> Platform.runLater(this::showStage));
      // if the user selects the default menu item (which includes the app name),
      // show the main app stage.
      java.awt.MenuItem openItem = new java.awt.MenuItem("Automation Studio Control");
      openItem.addActionListener(event -> Platform.runLater(this::showStage));
      java.awt.MenuItem openStudioItem = new java.awt.MenuItem("Automation Studio Öffnen");
      openStudioItem.addActionListener(event -> Platform.runLater(this::showStage));
      // the convention for tray icons seems to be to set the default icon for opening
      // the application stage in a bold font.
      java.awt.Font defaultFont = java.awt.Font.decode(null);
      java.awt.Font boldFont = defaultFont.deriveFont(java.awt.Font.BOLD);
      openItem.setFont(boldFont);
      // to really exit the application, the user must go to the system tray icon
      // and select the exit option, this will shutdown JavaFX and remove the
      // tray icon (removing the tray icon will also shut down AWT).
      java.awt.MenuItem exitItem = new java.awt.MenuItem("Exit");
      exitItem.addActionListener(event -> {
        // notificationTimer.cancel();
        Platform.exit();
        tray.remove(trayIcon);
      });
      // setup the popup menu for the application.
      final java.awt.PopupMenu popup = new java.awt.PopupMenu();
      popup.add(openItem);
      popup.add(openStudioItem);
      popup.addSeparator();
      popup.add(exitItem);
      trayIcon.setPopupMenu(popup);
      tray.add(trayIcon);
    }
    catch (java.awt.AWTException | IOException e)
    {
      System.out.println("Unable to init system tray");
      e.printStackTrace();
    }
  }

  /**
   * Shows the application stage and ensures that it is brought ot the front of
   * all stages.
   */
  private void showStage()
  {
    if (stage != null)
    {
      stage.show();
      stage.toFront();
    }
  }

  public static void main(String[] args) throws IOException, java.awt.AWTException
  {
    launch(args);
  }
}
