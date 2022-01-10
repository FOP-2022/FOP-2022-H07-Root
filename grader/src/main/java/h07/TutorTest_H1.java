package h07;

import org.junit.jupiter.api.Test;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.Duration;
import java.util.function.DoublePredicate;

import static org.junit.jupiter.api.Assertions.*;

@TestForSubmission("h07")
public class TutorTest_H1 {

    @Test
    public void epsilonEnvironmentPredExists() {
        Class<?> epsilonEnvironmentPred = null;
        try {
            epsilonEnvironmentPred = Class
                .forName("h07.predicate.EpsilonEnvironmentPred");
        } catch (ClassNotFoundException e) {
            fail("Die Klasse EpsilonEnvironmentPred existiert nicht");
        }

        assertTrue(
            Modifier.isPublic(epsilonEnvironmentPred.getModifiers()), "EpsilonEnvironmentPred soll öffentlich sein");
        assertFalse(epsilonEnvironmentPred.isInterface(), "EpsilonEnvironmentPred soll eine Klasse sein");
        assertTrue(DoublePredicate.class.isAssignableFrom(epsilonEnvironmentPred));
    }

    @Test
    public void epsilonEnvironmentPredMostlyCorrect() {
        Class<?> epsilonEnvironmentPred = null;
        try {
            epsilonEnvironmentPred = Class
                .forName("h07.predicate.EpsilonEnvironmentPred");
        } catch (ClassNotFoundException e) {
            fail("Die Klasse EpsilonEnvironmentPred existiert nicht");
        }
        Constructor<?> epsConstructor = null;
        try {
            epsConstructor = epsilonEnvironmentPred.getDeclaredConstructor(double.class, double.class);
        } catch (NoSuchMethodException e) {
            fail("Die Klasse EpsilonEnvironmentPred hat nicht den geforderten Konstruktor");
        }

        Method test = null;
        try {
            test = epsilonEnvironmentPred.getMethod("test", double.class);
        } catch (NoSuchMethodException e) {
            fail("Die Methode test von EpsilonEnvironmentPred konnte nicht gefunden werden");
        }
        try {
            var uut = epsConstructor.newInstance(5, 0.1);
            assertEquals(true, test.invoke(uut, 5.04), "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=5, epsilon=0.1).test(5.04)");
            assertEquals(false, test.invoke(uut, 4.89999), "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=5, epsilon=0.1).test(4.89999)");
            assertEquals(true, test.invoke(uut, 5), "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=5, epsilon=0.1).test(5)");

            uut = epsConstructor.newInstance(-2.5, 1.5);
            assertEquals(true, test.invoke(uut, -3), "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=-2.5, epsilon=1.5).test(-3)");
            assertEquals(false, test.invoke(uut, -0.9), "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=-2.5, epsilon=1.5).test(-0.9)");
            assertEquals(false, test.invoke(uut, 5), "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=-2.5, epsilon=1.5).test(5)");
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            fail("Der Konstruktor von EpsilonEnvironmentPred schlug fehl.");
        }
    }

