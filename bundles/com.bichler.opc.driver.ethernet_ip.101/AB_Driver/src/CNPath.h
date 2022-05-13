/*
 * CNSymbolPath.h
 *
 *  Created on: 18.03.2022
 *      Author: hbich
 */

#ifndef CNPATH_H_
#define CNPATH_H_

#include <stdbool.h>

typedef struct
{

} CNPath;

typedef struct
{
  char *symbol;
} CNSymbolPath;

typedef struct
{
  int class_code;
  char *class_name;
  int instance;
  int attr;
} CNClassPath;

int
getRequestSizeCNSymbolPath (CNSymbolPath *symPath);

void
encodeCNSymbolPath1 (CNSymbolPath *symPath, char *buffer);

char*
encodeCNSymbolPath2 (CNSymbolPath *symPath, char *buffer);

bool
needPad (char *item);

char
getPathLength (CNClassPath *classPath);

int
getRequestSizeCNClassPath (CNClassPath *classPath);

void
encodeCNClassPath (CNClassPath *classPath, char *buffer, int offset);

#endif /* CNPATH_H_ */
