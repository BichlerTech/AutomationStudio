package com.bichler.iec.generator

import com.bichler.iec.iec.TaskConfiguration

/**
 * this class is not used at the moment
 */
class IecTaskGenerator extends IecGenerator {

def compileTaskDeclarationPrototypes(TaskConfiguration task) '''
«IF task.name != null»
int task_«task.name» ();
«ENDIF»
'''
	
def compileProgramDeclarationImplementation(TaskConfiguration task) '''
«IF task.name != null»
«IF task.taskInit.single != null»
task_toSingle(
// add task to single list
int task_«task.name» ()
{
	return 0;
}, «task.taskInit.prior» «task.taskInit.single»);
«ELSEIF task.taskInit.interval != null»
task_toInterval(
// add task to schedule list
int task_«task.name» ()
{
	return 0;
}, «task.taskInit.prior» «task.taskInit.interval»);
«ENDIF»
«ENDIF»
'''
}