    @Test
    public void epsilonEnvironmentPredIsCorrect() {
        Class<?> epsilonEnvironmentPred = null;
        try {
            epsilonEnvironmentPred = Class
                .forName("h07.predicate.EpsilonEnvironmentPred");
        } catch (ClassNotFoundException e) {
            fail("Die Klasse EpsilonEnvironmentPred existiert nicht");
        }
        Constructor<?> epsConstructor = null;
        try {
            epsConstructor = epsilonEnvironmentPred.getDeclaredConstructor(double.class, double.class);
        } catch (NoSuchMethodException e) {
            fail("Die Klasse EpsilonEnvironmentPred hat nicht den geforderten Konstruktor");
        }

        Method test = null;
        try {
            test = epsilonEnvironmentPred.getMethod("test", double.class);
        } catch (NoSuchMethodException e) {
            fail("Die Methode test von EpsilonEnvironmentPred konnte nicht gefunden werden");
        }
        try {
            var uut = epsConstructor.newInstance(5, 0.1);
            assertEquals(true, test.invoke(uut, 5.1), "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=5, epsilon=0.1).test(5.1)");
            assertEquals(true, test.invoke(uut, 5.04), "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=5, epsilon=0.1).test(5.04)");
            assertEquals(false, test.invoke(uut, 5.100000001), "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=5, epsilon=0.1).test(5.100000001)");
            assertEquals(false, test.invoke(uut, 4.89999), "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=5, epsilon=0.1).test(4.89999)");
            assertEquals(true, test.invoke(uut, 4.9), "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=5, epsilon=0.1).test(4.9)");
            assertEquals(true, test.invoke(uut, 5), "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=5, epsilon=0.1).test(5)");

            uut = epsConstructor.newInstance(-2.5, 1.5);
            assertEquals(true, test.invoke(uut, -1.00000001), "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=-2.5, epsilon=1.5).test(-1.00000001)");
            assertEquals(true, test.invoke(uut, -3), "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=-2.5, epsilon=1.5).test(-3)");
            assertEquals(false, test.invoke(uut, -0.9), "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=-2.5, epsilon=1.5).test(-0.9)");
            assertEquals(false, test.invoke(uut, 5), "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=-2.5, epsilon=1.5).test(5)");
            assertEquals(true, test.invoke(uut, -3.999), "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=-2.5, epsilon=1.5).test(-3.999)");
            assertEquals(true, test.invoke(uut, -2.4), "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=-2.5, epsilon=1.5).test(-2.4)");

            uut = epsConstructor.newInstance(0.0, -1);
            assertEquals(true, test.invoke(uut, 0.0), "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=0.0, epsilon=-1).test(0.0) (Negatives Epsilon führt zu epsilon=0.0)");
            assertEquals(false, test.invoke(uut, -0.01), "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=0.0, epsilon=-1).test(-0.01) (Negatives Epsilon führt zu epsilon=0.0)");
            assertEquals(false, test.invoke(uut, 1.1), "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=0.0, epsilon=-1).test(1.1) (Negatives Epsilon führt zu epsilon=0.0)");
            assertEquals(false, test.invoke(uut, -2), "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=0.0, epsilon=-1).test(-2) (Negatives Epsilon führt zu epsilon=0.0)");
            assertEquals(false, test.invoke(uut, 0.01), "Unerwartetes Ergebnis für new EpsilonEnvironmentPredicate(x=0.0, epsilon=-1).test(0.01) (Negatives Epsilon führt zu epsilon=0.0)");
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            fail("Der Konstruktor von EpsilonEnvironmentPred schlug fehl.");
        }
    }

    @Test
    public void doublePredicateFactoryExist() {
        Class<?> creator = null;
        try {
            creator = Class
                .forName("h07.predicate.DoublePredicateFactory");
        } catch (ClassNotFoundException e) {
            fail("Die Klasse DoublePredicateFactory existiert nicht");
        }

        assertTrue(
            Modifier.isPublic(creator.getModifiers()),
            "DoublePredicateFactory soll öffentlich sein");
        assertFalse(creator.isInterface(),
            "DoublePredicateFactory soll eine Klasse sein");
        assertFalse(Modifier.isAbstract(creator.getModifiers()),
            "DoublePredicateFactory soll nicht abstrakt sein");
    }

    @Test
    public void buildComplexPredicateExists() {
        Class<?> creator = null;
        try {
            creator = Class.forName("h07.predicate.DoublePredicateFactory");
        } catch (ClassNotFoundException e) {
            fail("Die Klasse DoublePredicateFactory existiert nicht");
        }

        try {
            creator.getDeclaredMethod("buildComplexPredicate", DoublePredicate[][].class);
        } catch (NoSuchMethodException e) {
            fail("DoublePredicateFactory hat keine Methode buildComplexPredicate(DoublePredicate[][])");
        }
    }

