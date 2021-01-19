package com.bichler.astudio.opcua.opcmodeler.editor.node;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.wb.swt.ResourceManager;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.core.user.util.UserUtils;
import com.bichler.astudio.opcua.components.ui.Studio_ResourceManager;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.utils.internationalization.CustomString;

import opc.sdk.core.node.Node;

public abstract class NodeEditorExtensionPart extends NodeEditorPart {

	private Section sctn_extension;
	private List<Text> listExtension;
	private List<Button> listRemoveButton;

	private Composite cmpt_extensionMain;

	public NodeEditorExtensionPart() {
		super();
	}

	@Override
	protected void createExtensionSection() {
		Node node = this.getEditorInput().getNode().getNode();
		NodeId nodeId = node.getNodeId();
		this.listExtension = new ArrayList<>();
		this.listRemoveButton = new ArrayList<>();

		sctn_extension = formToolkit.createSection(this.compositeContainer, Section.TWISTIE | Section.TITLE_BAR);
		sctn_extension.setText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorExtensionPart.sctn_extension.text")); //$NON-NLS-1$
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).applyTo(sctn_extension);
		sctn_extension.setExpanded(true);
		formToolkit.paintBordersFor(sctn_extension);

		cmpt_extensionMain = new Composite(sctn_extension, SWT.NONE);
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL).grab(true, false).applyTo(cmpt_extensionMain);
		formToolkit.adapt(cmpt_extensionMain);
		formToolkit.paintBordersFor(cmpt_extensionMain);
		sctn_extension.setClient(cmpt_extensionMain);

		GridLayout gl_composite_2 = new GridLayout(1, false);
		gl_composite_2.verticalSpacing = 3;
		gl_composite_2.horizontalSpacing = 20;
		cmpt_extensionMain.setLayout(gl_composite_2);

		// skip OPC UA default nodes
		if (nodeId.getNamespaceIndex() == 0) {
			// set invisible
			this.sctn_extension.setVisible(false);
		}
		// admin rights for extension section
		boolean isAdmin = UserUtils.testUserRights(1);
		if(!isAdmin) {
			this.sctn_extension.setVisible(false);
		}
		
	}

	@Override
	protected void doSaveFinish() {
		super.doSaveFinish();
		
		Node node = getEditorInput().getNode().getNode();

		ArrayList<String> extensions = new ArrayList<>();
		for (Text extensionFields : this.listExtension) {
			String ext = extensionFields.getText();
			if (ext.isEmpty()) {
				continue;
			}
			extensions.add(ext);
		}

		if (extensions.isEmpty()) {
			Studio_ResourceManager.NODE_EXTENSIONS.remove(node.getNodeId());
		} else {
			Studio_ResourceManager.NODE_EXTENSIONS.put(node.getNodeId(), extensions);
		}
	}

	@Override
	protected void setInputs() {
		super.setInputs();
		
		Node node = this.getEditorInput().getNode().getNode();
		ArrayList<String> extensions = Studio_ResourceManager.NODE_EXTENSIONS.get(node.getNodeId());
		// create controls for new extensions
		if (extensions == null) {
			extensions = new ArrayList<>();
			createCompositeExtension("");
		}
		// create controls for existing extensions
		else {
			for (String ext : extensions) {
				createCompositeExtension(ext);
			}
		}
		createCompositeAddExtension();

		// collapse if extension is empty
		if (extensions.isEmpty()) {
			this.sctn_extension.setExpanded(false);
		}
	}

	@Override
	protected void setHandlers() {
		super.setHandlers();
		
		this.sctn_extension.addExpansionListener(new ScrolledExpansionAdapter());
	}

	private void createCompositeExtension(String value) {
		Composite extension = new Composite(this.cmpt_extensionMain, SWT.NONE);
		extension.setLayout(new GridLayout(2, false));
		extension.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, true, false, 1, 1));
		formToolkit.adapt(extension);
		formToolkit.paintBordersFor(extension);

		createCompositeDeleteExtension(extension);

		final Text txt_extension = new Text(extension, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
		GridData gd_extension = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_extension.heightHint = 180;
		gd_extension.horizontalIndent = 5;
		txt_extension.setLayoutData(gd_extension);

		txt_extension.setText(value);
		formToolkit.adapt(txt_extension, true, false);

		extension.setData(txt_extension);

		txt_extension.addModifyListener(new ModifyListener() {
			boolean firstTime = true;

			ControlDecoration decorator;
			{
				decorator = new ControlDecoration(txt_extension, SWT.TOP);
				decorator.setDescriptionText("Not a valid XML");
				Image image = FieldDecorationRegistry.getDefault()
						.getFieldDecoration(FieldDecorationRegistry.DEC_WARNING).getImage();
				decorator.setImage(image);
			}

			@Override
			public void modifyText(ModifyEvent e) {
				if (!firstTime) {
					setDirty(true);
				}
				firstTime = false;
				enableRemove();

				String xml = txt_extension.getText();
				boolean isXML = isXMLLike(xml);
				if (isXML) {
					decorator.hide();
				} else {
					decorator.show();
				}
			}
		});

		this.listExtension.add(txt_extension);
//		enableRemove();
		txt_extension.notifyListeners(SWT.Modify, new Event());
	}

	private boolean isXMLLike(String xml) {
		boolean retBool = false;
		Pattern pattern;
		Matcher matcher;

		// REGULAR EXPRESSION TO SEE IF IT AT LEAST STARTS AND ENDS
		// WITH THE SAME ELEMENT
		final String XML_PATTERN_STR = "<(\\S+?)(.*?)>(.*?)</\\1>";

		// IF WE HAVE A STRING
		if (xml != null && xml.trim().length() > 0) {

			// IF WE EVEN RESEMBLE XML
			if (xml.trim().startsWith("<")) {

				pattern = Pattern.compile(XML_PATTERN_STR,
						Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);

				// RETURN TRUE IF IT HAS PASSED BOTH TESTS
				matcher = pattern.matcher(xml);
				retBool = matcher.matches();
			}
			// ELSE WE ARE FALSE
		}

		return retBool;
	}

	private void disposeCompositeExtension(Composite extension) {
		Text txt_data = (Text) extension.getData();
		listExtension.remove(txt_data);
		extension.dispose();
		cmpt_extensionMain.layout(true);
		computeScroll();
	}

	private void createCompositeAddExtension() {
		final Composite cmp_add = new Composite(this.cmpt_extensionMain, SWT.NONE);
		cmp_add.setLayout(new GridLayout(1, false));
		cmp_add.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
		formToolkit.adapt(cmp_add);
		formToolkit.paintBordersFor(cmp_add);

		Composite cmp_layer1 = new Composite(cmp_add, SWT.NONE);
		cmp_layer1.setLayout(new GridLayout(1, false));
		cmp_layer1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		formToolkit.adapt(cmp_layer1);
		formToolkit.paintBordersFor(cmp_layer1);

		Button btn_extAdd = new Button(cmp_layer1, SWT.NONE);
		btn_extAdd.setToolTipText(
				CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE, "NodeEditorPart.btn_add.toolTipText")); //$NON-NLS-1$
		GridData gd_btn_add = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_add.widthHint = 60;
		gd_btn_add.heightHint = 40;
		btn_extAdd.setLayoutData(gd_btn_add);
		btn_extAdd.setText(""); //$NON-NLS-1$
		btn_extAdd.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "icons/default_icons/add_32.png"));
		formToolkit.adapt(btn_extAdd, true, true);

		btn_extAdd.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean confirm = MessageDialog.openConfirm(getSite().getShell(), "Add",
						"Confirm to add an extension value!");
				if (!confirm) {
					return;
				}
				cmp_add.dispose();
				createCompositeExtension("");
				createCompositeAddExtension();
				cmpt_extensionMain.layout(true);
				computeScroll();
			}
		});
	}

	private void createCompositeDeleteExtension(final Composite extension) {
		Composite cmpRemove = new Composite(extension, SWT.NONE);
		cmpRemove.setLayout(new GridLayout(1, false));
		cmpRemove.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		formToolkit.adapt(cmpRemove);
		formToolkit.paintBordersFor(cmpRemove);

		final Button btn_extDelete = new Button(cmpRemove, SWT.NONE);
		btn_extDelete.setToolTipText(CustomString.getString(Activator.getDefault().RESOURCE_BUNDLE,
				"NodeEditorExtensionPart.btn_extDelete.toolTipText"));
		GridData gd_btn_delete = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_btn_delete.widthHint = 60;
		gd_btn_delete.heightHint = 40;
		btn_extDelete.setLayoutData(gd_btn_delete);
		btn_extDelete.setText(""); //$NON-NLS-1$
		btn_extDelete
				.setImage(ResourceManager.getPluginImage(Activator.PLUGIN_ID, "icons/default_icons/delete_32.png"));
		formToolkit.adapt(btn_extDelete, true, true);

		btn_extDelete.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean confirm = MessageDialog.openConfirm(getSite().getShell(), "Remove",
						"Confirm to remove the extension value!");
				if (!confirm) {
					return;
				}
				if (listRemoveButton.size() > 1) {
					listRemoveButton.remove(btn_extDelete);
					disposeCompositeExtension(extension);
				} else {
					listExtension.get(0).setText("");
				}
				enableRemove();
				setDirty(true);
			}

		});
		this.listRemoveButton.add(btn_extDelete);
	}

	private void enableRemove() {
		// enable buttons
		boolean enabled = false;
		if (this.listRemoveButton.size() > 1) {
			enabled = true;
		} else {
			enabled = !this.listExtension.get(0).getText().isEmpty();
		}
		for (Button button : this.listRemoveButton) {
			button.setEnabled(enabled);
		}
	}
}
