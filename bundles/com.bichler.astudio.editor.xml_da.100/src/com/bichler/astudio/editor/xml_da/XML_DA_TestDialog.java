package com.bichler.astudio.editor.xml_da;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.axis.types.UnsignedInt;
import org.apache.axis.types.UnsignedLong;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.opcfoundation.webservices.XMLDA._1_0.ArrayOfBoolean;
import org.opcfoundation.webservices.XMLDA._1_0.ArrayOfByte;
import org.opcfoundation.webservices.XMLDA._1_0.ArrayOfDouble;
import org.opcfoundation.webservices.XMLDA._1_0.ArrayOfFloat;
import org.opcfoundation.webservices.XMLDA._1_0.ArrayOfInt;
import org.opcfoundation.webservices.XMLDA._1_0.ArrayOfLong;
import org.opcfoundation.webservices.XMLDA._1_0.ArrayOfShort;
import org.opcfoundation.webservices.XMLDA._1_0.ArrayOfString;
import org.opcfoundation.webservices.XMLDA._1_0.ArrayOfUnsignedInt;
import org.opcfoundation.webservices.XMLDA._1_0.ArrayOfUnsignedLong;
import org.opcfoundation.webservices.XMLDA._1_0.ItemValue;
import org.opcfoundation.webservices.XMLDA._1_0.ReadRequestItem;
import org.opcfoundation.webservices.XMLDA._1_0.ReadRequestItemList;
import org.opcfoundation.webservices.XMLDA._1_0.ReadResponse;
import org.opcfoundation.webservices.XMLDA._1_0.ReplyItemList;
import org.opcfoundation.webservices.XMLDA._1_0.RequestOptions;
import org.opcfoundation.webservices.XMLDA._1_0.ServiceStub;

import com.bichler.astudio.editor.xml_da.XML_DA_DriverEditor.Device;
import com.bichler.astudio.editor.xml_da.xml.XMLDAModelNode;

public class XML_DA_TestDialog extends TitleAreaDialog
{
  public void setTxtURL(Text txtURL)
  {
    this.txtURL = txtURL;
  }

  public void setTxtUserName(Text txtUserName)
  {
    this.txtUserName = txtUserName;
  }

  public void setTxtPWD(Text txtPWD)
  {
    this.txtPWD = txtPWD;
  }

  public void setURL(String url)
  {
    this.url = url;
  }

  public void setUserName(String username)
  {
    this.username = username;
  }

  public void setPWD(String pwd)
  {
    this.pwd = pwd;
  }
  private Text txtURL;
  private Text txtUserName;
  private Text txtPWD;
  private String url;
  private String username;
  private String pwd;
  private Object[] nodes;
  private Device device;

  public XML_DA_TestDialog(Shell parent)
  {
    super(parent);
  }

  @Override
  protected Control createDialogArea(Composite parent)
  {
    Composite area = (Composite) super.createDialogArea(parent);
    Composite container = new Composite(area, SWT.NONE);
    container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    GridLayout layout = new GridLayout(2, false);
    container.setLayout(layout);
    createURL(container);
    createUserName(container);
    createPWD(container);
    createDPs(container);
    return area;
  }

  private void createURL(Composite container)
  {
    Label lbtURL = new Label(container, SWT.NONE);
    lbtURL.setText("URL: ");
    GridData dataUrl = new GridData();
    dataUrl.grabExcessHorizontalSpace = true;
    dataUrl.horizontalAlignment = GridData.FILL;
    txtURL = new Text(container, SWT.BORDER);
    txtURL.setLayoutData(dataUrl);
    txtURL.setText(device.url);
    // Button btndp = new Button(container, SWT.NONE);
    // btndp.setText("Verbinden");
    // GridData datadpGrid = new GridData();
    // datadpGrid.widthHint = 55;
    // btndp.setLayoutData(datadpGrid);
    // btndp.addSelectionListener(new SelectionAdapter()
    // {
    //
    // });
  }

  private void createUserName(Composite container)
  {
    Label lbtUSerName = new Label(container, SWT.NONE);
    lbtUSerName.setText("Username: ");
    GridData dataUserName = new GridData();
    dataUserName.grabExcessHorizontalSpace = true;
    dataUserName.horizontalAlignment = GridData.FILL;
    txtUserName = new Text(container, SWT.BORDER);
    txtUserName.setLayoutData(dataUserName);
    txtUserName.setText(device.username + "");
    // new Label(container, SWT.NONE);
  }

