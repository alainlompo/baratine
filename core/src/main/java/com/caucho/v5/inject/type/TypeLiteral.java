/*
 * Copyright (c) 1998-2015 Caucho Technology -- all rights reserved
 *
 * This file is part of Baratine(TM)(TM)
 *
 * Each copy or derived work must preserve the copyright notice and this
 * notice unmodified.
 *
 * Baratine is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * Baratine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE, or any warranty
 * of NON-INFRINGEMENT.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Baratine; if not, write to the
 *
 *   Free Software Foundation, Inc.
 *   59 Temple Place, Suite 330
 *   Boston, MA 02111-1307  USA
 *
 * @author Scott Ferguson
 */

package com.caucho.v5.inject.type;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Objects;


/**
 * Utility class for creating Type annotations, used for generic
 * resources like DataSources.
 */
public class TypeLiteral<T>
{
  private Type _type;
  
  protected TypeLiteral()
  {
    _type = calculateType();
  }
  
  private TypeLiteral(Type type)
  {
    Objects.requireNonNull(type);
    
    _type = type;
  }
  
  public static <T> TypeLiteral<T> of(Method method)
  {
    return new TypeLiteral(method.getGenericReturnType());
  }
  
  public Class<? super T> getRawClass()
  {
    Type type = getType();
    
    if (type instanceof Class) {
      return (Class) type;
    }
    else if (type instanceof ParameterizedType) {
      ParameterizedType pType = (ParameterizedType) type;
      
      return (Class) pType.getRawType();
    }
    else {
      throw new UnsupportedOperationException(type + " " + type.getClass().getName());
    }
  }
  
  public Type getType()
  {
    return _type;
  }
    
  private Type calculateType()
  {
    Type type = getClass().getGenericSuperclass();

    if (type instanceof Class<?>) {
      return type;
    }
    else if (type instanceof ParameterizedType) {
      ParameterizedType pType = (ParameterizedType) type;
      
      return pType.getActualTypeArguments()[0];
    }
    else {
      throw new UnsupportedOperationException(type + " " + type.getClass().getName());
    }
  }
  
  @Override
  public String toString()
  {
    return getClass().getSimpleName() + "[" + getType() + "]";
  }
}
