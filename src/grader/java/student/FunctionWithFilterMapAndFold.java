package student;

import reflection.*;

import java.lang.reflect.Array;
import java.util.List;

import static h07.Global.SIMILARITY;
import static java.lang.reflect.Modifier.*;
import static java.util.Objects.requireNonNullElseGet;
import static student.Person_STUD.cPerson;

public interface FunctionWithFilterMapAndFold {

    class Student {

        private static ClassTester<?> cFunctionWithFilterMapAndFold;
        private static AttributeTester aTraits;
        private static MethodTester mApply;

        public static ClassTester<?> c() {
            return (cFunctionWithFilterMapAndFold = requireNonNullElseGet(cFunctionWithFilterMapAndFold, () -> new ClassTester<>(
                "h07.person",
                "FunctionWithFilterMapAndFold",
                SIMILARITY,
                PUBLIC | ABSTRACT
            ))).assureResolved();
        }

        public static AttributeTester aTraits() {
            return aTraits = requireNonNullElseGet(aTraits, () ->
                new AttributeTester()
                    .setClassTester(c())
                    .setMatcher(
                        new AttributeMatcher(
                            "traits",
                            SIMILARITY,
                            PROTECTED | FINAL,
                            int.class))).assureResolved();
        }



        public static MethodTester mApply() {
            return (mApply = requireNonNullElseGet(mApply, () -> new MethodTester(
                c(),
                "apply",
                SIMILARITY,
                PUBLIC,
                int.class,
                List.of(new ParameterMatcher(Array.newInstance(cPerson().getActualClass(), 0).getClass()))
            ))).assureResolved();
        }

    }

}
