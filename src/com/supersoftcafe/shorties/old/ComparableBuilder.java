/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supersoftcafe.shorties.old;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/**
 *
 * @author mbrown
 * @param <OWNER>
 */
public interface ComparableBuilder<OWNER> {
    public <T extends Comparable<T>> ComparableBuilder<OWNER> with(Function<OWNER, T> getter); 
    public ComparableBuilder<OWNER> withInt(ToIntFunction<OWNER> getter);
    public ComparableBuilder<OWNER> withLong(ToLongFunction<OWNER> getter);
    public ComparableBuilder<OWNER> withDouble(ToDoubleFunction<OWNER> getter);
    public ComparableBuilder<OWNER> withBoolean(Predicate<OWNER> getter);
    
    public <T extends Comparable<T>> ComparableBuilder<OWNER> withArray(Function<OWNER, T[]> getter);
    public ComparableBuilder<OWNER> withArrayOfChar(Function<OWNER, char[]> getter);
    public ComparableBuilder<OWNER> withArrayOfByte(Function<OWNER, byte[]> getter);
    public ComparableBuilder<OWNER> withArrayOfShort(Function<OWNER, short[]> getter);
    public ComparableBuilder<OWNER> withArrayOfInt(Function<OWNER, int[]> getter);
    public ComparableBuilder<OWNER> withArrayOfLong(Function<OWNER, long[]> getter);
    public ComparableBuilder<OWNER> withArrayOfFloat(Function<OWNER, float[]> getter);
    public ComparableBuilder<OWNER> withArrayOfDouble(Function<OWNER, double[]> getter);
    public ComparableBuilder<OWNER> withArrayOfBoolean(Function<OWNER, boolean[]> getter);
    
    public <T extends Comparable<T>> ComparableBuilder<OWNER> withList(Function<OWNER, List<T>> getter);
}
