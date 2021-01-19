package opc.sdk.core.node.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public enum AuthorityRule {
	Read(1), Write(2), Call(4), Events(8), Datachange(16), AddNode(32), DeleteNode(64), AddReference(128),
	DeleteReference(256), Browse(512), RegisterNode(1024), HistoryRead(2048), HistoryUpdate(4096),
	TranslateBrowsePath(8192);

	public static final Set<AuthorityRule> ALL = EnumSet.allOf(AuthorityRule.class);

	AuthorityRule(int level) {
		this.level = level;
	}

	public int getLevel() {
		return this.level;
	}

	private int level = -1;

	public static Set<AuthorityRule> getSet(int mask) {
		List<AuthorityRule> res = new ArrayList<>();
		for (AuthorityRule l : AuthorityRule.values()) {
			if ((mask & l.level) == l.level) {
				res.add(l);
			}
		}
		if (res.isEmpty()) {
			return EnumSet.noneOf(AuthorityRule.class);
		}
		return EnumSet.copyOf(res);
	}

	public static int getMask(AuthorityRule... list) {
		int result = 0;
		for (AuthorityRule l : list) {
			result |= l.level;
		}
		return result;
	}

	public static int getMask(Collection<AuthorityRule> list) {
		int result = 0;
		for (AuthorityRule l : list) {
			result |= l.level;
		}
		return result;
	}
}
