/*
 * Copyright (c) 1998-2015 Caucho Technology -- all rights reserved
 *
 * This file is part of Baratine(TM)
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

package com.caucho.v5.amp.inbox;

import com.caucho.v5.amp.deliver.MessageDeliver;
import com.caucho.v5.amp.spi.InboxAmp;
import com.caucho.v5.amp.spi.MessageAmp;
import com.caucho.v5.amp.spi.OutboxAmp;

/**
 * Thread context for ramp events.
 */
public class OutboxAmpNull implements OutboxAmp
{
  public static final OutboxAmpNull NULL = new OutboxAmpNull();
  
  @Override
  public InboxAmp inbox()
  {
    return null;
  }

  @Override
  public MessageAmp message()
  {
    return null;
  }

  @Override
  public void inbox(InboxAmp inbox)
  {
  }

  @Override
  public void message(MessageAmp message)
  {
  }
  
  @Override
  public void offer(MessageDeliver<?> msg)
  {
    throw new UnsupportedOperationException(getClass().getName());
  }

  @Override
  public void flush()
  {
  }

  /*
  @Override
  public MessageAmp flushAfterTask()
  {
    // TODO Auto-generated method stub
    return null;
  }
  */

  @Override
  public void close()
  {
    // TODO Auto-generated method stub
    
  }

  @Override
  public boolean flushAndExecuteLast()
  {
    // TODO Auto-generated method stub
    return false;
  }

}
