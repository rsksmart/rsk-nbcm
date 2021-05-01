package co.rsk.nbcm;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.util.function.BiFunction;

public final class ClassSurgeon {
    public void transformMessageVisitor(Instrumentation inst) {
        final String className = "co.rsk.net.messages.MessageVisitor";
        transformClass(className, inst, (n,l)->new MessageVisitorTransformer(n, l));
    }

    public void transformMessageCodec(Instrumentation inst) {
        final String className = "org.ethereum.net.rlpx.MessageCodec";
        transformClass(className, inst, (n,l)->new MessageCodecTransformer(n, l));
    }

    private static void transformClass(
            String className, Instrumentation instrumentation, BiFunction<String, ClassLoader, TransformerBase> transformMaker) {
        Class<?> targetCls = null;
        ClassLoader targetClassLoader = null;
        // see if we can get the class using forName
        try {
            targetCls = Class.forName(className);
            targetClassLoader = targetCls.getClassLoader();
            transform(targetCls, targetClassLoader, instrumentation, transformMaker);
            return;
        } catch (Exception ex) {
            System.out.println("Class [{}] not found with Class.forName");
        }
        // otherwise iterate all loaded classes and find what we want
        for(Class<?> clazz: instrumentation.getAllLoadedClasses()) {
            if(clazz.getName().equals(className)) {
                targetCls = clazz;
                targetClassLoader = targetCls.getClassLoader();
                transform(targetCls, targetClassLoader, instrumentation, transformMaker);
                return;
            }
        }
        throw new RuntimeException(
                "Failed to find class [" + className + "]");
    }

    private static void transform(
            Class<?> clazz,
            ClassLoader classLoader,
            Instrumentation instrumentation,
            BiFunction<String, ClassLoader, TransformerBase> trasformMaker) {
        ClassFileTransformer dt = trasformMaker.apply(clazz.getName(), classLoader);
        instrumentation.addTransformer(dt, true);
        try {
            instrumentation.retransformClasses(clazz);
        } catch (Exception ex) {
            throw new RuntimeException(
                    "Transform failed for: [" + clazz.getName() + "]", ex);
        }
    }
}
