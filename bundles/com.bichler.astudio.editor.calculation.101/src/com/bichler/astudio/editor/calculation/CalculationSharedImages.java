/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Jacek Pospychala <jacek.pospychala@pl.ibm.com> - bug 202583
 *******************************************************************************/
package com.bichler.astudio.editor.calculation;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;

public final class CalculationSharedImages {

	private CalculationSharedImages() { // do nothing
	}

	public final static String ICONS_PATH = "icons/"; //$NON-NLS-1$
	public final static String ICONS_16_PATH = "img_16/"; //$NON-NLS-1$
	public final static String ICONS_24_PATH = "img_24/"; //$NON-NLS-1$
	
	public static final String ICON_CHECKED_0 = ICONS_PATH + ICONS_16_PATH + "checked_0.png"; //$NON-NLS-1$
	public static final String ICON_CHECKED_0_GRAY = ICONS_PATH + ICONS_16_PATH + "checked_0_gray.png"; //$NON-NLS-1$
	public static final String ICON_CHECKED_1 = ICONS_PATH + ICONS_16_PATH + "checked_1.png"; //$NON-NLS-1$
	public static final String ICON_CHECKED_1_GRAY = ICONS_PATH + ICONS_16_PATH + "checked_1_gray.png"; //$NON-NLS-1$
	
	public static final String ICON_DATEPOINT = ICONS_PATH + ICONS_16_PATH + "dp.png"; //$NON-NLS-1$
	public static final String ICON_DATEPOINTLEAF = ICONS_PATH + ICONS_16_PATH + "dpleaf.png"; //$NON-NLS-1$
	
	public static final String ICON_ADD = ICONS_PATH + ICONS_24_PATH + "add.png"; //$NON-NLS-1$
	public static final String ICON_DELETE = ICONS_PATH + ICONS_24_PATH + "delete.png"; //$NON-NLS-1$
	public static final String ICON_IMPORT = ICONS_PATH + ICONS_24_PATH + "import.png"; //$NON-NLS-1$
	
	
	
	
	
	
	
	public final static String SVG_SITE_PATH = "svg_site/"; //$NON-NLS-1$
	public final static String SVG_PALETTE_PATH = "palette/"; //$NON-NLS-1$
	public final static String BUTTONS_PATH = "buttons/";
	public final static String WIZARDS_PATH = "wizards/";

	
	
	public final static String ICONS_32_PATH = "img_32/"; //$NON-NLS-1$
	public final static String ICONS_48_PATH = "img_48/"; //$NON-NLS-1$
	
	
	
	
	
	public static final String ICON_SITE = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "site.png"; //$NON-NLS-1$
	public static final String ICON_LINEARGRADIENT = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "linear_gradient.png"; //$NON-NLS-1$
	public static final String ICON_RADIALGRADIENT = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "radial_gradient.png"; //$NON-NLS-1$
	public static final String ICON_STOP = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "stop.png"; //$NON-NLS-1$
	public static final String ICON_DEFS = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "defs.png"; //$NON-NLS-1$
	
	public static final String ICON_CIRCLE = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "circle.png"; //$NON-NLS-1$
	public static final String ICON_RECT = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "rounded_rect.png"; //$NON-NLS-1$
	public static final String ICON_A = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "link.png"; //$NON-NLS-1$
	public static final String ICON_TEXT = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "text.png"; //$NON-NLS-1$
	public static final String ICON_PATH = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "path.png"; //$NON-NLS-1$
	public static final String ICON_G = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "g.png"; //$NON-NLS-1$
	public static final String ICON_USE = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "use.png"; //$NON-NLS-1$
	public static final String ICON_SVG = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "svg.png"; //$NON-NLS-1$
	public static final String ICON_IMAGE = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "image.png"; //$NON-NLS-1$
	
