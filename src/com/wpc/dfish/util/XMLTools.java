package com.wpc.dfish.util;

import java.io.*;
import java.util.*;

import org.dom4j.*;
import org.dom4j.io.*;

/**
 * <p>Title: 任务管理系统</p>
 * <p>Description:传入xml文件绝对路径，实现取得各变量值 </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: rjsoft</p>
 * XMLTools 用于读取写入 XML，带缓存
 * @author itask team,刘云辉v1.0 邱元山v2.0 林利炜v2.1 刘峥v2.2
 * 1.0为jdom 2.0为联通合同管理系统用的dom4j版本。 2.1是基于2.0但接口延续1.0版本，用于兼容原调用的地方。
 * 2.2版本，增加对类似XPath的写法。可以用/作为分隔符，如果XML根元素为root，则允许/root/sysarg/path或root/sysarg/path或原来的sysarg.path这样的写法。
 * 这里/和.是等价的。如果最后一个为@开头如/root/sysarg/path/@name 则name被认为是path节点的一个属性。<path name="nameValue">pathValue</path>
 * @version 2.2
 */
public class XMLTools {
  private String fileName;
  private org.dom4j.Document doc;
  private Map<String,String> propertyCache;
  private static final String ENCODING="UTF-8";
//  private long shouldWriteTime; //用于控制频繁写入。暂不使用
//  private boolean writed;
//  private static final long minWriteInterval = 100;
  boolean needCache;
  /**
   * 创建一个XMLTools对象
   * @param fileFullName String
   */
  public XMLTools(String fileFullName) {
    this(fileFullName, true);
  }

  public XMLTools(String fileFullName, boolean needCache) {
    this.fileName = fileFullName;
    this.needCache = needCache;
    this.readDoc();
    if (needCache) {
      propertyCache = new SizeFixedCache<String,String>(256);
    }
  }

  public XMLTools(String fileFullName, int cacheSize) {
    this.fileName = fileFullName;
    this.needCache = true;
    this.readDoc();
    propertyCache = new SizeFixedCache<String,String>(cacheSize);
  }

  /**
   * 取得节点值
   * @param name String
   * @return String
   */
  public String getProperty(String name) {
    name = name.replace('.', '/');
    if (needCache && propertyCache.containsKey(name)) {
      return propertyCache.get(name);
    }
    String[] propName = parsePropertyName(name);
    Element root = doc.getRootElement();
    Element element = root;
    for (int i = 0; i < propName.length; i++) {
      if (propName[i] == null || propName[i].equals("") ||
          (i == 0 && propName[i].equals(root.getQName().getName())) ||
          (i == 1 && propName[i].equals(root.getQName().getName())) &&
          (propName[0] == null || propName[0].equals(""))) {
        //如果XPath形式，不管是/root 还是 root 都要忽略掉
        element = root;
      }
      else if (propName[i].startsWith("@")) { //加入对属性的支持
        return element.attributeValue(propName[i].substring(1));
      }
      else {
        element = element.element(propName[i]);
        if (element == null) {
          return null;
        }
      }
    }

    String value = element.getText();

    if (value == null || "".equals(value)) {
      return value;
    }
    else {
      value = value.trim();
      if (needCache) {
        propertyCache.put(name, value);
      }
      return value;
    }
  }

  /**
   * 取得子节点名
   * @param parent String
   * @return String[]
   */
  public String[] getChildrenProperties(String parent) {
    String[] propName = parsePropertyName(parent);
    Element element = doc.getRootElement();

    for (int i = 0; i < propName.length; i++) {
      element = element.element(propName[i]);
      if (element == null) {
        return new String[0];
      }
    }
    List children = element.elements();
    int childCount = children.size();
    String[] childrenNames = new String[childCount];

    for (int i = 0; i < childCount; i++) {
      childrenNames[i] = ( (Element) children.get(i)).getName();
    }

    return childrenNames;
  }

