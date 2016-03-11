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

package com.caucho.v5.amp.outbox;

import com.caucho.v5.amp.spi.ShutdownModeAmp;

/**
 * Thread worker controller for a queue/inbox.
 */
public interface WorkerOutbox<M extends MessageOutbox<M>>
{
  /**
   * Wake the worker
   * 
   * @return true if the worker was newly woken.
   */
  boolean wake();
  
  /*
  default C context()
  {
    return null;
  }
  */
  
  /**
   * Executes the message in the target worker's context, returning the
   * new tail message.
   */
  default void runAs(Outbox outbox, M tailMsg)
  {
    tailMsg.offerQueue(0);
    outbox.flush();
    wake();
  }
  
  /**
   * Executes the message in the target worker's context, returning the
   * new tail message.
   */
  default boolean runOne(Outbox outbox, M tailMsg)
  {
    tailMsg.offerQueue(0);
    outbox.flush();
    wake();
    
    return false;
  }
  
  default void onInit()
  {
  }
  
  
  default void onActive()
  {
  }

  default void shutdown(ShutdownModeAmp mode)
  {
  }
  
  default void wakeAll()
  {
    wake();
  }
  
  default void wakeAllAndWait()
  {
    wakeAll();
  }
}