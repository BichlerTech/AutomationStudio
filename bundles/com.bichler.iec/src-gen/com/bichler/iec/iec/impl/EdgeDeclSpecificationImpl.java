/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.EdgeDeclSpecification;
import com.bichler.iec.iec.IecPackage;

import java.lang.Boolean;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Edge Decl Specification</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.EdgeDeclSpecificationImpl#isREdge <em>REdge</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.EdgeDeclSpecificationImpl#isFEdge <em>FEdge</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EdgeDeclSpecificationImpl extends DeclSpecificationImpl implements EdgeDeclSpecification
{
  /**
   * The default value of the '{@link #isREdge() <em>REdge</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isREdge()
   * @generated
   * @ordered
   */
  protected static final boolean REDGE_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isREdge() <em>REdge</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isREdge()
   * @generated
   * @ordered
   */
  protected boolean rEdge = REDGE_EDEFAULT;

  /**
   * The default value of the '{@link #isFEdge() <em>FEdge</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isFEdge()
   * @generated
   * @ordered
   */
  protected static final boolean FEDGE_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isFEdge() <em>FEdge</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isFEdge()
   * @generated
   * @ordered
   */
  protected boolean fEdge = FEDGE_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected EdgeDeclSpecificationImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return IecPackage.Literals.EDGE_DECL_SPECIFICATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean isREdge()
  {
    return rEdge;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setREdge(boolean newREdge)
  {
    boolean oldREdge = rEdge;
    rEdge = newREdge;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.EDGE_DECL_SPECIFICATION__REDGE, oldREdge, rEdge));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean isFEdge()
  {
    return fEdge;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setFEdge(boolean newFEdge)
  {
    boolean oldFEdge = fEdge;
    fEdge = newFEdge;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.EDGE_DECL_SPECIFICATION__FEDGE, oldFEdge, fEdge));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case IecPackage.EDGE_DECL_SPECIFICATION__REDGE:
        return isREdge();
      case IecPackage.EDGE_DECL_SPECIFICATION__FEDGE:
        return isFEdge();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case IecPackage.EDGE_DECL_SPECIFICATION__REDGE:
        setREdge((Boolean)newValue);
        return;
      case IecPackage.EDGE_DECL_SPECIFICATION__FEDGE:
        setFEdge((Boolean)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case IecPackage.EDGE_DECL_SPECIFICATION__REDGE:
        setREdge(REDGE_EDEFAULT);
        return;
      case IecPackage.EDGE_DECL_SPECIFICATION__FEDGE:
        setFEdge(FEDGE_EDEFAULT);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case IecPackage.EDGE_DECL_SPECIFICATION__REDGE:
        return rEdge != REDGE_EDEFAULT;
      case IecPackage.EDGE_DECL_SPECIFICATION__FEDGE:
        return fEdge != FEDGE_EDEFAULT;
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuilder result = new StringBuilder(super.toString());
    result.append(" (rEdge: ");
    result.append(rEdge);
    result.append(", fEdge: ");
    result.append(fEdge);
    result.append(')');
    return result.toString();
  }

} //EdgeDeclSpecificationImpl
