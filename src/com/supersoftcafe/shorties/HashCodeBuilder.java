/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supersoftcafe.shorties;

import java.util.List;
import java.util.function.Function;
import java.util.function.ToIntFunction;

/**
 *
 * @author mbrown
 */
public class HashCodeBuilder<OWNER> extends AbstractBuilder<OWNER, HashCodeBuilder<OWNER>> {
    private static final int NULL_CONSTANT = 23423489;
    private static final int INITIAL_CONSTANT = 8430928;
    
    private ToIntFunction<OWNER> hashCodeFunction = x -> 777;

    
    public HashCodeBuilder(Class<OWNER> clazz) {
        super(clazz);
    }
    

    @Override
    public <T> HashCodeBuilder<OWNER> with(Function<OWNER, T> getter) {
        ToIntFunction<OWNER> previous = hashCodeFunction;
        hashCodeFunction = x -> mergeTwoCodes(hashCodeOf(getter.apply(x)), previous.applyAsInt(x));
        return this;
    }

    @Override
    public <T> HashCodeBuilder<OWNER> withList(Function<OWNER, List<T>> getter) {
        ToIntFunction<OWNER> previous = hashCodeFunction;
        
        if (treatNullAsEmpty) {
            hashCodeFunction = x -> {
                List<T> list = getter.apply(x);
                int hashCode = isNullOrEmpty(list) ? NULL_CONSTANT : hashCodeOf(list);
                return mergeTwoCodes(hashCode, previous.applyAsInt(x));
            };
        } else {
            hashCodeFunction = x -> {
                List<T> list = getter.apply(x);
                int hashCode = list == null ? NULL_CONSTANT : hashCodeOf(list);
                return mergeTwoCodes(hashCode, previous.applyAsInt(x));
            };
        }
        
        return this;
    }

    public <T> ToIntFunction<OWNER> build() {
        return hashCodeFunction;
    }
    
    
    private static <T> int hashCodeOf(List<T> list) {
        return list.stream()
                .mapToInt(y -> hashCodeOf(y))
                .reduce(INITIAL_CONSTANT, (p, q) -> mergeTwoCodes(p, q));
    }
    
    private static int hashCodeOf(Object value) {
        return value == null ? NULL_CONSTANT : value.hashCode();
    }
    
    private static <OWNER> int mergeTwoCodes(int a, int b) {
        return a + b;
    }
}
