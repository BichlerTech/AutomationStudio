package opc.sdk.ua;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public enum AttributesToSave {
	None(0x00000000), AccessLevel(0x00000001), ArrayDimensions(0x0000002), BrowseName(0x00000004),
	ContainsNoLoops(0x00000008), DataType(0x00000010), Description(0x00000020), DisplayName(0x00000040),
	EventNotifier(0x00000080), Executable(0x00000100), Historizing(0x00000200), InverseName(0x00000400),
	IsAbstract(0x00000800), MinimumSamplingInterval(0x00001000), NodeClass(0x00002000), NodeId(0x00004000),
	Symmetric(0x00008000), UserAccessLevel(0x00010000), UserExecutable(0x00020000), UserWriteMask(0x00040000),
	ValueRank(0x00080000), WriteMask(0x00100000), Value(0x00200000), SymbolicName(0x00400000),
	TypeDefinitionId(0x00800000), ModellingRuleId(0x01000000), NumericId(0x02000000), ReferenceTypeId(0x08000000),
	SuperTypeId(0x10000000), StatusCode(0x20000000);

	private int value;

	AttributesToSave(int value) {
		this.value = value;
	}

	int getValue() {
		return this.value;
	}

	public static EnumSet<AttributesToSave> getSet(int value) {
		List<AttributesToSave> attributes = new ArrayList<AttributesToSave>();
		for (AttributesToSave attribute : AttributesToSave.values()) {
			if ((attribute.getValue() & value) != 0) {
				attributes.add(attribute);
			}
		}
		return EnumSet.copyOf(attributes);
	}
}
