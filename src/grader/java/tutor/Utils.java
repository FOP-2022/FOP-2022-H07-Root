package tutor;

import org.opentest4j.AssertionFailedError;
import spoon.reflect.code.CtCodeElement;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.visitor.Filter;
import tutor.Utils.TestCollection.Entry;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static tutor.Utils.TestCollection.Mode.SILENT;

public interface Utils {

    static <T> Iterable<T> iterate(Stream<T> s) {
        return s::iterator;
    }

    static void repeat(int times, Runnable runnable) {
        for (int i = 0; i < times; i++) {
            runnable.run();
        }
    }

    interface Test {

        static void a() {
            b();
        }

        static void b() {

        }
    }

    interface Messages {

        static String isAnInvalidExpression(char[] array) {
            return isAnInvalid(String.format("<%s>",
                IntStream.range(0, array.length).mapToObj(i -> String.valueOf(array[i])).collect(Collectors.joining())), "expression");
        }

        static String isAnInvalid(Object x, Object y) {
            return String.format("%s is an invalid %s", x, y);
        }

        static String incorrect(String name) {
            return String.format("%s is not correct", name);
        }

        static String incorrectAttribute(String attributeName) {
            return incorrect(String.format("value of attribute <%s>", attributeName));
        }

        static String incorrectReturnValue() {
            return incorrect("return value");
        }

        static String incorrectReturnedObject() {
            return incorrect("return object");
        }

        static String incorrectParameter(String parameterName) {
            return incorrect(String.format("value of parameter <%s>", parameterName));
        }

        static String notInRange(String name) {
            return String.format("%s is not in range", name);
        }

        static String indexNotInRange() {
            return notInRange("index");
        }

        static String invalid(String key, Object value) {
            return String.format("invalid %s ==> <%s>", key, value);
        }

        static String invalidIndex(int value) {
            return invalid("index", value);
        }

        static String unexpectedCall() {
            return "unexpected call";
        }

        static String unexpectedRecursiveCallFor(String methodName, List<Object> objects) {
            return String.format("method <%s> was called unexpectedly", methodCall(methodName, objects));
        }

        static String methodCall(String methodName, List<Object> parameterValues) {
            return String.format("%s(%s)", methodName,
                parameterValues.stream().map(Objects::toString).collect(Collectors.joining(", ")));
        }

        static String incorrectOrderOfWhenUsing(String of, String when) {
            return String.format("incorrect order of <%s> when <%s>", of, when);
        }

        static String incorrectValuesInWhenUsing(String in, String when) {
            return String.format("incorrect values in %s when %s", in, when);
        }

        static String wasNotCalledRecursively(String name) {
            return String.format("method %s was not called recursively", name);
        }

        static String ofEntryError(Entry entry) {
            var builder = new StringBuilder();
            var collection = entry.collection();
            if (collection.methodName != null) {
                builder.append(collection.methodName);
                builder.append('(');
                builder.append(Arrays.stream(entry.parameters).map(Objects::toString).collect(Collectors.joining(",")));
                builder.append(')');
            }
            if (entry.hasError()) {
                if (builder.length() != 0) {
                    builder.append(' ');
                }
                builder.append(entry.error.getMessage());
            }
            return builder.toString();
        }
    }

    interface SpoonPredicate {

        static <T extends CtElement> Filter<T> isCodeElement() {
            return o -> o instanceof CtCodeElement;
        }
    }

    class TestCollection {

        public final String methodName;
        final LinkedList<Entry> entries = new LinkedList<>();

        private TestCollection(String methodName) {
            this.methodName = methodName;
        }

        public static TestCollection test(String methodName) {
            return new TestCollection(methodName);
        }

        public static TestCollection test() {
            return test(null);
        }

        public TestCollection run(Runnable runnable, Object... parameters) {
            add(runnable, parameters);
            run();
            return this;
        }

        public TestCollection add(Runnable runnable, Object... parameters) {
            entries.add(new Entry(runnable, parameters));
            return this;
        }

        public TestCollection addReq(Runnable runnable, Object... parameters) {
            entries.add(new Entry(runnable, parameters, true));
            return this;
        }

        public TestCollection letShow() {
            entries.getLast().show |= true;
            return this;
        }

        public TestCollection terminateOnFailure() {
            entries.getLast().terminate = true;
            return this;
        }

        public boolean hasFailedEntries() {
            return entries.stream().anyMatch(Entry::hasError);
        }

        public Stream<Entry> getEntriesToShow() {
            return entries.stream().filter(Entry::hasError).filter(Entry::toShow);
        }

        public <T> T run() {
            return run(Mode.SHOW_ALL);
        }

        public <T> T run(Mode mode) {
            int count = 0;
            int countType = 0;
            for (Entry e : entries) {
                try {
                    e.runnable.run();
                } catch (AssertionFailedError error) {
                    e.error = error;
                    if (e.newType) {
                        countType = 0;
                    }
                    if (mode == Mode.SHOW_FIRST && countType == 0) {
                        e.show = true;
                    }
                    if (mode == Mode.SHOW_ALL) {
                        e.show = true;
                    }
                    count++;
                    countType++;
                    if (e.terminate()) {
                        e.show = true;
                        break;
                    }
                }
            }
            if (count != 0 && mode != SILENT) {
                var firstEntry = getEntriesToShow().findFirst().orElseThrow();
                Throwable cause = firstEntry.error; // TODO
                while (cause.getCause() != null) {
                    cause = cause.getCause();
                }
                fail(getEntriesToShow().map(Messages::ofEntryError).collect(Collectors.joining(", ")), cause);
            }
            entries.clear();

            return null;
        }

        public enum Mode {
            SHOW_FIRST, SHOW_ALL, SHOW_NO, SILENT, STOP_ON_EXCEPTION
        }

        class Entry {
            Runnable runnable;
            Object[] parameters;
            AssertionFailedError error;
            boolean show;
            boolean newType;
            boolean terminate;

            Entry(Runnable runnable, Object[] parameters) {
                this.runnable = runnable;
                this.parameters = parameters;
            }

            Entry(Runnable runnable, Object[] parameters, boolean terminate) {
                this.runnable = runnable;
                this.parameters = parameters;
                this.terminate = terminate;
            }

            Entry(Runnable runnable) {
                this(runnable, null);
            }

            boolean hasError() {
                return error != null;
            }

            TestCollection collection() {
                return TestCollection.this;
            }

            boolean toShow() {
                return show;
            }

            boolean terminate() {
                return terminate;
            }

            boolean newType() {
                return newType;
            }
        }
    }

    class State {
        private Object state = 0;

        public <T> boolean is(T toCompare) {
            return Objects.equals(toCompare, state);
        }

        public <T> T get() {
            //noinspection unchecked
            return (T) state;
        }

        public int getInt() {
            return (int) state;
        }

        public int incInt() {
            state = ((int) state) + 1;
            return (int) state - 1;
        }

        public <T> void set(T state) {
            this.state = state;
        }
    }
}
