package opc.sdk.server.service.filter;

import java.util.List;

import opc.sdk.core.types.TypeTable;

import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.core.EventFilter;

/**
 * 
 * @author Thomas Z&ouml;chbauer
 *
 */
public class FilterContext {
	private NamespaceTable namespaceUris = null;
	private TypeTable typeTable = null;
	// private OperationContext context = null;
	private EventFilter eventFilter = null;
	private List<String> preferredLocales = null;

	/**
	 * Initializes the context.
	 * 
	 * @param filter
	 * 
	 * @param namespaceUris
	 * @param typeTree
	 * @param context
	 */
	public FilterContext(EventFilter filter, NamespaceTable namespaceUris, TypeTable typeTree) {
		if (namespaceUris == null) {
			throw new IllegalArgumentException("NamespaceUris");
		}
		if (typeTree == null) {
			throw new IllegalArgumentException("TypeTree");
		}
		this.eventFilter = filter;
		this.namespaceUris = namespaceUris;
		this.typeTable = typeTree;
		// this.context = context;
	}

	public FilterContext(NamespaceTable namespaceUris, TypeTable typeTree, List<String> preferedLocales) {
		if (namespaceUris == null) {
		}
		if (typeTree == null) {
		}
		this.namespaceUris = namespaceUris;
		this.typeTable = typeTree;
		// this.context = null;
		this.setPreferredLocales(preferedLocales);
	}

	// public OperationContext getContext() {
	// return context;
	// }
	public EventFilter getEventFilter() {
		return eventFilter;
	}

	public NamespaceTable getNamespaceUris() {
		return namespaceUris;
	}

	public TypeTable getTypeTable() {
		return typeTable;
	}

	// public void setContext(OperationContext mContext) {
	// context = mContext;
	// }
	public void setEventFilter(EventFilter mEventFilter) {
		eventFilter = mEventFilter;
	}

	public void setNamespaceUris(NamespaceTable mNamespaceUris) {
		namespaceUris = mNamespaceUris;
	}

	public void setTypeTable(TypeTable mTypeTable) {
		typeTable = mTypeTable;
	}

	public List<String> getPreferredLocales() {
		return preferredLocales;
	}

	public void setPreferredLocales(List<String> preferredLocales) {
		this.preferredLocales = preferredLocales;
	}
}
