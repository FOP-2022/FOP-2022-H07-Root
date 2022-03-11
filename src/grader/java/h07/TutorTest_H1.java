package h07;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import reflection.ClassTester;
import reflection.IdentifierMatcher;
import reflection.MethodTester;
import reflection.ParameterMatcher;
import spoon.reflect.code.CtLambda;
import spoon.reflect.reference.CtExecutableReference;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.DoublePredicate;

import static h07.Global.SIMILARITY;
import static java.lang.reflect.Modifier.PUBLIC;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static tutor.Utils.TestCollection.test;

/**
 * The JUnit tests for H1.
 */
@TestForSubmission("h07")
public class TutorTest_H1 {

    public static ClassTester<?> EPS_CLASS;
    public static MethodTester EPS_TEST;
    public static ClassTester<?> DPF_CLASS;
    public static MethodTester DPF_CONJUNCTION;
    public static MethodTester DPF_DISJUNCTION;
    public static MethodTester DPF_COMPLEX_PREDICATE;
    public static MethodTester DPF_DEFAULT_PREDICATE;
    public static MethodTester DPF_CHECKSUM;

    public static ClassTester<?> EPS_CLASS() {
        return (EPS_CLASS = Objects.requireNonNullElseGet(EPS_CLASS, () -> new ClassTester(
            "h07.predicate",
            "EpsilonEnvironmentPred",
            SIMILARITY,
            PUBLIC,
            List.of(
                new IdentifierMatcher("DoublePredicate", "java.util.function", SIMILARITY)
            )).assureResolved()));
    }

    public static MethodTester EPS_TEST() {
        return (EPS_TEST = Objects.requireNonNullElseGet(EPS_TEST, () -> new MethodTester(
            EPS_CLASS(),
            "test",
            SIMILARITY,
            PUBLIC,
            boolean.class,
            List.of(
                new ParameterMatcher(double.class)
            )).assureResolved()));
    }

    public static ClassTester<?> DPF_CLASS() {
        return (DPF_CLASS = Objects.requireNonNullElseGet(DPF_CLASS, () -> new ClassTester(
            "h07.predicate",
            "DoublePredicateFactory",
            SIMILARITY,
            PUBLIC).assureResolved()));
    }

    public static MethodTester DPF_CONJUNCTION() {
        return (DPF_CONJUNCTION = Objects.requireNonNullElseGet(DPF_CONJUNCTION, () -> new MethodTester(
            DPF_CLASS(),
            "buildConjunction",
            SIMILARITY,
            PUBLIC,
            DoublePredicate.class,
            List.of(
                new ParameterMatcher(DoublePredicate[].class)
            )).assureResolved()));
    }

    public static MethodTester DPF_DISJUNCTION() {
        return (DPF_DISJUNCTION = Objects.requireNonNullElseGet(DPF_DISJUNCTION, () -> new MethodTester(
            DPF_CLASS(),
            "buildDisjunction",
            SIMILARITY,
            PUBLIC,
            DoublePredicate.class,
            List.of(
                new ParameterMatcher(DoublePredicate[].class), new ParameterMatcher(boolean.class)
            )).assureResolved()));
    }

    public static MethodTester DPF_COMPLEX_PREDICATE() {

        return (DPF_COMPLEX_PREDICATE = Objects.requireNonNullElseGet(DPF_COMPLEX_PREDICATE, () -> new MethodTester(
            DPF_CLASS(),
            "buildComplexPredicate",
            SIMILARITY,
            PUBLIC,
            DoublePredicate.class,
            List.of(
                new ParameterMatcher(DoublePredicate[][].class)
            )
        ).assureResolved()));
    }

    public static MethodTester DPF_DEFAULT_PREDICATE() {
        return (DPF_DEFAULT_PREDICATE = Objects.requireNonNullElseGet(DPF_DEFAULT_PREDICATE, () -> new MethodTester(
            DPF_CLASS(),
            "getDefaultComplexPredicate",
            SIMILARITY,
            PUBLIC,
            DoublePredicate.class,
            List.of()
        ).assureResolved()));
    }

    public static MethodTester DPF_CHECKSUM() {
        return (DPF_CHECKSUM = Objects.requireNonNullElseGet(DPF_CHECKSUM, () -> new MethodTester(
            DPF_CLASS(),
            "getChecksumPredicate",
            SIMILARITY,
            PUBLIC,
            DoublePredicate.class,
            List.of(
                new ParameterMatcher("decimalPlaces", 0, int.class),
                new ParameterMatcher("divisor", 0, int.class)
            )).assureResolved()));
    }

    @Test
    public void epsilonEnvironmentPredExists() {
        test()
            .addReq(EPS_CLASS()::assureResolved)
            .add(EPS_CLASS()::assertAccessModifier)
            .add(EPS_CLASS()::assertImplementsInterfaces)
            .add(EPS_CLASS()::assertIsPlainClass).run();
    }

