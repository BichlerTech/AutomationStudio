package com.bichler.astudio.opcua.opcmodeler.dialogs;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.AttributeWriteMask;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.NodeClass;

import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.UADataTypeNode;
import opc.sdk.core.node.UAMethodNode;
import opc.sdk.core.node.UAObjectNode;
import opc.sdk.core.node.UAObjectTypeNode;
import opc.sdk.core.node.UAReferenceTypeNode;
import opc.sdk.core.node.UAVariableNode;
import opc.sdk.core.node.UAVariableTypeNode;
import opc.sdk.core.node.ViewNode;

public class WriteMaskDialog extends Dialog {
	private List<AttributeWriteMask> writeMask = null;
	private Button[] writemaskCheckboxes = null;
	private NodeClass nodeClass = null;
	private UnsignedInteger mask = null;

	public WriteMaskDialog(Shell shell, NodeClass nodeClass, UnsignedInteger mask) {
		super(shell);
		this.nodeClass = nodeClass;
		this.writeMask = new ArrayList<AttributeWriteMask>();
		this.mask = mask;
	}

	/**
	 * Configures the Shell.
	 * 
	 * @param NewShell Shell to configure.
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.writemask.title"));
	}

	/**
	 * Creates the content of the WriteMaskDialog.
	 * 
	 * @param Parent Composite parent to fill with SWT controls.
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new GridLayout(1, false));
		createLabel(composite, CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"CreateVariableDialog.lbl_writeMask.text"));
		this.writemaskCheckboxes = createAttributeWriteMaskField(composite);
		setWriteMaskCheckboxes();
		return composite;
	}

	private void createAttributeWriteMasks() {
		for (int i = 0; i < this.writemaskCheckboxes.length; i++) {
			Button wm_Checkbox = this.writemaskCheckboxes[i];
			if (wm_Checkbox.getSelection()) {
				this.writeMask.add(AttributeWriteMask.valueOf(this.writemaskCheckboxes[i].getText()));
			}
		}
	}

	/**
	 * Create the Checkboxes for all WriteMasks.
	 * 
	 * @param Parent Composite Parent
	 * @return Checkboxes
	 */
	private Button[] createAttributeWriteMaskField(Composite parent) {
		Group attributeWritemaskGroup = new Group(parent, SWT.NONE);
		attributeWritemaskGroup.setLayout(new GridLayout(3, false));
		// NO ATTRIBUTEWRITEMASK.NONE && NO ATTRIBUTEWRITEMASK.VALUE
		Button[] cb_attributeWriteMask = new Button[AttributeWriteMask.values().length - 2];
		int i = 0;
		for (AttributeWriteMask writeMask : AttributeWriteMask.values()) {
			// fill all AttributeWriteMask except None
			if (!(AttributeWriteMask.None.equals(writeMask)
					|| AttributeWriteMask.ValueForVariableType.equals(writeMask))) {
				// create checkbox
				cb_attributeWriteMask[i] = new Button(attributeWritemaskGroup, SWT.CHECK);
				// set name of checkbox
				cb_attributeWriteMask[i].setText(writeMask.name());
				// check for valid attribute
				if (!isAttributeSupported(writeMask)) {
					cb_attributeWriteMask[i].setEnabled(false);
				}
				// layout data
				GridDataFactory.fillDefaults().applyTo(cb_attributeWriteMask[i]);
				i++;
			}
		}
		return cb_attributeWriteMask;
	}

	/**
	 * Creates a Label.
	 * 
	 * @param Parent  Parent Composite.
	 * @param Display Text Display for the Label.
	 */
	private void createLabel(Composite parent, String display) {
		Label label = new Label(parent, SWT.NONE);
		label.setText(display);
		GridDataFactory.fillDefaults().applyTo(label);
	}

	/**
	 * Return the WriteMask.
	 * 
	 * @return WriteMask
	 */
	public List<AttributeWriteMask> getWritemask() {
		return this.writeMask;
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

	@Override
	protected void okPressed() {
		createAttributeWriteMasks();
		super.okPressed();
	}

	/**
	 * Set the WriteMask & UserWritemask Checkboxes
	 */
	private void setWriteMaskCheckboxes() {
		EnumSet<AttributeWriteMask> writeMaskToSelect = AttributeWriteMask.getSet(this.mask);
		for (int i = 0; i < this.writemaskCheckboxes.length; i++) {
			AttributeWriteMask mask = AttributeWriteMask.valueOf(this.writemaskCheckboxes[i].getText());
			this.writemaskCheckboxes[i].setSelection(writeMaskToSelect.contains(mask));
		}
	}
}
