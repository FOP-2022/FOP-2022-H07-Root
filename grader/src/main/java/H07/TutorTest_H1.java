package H07;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.sourcegrade.jagr.api.testing.TestCycle;
import org.sourcegrade.jagr.api.testing.extension.TestCycleResolver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.Duration;
import java.util.Arrays;
import java.util.function.DoublePredicate;

import static org.junit.jupiter.api.Assertions.*;

@TestForSubmission("H07")
public class TutorTest_H1 {

  @Test
  public void epsilonEnvironmentPredExists() {
    Class<?> epsilonEnvironmentPred = null;
    try {
      epsilonEnvironmentPred = Class
        .forName("H07.predicate.EpsilonEnvironmentPred");
    } catch (ClassNotFoundException e) {
      fail("Die Klasse EpsilonEnvironmentPred existiert nicht");
    }

    assertTrue(
      Modifier.isPublic(epsilonEnvironmentPred.getModifiers()), "EpsilonEnvironmentPred soll öffentlich sein");
    assertFalse(epsilonEnvironmentPred.isInterface(), "EpsilonEnvironmentPred soll eine Klasse sein");
    assertTrue(DoublePredicate.class.isAssignableFrom(epsilonEnvironmentPred));
  }

  @Test
  public void epsilonEnvironmentPredIsCorrect() {
    Class<?> epsilonEnvironmentPred = null;
    try {
      epsilonEnvironmentPred = Class
        .forName("H07.predicate.EpsilonEnvironmentPred");
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
      assertEquals(true, test.invoke(uut, 5.1));
      assertEquals(true, test.invoke(uut, 5.04));
      assertEquals(false, test.invoke(uut, 5.100000001));
      assertEquals(false, test.invoke(uut, 4.89999));
      assertEquals(true, test.invoke(uut, 4.9));
      assertEquals(true, test.invoke(uut, 5));

      uut = epsConstructor.newInstance(-2.5, 1.5);
      assertEquals(true, test.invoke(uut, -1.00000001));
      assertEquals(true, test.invoke(uut, -3));
      assertEquals(false, test.invoke(uut, -0.9));
      assertEquals(false, test.invoke(uut, 5));
      assertEquals(true, test.invoke(uut, -3.999));
      assertEquals(true, test.invoke(uut, -2.4));

      uut = epsConstructor.newInstance(0.0, -1);
      assertEquals(true, test.invoke(uut, 0.0));
      assertEquals(false, test.invoke(uut, -3));
      assertEquals(false, test.invoke(uut, -0.9));
      assertEquals(false, test.invoke(uut, 5));
      assertEquals(false, test.invoke(uut, 0.01));
    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
      fail("Der Konstruktor von EpsilonEnvironmentPred schlug fehl.");
    }
  }

  @Test
  public void complexDoublePredicateCreatorExist() {
    Class<?> creator = null;
    try {
      creator = Class
        .forName("H07.predicate.ComplexDoublePredicateCreator");
    } catch (ClassNotFoundException e) {
      fail("Die Klasse ComplexDoublePredicateCreator existiert nicht");
    }

    assertTrue(
      Modifier.isPublic(creator.getModifiers()),
      "ComplexDoublePredicateCreator soll öffentlich sein");
    assertFalse(creator.isInterface(),
      "ComplexDoublePredicateCreator soll eine Klasse sein");
    assertFalse(Modifier.isAbstract(creator.getModifiers()),
      "ComplexDoublePredicateCreator soll nicht abstrakt sein");
  }

  @Test
  public void buildComplexPredicateExists() {
    Class<?> creator = null;
    try {
      creator = Class.forName("H07.predicate.ComplexDoublePredicateCreator");
    } catch (ClassNotFoundException e) {
      fail("Die Klasse ComplexDoublePredicateCreator existiert nicht");
    }

    try {
      creator.getDeclaredMethod("buildComplexPredicate", DoublePredicate[][].class);
    } catch (NoSuchMethodException e) {
      fail("ComplexDoublePredicateCreator hat keine Methode buildComplexPredicate(DoublePredicate[][])");
    }
  }

  @Test
  public void complexDoublePredicateWorksSimple() {
    Class<?> creator = null;
    try {
      creator = Class.forName("H07.predicate.ComplexDoublePredicateCreator");
    } catch (ClassNotFoundException e) {
      fail("Die Klasse ComplexDoublePredicateCreator existiert nicht");
    }

    Method create = null;
    try {
      create = creator.getDeclaredMethod("buildComplexPredicate", DoublePredicate[][].class);
    } catch (NoSuchMethodException e) {
      fail("ComplexDoublePredicateCreator hat keine Methode buildComplexPredicate(DoublePredicate[][])");
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
      creator = Class.forName("H07.predicate.ComplexDoublePredicateCreator");
    } catch (ClassNotFoundException e) {
      fail("Die Klasse ComplexDoublePredicateCreator existiert nicht");
    }

    Method create = null;
    try {
      create = creator.getDeclaredMethod("buildComplexPredicate", DoublePredicate[][].class);
    } catch (NoSuchMethodException e) {
      fail("ComplexDoublePredicateCreator hat keine Methode buildComplexPredicate(DoublePredicate[][])");
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
      fail("Die Methode create von ComplexDoublePredicateCreator schlug fehl.", e);
    }
  }

