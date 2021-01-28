package util;

import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;
import javax.sql.DataSource;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
    /**
     *
     * @return
     */
    private static volatile DataSource DATA_SOURCE;

    /**
     *提供获取数据库连接词的功能
     * 使用单例模式(多线程安全)
     * 回顾多线程安全版本的单例模式:
     * 1.为什么在最外层判断是否为null?
     * 2.synchronized加锁以后,为什么还要判断是否为null
     * 3.为什么Datasource类变量要使用volatile关键字修饰
     * 多线程操作:原子性,可见性(从主内存拷贝到工作内存),有序性
     * synchronized保证前三个特性,volatile保证可见性,有序性
     * @return
     */
    private static DataSource getDataSource(){
        if (DATA_SOURCE == null){ //提高效率
            //保证只有一个不为空的DATA_SOURCE   单例
                synchronized (DBUtil.class){
                if (DATA_SOURCE == null){
                    SQLiteConfig config = new SQLiteConfig();
                    config.setDateStringFormat(Util.DATA_PATTERN);
                    DATA_SOURCE = new SQLiteDataSource();
                    ((SQLiteDataSource)DATA_SOURCE).setUrl(getUrl());
                }
            }
        }
        return DATA_SOURCE;
    }

    /**
     * 获取sqlit数据库url的方法
     * @return
     */
    private static String getUrl(){
        //classes路径
        URL classesURL = DBUtil.class.getClassLoader().getResource("./"); //classes路径
        //target路径
        String dir = new File(classesURL.getPath()).getParent();
        //ErLangShen.db路径 jdbc:sqlite://D:\java%20code\%e9%a1%b9%e7%9b%ae\ErLangShen\target\ErLangShen.db
        String url  = "jdbc:sqlite://" + dir + File.separator + "ErLangShen.db";
        return url;
    }

    /**
     * 提供获取数据库连接的办法
     * 从数据库连接词DataSource.getConnection()来获取数据库连接
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();

    }




    public static void close(Connection connection, Statement statement) {
        close(connection,statement,null);
    }

    public static void close(Connection connection, Statement statement, ResultSet resultSet)  {
        try {
            if (connection != null) {
                connection.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (resultSet != null){
                resultSet.close();
            }
        }catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("释放资源失败",e);
        }

    }
}
