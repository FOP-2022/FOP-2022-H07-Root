package student;

import reflection.ClassTester;
import reflection.MethodTester;
import reflection.ParameterMatcher;

import java.lang.reflect.Modifier;
import java.util.List;

import static h07.Global.SIMILARITY;
import static java.lang.reflect.Modifier.PUBLIC;
import static java.util.Objects.requireNonNullElseGet;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mock;

public class DownTraverser_STUD extends Traverser_STUD {

    private static ClassTester<?> cDownTraverserO;

    public static ClassTester<?> cDownTraverser() {
        return cDownTraverserO = requireNonNullElseGet(cDownTraverserO, () -> new ClassTester<>(
            "h06",
            "DownTraverser",
            SIMILARITY,
            Modifier.PUBLIC).assureResolved());
    }

    private static MethodTester mGetFirstIndexO;

    public static MethodTester mGetFirstIndexS() {
        return mGetFirstIndexO = requireNonNullElseGet(mGetFirstIndexO, () -> new MethodTester(
            cDownTraverser(),
            "getFirstIndex",
            SIMILARITY,
            PUBLIC,
            int.class,
            List.of(new ParameterMatcher(double[].class))
        ).assureResolved());
    }

    private static MethodTester mGetNextIndexO;

    public static MethodTester mGetNextIndexS() {
        return mGetNextIndexO = requireNonNullElseGet(mGetNextIndexO, () -> new MethodTester(
            cDownTraverser(),
            "getNextIndex",
            SIMILARITY,
            PUBLIC,
            int.class,
            List.of(new ParameterMatcher(int.class))
        ).assureResolved());
    }

    private final Object object;

    public DownTraverser_STUD() {
        object = mock(cDownTraverser().getTheClass(), CALLS_REAL_METHODS);
        cDownTraverser().setClassInstance(object);
    }

    @Override
    public int getFirstIndex(double[] array) {
        return mGetFirstIndexS().invoke(object, (Object) array);
    }

    @Override
    public int getNextIndex(int index) {
        return mGetNextIndexS().invoke(object, index);
    }

    @Override
    public Object getActualObject() {
        return object;
    }
}
