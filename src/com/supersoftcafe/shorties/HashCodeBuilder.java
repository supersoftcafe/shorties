/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supersoftcafe.shorties;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/**
 *
 * @author mbrown
 */
public class HashCodeBuilder<OWNER> {
    private static final int NULL_CONSTANT    = 23423489;
    private static final int INITIAL_CONSTANT = 8430928;
    
    private final Class<OWNER> clazz;
    private final List<ToIntFunction<OWNER>> hashCodeFunctions;

    
    public HashCodeBuilder(Class<OWNER> clazz) {
        this.clazz = clazz;
        this.hashCodeFunctions = new ArrayList<>();
    }
    
    
    public HashCodeBuilder<OWNER> withObject(Function<OWNER, Object> getter) {
        return add(x -> hashCodeOf(getter.apply(x)));
    }
    
    public HashCodeBuilder<OWNER> withInteger(ToIntFunction<OWNER> getter) {
        return add(getter);
    }
    
    public HashCodeBuilder<OWNER> withLong(ToLongFunction<OWNER> getter) {
        return add(x -> Long.hashCode(getter.applyAsLong(x)));
    }
    
    public HashCodeBuilder<OWNER> withDouble(ToDoubleFunction<OWNER> getter) {
        return add(x -> Double.hashCode(getter.applyAsDouble(x)));
    }
    
    public HashCodeBuilder<OWNER> withDouble(Predicate<OWNER> getter) {
        return add(x -> Boolean.hashCode(getter.test(x)));
    }
    
    
    private HashCodeBuilder<OWNER> add(ToIntFunction<OWNER> function) {
        hashCodeFunctions.add(function);
        return this;
    }
    
    private int hashCodeOf(Object o) {
        return o == null ? NULL_CONSTANT : o.hashCode();
    }
    

    public ToIntFunction<OWNER> build() {
        switch (hashCodeFunctions.size()) {
            case 1:
                ToIntFunction<OWNER> a1 = hashCodeFunctions.get(0);
                return a1;
            case 2:
                ToIntFunction<OWNER> b1 = hashCodeFunctions.get(0);
                ToIntFunction<OWNER> b2 = hashCodeFunctions.get(1);
                return x -> combine(b1.applyAsInt(x), b2.applyAsInt(x));
            case 3:
                ToIntFunction<OWNER> c1 = hashCodeFunctions.get(0);
                ToIntFunction<OWNER> c2 = hashCodeFunctions.get(1);
                ToIntFunction<OWNER> c3 = hashCodeFunctions.get(1);
                return x -> combine(combine(c1.applyAsInt(x), c2.applyAsInt(x)), c3.applyAsInt(x));
            default:
                ArrayList<ToIntFunction<OWNER>> localFunctions = new ArrayList<>(hashCodeFunctions);
                return x -> {
                    int size = localFunctions.size(), value = 0;
                    for (int index = 0; index < size; ++index) {
                        value = combine(value, localFunctions.get(index).applyAsInt(x));
                    }
                    return value;
                };
        }
    }
    
    private static int combine(int value1, int value2) {
        return value1 + value2;
    }
}
