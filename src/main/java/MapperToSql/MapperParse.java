package MapperToSql;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class MapperParse {
      public static String parse(File f) throws Exception {
        // 解析mapper.xml文件
        // 创建SAXReader的对象reader
        SAXReader reader = new SAXReader();
        String sql = "";
        try {
            // 通过reader对象的read方法加载mapper.xml文件,获取docuemnt对象。
            Document document = reader.read(f);
            // 通过document对象获取根节点bookstore
            Element mapper = document.getRootElement();
            // 通过element对象的elementIterator方法获取迭代器
            Iterator it = mapper.elementIterator();
            // 遍历迭代器，获取根节点中的信息（书籍）
            String table = "";
            String columns = "";
            while (it.hasNext()) {
                Element resultMap = (Element) it.next();

                // table = searchTable(resultMap);
                if (columns.trim().length() == 0) {
                    columns = searchColumns(resultMap);
                }
                if (table.trim().length() == 0) {
                    table = searchTable(resultMap);
                }
                if (table.trim().length() != 0 && columns.trim().length() != 0) {
                    break;
                }

            }
            if (table.trim().length() != 0 && columns.trim().length() != 0) {
                sql = "CREATE TABLE `" + table.trim() + "` ( `id` char(36) NOT NULL COMMENT '主键ID'," + columns
                        + "PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;\r\n";
            } else {
                sql = "表文件 -----" + f.getName() + "----------有问题，请自行查看";
            }

            System.out.println(sql);

        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sql;
    }

    public static String searchTable(Element e) {
        String table = "";
        if (e.getName().equals("delete")) {
            List<Attribute> resultMapAttrs = e.attributes();
            for (Attribute attr : resultMapAttrs) {
                if (attr.getName().equals("id") && attr.getValue().equals("deleteByPrimaryKey")) {
                    String delSql = e.getText();
                    table = delSql.substring(delSql.indexOf("from") + 4, delSql.indexOf("where")).replaceAll("\n\t\t",
                                                        "");

                }
            }
        }
        return table;
    }

    public static String searchColumns(Element el) {
        String sql = "";
        if (el.getName().equals("resultMap")) {
            List<Attribute> resultMapAttrs = el.attributes();
            for (Attribute attr : resultMapAttrs) {
                if (attr.getName().equals("id") && attr.getValue().equals("BaseResultMap")) {
                    Iterator itt = el.elementIterator();
                    while (itt.hasNext()) {
                        Element result = (Element) itt.next();
                        if (!result.getName().equals("id")) {
                            List<Attribute> resultAttrs = result.attributes();
                            for (Attribute resultAttr : resultAttrs) {
                                if (resultAttr.getName().equals("column")) {
                                    sql = sql + "`" + resultAttr.getValue() + "` ";
                                }
                                if (resultAttr.getName().equals("jdbcType")) {
                                    if (resultAttr.getValue().equals("VARCHAR")) {
                                        sql = sql + " varchar(50) DEFAULT NULL, ";
                                    }
                                    if (resultAttr.getValue().equals("TIMESTAMP")) {
                                        sql = sql + " datetime DEFAULT NULL, ";
                                    }
                                    if (resultAttr.getValue().equals("CHAR")) {
                                        sql = sql + " char(36) DEFAULT NULL, ";
                                    }
                                    if (resultAttr.getValue().equals("DATE")) {
                                        sql = sql + " date DEFAULT NULL, ";
                                    }
                                    if (resultAttr.getValue().equals("DECIMAL")) {
                                        sql = sql + " decimal(18,4) DEFAULT NULL, ";
                                    }
                                    if (resultAttr.getValue().equals("TINYINT")) {
                                        sql = sql + " tinyint(4) DEFAULT NULL, ";
                                    }
                                    if (resultAttr.getValue().equals("INTEGER")) {
                                        sql = sql + " int(11) DEFAULT NULL, ";
                                    }
                                }

                            }
                        }
                    }
                }

            }

        }
        return sql;
    }
}
