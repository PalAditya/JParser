/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.MockitoAnnotations;

/**
 *
 * @author Lenovo
 */
public class SLRParserTest {
    
    public SLRParserTest() {
    }
    String file="src/test/res/MockTest.txt";
    SLRParser instance;
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        instance=new SLRParser();
        instance.read_grammar(file);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of buildDFA method, of class LR0Parser.
     */
    @Test
    public void testBuildDFA() {
        instance.buildDFA();
        assertEquals(instance.states.size(),6);
    }

    /**
     * Test of getParsingTable method, of class SLRParser.
     */
    @Test
    public void testGetParsingTable() {
        boolean output = true;
        boolean expResult = true;
        instance.buildDFA();
        boolean result = instance.getParsingTable(output);
        assertEquals(expResult, result);
    }
    
    @Test
    public void ParsingTableTest()
    {
        instance.buildDFA();
        boolean result = instance.getParsingTable(false);
        boolean parseResult=instance.parse("c b", true);
        assertEquals(parseResult,true);
    }
    
}