    @Test
    public void epsilonEnvironmentPredMostlyCorrect() {
        test("test")
            .addReq(EPS_CLASS()::assureResolved)
            .add(EPS_CLASS()::assertAccessModifier)
            .add(EPS_CLASS()::assertImplementsInterfaces)
            .add(EPS_CLASS()::assertIsPlainClass)
            .add(EPS_TEST()::resolve)
            .add(EPS_TEST()::assertAccessModifier)
            .add(() -> {
                try {
                    var epsConstructor = EPS_CLASS().resolveConstructor(List.of(
                        new ParameterMatcher(double.class),
                        new ParameterMatcher(double.class))
                    );
                    var uut = epsConstructor.newInstance(5, 0.1);
                    var test = EPS_TEST().resolveMethod();
                    assertEquals(true, test.invoke(uut, 5.04),
                        "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=5, epsilon=0.1).test(5.04)");
                    assertEquals(false, test.invoke(uut, 4.89999),
                        "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=5, epsilon=0.1).test(4.89999)");
                    assertEquals(true, test.invoke(uut, 5),
                        "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=5, epsilon=0.1).test(5)");

                    uut = epsConstructor.newInstance(-2.5, 1.5);
                    assertEquals(true, test.invoke(uut, -3),
                        "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=-2.5, epsilon=1.5).test(-3)");
                    assertEquals(false, test.invoke(uut, -0.9),
                        "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=-2.5, epsilon=1.5).test(-0.9)");
                    assertEquals(false, test.invoke(uut, 5),
                        "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=-2.5, epsilon=1.5).test(5)");
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    fail("Der Konstruktor von EpsilonEnvironmentPred schlug fehl.", e);
                }
            }).run();
    }

    @Test
    public void epsilonEnvironmentPredIsCorrect() {
        test("test")
            .addReq(EPS_CLASS()::assureResolved)
            .add(EPS_CLASS()::assertAccessModifier)
            .add(EPS_CLASS()::assertImplementsInterfaces)
            .add(EPS_CLASS()::assertIsPlainClass)
            .add(EPS_TEST()::resolve)
            .add(EPS_TEST()::assertAccessModifier)
            .add(() -> {
                try {
                    var epsConstructor = EPS_CLASS().resolveConstructor(List.of(
                        new ParameterMatcher(double.class),
                        new ParameterMatcher(double.class))
                    );
                    var uut = epsConstructor.newInstance(5, 0.1);
                    var test = EPS_TEST().resolveMethod();
                    assertEquals(true, test.invoke(uut, 5.1),
                        "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=5, epsilon=0.1).test(5.1)");
                    assertEquals(true, test.invoke(uut, 5.04),
                        "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=5, epsilon=0.1).test(5.04)");
                    assertEquals(false, test.invoke(uut, 5.100000001),
                        "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=5, epsilon=0.1).test(5.100000001)");
                    assertEquals(false, test.invoke(uut, 4.89999),
                        "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=5, epsilon=0.1).test(4.89999)");
                    assertEquals(true, test.invoke(uut, 4.9),
                        "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=5, epsilon=0.1).test(4.9)");
                    assertEquals(true, test.invoke(uut, 5),
                        "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=5, epsilon=0.1).test(5)");

                    uut = epsConstructor.newInstance(-2.5, 1.5);
                    assertEquals(true, test.invoke(uut, -1.00000001),
                        "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=-2.5, epsilon=1.5).test(-1.00000001)");
                    assertEquals(true, test.invoke(uut, -3),
                        "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=-2.5, epsilon=1.5).test(-3)");
                    assertEquals(false, test.invoke(uut, -0.9),
                        "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=-2.5, epsilon=1.5).test(-0.9)");
                    assertEquals(false, test.invoke(uut, 5),
                        "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=-2.5, epsilon=1.5).test(5)");
                    assertEquals(true, test.invoke(uut, -3.999),
                        "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=-2.5, epsilon=1.5).test(-3.999)");
                    assertEquals(true, test.invoke(uut, -2.4),
                        "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=-2.5, epsilon=1.5).test(-2.4)");

                    uut = epsConstructor.newInstance(0.0, -1);
                    assertEquals(true, test.invoke(uut, 0.0),
                        "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=0.0, epsilon=-1).test(0.0)"
                            + " (Negatives Epsilon führt zu epsilon=0.0)");
                    assertEquals(false, test.invoke(uut, -0.01),
                        "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=0.0, epsilon=-1).test(-0.01)"
                            + " (Negatives Epsilon führt zu epsilon=0.0)");
                    assertEquals(false, test.invoke(uut, 1.1),
                        "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=0.0, epsilon=-1).test(1.1)"
                            + " (Negatives Epsilon führt zu epsilon=0.0)");
                    assertEquals(false, test.invoke(uut, -2),
                        "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=0.0, epsilon=-1).test(-2)"
                            + " (Negatives Epsilon führt zu epsilon=0.0)");
                    assertEquals(false, test.invoke(uut, 0.01),
                        "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=0.0, epsilon=-1).test(0.01)"
                            + " (Negatives Epsilon führt zu epsilon=0.0)");
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    fail("Der Konstruktor von EpsilonEnvironmentPred schlug fehl.", e);
                }
            }).run();
    }

    @BeforeEach
    public void beforeEach() {
        Mockito.reset();
    }

