package co.rsk.nbcm;

import java.lang.instrument.Instrumentation;

public final class AgentMain {

    public static void premain(
            String agentArgs, Instrumentation inst) {

        System.out.println("[AgentMain] In premain method");
        ClassSurgeon surgeon = new ClassSurgeon();
        surgeon.transformMessageVisitor(inst);
        surgeon.transformMessageCodec(inst);
    }
    public static void agentmain(
            String agentArgs, Instrumentation inst) {

        System.out.println("[AgentMain] In agentmain method");
        String className = "com.baeldung.instrumentation.application.MyAtm";
      //  transformClass(className,inst);
    }
}
