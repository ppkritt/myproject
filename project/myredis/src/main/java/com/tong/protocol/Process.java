package com.tong.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Process {
    private Reads reads = new Reads();

    public Object process(InputStream is) throws IOException {
        int b = is.read();
        if (b == -1){
            throw new RuntimeException("不应该读到结尾的。");
        }
        switch (b){
            case '+':
                return processSimpleString(is);
            case '-':
                throw new RuntimeException(processError(is));
            case ':':
                return processInteger(is);
            case '$':
                return processBulkString(is);
            case '*':
                return processArray(is);
            default:
                throw new RuntimeException("不识别的类型");

        }
    }

    private String processSimpleString(InputStream is) throws IOException {
        return reads.readLine(is);
    }

    private String processError(InputStream is) throws IOException {
        return reads.readLine(is);
    }

    private Long processInteger(InputStream is) throws IOException {
        return reads.readInteger(is);
    }

    private byte[] processBulkString(InputStream is) throws IOException {
        int len = (int)reads.readInteger(is);
        if (len == -1){
            return null;
        }
        byte[] arr = new byte[len];
        is.read(arr,0, len);
        is.read();
        is.read();
        return arr;
    }

    private List<Object> processArray(InputStream is) throws IOException {
        int len = (int)reads.readInteger(is);
        if (len == -1){
            return null;
        }
        List<Object> list = new ArrayList<>(len);
        for (int i = 0; i < len; i++) {
            try{
                list.add(process(is));
            }catch (Exception e){
                list.add(e);
            }
        }
        return list;
    }


}
