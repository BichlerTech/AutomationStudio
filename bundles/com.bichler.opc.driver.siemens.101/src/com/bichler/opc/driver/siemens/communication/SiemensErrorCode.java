package com.bichler.opc.driver.siemens.communication;

public enum SiemensErrorCode {
	NO_ERROR(0x0000), NO_PERIPHERAL(0x0001), NO_DATA_AVAILABLE_200(0x0003), ADDRESSRANGE_EXCEEDS(0x0005),
	BIT_BLOCK_LENGTH(0x0006), WRITE_ITEMSIZE(0x0007), NO_DATA_AVAILABLE(0x000A), UNEXPECTED_FUNC(0x0080),
	EMPTY_RESULT_SET_ERROR(0x0081), EMPTY_RESULT_ERROR(0x0082), UNKNOWN_ERROR(0x0083), CPU_RETURNED_NO_DATA(0x0084),
	CANNOT_EVALUATE_PDU(0x0086), SHORT_PACKET(-1024), TIMEOUT(-1025), UNKNOWN_DATA_UNIT_SIZE(-129),
	FUNCTION_OCCUPIED(0x8000), WRONG_OPERATINGSTATUS(0x8001), HARDWARE_FAULT(0x8101), ACCESS_DENIED(0x8103),
	CONTEXT_UNSUPPORTED(0x8104), INVALID_ADDRESS(0x8105), DATATYPE_UNSUPPORTED(0x8106), DATATYPE_INCONSISTENT(0x8107),
	OBJECT_NOTEXISTS(0x810A), INCORRECT_PDUSIZE(0x8500), ADDRESS_INVALID(0x8702), BLOCK_NAME_ERROR(0xd201),
	SYNTAX_ERROR_PARAM(0xd202), SYNTAX_ERROR_BLOCKTYPE(0xd203), NO_LINKED_BLOCK(0xd204), OBJECT_EXISTS1(0xd205),
	OBJECT_EXISTS2(0xd206), BLOCK_EXISTS(0xd207), BLOCK_NOT_EXISTS(0xd209), NO_BLOCK_NOT_EXISTS(0xd20e),
	BLOCK_NR_TOOBIG(0xd210), UNF_BLOCK_TRANS(0xd240), PASSW_PROTECTED(0xd241);
	private final int code;

