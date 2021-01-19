package com.bichler.astudio.opcua.opcmodeler.commands.handler.nodes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.handlers.HandlerUtil;
import org.opcfoundation.ua.builtintypes.ExpandedNodeId;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.AccessLevel;
import org.opcfoundation.ua.core.Argument;
import org.opcfoundation.ua.core.EnumValueType;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.ReferenceNode;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSimpleField;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType;

import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.components.ui.Studio_ResourceManager;
import com.bichler.astudio.opcua.opcmodeler.Activator;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.ModelBrowserView;
import com.bichler.astudio.opcua.opcmodeler.views.modeldesignbrowser.nodes.BrowserModelNode;

import opc.sdk.core.enums.ValueRanks;
import opc.sdk.core.node.MethodNode;
import opc.sdk.core.node.NamingRuleType;
import opc.sdk.core.node.Node;
import opc.sdk.core.node.ObjectNode;
import opc.sdk.core.node.ReferenceTypeNode;
import opc.sdk.core.node.UADataTypeNode;
import opc.sdk.core.node.UAMethodNode;
import opc.sdk.core.node.UAObjectNode;
import opc.sdk.core.node.UAObjectTypeNode;
import opc.sdk.core.node.UAReferenceTypeNode;
import opc.sdk.core.node.UAVariableNode;
import opc.sdk.core.node.UAVariableTypeNode;
import opc.sdk.core.node.VariableNode;
import opc.sdk.server.service.node.UAServerVariableNode;

public class ExportDocuElementsHandler extends AbstractHandler {

//	private FileOutputStream out = null;
	private XWPFDocument document = null;

	private final String BTECH_HEADING1 = "BTECH_HEADING1";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		// get the selection (parent node)
		final TreeSelection selection = (TreeSelection) HandlerUtil.getActiveWorkbenchWindow(event)
				.getSelectionService().getSelection(ModelBrowserView.ID);
		Node selectedNode = ((BrowserModelNode) selection.getFirstElement()).getNode();

		FileDialog dialog = new FileDialog(Display.getDefault().getActiveShell(), SWT.SAVE);
		dialog.setFilterExtensions(new String[] { "*.docx", "*.html", "*.*" });
		String fileName = dialog.open();
		if (fileName == null) {
			return null;
		}

		if (fileName.endsWith(".docx")) {
			exportDoc(fileName, selectedNode);
		} else if (fileName.endsWith(".html")) {
			StringBuffer buffer = new StringBuffer();
			buffer.append("<html>");
			buffer.append("<head>");
			exportStyle(buffer, fileName);
			createJavaScriptHeader(buffer);
			buffer.append("</head>");
			buffer.append("<body style=\"width:100%; font-size: 12px; font-family:Arial;\">");
			buffer.append("<div style=\"width: 600px; height: 100%;\">");
			createBichlerHeaderHTML(buffer);
			exportIndex(selectedNode, buffer, fileName.replace(".html", ""));
			exportIcons(fileName);
			exportPics(selectedNode, fileName);
			File f = new File(fileName);
			exportHTML(selectedNode, buffer, f.getName().replace(".html", ""));
			createBichlerFooterHTML(buffer);
			buffer.append("</div>");
			buffer.append("</body>");
			buffer.append("</html>");

			
			// Write the Document in file system
			File file = new File(fileName);
			try (FileWriter fw = new FileWriter(file);){
				if (!file.exists())
					file.createNewFile();
				fw.write(buffer.toString());
				fw.flush();

			} catch (IOException iox) {

			}
		} else {

		}

