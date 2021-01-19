package com.bichler.astudio.javafx.component.editor;

import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.views.properties.PropertySheetPage;

import javafx.application.Platform;
import javafx.embed.swt.FXCanvas;
import javafx.scene.Scene;

public abstract class FXEditPart extends GraphicalEditorWithFlyoutPalette implements IEditorPart {
	private boolean isDirty = false;
	private FXCanvas fxCanvas;
	private PropertySheetPage propertyPage = null;
	// private Scene scene;

	public FXEditPart() {
		Platform.setImplicitExit(false);
		setEditDomain(new DefaultEditDomain(this));
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {

		setSite(site);
		setInput(input);

		setPartName(input.getName());
	}

	@Override
	public boolean isDirty() {
		return this.isDirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		if (propertyPage == null)
	    {
	      propertyPage = new PropertySheetPage();
	    }
		this.fxCanvas = new FXCanvas(parent, SWT.NONE);
//		this.fxCanvas.setBackground(new Color(device, rgb));
		Scene scene = createScene();
		
		/**
		 * Solved because of set implicite(false)
		 */
		// Platform.runLater(new Runnable() {
		//
		// @Override
		// public void run() {
		// fxCanvas.setScene(scene);
		// }
		// });

		setScene(scene);
	}

	public void setScene(final Scene scene) {
		fxCanvas.setScene(scene);
	}

	@Override
	public void setPartName(String partName) {
		super.setPartName(partName);
	}

	public void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
		firePropertyChange(PROP_DIRTY);
	}

	@Override
	public FXEditorInput getEditorInput() {
		return (FXEditorInput) super.getEditorInput();
	}
	
	public abstract Scene createScene();
	
}
