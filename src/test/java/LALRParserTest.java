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
public class LALRParserTest {
    
    public LALRParserTest() {
    }
    String file="src\\test\\res\\MockTest.txt";
    LALRParser instance;
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
        instance=new LALRParser();
        instance.read_grammar(file);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of buildDFA method, of class LALRParser.
     */
    @Test
    public void testBuildDFA() {
        instance.buildDFA();
        assertEquals(instance.states.size(),6);
    }

    /**
     * Test of getParsingTable method, of class LALRParser.
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
        parseResult=instance.parse("c b k", true);
        assertEquals(parseResult,false);
    }
    @Test
    public void extraTests()
    {
        instance=new LALRParser();
        instance.read_grammar("src/test/res/LR0.txt");
        instance.buildDFA();
        boolean result = instance.getParsingTable(false);
        assertEquals(result,false);
        boolean parseResult=instance.parse("a c d", false);
        assertEquals(parseResult,true);
    }
}
