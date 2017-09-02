package com.lvonce.binaryweaver;

import org.testng.annotations.*;
import static org.testng.Assert.*;

import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import static org.objectweb.asm.Opcodes.*;

public class ASMInsnMapperTest {

    private ClassWriter cw;
    private MethodVisitor mv;

    public ASMInsnMapperTest() {
        this.cw = new ClassWriter(0);
        this.mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(Ljava/lang/Object;)V", null, null);
    }

    @Test
    public void pushConstInsnTest() {
        assertEquals(ASMInsnMapper.pushConstInsn(mv, -1), ICONST_M1);
        assertEquals(ASMInsnMapper.pushConstInsn(mv, 0), ICONST_0);
        assertEquals(ASMInsnMapper.pushConstInsn(mv, 1), ICONST_1);
        assertEquals(ASMInsnMapper.pushConstInsn(mv, 2), ICONST_2);
        assertEquals(ASMInsnMapper.pushConstInsn(mv, 3), ICONST_3);
        assertEquals(ASMInsnMapper.pushConstInsn(mv, 4), ICONST_4);
        assertEquals(ASMInsnMapper.pushConstInsn(mv, 5), ICONST_5);
    }

}