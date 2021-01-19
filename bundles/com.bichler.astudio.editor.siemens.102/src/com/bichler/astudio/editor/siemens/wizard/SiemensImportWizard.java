package com.bichler.astudio.editor.siemens.wizard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import com.bichler.astudio.editor.siemens.SiemensActivator;
import com.bichler.astudio.editor.siemens.SiemensDPEditor;
import com.bichler.astudio.editor.siemens.datenbaustein.SiemensDBResourceManager;
import com.bichler.astudio.editor.siemens.driver.datatype.SIEMENS_DATA_TYPE;
import com.bichler.astudio.editor.siemens.model.AbstractSiemensNode;
import com.bichler.astudio.editor.siemens.wizard.page.SiemensImportWizardPage;
import com.bichler.astudio.editor.siemens.xml.SiemensEntryModelNode;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.utils.internationalization.CustomString;
import com.bichler.opc.driver.siemens.communication.SiemensAreaCode;

public class SiemensImportWizard extends Wizard {
	private IFileSystem filesystem;
	private SiemensImportWizardPage importPage;
	private SiemensDBResourceManager structManager;

	public SiemensImportWizard(SiemensDBResourceManager structManager) {
		setWindowTitle(CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
				"com.bichler.astudio.editor.siemens.wizard.import.title"));
		this.structManager = structManager;
	}

	@Override
	public void addPages() {
		this.importPage = new SiemensImportWizardPage(this.filesystem, structManager);
		addPage(this.importPage);
	}

	@Override
	public boolean performFinish() {
		TableViewer tv = null;
		IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActiveEditor();
		if (activeEditor instanceof SiemensDPEditor) {
			tv = ((SiemensDPEditor) activeEditor).getTableViewer();
		} else {
			System.out.println("wrong active data point editor!");
			return false;
		}
		// List<AbstractSiemensNode> items = (ArrayList<AbstractSiemensNode>)
		// treeViewer
		// .getInput();
		AbstractSiemensNode input = this.importPage.getInput();
		Map<String, AbstractSiemensNode> objectMapping = new HashMap<String, AbstractSiemensNode>();
		List<AbstractSiemensNode> activated = new ArrayList<>();
		AbstractSiemensNode root = input.fillActiveAll(activated, objectMapping);
		String symbAddrName = root.getName();
		// String symbolAddressName = "";
		String symbolAddress = "";
		if (symbAddrName != null) {
			// symbolAddressName = SiemensAreaCode.DB.name();
			symbolAddress = symbAddrName.replaceFirst(SiemensAreaCode.DB.name(), "").trim();
		}
		activated.remove(root);
		List<SiemensEntryModelNode> model = new ArrayList<>();
		SiemensEntryModelNode last = null;
		for (AbstractSiemensNode item : activated) {
			// String datatype = ;
			SIEMENS_DATA_TYPE dt = SIEMENS_DATA_TYPE.getTypeFromString(item.getDataType());
			switch (dt) {
			case STRUCT:
			case UNDEFINED:
				// continue;
			case ARRAY:
				// continue;
			case BOOL:
				break;
			case BYTE:
				break;
			case CHAR:
				break;
			case COUNTER:
				break;
			case DATE:
				break;
			case DATE_AND_TIME:
				break;
			case DINT:
				break;
			case DWORD:
				break;
			case INT:
				break;
			case LREAL:
				break;
			case REAL:
				break;
			case STRING:
				break;
			case STRINGCHAR:
				break;
			case TIME:
				break;
			case TIMER:
				break;
			case TIME_OF_DAY:
				break;
			case WORD:
				break;
			default:
				break;
			}
			SiemensEntryModelNode semn = new SiemensEntryModelNode();
			// semn.setId(++maxNodeId);
			float i = item.getAddressIndex();
			// float i2 = item.getIndex();
			// semn.setNEW_INDEX(item.getIndex());
			semn.setIndex(i);
			semn.setActive(true);
			// semn.setAddress(item.getAddress());
			semn.setAddressType(SiemensAreaCode.DB);
			semn.setAddress(symbolAddress);
			String dtype = item.getDataType();
			semn.setDataType(dtype);
			// if (item instanceof SiemensArrayNode) {
			// int dim = ((SiemensArrayNode) item).getDimension();
			// semn.setParsedDataType(SiemensModelParser.DATATYPE_ARRAY + "["
			// + dim + "]");
			// }
			semn.setDescription(item.getDescription());
			semn.setSymbolName(item.getSymbolName());
			semn.setLabelImage(item.getLabelImage());
			model.add(semn);
			semn.setNeighbor(last);
			last = semn;
		}
		// for(SiemensEntryModelNode node : model){
		// node.calculateIndex();
		// }
		@SuppressWarnings("unchecked")
		List<SiemensEntryModelNode> oldModel = (List<SiemensEntryModelNode>) tv.getInput();
		if (oldModel != null && !oldModel.isEmpty()) {
			boolean yes = MessageDialog.openQuestion(getShell(),
					CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
							"com.bichler.astudio.editor.siemens.wizard.import.dialog.message.title"),
					CustomString.getString(SiemensActivator.getDefault().RESOURCE_BUNDLE,
							"com.bichler.astudio.editor.siemens.wizard.import.dialog.message.description"));
			if (!yes) {
				oldModel.addAll(model);
			} else {
				tv.setInput(model);
			}
		} else {
			tv.setInput(model);
		}
		tv.refresh(true);
		return true;
	}

	public void setFilesystem(IFileSystem filesystem) {
		this.filesystem = filesystem;
	}
}
