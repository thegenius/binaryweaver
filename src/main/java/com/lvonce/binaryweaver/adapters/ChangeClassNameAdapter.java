package com.lvonce.binaryweaver.adapters;

import org.objectweb.asm.ClassVisitor;

public class ChangeClassNameAdapter extends ClassVisitor {
    private final String newClassName;

    public ChangeClassNameAdapter(int api, ClassVisitor cv, String newClassName) {
        super(api, cv);
        this.newClassName = newClassName;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        cv.visit(version, access, this.newClassName, signature, superName, interfaces);
    }
}