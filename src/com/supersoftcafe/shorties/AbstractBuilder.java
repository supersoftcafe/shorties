/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supersoftcafe.shorties;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/**
 *
 * @author mbrown
 * @param <OWNER>
 * @param <SUBTYPE>
 */
public abstract class AbstractBuilder<OWNER, SUBTYPE extends AbstractBuilder<OWNER, SUBTYPE>> {
    protected boolean treatNullAsEmpty;
    protected Class<OWNER> clazz;

    
    public AbstractBuilder(Class<OWNER> clazz) {
        this.clazz = clazz;
    }
    
    
    public abstract <T> SUBTYPE with(Function<OWNER, T> getter);
    public abstract <T> SUBTYPE withList(Function<OWNER, List<T>> getter);
    
    
    
    @SuppressWarnings("unchecked")
    public SUBTYPE treatNullAsEmpty() {
        treatNullAsEmpty = true;
        return (SUBTYPE)this;
    }
    
    protected static <T> boolean isNullOrEmpty(List<T> list) {
        return list == null || list.isEmpty();
    }
    
    
    
    public SUBTYPE withInt(ToIntFunction<OWNER> getter) {
        return with(x -> getter.applyAsInt(x));
    }
    public SUBTYPE withLong(ToLongFunction<OWNER> getter) {
        return with(x -> getter.applyAsLong(x));
    }
    public SUBTYPE withDouble(ToDoubleFunction<OWNER> getter) {
        return with(x -> getter.applyAsDouble(x));
    }
    public SUBTYPE withBoolean(Predicate<OWNER> getter) {
        return with(x -> getter.test(x));
    }
    
    public <T> SUBTYPE withArray(Function<OWNER, T[]> getter) {
        return withList(x -> {
            T[] y = getter.apply(x);
            return y == null ? null : Arrays.asList(y);
        });
    }
    
    public SUBTYPE withArrayOfChar(Function<OWNER, char[]> getter) {
        return withList(x -> {
            char[] y = getter.apply(x);
            return y == null ? null : new AbstractList<Object>() {
                @Override public Object get(int i) { return y[i]; }
                @Override public int size()     {return y.length; }
            };
        });
    }
    
    public SUBTYPE withArrayOfByte(Function<OWNER, byte[]> getter) {
        return withList(x -> {
            byte[] y = getter.apply(x);
            return y == null ? null : new AbstractList<Object>() {
                @Override public Object get(int i) { return y[i]; }
                @Override public int size()     {return y.length; }
            };
        });
    }
    
    public SUBTYPE withArrayOfShort(Function<OWNER, short[]> getter) {
        return withList(x -> {
            short[] y = getter.apply(x);
            return y == null ? null : new AbstractList<Object>() {
                @Override public Object get(int i) { return y[i]; }
                @Override public int size()     {return y.length; }
            };
        });
    }
    
    public SUBTYPE withArrayOfInt(Function<OWNER, int[]> getter) {
        return withList(x -> {
            int[] y = getter.apply(x);
            return y == null ? null : new AbstractList<Object>() {
                @Override public Object get(int i) { return y[i]; }
                @Override public int size()     {return y.length; }
            };
        });
    }
    
    public SUBTYPE withArrayOfLong(Function<OWNER, long[]> getter) {
        return withList(x -> {
            long[] y = getter.apply(x);
            return y == null ? null : new AbstractList<Object>() {
                @Override public Object get(int i) { return y[i]; }
                @Override public int size()     {return y.length; }
            };
        });
    }
    
    public SUBTYPE withArrayOfFloat(Function<OWNER, float[]> getter) {
        return withList(x -> {
            float[] y = getter.apply(x);
            return y == null ? null : new AbstractList<Object>() {
                @Override public Object get(int i) { return y[i]; }
                @Override public int size()     {return y.length; }
            };
        });
    }
    
    public SUBTYPE withArrayOfDouble(Function<OWNER, double[]> getter) {
        return withList(x -> {
            double[] y = getter.apply(x);
            return y == null ? null : new AbstractList<Object>() {
                @Override public Object get(int i) { return y[i]; }
                @Override public int size()     {return y.length; }
            };
        });
    }
    
    public SUBTYPE withArrayOfBoolean(Function<OWNER, boolean[]> getter) {
        return withList(x -> {
            boolean[] y = getter.apply(x);
            return y == null ? null : new AbstractList<Object>() {
                @Override public Object get(int i) { return y[i]; }
                @Override public int size()     {return y.length; }
            };
        });
    }
    
    
}
