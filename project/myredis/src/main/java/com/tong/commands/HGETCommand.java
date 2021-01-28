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

public class HGETCommand implements Command {
    private final static Logger logger = LoggerFactory.getLogger(HGETCommand.class);
    private List<Object> list;
    private Write w = new Write();

    @Override
    public void run(OutputStream out) throws IOException {
        if (list.size() != 2) {
            w.writeError(out, "命令至少需要两个参数");
            return;
        }
        String key = new String((byte[]) list.get(0));
        String filed = new String((byte[]) list.get(1));
        logger.debug("运行的是 hget 命令: {} {}", key, filed);
        Map<String,String> hash = Database.getInstance().getHash(key);
        String value = hash.get(filed);
        logger.debug("得到的数据是 {}", value);
        if (value != null){
            w.writeBulkString(out, value);
        }else {
            w.writeNull(out);
        }
    }

    @Override
    public void setList(List<Object> list) {
        this.list = list;
    }
}