  @Test
  public void getDefaultComplexPredicateExists() {
    Class<?> creator = null;
    try {
      creator = Class.forName("H07.predicate.ComplexDoublePredicateCreator");
    } catch (ClassNotFoundException e) {
      fail("Die Klasse ComplexDoublePredicateCreator existiert nicht");
    }

    try {
      creator.getDeclaredMethod("getDefaultComplexPredicate");
    } catch (NoSuchMethodException e) {
      fail("ComplexDoublePredicateCreator hat keine Methode getDefaultComplexPredicate()");
    }
  }

  @Test
  public void getDefaultComplexPredicateWorksMostOfTheTime() {
    Class<?> creator = null;
    try {
      creator = Class.forName("H07.predicate.ComplexDoublePredicateCreator");
    } catch (ClassNotFoundException e) {
      fail("Die Klasse ComplexDoublePredicateCreator existiert nicht");
    }

    Method create = null;
    try {
      create = creator.getDeclaredMethod("getDefaultComplexPredicate");
    } catch (NoSuchMethodException e) {
      fail("ComplexDoublePredicateCreator hat keine Methode getDefaultComplexPredicate()");
    }

    try {
      var pred = (DoublePredicate) create.invoke(null);
      assertFalse(pred.test(Math.PI));
      assertTrue(pred.test(2.5));
      assertTrue(pred.test(1.5));
      assertFalse(pred.test(0.0));
      assertFalse(pred.test(93.5));
    } catch (IllegalAccessException | InvocationTargetException e) {
      fail("Die Methode von complexDoublePredicate schlug fehl.", e);
    }
  }

  @Test
  @ExtendWith(TestCycleResolver.class)
  public void lambdaInCorrectForm(TestCycle testCycle) {
    var cdpc = testCycle.getSubmission().getSourceFile("H07/predicate/ComplexDoublePredicateCreator.java");
    assert cdpc != null;
    var content = cdpc.getContent();
    var statements = Arrays.asList(content.split(";"));
    assertTrue(content.contains("getDefaultComplexPredicate"), "Methode getDefaultComplexPredicate konnte nicht im Quelltext gefunden werden.");
    assertTrue(content.contains("new DoublePredicate[3][]"), "Array, der in getDefaultComplexPredicate erstellt " +
      "und als Argument an buildComplexPredicate weitergeben wird, konnte nicht gefunden werden.");
    assertTrue(statements.stream().anyMatch(st -> st.contains("new EpsilonEnvironmentPred") && st.contains("=")
      && !st.contains("(double")), "Keine Zuweisung mit new EpsilonEnvironmentPred für ersten Teilarray in getDefaultComplexPredicate");
    assertTrue(statements.stream().anyMatch(st -> st.contains("->") && st.contains("=")
      && !st.contains("(double") && !st.contains("return")), "Keine Zuweisung in Lambda-Kurzform für zweiten Teilarray in getDefaultComplexPredicate");
    assertTrue(statements.stream().anyMatch(st -> st.contains("->") && st.contains("=")
      && st.contains("(double") && st.contains("{") && st.contains("}")), "Keine Zuweisung mit Lambda-Standardform " +
      "für dritten Teilarray in getDefaultComplexPredicate");
  }

  @Test
  public void getDefaultComplexPredicateWorks() {
    Class<?> creator = null;
    try {
      creator = Class.forName("H07.predicate.ComplexDoublePredicateCreator");
    } catch (ClassNotFoundException e) {
      fail("Die Klasse ComplexDoublePredicateCreator existiert nicht");
    }

    Method create = null;
    try {
      create = creator.getDeclaredMethod("getDefaultComplexPredicate");
    } catch (NoSuchMethodException e) {
      fail("ComplexDoublePredicateCreator hat keine Methode getDefaultComplexPredicate()");
    }

    try {
      var pred = (DoublePredicate) create.invoke(null);
      assertFalse(pred.test(Math.PI));
      assertTrue(pred.test(2.49999));
      assertTrue(pred.test(1.5 - 0.99 / 50000));
      assertTrue(pred.test(1.5 + 0.99 / 50000));
      assertFalse(pred.test(1.5 - 1.01 / 50000));
      assertFalse(pred.test(1.5 + 1.01 / 50000));
      assertFalse(pred.test(0.4999999999));
      assertFalse(pred.test(93.5));
      assertTrue(pred.test(92.5)); // Only log^3 > x
      assertFalse(pred.test(125.5)); // Only sin > cos
      assertTrue(pred.test(126.5)); // Only sin > cos
      assertTrue(pred.test(127.5)); // Only sin > cos
      assertTrue(pred.test(128.5)); // Only sin > cos
      assertTrue(pred.test(129.5)); // Only sin > cos
      assertFalse(pred.test(130.5)); // Only sin > cos
      assertTrue(pred.test(994.5)); // Only sin > cos
      assertTrue(pred.test(995.5)); // Only sin > cos
      assertTrue(pred.test(996.5)); // Only sin > cos
      assertTrue(pred.test(996.5 - 3.99 / 50000)); // Close in 2 row
      assertFalse(pred.test(996.5 - 4.01 / 50000)); // Close in 2 row
      assertTrue(pred.test(996.5 + 3.99 / 50000)); // Close in 2 row
      assertFalse(pred.test(996.5 + 4.01 / 50000)); // Close in 2 row
      assertFalse(pred.test(-93.5));
    } catch (IllegalAccessException | InvocationTargetException e) {
      fail("Die Methode von complexDoublePredicate schlug fehl.", e);
    }
  }
}
