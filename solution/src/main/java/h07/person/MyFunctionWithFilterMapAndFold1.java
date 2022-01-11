package h07.person;

import java.util.function.IntBinaryOperator;

public class MyFunctionWithFilterMapAndFold1 extends FunctionWithFilterMapAndFold {
    public MyFunctionWithFilterMapAndFold1(Traits traits) {
        super(traits);
    }

    /**
     * Filters people according to a filter.
     *
     * @param people an array of {@link Person}
     * @param filter a {@link PersonFilter} to filter matching
     * @return all {@link Person} p of people, where filter.test(p)
     */
    public static Person[] filter(PersonFilter filter, Person[] people) {
        Person[] filtered = new Person[people.length];
        int n = 0;
        for (Person p : people) {
            if (filter.test(p)) {
                filtered[n++] = p;
            }
        }
        Person[] result = new Person[n];
        System.arraycopy(filtered, 0, result, 0, n);
        return result;
    }

    /**
     * Maps all people to a corresponding int.
     *
     * @param people an array of {@link Person}
     * @param map    a {@link PersonToIntFunction} that realises the mapping to int
     * @return for each {@link Person} p the result contains map.apply(p) in the same order
     */
    public static int[] map(PersonToIntFunction map, Person[] people) {
        int[] toInts = new int[people.length];
        for (int i = 0; i < people.length; i++) {
            toInts[i] = map.apply(people[i]);
        }
        return toInts;
    }

    /**
     * Folds many int values into one, from left to right.
     *
     * @param ints     the int values
     * @param init     the start value
     * @param operator how ints are combined
     * @return returns the result of the foldl for [int1, ..., intN] as operator(... operator(init, int1) ..., intN)
     */
    public static int foldl(IntBinaryOperator operator, int init, int[] ints) {
        int acc = init;
        for (var val : ints) {
            acc = operator.applyAsInt(val, acc);
        }
        return acc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int apply(Person[] people) {
        var filtered = filter(traits.getPred(), people);
        var mapped = map(traits.getFct(), filtered);
        return foldl(traits.getOp(), traits.getInit(), mapped);
    }
}
