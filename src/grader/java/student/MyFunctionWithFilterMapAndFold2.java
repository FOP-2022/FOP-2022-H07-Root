package student;

import reflection.ClassTester;
import reflection.MethodTester;
import reflection.ParameterMatcher;

import java.util.List;

import static h07.Global.SIMILARITY;
import static h07.TestUtils.getArrayClass;
import static student.Person_STUD.cPerson;
import static java.lang.reflect.Modifier.PUBLIC;
import static java.util.Objects.requireNonNullElseGet;

public interface MyFunctionWithFilterMapAndFold2 {

    class Student {

        private static ClassTester<?> c;
        private static MethodTester mApply;
        private static MethodTester mMyFunctionWithFilterMapAndFold2;

        public static ClassTester<?> c() {
            return (c = requireNonNullElseGet(c, () -> new ClassTester(
                "h07.person",
                "MyFunctionWithFilterMapAndFold2",
                SIMILARITY,
                PUBLIC,
                List.of(FunctionWithFilterMapAndFold.Student.c().getActualClass())
            ))).assureResolved();
        }

        public static MethodTester mMyFunctionWithFilterMapAndFold2() {
            return mMyFunctionWithFilterMapAndFold2 = requireNonNullElseGet(mMyFunctionWithFilterMapAndFold2, () -> new MethodTester(
                c(),
                "h07.person.MyFunctionWithFilterMapAndFold2",
                SIMILARITY,
                PUBLIC,
                c().getActualClass(),
                List.of(
                    new ParameterMatcher(Traits.Student.c().getActualClass()))
            ).assureResolved());
        }

        public static MethodTester mApply() {
            return mApply = requireNonNullElseGet(mApply, () -> new MethodTester(
                c(),
                "apply",
                SIMILARITY,
                PUBLIC,
                int.class,
                List.of(
                    new ParameterMatcher(getArrayClass(cPerson().getActualClass(), 1)))
            ).assureResolved());
        }

    }

}
