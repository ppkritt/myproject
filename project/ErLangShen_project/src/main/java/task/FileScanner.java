package task;

import java.io.File;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FileScanner {
    private  ScanCallback callback;

//    private ThreadPoolExecutor pool = new ThreadPoolExecutor(
//          3,
//          3,
//          0,
//          TimeUnit.MICROSECONDS,new LinkedBlockingDeque<>(),
//          new ThreadPoolExecutor.AbortPolicy()
//    );

    private ExecutorService pool = Executors.newFixedThreadPool(4);

    private volatile AtomicInteger count = new AtomicInteger();

    private Object lock = new Object();

    public FileScanner(ScanCallback callback) {
        this.callback = callback;
    }

    public FileScanner() {

    }


    /**
     * 扫描文件目录
     * 开始不知道多少子文件夹
     * @param path
     */
    public void scan(String path) {
        count.incrementAndGet();//根目录扫描任务,计数器++i
        doScan(new File(path));
    }

    private void doScan(File dir){
        callback.callback(dir);
        pool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    File[] children = dir.listFiles();
                    if (children != null) {
                        for (File file : children
                        ) {
                            if (file.isDirectory()) {//如果是文件夹,递归
                               // System.out.println("文件夹" + file.getPath());
                                count.incrementAndGet();//++i
                                doScan(file);
                            }
                        }
                    }
                }finally {
                    int c = count.decrementAndGet();
                    if (c == 0){
                        synchronized (lock){
                            lock.notify();
                            System.out.println("********************************************");
                        }
                    }
                }
            }
        });
    }


    /**
     * 等待扫描任务结束
     * 多线程的任务等待:thread.start()
     * join()
     * wait()
     */
    public void waitFinish() throws InterruptedException {
        synchronized (lock){
            try{
                lock.wait();
            }finally {
                System.out.println("关闭线程池.");
                pool.shutdownNow();
            }
        }
    }



}