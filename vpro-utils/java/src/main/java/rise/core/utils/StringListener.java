package rise.core.utils;

public interface StringListener {

  public void receive(String event, String message);

  public void receive(String event);
}
