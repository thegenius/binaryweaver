package com.lvonce.binaryweaver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.SecurityException;
import java.lang.NoSuchMethodException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class BinaryClassUtil {
	private static Logger logger = LoggerFactory.getLogger(BinaryClassUtil.class);

	public static <T> T constructInstance(Class<T> classType, Class<?>[] paramTypes, Object... args) {
		logger.debug("constructInstance({}, {}, {})", classType, paramTypes, args);
		try {
			if (paramTypes == null) {
				return classType.newInstance();
			} else {
				Constructor<T> constructor = classType.getDeclaredConstructor(paramTypes);
				return constructor.newInstance(args);
			}
		} catch (InstantiationException | IllegalAccessException | SecurityException | InvocationTargetException
				| NoSuchMethodException e) {
			e.printStackTrace();
			logger.debug("constructInstance({}, {}, {}): {}", classType, paramTypes, args, e.getMessage());
			return null;
		}
	}

	public static <T> T createInstance(Class<T> classType, Object... args) {
		logger.debug("createInstance({}, {})", classType, args);
		try {
			if (args == null || args.length == 0) {
				return classType.newInstance();
			} else {
				Class<?>[] paramTypes = new Class[args.length];
				for (int i = 0; i < args.length; ++i) {
					paramTypes[i] = args[i].getClass();
				}
				return constructInstance(classType, paramTypes, args);
			}
		} catch (InstantiationException | IllegalAccessException | SecurityException e) {
			logger.debug("createInstance({}, {}): {}", classType, args, e.getStackTrace());
			return null;
		}
	}

	public static Class<?> defineClass(byte[] bytesOfClass) {
		Class<?> classType = new ClassLoader() {
			public Class<?> defineClass(byte[] bytes) {
				return super.defineClass(null, bytes, 0, bytes.length);
			}
		}.defineClass(bytesOfClass);
		return classType;
	}

	public static Object newInstance(byte[] bytesOfClass, Object... args) {
		logger.debug("newInstance({}, {})", bytesOfClass.length, args);
		return createInstance(defineClass(bytesOfClass), args);
	}

	public static Object buildInstance(byte[] bytesOfClass, Class<?>[] paramTypes, Object... args) {
		logger.debug("newInstance({}, {})", bytesOfClass.length, args);
		Class<?> classType = new ClassLoader() {
			public Class<?> defineClass(byte[] bytes) {
				return super.defineClass(null, bytes, 0, bytes.length);
			}
		}.defineClass(bytesOfClass);
		return constructInstance(classType, paramTypes, args);
	}

}
