/*=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
* COPYRIGHT 2016.  ALL RIGHTS RESERVED.  THIS MODULE CONTAINS
* CHARTER COMMUNICATIONS CONFIDENTIAL AND PROPRIETARY INFORMATION.
* THE INFORMATION CONTAINED HEREIN IS GOVERNED BY LICENSE AND
* SHALL NOT BE DISTRIBUTED OR COPIED WITHOUT WRITTEN PERMISSION
* FROM CHARTER COMMUNICATIONS.
*
* Author:  Nagaraju Meka
* File:    Comparison.java
* Created: Dec 06, 2016
*
* Description:
*
*
*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-*/

package com.chtr.tmoauto.util.expect;

import java.util.Collection;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.StringJoiner;

class Comparison
{
   private Object left;
   private boolean leftSet = false;
   private Operator operator;
   private Object right;
   private boolean rightSet = false;


   public static Comparison fromTokenList(List<Token> tokens)
   {
      Comparison comparison = new Comparison();
      Deque<Operator> opsStack = new LinkedList<>();
      Deque<Object> valStack = new LinkedList<>();
      tokens.forEach(t -> {
         switch (t.getType())
         {
            case VAR:
            case LITERAL:
               valStack.push(t.getValue());
               break;
            case OPERATOR:
               opsStack.push((Operator)t.getValue());
               break;
            case AND:
               throw new RuntimeException("AND token found in list, something went wrong with parsing");
            case CRUFF:
            default:
               /* no-op */
               break;
         }
      });
      if ( !valStack.isEmpty())
      {
         comparison.setRight(valStack.pop());
      }
      if ( !opsStack.isEmpty())
      {
         comparison.setOperator(opsStack.pop());
      }
      else
      {
         comparison.setOperator(Operator.EQUALS);
      }
      if ( !valStack.isEmpty())
      {
         comparison.setLeft(valStack.pop());
      }
      return comparison;
   }


   public Comparison()
   {
      super();
   }


   public Object getLeft()
   {
      return left;
   }


   public void setLeft(Object value)
   {
      this.left = value;
      this.leftSet = true;
   }


   public boolean isLeftSet()
   {
      return leftSet;
   }


   public Operator getOperator()
   {
      return operator;
   }


   public void setOperator(Operator operator)
   {
      this.operator = operator;
   }


   public Object getRight()
   {
      return right;
   }


   public void setRight(Object expectedResult)
   {
      this.right = expectedResult;
      this.rightSet = true;
   }


   public boolean evaluate()
   {
      return operator.evaluate(left, right);
   }


   public boolean isValid()
   {
      return leftSet && operator != null && rightSet;
   }


   public void validate()
   {
      if ( !isValid())
      {
         StringJoiner sj = new StringJoiner(", ");
         if ( !leftSet)
         {
            sj.add("left side was not set");
         }
         if (operator == null)
         {
            sj.add("operator was not set");
         }
         if ( !rightSet)
         {
            sj.add("right side was not set");
         }
         throw new RuntimeException("Invalid comparison: " + sj.toString());
      }
   }


   @Override
   public String toString()
   {
      StringBuilder sb = new StringBuilder();
      sb.append("(");
      if (left instanceof Collection)
      {
         sb.append(Utils.collectionString((Collection< ? >)left));
      }
      else if (left instanceof String)
      {
         sb.append("\"").append(left).append("\"");
      }
      else
      {
         sb.append(left);
      }
      sb.append(" ").append(operator.symbols.get(0)).append(" ");
      if (right instanceof Collection)
      {
         sb.append(Utils.collectionString((Collection< ? >)right));
      }
      else if (right instanceof String)
      {
         sb.append("\"").append(right).append("\"");
      }
      else
      {
         sb.append(right);
      }
      sb.append(")");
      if (evaluate())
      {
         return sb.toString();
      }
      else
      {
         return "[! " + sb.toString() + " !]";
      }
   }
}