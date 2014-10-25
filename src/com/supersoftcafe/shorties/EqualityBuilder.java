
package com.supersoftcafe.shorties;

import java.lang.reflect.Field;
import java.lang.reflect.ReflectPermission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/**
 * Builds an object that can be used for checking equality between two
 * instances of a class.
 * @author mbrown
 */
public class EqualityBuilder<T> {
    private final Class<T> clazz;
    private final Set<Option> options;
    private final List<BiPredicate<? super T, ? super T>> equalityChecks;
    
    
    private EqualityBuilder(Class<T> clazz) {
        this.clazz = clazz;
        this.options = EnumSet.noneOf(Option.class);
        this.equalityChecks = new ArrayList<>();
    }
    
    public static <T> EqualityBuilder<T> from(Class<T> clazz) {
        return new EqualityBuilder<T>(clazz);
    }
    
    public EqualityBuilder<T> treatNullAsEmpty() {
        options.add(Option.TREAT_NULL_AS_EMPTY);
        return this;
    }
    
    public EqualityBuilder<T> with(BiPredicate<? super T, ? super T> equalityChecker) {
        if (equalityChecker == null)
            throw new NullPointerException("equalityChecks must not be null");
        equalityChecks.add(equalityChecker);
        return this;
    }
    
    public EqualityBuilder<T> withInteger(ToIntFunction<T> getter) {
        if (getter == null)
            throw new NullPointerException("getter must not be null");
        return with((u, t) -> getter.applyAsInt(u) == getter.applyAsInt(t));
    }
    
    public EqualityBuilder<T> withLong(ToLongFunction<T> getter) {
        if (getter == null)
            throw new NullPointerException("getter must not be null");
        return with((u, t) -> getter.applyAsLong(u) == getter.applyAsLong(t));
    }
    
    public EqualityBuilder<T> withDouble(ToDoubleFunction<T> getter) {
        if (getter == null)
            throw new NullPointerException("getter must not be null");
        return with((u, t) -> getter.applyAsDouble(u) == getter.applyAsDouble(t));
    }
    
    public <R> EqualityBuilder<T> withObject(Function<T, R> getter) {
        if (getter == null)
            throw new NullPointerException("getter must not be null");
        return with((u, t) -> {
            R value1 = getter.apply(u), value2 = getter.apply(t);
            return value1 == null ? value2 == null : value1.equals(value2);
        });
    }
    
    public EqualityBuilder<T> withArrayOfInteger(Function<T, int[]> getter) {
        if (getter == null)
            throw new NullPointerException("getter must not be null");
        if (options.contains(Option.TREAT_NULL_AS_EMPTY)) {
            return with((u, t) -> {
                int[] value1 = getter.apply(u), value2 = getter.apply(t);
                return isNullOrEmpty(value1) ? isNullOrEmpty(value2) : Arrays.equals(value1, value2);
            });
        } else {
            return with((u, t) -> {
                int[] value1 = getter.apply(u), value2 = getter.apply(t);
                return Arrays.equals(value1, value2);
            });
        }
    }
    
