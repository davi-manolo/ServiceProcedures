/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 * @author manolo
 */
public class LoadProceduresProperty {

    private static Properties props = new Properties();
    private static LoadProceduresProperty uniqueInstance;

    private LoadProceduresProperty() {}

    public void load() throws IOException {
        try (InputStream in = new FileInputStream("procedures.properties")) {
            props.load(in);
        }
    }

    public Properties getProperty() {
        return props;
    }

    public List<String> getKeys() {
        Set<Object> keys = props.keySet();
        return keys.stream().map(entry -> entry.toString()).collect(Collectors.toList());
    }

    public List<Integer> getValues() {
        Collection<Object> values = props.values();
        return values.stream()
                .map(entry -> Integer
                .parseInt(entry.toString()))
                .collect(Collectors.toList());
    }

    public int getValue(String key) {
        Object obj = props.get(key);
        return Integer.parseInt(obj.toString());
    }

    public static synchronized LoadProceduresProperty getInstance() {
        if (uniqueInstance == null) {
            uniqueInstance = new LoadProceduresProperty();
        }
        return uniqueInstance;
    }

}
