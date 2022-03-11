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
import static org.mockito.Mockito.doAnswer;
import static student.PersonToIntFunction.Student.c;
import static student.PersonToIntFunction.Student.mApply;

public interface PersonToIntFunction {

    int apply(Person_STUD person);

    class Student {

        private static ClassTester<?> cPersonToIntFunction;
        private static MethodTester mApply;

        public static ClassTester<?> c() {
            return (cPersonToIntFunction = requireNonNullElseGet(cPersonToIntFunction, () -> new ClassTester<>(
                "h07.person",
                "PersonToIntFunction",
                SIMILARITY,
                PUBLIC | INTERFACE
            )).assureResolved());
        }

        public static MethodTester mApply() {
            return mApply = requireNonNullElseGet(mApply, () -> new MethodTester(
                c(),
                "apply",
                SIMILARITY,
                PUBLIC,
                int.class,
                List.of(new ParameterMatcher(Person_STUD.cPerson().getActualClass()))
            ).assureResolved());
        }
    }

    class Mock implements PersonToIntFunction, Mocked {

        private final Object object;

        public Mock(Object object) {
            this.object = requireNonNull(object);
            PersonToIntFunction.Student.c().setClassInstance(object);
        }

        public Mock(PersonToIntFunction personToIntFunction) {
            object = c().instantiate();
            //doAnswer(a -> personToIntFunction.apply(new Person_STUD(a.getArgument(0)))).when( mApply().invoke(object, any(Person_STUD.cPerson().getActualClass()));
            mApply().invoke(doAnswer(a -> personToIntFunction.apply(new Person_STUD(a.getArgument(0)))).when(object), any(Person_STUD.cPerson().getActualClass()));
        }

        @Override
        public int apply(Person_STUD person) {
            return mApply().invoke(object, person);
        }

        @Override
        public Object getActualObject() {
            return object;
        }
    }
}
