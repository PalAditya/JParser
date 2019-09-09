/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import static org.mockito.Matchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

/**
 *
 * @author Lenovo
 */
public class ParserTest {
    Parser instance;
    String file;
    
    @Mock
    AugmentedFirstAndFollow utils;
    
    @Mock
    Parser parser;
    
    public ParserTest() {
    }
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
        instance = new Parser();
        file="../res/MockTest.txt";
        instance.allSymbols=new ArrayList<>(Arrays.asList("b,c,A,B"));
        instance.nonTerminals=new HashSet<>(new ArrayList<>(Arrays.asList("A","B")));
        instance.terminals=new HashSet<>(new ArrayList<>(Arrays.asList("b","c")));
        instance.startSymbol="A";        
        instance.utils=new AugmentedFirstAndFollow();
        HashMap<String,ArrayList<ArrayList<String>>> hm=new HashMap<>();
        ArrayList<String> al=new ArrayList<>();
        ArrayList<String> al2=new ArrayList<>();
        ArrayList<ArrayList<String>> a1=new ArrayList<>();
        al.add("c");
        al.add("B");
        al2.add("@");
        a1.add(al);
        a1.add(al2);
        ArrayList<String> all=new ArrayList<>();
        ArrayList<ArrayList<String>> a2=new ArrayList<>();
        all.add("b");
        a2.add(all);
        hm.put("A",a1);
        hm.put("B",a2);
        instance.rules=hm;
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getId method, of class Parser.
     */
    @Test
    public void testGetId() {
        int expResult = 0;
        int result = instance.getId();
        assertEquals(expResult, result);
        result = instance.getId();
        assertEquals(1, result);
    }
    /**
     * Test of modify method, of class Parser.
     */
    @Test
    public void testModify() {
        instance.modify(instance.utils);
        assert(instance.utils.ntCount.contains(instance.startSymbol+"'"));
        assertEquals(instance.utils.tCount.size(),instance.terminals.size()+1);
    }
    /**
     * Test of read_grammar method, of class Parser.
     */
    @Test
    public void testRead_grammar() {
        Mockito.doAnswer(new Answer() {
        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable {
            parser=instance;
            return null;
        }
    }).when(parser).read_grammar(any());
        parser.read_grammar(file);
        assertEquals(parser.startSymbol,instance.startSymbol);
        Collections.sort(parser.allSymbols);
        Collections.sort(instance.allSymbols);
        assertEquals(parser.allSymbols,instance.allSymbols);
    }

    /**
     * Test of join method, of class Parser.
     */
    @Test
    public void testJoin() {
        ArrayList<String> v = new ArrayList<>();
        v.add("Aditya");
        v.add("Pal");
        String delim = " ";
        String expResult = "Aditya Pal";
        String result = instance.join(v, delim);
        assertEquals(expResult, result);
        delim = ",";
        expResult = "Aditya,Pal";
        result = instance.join(v, delim);
        assertEquals(expResult, result);       
        v.clear();
        result=instance.join(v,delim);
        assertEquals(result,"");
        v=null;
        result = instance.join(v, delim);
        assertEquals(result,"");
    }

    /**
     * Test of getClosure method, of class Parser.
     */
    @Test
    public void testGetClosure() {
        HashSet<Parser.Pair> closure = new HashSet<>();
        closure.add(new Parser.Pair("A -> c B",1));       
        instance.getClosure(closure);
        assert(closure.size()==2);
        assert(closure.contains(new Parser.Pair("A -> c B",1)));
        assert(closure.contains(new Parser.Pair("B -> b",0)));
		closure.clear();
        closure.add(new Parser.Pair("A -> c B",4));
        instance.getClosure(closure);
        assert(closure.size()==1);
    }
    /**
     * Test of getGoto method, of class Parser.
     */
    @Test
    public void testGetGoto() {
        HashSet<Parser.Pair> X = new HashSet<>();
        X.add(new Parser.Pair("A -> c B",1));  
        String I = "B";
        HashSet<Parser.Pair> result = instance.getGoto(X, I);
        assert(result.size()==1);
        assert(result.contains(new Parser.Pair("A -> c B",2)));
		I= "C";
        result = instance.getGoto(X, I);
        assert(result.isEmpty());
        X.clear();
        X.add(new Parser.Pair("A -> c B",4));
        result = instance.getGoto(X, I);
        assert(result.isEmpty());
    }

