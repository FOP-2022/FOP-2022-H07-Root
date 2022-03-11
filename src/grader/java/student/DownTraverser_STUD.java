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
    private static MethodTester mGetFirstIndexO;
    private static MethodTester mGetNextIndexO;
    private final Object object;

    public DownTraverser_STUD() {
        object = mock(cDownTraverser().getActualClass(), CALLS_REAL_METHODS);
        cDownTraverser().setClassInstance(object);
    }

    public static ClassTester<?> cDownTraverser() {
        return cDownTraverserO = requireNonNullElseGet(cDownTraverserO, () -> new ClassTester<>(
            "h06",
            "DownTraverser",
            SIMILARITY,
            Modifier.PUBLIC).assureResolved());
    }

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

    @Override
    public int getFirstIndex(double[] array) {
        return mGetFirstIndexS().invoke(object, array);
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
