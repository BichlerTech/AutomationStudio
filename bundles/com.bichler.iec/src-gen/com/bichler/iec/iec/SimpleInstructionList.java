/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Simple Instruction List</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.SimpleInstructionList#getInstructions <em>Instructions</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getSimpleInstructionList()
 * @model
 * @generated
 */
public interface SimpleInstructionList extends EObject
{
  /**
   * Returns the value of the '<em><b>Instructions</b></em>' containment reference list.
   * The list contents are of type {@link com.bichler.iec.iec.SimpleInstruction}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Instructions</em>' containment reference list.
   * @see com.bichler.iec.iec.IecPackage#getSimpleInstructionList_Instructions()
   * @model containment="true"
   * @generated
   */
  EList<SimpleInstruction> getInstructions();

} // SimpleInstructionList
