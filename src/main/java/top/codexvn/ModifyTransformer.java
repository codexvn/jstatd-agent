package top.codexvn;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;


public class ModifyTransformer implements ClassFileTransformer {
    private final int remoteHostPort;
    static String targetVMClassName = "java.rmi.server.UnicastRemoteObject";

    public ModifyTransformer(int remoteHostPort) {
        this.remoteHostPort = remoteHostPort;
    }

    @Override
    public byte[] transform(Module module, ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {

        if (className.equals(targetVMClassName.replaceAll("\\.", "/"))) {
            System.out.println("Module: " + module);
            System.out.println("Found class: " + className);
            ClassReader reader = new ClassReader(classfileBuffer);
            ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
            System.out.println("remoteHostPort: " + remoteHostPort);
            reader.accept(new TimeClassVisitor(remoteHostPort, writer), ClassReader.EXPAND_FRAMES);
            return writer.toByteArray();
        }
        return classfileBuffer;
    }
}
