/*
 * CNPath.c
 *
 *  Created on: 18.03.2022
 *      Author: hbich
 */
#include "CNPath.h"
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

int
getRequestSizeCNSymboPath (CNSymbolPath *symPath)
{
  char *tmpSymbol = symPath->symbol;
  int size = 0;
  char *items = strtok (tmpSymbol, "\\.");

  while (items != NULL)
    {
      size += strlen (encodeCNSymbolPath2 (symPath, items));
    }
  return size;
}

void
encodeCNSymbolPath1 (CNSymbolPath *symPath, char *buffer)
{
  char *tmpSymbol = symPath->symbol;
  char insert[] =
    { (char) (getRequestSizeCNSymbolPath (symPath) / 2), 0x0 };
  strcat (buffer, insert);
  // spec 4 p.21: "ANSI extended symbol segment"
  char *items = strtok (tmpSymbol, "\\.");
  while (items != NULL)
    {
      strcat (buffer, encodeCNSymbolPath2 (symPath, items));
      items = strtok (tmpSymbol, "\\.");
    }
}

char*
encodeCNSymbolPath2 (CNSymbolPath *symPath, char *item)
{
  char *buffer = NULL;
  char *tmpToken = item;
  int index = -1;
  if (strstr (item, "["))
    {
      if (!strstr (item, "]"))
	{
	  //Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Wrong array definition in path segment: {0}",
	  //		item);
	}
      else
	{
	  char *items = strtok (tmpToken, "[");
	  items = strtok (tmpToken, "]");

	  if (items != NULL)
	    {
	      index = atoi (items);
	    }
	}
    }
  buffer = malloc (
      2 + strlen (item) + (needPad (item) ? 1 : 0) + (index > -1 ? 2 : 0));
  buffer[0] = (char) 0x91;
  buffer[1] = (char) strlen (item);
  strncpy (&(buffer[2]), item, strlen (item));
  if (needPad (item))
    buffer[2 + strlen (item)] = ((char) 0);

  /**
   * we have an array definition
   */
  if (index > -1)
    {
      buffer[2 + strlen (item) + 1] = (char) 0x28;
      buffer[2 + strlen (item) + 2] = index;
    }

  return buffer;
}

bool
needPad (char *item)
{
  return (strlen (item) % 2) != 0;
}

char
getPathLength (CNClassPath *classPath)
{
  return classPath->attr == 0 ? (char) 2 : (char) 3;
}

int
getRequestSizeCNClassPath (CNClassPath *classPath)
{ // Convert words into bytes
  return getPathLength (classPath) * 2;
}

void
encodeCNClassPath (CNClassPath *classPath, char *buffer, int offset)
{
  buffer[offset] = getPathLength (classPath);
  buffer[offset + 1] = (char) 0x20;
  buffer[offset + 2] = (char) classPath->class_code;
  buffer[offset + 3] = (char) 0x24;
  buffer[offset + 4] = (char) classPath->instance;
  if (classPath->attr > 0)
    {
      buffer[offset + 5] = (char) 0x30;
      buffer[offset + 6] = (char) classPath->attr;
    }
}

