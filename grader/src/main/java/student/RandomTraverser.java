package student;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.IntStream.range;


public class RandomTraverser extends AbstractArrayTraverser {

    private final int first;
    private final Map<Integer, Integer> map = new HashMap<>();

    public RandomTraverser(Random random) {
        this(random, 1 + random.nextInt(9));
    }

    public RandomTraverser(Random random, int size) {
        super(size);
        LinkedList<Integer> rest = range(0, size).boxed().collect(Collectors.toCollection(LinkedList::new));
        Collections.shuffle(rest, random);
        Integer last = first = rest.removeFirst(), current;
        while (!rest.isEmpty()) {
            current = rest.removeFirst();
            map.put(last, current);
            last = current;
        }
        map.put(last, random.nextBoolean() ? -1 : size);
    }

    public List<Integer> getExpectedArrayCallOrder(double[] array) {
        return Stream.iterate(getFirstIndex(array), this::getNextIndex).limit(array.length).collect(Collectors.toList());
    }

    @Override
    public int getFirstIndex(double[] array) {
        return first;
    }

    @Override
    public int getNextIndex(int index) {
        if (map == null) {
            return 0;
        }
        return map.get(index);
    }
}
