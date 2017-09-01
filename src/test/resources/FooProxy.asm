package asm.com.lvonce.classbuilder;
import java.util.*;
import org.objectweb.asm.*;
public class FooProxyDump implements Opcodes {

public static byte[] dump () throws Exception {

ClassWriter cw = new ClassWriter(0);
FieldVisitor fv;
MethodVisitor mv;
AnnotationVisitor av0;

cw.visit(52, ACC_PUBLIC + ACC_SUPER, "com/lvonce/classbuilder/FooProxy", "Ljava/lang/Object;Lcom/lvonce/classbuilder/Foo;Lcom/lvonce/classbuilder/Reloadable<Lcom/lvonce/classbuilder/FooClass;>;", "java/lang/Object", new String[] { "com/lvonce/classbuilder/Foo", "com/lvonce/classbuilder/Reloadable" });

{
fv = cw.visitField(ACC_PRIVATE, "foo", "Lcom/lvonce/classbuilder/FooClass;", null, null);
fv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
mv.visitVarInsn(ALOAD, 0);
mv.visitTypeInsn(NEW, "com/lvonce/classbuilder/FooClass");
mv.visitInsn(DUP);
mv.visitMethodInsn(INVOKESPECIAL, "com/lvonce/classbuilder/FooClass", "<init>", "()V", false);
mv.visitFieldInsn(PUTFIELD, "com/lvonce/classbuilder/FooProxy", "foo", "Lcom/lvonce/classbuilder/FooClass;");
mv.visitInsn(RETURN);
mv.visitMaxs(3, 1);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(I)V", null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
mv.visitVarInsn(ALOAD, 0);
mv.visitTypeInsn(NEW, "com/lvonce/classbuilder/FooClass");
mv.visitInsn(DUP);
mv.visitVarInsn(ILOAD, 1);
mv.visitMethodInsn(INVOKESPECIAL, "com/lvonce/classbuilder/FooClass", "<init>", "(I)V", false);
mv.visitFieldInsn(PUTFIELD, "com/lvonce/classbuilder/FooProxy", "foo", "Lcom/lvonce/classbuilder/FooClass;");
mv.visitInsn(RETURN);
mv.visitMaxs(4, 2);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "func1", "()V", null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitFieldInsn(GETFIELD, "com/lvonce/classbuilder/FooProxy", "foo", "Lcom/lvonce/classbuilder/FooClass;");
mv.visitMethodInsn(INVOKEVIRTUAL, "com/lvonce/classbuilder/FooClass", "func1", "()V", false);
mv.visitInsn(RETURN);
mv.visitMaxs(1, 1);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "func2", "(II)I", null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitFieldInsn(GETFIELD, "com/lvonce/classbuilder/FooProxy", "foo", "Lcom/lvonce/classbuilder/FooClass;");
mv.visitVarInsn(ILOAD, 1);
mv.visitVarInsn(ILOAD, 2);
mv.visitMethodInsn(INVOKEVIRTUAL, "com/lvonce/classbuilder/FooClass", "func2", "(II)I", false);
mv.visitInsn(IRETURN);
mv.visitMaxs(3, 3);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "func3", "(I)I", null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitFieldInsn(GETFIELD, "com/lvonce/classbuilder/FooProxy", "foo", "Lcom/lvonce/classbuilder/FooClass;");
mv.visitVarInsn(ILOAD, 1);
mv.visitMethodInsn(INVOKEVIRTUAL, "com/lvonce/classbuilder/FooClass", "func3", "(I)I", false);
mv.visitInsn(IRETURN);
mv.visitMaxs(2, 2);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "func4", "(Ljava/lang/Long;ILjava/lang/Object;)Ljava/lang/Object;", null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitFieldInsn(GETFIELD, "com/lvonce/classbuilder/FooProxy", "foo", "Lcom/lvonce/classbuilder/FooClass;");
mv.visitVarInsn(ALOAD, 1);
mv.visitVarInsn(ILOAD, 2);
mv.visitVarInsn(ALOAD, 3);
mv.visitMethodInsn(INVOKEVIRTUAL, "com/lvonce/classbuilder/FooClass", "func4", "(Ljava/lang/Long;ILjava/lang/Object;)Ljava/lang/Object;", false);
mv.visitInsn(ARETURN);
mv.visitMaxs(4, 4);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "setFoo", "(Lcom/lvonce/classbuilder/FooClass;)V", null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitVarInsn(ALOAD, 1);
mv.visitFieldInsn(PUTFIELD, "com/lvonce/classbuilder/FooProxy", "foo", "Lcom/lvonce/classbuilder/FooClass;");
mv.visitInsn(RETURN);
mv.visitMaxs(2, 2);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "getFoo", "()Lcom/lvonce/classbuilder/FooClass;", null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitFieldInsn(GETFIELD, "com/lvonce/classbuilder/FooProxy", "foo", "Lcom/lvonce/classbuilder/FooClass;");
mv.visitInsn(ARETURN);
mv.visitMaxs(1, 1);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC, "__setReloadTarget__", "(Lcom/lvonce/classbuilder/FooClass;)V", null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitVarInsn(ALOAD, 1);
mv.visitFieldInsn(PUTFIELD, "com/lvonce/classbuilder/FooProxy", "foo", "Lcom/lvonce/classbuilder/FooClass;");
mv.visitInsn(RETURN);
mv.visitMaxs(2, 2);
mv.visitEnd();
}
{
mv = cw.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "__setReloadTarget__", "(Ljava/lang/Object;)V", null, null);
mv.visitCode();
mv.visitVarInsn(ALOAD, 0);
mv.visitVarInsn(ALOAD, 1);
mv.visitTypeInsn(CHECKCAST, "com/lvonce/classbuilder/FooClass");
mv.visitMethodInsn(INVOKEVIRTUAL, "com/lvonce/classbuilder/FooProxy", "__setReloadTarget__", "(Lcom/lvonce/classbuilder/FooClass;)V", false);
mv.visitInsn(RETURN);
mv.visitMaxs(2, 2);
mv.visitEnd();
}
cw.visitEnd();

return cw.toByteArray();
}
}
