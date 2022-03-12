package student;

import reflection.ClassTester;
import reflection.MethodTester;
import reflection.ParameterMatcher;

import java.util.List;

import static h07.Global.SIMILARITY;
import static java.lang.reflect.Modifier.PUBLIC;
import static java.util.Objects.requireNonNullElseGet;

public interface MyFunctionWithAdjacent extends FunctionWithFilterMapAndFold {

    class Student {

        private static ClassTester<?> c;
        private static MethodTester mConstructor;

        public static ClassTester<?> c() {
            return (c = requireNonNullElseGet(c, () -> new ClassTester(
                "h07.person",
                "MyFunctionWithAdjacent",
                SIMILARITY,
                PUBLIC,
                FunctionWithFilterMapAndFold.Student.c().getActualClass(),
                List.of()
            )).assureResolved());
        }

        public static MethodTester mConstructor() {
            return (mConstructor = requireNonNullElseGet(mConstructor, () -> new MethodTester(
                c(),
                "h07.person.MyFunctionWithAdjacent",
                SIMILARITY,
                PUBLIC,
                c().getActualClass(),
                List.of(new ParameterMatcher(Traits.Student.c().getActualClass()))
            ))).assureResolved();
        }
    }
}
