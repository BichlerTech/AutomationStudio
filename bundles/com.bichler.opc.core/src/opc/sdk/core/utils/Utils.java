package opc.sdk.core.utils;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.QualifiedName;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.RelativePath;
import org.opcfoundation.ua.core.RelativePathElement;
import org.opcfoundation.ua.core.StatusCodes;
import org.opcfoundation.ua.core.ViewDescription;
import org.opcfoundation.ua.utils.NumericRange;

import opc.sdk.core.application.ElementType;
import opc.sdk.core.types.TypeTable;
import opc.sdk.core.utils.RelativePathFormatter.Element;

/**
 * OPC Core Utils.
 * 
 * @author Thomas Z&ouml;bauer
 * 
 */
public class Utils {
	public static final String[] DISCOVERYURLS = { "opc.tcp://%s:4840", "http://%s:4840/UADiscovery",
			"http://%s/UADiscovery/Default.svc" };
	public static final UnsignedInteger[] OBJECTATTRIBUTES = { Attributes.NodeId, Attributes.NodeClass,
			Attributes.BrowseName, Attributes.DisplayName, Attributes.Description, Attributes.WriteMask,
			Attributes.UserWriteMask, Attributes.EventNotifier };
	public static final UnsignedInteger[] OBJECTTYPEATTRIBUTES = { Attributes.NodeId, Attributes.NodeClass,
			Attributes.BrowseName, Attributes.DisplayName, Attributes.Description, Attributes.WriteMask,
			Attributes.UserWriteMask, Attributes.IsAbstract };
	public static final UnsignedInteger[] VARIABLEATTRIBUTES = { Attributes.NodeId, Attributes.NodeClass,
			Attributes.BrowseName, Attributes.DisplayName, Attributes.Description, Attributes.WriteMask,
			Attributes.UserWriteMask, Attributes.Value, Attributes.DataType, Attributes.ValueRank,
			Attributes.ArrayDimensions, Attributes.AccessLevel, Attributes.UserAccessLevel,
			Attributes.MinimumSamplingInterval, Attributes.Historizing };
	public static final UnsignedInteger[] VARIABLETYPEATTRIBUTES = { Attributes.NodeId, Attributes.NodeClass,
			Attributes.BrowseName, Attributes.DisplayName, Attributes.Description, Attributes.WriteMask,
			Attributes.UserWriteMask, Attributes.Value, Attributes.DataType, Attributes.ValueRank,
			Attributes.ArrayDimensions, Attributes.IsAbstract };
	public static final UnsignedInteger[] DATATYPEATTRIBUTES = { Attributes.NodeId, Attributes.NodeClass,
			Attributes.BrowseName, Attributes.DisplayName, Attributes.Description, Attributes.WriteMask,
			Attributes.UserWriteMask, Attributes.IsAbstract };
	public static final UnsignedInteger[] METHODATTRIBUTES = { Attributes.NodeId, Attributes.NodeClass,
			Attributes.BrowseName, Attributes.DisplayName, Attributes.Description, Attributes.WriteMask,
			Attributes.UserWriteMask, Attributes.Executable, Attributes.UserExecutable };
	public static final UnsignedInteger[] REFERENCETYPEATTRIBUTES = { Attributes.NodeId, Attributes.NodeClass,
			Attributes.BrowseName, Attributes.DisplayName, Attributes.Description, Attributes.WriteMask,
			Attributes.UserWriteMask, Attributes.IsAbstract, Attributes.Symmetric, Attributes.InverseName };
	public static final UnsignedInteger[] VIEWATTRIBUTES = { Attributes.NodeId, Attributes.NodeClass,
			Attributes.BrowseName, Attributes.DisplayName, Attributes.Description, Attributes.WriteMask,
			Attributes.UserWriteMask, Attributes.ContainsNoLoops, Attributes.EventNotifier };
	public static final UnsignedInteger[] ALL_ATTRIBUTES = { Attributes.NodeId, Attributes.NodeClass,
			Attributes.BrowseName, Attributes.DisplayName, Attributes.Description, Attributes.WriteMask,
			Attributes.UserWriteMask, Attributes.EventNotifier, Attributes.IsAbstract, Attributes.ContainsNoLoops,
			Attributes.Executable, Attributes.Symmetric, Attributes.InverseName, Attributes.UserExecutable,
			Attributes.Value, Attributes.DataType, Attributes.ValueRank, Attributes.ArrayDimensions,
			Attributes.AccessLevel, Attributes.UserAccessLevel, Attributes.MinimumSamplingInterval,
			Attributes.Historizing };

	private Utils() {
	}

