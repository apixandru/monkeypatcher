package com.apixandru.monkeypatcher.patchers.quickreturn;

import com.apixandru.monkeypatcher.patchers.AbstractObjectWebPatcher;
import jdk.internal.org.objectweb.asm.tree.InsnNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;

import static jdk.internal.org.objectweb.asm.Opcodes.RETURN;

public final class QuickReturn extends AbstractObjectWebPatcher<QuickReturnConfig> {

    public QuickReturn(QuickReturnConfig config) {
        super(config);
    }

    @Override
    protected void doPatchMethod(MethodNode methodNode, String methodName, String className) {
        methodNode.instructions.insert(new InsnNode(RETURN));
    }

}
