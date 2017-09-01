package com.lvonce.binaryweaver.adapters;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Label;
import static org.objectweb.asm.Opcodes.*;

/*
visit 
	visitSource? 
	visitOuterClass? 
	( visitAnnotation | visitAttribute )*
	( visitInnerClass | visitField | visitMethod )*
visitEnd
*/
public class ClassPrintAdapter extends ClassVisitor {
    public ClassPrintAdapter() {
        super(ASM5);
    }

    public ClassPrintAdapter(int version, ClassVisitor cv) {
        super(version, cv);
    }

    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        System.out.println("visit(" + version + "," + access + "," + name + "," + signature + "," + superName + ","
                + interfaces + ")");
    }

    public void visitSource(String source, String debug) {
        System.out.println("visitSource(" + source + "," + debug + ")");
    }

    public void visitOuterClass(String owner, String name, String desc) {
        System.out.println("visitOuterClass(" + owner + "," + name + "," + desc + ")");
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        System.out.println("visitAnnotation(" + desc + "," + visible + ")");
        return null;
    }

    public void visitAttribute(Attribute attr) {
        System.out.println("visitAttribute(" + attr.toString() + ")");
    }

    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        System.out.println("visitInnerClass(" + name + "," + outerName + "," + innerName + "," + access + ")");
    }

    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        System.out.println("visitField(" + access + "," + name + "," + desc + "," + signature + "," + value + ")");
        return null;
    }

    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        System.out.println("visitMethod(" + access + "," + name + "," + desc + "," + signature + "," + exceptions + ")");
        return null;
    }

    public void visitEnd() {
        System.out.println("visitEnd");
    }
}