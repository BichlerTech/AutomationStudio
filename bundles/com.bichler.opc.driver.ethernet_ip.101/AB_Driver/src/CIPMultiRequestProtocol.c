/*
 * CIPMultiRequestProtocol.c
 *
 *  Created on: 19.03.2022
 *      Author: hbich
 */

#include "MessageRouterProtocol.h"

typedef struct
{
  MessageRouterProtocol *services;
  int serviceCount;
} CIPMultiRequestProtocol;