    public EqualityBuilder<T> withField(String fieldName) {
        if (fieldName == null)
            throw new NullPointerException("fieldName must not be null");
        try {
            return withField(clazz.getDeclaredField(fieldName));
        } catch (NoSuchFieldException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
    
    private EqualityBuilder<T> withField(Field field) {
        checkAndSetAccess(field);
        Class<?> type = field.getType();
        if (type == Boolean.TYPE) {
            return withBooleanField(field);
        } else if (type == Integer.TYPE || type == Short.TYPE || type == Byte.TYPE || type == Character.TYPE) {
            return withIntegerField(field);
        } else if (type == Long.TYPE) {
            return withLongField(field);
        } else if (type == Double.TYPE || type == Float.TYPE) {
            return withDoubleField(field);
        } else if (type == int[].class) {
            return withArrayOfIntegerField(field);
        } else {
            return withObjectField(field);
        }
    }
    
    private EqualityBuilder<T> withBooleanField(Field field) {
        return with((t, u) -> {
            try {
                return field.getBoolean(t) == field.getBoolean(u);
            } catch (IllegalAccessException ex) {
                throw new IllegalStateException(ex);
            }
        });
    }
    
    private EqualityBuilder<T> withIntegerField(Field field) {
        return with((t, u) -> {
            try {
                return field.getInt(t) == field.getInt(u);
            } catch (IllegalAccessException ex) {
                throw new IllegalStateException(ex);
            }
        });
    }
    
    private EqualityBuilder<T> withLongField(Field field) {
        return with((t, u) -> {
            try {
                return field.getLong(t) == field.getLong(u);
            } catch (IllegalAccessException ex) {
                throw new IllegalStateException(ex);
            }
        });
    }
    
    private EqualityBuilder<T> withDoubleField(Field field) {
        return with((t, u) -> {
            try {
                return field.getDouble(t) == field.getDouble(u);
            } catch (IllegalAccessException ex) {
                throw new IllegalStateException(ex);
            }
        });
    }
    
    private EqualityBuilder<T> withObjectField(Field field) {
        return withObject(x -> {
            try {
                return field.get(x);
            } catch (IllegalAccessException ex) {
                throw new IllegalStateException(ex);
            }
        });
    }
    
    private EqualityBuilder<T> withArrayOfIntegerField(Field field) {
        return withArrayOfInteger(x -> {
            try {
                return (int[])field.get(x);
            } catch (IllegalAccessException ex) {
                throw new IllegalStateException(ex);
            }
        });
    }
    
    
    
    public Equality buildIncluding(String... fieldNames) {
        for (String fieldName : fieldNames)
            withField(fieldName);
        return build();
    }
    
    public Equality buildExcluding(String... fieldNames) {
        Set<String> excludeNames = new HashSet<>(Arrays.asList(fieldNames));
        for (Field field : clazz.getDeclaredFields())
            if (!excludeNames.contains(field.getName()))
                withField(field);
        return build();
    }
    
    public Equality build() {
        switch (equalityChecks.size()) {
            case 0: return build0(clazz, equalityChecks);
            case 1: return build1(clazz, equalityChecks);
            case 2: return build2(clazz, equalityChecks);
            case 3: return build3(clazz, equalityChecks);
            default:return buildN(clazz, equalityChecks);
        }
    }
    
    private static <T> Equality build0(Class<T> type, List<BiPredicate<? super T, ? super T>> equalityChecks) {
        return (t, u) -> {
            return t == null && u == null
                || type.isInstance(t) && type.isInstance(u);
        };
    }
    
    @SuppressWarnings("unchecked")
    private static <T> Equality build1(Class<T> type, List<BiPredicate<? super T, ? super T>> equalityChecks) {
        BiPredicate<? super T, ? super T> p1 = equalityChecks.get(0);
        return (t, u) -> {
            return t == null && u == null
                || type.isInstance(t) && type.isInstance(u)
                && p1.test((T)t, (T)u);
        };
    }
    
    @SuppressWarnings("unchecked")
    private static <T> Equality build2(Class<T> type, List<BiPredicate<? super T, ? super T>> equalityChecks) {
        BiPredicate<? super T, ? super T> q1 = equalityChecks.get(0);
        BiPredicate<? super T, ? super T> q2 = equalityChecks.get(1);
        return (t, u) -> {
            return t == null && u == null
                || type.isInstance(t) && type.isInstance(u)
                && q1.test((T)t, (T)u) && q2.test((T)t, (T)u);
        };
    }
    
    @SuppressWarnings("unchecked")
    private static <T> Equality build3(Class<T> type, List<BiPredicate<? super T, ? super T>> equalityChecks) {
        BiPredicate<? super T, ? super T> r1 = equalityChecks.get(0);
        BiPredicate<? super T, ? super T> r2 = equalityChecks.get(1);
        BiPredicate<? super T, ? super T> r3 = equalityChecks.get(2);
        return (t, u) -> {
            return t == null && u == null
                || type.isInstance(t) && type.isInstance(u)
                && r1.test((T)t, (T)u) && r2.test((T)t, (T)u) && r3.test((T)t, (T)u);
        };
    }
    
    @SuppressWarnings("unchecked")
    private static <T> Equality buildN(Class<T> type, List<BiPredicate<? super T, ? super T>> equalityChecks) {
        List<BiPredicate<? super T, ? super T>> predicates = new ArrayList<>(equalityChecks);
        return (t, u) -> {
            return t == null && u == null
                || type.isInstance(t) && type.isInstance(u)
                && predicates.stream().allMatch(x -> x.test((T)t, (T)u));
        };
    }
    
    static final private java.security.Permission ACCESS_PERMISSION = new ReflectPermission("suppressAccessChecks");
    
    private void checkAndSetAccess(Field field) {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) sm.checkPermission(ACCESS_PERMISSION);
        field.setAccessible(true);
    }
    
    private static boolean isNullOrEmpty(Collection<?> list) {
        return list == null || list.isEmpty();
    }
    
    private static boolean isNullOrEmpty(int[] array) {
        return array == null || array.length == 0;
    }
    
    private enum Option {
        TREAT_NULL_AS_EMPTY
    }
}
