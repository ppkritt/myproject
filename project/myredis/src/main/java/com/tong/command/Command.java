package com.tong.command;

import java.io.OutputStream;
import java.util.List;

public interface Command {
    void run(OutputStream out) throws Exception;

    void setList(List<Object> list);
}
