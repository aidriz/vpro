package rise.core.utils;

public interface ObjectListener {

  public void receive(Object event, String message);

  public void receive(Object event);

}
