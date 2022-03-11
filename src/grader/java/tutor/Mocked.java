package tutor;

public interface Mocked {

    static Object getActualObject(Object object) {
        if (object instanceof Mocked)
            return ((Mocked) object).getActualObject();
        return object;
    }

    Object getActualObject();

}
