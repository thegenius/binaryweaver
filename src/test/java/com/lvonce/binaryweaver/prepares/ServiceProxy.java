package com.lvonce.binaryweaver.prepares;

public interface ServiceProxy {
    public RpcFuture func1();

    public RpcFuture func2(int a);

    public RpcFuture func3(int[] a);

    public RpcFuture func4(Integer[] a);
}