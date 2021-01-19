package com.bichler.opcua.statemachine.reverse.model;

import java.io.BufferedWriter;
import java.io.IOException;

import com.bichler.opcua.statemachine.reverse.IdGenerator;
import com.bichler.opcua.statemachine.reverse.engineering.IUMLTranslation;
import com.bichler.opcua.statemachine.reverse.engineering.ReverseTranslationConstants;

public class GeneralizationEdgeNotation extends AbstractEdgeNotation {

	public GeneralizationEdgeNotation(String id, IUMLTranslation source, IUMLTranslation target) {
		super(id, source, target);
	}

	@Override
	public void notate(BufferedWriter out, IdGenerator idGenerator, String modelName) throws IOException {
		String edgeId = getSource().getEdgeElementId();
		notate(out, idGenerator, modelName, edgeId);
	}

	@Override
	public void notate(BufferedWriter out, IdGenerator idGenerator, String modelName, String edgeId)
			throws IOException {

		String sourceId = getSource().getNotationId();
		String targetId = getTarget().getNotationId();

		out.write(
				"  <" + ReverseTranslationConstants.UML_TAG_EDGES + " " + ReverseTranslationConstants.UML_ATTRIBUTE_TYPE
						+ "=\"notation:Connector\" xmi:id=\"" + idGenerator.buildId(6)
						+ "\" type=\"Generalization_Edge\" source=\"" + sourceId + "\" target=\"" + targetId + "\">");
		out.newLine();
		out.write("    <" + ReverseTranslationConstants.UML_TAG_CHILDREN + " "
				+ ReverseTranslationConstants.UML_ATTRIBUTE_TYPE + "=\"notation:DecorationNode\" xmi:id=\""
				+ idGenerator.buildId(6) + "\" type=\"Generalization_StereotypeLabel\">");
		out.newLine();
		out.write("      <" + ReverseTranslationConstants.UML_TAG_STYLES + " "
				+ ReverseTranslationConstants.UML_ATTRIBUTE_TYPE + "=\"notation:BooleanValueStyle\" xmi:id=\""
				+ idGenerator.buildId(6) + "\" name=\"IS_UPDATED_POSITION\" booleanValue=\"true\"/>");
		out.newLine();
		out.write("      <" + ReverseTranslationConstants.UML_TAG_LAYOUTCONSTRAINT + " "
				+ ReverseTranslationConstants.UML_ATTRIBUTE_TYPE + "=\"notation:Location\" xmi:id=\""
				+ idGenerator.buildId(6) + "\" y=\"39\"/>");
		out.newLine();
		out.write("    </" + ReverseTranslationConstants.UML_TAG_CHILDREN + ">");
		out.newLine();
		out.write("    <" + ReverseTranslationConstants.UML_TAG_STYLES + " "
				+ ReverseTranslationConstants.UML_ATTRIBUTE_TYPE + "=\"notation:FontStyle\" xmi:id=\""
				+ idGenerator.buildId(6) + "\"/>");
		out.newLine();
		out.write("    <" + ReverseTranslationConstants.UML_TAG_ELEMENT + " "
				+ ReverseTranslationConstants.UML_ATTRIBUTE_TYPE + "=\"uml:Generalization\" href=\"" + modelName
				+ ".uml#" + edgeId + "\"/>");
		out.newLine();
		out.write("    <" + ReverseTranslationConstants.UML_TAG_BENDPOINTS + " "
				+ ReverseTranslationConstants.UML_ATTRIBUTE_TYPE + "=\"notation:RelativeBendpoints\" xmi:id=\""
				+ idGenerator.buildId(6) + "\" points=\"[0, 0, 0, 0]$[0, 0, 0, 0]\"/>");
		out.newLine();
		out.write("    <" + ReverseTranslationConstants.UML_TAG_SOURCEANCHOR + " "
				+ ReverseTranslationConstants.UML_ATTRIBUTE_TYPE + "=\"notation:IdentityAnchor\" xmi:id=\""
				+ idGenerator.buildId(6) + "\" id=\"(0.5,0.0)\"/>");
		out.newLine();
		out.write("    <" + ReverseTranslationConstants.UML_TAG_TARGETANCHOR + " "
				+ ReverseTranslationConstants.UML_ATTRIBUTE_TYPE + "=\"notation:IdentityAnchor\" xmi:id=\""
				+ idGenerator.buildId(6) + "\" id=\"(0.5,1.0)\"/>");
		out.newLine();
		out.write("  </" + ReverseTranslationConstants.UML_TAG_EDGES + ">");
		out.newLine();
	}

}
