/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supersoftcafe.shorties;

import java.lang.reflect.Array;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

public abstract class Builder<OWNER, TYPE extends Builder<OWNER, TYPE>> {
    protected final Class<OWNER> clazz;
    
    public Builder(Class<OWNER> clazz) {
        this.clazz = clazz;
    }

    
    public abstract <T extends Comparable<T>> TYPE with(Function<OWNER, T> getter);
    
    public abstract <T extends Comparable<T>> TYPE withList(Function<OWNER, List<T>> getter);
    
    public abstract <T extends Comparable<T>> TYPE withArray(Function<OWNER, T[]> getter);
    
    public abstract TYPE withLong(ToLongFunction<OWNER> getter);
    
    public abstract TYPE withDouble(ToDoubleFunction<OWNER> getter);
    
    protected abstract <T> TYPE withPrimitiveArray(Function<OWNER, T> getter, AsLongArray<T> accessor);
    protected abstract <T> TYPE withPrimitiveArray(Function<OWNER, T> getter, AsDoubleArray<T> accessor);
    
    
    
    public TYPE withInteger(ToIntFunction<OWNER> getter) {
        return withLong(x -> getter.applyAsInt(x));
    }
    
    public TYPE withBoolean(Predicate<OWNER> getter) {
        return withLong(x -> getter.test(x)?1:0);
    }
    
    
    
    public TYPE withArrayOfChar(Function<OWNER, char[]> getter) {
        return withPrimitiveArray(getter, CHAR_ACCESS);
    }
    
    public TYPE withArrayOfByte(Function<OWNER, byte[]> getter) {
        return withPrimitiveArray(getter, BYTE_ACCESS);
    }
    
    public TYPE withArrayOfShort(Function<OWNER, short[]> getter) {
        return withPrimitiveArray(getter, SHORT_ACCESS);
    }
    
    public TYPE withArrayOfInteger(Function<OWNER, int[]> getter) {
        return withPrimitiveArray(getter, INT_ACCESS);
    }
    
    public TYPE withArrayOfLong(Function<OWNER, long[]> getter) {
        return withPrimitiveArray(getter, LONG_ACCESS);
    }
    
    public TYPE withArrayOfFloat(Function<OWNER, float[]> getter) {
        return withPrimitiveArray(getter, FLOAT_ACCESS);
    }
    
    public TYPE withArrayOfDouble(Function<OWNER, double[]> getter) {
        return withPrimitiveArray(getter, DOUBLE_ACCESS);
    }
    
    public TYPE withArrayOfBoolean(Function<OWNER, boolean[]> getter) {
        return withPrimitiveArray(getter, BOOLEAN_ACCESS);
    }
    
    
    
    protected static interface AsLongArray<T> {
        default int sizeOfArray(T array) {
            return Array.getLength(array);
        }
        long entryAsLong(T array, int index);
    }
    
    protected static interface AsDoubleArray<T> {
        default int sizeOfArray(T array) {
            return Array.getLength(array);
        }
        double entryAsDouble(T array, int index);
    }
    
    private static final AsLongArray<char[]>     CHAR_ACCESS    = (x, i) -> x[i];
    private static final AsLongArray<byte[]>     BYTE_ACCESS    = (x, i) -> x[i];
    private static final AsLongArray<short[]>    SHORT_ACCESS   = (x, i) -> x[i];
    private static final AsLongArray<int[]>      INT_ACCESS     = (x, i) -> x[i];
    private static final AsLongArray<long[]>     LONG_ACCESS    = (x, i) -> x[i];
    private static final AsLongArray<boolean[]>  BOOLEAN_ACCESS = (x, i) -> x[i]?1:0;
    private static final AsDoubleArray<float[]>  FLOAT_ACCESS   = (x, i) -> x[i];
    private static final AsDoubleArray<double[]> DOUBLE_ACCESS  = (x, i) -> x[i];
}