    @Test
    public void complexDoublePredicateWorksSimple() {
        Class<?> creator = null;
        try {
            creator = Class.forName("h07.predicate.DoublePredicateFactory");
        } catch (ClassNotFoundException e) {
            fail("Die Klasse DoublePredicateFactory existiert nicht");
        }

        Method create = null;
        try {
            create = creator.getDeclaredMethod("buildComplexPredicate", DoublePredicate[][].class);
        } catch (NoSuchMethodException e) {
            fail("DoublePredicateFactory hat keine Methode buildComplexPredicate(DoublePredicate[][])");
        }

        try {
            DoublePredicate[][] predicates = new DoublePredicate[][]{
                new DoublePredicate[]{d -> true}
            };
            var pred = create.invoke(null, (Object) predicates);
            assertTrue(((DoublePredicate) pred).test(2.0));
            assertTrue(((DoublePredicate) pred).test(-2.3));
            predicates = new DoublePredicate[][]{
                new DoublePredicate[]{d -> d < 3}
            };
            pred = create.invoke(null, (Object) predicates);
            assertTrue(((DoublePredicate) pred).test(2.0));
            assertFalse(((DoublePredicate) pred).test(5));
            assertFalse(((DoublePredicate) pred).test(3));
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail("Die Methode von complexDoublePredicate schlug fehl.", e);
        }
    }

    @Test
    public void complexDoublePredicateWorksAll() {
        Class<?> creator = null;
        try {
            creator = Class.forName("h07.predicate.DoublePredicateFactory");
        } catch (ClassNotFoundException e) {
            fail("Die Klasse DoublePredicateFactory existiert nicht");
        }

        Method create = null;
        try {
            create = creator.getDeclaredMethod("buildComplexPredicate", DoublePredicate[][].class);
        } catch (NoSuchMethodException e) {
            fail("DoublePredicateFactory hat keine Methode buildComplexPredicate(DoublePredicate[][])");
        }

        try {
            DoublePredicate[][] predicates = new DoublePredicate[][]{
                new DoublePredicate[]{d -> true},
                new DoublePredicate[]{d -> false, d -> true, d -> false},
                new DoublePredicate[]{d -> d > 0, d -> d < -3}
            };
            var pred = create.invoke(null, (Object) predicates);
            assertTrue(((DoublePredicate) pred).test(2.0));
            assertTrue(((DoublePredicate) pred).test(6.2));
            assertTrue(((DoublePredicate) pred).test(-5));
            assertFalse(((DoublePredicate) pred).test(0.0));
            assertFalse(((DoublePredicate) pred).test(-1));
            assertFalse(((DoublePredicate) pred).test(-0.01));
            assertFalse(((DoublePredicate) pred).test(-2.3));
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
            pred = create.invoke(null, (Object) timeout);
            Object finalPred = pred;
            assertTimeoutPreemptively(Duration.ofMillis(10), () -> {
                ((DoublePredicate) finalPred).test(1);
            }, "Die Verknüpfungsreihenfolge entspricht nicht den Anforderungen. Erkennbar an zu langer Ausführungszeit.");
            assertTrue(((DoublePredicate) pred).test(2.0));
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail("Die Methode create von DoublePredicateFactory schlug fehl.", e);
        }
    }

    @Test
    public void getDefaultComplexPredicateExists() {
        Class<?> creator = null;
        try {
            creator = Class.forName("h07.predicate.DoublePredicateFactory");
        } catch (ClassNotFoundException e) {
            fail("Die Klasse DoublePredicateFactory existiert nicht");
        }

        try {
            creator.getDeclaredMethod("getDefaultComplexPredicate");
        } catch (NoSuchMethodException e) {
            fail("DoublePredicateFactory hat keine Methode getDefaultComplexPredicate()");
        }
    }

