package com.bichler.astudio.opcua.opcmodeler.dialogs;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.UADataTypeNode;
import opc.sdk.core.node.UAMethodNode;
import opc.sdk.core.node.UAObjectNode;
import opc.sdk.core.node.UAObjectTypeNode;
import opc.sdk.core.node.UAReferenceTypeNode;
import opc.sdk.core.node.UAVariableNode;
import opc.sdk.core.node.UAVariableTypeNode;
import opc.sdk.core.node.ViewNode;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.wb.swt.SWTResourceManager;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.AttributeWriteMask;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.NodeClass;

import com.bichler.astudio.utils.ui.swt.CheckBoxButton;

public class WriteMaskDialog_ extends Dialog {
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	private CheckBoxButton cb_accessLevel;
	private CheckBoxButton[] writemaskCheckboxes = null;
	private List<AttributeWriteMask> writeMask = null;
	private NodeClass nodeClass = null;
	private UnsignedInteger mask = null;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public WriteMaskDialog_(Shell parentShell) {
		super(parentShell);
		this.writeMask = new ArrayList<AttributeWriteMask>();
		this.writemaskCheckboxes = new CheckBoxButton[AttributeWriteMask.values().length - 2];
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		Form frm_mainForm = formToolkit.createForm(container);
		formToolkit.paintBordersFor(frm_mainForm);
		frm_mainForm.setText("WriteMasks");
		GridLayout gridLayout = new GridLayout(3, false);
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		frm_mainForm.getBody().setLayout(gridLayout);
		formToolkit.decorateFormHeading(frm_mainForm);
		this.createAttributeWriteMaskField(frm_mainForm.getBody());
		this.setInputs();
		return container;
	}

	/**
	 * Set the WriteMask & UserWritemask Checkboxes
	 */
	private void setInputs() {
		EnumSet<AttributeWriteMask> writeMaskToSelect = AttributeWriteMask.getSet(this.mask);
		for (int i = 0; i < this.writemaskCheckboxes.length; i++) {
			AttributeWriteMask mask = AttributeWriteMask.valueOf(this.writemaskCheckboxes[i].getText());
			this.writemaskCheckboxes[i].setChecked(writeMaskToSelect.contains(mask));
		}
	}

	/**
	 * Create the Checkboxes for all WriteMasks.
	 * 
	 * @param Parent Composite Parent
	 * @return Checkboxes
	 */
	private void createAttributeWriteMaskField(Composite parent) {
		// NO ATTRIBUTEWRITEMASK.NONE && NO ATTRIBUTEWRITEMASK.VALUE
		this.writemaskCheckboxes = new CheckBoxButton[AttributeWriteMask.values().length - 2];
		int i = 0;
		GridData gd_cb_accessLevel = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		for (AttributeWriteMask writeMask : AttributeWriteMask.values()) {
			// fill all AttributeWriteMask except None
			if (!(AttributeWriteMask.None.equals(writeMask)
					|| AttributeWriteMask.ValueForVariableType.equals(writeMask))) {
				this.writemaskCheckboxes[i] = new CheckBoxButton(parent, SWT.NONE);
				gd_cb_accessLevel.widthHint = 190;
				this.writemaskCheckboxes[i].setLayoutData(gd_cb_accessLevel);
				formToolkit.adapt(this.writemaskCheckboxes[i]);
				formToolkit.paintBordersFor(this.writemaskCheckboxes[i]);
				this.writemaskCheckboxes[i].setText(writeMask.name());
				// check for valid attribute
				if (!isAttributeSupported(writeMask)) {
					this.writemaskCheckboxes[i].setEnabled(false);
				}
				i++;
			}
		}
		return;
	}

	/**
	 * Checks if the Attribute is supported to enable a checkbox.
	 * 
	 * @param WriteMask WriteMask Attribute to check.
	 * @return TRUE if the Node supports the Attribute, otherwise FALSE.
	 */
	private boolean isAttributeSupported(AttributeWriteMask writeMask) {
		// create a node from the NodeClass ConstructorParameter
		Node node = null;
		switch (this.nodeClass) {
		case DataType:
			node = new UADataTypeNode();
			break;
		case Object:
			node = new UAObjectNode();
			break;
		case ObjectType:
			node = new UAObjectTypeNode();
			break;
		case Variable:
			node = new UAVariableNode();
			break;
		case VariableType:
			node = new UAVariableTypeNode();
			break;
		case Method:
			node = new UAMethodNode();
			break;
		case ReferenceType:
			node = new UAReferenceTypeNode();
			break;
		case View:
			node = new ViewNode();
			break;
		case Unspecified:
			break;
		default:
			break;
		}
		// Activate the supported Attributes
		switch (writeMask) {
		case AccessLevel:
			return node.supportsAttribute(Attributes.AccessLevel);
		case ArrayDimensions:
			return node.supportsAttribute(Attributes.ArrayDimensions);
		case BrowseName:
			return node.supportsAttribute(Attributes.ArrayDimensions);
		case ContainsNoLoops:
			return node.supportsAttribute(Attributes.ContainsNoLoops);
		case DataType:
			return node.supportsAttribute(Attributes.DataType);
		case Description:
			return node.supportsAttribute(Attributes.Description);
		case DisplayName:
			return node.supportsAttribute(Attributes.DisplayName);
		case EventNotifier:
			return node.supportsAttribute(Attributes.EventNotifier);
		case Executable:
			return node.supportsAttribute(Attributes.Executable);
		case Historizing:
			return node.supportsAttribute(Attributes.Historizing);
		case InverseName:
			return node.supportsAttribute(Attributes.InverseName);
		case IsAbstract:
			return node.supportsAttribute(Attributes.IsAbstract);
		case MinimumSamplingInterval:
			return node.supportsAttribute(Attributes.MinimumSamplingInterval);
		case NodeClass:
			return node.supportsAttribute(Attributes.NodeClass);
		case NodeId:
			return node.supportsAttribute(Attributes.NodeId);
		case Symmetric:
			return node.supportsAttribute(Attributes.Symmetric);
		case UserAccessLevel:
			return node.supportsAttribute(Attributes.UserAccessLevel);
		case UserExecutable:
			return node.supportsAttribute(Attributes.UserExecutable);
		case UserWriteMask:
			return node.supportsAttribute(Attributes.UserWriteMask);
		case ValueRank:
			return node.supportsAttribute(Attributes.ValueRank);
		case WriteMask:
			return node.supportsAttribute(Attributes.WriteMask);
		default:
			return false;
		}
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		parent.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(547, 326);
	}

	public NodeClass getNodeClass() {
		return nodeClass;
	}

	public void setNodeClass(NodeClass nodeClass) {
		this.nodeClass = nodeClass;
	}

	public UnsignedInteger getMask() {
		return mask;
	}

	public void setMask(UnsignedInteger mask) {
		this.mask = mask;
	}

	@Override
	protected void okPressed() {
		createAttributeWriteMasks();
		super.okPressed();
	}

	private void createAttributeWriteMasks() {
		for (int i = 0; i < this.writemaskCheckboxes.length; i++) {
			CheckBoxButton wm_Checkbox = this.writemaskCheckboxes[i];
			if (wm_Checkbox.isChecked()) {
				this.writeMask.add(AttributeWriteMask.valueOf(this.writemaskCheckboxes[i].getText()));
			}
		}
	}

	/**
	 * Return the WriteMask.
	 * 
	 * @return WriteMask
	 */
	public List<AttributeWriteMask> getWritemask() {
		return this.writeMask;
	}
}
