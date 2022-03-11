package tutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.reflect.Modifier.PRIVATE;
import static java.lang.reflect.Modifier.PROTECTED;
import static java.lang.reflect.Modifier.PUBLIC;
import static java.lang.reflect.Modifier.STATIC;
import static java.lang.reflect.Modifier.isInterface;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.ASM9;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.DRETURN;
import static org.objectweb.asm.Opcodes.FRETURN;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.LRETURN;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Type.getArgumentTypes;
import static org.objectweb.asm.Type.getMethodDescriptor;
import static tutor.MyTransformer.ByteUtils.createStaticMethod;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.sourcegrade.jagr.api.testing.ClassTransformer;

/**
 * The transformer class.
 */
public class MyTransformer implements ClassTransformer {

    public static <T> T createMock() {
        String n = Thread.currentThread().getStackTrace()[2].getClassName();
        //noinspection unchecked
        return (T) assertDoesNotThrow(() -> mock(Class.forName(n), CALLS_REAL_METHODS));
    }

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
                if (type == Type.BOOLEAN_TYPE || type == Type.BYTE_TYPE || type == Type.CHAR_TYPE
                    || type == Type.SHORT_TYPE || type == Type.INT_TYPE) {
                    visitor.visitVarInsn(Opcodes.ISTORE, start++);
                } else if (type == Type.LONG_TYPE) {
                    visitor.visitVarInsn(Opcodes.LSTORE, start++);
                } else if (type == Type.FLOAT_TYPE) {
                    visitor.visitVarInsn(Opcodes.FSTORE, start++);
                } else if (type == Type.DOUBLE_TYPE) {
                    visitor.visitVarInsn(Opcodes.DSTORE, start++);
                } else {
                    visitor.visitVarInsn(Opcodes.ASTORE, start++);
                }
            }
            return start - 1;
        }

        @SuppressWarnings("UnusedReturnValue")
        static int load(MethodVisitor visitor, List<Type> types, int start, boolean down) {
            for (Type type : types) {
                if (type == Type.BOOLEAN_TYPE || type == Type.BYTE_TYPE || type == Type.CHAR_TYPE
                    || type == Type.SHORT_TYPE || type == Type.INT_TYPE) {
                    visitor.visitVarInsn(Opcodes.ILOAD, start);
                } else if (type == Type.LONG_TYPE) {
                    visitor.visitVarInsn(Opcodes.LLOAD, start);
                } else if (type == Type.FLOAT_TYPE) {
                    visitor.visitVarInsn(Opcodes.FLOAD, start);
                } else if (type == Type.DOUBLE_TYPE) {
                    visitor.visitVarInsn(Opcodes.DLOAD, start);
                } else {
                    visitor.visitVarInsn(Opcodes.ALOAD, start);
                }
                start = start + (down ? -1 : 1);
            }
            return start + (down ? +1 : -1);
        }

        static void createReturn(MethodVisitor mv, String descriptor) {
            var type = Type.getReturnType(descriptor);
            if (type == Type.BOOLEAN_TYPE || type == Type.BYTE_TYPE || type == Type.CHAR_TYPE
                || type == Type.SHORT_TYPE || type == Type.INT_TYPE) {
                mv.visitInsn(IRETURN);
            } else if (type == Type.LONG_TYPE) {
                mv.visitInsn(LRETURN);
            } else if (type == Type.FLOAT_TYPE) {
                mv.visitInsn(FRETURN);
            } else if (type == Type.DOUBLE_TYPE) {
                mv.visitInsn(DRETURN);
            } else if (type == Type.VOID_TYPE) {
                mv.visitInsn(RETURN);
            } else {
                mv.visitInsn(ARETURN);
            }
        }

        static void createStaticMethod(MethodVisitor mv, String owner, String className, String methodName, String descriptor) {

            var m = assertDoesNotThrow(() -> MyTransformer.class.getMethod("createMock"));
            mv.visitMethodInsn(INVOKESTATIC, m.getDeclaringClass().getCanonicalName().replaceAll("\\.", "/"), m.getName(), getMethodDescriptor(m), false);
            mv.visitTypeInsn(CHECKCAST, owner);
            var arguments = List.of(Type.getArgumentTypes(descriptor));
            load(mv, List.of(Type.getArgumentTypes(descriptor)), 0, false);
            mv.visitMethodInsn(INVOKEVIRTUAL, owner, methodName.replaceAll("STATIC", ""), descriptor, false);
            createReturn(mv, descriptor);

            mv.visitMaxs(0, 0);
        }
    }

    static class MethodTransformer extends ClassVisitor {

        String owner = null;
        String className = null;
        int maxVar = 0;
        ClassWriter writer;
        boolean isInterface;

        public MethodTransformer(ClassWriter writer) {
            super(ASM9, writer);
            this.writer = writer;
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName,
                          String[] interfaces) {

            owner = name;
            className = name.substring(name.lastIndexOf("/") + 1);
            access &= ~PRIVATE;
            access &= ~PROTECTED;
            access |= PUBLIC;
            super.visit(version, access, name, signature, superName, interfaces);
        }

        @Override
        public void visitInnerClass(String name, String outerName, String innerName, int access) {

            access &= ~PRIVATE;
            access &= ~PROTECTED;
            access |= PUBLIC;
            super.visitInnerClass(name, outerName, innerName, access);
        }

        @Override
        public void visitOuterClass(String owner, String name, String descriptor) {

            super.visitOuterClass(owner, name, descriptor);
            this.owner = owner;
            className = name;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            // set access modifier to public
            access &= ~PRIVATE;
            access &= ~PROTECTED;
            access |= PUBLIC;
            boolean isStaticMethod = isStatic(access);
            // check if method is of a type which should not be transformed
            boolean isMainMethod = name.equals("main");
            boolean isConstructor = name.equals("<clinit>");
            boolean isLambdaMethod = name.startsWith("lambda$");
            boolean isInterface = isInterface(access);
            final boolean isProtected = isMainMethod || isConstructor || isLambdaMethod;
            if (isProtected)
                // method is not static or is forced to be static
                return super.visitMethod(access, name, descriptor, signature, exceptions);
            if (!isStaticMethod) {
                //noinspection UnnecessaryLocalVariable
                var visitor = new MethodVisitor(ASM9, super.visitMethod(access, name, descriptor, signature, exceptions)) {

                    @Override
                    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
                        boolean isStatic = opcode == INVOKESTATIC;
                        boolean isExercise = owner.startsWith("h07");
                        if (isStatic && isExercise) {
                            super.visitMethodInsn(opcode, owner, name + "STATIC", descriptor, isInterface);
                            return;
                        }
                        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                    }
                };

                return visitor;
            }

            var w = super.visitMethod(access, name + "STATIC", descriptor, signature, exceptions);
            createStaticMethod(w, owner, className, name, descriptor);
            w.visitMaxs(0, 0);
            // transform method to object method
            access ^= STATIC;
            var objectCaller = new MethodVisitor(ASM9, super.visitMethod(access, name, descriptor, signature, exceptions)) {

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
                    boolean sameClass = owner.contains(className);
                    boolean isExercise = owner.startsWith("h07") || owner.startsWith("tutor") || owner.startsWith("student") || owner.startsWith("reflection");
                    List<Type> types = stream(getArgumentTypes(descriptor)).collect(toList());
                    if (opcode == INVOKESTATIC && sameClass) {
                        int n = ByteUtils.store(this, types, maxVar);
                        super.visitVarInsn(Opcodes.ALOAD, 0);
                        ByteUtils.load(this, types, n, true);
                        super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, owner, name, descriptor, isInterface);
                    } else if (opcode == INVOKESTATIC && isExercise) {
                        super.visitMethodInsn(opcode, owner, name + "STATIC", descriptor, isInterface);
                    } else {

                        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
                    }
                }
            };
            objectCaller.visitMaxs(0, 0);
            return objectCaller;
        }
    }
}
