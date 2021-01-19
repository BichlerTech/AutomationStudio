package com.bichler.astudio.opcua.wizard.page;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;

import com.bichler.astudio.opcua.OPCUAActivator;
import com.bichler.astudio.opcua.widget.model.AdvancedConfigurationNode;
import com.bichler.astudio.opcua.widget.model.AdvancedRootConfigurationNode;
import com.bichler.astudio.utils.internationalization.CustomString;

public class NewAdvancedTreeItemPage extends WizardPage {

	private CCombo combo;
	private AdvancedRootConfigurationNode root;
	private AdvancedConfigurationNode selection;

	/**
	 * Create the wizard.
	 * 
	 * @param selection
	 * @param root
	 */
	public NewAdvancedTreeItemPage(AdvancedRootConfigurationNode root, AdvancedConfigurationNode selection) {
		super("Item Type");
		setTitle(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.countertree.selection"));
		setDescription(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
				"wizard.countertree.selection.description"));

		this.root = root;
		this.selection = selection;
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(2, false));

		Label lblTyp = new Label(container, SWT.NONE);
		lblTyp.setText(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE, "wizard.countertree.type"));

		this.combo = new CCombo(container, SWT.BORDER);
		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		setHandler();

		fillControl();

	}

	private DevGrType type = null;

	public DevGrType getType() {
		return this.type;
	}

	private void setHandler() {
		this.combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				String typeText = ((CCombo) e.getSource()).getText();

				if (typeText != null && !typeText.isEmpty()) {
					type = DevGrType.valueOf(typeText);
				}
			}
		});
	}

	private void fillControl() {

		if (this.root != null && this.selection == null) {
			this.combo.add(DevGrType.Group.name());
			this.combo.select(0);
		}
		if (this.selection != null) {

			if (this.selection.isGroup()) {
				this.combo.add(DevGrType.Group.name());
				this.combo.add(DevGrType.Device.name());
				this.combo.select(0);
			} else if (this.selection.isDevice()) {
				// this.combo.add(DevGrType.Group.name());
				this.combo.add(DevGrType.Device.name());
				this.combo.add(DevGrType.Counter.name());
				this.combo.select(0);
			} else if (this.selection.isState()) {
				this.combo.add(DevGrType.Counter.name());
				this.combo.select(0);
			}
		}

		this.combo.notifyListeners(SWT.Selection, new Event());
	}

	public enum DevGrType {
		Group, Device, Counter;
	}
}
