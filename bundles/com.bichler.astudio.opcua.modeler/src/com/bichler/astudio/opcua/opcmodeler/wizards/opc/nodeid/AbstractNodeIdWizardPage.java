package com.bichler.astudio.opcua.opcmodeler.wizards.opc.nodeid;

import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.opcfoundation.ua.builtintypes.ByteString;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.IdType;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.modeller.controls.CometCombo;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.utils.extern.DesignerUtils;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.richclientgui.toolbox.validation.IQuickFixProvider;
import com.richclientgui.toolbox.validation.ValidatingField;
import com.richclientgui.toolbox.validation.string.StringValidationToolkit;
import com.richclientgui.toolbox.validation.validator.IFieldValidator;

import opc.sdk.core.node.NodeIdMode;
import opc.sdk.core.node.NodeIdUtil;

public abstract class AbstractNodeIdWizardPage extends WizardPage implements NodeIdValidationPage {
	protected NodeId initId;
	protected Button btnNumeric;
	protected Button btnString;
	protected Button btnUUID;
	protected Button btnOpaque;
	protected Group grpTyp;
	protected ValidatingField<String> txtNodeId;
	protected Group group;
	protected CometCombo comboNodeId;
	protected Button btnGenerate;
	protected IdType selectedType;

	protected AbstractNodeIdWizardPage(String title, NodeId id) {
		super(title);
		if (!NodeId.isNull(id)) {
			this.initId = id;
		} else {
			this.initId = NodeId.NULL;
		}
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(1, false));
		grpTyp = new Group(container, SWT.NONE);
		grpTyp.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "OmronTcpTypePart.lbl_varType.text"));
		grpTyp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		grpTyp.setLayout(new FillLayout(SWT.VERTICAL));
		this.btnNumeric = new Button(grpTyp, SWT.RADIO);
		btnNumeric.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.number"));
		this.btnString = new Button(grpTyp, SWT.RADIO);
		btnString.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.text"));
		this.btnUUID = new Button(grpTyp, SWT.RADIO);
		btnUUID.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.uuid"));
		this.btnOpaque = new Button(grpTyp, SWT.RADIO);
		btnOpaque.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.opaque"));
		createComboTextSection(container);
		IdType sT = getInitId().getIdType();
		switch (sT) {
		case Numeric:
			this.btnNumeric.setSelection(true);
			break;
		case Guid:
			this.btnUUID.setSelection(true);
			break;
		case Opaque:
			this.btnOpaque.setSelection(true);
			break;
		case String:
			this.btnString.setSelection(true);
			break;
		}
		selectedType = sT;
		setNamespaceValue(getInitId().getNamespaceIndex());
		setValue(getInitId());
		setHandler();
	}

	@Override
	public boolean isPageComplete() {
		if (!btnNumeric.getSelection() && !btnString.getSelection() && !btnUUID.getSelection()
				&& !btnOpaque.getSelection()) {
			return false;
		}
		if (txtNodeId.getContents() == null) {
			return false;
		}
		if (txtNodeId.getContents().isEmpty()) {
			return false;
		}
		boolean isValid = txtNodeId.isValid();
		if (!isValid) {
			return false;
		}
		return true;
	}

	public NodeId getNewNodeId() {
		return (NodeId) this.txtNodeId.getControl().getData(selectedType.name());
	}

	@Override
	public void setAllow(boolean allowSomething) {
		this.isKeyInput = allowSomething;
	}

	public abstract NodeIdTypeFieldValidator<String> getNodeIdTypeFieldValidator();

	protected NodeId getInitId() {
		return this.initId;
	}

	protected abstract CometCombo createComboNodeId(Composite parent);

	protected void setValue(NodeId nodeId) {
		this.txtNodeId.getControl().setData(nodeId.getIdType().name(), nodeId);
		switch (nodeId.getIdType()) {
		case Numeric:
		case Guid:
			this.txtNodeId.setContents(nodeId.getValue().toString());
			break;
		case Opaque:
			this.txtNodeId.setContents(Arrays.toString((byte[]) ((ByteString) nodeId.getValue()).getValue()));
			break;
		case String:
			this.txtNodeId.setContents((String) nodeId.getValue());
			break;
		}
	}

	protected abstract void validateNodeIdValueText();

	private void createComboTextSection(Composite container) {
		group = new Group(container, SWT.NONE);
		group.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		group.setLayout(new GridLayout(2, false));
		Label space1 = new Label(group, SWT.NONE);
		space1.setText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NamespaceDialog.lbl_nameSpaces.text"));
		this.comboNodeId = createComboNodeId(group);
		GridDataFactory.fillDefaults().align(GridData.FILL, GridData.CENTER).grab(true, false)
				.applyTo(this.comboNodeId);
		Label lblIdentifier = new Label(group, SWT.NONE);
		lblIdentifier.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.identifier"));
		int DECORATOR_POSITION = SWT.TOP | SWT.RIGHT;
		int DECORATOR_MARGIN_WIDTH = 1;
		StringValidationToolkit nodeIdValToolkit = new StringValidationToolkit(DECORATOR_POSITION,
				DECORATOR_MARGIN_WIDTH, true);
		this.txtNodeId = nodeIdValToolkit.createTextField(group, getNodeIdTypeFieldValidator(), false, "");
		Text txtControl = (Text) this.txtNodeId.getControl();
		GridDataFactory.fillDefaults().align(GridData.FILL, GridData.CENTER).grab(true, false).applyTo(txtControl);
		// space
		new Label(group, SWT.NONE);
		this.btnGenerate = new Button(group, SWT.PUSH);
		this.btnGenerate.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "form.genid"));
		GridDataFactory.fillDefaults().align(SWT.END, SWT.CENTER).applyTo(this.btnGenerate);
	}

	private void setHandler() {
		this.btnNumeric.addSelectionListener(new TypeSelection(IdType.Numeric));
		this.btnString.addSelectionListener(new TypeSelection(IdType.String));
		this.btnUUID.addSelectionListener(new TypeSelection(IdType.Guid));
		this.btnOpaque.addSelectionListener(new TypeSelection(IdType.Opaque));
		Text txtControl = (Text) this.txtNodeId.getControl();
		txtControl.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				isKeyInput = false;
			}

			@Override
			public void keyPressed(KeyEvent e) {
				isKeyInput = true;
			}
		});
		txtControl.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent e) {
				switch (selectedType) {
				case Guid:
				case Opaque:
					if (isKeyInput) {
						e.doit = false;
					}
					break;
				default:
					break;
				}
			}
		});
		txtControl.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				switch (selectedType) {
				case Numeric:
				case String:
					boolean isComplete = isPageComplete();
					setPageComplete(isComplete);
					break;
				default:
					break;
				}
			}
		});
		this.comboNodeId.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				validateNodeIdValueText();
			}
		});
		this.btnGenerate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				NodeIdMode ccNodeId = DesignerUtils.getPreferenceContinueCreateNodeIds();
				// neue nodeid
				NodeId newValue = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
						.getNodeFactory().showNextNodeId(comboNodeId.getSelectionIndex(), selectedType, ccNodeId);
				setValue(newValue);
			}
		});
	}

	private void setNamespaceValue(int namespaceIndex) {
		NamespaceTable uris = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
		String index = uris.getUri(namespaceIndex);
		this.comboNodeId.select(namespaceIndex);
	}

	public class NodeIdTypeFieldValidator<T> implements IFieldValidator<String> {
		public NodeIdTypeFieldValidator() {
		}

		@Override
		public String getErrorMessage() {
			return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeId.invalid.error");
		}

		@Override
		public String getWarningMessage() {
			return null;
		}

		@Override
		public boolean isValid(String value) {
			// check if the input string is empty or null
			if (selectedType == null) {
				return false;
			}
			NodeId nodeId = null;
			if (!isKeyInput) {
				String txtId = txtNodeId.getContents();
				String txtIndex = comboNodeId.getText();
				Object idValue = null;
				try {
					idValue = Integer.parseInt(txtId);
				} catch (NumberFormatException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
				}
				if (idValue == null) {
					idValue = txtIndex;
				}
				int nsIndex = -1;
				try {
					nsIndex = Integer.parseInt(txtIndex);
				} catch (NumberFormatException nfe) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, nfe);
				}
				if (nsIndex < 0) {
					return false;
				}
				nodeId = NodeIdUtil.createNodeId(nsIndex, idValue);
				if (nodeId == null) {
					return false;
				}
			} else {
				switch (selectedType) {
				case Numeric:
					Integer v = null;
					try {
						v = Integer.parseInt(value);
						nodeId = new NodeId(Integer.parseInt(comboNodeId.getText()), v);
						break;
					} catch (NumberFormatException e) {
						return false;
					}
				case String:
					nodeId = new NodeId(comboNodeId.getSelectionIndex(), value);
					break;
				}
			}
			if (nodeId == null) {
				return false;
			}
			txtNodeId.getControl().setData(nodeId.getIdType().name(), nodeId);

			return true;
		}

		@Override
		public boolean warningExist(String nodeId) {
			return false;
		}
	}

	public abstract IQuickFixProvider<String> getQuickFixProvider();

	class NodeIdFieldQuickFixProvider<T> implements IQuickFixProvider<String> {
		public NodeIdFieldQuickFixProvider() {
		}

		@Override
		public boolean doQuickFix(ValidatingField<String> value) {
			NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
			NodeIdMode ccNodeId = DesignerUtils.getPreferenceContinueCreateNodeIds();
			int index = nsTable.getIndex(comboNodeId.getText());
			NodeId nxtNodeId = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
					.getNodeFactory().showNextNodeId(index, selectedType, ccNodeId);
			setValue(nxtNodeId);
			return true;
		}

		@Override
		public String getQuickFixMenuText() {
			return CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "messages.error.nodeid");
		}

		@Override
		public boolean hasQuickFix(String value) {
			/*
			 * if (value != null) { return false; }
			 */
			return true;
		}
	}

	boolean isKeyInput = false;

	class TypeSelection extends SelectionAdapter {
		private final IdType type;

		public TypeSelection(IdType numeric) {
			this.type = numeric;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			selectedType = this.type;
			NodeId data = (NodeId) txtNodeId.getControl().getData(selectedType.name());
			if (data != null) {
				setValue(data);
			} else {
				validateNodeIdValueText();
			}
			boolean isComplete = isPageComplete();
			setPageComplete(isComplete);
		}
	}
}