    @Test
    public void doublePredicateFactoryExist() {
        test()
            .addReq(DPF_CLASS()::resolveClass)
            .add(DPF_CLASS()::assertAccessModifier)
            .add(DPF_CLASS()::assertIsPlainClass)
            .run();
    }

    @Test
    public void buildConjunctionExists() {
        test()
            .addReq(DPF_CLASS()::resolveClass)
            .add(DPF_CLASS()::assertAccessModifier)
            .add(DPF_CLASS()::assertIsPlainClass)
            .addReq(DPF_CONJUNCTION()::resolve)
            .add(DPF_CONJUNCTION()::assertAccessModifier)
            .add(() -> DPF_CONJUNCTION().assertChildRecursive(1))
            .add(DPF_CONJUNCTION()::assertParametersMatch)
            .run();
    }

    @Test
    public void buildConjunctionWorks() {
        test()
            .addReq(DPF_CLASS()::resolveClass)
            .add(DPF_CLASS()::assertAccessModifier)
            .add(DPF_CLASS()::assertIsPlainClass)
            .addReq(DPF_CONJUNCTION()::resolve)
            .add(DPF_CONJUNCTION()::assertAccessModifier)
            .add(() -> DPF_CONJUNCTION().assertChildRecursive(1))
            .add(DPF_CONJUNCTION()::assertParametersMatch)
            .add(() -> {
                DoublePredicate[] predicates = new DoublePredicate[]{d -> true};
                var instanz = mock(DPF_CLASS().resolveClass().getActualClass(), Answers.CALLS_REAL_METHODS);
                DPF_CLASS().setClassInstance(instanz);
                var pred = DPF_CONJUNCTION().invoke(instanz, (Object) predicates);
                var predString1 = "{d -> true}";
                if (pred == null) {
                    fail("Resultat von buildConjunction ist null bei Input " + predString1);
                }
                assertTrue(((DoublePredicate) pred).test(2.0), "Fehler bei Prädikaten: " + predString1 + " und Input: 2.0");
                assertTrue(((DoublePredicate) pred).test(-2.3), "Fehler bei Prädikaten: " + predString1 + " und Input: -2.3");

                predicates = new DoublePredicate[]{d -> d > 3, d -> d < 5, d -> d % 2 < 0.5};
                var predString2 = "{d -> d > 3, d -> d < 5, d -> d % 2 < 0.5}";
                pred = DPF_CONJUNCTION().invoke(instanz, (Object) predicates);
                if (pred == null) {
                    fail("Resultat von buildDisjunction ist null bei Input " + predString2);
                }
                assertFalse(((DoublePredicate) pred).test(3.1), "Fehler bei Prädikaten: " + predString2 + " und Input: 3.1");
                assertFalse(((DoublePredicate) pred).test(4.9), "Fehler bei Prädikaten: " + predString2 + " und Input: 4.9");
                assertFalse(((DoublePredicate) pred).test(2.9), "Fehler bei Prädikaten: " + predString2 + " und Input: 2.9");
                assertFalse(((DoublePredicate) pred).test(2.01), "Fehler bei Prädikaten: " + predString2 + " und Input: 2.01");
                assertFalse(((DoublePredicate) pred).test(5.1), "Fehler bei Prädikaten: " + predString2 + " und Input: 5.1");
                assertFalse(((DoublePredicate) pred).test(4.9), "Fehler bei Prädikaten: " + predString2 + " und Input: 4.9");
                assertTrue(((DoublePredicate) pred).test(4.2), "Fehler bei Prädikaten: " + predString2 + " und Input: 4.2");
                assertTrue(((DoublePredicate) pred).test(4.01), "Fehler bei Prädikaten: " + predString2 + " und Input: 4.01");
                assertTrue(((DoublePredicate) pred).test(4.49), "Fehler bei Prädikaten: " + predString2 + " und Input: 4.49");
            })
            .run();
    }

    @Test
    public void buildDisjunctionExists() {
        test()
            .addReq(DPF_CLASS()::resolveClass)
            .add(DPF_CLASS()::assertAccessModifier)
            .add(DPF_CLASS()::assertIsPlainClass)
            .addReq(DPF_DISJUNCTION()::resolve)
            .add(DPF_DISJUNCTION()::assertAccessModifier)
            .add(DPF_DISJUNCTION()::assertNoInvocation)
            .add(DPF_DISJUNCTION()::assertParametersMatch)
            .run();
    }

