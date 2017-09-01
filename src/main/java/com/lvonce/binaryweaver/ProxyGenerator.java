package com.lvonce.binaryweaver;


import com.lvonce.binaryweaver.adapters.*;
import static com.lvonce.binaryweaver.BinaryClassUtil.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lvonce.binaryweaver.adapters.DelegateMethodAdapter;

public class ProxyGenerator {
	private static final Logger logger = LoggerFactory.getLogger(ProxyGenerator.class);

	public static Class<?> genPrxoyClass(String originClassName, String proxyClassName) {
        byte[] classData = Utils.getClassBytes(originClassName);
		byte[] newClassData = Utils.transformClass(classData, DelegateMethodAdapter.class, proxyClassName);
		return BinaryClassUtil.defineClass(newClassData);
	}

}
