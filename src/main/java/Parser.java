import java.util.*;
import java.io.*;
/**
 * @author Aditya Pal
 * Base class that is inherited by all other parsers
 * @version 0.1
 */
public class Parser
{
    /**
     * Maps non_terminals to productions
     * @since 0.1
     */
    HashMap<String,ArrayList<ArrayList<String>>> rules;
     /**
     * Stores terminal Symbols
     * @since 0.1
     */
    HashSet<String> terminals;
     /**
     * Stores nonterminal Symbols
     * @since 0.1
     */
    HashSet<String> nonTerminals;
     /**
     * Stores all Symbols
     * @since 0.1
     */
    ArrayList<String> allSymbols;
     /**
     * Stores the parsing table
     * @since 0.1
.     */
    ArrayList<String> table[][];
    /**
    * startSymbol: Stores start symbol
    * @since 0.1
    */ 
    String startSymbol;
     /**
     * Stores first and follow for the grammar
     * @since 0.1
     */
    AugmentedFirstAndFollow utils;
    
    ArrayList<DFA> states;
    /**
    * dfa: Stores the first DFA as a reference<br>
    */
    DFA dfa;
    int minId;
    /**
     * @since 0.1
     * Models how a production <b>item</b> looks (String with dot)
     */
    static class Pair
    {
        String rule;
        int dot;
        /**
         * @param rule: Stores the production rule
         * @param dot: Remembers dot position
         */
        Pair(String rule,int dot)
        {
            this.rule=rule;
            this.dot=dot;
        }
        @Override
        public String toString()
        {
            int l=rule.indexOf("->"),i;
            String left=rule.substring(0,l).trim();
            String right=rule.substring(l+2).trim();
            StringBuilder sb=new StringBuilder();
            sb.append(left).append(" -> ");
            String tokens[]=right.split(" ");
            for(i=0;i<tokens.length;i++)
            {               
                if(tokens[i].equals(","))
                {
                    sb.append(": ");
                    continue;
                }
                if(i==dot)
                    sb.append(". ");
                sb.append(tokens[i]).append(" ");
            }
            if(i==dot)
                sb.append(". ");
            return sb.toString();
        }
        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + rule.hashCode();
            result = prime * result + dot;
            return result;
        }       
        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Pair other = (Pair) obj;
            if (dot!= other.dot)
                return false;
            return rule.equals(other.rule);
        }
    }/**
     * @since 0.1
     * Stores the Models <b>automata</b> states
     */
    static class DFA
    {
        /**
         * @param rules: Stores all the rules of the automata
         * @param id: Unique integer id for a state
         * @param transitions: Maps which state to shift to on processing an input symbol
         */
        HashSet<Pair> rules;
        int id;
        HashMap<String, DFA> transitions;
        DFA()
        {
            rules=new HashSet<>();
            id=-1;
            transitions=new HashMap<>();
        }
        @Override
        public String toString()
        {
            return "Id: "+id+" , Rules: "+rules+"\n";//Mapping: "+transitions+"\n\n";
        }
    }
    /**
     * Returns a Parser object for use<br><p>
     * rules: Maps non_terminals to productions. HashMap of type String, ArrayList of ArrayList of Strings<br>
     * terminals: HashSet of Strings, stores terminal Symbols<br>
     * nonTerminals: HashSet of Strings, stores non terminal Symbols<br>
     * allSymbols: Stores all Symbols<br>
     * table: Stores the parsing table<br>
     * startSymbol: Stores start symbol<br>
     * states: Stores all states of the automata<br>
     * dfa: Stores the first DFA as a reference<br>
     * minId: minimum id for any dfa.<br>
     * @since 0.1<br>
     */
    public Parser()
    {
        rules=new HashMap<>();       
        startSymbol="";
        terminals=new HashSet<>();
        nonTerminals=new HashSet<>();
        allSymbols=new ArrayList<>();
        minId=-1;
        dfa=new DFA();
        utils=new AugmentedFirstAndFollow();
        states=new ArrayList<>();
    }
    /**
     * Returns the id for a dfa to use
     * @return Integer
     * @since 0.1
     */
    protected int getId()
    {
        return ++minId;
    }
    /**
     * Modifies the utils object to allow it to compute first and follow for augmented grammar
     * @param utils: The object of AugmentedFirstAndFollow class to change
     * @since 0.1
     */
    void modify(AugmentedFirstAndFollow utils)
    {
        utils.ntCount.clear();
        utils.ntCount.addAll(nonTerminals);
        utils.tCount.clear();
        utils.tCount.addAll(terminals);
        utils.tCount.add("$");
        utils.ntCount.add(startSymbol+"'");
    }
    /**
     * Sets the value of terminals, non-terminals, rules and initializes the utils object<br>
     * Expects file in the format<br>
     * Line 1: All terminals<br>
     * Line 2: All nonterminals<br>
     * Lines 3-n: Grammar rules, with tokens separated by spaces. Use | for 'OR' rules, "@" for epsilon<p>
     * Either parses the Grammar file successfully, or exits with error code -1
     * @param filePath: Accepts the path to grammar file<br>
     * @since 0.1
     */
    public void read_grammar(String filePath)
    {
        String str="";
        int line=0;
        try
        {
            BufferedReader br=new BufferedReader(new FileReader(filePath));
            str=br.readLine();
            String _terminals[]=str.split(" ");
            for(int i=0;i<_terminals.length;i++)
                terminals.add(_terminals[i]);
            str=br.readLine();
            String _nonTerminals[]=str.split(" ");
            for(int i=0;i<_nonTerminals.length;i++)
                nonTerminals.add(_nonTerminals[i]);
            allSymbols.addAll(terminals);
            allSymbols.addAll(nonTerminals);
            while((str=br.readLine())!=null)//Read the string, put in map
            {
                int l=str.indexOf("->"),i,j;
                String left=str.substring(0,l).trim();
                String right=str.substring(l+2).trim();
                l=right.length();
                String tokens[]=right.split("[|]");
                if(line==0)
                {
                    startSymbol=left;
                }
                line++;
                ArrayList<ArrayList<String>> al=new ArrayList<>();
                for(i=0;i<tokens.length;i++)
                {
                    String s[]=tokens[i].trim().split(" ");
                    ArrayList<String> temp=new ArrayList<>();
                    for(j=0;j<s.length;j++)
                        temp.add(s[j]);
                    al.add(temp);
                }
                rules.put(left,al);//Put all the rules in the map
                line++;
            }
            br.close();
            augment();
            modify(utils);
            unAugment();
            utils.module1(filePath,true,false);
        }catch(Exception e)
        {
            System.out.println("Couldn't read grammar file: "+e.getMessage());
            System.exit(-1);
        }
    }
    /**
     * Returns a concatenated version of the ArrayList<br>
     * Transforms ["Aditya","Pal"] to "Aditya Pal" (delim: " "), and "Aditya,Pal" (delim:",")<br><p>
     * @param v: An ArrayList of Strings, to be concatenated
     * @param delim: Character to be inserted in middle of strings<br>
     * @return String
     * @since 0.1
     */
    public String join(ArrayList<String> v, String delim) 
    {
        if(v==null||v.isEmpty())
            return "";
        StringBuilder ss=new StringBuilder();
        for(int i = 0; i < v.size(); ++i)
        {
            if(i != 0)
                ss.append(delim);
            ss.append(v.get(i));
        }
        return ss.toString();
    }
    /**
     * Performs closure computation on the given set (LR0/SLR style computation)
     * @param closure: A HashSet, whose closure is to be computed
     * @since 0.1
     */
    public void getClosure(HashSet<Pair> closure)
    {
        boolean done=false;
        while(!done)
        {
            done=true;
            Iterator iterator=closure.iterator();
            HashSet<Pair> addAble=new HashSet<>();
            while(iterator.hasNext())
            {
                Pair pair=(Pair)iterator.next();
                int l=pair.rule.indexOf("->"),i;
                String left=pair.rule.substring(0,l).trim();
                String right=pair.rule.substring(l+2).trim();
                String tokens[]=right.split(" ");
                if(pair.dot>=tokens.length||pair.dot<0)
                    continue;
                else if(nonTerminals.contains(tokens[pair.dot]))
                {
                    ArrayList<ArrayList<String>> al=rules.get(tokens[pair.dot]);
                    for(i=0;i<al.size();i++)
                    {
                        String str=join(al.get(i)," ");
                        Pair p=new Pair(tokens[pair.dot]+" -> "+str,0);
                        if(!closure.contains(p))
                        {
                            done=false;
                            addAble.add(new Pair(tokens[pair.dot]+" -> "+str.trim(),0));
                        }
                    }
                }
            }
            closure.addAll(addAble);
        }
    }
     /**
     * Performs goto computation on the given set (LR0/SLR style computation)
     * @param X: A HashSet, whose GoTo is to be computed
     * @param I: The string symbol based on which Goto will be evaluated
     * @return  HashSet&lt;Pair&gt;
     * @see Pair
     * @since 0.1
     */
    public HashSet<Pair> getGoto(HashSet<Pair> X, String I)
    {
        HashSet<Pair> goTo=new HashSet<>();
        HashSet<Pair> add=new HashSet<>();
        X.forEach((p) -> {
            String str[]=p.rule.substring(p.rule.indexOf("->")+2).trim().split(" ");
            if (!(p.dot>=str.length||p.dot<0)) {
                if (str[p.dot].equals(I)) {
                    goTo.add(new Pair(p.rule,p.dot+1));
                }
            }
        });
        goTo.stream().map((p) -> {
            HashSet<Pair> temp=new HashSet<>();
            temp.add(p);
            return temp;
        }).map((temp) -> {
            getClosure(temp);
            return temp;
        }).forEachOrdered((temp) -> {
            add.addAll(temp);
        });
        goTo.addAll(add);
        return goTo;
    }
     /**
     * Augments the grammar by adding extra symbol in front of start symbol, and adding that to rules
     * @since 0.1
     */
    public void augment()
    {
        ArrayList<ArrayList<String>> al=new ArrayList<>();
        ArrayList<String> al2=new ArrayList<>();
        al2.add(startSymbol);
        al.add(al2);
        rules.put(startSymbol+"'",al);
    }
    /**
     * De-augments the grammar produced by augment() call
     * @since 0.1
     * @see augment
     */
    public void unAugment()
    {
        rules.remove(startSymbol+"'");
    }
     /**
     * Parses the string <p>
     * @param _toParse: Input String
     * @param output: Option for switching on/off the stack display
     * @return  boolean
     * @since 0.1
     */
    public boolean parse(String _toParse, boolean output)
    {
        _toParse=_toParse+" $";
        Stack<String> stack=new Stack<>();
        ArrayList<ArrayList<String>> al=new ArrayList<>();
        ArrayList<String> a=new ArrayList<>();
        a.add("Step");
        a.add("Stack");
        a.add("Action");
        a.add("Input");
        al.add(a);
        stack.push("$");
        stack.push("0");
        int pointer=0,i,step=0;
        String toParse[]=_toParse.split(" ");
        while(!stack.empty()&&pointer<toParse.length)
        {
            a=new ArrayList<>();
            int row=Integer.parseInt(stack.peek());
            int col=allSymbols.indexOf(toParse[pointer]);
            a.add(step+"");
            a.add(stack+"");
            if(col<0)
            {
                a.add("Parse error");
                al.add(a);
                if(output)
                    pretty_it(al);
                break;
            }
            if(table[row][col].size()==0)
            {
                a.add("Parse error");
                al.add(a);
                if(output)
                    pretty_it(al);
                break;
            }
            String action=table[row][col].get(0);
            a.add(action);
            String str[]=action.split(" ");
            String left=str[0].trim();
            String right="";
            for(i=1;i<str.length;i++)
                right+=str[i]+" ";
            right=right.trim();
            if(left.equals("Shift"))
            {
                stack.push(toParse[pointer]);
                stack.push(right);
                pointer++;
            }
            else if(left.equals("Reduce"))
            {
                left=right.substring(0,right.indexOf("->")).trim();
                right=right.substring(right.indexOf("->")+2).trim();
                if(right.charAt(0)!='@')
                {
                    for(i=0;i<2*(str.length-3);i++)
                    {
                        if(stack.size()!=0)
                            stack.pop();
                        else
                        {
                            a.add("Parse error");
                            al.add(a);
                            if(output)
                                pretty_it(al);
                            return false;
                        }
                    }
                }
                int top=Integer.parseInt(stack.peek());
                stack.push(left);                
                stack.push(table[top][allSymbols.indexOf(left)].get(0));
            }
            else if(left.equals("Accept")&&pointer==toParse.length-1)
            {
                String ppp="";
                for(i=pointer;i<toParse.length;i++)
                    ppp+=toParse[i]+" ";
                a.add(ppp);
                al.add(a);
                if(output)
                {
                    pretty_it(al);
                    System.out.println("Woohoo, accepted :) ");
                }
                return true;
            }
            String pp="";
            for(i=pointer;i<toParse.length;i++)
                pp+=toParse[i]+" ";
            a.add(pp);
            al.add(a);
            step++;
        }
        return false;
    }
    /**
     * Utility to use the PrettyPrinter class
     * @param al: The arraylist of arraylists to be converted into 2d array
     * @since 0.1
     */
    protected void pretty_it(ArrayList<ArrayList<String>> al)
    {
        int n=al.size(),i,j;
        String t[][]=new String[n][4];
        for(i=0;i<n;i++)
        {
            for(j=0;j<4;j++)
            {
                if(al.get(i).size()==3&&j==3)
                    t[i][j]="Parse Error";
                else
                    t[i][j]=al.get(i).get(j);
            }
        }
        PrettyPrinter printer = new PrettyPrinter(System.out);
        printer.print(t);
    }
     /**
     * Finds the index of current state, or returns -1 if it is a new state
     * @param Goto: A HashSet, whose index is to be found
     * @return int
     * @since 0.1
     */
    protected int getIndex(HashSet<Pair> Goto)
    {
        int i=0;
        for(DFA dfa: states)
        {
            if((dfa.rules.containsAll(Goto)&&Goto.containsAll(dfa.rules)))
                return i;
            i++;
        }
        return -1;
    }
    /**
     * Prints the transitions in the current DFA
     * @since 0.1
     */
    protected void print_transitions()
    {
        states.stream().map((dfa) -> {
            System.out.println("Map for state: "+dfa);
            return dfa;
        }).map((dfa) -> {
            System.out.println("Transitions: ");
            return dfa;
        }).map((dfa) -> {
            for(Map.Entry<String, DFA> mp:dfa.transitions.entrySet())
            {
                System.out.println(mp.getKey()+"->"+mp.getValue().rules+" ( S"+getIndex(mp.getValue().rules)+" )");
            }
            return dfa;
        }).forEachOrdered((_item) -> {
            System.out.println();
        });
    }
}