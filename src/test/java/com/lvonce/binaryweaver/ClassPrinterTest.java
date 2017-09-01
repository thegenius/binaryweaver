
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

public class ClassPrinterTest {
    private static Logger logger = LoggerFactory.getLogger(ClassPrinterTest.class);

    @Test
    public void testPrint() {
        // InputStream istream = this.getClass().getClassLoader().getResourceAsStream("FooClass.class");
        // assertNotNull(istream);
        // byte[] classData = Utils.streamToByteArray(istream);
        byte[] classData = Utils.getClassBytes("com.lvonce.binaryweaver.FooClass");
        assertNotNull(classData);

        byte[] newClassData = Utils.transformClass(classData, DelegateMethodAdapter.class, "ProxyTest");
        Foo proxy = (Foo) BinaryClassUtil.buildInstance(newClassData, new Class<?>[] { int.class }, 23);
        assertNotNull(proxy);

        proxy.func1();
        assertEquals(proxy.func2(23, 21), 45);
        assertEquals(proxy.func3(5), 115);
        assertEquals(proxy.func4(0L, 5, null), null);
        Reloadable proxy2 = (Reloadable)proxy;
        proxy2.__setReloadTarget__(null);
        // proxy.func1();

        Utils.writeToFile("ProxyTest.class", newClassData);
    }

}