/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.ArrayDeclaration;
import com.bichler.iec.iec.ArrayInitialization;
import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.NonGenericType;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EDataTypeEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Array Declaration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.ArrayDeclarationImpl#getRanges <em>Ranges</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.ArrayDeclarationImpl#getBaseType <em>Base Type</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.ArrayDeclarationImpl#getConstant <em>Constant</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ArrayDeclarationImpl extends TypeDeclarationImpl implements ArrayDeclaration
{
  /**
   * The cached value of the '{@link #getRanges() <em>Ranges</em>}' attribute list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getRanges()
   * @generated
   * @ordered
   */
  protected EList<String> ranges;

  /**
   * The cached value of the '{@link #getBaseType() <em>Base Type</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getBaseType()
   * @generated
   * @ordered
   */
  protected NonGenericType baseType;

  /**
   * The cached value of the '{@link #getConstant() <em>Constant</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getConstant()
   * @generated
   * @ordered
   */
  protected ArrayInitialization constant;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ArrayDeclarationImpl()
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
    return IecPackage.Literals.ARRAY_DECLARATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<String> getRanges()
  {
    if (ranges == null)
    {
      ranges = new EDataTypeEList<String>(String.class, this, IecPackage.ARRAY_DECLARATION__RANGES);
    }
    return ranges;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NonGenericType getBaseType()
  {
    return baseType;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetBaseType(NonGenericType newBaseType, NotificationChain msgs)
  {
    NonGenericType oldBaseType = baseType;
    baseType = newBaseType;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.ARRAY_DECLARATION__BASE_TYPE, oldBaseType, newBaseType);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setBaseType(NonGenericType newBaseType)
  {
    if (newBaseType != baseType)
    {
      NotificationChain msgs = null;
      if (baseType != null)
        msgs = ((InternalEObject)baseType).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.ARRAY_DECLARATION__BASE_TYPE, null, msgs);
      if (newBaseType != null)
        msgs = ((InternalEObject)newBaseType).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.ARRAY_DECLARATION__BASE_TYPE, null, msgs);
      msgs = basicSetBaseType(newBaseType, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.ARRAY_DECLARATION__BASE_TYPE, newBaseType, newBaseType));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ArrayInitialization getConstant()
  {
    return constant;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetConstant(ArrayInitialization newConstant, NotificationChain msgs)
  {
    ArrayInitialization oldConstant = constant;
    constant = newConstant;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.ARRAY_DECLARATION__CONSTANT, oldConstant, newConstant);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setConstant(ArrayInitialization newConstant)
  {
    if (newConstant != constant)
    {
      NotificationChain msgs = null;
      if (constant != null)
        msgs = ((InternalEObject)constant).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.ARRAY_DECLARATION__CONSTANT, null, msgs);
      if (newConstant != null)
        msgs = ((InternalEObject)newConstant).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.ARRAY_DECLARATION__CONSTANT, null, msgs);
      msgs = basicSetConstant(newConstant, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.ARRAY_DECLARATION__CONSTANT, newConstant, newConstant));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case IecPackage.ARRAY_DECLARATION__BASE_TYPE:
        return basicSetBaseType(null, msgs);
      case IecPackage.ARRAY_DECLARATION__CONSTANT:
        return basicSetConstant(null, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
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
      case IecPackage.ARRAY_DECLARATION__RANGES:
        return getRanges();
      case IecPackage.ARRAY_DECLARATION__BASE_TYPE:
        return getBaseType();
      case IecPackage.ARRAY_DECLARATION__CONSTANT:
        return getConstant();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case IecPackage.ARRAY_DECLARATION__RANGES:
        getRanges().clear();
        getRanges().addAll((Collection<? extends String>)newValue);
        return;
      case IecPackage.ARRAY_DECLARATION__BASE_TYPE:
        setBaseType((NonGenericType)newValue);
        return;
      case IecPackage.ARRAY_DECLARATION__CONSTANT:
        setConstant((ArrayInitialization)newValue);
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
      case IecPackage.ARRAY_DECLARATION__RANGES:
        getRanges().clear();
        return;
      case IecPackage.ARRAY_DECLARATION__BASE_TYPE:
        setBaseType((NonGenericType)null);
        return;
      case IecPackage.ARRAY_DECLARATION__CONSTANT:
        setConstant((ArrayInitialization)null);
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
      case IecPackage.ARRAY_DECLARATION__RANGES:
        return ranges != null && !ranges.isEmpty();
      case IecPackage.ARRAY_DECLARATION__BASE_TYPE:
        return baseType != null;
      case IecPackage.ARRAY_DECLARATION__CONSTANT:
        return constant != null;
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
    result.append(" (ranges: ");
    result.append(ranges);
    result.append(')');
    return result.toString();
  }

} //ArrayDeclarationImpl
