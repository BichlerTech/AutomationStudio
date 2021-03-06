package opc.sdk.core.node.user;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.core.StatusCodes;

public class DBUser {
	private int id = 0;
	private int roleMask = 0;
	private String modifiedUserName = "";
	private String username = "";
	private String password = "";
	// private String rolename = ""
	private boolean isModified = true;
	private boolean isDeleted = false;
	// MULTI ROLES
	private DBRole[] userroles = new DBRole[0];
	private String description = "";
	private Logger logger = Logger.getLogger(getClass().getName());

	DBUser(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public DBUser(int id, int rolemask, String username, String password) {
		this(username, password);
		this.id = id;
		this.roleMask = rolemask;
	}

	public DBUser(String username, String password, DBRole role) {
		this(username, password);
		addRole(role);
	}

	public DBUser(String username, String password, DBRole[] roles) {
		this(username, password);
		for (DBRole role : roles) {
			addRole(role);
		}
	}

	public void addRole(DBRole role2add) {
		List<DBRole> newRoles = new ArrayList<>();
		for (DBRole role : this.userroles) {
			// do not add existing
			if (role.equals(role2add)) {
				// exit
				return;
			}
			// add new
			newRoles.add(role);
		}
		newRoles.add(role2add);
		this.userroles = newRoles.toArray(new DBRole[0]);
	}

	public void setRoles(DBRole[] roles) {
		this.userroles = new DBRole[0];
		for (DBRole role : roles) {
			addRole(role);
		}
	}

	public void removeRole(DBRole role2remove) {
		// do not add existing
		// add new
		List<DBRole> newRoles = new ArrayList<>();
		for (DBRole role : this.userroles) {
			// skip role to remove
			if (role == role2remove) {
				continue;
			}
			newRoles.add(role);
		}
		this.userroles = newRoles.toArray(new DBRole[0]);
	}

	/**
	 * Matches a user with its login name and password.
	 * 
	 * @param username
	 * @param password
	 * @return
	 */
	public StatusCode match(String username, String password) {
		// username do not match
		if (!this.username.equals(username)) {
			return new StatusCode(StatusCodes.Bad_IdentityTokenRejected);
		}
		// check empty passwords
		boolean nullPw1 = false;
		boolean nullPw2 = false;
		if (this.password == null || this.password.isEmpty()) {
			nullPw1 = true;
		}
		if (password == null || password.isEmpty()) {
			nullPw2 = true;
		}
		// null password
		if (nullPw1 && nullPw2) {
			return StatusCode.GOOD;
		}
		AuthorityUser matcher = new AuthorityUser();
		boolean isEqual = false;
		try {
			isEqual = matcher.validatePassword(password, this.password);
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		if (isEqual) {
			return StatusCode.GOOD;
		}
		return new StatusCode(StatusCodes.Bad_IdentityTokenRejected);
	}

	public int getId() {
		return id;
	}

	public DBRole[] getRoles() {
		return this.userroles;
	}

	public int getRoleMask() {
		return this.roleMask;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public boolean isDeleted() {
		return this.isDeleted;
	}

	public boolean isModified() {
		return isModified;
	}

	public void setRoleMask(int mask) {
		this.roleMask = mask;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setModified(boolean modified) {
		this.isModified = modified;
	}

	public void setId(int idUser) {
		this.id = idUser;
	}

	public void setRemoved(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getModifiedUserName() {
		return this.modifiedUserName;
	}

	public void setModifiedUserName(String name) {
		this.modifiedUserName = name;
	}
}
