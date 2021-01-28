package com.tong.commands;

import com.tong.command.Command;
import com.tong.database.Database;
import com.tong.protocol.Write;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public class HSETCommand implements Command {
    private final static Logger logger = LoggerFactory.getLogger(HSETCommand.class);
    private List<Object> list;
    private Write w = new Write();

    @Override
    public void run(OutputStream out) throws IOException {
        if (list.size() != 3) {
            w.writeError(out, "命令至少需要三个参数");
            return;
        }
        String key = new String((byte[]) list.get(0));
        String filed = new String((byte[]) list.get(1));
        String value = new String((byte[]) list.get(2));
        logger.debug("运行的是 hset 命令: {} {} {}", key, filed, value);
        Map<String,String> hash = Database.getInstance().getHash(key);
        boolean isUpdate = hash.containsKey(filed);
        hash.put(filed,value);
        if (isUpdate){
            logger.debug("更新 {} 的值为 {}", filed, value);
            w.writeInteger(out, 0);
        }else {
            logger.debug("插入了 {} {}", filed, value);
            w.writeInteger(out, 1);
        }
    }

    @Override
    public void setList(List<Object> list) {
        this.list = list;
    }
}
