package h07;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.sourcegrade.jagr.api.rubric.TestForSubmission;
import student.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.IntBinaryOperator;

import static h07.TestUtils.personFilter;
import static org.junit.jupiter.api.Assertions.*;
import static student.FunctionWithFilterMapAndFold.Student.aTraits;
import static student.MyFunctionWithFilterMapAndFold1.Student.*;
import static student.PersonFilter.Student.mTest;
import static student.PersonFunctionFactory.Student.*;
import static student.PersonToIntFunction.Student.mApply;
import static student.Person_STUD.*;
import static student.Traits.Student.*;
import static tutor.Utils.TestCollection.test;

/**
 * The JUnit tests for H2.
 */
@TestForSubmission("h07")
public class TutorTest_H2 {

    @BeforeEach
    public void beforeEach() {
        Mockito.reset();
    }

    // Check if class Person exists.
    @Test
    public void t01() {
        test()
            .addReq(cPerson()::assureResolved)
            .add(cPerson()::checkDeclaration)
            .add(mConstructor()::checkDeclaration)
            .add(aLastName()::checkDeclaration)
            .add(aFirstName()::checkDeclaration)
            .add(aStreet()::checkDeclaration)
            .add(aHouseNumber()::checkDeclaration)
            .add(aPostalCode()::checkDeclaration)
            .run();
    }

    @Test
    public void personFilterExist() {
        // TODO check annotation
        test()
            .addReq(PersonFilter.Student.c()::assureResolved)
            .addReq(mTest()::assureResolved)
            .add(mTest()::checkDeclaration)
            .run();
    }

    @Test
    public void personToIntFunctionExist() {
        // TODO check annotation
        test()
            .addReq(PersonToIntFunction.Student.c()::assureResolved)
            .addReq(mApply()::assureResolved)
            .add(mApply()::checkDeclaration)
            .run();
    }

    @Test
    public void traitsExist() {
        test()
            .addReq(Traits.Student.c()::assureResolved)
            .run();
    }

    @Test
    public void traitsCorrect() {
        // TODO check superclass
        test()
            .addReq(Traits.Student.c()::assureResolved)
            .add(Traits.Student.c()::checkDeclaration)
            .add(aOp()::checkDeclaration)
            .add(aInit()::checkDeclaration)
            .add(aFct()::checkDeclaration)
            .add(aPred()::checkDeclaration)
            .run();
        // no checks for methods and constructors are intended
    }

    @Test
    public void functionWithFilterMapAndFoldExists() {
        test()
            .addReq(FunctionWithFilterMapAndFold.Student.c()::assureResolved)
            .add(FunctionWithFilterMapAndFold.Student.c()::checkDeclaration)
            .add(FunctionWithFilterMapAndFold.Student.mApply()::checkDeclaration)
            .run();
    }

    @Test
    public void myFunctionWithFilterMapAndFold1Exists() {
        test()
            .addReq(MyFunctionWithFilterMapAndFold1.Student.c()::assureResolved)
            .add(MyFunctionWithFilterMapAndFold1.Student.c()::checkDeclaration)
            .run();
    }

    @Test
    public void myFunctionWithFilterMapAndFold2Exists() {
        test()
            .addReq(MyFunctionWithFilterMapAndFold2.Student.c()::assureResolved)
            .add(MyFunctionWithFilterMapAndFold2.Student.c()::checkDeclaration)
            .run();
    }

    @Test
    public void myFunctionWithFilterMapAndFold1Partly() {
        determineWrongFilterMapFold();
    }

    private void determineWrongFilterMapFold() {
        test()
            .add(MyFunctionWithFilterMapAndFold1.Student.c()::checkDeclaration)
            .add(() -> {
                var filter = mFilter();
                var personFilter = personFilter();
                var actual = TestUtils.invokeHigherOrderMethod(filter,
                    TestUtils.makeTraits(Map.of(PersonFilter.class, new ArrayList<>(List.of(personFilter)))), personFilter, TestUtils.people());
                TestUtils.assertPeopleEquals((Object[]) TestUtils.filtered(), (Object[]) actual);
            })
            .add(() -> {
                var map = mMap();
                var personToIntFunction = TestUtils.personToIntFunction();
                var actual = TestUtils.invokeHigherOrderMethod(map,
                    TestUtils.makeTraits(Map.of(PersonToIntFunction.class, new ArrayList<>(List.of(personToIntFunction)))), personToIntFunction, TestUtils.people());
                assertArrayEquals(TestUtils.mapped, (int[]) actual);
            })
            .add(() -> {
                var foldl = mFoldl();
                List<IntBinaryOperator> functions = List.of(Integer::sum, Integer::sum, Math::max);
                List<Integer> inits = List.of(0, 5, 5);
                List<Integer> expected = List.of(128613, 128618, 64289);
                TestUtils.forEach(functions, inits, expected, (f, init, result) -> {
                    var actual = TestUtils.invokeHigherOrderMethod(foldl,
                        TestUtils.makeTraits(Map.of(IntBinaryOperator.class, new ArrayList<>(List.of(f)), int.class, new ArrayList<>(List.of(init)))), f, init, TestUtils.mapped);
                    assertEquals(result, actual,
                        String.format("Für Eingabe %s, Init %d und %s folgt bei foldl nicht der erwartete Wert %d sondern %s .",
                            Arrays.toString(TestUtils.mapped),
                            init,
                            result < 0 ? "Funktion: Math::max" : "Addition",
                            result,
                            actual
                        )
                    );
                });
            })
            .run();
    }

