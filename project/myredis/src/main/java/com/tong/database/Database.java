package com.tong.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Database {
    private static Map<String, List<String>> lists = new HashMap<>();
    private static Map<String, Map<String,String>> hashes = new HashMap<>();
    private static Database instance = new Database();
    public static Database getInstance() {
        return instance;
    }
    private Database() {
        hashes = new HashMap<>();
        lists = new HashMap<>();
    }
    public List<String> getList(String key){
        List<String> list = lists.get(key);
        if (list == null){
            list = new ArrayList<>();
            lists.put(key,list);
        }
        return list;
    }

    public Map<String,String> getHash(String key){
        Map<String,String> hash = hashes.get(key);
        if (hash == null){
            hash = new HashMap<>();
            hashes.put(key,hash);
        }
        return hash;
    }
}
