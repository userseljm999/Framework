/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
* COPYRIGHT 2016.  ALL RIGHTS RESERVED.  THIS MODULE CONTAINS
* CHARTER COMMUNICATIONS CONFIDENTIAL AND PROPRIETARY INFORMATION.
* THE INFORMATION CONTAINED HEREIN IS GOVERNED BY LICENSE AND
* SHALL NOT BE DISTRIBUTED OR COPIED WITHOUT WRITTEN PERMISSION
* FROM CHARTER COMMUNICATIONS.
*
* Author:  Danny Byers
* File:    Requirement.java
* Created: Dec 08, 2016
*
* Description:
*
*
*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

package com.chtr.tmoauto.system.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * This annotation links a test case to a Business requirement.
 * @author P2193524
 *
 */
@Target({ ElementType.METHOD })
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Requirement
{
   String value();
}