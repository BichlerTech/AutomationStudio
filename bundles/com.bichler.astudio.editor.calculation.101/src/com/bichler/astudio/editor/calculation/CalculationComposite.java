package com.bichler.astudio.editor.calculation;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.wb.swt.SWTResourceManager;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedByte;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.UnsignedLong;
import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.ReadValueId;
import org.opcfoundation.ua.core.TimestampsToReturn;

import com.bichler.opc.driver.calculation.CalcEvent;
import com.bichler.opc.driver.calculation.CalculationExpression;
import com.bichler.opc.driver.calculation.CalculationNode;
import com.bichler.opc.driver.calculation.CalculationObject;
import com.bichler.opc.driver.calculation.targets.CalculationBooleanTarget;
import com.bichler.opc.driver.calculation.targets.CalculationSByteTarget;
import com.bichler.opc.driver.calculation.targets.CalculationDoubleTarget;
import com.bichler.opc.driver.calculation.targets.CalculationIntTarget;
import com.bichler.opc.driver.calculation.targets.CalculationLongTarget;
import com.bichler.opc.driver.calculation.targets.CalculationStringTarget;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.UAVariableNode;
import opc.sdk.core.node.UAVariableTypeNode;
import opc.sdk.core.node.VariableNode;
import opc.sdk.server.core.UAServerApplicationInstance;
import com.bichler.astudio.editor.calculation.dnd.CalculationDnDTargetAdapter;
import com.bichler.astudio.editor.calculation.model.CalculationModelNode;

import com.bichler.astudio.editor.calculation.dialog.CalculationOPCUANodeDialog;
import com.bichler.astudio.opcua.dnd.OPCUADropTarget;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.astudio.utils.ui.swt.CheckBoxButton;
import com.bichler.astudio.utils.ui.swt.NumericText;

public class CalculationComposite extends Composite
{
  private UAServerApplicationInstance opcServer = null;
  private NumericText txtTimeout;
  private Combo combo;
  private CalculationModelNode dpItem = new CalculationModelNode();
  private Text txtTarget;
  private ScrolledComposite scrolledComposite;
  private IEditorPart editor = null;
  private CheckBoxButton cbtActive;
  private Composite compositeExpression;

  public UAServerApplicationInstance getOpcServer()
  {
    return opcServer;
  }

  public CalculationModelNode getDp()
  {
    return dpItem;
  }