    @Test
    public void myFunctionWithFilterMapAndFold1All() {
        test()
            .add(MyFunctionWithFilterMapAndFold1.Student.c()::assureResolved)
            .terminateOnFailure()
            .add(MyFunctionWithFilterMapAndFold1.Student.c()::checkDeclaration)
            .add(() -> {
                var apply = mApply();
                var traitsObj = TestUtils.getTraitsObject(false);
                var uut = MyFunctionWithFilterMapAndFold1.Student.mMyFunctionWithFilterMapAndFold1().invokeStatic(traitsObj);
                var actual = MyFunctionWithFilterMapAndFold1.Student.mApply().invoke(uut, TestUtils.filtered());
                assertEquals(381, actual);
            })
            .run();
    }

    @Test
    public void myFunctionWithFilterMapAndFoldCorrect2() {
        test()
            .add(MyFunctionWithFilterMapAndFold2.Student.c()::assureResolved)
            .terminateOnFailure()
            .add(MyFunctionWithFilterMapAndFold2.Student.c()::checkDeclaration)
            .add(() -> {
                var traitsObj = TestUtils.getTraitsObject(false);
                var uut = MyFunctionWithFilterMapAndFold2.Student.mMyFunctionWithFilterMapAndFold2().invokeStatic(traitsObj);
                var actual = MyFunctionWithFilterMapAndFold2.Student.mApply().invoke(uut, TestUtils.filtered());

                assertEquals(381, actual);
            })
            .run();
    }

    @Test
    public void functionCreatorExistsWithActive() {

        test()
            .add(PersonFunctionFactory.Student.c()::assureResolved)
            .terminateOnFailure()
            .add(PersonFunctionFactory.Student.c()::checkDeclaration)
            .add(aFirstImplementationActive()::checkDeclaration)
            .add(mSetFirstImplementationActive()::checkDeclaration)
            .add(mIsFirstImplementationActive()::checkDeclaration)
            .terminateOnFailure()
            .add(() -> {
                var object = PersonFunctionFactory.Student.c().instantiate();
                mSetFirstImplementationActive().invoke(object, false);
                var value = (boolean) mIsFirstImplementationActive().invokeIns();
                assertFalse(value, "<code>isFirstImplementationActive</code> soll <code>false</code> liefern,"
                    + " nachdem <code>setFirstImplementationActive(false)</code> aufgerufen wurde.");
            })
            .add(() -> {
                var object = PersonFunctionFactory.Student.c().instantiate();
                mSetFirstImplementationActive().invoke(object, true);
                var value = (boolean) mIsFirstImplementationActive().invokeIns();
                assertTrue(value, "<code>isFirstImplementationActive</code> soll <code>true</code> liefern,"
                    + " nachdem <code>setFirstImplementationActive(true)</code> aufgerufen wurde.");
            })
            .run();
    }

    @Test
    public void createFunctionWithFilterMapAndFoldCreatorCorrect() {
        var traitsObj = TestUtils.getTraitsObject(false);
        mSetFirstImplementationActive().invoke(PersonFunctionFactory.Student.c().instantiate(), true);
        var fct = mCreateFunctionWithFilterMapAndFold().invoke(mCreateFunctionWithFilterMapAndFold().getClassTester().instantiate(), traitsObj.getActualObject());
        assertSame(traitsObj.getActualObject(), aTraits().getValue(fct),
            "Traits vom MyFunctionWithFilterMapAndFold1-Objekt ist nicht das Gleiche wie das im Konstruktor übergebene.");
        mSetFirstImplementationActive().invoke(PersonFunctionFactory.Student.c().instantiate(), false);
        var fct2 = mCreateFunctionWithFilterMapAndFold().invoke(mCreateFunctionWithFilterMapAndFold().getClassTester().instantiate(), traitsObj);
        assertSame(traitsObj.getActualObject(), aTraits().getValue(fct),
            "Traits vom MyFunctionWithFilterMapAndFold2-Objekt ist nicht das Gleiche wie das im Konstruktor übergebene.");
    }

