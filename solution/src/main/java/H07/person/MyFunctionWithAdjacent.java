package H07.person;

public class MyFunctionWithAdjacent extends FunctionWithFilterMapAndFold {

    public MyFunctionWithAdjacent(Traits traits) {
        super(traits);
    }

    @Override
    public int apply(Person[] people) {
        var filtered = MyFunctionWithFilterMapAndFold1.filter(people, traits.getPred());
        var mapped = MyFunctionWithFilterMapAndFold1.map(filtered, traits.getFct());
        var acc = traits.getInit();
        for (int i = 0; i < mapped.length - 1; i++) {
            acc = traits.getOp().applyAsInt(acc, traits.getCombine().applyAsInt(mapped[i], mapped[i + 1]));
        }
        return acc;
    }
}
