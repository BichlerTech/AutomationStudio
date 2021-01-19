package com.bichler.astudio.editor.aggregated.clientbrowser.util;


public class ClientBrowserUtils {

	// public static void openClientConnectionView(UAServerModelNode server) {
	// openClientConnectionView(server,
	// new DefaultOPCConnectionAdapter(server));
	// }
//	@Deprecated
//	public static void openClientConnectionView(ClientConnectionView view,
//			UAServerModelNode server, RootCCModel adapter) {
//		IWorkbenchPage page = PlatformUI.getWorkbench()
//				.getActiveWorkbenchWindow().getActivePage();
//
//		if (!adapter.isServerActive()) {
//			MessageDialog
//					.openError(page.getActivePart().getSite().getShell(),
//							"Verbindungsproblem",
//							"Kein Verbindungsaufbau zu Remote-Server "
//									+ server.getDevice().getUaServerUri()
//									+ " möglich!");
//			return;
//		}
//
//		view.setRemoteServer(server, adapter);
//	}
//	@Deprecated
//	public static void openClientConnectionView(UAServerModelNode server,
//			RootCCModel adapter) {
//		IWorkbenchPage page = PlatformUI.getWorkbench()
//				.getActiveWorkbenchWindow().getActivePage();
//
//		if (!adapter.isServerActive()) {
//			MessageDialog
//					.openError(page.getActivePart().getSite().getShell(),
//							"Verbindungsproblem",
//							"Kein Verbindungsaufbau zu Remote-Server "
//									+ server.getDevice().getUaServerUri()
//									+ " möglich!");
//			return;
//		}
//
//		try {
//			ClientConnectionView view = (ClientConnectionView) page
//					.showView(ClientConnectionView.ID);
//			view.setRemoteServer(server, adapter);
//			// view.setFocus();
//		} catch (PartInitException e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Deprecated
//	public static void closeClientConnectionView() {
//		IWorkbenchPage page = PlatformUI.getWorkbench()
//				.getActiveWorkbenchWindow().getActivePage();
//
//		IViewPart viewRef = page.findView(ClientConnectionView.ID);
//		if (viewRef != null) {
//			page.hideView(viewRef);
//		}
//	}

//	public static ClientConnectionView findClientConnectionView() {
//		IWorkbenchPage page = PlatformUI.getWorkbench()
//				.getActiveWorkbenchWindow().getActivePage();
//
//		return (ClientConnectionView) page.findView(ClientConnectionView.ID);
//	}

	/**
	 * Checks if the connection to a remote server is available or not!
	 * 
	 * @param server
	 * @return
	 */
	// private static boolean isServerActive(UAServerModelNode server) {
	// if (server != null && server.getDevice() != null
	// && server.getDevice().getUaclient() != null) {
	// return true;
	// }
	//
	// return false;
	// }

	// static class DefaultOPCConnectionAdapter implements OPCConnectionAdapter
	// {
	//
	// private UAServerModelNode server;
	//
	// public DefaultOPCConnectionAdapter(UAServerModelNode server) {
	// this.server = server;
	// }
	//
	// /**
	// * Checks if the connection to a remote server is available or not!
	// *
	// * @param server
	// * @return
	// */
	// @Override
	// public boolean isServerActive() {
	// if (server != null && server.getDevice() != null
	// && server.getDevice().getUaclient() != null) {
	// return true;
	// }
	//
	// return false;
	// }
	//
	// @Override
	// public BrowseResult browse(NodeId parentId, BrowseDirection forward,
	// boolean includeSubtyes, UnsignedInteger nodeClass,
	// NodeId referenceId, UnsignedInteger resultMask) {
	//
	// if (server != null && server.getDevice() != null
	// && server.getDevice().getUaclient() != null) {
	// try {
	// return server
	// .getDevice()
	// .getUaclient()
	// .browse(null,
	// parentId,
	// BrowseDirection.Forward,
	// true,
	// NodeClass.getMask(NodeClass.ALL),
	// Identifiers.HierarchicalReferences,
	// BrowseResultMask
	// .getMask(BrowseResultMask.ALL),
	// UnsignedInteger.ZERO, null);
	// } catch (ServiceResultException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// return null;
	// }
	//
	// }
}
