package opc.sdk.core.node.user;

import java.util.Set;

public class DBAuthority {
	private int authorityRole = 0;

	public DBAuthority() {
		this.authorityRole = AuthorityRule.getMask(AuthorityRule.ALL);
	}

	public int getAuthorityRole() {
		return authorityRole;
	}

	/**
	 * Adds a user authority role
	 * 
	 * @param role
	 * @param isSet TRUE set the attribute, FALSE remove
	 */
	public void setAuthorityRole(AuthorityRule role, boolean isSet) {
		Set<AuthorityRule> current = AuthorityRule.getSet(this.authorityRole);
		if (isSet) {
			current.add(role);
		} else {
			current.remove(role);
		}
		this.authorityRole = AuthorityRule.getMask(current);
	}

	public void setAuthorityRole(int role) {
		this.authorityRole = role;
	}
}
