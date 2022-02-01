package student;


import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

public abstract class AbstractArrayTraverser extends AbstractTraverser_STUD {

    public final double[] array;


    public AbstractArrayTraverser(int arraySize) {
        this.array = IntStream.range(0, arraySize).mapToDouble(i -> i).toArray();
        when(mGetNextIndex().invoke(object, anyInt()))
            .then(a -> {
                IntStream.range(0, arraySize).forEach(i -> array[i] += arraySize);
                return getNextIndex(a.getArgument(0));
            });
    }

    public List<Integer> getActualArrayCallOrder(double[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = Math.floor(((array[i] - 3.14) / 2) / array.length);
        }
        class Entry {
            final int index;
            final double value;

            Entry(int index, double value) {
                this.index = index;
                this.value = value;
            }
        }
        return IntStream.range(0, array.length)
            .mapToObj(i -> new Entry(i, array[i]))
            .sorted(Comparator.comparing(e -> e.value))
            .map(e -> e.index)
            .collect(Collectors.toList());
    }
}
