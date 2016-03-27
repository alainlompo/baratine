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

package com.caucho.v5.amp.stub;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;
import java.util.List;

import com.caucho.v5.amp.ServiceRefAmp;
import com.caucho.v5.amp.deliver.QueueDeliver;
import com.caucho.v5.amp.journal.JournalAmp;
import com.caucho.v5.amp.spi.HeadersAmp;
import com.caucho.v5.amp.spi.InboxAmp;
import com.caucho.v5.amp.spi.LoadState;
import com.caucho.v5.amp.spi.LoadStateNull;
import com.caucho.v5.amp.spi.MessageAmp;
import com.caucho.v5.amp.spi.ShutdownModeAmp;
import com.caucho.v5.inject.type.AnnotatedTypeClass;
import com.caucho.v5.util.L10N;

import io.baratine.service.Result;
import io.baratine.service.ServiceExceptionMethodNotFound;

/**
 * Abstract stream for an actor.
 */
public class StubAmpBase implements StubAmp
{
  private static final L10N L = new L10N(StubAmpBase.class);
  
  private static final AnnotatedType _annotatedTypeObject
    = new AnnotatedTypeClass(Object.class);
  
  private LoadState _loadState;
  
  protected StubAmpBase()
  {
    initLoadState();
  }
  
  public void initLoadState()
  {
    _loadState = createLoadState();
  }
  
  @Override
  public LoadState loadState()
  {
    return _loadState;
  }
  
  public LoadState createLoadState()
  {
    return LoadStateLoad.LOAD;
  }
  
  @Override
  public String name()
  {
    AnnotatedType annType = api();
    Type type = annType.getType();
    
    if (type instanceof Class) {
      Class<?> cl = (Class<?>) type;
      
      return "anon:" + cl.getSimpleName();
    }
    else {
      return "anon:" + type;
    }
  }
  
  @Override
  public boolean isUp()
  {
    return ! isClosed();
  }
  
  @Override
  public boolean isClosed()
  {
    return false;
  }
  
  @Override
  public boolean isPublic()
  {
    return false;
  }
  
  @Override
  public AnnotatedType api()
  {
    return _annotatedTypeObject;
  }
  
  @Override
  public Object bean()
  {
    throw new UnsupportedOperationException(getClass().getName());
  }
  
  @Override
  public Object loadBean()
  {
    return bean();
  }
  
  @Override
  public Object onLookup(String path, ServiceRefAmp parentRef)
  {
    return null;
  }
  
  @Override
  public MethodAmp []getMethods()
  {
    return new MethodAmp[0];
  }
  
  @Override
  public MethodAmp getMethod(String methodName)
  {
    throw new UnsupportedOperationException(getClass().getName());
  }
  
  @Override
  public JournalAmp journal()
  {
    return null;
  }

  @Override
  public void journal(JournalAmp journal)
  {
  }
  
  @Override
  public String journalKey()
  {
    return null;
  }
  
  /*
  @Override
  public boolean requestCheckpoint()
  {
    return false;
  }
  */

  /*
  @Override
  public void onModify()
  {
  }
  */
  
  @Override
  public void queryReply(HeadersAmp headers, 
                         StubAmp actor,
                         long qid, 
                         Object value)
  {
  }
  
  @Override
  public void queryError(HeadersAmp headers, 
                         StubAmp actor,
                         long qid, 
                         Throwable exn)
  {
  }
  
  @Override
  public void streamReply(HeadersAmp headers, 
                          StubAmp actor,
                          long qid,
                          int sequence,
                          List<Object> values,
                          Throwable exn,
                          boolean isComplete)
  {
    System.out.println("STR: " + values + " " + isComplete + " " + this);
  }

  @Override
  public StubAmp worker(StubAmp actorMessage)
  {
    return actorMessage;
  }
  
  @Override
  public LoadState load(StubAmp actorMessage, MessageAmp msg)
  {
    return actorMessage.loadState().load(actorMessage, 
                                         msg.inboxTarget(), 
                                         msg);
  }
  
  @Override
  public LoadState load(MessageAmp msg)
  {
    return _loadState.load(this, msg.inboxTarget(), msg);
  }
  
  // @Override
  public LoadState load(InboxAmp inbox, MessageAmp msg)
  {
    return _loadState.load(this, inbox, msg);
  }
  
