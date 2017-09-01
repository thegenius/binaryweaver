package com.lvonce.binaryweaver;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import java.io.ByteArrayOutputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import static org.objectweb.asm.Opcodes.*;
import java.io.File;
import java.io.InputStream;
import java.io.FileOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Utils {
    private static Logger logger = LoggerFactory.getLogger(Utils.class);

    public static void writeToFile(String fileName, byte[] data) {
        try {
            File file = new File(fileName);
            FileOutputStream fout = new FileOutputStream(file);
            fout.write(data);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] streamToByteArray(InputStream stream) {
        try {
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            int count = 0;
            while ((count = stream.read(buffer)) != -1) {
                os.write(buffer, 0, count);
            }
            stream.close();
            os.flush();
            os.close();
            return os.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] getClassBytes(String classFullName) {
        String location = classFullName.replace(".", File.separator) + ".class";
        InputStream istream = ClassLoader.getSystemClassLoader().getResourceAsStream(location);
        return streamToByteArray(istream);
    }

    public static void printParamType(Class<?>[] paramTypes) {
        logger.info("printParamType", "------------------------------");
        for (Class<?> clazz : paramTypes) {
            logger.info("printParamType", "{}", clazz.toString());
        }
        logger.info("printParamType", "------------------------------");
    }

    public static byte[] transformClass(byte[] originClassBytes, Class<? extends ClassVisitor> adapterClass,
            Object... adapterInitArgs) {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        ClassReader cr = new ClassReader(originClassBytes);
        if (adapterInitArgs != null) {
            List<Class<?>> adapterInitParamTypes = new ArrayList<Class<?>>();
            adapterInitParamTypes.add(int.class);
            adapterInitParamTypes.add(ClassVisitor.class);
            List<Object> adapterInitParams = new ArrayList<Object>();
            adapterInitParams.add(ASM5);
            adapterInitParams.add(cw);

            for (Object param : adapterInitArgs) {
                adapterInitParamTypes.add(param.getClass());
                adapterInitParams.add(param);
            }
            Class<?>[] initParamTypes = adapterInitParamTypes.toArray(new Class<?>[adapterInitParamTypes.size()]);
            Object[] initPrams = adapterInitParams.toArray();
            printParamType(initParamTypes);
            ClassVisitor cv = BinaryClassUtil.constructInstance(adapterClass, initParamTypes, initPrams);
            logger.info("", "cv == null?: {}", cv == null);
            cr.accept(cv, 0);
            return cw.toByteArray();
        } else {
            Class<?>[] initParamTypes = new Class<?>[] { int.class, ClassVisitor.class };
            ClassVisitor cv = BinaryClassUtil.constructInstance(adapterClass, initParamTypes, ASM5, cw);
            cr.accept(cv, 0);
            return cw.toByteArray();
        }
    }

}