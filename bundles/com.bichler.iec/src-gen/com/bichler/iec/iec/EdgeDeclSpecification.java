/**
 */
package com.bichler.iec.iec;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Edge Decl Specification</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.EdgeDeclSpecification#isREdge <em>REdge</em>}</li>
 *   <li>{@link com.bichler.iec.iec.EdgeDeclSpecification#isFEdge <em>FEdge</em>}</li>
 * </ul>
 *
 * @see com.bichler.iec.iec.IecPackage#getEdgeDeclSpecification()
 * @model
 * @generated
 */
public interface EdgeDeclSpecification extends DeclSpecification
{
  /**
   * Returns the value of the '<em><b>REdge</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>REdge</em>' attribute.
   * @see #setREdge(boolean)
   * @see com.bichler.iec.iec.IecPackage#getEdgeDeclSpecification_REdge()
   * @model
   * @generated
   */
  boolean isREdge();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.EdgeDeclSpecification#isREdge <em>REdge</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>REdge</em>' attribute.
   * @see #isREdge()
   * @generated
   */
  void setREdge(boolean value);

  /**
   * Returns the value of the '<em><b>FEdge</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @return the value of the '<em>FEdge</em>' attribute.
   * @see #setFEdge(boolean)
   * @see com.bichler.iec.iec.IecPackage#getEdgeDeclSpecification_FEdge()
   * @model
   * @generated
   */
  boolean isFEdge();

  /**
   * Sets the value of the '{@link com.bichler.iec.iec.EdgeDeclSpecification#isFEdge <em>FEdge</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>FEdge</em>' attribute.
   * @see #isFEdge()
   * @generated
   */
  void setFEdge(boolean value);

} // EdgeDeclSpecification
