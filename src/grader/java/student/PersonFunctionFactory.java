package student;

import reflection.*;

import java.util.List;

import static h07.Global.SIMILARITY;
import static java.lang.reflect.Modifier.*;
import static java.util.Objects.requireNonNullElseGet;

public interface PersonFunctionFactory {

    class Student {

        private static ClassTester<?> cPersonFunctionFactory;
        private static AttributeTester aFirstImplementationActive;
        private static MethodTester mIsFirstImplementationActive;
        private static MethodTester mCreateStrangeThingsFunction;
        private static MethodTester mDistance;
        private static MethodTester mSetFirstImplementationActive;
        private static MethodTester mCreateFunctionWithFilterMapAndFold;

        public static ClassTester<?> c() {
            return (cPersonFunctionFactory = requireNonNullElseGet(cPersonFunctionFactory, () -> new ClassTester(
                "h07.person",
                "PersonFunctionFactory",
                SIMILARITY,
                PUBLIC,
                List.of()
            ))).assureResolved();
        }

        public static AttributeTester aFirstImplementationActive() {
            return aFirstImplementationActive = requireNonNullElseGet(aFirstImplementationActive, () ->
                new AttributeTester()
                    .setClassTester(c())
                    .setMatcher(
                        new AttributeMatcher(
                            "firstImplementationActive",
                            SIMILARITY,
                            PRIVATE | STATIC,
                            boolean.class))).assureResolved();
        }

        public static MethodTester mIsFirstImplementationActive() {
            return (mIsFirstImplementationActive = requireNonNullElseGet(mIsFirstImplementationActive, () -> new MethodTester(
                c(),
                "isFirstImplementationActive",
                SIMILARITY,
                PUBLIC,
                boolean.class,
                List.of()
            ))).assureResolved();
        }

        public static MethodTester mSetFirstImplementationActive() {
            return (mSetFirstImplementationActive = requireNonNullElseGet(mSetFirstImplementationActive, () -> new MethodTester(
                c(),
                "setFirstImplementationActive",
                SIMILARITY,
                PUBLIC,
                void.class,
                List.of(new ParameterMatcher(boolean.class))
            ))).assureResolved();
        }

        public static MethodTester mCreateStrangeFunction() {
            return (mCreateStrangeThingsFunction = requireNonNullElseGet(mCreateStrangeThingsFunction, () -> new MethodTester(
                c(),
                "createStrangeFunction",
                SIMILARITY,
                PUBLIC,
                FunctionWithFilterMapAndFold.Student.c().getActualClass(),
                List.of(new ParameterMatcher(String.class))
            ))).assureResolved();
        }

        public static MethodTester mDistance() {
            return (mDistance = requireNonNullElseGet(mDistance, () -> new MethodTester(
                c(),
                "distance",
                SIMILARITY,
                PUBLIC,
                FunctionWithFilterMapAndFold.Student.c().getActualClass(),
                List.of()
            ))).assureResolved();
        }

        public static MethodTester mCreateFunctionWithFilterMapAndFold() {
            return (mCreateFunctionWithFilterMapAndFold = requireNonNullElseGet(mCreateFunctionWithFilterMapAndFold, () -> new MethodTester(
                c(),
                "createFunctionWithFilterMapAndFold",
                SIMILARITY,
                PUBLIC,
                FunctionWithFilterMapAndFold.Student.c().getActualClass(),
                List.of(new ParameterMatcher(Traits.Student.c().getActualClass()))
            ))).assureResolved();
        }

    }

}
