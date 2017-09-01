package com.lvonce.binaryweaver;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.lang.reflect.Method;

public class SignatureUtil {
	private static final LinkedHashMap<String, String> sigStringMap = new LinkedHashMap<String, String>() {
		{
			put("boolean", "Z");
			put("byte", "B");
			put("char", "C");
			put("double", "D");
			put("float", "F");
			put("int", "I");
			put("long", "J");
			put("Object", "L");
			put("short", "S");
			put("void", "V");
			put("array", "[");
		}
	};

	private static final LinkedHashMap<Character, String> sigClassMap = new LinkedHashMap<Character, String>() {
		{
			put('Z', "boolean");
			put('B', "byte");
			put('C', "char");
			put('D', "double");
			put('F', "float");
			put('I', "int");
			put('J', "long");
			put('L', "Object");
			put('S', "short");
			put('V', "void");
			put('[', "array");
		}
	};

	/**
	 * Get the string representation of the given signature
	 * @param sig: the sig you want to convert to string
	 * @param indexWrapper: the index begin to process, and will return the stop index implicity.
	 * @return: return the string representation
	 */
	public static String getTypeString(String sig, int[] indexWrapper) {
		int index = indexWrapper[0];

		// process the array type
		if (sig.charAt(index) == '[') {
			++index;
			if (sig.charAt(index) == '[') {
				++index;
				indexWrapper[0] = index;
				return "[[" + getTypeString(sig, indexWrapper);
			}
			indexWrapper[0] = index;
			return "[" + getTypeString(sig, indexWrapper);
		}

		// process the object type
		if (sig.charAt(index) == 'L') {
			StringBuilder builder = new StringBuilder();
			++index;
			while (sig.charAt(index) != ';') {
				builder.append(sig.charAt(index));
				++index;
			}
			++index;
			indexWrapper[0] = index;
			return builder.toString().replace('.', '/');
		}

		// process the primitive type
		String result = sigClassMap.getOrDefault(sig.charAt(index), "");
		++index;
		indexWrapper[0] = index;
		return result;
	}

	/** Get the parameter type list, represent as string list.
	 * @param sig: the signature you want to convert to type list.
	 * @return: return a string list of the param type string representation.
	 */
	public static String[] getParamTypes(String sig) {
		ArrayList<String> parameterList = new ArrayList<String>();

		Integer index = 0;
		int len = sig.length();
		while (sig.charAt(index) != '(') {
			++index;
		}
		++index;
		while (sig.charAt(index) != ')') {
			int[] indexWrapper = { index };
			String typeString = getTypeString(sig, indexWrapper);
			parameterList.add(typeString);
			index = indexWrapper[0];
		}

		if (parameterList.size() != 0) {
			String[] parameterTypes = new String[parameterList.size()];
			parameterList.toArray(parameterTypes);
			return parameterTypes;
		} else {
			return null;
		}
	}

	/** Get the parameter type list, represent as string list.
	 * @param sig: the signature you want to convert to type list.
	 * @return: return a string list of the param type string representation and return type as the last one.
	 */
	public static String[] getSignatureTypes(String sig) {
		ArrayList<String> parameterList = new ArrayList<String>();

		Integer index = 0;
		int len = sig.length();
		while (sig.charAt(index) != '(') {
			++index;
		}
		++index;
		while (sig.charAt(index) != ')') {
			int[] indexWrapper = { index };
			String typeString = getTypeString(sig, indexWrapper);
			parameterList.add(typeString);
			index = indexWrapper[0];
		}
		++index;
		int[] indexWrapper = {index};
		String typeString = getTypeString(sig, indexWrapper);
		parameterList.add(typeString);

		if (parameterList.size() != 0) {
			String[] parameterTypes = new String[parameterList.size()];
			parameterList.toArray(parameterTypes);
			return parameterTypes;
		} else {
			return null;
		}
	}

	/**
	 * Get the signature representation of a class type.
	 * @param classType: the class you want to convert to signature representation
	 * @return: return the signature representation of the given class
	 */
	public static String getClassSig(Class<?> classType) {
		String typeName = classType.getName();
		typeName = typeName.replace('.', '/');
		if (typeName.startsWith("[")) {
			return sigStringMap.getOrDefault(typeName, typeName);
		} else {
			return sigStringMap.getOrDefault(typeName, "L" + typeName + ";");
		}
	}

	/**
	 * Get the signature string of a given method with out return type.
	 * @param method: the method you want to get signature.
	 * @return : return the signature that can be recognized by jvm.
	 */
	public static String getSignatureWithoutReturnType(Method method) {
		Class<?>[] parameterTypes = method.getParameterTypes();
		StringBuilder signatureBuilder = new StringBuilder();
		signatureBuilder.append("(");
		for (Class<?> paramType : parameterTypes) {
			String paramSig = getClassSig(paramType);
			signatureBuilder.append(paramSig);
		}
		signatureBuilder.append(")");
		return signatureBuilder.toString();
	}

	/**
	 * Get the signature string of a given method.
	 * @param method: the method you want to get signature.
	 * @return : return the signature that can be recognized by jvm.
	 */
	public static String getSignature(Method method) {
		return getSignature(method, null);
	}

	/**
	 * Get the signature string of a given method.
	 * @param method: the method you want to get signature.
	 * @param returnClass: override the return type of the method when convert to signature, may be null.
	 * @return : return the signature that can be recognized by jvm.
	 */
	public static String getSignature(Method method, Class<?> returnClass) {
		Class<?>[] parameterTypes = method.getParameterTypes();
		StringBuilder signatureBuilder = new StringBuilder();
		signatureBuilder.append(getSignatureWithoutReturnType(method));

		Class<?> returnType = method.getReturnType();
		if (returnClass != null) {
			returnType = returnClass;
		}
		signatureBuilder.append(getClassSig(returnType));
		return signatureBuilder.toString();
	}
}
