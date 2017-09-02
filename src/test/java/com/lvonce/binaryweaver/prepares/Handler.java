package com.lvonce.binaryweaver.prepares;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
public class Handler {
    private static final Logger logger = LoggerFactory.getLogger(Handler.class);
    public void func1() {
        logger.info("handler func1");
    }

    public void func2(int a) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String argsString = mapper.writeValueAsString(a);
        logger.info("handler func2(" + argsString + ")");
    }

    public void func3(int[] a) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String argsString = mapper.writeValueAsString(a);
        logger.info("handler func3(" + argsString + ")");
    }

    public void func4(Integer a) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String argsString = mapper.writeValueAsString(a);
        logger.info("handler func4(" + argsString + ")");
    }

    public void func5(Integer[] a) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String argsString = mapper.writeValueAsString(a);
        logger.info("handler func5(" + argsString + ")");
    }

    public void func6(Integer[] a, Integer[] b) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String argsStringA = mapper.writeValueAsString(a);
        String argsStringB = mapper.writeValueAsString(b);
        logger.info("handler func6(" + argsStringA + "," + argsStringB + ")");
    }
}