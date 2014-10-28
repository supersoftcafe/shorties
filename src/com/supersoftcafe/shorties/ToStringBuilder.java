/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supersoftcafe.shorties;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.RandomAccess;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/**
 *
 * @author mbrown
 */
public final class ToStringBuilder<OWNER> {
    private final Class<OWNER> clazz;
    private final List<BiFunction<OWNER, StringBuilder, StringBuilder>> toStringFunctions;
    
    
    private ToStringBuilder(Class<OWNER> clazz) {
        this.clazz = clazz;
        this.toStringFunctions = new ArrayList<>(5);
        withString("class", o -> o.getClass().getName());
    }
    
    
    public ToStringBuilder<OWNER> create(Class<OWNER> clazz) {
        return new ToStringBuilder(clazz);
    }
    
    
    public ToStringBuilder<OWNER> withObject(String fieldName, Function<OWNER, Object> getter) {
        return add((o, sb) -> appendFieldName(sb, fieldName).append(getter.apply(o)));
    }
    
    public ToStringBuilder<OWNER> withString(String fieldName, Function<OWNER, String> getter) {
        return add((o, sb) -> appendString(appendFieldName(sb, fieldName), getter.apply(o)));
    }
    
    public ToStringBuilder<OWNER> withInteger(String fieldName, ToIntFunction<OWNER> getter) {
        return add((o, sb) -> appendFieldName(sb, fieldName).append(getter.applyAsInt(o)));
    }
    
    public ToStringBuilder<OWNER> withLong(String fieldName, ToLongFunction<OWNER> getter) {
        return add((o, sb) -> appendFieldName(sb, fieldName).append(getter.applyAsLong(o)));
    }
    
    public ToStringBuilder<OWNER> withDouble(String fieldName, ToDoubleFunction<OWNER> getter) {
        return add((o, sb) -> appendFieldName(sb, fieldName).append(getter.applyAsDouble(o)));
    }
    
    public ToStringBuilder<OWNER> withBoolean(String fieldName, Predicate<OWNER> getter) {
        return add((o, sb) -> appendFieldName(sb, fieldName).append(getter.test(o)));
    }
    
    
    public ToStringBuilder<OWNER> withArrayOfObject(String fieldName, Function<OWNER, Object[]> getter) {
        return add((o, sb) -> appendArray(getter.apply(o), appendFieldName(sb, fieldName), OBJECT_ARRAY_APPENDER));
    }
    
    public ToStringBuilder<OWNER> withArrayOfString(String fieldName, Function<OWNER, String[]> getter) {
        return add((o, sb) -> appendArray(getter.apply(o), appendFieldName(sb, fieldName), STRING_ARRAY_APPENDER));
    }
    
    public ToStringBuilder<OWNER> withArrayOfInteger(String fieldName, Function<OWNER, Object> getter) {
        return add((o, sb) -> appendArray(getter.apply(o), appendFieldName(sb, fieldName), INTEGER_ARRAY_APPENDER));
    }
    
    public ToStringBuilder<OWNER> withArrayOfLong(String fieldName, Function<OWNER, Object> getter) {
        return add((o, sb) -> appendArray(getter.apply(o), appendFieldName(sb, fieldName), LONG_ARRAY_APPENDER));
    }
    
    public ToStringBuilder<OWNER> withArrayOfDouble(String fieldName, Function<OWNER, Object> getter) {
        return add((o, sb) -> appendArray(getter.apply(o), appendFieldName(sb, fieldName), DOUBLE_ARRAY_APPENDER));
    }
    
    public ToStringBuilder<OWNER> withArrayOfBoolean(String fieldName, Function<OWNER, boolean[]> getter) {
        return add((o, sb) -> appendArray(getter.apply(o), appendFieldName(sb, fieldName), BOOLEAN_ARRAY_APPENDER));
    }
    
    
    public <T> ToStringBuilder<OWNER> withListOfObject(String fieldName, Function<OWNER, List<T>> getter) {
        return add((o, sb) -> appendList(getter.apply(o), appendFieldName(sb, fieldName), OBJECT_APPENDER));
    }
    
    public <T extends String> ToStringBuilder<OWNER> withListOfString(String fieldName, Function<OWNER, List<T>> getter) {
        return add((o, sb) -> appendList(getter.apply(o), appendFieldName(sb, fieldName), STRING_APPENDER));
    }
    
    public <T extends Number> ToStringBuilder<OWNER> withListOfInteger(String fieldName, Function<OWNER, List<T>> getter) {
        return add((o, sb) -> appendList(getter.apply(o), appendFieldName(sb, fieldName), INTEGER_APPENDER));
    }
    
    public <T extends Number> ToStringBuilder<OWNER> withListOfLong(String fieldName, Function<OWNER, List<T>> getter) {
        return add((o, sb) -> appendList(getter.apply(o), appendFieldName(sb, fieldName), LONG_APPENDER));
    }
    
    public <T extends Number> ToStringBuilder<OWNER> withListOfDouble(String fieldName, Function<OWNER, List<T>> getter) {
        return add((o, sb) -> appendList(getter.apply(o), appendFieldName(sb, fieldName), DOUBLE_APPENDER));
    }
    
