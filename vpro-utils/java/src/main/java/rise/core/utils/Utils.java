package rise.core.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Utils {
  public static final String ROOT_DIR_PROP = "root.dir";

  // do not create logger yet. First create and load the log.dir variable
  private static Logger logger = null;

  private static HashSet<String> reportedProperties = new HashSet<String>();

  /**
   * Return the value of the given java property
   */
  public static String getProperty(String propertyKey, String propertyMsg) {
    try {
      String propertyValue = System.getProperty(propertyKey);
      if (propertyValue == null)
        throw new IllegalArgumentException(propertyKey);
      if (!reportedProperties.contains(propertyKey)) {
        logger.info("getProperty(): " + propertyKey + "=" + propertyValue);
        reportedProperties.add(propertyKey);
      }
      return propertyValue;

    } catch (IllegalArgumentException ex) {
      logger.error("getProperty(): Please revise the property '" + propertyMsg
                   + "'" + ex);
      ex.printStackTrace();
      System.exit(0);
    }

    return null;
  }

  /**
   * Return the value of the given java property. Must be a valid int!
   */
  public static int getIntProperty(String propertyKey, String propertyMsg) {
    try {
      String propertyValue = System.getProperty(propertyKey);
      if (propertyValue == null)
        throw new IllegalArgumentException(propertyKey);
      int result = Integer.parseInt(propertyValue);
      // logger.info("getProperty(): " + propertyKey + "=" + propertyValue);
      return result;
    } catch (IllegalArgumentException ex) {
      logger.error("getIntProperty(): Please revise the property '"
                   + propertyMsg + "'" + ex);
      ex.printStackTrace();
      System.exit(0);
    }
    return -1;
  }

  /**
   * Return the value of the given java property. Must be a valid booloean!
   */
  public static boolean getBoolProperty(String propertyKey,
      String propertyMsg) {
    try {
      String propertyValue = System.getProperty(propertyKey);
      if (propertyValue == null)
        throw new IllegalArgumentException(propertyKey);
      boolean result = Boolean.parseBoolean(propertyValue);
      // logger.info("getProperty(): " + propertyKey + "=" + propertyValue);
      return result;
    } catch (IllegalArgumentException ex) {
      logger.error("getIntProperty(): Please revise the property '"
                   + propertyMsg + "'" + ex);
      ex.printStackTrace();
      System.exit(0);
    }
    return false;
  }

  /**
   * Return the value of the given java property. Must be a valid float!
   */
  public static float getFloatProperty(String propertyKey, String propertyMsg) {
    try {
      String propertyValue = System.getProperty(propertyKey);
      if (propertyValue == null)
        throw new IllegalArgumentException(propertyKey);
      float result = Float.parseFloat(propertyValue);
      // logger.info("getProperty(): " + propertyKey + "=" + propertyValue);
      return result;
    } catch (IllegalArgumentException ex) {
      logger.error("getIntProperty(): Please revise the property '"
                   + propertyMsg + "'" + ex);
      ex.printStackTrace();
      System.exit(0);
    }
    return -1F;
  }

  public static File getFile(String sub) {
    String root = getProperty(ROOT_DIR_PROP, "Root of application");
    if (new File(sub).isAbsolute())
      return new File(sub);
    return new File(root, sub);
  }

  /**
   * Return the value of the given java property, assuming the result should be
   * a file. TODO: should this check if it's a file or directory?
   */
  public static File getDirProperty(String propertyKey, String propertyMsg) {
    File f = getFile(getProperty(propertyKey, propertyMsg));
    /*
     * if (! (f.exists() && ((!dir && f.isFile()) || (dir && f.isDirectory()))))
     * { //logger.error(f + " does not exist or is not a " + (dir ? "directory"
     * : "file")); f = null; }
     */
    return f;
  }

  /** Read a properties file containing module specific global settings */
  public static void readGlobalProperties(String globalSettingsProp,
      String[] necessaryProperties) {
    // BasicConfigurator.configure();
    File propFile = getDirProperty(globalSettingsProp,
        "Property pointing to the global settings file");
    VarProperties properties = new VarProperties(System.getProperties());
    try {
      properties.load(new FileInputStream(propFile));
      for (Entry<Object, Object> entry : properties.entrySet()) {
        System.setProperty((String) entry.getKey(), (String) entry.getValue());
      }
      // BB commented out since it prevents logging from happening???
      // LogManager.resetConfiguration();
      PropertyConfigurator.configure(properties);
    } catch (IOException e) {
      String current = "";
      try {
        current = new java.io.File(".").getCanonicalPath();
      } catch (IOException e1) {
        e1.printStackTrace();
      }

      // logger.error("Can't load properties file " + propFile + ", exiting
      // ...");
      System.exit(2);
    }
    for (String k : necessaryProperties) {
      if (!properties.containsKey(k)) {
        logger.error("Property " + k + " not available, exiting ...");
        System.exit(2);
      }
    }
  }

  public static void saveDefaultSettings(final String filename,
      final String key, final String value) {

    File propFile = getDirProperty(filename,
        "Property pointing to the global settings file");
    Path filepath = FileSystems.getDefault().getPath(propFile.getPath());

    try {
      List<String> fileLines = Files.readAllLines(filepath,
          StandardCharsets.UTF_8);

      for (int i = 0; i < fileLines.size(); i++) {
        if (fileLines.get(i).contains(key)) {
          fileLines.remove(i);
          fileLines.add(i, key + "=" + value);
        }
        // System.out.println(fileLines.get(i));
      }

      Files.write(filepath, fileLines, StandardOpenOption.TRUNCATE_EXISTING);

    } catch (IOException e) {
      logger.error("Error during saving preferences to file " + filepath, e);
    }

  }

  /**
   * Return a file object that points to the wanted resource. Should facilitate
   * system-wide handling of resources in a uniform way.
   * 
   * @param moduleDir
   *          a path to the module subdirectory
   * @param subDir
   *          a subdirectory of the resource root folder
   * @param fileName
   *          the filename for the resource
   * @return
   */
  public static File getResource(String moduleDir, String subDir,
      String fileName) {
    return getResource(moduleDir, subDir + fileName);
  }

  /**
   * Return a file object that points to the wanted resource. Should facilitate
   * system-wide handling of resources in a uniform way.
   * 
   * @param moduleDir
   *          a path to the module subdirectory
   * @param subDir
   *          a subdirectory of the resource root folder
   * @param fileName
   *          the filename for the resource
   * @return
   */
  public static File getResource(String moduleDir, String filePath) {
    File modDir = getFile("modules");
    File resource = null;
    if (modDir.exists() && modDir.isDirectory()) {
      resource = new File(modDir,
          moduleDir + "src/main/resources/" + moduleDir + filePath);
    } else {
      resource = getFile(
          "target/" + moduleDir + "resources/" + moduleDir + filePath);
      if (!resource.exists()) { // possibly the top level module
        resource = getFile("target/resources/" + moduleDir + filePath);
      }
      if (!resource.exists()) {
        resource = getFile(filePath);
      }

    }
    return resource;
  }

  /** Same as getResource, only return null if file does not exist */
  public static File getResourceSafe(String moduleDir, String subDir,
      String fileName) {
    File resource = getResourceSafe(moduleDir, subDir, fileName);
    if (resource.exists() && resource.isFile() && resource.canRead())
      return resource;
    return null;
  }

  public static String adaptText(final String textIn, final String name,
      final String color, final String hobby) {
    // this needs somehow to be made multilangual???
    if (textIn.toLowerCase().contains("#naam#")) {
      String item = name;
      if (item.length() == 0) {
        item = "naam";
      }
      return textIn.replace("#naam#", item);
    }
    if (textIn.toLowerCase().contains("#sporten#")) {
      String item = hobby;
      if (item.length() == 0) {
        item = "activiteit";
      }
      return textIn.replace("#sporten#", item);
    }

    return textIn;
  }

  public static void loadEnvironment(final String[] necessaryProperties) {
    if (System.getProperty("root.dir") == null) {
      System.setProperty("root.dir", rise.core.utils.Constants.DEFAULT_ROOT_fOLDER);
    }

    createLogFolderSetProperty();

    File propfile = new File(
        System.getProperty("root.dir") + "share//log4j.properties");
    // System.out
    // .println(System.getProperty("root.dir") + "share//log4j.properties");
    // System.out.println(propfile.getAbsolutePath());
    try {
      PropertyConfigurator
          .configure(new FileInputStream(propfile.getAbsolutePath()));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    // now load the //logger
    logger = Logger.getLogger(Utils.class);

    if (System.getProperty(rise.core.utils.Constants.IP_ADDRESSES_FILE_NAME_PROP) == null) {
      System.setProperty(rise.core.utils.Constants.IP_ADDRESSES_FILE_NAME_PROP,
          rise.core.utils.Constants.DEFAULT_SHARE_IP_FILE);
    }
    // logger.info("loadEnvironment loaded");
    readGlobalProperties(rise.core.utils.Constants.IP_ADDRESSES_FILE_NAME_PROP, necessaryProperties);

  }

  private static void createLogFolderSetProperty() {

    File dataDir = new File(System.getProperty("root.dir"), "data_collection");
    dataDir.mkdirs();
    File persistentDir = new File(dataDir, "persistent/");
    persistentDir.mkdirs();
    System.setProperty(rise.core.utils.Constants.PERSISTENT_DIR_PROP, "data_collection/persistent/");

    Calendar c = new GregorianCalendar();
    String s = String.format("%1$tY%1$tm%1$td_%1$tH%1$tM%1$tS/", c);

    File logDir = new File(dataDir, s);
    logDir.mkdirs();
    String temp = logDir.getAbsolutePath() + "\\";
    temp = temp.replace("\\", "/");
    System.setProperty("log.dir", temp);
    System.out.println("Logging to " + System.getProperty("log.dir"));
  }

  /**
   * Scans all classes accessible from the context class loader which belong to
   * the given package and subpackages.
   *
   * @param packageName
   *          The base package
   * @return The classes
   * @throws ClassNotFoundException
   * @throws IOException
   */

  public static Class[] getClasses(String packageName)
      throws ClassNotFoundException, IOException {

    // logger.info("Utils getClasses");

    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    assert classLoader != null;
    String path = packageName.replace('.', '/');
    Enumeration<URL> resources = classLoader.getResources(path);
    ArrayList<Class> classes = new ArrayList<Class>();
    while (resources.hasMoreElements()) {
      URL resource = resources.nextElement();
      File resourceDir = new File(resource.getPath());
      // findClassesInJar(
      // "D:/MyProjects/PAL/PAL5/palProject/Runnables/libs/BehaviorManager-1.0-SNAPSHOT.jar",
      // packageName);
      // check if a directory is found otherwise use this to extract classes
      // from jar
      logger.info(resource.getPath());
      if (resourceDir.exists()) {
        classes.addAll(findClasses(resourceDir, packageName));
      } else {
        classes.addAll(findClassesInJar(resource.getPath(), packageName));
      }

    }
    return classes.toArray(new Class[classes.size()]);
  }

  /**
   * Recursive method used to find all classes in a given directory and subdirs.
   *
   * @param directory
   *          The base directory
   * @param packageName
   *          The package name for classes found inside the base directory
   * @return The classes
   * @throws ClassNotFoundException
   */
  @SuppressWarnings("rawtypes")
  private static ArrayList<Class> findClassesInJar(String directory,
      String packageName) throws ClassNotFoundException {

    ArrayList<Class> classes = new ArrayList<Class>();
    JarFile jarFile;
    String fileName = "";
    try {
      // remove packagename from full path and also the '!'
      fileName = directory.replace(packageName.replace('.', '/'), "")
          .replace("file:/", "");
      fileName = fileName.replace("!", "");
      jarFile = new JarFile(fileName);
      // "D:/MyProjects/PAL/PAL5/palProject/Runnables/libs/BehaviorManager-1.0-SNAPSHOT.jar");
      Enumeration allEntries = jarFile.entries();
      while (allEntries.hasMoreElements()) {
        JarEntry entry = (JarEntry) allEntries.nextElement();
        String name = entry.getName().replace("/", ".");
        if (name.contains(packageName)) {
          if (name.endsWith(".class")) {
            classes.add(Class.forName(name.substring(0, name.length() - 6)));
          }
        }
      }
    } catch (IOException e) {
      logger.error("jar " + fileName);
      logger.error(
          "Error while reading classes from jar file \n" + e.getMessage());
    }
    return classes;
  }

  /**
   * Recursive method used to find all classes in a given directory and subdirs.
   *
   * @param directory
   *          The base directory
   * @param packageName
   *          The package name for classes found inside the base directory
   * @return The classes
   * @throws ClassNotFoundException
   */

  private static ArrayList<Class> findClasses(File directory,
      String packageName) throws ClassNotFoundException {
    ArrayList<Class> classes = new ArrayList<Class>();
    if (!directory.exists()) {
      logger.info("directory not exists " + directory.getAbsolutePath());
      return classes;
    }
    File[] files = directory.listFiles();
    for (File file : files) {
      if (file.isDirectory()) {
        assert !file.getName().contains(".");
        classes.addAll(findClasses(file, packageName + "." + file.getName()));
      } else if (file.getName().endsWith(".class")) {
        classes.add(Class.forName(packageName + '.' + file.getName()
            .substring(0, file.getName().length() - 6)));
      }
    }
    return classes;
  }

}
