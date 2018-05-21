import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;

class ArrayComparator implements Comparator<Object[]> {
    private static Converter DEFAULT_CONVERTER = new Converter() {
        @Override
        public Comparable convert(Object o) {
            // simply assume the object is Comparable
            return (Comparable) o;
        }
    };
    private final int columnToSort;
    private final boolean ascending;
    private final Converter converter;


    public ArrayComparator(int columnToSort, boolean ascending) {
        this(columnToSort, ascending, DEFAULT_CONVERTER);
    }

    public ArrayComparator(int columnToSort, boolean ascending, Converter converter) {
        this.columnToSort = columnToSort;
        this.ascending = ascending;
        this.converter = converter;
    }

    public int compare(Object[] o1, Object[] o2) {
        Comparable c1 = converter.convert(o1[columnToSort]);
        Comparable c2 = converter.convert(o2[columnToSort]);
        int cmp = c1.compareTo(c2);
        return ascending ? cmp : -cmp;
    }

}

interface Converter {
    Comparable convert(Object o);
}

class DateConverter implements Converter {
    private static final DateFormat df = new SimpleDateFormat("yyyy.MM.dd hh:mm");

    @Override
    public Comparable convert(Object o) {
        try {
            return df.parse(o.toString());
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }
}