package com.lvonce.binaryweaver;

import com.lvonce.binaryweaver.adapters.*;
import static com.lvonce.binaryweaver.BinaryClassUtil.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lvonce.binaryweaver.adapters.DelegateMethodAdapter;

public class Proxy {
	private static final Logger logger = LoggerFactory.getLogger(Proxy.class);

	public static Class<?> createClass(String originClassName, String proxyClassName) {
		byte[] classData = Utils.getClassBytes(originClassName);
		byte[] newClassData = createClass(classData, proxyClassName);
		return BinaryClassUtil.defineClass(newClassData);
	}

	public static byte[] createClass(byte[] originClassBytes, String proxyClassName) {
		byte[] newClassData = Utils.transformClass(originClassBytes, DelegateMethodAdapter.class, proxyClassName);
		return newClassData;
	}
}
