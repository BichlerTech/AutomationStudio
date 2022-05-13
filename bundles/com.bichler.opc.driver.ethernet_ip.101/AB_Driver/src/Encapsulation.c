/*
 * Encapsulation.c
 *
 *  Created on: 18.03.2022
 *      Author: hbich
 */

#include "Encapsulation.h"

int
getRequestSizeProtocol (Encapsulation *encap)
{
  return 1; //ENCAPSULATION_HEADER_SIZE
//  +getRequestSizeProtocol ();
}

void
initEncapsulation (Encapsulation *encap, ComEthernetIPCommands command,
		   int session, Protocol *body)
{

}

int
getRequestSizeEncapsulation (Encapsulation *encap)
{
  return ENCAPSULATION_HEADER_SIZE
  +getRequestSizeProtocol (encap);
}
