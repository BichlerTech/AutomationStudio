package com.bichler.astudio.opcua.nosql.userauthority.wizard.provider;

import java.util.EnumSet;
import java.util.Set;

import opc.sdk.core.node.user.AuthorityRule;
import opc.sdk.core.node.user.DBAuthority;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.opcfoundation.ua.core.NodeClass;

import com.bichler.astudio.images.opcua.OPCImagesActivator;
import com.bichler.astudio.opcua.nosql.userauthority.wizard.model.AbstractOPCUserModel;

public abstract class UserRoleLabelProvider implements ITableLabelProvider {

	public UserRoleLabelProvider() {
	}

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		switch (columnIndex) {
		// image
		case 0:
			if (element instanceof AbstractOPCUserModel) {
				NodeClass img = ((AbstractOPCUserModel) element).getNodeClass();
				switch (img) {
				case DataType:
					return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
							OPCImagesActivator.DATATYPE);
				case Method:
					return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
							OPCImagesActivator.METHOD);
				case Object:
					return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
							OPCImagesActivator.OBJECT);
				case ObjectType:
					return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
							OPCImagesActivator.OBJECTTYPE);
				case ReferenceType:
					return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
							OPCImagesActivator.REFERENCETYPE);
				case Variable:
					return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
							OPCImagesActivator.VARIABLE);
				case VariableType:
					return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
							OPCImagesActivator.VARIABLETYPE);
				case View:
					return OPCImagesActivator.getDefault().getRegisteredImage(OPCImagesActivator.FOLDER_16,
							OPCImagesActivator.VIEW);
				}
			}
			// no image
		default:
			break;

		}
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		switch (columnIndex) {
		case 0:
			if (element instanceof AbstractOPCUserModel) {
				return ((AbstractOPCUserModel) element).getDisplayname();
			}
			break;
		case 1:
			if (element instanceof AbstractOPCUserModel) {
				DBAuthority authority = ((AbstractOPCUserModel) element).getAuthority();

				if (authority == null) {
					return "[null]";
				}

				int attributes = authority.getAuthorityRole();
				Set<AuthorityRule> role = AuthorityRule.getSet(attributes);

				String accesslevel = toStringAccessLevel(role);
				return accesslevel;
			}
			break;
		default:
			break;
		}
		return null;
	}

	protected abstract String toStringAccessLevel(Set<AuthorityRule> roles);

	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
	}

}
