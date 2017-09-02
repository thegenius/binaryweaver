package com.lvonce.binaryweaver;

import java.util.UUID;
import java.lang.reflect.Method;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.FieldVisitor;

import static org.objectweb.asm.Opcodes.*;
import static com.lvonce.binaryweaver.ASMInsnMapper.*;

public class Joiner {

    public static <T> T create(Class<T> interfaceType, Object provider) throws NoSuchMethodException {
        return (T) create(interfaceType, provider, "call");
    }

    @SuppressWarnings("unchecked")
    public static <T> T create(Class<T> interfaceType, Object provider, String funcName) throws NoSuchMethodException {
        String interfaceName = interfaceType.getName().replace('.', '/');
        String routerName = interfaceName + UUID.randomUUID().toString().replace('-', '_');
        Class<?> providerClass = provider.getClass();
        String providerClassName = providerClass.getName().replace('.', '/');

        Class[] joinMethodParamTypes = new Class[] { String.class, String.class, Object[].class };
        Method joinMethod = providerClass.getDeclaredMethod(funcName, joinMethodParamTypes);

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        FieldVisitor fv = null;
        MethodVisitor mv = null;

        // create class
        cw.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER, routerName, null, "java/lang/Object",
                new String[] { interfaceName });

        // create service field
        {
            fv = cw.visitField(ACC_PRIVATE + ACC_FINAL, "service", "L" + providerClassName + ";", null, null);
            fv.visitEnd();
        }

        // create constructor
        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(L" + providerClassName + ";)V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitFieldInsn(PUTFIELD, routerName, "service", "L" + providerClassName + ";");
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 2);
            mv.visitEnd();
        }

        Method[] methods = interfaceType.getDeclaredMethods();
        for (Method method : methods) {
            String methodName = method.getName().replace('.', '/');
            String sigName = SignatureUtil.getSignature(method);
            String sigNameWithoutReturnType = SignatureUtil.getSignatureWithoutReturnType(method);
            Class<?>[] parameterTypes = method.getParameterTypes();
            int paramLength = parameterTypes.length;

            mv = cw.visitMethod(Opcodes.ACC_PUBLIC, methodName, sigName, null, null);
            mv.visitCode();

            mv.visitVarInsn(ALOAD, 0);
            mv.visitFieldInsn(GETFIELD, routerName, "service", "L" + providerClassName + ";");

            //mv.visitLdcInsn(interfaceName);
            mv.visitLdcInsn(methodName);
            mv.visitLdcInsn(sigNameWithoutReturnType);

            pushConstInsn(mv, paramLength);
            mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");

            for (int i = 0; i < paramLength; ++i) {
                String paramName = parameterTypes[i].getName().replace('.', '/');
                mv.visitInsn(DUP);
                pushConstInsn(mv, i);
                loadInsn(mv, paramName, i + 1);
                boxInsn(mv, paramName);
                mv.visitInsn(AASTORE);
            }

            //mv.visitMethodInsn(INVOKEVIRTUAL, providerClassName, funcName, "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/Object;", false);
            mv.visitMethodInsn(INVOKEVIRTUAL, providerClassName, funcName,
                    "(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/Object;", false);
            String returnTypeName = method.getReturnType().getName().replace('.', '/');
            castInsn(mv, returnTypeName);
            unboxInsn(mv, returnTypeName);
            returnInsn(mv, returnTypeName);
            mv.visitMaxs(7, 3);
            mv.visitEnd();
        }

        cw.visitEnd();

        byte[] data = cw.toByteArray();
        return (T) BinaryClassUtil.newInstance(data, provider);
    }
}
