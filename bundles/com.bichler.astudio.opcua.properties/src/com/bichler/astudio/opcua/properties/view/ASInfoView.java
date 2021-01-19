package com.bichler.astudio.opcua.properties.view;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.ViewPart;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.opcua.components.ui.Studio_ResourceManager;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelBrowserView;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;

public class ASInfoView extends ViewPart implements ISelectionListener {

	public static final String ID = "com.bichler.astudio.properties.view.infos";
	private Text info = null;
	private NodeId actNID = null;

	@Override
	public void createPartControl(Composite parent) {
		info = new Text(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
		info.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		getSite().getPage().addSelectionListener(this);
		info.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				// the text has changed
				String infoStr = info.getText();
				// replace json chars
				infoStr = infoStr.replaceAll("\\[", "&#91;");
				infoStr = infoStr.replaceAll("\\{", "&#123;");
				infoStr = infoStr.replaceAll("\\]", "&#93;");
				infoStr = infoStr.replaceAll("\\}", "&#125;");
				//infoStr = infoStr.replaceAll("\\<", "&lt;");
				//infoStr = infoStr.replaceAll("\\>", "&gt;");
				Studio_ResourceManager.NODE_INFOS.put(actNID, infoStr);
				ModelBrowserView view = (ModelBrowserView) getSite().getPage().findView(ModelBrowserView.ID);
				view.setDirty(true);
				
				// new update view
				// get docu view
				ASDocuView docuView = (ASDocuView)getSite().getPage()
						.findView(ASDocuView.ID);
				if(docuView != null)
					docuView.updateDocu();
			}
		});
	}

	@Override
	public void dispose() {
		// remove view from selection service
		getSite().getPage().removeSelectionListener(this);
		super.dispose();
	}
	
	@Override
	public void setFocus() {

	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection sel) {
		if (sel instanceof TreeSelection) {
			Object obj = ((TreeSelection) sel).getFirstElement();
			if (obj instanceof BrowserModelNode) {
				actNID = ((BrowserModelNode) obj).getNode().getNodeId();
				if (actNID != null) {
					String infos = Studio_ResourceManager.NODE_INFOS.get(actNID);
					if (infos != null && !infos.isEmpty()) {
						String docuPath = Studio_ResourceManager.getInfoModellerDokuPath();
						// remove json replacements						
						infos = infos.replaceAll("&#91;", "\\[");
						infos = infos.replaceAll("&#123;", "\\{");
						infos = infos.replaceAll("&#93;", "\\]");
						infos = infos.replaceAll("&#125;", "\\}");
						infos = infos.replaceAll("&#125;", "\\}");
						//infos = infos.replaceAll("&lt;", "\\<");
						//infos = infos.replaceAll("&gt;", "\\>");
						//infos = infos.replaceAll("src=\"", "src=\"" +docuPath.replaceAll("\\\\", "\\\\\\\\") + "\\\\");
						info.setText(infos);
					} else {
						info.setText("");
					}
				} else {
					info.setText("");
				}
			}
		}
	}

}
