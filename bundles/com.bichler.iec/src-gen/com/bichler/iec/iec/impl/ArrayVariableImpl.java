/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.ArrayVariable;
import com.bichler.iec.iec.Expression;
import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.Variable;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Array Variable</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.ArrayVariableImpl#getSubscriptedVariable <em>Subscripted Variable</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.ArrayVariableImpl#getSubscripts <em>Subscripts</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ArrayVariableImpl extends MultiElementVariableImpl implements ArrayVariable
{
  /**
   * The cached value of the '{@link #getSubscriptedVariable() <em>Subscripted Variable</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSubscriptedVariable()
   * @generated
   * @ordered
   */
  protected Variable subscriptedVariable;

  /**
   * The cached value of the '{@link #getSubscripts() <em>Subscripts</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSubscripts()
   * @generated
   * @ordered
   */
  protected EList<Expression> subscripts;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ArrayVariableImpl()
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
    return IecPackage.Literals.ARRAY_VARIABLE;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Variable getSubscriptedVariable()
  {
    if (subscriptedVariable != null && subscriptedVariable.eIsProxy())
    {
      InternalEObject oldSubscriptedVariable = (InternalEObject)subscriptedVariable;
      subscriptedVariable = (Variable)eResolveProxy(oldSubscriptedVariable);
      if (subscriptedVariable != oldSubscriptedVariable)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, IecPackage.ARRAY_VARIABLE__SUBSCRIPTED_VARIABLE, oldSubscriptedVariable, subscriptedVariable));
      }
    }
    return subscriptedVariable;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Variable basicGetSubscriptedVariable()
  {
    return subscriptedVariable;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setSubscriptedVariable(Variable newSubscriptedVariable)
  {
    Variable oldSubscriptedVariable = subscriptedVariable;
    subscriptedVariable = newSubscriptedVariable;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.ARRAY_VARIABLE__SUBSCRIPTED_VARIABLE, oldSubscriptedVariable, subscriptedVariable));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public EList<Expression> getSubscripts()
  {
    if (subscripts == null)
    {
      subscripts = new EObjectContainmentEList<Expression>(Expression.class, this, IecPackage.ARRAY_VARIABLE__SUBSCRIPTS);
    }
    return subscripts;
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
      case IecPackage.ARRAY_VARIABLE__SUBSCRIPTS:
        return ((InternalEList<?>)getSubscripts()).basicRemove(otherEnd, msgs);
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
      case IecPackage.ARRAY_VARIABLE__SUBSCRIPTED_VARIABLE:
        if (resolve) return getSubscriptedVariable();
        return basicGetSubscriptedVariable();
      case IecPackage.ARRAY_VARIABLE__SUBSCRIPTS:
        return getSubscripts();
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
      case IecPackage.ARRAY_VARIABLE__SUBSCRIPTED_VARIABLE:
        setSubscriptedVariable((Variable)newValue);
        return;
      case IecPackage.ARRAY_VARIABLE__SUBSCRIPTS:
        getSubscripts().clear();
        getSubscripts().addAll((Collection<? extends Expression>)newValue);
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
      case IecPackage.ARRAY_VARIABLE__SUBSCRIPTED_VARIABLE:
        setSubscriptedVariable((Variable)null);
        return;
      case IecPackage.ARRAY_VARIABLE__SUBSCRIPTS:
        getSubscripts().clear();
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
      case IecPackage.ARRAY_VARIABLE__SUBSCRIPTED_VARIABLE:
        return subscriptedVariable != null;
      case IecPackage.ARRAY_VARIABLE__SUBSCRIPTS:
        return subscripts != null && !subscripts.isEmpty();
    }
    return super.eIsSet(featureID);
  }

} //ArrayVariableImpl
