
package com.lvonce.binaryweaver;

import java.util.Map;
import java.util.UUID;
import java.util.LinkedHashMap;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;

import static org.objectweb.asm.Opcodes.*;
import static com.lvonce.binaryweaver.ASMInsnMapper.*;

public class Forker {

    public static <T, E> T create(Class<T> dispatcherInterface, E provider) {
        String providerClassName = provider.getClass().getName().replace('.', '/');
        String routerName = providerClassName + UUID.randomUUID().toString().replace('-', '_');
        String dispatcherName = dispatcherInterface.getName().replace('.', '/');

        Method[] methods = provider.getClass().getDeclaredMethods();
        LinkedHashMap<String, LinkedHashMap<String, Method>> methodMap = new LinkedHashMap<String, LinkedHashMap<String, Method>>();
        for (Method method : methods) {
            int modifier = method.getModifiers();
            if (Modifier.isPublic(modifier)) {
                String methodName = method.getName().replace('.', '/');
                String sigName = SignatureUtil.getSignatureWithoutReturnType(method);
                LinkedHashMap<String, Method> sigMethodMap = methodMap.get(methodName);
                if (sigMethodMap == null) {
                    sigMethodMap = new LinkedHashMap<String, Method>();
                    methodMap.put(methodName, sigMethodMap);
                }
                sigMethodMap.put(sigName, method);
            }
        }

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        FieldVisitor fv;
        MethodVisitor mv;
        cw.visit(52, ACC_PUBLIC + ACC_SUPER, routerName, null, "java/lang/Object", new String[] { dispatcherName });
        {
            fv = cw.visitField(ACC_PRIVATE + ACC_FINAL, "service", "L" + providerClassName + ";", null, null);
            fv.visitEnd();
        }

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

        {
            mv = cw.visitMethod(ACC_PUBLIC, "call",
                    "(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/Object;", null, null);
            mv.visitCode();

            Label nextMethodLabel = new Label();
            Label nextSigLabel = new Label();
            Label lastLabel = new Label();
            int methodCount = methodMap.size();
            int methodIndex = 0;
            int sigIndex = 0;
            for (Map.Entry<String, LinkedHashMap<String, Method>> entry : methodMap.entrySet()) {

                if (methodIndex != 0) {
                    mv.visitLabel(nextMethodLabel);
                    mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
                    nextMethodLabel = new Label();
                }

                String methodName = entry.getKey();
                LinkedHashMap<String, Method> sigMethodMap = entry.getValue();
                int sigCount = sigMethodMap.size();
                for (Map.Entry<String, Method> sigMethodEntry : sigMethodMap.entrySet()) {
                    String sigNameWithoutReturnType = sigMethodEntry.getKey();
                    Method method = sigMethodEntry.getValue();
                    String sigName = SignatureUtil.getSignature(method);
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    int paramLength = parameterTypes.length;

                    if (sigIndex == 0) {
                        mv.visitVarInsn(ALOAD, 1);
                        mv.visitLdcInsn(methodName);
                        mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
                        if (methodIndex == methodCount - 1) {
                            mv.visitJumpInsn(IFEQ, lastLabel);
                        } else {
                            mv.visitJumpInsn(IFEQ, nextMethodLabel);
                        }
                    } else {
                        mv.visitLabel(nextSigLabel);
                        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
                        nextSigLabel = new Label();
                    }

                    mv.visitVarInsn(ALOAD, 2);
                    mv.visitLdcInsn(sigNameWithoutReturnType);
                    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
                    if (sigIndex == sigCount - 1) {
                        mv.visitJumpInsn(IFEQ, lastLabel);
                    } else {
                        mv.visitJumpInsn(IFEQ, nextSigLabel);
                    }

                    mv.visitVarInsn(ALOAD, 0);
                    mv.visitFieldInsn(GETFIELD, routerName, "service", "L" + providerClassName + ";");
                    boolean singleArrayOfObject = false;
                    if (paramLength == 1) {
                        String paramTypeName = parameterTypes[0].getName().replace('.', '/');
                        if (paramTypeName.startsWith("[L")) {
                            singleArrayOfObject = true;
                            mv.visitVarInsn(ALOAD, 3);
                            mv.visitTypeInsn(CHECKCAST, paramTypeName);
                        }
                    }
                    if (!singleArrayOfObject) {
                        for (int j = 0; j < paramLength; ++j) {
                            String paramName = parameterTypes[j].getName().replace('.', '/');
                            mv.visitVarInsn(ALOAD, 3);
                            pushConstInsn(mv, j);
                            mv.visitInsn(AALOAD);
                            castInsn(mv, paramName);
                            unboxInsn(mv, paramName);
                        }
                    }

                    mv.visitMethodInsn(INVOKEVIRTUAL, providerClassName, methodName, sigName, false);

                    String returnTypeName = method.getReturnType().getName().replace('.', '/');
                    if (returnTypeName.equals("void")) {
                        mv.visitInsn(ACONST_NULL);
                        mv.visitInsn(ARETURN);
                    } else {
                        boxInsn(mv, returnTypeName);
                        mv.visitInsn(ARETURN);
                    }

                    ++sigIndex;
                }

                sigIndex = 0;
                ++methodIndex;
            }

            // final return null
            {
                mv.visitLabel(lastLabel);
                mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
                mv.visitInsn(ACONST_NULL);
                mv.visitInsn(ARETURN);
                mv.visitMaxs(4, 3);
                mv.visitEnd();
            }
        }
        cw.visitEnd();
        return (T) BinaryClassUtil.newInstance(cw.toByteArray(), provider);
    }
}
