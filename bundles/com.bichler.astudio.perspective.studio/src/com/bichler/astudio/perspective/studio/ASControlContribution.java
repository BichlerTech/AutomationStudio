package com.bichler.astudio.perspective.studio;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;

public class ASControlContribution extends WorkbenchWindowControlContribution {

	// Our data model.
	// private NetworkConfig configuration = new NetworkConfig();

	// Each of these corresponds to a widget in the ToolBar.
	private Action scanAction;
	private ImageAndTextContributionItem sourceCombo;
	private TextContributionItem ipText;

	@Override
	protected Control createControl(Composite parent) {

		setupContributionItems();

		// Let's not get our hands messy with SWT... add IActions or
		// IContributionItems to a ToolBarManager and let the ToolBarManager
		// create the SWT ToolBar.
		ToolBarManager manager = new ToolBarManager(SWT.FLAT | SWT.RIGHT);

		manager.add(scanAction);
		manager.add(sourceCombo);
		// manager.add(ipText);

		ToolBar toolBar = manager.createControl(parent);
		toolBar.setSize(100, 70);

		ToolItem item = createToolItem(toolBar, SWT.PUSH, "hallo",
				ASPerspectiveActivator.getImage(StudioImages.ICON_ABOUT),
				ASPerspectiveActivator.getImage(StudioImages.ICON_ABOUT), "auch hallo");
		// Highlight the ToolBar in red.
		// toolBar.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_RED));
		//manager.add(item);
		toolBar.pack ();
		return toolBar;
	}

	private ToolItem createToolItem(ToolBar toolBar, int type, String text, Image image, Image hotImage,
			String toolTipText) {
		ToolItem item = new ToolItem(toolBar, type);
		item.setText(text);
		item.setImage(image);
		item.setHotImage(hotImage);
		item.setToolTipText(toolTipText);

		return item;
	}

	private void setupContributionItems() {
		scanAction = new Action("Scan Host") {
			@Override
			public void run() {
				System.out.println("Scanning...");
				String host = sourceCombo.getComboControl().getText();
				// configuration.scanHost(host);
				System.out.println("Scanned!");
				refreshTexts();
			}
		};
		scanAction.setToolTipText("Scans the host for a configuration.");

		final SelectionListener comboListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ipText.getTextControl().setText("");

			}
		};
		sourceCombo = new ImageAndTextContributionItem("sourceCombo") {
			@Override
			public Control createControl(Composite parent) {
				// Let ComboContributionItem create the initial control.
				Control control = super.createControl(parent);
				// Now customize the Combo widget.
				Combo combo = getComboControl();
				// combo.setItems(configuration.getAvailableHosts());
				combo.addSelectionListener(comboListener);
				// Return the default control.
				return control;
			}
		};

		ipText = new TextContributionItem("ipText", SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);

	}

	private void refreshTexts() {
		ipText.getTextControl().setText("localhost");

	}

}
