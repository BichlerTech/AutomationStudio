package com.bichler.opcua.statemachine.reverse.model;

import java.io.BufferedWriter;
import java.io.IOException;

import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.opcua.statemachine.reverse.IdGenerator;
import com.bichler.opcua.statemachine.reverse.engineering.IUMLTranslation;
import com.bichler.opcua.statemachine.reverse.engineering.ReverseTranslationConstants;

public abstract class AbstractUMLTranslation implements IUMLTranslation {
	private IUMLTranslation parent = null;
	private String umlId = "";
	private NodeId nodeId = NodeId.NULL;
	private String modelname = "";
	private String packageName = "";

	private String edgeElementId = "";
	private String notationId = "";
	private String name = "";

	public AbstractUMLTranslation(String modelname, NodeId nodeId, String umlId, IUMLTranslation parent) {
		this.modelname = modelname;
		this.nodeId = nodeId;
		this.umlId = umlId;
		this.parent = parent;
	}

	@Override
	public IEdgeNotation notate(BufferedWriter out, IdGenerator idGenerator) throws IOException {
		setNotationId(idGenerator.buildId(6));

		out.write("    <children xmi:type=\"notation:Shape\" xmi:id=\"" + getNotationId()
				+ "\" type=\""+getShapeType()+"_Shape\">");
		out.newLine();
		out.write("      <children xmi:type=\"notation:DecorationNode\" xmi:id=\"" + idGenerator.buildId(6)
				+ "\" type=\""+getShapeType()+"_NameLabel\"/>");
		out.newLine();
		out.write("      <children xmi:type=\"notation:DecorationNode\" xmi:id=\"" + idGenerator.buildId(6)
				+ "\" type=\""+getShapeType()+"_FloatingNameLabel\">");
		out.newLine();
		out.write("        <layoutConstraint xmi:type=\"notation:Location\" xmi:id=\"" + idGenerator.buildId(6)
				+ "\" y=\"15\"/>");
		out.newLine();
		out.write("      </children>");
		out.newLine();
		out.write("      <children xmi:type=\"notation:BasicCompartment\" xmi:id=\"" + idGenerator.buildId(6)
				+ "\" type=\""+getShapeType()+"_AttributeCompartment\">");
		out.newLine();
		out.write("        <styles xmi:type=\"notation:TitleStyle\" xmi:id=\"" + idGenerator.buildId(6) + "\"/>");
		out.newLine();
		out.write("        <styles xmi:type=\"notation:SortingStyle\" xmi:id=\"" + idGenerator.buildId(6) + "\"/>");
		out.newLine();
		out.write("        <styles xmi:type=\"notation:FilteringStyle\" xmi:id=\"" + idGenerator.buildId(6) + "\"/>");
		out.newLine();
		out.write("        <layoutConstraint xmi:type=\"notation:Bounds\" xmi:id=\"" + idGenerator.buildId(6) + "\"/>");
		out.newLine();
		out.write("      </children>");
		out.newLine();
		out.write("      <children xmi:type=\"notation:BasicCompartment\" xmi:id=\"" + idGenerator.buildId(6)
				+ "\" type=\""+getShapeType()+"_OperationCompartment\">");
		out.newLine();
		out.write("        <styles xmi:type=\"notation:TitleStyle\" xmi:id=\"" + idGenerator.buildId(6) + "\"/>");
		out.newLine();
		out.write("        <styles xmi:type=\"notation:SortingStyle\" xmi:id=\"" + idGenerator.buildId(6) + "\"/>");
		out.newLine();
		out.write("        <styles xmi:type=\"notation:FilteringStyle\" xmi:id=\"" + idGenerator.buildId(6) + "\"/>");
		out.newLine();
		out.write("        <layoutConstraint xmi:type=\"notation:Bounds\" xmi:id=\"" + idGenerator.buildId(6) + "\"/>");
		out.newLine();
		out.write("      </children>");
		out.newLine();
		out.write("      <children xmi:type=\"notation:BasicCompartment\" xmi:id=\"" + idGenerator.buildId(6)
				+ "\" type=\""+getShapeType()+"_NestedClassifierCompartment\">");
		out.newLine();
		out.write("        <styles xmi:type=\"notation:TitleStyle\" xmi:id=\"" + idGenerator.buildId(6) + "\"/>");
		out.newLine();
		out.write("        <styles xmi:type=\"notation:SortingStyle\" xmi:id=\"" + idGenerator.buildId(6) + "\"/>");
		out.newLine();
		out.write("        <styles xmi:type=\"notation:FilteringStyle\" xmi:id=\"" + idGenerator.buildId(6) + "\"/>");
		out.newLine();
		out.write("        <layoutConstraint xmi:type=\"notation:Bounds\" xmi:id=\"" + idGenerator.buildId(6) + "\"/>");
		out.newLine();
		out.write("      </children>");
		out.newLine();
		out.write("      <element xmi:type=\"uml:"+getShapeType()+"\" href=\"" + getModelname() + ".uml#" + getId() + "\"/>");
		out.newLine();
		out.write("      <layoutConstraint xmi:type=\"notation:Bounds\" xmi:id=\"" + idGenerator.buildId(6)
				+ "\" x=\"0\" y=\"0\"/>");
		out.newLine();
		out.write("  </children>");
		out.newLine();

		IEdgeNotation edge = createEdge(idGenerator);

		return edge;
	}

	@Override
	public IEdgeNotation createEdge(IdGenerator idGenerator) {
		if (hasParent()) {
			return new GeneralizationEdgeNotation(idGenerator.buildId(6), this, getParent());
		}
		return null;
	}

