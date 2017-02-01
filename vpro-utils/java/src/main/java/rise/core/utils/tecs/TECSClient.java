package rise.core.utils.tecs;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.thrift.TBase;

import de.dfki.tecs.Event;
import de.dfki.tecs.ps.PSClient;

public class TECSClient {
  private static final String eventPackageName = "rise.core.utils.tecs";

  protected PSClient _currentClient = null;

  private String _clientName;
  // a list of handlers
  @SuppressWarnings("rawtypes")
  protected Map<String, List<EventHandler<? extends TBase>>> subscribedEvents;

  @SuppressWarnings("rawtypes")
      public TECSClient(final String hostIP, final String clientName, int port) {
    _clientName = clientName;
    _currentClient = new PSClient(clientName, hostIP, port);
    subscribedEvents = new HashMap<String, List<EventHandler<? extends TBase>>>();
  }

@SuppressWarnings("rawtypes")
      public TECSClient(final String hostIP, final String clientName) {
    _clientName = clientName;
    _currentClient = new PSClient(clientName, hostIP, 9000);
    subscribedEvents = new HashMap<String, List<EventHandler<? extends TBase>>>();
  }

  /** Subscribe to an event time, and add the handler */
  @SuppressWarnings("rawtypes")
  public void subscribe(String eventType,
      EventHandler<? extends TBase> handler) {
    List<EventHandler<? extends TBase>> handlers = subscribedEvents
        .get(eventType);
    if (handlers == null) {
      handlers = new ArrayList<EventHandler<? extends TBase>>();
      subscribedEvents.put(eventType, handlers);
    }
    handlers.add(handler);
  }

  /** Common event handling for all agents */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  protected void onEvent(Event event) {

    String eventType = event.getEtype();
    if (subscribedEvents.containsKey(eventType)) {
      Object o;
      try {
        String className = eventPackageName + "." + eventType;
        o = Class.forName(className).getConstructor().newInstance();
      } catch (InstantiationException | IllegalAccessException
          | IllegalArgumentException | InvocationTargetException
          | NoSuchMethodException | SecurityException
          | ClassNotFoundException e) {
        e.printStackTrace();
        return;
      }
      event.parseData((TBase) o);
      // do not handle message send by yourself
      if (!event.getSource().equals(_clientName)) {
        // System.out.println(
        // "source " + event.getHeader().source + ", I am " + _clientName);
        for (rise.core.utils.tecs.EventHandler h : subscribedEvents.get(eventType)) {
          h.handleEvent((TBase) o);
        }
      }
    }
  }

  /*
   * SEND part. Message to be send can originate from different threads. But the
   * sending is not "thread-safe" So each message to be send is put into a queue
   * And there a thread is started to do the actual sending
   */
  @SuppressWarnings("rawtypes")
  public void send(TBase event) {
    send(".*", event);
  }

  @SuppressWarnings("rawtypes")
  public void send(final String toWhom, final TBase toSend) {
    synchronized (itemsToSendLock) {
      rise.core.utils.tecs.MessageContainer mc = new rise.core.utils.tecs.MessageContainer(toWhom,
          toSend);
      itemsToSend.add(mc);
    }
  }

  // queue to hold messages to be send
  protected LinkedList<MessageContainer> itemsToSend = new LinkedList<MessageContainer>();
  // to prevent "multi-use" of the queue
  private Object itemsToSendLock = new Object();

  protected void sendThis(MessageContainer event) {
    String name = event.getMessage().getClass().getSimpleName();
    _currentClient.send(event.getToWhom(), name, event.getMessage());
  }

  public void startListening() {

    for (String event : subscribedEvents.keySet()) {
      _currentClient.subscribe(event);
    }
    _currentClient.connect();

    Thread listenToClient = new Thread() {
      @Override
      public void run() {
        while (_currentClient.isConnected()) {
          boolean emptyRun = true;
          while (_currentClient.canRecv()) {
            Event event = _currentClient.recv();
            onEvent(event);
            emptyRun = false;
          }
          // read and write should be on the same thread, so do that here
          MessageContainer input = null;
          synchronized (itemsToSendLock) {
            if (!itemsToSend.isEmpty()) {
              input = itemsToSend.getFirst();
              itemsToSend.removeFirst();
              emptyRun = false;
            }
          }
          if (input != null) {
            sendThis(input);
          }
          if (emptyRun) {
            try {
              Thread.sleep(100);
            } catch (InterruptedException ex) {
              _currentClient.disconnect();
            }
          }
        }
      }
    };
    listenToClient.start();
  }

  public void disconnect() {
    if (_currentClient != null) {
      //if (_currentClient.isConnected()) {
        _currentClient.disconnect();
      //}
    }
  }

}