	public static final String ICON_TRENDS = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "trends.png"; //$NON-NLS-1$
	public static final String ICON_SCRIPTS = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "scripts.png"; //$NON-NLS-1$
	public static final String ICON_COMET = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "comet.png"; //$NON-NLS-1$
	public static final String ICON_LANGS = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "langs.png"; //$NON-NLS-1$
	public static final String ICON_PROJECT = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "project.png"; //$NON-NLS-1$
	public static final String ICON_LOGIN = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "login.png"; //$NON-NLS-1$
	public static final String ICON_NAVIGATIONS = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "navigations.png"; //$NON-NLS-1$
	public static final String ICON_WORKBENCHES = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "workbench.png"; //$NON-NLS-1$
	public static final String ICON_INDEX = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "index.png"; //$NON-NLS-1$
	public static final String ICON_LICENSE = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "license.png"; //$NON-NLS-1$
	public static final String ICON_DATASERVER = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "dataserver.png"; //$NON-NLS-1$
	public static final String ICON_SERVER = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "server.png"; //$NON-NLS-1$
	public static final String ICON_HOSTCONNECTION = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "hostconnection.png"; //$NON-NLS-1$
	public static final String ICON_HOSTCONNECTION_GREEN = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "hostconnection_green.png"; //$NON-NLS-1$
	public static final String ICON_SERVERSSH = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "serverssh.png"; //$NON-NLS-1$
	public static final String ICON_HMI = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "hmi.png"; //$NON-NLS-1$
	public static final String ICON_SQL = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "sql.png"; //$NON-NLS-1$
	public static final String ICON_OPCUACONFIGURATION = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "config.png"; //$NON-NLS-1$
	public static final String ICON_OPCUACERTIFICATE = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "certificate.png"; //$NON-NLS-1$
	public static final String ICON_OPCUAINFORMATIONMODEL = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "information.png"; //$NON-NLS-1$
	public static final String ICON_OPCUADRIVERDPS = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "datapoints.png"; //$NON-NLS-1$
	public static final String ICON_OPCUADRIVERS = ICONS_PATH + SVG_SITE_PATH + ICONS_16_PATH + "drivers.png"; //$NON-NLS-1$
	
	/** all palette icons */
	public static final String ICON_PALETTE = ICONS_PATH + SVG_PALETTE_PATH + ICONS_16_PATH + "palette.png"; //$NON-NLS-1$
	public static final String ICON_TEMPLATES = ICONS_PATH + SVG_PALETTE_PATH + ICONS_16_PATH + "templates.png"; //$NON-NLS-1$
	
	public static final String ICON_PALETTE_SELECTION = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "select.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_PATHEDIT = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "pathedit.png"; //$NON-NLS-1$
	
	public static final String ICON_PALETTE_ZOOMIN = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "zoomIn.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_ZOOMOUT = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "zoomOut.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_ZOOM1_1 = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "zoom1_1.png"; //$NON-NLS-1$
	//public static final String ICON_PALETTE_ZOOM_HOVER = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "zoom.png"; //$NON-NLS-1$
	//public static final String ICON_PALETTE_ZOOM_SELECTED = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "zoom.png"; //$NON-NLS-1$
	
	public static final String ICON_PALETTE_LASSOSELETION = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "lassoselection.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_LASSOSELETION_HOVER = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "lassoselection_hover.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_LASSOSELETION_SELECTED = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "lassoselection_selected.png"; //$NON-NLS-1$
	
	public static final String ICON_PALETTE_RECT = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "rect.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_RECT_HOVER = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "rect_hover.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_RECT_SELECTED = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "rect_selected.png"; //$NON-NLS-1$
	
	public static final String ICON_PALETTE_FREEHANDRECT = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "freehandrect.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_FREEHANDRECT_HOVER = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "freehandrect_hover.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_FREEHANDRECT_SELECTED = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "freehandrect_selected.png"; //$NON-NLS-1$
	
	public static final String ICON_PALETTE_SQUARE = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "square.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_SQUARE_HOVER = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "square_hover.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_SQUARE_SELECTED = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "square_selected.png"; //$NON-NLS-1$
	