  @Override
  public LoadState loadReplay(InboxAmp inbox, MessageAmp msg)
  {
    return _loadState.loadReplay(this, inbox, msg);
  }
  
  @Override
  public void onModify()
  {
    loadState().onModify(this);
  }
  
  @Override
  public boolean onSave(Result<Boolean> result)
  {
    SaveResult saveResult = new SaveResult(result);
    
    loadState().onSave(this, saveResult);
    
    saveResult.completeBean();
    
    return true;
  }
  
  public boolean onSaveStartImpl(Result<Boolean> cont)
  {
    cont.ok(true);
    
    return true;
  }
  
  public void setLoadState(LoadState loadState)
  {
    _loadState = loadState;
    if (loadState instanceof LoadStateNull) {
      System.out.println("LSN: " + this);
    }
  }
  
  //
  // stream (map/reduce)
  //
  
  /*
  @Override
  public <T,R> void stream(MethodAmp method,
                           HeadersAmp headers,
                           QueryRefAmp queryRef,
                           CollectorAmp<T,R> stream,
                           Object[] args)
  {
    method.stream(headers, queryRef, this, stream, args);
  }
  */
  
  @Override
  public void beforeBatch()
  {
  }
  
  @Override
  public void beforeBatchImpl()
  {
  }
  
  @Override
  public void afterBatch()
  {
  }

  /*
  @Override
  public QueueService<MessageAmp> buildQueue(QueueServiceBuilder<MessageAmp> queueBuilder,
                                              InboxQueue queueMailbox)
  {
    throw new UnsupportedOperationException(getClass().getName());
  }
  */
  
  @Override
  public boolean isLifecycleAware()
  {
    return false;
  }
  
  @Override
  public boolean isStarted()
  {
    return loadState().isActive();
  }
  
  @Override
  public void replay(InboxAmp mailbox,
                     QueueDeliver<MessageAmp> queue,
                     Result<Boolean> cont)
  {
  }
  
  /*
  @Override
  public void afterReplay()
  {
  }
  */
  
  @Override
  public void onInit(Result<? super Boolean> result)
  {
    if (result != null) {
      result.ok(true);
    }
  }
  
  @Override
  public void onActive(Result<? super Boolean> result)
  {
    result.ok(true);
  }
  
  /*
  @Override
  public boolean checkpointStart(Result<Boolean> result)
  {
    result.complete(true);
    
    return true;
  }
  */
  
  @Override
  public void onSaveEnd(boolean isValid)
  {
  }
  
  @Override
  public void onShutdown(ShutdownModeAmp mode)
  {
  }
  
  public String toString()
  {
    return getClass().getSimpleName() + "[" + name() + "]";
  }
  
  static class MethodBase extends MethodAmpBase {
    public MethodBase(String methodName)
    {
    }

    @Override
    public void send(HeadersAmp headers,
                     StubAmp actor,
                     Object []args)
    {
    }

    @Override
    public void query(HeadersAmp headers,
                      Result<?> result,
                      StubAmp actor,
                      Object []args)
    {
      result.fail(new ServiceExceptionMethodNotFound(
                                       L.l("'{0}' is an undefined method for {1}",
                                           this, actor)));
    }
  }
  
  private static class LoadStateLoad implements LoadState {
    private static final LoadStateLoad LOAD = new LoadStateLoad();
    
    @Override
    public LoadState load(StubAmp actor,
                          InboxAmp inbox,
                          MessageAmp msg)
    {
      return this;
    }
    
    @Override
    public LoadState loadReplay(StubAmp actor,
                                InboxAmp inbox,
                                MessageAmp msg)
    {
      return this;
    }

    @Override
    public void send(StubAmp actorDeliver,
                     StubAmp actorMessage,
                      MethodAmp method, 
                      HeadersAmp headers,
                      Object[] args)
    {
      method.send(headers, actorDeliver.worker(actorMessage), args);
    }

    @Override
    public void query(StubAmp actorDeliver,
                      StubAmp actorMessage,
                      MethodAmp method, 
                      HeadersAmp headers,
                      Result<?> result, 
                      Object[] args)
    {
      method.query(headers, result, actorDeliver.worker(actorMessage), args);
    }

    @Override
    public void onModify(StubAmp actorAmpBase)
    {
      // TODO Auto-generated method stub
      
    }
  }
}