    @Test
    public void buildDisjunctionWorks() {
        test()
            .addReq(DPF_CLASS()::resolveClass)
            .add(DPF_CLASS()::assertAccessModifier)
            .add(DPF_CLASS()::assertIsPlainClass)
            .addReq(DPF_DISJUNCTION()::resolve)
            .add(DPF_DISJUNCTION()::assertAccessModifier)
            .add(DPF_DISJUNCTION()::assertNoInvocation)
            .add(DPF_DISJUNCTION()::assertParametersMatch)
            .add(() -> {
                DoublePredicate[] predicates = new DoublePredicate[]{d -> true};
                var instance = mock(DPF_CLASS().resolveClass().getActualClass(), Answers.CALLS_REAL_METHODS);
                DPF_CLASS().setClassInstance(instance);
                var pred = DPF_DISJUNCTION().invoke(instance, predicates, true);
                var predString1 = "{d -> true}";
                if (pred == null) {
                    fail("Resultat von buildDisjunction ist null bei Input " + predString1);
                }
                assertTrue(((DoublePredicate) pred).test(2.0), "Fehler bei Prädikaten: " + predString1 + " und Input: 2.0");
                assertTrue(((DoublePredicate) pred).test(-2.3), "Fehler bei Prädikaten: " + predString1 + " und Input: -2.3");

                predicates = new DoublePredicate[]{d -> d < 3, d -> d > 5, d -> d % 2 < 0.5};
                var predString2 = "{d -> d < 3, d -> d > 5, d -> d % 2 < 0.5}";
                pred = DPF_DISJUNCTION().invoke(instance, predicates, true);
                if (pred == null) {
                    fail("Resultat von buildDisjunction ist null bei Input " + predString2);
                }
                assertTrue(((DoublePredicate) pred).test(2.0), "Fehler bei Prädikaten: " + predString2 + " und Input: 2.0");
                assertTrue(((DoublePredicate) pred).test(4.2), "Fehler bei Prädikaten: " + predString2 + " und Input: 4.2");
                assertTrue(((DoublePredicate) pred).test(5.1), "Fehler bei Prädikaten: " + predString2 + " und Input: 5.1");
                assertFalse(((DoublePredicate) pred).test(3.1), "Fehler bei Prädikaten: " + predString2 + " und Input: 3.1");
                assertFalse(((DoublePredicate) pred).test(4.9), "Fehler bei Prädikaten: " + predString2 + " und Input: 4.9");
                assertFalse(((DoublePredicate) pred).test(4.51), "Fehler bei Prädikaten: " + predString2 + " und Input: 4.51");

                final DoublePredicate waiting = d -> {
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return false;
                };
                DoublePredicate[] timeout = new DoublePredicate[]{waiting, d -> true};
                var timePred = DPF_DISJUNCTION().invoke(instance, timeout, false);
                if (timePred == null) {
                    fail("Resultat von buildDisjunction ist null bei Input Prädikate: {d -> [langsames Prädikat], d -> true}");
                }
                assertTimeoutPreemptively(Duration.ofMillis(10), () -> {
                    ((DoublePredicate) timePred).test(1);
                }, "Die Verknüpfungsreihenfolge entspricht nicht den Anforderungen." +
                    " Erkennbar an zu langer Ausführungszeit. Prädikate: {d -> [langsames Prädikat], d -> true}, Forward: false");
                DoublePredicate[] timeout2 = new DoublePredicate[]{d -> true, waiting};
                var timePred2 = DPF_DISJUNCTION().invoke(instance, timeout2, true);
                if (timePred2 == null) {
                    fail("Resultat von buildDisjunction ist null bei Input Prädikate: {d -> true, d -> [langsames Prädikat]}");
                }
                assertTimeoutPreemptively(Duration.ofMillis(10), () -> {
                    ((DoublePredicate) timePred2).test(1);
                }, "Die Verknüpfungsreihenfolge entspricht nicht den Anforderungen." +
                    " Erkennbar an zu langer Ausführungszeit. Prädikate: {d -> true, d -> [langsames Prädikat]}, Forward: true");
            })
            .run();
    }

    @Test
    public void complexDoublePredicateWorksSimple() {
        test()
            .addReq(DPF_CLASS()::resolveClass)
            .add(DPF_CLASS()::assertAccessModifier)
            .add(DPF_CLASS()::assertIsPlainClass)
            .addReq(DPF_COMPLEX_PREDICATE()::resolve)
            .add(DPF_COMPLEX_PREDICATE()::assertAccessModifier)
            .add(DPF_COMPLEX_PREDICATE()::assertParametersMatch)
            .add(() -> {
                DoublePredicate[][] predicates = new DoublePredicate[][]{
                    new DoublePredicate[]{d -> true}
                };
                var predString1 = "{{d -> true}}";
                var instance = mock(DPF_CLASS().resolveClass().getActualClass(), Answers.CALLS_REAL_METHODS);
                DPF_CLASS().setClassInstance(instance);
                var pred = DPF_COMPLEX_PREDICATE().invoke(instance, (Object) predicates);
                assertTrue(((DoublePredicate) pred).test(2.0), "Fehler bei Prädikaten: " + predString1 + " und Input: 2.0");
                assertTrue(((DoublePredicate) pred).test(-2.3), "Fehler bei Prädikaten: " + predString1 + " und Input: -2.3");
                predicates = new DoublePredicate[][]{
                    new DoublePredicate[]{d -> d < 3}
                };
                var predString2 = "{{d -> d < 3}}";
                pred = DPF_COMPLEX_PREDICATE().invoke(instance, (Object) predicates);
                assertTrue(((DoublePredicate) pred).test(2.0), "Fehler bei Prädikaten: " + predString2 + " und Input: 2.0");
                assertFalse(((DoublePredicate) pred).test(5), "Fehler bei Prädikaten: " + predString2 + " und Input: 5");
                assertFalse(((DoublePredicate) pred).test(3), "Fehler bei Prädikaten: " + predString2 + " und Input: 3");
            }).run();
    }

