package h07.person;

public class MyFunctionWithAdjacent extends FunctionWithFilterMapAndFold {

    /**
     * Defines the required argument of {@link Traits} for all subclasses. To be used with super.
     *
     * @param traits {@link Traits} to configure the functions effect. For this class the {@link Traits} should contain a combine-Function
     */
    public MyFunctionWithAdjacent(Traits traits) {
        super(traits);
    }

    /**
     * Filters and maps to int-array, then combines adjacent entries as such that [int1, int2, int3] -> [combine(int1, int2), combine(int2, int3)] and finally folds this into a single int result
     *
     * @param people an array of {@link Person} that should be processed
     * @return the resulting value when applying {@link Traits} to people
     */
    @Override
    public int apply(Person[] people) {
        var filtered = MyFunctionWithFilterMapAndFold1.filter(traits.getPred(), people);
        var mapped = MyFunctionWithFilterMapAndFold1.map(traits.getFct(), filtered);
        var acc = traits.getInit();
        for (int i = 0; i < mapped.length - 1; i++) {
            acc = traits.getOp().applyAsInt(traits.getCombine().applyAsInt(mapped[i], mapped[i + 1]), acc);
        }
        return acc;
    }
}
