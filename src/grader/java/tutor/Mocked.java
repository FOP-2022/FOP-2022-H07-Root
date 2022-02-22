package tutor;

public interface Mocked {

    Object getActualObject();

    static Object getActualObject(Object object) {
        if (object instanceof Mocked)
            return ((Mocked) object).getActualObject();
        return object;
    }

}
