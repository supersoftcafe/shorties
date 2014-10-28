/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.supersoftcafe.shorties;

import com.supersoftcafe.shorties.old.Equality;
import com.supersoftcafe.shorties.old.EqualityBuilder;
import java.lang.reflect.Field;
import java.util.function.BiPredicate;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mbrown
 */
public class EqualityBuilderTest {
    private String aString;
    private int anInt;
    private int[] anArrayOfInt;
    
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testBasic() {
        Equality equals =
                EqualityBuilder.from(EqualityBuilderTest.class)
                .with(x -> x.aString)
                .with(x -> x.anInt)
                .build();
        
        EqualityBuilderTest a = new EqualityBuilderTest();
        a.aString = "fred";
        a.anInt = 27;
        
        EqualityBuilderTest b = new EqualityBuilderTest();
        b.aString = "fred";
        b.anInt = 27;
        
        assertTrue(equals.test(a, b));
    }
    
    @Test
    public void testArrayOfInt() {
        Equality equals =
                EqualityBuilder.from(EqualityBuilderTest.class)
                .buildIncluding("anArrayOfInt");
        
        
        EqualityBuilderTest a = new EqualityBuilderTest();
        a.anArrayOfInt = new int[] {1,2,3};
        
        EqualityBuilderTest b = new EqualityBuilderTest();
        b.anArrayOfInt = new int[] {1,2,3};
        
        assertTrue(equals.test(a, b));
    }
    
    
    @Test
    public void testAll() {
        Equality equals = Equality.from(EqualityBuilderTest.class);
        
        EqualityBuilderTest a = new EqualityBuilderTest();
        a.anArrayOfInt = new int[] {1,2,3};
        a.anInt = 2;
        
        EqualityBuilderTest b = new EqualityBuilderTest();
        b.anArrayOfInt = new int[] {1,2,3};
        b.anInt = 2;
        
        assertTrue(equals.test(a, b));
    }
}
