/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Instruction List</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.InstructionList#getInstructions <em>Instructions</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getInstructionList()
 * @model
 * @generated
 */
public interface InstructionList extends FunctionBody, FunctionBlockBody
{
  /**
   * Returns the value of the '<em><b>Instructions</b></em>' containment reference list.
   * The list contents are of type {@link com.bichler.iec.iec.Instruction}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Instructions</em>' containment reference list.
   * @see com.bichler.iec.iec.IecPackage#getInstructionList_Instructions()
   * @model containment="true"
   * @generated
   */
  EList<Instruction> getInstructions();

} // InstructionList
