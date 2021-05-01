package co.rsk.nbcm;

import javassist.*;

    public class MessageVisitorTransformer extends TransformerBase {

        public MessageVisitorTransformer(String targetClassName, ClassLoader targetClassLoader) {
            super(targetClassName, targetClassLoader);
        }

        @Override
        void defineTransformation(CtClass cc) throws NotFoundException, CannotCompileException {
            CtMethod m = cc.getDeclaredMethod("apply");
            m.insertBefore("co.rsk.nbcm.DataFile.setBlockHeight(message.getBlock().getNumber());");
        }
    }
