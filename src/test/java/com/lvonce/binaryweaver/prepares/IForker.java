
package com.lvonce.binaryweaver.prepares;

public interface IForker {
    public Object call(String methodName, String sigName, Object... args);
}