    @Test
    public void complexDoublePredicateWorksAll() {
        var instanz = mock(DPF_CONJUNCTION().getClassTester().findClass(), Answers.CALLS_REAL_METHODS);

        DPF_CONJUNCTION().invoke(doAnswer(a -> {
            var arg = (DoublePredicate[]) a.getArgument(0);
            return DoublePredicateFactorySolution.buildConjunction(arg);
        }).when(instanz), (Object) any(DoublePredicate[].class));

        DPF_DISJUNCTION().invoke(doAnswer(a -> {
            var arg1 = (DoublePredicate[]) a.getArgument(0);
            var arg2 = (boolean) a.getArgument(1);
            return DoublePredicateFactorySolution.buildDisjunction(arg1, arg2);
        }).when(instanz), any(DoublePredicate[].class), anyBoolean());

        test()
            .addReq(DPF_CLASS()::resolveClass)
            .add(DPF_CLASS()::assertAccessModifier)
            .add(DPF_CLASS()::assertIsPlainClass)
            .addReq(DPF_COMPLEX_PREDICATE()::resolve)
            .add(DPF_COMPLEX_PREDICATE()::assertAccessModifier)
            .add(DPF_COMPLEX_PREDICATE()::assertParametersMatch)
            .add(() -> {
                DoublePredicate[][] predicates = new DoublePredicate[][]{
                    new DoublePredicate[]{d -> true},
                    new DoublePredicate[]{d -> false, d -> true, d -> false},
                    new DoublePredicate[]{d -> d > 0, d -> d < -3}
                };
                var instance = mock(DPF_CLASS().resolveClass().getActualClass(), Answers.CALLS_REAL_METHODS);
                DPF_CLASS().setClassInstance(instance);
                var pred = DPF_COMPLEX_PREDICATE().invoke(instance, (Object) predicates);
                var predString = "{{d -> true}, {d -> false, d -> true, d -> false}, {d -> d > 0, d -> d < -3}}";
                if (pred == null) {
                    fail("buildComplexPredicate returned null for input [" + predString + "]");
                }
                assertTrue(((DoublePredicate) pred).test(2.0), "Fehler bei Prädikaten: " + predString + " und Input: 2.0");
                assertTrue(((DoublePredicate) pred).test(6.2), "Fehler bei Prädikaten: " + predString + " und Input: 6.2");
                assertTrue(((DoublePredicate) pred).test(-5), "Fehler bei Prädikaten: " + predString + " und Input: -5");
                assertFalse(((DoublePredicate) pred).test(0.0), "Fehler bei Prädikaten: " + predString + " und Input: 0.0");
                assertFalse(((DoublePredicate) pred).test(-1), "Fehler bei Prädikaten: " + predString + " und Input: -1");
                assertFalse(((DoublePredicate) pred).test(-0.01), "Fehler bei Prädikaten: " + predString + " und Input: -0.01");
                assertFalse(((DoublePredicate) pred).test(-2.3), "Fehler bei Prädikaten: " + predString + " und Input: -2.3");
                final DoublePredicate waiting = d -> {
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return false;
                };
                DoublePredicate[][] timeout = new DoublePredicate[][]{
                    new DoublePredicate[]{d -> true, waiting},
                    new DoublePredicate[]{waiting, d -> true},
                    new DoublePredicate[]{d -> true, waiting},
                    new DoublePredicate[]{waiting, d -> true},
                };
                pred = DPF_COMPLEX_PREDICATE().invoke(instance, (Object) timeout);
                if (pred == null) {
                    fail("buildComplexPredicate returned null for input [{{d -> true, d -> [langsames Prädikat]}, {d -> [langsames Prädikat], d -> true}," +
                        " {d -> true, d -> [langsames Prädikat]}, {d -> [langsames Prädikat], d -> true}}]");
                }
                Object finalPred = pred;
                assertTimeoutPreemptively(Duration.ofMillis(10), () -> {
                    ((DoublePredicate) finalPred).test(1);
                }, "Die Verknüpfungsreihenfolge entspricht nicht den Anforderungen. Erkennbar an zu langer Ausführungszeit." +
                    " Prädikate: {{d -> true, d -> [langsames Prädikat]}, {d -> [langsames Prädikat], d -> true}}");
                assertTrue(((DoublePredicate) pred).test(2.0));
            }).run();
    }

    @Test
    public void getDefaultComplexPredicateExists() {
        test()
            .addReq(DPF_CLASS()::resolveClass)
            .add(DPF_CLASS()::assertIsPlainClass)
            .addReq(DPF_COMPLEX_PREDICATE()::resolve)
            .add(DPF_COMPLEX_PREDICATE()::assertAccessModifier)
            .add(DPF_COMPLEX_PREDICATE()::assertParametersMatch).run();
    }

