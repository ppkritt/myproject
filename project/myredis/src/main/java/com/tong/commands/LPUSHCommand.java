package com.tong.commands;

import com.tong.command.Command;
import com.tong.database.Database;
import com.tong.protocol.Write;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;


public class LPUSHCommand implements Command {
    private final static Logger logger = LoggerFactory.getLogger(LPUSHCommand.class);
    private List<Object> list;
    private Write w = new Write();

    @Override
    public void run(OutputStream out) throws IOException {
        if (list.size() != 2) {
            w.writeError(out, "命令至少需要两个参数");
            return;
        }

        String key = new String((byte[]) list.get(0));
        String value = new String((byte[]) list.get(1));
        logger.debug("运行的是 lpush 命令: {} {}", key, value);
        List<String> list = Database.getInstance().getList(key);
        list.add(0,value);
        logger.debug("插入后数据共有 {} 个", list.size());
        w.writeInteger(out,list.size());
    }

    @Override
    public void setList(List<Object> list) {
        this.list = list;
    }
}
