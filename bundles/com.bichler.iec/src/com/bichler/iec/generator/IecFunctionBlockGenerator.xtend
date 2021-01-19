package com.bichler.iec.generator

import com.bichler.iec.iec.FunctionBlockDeclaration
import com.bichler.iec.iec.VarDeclarations
import com.bichler.iec.iec.FunctionBlockVarDeclarations
import com.bichler.iec.iec.FunctionBlockBody

/**
 * generate code for all function block specific functions and types
 */
class IecFunctionBlockGenerator extends IecGenerator {

def compileFunctionBlockStructPrototype(FunctionBlockDeclaration function) '''
«IF function.name != null»
typedef struct «function.name»_struct_ «function.name»_struct;
«ENDIF»
'''

def compileFunctionBlockDeclarationStructs(FunctionBlockDeclaration function) '''
«IF function.name != null»
struct «function.name»_struct_ {
«FOR fd : function.varDeclarations»
«fd.compileVarDeclarations»
«ENDFOR»
};
«ENDIF»
'''

def compileFunctionBlockDeclarationPrototypes(FunctionBlockDeclaration function) '''
«IF function.name != null»
int func_block_«function.name» («function.name»_struct *value);
«ENDIF»
'''

def compileFunctionBlockDeclarationImplementation(FunctionBlockDeclaration function) '''
«IF function.name != null»
int func_block_«function.name» («function.name»_struct *value)
{
	«FOR v : function.varDeclarations»
	«v.compileInitializeVars»
	«ENDFOR»
	«IF function.body != null»
	«function.body.bodyBody»
	«ENDIF»
	
	return 0;
}
«ENDIF»
'''

def dispatch bodyBody(FunctionBlockBody body) '''
«body»
'''
}
