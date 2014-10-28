/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supersoftcafe.shorties;

import java.util.Iterator;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Function;

/**
 *
 * @author mbrown
 */
public class EqualsBuilder<OWNER> extends AbstractBuilder<OWNER, EqualsBuilder<OWNER>> {
    private BiPredicate<OWNER, OWNER> equalsPredicate;

    
    public EqualsBuilder(Class<OWNER> clazz) {
        super(clazz);
        equalsPredicate = (x, y) -> clazz.isInstance(x) && clazz.isInstance(y);
    }
    
    
    @Override
    public <T> EqualsBuilder<OWNER> with(Function<OWNER, T> getter) {
        BiPredicate<OWNER, OWNER> previous = equalsPredicate;
        equalsPredicate = (x, y) -> previous.test(x, y) && equality(getter.apply(x), getter.apply(y));
        return this;
    }

    @Override
    public <T> EqualsBuilder<OWNER> withList(Function<OWNER, List<T>> getter) {
        BiPredicate<OWNER, OWNER> previous = equalsPredicate;
        if (treatNullAsEmpty) {
            equalsPredicate = (x, y) -> {
                if (!previous.test(x, y)) return false;
                List<T> list1 = getter.apply(x), list2 = getter.apply(y);
                return equality(list1, isNullOrEmpty(list1), list2, isNullOrEmpty(list2));
            };
        } else {
            equalsPredicate = (x, y) -> {
                if (!previous.test(x, y)) return false;
                List<T> list1 = getter.apply(x), list2 = getter.apply(y);
                return equality(list1, list1 == null, list2, list2 == null);
            };
        }
        return this;
    }
    
    public BiPredicate<OWNER, OWNER> build() {
        return equalsPredicate;
    }
    
    private static final <T> boolean equality(List<T> list1, boolean isNull1, List<T> list2, boolean isNull2) {
        if (isNull1 ? isNull2 : list1.size() == list2.size()) {
            Iterator<T> iter1 = list1.iterator();
            for (T value2 : list2) {
                if (!equality(iter1.next(), value2)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    private static final boolean equality(Object value1, Object value2) {
        return value1 == null ? value2 == null : value1.equals(value2);
    }
}
