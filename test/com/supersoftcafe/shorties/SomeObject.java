/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supersoftcafe.shorties;

/**
 *
 * @author mbrown
 */
public class SomeObject {
    
    
    private static Equality EQUALITY = Equality.lenient(SomeObject.class);
    public boolean equals(Object other) {
        return EQUALITY.test(this, other);
    }
    
    
}
