/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supersoftcafe.shorties.old;

import java.util.function.BiPredicate;

/**
 *
 * @author mbrown
 */
public interface Equality extends BiPredicate<Object, Object> {
    public static Equality from(Class<?> clazz, String... excludeFields) {
        return EqualityBuilder.from(clazz)
                .buildExcluding(excludeFields);
    }
    
    public static Equality lenient(Class<?> clazz, String... excludeFields) {
        return EqualityBuilder.from(clazz)
                .treatNullAsEmpty()
                .buildExcluding(excludeFields);
    }
}
