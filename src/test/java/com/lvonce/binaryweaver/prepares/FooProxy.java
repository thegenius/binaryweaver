package com.lvonce.binaryweaver;

public class FooProxy implements Foo, Reloadable<FooClass> {
    private FooClass foo;

    public FooProxy() {
        this.foo = new FooClass();
    }

    public FooProxy(int fieldA) {
        this.foo = new FooClass(fieldA);
    }

    public void func1() {
        this.foo.func1();
    }

    public int func2(int a, int b) {
        return this.foo.func2(a, b);
    }

    public int func3(int x) {
        return this.foo.func3(x);
    }

    public Object func4(Long x, int y, Object c) {
        return this.foo.func4(x, y, c);
    }

    public void setFoo(FooClass foo) {
        this.foo = foo;
    }

    public FooClass getFoo() {
        return this.foo;
    }

    public void __setReloadTarget__(FooClass target) {
        this.foo = target;
    }

}