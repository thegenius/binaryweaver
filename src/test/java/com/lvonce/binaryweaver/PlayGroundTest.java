package com.lvonce.binaryweaver;

import java.util.List;
import java.util.ArrayList;

import org.testng.annotations.Test;

public class PlayGroundTest {

    /**
     * the snippet is:
     *      T[] array = list.toArray(new T[list.size()]);
     */
    public static void listToArraySnippet() {
        List<String> list = new ArrayList<>();
        list.add("hello");
        list.add("hello");
        list.add("hello");

        Object[] array1 = list.toArray();
        for (Object e : array1) {
            System.out.println(e);
        }

        String[] array2 = list.toArray(new String[list.size()]);
        for (Object e : array2) {
            System.out.println(e);
        }
    }

    public static class Hello {
        public void hello() {
            System.out.println("hello");
        }
    }

    @Test
    public static void anonymousInnerClass() {
        Hello hello = new Hello() {
            {
                System.out.println("initialize block");
            }
            @Override
            public void hello() {
                System.out.println("updated hello");
            }
        };
        hello.hello();
        System.out.println(hello.getClass().getName());
    }

    public static void main(String[] args) {
        listToArraySnippet();
        // anonymousInnerClass();
    }

}