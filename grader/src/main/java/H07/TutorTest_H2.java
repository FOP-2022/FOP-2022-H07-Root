package H07;

import H07.person.Person;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import org.junit.jupiter.api.Test;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntBinaryOperator;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@TestForSubmission("H07")
public class TutorTest_H2 {

  @Test
  public void personExist() {
    Class<?> person = TestUtils.getPersonClass("Person");

    assertTrue(Modifier.isPublic(person.getModifiers()),
            "Die Klasse Person soll öffentlich sein");
    assertFalse(Modifier.isInterface(person.getModifiers()),
            "Die Klasse Person soll kein Interface sein");

    Constructor<?> constructor = null;
    Person p = null;
    for (Constructor<?> cons : person.getDeclaredConstructors()) {
      assertEquals(5, cons.getParameterCount());
      try {
        p = (Person) cons.newInstance("l", "f", "s", 1, 2);
      } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
        fail("Der Konstruktor konnte nicht mit den Parametern [\"l\", \"f\", \"s\", 1, 2] aufgerufen werden");
      }
    }
    assertEquals("l", TestUtils.personGet(person, p, "lastName"));
    assertEquals("f", TestUtils.personGet(person, p, "firstName"));
    assertEquals("s", TestUtils.personGet(person, p, "street"));
    assertEquals(1, TestUtils.personGet(person, p, "houseNumber"));
    assertEquals(2, TestUtils.personGet(person, p, "postalCode"));
  }

  @Test
  public void personFilterExist() {
    Class<?> personFilter = TestUtils.getPersonClass("PersonFilter");
    Class<?> person = TestUtils.getPersonClass("Person");

    assertTrue(personFilter.isAnnotationPresent(FunctionalInterface.class),
      "Die Klasse PersonFilter soll ein FunctionalInterface sein");
    assertTrue(Modifier.isPublic(personFilter.getModifiers()),
      "Die Klasse PersonFilter soll öffentlich sein");

    Method method = null;
    try {
      method = personFilter.getMethod("test", person);
    } catch (NoSuchMethodException e) {
      fail("Die Methode PersonFilter.test konnte nicht gefunden werden");
    }
  }

  @Test
  public void personToIntFunctionExist() {
    Class<?> personToIntFunction = TestUtils.getPersonClass("PersonToIntFunction");
    Class<?> person = TestUtils.getPersonClass("Person");

    assertTrue(personToIntFunction.isAnnotationPresent(FunctionalInterface.class),
            "Die Klasse PersonToIntFunction soll ein FunctionalInterface sein");
    assertTrue(Modifier.isPublic(personToIntFunction.getModifiers()),
            "Die Klasse PersonToIntFunction soll öffentlich sein");

    Method method = null;
    try {
      method = personToIntFunction.getMethod("apply", person);
    } catch (NoSuchMethodException e) {
      fail("Die Methode PersonFilter.apply konnte nicht gefunden werden");
    }
  }

  @Test
  public void traitsExist() {
    Class<?> traits = TestUtils.getPersonClass("Traits");

    assertEquals(Object.class, traits.getSuperclass(),
            "Die Klasse Traits soll lediglich von Object erben");
    assertTrue(Modifier.isPublic(traits.getModifiers()),
            "Die Klasse Traits soll öffentlich sein");
    assertFalse(Modifier.isAbstract(traits.getModifiers()),
            "Die Klasse Traits soll nicht abstrakt sein");
  }


  @Test
  public void traitsCorrect() {
    Class<?> traits = TestUtils.getPersonClass("Traits");

    assertEquals(Object.class, traits.getSuperclass(),
            "Die Klasse Traits soll lediglich von Object erben");
    assertTrue(Modifier.isPublic(traits.getModifiers()),
            "Die Klasse Traits soll öffentlich sein");
    assertFalse(Modifier.isAbstract(traits.getModifiers()),
            "Die Klasse Traits soll nicht abstrakt sein");
    var expectedFields = List.of("op", "init", "fct", "pred");
    var expectedFieldClasses = List.of(IntBinaryOperator.class, int.class,
            TestUtils.getPersonClass("PersonToIntFunction"), TestUtils.getPersonClass("PersonFilter"));
    var actualFields = Arrays.stream(traits.getDeclaredFields())
            .map(Field::getName).filter(fn -> !fn.equals("combine"))
            .collect(Collectors.toList());
    assertTrue(actualFields.containsAll(expectedFields), "Die Attribute [op, init, fct, pred] " +
            "sollten alle in Traits vorhanden sein.");
    TestUtils.forEach(expectedFields, expectedFieldClasses, (f, c) -> fieldAndGetterCorrect(traits, f, c));

    var consOpt = Arrays.stream(traits.getConstructors()).filter(c -> c.getParameterCount() >= 4).findAny();
    if (consOpt.isEmpty()) {
      fail("Traits hat keinen geeigneten Konstruktor");
    }
    Object traitsObj;
    try {
      var cons = consOpt.get();
      IntBinaryOperator op = (a, b) -> a + b + 1;
      var personToIntFunction = TestUtils.personToIntFunction();
      var personFilter = TestUtils.personFilter();
      if (cons.getParameterCount() == 5) {
        traitsObj = cons.newInstance(op, 357, personToIntFunction, personFilter, null);
      } else {
        traitsObj = cons.newInstance(op, 357, personToIntFunction, personFilter);
      }
      assertEquals(op, TestUtils.get(traits, traitsObj, "op"), "Op-Getter falsch in Traits");
      assertEquals(personToIntFunction, TestUtils.get(traits, traitsObj, "fct"), "Fct-Getter falsch in Traits");
      assertEquals(personFilter, TestUtils.get(traits, traitsObj, "pred"), "Pred-Getter falsch in Traits");
      assertEquals(357, TestUtils.get(traits, traitsObj, "init"),"Init-Getter falsch in Traits");
    } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
      fail("Traits-Objekt konnte nicht richtig erstellt oder verwendet werden", e);
    }
  }

  // Only for non-boolean getters
  private void fieldAndGetterCorrect(Class<?> clazz, String field, Class<?> expectedFieldClass) {
    var f = TestUtils.getField(clazz, field);
    assertTrue(Modifier.isFinal(f.getModifiers()));
    assertTrue(Modifier.isPrivate(f.getModifiers()));
    assertEquals(expectedFieldClass, f.getType());
    var getter = TestUtils.getMethod(clazz, "get" + field.substring(0, 1).toUpperCase() + field.substring(1));
    assertTrue(Modifier.isPublic(getter.getModifiers()));
    assertEquals(expectedFieldClass, getter.getReturnType());
  }

  @Test
  public void functionWithFilterMapAndFoldExists() {
    Class<?> fwfmf = TestUtils.getPersonClass("FunctionWithFilterMapAndFold");
    Class<?> traits = TestUtils.getPersonClass("Traits");

    assertEquals(Object.class, fwfmf.getSuperclass(),
            "Die Klasse FunctionWithFilterMapAndFold soll lediglich von Object erben");
    assertTrue(Modifier.isPublic(fwfmf.getModifiers()),
            "Die Klasse FunctionWithFilterMapAndFold soll öffentlich sein");
    assertTrue(Modifier.isAbstract(fwfmf.getModifiers()),
            "Die Klasse FunctionWithFilterMapAndFold soll abstrakt sein");
    var f = TestUtils.getField(fwfmf, "traits");
    assertTrue(Modifier.isFinal(f.getModifiers()));
    assertTrue(Modifier.isProtected(f.getModifiers()));
    assertEquals(traits, f.getType());

    var cons = Arrays.stream(fwfmf.getConstructors())
            .filter(c -> c.getParameterCount() == 1 && c.getParameters()[0].getType().equals(traits))
            .findAny().orElse(null);
    if (cons == null) {
      fail("Kein geeigneter Konstuktor von FunctionWithFilterMapAndFold konnte gefunden werden.");
    }
    assertTrue(Modifier.isPublic(cons.getModifiers()));

    var apply = TestUtils.getMethod(fwfmf, "apply", Person[].class);
    assertTrue(Modifier.isPublic(apply.getModifiers()));
    assertTrue(Modifier.isAbstract(apply.getModifiers()));
    assertEquals(int.class, apply.getReturnType());

    Class<?> fwfmf1 = TestUtils.getPersonClass("MyFunctionWithFilterMapAndFold1");

    assertEquals(fwfmf, fwfmf1.getSuperclass(),
            "Die Klasse MyFunctionWithFilterMapAndFold1 soll von FunctionWithFilterMapAndFold erben");
    assertTrue(Modifier.isPublic(fwfmf1.getModifiers()),
            "Die Klasse MyFunctionWithFilterMapAndFold1 soll öffentlich sein");
    assertFalse(Modifier.isAbstract(fwfmf1.getModifiers()),
            "Die Klasse MyFunctionWithFilterMapAndFold1 soll nicht abstrakt sein");

    Class<?> fwfmf2 = TestUtils.getPersonClass("MyFunctionWithFilterMapAndFold2");

    assertEquals(fwfmf, fwfmf2.getSuperclass(),
            "Die Klasse MyFunctionWithFilterMapAndFold2 soll von FunctionWithFilterMapAndFold erben");
    assertTrue(Modifier.isPublic(fwfmf2.getModifiers()),
            "Die Klasse MyFunctionWithFilterMapAndFold2 soll öffentlich sein");
    assertFalse(Modifier.isAbstract(fwfmf2.getModifiers()),
            "Die Klasse MyFunctionWithFilterMapAndFold2 soll nicht abstrakt sein");
  }

  @Test
  public void myFunctionWithFilterMapAndFold1Partly() {

    var erroneous = determineWrongFilterMapFold();

    assertTrue(erroneous.size() <= 1, "Bei MyFunctionWithFilterMapAndFold1" +
            " schlugen folgende Funktionen fehl: " + erroneous.toString());
  }

  private List<String> determineWrongFilterMapFold() {
    List<String> erroneous = new ArrayList<>();

    var classUT = TestUtils.getPersonClass("MyFunctionWithFilterMapAndFold1");
    assertTrue(Modifier.isPublic(classUT.getModifiers()),
            "Die Klasse MyFunctionWithFilterMapAndFold1 soll öffentlich sein");
    assertFalse(Modifier.isAbstract(classUT.getModifiers()),
            "Die Klasse MyFunctionWithFilterMapAndFold1 soll nicht abstrakt sein");
    try {
      var filter = TestUtils.getMethod(classUT, "filter",
              Array.newInstance(TestUtils.getPersonClass("Person"), 0).getClass(),
              TestUtils.getPersonClass("PersonFilter"));
      var personFilter = TestUtils.personFilter();
      var actual = TestUtils.invokeMethod(filter, null, TestUtils.people(), personFilter);
      TestUtils.assertPeopleEquals((Object[]) TestUtils.filtered(), (Object[]) actual);
    } catch (Throwable t) {
      erroneous.add("Filter");
    }

    try {
      var map = TestUtils.getMethod(classUT, "map",
              Array.newInstance(TestUtils.getPersonClass("Person"), 0).getClass(),
              TestUtils.getPersonClass("PersonToIntFunction"));
      var personToIntFunction = TestUtils.personToIntFunction();
      var actual = TestUtils.invokeMethod(map, null, TestUtils.people(), personToIntFunction);
      assertArrayEquals(TestUtils.mapped, (int[]) actual);
    } catch (Throwable t) {
      erroneous.add("Map");
    }

    try {
      var foldl = TestUtils.getMethod(classUT, "foldl",
              int[].class, int.class,
              IntBinaryOperator.class);
      List<IntBinaryOperator> functions = List.of(Integer::sum, Integer::sum, (a, b) -> a - b);
      List<Integer> inits = List.of(0, 5, 5);
      List<Integer> expected = List.of(128613, 128618, -128608);
      TestUtils.forEach(functions, inits, expected, (f, init, result) -> {
        var actual = TestUtils.invokeMethod(foldl, null, TestUtils.mapped, init, f);
        assertEquals(result, actual, "Für Eingabe " + Arrays.toString(TestUtils.mapped) + ", Init " + init +
                " und " + (result < 0 ? "Substraktion" : "Addition") +
                " folgt bei foldl nicht der erwartete Wert " + result + ".");
      });
    } catch (Throwable t) {
      erroneous.add("Foldl[" + t.getMessage() + "]");
    }
    return erroneous;
  }

  @Test
  public void myFunctionWithFilterMapAndFold1All() {
    var erroneous = determineWrongFilterMapFold();
    assertEquals(0, erroneous.size(), "Bei MyFunctionWithFilterMapAndFold1" +
            " schlugen folgende Funktionen fehl: " + erroneous.toString());
    var classUT = TestUtils.getPersonClass("MyFunctionWithFilterMapAndFold1");
    assertTrue(Modifier.isPublic(classUT.getModifiers()),
            "Die Klasse MyFunctionWithFilterMapAndFold1 soll öffentlich sein");
    assertFalse(Modifier.isAbstract(classUT.getModifiers()),
            "Die Klasse MyFunctionWithFilterMapAndFold1 soll nicht abstrakt sein");
    var apply = TestUtils.getMethod(classUT, "apply",
            Array.newInstance(TestUtils.getPersonClass("Person"), 0).getClass());
    var traitsObj = TestUtils.getTraitsObject(false);
    Object uut;
    try {
      uut = classUT.getConstructor(TestUtils.getPersonClass("Traits")).newInstance(traitsObj);
      var actual = TestUtils.invokeMethod(apply, uut, TestUtils.filtered());
      assertEquals(381, actual);
    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
      fail("Der Konstruktor von MyFunctionWithFilterMapAndFold1 konnte nicht mit einem Traits Objekt aufgerufen werden");
    }
  }

  @Test
  public void myFunctionWithFilterMapAndFoldCorrect2() {
    var classUT = TestUtils.getPersonClass("MyFunctionWithFilterMapAndFold2");
    assertTrue(Modifier.isPublic(classUT.getModifiers()),
            "Die Klasse MyFunctionWithFilterMapAndFold2 soll öffentlich sein");
    assertFalse(Modifier.isAbstract(classUT.getModifiers()),
            "Die Klasse MyFunctionWithFilterMapAndFold2 soll nicht abstrakt sein");
    var traitsObj = TestUtils.getTraitsObject(false);
    Object uut;
    var apply = TestUtils.getMethod(classUT, "apply",
            Array.newInstance(TestUtils.getPersonClass("Person"), 0).getClass());
    try {
      uut = classUT.getConstructor(TestUtils.getPersonClass("Traits")).newInstance(traitsObj);
      var actual = TestUtils.invokeMethod(apply, uut, TestUtils.filtered());
      assertEquals(381, actual);
    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
      fail("Der Konstruktor von MyFunctionWithFilterMapAndFold2 konnte nicht mit einem Traits Objekt aufgerufen werden");
    }
  }

  @Test
  public void functionCreatorExistsWithActive() {
    var classUT = TestUtils.getPersonClass("PersonFunctionCreator");
    try {
      var field = classUT.getDeclaredField("firstImplementationActive");
      assertTrue(Modifier.isPrivate(field.getModifiers()), "firstImplementationActive sollte private sein");
      assertTrue(Modifier.isStatic(field.getModifiers()), "firstImplementationActive sollte static sein");
      assertFalse(Modifier.isFinal(field.getModifiers()), "firstImplementationActive sollte nicht final sein");
    } catch (NoSuchFieldException e) {
      fail("Attribut firstImplementationActive von PersonFunctionCreator konnte nicht gefunden werden.");
    }
    try {
      var getter = classUT.getDeclaredMethod("isFirstImplementationActive");
      var setter = classUT.getDeclaredMethod("setFirstImplementationActive", boolean.class);
      assertTrue(Modifier.isPublic(getter.getModifiers()), "isFirstImplementationActive sollte public sein");
      assertTrue(Modifier.isStatic(getter.getModifiers()), "isFirstImplementationActive sollte static sein");
      assertTrue(Modifier.isPublic(setter.getModifiers()), "setFirstImplementationActive sollte public sein");
      assertTrue(Modifier.isStatic(setter.getModifiers()), "setFirstImplementationActive sollte static sein");
      setter.invoke(null, true);
      assertTrue((Boolean) getter.invoke(null), "Getter isFirstImplementationActive sollte true zurückgeben," +
              " nachdem setFirstImplementationActive(true) aufgerufen wurde.");
      setter.invoke(null, false);
      assertFalse((Boolean) getter.invoke(null), "Getter isFirstImplementationActive sollte false zurückgeben," +
              " nachdem setFirstImplementationActive(false) aufgerufen wurde.");
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
      fail("Getter oder Setter von firstImplementationActive konnte nicht erfolgreich verwendet werden.", e);
    }
  }

  @Test
  public void createFunctionWithFilterMapAndFoldCreatorCorrect() {
    var classUT = TestUtils.getPersonClass("PersonFunctionCreator");
    var traits = TestUtils.getPersonClass("Traits");
    try {
      var setter = classUT.getDeclaredMethod("setFirstImplementationActive", boolean.class);
      var create = classUT.getDeclaredMethod("createFunctionWithFilterMapAndFold", traits);
      var traitsObj = TestUtils.getTraitsObject(false);
      setter.invoke(null, true);
      var fct = create.invoke(null, traitsObj);
      assertEquals(fct.getClass(), TestUtils.getPersonClass("MyFunctionWithFilterMapAndFold1"));
      var field = fct.getClass().getSuperclass().getDeclaredField("traits");
      field.setAccessible(true);
      assertSame(traitsObj, field.get(fct));
      setter.invoke(null, false);
      var fct2 = create.invoke(null, traitsObj);
      assertEquals(fct2.getClass(), TestUtils.getPersonClass("MyFunctionWithFilterMapAndFold2"));
      var field2 = fct2.getClass().getSuperclass().getDeclaredField("traits");
      field2.setAccessible(true);
      assertSame(traitsObj, field2.get(fct2));
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
      fail("Beim Erstellen einer Funktion mit createFunctionWithFilterMapAndFoldCreator konnte nicht erfolgreich verwendet werden.", e);
    }
  }


  @Test
  public void combinedFctCorrect() {
    var classUT = TestUtils.getPersonClass("PersonFunctionCreator");
    var traits = TestUtils.getPersonClass("Traits");
    var personFilterClass = TestUtils.getPersonClass("PersonFilter");
    var personToIntFunctionClass = TestUtils.getPersonClass("PersonToIntFunction");
    var personClass = TestUtils.getPersonClass("Person");
    try {
      var createCombinedFct = classUT.getDeclaredMethod("combinedFct", String.class);
      var fct = createCombinedFct.invoke(null, "cake");
      var field = fct.getClass().getSuperclass().getDeclaredField("traits");
      field.setAccessible(true);
      var traitsObj = field.get(fct);
      assertEquals(357, TestUtils.get(traits, traitsObj, "init"), "combinedFcts Init sollte 357 sein");
      var personFilter = TestUtils.get(traits, traitsObj, "pred");
      var intOperator = (IntBinaryOperator) TestUtils.get(traits, traitsObj, "op");
      var personToIntFunction = TestUtils.get(traits, traitsObj, "fct");
      assertEquals(5, intOperator.applyAsInt(1, 3), "Op sollte  a + b + 1  entsprechen");
      assertEquals(-3, intOperator.applyAsInt(3, -7), "Op sollte  a + b + 1  entsprechen");
      var person1 = TestUtils.makePerson("cake", "vincent", "baker street", 2, 234);
      var person2 = TestUtils.makePerson("muffin", "alex", "baker street", 56, 120);
      assertTrue((Boolean) personFilterClass.getDeclaredMethod("test", personClass).invoke(personFilter, person1),
              "Pred sollte  p -> p.lastName.equals(<parameter>)  entsprechen");
      assertFalse((Boolean) personFilterClass.getDeclaredMethod("test", personClass).invoke(personFilter, person2),
              "Pred sollte  p -> p.lastName.equals(<parameter>)  entsprechen");
      assertEquals(234, personToIntFunctionClass.getDeclaredMethod("apply", personClass).invoke(personToIntFunction, person1),
              "Fct sollte  p -> p.postalCode  entsprechen");
      assertEquals(120, personToIntFunctionClass.getDeclaredMethod("apply", personClass).invoke(personToIntFunction, person2),
              "Fct sollte  p -> p.postalCode  entsprechen");
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
      fail("Beim Testen von combinedFct konnte eine notwendige Operation nicht durchgeführt werden", e);
    }
  }

  @Test
  public void myFunctionWithAdjacentExistsAndCombineInTraits() {
    Class<?> classUT = TestUtils.getPersonClass("MyFunctionWithAdjacent");
    Class<?> traits = TestUtils.getPersonClass("Traits");
    Class<?> fwfmf = TestUtils.getPersonClass("FunctionWithFilterMapAndFold");

    assertEquals(fwfmf, classUT.getSuperclass(),
            "Die Klasse MyFunctionWithAdjacent soll von FunctionWithFilterMapAndFold erben");
    assertTrue(Modifier.isPublic(classUT.getModifiers()),
            "Die Klasse MyFunctionWithAdjacent soll öffentlich sein");
    assertFalse(Modifier.isAbstract(classUT.getModifiers()),
            "Die Klasse MyFunctionWithAdjacent soll nicht abstrakt sein");
    var cons = Arrays.stream(classUT.getConstructors())
            .filter(c -> c.getParameterCount() == 1 && c.getParameters()[0].getType().equals(traits))
            .findAny().orElse(null);
    if (cons == null) {
      fail("Kein geeigneter Konstuktor von FunctionWithFilterMapAndFold konnte gefunden werden.");
    }
    assertTrue(Modifier.isPublic(cons.getModifiers()));

    var apply = TestUtils.getMethod(classUT, "apply", Person[].class);
    assertTrue(Modifier.isPublic(apply.getModifiers()));
    assertFalse(Modifier.isAbstract(apply.getModifiers()));
    assertEquals(int.class, apply.getReturnType());

    var consOpt = Arrays.stream(traits.getConstructors()).filter(c -> c.getParameterCount() >= 5).findAny();
    if (consOpt.isEmpty()) {
      fail("Traits hat keinen geeigneten Konstruktor mit 5 Parametern");
    }
    Object traitsObj;
    try {
      var traitsCons = consOpt.get();
      IntBinaryOperator op = (a, b) -> a + b + 1;
      var personToIntFunction = TestUtils.personToIntFunction();
      var personFilter = TestUtils.personFilter();
      IntBinaryOperator combine = (a, b) -> a * b / 79;
      traitsObj = traitsCons.newInstance(op, 357, personToIntFunction, personFilter, combine);
      assertEquals(op, TestUtils.get(traits, traitsObj, "op"), "Op-Getter falsch in Traits");
      assertEquals(personToIntFunction, TestUtils.get(traits, traitsObj, "fct"), "Fct-Getter falsch in Traits");
      assertEquals(personFilter, TestUtils.get(traits, traitsObj, "pred"), "Pred-Getter falsch in Traits");
      assertEquals(357, TestUtils.get(traits, traitsObj, "init"),"Init-Getter falsch in Traits");
      assertEquals(combine, TestUtils.get(traits, traitsObj, "combine"),"Combine-Getter falsch in Traits");
    } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
      fail("Traits-Objekt konnte nicht richtig erstellt oder verwendet werden", e);
    }
  }

  @Test
  public void myFunctionWithAdjacentCorrect() {
    var classUT = TestUtils.getPersonClass("MyFunctionWithAdjacent");
    assertTrue(Modifier.isPublic(classUT.getModifiers()),
            "Die Klasse MyFunctionWithAdjacent soll öffentlich sein");
    assertFalse(Modifier.isAbstract(classUT.getModifiers()),
            "Die Klasse MyFunctionWithAdjacent soll nicht abstrakt sein");
    var traitsObj = TestUtils.getTraitsObject(true);
    Object uut;
    var apply = TestUtils.getMethod(classUT, "apply",
            Array.newInstance(TestUtils.getPersonClass("Person"), 0).getClass());
    try {
      uut = classUT.getConstructor(TestUtils.getPersonClass("Traits")).newInstance(traitsObj);
      var actual = TestUtils.invokeMethod(apply, uut, TestUtils.people());
      assertEquals(371, actual);
    } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
      fail("Der Konstruktor von MyFunctionWithFilterMapAndFold1 konnte nicht mit einem Traits Objekt aufgerufen werden");
    }
  }

  @Test
  public void distanceCorrect() {
    var classUT = TestUtils.getPersonClass("PersonFunctionCreator");
    var fctClass = TestUtils.getPersonClass("MyFunctionWithAdjacent");
    var traits = TestUtils.getPersonClass("Traits");
    var personFilterClass = TestUtils.getPersonClass("PersonFilter");
    var personToIntFunctionClass = TestUtils.getPersonClass("PersonToIntFunction");
    var personClass = TestUtils.getPersonClass("Person");
    try {
      var createCombinedFct = classUT.getDeclaredMethod("distance");
      var fct = createCombinedFct.invoke(null);
      assertEquals(fctClass, fct.getClass());
      var field = fct.getClass().getSuperclass().getDeclaredField("traits");
      field.setAccessible(true);
      var traitsObj = field.get(fct);
      assertEquals(0, TestUtils.get(traits, traitsObj, "init"), "distance: Init sollte 357 sein");
      var personFilter = TestUtils.get(traits, traitsObj, "pred");
      var intOperator = (IntBinaryOperator) TestUtils.get(traits, traitsObj, "op");
      var combine = (IntBinaryOperator) TestUtils.get(traits, traitsObj, "combine");
      var personToIntFunction = TestUtils.get(traits, traitsObj, "fct");
      assertEquals(4, intOperator.applyAsInt(1, 3), "Op sollte  a + b  entsprechen");
      assertEquals(-4, intOperator.applyAsInt(3, -7), "Op sollte  a + b  entsprechen");
      var person1 = TestUtils.makePerson("cake", "vincent", "baker street", 2, 234);
      var person2 = TestUtils.makePerson("muffin", "alex", "baker street", 56, 64289);
      assertTrue((Boolean) personFilterClass.getDeclaredMethod("test", personClass).invoke(personFilter, person1),
        "Pred sollte  p -> p.postalCode != 64289  entsprechen");
      assertFalse((Boolean) personFilterClass.getDeclaredMethod("test", personClass).invoke(personFilter, person2),
        "Pred sollte  p -> p.postalCode != 64289  entsprechen");
      assertEquals(234, personToIntFunctionClass.getDeclaredMethod("apply", personClass).invoke(personToIntFunction, person1),
        "Fct sollte  p -> p.postalCode  entsprechen");
      assertEquals(64289, personToIntFunctionClass.getDeclaredMethod("apply", personClass).invoke(personToIntFunction, person2),
        "Fct sollte  p -> p.postalCode  entsprechen");
      assertEquals(20, combine.applyAsInt(20, 40),
        "Combine sollte  (a, b) -> Math.abs(a - b)  entsprechen");
      assertEquals(45, combine.applyAsInt(34, -11),
        "Combine sollte  (a, b) -> Math.abs(a - b)  entsprechen");
    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
      fail("Beim Testen von combinedFct konnte eine notwendige Operation nicht durchgeführt werden", e);
    }
  }
}
