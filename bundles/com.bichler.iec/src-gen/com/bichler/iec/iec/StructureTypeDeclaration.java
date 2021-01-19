/**
 */
package com.bichler.iec.iec;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Structure Type Declaration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.StructureTypeDeclaration#getDeclaration <em>Declaration</em>}</li>
 *   <li>{@link com.bichler.iec.iec.StructureTypeDeclaration#getInitialization <em>Initialization</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getStructureTypeDeclaration()
 * @model
 * @generated
 */
public interface StructureTypeDeclaration extends TypeDeclaration
{
  /**
   * Returns the value of the '<em><b>Declaration</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Declaration</em>' containment reference.
   * @see #setDeclaration(StructureDeclaration)
   * @see com.bichler.iec.iec.IecPackage#getStructureTypeDeclaration_Declaration()
   * @model containment="true"
   * @generated
   */
  StructureDeclaration getDeclaration();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.StructureTypeDeclaration#getDeclaration <em>Declaration</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Declaration</em>' containment reference.
   * @see #getDeclaration()
   * @generated
   */
  void setDeclaration(StructureDeclaration value);

  /**
   * Returns the value of the '<em><b>Initialization</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Initialization</em>' containment reference.
   * @see #setInitialization(InitializedStructure)
   * @see com.bichler.iec.iec.IecPackage#getStructureTypeDeclaration_Initialization()
   * @model containment="true"
   * @generated
   */
  InitializedStructure getInitialization();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.StructureTypeDeclaration#getInitialization <em>Initialization</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Initialization</em>' containment reference.
   * @see #getInitialization()
   * @generated
   */
  void setInitialization(InitializedStructure value);

} // StructureTypeDeclaration
