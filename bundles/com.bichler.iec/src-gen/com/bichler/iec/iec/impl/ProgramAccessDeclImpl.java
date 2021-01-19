/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.NamedVariableAccess;
import com.bichler.iec.iec.NonGenericType;
import com.bichler.iec.iec.ProgramAccessDecl;
import com.bichler.iec.iec.Variable;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Program Access Decl</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.ProgramAccessDeclImpl#getAccessName <em>Access Name</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.ProgramAccessDeclImpl#getSymbolicVariable <em>Symbolic Variable</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.ProgramAccessDeclImpl#getTypeName <em>Type Name</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.ProgramAccessDeclImpl#getDirection <em>Direction</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ProgramAccessDeclImpl extends MinimalEObjectImpl.Container implements ProgramAccessDecl
{
  /**
   * The cached value of the '{@link #getAccessName() <em>Access Name</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getAccessName()
   * @generated
   * @ordered
   */
  protected Variable accessName;

  /**
   * The cached value of the '{@link #getSymbolicVariable() <em>Symbolic Variable</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSymbolicVariable()
   * @generated
   * @ordered
   */
  protected NamedVariableAccess symbolicVariable;

  /**
   * The cached value of the '{@link #getTypeName() <em>Type Name</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTypeName()
   * @generated
   * @ordered
   */
  protected NonGenericType typeName;

  /**
   * The default value of the '{@link #getDirection() <em>Direction</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDirection()
   * @generated
   * @ordered
   */
  protected static final String DIRECTION_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getDirection() <em>Direction</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getDirection()
   * @generated
   * @ordered
   */
  protected String direction = DIRECTION_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ProgramAccessDeclImpl()
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
    return IecPackage.Literals.PROGRAM_ACCESS_DECL;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Variable getAccessName()
  {
    return accessName;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetAccessName(Variable newAccessName, NotificationChain msgs)
  {
    Variable oldAccessName = accessName;
    accessName = newAccessName;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.PROGRAM_ACCESS_DECL__ACCESS_NAME, oldAccessName, newAccessName);
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
  public void setAccessName(Variable newAccessName)
  {
    if (newAccessName != accessName)
    {
      NotificationChain msgs = null;
      if (accessName != null)
        msgs = ((InternalEObject)accessName).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.PROGRAM_ACCESS_DECL__ACCESS_NAME, null, msgs);
      if (newAccessName != null)
        msgs = ((InternalEObject)newAccessName).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.PROGRAM_ACCESS_DECL__ACCESS_NAME, null, msgs);
      msgs = basicSetAccessName(newAccessName, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.PROGRAM_ACCESS_DECL__ACCESS_NAME, newAccessName, newAccessName));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NamedVariableAccess getSymbolicVariable()
  {
    return symbolicVariable;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetSymbolicVariable(NamedVariableAccess newSymbolicVariable, NotificationChain msgs)
  {
    NamedVariableAccess oldSymbolicVariable = symbolicVariable;
    symbolicVariable = newSymbolicVariable;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.PROGRAM_ACCESS_DECL__SYMBOLIC_VARIABLE, oldSymbolicVariable, newSymbolicVariable);
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
  public void setSymbolicVariable(NamedVariableAccess newSymbolicVariable)
  {
    if (newSymbolicVariable != symbolicVariable)
    {
      NotificationChain msgs = null;
      if (symbolicVariable != null)
        msgs = ((InternalEObject)symbolicVariable).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.PROGRAM_ACCESS_DECL__SYMBOLIC_VARIABLE, null, msgs);
      if (newSymbolicVariable != null)
        msgs = ((InternalEObject)newSymbolicVariable).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.PROGRAM_ACCESS_DECL__SYMBOLIC_VARIABLE, null, msgs);
      msgs = basicSetSymbolicVariable(newSymbolicVariable, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.PROGRAM_ACCESS_DECL__SYMBOLIC_VARIABLE, newSymbolicVariable, newSymbolicVariable));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NonGenericType getTypeName()
  {
    return typeName;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetTypeName(NonGenericType newTypeName, NotificationChain msgs)
  {
    NonGenericType oldTypeName = typeName;
    typeName = newTypeName;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.PROGRAM_ACCESS_DECL__TYPE_NAME, oldTypeName, newTypeName);
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
  public void setTypeName(NonGenericType newTypeName)
  {
    if (newTypeName != typeName)
    {
      NotificationChain msgs = null;
      if (typeName != null)
        msgs = ((InternalEObject)typeName).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.PROGRAM_ACCESS_DECL__TYPE_NAME, null, msgs);
      if (newTypeName != null)
        msgs = ((InternalEObject)newTypeName).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.PROGRAM_ACCESS_DECL__TYPE_NAME, null, msgs);
      msgs = basicSetTypeName(newTypeName, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.PROGRAM_ACCESS_DECL__TYPE_NAME, newTypeName, newTypeName));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getDirection()
  {
    return direction;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setDirection(String newDirection)
  {
    String oldDirection = direction;
    direction = newDirection;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.PROGRAM_ACCESS_DECL__DIRECTION, oldDirection, direction));
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
      case IecPackage.PROGRAM_ACCESS_DECL__ACCESS_NAME:
        return basicSetAccessName(null, msgs);
      case IecPackage.PROGRAM_ACCESS_DECL__SYMBOLIC_VARIABLE:
        return basicSetSymbolicVariable(null, msgs);
      case IecPackage.PROGRAM_ACCESS_DECL__TYPE_NAME:
        return basicSetTypeName(null, msgs);
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
      case IecPackage.PROGRAM_ACCESS_DECL__ACCESS_NAME:
        return getAccessName();
      case IecPackage.PROGRAM_ACCESS_DECL__SYMBOLIC_VARIABLE:
        return getSymbolicVariable();
      case IecPackage.PROGRAM_ACCESS_DECL__TYPE_NAME:
        return getTypeName();
      case IecPackage.PROGRAM_ACCESS_DECL__DIRECTION:
        return getDirection();
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
      case IecPackage.PROGRAM_ACCESS_DECL__ACCESS_NAME:
        setAccessName((Variable)newValue);
        return;
      case IecPackage.PROGRAM_ACCESS_DECL__SYMBOLIC_VARIABLE:
        setSymbolicVariable((NamedVariableAccess)newValue);
        return;
      case IecPackage.PROGRAM_ACCESS_DECL__TYPE_NAME:
        setTypeName((NonGenericType)newValue);
        return;
      case IecPackage.PROGRAM_ACCESS_DECL__DIRECTION:
        setDirection((String)newValue);
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
      case IecPackage.PROGRAM_ACCESS_DECL__ACCESS_NAME:
        setAccessName((Variable)null);
        return;
      case IecPackage.PROGRAM_ACCESS_DECL__SYMBOLIC_VARIABLE:
        setSymbolicVariable((NamedVariableAccess)null);
        return;
      case IecPackage.PROGRAM_ACCESS_DECL__TYPE_NAME:
        setTypeName((NonGenericType)null);
        return;
      case IecPackage.PROGRAM_ACCESS_DECL__DIRECTION:
        setDirection(DIRECTION_EDEFAULT);
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
      case IecPackage.PROGRAM_ACCESS_DECL__ACCESS_NAME:
        return accessName != null;
      case IecPackage.PROGRAM_ACCESS_DECL__SYMBOLIC_VARIABLE:
        return symbolicVariable != null;
      case IecPackage.PROGRAM_ACCESS_DECL__TYPE_NAME:
        return typeName != null;
      case IecPackage.PROGRAM_ACCESS_DECL__DIRECTION:
        return DIRECTION_EDEFAULT == null ? direction != null : !DIRECTION_EDEFAULT.equals(direction);
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
    result.append(" (direction: ");
    result.append(direction);
    result.append(')');
    return result.toString();
  }

} //ProgramAccessDeclImpl
