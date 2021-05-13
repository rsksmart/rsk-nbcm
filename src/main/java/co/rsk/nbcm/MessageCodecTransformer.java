package co.rsk.nbcm;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;

/**
 * Instrumentation for the "org.ethereum.net.rlpx.MessageCodec" class.
 */
public class MessageCodecTransformer extends TransformerBase {

    public MessageCodecTransformer(String targetClassName, ClassLoader targetClassLoader) {
        super(targetClassName, targetClassLoader);
    }

    @Override
    void defineTransformation(CtClass cc) throws NotFoundException, CannotCompileException {
        defineMethodDecodeMessage(cc);
        defineMethodEncodeMessage(cc);
    }

    private void defineMethodDecodeMessage(CtClass cc) throws NotFoundException, CannotCompileException {
        CtMethod m = cc.getDeclaredMethod("decodeMessage");
        m.addLocalVariable("frameSize", CtClass.longType);
        m.addLocalVariable("blockHeight", CtClass.longType);
        m.insertBefore(
                "frameSize =  "
                        + "(frames.size() == 1) "
                        + " ? ((org.ethereum.net.rlpx.FrameCodec.Frame)(frames.get(0))).getSize() "
                        + " : ((org.ethereum.net.rlpx.FrameCodec.Frame)(frames.get(0))).totalFrameSize; "
        );
        String subMsg = "((co.rsk.net.eth.RskMessage)$_).getMessage()";
        String type = subMsg + ".getMessageType().toString()";
        m.insertAfter(
                "if (($r)$_ instanceof co.rsk.net.eth.RskMessage) { "
                        + "    blockHeight = (" + type + "==\"BLOCK_MESSAGE\") ? ((co.rsk.net.messages.BlockMessage)" + subMsg + ").getBlock().getNumber() : -1l ;"
                        + "    co.rsk.nbcm.DataFile.receiveMessage(frameSize," + type + ",blockHeight); "
                        + "} else {"
                        + "    co.rsk.nbcm.DataFile.receiveMessage(frameSize,((org.ethereum.net.message.Message)$_).getCommand().name(), -1l); "
                        + "}"
        );
    }

    private void defineMethodEncodeMessage(CtClass cc) throws NotFoundException, CannotCompileException {
        CtMethod m = cc.getDeclaredMethod("encode");
        m.addLocalVariable("frameSize", CtClass.longType);
        m.addLocalVariable("blockHeight", CtClass.longType);
        String subMsg = "((co.rsk.net.eth.RskMessage)msg).getMessage()";
        String type = subMsg + ".getMessageType().toString()";
        m.insertAfter(
                "frameSize = ((org.ethereum.net.message.Message)msg).getEncoded().length;"
                        + "if (msg instanceof co.rsk.net.eth.RskMessage) { "
                        + "    blockHeight = (" + type + "==\"BLOCK_MESSAGE\") ? ((co.rsk.net.messages.BlockMessage)" + subMsg + ").getBlock().getNumber() : -1l ;"
                        + "    co.rsk.nbcm.DataFile.sendMessage(frameSize," + type + ",blockHeight); "
                        + "} else {"
                        + "    co.rsk.nbcm.DataFile.sendMessage(frameSize,((org.ethereum.net.message.Message)msg).getCommand().name(), -1l); "
                        + "}"
        );
    }
}