	public static final String ICON_PALETTE_CIRCLE = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "circle.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_CIRCLE_HOVER = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "circle_hover.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_CIRCLE_SELECTED = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "circle_selected.png"; //$NON-NLS-1$

	public static final String ICON_PALETTE_ELLIPSE = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "ellipse.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_ELLIPSE_HOVER = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "ellipse_hover.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_ELLIPSE_SELECTED = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "ellipse_selected.png"; //$NON-NLS-1$

	public static final String ICON_PALETTE_FREEHANDELLIPSE = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "freehandellipse.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_FREEHANDELLIPSE_HOVER = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "freehandellipse_hover.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_FREEHANDELLIPSE_SELECTED = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "freehandellipse_selected.png"; //$NON-NLS-1$

	
	public static final String ICON_PALETTE_LINE = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "line.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_LINE_HOVER = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "line_hover.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_LINE_SELECTED = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "line_selected.png"; //$NON-NLS-1$
	
	public static final String ICON_PALETTE_PENCIL = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "pencil.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_PENCIL_HOVER = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "pencil_hover.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_PENCIL_SELECTED = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "pencil_selected.png"; //$NON-NLS-1$
	
	public static final String ICON_PALETTE_CONNECT = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "connect.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_CONNECT_HOVER = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "connect_hover.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_CONNECT_SELECTED = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "connect_selected.png"; //$NON-NLS-1$
	
	public static final String ICON_PALETTE_GRID = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "grid_g.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_GRID_HOVER = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "grid_g.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_GRID_SELECTED = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "grid_g.png"; //$NON-NLS-1$
	
	public static final String ICON_PALETTE_CLONE = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "clone.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_CLONE_HOVER = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "clone.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_CLONE_SELECTED = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "clone.png"; //$NON-NLS-1$
	
	public static final String ICON_PALETTE_WIREFRAME = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "wireframe.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_WIREFRAME_HOVER = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "wireframe.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_WIREFRAME_SELECTED = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "wireframe.png"; //$NON-NLS-1$
	
	public static final String ICON_PALETTE_FILE = ICONS_PATH + SVG_PALETTE_PATH + ICONS_16_PATH + "file.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_FOLDER = ICONS_PATH + SVG_PALETTE_PATH + ICONS_16_PATH + "folder.png"; //$NON-NLS-1$
	
	public static final String ICON_PALETTE_UNDO = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "undo.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_REDO = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "redo.png"; //$NON-NLS-1$
	
	public static final String ICON_COLORPROP_NONE = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "none.png"; //$NON-NLS-1$
	public static final String ICON_COLORPROP_FILL = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "fill.png"; //$NON-NLS-1$
	public static final String ICON_COLORPROP_LINEARGRADIENT = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "lineargradient.png"; //$NON-NLS-1$
	public static final String ICON_COLORPROP_RADIALGRADIENT = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "radialgradient.png";
	
	public static final String ICON_PALETTE_POLYLINE = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "polyline.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_POLYLINE_HOVER = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "polyline_hover.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_POLYLINE_SELECTED = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "polyline_selected.png"; //$NON-NLS-1$
	
	public static final String ICON_PALETTE_POLYGON = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "polygon.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_POLYGON_HOVER = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "polygon_hover.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_POLYGON_SELECTED = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "polygon_selected.png"; //$NON-NLS-1$
	
	public static final String ICON_PALETTE_TEXT = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "text.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_TEXT_HOVER = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "text_hover.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_TEXT_SELECTED = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "text_selected.png"; //$NON-NLS-1$
	
	public static final String ICON_PALETTE_IMAGE = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "image.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_IMAGE_HOVER = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "image_hover.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_IMAGE_SELECTED = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "image_selected.png"; //$NON-NLS-1$
	
	public static final String ICON_PALETTE_DELETE = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "delete.png"; //$NON-NLS-1$
	
