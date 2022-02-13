package tutor;

import org.objectweb.asm.*;
import org.sourcegrade.jagr.api.testing.ClassTransformer;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.objectweb.asm.Type.getArgumentTypes;

public class MyTransformer implements ClassTransformer {

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void transform(ClassReader reader, ClassWriter writer) {
        reader.accept(new MethodTransformer(writer), 0);
    }

    @Override
    public int getWriterFlags() {
        return ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES;
    }

    interface ByteUtils {

        static int store(MethodVisitor visitor, List<Type> types, int start) {
            types = new ArrayList<>(types);
            Collections.reverse(types);
            for (Type type : types) {
                if (type == Type.BOOLEAN_TYPE || type == Type.BYTE_TYPE || type == Type.CHAR_TYPE || type == Type.SHORT_TYPE || type == Type.INT_TYPE) {
                    visitor.visitVarInsn(Opcodes.ISTORE, start++);
                } else if (type == Type.LONG_TYPE) {
                    visitor.visitVarInsn(Opcodes.LSTORE, start++);
                    //start++;
                } else if (type == Type.FLOAT_TYPE) {
                    visitor.visitVarInsn(Opcodes.FSTORE, start++);
                } else if (type == Type.DOUBLE_TYPE) {
                    visitor.visitVarInsn(Opcodes.DSTORE, start++);
                    //start++;
                } else {
                    visitor.visitVarInsn(Opcodes.ASTORE, start++);
                }
            }
            return start - 1;
        }

        @SuppressWarnings("UnusedReturnValue")
        static int load(MethodVisitor visitor, List<Type> types, int start) {
            for (Type type : types) {
                if (type == Type.BOOLEAN_TYPE || type == Type.BYTE_TYPE || type == Type.CHAR_TYPE || type == Type.SHORT_TYPE || type == Type.INT_TYPE) {
                    visitor.visitVarInsn(Opcodes.ILOAD, start--);
                } else if (type == Type.LONG_TYPE) {
                    visitor.visitVarInsn(Opcodes.LLOAD, start--);
                    //start--;
                } else if (type == Type.FLOAT_TYPE) {
                    visitor.visitVarInsn(Opcodes.FLOAD, start--);
                } else if (type == Type.DOUBLE_TYPE) {
                    visitor.visitVarInsn(Opcodes.DLOAD, start--);
                    //start--;
                } else {
                    visitor.visitVarInsn(Opcodes.ALOAD, start--);
                }

            }
            return start + 1;
        }
    }

    static class MethodTransformer extends ClassVisitor {

        String className;
        int maxVar = 0;

        public MethodTransformer(ClassWriter writer) {
            super(Opcodes.ASM9, writer);
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
            this.className = name;
            super.visit(version, access, name, signature, superName, interfaces);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {

            boolean isStatic = Modifier.isStatic(access);
            boolean isSynthetic = name.startsWith("lambda$");
            boolean forceStatic = isStatic && (name.equals("main") || name.equals("<clinit>") || isSynthetic) ;
            access &= ~Modifier.PRIVATE;
            access &= ~Modifier.PROTECTED;
            access |= Modifier.PUBLIC;

            if (isStatic && !forceStatic) {
                final int modifiedAccess = access ^ Modifier.STATIC;
                var mv = new MethodVisitor(Opcodes.ASM9, super.visitMethod(modifiedAccess, name, descriptor, signature, exceptions)) {

                    @Override
                    public void visitIincInsn(int var, int increment) {
                        var += 1;
                        maxVar = Math.max(maxVar, var);
                        super.visitIincInsn(var, increment);
                    }

                    @Override
                    public void visitVarInsn(int opcode, int var) {
                        var += 1;
                        maxVar = Math.max(maxVar, var);
                        super.visitVarInsn(opcode, var);
                    }

                    @Override
                    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                        List<Type> types = stream(getArgumentTypes(descriptor)).collect(toList());
                        if (opcode == Opcodes.INVOKESTATIC && className.equals(owner)) {
                            int n = ByteUtils.store(this, types, maxVar);
                            super.visitVarInsn(Opcodes.ALOAD, 0);
                            ByteUtils.load(this, types, n);
                            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, owner, name, descriptor, isInterface);

                        } else {
                            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                        }
                    }
                };
                mv.visitMaxs(0, 0);
                return mv;
            }
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }
    }
}
