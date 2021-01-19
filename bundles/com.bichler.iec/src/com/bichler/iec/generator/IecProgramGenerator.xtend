package com.bichler.iec.generator

import com.bichler.iec.iec.ProgramAccessDecls
import com.bichler.iec.iec.ProgramConfiguration
import com.bichler.iec.iec.ProgramDeclaration
import com.bichler.iec.iec.ProgramVarDeclarations
import com.bichler.iec.iec.SymbolicVariableAccess
import com.bichler.iec.iec.TaskConfiguration

class IecProgramGenerator extends IecGenerator {

	def compileProgramMemory(ProgramConfiguration config) '''
«config.prog.name»_struct *«config.name» = NULL;
'''

	def compileProgramMemoryInit(ProgramConfiguration config) '''
«config.name» = malloc(sizeof(«config.prog.name»_struct));
'''

	def compileProgramStructPrototype(ProgramDeclaration program) '''
«IF program.name != null»
typedef struct «program.name»_struct_ «program.name»_struct;
«ENDIF»
'''

	def compileProgramDeclarationStructs(ProgramDeclaration program) '''
«IF program.name != null»
struct «program.name»_struct_ {
«FOR fd : program.varDeclarations»
«fd.compileVarDeclarations»
«ENDFOR»
};
«ENDIF»'''

	def compileProgramDeclarationPrototypes(ProgramDeclaration program) '''
«IF program.name != null»
int program_«program.name» ();
«ENDIF»
'''

	def compileProgramDeclarationImplementation(ProgramDeclaration program) '''
«IF program.name != null»
int program_«program.name» ()
{
	«FOR decl : program.varDeclarations»
	«IF decl instanceof SymbolicVariableAccess»
	«decl.compileVarDeclarations»
	«ENDIF»
	«ENDFOR»
	«IF program.body != null»
	«program.body.bodyBody»
	«ENDIF»
	
	return 0;
}
«ENDIF»
'''

	def dispatch compileVarDeclarations(ProgramAccessDecls vars) '''
«FOR v : vars.programAccessDecl»
«v.typeName.compileGenericType» *«v.symbolicVariable.compileSymbolicVariableAccess»;«ENDFOR»'''

	def dispatch compileVarDeclarations(ProgramVarDeclarations vars) ''''''

	def dispatch compilePointerFlag(ProgramDeclaration prog, String variable) '''
«FOR vars : prog.varDeclarations»
«IF vars != null »
«compilePointer(vars, variable)»«ENDIF»«ENDFOR»'''

	def dispatch compilePointer(ProgramVarDeclarations vars, String variable) ''''''

	/**
	 * checks if the variable is a access var (pointer in c) so we add a * in front
	 */
	def dispatch compilePointer(ProgramAccessDecls vars, String variable) '''
«FOR v : vars.programAccessDecl»
«IF v.accessName.name.compareTo(variable) == 0»
*«ENDIF»«ENDFOR»'''

	def compileProgramConfiguration(ProgramConfiguration config, Iterable<TaskConfiguration> tasks) '''
«IF config.name != null»
«IF config.task != null»
«FOR t :tasks»
«IF t.name != null && t.name.compareTo(config.task.name) == 0»
«IF t.taskInit != null»
«IF t.taskInit.single != null»
addSingleProgram(/*function pointer*/&program_«config.prog.name», /*single event*/«t.taskInit.single», /*priority*/ «t.taskInit.prior»);
«ELSEIF t.taskInit.interval != null»
addIntervalProgram(/*function pointer*/&program_«config.prog.name»(), /*interval event*/«t.taskInit.single», /*priority*/ «t.taskInit.prior»);
«ENDIF»
«ENDIF»
«ENDIF»
«ENDFOR»
«ENDIF»
«ENDIF»
'''
}