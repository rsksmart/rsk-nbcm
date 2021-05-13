package co.rsk.nbcm;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * Added methods for the "co.rsk.net.messages.MessageVisitor" class.
 */
public class MessageVisitorTransformer extends TransformerBase {

    public MessageVisitorTransformer(String targetClassName, ClassLoader targetClassLoader) {
        super(targetClassName, targetClassLoader);
    }

    @Override
    void defineTransformation(CtClass cc) throws NotFoundException, CannotCompileException {
        CtMethod mApply = cc.getDeclaredMethod("apply");
        mApply.insertBefore("co.rsk.nbcm.DataFile.applyBlockBegin();");
        mApply.insertAfter("co.rsk.nbcm.DataFile.applyBlockEnd();");

        CtMethod mRelay = cc.getDeclaredMethod("tryRelayBlock");
        mRelay.insertBefore("co.rsk.nbcm.DataFile.setIsBestBlock(((co.rsk.net.BlockProcessResult)result).isBest());");
    }
}
