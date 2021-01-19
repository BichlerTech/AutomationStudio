package com.bichler.astudio;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
//import org.eclipse.ui.actions.ActionFactory;
//import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

//import com.bichler.astudio.nls.DesignerWorkbenchMessages;

/**
 * An action bar advisor is responsible for creating, adding, and disposing of
 * the actions added to a workbench window. Each window will be populated with
 * new actions.
 */
public class ApplicationActionBarAdvisor extends ActionBarAdvisor
{
//  private IWorkbenchAction undoAction;
//  private IWorkbenchAction redoAction;
//  private IWorkbenchAction cutAction;
//  private IWorkbenchAction copyAction;
//  private IWorkbenchAction pasteAction;
//  private IWorkbenchAction deleteAction;
//  private IWorkbenchAction selectAllAction;
//  private IWorkbenchAction findAction;
  // Actions - important to allocate these only in makeActions, and then use them
  // in the fill methods. This ensures that the actions aren't recreated
  // when fillActionBars is called with FILL_PROXY.
//  private IWorkbenchAction exitAction;
  // private IWorkbenchAction aboutAction;
//  private IWorkbenchAction newWindowAction;
  // private Action messagePopupAction;
  // private IWorkbenchAction introAction;

  public ApplicationActionBarAdvisor(IActionBarConfigurer configurer)
  {
    super(configurer);
  }

  protected void makeActions(final IWorkbenchWindow window)
  {
    register(ActionFactory.HELP_SEARCH.create(window));
    register(ActionFactory.DYNAMIC_HELP.create(window));
    //register(ActionFactory.LOCK_TOOL_BAR.create(window));
    register(ActionFactory.NEW.create(window));
//    undoAction = ActionFactory.UNDO.create(window);
//    register(undoAction);
//    redoAction = ActionFactory.REDO.create(window);
//    register(redoAction);
//    cutAction = ActionFactory.CUT.create(window);
//    register(cutAction);
//    copyAction = ActionFactory.COPY.create(window);
//    register(copyAction);
//    pasteAction = ActionFactory.PASTE.create(window);
//    register(pasteAction);
//    selectAllAction = ActionFactory.SELECT_ALL.create(window);
//    register(selectAllAction);
//    findAction = ActionFactory.FIND.create(window);
//    register(findAction);
//    deleteAction = ActionFactory.DELETE.create( window );
//    register( deleteAction );
//    // Creates the actions and registers them.
//    // Registering is needed to ensure that key bindings work.
//    // The corresponding commands keybindings are defined in the plugin.xml file.
//    // Registering also provides automatic disposal of the actions when
//    // the window is closed.
//    exitAction = ActionFactory.QUIT.create(window);
//    register(exitAction);
//    // aboutAction = ActionFactory.ABOUT.create(window);
//    // register(aboutAction);
//    newWindowAction = ActionFactory.OPEN_NEW_WINDOW.create(window);
//    register(newWindowAction);
//    register(ActionFactory.UNDO.create(window));
//    register(ActionFactory.REDO.create(window));
  //  register(ActionFactory.HELP_SEARCH.create(window));
  //  register(ActionFactory.DYNAMIC_HELP.create(window));
    // introAction = ActionFactory.INTRO.create(window);
    // register(introAction);
    // messagePopupAction = new MessagePopupAction("Open Message", window);
    // register(messagePopupAction);
  }

  protected void fillMenuBar(IMenuManager menuBar)
  {
    MenuManager fileMenu = new MenuManager("&File", IWorkbenchActionConstants.M_FILE);
    // XXX Window menu
    MenuManager windowMenu = new MenuManager("&Window", IWorkbenchActionConstants.M_WINDOW);
    MenuManager helpMenu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);
   
    menuBar.add(fileMenu);

    // XXX Window menu
    menuBar.add(windowMenu);

  //  ...
    
    // XXX Window menu
   // windowMenu.add(preferencesAction);
    
    // Help
    // XXX add an additions group because this is what SDK UI expects
    helpMenu.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
    helpMenu.add(new Separator());
//    helpMenu.add(aboutAction);
   // menuBar.add(createEditMenu());
    // MenuManager fileMenu = new MenuManager("&File",
    // IWorkbenchActionConstants.M_FILE);
    // MenuManager helpMenu = new MenuManager("&Help",
    // IWorkbenchActionConstants.M_HELP);
    // menuBar.add(fileMenu);
    // Add a group marker indicating where action set menus will appear.
    // menuBar.add(new GroupMarker(IWorkbenchActionConstants.MB_ADDITIONS));
     menuBar.add(helpMenu);
    // File
    // fileMenu.add(newWindowAction);
    // fileMenu.add(new Separator());
    // fileMenu.add(messagePopupAction);
    // fileMenu.add(new Separator());
    // fileMenu.add(exitAction);
    // Help
    // helpMenu.add(aboutAction);
    // helpMenu.add(introAction);
  }

  @Override
  protected void fillCoolBar(ICoolBarManager coolBar)
  {
//	  IToolBarManager toolBar = new ToolBarManager(SWT.FLAT | SWT.RIGHT);
//
//	  RetargetAction backAction = new RetargetAction("back", "&Back");
//	  backAction.setImageDescriptor(images.getImageDescriptor(ISharedImages.IMG_TOOL_BACK));
//
//	  ActionContributionItem backCI = new ActionContributionItem(backAction);
//	  backCI.setMode(ActionContributionItem.MODE_FORCE_TEXT);
//	  toolBar.add(backCI); 
//	  IToolBarManager toolbar = new ToolBarManager(SWT.FLAT | SWT.BOTTOM);
//      coolBar.add(new ToolBarContributionItem(toolbar, "main"));   
//      ActionContributionItem actionContributionItem = new ActionContributionItem(openViewAction);
//  	actionContributionItem.setMode(ActionContributionItem.MODE_FORCE_TEXT);
//      toolbar.add(actionContributionItem);
  }

  /**
   * Creates and returns the Edit menu.
   */
//  private MenuManager createEditMenu()
//  {
//    MenuManager menu = new MenuManager(DesignerWorkbenchMessages.Workbench_edit, IWorkbenchActionConstants.M_EDIT);
//    menu.add(new GroupMarker(IWorkbenchActionConstants.EDIT_START));
//    menu.add(undoAction);
//    menu.add(redoAction);
//    menu.add(new GroupMarker(IWorkbenchActionConstants.UNDO_EXT));
//    menu.add(new Separator());
//    menu.add(cutAction);
//    menu.add(copyAction);
//    menu.add(pasteAction);
//    menu.add(new GroupMarker(IWorkbenchActionConstants.CUT_EXT));
//    menu.add(new Separator());
//    menu.add(deleteAction);
//    menu.add(selectAllAction);
//    menu.add(new Separator());
//    menu.add(findAction);
//    menu.add(new GroupMarker(IWorkbenchActionConstants.FIND_EXT));
//    menu.add(new Separator());
//    menu.add(new GroupMarker(IWorkbenchActionConstants.EDIT_END));
//    menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
//    return menu;
//  }
}
