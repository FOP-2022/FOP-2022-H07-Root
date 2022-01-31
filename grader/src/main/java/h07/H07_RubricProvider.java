package h07;

import org.sourcegrade.jagr.api.rubric.Criterion;
import org.sourcegrade.jagr.api.rubric.Grader;
import org.sourcegrade.jagr.api.rubric.JUnitTestRef;
import org.sourcegrade.jagr.api.rubric.Rubric;
import org.sourcegrade.jagr.api.rubric.RubricForSubmission;
import org.sourcegrade.jagr.api.rubric.RubricProvider;
import org.sourcegrade.jagr.api.testing.RubricConfiguration;
import org.sourcegrade.jagr.api.testing.TestCycle;

/**
 * The rubric provider for h07, see {@link RubricProvider}.
 */
@RubricForSubmission("h07")
public class H07_RubricProvider implements RubricProvider {
    /*---------------------------------- H1 ----------------------------------*/
    public static final Criterion epsEnvPredExists = Criterion.builder()
        .shortDescription("Die Klasse EpsilonEnvironmentPred existiert und implementiert DoublePredicate")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() ->
                TutorTest_H1.class.getMethod("epsilonEnvironmentPredExists")))
            .pointsPassedMax()
            .pointsFailedMin()
            .build()
        ).build();

    public static final Criterion epsEnvPredMostlyCorrect = Criterion.builder()
        .shortDescription("test(double) von Klasse EpsilonEnvironmentPred ist meist korrekt")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() ->
                TutorTest_H1.class.getMethod("epsilonEnvironmentPredMostlyCorrect")))
            .pointsPassedMax()
            .pointsFailedMin()
            .build()
        ).build();

    public static final Criterion epsEnvPredIsCorrect = Criterion.builder()
        .shortDescription("Die Klasse EpsilonEnvironmentPred ist korrekt")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() ->
                TutorTest_H1.class.getMethod("epsilonEnvironmentPredIsCorrect")))
            .pointsPassedMax()
            .pointsFailedMin()
            .build()
        ).build();

    public static final Criterion buildConjunctionExists = Criterion.builder()
        .shortDescription("Die Klasse DoublePredicateFactory beinhaltet die rekursive Methode buildConjunction")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() ->
                TutorTest_H1.class.getMethod("buildDisjunctionExists")))
            .requirePass(JUnitTestRef.ofMethod(() ->
                TutorTest_H1.class.getMethod("doublePredicateFactoryExist")))
            .pointsPassedMax()
            .pointsFailedMin()
            .build()
        ).build();

    public static final Criterion buildConjunctionWorks = Criterion.builder()
        .shortDescription("BuildConjunction funktioniert korrekt")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() ->
                TutorTest_H1.class.getMethod("buildConjunctionWorks")))
            .pointsPassedMax()
            .pointsFailedMin()
            .build()
        ).build();

    public static final Criterion buildDisjunctionExists = Criterion.builder()
        .shortDescription("Die Klasse DoublePredicateFactory beinhaltet die iterative Methode buildConjunction")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() ->
                TutorTest_H1.class.getMethod("buildDisjunctionExists")))
            .requirePass(JUnitTestRef.ofMethod(() ->
                TutorTest_H1.class.getMethod("doublePredicateFactoryExist")))
            .pointsPassedMax()
            .pointsFailedMin()
            .build()
        ).build();

    public static final Criterion buildDisjunctionWorks = Criterion.builder()
        .shortDescription("BuildDisjunction funktioniert korrekt")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() ->
                TutorTest_H1.class.getMethod("buildDisjunctionWorks")))
            .pointsPassedMax()
            .pointsFailedMin()
            .build()
        ).build();

    public static final Criterion buildComplexPredicateWorksSimple = Criterion.builder()
        .shortDescription("Die Methode buildComplexPredicate funktioniert für ein ein-elementiges [[Predicate]]-Array")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() ->
                TutorTest_H1.class.getMethod("complexDoublePredicateWorksSimple")))
            .pointsPassedMax()
            .pointsFailedMin()
            .build()
        ).build();

    public static final Criterion buildComplexPredicateWorksAll = Criterion.builder()
        .shortDescription("Die Methode buildComplexPredicate ist korrekt")
        .maxPoints(2)
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() ->
                TutorTest_H1.class.getMethod("complexDoublePredicateWorksAll")))
            .pointsPassedMax()
            .pointsFailedMin()
            .build()
        ).build();

    public static final Criterion getDefaultComplexPredicateExists = Criterion.builder()
        .shortDescription("Die Methode getDefaultComplexPredicate existiert")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() ->
                TutorTest_H1.class.getMethod("getDefaultComplexPredicateExists")))
            .pointsPassedMax()
            .pointsFailedMin()
            .build()
        ).build();

    public static final Criterion getDefaultComplexPredicateStructure = Criterion.builder()
        .shortDescription("Die Methode getDefaultComplexPredicate verwendet die geforderte Struktur mit Lambdas und EpsEnvPredicates")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() ->
                TutorTest_H1.class.getMethod("lambdaInCorrectForm", TestCycle.class)))
            .pointsPassedMax()
            .pointsFailedMin()
            .build()
        ).build();

    public static final Criterion getDefaultComplexPredicateWorksMostOfTheTime = Criterion.builder()
        .shortDescription("Die Methode getDefaultComplexPredicate funktioniert mit vielen Eingaben")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() ->
                TutorTest_H1.class.getMethod("getDefaultComplexPredicateWorksMostOfTheTime")))
            .pointsPassedMax()
            .pointsFailedMin()
            .build()
        ).build();

    public static final Criterion getDefaultComplexPredicateWorks = Criterion.builder()
        .shortDescription("Die Methode getDefaultComplexPredicate ist korrekt")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() ->
                TutorTest_H1.class.getMethod("getDefaultComplexPredicateWorks")))
            .pointsPassedMax()
            .pointsFailedMin()
            .build()
        ).build();

    public static final Criterion getChecksumPredicateExists = Criterion.builder()
        .shortDescription("Die Methode getChecksumPredicate existiert")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() ->
                TutorTest_H1.class.getMethod("checksumPredExists")))
            .pointsPassedMax()
            .pointsFailedMin()
            .build()
        ).build();

    public static final Criterion getChecksumPredicateWorksMostOfTheTime = Criterion.builder()
        .shortDescription("Die Methode getChecksumPredicate funktioniert mit vielen Eingaben")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() ->
                TutorTest_H1.class.getMethod("checksumPredWorksMostly")))
            .pointsPassedMax()
            .pointsFailedMin()
            .build()
        ).build();

    public static final Criterion getChecksumPredicateWorks = Criterion.builder()
        .shortDescription("Die Methode getChecksumPredicate ist korrekt")
        .maxPoints(1)
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() ->
                TutorTest_H1.class.getMethod("checksumPredIsFullyCorrect")))
            .pointsPassedMax()
            .pointsFailedMin()
            .build()
        ).build();

    /*---------------------------------- H2 -----------------------------------*/
    public static final Criterion personExist = Criterion.builder()
        .shortDescription("Die Klasse Person existiert")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() ->
                TutorTest_H2.class.getMethod("personExist")))
            .pointsPassedMax()
            .pointsFailedMin()
            .build()
        ).build();

    public static final Criterion personFilterExist = Criterion.builder()
        .shortDescription("Die Klasse PersonFilter existiert")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() ->
                TutorTest_H2.class.getMethod("personFilterExist")))
            .pointsPassedMax()
            .pointsFailedMin()
            .build()
        ).build();

    public static final Criterion personToIntFunctionExist = Criterion.builder()
        .shortDescription("Die Klasse PersonToIntFunction existiert")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() ->
                TutorTest_H2.class.getMethod("personToIntFunctionExist")))
            .pointsPassedMax()
            .pointsFailedMin()
            .build()
        ).build();

    public static final Criterion traitsExist = Criterion.builder()
        .shortDescription("Die Klasse Traits existiert")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() ->
                TutorTest_H2.class.getMethod("traitsExist")))
            .pointsPassedMax()
            .pointsFailedMin()
            .build()
        ).build();

    public static final Criterion traitsCorrect = Criterion.builder()
        .shortDescription("Die Klasse Traits ist korrekt")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() ->
                TutorTest_H2.class.getMethod("traitsCorrect")))
            .pointsPassedMax()
            .pointsFailedMin()
            .build()
        ).build();

    public static final Criterion functionWithFilterMapAndFoldExists = Criterion.builder()
        .shortDescription("Die Klassen FunctionWithFilterMapAndFold sowie die beiden Implmentation existieren.")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() ->
                TutorTest_H2.class.getMethod("functionWithFilterMapAndFoldExists")))
            .pointsPassedMax()
            .pointsFailedMin()
            .build()
        ).build();

    public static final Criterion myFunctionWithFilterMapAndFold1Partly = Criterion.builder()
        .shortDescription("Von den Bestandteilen von myFunctionWithFilterMapAndFold1 sind mind. 2/3 korrekt")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() ->
                TutorTest_H2.class.getMethod("myFunctionWithFilterMapAndFold1Partly")))
            .pointsPassedMax()
            .pointsFailedMin()
            .build()
        ).build();

    public static final Criterion myFunctionWithFilterMapAndFold1All = Criterion.builder()
        .shortDescription("Die Klasse myFunctionWithFilterMapAndFold1 ist korrekt")
        .maxPoints(2)
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() ->
                TutorTest_H2.class.getMethod("myFunctionWithFilterMapAndFold1All")))
            .pointsPassedMax()
            .pointsFailedMin()
            .build()
        ).build();

    public static final Criterion myFunctionWithFilterMapAndFoldCorrect2 = Criterion.builder()
        .shortDescription("Die Klasse FunctionWithFilterMapAndFold2Correct ist korrekt")
        .maxPoints(2)
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() ->
                TutorTest_H2.class.getMethod("myFunctionWithFilterMapAndFoldCorrect2")))
            .pointsPassedMax()
            .pointsFailedMin()
            .build()
        ).build();

    public static final Criterion functionCreatorExistsWithActive = Criterion.builder()
        .shortDescription("Die Klasse PersonFunctionFactory existiert und firstImplementationActive ist dort vorhanden")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() ->
                TutorTest_H2.class.getMethod("functionCreatorExistsWithActive")))
            .pointsPassedMax()
            .pointsFailedMin()
            .build()
        ).build();

    public static final Criterion createFunctionWithFilterMapAndFoldCorrect = Criterion.builder()
        .shortDescription("Die Methode createFunctionWithFilterMapAndFold ist korrekt")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() ->
                TutorTest_H2.class.getMethod("createFunctionWithFilterMapAndFoldCreatorCorrect")))
            .pointsPassedMax()
            .pointsFailedMin()
            .build()
        ).build();

    public static final Criterion createStrangeFunctionCorrect = Criterion.builder()
        .shortDescription("Die Funktion createStrangeFunction ist korrekt implementiert.")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() ->
                TutorTest_H2.class.getMethod("createStrangeFunctionCorrect")))
            .pointsPassedMax()
            .pointsFailedMin()
            .build()
        ).build();

    public static final Criterion myFunctionWithAdjacentExistsAndCombineInTraits = Criterion.builder()
        .shortDescription("Die Klasse MyFunctionWithAdjacent, sowie der zusätzliche Parameter combine von Traits, existieren")
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() ->
                TutorTest_H2.class.getMethod("myFunctionWithAdjacentExistsAndCombineInTraits")))
            .pointsPassedMax()
            .pointsFailedMin()
            .build()
        ).build();

    public static final Criterion myFunctionWithAdjacentCorrect = Criterion.builder()
        .shortDescription("Die Klasse MyFunctionWithAdjacent ist korrekt")
        .maxPoints(2)
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() ->
                TutorTest_H2.class.getMethod("myFunctionWithAdjacentCorrect")))
            .pointsPassedMax()
            .pointsFailedMin()
            .build()
        ).build();

    public static final Criterion distanceFctCorrect = Criterion.builder()
        .shortDescription("Die distance-Methode von PersonFunctionFactory ist korrekt")
        .maxPoints(2)
        .grader(Grader.testAwareBuilder()
            .requirePass(JUnitTestRef.ofMethod(() ->
                TutorTest_H2.class.getMethod("distanceCorrect")))
            .pointsPassedMax()
            .pointsFailedMin()
            .build()
        ).build();

    public static final Criterion H1_1 = Criterion.builder()
        .shortDescription("H1.1 - EpsilonEnvironmentPred")
        .addChildCriteria(
            epsEnvPredExists,
            epsEnvPredMostlyCorrect,
            epsEnvPredIsCorrect)
        .build();

    public static final Criterion H1_2 = Criterion.builder()
        .shortDescription("H1.2 - buildDisjunction")
        .addChildCriteria(
            buildDisjunctionExists,
            buildDisjunctionWorks
        ).build();

    public static final Criterion H1_3 = Criterion.builder()
        .shortDescription("H1.3 - buildConjunction")
        .addChildCriteria(
            buildConjunctionExists,
            buildConjunctionWorks
        ).build();

    public static final Criterion H1_4 = Criterion.builder()
        .shortDescription("H1.4 - buildComplexPredicate")
        .addChildCriteria(
            buildComplexPredicateWorksSimple,
            buildComplexPredicateWorksAll
        ).build();

    public static final Criterion H1_5 = Criterion.builder()
        .shortDescription("H1.5 - buildComplexPredicate")
        .addChildCriteria(
            getDefaultComplexPredicateExists,
            getDefaultComplexPredicateStructure,
            getDefaultComplexPredicateWorksMostOfTheTime,
            getDefaultComplexPredicateWorks)
        .build();

    public static final Criterion H1_6 = Criterion.builder()
        .shortDescription("H1.6 - getChecksumPredicate")
        .addChildCriteria(
            getChecksumPredicateExists,
            getChecksumPredicateWorksMostOfTheTime,
            getChecksumPredicateWorks)
        .build();

    public static final Criterion H1 = Criterion.builder()
        .shortDescription("H1 - Predicates")
        .addChildCriteria(
            H1_1,
            H1_2,
            H1_3,
            H1_4,
            H1_5,
            H1_6
        )
        .build();

    public static final Criterion H2_1 = Criterion.builder()
        .shortDescription("H2.1-H2.3 - Filtern, Abbilden, Falten")
        .addChildCriteria(
            personExist,
            personFilterExist,
            personToIntFunctionExist,
            traitsExist,
            traitsCorrect,
            functionWithFilterMapAndFoldExists,
            myFunctionWithFilterMapAndFold1Partly,
            myFunctionWithFilterMapAndFold1All,
            myFunctionWithFilterMapAndFoldCorrect2)
        .build();

    public static final Criterion H2_2 = Criterion.builder()
        .shortDescription("H2.4-H2.5 - PersonFunctionFactory")
        .addChildCriteria(
            functionCreatorExistsWithActive,
            createFunctionWithFilterMapAndFoldCorrect,
            createStrangeFunctionCorrect)
        .build();

    public static final Criterion H2_3 = Criterion.builder()
        .shortDescription("H2.6-H2.7 - Verarbeitung angrenzender Einträge")
        .addChildCriteria(
            myFunctionWithAdjacentExistsAndCombineInTraits,
            myFunctionWithAdjacentCorrect,
            distanceFctCorrect)
        .build();

    public static final Criterion H2 = Criterion.builder()
        .shortDescription("H2 - Person Verarbeitung mit Filter, Map and Foldl")
        .addChildCriteria(
            H2_1,
            H2_2,
            H2_3
        )
        .build();

    public static final Criterion JAVADOC = Criterion.builder()
        .shortDescription("Alle selbstgeschriebenen Methoden wurden korrekt mit JavaDoc dokumentiert")
        .maxPoints(0)
        .minPoints(-4)
        .build();

    public static final Rubric RUBRIC = Rubric.builder()
        .title("h07")
        .addChildCriteria(H1, H2, JAVADOC)
        .build();

    @Override
    public Rubric getRubric() {
        return RUBRIC;
    }

    @Override
    public void configure(RubricConfiguration configuration) {
        configuration.addTransformer(Global.TRANSFORMER);
    }
}
