package H07.person;

public class MyFunctionWithFilterMapAndFold2 extends FunctionWithFilterMapAndFold {
    public MyFunctionWithFilterMapAndFold2(Traits traits) {
        super(traits);
    }

    @Override
    public int apply(Person[] people) {
        int acc = traits.getInit();
        for (Person p : people) {
            if (!traits.getPred().test(p)) {
                continue;
            }
            int val = traits.getFct().apply(p);
            acc = traits.getOp().applyAsInt(acc, val);
        }
        return acc;
    }
}
