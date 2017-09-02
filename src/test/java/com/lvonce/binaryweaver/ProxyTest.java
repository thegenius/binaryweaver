
package com.lvonce.binaryweaver;

import java.io.File;
import java.io.FileOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

import java.lang.reflect.Method;
import org.testng.annotations.Test;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.testng.Assert.*;

import com.lvonce.binaryweaver.adapters.*;
import com.lvonce.binaryweaver.prepares.*;

public class ProxyTest {
    private static Logger logger = LoggerFactory.getLogger(ProxyTest.class);

    @Test
    public void testPrint() {
        // byte[] classData = Utils.getClassBytes("com.lvonce.binaryweaver.FooClass");
        // assertNotNull(classData);
        // byte[] newClassData = Utils.transformClass(classData, DelegateMethodAdapter.class, "ProxyTest");
        // Foo proxy = (Foo) BinaryClassUtil.buildInstance(newClassData, new Class<?>[] { int.class }, 23);
        Class<?> classType = Proxy.createClass("com.lvonce.binaryweaver.FooClass", "ProxyTest");
        Foo proxy = (Foo) BinaryClassUtil.constructInstance(classType, new Class<?>[] { int.class }, 23);
        assertNotNull(proxy);

        proxy.func1();
        assertEquals(proxy.func2(23, 21), 45);
        assertEquals(proxy.func3(5), 115);
        assertEquals(proxy.func4(0L, 5, null), null);
        Reloadable proxy2 = (Reloadable)proxy;
        proxy2.__setReloadTarget__(null);
        // proxy.func1();

        //Utils.writeToFile("ProxyTest.class", newClassData);
    }

}