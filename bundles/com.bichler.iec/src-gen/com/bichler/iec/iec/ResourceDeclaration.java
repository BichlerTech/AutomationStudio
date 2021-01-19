/**
 */
package com.bichler.iec.iec;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Resource Declaration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.ResourceDeclaration#getResname <em>Resname</em>}</li>
 *   <li>{@link com.bichler.iec.iec.ResourceDeclaration#getGlobalVarDeclarations <em>Global Var Declarations</em>}</li>
 *   <li>{@link com.bichler.iec.iec.ResourceDeclaration#getSingleresource <em>Singleresource</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getResourceDeclaration()
 * @model
 * @generated
 */
public interface ResourceDeclaration extends LibraryElementDeclaration
{
  /**
   * Returns the value of the '<em><b>Resname</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Resname</em>' attribute.
   * @see #setResname(String)
   * @see com.bichler.iec.iec.IecPackage#getResourceDeclaration_Resname()
   * @model
   * @generated
   */
  String getResname();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.ResourceDeclaration#getResname <em>Resname</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Resname</em>' attribute.
   * @see #getResname()
   * @generated
   */
  void setResname(String value);

  /**
   * Returns the value of the '<em><b>Global Var Declarations</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Global Var Declarations</em>' containment reference.
   * @see #setGlobalVarDeclarations(GlobalVarDeclarations)
   * @see com.bichler.iec.iec.IecPackage#getResourceDeclaration_GlobalVarDeclarations()
   * @model containment="true"
   * @generated
   */
  GlobalVarDeclarations getGlobalVarDeclarations();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.ResourceDeclaration#getGlobalVarDeclarations <em>Global Var Declarations</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Global Var Declarations</em>' containment reference.
   * @see #getGlobalVarDeclarations()
   * @generated
   */
  void setGlobalVarDeclarations(GlobalVarDeclarations value);

  /**
   * Returns the value of the '<em><b>Singleresource</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Singleresource</em>' containment reference.
   * @see #setSingleresource(SingleResourceDeclaration)
   * @see com.bichler.iec.iec.IecPackage#getResourceDeclaration_Singleresource()
   * @model containment="true"
   * @generated
   */
  SingleResourceDeclaration getSingleresource();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.ResourceDeclaration#getSingleresource <em>Singleresource</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Singleresource</em>' containment reference.
   * @see #getSingleresource()
   * @generated
   */
  void setSingleresource(SingleResourceDeclaration value);

} // ResourceDeclaration