    public <T extends Boolean> ToStringBuilder<OWNER> withListOfBoolean(String fieldName, Function<OWNER, List<T>> getter) {
        return add((o, sb) -> appendList(getter.apply(o), appendFieldName(sb, fieldName), BOOLEAN_APPENDER));
    }
    
    
    public Function<OWNER, String> build() {
        switch (toStringFunctions.size()) {
            case 1:
                BiFunction<OWNER, StringBuilder, StringBuilder> a1 = toStringFunctions.get(0);
                return x -> a1.apply(x, new StringBuilder().append('{')).append('}').toString();
            case 2:
                BiFunction<OWNER, StringBuilder, StringBuilder> b1 = toStringFunctions.get(0);
                BiFunction<OWNER, StringBuilder, StringBuilder> b2 = toStringFunctions.get(1);
                return x -> b2.apply(x, b1.apply(x, new StringBuilder().append('{'))).append('}').toString();
            case 3:
                BiFunction<OWNER, StringBuilder, StringBuilder> c1 = toStringFunctions.get(0);
                BiFunction<OWNER, StringBuilder, StringBuilder> c2 = toStringFunctions.get(1);
                BiFunction<OWNER, StringBuilder, StringBuilder> c3 = toStringFunctions.get(2);
                return x -> c3.apply(x, c2.apply(x, c1.apply(x, new StringBuilder().append('{')))).append('}').toString();
            default:
                List<BiFunction<OWNER, StringBuilder, StringBuilder>> localFunctions = new ArrayList<>(toStringFunctions);
                return x -> {
                    int size = localFunctions.size();
                    StringBuilder sb = new StringBuilder().append('{');
                    for (int index = 0; index < size; ++index)
                        localFunctions.get(index).apply(x, sb);
                    return sb.append('}').toString();
                };
        }
    }
    
    
    
    private ToStringBuilder<OWNER> add(BiFunction<OWNER, StringBuilder, StringBuilder> function) {
        toStringFunctions.add(function);
        return this;
    }
    
    
    private static StringBuilder appendFieldName(StringBuilder sb, String fieldName) {
        return sb.append(FIELD_SEPERATOR).append(fieldName).append(NAME_SEPERATOR);
    }
    
    private static StringBuilder appendString(StringBuilder sb, CharSequence sequence) {
        if (sequence == null) {
            return sb.append(NULL);
        } else {
            return sb.append('\"').append(sequence).append('\"');
        }
    }
    
    private static <T> StringBuilder appendList(List<T> list, StringBuilder sb, Appender<? super T> appender) {
        if (list == null) {
            return sb.append(NULL);
        } else if (list.isEmpty()) {
            return sb.append("[]");
        } else {
            char startChar = '[';
            if (list instanceof RandomAccess) {
                int size = list.size();
                for (int index = 0; index < size; ++index) {
                    appender.apply(list.get(index), sb.append(startChar));
                    startChar = ',';
                }
            } else {
                for (T value : list) {
                    appender.apply(value, sb.append(startChar));
                    startChar = ',';
                }
            }
            return sb.append(']');
        }
    }
    
    private static <T> StringBuilder appendArray(T array, StringBuilder sb, ArrayAppender<T> appender) {
        if (array == null) {
            return sb.append(NULL);
        } else {
            char startChar = '[';
            int size = Array.getLength(array);
            for (int index = 0; index < size; ++index) {
                appender.apply(array, index, sb.append(startChar));
                startChar = ',';
            }
            if (startChar == '[') {
                return sb.append("[]");
            } else {
                return sb.append(']');
            }
        }
    }
    
    private static final char NAME_SEPERATOR  = '=';
    private static final char FIELD_SEPERATOR = ';';
    private static final String NULL = "null";
    
    private interface Appender<T> {StringBuilder apply(T o, StringBuilder sb); }
    
    private static final Appender<Object>       OBJECT_APPENDER = (o, sb) -> sb.append(o);
    private static final Appender<CharSequence> STRING_APPENDER = (o, sb) -> sb.append(o);
    private static final Appender<Number>      INTEGER_APPENDER = (o, sb) -> o == null ? sb.append(NULL) : sb.append(o.intValue());
    private static final Appender<Number>         LONG_APPENDER = (o, sb) -> o == null ? sb.append(NULL) : sb.append(o.longValue());
    private static final Appender<Number>       DOUBLE_APPENDER = (o, sb) -> o == null ? sb.append(NULL) : sb.append(o.doubleValue());
    private static final Appender<Boolean>     BOOLEAN_APPENDER = (o, sb) -> o == null ? sb.append(NULL) : sb.append(o.booleanValue());

    private interface ArrayAppender<T> {void apply(T a, int i, StringBuilder sb); }
    
    private static final ArrayAppender<Object[]>       OBJECT_ARRAY_APPENDER = (a, i, sb) -> sb.append(a[i]);
    private static final ArrayAppender<CharSequence[]> STRING_ARRAY_APPENDER = (a, i, sb) -> sb.append('\"').append(a[i]).append('\"');
    private static final ArrayAppender<Object>        INTEGER_ARRAY_APPENDER = (a, i, sb) -> sb.append(Array.getInt(a, i));
    private static final ArrayAppender<Object>           LONG_ARRAY_APPENDER = (a, i, sb) -> sb.append(Array.getLong(a, i));
    private static final ArrayAppender<Object>         DOUBLE_ARRAY_APPENDER = (a, i, sb) -> sb.append(Array.getDouble(a, i));
    private static final ArrayAppender<boolean[]>     BOOLEAN_ARRAY_APPENDER = (a, i, sb) -> sb.append(a[i]);
}
