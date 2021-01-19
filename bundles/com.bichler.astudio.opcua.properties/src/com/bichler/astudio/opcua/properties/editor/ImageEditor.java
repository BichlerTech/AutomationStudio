package com.bichler.astudio.opcua.properties.editor;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryGroupRenderer;
import org.eclipse.nebula.widgets.gallery.DefaultGalleryItemRenderer;
import org.eclipse.nebula.widgets.gallery.Gallery;
import org.eclipse.nebula.widgets.gallery.GalleryItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.menus.IMenuService;
import org.eclipse.ui.part.EditorPart;

import com.bichler.astudio.opcua.components.ui.Studio_ResourceManager;

public class ImageEditor extends EditorPart {
	public static final String ID = "com.bichler.astudio.opcua.properties.editor.ImageEditor";
	private Gallery gallery;

	private String filterElementPNG = ".png";
	private String filterElementJPG = ".jpg";

	private Map<String, GalleryItem> filterGroups = new HashMap<String, GalleryItem>();

	public ImageEditor() {
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		this.setSite(site);
		this.setInput(input);
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		// Display display = new Display();
		Image image = null;// new Image(parent.getDisplay(), Program
//				.findProgram("jpg").getImageData()); //$NON-NLS-1$
		gallery = new Gallery(parent, SWT.V_SCROLL | SWT.MULTI);

		// Renderers
		DefaultGalleryGroupRenderer gr = new DefaultGalleryGroupRenderer();
		gr.setMinMargin(2);
		gr.setItemHeight(88);
		gr.setItemWidth(88);
		gr.setAutoMargin(true);
		gallery.setGroupRenderer(gr);
		gallery.setToolTipText("");

		DefaultGalleryItemRenderer ir = new DefaultGalleryItemRenderer();
		gallery.setItemRenderer(ir);

		String docu = Studio_ResourceManager.getInfoModellerDokuPath();

		IPath docuPath = new Path(docu);
		File f = new File(docu);

		String[] filterElements = { filterElementPNG, filterElementJPG };

		for (String felement : filterElements) {
			// This filter will only include files ending with .py
			FilenameFilter filter = new FilenameFilter() {
				@Override
				public boolean accept(File f, String name) {
					return name.endsWith(felement);
				}
			};

			// This is how to apply the filter
			String[] pngs = f.list(filter);
			if (pngs != null) {
				GalleryItem group = new GalleryItem(gallery, SWT.NONE);
				group.setText("Group " + felement.replace("\\.", "")); //$NON-NLS-1$
				group.setExpanded(true);
				filterGroups.put(felement, group);
				for (String pathname : pngs) {

					image = new Image(parent.getDisplay(), docuPath.append(pathname).toOSString());
					if (image != null) {
						GalleryItem item = new GalleryItem(group, SWT.NONE);
						item.setImage(image);
						item.setText(pathname);
						item.setExpanded(true);
					}
				}
			}
		}

		// Disable native tooltip
		gallery.setToolTipText(""); //$NON-NLS-1$

		// Implement a "fake" tooltip
		final Listener labelListener = new Listener() {
			public void handleEvent(Event event) {
				Label label = (Label) event.widget;
				Shell shell = label.getShell();
				switch (event.type) {
				case SWT.MouseDown:
					Event e = new Event();
					e.item = (TableItem) label.getData("_TABLEITEM"); //$NON-NLS-1$
					// Assuming Gallery is single select, set the selection as if
					// the mouse down event went through to the table
					gallery.setSelection(new GalleryItem[] { (GalleryItem) e.item });
					gallery.notifyListeners(SWT.Selection, e);
					shell.dispose();
					gallery.setFocus();
					break;
				case SWT.MouseExit:
					shell.dispose();
					break;
				}
			}
		};

		Listener tableListener = new Listener() {
			Shell tip = null;
			Label label = null;

			public void handleEvent(Event event) {
				switch (event.type) {
				case SWT.Dispose:
				case SWT.KeyDown:
				case SWT.MouseMove: {
					if (tip == null)
						break;
					tip.dispose();
					tip = null;
					label = null;
					break;
				}
				case SWT.MouseDown: {
					GalleryItem item = gallery.getItem(new Point(event.x, event.y));
					// copy name to clip-board
					if (item != null) {
						StringSelection selection = new StringSelection(item.getText());
						Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
						clipboard.setContents(selection, null);
					}
					break;
				}
				case SWT.MouseHover: {
					GalleryItem item = gallery.getItem(new Point(event.x, event.y));
					if (item != null) {
						if (tip != null && !tip.isDisposed())
							tip.dispose();
						tip = new Shell(parent.getDisplay(), SWT.ON_TOP | SWT.NO_FOCUS | SWT.TOOL);
						tip.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
						FillLayout layout = new FillLayout();
						layout.marginWidth = 2;
						tip.setLayout(layout);
						label = new Label(tip, SWT.NONE);
						label.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_FOREGROUND));
						label.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
						label.setData("_TABLEITEM", item); //$NON-NLS-1$
						label.setText(item.getText());
						label.addListener(SWT.MouseExit, labelListener);
						label.addListener(SWT.MouseDown, labelListener);
						Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);

						Point pt = gallery.toDisplay(event.x, event.y + 16);
						tip.setBounds(pt.x, pt.y, size.x, size.y);
						tip.setVisible(true);
					}
				}
				}
			}
		};
		gallery.addListener(SWT.Dispose, tableListener);
		gallery.addListener(SWT.KeyDown, tableListener);
		gallery.addListener(SWT.MouseMove, tableListener);
		gallery.addListener(SWT.MouseHover, tableListener);
		gallery.addListener(SWT.MouseDown, tableListener);

		ToolBarManager manager = (ToolBarManager) getEditorSite().getActionBars().getToolBarManager();
		IMenuService menuService = (IMenuService) getSite().getService(IMenuService.class);
		menuService.populateContributionManager(manager, "toolbar:com.bichler.astudio.properties.editor.images");
	}

	/**
	 * add galleryItem and update editor
	 */
	public void addGalleryItem(String path) {
		File pic = new File(path);

		String picType = "";
		if (path.endsWith(filterElementJPG))
			picType = filterElementJPG;
		else if (path.endsWith(filterElementPNG))
			picType = filterElementPNG;
		GalleryItem group = filterGroups.get(picType);
		if (group != null) {
			Image image = new Image(gallery.getDisplay(), path);
			if (image != null) {
				GalleryItem item = new GalleryItem(group, SWT.NONE);
				item.setImage(image);
				item.setText(pic.getName());
				item.setExpanded(true);
			}
		}
		gallery.update();
	}

	/**
	 * delete all selected images and refresh editor
	 */
	public void deleteFile() {
		if (gallery.getSelectionCount() <= 0) {
		} else {
			gallery.getSelection();
			String picsPath = Studio_ResourceManager.getInfoModellerDokuPath();
			for (GalleryItem gitem : gallery.getSelection()) {
				String path = new Path(picsPath).append(gitem.getText()).toOSString();
				File item2delete = new File(path);
				if (item2delete.exists())
					item2delete.delete();
				gallery.remove(gitem);
			}
			gallery.update();
		}

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
}