    @Test
    public void getDefaultComplexPredicateWorksMostOfTheTime() {
        test()
            .addReq(DPF_CLASS()::resolveClass)
            .add(DPF_CLASS()::assertIsPlainClass)
            .addReq(DPF_DEFAULT_PREDICATE()::resolve)
            .add(DPF_DEFAULT_PREDICATE()::assertAccessModifier)
            .add(DPF_DEFAULT_PREDICATE()::assertParametersMatch)
            .add(() -> {
                var instanz = mock(DPF_CLASS().resolveClass().getActualClass(), Answers.CALLS_REAL_METHODS);
                DPF_CLASS().setClassInstance(instanz);
                DoublePredicate pred = DPF_DEFAULT_PREDICATE().invoke(instanz);
                assertFalse(pred.test(Math.PI),
                    "Math.PI sollte getDefaultComplexPredicate nicht erfüllen [nicht nah genug an 3.5]");
                assertTrue(pred.test(2.5),
                    "2.5 sollte getDefaultComplexPredicate erfüllen [sin > cos, ~= 2.5]");
                assertTrue(pred.test(1.5),
                    "1.5 sollte getDefaultComplexPredicate erfüllen [sin > cos, ~= 1.5]");
                assertFalse(pred.test(0.0),
                    "0.0 sollte getDefaultComplexPredicate nicht erfüllen [nicht nah genug an 0.5]");
                assertTrue(pred.test(0.5),
                    "0.5 sollte getDefaultComplexPredicate erfüllen [sin > cos && < 10*e, ~= 0.5]");
                assertFalse(pred.test(93.5),
                    "93.5 sollte getDefaultComplexPredicate nicht erfüllen da keines der Predicates von Predicate-Teil 3 zutrifft");
            }).run();
    }

    @Test
    public void getDefaultComplexPredicateWorks() {
        test()
            .addReq(DPF_CLASS()::resolveClass)
            .add(DPF_CLASS()::assertIsPlainClass)
            .addReq(DPF_DEFAULT_PREDICATE()::resolve)
            .add(DPF_DEFAULT_PREDICATE()::assertAccessModifier)
            .add(DPF_DEFAULT_PREDICATE()::assertParametersMatch)
            .add(() -> {
                var instanz = mock(DPF_CLASS().resolveClass().getActualClass(), Answers.CALLS_REAL_METHODS);
                DPF_CLASS().setClassInstance(instanz);
                var pred = (DoublePredicate) DPF_DEFAULT_PREDICATE().invoke(instanz);
                assertFalse(pred.test(Math.PI),
                    "Math.PI sollte getDefaultComplexPredicate nicht erfüllen"
                        + " [nicht nah genug an 3.5]");
                assertTrue(pred.test(2.49999),
                    "2.49999 sollte getDefaultComplexPredicate erfüllen"
                        + " [sin > cos, ~= 2.5]");
                assertTrue(pred.test(1.5 - 0.99 / 50000),
                    "1.5 - 0.99 / 50000 sollte getDefaultComplexPredicate erfüllen"
                        + " [sin > cos, ~= 1.5]");
                assertTrue(pred.test(1.5 + 0.99 / 50000),
                    "1.5 - 0.99 / 50000 sollte getDefaultComplexPredicate erfüllen"
                        + " [sin > cos, ~= 1.5]");
                assertFalse(pred.test(1.5 - 1.01 / 50000),
                    "1.5 - 1.01 / 50000 sollte getDefaultComplexPredicate nicht erfüllen"
                        + " [nicht nah genug 1.5 für Predicate-Teil 1]");
                assertFalse(pred.test(1.5 + 1.01 / 50000),
                    "1.5 + 1.01 / 50000 sollte getDefaultComplexPredicate nicht erfüllen"
                        + " [nicht nah genug 1.5 für Predicate-Teil 1]");
                assertFalse(pred.test(0.4999999999),
                    "0.4999999999 sollte getDefaultComplexPredicate nicht erfüllen"
                        + " [nicht nah genug an 0.5 für Predicate-Teil 1]");
                assertTrue(pred.test(0.5),
                    "0.5 sollte getDefaultComplexPredicate erfüllen"
                        + " [sin > cos && < 10*e, ~= 0.5]");
                assertFalse(pred.test(93.5),
                    "93.5 sollte getDefaultComplexPredicate nicht erfüllen"
                        + " da keines der Predicates von Predicate-Teil 3 zutrifft");
                assertTrue(pred.test(92.5),
                    "92.5 / 50000 sollte getDefaultComplexPredicate erfüllen"
                        + " [x < log(x)^3, ~= 92.5]");
                assertFalse(pred.test(125.5),
                    "125.5 sollte getDefaultComplexPredicate nicht erfüllen"
                        + " da keines der Predicates von Predicate-Teil 3 zutrifft");
                assertTrue(pred.test(126.5),
                    "126.5 sollte getDefaultComplexPredicate erfüllen [sin > cos, ~= 126.5]");
                assertTrue(pred.test(127.5),
                    "127.5 sollte getDefaultComplexPredicate erfüllen [sin > cos, ~= 127.5]");
                assertTrue(pred.test(128.5),
                    "128.5 sollte getDefaultComplexPredicate erfüllen [sin > cos, ~= 128.5]");
                assertTrue(pred.test(129.5),
                    "129.5 sollte getDefaultComplexPredicate erfüllen [sin > cos, ~= 129.5]");
                assertFalse(pred.test(130.5),
                    "130.5 sollte getDefaultComplexPredicate nicht erfüllen"
                        + " da keines der Predicates von Predicate-Teil 3 zutrifft");
                assertTrue(pred.test(994.5),
                    "994.5 sollte getDefaultComplexPredicate erfüllen [sin > cos, ~= 994.5]");
                // Etwas breitere Spanne für Teilarray an Index 1, da Beschreibung schwierig war.
                assertTrue(pred.test(996.5 - 2.99 / 50000),
                    "996.5 - 2.99 / 50000 sollte getDefaultComplexPredicate erfüllen"
                        + " [sin > cos, ~= 996.5]");
                assertFalse(pred.test(996.5 - 5.01 / 50000),
                    "996.5 - 5.01 / 50000 sollte getDefaultComplexPredicate nicht erfüllen"
                        + " [nicht nah genug 996.5 für Predicate-Teil 2]");
                assertTrue(pred.test(996.5 + 2.99 / 50000),
                    "996.5 + 2.99 / 50000 sollte getDefaultComplexPredicate erfüllen"
                        + " [sin > cos, ~= 996.5]");
                assertFalse(pred.test(996.5 + 5.01 / 50000),
                    "996.5 + 5.01 / 50000 sollte getDefaultComplexPredicate nicht erfüllen"
                        + " [nicht nah genug 996.5 für Predicate-Teil 2]");
                assertFalse(pred.test(-93.5),
                    "-93.5 sollte getDefaultComplexPredicate nicht erfüllen da u.A."
                        + " keines der Predicates von Predicate-Teil 3 zutrifft");
            }).run();
    }

