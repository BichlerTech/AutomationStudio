/*
 * Encapsulation.h
 *
 *  Created on: 18.03.2022
 *      Author: hbich
 */

#ifndef ENCAPSULATION_H_
#define ENCAPSULATION_H_

#include "ComEthernetIPCommands.h"

#define ENCAPSULATION_HEADER_SIZE 24;

typedef struct Protocol_ Protocol;
typedef struct Encapsulation_ Encapsulation;

struct Protocol_
{

};

int
getRequestSizeProtocol (Encapsulation *encap);

struct Encapsulation
{
  ComEthernetIPCommands command;
  int session;
  Protocol body;
  char *context; // = new byte[] { 'F', 'u', 'n', 's', 't', 'u', 'f', 'f' };

};

void
initEncapsulation (Encapsulation *encap, ComEthernetIPCommands command,
		   int session, Protocol *body);

int
getRequestSizeEncapsulation (Encapsulation *encap);

#endif /* ENCAPSULATION_H_ */
