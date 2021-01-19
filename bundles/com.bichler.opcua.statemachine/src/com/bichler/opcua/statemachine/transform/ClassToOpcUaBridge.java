package com.bichler.opcua.statemachine.transform;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PackageableElement;
import org.opcfoundation.ua.builtintypes.NodeId;

import com.bichler.opcua.statemachine.addressspace.importer.StatemachineNodesetImporter;
import com.bichler.opcua.statemachine.exception.StatemachineException;
import com.bichler.opcua.statemachine.transform.model.ClassTemplate;
import com.bichler.opcua.statemachine.transform.model.ClassTemplateFactory;

import opc.sdk.core.node.Node;

public class ClassToOpcUaBridge {

	public ClassToOpcUaBridge() {

	}

	public Map<String, ClassTemplate[]> transform(Model model, StatemachineNodesetImporter importer, List<Node> createdNodes) throws StatemachineException {
		Map<String, List<Classifier>> classModel = new LinkedHashMap<>();

		for (PackageableElement element : model.getPackagedElements()) {
			if (!(element instanceof Package)) {
				continue;
			}

			String namespaceURI = ((Package) element).getURI();
			if (namespaceURI == null) {
				throw new StatemachineException(
						"UML package <" + element.getName() + "> does not contain a required URI");
			}
			if (namespaceURI.isEmpty()) {
				throw new StatemachineException(
						"UML package <" + element.getName() + "> does not contain a required URI");
			}

			List<Classifier> classes = classModel.get(namespaceURI);
			if (classes == null) {
				classes = new ArrayList<>();
				classModel.put(namespaceURI, classes);
			}

			for (PackageableElement modelElement : ((Package) element).getPackagedElements()) {
				if (modelElement instanceof Class) {
					classes.add((Class) modelElement);
				} else if (modelElement instanceof DataType) {
					classes.add((DataType) modelElement);
				}
			}
		}

		Map<String, ClassTemplate[]> templatesToGenerate = new LinkedHashMap<>();
		for (Entry<String, List<Classifier>> modelToGenerate : classModel.entrySet()) {
			String namespaceURI = modelToGenerate.getKey();
			// set up statemachine templates
			List<ClassTemplate> templates = new ArrayList<>();
			for (Classifier element : modelToGenerate.getValue()) {
				NodeId referencedId = NodeId.NULL;
				for (Node node : createdNodes) {
					String name = (element.getName() != null) ? element.getName() : "";
					String nodeName = (node.getBrowseName().getName() != null) ? node.getBrowseName().getName() : "";

					if (!name.isEmpty() && name.equals(nodeName)) {
						referencedId = node.getNodeId();
						break;
					}
				}
				ClassTemplate smt = ClassTemplateFactory.createTemplate(importer, element, referencedId);
				templates.add(smt);
			}

			templatesToGenerate.put(namespaceURI, templates.toArray(new ClassTemplate[0]));
		}
		return templatesToGenerate;
	}
}
