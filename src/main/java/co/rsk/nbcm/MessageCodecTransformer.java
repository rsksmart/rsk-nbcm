package co.rsk.nbcm;

import javassist.*;

public class MessageCodecTransformer extends TransformerBase {

    public MessageCodecTransformer(String targetClassName, ClassLoader targetClassLoader) {
        super(targetClassName, targetClassLoader);
    }

    @Override
    void defineTransformation(CtClass cc) throws NotFoundException, CannotCompileException {
        CtMethod m = cc.getDeclaredMethod("decodeMessage");
        m.addLocalVariable("frameType", CtClass.longType);
        m.addLocalVariable("frameSize", CtClass.longType);

        m.insertBefore(
                "frameType = ((org.ethereum.net.rlpx.FrameCodec.Frame)frames.get(0)).getType(); "
                +"frameSize = ((org.ethereum.net.rlpx.FrameCodec.Frame)frames.get(0)).getSize(); "
                +"co.rsk.nbcm.DataFile.writeReceived(\"(\"+frameType+\")\",frameSize);"
        );
        // co.rsk.net.messages.MessageType.values()[(int)frameType].name()+
      //  m.insertBefore(
        //        "frameSize = (long)9999;");//((org.ethereum.net.rlpx.FrameCodec.Frame)frames.get(0)).getSize();");
       // m.
        //m.insertBefore("co.rsk.nbcm.DataFile.writeReceived(\"frameType\",frameSize);");
      //  m.insertBefore("co.rsk.nbcm.DataFile.writeReceived(\"gaga\",(long)frameSize);");

    }
}

