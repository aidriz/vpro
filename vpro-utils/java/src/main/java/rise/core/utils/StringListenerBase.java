package rise.core.utils;

public class StringListenerBase implements StringListener {

  public void receive(String event, String message) {
    receive(event);
  };

  public void receive(String event) {};
}
