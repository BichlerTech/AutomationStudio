package com.bichler.iec.generator

import com.bichler.iec.iec.FunctionDeclaration
import com.bichler.iec.iec.FunctionBody
import com.bichler.iec.iec.IoVarDeclarations
import com.bichler.iec.iec.InputDeclarations
import com.bichler.iec.iec.DeclSpecification
import com.bichler.iec.iec.VarDeclSpecification
import com.bichler.iec.iec.OutputDeclarations
import com.bichler.iec.iec.InputOutputDeclarations

/**
 * generate code for all function specific functions and types
 */
class IecFunctionGenerator extends IecGenerator {
	
def compileFunctionStructPrototype(FunctionDeclaration function) '''
«IF function.name != null»
typedef struct «function.name»_struct_ «function.name»_struct;
«ENDIF»
'''

def compileFunctionDeclarationStructs(FunctionDeclaration function) '''
«IF function.name != null»
struct «function.name»_struct_ {
«function.type.compileGenericType» «function.name»;
«FOR fd : function.ioVarDeclarations»
«fd.compileVarDeclarations»
«ENDFOR»
};
«ENDIF»
'''

def compileFunctionDeclarationPrototypes(FunctionDeclaration function) '''
«IF function.name != null»
// prototype  for function «function.name»
«function.type.compileGenericType» func_«function.name» («function.name»_struct *value);
«ENDIF»
'''

def compileFunctionDeclarationImplementation(FunctionDeclaration function) '''
«IF function.name != null»

// implementation of function «function.name»
«function.type.compileGenericType» func_«function.name» («function.name»_struct *value)
{
	value->«function.name» = «function.type.compileDefaultInitialValue»;
	«FOR v : function.ioVarDeclarations»
	«v.compileInitializeVars»
	«ENDFOR»
	«IF function.body != null»
	/**
	* function body
	*/
	«function.body.bodyBody»
	«ENDIF»
	
	return value->«function.name»;
}
«ENDIF»
'''

def dispatch bodyBody(FunctionBody body) '''
'''
}