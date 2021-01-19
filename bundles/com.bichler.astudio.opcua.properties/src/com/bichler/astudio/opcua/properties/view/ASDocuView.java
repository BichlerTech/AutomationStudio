package com.bichler.astudio.opcua.properties.view;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.astudio.opcua.opcmodeler.commands.handler.nodes.ExportDocuElementsHandler;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelBrowserView;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.UADataTypeNode;
import opc.sdk.core.node.UAMethodNode;
import opc.sdk.core.node.UAObjectNode;
import opc.sdk.core.node.UAObjectTypeNode;
import opc.sdk.core.node.UAVariableNode;
import opc.sdk.core.node.UAVariableTypeNode;

public class ASDocuView extends ViewPart implements ISelectionListener {

	public static final String ID = "com.bichler.astudio.properties.view.docu";
	private Browser info = null;
	private ISelection selection;

	@Override
	public void createPartControl(Composite parent) {
		info = new Browser(parent, SWT.BORDER | SWT.WRAP);
		info.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		info.addProgressListener(new ProgressListener() {
            @Override
            public void completed(ProgressEvent event) {
            }
            @Override
            public void changed(ProgressEvent event) {
            }
        });
		new SelectNodeFunction (info, "selectNodeFunction");
		// add view to selection service
		getSite().getPage().addSelectionListener(this);
	}

	@Override
	public void setFocus() {

	}	
	
	public Browser getInfo() {
		return this.info;
	}
	
	

	@Override
	public void dispose() {
		// remove view from selection service
		getSite().getPage().removeSelectionListener(this);
		super.dispose();
	}

	public void updateDocu() {
		printDocu(selection);
	}
	
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection sel) {
		if (sel instanceof TreeSelection) {
			this.selection = sel;
			printDocu(sel);
		}
	}
	
	private void printDocu(ISelection sel) {
		if (sel instanceof TreeSelection) {
			Object obj = ((TreeSelection) sel).getFirstElement();
			if (obj instanceof BrowserModelNode) {
				Node node = ((BrowserModelNode) obj).getNode();
				switch (node.getNodeClass()) {
				case VariableType:
					info.setText(ExportDocuElementsHandler.createVariableTypeHTML((UAVariableTypeNode) node));
					break;
				case Variable:
					info.setText(ExportDocuElementsHandler.createVariableHTML((UAVariableNode) node));
					break;
				case ObjectType:
					info.setText(ExportDocuElementsHandler.createObjectTypeHTML((UAObjectTypeNode) node));
					break;
				case Object:
					info.setText(ExportDocuElementsHandler.createObjectHTML((UAObjectNode) node));
					break;
				case Method:
					info.setText(ExportDocuElementsHandler.createMethodHTML((UAMethodNode) node));
					break;
				case DataType:
					info.setText(ExportDocuElementsHandler.createDataTypeHTML((UADataTypeNode) node));
					break;
				default:
					info.setText("");
					break;
				}

			}
		}
	}

	// Called by JavaScript
    class SelectNodeFunction extends BrowserFunction {
    	Browser browser = null;
        
    	SelectNodeFunction (Browser browser, String name) {
           super (browser, name);
           this.browser = browser;
       }
       public Object function (Object[] arguments) {
           NodeId reference = NodeId.parseNodeId(arguments[0].toString());
           ModelBrowserView view = (ModelBrowserView) getSite().getWorkbenchWindow().getActivePage()
					.findView(ModelBrowserView.ID);
			view.selectNode(reference);
           return null;
       }
    }
}
