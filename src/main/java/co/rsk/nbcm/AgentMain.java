package co.rsk.nbcm;

import java.lang.instrument.Instrumentation;

/**
 * Main class for the rsk-nbcm monitor.
 */
public final class AgentMain {

    public static void premain(
            String agentArgs, Instrumentation inst) {

        System.out.println("[AgentMain] In premain method");
        transformClasses(inst);
    }

    public static void agentmain(
            String agentArgs, Instrumentation inst) {

        System.out.println("[AgentMain] In agentmain method");
        transformClasses(inst);
    }

    private static void transformClasses(Instrumentation inst) {
        ClassSurgeon surgeon = new ClassSurgeon();
        surgeon.transformMessageVisitor(inst);
        surgeon.transformMessageCodec(inst);
    }
}