    @Test
    public void getDefaultComplexPredicateWorksMostOfTheTime() {
        Class<?> creator = null;
        try {
            creator = Class.forName("h07.predicate.DoublePredicateFactory");
        } catch (ClassNotFoundException e) {
            fail("Die Klasse DoublePredicateFactory existiert nicht");
        }

        Method create = null;
        try {
            create = creator.getDeclaredMethod("getDefaultComplexPredicate");
        } catch (NoSuchMethodException e) {
            fail("DoublePredicateFactory hat keine Methode getDefaultComplexPredicate()");
        }

        try {
            var pred = (DoublePredicate) create.invoke(null);
            assertFalse(pred.test(Math.PI), "Math.PI sollte getDefaultComplexPredicate nicht erfüllen [nicht nah genug an 3.5]");
            assertTrue(pred.test(2.5), "2.5 sollte getDefaultComplexPredicate erfüllen [sin > cos, ~= 2.5]");
            assertTrue(pred.test(1.5), "1.5 sollte getDefaultComplexPredicate erfüllen [sin > cos, ~= 1.5]");
            assertFalse(pred.test(0.0), "0.0 sollte getDefaultComplexPredicate nicht erfüllen [nicht nah genug an 0.5]");
            assertTrue(pred.test(0.5), "0.5 sollte getDefaultComplexPredicate erfüllen [sin > cos && < 10*e, ~= 0.5]");
            assertFalse(pred.test(93.5), "93.5 sollte getDefaultComplexPredicate nicht erfüllen da keines der Predicates von Predicate-Teil 3 zutrifft");
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail("Die Methode von complexDoublePredicate schlug fehl.", e);
        }
    }

    @Test
    public void getDefaultComplexPredicateWorks() {
        Class<?> creator = null;
        try {
            creator = Class.forName("h07.predicate.DoublePredicateFactory");
        } catch (ClassNotFoundException e) {
            fail("Die Klasse DoublePredicateFactory existiert nicht");
        }

        Method create = null;
        try {
            create = creator.getDeclaredMethod("getDefaultComplexPredicate");
        } catch (NoSuchMethodException e) {
            fail("DoublePredicateFactory hat keine Methode getDefaultComplexPredicate()");
        }

        try {
            var pred = (DoublePredicate) create.invoke(null);
            assertFalse(pred.test(Math.PI), "Math.PI sollte getDefaultComplexPredicate nicht erfüllen [nicht nah genug an 3.5]");
            assertTrue(pred.test(2.49999), "2.49999 sollte getDefaultComplexPredicate erfüllen [sin > cos, ~= 2.5]");
            assertTrue(pred.test(1.5 - 0.99 / 50000), "1.5 - 0.99 / 50000 sollte getDefaultComplexPredicate erfüllen [sin > cos, ~= 1.5]");
            assertTrue(pred.test(1.5 + 0.99 / 50000), "1.5 - 0.99 / 50000 sollte getDefaultComplexPredicate erfüllen [sin > cos, ~= 1.5]");
            assertFalse(pred.test(1.5 - 1.01 / 50000), "1.5 - 1.01 / 50000 sollte getDefaultComplexPredicate nicht erfüllen [nicht nah genug 1.5 für Predicate-Teil 1]");
            assertFalse(pred.test(1.5 + 1.01 / 50000), "1.5 + 1.01 / 50000 sollte getDefaultComplexPredicate nicht erfüllen [nicht nah genug 1.5 für Predicate-Teil 1]");
            assertFalse(pred.test(0.4999999999), "0.4999999999 sollte getDefaultComplexPredicate nicht erfüllen [nicht nah genug an 0.5 für Predicate-Teil 1]");
            assertTrue(pred.test(0.5), "0.5 sollte getDefaultComplexPredicate erfüllen [sin > cos && < 10*e, ~= 0.5]");
            assertFalse(pred.test(93.5), "93.5 sollte getDefaultComplexPredicate nicht erfüllen da keines der Predicates von Predicate-Teil 3 zutrifft");
            assertTrue(pred.test(92.5), "92.5 / 50000 sollte getDefaultComplexPredicate erfüllen [x < log(x)^3, ~= 92.5]");
            assertFalse(pred.test(125.5), "125.5 sollte getDefaultComplexPredicate nicht erfüllen da keines der Predicates von Predicate-Teil 3 zutrifft");
            assertTrue(pred.test(126.5), "126.5 sollte getDefaultComplexPredicate erfüllen [sin > cos, ~= 126.5]");
            assertTrue(pred.test(127.5), "127.5 sollte getDefaultComplexPredicate erfüllen [sin > cos, ~= 127.5]");
            assertTrue(pred.test(128.5), "128.5 sollte getDefaultComplexPredicate erfüllen [sin > cos, ~= 128.5]");
            assertTrue(pred.test(129.5), "129.5 sollte getDefaultComplexPredicate erfüllen [sin > cos, ~= 129.5]");
            assertFalse(pred.test(130.5), "130.5 sollte getDefaultComplexPredicate nicht erfüllen da keines der Predicates von Predicate-Teil 3 zutrifft");
            assertTrue(pred.test(994.5), "994.5 sollte getDefaultComplexPredicate erfüllen [sin > cos, ~= 994.5]");
            // Etwas breitere Spanne für Teilarray an Index 1, da Beschreibung schwierig war.
            assertTrue(pred.test(996.5 - 2.99 / 50000), "996.5 - 2.99 / 50000 sollte getDefaultComplexPredicate erfüllen [sin > cos, ~= 996.5]");
            assertFalse(pred.test(996.5 - 5.01 / 50000), "996.5 - 5.01 / 50000 sollte getDefaultComplexPredicate nicht erfüllen [nicht nah genug 996.5 für Predicate-Teil 2]");
            assertTrue(pred.test(996.5 + 2.99 / 50000), "996.5 + 2.99 / 50000 sollte getDefaultComplexPredicate erfüllen [sin > cos, ~= 996.5]");
            assertFalse(pred.test(996.5 + 5.01 / 50000), "996.5 + 5.01 / 50000 sollte getDefaultComplexPredicate nicht erfüllen [nicht nah genug 996.5 für Predicate-Teil 2]");
            assertFalse(pred.test(-93.5), "-93.5 sollte getDefaultComplexPredicate nicht erfüllen da u.A. keines der Predicates von Predicate-Teil 3 zutrifft");
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail("Die Methode von complexDoublePredicate schlug fehl.", e);
        }
    }

