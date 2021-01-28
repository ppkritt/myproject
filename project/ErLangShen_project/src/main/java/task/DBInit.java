package task;

import util.DBUtil;

import java.io.*;
import java.lang.reflect.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class DBInit {

    /**
     * 读取sql文件
     *
     */
    public static String[] readSql() {
        try {
            //通过ClassLoader获取流
            InputStream in = DBInit.class.getClassLoader().getResourceAsStream("init.sql");
            //字节流转换为字符流
            BufferedReader br = new BufferedReader(new InputStreamReader(in,"UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line ;
            while((line = br.readLine()) != null){
                if (line.contains("--")){
                    line = line.substring(0,line.indexOf("--"));
                }
                sb.append(line);
            }
            String[] sqls = sb.toString().split(";");
            return sqls;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("读取错误");
        }
    }

    public static void init() {
        //数据库jdbc操作
        //1.建立数据库连接connection
        Connection connection = null;
        Statement statement = null;
        try {
            //1.建立数据库连接connection
            connection = DBUtil.getConnection();
            //2.创建sql语句执行对象Statement
            String[] sqls = readSql();
            //2.创建sql语句执行对象Statement
            statement = connection.createStatement();
            for (String sql:sqls
                 ) {
                //3.执行sql
                statement.executeUpdate(sql);
            }
            //4.如果是查询操作,获取结果集ResultSet,处理结果集
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("初始话数据库表操作失败",e);
        }finally {
            //5.释放资源
            DBUtil.close(connection,statement);
        }

    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(readSql()));
    }
}
