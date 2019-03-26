package MapperToSql;

import java.io.File;
import java.io.PrintWriter;
import java.net.URL;

public class MapperSearch {

    public static void main(String[] args) throws Exception {

        // 这个目录很重要，为mapper的根目录，所有的mapper.xml文件都要在这个目录下面
        String packageName = "mapper";
        StringBuilder sb = new StringBuilder();
        searchFile(packageName, sb);

        // 导出文件
        File fp = new File("d:\\database.sql");
        PrintWriter pfp = new PrintWriter(fp);
        pfp.print(sb.toString());
        pfp.close();

    }

    // 查找mapper文件， 并生成sql语句
    public static void searchFile(String packageName, StringBuilder sb) throws Exception {
        URL url = MapperSearch.class.getClassLoader().getResource(packageName.replace(".", "/"));
        System.out.println(url);
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                searchFile(packageName.replace(".", "/") + "/" + file.getName(), sb);
            } else {
                if (file.getName().indexOf("Mapper.xml") > 0) {
                    //System.out.println(file.getName());
                    sb.append(MapperParse.parse(file));
                }
            }
        }
    }
}