/*****************************************************************************
 *                                                                           *
 * Copyright (c) 2018 Bichler Technologies GmbH. All rights reserved.        *
 *                                                                           *
 * Software License Agreement ("SLA") Version 1.0                            *
 *                                                                           *
 * !!!!This file was generated by Automation Studio, do not change it!!!     *
 *                                                                           *
 * Unless explicitly acquired and licensed from Licensor under another       *
 * license, the contents of this file are subject to the Software License    *
 * Agreement ("SLA") Version 2.6, or subsequent versions as allowed by the   *
 * SLA, and You may not copy or use this file in either source code or       *
 * executable form, except in compliance with the terms and conditions of    *
 * the SLA.                                                                  *
 *                                                                           *
 * All software distributed under the SLA is provided strictly on an "AS     *
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED,       *
 * AND LICENSOR HEREBY DISCLAIMS ALL SUCH WARRANTIES, INCLUDING WITHOUT      *
 * LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR   *
 * PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the SLA for specific   *
 * language governing rights and limitations under the SLA.                  *
 *                                                                           *
 * The complete license agreement can be found here:                         *
 * http://bichler.tech/License/SLA/1.0/                                      *
 *                                                                           *
 * IecTypesGenerator.xtend                                                   *
 *  Created on: Jun 04, 2018                                                 *
 *      Author: hannes bichler                                               *
 *                                                                           *
 *****************************************************************************/
package com.bichler.iec.generator

import com.bichler.iec.iec.DataTypeDeclaration
import com.bichler.iec.iec.TypeDeclaration
import com.bichler.iec.iec.ArrayDeclaration
import com.bichler.iec.iec.RangeDeclaration
import com.bichler.iec.iec.EnumDeclaration
import com.bichler.iec.iec.StringDeclaration
import com.bichler.iec.iec.StructureTypeDeclaration
import com.bichler.iec.iec.StructureDeclaration
import com.bichler.iec.iec.SimpleTypeDeclaration
import org.eclipse.xtext.generator.IFileSystemAccess
import org.eclipse.emf.ecore.resource.Resource

/**
 * generate code for all function block specific functions and types
 */
class IecTypesGenerator extends IecGenerator {

	public def doGenerateType(Resource resource) {
		var String s = ""
		var IecTypesGenerator tgen = new IecTypesGenerator()
		for (e : resource.allContents.toIterable.filter(DataTypeDeclaration)) {
			s += "struct " + e.name + " {\n"
			s += tgen.compileTypeDefinition(e)
			s += "};\n\n"
		// s += fgen.compileFunctionStructPrototype(e); // e.compileFunctionStructPrototype
		}

		return s;
	}

	def compileDataType(DataTypeDeclaration dtDecl) '''
�compileHeader�
�IF dtDecl.typeDeclaration !== null�
�FOR typeDecl : dtDecl.typeDeclaration�
�typeDecl.compileTypeDeclaration�
�ENDFOR�
�ENDIF�
'''

	def compileTypeDeclaration(TypeDeclaration typeDecl) '''
�IF typeDecl.bodyVariable !== null�
typedef �typeDecl.derivedType.name� 
�ENDIF�
'''

	def compileHeader() '''
/*****************************************************************************
 *                                                                           *
 * Copyright (c) 2018 Bichler Technologies GmbH. All rights reserved.        *
 *                                                                           *
 * Software License Agreement ("SLA") Version 1.0                            *
 *                                                                           *
 * !!!!This file was generated by Automation Studio, do not change it!!!     *
 *                                                                           *
 * Unless explicitly acquired and licensed from Licensor under another       *
 * license, the contents of this file are subject to the Software License    *
 * Agreement ("SLA") Version 2.6, or subsequent versions as allowed by the   *
 * SLA, and You may not copy or use this file in either source code or       *
 * executable form, except in compliance with the terms and conditions of    *
 * the SLA.                                                                  *
 *                                                                           *
 * All software distributed under the SLA is provided strictly on an "AS     *
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED,       *
 * AND LICENSOR HEREBY DISCLAIMS ALL SUCH WARRANTIES, INCLUDING WITHOUT      *
 * LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR   *
 * PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the SLA for specific   *
 * language governing rights and limitations under the SLA.                  *
 *                                                                           *
 * The complete license agreement can be found here:                         *
 * http://bichler.tech/License/SLA/1.0/                                      *
 *                                                                           *
 * iec_datatypes.h                                                           *
 *  Created on: Jun 04, 2018                                                 *
 *      Author: hannes bichler                                               *
 *                                                                           *
 *****************************************************************************/

#ifndef IEC_TYPES_H_
#define IEC_TYPES_H_ 1

#include <stdlib.h>
#include <stdio.h>

//****************************************************************************
// start system type definitions
//****************************************************************************

'''

def compileSystemCustomSep() '''

//****************************************************************************
// end system type definitions
//****************************************************************************

//****************************************************************************
// start custom type definitions
//****************************************************************************


'''

def compileCustomEnd() '''

//****************************************************************************
// end custom type definitions
//****************************************************************************

'''

