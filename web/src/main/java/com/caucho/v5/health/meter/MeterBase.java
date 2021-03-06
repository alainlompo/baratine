/*
 * Copyright (c) 1998-2015 Caucho Technology -- all rights reserved
 *
 * This file is part of Baratine(TM)
 *
 * Each copy or derived work must preserve the copyright notice and this
 * notice unmodified.
 *
 * Baratine is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2
 * as published by the Free Software Foundation.
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

package com.caucho.v5.health.meter;

abstract public class MeterBase implements Meter {
  private final String _name;

  protected MeterBase(String name)
  {
    _name = name;
  }

  /**
   * Returns the meter's name.
   */
  @Override
  public final String getName()
  {
    return _name;
  }
  
  /**
   * Sample the meter, resetting any counters for the next sample.
   */
  @Override
  abstract public void sample();
  
  /**
   * Calculate the current value based on the previous sample().
   */
  @Override
  abstract public double calculate();

  /**
   * Returns the current value.
   */
  @Override
  public double peek()
  {
    return 0;
  }

  @Override
  public String toString()
  {
    return getClass().getSimpleName() + "[" + _name + "]";
  }
}
