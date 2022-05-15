/*
 * ComEthernetIPCommands.h
 *
 *  Created on: 18.03.2022
 *      Author: hbich
 */

#ifndef COMETHERNETIPCOMMANDS_H_
#define COMETHERNETIPCOMMANDS_H_

typedef enum
{
  NOP = 0x0000,
  LISTSERVICES = 0x0004,
  LISTIDENTITY = 0x0063,
  LISTINTERFACES = 0x0064,
  REGISTERSESSION = 0x0065,
  UNREGISTERSESSION = 0x0066,
  SENDRRDATA = 0x006F,
  SENDUNITDATA = 0x0070,
  INDICATESTATUS = 0x0072
} ComEthernetIPCommands;

//ComEthernetIPCommands forCode(int code) {
//		for (ComEthernetIPCommands command : values())
//			if (command.getValue() == code)
//				return command;
//		return null;
//	}
#endif /* COMETHERNETIPCOMMANDS_H_ */
