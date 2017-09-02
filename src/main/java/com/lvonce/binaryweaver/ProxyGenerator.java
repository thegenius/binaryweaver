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
		byte[] newClassData = genProxyClass(classData, proxyClassName);
		return BinaryClassUtil.defineClass(newClassData);
	}

	public static byte[] genProxyClass(byte[] originClassBytes, String proxyClassName) {
		byte[] newClassData = Utils.transformClass(originClassBytes, DelegateMethodAdapter.class, proxyClassName);
		return newClassData;
	}
}
