package app;
import util.PinYinUtil;
import util.Util;

import java.io.File;
import java.util.Date;
import java.util.Objects;

public class FileMeta {
    private String name;
    private String path;
    private Long size;
    private Date lastModified;
    //客户端使用,与app.fxml匹配
    private String sizeText;
    private String lastModifiedText;
    private String pinyin;
    private String pinyinFirst;
    private Boolean isDir;

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getPinyinFirst() {
        return pinyinFirst;
    }

    public void setPinyinFirst(String pinyinFirst) {
        this.pinyinFirst = pinyinFirst;
    }

    public FileMeta(File file) {
        this(file.getName(),file.getParent(),file.length(),new Date(file.lastModified()),file.isDirectory());
    }

    public FileMeta(String name, String path, Long size, Date lastModified,Boolean isDir) {

        this.name = name;
        this.path = path;
        this.size = size;
        this.lastModified = lastModified;
        this.isDir = isDir;
        if (PinYinUtil.containsChinese(name)){
            String[] str = PinYinUtil.get(name);
            pinyin = str[0];
            pinyinFirst = str[1];
        }
        sizeText = Util.parseSize(size);
        lastModifiedText = Util.parseDate(lastModified);
    }

    public Boolean getDir() {
        return isDir;
    }

    public void setDir(Boolean dir) {
        isDir = dir;
    }

    @Override
    public String toString() {
        return "FileMeta{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", isDir=" + isDir +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileMeta meta = (FileMeta) o;
        return Objects.equals(name, meta.name) &&
                Objects.equals(path, meta.path) &&
                Objects.equals(isDir, meta.isDir);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, path, isDir);
    }

    public FileMeta() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public String getSizeText() {
        return sizeText;
    }

    public void setSizeText(String sizeText) {
        this.sizeText = sizeText;
    }

    public String getLastModifiedText() {
        return lastModifiedText;
    }

    public void setLastModifiedText(String lastModifiedText) {
        this.lastModifiedText = lastModifiedText;
    }

}
