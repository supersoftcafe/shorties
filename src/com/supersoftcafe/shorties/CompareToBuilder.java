/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supersoftcafe.shorties;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.RandomAccess;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/**
 *
 * @author mbrown
 */
public class CompareToBuilder<OWNER> {
    private final Class<OWNER> clazz;
    private final List<Comparator<OWNER>> compareToFunctions;

    
    private CompareToBuilder(Class<OWNER> clazz) {
        this.clazz = clazz;
        this.compareToFunctions = new ArrayList<>();
    }
    
    
    public CompareToBuilder<OWNER> creates(Class<OWNER> clazz) {
        return new CompareToBuilder(clazz);
    }

    
    public <T extends Comparable<T>> CompareToBuilder<OWNER> withObject(Function<OWNER, T> getter) {
        return add((p, q) -> compareObject(getter.apply(p), getter.apply(q)));
    }
    
    public <T extends String> CompareToBuilder<OWNER> withString(Function<OWNER, T> getter) {
        return add((p, q) -> compareObject(getter.apply(p), getter.apply(q)));
    }
    
    public CompareToBuilder<OWNER> withInteger(ToIntFunction<OWNER> getter) {
        return add((p, q) -> Integer.compare(getter.applyAsInt(p), getter.applyAsInt(q)));
    }
    
    public CompareToBuilder<OWNER> withInteger(ToLongFunction<OWNER> getter) {
        return add((p, q) -> Long.compare(getter.applyAsLong(p), getter.applyAsLong(q)));
    }
    
    public CompareToBuilder<OWNER> withDouble(ToDoubleFunction<OWNER> getter) {
        return add((p, q) -> Double.compare(getter.applyAsDouble(p), getter.applyAsDouble(q)));
    }
    
    public CompareToBuilder<OWNER> withBoolean(Predicate<OWNER> getter) {
        return add((p, q) -> Boolean.compare(getter.test(p), getter.test(q)));
    }

    
    public <T extends Comparable<T>> CompareToBuilder<OWNER> withListOfObject(Function<OWNER, List<T>> getter) {
        return withListOfObject(getter, COMPARABLE_COMPARATOR);
    }
    
    public <T> CompareToBuilder<OWNER> withListOfObject(Function<OWNER, List<T>> getter, Comparator<? super T> comparator) {
        return add((p, q) -> compareList(getter.apply(p), getter.apply(q), comparator));
    }
    
    public <T extends String> CompareToBuilder<OWNER> withListOfString(Function<OWNER, List<T>> getter) {
        return withListOfObject(getter, COMPARABLE_COMPARATOR);
    }
    
    public <T extends Number> CompareToBuilder<OWNER> withListOfInteger(Function<OWNER, List<T>> getter) {
        return withListOfObject(getter, INTEGER_COMPARATOR);
    }
    
    public <T extends Number> CompareToBuilder<OWNER> withListOfLong(Function<OWNER, List<T>> getter) {
        return withListOfObject(getter, LONG_COMPARATOR);
    }
    
    public <T extends Number> CompareToBuilder<OWNER> withListOfDouble(Function<OWNER, List<T>> getter) {
        return withListOfObject(getter, DOUBLE_COMPARATOR);
    }
    
    public <T extends Boolean> CompareToBuilder<OWNER> withListOfBoolean(Function<OWNER, List<T>> getter) {
        return withListOfObject(getter, BOOLEAN_COMPARATOR);
    }
    
    
    public <T extends Comparable<T>> CompareToBuilder<OWNER> withArrayOfObect(Function<OWNER, T[]> getter) {
        return add((p, q) -> compareArray(getter.apply(p), getter.apply(q), COMPARABLE_ARRAY_COMPARATOR));
    }
    
    public <T extends Comparable<T>> CompareToBuilder<OWNER> withArrayOfObect(Function<OWNER, T[]> getter, Comparator<? super T> comparator) {
        return add((p, q) -> compareArray(getter.apply(p), getter.apply(q), (r, s, i) -> comparator.compare(r[i], s[i])));
    }
    
    public <T extends String> CompareToBuilder<OWNER> withArrayOfString(Function<OWNER, T[]> getter) {
        return add((p, q) -> compareArray(getter.apply(p), getter.apply(q), COMPARABLE_ARRAY_COMPARATOR));
    }
    
    public CompareToBuilder<OWNER> withArrayOfInteger(Function<OWNER, Object> getter) {
        return add((p, q) -> compareArray(getter.apply(p), getter.apply(q), INTEGER_ARRAY_COMPARATOR));
    }
    
    public CompareToBuilder<OWNER> withArrayOfLong(Function<OWNER, Object> getter) {
        return add((p, q) -> compareArray(getter.apply(p), getter.apply(q), LONG_ARRAY_COMPARATOR));
    }
    
    public CompareToBuilder<OWNER> withArrayOfDouble(Function<OWNER, Object> getter) {
        return add((p, q) -> compareArray(getter.apply(p), getter.apply(q), DOUBLE_ARRAY_COMPARATOR));
    }
    
    public CompareToBuilder<OWNER> withArrayOfBoolean(Function<OWNER, boolean[]> getter) {
        return add((p, q) -> compareArray(getter.apply(p), getter.apply(q), BOOLEAN_ARRAY_COMPARATOR));
    }
    
    
    