	private boolean hasParent() {
		return getParent() != null;
	}

	@Override
	public void translate(BufferedWriter out, IdGenerator idGenerator) throws IOException {
		boolean hasParent = getParent() != null;
		boolean hasSameModel = true;
		if (hasParent) {
			hasSameModel = getModelname().equals(getParent().getModelname());
		}

		StringBuilder builder = new StringBuilder();

		builder.append(ReverseTranslationConstants.UML_SPACE + ReverseTranslationConstants.UML_SPACE
				+ ReverseTranslationConstants.UML_SPACE + ReverseTranslationConstants.UML_SPACE + "<");
		builder.append(ReverseTranslationConstants.UML_TAG_PACKAGEDELEMENT);
		builder.append(ReverseTranslationConstants.UML_SPACE);
		builder.append(ReverseTranslationConstants.UML_ATTRIBUTE_TYPE);
		builder.append(ReverseTranslationConstants.UML_EQUAL);
		builder.append(ReverseTranslationConstants.UML_QUOTES);
		builder.append(ReverseTranslationConstants.UML_VALUE_UML+getShapeType());
		builder.append(ReverseTranslationConstants.UML_QUOTES);
		builder.append(ReverseTranslationConstants.UML_SPACE);
		builder.append(ReverseTranslationConstants.UML_ATTRIBUTE_ID);
		builder.append(ReverseTranslationConstants.UML_EQUAL);
		builder.append(ReverseTranslationConstants.UML_QUOTES);
		builder.append(getId());
		builder.append(ReverseTranslationConstants.UML_QUOTES);
		builder.append(ReverseTranslationConstants.UML_SPACE);
		builder.append(ReverseTranslationConstants.UML_NAME);
		builder.append(ReverseTranslationConstants.UML_EQUAL);
		builder.append(ReverseTranslationConstants.UML_QUOTES);
		builder.append(getName());
		builder.append(ReverseTranslationConstants.UML_QUOTES);

		if (!hasParent) {
			builder.append("/>");
		} else {
			builder.append(">");
		}

		out.write(builder.toString());
		out.newLine();

		if (hasParent) {
			builder.delete(0, builder.length());

			setEdgeElementId(idGenerator.buildId(5));

			builder.append(ReverseTranslationConstants.UML_SPACE + ReverseTranslationConstants.UML_SPACE
					+ ReverseTranslationConstants.UML_SPACE + ReverseTranslationConstants.UML_SPACE
					+ ReverseTranslationConstants.UML_SPACE + ReverseTranslationConstants.UML_SPACE);
			builder.append("<");
			builder.append(ReverseTranslationConstants.UML_TAG_GENERALIZATION);
			builder.append(ReverseTranslationConstants.UML_SPACE);
			builder.append(ReverseTranslationConstants.UML_ATTRIBUTE_TYPE);
			builder.append(ReverseTranslationConstants.UML_EQUAL);
			builder.append(ReverseTranslationConstants.UML_QUOTES);
			builder.append(ReverseTranslationConstants.UML_VALUE_GENERALIZATION);
			builder.append(ReverseTranslationConstants.UML_QUOTES);
			builder.append(ReverseTranslationConstants.UML_SPACE);
			builder.append(ReverseTranslationConstants.UML_ATTRIBUTE_ID);
			builder.append(ReverseTranslationConstants.UML_EQUAL);
			builder.append(ReverseTranslationConstants.UML_QUOTES);
			builder.append(getEdgeElementId());
			builder.append(ReverseTranslationConstants.UML_QUOTES);

			if (hasSameModel) {
				builder.append(ReverseTranslationConstants.UML_SPACE);
				builder.append(ReverseTranslationConstants.UML_TAG_GENERAL);
				builder.append(ReverseTranslationConstants.UML_EQUAL);
				builder.append(ReverseTranslationConstants.UML_QUOTES);
				builder.append(getParent().getId());
				builder.append(ReverseTranslationConstants.UML_QUOTES);
				builder.append("/>");
			} else {
				builder.append(">");
			}

			out.write(builder.toString());
			out.newLine();
			builder.delete(0, builder.length());

			if (!hasSameModel) {
				out.write("        <general xmi:type=\"uml:"+getShapeType()+"\" href=\"" + getParent().getModelname() + ".uml#"
						+ getParent().getId() + "\"/>");
				out.newLine();
				out.write("     </generalization>");
				out.newLine();
			}

			builder.append(ReverseTranslationConstants.UML_SPACE + ReverseTranslationConstants.UML_SPACE
					+ ReverseTranslationConstants.UML_SPACE + ReverseTranslationConstants.UML_SPACE);
			builder.append("</");
			builder.append(ReverseTranslationConstants.UML_TAG_PACKAGEDELEMENT);
			builder.append(">");

			out.write(builder.toString());
			out.newLine();
		}
	}

	@Override
	public String toString() {
		return getName() + " " + super.toString();
	}

	@Override
	public String getId() {
		return this.umlId;
	}

	@Override
	public NodeId getNodeId() {
		return nodeId;
	}

	@Override
	public String getEdgeElementId() {
		return this.edgeElementId;
	}

	@Override
	public void setEdgeElementId(String edgeId) {
		this.edgeElementId = edgeId;
	}

	@Override
	public String getModelname() {
		return this.modelname;
	}

	@Override
	public String getPackageName() {
		return this.packageName;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public IUMLTranslation getParent() {
		return this.parent;
	}

	@Override
	public String getNotationId() {
		return this.notationId;
	}

	@Override
	public void setNotationId(String id) {
		this.notationId = id;
	}

	@Override
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public abstract String getShapeType();
	
}
