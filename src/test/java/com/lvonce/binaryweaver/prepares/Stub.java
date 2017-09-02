package com.lvonce.binaryweaver.prepares;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
public class Stub {
    private static final Logger logger = LoggerFactory.getLogger(Stub.class);
    public Object call(String name, String sig, Object... args) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String argsString = mapper.writeValueAsString(args);
            //logger.info("proxy call(" +scope+", "+name+", "+sig+", " + argsString+")");
            logger.info("proxy call(" + name + ", " + sig + ", " + argsString + ")");
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}