package com.lvonce.binaryweaver;

import org.testng.annotations.*;
import static org.testng.Assert.*;

public class BinaryClassUtilTest {

    private interface Service {
        public int run();
    }

    private class ServiceImpl implements Service {
        public ServiceImpl() {
            
        }
        public int run() {
            return 23;
        }
    }

    @Test
    public void constructInstanceTest() {
        Service s = BinaryClassUtil.constructInstance(ServiceImpl.class, new Class<?>[]{BinaryClassUtilTest.class}, this);
        assertEquals(s.run(), 23);
    }

}