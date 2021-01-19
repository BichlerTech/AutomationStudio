package com.bichler.astudio.intro.config;

import org.eclipse.core.runtime.IRegistryChangeEvent;
import org.eclipse.core.runtime.IRegistryChangeListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.IntroPart;

import com.bichler.astudio.ASActivator;

public class WelcomePage extends IntroPart implements IRegistryChangeListener
{
  @Override
  public void standbyStateChanged(boolean standby)
  {
    // TODO Auto-generated method stub
  }

  @Override
  public void registryChanged(IRegistryChangeEvent event)
  {
    // TODO Auto-generated method stub
  }

  @Override
  public void createPartControl(Composite container)
  {
    Composite outerContainer = new Composite(container, SWT.NONE);
    
    GridLayout gridLayout = new GridLayout();
    gridLayout.numColumns = 3;
    
//    GridData dg = new GridData(style)
    outerContainer.setLayout(gridLayout);
    
    Label imagetop = new Label(outerContainer, SWT.TOP);
    imagetop.setImage(ASActivator.getDefault().getImage("icons/header.png"));
    GridData gd_header = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
    gd_header.horizontalAlignment = GridData.CENTER;
    gd_header.verticalAlignment = GridData.BEGINNING;
    gd_header.horizontalSpan = 3;
    imagetop.setLayoutData(gd_header);
    
//    Pane imageleft = new Label(outerContainer, SWT.LEFT);
//    imageleft.setImage(HBStudioActivator.getDefault().getImage("icons/left.png"));
//    GridData gd_left = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
//    gd_left.horizontalAlignment = GridData.BEGINNING;
//    gd_left.verticalAlignment = GridData.BEGINNING;
//    imageleft.setLayoutData(gd_left);
    
//    Label label = new Label(outerContainer, SWT.LEFT);
//    label.setImage(HBStudioActivator.getDefault().getImage(SharedImages.ICONS_WELCOME_OPCUA));
//    GridData gd_opc = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
//    gd_opc.horizontalAlignment = GridData.BEGINNING;
//    gd_opc.verticalAlignment = GridData.BEGINNING;
//    label.setLayoutData(gd_opc);
  
//    RowLayout layout = new RowLayout(SWT.TOP);
//    layout.marginTop = 0;
//    layout.marginLeft = 0;
//    outerContainer.setLayout(layout);
    
    
    
//    outerContainer.setBackground(outerContainer.getDisplay().getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
//    Label label = new Label(outerContainer, SWT.CENTER);
//    label.setText("WELCOME TO ECLIPSE");
//    GridData gd = new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
//    gd.horizontalAlignment = GridData.CENTER;
//    gd.verticalAlignment = GridData.CENTER;
//    label.setLayoutData(gd);
//    label.setBackground(outerContainer.getDisplay().getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT));
    
//    Browser browser = new Browser(outerContainer, SWT.NONE);
//    browser.setBounds(0,0,600,400);
//    browser.setUrl("www.pietec.at/img/portfolio/item6.html");
  }

  @Override
  public void setFocus()
  {
    // TODO Auto-generated method stub
  }
}
