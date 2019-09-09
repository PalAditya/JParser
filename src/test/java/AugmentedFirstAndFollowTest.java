/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author Lenovo
 */
public class AugmentedFirstAndFollowTest {
    
    public AugmentedFirstAndFollowTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    AugmentedFirstAndFollow obj;
    @Before
    public void setUp() {
        obj=new AugmentedFirstAndFollow();
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of join method, of class AugmentedFirstAndFollow.
     */
    @Test
    public void testJoin() {
        ArrayList<String> v = new ArrayList<>();
        v.add("Aditya");
        v.add("Pal");
        String delim = " ";
        AugmentedFirstAndFollow instance = new AugmentedFirstAndFollow();
        String expResult = "Aditya Pal";
        String result = instance.join(v, delim);
        assertEquals(expResult, result);
        delim = ",";
        expResult = "Aditya,Pal";
        result = instance.join(v, delim);
        assertEquals(expResult, result);
        
    }

    /**
     * Test of getChar method, of class AugmentedFirstAndFollow.
     */
    @Test
    public void testGetChar() {
        String s = "Aditya";
        AugmentedFirstAndFollow instance = new AugmentedFirstAndFollow();
        String expResult = "Aditya'";
        String result = instance.getChar(s);
        assertEquals(expResult, result);
    }

     /** 
      * Test of module1 method, of class AugmentedFirstAndFollow.
     * @throws java.lang.Exception
     */
    @Test
    public void testModule1() throws Exception {
        String filename = "D://Documents/Lab4/MockTest.txt";
        boolean augment = false;
        boolean output = false;
        HashMap<String,ArrayList<String>> hm=new HashMap<>();
        ArrayList<String> al=new ArrayList<>();
        ArrayList<String> al2=new ArrayList<>();
        al.add("c");
        al2.add("b");
        hm.put("A",al);
        hm.put("B",al2);
        HashMap<String,ArrayList<String>> hm2=new HashMap<>();
        ArrayList<String> al3=new ArrayList<>();
        ArrayList<String> al22=new ArrayList<>();
        obj.module1(filename, augment, output);
        al3.add("$");
        hm2.put("A",al3);
        hm2.put("B",al22);
        assertEquals(obj.fLine,"A");
        assertEquals(obj._first,hm);
        assertEquals(obj._follow,hm2);
    }

    @Test
    public void testUniqueAndEpsilonLess() {
        ArrayList<String> al = new ArrayList<>();
        ArrayList<String> expResult = new ArrayList<>();
        al.add("a");
        al.add("c");
        al.add("b");
        al.add("@");
        al.add("a");
        expResult.add("a");
        expResult.add("b");
        expResult.add("c");
        ArrayList<String> result = obj.uniqueAndEpsilonLess(al);
        assertEquals(expResult, result);
    }

    /**
     * Test of unique method, of class AugmentedFirstAndFollow.
     */
    @Test
    public void testUnique() {
        ArrayList<String> al = new ArrayList<>();
        ArrayList<String> expResult = new ArrayList<>();
        al.add("a");
        al.add("c");
        al.add("b");
        al.add("a");
        expResult.add("a");
        expResult.add("b");
        expResult.add("c");
        ArrayList<String> result = obj.uniqueAndEpsilonLess(al);
        assertEquals(expResult, result);
    }

    /**
     * Test of calculateFollow method, of class AugmentedFirstAndFollow.
     */
    /*@Test
    public void testCalculateFollow() {
        System.out.println("calculateFollow");
        HashMap<String, ArrayList<ArrayList<String>>> hm = null;
        HashMap<String, ArrayList<String>> first = null;
        HashMap<String, ArrayList<String>> follow = null;
        boolean output = false;
        boolean augment = false;
        AugmentedFirstAndFollow instance = new AugmentedFirstAndFollow();
        instance.calculateFollow(hm, first, follow, output, augment);
    }*/

}