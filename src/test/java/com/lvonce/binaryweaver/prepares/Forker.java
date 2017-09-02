
package com.lvonce.binaryweaver.prepares;

public interface Forker {
    public Object call(String methodName, String sigName, Object... args);
}