		return null;
	}

	private static void createJavaScriptHeader(StringBuffer buffer) {
		buffer.append("<script>");
		buffer.append("function selectNodeFunction(strNid) {");
		// is it possible to chump to?
		buffer.append("alert(strNid);");
		//buffer.append("document.getElementById("demo").innerHTML = "Paragraph changed.";
		buffer.append("return false;}");
		buffer.append("</script>\n");
		
	}
	public static String createVariableTypeHTML(UAVariableTypeNode node) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<html>");
		buffer.append("<body style=\"width:100%; font-size: 12px; font-family:Arial;\">");
		buffer.append("<div style=\"width: 600px; height: 100%;\">");
		createBichlerHeaderHTML(buffer);
		String innerHTML = createVariableTypeInnerHTML(node);
		String docuPath = Studio_ResourceManager.getInfoModellerDokuPath();
		innerHTML = innerHTML.replaceAll("src=\"", "src=\"" + docuPath.replaceAll("\\\\", "\\\\\\\\") + "\\\\");
		buffer.append(innerHTML);
		createBichlerFooterHTML(buffer);
		buffer.append("</div>");
		buffer.append("</body>");
		buffer.append("</html>");
		return buffer.toString();
	}

	public static String createVariableTypeInnerHTML(UAVariableTypeNode node) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<h1 style=\"color: gray;\">");
		buffer.append("<a href=\"#" + node.getDisplayName().getText() + "\" id=\"" + node.getDisplayName().getText()
				+ "\" onClick=\"return selectNodeFunction('" + node.getNodeId() + "');\">");
		buffer.append(formatHTML(node.getDisplayName().getText()));
		buffer.append("</a>");
		buffer.append("</h1>");
		if (node.getDescription() != null && node.getDescription().getText() != null) {
			buffer.append("<h4>Beschreibung:</h4>");
			buffer.append(formatHTML(node.getDescription().getText()) + "<br/>");
		}
		String infos = Studio_ResourceManager.NODE_INFOS.get(node.getNodeId());
		if (infos != null && !infos.isEmpty()) {
			buffer.append("<h4>Zus&auml;tzliche Infos:</h4>");
			buffer.append(formatHTMLWithoutTags(infos));
			buffer.append("<br/>");
		}
		buffer.append("<div style=\"width: 100%; height: 10px;\">");
		buffer.append("</div>");
		buffer.append(
				"<table style=\"width:100%; border: 1px solid black; border-collapse: collapse; font-size: 10px; font-family:Arial;\">");
		buffer.append("<tr>");
		buffer.append("<td style=\"width: 240px; border: 1px solid black; font-weight: bold;\">");
		buffer.append("Attribute");
		buffer.append("</td>");
		buffer.append(
				"<td style=\"width: 120px; border-left: 1px solid black; border-bottom: 1px solid black; font-weight: bold;\">");
		buffer.append("Value");
		buffer.append("</td>");
		buffer.append("<td style=\"width: 240px; border-bottom: 1px solid black;\">");
		buffer.append("</td>");
		buffer.append("<td style=\"width: 340px; border-bottom: 1px solid black;\">");
		buffer.append("</td>");
		buffer.append("<td style=\"width: 240px; border-bottom: 1px solid black;\">");
		buffer.append("</td>");
		buffer.append("<td style=\"width: 240px; border-bottom: 1px solid black; border-right: 1px solid black;\">");
		buffer.append("</td>");
		buffer.append("</tr>");

		// row two
		buffer.append("<tr>");
		buffer.append("<td style=\" border: 1px solid black;\">");
		buffer.append("BrowseName");
		buffer.append("</td>");
		buffer.append("<td style=\" border-left: 1px solid black; border-bottom: 1px solid black;\" colspan=\"5\">");
		if (node != null && node.getBrowseName() != null)
			buffer.append(formatHTML(node.getBrowseName().toString()));
		buffer.append("</td>");

		buffer.append("</tr>");

		// row three
		buffer.append("<tr>");
		buffer.append("<td style=\"border: 1px solid black;\">");
		buffer.append("IsAbstract");
		buffer.append("</td>");
		buffer.append("<td style=\"border-left: 1px solid black; border-bottom: 1px solid black;\" >");
		buffer.append(node.getIsAbstract());
		buffer.append("</td>");
		buffer.append("<td style=\" border-bottom: 1px solid black;\" >");
		buffer.append("</td>");
		buffer.append("<td style=\" border-bottom: 1px solid black;\">");
		buffer.append("</td>");
		buffer.append("<td style=\"border-bottom: 1px solid black;\">");
		buffer.append("</td>");
		buffer.append("<td style=\"border-bottom: 1px solid black;\">");
		buffer.append("</td>");
		buffer.append("</tr>");

		// row four
		buffer.append("<tr>");
		buffer.append("<td style=\"border: 1px solid black; font-weight: bold;\">");
		buffer.append("References");
		buffer.append("</td>");
		buffer.append("<td style=\"border: 1px solid black; font-weight: bold;\">");
		buffer.append("Node<br>class");
		buffer.append("</td>");
		buffer.append("<td style=\"border: 1px solid black; font-weight: bold;\">");
		buffer.append("BrowseName");
		buffer.append("</td>");
		buffer.append("<td style=\"border: 1px solid black; font-weight: bold;\">");
		buffer.append("DataType");
		buffer.append("</td>");
		buffer.append("<td style=\"border: 1px solid black; font-weight: bold;\">");
		buffer.append("TypeDefinition");
		buffer.append("</td>");
		buffer.append("<td style=\"border: 1px solid black; font-weight: bold;\">");
		buffer.append("Modelling Rule");
		buffer.append("</td>");
		buffer.append("</tr>");

		ExpandedNodeId parentid = node.findTarget(Identifiers.HasSubtype, true);
		if (parentid != null) {
			Node nnode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
					.getNodeById(parentid);
			if (nnode != null) {
				// row five
				buffer.append("<tr>");
				buffer.append("<td style=\"border: 1px solid black;\" colspan=\"6\">");
				if (nnode != null && nnode.getBrowseName() != null)
					buffer.append("Subtype of " + formatHTML(nnode.getBrowseName().toString()));
				buffer.append("</td>");
				buffer.append("</tr>");
			}
		}

		for (ReferenceNode refnode : node.getReferences()) {

			boolean isSubtype = ServerInstance.getInstance().getServerInstance().getTypeTable()
					.isTypeOf(refnode.getReferenceTypeId(), Identifiers.HasSubtype);

			// we do not need parent references
			if (isSubtype && refnode.getIsInverse()) {
				continue;
			}

			Node nnode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
					.getNodeById(refnode.getTargetId());

			buffer.append("<tr>");
			buffer.append("<td style=\"border: 1px solid black;\">");

			Node reftypenode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
					.getNodeById(refnode.getReferenceTypeId());
			String refname = getReferenceName(refnode, (ReferenceTypeNode) reftypenode);
			if (refname != null) {
				buffer.append("<a href=\"#\" onClick=\"return selectNodeFunction('" + reftypenode.getNodeId() + "');\">");
				buffer.append(formatHTML(refname));
			}
			buffer.append("</td>");
			buffer.append("<td style=\"border: 1px solid black;\">");
			if (node != null && nnode.getNodeClass() != null)
				buffer.append(formatHTML(nnode.getNodeClass().name()));
			buffer.append("</td>");
			buffer.append("<td style=\"border: 1px solid black;\">");
			if (node != null && nnode.getBrowseName() != null) {
				buffer.append("<a href=\"#\" onClick=\"return selectNodeFunction('" + nnode.getNodeId() + "');\">");
				buffer.append(formatHTML(nnode.getBrowseName().toString()));
				buffer.append("</a>");
			}
			buffer.append("</td>");
			buffer.append("<td style=\"border: 1px solid black;\">");
			Node dtNode = null;
			if (nnode instanceof UAVariableTypeNode) {
				dtNode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
						.getNodeById(((UAVariableTypeNode) nnode).getDataType());
				if (dtNode != null && dtNode.getBrowseName() != null)
					buffer.append(formatHTML(dtNode.getBrowseName().toString()));
			} else if (nnode instanceof UAVariableNode) {
				dtNode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
						.getNodeById(((UAVariableNode) nnode).getDataType());
			}
			if (dtNode != null && dtNode.getBrowseName() != null) {
				buffer.append("<a href=\"#\" onClick=\"return selectNodeFunction('" + dtNode.getNodeId() + "');\">");
				buffer.append(formatHTML(dtNode.getBrowseName().toString()));
				buffer.append("</a>");
			}
			buffer.append("</td>");
			buffer.append("<td style=\"border: 1px solid black;\">");
			ExpandedNodeId type = nnode.findTarget(Identifiers.HasTypeDefinition, false);
			reftypenode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeById(type);
			if (reftypenode != null && reftypenode.getBrowseName() != null) {
				buffer.append("<a href=\"#\" onClick=\"return selectNodeFunction('" + reftypenode.getNodeId() + "');\">");
				buffer.append(formatHTML(reftypenode.getBrowseName().toString()));
				buffer.append("</a>");
			}
			buffer.append("</td>");
			buffer.append("<td style=\"border: 1px solid black;\">");
			buffer.append(getModellingReadWrite(nnode));
			buffer.append("</td>");
			buffer.append("</tr>");
		}

		buffer.append("</table>");
		buffer.append("<div style=\"width: 600px; height:30px;\">");
		buffer.append("</div>");

		return buffer.toString();
	}

	public static void createAutomationHeader(XWPFDocument document, FileOutputStream out) throws IOException {
		// write footer content
		CTP ctpHeader = CTP.Factory.newInstance();
		CTR ctrHeader = ctpHeader.addNewR();
		CTText ctHeader = ctrHeader.addNewT();
		String headerText = "generated by Automation Studio Xi";
		ctHeader.setStringValue(headerText);
		XWPFParagraph headerParagraph = new XWPFParagraph(ctpHeader, document);
		headerParagraph.setAlignment(ParagraphAlignment.CENTER);
		headerParagraph.setBorderBottom(Borders.DIAMONDS_GRAY);
		XWPFParagraph[] parsHeader = new XWPFParagraph[1];
		parsHeader[0] = headerParagraph;
		CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
		XWPFHeaderFooterPolicy policy = new XWPFHeaderFooterPolicy(document, sectPr);
		policy.createHeader(XWPFHeaderFooterPolicy.DEFAULT, parsHeader);
	}

	public static void createBichlerFooter(XWPFDocument document, FileOutputStream out) throws IOException {
		// write footer content
		CTP ctpFooter = CTP.Factory.newInstance();
		CTR ctrFooter = ctpFooter.addNewR();
		CTText ctFooter = ctrFooter.addNewT();
		Calendar calendar = new GregorianCalendar();
		int year = calendar.get(Calendar.YEAR);
		String footerText = "© Bichler Technologies GmbH " + year + " All Rights Reserved";
		ctFooter.setStringValue(footerText);
		XWPFParagraph footerParagraph = new XWPFParagraph(ctpFooter, document);
		footerParagraph.setAlignment(ParagraphAlignment.CENTER);
		footerParagraph.setBorderTop(Borders.DIAMONDS_GRAY);
		XWPFParagraph[] parsFooter = new XWPFParagraph[1];
		parsFooter[0] = footerParagraph;
		CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
		XWPFHeaderFooterPolicy policy = new XWPFHeaderFooterPolicy(document, sectPr);
		policy.createFooter(XWPFHeaderFooterPolicy.DEFAULT, parsFooter);
	}

	public static String createVariableHTML(UAVariableNode node) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<html>");
		buffer.append("<body style=\"width:100%; font-size: 12px; font-family:Arial;\">");
		buffer.append("<div style=\"width: 600px; height: 100%;\">");
		createBichlerHeaderHTML(buffer);
		String innerHTML = createVariableInnerHTML(node);
		String docuPath = Studio_ResourceManager.getInfoModellerDokuPath();
		innerHTML = innerHTML.replaceAll("src=\"", "src=\"" + docuPath.replaceAll("\\\\", "\\\\\\\\") + "\\\\");
		buffer.append(innerHTML);
		createBichlerFooterHTML(buffer);
		buffer.append("</div>");
		buffer.append("</body>");
		buffer.append("</html>");
		return buffer.toString();
	}

	public static String createVariableInnerHTML(UAVariableNode node) {
		StringBuffer buffer = new StringBuffer();

		buffer.append("<h1 style=\"color: gray;\">");
		buffer.append("<a href=\"#" + node.getDisplayName().getText() + "\" id=\"" + node.getDisplayName().getText()
				+ "\" onClick=\"return selectNodeFunction('" + node.getNodeId() + "');\">");
		buffer.append(formatHTML(node.getDisplayName().getText()));
		buffer.append("</a>");
		buffer.append("</h1>");
		if (node.getDescription() != null && node.getDescription().getText() != null) {
			buffer.append("<h4>Beschreibung:</h4>");
			if (node != null && node.getDescription() != null)
				buffer.append(formatHTML(node.getDescription().getText()) + "<br/>");
		}
		String infos = Studio_ResourceManager.NODE_INFOS.get(node.getNodeId());
		if (infos != null && !infos.isEmpty()) {
			buffer.append("<h4>Zus&auml;tzliche Infos:</h4>");
			buffer.append(formatHTMLWithoutTags(infos));
		}
		buffer.append("<div style=\"width: 100%; height: 10px;\">");
		buffer.append("</div>");
		buffer.append(
				"<table style=\"width:100%; border: 1px solid black; border-collapse: collapse; font-size: 10px; font-family:Arial;\">");
		buffer.append("<tr>");
		buffer.append("<td style=\"width: 240px; border: 1px solid black; font-weight: bold;\">");
		buffer.append("Attribute");
		buffer.append("</td>");
		buffer.append(
				"<td style=\"width: 120px; border-left: 1px solid black; border-bottom: 1px solid black; font-weight: bold;\">");
		buffer.append("Value");
		buffer.append("</td>");
		buffer.append("<td style=\"width: 240px; border-bottom: 1px solid black;\">");
		buffer.append("</td>");
		buffer.append("<td style=\"width: 340px; border-bottom: 1px solid black;\">");
		buffer.append("</td>");
		buffer.append("<td style=\"width: 240px; border-bottom: 1px solid black;\">");
		buffer.append("</td>");
		buffer.append("<td style=\"width: 240px; border-bottom: 1px solid black; border-right: 1px solid black;\">");
		buffer.append("</td>");
		buffer.append("</tr>");

		// row two
		buffer.append("<tr>");
		buffer.append("<td style=\" border: 1px solid black;\">");
		buffer.append("BrowseName");
		buffer.append("</td>");
		buffer.append("<td style=\" border-left: 1px solid black; border-bottom: 1px solid black;\" colspan=\"5\">");
		buffer.append(formatHTML(node.getBrowseName().toString()));
		buffer.append("</td>");

		buffer.append("</tr>");

		// row four
		buffer.append("<tr>");
		buffer.append("<td style=\"border: 1px solid black; font-weight: bold;\">");
		buffer.append("References");
		buffer.append("</td>");
		buffer.append("<td style=\"border: 1px solid black; font-weight: bold;\">");
		buffer.append("Node<br>class");
		buffer.append("</td>");
		buffer.append("<td style=\"border: 1px solid black; font-weight: bold;\">");
		buffer.append("BrowseName");
		buffer.append("</td>");
		buffer.append("<td style=\"border: 1px solid black; font-weight: bold;\">");
		buffer.append("DataType");
		buffer.append("</td>");
		buffer.append("<td style=\"border: 1px solid black; font-weight: bold;\">");
		buffer.append("TypeDefinition");
		buffer.append("</td>");
		buffer.append("<td style=\"border: 1px solid black; font-weight: bold;\">");
		buffer.append("Modelling Rule");
		buffer.append("</td>");
		buffer.append("</tr>");

		for (ReferenceNode refnode : node.getReferences()) {

			// we do not need parent references
//			if (refnode.getIsInverse())
//				continue;

			Node nnode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
					.getNodeById(refnode.getTargetId());

			buffer.append("<tr>");
			buffer.append("<td style=\"border: 1px solid black;\">");

			Node reftypenode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
					.getNodeById(refnode.getReferenceTypeId());
			String refName = getReferenceName(refnode, (ReferenceTypeNode) reftypenode);
			if (refName != null) {
				buffer.append("<a href=\"#\" onClick=\"return selectNodeFunction('" + reftypenode.getNodeId() + "');\">");
				buffer.append(formatHTML(refName));
				buffer.append("</a>");
			}
			buffer.append("</td>");
			buffer.append("<td style=\"border: 1px solid black;\">");
			if (nnode != null && nnode.getNodeClass() != null)
				buffer.append(formatHTML(nnode.getNodeClass().name()));
			buffer.append("</td>");
			buffer.append("<td style=\"border: 1px solid black;\">");
			if (nnode != null && nnode.getBrowseName() != null) {
				buffer.append("<a href=\"#\" onClick=\"return selectNodeFunction('" + nnode.getNodeId() + "');\">");
				buffer.append(formatHTML(nnode.getBrowseName().toString()));
				buffer.append("</a>");
			}
			buffer.append("</td>");
			buffer.append("<td style=\"border: 1px solid black;\">");
			Node dtNode = null;
			if (nnode instanceof UAVariableTypeNode) {
				dtNode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
						.getNodeById(((UAVariableTypeNode) nnode).getDataType());
			} else if (nnode instanceof UAVariableNode) {
				dtNode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
						.getNodeById(((UAVariableNode) nnode).getDataType());
			}
			if (dtNode != null && dtNode.getBrowseName() != null) {
				buffer.append("<a href=\"#\" onClick=\"return selectNodeFunction('" + dtNode.getNodeId() + "');\">");
				buffer.append(dtNode.getBrowseName().toString());
				buffer.append("</a>");
			}
			buffer.append("</td>");
			buffer.append("<td style=\"border: 1px solid black;\">");
			ExpandedNodeId type = nnode.findTarget(Identifiers.HasTypeDefinition, false);
			reftypenode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeById(type);
			if (reftypenode != null && reftypenode.getBrowseName() != null) {
				buffer.append("<a href=\"#\" onClick=\"return selectNodeFunction('" + reftypenode.getNodeId() + "');\">");
				buffer.append(formatHTML(reftypenode.getBrowseName().toString()));
				buffer.append("</a>");
			}
			buffer.append("</td>");
			buffer.append("<td style=\"border: 1px solid black;\">");
			buffer.append(getModellingReadWrite(nnode));
			buffer.append("</td>");
			buffer.append("</tr>");
		}

		buffer.append("</table>");
		buffer.append("<div style=\"width: 600px; height:30px;\">");
		buffer.append("</div>");
		return buffer.toString();
	}

	public static String createObjectTypeHTML(UAObjectTypeNode node) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<html>");
		buffer.append("<body style=\"width:100%; font-size: 12px; font-family:Arial;\">");
		buffer.append("<div style=\"width: 600px; height: 100%;\">");
		createBichlerHeaderHTML(buffer);
		String innerHTML = createObjectTypeInnerHTML(node);
		String docuPath = Studio_ResourceManager.getInfoModellerDokuPath();
		innerHTML = innerHTML.replaceAll("src=\"", "src=\"" + docuPath.replaceAll("\\\\", "\\\\\\\\") + "\\\\");
		buffer.append(innerHTML);
		createBichlerFooterHTML(buffer);
		buffer.append("</div>");
		buffer.append("</body>");
		buffer.append("</html>");
		return buffer.toString();
	}

	public static String createObjectTypeInnerHTML(UAObjectTypeNode node) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<h1 style=\"color: gray;\">");
		buffer.append("<a href=\"#" + node.getDisplayName().getText() + "\" id=\"" + node.getDisplayName().getText()
				+ "\" onClick=\"return selectNodeFunction('" + node.getNodeId() + "');\">");
		buffer.append(formatHTML(node.getDisplayName().getText()));
		buffer.append("</a>");
		buffer.append("</h1>");
		if (node.getDescription() != null && node.getDescription().getText() != null) {
			buffer.append("<h4>Beschreibung:</h4>");
			buffer.append(formatHTML(node.getDescription().getText()) + "<br/>");
		}
		String infos = Studio_ResourceManager.NODE_INFOS.get(node.getNodeId());
		if (infos != null && !infos.isEmpty()) {
			buffer.append("<h4>Zus&auml;tzliche Infos:</h4>");
			buffer.append(formatHTMLWithoutTags(infos));
		}
		buffer.append("<div style=\"width: 100%; height: 10px;\">");
		buffer.append("</div>");
		buffer.append(
				"<table style=\"width:100%; border: 1px solid black; border-collapse: collapse; font-size: 10px; font-family:Arial;\">");
		buffer.append("<tr>");
		buffer.append("<td style=\"width: 240px; border: 1px solid black; font-weight: bold;\">");
		buffer.append("Attribute");
		buffer.append("</td>");
		buffer.append(
				"<td style=\"width: 120px; border-left: 1px solid black; border-bottom: 1px solid black; font-weight: bold;\">");
		buffer.append("Value");
		buffer.append("</td>");
		buffer.append("<td style=\"width: 240px; border-bottom: 1px solid black;\">");
		buffer.append("</td>");
		buffer.append("<td style=\"width: 340px; border-bottom: 1px solid black;\">");
		buffer.append("</td>");
		buffer.append("<td style=\"width: 240px; border-bottom: 1px solid black;\">");
		buffer.append("</td>");
		buffer.append("<td style=\"width: 240px; border-bottom: 1px solid black; border-right: 1px solid black;\">");
		buffer.append("</td>");
		buffer.append("</tr>");

		// row two
		buffer.append("<tr>");
		buffer.append("<td style=\" border: 1px solid black;\">");
		buffer.append("BrowseName");
		buffer.append("</td>");
		buffer.append("<td style=\" border-left: 1px solid black; border-bottom: 1px solid black;\" colspan=\"5\">");
		if (node != null && node.getBrowseName() != null) {
			buffer.append(formatHTML(node.getBrowseName().toString()));
		}
		buffer.append("</td>");

		buffer.append("</tr>");

		// row three
		buffer.append("<tr>");
		buffer.append("<td style=\"border: 1px solid black;\">");
		buffer.append("IsAbstract");
		buffer.append("</td>");
		buffer.append("<td style=\"border-left: 1px solid black; border-bottom: 1px solid black;\" >");
		if (node != null)
			buffer.append(node.getIsAbstract());
		buffer.append("</td>");
		buffer.append("<td style=\" border-bottom: 1px solid black;\" >");
		buffer.append("</td>");
		buffer.append("<td style=\" border-bottom: 1px solid black;\">");
		buffer.append("</td>");
		buffer.append("<td style=\"border-bottom: 1px solid black;\">");
		buffer.append("</td>");
		buffer.append("<td style=\"border-bottom: 1px solid black;\">");
		buffer.append("</td>");
		buffer.append("</tr>");

		// row four
		buffer.append("<tr>");
		buffer.append("<td style=\"border: 1px solid black; font-weight: bold;\">");
		buffer.append("References");
		buffer.append("</td>");
		buffer.append("<td style=\"border: 1px solid black; font-weight: bold;\">");
		buffer.append("Node<br>class");
		buffer.append("</td>");
		buffer.append("<td style=\"border: 1px solid black; font-weight: bold;\">");
		buffer.append("BrowseName");
		buffer.append("</td>");
		buffer.append("<td style=\"border: 1px solid black; font-weight: bold;\">");
		buffer.append("DataType");
		buffer.append("</td>");
		buffer.append("<td style=\"border: 1px solid black; font-weight: bold;\">");
		buffer.append("TypeDefinition");
		buffer.append("</td>");
		buffer.append("<td style=\"border: 1px solid black; font-weight: bold;\">");
		buffer.append("Modelling Rule");
		buffer.append("</td>");
		buffer.append("</tr>");

		// row five
		buffer.append("<tr>");
		buffer.append("<td style=\"border: 1px solid black;\" colspan=\"6\">");
		ExpandedNodeId parentid = node.findTarget(Identifiers.HasSubtype, true);
		if (parentid != null) {
			Node nnode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
					.getNodeById(parentid);
			if (nnode != null) {
				// row five
				buffer.append("<tr>");
				buffer.append("<td style=\"border: 1px solid black;\" colspan=\"6\">");
				buffer.append("Subtype of ");
				buffer.append("<a href=\"#\" onClick=\"return selectNodeFunction('" + nnode.getNodeId() + "');\">");
				buffer.append(formatHTML(nnode.getBrowseName().toString()));
				buffer.append("</a>");
				buffer.append("</td>");
				buffer.append("</tr>");
			}
		}
		buffer.append("</td>");
		buffer.append("</tr>");

		for (ReferenceNode refnode : node.getReferences()) {

			boolean isSubtype = ServerInstance.getInstance().getServerInstance().getTypeTable()
					.isTypeOf(refnode.getReferenceTypeId(), Identifiers.HasSubtype);

			// we do not need parent references
			if (isSubtype && refnode.getIsInverse()) {
				continue;
			}

			Node nnode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
					.getNodeById(refnode.getTargetId());

			buffer.append("<tr>");
			buffer.append("<td style=\"border: 1px solid black;\">");

			Node reftypenode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
					.getNodeById(refnode.getReferenceTypeId());

			String refName = getReferenceName(refnode, (ReferenceTypeNode) reftypenode);
			if (refnode != null) {
				buffer.append("<a href=\"#\" onClick=\"return selectNodeFunction('" + reftypenode.getNodeId() + "');\">");
				buffer.append(formatHTML(refName));
				buffer.append("</a>");
			}
			buffer.append("</td>");
			buffer.append("<td style=\"border: 1px solid black;\">");
			buffer.append(formatHTML(nnode.getNodeClass().name()));
			buffer.append("</td>");
			buffer.append("<td style=\"border: 1px solid black;\">");
			buffer.append("<a href=\"#\" onClick=\"return selectNodeFunction('" + nnode.getNodeId() + "');\">");
			buffer.append(formatHTML(nnode.getBrowseName().toString()));
			buffer.append("</a>");
			buffer.append("</td>");
			buffer.append("<td style=\"border: 1px solid black;\">");
			Node dtNode = null;
			if (nnode instanceof UAVariableTypeNode) {
				dtNode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
						.getNodeById(((UAVariableTypeNode) nnode).getDataType());
			} else if (nnode instanceof UAVariableNode) {
				dtNode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
						.getNodeById(((UAVariableNode) nnode).getDataType());
			}
			if (dtNode != null && dtNode.getBrowseName() != null) {
				buffer.append("<a href=\"#\" onClick=\"return selectNodeFunction('" + dtNode.getNodeId() + "');\">");
				buffer.append(formatHTML(dtNode.getBrowseName().toString()));
				buffer.append("</a>");
			}
			buffer.append("</td>");
			buffer.append("<td style=\"border: 1px solid black;\">");
			ExpandedNodeId type = nnode.findTarget(Identifiers.HasTypeDefinition, false);
			reftypenode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeById(type);

			if (reftypenode != null) {
				buffer.append("<a href=\"#\" onClick=\"return selectNodeFunction('" + reftypenode.getNodeId() + "');\">");
				buffer.append(formatHTML(reftypenode.getBrowseName().toString()));
				buffer.append("</a>");
			}
			buffer.append("</td>");
			buffer.append("<td style=\"border: 1px solid black;\">");
			buffer.append(getModellingReadWrite(nnode));
			buffer.append("</td>");
			buffer.append("</tr>");
		}

		buffer.append("</table>");
		buffer.append("<div style=\"width: 600px; height:30px;\">");
		buffer.append("</div>");

		return buffer.toString();
	}

	public static String createDataTypeHTML(UADataTypeNode node) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<html>");
		buffer.append("<body style=\"width:100%; font-size: 12px; font-family:Arial;\">");
		buffer.append("<div style=\"width: 600px; height: 100%;\">");
		createBichlerHeaderHTML(buffer);
		String innerHTML = createDataTypeInnerHTML(node);
		String docuPath = Studio_ResourceManager.getInfoModellerDokuPath();
		innerHTML = innerHTML.replaceAll("src=\"", "src=\"" + docuPath.replaceAll("\\\\", "\\\\\\\\") + "\\\\");
		buffer.append(innerHTML);
		createBichlerFooterHTML(buffer);
		buffer.append("</div>");
		buffer.append("</body>");
		buffer.append("</html>");
		return buffer.toString();
	}

	public static String createDataTypeInnerHTML(UADataTypeNode node) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<h1 style=\"color: gray;\">");
		buffer.append("<a href=\"#" + node.getDisplayName().getText() + "\" id=\"" + node.getDisplayName().getText()
				+ "\" onClick=\"return selectNodeFunction('" + node.getNodeId() + "');\">");
		buffer.append(formatHTML(node.getDisplayName().getText()));
		buffer.append("</a>");
		buffer.append("</h1>");
		if (node.getDescription() != null && node.getDescription().getText() != null) {
			buffer.append("<h4>Beschreibung:</h4>");
			buffer.append(formatHTML(node.getDescription().getText()) + "<br/>");
		}
		String infos = Studio_ResourceManager.NODE_INFOS.get(node.getNodeId());
		if (infos != null && !infos.isEmpty()) {
			buffer.append("<h4>Zus&auml;tzliche Infos:</h4>");
			buffer.append(formatHTMLWithoutTags(infos));
			buffer.append("<br/><br/>");
		}
		buffer.append(
				"<table style=\"width:100%; border: 1px solid black; border-collapse: collapse; font-size: 10px; font-family:Arial;\">");
		buffer.append("<td style=\"width: 150px; border: 1px double black; font-weight: bold;\">");
		buffer.append("Value");
		buffer.append("</td>");
		buffer.append("<td style=\"width: 300px; border: 1px solid black; font-weight: bold;\">");
		buffer.append("Description");
		buffer.append("</td>");
		// verify main datatype node
		ExpandedNodeId parent = node.findTarget(Identifiers.HasSubtype, true);
		if (parent != null) {
			Node pnode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeById(parent);
			if (pnode.getNodeId().equals(Identifiers.Enumeration)) {
				// we found an enumeration node
				// find description
				ExpandedNodeId prop = node.findTarget(Identifiers.HasProperty, false);
				pnode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeById(prop);
				if (pnode instanceof UAServerVariableNode) {
					Variant var = ((UAServerVariableNode) pnode).getValue();
					for (Object obj : (Object[]) var.getValue()) {
						buffer.append("<tr>");
						if (obj instanceof EnumValueType) {
							EnumValueType valtype = (EnumValueType) obj;

							buffer.append("<td style=\"width: 150px; border: 1px solid black; \">");
							buffer.append(formatHTML(valtype.getDisplayName().getText()));
							buffer.append("</td>");
							buffer.append("<td style=\"width: 300px; border: 1px solid black; \">");
							if (valtype.getDescription() != null && valtype.getDescription().getText() != null)
								buffer.append(valtype.getDescription().getText());
							buffer.append("</td>");
						} else if (obj instanceof LocalizedText) {
							LocalizedText locText = (LocalizedText) obj;

							buffer.append("<td style=\"width: 150px; border: 1px solid black; \">");
							buffer.append(formatHTML(locText.getText()));
							buffer.append("</td>");
							buffer.append("<td style=\"width: 300px; border: 1px solid black; \">");
							buffer.append("</td>");
						}
						buffer.append("</tr>");
					}
				}
			}
		}
		buffer.append("</table>");

		return buffer.toString();
	}

	public static String createMethodHTML(UAMethodNode node) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<html>");
		buffer.append("<body style=\"width:100%; font-size: 12px; font-family:Arial;\">");
		buffer.append("<div style=\"width: 600px; height: 100%;\">");
		createBichlerHeaderHTML(buffer);
		String innerHTML = createMethodInnerHTML(node);
		String docuPath = Studio_ResourceManager.getInfoModellerDokuPath();
		innerHTML = innerHTML.replaceAll("src=\"", "src=\"" + docuPath.replaceAll("\\\\", "\\\\\\\\") + "\\\\");
		buffer.append(innerHTML);
		createBichlerFooterHTML(buffer);
		buffer.append("</div>");
		buffer.append("</body>");
		buffer.append("</html>");
		return buffer.toString();
	}

	public static String createMethodInnerHTML(UAMethodNode node) {
		StringBuffer buffer = new StringBuffer();

		buffer.append("<h1 style=\"color: gray;\">");
		buffer.append("<a href=\"#" + node.getDisplayName().getText() + "\" id=\"" + node.getDisplayName().getText()
				+ "\" onClick=\"return selectNodeFunction('" + node.getNodeId() + "');\">");
		buffer.append(formatHTML(node.getDisplayName().getText()));
		buffer.append("</a>");
		buffer.append("</h1>");
		if (node.getDescription() != null && node.getDescription().getText() != null) {
			buffer.append("<h4>Beschreibung:</h4>");
			buffer.append(formatHTML(node.getDescription().getText()) + "<br/>");
		}
		String infos = Studio_ResourceManager.NODE_INFOS.get(node.getNodeId());
		if (infos != null && !infos.isEmpty()) {
			buffer.append("<h4>Zus&auml;tzliche Infos:</h4>");
			buffer.append(formatHTMLWithoutTags(infos));
		}

		buffer.append("<br/><strong>Signature</strong><br/><br/>");
		buffer.append("&nbsp;&nbsp;" + formatHTML(node.getDisplayName().getText()) + " (<br/>");
		// first get all input arguments
		for (ReferenceNode refnode : node.getReferences()) {

			// we do not need parent references
			if (refnode.getIsInverse())
				continue;

			Node nnode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
					.getNodeById(refnode.getTargetId());

			if (nnode.getBrowseName().getName().compareTo("InputArguments") == 0 && nnode instanceof UAVariableNode) {
				Variant var = ((UAVariableNode) nnode).getValue();
				if (var != null && var.getValue() != null) {
					Object[] objArr = (Object[]) var.getValue();
					for (Object obj : objArr) {
						if (obj instanceof Argument) {
							Argument arg = (Argument) obj;
							Node dtype = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
									.getNodeById(arg.getDataType());
							buffer.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[in] " + dtype.getBrowseName().toString()
									+ " " + arg.getName() + "<br/>");
						}
					}
				}
			} else if (nnode.getBrowseName().getName().compareTo("OutputArguments") == 0
					&& nnode instanceof UAVariableNode) {
				Variant var = ((UAVariableNode) nnode).getValue();
				if (var != null && var.getValue() != null) {
					Object[] objArr = (Object[]) var.getValue();
					for (Object obj : objArr) {
						if (obj instanceof Argument) {
							Argument arg = (Argument) obj;
							Node dtype = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
									.getNodeById(arg.getDataType());
							buffer.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;[out] "
									+ dtype.getBrowseName().toString() + " " + arg.getName() + "<br/>");
						}
					}
				}
			}
		}
		buffer.append("&nbsp;&nbsp;);<br/>");

		buffer.append("<div style=\"width: 100%; height: 10px;\">");
		buffer.append("</div>");
		buffer.append(
				"<table style=\"width:100%; border: 1px solid black; border-collapse: collapse; font-size: 10px; font-family:Arial;\">");
		buffer.append("<tr>");
		buffer.append("<td style=\"width: 240px; border: 1px solid black; font-weight: bold;\">");
		buffer.append("Attribute");
		buffer.append("</td>");
		buffer.append(
				"<td style=\"width: 120px; border-left: 1px solid black; border-bottom: 1px solid black; font-weight: bold;\">");
		buffer.append("Value");
		buffer.append("</td>");
		buffer.append("<td style=\"width: 240px; border-bottom: 1px solid black;\">");
		buffer.append("</td>");
		buffer.append("<td style=\"width: 340px; border-bottom: 1px solid black;\">");
		buffer.append("</td>");
		buffer.append("<td style=\"width: 240px; border-bottom: 1px solid black;\">");
		buffer.append("</td>");
		buffer.append("<td style=\"width: 240px; border-bottom: 1px solid black; border-right: 1px solid black;\">");
		buffer.append("</td>");
		buffer.append("</tr>");

		// row two
		buffer.append("<tr>");
		buffer.append("<td style=\" border: 1px solid black;\">");
		buffer.append("BrowseName");
		buffer.append("</td>");
		buffer.append("<td style=\" border-left: 1px solid black; border-bottom: 1px solid black;\" colspan=\"5\">");
		buffer.append(formatHTML(node.getBrowseName().toString()));
		buffer.append("</td>");

		buffer.append("</tr>");

		// row four
		buffer.append("<tr>");
		buffer.append("<td style=\"border: 1px solid black; font-weight: bold;\">");
		buffer.append("References");
		buffer.append("</td>");
		buffer.append("<td style=\"border: 1px solid black; font-weight: bold;\">");
		buffer.append("Node<br>class");
		buffer.append("</td>");
		buffer.append("<td style=\"border: 1px solid black; font-weight: bold;\">");
		buffer.append("BrowseName");
		buffer.append("</td>");
		buffer.append("<td style=\"border: 1px solid black; font-weight: bold;\">");
		buffer.append("DataType");
		buffer.append("</td>");
		buffer.append("<td style=\"border: 1px solid black; font-weight: bold;\">");
		buffer.append("TypeDefinition");
		buffer.append("</td>");
		buffer.append("<td style=\"border: 1px solid black; font-weight: bold;\">");
		buffer.append("Modelling Rule");
		buffer.append("</td>");
		buffer.append("</tr>");

		for (ReferenceNode refnode : node.getReferences()) {

			// we do not need parent references
//			if (refnode.getIsInverse())
//				continue;

			Node nnode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
					.getNodeById(refnode.getTargetId());

			buffer.append("<tr>");
			buffer.append("<td style=\"border: 1px solid black;\">");

			Node reftypenode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
					.getNodeById(refnode.getReferenceTypeId());

			if (reftypenode != null && ((UAReferenceTypeNode) reftypenode).getInverseName() != null) {
				buffer.append("<a href=\"#\" onClick=\"return selectNodeFunction('" + reftypenode.getNodeId() + "');\">");
				buffer.append(formatHTML(((UAReferenceTypeNode) reftypenode).getInverseName().toString()));
				buffer.append("</a>");
			}
			buffer.append("</td>");
			buffer.append("<td style=\"border: 1px solid black;\">");
			buffer.append(formatHTML(nnode.getNodeClass().name()));
			buffer.append("</td>");
			buffer.append("<td style=\"border: 1px solid black;\">");
			if (nnode != null && nnode.getBrowseName() != null) {
				buffer.append("<a href=\"#\" onClick=\"return selectNodeFunction('" + nnode.getNodeId() + "');\">");
				buffer.append(formatHTML(nnode.getBrowseName().toString()));
				buffer.append("</a>");
			}
			buffer.append("</td>");
			buffer.append("<td style=\"border: 1px solid black;\">");
			if (nnode instanceof UAVariableNode) {
				Node dtNode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
						.getNodeById(((UAVariableNode) nnode).getDataType());
				if (dtNode != null) {
					buffer.append("<a href=\"#\" onClick=\"return selectNodeFunction('" + dtNode.getNodeId() + "');\">");
					buffer.append(dtNode.getBrowseName().toString());
					if (1 == ((UAVariableNode) nnode).getValueRank()) {
						buffer.append("[]");
					}
					buffer.append("</a>");
				}
			}
			buffer.append("</td>");
			buffer.append("<td style=\"border: 1px solid black;\">");
			ExpandedNodeId type = nnode.findTarget(Identifiers.HasTypeDefinition, false);
			reftypenode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeById(type);
			if (reftypenode != null) {
				buffer.append("<a href=\"#\" onClick=\"return selectNodeFunction('" + reftypenode.getNodeId() + "');\">");
				buffer.append(formatHTML(reftypenode.getBrowseName().toString()));
				buffer.append("</a>");
			}
			buffer.append("</td>");
			buffer.append("<td style=\"border: 1px solid black;\">");
			buffer.append(getModellingReadWrite(nnode));
			buffer.append("</td>");
			buffer.append("</tr>");
		}

		buffer.append("</table>");
		buffer.append("<div style=\"width: 600px; height:30px;\">");
		buffer.append("</div>");
		return buffer.toString();
	}

	public static String createObjectHTML(UAObjectNode node) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<html>\n");
		buffer.append("<body style=\"width:100%; font-size: 12px; font-family:Arial;\">\n");
		buffer.append("<div style=\"width: 600px; height: 100%;\">\n");
		createBichlerHeaderHTML(buffer);
		String innerHTML = createObjectInnerHTML(node);
		String docuPath = Studio_ResourceManager.getInfoModellerDokuPath();
		innerHTML = innerHTML.replaceAll("src=\"", "src=\"" + docuPath.replaceAll("\\\\", "\\\\\\\\") + "\\\\");
		buffer.append(innerHTML);
		createBichlerFooterHTML(buffer);
		buffer.append("</div>\n");
		buffer.append("</body>\n");
		buffer.append("</html>\n");
		return buffer.toString();
	}

	public static String createObjectInnerHTML(UAObjectNode node) {
		StringBuffer buffer = new StringBuffer();

		buffer.append("<h1 style=\"color: gray;\">");
		buffer.append("<a href=\"#" + node.getDisplayName().getText() + "\" id=\"" + node.getDisplayName().getText()
				+ "\" onClick=\"return selectNodeFunction('" + node.getNodeId() + "');\">");
		buffer.append(formatHTML(node.getDisplayName().getText()));
		buffer.append("</a>");
		buffer.append("</h1>");
		if (node.getDescription() != null && node.getDescription().getText() != null) {
			buffer.append("<h4>Beschreibung:</h4>");
			buffer.append(formatHTML(node.getDescription().getText()) + "<br/>");
		}
		String infos = Studio_ResourceManager.NODE_INFOS.get(node.getNodeId());
		if (infos != null && !infos.isEmpty()) {
			buffer.append("<h4>Zus&auml;tzliche Infos:</h4>");
			buffer.append(formatHTMLWithoutTags(infos));
		}
		buffer.append("<div style=\"width: 100%; height: 10px;\">");
		buffer.append("</div>");
		buffer.append(
				"<table style=\"width:100%; border: 1px solid black; border-collapse: collapse; font-size: 10px; font-family:Arial;\">");
		buffer.append("<tr>");
		buffer.append("<td style=\"width: 240px; border: 1px solid black; font-weight: bold;\">");
		buffer.append("Attribute");
		buffer.append("</td>");
		buffer.append(
				"<td style=\"width: 120px; border-left: 1px solid black; border-bottom: 1px solid black; font-weight: bold;\">");
		buffer.append("Value");
		buffer.append("</td>");
		buffer.append("<td style=\"width: 240px; border-bottom: 1px solid black;\">");
		buffer.append("</td>");
		buffer.append("<td style=\"width: 340px; border-bottom: 1px solid black;\">");
		buffer.append("</td>");
		buffer.append("<td style=\"width: 240px; border-bottom: 1px solid black;\">");
		buffer.append("</td>");
		buffer.append("<td style=\"width: 240px; border-bottom: 1px solid black; border-right: 1px solid black;\">");
		buffer.append("</td>");
		buffer.append("</tr>");

		// row two
		buffer.append("<tr>");
		buffer.append("<td style=\" border: 1px solid black;\">");
		buffer.append("BrowseName");
		buffer.append("</td>");
		buffer.append("<td style=\" border-left: 1px solid black; border-bottom: 1px solid black;\" colspan=\"5\">");
		buffer.append(formatHTML(node.getBrowseName().toString()));
		buffer.append("</td>");

		buffer.append("</tr>");

		// row four
		buffer.append("<tr>");
		buffer.append("<td style=\"border: 1px solid black; font-weight: bold;\">");
		buffer.append("References");
		buffer.append("</td>");
		buffer.append("<td style=\"border: 1px solid black; font-weight: bold;\">");
		buffer.append("Node<br>class");
		buffer.append("</td>");
		buffer.append("<td style=\"border: 1px solid black; font-weight: bold;\">");
		buffer.append("BrowseName");
		buffer.append("</td>");
		buffer.append("<td style=\"border: 1px solid black; font-weight: bold;\">");
		buffer.append("DataType");
		buffer.append("</td>");
		buffer.append("<td style=\"border: 1px solid black; font-weight: bold;\">");
		buffer.append("TypeDefinition");
		buffer.append("</td>");
		buffer.append("<td style=\"border: 1px solid black; font-weight: bold;\">");
		buffer.append("Modelling Rule");
		buffer.append("</td>");
		buffer.append("</tr>");

		for (ReferenceNode refnode : node.getReferences()) {

			// we do not need parent references
//			if (refnode.getIsInverse())
//				continue;

			Node nnode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
					.getNodeById(refnode.getTargetId());

			buffer.append("<tr>");
			buffer.append("<td style=\"border: 1px solid black;\">");

			Node reftypenode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
					.getNodeById(refnode.getReferenceTypeId());
			String refName = getReferenceName(refnode, (ReferenceTypeNode) reftypenode);
			if (refName != null) {
				buffer.append("<a href=\"#\" onClick=\"return selectNodeFunction('" + reftypenode.getNodeId() + "');\">");
				buffer.append(formatHTML(refName));
				buffer.append("</a>");
			} else
				Logger.getLogger(ExportDocuElementsHandler.class.getName()).log(Level.WARNING,
						"No reference name found for: {0}", reftypenode);
			buffer.append("</td>");
			buffer.append("<td style=\"border: 1px solid black;\">");
			buffer.append(formatHTML(nnode.getNodeClass().name()));
			buffer.append("</td>");
			buffer.append("<td style=\"border: 1px solid black;\">");
			if (nnode != null && nnode.getBrowseName() != null) {
				buffer.append("<a href=\"#\" onClick=\"return selectNodeFunction('" + nnode.getNodeId() + "');\">");
				buffer.append(formatHTML(nnode.getBrowseName().toString()));
				buffer.append("</a>");
			}
			buffer.append("</td>");
			buffer.append("<td style=\"border: 1px solid black;\">");
			Node dtNode = null;
			if (nnode instanceof UAVariableTypeNode) {
				dtNode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
						.getNodeById(((UAVariableTypeNode) nnode).getDataType());

			} else if (nnode instanceof UAVariableNode) {
				dtNode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
						.getNodeById(((UAVariableNode) nnode).getDataType());
			}
			if (dtNode != null && dtNode.getBrowseName() != null) {
				buffer.append("<a href=\"#\" onClick=\"return selectNodeFunction('" + dtNode.getNodeId() + "');\">");
				buffer.append(formatHTML(dtNode.getBrowseName().toString()));
				buffer.append("</a>");
			}
			buffer.append("</td>");
			buffer.append("<td style=\"border: 1px solid black;\">");
			ExpandedNodeId type = nnode.findTarget(Identifiers.HasTypeDefinition, false);
			reftypenode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeById(type);
			if (reftypenode != null) {
				buffer.append("<a href=\"#\" onClick=\"return selectNodeFunction('" + reftypenode.getNodeId() + "');\">");
				buffer.append(formatHTML(reftypenode.getBrowseName().toString()));
				buffer.append("</a>");
			}
			buffer.append("</td>");
			buffer.append("<td style=\"border: 1px solid black;\">");
			buffer.append(getModellingReadWrite(nnode));
			buffer.append("</td>");
			buffer.append("</tr>");
		}

		buffer.append("</table>");
		return buffer.toString();
	}

	private void addBTechHeadingStype(String styleId, int headingLevel) {
		CTStyle ctStyle = CTStyle.Factory.newInstance();
		ctStyle.setStyleId(styleId);
		CTString styleName = CTString.Factory.newInstance();
		styleName.setVal(styleId);
		ctStyle.setName(styleName);

		CTDecimalNumber indentNumber = CTDecimalNumber.Factory.newInstance();
		indentNumber.setVal(BigInteger.valueOf(headingLevel));

		ctStyle.setUiPriority(indentNumber);

		CTOnOff onoffnull = CTOnOff.Factory.newInstance();
		ctStyle.setUnhideWhenUsed(onoffnull);

		ctStyle.setQFormat(onoffnull);
		CTPPr ppr = CTPPr.Factory.newInstance();
		ppr.setOutlineLvl(indentNumber);
		ctStyle.setPPr(ppr);

		XWPFStyle style = new XWPFStyle(ctStyle);

		XWPFStyles styles = document.createStyles();
		style.setType(STStyleType.PARAGRAPH);
		styles.addStyle(style);

//		XWPFNumbering numbering = document.getNumbering();

	}
	
	private static void createBichlerHeaderHTML(StringBuffer buffer) {
		buffer.append("<div style=\"width:100%; height: 30px; text-align: center; color: gray;\">");
		buffer.append("generated by Automation Studio Xi");
		buffer.append("</div>");
		buffer.append("<hr color=\"#999999\">");
	}

	private static void createBichlerFooterHTML(StringBuffer buffer) {
		buffer.append("<hr color=\"#999999\">");
		buffer.append("<div style=\"width:100%; height: 30px; text-align: center; color: gray;\">");
		Calendar calendar = new GregorianCalendar();
		int year = calendar.get(Calendar.YEAR);
		buffer.append("&copy; Bichler Technologies GmbH " + year + " All Rights Reserved");
		buffer.append("</div>");
	}

	private void exportDoc(String fileName, Node selectedNode) {
		FileOutputStream out = null; 
		try {
			// Blank Document
			document = new XWPFDocument();
			addBTechHeadingStype(BTECH_HEADING1, 1);

			// Write the Document in file system
			File file = new File(fileName);

			if (!file.exists())
				file.createNewFile();
			out = new FileOutputStream(file);

			createAutomationHeader(document, out);
			createBichlerFooter(document, out);

			XWPFParagraph para = document.createParagraph();
			CTSimpleField fldSimple = para.getCTP().addNewFldSimple();
			fldSimple.setInstr("TOC \\h");
			fldSimple.setDirty(STOnOff.TRUE);

			// create next node documentation
			switch (selectedNode.getNodeClass()) {
			case Variable:
			case VariableType:
				processVariableTypeNode(selectedNode);
				break;
			case ObjectType:
			case Object:
				processObjectTypeNode(selectedNode);
				break;
			default:
				break;
			}

			// createTOC(toc);
			// write document content
			document.write(out);

			Logger.getLogger(getClass().getName()).log(Level.INFO, "docu export finished");
		} catch (Exception ex) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
				}
			if (document != null) {
				try {
					document.close();
				} catch (IOException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
				}
			}
		}
	}

	private void exportStyle(StringBuffer buffer, String fileName) {
		File file = new File(fileName);
		fileName = file.getName().replace(".html", "");
		buffer.append("<style>");
		buffer.append(".object {list-style-image: url('.\\\\" + fileName + "_Resources\\\\object_16.png');}");
		buffer.append(".objecttype {list-style-image: url('.\\\\" + fileName + "_Resources\\\\objecttype_16.png');}");
		buffer.append(".variable {list-style-image: url('.\\\\" + fileName + "_Resources\\\\variable_16.png');}");
		buffer.append(".method {list-style-image: url('.\\\\" + fileName + "_Resources\\\\method_16.png');}");
		buffer.append(".datatype {list-style-image: url('.\\\\" + fileName + "_Resources\\\\datatype_16.png');}");
		buffer.append(
				".variabletype {list-style-image: url('.\\\\" + fileName + "_Resources\\\\variabletype_16.png');}");

		buffer.append("</style>");
	}

	private void exportIndex(Node node, StringBuffer buffer, String folder) {
		String info = Studio_ResourceManager.NODE_INFOS.get(node.getNodeId());

		switch (node.getNodeClass()) {
		case Object:
			buffer.append("<li class=\"object\">");
			break;
		case Variable:
			buffer.append("<li class=\"variable\">");
			break;
		case ObjectType:
			buffer.append("<li class=\"objecttype\">");
			break;
		case VariableType:
			buffer.append("<li class=\"variabletype\">");
			break;
		case Method:
			buffer.append("<li class=\"method\">");
			break;
		case DataType:
			buffer.append("<li class=\"datatype\">");
			break;
		default:
			break;
		}
		buffer.append("<a href=\"#" + node.getDisplayName().getText() + "\">"
				+ formatHTML(node.getDisplayName().getText()) + "</a>");
		buffer.append("</li>");
		if (node.getReferences().length > 0) {
			buffer.append("<ul>");
			for (ReferenceNode refnode : node.getReferences()) {

				boolean ishierarchical = ServerInstance.getInstance().getServerInstance().getTypeTable()
						.isTypeOf(refnode.getReferenceTypeId(), Identifiers.HierarchicalReferences);
				// we do not need parent references
				if (refnode.getIsInverse() || !ishierarchical)
					continue;

				Node nnode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
						.getNodeById(refnode.getTargetId());

				exportIndex(nnode, buffer, folder);
			}
			buffer.append("</ul>");
		}

	}

	private void exportIcons(String fileName) {
		// create resources folder
		File resources = new File(fileName.replace(".html", "") + "_Resources");
		if (!resources.exists()) {
			try {
				Files.createDirectories(Paths.get(resources.getAbsolutePath()));
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			}
		}

		// add icons
		List<String> icons = new ArrayList<String>();
		icons.add("variabletype_16.png");
		icons.add("variable_16.png");
		icons.add("object_16.png");
		icons.add("objecttype_16.png");
		icons.add("method_16.png");
		icons.add("datatype_16.png");
		for (String icon : icons) {
			try {
				URL var_src = FileLocator.find(Activator.getDefault().getBundle(),
						Path.ROOT.append("icons").append("browser_icons").append(icon), null);
				URL variable_src = FileLocator.toFileURL(var_src);
				String var16 = Path.fromPortableString(resources.getAbsolutePath()).append(icon).toOSString();
				byte[] n = new byte[1024];
				int len = 0;
				File var = new File(var16);
				if (!var.exists())
					var.createNewFile();
				try (InputStream in = new FileInputStream(new File(variable_src.getPath()));
						OutputStream out = new FileOutputStream(var)) {

					while ((len = in.read(n)) > 0) {
						out.write(n, 0, len);
						out.flush();
					}
				} catch (IOException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
				}
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}

	private void exportPics(Node node, String fileName) {
		List<String> pics = new ArrayList<String>();
		String info = Studio_ResourceManager.NODE_INFOS.get(node.getNodeId());
		if (info != null && info.contains("<img")) {
			int endindex = info.length();
			int delta = 5;
			while (endindex > 0) {
				int startindex = info.indexOf("src=\"");
				endindex = info.indexOf("\"", startindex + delta);
				// no additional image found
				if (startindex < 0)
					break;
				pics.add(info.substring(startindex + delta, endindex));
				info = info.substring(endindex + 2);
			}
		}

		File resources = new File(fileName.replace(".html", "") + "_Resources");
		for (String pic : pics) {
			String varname = Path.fromPortableString(resources.getAbsolutePath()).append(pic).toOSString();

			String docu = Studio_ResourceManager.getInfoModellerDokuPath();
			String newImage = Path.fromPortableString(docu).append(pic).toOSString();

			File var = new File(varname);
			try {
				if (!var.exists())
					var.createNewFile();
				byte[] n = new byte[1024];
				int len = 0;
				try (InputStream in = new FileInputStream(new File(newImage));
						OutputStream out = new FileOutputStream(var)) {

					while ((len = in.read(n)) > 0) {
						out.write(n, 0, len);
						out.flush();
					}
				} catch (IOException e) {
					Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
				}
			} catch (IOException e) {
				Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
			}
		}
		for (ReferenceNode refnode : node.getReferences()) {

			boolean ishierarchical = ServerInstance.getInstance().getServerInstance().getTypeTable()
					.isTypeOf(refnode.getReferenceTypeId(), Identifiers.HierarchicalReferences);
			// we do not need parent references
			if (refnode.getIsInverse() || !ishierarchical)
				continue;

			Node nnode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
					.getNodeById(refnode.getTargetId());

			exportPics(nnode, fileName);
		}
	}

	private void exportHTML(Node node, StringBuffer buffer, String file) {
		String innerHTML = "";
		switch (node.getNodeClass()) {
		case VariableType:
			innerHTML = ExportDocuElementsHandler.createVariableTypeInnerHTML((UAVariableTypeNode) node);
			innerHTML = innerHTML.replaceAll("src=\"", "src=\".\\\\" + file + "_Resources\\\\");
			buffer.append(innerHTML);
			break;
		case Variable:
			innerHTML = ExportDocuElementsHandler.createVariableInnerHTML((UAVariableNode) node);
			innerHTML = innerHTML.replaceAll("src=\"", "src=\".\\\\" + file + "_Resources\\\\");
			buffer.append(innerHTML);
			break;
		case ObjectType:
			innerHTML = ExportDocuElementsHandler.createObjectTypeInnerHTML((UAObjectTypeNode) node);
			innerHTML = innerHTML.replaceAll("src=\"", "src=\".\\\\" + file + "_Resources\\\\");
			buffer.append(innerHTML);
			break;
		case Object:
			innerHTML = ExportDocuElementsHandler.createObjectInnerHTML((UAObjectNode) node);
			innerHTML = innerHTML.replaceAll("src=\"", "src=\".\\\\" + file + "_Resources\\\\");
			buffer.append(innerHTML);
			break;
		case Method:
			buffer.append(ExportDocuElementsHandler.createMethodInnerHTML((UAMethodNode) node));
			break;
		case DataType:
			innerHTML = ExportDocuElementsHandler.createDataTypeInnerHTML((UADataTypeNode) node);
			innerHTML = innerHTML.replaceAll("src=\"", "src=\".\\\\" + file + "_Resources\\\\");
			buffer.append(innerHTML);
			break;
		default:
			buffer.append("");
			break;
		}

		for (ReferenceNode refnode : node.getReferences()) {

			boolean ishierarchical = ServerInstance.getInstance().getServerInstance().getTypeTable()
					.isTypeOf(refnode.getReferenceTypeId(), Identifiers.HierarchicalReferences);
			// we do not need parent references
			if (refnode.getIsInverse() || !ishierarchical)
				continue;

			Node nnode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
					.getNodeById(refnode.getTargetId());

			exportHTML(nnode, buffer, file);
		}
	}

	private static String getModellingReadWrite(Node node) {
		String modelling = "";
		ExpandedNodeId type = node.findTarget(Identifiers.HasModellingRule, false);
		Node reftypenode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeById(type);
		if (reftypenode != null) {
			modelling = reftypenode.getBrowseName().getName();
			if (modelling != null && !modelling.isEmpty()) {
				if (NamingRuleType.Mandatory.name().compareTo(modelling) == 0) {
					modelling = "M";
				} else if (NamingRuleType.Optional.name().compareTo(modelling) == 0) {
					modelling = "O";
				} else if (NamingRuleType.OptionalPlaceholder.name().compareTo(modelling) == 0) {
					modelling = "OP";
				}
			}
		}
		String del = "";
		if (node instanceof UAVariableNode) {
			modelling += " ";
			if ((((VariableNode) node).getAccessLevel().intValue() & AccessLevel.CurrentRead.getValue()) != 0) {
				modelling += "R";
				del = ",";
			}
			if ((((VariableNode) node).getAccessLevel().intValue() & AccessLevel.CurrentWrite.getValue()) != 0) {
				modelling += del + "W";
			}
		}
		return modelling;
	}

	private static String getReferenceName(ReferenceNode refnode, ReferenceTypeNode reftypenode) {
		String referenceName = "";
		if (refnode.getIsInverse()) {
			referenceName = reftypenode.getInverseName().getText();
		} else {
			referenceName = reftypenode.getBrowseName().toString();
		}
		return referenceName;
	}

	private static String formatHTML(String input) {

		input = input.replaceAll("Ä", "&Auml;");
		input = input.replaceAll("Ö", "&Ouml;");
		input = input.replaceAll("Ü", "&Uuml;");
		input = input.replaceAll("ä", "&auml;");
		input = input.replaceAll("ö", "&ouml;");
		input = input.replaceAll("ü", "&uuml;");
		input = input.replaceAll("\t", "&nbsp;&nbsp;");
		input = input.replaceAll("  ", "&nbsp;&nbsp;");
		input = input.replaceAll("<", "&lt;");
		input = input.replaceAll(">", "&gt;");
		input = input.replaceAll("\r\n", "<br/>");
		return input;
	}

	private static String formatHTMLWithoutTags(String input) {
//		String docuPath = Studio_ResourceManager.getInfoModellerDokuPath();
		input = input.replaceAll("Ä", "&Auml;");
		input = input.replaceAll("Ö", "&Ouml;");
		input = input.replaceAll("Ü", "&Uuml;");
		input = input.replaceAll("ä", "&auml;");
		input = input.replaceAll("ö", "&ouml;");
		input = input.replaceAll("ü", "&uuml;");
//		input = input.replaceAll("src=\"", "src=\"" + docuPath.replaceAll("\\\\", "\\\\\\\\") + "\\\\");
//		input = input.replaceAll("\t", "&nbsp;&nbsp;");
//		input = input.replaceAll("  ", "&nbsp;&nbsp;");
//		input = input.replaceAll("<", "&lt;");
//		input = input.replaceAll(">", "&gt;");
//		input = input.replaceAll("\r\n", "<br/>");
		return input;
	}

	private void processObjectTypeNode(Node selectedNode) throws IOException {
		XWPFParagraph paragraph = document.createParagraph();
		XWPFRun run = paragraph.createRun();
		run.setText(selectedNode.getDisplayName().getText());
		run.setFontFamily("Arial");
		run.setFontSize(14);
		paragraph.setStyle(BTECH_HEADING1);

		paragraph = document.createParagraph();
		run = paragraph.createRun();
		run.setFontFamily("Arial");
		run.setFontSize(11);
		run.setText("Beschreibung");

		paragraph = document.createParagraph();
		run = paragraph.createRun();
		run.setFontFamily("Arial");
		run.setFontSize(11);
		run.setText(selectedNode.getDescription().toString());

		String infos = Studio_ResourceManager.NODE_INFOS.get(selectedNode.getNodeId());
		if (infos != null && !infos.isEmpty()) {
			paragraph = document.createParagraph();
			run = paragraph.createRun();
			run.setFontFamily("Arial");
			run.setFontSize(11);
			run.setText("zusätzliche Infos");

			paragraph = document.createParagraph();
			run = paragraph.createRun();
			run.setFontFamily("Arial");
			run.setFontSize(11);
			run.setText(infos);
		}
		// create table
		XWPFTable table = document.createTable();

		// create header row
		XWPFTableRow tableRowOne = table.getRow(0);

		XWPFTableCell cell0 = tableRowOne.getCell(0);
		paragraph = cell0.getParagraphs().get(0);
		CTPPr ppr = paragraph.getCTP().getPPr();
		if (ppr == null)
			ppr = paragraph.getCTP().addNewPPr();
		CTSpacing spacing = ppr.isSetSpacing() ? ppr.getSpacing() : ppr.addNewSpacing();
		spacing.setAfter(BigInteger.valueOf(0));
		spacing.setBefore(BigInteger.valueOf(0));

		run = paragraph.createRun();
		run.setBold(true);
		run.setText("Attribute");
		run.setFontFamily("Arial");
		run.setFontSize(8);

		CTTblWidth cellWidth = cell0.getCTTc().addNewTcPr().addNewTcW();
		cellWidth.setW(BigInteger.valueOf(1300));

		XWPFTableCell cell1 = tableRowOne.addNewTableCell();

		paragraph = cell1.getParagraphs().get(0);
		ppr = paragraph.getCTP().getPPr();
		if (ppr == null)
			ppr = paragraph.getCTP().addNewPPr();
		spacing = ppr.isSetSpacing() ? ppr.getSpacing() : ppr.addNewSpacing();
		spacing.setAfter(BigInteger.valueOf(0));
		spacing.setBefore(BigInteger.valueOf(0));

		run = paragraph.createRun();
		run.setBold(true);
		run.setText("Value");
		run.setFontSize(8);
		run.setFontFamily("Arial");

		XWPFTableCell cell2 = tableRowOne.addNewTableCell();
		XWPFTableCell cell3 = tableRowOne.addNewTableCell();
		XWPFTableCell cell4 = tableRowOne.addNewTableCell();
		XWPFTableCell cell5 = tableRowOne.addNewTableCell();

		// merge header columns
		cell1.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
		cell2.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
		cell3.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
		cell4.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
		cell5.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);

		// create Browsename row
		XWPFTableRow tableRowTwo = table.createRow();
		setDefaultFontText(tableRowTwo.getCell(0), "Browsename", null);
		setDefaultFontText(tableRowTwo.getCell(1), selectedNode.getBrowseName().toString(), null);

		tableRowTwo.getCell(1).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
		tableRowTwo.getCell(2).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
		tableRowTwo.getCell(3).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
		tableRowTwo.getCell(4).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
		tableRowTwo.getCell(5).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);

		if (!(selectedNode instanceof MethodNode) && !(selectedNode instanceof ObjectNode)) {
			// create isAbstract row
			XWPFTableRow tableRowThree = table.createRow();
			setDefaultFontText(tableRowThree.getCell(0), "IsAbstract", null);
			if (selectedNode instanceof UAVariableTypeNode)
				setDefaultFontText(tableRowThree.getCell(1),
						Boolean.toString(((UAVariableTypeNode) selectedNode).getIsAbstract()), null);
			if (selectedNode instanceof UAObjectTypeNode)
				setDefaultFontText(tableRowThree.getCell(1),
						Boolean.toString(((UAObjectTypeNode) selectedNode).getIsAbstract()), null);
			if (selectedNode instanceof UADataTypeNode)
				setDefaultFontText(tableRowThree.getCell(1),
						Boolean.toString(((UADataTypeNode) selectedNode).getIsAbstract()), null);
			tableRowThree.getCell(1).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
			tableRowThree.getCell(2).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
			tableRowThree.getCell(3).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
			tableRowThree.getCell(4).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
			tableRowThree.getCell(5).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
		}

		// create fourth row
		XWPFTableRow tableRowFour = table.createRow();

		paragraph = tableRowFour.getCell(0).getParagraphs().get(0);
		run = paragraph.createRun();
		run.setBold(true);
		run.setFontFamily("Arial");
		run.setFontSize(8);
		run.setText("References");

		paragraph = tableRowFour.getCell(1).getParagraphs().get(0);
		cellWidth = tableRowFour.getCell(1).getCTTc().addNewTcPr().addNewTcW();
		cellWidth.setW(BigInteger.valueOf(1200));
		run = paragraph.createRun();
		run.setBold(true);
		run.setFontFamily("Arial");
		run.setFontSize(8);
		run.setText("Node Class");

		paragraph = tableRowFour.getCell(2).getParagraphs().get(0);
		cellWidth = tableRowFour.getCell(2).getCTTc().addNewTcPr().addNewTcW();
		cellWidth.setW(BigInteger.valueOf(2750));
		run = paragraph.createRun();
		run.setBold(true);
		run.setFontFamily("Arial");
		run.setFontSize(8);
		run.setText("BrowseName");

		paragraph = tableRowFour.getCell(3).getParagraphs().get(0);
		cellWidth = tableRowFour.getCell(3).getCTTc().addNewTcPr().addNewTcW();
		cellWidth.setW(BigInteger.valueOf(1500));
		run = paragraph.createRun();
		run.setBold(true);
		run.setFontFamily("Arial");
		run.setFontSize(8);
		run.setText("DataType");

		paragraph = tableRowFour.getCell(4).getParagraphs().get(0);
		cellWidth = tableRowFour.getCell(4).getCTTc().addNewTcPr().addNewTcW();
		cellWidth.setW(BigInteger.valueOf(2200));
		run = paragraph.createRun();
		run.setBold(true);
		run.setFontFamily("Arial");
		run.setFontSize(8);
		run.setText("TypeDefinition");

		paragraph = tableRowFour.getCell(5).getParagraphs().get(0);
		cellWidth = tableRowFour.getCell(5).getCTTc().addNewTcPr().addNewTcW();
		cellWidth.setW(BigInteger.valueOf(1300));
		run = paragraph.createRun();
		run.setBold(true);
		run.setFontFamily("Arial");
		run.setFontSize(8);
		run.setText("Modelling Rule");

		// create fifth row

		ExpandedNodeId parentid = selectedNode.findTarget(Identifiers.HasSubtype, true);
		if (parentid != null) {
			XWPFTableRow tableRowFive = table.createRow();
			Node node = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeById(parentid);
			if (node != null)
				setDefaultFontText(tableRowFive.getCell(0), "Subtype of " + node.getBrowseName().toString(), null);
		}

		for (ReferenceNode refnode : selectedNode.getReferences()) {

			// we do not need parent references
			if (refnode.getIsInverse())
				continue;

			Node node = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
					.getNodeById(refnode.getTargetId());

			// do not add subtypes
			if (refnode.getReferenceTypeId().equals(Identifiers.HasSubtype)
					|| refnode.getReferenceTypeId().equals(Identifiers.Organizes)) {
				// create empty paragraph
				document.createParagraph();
				// create next node documentation
				processVariableTypeNode(node);
			}
			XWPFTableRow tableRowRef = table.createRow();

			Node reftypenode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
					.getNodeById(refnode.getReferenceTypeId());

			setDefaultFontText(tableRowRef.getCell(0), reftypenode.getBrowseName().toString(), null);
			setDefaultFontText(tableRowRef.getCell(1), node.getNodeClass().name(), null);

			setDefaultFontText(tableRowRef.getCell(2), node.getBrowseName().toString(), null);
			if (node instanceof UAVariableTypeNode) {
				Node dtNode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
						.getNodeById(((UAVariableTypeNode) node).getDataType());

				setDefaultFontText(tableRowRef.getCell(3), dtNode.getBrowseName().toString(), null);

			} else if (node instanceof UAVariableNode) {
				Node dtNode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
						.getNodeById(((UAVariableNode) node).getDataType());
				if (dtNode != null)
					setDefaultFontText(tableRowRef.getCell(3), dtNode.getBrowseName().toString(), null);

			} else {
				setDefaultFontText(tableRowRef.getCell(3), "-", "ffffff");
			}

			ExpandedNodeId type = node.findTarget(Identifiers.HasTypeDefinition, false);
			reftypenode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeById(type);
			if (reftypenode != null)
				setDefaultFontText(tableRowRef.getCell(4), reftypenode.getBrowseName().toString(), null);
			else
				setDefaultFontText(tableRowRef.getCell(4), "-", "ffffff");

			String modelling = getModellingReadWrite(node);
			String modellingColor = null;
			if (modelling == null || modelling.isEmpty()) {
				modelling = "-";
				modellingColor = "ffffff";
			}
			setDefaultFontText(tableRowRef.getCell(5), modelling, modellingColor);
		}

		// add children
		for (ReferenceNode refnode : selectedNode.getReferences()) {

			// we do not need parent references
			if (refnode.getIsInverse())
				continue;

			Node node = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
					.getNodeById(refnode.getTargetId());

			// do not add subtypes
			if (refnode.getReferenceTypeId().equals(Identifiers.HasSubtype)
					|| refnode.getReferenceTypeId().equals(Identifiers.Organizes)
					|| refnode.getReferenceTypeId().equals(Identifiers.HasComponent)) {
				// create empty paragraph
				document.createParagraph();

				// create next node documentation
				switch (node.getNodeClass()) {
				case Variable:
				case VariableType:
					processVariableTypeNode(node);
					break;
				case ObjectType:
				case Object:
					processObjectTypeNode(node);
					break;
				default:
					break;
				}

			}
		}

		// write document content
		// document.write(out);
	}

	private void processVariableTypeNode(Node selectedNode) throws IOException {
		XWPFParagraph paragraph = document.createParagraph();
		XWPFRun run = paragraph.createRun();
		run.setText(selectedNode.getDisplayName().getText());
		run.setFontFamily("Arial");
		run.setFontSize(14);
		paragraph.setStyle(BTECH_HEADING1);

		paragraph = document.createParagraph();
		run = paragraph.createRun();
		run.setFontFamily("Arial");
		run.setFontSize(11);
		run.setText("Beschreibung");

		paragraph = document.createParagraph();
		run = paragraph.createRun();
		run.setFontFamily("Arial");
		run.setFontSize(11);
		run.setText(selectedNode.getDescription().toString());

		String infos = Studio_ResourceManager.NODE_INFOS.get(selectedNode.getNodeId());
		if (infos != null && !infos.isEmpty()) {
			paragraph = document.createParagraph();
			run = paragraph.createRun();
			run.setFontFamily("Arial");
			run.setFontSize(11);
			run.setText("zusätzliche Infos");

			paragraph = document.createParagraph();
			run = paragraph.createRun();
			run.setFontFamily("Arial");
			run.setFontSize(11);
			run.setText(infos);
		}
		// create table
		XWPFTable table = document.createTable();

		// create header row
		XWPFTableRow tableRowOne = table.getRow(0);

		XWPFTableCell cell0 = tableRowOne.getCell(0);
		paragraph = cell0.getParagraphs().get(0);
		CTPPr ppr = paragraph.getCTP().getPPr();
		if (ppr == null)
			ppr = paragraph.getCTP().addNewPPr();
		CTSpacing spacing = ppr.isSetSpacing() ? ppr.getSpacing() : ppr.addNewSpacing();
		spacing.setAfter(BigInteger.valueOf(0));
		spacing.setBefore(BigInteger.valueOf(0));

		run = paragraph.createRun();
		run.setBold(true);
		run.setText("Attribute");
		run.setFontFamily("Arial");
		run.setFontSize(8);

		CTTblWidth cellWidth = cell0.getCTTc().addNewTcPr().addNewTcW();
		cellWidth.setW(BigInteger.valueOf(1300));

		XWPFTableCell cell1 = tableRowOne.addNewTableCell();

		paragraph = cell1.getParagraphs().get(0);
		ppr = paragraph.getCTP().getPPr();
		if (ppr == null)
			ppr = paragraph.getCTP().addNewPPr();
		spacing = ppr.isSetSpacing() ? ppr.getSpacing() : ppr.addNewSpacing();
		spacing.setAfter(BigInteger.valueOf(0));
		spacing.setBefore(BigInteger.valueOf(0));

		run = paragraph.createRun();
		run.setBold(true);
		run.setText("Value");
		run.setFontSize(8);
		run.setFontFamily("Arial");

		XWPFTableCell cell2 = tableRowOne.addNewTableCell();
		XWPFTableCell cell3 = tableRowOne.addNewTableCell();
		XWPFTableCell cell4 = tableRowOne.addNewTableCell();
		XWPFTableCell cell5 = tableRowOne.addNewTableCell();

		// merge header columns
		cell1.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
		cell2.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
		cell3.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
		cell4.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
		cell5.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);

		// create Browsename row
		XWPFTableRow tableRowTwo = table.createRow();
		setDefaultFontText(tableRowTwo.getCell(0), "Browsename", null);
		setDefaultFontText(tableRowTwo.getCell(1), selectedNode.getBrowseName().toString(), null);

		tableRowTwo.getCell(1).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
		tableRowTwo.getCell(2).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
		tableRowTwo.getCell(3).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
		tableRowTwo.getCell(4).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
		tableRowTwo.getCell(5).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);

		if (!(selectedNode instanceof MethodNode) && !(selectedNode instanceof VariableNode)) {
			// create isAbstract row
			XWPFTableRow tableRowThree = table.createRow();
			setDefaultFontText(tableRowThree.getCell(0), "IsAbstract", null);
			if (selectedNode instanceof UAVariableTypeNode)
				setDefaultFontText(tableRowThree.getCell(1),
						Boolean.toString(((UAVariableTypeNode) selectedNode).getIsAbstract()), null);
			if (selectedNode instanceof UAObjectTypeNode)
				setDefaultFontText(tableRowThree.getCell(1),
						Boolean.toString(((UAObjectTypeNode) selectedNode).getIsAbstract()), null);
			if (selectedNode instanceof UADataTypeNode)
				setDefaultFontText(tableRowThree.getCell(1),
						Boolean.toString(((UADataTypeNode) selectedNode).getIsAbstract()), null);
			tableRowThree.getCell(1).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
			tableRowThree.getCell(2).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
			tableRowThree.getCell(3).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
			tableRowThree.getCell(4).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
			tableRowThree.getCell(5).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);

			// create value rank
			if (selectedNode instanceof UAVariableTypeNode) {
				XWPFTableRow tableRowValueRank = table.createRow();
				setDefaultFontText(tableRowValueRank.getCell(0), "ValueRank", null);

				setDefaultFontText(tableRowValueRank.getCell(1),
						ValueRanks.getValueRanks(((UAVariableTypeNode) selectedNode).getValueRank()).toString(), null);
				tableRowValueRank.getCell(1).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
				tableRowValueRank.getCell(2).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
				tableRowValueRank.getCell(3).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
				tableRowValueRank.getCell(4).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
				tableRowValueRank.getCell(5).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);

				Node dataType = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
						.getNodeById(((UAVariableTypeNode) selectedNode).getDataType());

				if (dataType != null) {
					XWPFTableRow tableRowDataType = table.createRow();
					setDefaultFontText(tableRowDataType.getCell(0), "DataType", null);

					setDefaultFontText(tableRowDataType.getCell(1), dataType.getBrowseName().toString(), null);
					tableRowDataType.getCell(1).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
					tableRowDataType.getCell(2).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
					tableRowDataType.getCell(3).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
					tableRowDataType.getCell(4).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
					tableRowDataType.getCell(5).getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
				}
			}
		}

		// create fourth row
		XWPFTableRow tableRowFour = table.createRow();

		paragraph = tableRowFour.getCell(0).getParagraphs().get(0);
		run = paragraph.createRun();
		run.setBold(true);
		run.setFontFamily("Arial");
		run.setFontSize(8);
		run.setText("References");

		paragraph = tableRowFour.getCell(1).getParagraphs().get(0);
		cellWidth = tableRowFour.getCell(1).getCTTc().addNewTcPr().addNewTcW();
		cellWidth.setW(BigInteger.valueOf(800));
		run = paragraph.createRun();
		run.setBold(true);
		run.setFontFamily("Arial");
		run.setFontSize(8);
		run.setText("Node Class");

		paragraph = tableRowFour.getCell(2).getParagraphs().get(0);
		cellWidth = tableRowFour.getCell(2).getCTTc().addNewTcPr().addNewTcW();
		cellWidth.setW(BigInteger.valueOf(2750));
		run = paragraph.createRun();
		run.setBold(true);
		run.setFontFamily("Arial");
		run.setFontSize(8);
		run.setText("BrowseName");

		paragraph = tableRowFour.getCell(3).getParagraphs().get(0);
		cellWidth = tableRowFour.getCell(3).getCTTc().addNewTcPr().addNewTcW();
		cellWidth.setW(BigInteger.valueOf(2200));
		run = paragraph.createRun();
		run.setBold(true);
		run.setFontFamily("Arial");
		run.setFontSize(8);
		run.setText("DataType");

		paragraph = tableRowFour.getCell(4).getParagraphs().get(0);
		cellWidth = tableRowFour.getCell(4).getCTTc().addNewTcPr().addNewTcW();
		cellWidth.setW(BigInteger.valueOf(1300));
		run = paragraph.createRun();
		run.setBold(true);
		run.setFontFamily("Arial");
		run.setFontSize(8);
		run.setText("TypeDefinition");

		paragraph = tableRowFour.getCell(5).getParagraphs().get(0);
		cellWidth = tableRowFour.getCell(5).getCTTc().addNewTcPr().addNewTcW();
		cellWidth.setW(BigInteger.valueOf(1300));
		run = paragraph.createRun();
		run.setBold(true);
		run.setFontFamily("Arial");
		run.setFontSize(8);
		run.setText("Modelling Rule");

		// create fifth row

		ExpandedNodeId parentid = selectedNode.findTarget(Identifiers.HasSubtype, true);
		if (parentid != null) {
			Node node = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeById(parentid);
			if (node != null) {
				XWPFTableRow tableRowFive = table.createRow();
				setDefaultFontText(tableRowFive.getCell(0), "Subtype of " + node.getBrowseName().toString(), null);
			}
		}

		for (ReferenceNode refnode : selectedNode.getReferences()) {

			// we do not need parent references
			if (refnode.getIsInverse())
				continue;

			Node node = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
					.getNodeById(refnode.getTargetId());

			if (node == null)
				continue;

			XWPFTableRow tableRowRef = table.createRow();

			Node reftypenode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
					.getNodeById(refnode.getReferenceTypeId());

			setDefaultFontText(tableRowRef.getCell(0), reftypenode.getBrowseName().toString(), null);
			setDefaultFontText(tableRowRef.getCell(1), node.getNodeClass().name(), null);

			setDefaultFontText(tableRowRef.getCell(2), node.getBrowseName().toString(), null);
			if (node instanceof UAVariableTypeNode) {
				Node dtNode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
						.getNodeById(((UAVariableTypeNode) node).getDataType());

				setDefaultFontText(tableRowRef.getCell(3), dtNode.getBrowseName().toString(), null);

			} else if (node instanceof UAVariableNode) {
				Node dtNode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
						.getNodeById(((UAVariableNode) node).getDataType());

				setDefaultFontText(tableRowRef.getCell(3), dtNode.getBrowseName().toString(), null);
			} else {
				setDefaultFontText(tableRowRef.getCell(3), "-", "ffffff");
			}
			ExpandedNodeId type = node.findTarget(Identifiers.HasTypeDefinition, false);
			reftypenode = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager().getNodeById(type);
			if (reftypenode != null)
				setDefaultFontText(tableRowRef.getCell(4), reftypenode.getBrowseName().toString(), null);
			else
				setDefaultFontText(tableRowRef.getCell(4), "-", "ffffff");

			String modelling = getModellingReadWrite(node);
			String modellingColor = null;
			if (modelling == null || modelling.isEmpty()) {
				modelling = "-";
				modellingColor = "ffffff";
			}
			setDefaultFontText(tableRowRef.getCell(5), modelling, modellingColor);
		}

		// add children
		for (ReferenceNode refnode : selectedNode.getReferences()) {

			// we do not need parent references
			if (refnode.getIsInverse())
				continue;

			Node node = ServerInstance.getInstance().getServerInstance().getAddressSpaceManager()
					.getNodeById(refnode.getTargetId());

			// do not add subtypes
			if (refnode.getReferenceTypeId().equals(Identifiers.HasSubtype)
					|| refnode.getReferenceTypeId().equals(Identifiers.Organizes)) {
				// create empty paragraph
				document.createParagraph();

				// create next node documentation
				switch (node.getNodeClass()) {
				case Variable:
				case VariableType:
					processVariableTypeNode(node);
					break;
				case ObjectType:
				case Object:
					processObjectTypeNode(node);
					break;
				default:
					break;
				}

			}
		}
		// write document content
		// document.write(out);
	}

	private void setDefaultFontText(XWPFTableCell cell, String text, String color) {
		XWPFParagraph paragraph = cell.getParagraphs().get(0);
		CTPPr ppr = paragraph.getCTP().getPPr();
		if (ppr == null)
			ppr = paragraph.getCTP().addNewPPr();
		CTSpacing spacing = ppr.isSetSpacing() ? ppr.getSpacing() : ppr.addNewSpacing();
		spacing.setAfter(BigInteger.valueOf(0));
		spacing.setBefore(BigInteger.valueOf(0));
		XWPFRun run = paragraph.createRun();
		run.setText(text);
		run.setFontFamily("Arial");
		run.setFontSize(8);
		if (color != null)
			run.setColor(color);
	}
}