    @Test
    public void createStrangeFunctionCorrect() {
        var fct = mCreateStrangeFunction().invoke(mCreateStrangeFunction().getClassTester().instantiate(), "cake");
        var traitsObj = new Traits.Mock(aTraits().getValue(fct));
        assertEquals(357, traitsObj.getInit(), "createStrangeFunctions Init sollte 357 sein");
        var personFilter = traitsObj.getPred();
        var intOperator = traitsObj.getOp();
        var personToIntFunction = traitsObj.getFct();
        assertEquals(5, intOperator.applyAsInt(1, 3), "Op sollte  a + b + 1  entsprechen");
        assertEquals(-3, intOperator.applyAsInt(3, -7), "Op sollte  a + b + 1  entsprechen");
        var person1 = TestUtils.makePerson("cake", "vincent", "baker street", 2, 234);
        var person2 = TestUtils.makePerson("muffin", "alex", "baker street", 56, 120);
        assertTrue((Boolean) PersonFilter.Student.mTest().invoke(personFilter.getActualObject(), person1),
            "Pred sollte  p -> p.lastName.equals(<parameter>)  entsprechen");
        assertFalse((Boolean) PersonFilter.Student.mTest().invoke(personFilter.getActualObject(), person2),
            "Pred sollte  p -> p.lastName.equals(<parameter>)  entsprechen");
        assertEquals(234,
            (int) PersonToIntFunction.Student.mApply().invoke(personToIntFunction.getActualObject(), person1),
            "Fct sollte  p -> p.postalCode  entsprechen");
        assertEquals(120,
            (int) PersonToIntFunction.Student.mApply().invoke(personToIntFunction.getActualObject(), person2),
            "Fct sollte  p -> p.postalCode  entsprechen");
    }

    @Test
    public void myFunctionWithAdjacentExistsAndCombineInTraits() {
        test()
            .add(MyFunctionWithAdjacent.Student.c()::checkDeclaration)
            .add(FunctionWithFilterMapAndFold.Student.mApply().forClass(FunctionWithFilterMapAndFold.Student.c())::checkDeclaration)
            .run();
        IntBinaryOperator op = (a, b) -> a + b + 1;
        var personToIntFunction = TestUtils.personToIntFunction();
        var personFilter = personFilter();
        IntBinaryOperator combine = (a, b) -> a * b / 79;
        var traitsObj = new Traits.Mock(op, 357, personToIntFunction, personFilter, combine);
        assertEquals(op, traitsObj.getOp(), "Op-Getter falsch in Traits");
        assertEquals(personToIntFunction.getActualObject(), traitsObj.getFct().getActualObject(), "Fct-Getter falsch in Traits");
        assertEquals(personFilter.getActualObject(), traitsObj.getPred().getActualObject(), "Pred-Getter falsch in Traits");
        assertEquals(357, traitsObj.getInit(), "Init-Getter falsch in Traits");
        assertEquals(combine, traitsObj.getCombine(), "Combine-Getter falsch in Traits");
    }

    @Test
    public void myFunctionWithAdjacentCorrect() {


        test()
            .add(FunctionWithFilterMapAndFold.Student.c()::assureResolved)
            .terminateOnFailure()
            .add(MyFunctionWithAdjacent.Student.c()::checkDeclaration)
            .terminateOnFailure()
            .add(MyFunctionWithAdjacent.Student.mConstructor()::checkDeclaration)
            .run();

        var traitsObj = TestUtils.getTraitsObject(true);
        var mApply = FunctionWithFilterMapAndFold.Student.mApply().forClass(MyFunctionWithAdjacent.Student.c());
        var uut = MyFunctionWithAdjacent.Student.mConstructor().invokeStatic(traitsObj.getActualObject());
        var actual = (Integer) mApply.invoke(uut, TestUtils.people());
        assertEquals(371, actual);

    }

    @Test
    public void distanceCorrect() {
        var fct = mDistance().invokeIns();
        var traitsObj = new Traits.Mock(aTraits().getValue(fct));
        assertEquals(0, traitsObj.getInit(), "distance: Init sollte 0 sein"); // 357 or 0 ?
        final var personFilter = traitsObj.getPred();
        final var intOperator = traitsObj.getOp();
        final var combine = traitsObj.getCombine();
        final var personToIntFunction = traitsObj.getFct();
        assertEquals(4, intOperator.applyAsInt(1, 3), "Op sollte  a + b  entsprechen");
        assertEquals(-4, intOperator.applyAsInt(3, -7), "Op sollte  a + b  entsprechen");
        final var person1 = TestUtils.makePerson("cake", "vincent", "baker street", 2, 234);
        final var person2 = TestUtils.makePerson("muffin", "alex", "baker street", 56, 64289);

        assertTrue(personFilter.test(new Person_STUD(person1)),
            "Pred sollte  p -> p.postalCode != 64289  entsprechen");
        assertFalse(personFilter.test(new Person_STUD(person2)),
            "Pred sollte  p -> p.postalCode != 64289  entsprechen");
        assertEquals(234,
            personToIntFunction.apply(new Person_STUD(person1)),
            "Fct sollte  p -> p.postalCode  entsprechen");
        assertEquals(64289,
            personToIntFunction.apply(new Person_STUD(person2)),
            "Fct sollte  p -> p.postalCode  entsprechen");
        assertEquals(20,
            combine.applyAsInt(20, 40),
            "Combine sollte  (a, b) -> Math.abs(a - b)  entsprechen");
        assertEquals(45,
            combine.applyAsInt(34, -11),
            "Combine sollte  (a, b) -> Math.abs(a - b)  entsprechen");
    }
}
