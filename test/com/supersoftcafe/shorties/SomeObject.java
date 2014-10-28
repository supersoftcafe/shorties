/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supersoftcafe.shorties;

import com.supersoftcafe.shorties.old.Equality;
import com.supersoftcafe.shorties.old.EqualityBuilder;
import java.util.List;

/**
 *
 * @author mbrown
 */
public class SomeObject {
    private int integer;
    private int[] arrayOfInteger;
    private List<SomeObject> listOfObjects;
    
    private static Equality E = EqualityBuilder.from(SomeObject.class)
            .with(x -> x.integer)
            .withArrayOfInteger(x -> x.arrayOfInteger)
            .withListOfObject(x -> x.listOfObjects, y -> y.integer)
            .build();
    
    
    
    private static Equality EQUALITY = Equality.lenient(SomeObject.class);
    public boolean equals(Object other) {
        return EQUALITY.test(this, other);
    }
    
    
}
