package app;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Window;
import task.*;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private GridPane rootPane;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<FileMeta> fileTable;

    @FXML
    private Label srcDirectory;//标签

    private Thread task;
    public void initialize(URL location, ResourceBundle resources) {
        //界面初始化时,需要初始化数据库及表
        DBInit.init();
        // 添加搜索框监听器，内容改变时执行监听事件
        searchField.textProperty().addListener(new ChangeListener<String>() {

            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                freshTable();
            }
        });
    }

    /**
     * 点击选择目录
     * @param event
     */
    public void choose(Event event) {
        // 选择文件目录
        DirectoryChooser directoryChooser=new DirectoryChooser();
        Window window = rootPane.getScene().getWindow();
        File file = directoryChooser.showDialog(window);
        if(file == null)
            return;
        // 获取选择的目录路径，并显示
        String path = file.getPath();
        srcDirectory.setText(path);
        //选择目录就需要执行目录的扫描任务:该目录下的目录及子目录下的文件夹

        if(task != null){
            task.interrupt();//中断
        }
            task = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("执行文件扫描任务");
                    ScanCallback callback = new FileSave();
                    FileScanner scan = new FileScanner(callback);
                    scan.scan(path);//多线程扫描.提高效率
                    //等待扫描完毕;
                    try {
                        scan.waitFinish();
                        System.out.println("所有任务执行完毕,刷新表格");
                        //刷新表格:将扫描的子文件和子文件夹都展示在表格
                        freshTable();
                    } catch (InterruptedException e) {
                        e.printStackTrace();

                    }
                }
            });
            task.start();

    }



    // 刷新表格数据
    private void freshTable(){
        ObservableList<FileMeta> metas = fileTable.getItems();
        metas.clear();
        String dir = srcDirectory.getText();
        if (dir != null && dir.trim().length() != 0){
            String content = searchField.getText();
            //提供数据库的查询方法
            List<FileMeta> fileMetas = FileSearch.search(dir,content);
            metas.addAll(fileMetas);
        }
    }
}