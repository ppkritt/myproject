package task;
import app.FileMeta;
import util.DBUtil;
import util.Util;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FileSave implements ScanCallback {
    @Override
    public void callback(File dir) {
        //文件夹下一级子文件和子文件夹保存到数据库
        // 获取本地目录下一级子文件和子文件夹
        // 集合框架中使用自定义类型，判断是否某个对象在集合存在：比对两个集合中的元素
        // list，set

        File[] children = dir.listFiles();
        List<FileMeta> locals = new ArrayList<>();
        if(children != null){
            for(File child : children){
                locals.add(new FileMeta(child));
            }
        }

        //获取数据库保存的dir目录的下一级子文件和子文件夹(jdbc select)
        List<FileMeta> metas = query(dir);

        // 数据库有，本地没有，做删除(delete)
        for(FileMeta meta : metas){
            if(!locals.contains(meta)){
                // meta的删除：
                // 1.删除meta信息本身
                // 2.如果meta是目录，还要将meta所有的子文件，子文件夹都删除
                delete(meta);
            }
        }

        // 本地有，数据库没有，做插入(insert)
        for(FileMeta meta : locals){
            if(!metas.contains(meta)){
                save(meta);
            }
        }
    }

    private void delete(FileMeta meta) {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection  = DBUtil.getConnection();
            String sql = "delete from file_meta where" +
                    " (name = ? and path = ? and is_directory = ?)" ;
            if (meta.getDir()){
                sql += " or path=?" +
                        " or path like ?";
            }
            ps = connection.prepareStatement(sql);
            ps.setString(1,meta.getName());
            ps.setString(2,meta.getPath());
            ps.setBoolean(3,meta.getDir());
            if (meta.getDir()){
                ps.setString(4,meta.getPath() + File.separator + meta.getName());
                ps.setString(5,meta.getPath() + File.separator + meta.getName() + File.separator +"%");
            }
            System.out.printf("删除文件信息，dir=%s\n",
                    meta.getPath()+File.separator+meta.getName());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("删除文件信息出错，检查delete语句", e);
        }finally {
            DBUtil.close(connection,ps);
        }
    }

    private List<FileMeta> query(File file){
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<FileMeta> list = new ArrayList<>();
        try {
            connection = DBUtil.getConnection();
            String sql = "select name, path, is_directory, size, last_modified"+
                    " from file_meta where path = ?";
            ps = connection.prepareStatement(sql);
            ps.setString(1,file.getPath());
            rs = ps.executeQuery();
            while (rs.next()){
                String name = rs.getString("name");
                String path = rs.getString("path");
                Boolean isDirectory = rs.getBoolean("is_directory");
                Long size = rs.getLong("size");
                Timestamp lastModified = rs.getTimestamp("last_modified");
                FileMeta meta = new FileMeta(name,path,size, new Date(lastModified.getTime()),isDirectory);
                System.out.printf("查询文件信息：name=%s, path=%s, is_directory=%s," +
                                " size=%s, last_modified=%s\n", name, path, String.valueOf(isDirectory),
                        String.valueOf(size), Util.parseDate(new Date(lastModified.getTime())));
                list.add(meta);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("sql出错");
        }finally {
            DBUtil.close(connection,ps,rs);
        }
    }



    /**
     * 文件保存在数据库
     * @param file
     */
    private void save(FileMeta file){
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DBUtil.getConnection();
            String sql = "insert into file_meta" +
                    "(name, path, is_directory, size, last_modified, pinyin, pinyin_first)" +
                    " values (?,?,?,?,?,?,?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1,file.getName());
            statement.setString(2,file.getPath());
            statement.setBoolean(3,file.getDir());
            statement.setLong(4,file.getSize());
            statement.setTimestamp(5,new Timestamp(file.getLastModified().getTime()));
            statement.setString(6,file.getPinyin());
            statement.setString(7,file.getPinyinFirst());
            System.out.printf("insert name=%s, path=%s\n", file.getName(), file.getPath());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("文件保存失败" + e);
        }finally {
            DBUtil.close(connection,statement);
        }
    }

}