	private SiemensErrorCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}

	public static SiemensErrorCode fromValue(int code) {
		switch (code) {
		case 0x0000:
			return NO_ERROR;
		case 0x0001:
			return NO_PERIPHERAL;
		case 0x0003:
			return NO_DATA_AVAILABLE_200;
		case 0x0005:
			return ADDRESSRANGE_EXCEEDS;
		case 0x0006:
			return BIT_BLOCK_LENGTH;
		case 0x0007:
			return WRITE_ITEMSIZE;
		case 0x000A:
			return NO_DATA_AVAILABLE;
		case 0x0080:
			return UNEXPECTED_FUNC;
		case 0x0081:
			return EMPTY_RESULT_SET_ERROR;
		case 0x0082:
			return EMPTY_RESULT_ERROR;
		case 0x0083:
			return UNKNOWN_ERROR;
		case 0x0084:
			return CPU_RETURNED_NO_DATA;
		case 0x0086:
			return CANNOT_EVALUATE_PDU;
		case -1024:
			return SHORT_PACKET;
		case -1025:
			return TIMEOUT;
		case -129:
			return UNKNOWN_DATA_UNIT_SIZE;
		case 0x8000:
			return FUNCTION_OCCUPIED;
		case 0x8001:
			return WRONG_OPERATINGSTATUS;
		case 0x8101:
			return HARDWARE_FAULT;
		case 0x8103:
			return ACCESS_DENIED;
		case 0x8104:
			return CONTEXT_UNSUPPORTED;
		case 0x8105:
			return INVALID_ADDRESS;
		case 0x8106:
			return DATATYPE_UNSUPPORTED;
		case 0x8107:
			return DATATYPE_INCONSISTENT;
		case 0x810A:
			return OBJECT_NOTEXISTS;
		case 0x8500:
			return INCORRECT_PDUSIZE;
		case 0x8702:
			return ADDRESS_INVALID;
		case 0xd201:
			return BLOCK_NAME_ERROR;
		case 0xd202:
			return SYNTAX_ERROR_PARAM;
		case 0xd203:
			return SYNTAX_ERROR_BLOCKTYPE;
		case 0xd204:
			return NO_LINKED_BLOCK;
		case 0xd205:
			return OBJECT_EXISTS1;
		case 0xd206:
			return OBJECT_EXISTS2;
		case 0xd207:
			return BLOCK_EXISTS;
		case 0xd209:
			return BLOCK_NOT_EXISTS;
		case 0xd20e:
			return NO_BLOCK_NOT_EXISTS;
		case 0xd210:
			return BLOCK_NR_TOOBIG;
		case 0xd240:
			return UNF_BLOCK_TRANS;
		case 0xd241:
			return PASSW_PROTECTED;
		}
		return NO_ERROR;
	}

	public static String strerror(SiemensErrorCode code) {
		switch (code) {
		case NO_ERROR:
			return "OK, no error!";
		case NO_PERIPHERAL:
			return "There is no peripheral at given address!";
		case NO_DATA_AVAILABLE_200:
			return "A piece of data is not available(200 CPU family)!";
		case ADDRESSRANGE_EXCEEDS:
			return "The desired address is beyond limit for this CPU!";
		case BIT_BLOCK_LENGTH:
			return "The CPU does not support reading a bit block of length != 1!";
		case WRITE_ITEMSIZE:
			return "Write data size does not fit item size!";
		case NO_DATA_AVAILABLE:
			return "No data available on address available!";
		case CPU_RETURNED_NO_DATA:
			return "The PLC returned a packet with no result data!";
		case UNKNOWN_ERROR:
			return "The PLC returned an unknown error code!";
		case EMPTY_RESULT_ERROR:
			return "The returned result contains no data!";
		case EMPTY_RESULT_SET_ERROR:
			return "Message contains an undefined result set!";
		case CANNOT_EVALUATE_PDU:
			return "Cannot evaluate the received PDU!";
		case UNEXPECTED_FUNC:
			return "Unexpected function code in response!";
		case SHORT_PACKET:
			return "Short packet from PLC!";
		case UNKNOWN_DATA_UNIT_SIZE:
			return "PLC responds with an unknown data type!";
		case TIMEOUT:
			return "Timeout when waiting for PLC response!";
		case FUNCTION_OCCUPIED:
			return "Function already occupied.";
		case WRONG_OPERATINGSTATUS:
			return "Not allowed in current operating status.";
		case HARDWARE_FAULT:
			return "Hardware fault occured.";
		case ACCESS_DENIED:
			return "Object access is not allowed.";
		case CONTEXT_UNSUPPORTED:
			return "context is not supported.";
		case INVALID_ADDRESS:
			return "Invalid address.";
		case DATATYPE_UNSUPPORTED:
			return "Data type not supported.";
		case DATATYPE_INCONSISTENT:
			return "Data type not consistent.";
		case OBJECT_NOTEXISTS:
			return "Object does not exist.";
		case INCORRECT_PDUSIZE:
			return "Incorrect PDU size.";
		case ADDRESS_INVALID:
			return "Address invalid.";
		case BLOCK_NAME_ERROR:
			return "Block name syntax error.";
		case SYNTAX_ERROR_PARAM:
			return "Syntax error function parameter.";
		case SYNTAX_ERROR_BLOCKTYPE:
			return "Syntax error block type.";
		case NO_LINKED_BLOCK:
			return "No linked block in storage medium.";
		case OBJECT_EXISTS1:
			return "Object already exists.";
		case OBJECT_EXISTS2:
			return "Object already exists.";
		case BLOCK_EXISTS:
			return "Block exists in EPROM.";
		case BLOCK_NOT_EXISTS:
			return "Block does not exist.";
		case NO_BLOCK_NOT_EXISTS:
			return "No block does not exist.";
		case BLOCK_NR_TOOBIG:
			return "Block number too big.";
		case UNF_BLOCK_TRANS:
			return "Unfinished block transfer in progress?";
		case PASSW_PROTECTED:
			return "Protected by password.";
		default:
			return "No message defined for code: " + code.getCode() + "!";
		}
	}
}
