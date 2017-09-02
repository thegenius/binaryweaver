package com.lvonce.binaryweaver;

    public class FooClass implements Foo {
        private int fieldA;
        public FooClass() {
            System.out.println("FooClass init");
        }
        public FooClass(int fieldA) {
            System.out.println("FooClass init");
            this.fieldA = fieldA;
        }

        public void func1() {
            System.out.println("FooClass func1()");
        }

        public int func2(int a, int b) {
            System.out.println("FooClass func2("+ a +","+ b +")");
            return a + b + 1;
        }

        public int func3(int x) {
            System.out.println("FooClass func3("+ x +")");
            return this.fieldA * x;
        }

        public Object func4(Long x, int y, Object c) {
            System.out.println("" + x +"," + y + "," + c );
            return null;
        }

    }