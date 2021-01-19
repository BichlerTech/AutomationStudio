package opc.client.application.runtime.model;

import org.jdom.Element;

/**
 * Listener for a model node
 * 
 * @author Thomas Z&ouml;chbauer
 * @since 23.05.2012, HB-Softsolution e.U.
 * 
 */
public interface IModelNode {
	void load(Element element);
}
