/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.CaseListElement;
import com.bichler.iec.iec.EnumeratedValue;
import com.bichler.iec.iec.IecPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Case List Element</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.CaseListElementImpl#getSubRange <em>Sub Range</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.CaseListElementImpl#getInteger <em>Integer</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.CaseListElementImpl#getEnumeratedValue <em>Enumerated Value</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CaseListElementImpl extends MinimalEObjectImpl.Container implements CaseListElement
{
  /**
   * The default value of the '{@link #getSubRange() <em>Sub Range</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSubRange()
   * @generated
   * @ordered
   */
  protected static final String SUB_RANGE_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getSubRange() <em>Sub Range</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSubRange()
   * @generated
   * @ordered
   */
  protected String subRange = SUB_RANGE_EDEFAULT;

  /**
   * The default value of the '{@link #getInteger() <em>Integer</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getInteger()
   * @generated
   * @ordered
   */
  protected static final String INTEGER_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getInteger() <em>Integer</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getInteger()
   * @generated
   * @ordered
   */
  protected String integer = INTEGER_EDEFAULT;

  /**
   * The cached value of the '{@link #getEnumeratedValue() <em>Enumerated Value</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEnumeratedValue()
   * @generated
   * @ordered
   */
  protected EnumeratedValue enumeratedValue;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected CaseListElementImpl()
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
    return IecPackage.Literals.CASE_LIST_ELEMENT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getSubRange()
  {
    return subRange;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setSubRange(String newSubRange)
  {
    String oldSubRange = subRange;
    subRange = newSubRange;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.CASE_LIST_ELEMENT__SUB_RANGE, oldSubRange, subRange));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getInteger()
  {
    return integer;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setInteger(String newInteger)
  {
    String oldInteger = integer;
    integer = newInteger;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.CASE_LIST_ELEMENT__INTEGER, oldInteger, integer));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EnumeratedValue getEnumeratedValue()
  {
    if (enumeratedValue != null && enumeratedValue.eIsProxy())
    {
      InternalEObject oldEnumeratedValue = (InternalEObject)enumeratedValue;
      enumeratedValue = (EnumeratedValue)eResolveProxy(oldEnumeratedValue);
      if (enumeratedValue != oldEnumeratedValue)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, IecPackage.CASE_LIST_ELEMENT__ENUMERATED_VALUE, oldEnumeratedValue, enumeratedValue));
      }
    }
    return enumeratedValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EnumeratedValue basicGetEnumeratedValue()
  {
    return enumeratedValue;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setEnumeratedValue(EnumeratedValue newEnumeratedValue)
  {
    EnumeratedValue oldEnumeratedValue = enumeratedValue;
    enumeratedValue = newEnumeratedValue;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.CASE_LIST_ELEMENT__ENUMERATED_VALUE, oldEnumeratedValue, enumeratedValue));
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
      case IecPackage.CASE_LIST_ELEMENT__SUB_RANGE:
        return getSubRange();
      case IecPackage.CASE_LIST_ELEMENT__INTEGER:
        return getInteger();
      case IecPackage.CASE_LIST_ELEMENT__ENUMERATED_VALUE:
        if (resolve) return getEnumeratedValue();
        return basicGetEnumeratedValue();
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
      case IecPackage.CASE_LIST_ELEMENT__SUB_RANGE:
        setSubRange((String)newValue);
        return;
      case IecPackage.CASE_LIST_ELEMENT__INTEGER:
        setInteger((String)newValue);
        return;
      case IecPackage.CASE_LIST_ELEMENT__ENUMERATED_VALUE:
        setEnumeratedValue((EnumeratedValue)newValue);
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
      case IecPackage.CASE_LIST_ELEMENT__SUB_RANGE:
        setSubRange(SUB_RANGE_EDEFAULT);
        return;
      case IecPackage.CASE_LIST_ELEMENT__INTEGER:
        setInteger(INTEGER_EDEFAULT);
        return;
      case IecPackage.CASE_LIST_ELEMENT__ENUMERATED_VALUE:
        setEnumeratedValue((EnumeratedValue)null);
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
      case IecPackage.CASE_LIST_ELEMENT__SUB_RANGE:
        return SUB_RANGE_EDEFAULT == null ? subRange != null : !SUB_RANGE_EDEFAULT.equals(subRange);
      case IecPackage.CASE_LIST_ELEMENT__INTEGER:
        return INTEGER_EDEFAULT == null ? integer != null : !INTEGER_EDEFAULT.equals(integer);
      case IecPackage.CASE_LIST_ELEMENT__ENUMERATED_VALUE:
        return enumeratedValue != null;
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
    result.append(" (subRange: ");
    result.append(subRange);
    result.append(", integer: ");
    result.append(integer);
    result.append(')');
    return result.toString();
  }

} //CaseListElementImpl
