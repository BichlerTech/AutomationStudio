package com.bichler.astudio.editor.allenbradley.wizard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import com.bichler.astudio.editor.allenbradley.AllenBradleyActivator;
import com.bichler.astudio.editor.allenbradley.AllenBradleyDPEditor;
import com.bichler.astudio.editor.allenbradley.datenbaustein.AllenBradleyDBResourceManager;
import com.bichler.astudio.editor.allenbradley.model.AbstractAllenBradleyNode;
import com.bichler.astudio.editor.allenbradley.xml.AllenBradleyEntryModelNode;
import com.bichler.astudio.filesystem.IFileSystem;
import com.bichler.astudio.utils.internationalization.CustomString;

public class AllenBradleyImportWizard extends Wizard
{
  private IFileSystem filesystem;
  private AllenBradleyImportWizardPage importPage;
  private AllenBradleyDBResourceManager structManager;

  public AllenBradleyImportWizard(AllenBradleyDBResourceManager structManager)
  {
    setWindowTitle(CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
        "com.bichler.astudio.editor.allenbradley.wizard.import.title"));
    this.structManager = structManager;
  }

  @Override
  public void addPages()
  {
    this.importPage = new AllenBradleyImportWizardPage(this.filesystem, structManager);
    addPage(this.importPage);
  }

  @Override
  public boolean performFinish()
  {
    TableViewer tv = null;
    IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
    // int maxNodeId = 1000;
    if (activeEditor instanceof AllenBradleyDPEditor)
    {
      // maxNodeId = ((allenbradleyDPEditor) activeEditor).getMaxNodeId();
      tv = ((AllenBradleyDPEditor) activeEditor).getTableViewer();
    }
    else
    {
      return false;
    }
    // List<AbstractSiemensNode> items = (ArrayList<AbstractSiemensNode>)
    // treeViewer
    // .getInput();
    AbstractAllenBradleyNode input = this.importPage.getInput();
    List<AbstractAllenBradleyNode> activated = new ArrayList<>();
    AbstractAllenBradleyNode root = input.fillActiveAll(activated);
    activated.remove(root);
    List<AllenBradleyEntryModelNode> model = new ArrayList<>();
    AllenBradleyEntryModelNode last = null;
    int index = 1;
    for (AbstractAllenBradleyNode item : activated)
    {
      AllenBradleyEntryModelNode semn = new AllenBradleyEntryModelNode();
      semn.setIndex(index);
      index++;
      semn.setSymbolName(item.getName());
      semn.setActive(true);
      String dtype = item.getDataType();
      semn.setDataType(dtype);
      semn.setDescription(item.getDescription());
      semn.setLabelImage(item.getLabelImage());
      model.add(semn);
      semn.setNeighbor(last);
      last = semn;
    }
    @SuppressWarnings("unchecked")
    List<AllenBradleyEntryModelNode> oldModel = (List<AllenBradleyEntryModelNode>) tv.getInput();
    if (oldModel != null && !oldModel.isEmpty())
    {
      boolean yes = MessageDialog.openQuestion(getShell(),
          CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
              "com.bichler.astudio.editor.allenbradley.wizard.import.title"),
          CustomString.getString(AllenBradleyActivator.getDefault().RESOURCE_BUNDLE,
              "com.bichler.astudio.editor.allenbradley.wizard.import.message.replacedp"));
      if (!yes)
      {
        oldModel.addAll(model);
      }
      else
      {
        tv.setInput(model);
      }
    }
    else
    {
      tv.setInput(model);
    }
    tv.refresh(true);
    return true;
  }

  public void setFilesystem(IFileSystem filesystem)
  {
    this.filesystem = filesystem;
  }
}
