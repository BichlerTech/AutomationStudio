package com.bichler.module;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.ICoolBarManager;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.bichler.astudio.language.handler.LanguageUtil;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}
	
	@Override
	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	@Override
	public void preWindowOpen() {
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(400, 300));
		configurer.setShowCoolBar(false);
		configurer.setShowStatusLine(false);
		configurer.setTitle("BTech Modules");
	}
	
	@Override
	public void postWindowCreate() {
		IContributionItem[] mItems, mSubItems;
		IMenuManager mm = getWindowConfigurer().getActionBarConfigurer().getMenuManager();
		mItems = mm.getItems();
		List<IContributionItem> items2Remove = new ArrayList<>();
		for (int i = 0; i < mItems.length; i++) {
			//System.err.println("--ITEM: "+mItems[i].getId());
			boolean remove = false;
			if (mItems[i] != null && mItems[i].getId() != null
					&& mItems[i].getId().compareTo("file") == 0) {
				remove = true;
			} else if (mItems[i] != null && mItems[i].getId() != null
					&& mItems[i].getId().compareTo("help") == 0) {
				remove = true;
			} else if (mItems[i] != null && mItems[i].getId() != null
					&& mItems[i].getId().compareTo("org.eclipse.ui.run") == 0) {
				remove = true;
			} else if (mItems[i] != null && mItems[i].getId() != null
					&& mItems[i].getId().compareTo("org.eclipse.search.menu") == 0) {
				remove = true;
			} else if (mItems[i] != null && mItems[i].getId() != null
					&& mItems[i].getId().compareTo("com.bichler.astudio.ansicht") == 0) {
				remove = true;
			}	
			
			if(remove) {
				items2Remove.add(mItems[i]);
			}
			/*
			if (mItems[i] instanceof MenuManager) {
				mSubItems = ((MenuManager) mItems[i]).getItems();
				for (int j = 0; j < mSubItems.length; j++) {
					if (mItems[i].getId().equals("file") || mItems[i].getId().equals("help")
							|| mItems[i].getId().equals("search")) {
						items2Remove.add(mItems[i]);
					}
				}
			} else {
				items2Remove.add(mItems[i]);
			}*/
		}
		for (IContributionItem item : items2Remove) {
			mm.remove(item);
		}

		items2Remove.clear();
		ICoolBarManager cm = getWindowConfigurer().getActionBarConfigurer().getCoolBarManager();
		mItems = cm.getItems();
		for (int i = 0; i < mItems.length; i++) {
			if (mItems[i] != null && mItems[i].getId() != null
					&& mItems[i].getId().compareTo("org.eclipse.search.searchActionSet") == 0) {
				items2Remove.add(mItems[i]);
			}
			/*else if(mItems[i] != null && mItems[i].getId() != null
					&& mItems[i].getId().compareTo("org.eclipse.debug.ui.launchActionSet") == 0) {
				items2Remove.add(mItems[i]);
			}*/
			
		}
		for (IContributionItem item : items2Remove) {
			cm.remove(item);
		}
		LanguageUtil.initializeLanguage();
	}
}
