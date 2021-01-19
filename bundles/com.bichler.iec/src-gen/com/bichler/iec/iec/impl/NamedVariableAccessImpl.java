/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.NamedVariableAccess;
import com.bichler.iec.iec.Variable;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Named Variable Access</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.NamedVariableAccessImpl#getNamedVariable <em>Named Variable</em>}</li>
 * </ul>
 *
 * @generated
 */
public class NamedVariableAccessImpl extends SymbolicVariableAccessImpl implements NamedVariableAccess
{
  /**
   * The cached value of the '{@link #getNamedVariable() <em>Named Variable</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getNamedVariable()
   * @generated
   * @ordered
   */
  protected Variable namedVariable;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected NamedVariableAccessImpl()
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
    return IecPackage.Literals.NAMED_VARIABLE_ACCESS;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Variable getNamedVariable()
  {
    if (namedVariable != null && namedVariable.eIsProxy())
    {
      InternalEObject oldNamedVariable = (InternalEObject)namedVariable;
      namedVariable = (Variable)eResolveProxy(oldNamedVariable);
      if (namedVariable != oldNamedVariable)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, IecPackage.NAMED_VARIABLE_ACCESS__NAMED_VARIABLE, oldNamedVariable, namedVariable));
      }
    }
    return namedVariable;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Variable basicGetNamedVariable()
  {
    return namedVariable;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setNamedVariable(Variable newNamedVariable)
  {
    Variable oldNamedVariable = namedVariable;
    namedVariable = newNamedVariable;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.NAMED_VARIABLE_ACCESS__NAMED_VARIABLE, oldNamedVariable, namedVariable));
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
      case IecPackage.NAMED_VARIABLE_ACCESS__NAMED_VARIABLE:
        if (resolve) return getNamedVariable();
        return basicGetNamedVariable();
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
      case IecPackage.NAMED_VARIABLE_ACCESS__NAMED_VARIABLE:
        setNamedVariable((Variable)newValue);
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
      case IecPackage.NAMED_VARIABLE_ACCESS__NAMED_VARIABLE:
        setNamedVariable((Variable)null);
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
      case IecPackage.NAMED_VARIABLE_ACCESS__NAMED_VARIABLE:
        return namedVariable != null;
    }
    return super.eIsSet(featureID);
  }

} //NamedVariableAccessImpl
