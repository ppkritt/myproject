package com.tong.protocol;

import java.io.IOException;
import java.io.InputStream;

public class Reads {
    public String readLine(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        boolean needRead = true;
        int b = -1;
        while(true){
            if (needRead){
                b = is.read();
                if (b == -1){
                    throw new RuntimeException("不应该读到结尾的。");
                }
            }else {
                needRead = true;
            }
            if (b == '\r'){

                int c = is.read();
                if (c == -1){
                    throw new RuntimeException("不应该读到结尾的。");
                }
                if (c == '\r'){
                    sb.append((char) b);
                    needRead = false;
                }else {
                    sb.append((char) b);
                    sb.append((char) c);
                }
                if (c == '\n'){
                    break;
                }
            }else {
                sb.append((char)b);
            }
        }
        return sb.toString();
    }

    public long readInteger(InputStream is) throws IOException {

        StringBuilder sb = new StringBuilder();
        boolean needRead = true;
        boolean isNegative = false;
        int b = -1;
        while(true){
            if (needRead){
                b = is.read();
                if (b == -1){
                    throw new RuntimeException("不应该读到结尾的。");
                }
            }else {
                needRead = true;
            }
            if (b == '\r'){
                int c = is.read();
                if (c == -1){
                    throw new RuntimeException("不应该读到结尾的。");
                }
                if (c == '\r'){
                    needRead = false;
                }
                if (c == '\n'){
                    break;
                }
            }else if(b == '-'){
                isNegative = true;
            }else {
                sb.append((char)b);
            }
        }
        long num = Long.parseLong(sb.toString());
        if (isNegative){
            num = -num;
        }
        return num;
    }
}
