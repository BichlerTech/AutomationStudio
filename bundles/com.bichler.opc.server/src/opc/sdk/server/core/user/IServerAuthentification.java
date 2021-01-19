package opc.sdk.server.core.user;

import opc.sdk.core.node.Node;
import opc.sdk.core.node.user.AuthorityRule;
import opc.sdk.server.service.session.OPCServerSession;

import org.opcfoundation.ua.builtintypes.ServiceResult;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.AnonymousIdentityToken;
import org.opcfoundation.ua.core.IssuedIdentityToken;
import org.opcfoundation.ua.core.UserIdentityToken;
import org.opcfoundation.ua.core.UserNameIdentityToken;
import org.opcfoundation.ua.core.X509IdentityToken;

public interface IServerAuthentification {
	ServiceResult checkAuthorityFromNode(AuthorityRule service, OPCServerSession session, Node node);

	public StatusCode verifyAuthentification(OPCServerSession session, UserIdentityToken newIdentity)
			throws ServiceResultException;

	StatusCode verifyAnonymous(OPCServerSession session, AnonymousIdentityToken newIdentity);

	StatusCode verifyIssued(OPCServerSession session, IssuedIdentityToken newIdentity);

	StatusCode verifyUsernamePassword(OPCServerSession session, UserNameIdentityToken token)
			throws ServiceResultException;

	StatusCode verifyX509Identity(OPCServerSession session, X509IdentityToken newIdentity)
			throws ServiceResultException;
}
