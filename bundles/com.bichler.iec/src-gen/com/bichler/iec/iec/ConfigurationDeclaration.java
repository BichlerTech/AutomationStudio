/**
 */
package com.bichler.iec.iec;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Configuration Declaration</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.ConfigurationDeclaration#getGlobalVarDeclarations <em>Global Var Declarations</em>}</li>
 *   <li>{@link com.bichler.iec.iec.ConfigurationDeclaration#getResdecl <em>Resdecl</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getConfigurationDeclaration()
 * @model
 * @generated
 */
public interface ConfigurationDeclaration extends LibraryElementDeclaration
{
  /**
   * Returns the value of the '<em><b>Global Var Declarations</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Global Var Declarations</em>' containment reference.
   * @see #setGlobalVarDeclarations(GlobalVarDeclarations)
   * @see com.bichler.iec.iec.IecPackage#getConfigurationDeclaration_GlobalVarDeclarations()
   * @model containment="true"
   * @generated
   */
  GlobalVarDeclarations getGlobalVarDeclarations();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.ConfigurationDeclaration#getGlobalVarDeclarations <em>Global Var Declarations</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Global Var Declarations</em>' containment reference.
   * @see #getGlobalVarDeclarations()
   * @generated
   */
  void setGlobalVarDeclarations(GlobalVarDeclarations value);

  /**
   * Returns the value of the '<em><b>Resdecl</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>Resdecl</em>' containment reference.
   * @see #setResdecl(ResourceDeclaration)
   * @see com.bichler.iec.iec.IecPackage#getConfigurationDeclaration_Resdecl()
   * @model containment="true"
   * @generated
   */
  ResourceDeclaration getResdecl();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.ConfigurationDeclaration#getResdecl <em>Resdecl</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Resdecl</em>' containment reference.
   * @see #getResdecl()
   * @generated
   */
  void setResdecl(ResourceDeclaration value);

} // ConfigurationDeclaration
