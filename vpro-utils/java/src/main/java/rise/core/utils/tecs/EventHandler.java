package rise.core.utils.tecs;

import org.apache.thrift.TBase;

@SuppressWarnings("rawtypes")
public interface EventHandler<T extends TBase> {
  public void handleEvent(T o);
}
