package opc.sdk.core.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import opc.sdk.core.application.ElementType;
import opc.sdk.core.types.TypeTable;

import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.UnsignedShort;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.RelativePathElement;
import org.opcfoundation.ua.core.StatusCodes;

/**
 * RelativePath Formatter
 * 
 * @author Thomas Z&ouml;bauer
 * 
 */
public class RelativePathFormatter {
	private List<Element> elements = null;

	public RelativePathFormatter() {
		this.elements = new ArrayList<>();
	}

	/**
	 * Adds an element to the RelativePathFormatter
	 * 
	 * @param element
	 */
	private void addElement(Element element) {
		this.elements.add(element);
	}

	public static RelativePathFormatter parse(QualifiedName[] relativePaths, NamespaceTable currentTable,
			NamespaceTable targetTable) {
		RelativePathFormatter path = parse(relativePaths);
		path.translateNamespaceIndexes(currentTable, targetTable);
		return path;
	}

	/**
	 * Updates the path to use the indexes from the target table
	 * 
	 * @param currentTable
	 * @param targetTable
	 */
	private void translateNamespaceIndexes(NamespaceTable currentTable, NamespaceTable targetTable) {
		// create mapping table
		Map<Integer, Integer> mapping = new HashMap<>();
		String[] namespaceUris = currentTable.toArray();
		for (int ii = 0; ii < namespaceUris.length; ii++) {
			mapping.put(ii, targetTable.getIndex(namespaceUris[ii]));
		}
		// update each element
		for (Element element : this.elements) {
			QualifiedName qname = element.getReferenceTypeName();
			if (qname != null) {
				element.setReferenceTypeName(
						new QualifiedName(mapping.get(qname.getNamespaceIndex()), qname.getName()));
			}
		}
	}

	/**
	 * Parses a string representing a relative path
	 * 
	 * @param relativePaths
	 * @return
	 */
	private static RelativePathFormatter parse(QualifiedName[] relativePaths) {
		if (relativePaths == null || relativePaths.length == 0) {
			return new RelativePathFormatter();
		}
		String stringPath = parseRelativePathToString(relativePaths);
		StringReader reader = new StringReader(stringPath);
		RelativePathFormatter path = new RelativePathFormatter();
		while (Utils.peek(reader) != -1) {
			try {
				Element element = Element.parse(reader);
				path.addElement(element);
			} catch (IOException e) {
				Logger.getLogger(RelativePathElement.class.getName()).log(Level.SEVERE, e.getMessage(), e);
			}
		}
		return path;
	}

