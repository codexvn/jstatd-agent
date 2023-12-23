package top.codexvn;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

public class TimeClassVisitor extends ClassVisitor {
    private final int remoteHostPort;

    public TimeClassVisitor(int remoteHostPort, ClassVisitor classVisitor) {
        super(Opcodes.ASM5, classVisitor);
        this.remoteHostPort = remoteHostPort;
    }

    /**
     * 目标方法
     * public static Remote exportObject(Remote obj, int port) throws RemoteException
     */
    @Override
    public MethodVisitor visitMethod(int methodAccess, String methodName, String methodDesc, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = cv.visitMethod(methodAccess, methodName, methodDesc, signature, exceptions);
        if (!methodName.equals("exportObject") || !methodDesc.equals("(Ljava/rmi/Remote;I)Ljava/rmi/Remote;")) {
            return methodVisitor;
        }
        return new AdviceAdapter(Opcodes.ASM5, methodVisitor, methodAccess, methodName, methodDesc) {
            @Override
            protected void onMethodEnter() {
                mv.visitLdcInsn(remoteHostPort);  // 将常量推送到栈上
                mv.visitVarInsn(Opcodes.ISTORE, 1);  // 将常量存储到 port 参数位置
            }
        };
    }
}