	/**
	 * Applys the index range to an array value.
	 * 
	 * @param range
	 * @param value
	 * @return
	 */
	public static StatusCode applyIndexRange(NumericRange range, DataValue value) {
		int begin = range.getBegin();
		int end = range.getEnd();
		// checks for empty range
		if (begin == -1 && end == -1) {
			return StatusCode.GOOD;
		}
		// nothing to do for null values
		if (value == null) {
			return StatusCode.GOOD;
		}
		System.err.println("ApplyIndexRange TODO: ");
		return StatusCode.GOOD;
	}

	/**
	 * Applies the data encoding to the value
	 * 
	 * @param dataEncoding
	 * @param value
	 * @return
	 */
	public static StatusCode applyDataEncoding(QualifiedName dataEncoding, DataValue value) {
		// check if nothing to do
		if (QualifiedName.isNull(dataEncoding) || value == null) {
			return StatusCode.GOOD;
		}
		// check for suppored encoding type
		if (dataEncoding.getNamespaceIndex() != 0) {
			return new StatusCode(StatusCodes.Bad_DataEncodingUnsupported);
		}
		boolean useXml = dataEncoding.getName().equals(QualifiedName.DEFAULT_XML_ENCODING.getName());
		if (!useXml && !dataEncoding.getName().equals(QualifiedName.DEFAULT_BINARY_ENCODING.getName())) {
			return new StatusCode(StatusCodes.Bad_DataEncodingUnsupported);
		}
		/**
		 * check for array of encodeables List<IEncodeable> encodeables = null; // check
		 * for array of extension objects List<ExtensionObject> extensions = null; //
		 * check for scalar value IEncodeable encodeable = null;
		 */
		// doConversion
		return new StatusCode(StatusCodes.Bad_TypeMismatch);
	}

	/**
	 * Returns true if the view description represents the default (null) view
	 * 
	 * @param view
	 * @return
	 */
	public static boolean isDefaultViewDescription(ViewDescription view) {
		if (view == null) {
			return true;
		}
		if (NodeId.isNull(view.getViewId()) && view.getViewVersion().intValue() == 0
				&& view.getTimestamp().equals(DateTime.MIN_VALUE)) {
			return false;
		}
		return true;
	}

	/**
	 * Parses a relative path formatted as a string
	 * 
	 * @param relativePaths
	 * @param typeTable
	 * @param namespaceUris
	 * @param namespaceTable
	 * @return
	 */
	public static RelativePath parseRelativePath(QualifiedName[] relativePaths, TypeTable typeTable,
			NamespaceTable currentTable, NamespaceTable targetTable, boolean isInverse) {
		// parse the string
		RelativePathFormatter formatter = RelativePathFormatter.parse(relativePaths, currentTable, targetTable);
		RelativePath relativePath = new RelativePath();
		RelativePathElement[] elements = new RelativePathElement[formatter.getElements().size()];
		for (int i = 0; i < formatter.getElements().size(); i++) {
			Element element = formatter.getElements().get(i);
			RelativePathElement parsedElement = new RelativePathElement();
			parsedElement.setReferenceTypeId(null);
			parsedElement.setIsInverse(isInverse);
			parsedElement.setIncludeSubtypes(element.getIncludeSubtypes());
			parsedElement.setTargetName(element.getTargetName());
			switch (element.getElementType()) {
			case ANYHIERACHICAL:
				parsedElement.setReferenceTypeId(Identifiers.HierarchicalReferences);
				break;
			case ANYCOMPONENT:
				parsedElement.setReferenceTypeId(Identifiers.Aggregates);
				break;
			case FORWARDREFERENCE:
			case INVERSEREFERENCE:
				parsedElement.setReferenceTypeId(typeTable.findReferenceType(element.getReferenceTypeName()));
				parsedElement.setIsInverse(element.getElementType() == ElementType.INVERSEREFERENCE);
				break;
			default:
				break;
			}
			if (NodeId.isNull(parsedElement.getReferenceTypeId())) {
				// relativePath.setElements(new RelativePathElement())
				return relativePath;
			}
			elements[i] = parsedElement;
		}
		relativePath.setElements(elements);
		return relativePath;
	}

	public static int peek(StringReader reader) {
		int value = -1;
		try {
			reader.mark(1);
			value = reader.read();
			reader.reset();
		} catch (IOException e) {
			Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, e.getMessage(), e);
		}
		return value;
	}

	public static String format(String text, Object... args) {
		return String.format(text, args);
	}

	public static URI parseUri(String uriString) {
		if (uriString == null || uriString.isEmpty()) {
			return null;
		}
		try {
			return new URI(uriString);
		} catch (URISyntaxException e) {
			return null;
		}
	}

	public static boolean checkDateTimeNull(DateTime time) {
		return (time == null || DateTime.MIN_VALUE.compareTo(time) == 0);
	}
}