    @Test
    public void checksumPredExists() {
        Class<?> creator = null;
        try {
            creator = Class.forName("h07.predicate.DoublePredicateFactory");
        } catch (ClassNotFoundException e) {
            fail("Die Klasse DoublePredicateFactory existiert nicht");
        }

        Method checksum = null;
        try {
            checksum = creator.getDeclaredMethod("getChecksumPredicate", int.class, int.class);
        } catch (NoSuchMethodException e) {
            fail("DoublePredicateFactory hat keine Methode getChecksumPredicate(int, int)");
        }
    }

    @Test
    public void checksumPredWorksMostly() {
        Class<?> creator = null;
        try {
            creator = Class.forName("h07.predicate.DoublePredicateFactory");
        } catch (ClassNotFoundException e) {
            fail("Die Klasse DoublePredicateFactory existiert nicht");
        }

        Method checksum = null;
        try {
            checksum = creator.getDeclaredMethod("getChecksumPredicate", int.class, int.class);
        } catch (NoSuchMethodException e) {
            fail("DoublePredicateFactory hat keine Methode getChecksumPredicate(int, int)");
        }

        try {
            var pred = (DoublePredicate) checksum.invoke(null, 3, 2);
            assertTrue(pred.test(1 / 8.0), "Unerwartetes Ergebnis für DoublePredicateFactory.getChecksumPredicate(decimalPlaces=3, divisor=2).test(1 / 8.0)");
            assertTrue(pred.test(2 / 9.0), "Unerwartetes Ergebnis für DoublePredicateFactory.getChecksumPredicate(decimalPlaces=3, divisor=2).test(2 / 9.0)");
            assertFalse(pred.test(5.111), "Unerwartetes Ergebnis für DoublePredicateFactory.getChecksumPredicate(decimalPlaces=3, divisor=2).test(5.111)");
            assertTrue(pred.test(1.233), "Unerwartetes Ergebnis für DoublePredicateFactory.getChecksumPredicate(decimalPlaces=3, divisor=2).test(1.233)");
            assertFalse(pred.test(1.234567), "Unerwartetes Ergebnis für DoublePredicateFactory.getChecksumPredicate(decimalPlaces=3, divisor=2).test(1.234567)");

            pred = (DoublePredicate) checksum.invoke(null, 5, 7);
            assertFalse(pred.test(0.14331412), "Unerwartetes Ergebnis für DoublePredicateFactory.getChecksumPredicate(decimalPlaces=5, divisor=7).test(0.14331412)");
            assertFalse(pred.test(123123.14331414), "Unerwartetes Ergebnis für DoublePredicateFactory.getChecksumPredicate(decimalPlaces=5, divisor=7).test(123123.14331412)");
            assertTrue(pred.test(923157.96785), "Unerwartetes Ergebnis für DoublePredicateFactory.getChecksumPredicate(decimalPlaces=5, divisor=7).test(923157.96785)");
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail("DoublePredicateFactory.getChecksumPredicate bzw. die test-Methode vom Ergebnis schlug fehl. ", e);
        }
    }

