package opc.sdk.core.node.user;

public class DBRole {
	private int id = 0;
	private String rolename = "";
	private boolean modified = true;
	private boolean deleted = false;
	private String description = "";

	public DBRole(String rolename) {
		this.rolename = rolename;
	}

	public DBRole(int id, String rolename) {
		this(rolename);
		this.id = id;
		this.modified = false;
	}

	public int getId() {
		return this.id;
	}

	public String getRolename() {
		return this.rolename;
	}

	public boolean isDeleted() {
		return this.deleted;
	}

	public boolean isModified() {
		return this.modified;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setRolename(String name) {
		this.rolename = name;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public void setModified(boolean modified) {
		this.modified = modified;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
