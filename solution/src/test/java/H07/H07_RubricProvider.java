package H07;

import org.jagrkt.api.rubric.*;

@RubricForSubmission("H07")
public class H07_RubricProvider implements RubricProvider {
    /*---------------------------------- H1 ----------------------------------*/
    public static final Criterion epsEnvPredExists = Criterion.builder()
            .shortDescription("Das Interface EpsilonEnvironmentPred existiert und implementiert DoublePredicate")
            .grader(Grader.testAwareBuilder()
                    .requirePass(JUnitTestRef.ofMethod(() ->
                            TutorTest_H1.class.getMethod("epsilonEnvironmentPredExists")))
                    .pointsPassedMax()
                    .pointsFailedMin()
                    .build()
            ).build();

    public static final Criterion epsEnvPredIsCorrect = Criterion.builder()
            .shortDescription("Das Interface EpsilonEnvironmentPred ist korrekt")
            .grader(Grader.testAwareBuilder()
                    .requirePass(JUnitTestRef.ofMethod(() ->
                            TutorTest_H1.class.getMethod("epsilonEnvironmentPredIsCorrect")))
                    .pointsPassedMax()
                    .pointsFailedMin()
                    .build()
            ).build();

    public static final Criterion complexDoublePredicateCreatorExist = Criterion.builder()
            .shortDescription("Die Klasse ComplexDoublePredicateCreatorExist existiert")
            .grader(Grader.testAwareBuilder()
                    .requirePass(JUnitTestRef.ofMethod(() ->
                            TutorTest_H1.class.getMethod("complexDoublePredicateCreatorExist")))
                    .pointsPassedMax()
                    .pointsFailedMin()
                    .build()
            ).build();

    public static final Criterion makeComplexPredicateExists = Criterion.builder()
            .shortDescription("Die Klasse ComplexDoublePredicateExist hat die Methode makeComplexPredicate")
            .grader(Grader.testAwareBuilder()
                    .requirePass(JUnitTestRef.ofMethod(() ->
                            TutorTest_H1.class.getMethod("makeComplexPredicateExists")))
                    .pointsPassedMax()
                    .pointsFailedMin()
                    .build()
            ).build();

    public static final Criterion makeComplexPredicateWorksSimple = Criterion.builder()
            .shortDescription("Die Methode makeComplexPredicate funktioniert für ein ein-elementiges [[Predicate]]-Array")
            .grader(Grader.testAwareBuilder()
                    .requirePass(JUnitTestRef.ofMethod(() ->
                            TutorTest_H1.class.getMethod("complexDoublePredicateWorksSimple")))
                    .pointsPassedMax()
                    .pointsFailedMin()
                    .build()
            ).build();

    public static final Criterion makeComplexPredicateWorksAll = Criterion.builder()
            .shortDescription("Die Methode makeComplexPredicate ist korrekt")
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

    public static final Criterion H1 = Criterion.builder()
            .shortDescription("H1 - Predicates")
            .addChildCriteria(
                    epsEnvPredExists,
                    epsEnvPredIsCorrect,
                    complexDoublePredicateCreatorExist,
                    makeComplexPredicateExists,
                    makeComplexPredicateWorksSimple,
                    makeComplexPredicateWorksAll,
                    getDefaultComplexPredicateExists,
                    getDefaultComplexPredicateWorksMostOfTheTime,
                    getDefaultComplexPredicateWorks)
            .build();

    /*---------------------------------- H2 ----------------------------------*/
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
            .grader(Grader.testAwareBuilder()
                    .requirePass(JUnitTestRef.ofMethod(() ->
                            TutorTest_H2.class.getMethod("myFunctionWithFilterMapAndFold1All")))
                    .pointsPassedMax()
                    .pointsFailedMin()
                    .build()
            ).build();

    public static final Criterion myFunctionWithFilterMapAndFoldCorrect2 = Criterion.builder()
            .shortDescription("Die Klasse FunctionWithFilterMapAndFold2Correct ist korrekt")
            .grader(Grader.testAwareBuilder()
                    .requirePass(JUnitTestRef.ofMethod(() ->
                            TutorTest_H2.class.getMethod("myFunctionWithFilterMapAndFoldCorrect2")))
                    .pointsPassedMax()
                    .pointsFailedMin()
                    .build()
            ).build();

    public static final Criterion functionCreatorExistsWithActive = Criterion.builder()
            .shortDescription("Die Klasse PersonFunctionCreator existiert und firstImplementationActive ist dort vorhanden")
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

    public static final Criterion combinedFctCorrect = Criterion.builder()
            .shortDescription("Die Funktion combinedFct ist korrekt implementiert.")
            .grader(Grader.testAwareBuilder()
                    .requirePass(JUnitTestRef.ofMethod(() ->
                            TutorTest_H2.class.getMethod("combinedFctCorrect")))
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
            .grader(Grader.testAwareBuilder()
                    .requirePass(JUnitTestRef.ofMethod(() ->
                            TutorTest_H2.class.getMethod("myFunctionWithAdjacentCorrect")))
                    .pointsPassedMax()
                    .pointsFailedMin()
                    .build()
            ).build();

    public static final Criterion distanceFctCorrect = Criterion.builder()
            .shortDescription("Die distance-Methode von PersonFunctionCreator ist korrekt")
            .grader(Grader.testAwareBuilder()
                    .requirePass(JUnitTestRef.ofMethod(() ->
                            TutorTest_H2.class.getMethod("distanceCorrekt")))
                    .pointsPassedMax()
                    .pointsFailedMin()
                    .build()
            ).build();

    public static final Criterion H2 = Criterion.builder()
            .shortDescription("H2 - Person Verarbeitung mit Filter, Map and Foldl")
            .addChildCriteria(
                    personExist,
                    personFilterExist,
                    personToIntFunctionExist,
                    traitsExist,
                    traitsCorrect,
                    functionWithFilterMapAndFoldExists,
                    myFunctionWithFilterMapAndFold1Partly,
                    myFunctionWithFilterMapAndFold1All,
                    myFunctionWithFilterMapAndFoldCorrect2,
                    functionCreatorExistsWithActive,
                    createFunctionWithFilterMapAndFoldCorrect,
                    combinedFctCorrect,
                    myFunctionWithAdjacentExistsAndCombineInTraits,
                    myFunctionWithAdjacentCorrect,
                    distanceFctCorrect
                    )
            .build();


    public static final Rubric RUBRIC = Rubric.builder()
            .title("H07")
            .addChildCriteria(H1, H2)
            .build();

    @Override
    public Rubric getRubric() {
        return RUBRIC;
    }
}
