package com.bichler.astudio.log.view.provider;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.bichler.astudio.log.view.Activator;
import com.bichler.astudio.log.view.HBLog4jSharedImages;
import com.bichler.astudio.log.server.core.ASLog;
import com.bichler.astudio.log.server.util.ASLogConstants;

public class LevelColumnLabelProvider extends ColumnLabelProvider {

	private Image img_debug;
	private Image img_error;
	private Image img_info;
	private Image img_fatal;
	private Image img_warn;

	public LevelColumnLabelProvider() {
		this.img_debug = Activator.getImageDescriptor(HBLog4jSharedImages.DEBUG).createImage();
		this.img_error = Activator.getImageDescriptor(HBLog4jSharedImages.ERROR).createImage();
		this.img_info = Activator.getImageDescriptor(HBLog4jSharedImages.INFO).createImage();
		this.img_fatal = Activator.getImageDescriptor(HBLog4jSharedImages.FATAL).createImage();
		this.img_warn = Activator.getImageDescriptor(HBLog4jSharedImages.WARN).createImage();
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof ASLog) {
			String level = ((ASLog) element).getLevel();

			switch (level) {
			case ASLogConstants.DEBUG_LEVEL_NAME:
				return this.img_debug;
			case ASLogConstants.ERROR_LEVEL_NAME:
				return this.img_error;
			case ASLogConstants.INFO_LEVEL_NAME:
				return this.img_info;
			case ASLogConstants.FATAL_LEVEL_NAME:
				return this.img_fatal;
			case ASLogConstants.WARN_LEVEL_NAME:
				return this.img_warn;
			}

		}

		return null;
	}

	@Override
	public String getText(Object element) {
		return "";
	}

}