    private CompareToBuilder<OWNER> add(Comparator<OWNER> comparator) {
        compareToFunctions.add(comparator);
        return this;
    }
    
    private static <T extends Comparable<T>> int compareObject(T value1, T value2) {
        if (value1 == null) {
            return value2 == null ? 0 : 1;
        } else if (value2 == null) {
            return -1;
        } else {
            return value1.compareTo(value2);
        }
    }
    
    private static <T> int compareList(List<T> list1, List<T> list2, Comparator<? super T> comparator) {
        if (list1 == null) {
            return list2 != null ? 1 : 0;
        } else if (list2 == null) {
            return -1;
        } else if (list1 instanceof RandomAccess && list2 instanceof RandomAccess) {
            int size1 = list1.size(), size2 = list2.size();
            int size = Integer.min(size1, size2);
            for (int index = 0; index < size; ++index) {
                int value = comparator.compare(list1.get(index), list2.get(index));
                if (value != 0) return value;
            }
            return size1 == size2 ? 0 : (size1 > size2 ? 1 : -1);
        } else {
            Iterator<? extends T> iter1 = list1.iterator();
            Iterator<? extends T> iter2 = list2.iterator();
            boolean res1, res2;
            while ((res1 = iter1.hasNext()) & (res2 = iter2.hasNext())) {
                int value = comparator.compare(iter1.next(), iter2.next());
                if (value != 0) return value;
            }
            return res1 == res2 ? 0 : (res1 ? 1 : 0);
        }
    }
    
    private static <T> int compareArray(T array1, T array2, ArrayComparator<T> comparator) {
        if (array1 == null) {
            return array2 != null ? 1 : 0;
        } else if (array2 == null) {
            return -1;
        } else {
            int size1 = Array.getLength(array1), size2 = Array.getLength(array2);
            int size = Integer.min(size1, size2);
            for (int index = 0; index < size; ++index) {
                int value = comparator.compare(array1, array2, index);
                if (value != 0) return value;
            }
            return size1 == size2 ? 0 : (size1 > size2 ? 1 : -1);
        }
    }
    
    
    public Comparator<OWNER> build() {
        switch (compareToFunctions.size()) {
            case 1:
                Comparator<OWNER> a1 = compareToFunctions.get(0);
                return a1;
            case 2:
                Comparator<OWNER> b1 = compareToFunctions.get(0);
                Comparator<OWNER> b2 = compareToFunctions.get(1);
                return (p, q) -> {
                    int value;
                    if ((value = b1.compare(p, q)) != 0) return value;
                    return b2.compare(p, q);
                };
            case 3:
                Comparator<OWNER> c1 = compareToFunctions.get(0);
                Comparator<OWNER> c2 = compareToFunctions.get(1);
                Comparator<OWNER> c3 = compareToFunctions.get(2);
                return (p, q) -> {
                    int value;
                    if ((value = c1.compare(p, q)) != 0) return value;
                    if ((value = c2.compare(p, q)) != 0) return value;
                    return c3.compare(p, q);
                };
            default:
                List<Comparator<OWNER>> localFunctions = new ArrayList<>(compareToFunctions);
                return (p, q) -> {
                    int index = 0, value = 0, size = localFunctions.size();
                    while (index < size && ((value = localFunctions.get(index).compare(p, q)) != 0)) ++index;
                    return value;
                };
        }
    }
    
    private static final Comparator<Comparable> COMPARABLE_COMPARATOR = (p, q) -> compareObject(p, q);
    private static final Comparator<Number>        INTEGER_COMPARATOR = (p, q) -> p == null ? (q == null ? 0 : 1) : Integer.compare(p.intValue(), q.intValue());
    private static final Comparator<Number>           LONG_COMPARATOR = (p, q) -> p == null ? (q == null ? 0 : 1) : Long.compare(p.longValue(), q.longValue());
    private static final Comparator<Number>         DOUBLE_COMPARATOR = (p, q) -> p == null ? (q == null ? 0 : 1) : Double.compare(p.doubleValue(), q.doubleValue());
    private static final Comparator<Boolean>       BOOLEAN_COMPARATOR = (p, q) -> p == null ? (q == null ? 0 : 1) : Boolean.compare(p.booleanValue(), q.booleanValue());
    
    private interface ArrayComparator<T> {int compare(T a1, T a2, int i); }
    
    private static final ArrayComparator<Comparable[]> COMPARABLE_ARRAY_COMPARATOR = (p, q, i) -> compareObject(p[i], q[i]);
    private static final ArrayComparator<Object>          INTEGER_ARRAY_COMPARATOR = (p, q, i) -> Integer.compare(Array.getInt(p, i), Array.getInt(q, i));
    private static final ArrayComparator<Object>             LONG_ARRAY_COMPARATOR = (p, q, i) -> Long.compare(Array.getLong(p, i), Array.getLong(q, i));
    private static final ArrayComparator<Object>           DOUBLE_ARRAY_COMPARATOR = (p, q, i) -> Double.compare(Array.getDouble(p, i), Array.getDouble(q, i));
    private static final ArrayComparator<boolean[]>       BOOLEAN_ARRAY_COMPARATOR = (p, q, i) -> Boolean.compare(p[i], q[i]);
}
