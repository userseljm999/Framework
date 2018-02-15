/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
* COPYRIGHT 2016.  ALL RIGHTS RESERVED.  THIS MODULE CONTAINS
* CHARTER COMMUNICATIONS CONFIDENTIAL AND PROPRIETARY INFORMATION.
* THE INFORMATION CONTAINED HEREIN IS GOVERNED BY LICENSE AND
* SHALL NOT BE DISTRIBUTED OR COPIED WITHOUT WRITTEN PERMISSION
* FROM CHARTER COMMUNICATIONS.
*
* Author:  Nagaraju Meka
* File:    Token.java
* Created: Dec 06, 2016
*
* Description:
*
*
*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

package com.chtr.tmoauto.util.expect;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Token
{
   public enum Type
   {
    VAR,
    OPERATOR,
    LITERAL,
    AND,
    CRUFF
   }
   private static Logger log = LoggerFactory.getLogger(Token.class);
   private final String originalString;
   private Type type;
   private Object value;
   private boolean missingValue = false;


   public Token(String str)
   {
      this.originalString = str;
      if (isCurlyBracketed(str))
      {
         this.type = Type.VAR;
         this.missingValue = true;
      }
      else if (isLiteral(str))
      {
         log.debug("literal string {}:", str);
         this.type = Type.LITERAL;
         this.value = Utils.literalValue(str);
      }
      else if (isConjunction(str))
      {
         this.type = Type.AND;
      }
      else if (Operator.isOperator(str))
      {
         this.type = Type.OPERATOR;
         this.value = Operator.forSymbol(str);
      }
      else
      {
         log.debug("determined to be cruff: {}", str);
         this.type = Type.CRUFF;
      }
   }


   public Object getValue()
   {
      if (type == Type.VAR && this.missingValue)
      {
         throw new RuntimeException("A value was never assigned to this var");
      }
      return value;
   }


   public void setValue(Object value)
   {
      this.value = value;
      this.missingValue = false;
   }


   public String getOriginalString()
   {
      return originalString;
   }


   public Type getType()
   {
      return type;
   }


   @Override
   public String toString()
   {
      return originalString;
   }


   public static boolean isConjunction(String str)
   {
      switch (str.trim().toLowerCase())
      {
         case "and":
         case ",":
         case "&":
         case "&&":
            return true;
         default:
            return false;
      }
   }


   public static boolean isCurlyBracketed(String str)
   {
      return str.startsWith("{") && str.endsWith("}");
   }


   public static boolean isLiteral(String str)
   {
      return str.startsWith("[") && str.endsWith("]");
   }
}