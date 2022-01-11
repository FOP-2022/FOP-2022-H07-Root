package h07.person;

/**
 * H2.3
 */
public class MyFunctionWithFilterMapAndFold2 extends FunctionWithFilterMapAndFold {
    public MyFunctionWithFilterMapAndFold2(Traits traits) {
        super(traits);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int apply(Person[] people) {
        int acc = traits.getInit();
        for (Person p : people) {
            if (!traits.getPred().test(p)) {
                continue;
            }
            int val = traits.getFct().apply(p);
            acc = traits.getOp().applyAsInt(val, acc);
        }
        return acc;
    }
}
