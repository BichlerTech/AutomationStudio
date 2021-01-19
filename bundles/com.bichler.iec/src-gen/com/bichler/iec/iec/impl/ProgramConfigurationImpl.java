/**
 */
package com.bichler.iec.iec.impl;

import com.bichler.iec.iec.IecPackage;
import com.bichler.iec.iec.ProgConfElements;
import com.bichler.iec.iec.ProgramConfiguration;
import com.bichler.iec.iec.ProgramDeclaration;
import com.bichler.iec.iec.TaskConfiguration;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Program Configuration</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link com.bichler.iec.iec.impl.ProgramConfigurationImpl#getName <em>Name</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.ProgramConfigurationImpl#getTask <em>Task</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.ProgramConfigurationImpl#getProg <em>Prog</em>}</li>
 *   <li>{@link com.bichler.iec.iec.impl.ProgramConfigurationImpl#getProgConf <em>Prog Conf</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ProgramConfigurationImpl extends MinimalEObjectImpl.Container implements ProgramConfiguration
{
  /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected static final String NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected String name = NAME_EDEFAULT;

  /**
   * The cached value of the '{@link #getTask() <em>Task</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getTask()
   * @generated
   * @ordered
   */
  protected TaskConfiguration task;

  /**
   * The cached value of the '{@link #getProg() <em>Prog</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProg()
   * @generated
   * @ordered
   */
  protected ProgramDeclaration prog;

  /**
   * The cached value of the '{@link #getProgConf() <em>Prog Conf</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getProgConf()
   * @generated
   * @ordered
   */
  protected ProgConfElements progConf;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ProgramConfigurationImpl()
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
    return IecPackage.Literals.PROGRAM_CONFIGURATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setName(String newName)
  {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.PROGRAM_CONFIGURATION__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public TaskConfiguration getTask()
  {
    if (task != null && task.eIsProxy())
    {
      InternalEObject oldTask = (InternalEObject)task;
      task = (TaskConfiguration)eResolveProxy(oldTask);
      if (task != oldTask)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, IecPackage.PROGRAM_CONFIGURATION__TASK, oldTask, task));
      }
    }
    return task;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public TaskConfiguration basicGetTask()
  {
    return task;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setTask(TaskConfiguration newTask)
  {
    TaskConfiguration oldTask = task;
    task = newTask;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.PROGRAM_CONFIGURATION__TASK, oldTask, task));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ProgramDeclaration getProg()
  {
    if (prog != null && prog.eIsProxy())
    {
      InternalEObject oldProg = (InternalEObject)prog;
      prog = (ProgramDeclaration)eResolveProxy(oldProg);
      if (prog != oldProg)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, IecPackage.PROGRAM_CONFIGURATION__PROG, oldProg, prog));
      }
    }
    return prog;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ProgramDeclaration basicGetProg()
  {
    return prog;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void setProg(ProgramDeclaration newProg)
  {
    ProgramDeclaration oldProg = prog;
    prog = newProg;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.PROGRAM_CONFIGURATION__PROG, oldProg, prog));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public ProgConfElements getProgConf()
  {
    return progConf;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetProgConf(ProgConfElements newProgConf, NotificationChain msgs)
  {
    ProgConfElements oldProgConf = progConf;
    progConf = newProgConf;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IecPackage.PROGRAM_CONFIGURATION__PROG_CONF, oldProgConf, newProgConf);
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
  public void setProgConf(ProgConfElements newProgConf)
  {
    if (newProgConf != progConf)
    {
      NotificationChain msgs = null;
      if (progConf != null)
        msgs = ((InternalEObject)progConf).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - IecPackage.PROGRAM_CONFIGURATION__PROG_CONF, null, msgs);
      if (newProgConf != null)
        msgs = ((InternalEObject)newProgConf).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - IecPackage.PROGRAM_CONFIGURATION__PROG_CONF, null, msgs);
      msgs = basicSetProgConf(newProgConf, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, IecPackage.PROGRAM_CONFIGURATION__PROG_CONF, newProgConf, newProgConf));
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
      case IecPackage.PROGRAM_CONFIGURATION__PROG_CONF:
        return basicSetProgConf(null, msgs);
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
      case IecPackage.PROGRAM_CONFIGURATION__NAME:
        return getName();
      case IecPackage.PROGRAM_CONFIGURATION__TASK:
        if (resolve) return getTask();
        return basicGetTask();
      case IecPackage.PROGRAM_CONFIGURATION__PROG:
        if (resolve) return getProg();
        return basicGetProg();
      case IecPackage.PROGRAM_CONFIGURATION__PROG_CONF:
        return getProgConf();
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
      case IecPackage.PROGRAM_CONFIGURATION__NAME:
        setName((String)newValue);
        return;
      case IecPackage.PROGRAM_CONFIGURATION__TASK:
        setTask((TaskConfiguration)newValue);
        return;
      case IecPackage.PROGRAM_CONFIGURATION__PROG:
        setProg((ProgramDeclaration)newValue);
        return;
      case IecPackage.PROGRAM_CONFIGURATION__PROG_CONF:
        setProgConf((ProgConfElements)newValue);
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
      case IecPackage.PROGRAM_CONFIGURATION__NAME:
        setName(NAME_EDEFAULT);
        return;
      case IecPackage.PROGRAM_CONFIGURATION__TASK:
        setTask((TaskConfiguration)null);
        return;
      case IecPackage.PROGRAM_CONFIGURATION__PROG:
        setProg((ProgramDeclaration)null);
        return;
      case IecPackage.PROGRAM_CONFIGURATION__PROG_CONF:
        setProgConf((ProgConfElements)null);
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
      case IecPackage.PROGRAM_CONFIGURATION__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case IecPackage.PROGRAM_CONFIGURATION__TASK:
        return task != null;
      case IecPackage.PROGRAM_CONFIGURATION__PROG:
        return prog != null;
      case IecPackage.PROGRAM_CONFIGURATION__PROG_CONF:
        return progConf != null;
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
    result.append(" (name: ");
    result.append(name);
    result.append(')');
    return result.toString();
  }

} //ProgramConfigurationImpl
