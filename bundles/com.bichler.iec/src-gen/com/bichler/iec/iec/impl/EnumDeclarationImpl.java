/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.EnumDeclaration;
import com.bichler.iec.iec.EnumeratedValue;
import com.bichler.iec.iec.Enumeration;
import com.bichler.iec.iec.IecPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Enum Declaration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.EnumDeclarationImpl#getEnumeration <em>Enumeration</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.EnumDeclarationImpl#getConstant <em>Constant</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EnumDeclarationImpl extends TypeDeclarationImpl implements EnumDeclaration
{
  /**
   * The cached value of the '{@link #getEnumeration() <em>Enumeration</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEnumeration()
   * @generated
   * @ordered
   */
  protected Enumeration enumeration;

  /**
   * The cached value of the '{@link #getConstant() <em>Constant</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getConstant()
   * @generated
   * @ordered
   */
  protected EnumeratedValue constant;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected EnumDeclarationImpl()
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
    return IecPackage.Literals.ENUM_DECLARATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Enumeration getEnumeration()
  {
    return enumeration;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetEnumeration(Enumeration newEnumeration, NotificationChain msgs)
  {
    Enumeration oldEnumeration = enumeration;
    enumeration = newEnumeration;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.ENUM_DECLARATION__ENUMERATION, oldEnumeration, newEnumeration);
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
  public void setEnumeration(Enumeration newEnumeration)
  {
    if (newEnumeration != enumeration)
    {
      NotificationChain msgs = null;
      if (enumeration != null)
        msgs = ((InternalEObject)enumeration).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.ENUM_DECLARATION__ENUMERATION, null, msgs);
      if (newEnumeration != null)
        msgs = ((InternalEObject)newEnumeration).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.ENUM_DECLARATION__ENUMERATION, null, msgs);
      msgs = basicSetEnumeration(newEnumeration, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.ENUM_DECLARATION__ENUMERATION, newEnumeration, newEnumeration));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EnumeratedValue getConstant()
  {
    return constant;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetConstant(EnumeratedValue newConstant, NotificationChain msgs)
  {
    EnumeratedValue oldConstant = constant;
    constant = newConstant;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.ENUM_DECLARATION__CONSTANT, oldConstant, newConstant);
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
  public void setConstant(EnumeratedValue newConstant)
  {
    if (newConstant != constant)
    {
      NotificationChain msgs = null;
      if (constant != null)
        msgs = ((InternalEObject)constant).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.ENUM_DECLARATION__CONSTANT, null, msgs);
      if (newConstant != null)
        msgs = ((InternalEObject)newConstant).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.ENUM_DECLARATION__CONSTANT, null, msgs);
      msgs = basicSetConstant(newConstant, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.ENUM_DECLARATION__CONSTANT, newConstant, newConstant));
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
      case IecPackage.ENUM_DECLARATION__ENUMERATION:
        return basicSetEnumeration(null, msgs);
      case IecPackage.ENUM_DECLARATION__CONSTANT:
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
      case IecPackage.ENUM_DECLARATION__ENUMERATION:
        return getEnumeration();
      case IecPackage.ENUM_DECLARATION__CONSTANT:
        return getConstant();
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
      case IecPackage.ENUM_DECLARATION__ENUMERATION:
        setEnumeration((Enumeration)newValue);
        return;
      case IecPackage.ENUM_DECLARATION__CONSTANT:
        setConstant((EnumeratedValue)newValue);
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
      case IecPackage.ENUM_DECLARATION__ENUMERATION:
        setEnumeration((Enumeration)null);
        return;
      case IecPackage.ENUM_DECLARATION__CONSTANT:
        setConstant((EnumeratedValue)null);
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
      case IecPackage.ENUM_DECLARATION__ENUMERATION:
        return enumeration != null;
      case IecPackage.ENUM_DECLARATION__CONSTANT:
        return constant != null;
    }
    return super.eIsSet(featureID);
  }

} //EnumDeclarationImpl