  /**
   * 设置节点值
   * @param name String
   * @param value String
   */
  public void setProperty(String name, String value) {
    name = name.replace('.', '/');
    if (value == null || value.equals("")) {
      this.deleteProperty(name);
      return;
    }
    if (needCache) {
      if (propertyCache.containsKey(name)) {
        String v = propertyCache.get(name);
        if (v != null && v.equals(value)) {
          return;
        }
      }
      propertyCache.put(name, value);
    }

    String[] propName = parsePropertyName(name);
    Element root = doc.getRootElement();
    Element element = root;
    Node target = null;
    for (int i = 0; i < propName.length; i++) {
      if (propName[i] == null || propName[i].equals("") ||
          (i == 0 && propName[i].equals(root.getQName().getName())) ||
          (i == 1 && propName[i].equals(root.getQName().getName())) &&
          (propName[0] == null || propName[0].equals(""))) {
        //如果XPath形式，不管是/root 还是 root 都要忽略掉
        element = root;
      }
      else if (propName[i].startsWith("@")) { //加入对属性的支持
        target = element.attribute(propName[i].substring(1));
        if (target == null) {
          target = element.addAttribute(propName[i].substring(1), value);
        }
      }
      else {
        target = element.element(propName[i]);
        if (target == null) {
          element = element.addElement(propName[i]);
          target = element;
        }
        else {
          element = (Element) target;
        }
      }
    }
    target.setText(value);
    writeDoc(ENCODING);
  }

  /**
   * 删除某个节点
   * @param name String
   */
  public void deleteProperty(String name) {
	if(needCache) propertyCache.remove(name);//LinLW 2011-03-14 修订清空property后缓存没有清空的BUG
    name = name.replace('.', '/');
    String[] propName = parsePropertyName(name);
    Element root = doc.getRootElement();
    Element element = root;

    for (int i = 0; i < (propName.length - 1); i++) {
      if (propName[i] == null || propName[i].equals("") ||
          (i == 0 && propName[i].equals(root.getQName().getName())) ||
          (i == 1 && propName[i].equals(root.getQName().getName())) &&
          (propName[0] == null || propName[0].equals(""))) {
        //如果XPath形式，不管是/root 还是 root 都要忽略掉
        element = root;
      }
      else {
        element = element.element(propName[i]);
        if (element == null) {
          return;
        }
      }
    }
    if (propName[propName.length - 1].startsWith("@")) { //加入对属性的支持
      element.remove(element.attribute(propName[propName.length -
                                       1].substring(1)));
    }
    else {
      element.remove(element.element(propName[propName.length - 1]));
    }
    writeDoc("UTF-8");
  }

  /**
   * 解析路径
   * @param name String
   * @return String[]
   */
  private static String[] parsePropertyName(String name) {
    return name.split("[./]"); //JDK1.4 +
  }

  /**
   * 读取文件内容
   */
  private synchronized void readDoc() {
    try {
      doc = new SAXReader().read(new File(fileName));
    }
    catch (DocumentException ex) {
//      System.out.println("读取XML文件[" + fileName + "]失败,使用空内容代替");
      org.dom4j.DocumentFactory f = new DocumentFactory();
      doc = f.createDocument(f.createElement("datas"));
    }
  }

  /**
   * 写入文件内容
   * @param encoding String
   */
  public synchronized void writeDoc(String encoding) {
    OutputStream out = null;
    boolean error = false;
    File tempFile = null;
    File file = new File(fileName);
    try {
      File pfile = file.getParentFile();
      if (!pfile.exists()) {
        pfile.mkdirs();
      }
      tempFile = new File(
          file.getParentFile(),
          file.getName().concat(".tmp"));
      out = new BufferedOutputStream(new FileOutputStream(tempFile));
      OutputFormat outFormat = OutputFormat.createPrettyPrint();
      outFormat.setIndent(" ");
      outFormat.setIndentSize(1);
      outFormat.setExpandEmptyElements(false);
      outFormat.setLineSeparator("\r\n");
      outFormat.setEncoding(encoding);
      XMLWriter outputter = new XMLWriter(out, outFormat);
      doc.setXMLEncoding(encoding);
      outputter.write(doc);
    }
    catch (Exception e) {
      e.printStackTrace();
      error = true;
    }
    finally {
      try {
        out.close();
      }
      catch (Exception e) {
        e.printStackTrace();
        error = true;
      }
    }
    if (!error) {
      file.delete();
      tempFile.renameTo(file);
    }
  }

}
