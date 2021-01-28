package com.tong.protocol;

import com.tong.command.Command;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Protocol {
    public static Command readCommand(InputStream is) throws Exception {
        Object o = read(is);
        List<Object> list = (List<Object>) o;
        if (list.size() < 1){
            throw new Exception("元素个数必须大于1。");
        }
        Object o1 = list.remove(0);
        if (!(o1 instanceof byte[])){
            throw new Exception("这不是一个命令。");
        }
        byte[] o2 = (byte[]) o1;
        String commandName = new String(o2);
        String className = String.format("com.tong.commands.%sCommand",commandName.toUpperCase());
        Class<?> cla = Class.forName(className);
        if (!(Command.class.isAssignableFrom(cla))){
            throw new Exception("错误的命令。");
        }
        Command command = (Command) cla.newInstance();
        command.setList(list);
        return command;
    }

    private static Object read(InputStream is) throws IOException {
        return new Process().process(is);
    }
}