	public static final String ICON_PALETTE_MOVETOP = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "movetop.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_MOVEBOTTOM = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "movebottom.png"; //$NON-NLS-1$

	public static final String ICON_PALETTE_CONVERTTOPATH = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "converttopath.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_MAKELINK = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "makelink.png"; //$NON-NLS-1$
	
	public static final String ICON_PALETTE_ADDNODE = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "addnode.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_DELETENODE = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "deletenode.png"; //$NON-NLS-1$
	public static final String ICON_PALETTE_OPENCLOSEPATH = ICONS_PATH + SVG_PALETTE_PATH + ICONS_32_PATH + "openclosepath.png"; //$NON-NLS-1$
	
	/** */
	
	
	
	public static final String ICON_BUTTON_SERVER_START = ICONS_PATH + BUTTONS_PATH + "start.png"; //$NON-NLS-1$
	public static final String ICON_BUTTON_SERVER_START_SELECTED = ICONS_PATH + BUTTONS_PATH + "start_selected.png"; //$NON-NLS-1$
	public static final String ICON_BUTTON_SERVER_START_DISABLED = ICONS_PATH + BUTTONS_PATH + "start_disabled.png"; //$NON-NLS-1$
	
	public static final String ICON_BUTTON_SERVER_STOP = ICONS_PATH + BUTTONS_PATH + "stop.png"; //$NON-NLS-1$
	public static final String ICON_BUTTON_SERVER_STOP_SELECTED = ICONS_PATH + BUTTONS_PATH + "stop_selected.png"; //$NON-NLS-1$
	public static final String ICON_BUTTON_SERVER_STOP_DISABLED = ICONS_PATH + BUTTONS_PATH + "stop_disabled.png"; //$NON-NLS-1$
	
	public static final String ICON_BUTTON_SERVER_RUNNING = ICONS_PATH + BUTTONS_PATH + "started.png"; //$NON-NLS-1$
	public static final String ICON_BUTTON_SERVER_STOPPED = ICONS_PATH + BUTTONS_PATH + "stopped.png"; //$NON-NLS-1$
	
	public static final String ICON_WIZARD_OPC_UA_SERVER_ADD = ICONS_PATH + WIZARDS_PATH + "server_wizard_banner.png"; //$NON-NLS-1$
	public static final String ICON_WIZARD_PROJECT_ADD = ICONS_PATH + WIZARDS_PATH + "project_wizard_banner.png"; //$NON-NLS-1$
	public static final String ICON_WIZARD_SCRIPT_ADD = ICONS_PATH + WIZARDS_PATH + "script_wizard_banner.png"; //$NON-NLS-1$
	public static final String ICON_WIZARD_CERTIFICATE_ADD = ICONS_PATH + WIZARDS_PATH + "certificate_wizard_banner.png"; //$NON-NLS-1$
	public static final String ICON_WIZARD_HMISERVER_ADD = ICONS_PATH + WIZARDS_PATH + "hmiserver_wizard_banner.png"; //$NON-NLS-1$
	public static final String ICON_WIZARD_DRIVER_ADD = ICONS_PATH + WIZARDS_PATH + "driver_wizard_banner.png"; //$NON-NLS-1$
	
	public static final String IMAGE_BG_RIGHT = ICONS_PATH + SVG_SITE_PATH + "default/sidebg.png";
	public static final String IMAGE_ICONSET = ICONS_PATH + SVG_SITE_PATH + "default/iconset1.png";
	public static final String IMAGE_ICONSET_SELECTED = ICONS_PATH + SVG_SITE_PATH + "default/iconset2.png";
	
	public static ImageDescriptor getImageDescriptor(String key) {
		return CalculationActivator.getDefault().getImageRegistry().getDescriptor(key);
	}

	public static Image getImage(String key) {
		return CalculationActivator.getDefault().getImageRegistry().get(key);
	}

}
