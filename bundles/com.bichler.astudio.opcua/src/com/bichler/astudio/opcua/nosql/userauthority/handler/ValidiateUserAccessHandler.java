package com.bichler.astudio.opcua.nosql.userauthority.handler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.NamespaceTable;

import com.bichler.astudio.opcua.addressspace.model.nosql.userauthority.NoSqlUtil;
import com.bichler.astudio.opcua.navigation.views.OPCNavigationView;
import com.bichler.astudio.log.ASLogActivator;
import com.bichler.astudio.opcua.handlers.events.OPCUAValidationUserAccessParameter;
import com.bichler.astudio.opcua.handlers.opcua.resource.OPCUAValidationHandler;
import com.bichler.astudio.opcua.opcmodeler.singletons.ServerInstance;
import com.bichler.astudio.opcua.nodes.OPCUAServerModelNode;

import opc.sdk.core.node.user.DBAuthority;
import opc.sdk.core.node.user.DBRole;

public class ValidiateUserAccessHandler extends AbstractHandler {
	public static final String ID = "com.bichler.astudio.opcua.validate.useraccess";

	// private static Logger logger = Logger
	// .getLogger(ValidiateUserAccessHandler.class);
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
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
		IEvaluationContext evalCxt = (IEvaluationContext) event.getApplicationContext();
		OPCUAValidationUserAccessParameter trigger = (OPCUAValidationUserAccessParameter) evalCxt
				.getVariable(OPCUAValidationHandler.PARAMETER_ID);
		// OPCUAValidationUserAccessParameter trigger =
		// (OPCUAValidationUserAccessParameter) event
		// .getTrigger();
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
			NamespaceTable nsTable = ServerInstance.getInstance().getServerInstance().getNamespaceUris();
			// Map<String, String> mapping = trigger.getMapping();
			for (DBRole role : roles) {
				Map<NodeId, DBAuthority> dbAuthorities = NoSqlUtil.readOPCNodesFromRoles(dbConnection, nsTable, role);
				for (Entry<NodeId, DBAuthority> entry : dbAuthorities.entrySet()) {
					// skip node to add
					if (NodeId.isNull(entry.getKey())) {
						continue;
					}
					// set changes to
					changes.put(entry.getKey(), entry.getKey());
				}
			}
		} catch (ClassNotFoundException e) {
			ASLogActivator.getDefault().getLogger().log(Level.SEVERE, e.getMessage());
		} catch (SQLException e) {
			ASLogActivator.getDefault().getLogger().log(Level.SEVERE, e.getMessage());
			// e.printStackTrace();
		} finally {
			if (dbConnection != null) {
				// disconnect from database connection
				NoSqlUtil.disconnect(dbConnection);
			}
		}
		// CSLogActivator.getDefault().getLogger()
		// .info(CustomString.getString(OPCUAActivator.getDefault().RESOURCE_BUNDLE,
		// "handler.message.validate")
		// + " " + trigger.getFilesystem().getRootPath());
		// LogActivator.getDefault().getLogManager().logInfo(
		// "Validate " + trigger.getFilesystem().getRootPath());
		return null;
	}
}
