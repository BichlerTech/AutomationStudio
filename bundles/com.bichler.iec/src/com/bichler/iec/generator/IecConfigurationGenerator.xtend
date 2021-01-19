package com.bichler.iec.generator

import com.bichler.iec.iec.ConfigurationDeclaration

class IecConfigurationGenerator extends IecGenerator {

	def compileConfigurationStructPrototype(ConfigurationDeclaration config) '''
«IF config.name != null»
typedef struct «config.name»_struct_ «config.name»_struct;
«config.name»_struct *«config.name»;
«ENDIF»
'''

	def compileConfigurationDeclarationStructs(ConfigurationDeclaration config) '''
«IF config.name != null»
struct «config.name»_struct_ {
«config.globalVarDeclarations.compileVarDeclarations»
};
«ENDIF»'''

}