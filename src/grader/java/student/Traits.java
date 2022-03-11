package student;

import reflection.AttributeMatcher;
import reflection.AttributeTester;
import reflection.ClassTester;
import reflection.MethodTester;
import tutor.Mocked;

import java.util.List;
import java.util.function.IntBinaryOperator;

import static h07.Global.SIMILARITY;
import static java.lang.reflect.Modifier.FINAL;
import static java.lang.reflect.Modifier.PRIVATE;
import static java.lang.reflect.Modifier.PUBLIC;
import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElseGet;
import static org.mockito.Mockito.doAnswer;
import static student.Traits.Student.c;
import static student.Traits.Student.mGetCombine;
import static student.Traits.Student.mGetFct;
import static student.Traits.Student.mGetInit;
import static student.Traits.Student.mGetOp;
import static student.Traits.Student.mGetPred;

public interface Traits {

    IntBinaryOperator getOp();

    int getInit();

    PersonToIntFunction getFct();

    PersonFilter getPred();

    IntBinaryOperator getCombine();

    class Student {

        private static ClassTester<?> c;
        private static MethodTester mGetOp;
        private static MethodTester mGetInit;
        private static MethodTester mGetFct;
        private static MethodTester mGetPred;
        private static MethodTester mGetCombine;
        private static AttributeTester aOp;
        private static AttributeTester aInit;
        private static AttributeTester aFct;
        private static AttributeTester aPred;
        private static AttributeTester aCombine;

        public static AttributeTester aOp() {
            return aOp = requireNonNullElseGet(aOp, () ->
                new AttributeTester()
                    .setClassTester(c())
                    .setMatcher(
                        new AttributeMatcher(
                            "op",
                            SIMILARITY,
                            PRIVATE | FINAL,
                            IntBinaryOperator.class)).assureResolved());
        }

        public static AttributeTester aInit() {
            return aInit = requireNonNullElseGet(aInit, () ->
                new AttributeTester()
                    .setClassTester(c())
                    .setMatcher(
                        new AttributeMatcher(
                            "init",
                            SIMILARITY,
                            PRIVATE | FINAL,
                            int.class)).assureResolved());
        }

        public static AttributeTester aFct() {
            return aFct = requireNonNullElseGet(aFct, () ->
                new AttributeTester()
                    .setClassTester(c())
                    .setMatcher(
                        new AttributeMatcher(
                            "fct",
                            SIMILARITY,
                            PRIVATE | FINAL,
                            PersonToIntFunction.Student.c().getActualClass())).assureResolved());
        }

        public static AttributeTester aPred() {
            return aPred = requireNonNullElseGet(aPred, () ->
                new AttributeTester()
                    .setClassTester(c())
                    .setMatcher(
                        new AttributeMatcher(
                            "pred",
                            SIMILARITY,
                            PRIVATE | FINAL,
                            PersonFilter.Student.c().getActualClass())).assureResolved());
        }

        public static AttributeTester aCombine() {
            return aCombine = requireNonNullElseGet(aCombine, () ->
                new AttributeTester()
                    .setClassTester(c())
                    .setMatcher(
                        new AttributeMatcher(
                            "combine",
                            SIMILARITY,
                            PRIVATE | FINAL,
                            IntBinaryOperator.class)).assureResolved());
        }

        public static ClassTester<?> c() {
            return (c = requireNonNullElseGet(c, () -> new ClassTester<>(
                "h07.person",
                "Traits",
                SIMILARITY,
                PUBLIC
            ))).assureResolved();
        }

        public static MethodTester mGetOp() {
            return mGetOp = requireNonNullElseGet(mGetOp, () -> new MethodTester(
                c(),
                "getOp",
                SIMILARITY,
                PUBLIC,
                IntBinaryOperator.class,
                List.of()
            ).assureResolved());
        }

        public static MethodTester mGetInit() {
            return mGetInit = requireNonNullElseGet(mGetInit, () -> new MethodTester(
                c(),
                "getInit",
                SIMILARITY,
                PUBLIC,
                int.class,
                List.of()
            ).assureResolved());
        }

        public static MethodTester mGetFct() {
            return mGetFct = requireNonNullElseGet(mGetFct, () -> new MethodTester(
                c(),
                "getFct",
                SIMILARITY,
                PUBLIC,
                PersonToIntFunction.Student.c().getActualClass(),
                List.of()
            ).assureResolved());
        }

        public static MethodTester mGetPred() {
            return mGetPred = requireNonNullElseGet(mGetPred, () -> new MethodTester(
                c(),
                "getPred",
                SIMILARITY,
                PUBLIC,
                PersonFilter.Student.c().getActualClass(),
                List.of()
            ).assureResolved());
        }

        public static MethodTester mGetCombine() {
            return mGetCombine = requireNonNullElseGet(mGetCombine, () -> new MethodTester(
                c(),
                "getCombine",
                SIMILARITY,
                PUBLIC,
                IntBinaryOperator.class,
                List.of()
            ).assureResolved());
        }
    }

    class Mock implements Traits, Mocked {

        private final Object object;

        public Mock(Object object) {
            this.object = requireNonNull(object);
            Traits.Student.c().setClassInstance(object);
        }

        public Mock(IntBinaryOperator op, int init, PersonToIntFunction.Mock fct, PersonFilter.Mock pred, IntBinaryOperator combine) {
            System.out.println("TEST");
            object = c().instantiate();

//            mGetOp().invoke();
            System.out.println(">" + object.getClass());
            mGetOp().invoke(doAnswer(a -> op).when(object));
            mGetInit().invoke(doAnswer(a -> init).when(object));
            mGetFct().invoke(doAnswer(a -> fct.getActualObject()).when(object));
            mGetPred().invoke(doAnswer(a -> pred.getActualObject()).when(object));
            if (Student.mGetCombine().resolved())
                mGetCombine().invoke(doAnswer(a -> combine).when(object));
        }

        @Override
        public IntBinaryOperator getOp() {
            return mGetOp().invoke(object);
        }

        @Override
        public int getInit() {
            return mGetInit().invoke(object);
        }

        @Override
        public PersonToIntFunction.Mock getFct() {
            return new PersonToIntFunction.Mock((Object) mGetFct().invoke(object));
        }

        @Override
        public PersonFilter.Mock getPred() {
            return new PersonFilter.Mock((Object) mGetPred().invoke(object));
        }

        @Override
        public IntBinaryOperator getCombine() {
            return mGetCombine().invoke(object);
        }

        @Override
        public Object getActualObject() {
            return object;
        }
    }
}
