/**
 */
package com.bichler.iec.iec;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Structure Initialization</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.StructureInitialization#getInitialElements <em>Initial Elements</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getStructureInitialization()
 * @model
 * @generated
 */
public interface StructureInitialization extends InitialElement
{
  /**
   * Returns the value of the '<em><b>Initial Elements</b></em>' containment reference list.
   * The list contents are of type {@link com.bichler.iec.iec.StructureElementInitialization}.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Initial Elements</em>' containment reference list.
   * @see com.bichler.iec.iec.IecPackage#getStructureInitialization_InitialElements()
   * @model containment="true"
   * @generated
   */
  EList<StructureElementInitialization> getInitialElements();

} // StructureInitialization
