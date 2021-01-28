package com.tong.commands;

import com.tong.command.Command;
import com.tong.database.Database;
import com.tong.protocol.Write;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.OutputStream;
import java.util.List;

public class LRANGECommand implements Command {
    private final static Logger logger = LoggerFactory.getLogger(LRANGECommand.class);
    private List<Object> list;
    private Write w = new Write();

    @Override
    public void run(OutputStream out) throws Exception {
        if (list.size() != 3) {
            w.writeError(out, "命令至少需要三个参数.");
            return;
        }
        String key = new String((byte[]) list.get(0));
        int start = Integer.parseInt(new String((byte[]) list.get(1)));
        int end = Integer.parseInt(new String((byte[]) list.get(2)));
        logger.debug("运行的是 lrange 命令: {} {} {}", key, start, end);
        List<String> list = Database.getInstance().getList(key);
        if (end < 0) {
            end = list.size() + end;
        }
        w.writeArray(out, list.subList(start, end + 1));
    }

    @Override
    public void setList(List<Object> list) {
        this.list = list;
    }
}
