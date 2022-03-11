package student;

import reflection.ClassTester;
import reflection.MethodTester;
import reflection.ParameterMatcher;

import java.util.List;
import java.util.function.IntBinaryOperator;

import static h07.Global.SIMILARITY;
import static h07.TestUtils.getArrayClass;
import static java.lang.reflect.Modifier.PUBLIC;
import static java.util.Objects.requireNonNullElseGet;
import static student.Person_STUD.cPerson;

public interface MyFunctionWithFilterMapAndFold1 {

    class Student {

        private static ClassTester<?> c;
        private static MethodTester mFilter;
        private static MethodTester mMap;
        private static MethodTester mFoldl;
        private static MethodTester mApply;
        private static MethodTester mMyFunctionWithFilterMapAndFold1;

        public static ClassTester<?> c() {
            //noinspection unchecked,rawtypes
            return (c = requireNonNullElseGet(c, () -> new ClassTester(
                "h07.person",
                "MyFunctionWithFilterMapAndFold1",
                SIMILARITY,
                PUBLIC,
                List.of(FunctionWithFilterMapAndFold.Student.c().getActualClass())
            )).assureResolved());
        }

        public static MethodTester mMyFunctionWithFilterMapAndFold1() {
            return mMyFunctionWithFilterMapAndFold1 = requireNonNullElseGet(mMyFunctionWithFilterMapAndFold1, () -> new MethodTester(
                c(),
                "h07.person.MyFunctionWithFilterMapAndFold1",
                SIMILARITY,
                PUBLIC,
                c().getActualClass(),
                List.of(
                    new ParameterMatcher(Traits.Student.c().getActualClass()))
            ).assureResolved());
        }

        public static MethodTester mFilter() {
            return mFilter = requireNonNullElseGet(mFilter, () -> new MethodTester(
                c(),
                "filter",
                SIMILARITY,
                PUBLIC,
                getArrayClass(cPerson().getActualClass(), 1),
                List.of(
                    new ParameterMatcher(PersonFilter.Student.c().getActualClass()),
                    new ParameterMatcher(getArrayClass(cPerson().getActualClass(), 1)))
            ).assureResolved());
        }

        public static MethodTester mMap() {
            return mMap = requireNonNullElseGet(mMap, () -> new MethodTester(
                c(),
                "map",
                SIMILARITY,
                PUBLIC,
                int[].class,
                List.of(
                    new ParameterMatcher(PersonToIntFunction.Student.c().getActualClass()),
                    new ParameterMatcher(getArrayClass(cPerson().getActualClass(), 1)))
            ).assureResolved());
        }

        public static MethodTester mFoldl() {
            return mFoldl = requireNonNullElseGet(mFoldl, () -> new MethodTester(
                c(),
                "foldl",
                SIMILARITY,
                PUBLIC,
                int.class,
                List.of(
                    new ParameterMatcher(IntBinaryOperator.class),
                    new ParameterMatcher(int.class),
                    new ParameterMatcher(int[].class))
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
