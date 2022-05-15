/*
 * CIPDatat.h
 *
 *  Created on: 18.03.2022
 *      Author: hbich
 */

#ifndef CIPDATA_H_
#define CIPDATA_H_

typedef enum
{
  UNKNOWN = 0x0000,
  BOOL = 0x00C1,
  SINT = 0x00C2,
  INT = 0x00C3,
  DINT = 0x00C4,
  LINT = 0x00C5,
  USINT = 0x00C6,
  UINT = 0x00C7,
  UDINT = 0x00C8,
  ULINT = 0x00C9,
  REAL = 0x00CA,
  BITS = 0x00D3,
  LREAL = 0x00CB,
  STRUCT = 0x02A0,
  STRUCT_STRING = 0x0FCE
} CIPDataEnum;

struct CIPData
{
  CIPDataEnum type;
};


#endif /* CIPDATA_H_ */