  @Override
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    NamespaceTable uris = opcServer.getServerInstance().getNamespaceUris();
    String id = "";
    String[] idelements = null;
    if (!NodeId.isNull(dpItem.getDp().getTarget().getTargetId()))
    {
      idelements = dpItem.getDp().getTarget().getTargetId().toString().split(";");
      if (idelements != null)
      {
        if (idelements.length == 1)
        {
          id = idelements[0];
        }
        else if (idelements.length == 2)
        {
          id = idelements[1];
        }
      }
    }
    buffer.append("    <calc>\n");
    buffer.append("      <target ns=\"");
    String nsUri = "";
    if (!NodeId.isNull(dpItem.getDp().getTarget().getTargetId()))
    {
      nsUri = uris.getUri(dpItem.getDp().getTarget().getTargetId().getNamespaceIndex());
    }
    buffer.append(nsUri);
    buffer.append(
        "\" id=\"" + id + "\" name=\"" + dpItem.getName() + "\" index=\"" + dpItem.getDp().getArrayindex() + "\"/>\n");
    buffer.append("      <active value=\"" + this.dpItem.getDp().isActive() + "\" />\n");
    buffer.append(
        "      <calcevent value=\"" + combo.getText() + "\" timeout=\"" + this.dpItem.getDp().getTimeout() + "\"/>\n");
    for (CalculationObject obj : this.dpItem.getDp().getCalculationExpressions())
    {
      if (obj instanceof CalculationExpression)
      {
        buffer.append("      <operation value=\"" + obj.getContent().replaceAll("<", "$lower$").replaceAll(">", "$greater$").replaceAll("&", "&amp;").replaceAll("'", "&#39;").replaceAll("\"", "&#34;")
            + "\" />\n");
      }
      else if (obj instanceof CalculationNode)
      {
        // String[] txtDisplay = obj.getContent().split(" ");
        // NodeId nodeid = NodeId
        // .decode(txtDisplay[txtDisplay.length - 1]);
        id = "";
        nsUri = "";
        try
        {
          NodeId nodeid = NodeId.parseNodeId(((CalculationNode) obj).getContent());
          idelements = nodeid.toString().split(";");
          // id value
          if (idelements != null)
          {
            if (idelements.length == 1)
            {
              id = idelements[0];
            }
            else if (idelements.length == 2)
            {
              id = idelements[1];
            }
            // id namespace
            nsUri = uris.getUri(nodeid.getNamespaceIndex());
          }
        }
        catch (IllegalArgumentException e)
        {
          // nodeid null
        }
        buffer.append("      <node ns=\"" + nsUri + "\" id=\"" + id + "\" name=\"" + obj.getContent() + "\" index=\""
            + ((CalculationNode) obj).getArrayIndex() + "\"/>\n");
      }
    }
    buffer.append("    </calc>\n");
    return buffer.toString();
  }

  // public void setOpcServer(UAServerApplicationInstance opcServer) {
  // this.opcServer = opcServer;
  // }
  /**
   * Create the composite.
   * 
   * @param parent
   * @param style
   * @param opcServer2
   */
  public CalculationComposite(final Composite parent, int style, final IEditorPart editor, CalculationModelNode dp,
      UAServerApplicationInstance opcServer)
  {
    super(parent, style);
    // parent.setBackground(Display.getCurrent()
    // .getSystemColor(SWT.color_));
    this.editor = editor;
    this.dpItem = dp;
    this.opcServer = opcServer;
    setLayout(new GridLayout(4, false));
    final Composite cmp_calculation = new Composite(this, SWT.NONE);
    cmp_calculation.setLayout(new GridLayout(4, false));
    GridData gd_cmp_calculation = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
    gd_cmp_calculation.heightHint = 60;
    cmp_calculation.setLayoutData(gd_cmp_calculation);
    Composite cmp_active = new Composite(cmp_calculation, SWT.NONE);
    GridData gd_cmp_active = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_cmp_active.heightHint = 49;
    cmp_active.setLayoutData(gd_cmp_active);
    GridLayout gl_cmp_active = new GridLayout(1, false);
    gl_cmp_active.horizontalSpacing = 0;
    cmp_active.setLayout(gl_cmp_active);
    cbtActive = new CheckBoxButton(cmp_active, SWT.CHECK | SWT.CENTER);
    GridData gd_cbt_active = new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1);
    gd_cbt_active.widthHint = 39;
    cbtActive.setLayoutData(gd_cbt_active);
    cbtActive.setAlignment(SWT.CENTER);
    cbtActive.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        // TODO Auto-generated method stub
        dpItem.getDp().setActive(cbtActive.isChecked());
        ((CalculationDPEditor) editor).setDirty(true);
      }
    });
    Composite composite_4 = new Composite(cmp_calculation, SWT.NONE);
    composite_4.setLayout(new GridLayout(1, false));
    GridData gd_composite_4 = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
    gd_composite_4.widthHint = 150;
    composite_4.setLayoutData(gd_composite_4);
    Label lblNewLabel = new Label(composite_4, SWT.NONE);
    lblNewLabel.setAlignment(SWT.CENTER);
    lblNewLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    lblNewLabel.setFont(SWTResourceManager.getFont("Lucida Grande", 13, SWT.NORMAL));
    lblNewLabel.setText(CustomString.getString(CalculationActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.calculation.dialog.additem.target"));
    txtTarget = new Text(composite_4, SWT.BORDER | SWT.READ_ONLY);
    txtTarget.setText("");
    txtTarget.addMouseListener(new MouseAdapter()
    {
      @Override
      public void mouseDoubleClick(MouseEvent e)
      {
        CalculationOPCUANodeDialog dia = new CalculationOPCUANodeDialog(Display.getCurrent().getActiveShell(),
            dpItem.getDp().getArrayindex());
        dia.setInternalServer(CalculationComposite.this.opcServer.getServerInstance());
        dia.setSelectedNodeId(dpItem.getDp().getTarget().getTargetId());
        dia.setFormTitle(CustomString.getString(CalculationActivator.getDefault().RESOURCE_BUNDLE,
            "com.bichler.astudio.editor.calculation.dialog.additem.title"));
        if (dia.open() == Dialog.OK)
        {
          Node node = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
              .getNodeById(dia.getSelectedNodeId());
          Variant dv = null;
          switch (node.getNodeClass())
          {
          case Variable:
            dv = ((UAVariableNode) node).getValue();
            break;
          case VariableType:
            dv = ((UAVariableTypeNode) node).getValue();
		default:
			break;
          }
          if (dv != null && dv.getValue() != null && dv.getCompositeClass() != null)
          {
            Class<?> type = dv.getCompositeClass();
            // change target item type
            if (type.equals(Boolean.class))
            {
              dpItem.getDp().setTarget(new CalculationBooleanTarget());
            }
            else if (type.equals(Integer.class))
            {
              dpItem.getDp().setTarget(new CalculationIntTarget());
            }
            else if (type.equals(UnsignedByte.class))
            {
              dpItem.getDp().setTarget(new CalculationIntTarget());
            }
            else if (type.equals(byte[].class))
            {
              dpItem.getDp().setTarget(new CalculationIntTarget());
            }
            else if (type.equals(Double.class))
            {
              dpItem.getDp().setTarget(new CalculationDoubleTarget());
            }
            else if (type.equals(Float.class))
            {
              dpItem.getDp().setTarget(new CalculationDoubleTarget());
            }
            else if (type.equals(Short.class))
            {
              dpItem.getDp().setTarget(new CalculationIntTarget());
            }
            else if (type.equals(Integer.class))
            {
              dpItem.getDp().setTarget(new CalculationIntTarget());
            }
            else if (type.equals(Long.class))
            {
              dpItem.getDp().setTarget(new CalculationLongTarget());
            }
            else if (type.equals(Byte.class))
            {
              dpItem.getDp().setTarget(new CalculationSByteTarget());
            }
            else if (type.equals(String.class))
            {
              dpItem.getDp().setTarget(new CalculationStringTarget());
            }
            else if (type.equals(UnsignedShort.class))
            {
              dpItem.getDp().setTarget(new CalculationIntTarget());
            }
            else if (type.equals(UnsignedInteger.class))
            {
              dpItem.getDp().setTarget(new CalculationIntTarget());
            }
            else if (type.equals(UnsignedLong.class))
            {
              dpItem.getDp().setTarget(new CalculationLongTarget());
            }
            int index = dia.getIndex();
            dpItem.getDp().setArrayindex(index);
          }
          onCaculationNodeDialogFinis();
          setCalculationComposite(dia.getSelectedNodeId(), dia.getSelectedDisplayName());
        }
      }

      @Override
      public void mouseDown(MouseEvent e)
      {
        selectDataPoint(dpItem.getDp());
      }
    });
    txtTarget.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    DropTarget dropTarget = new DropTarget(this.txtTarget, DND.DROP_COPY);
    dropTarget.setTransfer(new Transfer[] { TextTransfer.getInstance() });
    dropTarget.addDropListener(new CalculationDnDTargetAdapter(this));
    Composite cmp_event = new Composite(cmp_calculation, SWT.NONE);
    cmp_event.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
    cmp_event.setLayout(new GridLayout(1, false));
    Label lblEvent = new Label(cmp_event, SWT.CENTER);
    lblEvent.setFont(SWTResourceManager.getFont("Lucida Grande", 13, SWT.NORMAL));
    GridData gd_lblEvent = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
    gd_lblEvent.widthHint = 60;
    lblEvent.setLayoutData(gd_lblEvent);
    lblEvent.setAlignment(SWT.CENTER);
    lblEvent.setText(CustomString.getString(CalculationActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.calculation.dialog.additem.event"));
    combo = new Combo(cmp_event, SWT.READ_ONLY);
    GridData gd_combo = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
    gd_combo.widthHint = 100;
    combo.setLayoutData(gd_combo);
    combo.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        if (combo.getText().compareTo(CalcEvent.CYCLIC.name()) == 0)
        {
          txtTimeout.setEnabled(true);
        }
        else
        {
          txtTimeout.setEnabled(false);
        }
        dpItem.getDp().setEvent(combo.getText());
        ((CalculationDPEditor) editor).setDirty(true);
      }
    });
    combo.setFont(SWTResourceManager.getFont("Lucida Grande", 12, SWT.NORMAL));
    combo.setItems(new String[] { CalcEvent.CYCLIC.name(), CalcEvent.ONREAD.name(), CalcEvent.VALUECHANGE.name() });
    combo.select(0);
    Composite cmp_timeout = new Composite(cmp_calculation, SWT.NONE);
    cmp_timeout.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
    cmp_timeout.setLayout(new GridLayout(1, false));
    Label lbl_Timeout = new Label(cmp_timeout, SWT.CENTER);
    lbl_Timeout.setFont(SWTResourceManager.getFont("Lucida Grande", 13, SWT.NORMAL));
    GridData gd_lbl_Timeout = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
    gd_lbl_Timeout.widthHint = 50;
    lbl_Timeout.setLayoutData(gd_lbl_Timeout);
    lbl_Timeout.setText(CustomString.getString(CalculationActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.calculation.dialog.additem.timeout"));
    txtTimeout = new NumericText(cmp_timeout, SWT.BORDER);
    txtTimeout.setEnabled(true);
    GridData gd_txt_timeout = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
    gd_txt_timeout.widthHint = 50;
    txtTimeout.setLayoutData(gd_txt_timeout);
    Composite composite = new Composite(this, SWT.NONE);
    composite.setLayout(new FillLayout(SWT.HORIZONTAL));
    composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
    scrolledComposite = new ScrolledComposite(composite, SWT.H_SCROLL | SWT.V_SCROLL);
    scrolledComposite.setExpandVertical(true);
    scrolledComposite.setExpandHorizontal(true);
    Button btn_add = new Button(this, SWT.NONE);
    btn_add.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        // add new expression or datapoint
        AddCalculationItemDialog dialog = new AddCalculationItemDialog(getShell());
        dialog.setDp(dpItem);
        if (dialog.open() == Dialog.OK)
        {
          // add index where the new expression should be added
          int index = dialog.getIndex();
          switch (dialog.getSelection())
          {
          case 0:
            // Expression
            dpItem.getDp().getCalculationExpressions().add(index, new CalculationExpression(""));
            fillExpressions();
            ((CalculationDPEditor) editor).setDirty(true);
            break;
          case 1:
            // new Node
            CalculationOPCUANodeDialog dia = new CalculationOPCUANodeDialog(Display.getCurrent().getActiveShell(), -1);
            dia.setInternalServer(CalculationComposite.this.opcServer.getServerInstance());
            dia.setSelectedNodeId(null);
            dia.setFormTitle(CustomString.getString(CalculationActivator.getDefault().RESOURCE_BUNDLE,
                "com.bichler.astudio.editor.calculation.dialog.additem.title"));
            if (dia.open() == Dialog.OK)
            {
              // String txt = + " "
              // + dia.getSelectedNodeId().toString();
              int arrayIndex = dia.getIndex();
              CalculationNode newNode = new CalculationNode(dia.getSelectedNodeId().toString());
              newNode.setArrayIndex(arrayIndex);
              dpItem.getDp().getCalculationExpressions().add(index, newNode);
              fillExpressions();
              onCaculationNodeDialogFinis();
              ((CalculationDPEditor) editor).setDirty(true);
            }
            break;
          default:
            break;
          }
        }
      }
    });
    GridData gd_btn_add = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_btn_add.widthHint = 60;
    gd_btn_add.heightHint = 60;
    btn_add.setLayoutData(gd_btn_add);
    btn_add.setImage(
        CalculationSharedImages.getImage(CalculationSharedImages.ICON_ADD));
    Button btn_delete = new Button(this, SWT.NONE);
    GridData gd_btn_delete = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
    gd_btn_delete.widthHint = 60;
    gd_btn_delete.heightHint = 60;
    btn_delete.setLayoutData(gd_btn_delete);
    btn_delete.setImage(CalculationSharedImages.getImage(CalculationSharedImages.ICON_DELETE));
    btn_delete.addSelectionListener(new SelectionAdapter()
    {
      @Override
      public void widgetSelected(SelectionEvent e)
      {
        DeleteExpression dialog = new DeleteExpression(getShell(), dpItem);
        if (dialog.open() == Dialog.OK)
        {
          dpItem.getDp().getCalculationExpressions().remove(dialog.getIndex());
          // TODO update all composites
          fillExpressions();
          ((CalculationDPEditor) editor).setDirty(true);
        }
      }
    });
    this.fillGeneral();
    this.fillExpressions();
    this.setHandlers();
  }

  void onCaculationNodeDialogFinis()
  {
    // this.editor.getSite().getSelectionProvider().setSelection(this.editor.getSite().getSelectionProvider().getSelection());
    ((CalculationDPEditor) this.editor).refreshPropertyPage();
  }

  public void setCalculationComposite(NodeId nodeId, String name)
  {
    txtTarget.setText(name);
    dpItem.getDp().getTarget().setTargetId(nodeId);
    dpItem.setName(name);
    ((CalculationDPEditor) editor).setDirty(true);
  }

  private void fillGeneral()
  {
    cbtActive.setChecked(dpItem.getDp().isActive());
    if (dpItem.getDp().getTarget() != null && dpItem.getDp().getTarget().getTargetId() != null)
    {
      // txt_Target.setText(dp.getTarget().getName());
      ReadValueId[] nodesToRead = new ReadValueId[1];
      nodesToRead[0] = new ReadValueId();
      nodesToRead[0].setNodeId(dpItem.getDp().getTarget().getTargetId());
      nodesToRead[0].setAttributeId(Attributes.DisplayName);
      DataValue[] dv = null;
      try
      {
        dv = this.opcServer.getServerInstance().getMaster().read(nodesToRead, 0.0, TimestampsToReturn.Neither, null,
            null);
        if (dv != null && dv.length > 0 && dv[0].getValue() != null && dv[0].getValue().getValue() != null)
        {
          txtTarget.setText(((LocalizedText) dv[0].getValue().getValue()).getText());
        }
      }
      catch (ServiceResultException sre)
      {
        sre.printStackTrace();
      }
    }
    txtTimeout.setText(dpItem.getDp().getTimeout() + "");
    combo.setText(dpItem.getDp().getEvent().name());
    if (combo.getText().compareTo(CalcEvent.CYCLIC.name()) == 0)
    {
      txtTimeout.setEnabled(true);
    }
    else
    {
      txtTimeout.setEnabled(false);
    }
  }

  private void setHandlers()
  {
    txtTimeout.addModifyListener(new ModifyListener()
    {
      @Override
      public void modifyText(ModifyEvent e)
      {
        dpItem.getDp().setTimeout(Integer.parseInt(txtTimeout.getText()));
        ((CalculationDPEditor) editor).setDirty(true);
      }
    });
  }

  private void fillExpressions()
  {
    Control content = scrolledComposite.getContent();
    if (content != null)
    {
      content.dispose();
    }
    this.compositeExpression = new Composite(scrolledComposite, SWT.BORDER);
    compositeExpression.setLayout(new GridLayout(dpItem.getDp().getCalculationExpressions().size() + 1, false));
    scrolledComposite.setContent(compositeExpression);
    for (final CalculationObject obj : dpItem.getDp().getCalculationExpressions())
    {
      // add new composite for new expression
      Composite newExpression = new Composite(compositeExpression, SWT.NONE);
      newExpression.setLayout(new GridLayout(1, false));
      GridData gd_newExpression = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
      gd_newExpression.widthHint = 109;
      newExpression.setLayoutData(gd_newExpression);
      Label lbl_newLabel = new Label(newExpression, SWT.NONE);
      lbl_newLabel.setAlignment(SWT.CENTER);
      lbl_newLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
      lbl_newLabel.setFont(SWTResourceManager.getFont("Lucida Grande", 13, SWT.NORMAL));
      // create expression or node specific composites
      if (obj instanceof CalculationExpression)
      {
        lbl_newLabel.setText(CustomString.getString(CalculationActivator.getDefault().RESOURCE_BUNDLE,
            "com.bichler.astudio.editor.calculation.dialog.additem.expression"));
        final Text text = new Text(newExpression, SWT.BORDER);
        if (obj.getContent() != null)
        {
          text.setText(obj.getContent());
        }
        // text.setEnabled(true);
        text.addModifyListener(new ModifyListener()
        {
          @Override
          public void modifyText(ModifyEvent e)
          {
            obj.setContent(((Text) e.getSource()).getText());
            ((CalculationDPEditor) editor).setDirty(true);
          }
        });
        text.addMouseListener(new MouseAdapter()
        {
          @Override
          public void mouseDown(MouseEvent e)
          {
            selectDataPoint(obj);
          }
        });
        text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
      }
      else if (obj instanceof CalculationNode)
      {
        final Text text = new Text(newExpression, SWT.BORDER | SWT.READ_ONLY);
        if (obj.getContent() != null)
        {
          try
          {
            NodeId contentId = NodeId.parseNodeId(obj.getContent());
            // fetch displayname from nodeid
            Node node = opcServer.getServerInstance().getAddressSpaceManager().getNodeById(contentId);
            if (node != null)
            {
              LocalizedText displayname = node.getDisplayName();
              if (displayname != null && displayname.getText() != null)
              {
                text.setText(displayname.getText());
              }
            }
          }
          catch (IllegalArgumentException e)
          {
            // invalid nodeid
          }
        }
        lbl_newLabel.setText("Node");
        text.addMouseListener(new MouseAdapter()
        {
          @Override
          public void mouseDoubleClick(MouseEvent e)
          {
            CalculationOPCUANodeDialog dia = new CalculationOPCUANodeDialog(Display.getCurrent().getActiveShell(),
                ((CalculationNode) obj).getArrayIndex());
            dia.setInternalServer(opcServer.getServerInstance());
            dia.setSelectedNodeId(NodeId.parseNodeId(((CalculationNode) obj).getContent()));
            dia.setFormTitle(CustomString.getString(CalculationActivator.getDefault().RESOURCE_BUNDLE,
                "com.bichler.astudio.editor.calculation.dialog.additem.title"));
            if (dia.open() == Dialog.OK)
            {
              String name = dia.getSelectedDisplayName();
              NodeId nodeId = dia.getSelectedNodeId();
              int index = dia.getIndex();
              text.setText(name);
              ((CalculationNode) obj).setArrayIndex(index);
              obj.setContent(name);
              ((CalculationNode) obj).setContent(nodeId.toString());
              onCaculationNodeDialogFinis();
              ((CalculationDPEditor) editor).setDirty(true);
            }
          }

          @Override
          public void mouseDown(MouseEvent e)
          {
            selectDataPoint(obj);
          }
        });
        text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        DropTarget dropTarget = new DropTarget(text, DND.DROP_COPY);
        dropTarget.setTransfer(new Transfer[] { TextTransfer.getInstance() });
        dropTarget.addDropListener(new CalculationDnDNodeAdapter(text, obj));
      }
    }
    scrolledComposite.setMinSize(compositeExpression.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    Composite composite_3 = new Composite(compositeExpression, SWT.NONE);
    composite_3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
    // scrolledComposite.layout();
    compositeExpression.layout();
    scrolledComposite.layout();
  }

  protected void selectDataPoint(Object target)
  {
    ((CalculationDPEditor) editor).setDataPointSelection(target);
  }

  @Override
  protected void checkSubclass()
  {
    // Disable the check that prevents subclassing of SWT components
  }


  class CalculationDnDNodeAdapter extends OPCUADropTarget
  {
    private Text txtControl;
    private CalculationObject bo;

    public CalculationDnDNodeAdapter(Text text, CalculationObject obj)
    {
      this.txtControl = text;
      this.bo = obj;
    }

    @Override
    public void setDropValues(NodeId nodeId, String name)
    {
      if (NodeId.isNull(nodeId))
      {
        return;
      }
      this.bo.setContent(nodeId.toString());
      this.txtControl.setText(name);
      ((CalculationDPEditor) editor).setDirty(true);
    }
  }
}