    @Test
    // @ExtendWith(TestCycleResolver.class)
    public void lambdaInCorrectForm() {
        var content = DPF_CLASS().getClassContent();
        var statements = Arrays.asList(content.split(";"));
        assertTrue(content.contains("getDefaultComplexPredicate"), "Methode getDefaultComplexPredicate konnte nicht im Quelltext gefunden werden.");

        assertTrue(content.contains("new DoublePredicate["), "Array, der in getDefaultComplexPredicate erstellt " +
            "und als Argument an buildComplexPredicate weitergeben wird, konnte nicht gefunden werden.");
        assertTrue(statements.stream().anyMatch(st -> st.contains("new EpsilonEnvironmentPred") && st.contains("=")
            && !st.contains("(double")), "Keine Zuweisung mit new EpsilonEnvironmentPred für ersten Teilarray in getDefaultComplexPredicate");
        assertTrue(statements.stream().anyMatch(st -> st.contains("->")
            && !st.contains("(double") && !st.contains("return")), "Für den zweiten bzw. " +
            "dritten Teilarray in getDefaultComplexPredicate soll die Lambda-Kurzform verwendet werden");
    }

    @Test
    public void checksumPredExists() {
        test()
            .addReq(DPF_CLASS()::resolveClass)
            .add(DPF_CLASS()::assertIsPlainClass)
            .addReq(DPF_CHECKSUM()::resolve)
            .add(DPF_CHECKSUM()::assertAccessModifier)
            .run();
    }

    private void checksumShape() {
        var m = DPF_CHECKSUM().assertCtMethodExists();
        assertEquals(1, m.filterChildren(ct -> ct instanceof CtLambda).list().size(),
            "Es sollte genau einen Lambda-Ausdruck in getChecksumPredicate geben.");
    }

    private void checksumMethods() {
        var m = DPF_CHECKSUM().assertCtMethodExists().filterChildren(ct -> ct instanceof CtExecutableReference).list();
        var nonStringCall = m.stream().filter(exe -> !((CtExecutableReference<?>) exe).getDeclaringType().getSimpleName().contains("String")).findAny();
        if (nonStringCall.isPresent()) {
            CtExecutableReference<?> exe = (CtExecutableReference<?>) nonStringCall.get();
            fail("getChecksumPredicate sollte nur Methoden von String aufrufen, enthält aber " + exe.getSignature() + " vom Typ " + exe.getDeclaringType());
        }
    }