	private static String parseRelativePathToString(QualifiedName[] relativePaths) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < relativePaths.length; i++) {
			QualifiedName sub = relativePaths[i];
			// <NamespaceIndex>:<Name>
			if (sub.getNamespaceIndex() != 0) {
				builder.append(sub.getNamespaceIndex());
				builder.append(":");
			}
			builder.append(sub.getName());
			if (i < relativePaths.length - 1) {
				// appends path for more elements
				builder.append("/");
			}
		}
		return builder.toString();
	}

	public List<Element> getElements() {
		return this.elements;
	}

	public static class Element {
		private ElementType elementType = null;
		private Boolean includeSubtypes = null;
		private QualifiedName targetName = null;
		private QualifiedName referenceTypeName = null;

		/**
		 * Initializes the object the default values
		 */
		public Element() {
			this.elementType = ElementType.ANYHIERACHICAL;
			this.referenceTypeName = null;
			this.includeSubtypes = true;
			this.targetName = null;
		}

		public Element(RelativePathElement element, TypeTable typeTree) {
			if (element == null) {
				throw new IllegalArgumentException("Element");
			}
			if (typeTree == null) {
				throw new IllegalArgumentException("TypeTree");
			}
			this.referenceTypeName = null;
			this.targetName = element.getTargetName();
			this.elementType = ElementType.FORWARDREFERENCE;
			this.includeSubtypes = element.getIncludeSubtypes();
			if (!element.getIsInverse() && element.getIncludeSubtypes()) {
				if (element.getReferenceTypeId().equals(Identifiers.HierarchicalReferences)) {
					this.elementType = ElementType.ANYHIERACHICAL;
				} else if (element.getReferenceTypeId().equals(Identifiers.Aggregates)) {
					this.elementType = ElementType.ANYCOMPONENT;
				}
			} else {
				if (element.getIsInverse()) {
					this.elementType = ElementType.INVERSEREFERENCE;
				}
				this.referenceTypeName = typeTree.findReferenceTypeName(element.getReferenceTypeId());
			}
		}

		public static Element parse(StringReader reader) throws IOException {
			// CHECK
			Element element = new Element();
			int next = Utils.peek(reader);
			// do not move the iterator
			switch (next) {
			case '/':
				element.setElementType(ElementType.ANYHIERACHICAL);
				reader.skip(1);
				break;
			case '.':
				element.setElementType(ElementType.ANYCOMPONENT);
				reader.skip(1);
				break;
			case '<':
				element.setElementType(ElementType.FORWARDREFERENCE);
				reader.skip(1);
				if (Utils.peek(reader) == '#') {
					element.setIncludeTypes(false);
					reader.skip(1);
				}
				if (Utils.peek(reader) == '!') {
					element.setElementType(ElementType.INVERSEREFERENCE);
					reader.skip(1);
				}
				element.setReferenceTypeName(parseName(reader, true));
				break;
			default:
				element.setElementType(ElementType.ANYHIERACHICAL);
				break;
			}
			element.setTargetName(parseName(reader, false));
			return element;
		}

		/**
		 * Extracts a browse name with an optional namespace prefix from the reader
		 * 
		 * @param reader
		 * @param next
		 * @param b
		 * @return
		 * @throws IOException
		 * @throws IOException
		 */
		private static QualifiedName parseName(StringReader reader, boolean referenceName) throws IOException {
			UnsignedShort namespaceIndex = new UnsignedShort(0);
			StringBuilder buffer = new StringBuilder();
			int last = Utils.peek(reader);
			// extract namespace index if requested
			for (int next = last; next != -1; next = Utils.peek(reader)) {
				last = next;
				if (!Character.isDigit(next)) {
					if (next == ':') {
						reader.skip(1);
						namespaceIndex = new UnsignedShort(buffer.toString());
						buffer.setLength(0);
						// fetch next character
						last = Utils.peek(reader);
					}
					break;
				}
				buffer.append(Character.toChars(next));
				reader.skip(1);
			}
			// extract rest of name
			for (int next = last; next != -1; next = Utils.peek(reader)) {
				last = next;
				// check for terminator
				if (referenceName) {
					if (next == '>') {
						reader.skip(1);
						break;
					}
				} else {
					if (next == '<' || next == '/' || next == '.') {
						break;
					}
				}
				// check for invalid character
				if (next == '!' || next == ':' || next == '<' || next == '>' || next == '/' || next == '.') {
					try {
						throw new ServiceResultException(StatusCodes.Bad_SyntaxError);
					} catch (ServiceResultException e) {
						Logger.getLogger(RelativePathElement.class.getName()).log(Level.SEVERE, e.getMessage(), e);
					}
				}
				// check for escape character
				if (next == '&') {
					reader.skip(1);
					next = reader.read();
					buffer.append(next);
					continue;
				}
				// append character
				buffer.append(Character.toChars(next));
				reader.skip(1);
			}
			// check for enclosing bracket
			if (referenceName && last != '>') {
				try {
					throw new ServiceResultException(StatusCodes.Bad_SyntaxError);
				} catch (ServiceResultException e) {
					Logger.getLogger(RelativePathElement.class.getName()).log(Level.SEVERE, e.getMessage(), e);
				}
			}
			if (buffer.length() == 0) {
				if (referenceName) {
					try {
						throw new ServiceResultException(StatusCodes.Bad_SyntaxError);
					} catch (ServiceResultException e) {
						Logger.getLogger(RelativePathElement.class.getName()).log(Level.SEVERE, e.getMessage(), e);
					}
				}
				if (namespaceIndex.intValue() == 0) {
					return null;
				}
			}
			return new QualifiedName(namespaceIndex, buffer.toString());
		}

		/**
		 * Whether to include subtypes of the reference type
		 * 
		 * @param includeSubtypes
		 */
		private void setIncludeTypes(boolean includeSubtypes) {
			this.includeSubtypes = includeSubtypes;
		}

		/**
		 * Set the type of element
		 * 
		 * @param elementType
		 */
		private void setElementType(ElementType elementType) {
			this.elementType = elementType;
		}

		/**
		 * The browse name of the target to find
		 * 
		 * @param parseName
		 */
		private void setTargetName(QualifiedName targetName) {
			this.targetName = targetName;
		}

		/**
		 * The browse name of the reference type to follow
		 * 
		 * @param parseName
		 */
		void setReferenceTypeName(QualifiedName referenceTypeName) {
			this.referenceTypeName = referenceTypeName;
		}

		public Boolean getIncludeSubtypes() {
			return this.includeSubtypes;
		}

		public QualifiedName getReferenceTypeName() {
			return this.referenceTypeName;
		}

		public QualifiedName getTargetName() {
			return this.targetName;
		}

		public ElementType getElementType() {
			return this.elementType;
		}
	}
}