    /**
     * Test of augment method, of class Parser.
     */
    @Test
    public void testAugment() {
        instance.augment();
        assertNotEquals(instance.rules.get(instance.startSymbol+"'"),null);
    }

    /**
     * Test of unAugment method, of class Parser.
     */
    @Test
    public void testUnAugment() {
        instance.unAugment();
        assertEquals(instance.rules.get(instance.startSymbol+"'"),null);
    }

    /**
     * Test of parse method, of class Parser.
     */
    @Test
    public void testParse() {
        String _toParse = "c b";
        boolean output = false;
        boolean expResult = false;
        boolean result = instance.parse(_toParse, output);
        assertEquals(expResult, result);
    }

    /**
     * Test of pretty_it method, of class Parser.
     */
    @Test
    public void testPretty_it() {
        ArrayList<ArrayList<String>> al = new ArrayList<>();
        al.add(new ArrayList<>(Arrays.asList("1","2","3","4")));
        instance.pretty_it(al);
        al.add(new ArrayList<>(Arrays.asList("1","2","3")));
        instance.pretty_it(al);
    }

    /**
     * Test of getIndex method, of class Parser.
     */
    @Test
    public void testGetIndex() {
        HashSet<Parser.Pair> Goto = new HashSet<>();
        int expResult = -1;
        int result = instance.getIndex(Goto);
        assertEquals(expResult, result);
        Goto.add(new Parser.Pair("A -> c b",0));
        Parser.DFA dfa=new Parser.DFA();
        Parser.DFA dfa2=new Parser.DFA();
        dfa.rules.add(new Parser.Pair("A -> c b d",0));
        dfa2.rules.add(new Parser.Pair("A -> c b",0));
        instance.states.add(dfa);
        instance.states.add(dfa2);
        result = instance.getIndex(Goto);
        assertEquals(1, result);
    }

    /**
     * Test of print_transitions method, of class Parser.
     */
    @Test
    public void testPrint_transitions() {
		Parser.DFA dfa=new Parser.DFA();
        Parser.DFA dfa2=new Parser.DFA();
        dfa.rules.add(new Parser.Pair("A -> c b d",0));
		dfa.transitions.put("A", dfa2);
        dfa2.rules.add(new Parser.Pair("A -> c b",0));
        instance.states.add(dfa);
        instance.states.add(dfa2);
        instance.print_transitions();
    }
	
	@Test
	public void auxiliary_tests()
	{
		assertEquals(new Parser.Pair("A -> c B , $",0).toString(),"A -> . c B : $ ");
		assertEquals(new Parser.Pair("A -> c B",2).toString(),"A -> c B . ");
		assertNotEquals(new Parser.Pair("A -> c B",2),null);
		assertNotEquals(new Parser.Pair("A -> c B",2),new AugmentedFirstAndFollow());
		Parser.Pair x=new Parser.Pair("A -> c B",2);
		assertEquals(x,x);
		assertNotEquals(new Parser.Pair("A -> c B",2),new Parser.Pair("A -> c B",1));
		Parser.DFA dfa=new Parser.DFA();
		dfa.id=0;
        dfa.rules.add(new Parser.Pair("A -> c b d",0));
		assertEquals(dfa.toString(),"Id: 0 , Rules: [A -> . c b d ]\n");
        instance.read_grammar("src/test/res/MockTest.txt");//For coverage
	}
    
}