  private void createPWD(Composite container)
  {
    Label lbtPassword = new Label(container, SWT.NONE);
    lbtPassword.setText("Password: ");
    GridData dataPWD = new GridData();
    dataPWD.grabExcessHorizontalSpace = true;
    dataPWD.horizontalAlignment = GridData.FILL;
    txtPWD = new Text(container, SWT.BORDER);
    txtPWD.setLayoutData(dataPWD);
    txtPWD.setText(device.password + "");
    // new Label(container, SWT.NONE);
  }

  private void createDPs(Composite container)
  {
    Label lblDPs = new Label(container, SWT.NONE);
    lblDPs.setText("Datenpunkte:");
    new Label(container, SWT.NONE);
    // Label dummy2 = new Label(container, SWT.NONE);
    for (Object obj : this.nodes)
    {
      final XMLDAModelNode node = (XMLDAModelNode) obj;
      Label lblItemPath = new Label(container, SWT.NONE);
      lblItemPath.setText("Itempfad: ");
      GridData dataItemPath = new GridData();
      dataItemPath.grabExcessHorizontalSpace = true;
      dataItemPath.horizontalAlignment = GridData.FILL;
      final Text txtItemPath = new Text(container, SWT.BORDER);
      txtItemPath.setText(node.getItemPath());
      txtItemPath.setLayoutData(dataItemPath);
      // new Label(container, SWT.NONE);
      Label lblItemName = new Label(container, SWT.NONE);
      lblItemName.setText("ItemName: ");
      GridData dataItemName = new GridData();
      dataItemName.grabExcessHorizontalSpace = true;
      dataItemName.horizontalAlignment = GridData.FILL;
      final Text txtItemName = new Text(container, SWT.BORDER);
      txtItemName.setText(node.getItemName());
      txtItemName.setLayoutData(dataItemName);
      // new Label(container, SWT.NONE);
      // + node.getItemPath() + " / " + node.getItemName()
      // + node.getDataType());
      // GridData datadp = new GridData();
      // datadp.grabExcessHorizontalSpace = true;
      // datadp.horizontalAlignment = GridData.FILL;
      // final Text txtItemPath = new Text(container, SWT.BORDER);
      // txtdp.setLayoutData(datadp);
      Button btndp = new Button(container, SWT.NONE);
      btndp.setText("Test");
      GridData datadpGrid = new GridData();
      // datadpGrid.grabExcessHorizontalSpace = true;
      // datadpGrid.horizontalAlignment = GridData.FILL;
      GridData dataValue = new GridData();
      dataValue.grabExcessHorizontalSpace = true;
      dataValue.horizontalAlignment = GridData.FILL;
      final Text txtValue = new Text(container, SWT.BORDER);
      txtValue.setLayoutData(dataValue);
      GridData dataType = new GridData();
      dataType.grabExcessHorizontalSpace = true;
      dataType.horizontalAlignment = GridData.FILL;
      new Label(container, SWT.NONE);
      final Text txtDataType = new Text(container, SWT.BORDER);
      txtDataType.setLayoutData(dataType);
      datadpGrid.widthHint = 55;
      btndp.setLayoutData(datadpGrid);
      btndp.addSelectionListener(new SelectionAdapter()
      {
        @Override
        public void widgetSelected(SelectionEvent e)
        {
          ServiceStub service;
          try
          {
            service = new ServiceStub(new URL(txtURL.getText()), null);
            service.setUsername(txtUserName.getText());
            service.setPassword(txtPWD.getText());
            RequestOptions options = new RequestOptions();
            ReadRequestItem[] requestItems = new ReadRequestItem[1];
            ReadRequestItemList itemList = new ReadRequestItemList();
            ReadRequestItem item = new ReadRequestItem();
            ReadResponse readresponse = null;
            options.setLocaleID("en");
            options.setReturnItemName(true);
            options.setReturnItemPath(true);
            item.setItemName(txtItemName.getText());
            item.setItemPath(txtItemPath.getText());
            requestItems[0] = item;
            itemList.setItems(requestItems);
            readresponse = service.read(options, itemList);
            if (readresponse == null)
            {
              Logger.getLogger(getClass().getName()).log(Level.SEVERE, "read response is null");
            }
            else
            {
              if (readresponse.getRItemList() != null)
              {
                ReplyItemList itemlist = readresponse.getRItemList();
                for (ItemValue val : itemlist.getItems())
                {
                  if (val != null)
                  {
                    if (txtValue != null)
                    {
                      String value = "";
                      if (val.getValue() instanceof ArrayOfInt)
                      {
                        ArrayOfInt obj = (ArrayOfInt) val.getValue();
                        value = "[";
                        String del = "";
                        for (int i : obj.get_int())
                        {
                          value += del + i;
                          del = ";";
                        }
                        value += "]";
                      }
                      else if (val.getValue() instanceof ArrayOfFloat)
                      {
                        ArrayOfFloat obj = (ArrayOfFloat) val.getValue();
                        value = "[";
                        String del = "";
                        for (float i : obj.get_float())
                        {
                          value += del + i;
                          del = ";";
                        }
                        value += "]";
                      }
                      else if (val.getValue() instanceof ArrayOfDouble)
                      {
                        ArrayOfDouble obj = (ArrayOfDouble) val.getValue();
                        value = "[";
                        String del = "";
                        for (double i : obj.get_double())
                        {
                          value += del + i;
                          del = ";";
                        }
                        value += "]";
                      }
                      else if (val.getValue() instanceof ArrayOfBoolean)
                      {
                        ArrayOfBoolean obj = (ArrayOfBoolean) val.getValue();
                        value = "[";
                        String del = "";
                        for (boolean i : obj.get_boolean())
                        {
                          value += del + i;
                          del = ";";
                        }
                        value += "]";
                      }
                      else if (val.getValue() instanceof ArrayOfByte)
                      {
                        ArrayOfByte obj = (ArrayOfByte) val.getValue();
                        value = "[";
                        String del = "";
                        for (byte i : obj.get_byte())
                        {
                          value += del + i;
                          del = ";";
                        }
                        value += "]";
                      }
                      else if (val.getValue() instanceof ArrayOfLong)
                      {
                        ArrayOfLong obj = (ArrayOfLong) val.getValue();
                        value = "[";
                        String del = "";
                        for (long i : obj.get_long())
                        {
                          value += del + i;
                          del = ";";
                        }
                        value += "]";
                      }
                      else if (val.getValue() instanceof ArrayOfShort)
                      {
                        ArrayOfShort obj = (ArrayOfShort) val.getValue();
                        value = "[";
                        String del = "";
                        for (short i : obj.get_short())
                        {
                          value += del + i;
                          del = ";";
                        }
                        value += "]";
                      }
                      else if (val.getValue() instanceof ArrayOfUnsignedInt)
                      {
                        ArrayOfUnsignedInt obj = (ArrayOfUnsignedInt) val.getValue();
                        value = "[";
                        String del = "";
                        for (UnsignedInt i : obj.getUnsignedInt())
                        {
                          value += del + i.intValue();
                          del = ";";
                        }
                        value += "]";
                      }
                      else if (val.getValue() instanceof ArrayOfUnsignedLong)
                      {
                        ArrayOfUnsignedLong obj = (ArrayOfUnsignedLong) val.getValue();
                        value = "[";
                        String del = "";
                        for (UnsignedLong i : obj.getUnsignedLong())
                        {
                          value += del + i.longValue();
                          del = ";";
                        }
                        value += "]";
                      }
                      else if (val.getValue() instanceof ArrayOfString)
                      {
                        ArrayOfString obj = (ArrayOfString) val.getValue();
                        value = "[";
                        String del = "";
                        for (String i : obj.getString())
                        {
                          value += del + i;
                          del = ";";
                        }
                        value += "]";
                      }
                      else
                        value = val.getValue().toString();
                      txtValue.setText(value);
                      txtDataType.setText(val.getValue().getClass().getSimpleName());
                    }
                  }
                }
              }
              else
              {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "itemlist in read response is null");
              }
            }
          }
          catch (MalformedURLException | RemoteException e1)
          {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e1.getMessage());
          }
        }
      });
    }
  }

  @Override
  protected boolean isResizable()
  {
    return true;
  }

  @Override
  protected void okPressed()
  {
    super.okPressed();
  }

  public void setDPs(Object[] selections, Device device)
  {
    this.nodes = selections;
    this.device = device;
  }
}
