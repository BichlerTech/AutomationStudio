package com.bichler.astudio.opcua.nosql.userauthority.handler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import opc.sdk.core.node.mapper.NodeIdMapper;
import opc.sdk.core.node.user.DBAuthority;
import opc.sdk.core.node.user.DBRole;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.astudio.opcua.opcmodeler.commands.NamespaceTableChangeParameter;
import com.bichler.astudio.opcua.addressspace.model.nosql.userauthority.NoSqlUtil;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
import com.bichler.astudio.opcua.nodes.OPCUAServerModelNode;

public class UpdateNamespaceTableUserAccessHandler extends AbstractHandler {

	public static final String ID = "command.update.opcua.namespacetable.driver.user";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		NamespaceTableChangeParameter trigger = getCommandParameter(event);
		// (NamespaceTableChangeEvent) event
		// .getTrigger();

		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

		OPCNavigationView navigator = (OPCNavigationView) page.findView(OPCNavigationView.ID);

		if (navigator == null) {
			return null;
		}
		// server node
		Object input = navigator.getViewer().getInput();
		if (input == null) {
			return null;
		}
		// db user path
		String dbUserPath = new Path(((OPCUAServerModelNode) input).getFilesystem().getRootPath()).append("users")
				.append(NoSqlUtil.DB_USER).toOSString();
		// result
		Map<NodeId, NodeId> changes = new LinkedHashMap<>();
		// db connection
		Connection dbConnection = null;
		try {
			dbConnection = NoSqlUtil.createConnection(dbUserPath);
			List<DBRole> roles = NoSqlUtil.readRoleTable(dbConnection);
			Map<NodeId, DBAuthority> newAuthorities = new HashMap<NodeId, DBAuthority>();

			// Map<String, String> mapping = trigger.getMapping();
			NamespaceTable origin = trigger.getOriginNamespaceTable();
			for (DBRole role : roles) {
				Map<NodeId, DBAuthority> dbAuthorities = NoSqlUtil.readOPCNodesFromRoles(dbConnection, origin, role);

				for (Entry<NodeId, DBAuthority> entry : dbAuthorities.entrySet()) {
					NodeId nodeId = NodeIdMapper.mapNamespaceIndex(entry.getKey(), trigger.getIndexMapping());
					// skip node to add
					if (NodeId.isNull(nodeId)) {
						continue;
					}
					// set new authorities
					newAuthorities.put(nodeId, entry.getValue());
					// set changes to
					changes.put(entry.getKey(), nodeId);
				}
				// remove nodeids from role
				NoSqlUtil.removeNodesFromDatabase(dbConnection, role.getId());
				// add new nodeids to role
				NoSqlUtil.writeNodeWithAuthorityToDatabase(dbConnection,
						ServerInstance.getInstance().getServerInstance().getNamespaceUris(), role.getId(),
						newAuthorities);
			}
		} catch (ClassNotFoundException | SQLException e) {
			Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage());
		} finally {
			if (dbConnection != null) {
				// disconnect from database connection
				NoSqlUtil.disconnect(dbConnection);
			}
		}

		return changes;
	}

	protected NamespaceTableChangeParameter getCommandParameter(ExecutionEvent event) {
		IEvaluationContext evalCxt = (IEvaluationContext) event.getApplicationContext();
		NamespaceTableChangeParameter trigger = (NamespaceTableChangeParameter) evalCxt
				.getVariable(NamespaceTableChangeParameter.PARAMETER_ID);
		return trigger;
	}
}
