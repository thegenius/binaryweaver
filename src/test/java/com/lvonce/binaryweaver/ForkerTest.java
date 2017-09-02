package com.lvonce.binaryweaver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import org.testng.annotations.Test;
import com.lvonce.binaryweaver.prepares.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.testng.Assert.*;

public class ForkerTest {
    private final Logger logger = LoggerFactory.getLogger(ForkerTest.class);

    @Test
    public void forkRouterTest() {
        try {
            IForker dispatcher = Forker.create(IForker.class, new Handler());
            logger.info("fork test begin");
            //dispatcher.call("func1", "()V");
            //dispatcher.call("func2", "(I)V", 23);
            //dispatcher.call("func3", "([I)V", new int[]{23, 41});
            //dispatcher.call("func4", "(Ljava/lang/Integer;)V", new Integer(23));
            //dispatcher.call("func5", "([Ljava/lang/Integer;)V", new Integer[]{new Integer(23)});
            //dispatcher.call("func6", "([Ljava/lang/Integer;[Ljava/lang/Integer;)V", new Integer[]{new Integer(23)},new Integer[]{new Integer(23)});
            dispatcher.call("func1", "()");
            dispatcher.call("func2", "(I)", 23);
            dispatcher.call("func3", "([I)", new int[] { 23, 41 });
            dispatcher.call("func4", "(Ljava/lang/Integer;)", new Integer(23));
            dispatcher.call("func5", "([Ljava/lang/Integer;)", new Integer[] { new Integer(23) });
            dispatcher.call("func6", "([Ljava/lang/Integer;[Ljava/lang/Integer;)", new Integer[] { new Integer(23) },
                    new Integer[] { new Integer(23) });
            logger.info("fork test end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}