    @Test
    public void checksumPredIsFullyCorrect() {
        Class<?> creator = null;
        try {
            creator = Class.forName("h07.predicate.DoublePredicateFactory");
        } catch (ClassNotFoundException e) {
            fail("Die Klasse DoublePredicateFactory existiert nicht");
        }

        Method checksum = null;
        try {
            checksum = creator.getDeclaredMethod("getChecksumPredicate", int.class, int.class);
        } catch (NoSuchMethodException e) {
            fail("DoublePredicateFactory hat keine Methode getChecksumPredicate(int, int)");
        }

        try {
            var pred = (DoublePredicate) checksum.invoke(null, 3, 2);
            assertFalse(pred.test(4.9), "Unerwartetes Ergebnis für DoublePredicateFactory.getChecksumPredicate(decimalPlaces=3, divisor=2).test(4.9)");
            assertTrue(pred.test(0.24), "Unerwartetes Ergebnis für DoublePredicateFactory.getChecksumPredicate(decimalPlaces=3, divisor=2).test(0.24)");
            assertTrue(pred.test(1 / 8.0), "Unerwartetes Ergebnis für DoublePredicateFactory.getChecksumPredicate(decimalPlaces=3, divisor=2).test(1 / 8.0)");
            assertTrue(pred.test(2 / 9.0), "Unerwartetes Ergebnis für DoublePredicateFactory.getChecksumPredicate(decimalPlaces=3, divisor=2).test(2 / 9.0)");
            assertFalse(pred.test(5.111), "Unerwartetes Ergebnis für DoublePredicateFactory.getChecksumPredicate(decimalPlaces=3, divisor=2).test(5.111)");
            assertTrue(pred.test(1.233), "Unerwartetes Ergebnis für DoublePredicateFactory.getChecksumPredicate(decimalPlaces=3, divisor=2).test(1.233)");
            assertFalse(pred.test(1.234567), "Unerwartetes Ergebnis für DoublePredicateFactory.getChecksumPredicate(decimalPlaces=3, divisor=2).test(1.234567)");

            pred = (DoublePredicate) checksum.invoke(null, 5, 7);
            assertTrue(pred.test(0.7), "Unerwartetes Ergebnis für DoublePredicateFactory.getChecksumPredicate(decimalPlaces=5, divisor=7).test(0.7)");
            assertTrue(pred.test(2), "Unerwartetes Ergebnis für DoublePredicateFactory.getChecksumPredicate(decimalPlaces=5, divisor=7).test(0.7)");
            assertFalse(pred.test(0.14331412), "Unerwartetes Ergebnis für DoublePredicateFactory.getChecksumPredicate(decimalPlaces=5, divisor=7).test(0.14331412)");
            assertFalse(pred.test(123123.14331414), "Unerwartetes Ergebnis für DoublePredicateFactory.getChecksumPredicate(decimalPlaces=5, divisor=7).test(123123.14331412)");
            assertTrue(pred.test(923157.96785), "Unerwartetes Ergebnis für DoublePredicateFactory.getChecksumPredicate(decimalPlaces=5, divisor=7).test(923157.96785)");
        } catch (IllegalAccessException | InvocationTargetException e) {
            fail("DoublePredicateFactory.getChecksumPredicate bzw. die test-Methode vom Ergebnis schlug fehl. ", e);
        }
    }
}
