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
import static com.lvonce.binaryweaver.ASMInsnMapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.lvonce.binaryweaver.SignatureUtil;

public class DelegateMethodAdapter extends ClassVisitor {
    private static final Logger logger = LoggerFactory.getLogger(DelegateMethodAdapter.class);
    private final String proxyClassName;
    private String targetClassName = null;

    public DelegateMethodAdapter(int api, ClassVisitor cv, String proxyClassName) {
        super(api, cv);
        this.proxyClassName = proxyClassName;
    }

    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        logger.debug("visit({}, {}, {}, {}, {}, {})", version, access, name, signature, superName, interfaces);
        this.targetClassName = name;
        int interfacesNum = interfaces.length;
        String[] proxyInterfaces = new String[interfacesNum + 1];
        for (int i = 0; i < interfaces.length; ++i) {
            proxyInterfaces[i] = interfaces[i];
        }
        proxyInterfaces[interfacesNum] = "com/lvonce/binaryweaver/Reloadable";
        signature = signature + "Lcom/lvonce/binaryweaver/Reloadable<L" + this.targetClassName + ";>;";
        cv.visit(version, access, this.proxyClassName, signature, superName, proxyInterfaces);

        // add __reloadTarget__ field
        FieldVisitor fv = cv.visitField(ACC_PRIVATE, "__reloadTarget__", "L" + this.targetClassName + ";", null, null);
        fv.visitEnd();

        // add setter
        MethodVisitor mv = cv.visitMethod(ACC_PUBLIC, "__setReloadTarget__", "(L"+this.targetClassName+";)V",
                null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitFieldInsn(PUTFIELD, this.proxyClassName, "__reloadTarget__", "L"+this.targetClassName+";");
        mv.visitInsn(RETURN);
        mv.visitMaxs(2, 2);
        mv.visitEnd();

        // add bridge function
        mv = cv.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "__setReloadTarget__", "(Ljava/lang/Object;)V",
                null, null);
        mv.visitCode();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 1);
        mv.visitTypeInsn(CHECKCAST, this.targetClassName);
        mv.visitMethodInsn(INVOKEVIRTUAL, this.proxyClassName, "__setReloadTarget__",
                "(L" + this.targetClassName + ";)V", false);
        mv.visitInsn(RETURN);
        mv.visitMaxs(2, 2);
        mv.visitEnd();
    }

    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        logger.debug("visitMethod", "{}, {}, {}, {}, {}", access, name, desc, signature, exceptions);
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        String[] sigTypes = SignatureUtil.getSignatureTypes(desc);
        if (name.equals("<init>")) {
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitTypeInsn(NEW, this.targetClassName);
            // load prarm
            mv.visitInsn(DUP);
            for (int i = 0; i < sigTypes.length - 1; ++i) {
                loadInsn(mv, sigTypes[i], i + 1);
            }
            mv.visitMethodInsn(INVOKESPECIAL, this.targetClassName, "<init>", desc, false);
            mv.visitFieldInsn(PUTFIELD, this.proxyClassName, "__reloadTarget__", "L" + this.targetClassName + ";");
            mv.visitInsn(RETURN);
            mv.visitMaxs(3, 1);
            mv.visitEnd();
        } else {
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, this.proxyClassName, "__reloadTarget__", "L" + this.targetClassName + ";");
            // load param
            for (int i = 0; i < sigTypes.length - 1; ++i) {
                loadInsn(mv, sigTypes[i], i + 1);
            }
            mv.visitMethodInsn(INVOKEVIRTUAL, this.targetClassName, name, desc, false);
            // return
            returnInsn(mv, sigTypes[sigTypes.length - 1], false);
            mv.visitMaxs(4, 4);
            mv.visitEnd();
        }
        return null;
    }
}
