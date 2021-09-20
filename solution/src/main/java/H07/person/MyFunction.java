package H07.person;

public class MyFunction extends MyFunctionWithFilterMapAndFold1 {

    public MyFunction(Traits traits) {
        super(traits);
    }

    public static Person[] filter(Person[] people, PersonFilter filter) {
        return MyFunctionWithFilterMapAndFold1.filter(people, filter);
    }
}
