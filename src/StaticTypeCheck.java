// StaticTypeCheck.java

import java.util.*;

// Static type checking for Clite is defined by the functions 
// V and the auxiliary functions typing and typeOf.  These
// functions use the classes in the Abstract Syntax of Clite.

public class StaticTypeCheck {
	private static int warning = 0;
	
	public static void is_declared (TypeMap tm,  Variable v, Type t) {
        if(tm.containsKey(v)) {
        	tm.replace(v, t);
        	return;
        }
        tm.put(v, t);
        return;
    }

    public static void check(boolean test, String msg) {
        if (test)  return;
        System.err.println(msg);
        System.exit(1);
    }
    
    public static void V (Program p) {
        V (p.body ,p.declarations);
    } 

    public static Type typeOf (Expression e, TypeMap tm) {
        if (e instanceof Value) return ((Value)e).type;
        if (e instanceof Variable) {
            Variable v = (Variable)e;
            check (tm.containsKey(v), "undefined variable: " + v);
            return (Type) tm.get(v);
        }
        if (e instanceof Binary) {
            Binary b = (Binary)e;
            
            Type typ1 = typeOf(b.term1,tm);
            Type typ2 = typeOf(b.term2,tm);
            if(b.op.val == "+" || b.op.val == "-" || b.op.val == "*") {//ArithmeticOp( )
                if ((typ1==Type.FLOAT || typ1==Type.INT) && (typ2==Type.FLOAT || typ2==Type.INT)) {
                	if(typ1==Type.FLOAT || typ2==Type.FLOAT) return (Type.FLOAT);
                	else return (Type.INT);
                }
                else if (b.op.val == "+" && typeOf(b.term1,tm)== Type.STRING && typeOf(b.term2,tm)== Type.STRING)
                    return (Type.STRING);
            }
            if(b.op.val == "/" || b.op.val == "%") {
            	 if ((typ1==Type.FLOAT || typ1==Type.INT) && (typ2==Type.FLOAT || typ2==Type.INT)) {
            		if(typ1==Type.FLOAT || typ2==Type.FLOAT) return (Type.FLOAT);
                 	else return (Type.INT);
            	 }
            }
            if (b.op.val ==  "&" || b.op.val == "|" || b.op.val == "<" || b.op.val == "<=" || b.op.val == "==" || b.op.val == "!=" || b.op.val == ">" || b.op.val == ">=") 
                return (Type.BOOL);
        }
        if (e instanceof Unary) {
            Unary u = (Unary)e;
            if (u.op.val == "!")        return (Type.BOOL);
            else if (u.op.val == "-")	return typeOf(u.term,tm);
            else if (u.op.val == "int") return (Type.INT);
            else if (u.op.val == "float") return (Type.FLOAT);
            else if (u.op.val == "char")  return (Type.CHAR);
            else if (u.op.val == "string")  return (Type.STRING);
        }
        throw new IllegalArgumentException("[typeOf] Expression \'" + e.toString()+ "\' has error");
    } 

