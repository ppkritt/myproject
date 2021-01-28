package com.tong.protocol;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class Write {
    public void writeBulkString(OutputStream out, String value) throws IOException {
        byte[] buf = value.getBytes("UTF-8");
        out.write('$');
        out.write(String.valueOf(buf.length).getBytes("UTF-8"));
        out.write("\r\n".getBytes());
        out.write(buf);
        out.write("\r\n".getBytes());
    }

    public void writeNull(OutputStream out) throws IOException {
        out.write(':');
        out.write("-1".getBytes("UTF-8"));
        out.write("\r\n".getBytes());
    }

    public void writeInteger(OutputStream out, long v) throws IOException {
        out.write(':');
        out.write(String.valueOf(v).getBytes("UTF-8"));
        out.write("\r\n".getBytes());
    }

    public void writeArray(OutputStream out, List<String> list) throws Exception {
        out.write('*');
        out.write(String.valueOf(list.size()).getBytes("UTF-8"));
        out.write("\r\n".getBytes("UTF-8"));
        for (Object o : list) {
            if (o instanceof String) {
                writeBulkString(out, (String)o);
            } else if (o instanceof Integer) {
                writeInteger(out, (Integer)o);
            } else if (o instanceof Long) {
                writeInteger(out, (Long)o);
            } else {
                throw new Exception("错误的类型");
            }
        }

    }

    public void writeError(OutputStream out, String error) throws IOException {
        out.write('-');
        out.write(error.getBytes("UTF-8"));
        out.write("\r\n".getBytes());
    }
}
