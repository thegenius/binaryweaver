package com.lvonce.binaryweaver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import org.testng.annotations.Test;
import com.lvonce.binaryweaver.prepares.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.testng.Assert.*;

public class JoinerTest {
    private final Logger logger = LoggerFactory.getLogger(JoinerTest.class);

    @Test
    public void joinRouterTest() {
        try {
            Service proxy = RouterGenerator.getJoinRouter(Service.class, new Stub());
            proxy.func1();
            proxy.func2(23);
            proxy.func3(new int[] { 23, 41 });
            proxy.func4(new Integer[] { new Integer(23) });

            RpcFuture future;
            ServiceProxy serviceProxy = RouterGenerator.getJoinRouter(ServiceProxy.class, new Stub());
            future = serviceProxy.func1();
            future = serviceProxy.func2(23);
            future = serviceProxy.func3(new int[] { 23, 41 });
            future = serviceProxy.func4(new Integer[] { new Integer(23) });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}