    @Test
    public void checksumPredWorksMostly() {
        test()
            .addReq(DPF_CLASS()::resolveClass)
            .add(DPF_CLASS()::assertIsPlainClass)
            .addReq(DPF_CHECKSUM()::resolve)
            .add(DPF_CHECKSUM()::assertAccessModifier)
            .add(DPF_CHECKSUM()::assertParametersMatch)
//            .add(this::checksumMethods)
            .add(this::checksumShape)
            .add(() -> {
                var instanz = mock(DPF_CLASS().resolveClass().getActualClass(), Answers.CALLS_REAL_METHODS);
                DPF_CLASS().setClassInstance(instanz);
                var pred = (DoublePredicate) DPF_CHECKSUM().invoke(instanz, 3, 2);
                assertTrue(pred.test(1 / 8.0),
                    "Unerwartetes Ergebnis für"
                        + " DoublePredicateFactory.getChecksumPredicate(decimalPlaces=3, divisor=2).test(1 / 8.0)");
                assertTrue(pred.test(2 / 9.0),
                    "Unerwartetes Ergebnis für"
                        + " DoublePredicateFactory.getChecksumPredicate(decimalPlaces=3, divisor=2).test(2 / 9.0)");
                assertFalse(pred.test(5.111),
                    "Unerwartetes Ergebnis für"
                        + " DoublePredicateFactory.getChecksumPredicate(decimalPlaces=3, divisor=2).test(5.111)");
                assertTrue(pred.test(1.233),
                    "Unerwartetes Ergebnis für"
                        + " DoublePredicateFactory.getChecksumPredicate(decimalPlaces=3, divisor=2).test(1.233)");
                assertFalse(pred.test(1.234567),
                    "Unerwartetes Ergebnis für"
                        + " DoublePredicateFactory.getChecksumPredicate(decimalPlaces=3, divisor=2).test(1.234567)");

                pred = DPF_CHECKSUM().invoke(instanz, 5, 7);
                assertFalse(pred.test(0.14331412),
                    "Unerwartetes Ergebnis für"
                        + " DoublePredicateFactory.getChecksumPredicate(decimalPlaces=5, divisor=7).test(0.14331412)");
                assertFalse(pred.test(123123.14331414),
                    "Unerwartetes Ergebnis für"
                        + " DoublePredicateFactory.getChecksumPredicate(decimalPlaces=5, divisor=7).test(123123.14331412)");
                assertTrue(pred.test(923157.96785),
                    "Unerwartetes Ergebnis für"
                        + " DoublePredicateFactory.getChecksumPredicate(decimalPlaces=5, divisor=7).test(923157.96785)");
            }).run();
    }

    @Test
    public void checksumPredIsFullyCorrect() {
        test()
            .addReq(DPF_CLASS()::resolveClass)
            .add(DPF_CLASS()::assertIsPlainClass)
            .addReq(DPF_CHECKSUM()::resolve)
            .add(DPF_CHECKSUM()::assertAccessModifier)
            .add(DPF_CHECKSUM()::assertParametersMatch)
//            .add(this::checksumMethods)
            .add(this::checksumShape)
            .add(() -> {
                var instanz = mock(DPF_CLASS().resolveClass().getActualClass(), Answers.CALLS_REAL_METHODS);
                DPF_CLASS().setClassInstance(instanz);
                var pred = (DoublePredicate) DPF_CHECKSUM().invoke(instanz, 3, 2);
                assertFalse(pred.test(4.9),
                    "Unerwartetes Ergebnis für"
                        + " DoublePredicateFactory.getChecksumPredicate(decimalPlaces=3, divisor=2).test(4.9)");
                assertTrue(pred.test(0.24),
                    "Unerwartetes Ergebnis für"
                        + " DoublePredicateFactory.getChecksumPredicate(decimalPlaces=3, divisor=2).test(0.24)");
                assertTrue(pred.test(1 / 8.0),
                    "Unerwartetes Ergebnis für"
                        + " DoublePredicateFactory.getChecksumPredicate(decimalPlaces=3, divisor=2).test(1 / 8.0)");
                assertTrue(pred.test(2 / 9.0),
                    "Unerwartetes Ergebnis für"
                        + " DoublePredicateFactory.getChecksumPredicate(decimalPlaces=3, divisor=2).test(2 / 9.0)");
                assertFalse(pred.test(5.111),
                    "Unerwartetes Ergebnis für"
                        + " DoublePredicateFactory.getChecksumPredicate(decimalPlaces=3, divisor=2).test(5.111)");
                assertTrue(pred.test(1.233),
                    "Unerwartetes Ergebnis für"
                        + " DoublePredicateFactory.getChecksumPredicate(decimalPlaces=3, divisor=2).test(1.233)");
                assertFalse(pred.test(1.234567),
                    "Unerwartetes Ergebnis für"
                        + " DoublePredicateFactory.getChecksumPredicate(decimalPlaces=3, divisor=2).test(1.234567)");

                pred = DPF_CHECKSUM().invoke(instanz, 5, 7);
                assertTrue(pred.test(0.7),
                    "Unerwartetes Ergebnis für"
                        + " DoublePredicateFactory.getChecksumPredicate(decimalPlaces=5, divisor=7).test(0.7)");
                assertTrue(pred.test(2),
                    "Unerwartetes Ergebnis für"
                        + " DoublePredicateFactory.getChecksumPredicate(decimalPlaces=5, divisor=7).test(0.7)");
                assertFalse(pred.test(0.14331412),
                    "Unerwartetes Ergebnis für"
                        + " DoublePredicateFactory.getChecksumPredicate(decimalPlaces=5, divisor=7).test(0.14331412)");
                assertFalse(pred.test(123123.14331414),
                    "Unerwartetes Ergebnis für"
                        + " DoublePredicateFactory.getChecksumPredicate(decimalPlaces=5, divisor=7).test(123123.14331412)");
                assertTrue(pred.test(923157.96785),
                    "Unerwartetes Ergebnis für"
                        + " DoublePredicateFactory.getChecksumPredicate(decimalPlaces=5, divisor=7).test(923157.96785)");
            }).run();
    }
}