    public static void V (Expression e, TypeMap tm) {
        if (e instanceof Value) 
            return;
        if (e instanceof Variable) { 
            Variable v = (Variable)e;
            check( tm.containsKey(v)
                   , "[V(E) Variable] undeclared variable: " + v);
            return;
        }
        if (e instanceof Binary) {
            Binary b = (Binary) e;
            Type typ1 = typeOf(b.term1, tm);
            Type typ2 = typeOf(b.term2, tm);
            
            V (b.term1, tm);
            V (b.term2, tm);
            
            if(b.op.val == "+" || b.op.val == "-" || b.op.val == "*") {//ArithmeticOp( )
                if ((typ1==Type.FLOAT || typ1==Type.INT) && (typ2==Type.FLOAT || typ2==Type.INT));
                else if (b.op.val == "+" && typ1== Type.STRING && typ2== Type.STRING);
                else check(false, "[V(E) Binary] type error for " + e.toString());
            }
            else if(b.op.val == "/" || b.op.val == "%") {
            	if(b.term2 instanceof Value) {
	            	if((typ1==Type.FLOAT || typ1==Type.INT) && (typ2==Type.FLOAT || typ2==Type.INT)) {
	            		if(typ2==Type.FLOAT && ((Value)b.term2).mkValue(((Value)b.term2).type()).floatValue() != 0.0);
		            	else if(typ2==Type.INT && typeOf(b.term2,tm)== Type.FLOAT && ((Value)b.term2).mkValue(((Value)b.term2).type()).intValue() != 0);
		            	else check(false, "[V(E) Binary] type error for " + e.toString());
	            	}
            	} 
            	else {
            		if((typ1==Type.FLOAT || typ1==Type.INT) && (typ2==Type.FLOAT || typ2==Type.INT));
                	else check(false, "[V(E) Binary] type error for " + e.toString());
            	}
            }
            else if (b.op.val == "<" || b.op.val == "<=" || b.op.val == "==" || b.op.val == "!=" || b.op.val == ">" || b.op.val == ">=") {
            	if(typ1 == typ2);
            	else if((typ1==Type.FLOAT || typ1==Type.INT) && (typ2==Type.FLOAT || typ2==Type.INT));
            	else check(false, "[V(E) Binary] type error for " + e.toString());
            }
            else if (b.op.val ==  "&" || b.op.val == "|") {
            	if(typ1== Type.BOOL && typeOf(b.term2,tm)== Type.BOOL);
            	else check(false, "[V(E) Binary] type error for " + e.toString());
            }
            else
                throw new IllegalArgumentException("[V(E) Binary] Binary Operator ERRER");
            return;
        }
        
        if (e instanceof Unary) {	//Check whether the Unary Expression type is bool or not
        	Unary u = (Unary) e;
        	Type type = typeOf(u.term, tm);
        	V(u.term,tm);				// 1)Check Expression is valid
        	if (u.op.val ==  "&" || u.op.val == "|") {	// 2)Check Expression type is bool
        		return;
        	}
        	else if(u.op.val=="!") {	// 2-1)Check Expression is composed of NOT
        		return;
        	}
        	else if(u.op.val=="-" && 	// 2-2)Check Expression is composed of NEG, INT
        			(type == Type.INT
        			||type == Type.FLOAT)) {
        		if(type == Type.FLOAT) {	// 2-2-1) if composed of NEG, FLOAT, implicitly convert to INT
        								// 		  and warning++;
        			System.out.println("\n[Warning]");
        			e.display(0);
        			System.out.println("implicit conversion from 'float' to 'int'");
        			warning++;
        		}
        		return;
        	}
        }
        throw new IllegalArgumentException("[V(Expression)] Expression \'" + e.toString()+ "\' has error");
    }

    public static void V (Statement s, TypeMap tm) {
        if ( s == null )
            throw new IllegalArgumentException( "AST error: null statement");
        if (s instanceof Skip) return;
        if (s instanceof Assignment) {
            Assignment a = (Assignment)s;
            V(a.source, tm);
            
            Type srctype = typeOf(a.source, tm);
            is_declared(tm,a.target,srctype);
            
            return;
        } 
        
        if (s instanceof Conditional) {		// Check the conditional statement type
        	Conditional a = (Conditional)s;
        	V(a.condition, tm);					// 조건부는 check the Expression is valid
        	V(a.thenBranch,tm);				// 실행부는 check the Block is valid
        	V(a.elseBranch,tm);
        	
            return;
        }
        if (s instanceof Loop) { 	//Check the loop statement type
        	Loop a = (Loop)s;
        	V(a.condition, tm);			// 조건부는 check the Expression is valid
        	V(a.block, tm);			// 실행부는 check the Block is valid
        	
            return;
        } 
        if (s instanceof Block) {	//Check the block statement type is valid
        	Block a = (Block)s;
        	
        	for (Statement member : a.members) //check the statements in block is valid
                V(member,tm);
            return;
        } 
        throw new IllegalArgumentException("should never reach here");
    }

    public static void main(String args[]) {
        Parser parser  = new Parser(new Lexer(args[0]));
        Program prog = parser.program();
        
        System.out.println("\nBegin type checking...");
        
        V(prog);
        
        for (Variable key : prog.declarations.keySet()) {
			System.out.println(String.format("키 : %s, 값 : %s", key, prog.declarations.get(key)));
		}
        if(warning>0) System.out.println("\nTotal :"+warning + " Warnning");
        else if(warning==0)System.out.print("perfect");
    } //main

} // class StaticTypeCheck