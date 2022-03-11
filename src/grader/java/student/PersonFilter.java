package student;

import reflection.ClassTester;
import reflection.MethodTester;
import reflection.ParameterMatcher;
import tutor.Mocked;

import java.util.List;

import static h07.Global.SIMILARITY;
import static java.lang.reflect.Modifier.INTERFACE;
import static java.lang.reflect.Modifier.PUBLIC;
import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElseGet;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static student.PersonFilter.Student.c;
import static student.PersonFilter.Student.mTest;

public interface PersonFilter {

    boolean test(Person_STUD person);

    class Student {

        private static ClassTester<?> cPersonFilter;
        private static MethodTester mTest;

        public static ClassTester<?> c() {
            return (cPersonFilter = requireNonNullElseGet(cPersonFilter, () -> new ClassTester<>(
                "h07.person",
                "PersonFilter",
                SIMILARITY,
                PUBLIC | INTERFACE
            )).assureResolved());
        }

        public static MethodTester mTest() {
            return mTest = requireNonNullElseGet(mTest, () -> new MethodTester(
                c(),
                "test",
                SIMILARITY,
                PUBLIC,
                boolean.class,
                List.of(new ParameterMatcher(Person_STUD.cPerson().getActualClass()))
            ).assureResolved());
        }
    }

    class Mock implements PersonFilter, Mocked {

        private final Object object;

        public Mock(Object object) {
            this.object = requireNonNull(object);
            c().setClassInstance(object);
        }

        public Mock(PersonFilter personFilter) {
            object = c().instantiate();
            c().setClassInstance(object);
            //mTest().invokany(Person_STUD.cPerson().getActualClass()e(doAnswer(a -> personFilter.test(new Person_STUD(a.getArgument(0)))).when(object), any(Person_STUD.cPerson().getActualClass()));


            when(mTest().invoke(object, any(Person_STUD.cPerson().getActualClass()))).thenAnswer(a -> personFilter.test(new Person_STUD(a.getArgument(0))));

            // doAnswer(a -> personFilter.test(new Person_STUD(a.getArgument(0)))).when(mTest().invoke(object, any(Person_STUD.cPerson().getActualClass())));
        }

        @Override
        public boolean test(Person_STUD person) {
            return mTest().invoke(object, person);
        }

        @Override
        public Object getActualObject() {
            return object;
        }
    }

}
