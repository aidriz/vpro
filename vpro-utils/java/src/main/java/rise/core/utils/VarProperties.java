package rise.core.utils;

import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VarProperties extends Properties {

  private static final long serialVersionUID = 1L;

  private static final Pattern varPattern = Pattern.compile("\\$\\{([^}]+)\\}");

  public VarProperties(Properties p) {
    super(p);
  }

  public String getProperty(String key) {
    String bare = super.getProperty(key);
    if (bare == null) return bare;
    Matcher m = varPattern.matcher(bare);
    StringBuffer sb = new StringBuffer();
    while(m.find()) {
      String propVar = m.group(1);
      String value = super.getProperty(propVar);
      if (value == null) {
        value = getProperty(propVar);
        if (value == null) value = "\\$\\{" + m.group(1) + "\\}";
      }
      m.appendReplacement(sb, value);
    }
    m.appendTail(sb);
    return sb.toString();
  }

  public Set<Entry<Object, Object>> entrySet() {
    Set<Entry<Object, Object>> orig = super.entrySet();
    for (Entry<Object, Object> e : orig) {
      String key = (String) e.getKey();
      e.setValue(getProperty(key));
    }
    return orig;
  }
}
