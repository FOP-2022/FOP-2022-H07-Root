package student;

import org.jetbrains.annotations.NotNull;
import reflection.AttributeMatcher;
import reflection.AttributeTester;
import reflection.ClassTester;
import tutor.Mocked;

import java.lang.reflect.Modifier;
import java.util.Objects;

import static h07.Global.SIMILARITY;
import static java.util.Objects.requireNonNull;

public class ReturnData_STUD implements Mocked {

    private static ClassTester<?> cReturnDataO;
    private static AttributeTester aResultO;
    private static AttributeTester aNextIndexO;
    private final Object object;

    public ReturnData_STUD(Object object) {
        this.object = requireNonNull(object);
    }

    public ReturnData_STUD() {
        this(cReturnData().instantiate());
    }


    public ReturnData_STUD(int result, int nextIndex) {
        this(cReturnData().instantiate());
        setResult(result);
        setNextIndex(nextIndex);
    }

    public static ClassTester<?> cReturnData() {
        return cReturnDataO = Objects.requireNonNullElseGet(cReturnDataO, () -> new ClassTester<>(
            "h06",
            "ReturnData",
            SIMILARITY).assureResolved());
    }

    public static AttributeTester aResult() {
        return aResultO = Objects.requireNonNullElseGet(aResultO, () ->
            new AttributeTester()
                .setClassTester(cReturnData())
                .setMatcher(
                    new AttributeMatcher(
                        "result",
                        SIMILARITY,
                        Modifier.PUBLIC,
                        int.class)).assureResolved());
    }

    public static AttributeTester aNextIndex() {
        return aNextIndexO = Objects.requireNonNullElseGet(aNextIndexO, () -> new AttributeTester()
            .setClassTester(cReturnData())
            .setMatcher(
                new AttributeMatcher(
                    "nextIndex",
                    SIMILARITY,
                    Modifier.PUBLIC,
                    int.class)
            ).assureResolved());
    }

    public int getResult() {
        return aResult().getValue(object);
    }

    public void setResult(int result) {
        aResult().setValue(object, result);
    }

    public int getNextIndex() {
        return aNextIndex().getValue(object);
    }

    public void setNextIndex(int result) {
        aNextIndex().setValue(object, result);
    }

    public @NotNull
    Object getActualObject() {
        return object;
    }
}