	def private int getRangeCount(String range) {
		var String start = range.substring(0, range.indexOf("."));
		var String end = range.substring(range.lastIndexOf(".") + 1);

		var int s = Integer.parseInt(start);
		var int e = Integer.parseInt(end);

		return e - s + 1;
	}

	/** ************************************************************************************
	 * 
	 *  start generate code for all type definitions
	 * 
	 * ************************************************************************************ */
	/**
	 * write out type definition for array type
	 */
	def dispatch compileCustomTypeDefinition(ArrayDeclaration decl) '''
�IF decl.baseType != null��decl.baseType.compileGenericType��ENDIF� �decl.derivedType.name��FOR r : decl.ranges��var range = getRangeCount(r)�[�range�]�ENDFOR�'''

	/**
	 * variable initialization used in separat function
	 * 
	 *  �IF decl.constant != null�
	 * �IF decl.constant.initialElements.size > 1�
	 * {
	 * �ENDIF�
	 * �FOR init : decl.constant.initialElements� �IF !decl.constant.initialElements.get(0).equals(init)�, �ENDIF�{�init.initialElement.compileInitialElement�}�ENDFOR��IF decl.constant.initialElements.size > 1�}�ENDIF��ENDIF�'''
	 */
	/**
	 * write out type definition for range type
	 */
	def dispatch compileCustomTypeDefinition(RangeDeclaration decl) '''
�var range = getRangeCount(decl.range)�
�decl.baseType.compileGenericType� �decl.derivedType.name�[�range�]'''

	/** *
	 * �IF decl.constant != null��initializeRange(range, decl.constant)��ENDIF�'''

	 * /**
	 * write out type definition for enumeration type
	 */
	def dispatch compileCustomTypeDefinition(EnumDeclaration decl) '''
enum �decl.derivedType.name�
{
	�decl.enumeration�
}'''

	/**
	 * write out type definition for string type
	 */
	def dispatch compileCustomTypeDefinition(StringDeclaration decl) '''
STRING[�decl.size�] �decl.derivedType.name�'''

	/**
	 * write out type definition for struct type
	 */
	def dispatch compileCustomTypeDefinition(StructureTypeDeclaration decl) ''' 
struct �decl.derivedType.name�
{
	�decl.declaration.compileCustomTypeDefinition�
}'''

	/**
	 * write out type definition for struct type
	 */
	def dispatch compileCustomTypeDefinition(StructureDeclaration decl) '''
�FOR elem : decl.structureElement�
�elem.specInit.compileCustomTypeDefinition� �elem.name�;
�ENDFOR�'''

	/**
	 * write out type definition for simple type
	 */
	def dispatch compileCustomTypeDefinition(SimpleTypeDeclaration decl) '''
�decl.specInit.baseType.compileGenericType� �decl.derivedType.name�'''

	/**
	 * 
	 * �IF decl.specInit.constant != null� : �decl.specInit.constant.compileInitialElement��ENDIF�'''
	 */
	/**
	 * base function for type definition
	 */
	def dispatch compileCustomTypeDefinition(TypeDeclaration decl) '''
'''

	/**
	 * generate all customer type definitions
	 */
	def compileTypeDefinition(DataTypeDeclaration datatype) '''
�FOR decls : datatype.typeDeclaration�
�decls.compileCustomTypeDefinition�;
�ENDFOR�
'''

def compileFooter() '''

#endif // IEC_TYPES_H_
'''

/** ************************************************************************************
 * 
 *  end generate code for all type definitions
 * 
 * ************************************************************************************ */
}
