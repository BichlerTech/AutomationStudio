/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.ExpressionOperation;
import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.Operand;
import com.bichler.iec.iec.SimpleInstructionList;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Expression Operation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.ExpressionOperationImpl#getOperator <em>Operator</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.ExpressionOperationImpl#getOperand <em>Operand</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.ExpressionOperationImpl#getSimpleInstructionList <em>Simple Instruction List</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ExpressionOperationImpl extends OperationImpl implements ExpressionOperation
{
  /**
   * The default value of the '{@link #getOperator() <em>Operator</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOperator()
   * @generated
   * @ordered
   */
  protected static final String OPERATOR_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getOperator() <em>Operator</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOperator()
   * @generated
   * @ordered
   */
  protected String operator = OPERATOR_EDEFAULT;

  /**
   * The cached value of the '{@link #getOperand() <em>Operand</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOperand()
   * @generated
   * @ordered
   */
  protected Operand operand;

  /**
   * The cached value of the '{@link #getSimpleInstructionList() <em>Simple Instruction List</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getSimpleInstructionList()
   * @generated
   * @ordered
   */
  protected SimpleInstructionList simpleInstructionList;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ExpressionOperationImpl()
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
    return IecPackage.Literals.EXPRESSION_OPERATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getOperator()
  {
    return operator;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setOperator(String newOperator)
  {
    String oldOperator = operator;
    operator = newOperator;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.EXPRESSION_OPERATION__OPERATOR, oldOperator, operator));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Operand getOperand()
  {
    return operand;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetOperand(Operand newOperand, NotificationChain msgs)
  {
    Operand oldOperand = operand;
    operand = newOperand;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.EXPRESSION_OPERATION__OPERAND, oldOperand, newOperand);
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
  public void setOperand(Operand newOperand)
  {
    if (newOperand != operand)
    {
      NotificationChain msgs = null;
      if (operand != null)
        msgs = ((InternalEObject)operand).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.EXPRESSION_OPERATION__OPERAND, null, msgs);
      if (newOperand != null)
        msgs = ((InternalEObject)newOperand).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.EXPRESSION_OPERATION__OPERAND, null, msgs);
      msgs = basicSetOperand(newOperand, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.EXPRESSION_OPERATION__OPERAND, newOperand, newOperand));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public SimpleInstructionList getSimpleInstructionList()
  {
    return simpleInstructionList;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetSimpleInstructionList(SimpleInstructionList newSimpleInstructionList, NotificationChain msgs)
  {
    SimpleInstructionList oldSimpleInstructionList = simpleInstructionList;
    simpleInstructionList = newSimpleInstructionList;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.EXPRESSION_OPERATION__SIMPLE_INSTRUCTION_LIST, oldSimpleInstructionList, newSimpleInstructionList);
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
  public void setSimpleInstructionList(SimpleInstructionList newSimpleInstructionList)
  {
    if (newSimpleInstructionList != simpleInstructionList)
    {
      NotificationChain msgs = null;
      if (simpleInstructionList != null)
        msgs = ((InternalEObject)simpleInstructionList).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.EXPRESSION_OPERATION__SIMPLE_INSTRUCTION_LIST, null, msgs);
      if (newSimpleInstructionList != null)
        msgs = ((InternalEObject)newSimpleInstructionList).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.EXPRESSION_OPERATION__SIMPLE_INSTRUCTION_LIST, null, msgs);
      msgs = basicSetSimpleInstructionList(newSimpleInstructionList, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.EXPRESSION_OPERATION__SIMPLE_INSTRUCTION_LIST, newSimpleInstructionList, newSimpleInstructionList));
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
      case IecPackage.EXPRESSION_OPERATION__OPERAND:
        return basicSetOperand(null, msgs);
      case IecPackage.EXPRESSION_OPERATION__SIMPLE_INSTRUCTION_LIST:
        return basicSetSimpleInstructionList(null, msgs);
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
      case IecPackage.EXPRESSION_OPERATION__OPERATOR:
        return getOperator();
      case IecPackage.EXPRESSION_OPERATION__OPERAND:
        return getOperand();
      case IecPackage.EXPRESSION_OPERATION__SIMPLE_INSTRUCTION_LIST:
        return getSimpleInstructionList();
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
      case IecPackage.EXPRESSION_OPERATION__OPERATOR:
        setOperator((String)newValue);
        return;
      case IecPackage.EXPRESSION_OPERATION__OPERAND:
        setOperand((Operand)newValue);
        return;
      case IecPackage.EXPRESSION_OPERATION__SIMPLE_INSTRUCTION_LIST:
        setSimpleInstructionList((SimpleInstructionList)newValue);
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
      case IecPackage.EXPRESSION_OPERATION__OPERATOR:
        setOperator(OPERATOR_EDEFAULT);
        return;
      case IecPackage.EXPRESSION_OPERATION__OPERAND:
        setOperand((Operand)null);
        return;
      case IecPackage.EXPRESSION_OPERATION__SIMPLE_INSTRUCTION_LIST:
        setSimpleInstructionList((SimpleInstructionList)null);
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
      case IecPackage.EXPRESSION_OPERATION__OPERATOR:
        return OPERATOR_EDEFAULT == null ? operator != null : !OPERATOR_EDEFAULT.equals(operator);
      case IecPackage.EXPRESSION_OPERATION__OPERAND:
        return operand != null;
      case IecPackage.EXPRESSION_OPERATION__SIMPLE_INSTRUCTION_LIST:
        return simpleInstructionList != null;
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
    result.append(" (operator: ");
    result.append(operator);
    result.append(')');
    return result.toString();
  }

} //ExpressionOperationImpl
