
package pxp171530;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * 
 * @author pushpita panigrahi - pxp171530
 * @author deeksha lakshmeesh mestha - dxm172630
 * @author sneha hulivan girisha - sxh173730
 * @author akash chand - axc173730
 *
 */

// Num class stores and performs arithmetic operations on arbitrarily large integers
public class Num  implements Comparable<Num> {

	private static HashMap<String, Integer> precedenceMap; //keeping track of the operator precedences
    static long defaultBase = 10;  // Change as needed
    long base = defaultBase;  // Change as needed
    long[] arr;  // array to store arbitrarily large integers
    boolean isNegative;  // boolean flag to represent negative numbers
    int len;  // actual number of elements of array that are used;  number is stored in arr[0..len-1]

    static {
		precedenceMap = new HashMap<>();
		precedenceMap.put("$", 0);
		precedenceMap.put("(", 1);
		precedenceMap.put("+", 2);
		precedenceMap.put("-", 2);
		precedenceMap.put("*", 3);
		precedenceMap.put("/", 3);
		precedenceMap.put("%", 3);
		precedenceMap.put("^", 4);
    }
    
	// constructor to create Num object given a string containing a number
    public Num(String s) {
		// handle negative
		if (s.charAt(0) == '-') {
			this.isNegative = true;
			s = s.substring(1);
		}
		// put the number into array with Least significant digit as arr[0]
		len = s.length();
		arr = new long[len];
		for (int i = 0; i < len; i++) {
			arr[i] = Long.parseLong(s.charAt(len - i - 1) + "");
		}
    }

	// constructor for creating Num object given a long[] in specified base
	// consumer should handle negative later
    private Num(long arr[], long base) {
		this.arr = arr;
		this.base = base;
		this.len = arr.length;
    }

	// constructor to create Num object given a long
    public Num(long x) {
		this(String.valueOf(x));
    }

	// deletes the leading zeroes for a number(or trailing zeroes in the arr) and
	// returns a new array
    private static long[] truncate(long arr[]) {
		int length = arr.length;
		boolean isZero = true;
		// If no trailing zeros
		if (arr[length - 1] != 0) {
			return arr;
		}

		for (int i = length - 1; i > 0; i--) {
			if (arr[i] != 0) {
				isZero = false;
			}
			if (arr[i] == 0 && arr[i - 1] != 0) {
				return Arrays.copyOfRange(arr, 0, i);
			}
		}
		return isZero ? Arrays.copyOfRange(arr, 0, 1) : arr;
    }

	// deletes the trailing zeroes of a Num object and updates len
	private static Num truncate(Num a) {
		a.arr = truncate(a.arr);
		a.len = a.arr.length;
		return a;
	}

	/**
	 * 
	 * @param a
	 * @param b
	 * @return a+b
	 */
    public static Num add(Num a, Num b) { 
		Num result;
		if (a.isNegative != b.isNegative) { // case b-a & a-b

			if (a.absoluteCompare(b) > 0) {
				if(a.len > b.len) { // if the arrays are of different sizes
		    		 Num paddedB = addPadding(b, a.len-b.len);
		    		 result = unsignedSubtract(a, paddedB);
				}else {
				result = unsignedSubtract(a, b);
				}
				result.isNegative = a.isNegative;
			} else {
				if(b.len > a.len) { // if the arrays are of different sizes
		    		 Num paddedA = addPadding(a, b.len-a.len);
		    		 result = unsignedSubtract(b, paddedA);
				}else
					result = unsignedSubtract(b, a);
				result.isNegative = b.isNegative;
			}
		} else {
			result = unsignedAdd(a, b);
			result.isNegative = a.isNegative;
		}
		return truncate(result);
    }

    /**
     * 
     * @param a-is a positive number
     * @param b-is a positive number
     * @return a + b 
     */
	private static Num unsignedAdd(Num a, Num b) {
		Num result;
		int size = a.len > b.len ? a.len + 1 : b.len + 1;

		long[] resultArr = new long[size];
		// perform addition
		int i = 0;
		long carry = 0, sum = 0;
		while (i < a.len && i < b.len) {
			sum = a.arr[i] + b.arr[i] + carry;
			resultArr[i] += sum % a.base;
			carry = sum / a.base;
			i++;
		}
		while (i < a.len) {
			sum = a.arr[i] + carry;
			resultArr[i] += sum % a.base;
			carry = sum / a.base;
			i++;
		}
		while (i < b.len) {
			sum = b.arr[i] + carry;
			resultArr[i] += sum % a.base;
			carry = sum / a.base;
			i++;
		}
		if (carry != 0) {
			resultArr[i] += carry;
		}
		result = new Num(truncate(resultArr), a.base);

		return truncate(result);
	}

	/**
	 * 
	 * @param a
	 * @param b
	 * @return a + b
	 */
	public static Num subtract(Num a, Num b) {// a-b
		Num result;
		if (a.isNegative != b.isNegative) { // if a and b are of different signs
			result = Num.unsignedAdd(a, b); // result isNegative is false- when b is negative.
			if (a.isNegative) {// case -a-b
				result.isNegative = true;
			} else {
				result.isNegative = false; // case a-(-b)
			}
		} else if (a.compareTo(b) >= 0) { // take care of cases where a=999 and b = 8999
			if (a.len > b.len) {
				Num paddedB = addPadding(b, a.len - b.len);
				result = unsignedSubtract(a, paddedB);
			} else {
				result = unsignedSubtract(a, b);
			}
		} else {
			if (b.len > a.len) {
				Num paddedA = addPadding(a, b.len - a.len);
				result = unsignedSubtract(b, paddedA);
			} else
				result = unsignedSubtract(b, a);
			result.isNegative = true;
		}
		return truncate(result);
	}

	// creates Num obj.arr with length = length + zeroes, by filling zero
	// returns new Num object
	private static Num addPadding(Num obj, int zeroes) {
		int totalLength = obj.len + zeroes;
		long[] temp = new long[totalLength];
		for (int i = 0; i < totalLength; i++) {
			if (i < obj.len)
				temp[i] = obj.arr[i];
			else
				temp[i] = 0;

		}
		return new Num(temp, obj.base);
    }

    /**
     * 
     * @param a-is a positive number
     * @param b-is a positive number
     * @return a - b 
     */
	private static Num unsignedSubtract(Num a, Num b) { //Assumes a and b contain equal sized arrays-> performs a-b
		int size = a.len > b.len ? a.len : b.len;
		Num result;
		long[] resultArr = new long[size];
		long[] minuend;
		minuend = new long[a.len];
		System.arraycopy(a.arr, 0, minuend, 0, a.len);
		int i = 0;
		long borrow = a.base;
		while (i < a.len && i < b.len) {
			if (minuend[i] >= b.arr[i])
				resultArr[i] = minuend[i] - b.arr[i];
			else {
				minuend[i] += borrow;
				--minuend[i + 1];
				resultArr[i] = minuend[i] - b.arr[i];
			}
			i++;
		}
		while (i < a.len) {
			resultArr[i] = minuend[i];
			i++;
		}
		result = new Num(resultArr, a.base);
		result.isNegative = a.isNegative;
		return truncate(result);
	}

	/**
	 * @param a
	 * @param b
	 * @return a * b
	 */
    public static Num product(Num a, Num b) {
		// An array parr is maintained to store the result. Products of each digit in b
		// with a is added and stored in parr. The carrySum and carryProd variables are
		// used to store the carry value while performing addition and multiplication
		// respectively.
    	long carryProd = 0, carrySum = 0,val1,val2;
		long[] parr = new long[a.len+b.len];
		
		for(int m = 0;m<b.len;m++) {
			for(int n = 0;n<a.len;n++) {
				val1 = ((b.arr[m]*a.arr[n]+carryProd)%a.base);
				carryProd = (b.arr[m]*a.arr[n]+carryProd)/a.base;
				val2 = parr[m + n] + val1 + carrySum;// adding the products
				parr[m+n] = val2%a.base;
				carrySum = val2/a.base;
				// Once all the digits in a are covered, we add the carrySum and carryProd to
				// the product value and reassign carrySum and carryProd as 0. This is done for
				// each digit in b.
				if(n==a.len-1) {
					parr[m+n+1] = parr[m+n+1] + carryProd + carrySum;
					carrySum = 0;
					carryProd = 0;
				}
			}
		}
		Num result = new Num(parr,a.base);
		if((a.isNegative && b.isNegative) || (!a.isNegative && !b.isNegative)) {
			result.isNegative = false;
		}else
		{
			result.isNegative = true;
		}
		return truncate(result);
    }

    // Use divide and conquer
    public static Num power(Num a, long n) {
		return truncate(powerHelper(a, n));
	}

	// 2^4 = (2*2)^(4/2) - only log n calls to product
	public static Num powerHelper(Num a, long n) {
		if (n == 0) {
			Num result = new Num(1);
			result.base = a.base; // 1 is 1 in any base
			return result;
		} else if (n == 1) {
			return a;
		} else if (n % 2 == 0) {
			return powerHelper(product(a, a), n / 2);
		} else {
			// if n is odd, do the same till n-1
			Num bfrResult = powerHelper(product(a, a), (n - 1) / 2);
			// extra product, final result = a * result
			return product(a, bfrResult);
		}
	}

    // Use binary search to calculate a/b
    public static Num divide(Num a, Num b) {
		Num zero = new Num(0);
		long base = a.base();
		zero.base = base;
		if (a.base != b.base)
			throw new ArithmeticException("Base of the two numbers need to be equal!!");

		if (isZero(b)) {
			return null;
		}
		Num left = zero;
		Num right = a;
		Num res = zero;
		boolean isNegative = false;
		Num comparision = new Num(b.arr, b.base);
		comparision.isNegative = true;
		// setting the sign of the result
		if ((a.isNegative == b.isNegative)) {
			isNegative = false;
		} else if (a.isNegative != b.isNegative) {
			isNegative = true;
		}
		// taking absolute value of a and b
		a.isNegative = false;
		b.isNegative = false;
		while (true) {
			Num mid = add(right, left).by2();
			Num prod = product(b, mid);
			Num diff = subtract(prod, a);
			// the difference should be between zero and the negative of the dividend
			// for the mid to be the quotient
			if (diff.compareTo(zero) <= 0 && diff.compareTo(comparision) > 0) {
				res = mid;
				res.isNegative = isNegative;
				return truncate(res);
			}
			if (a.compareTo(prod) > 0) {
				left = mid;
			} else {
				right = mid;
			}
		}
    }

	// Checks if a Num object equals to 0
	private static boolean isZero(Num a) {
		return ((a.len == 1) && (a.arr[0] == 0));
	}

    // return a%b
	// https://www.geeksforgeeks.org/program-to-find-remainder-without-using-modulo-or-operator/
	public static Num mod(Num a, Num b) throws Exception {

		// if base not equal or a modulo 0 is expected or mod of -ve number is expected
		if (a.base != b.base || a.isNegative || b.isNegative) {
			throw new java.lang.ArithmeticException();
		}

		if (isZero(b)) {
			return null;
		}
		// modulo = number - (divisor * (number / divisor))
		Num quotient = divide(a, b);
		Num multiple = product(b, quotient);
		Num remainder = subtract(a, multiple);
		return remainder;
    }

    // Use binary search
	// http://www.ardendertat.com/2012/01/26/programming-interview-questions-27-squareroot-of-a-number/
	public static Num squareRoot(Num a) throws Exception {
    		if(a.isNegative) {
			return null;
    		} 
    		
		Num zero = new Num(0);
		zero.base = a.base;
    		Num one = new Num(1);
    		one.base = a.base;

		// Binary search
		Num low = zero;
		Num high = add(one, a.by2());
		Num mid, square;
		while (add(low, one).absoluteCompare(high) < 0) {
			mid = add(high, low).by2();
			square = power(mid, 2);
			if (square.absoluteCompare(a) == 0) {
				return mid;
			} else if (square.absoluteCompare(a) < 0) {
				low = mid;
			} else {
				high = mid;
			}
    		}
		return truncate(low);
    }


    // Utility functions

	// Compares the numbers irrespective of sign - compare absolute of 2 numbers
	private int absoluteCompare(Num other) {
		int size = 0;
		if (this.len > other.len)
			return 1;
		else if (this.len < other.len)
			return -1;
		else {
			size = this.len;
			for (int i = size - 1; i >= 0; i--) {
				if (this.arr[i] > other.arr[i])
					return 1;
				else if (this.arr[i] < other.arr[i])
					return -1;
				else
					continue;
			}
		}
		return 0;
	}

    // compare "this" to "other": return +1 if this is greater, 0 if equal, -1 otherwise
    public int compareTo(Num other) { 
    	int size =0;
    	if(this.isNegative==other.isNegative) {
    		if(this.len>other.len) {
    			if(this.isNegative) // in case both are negative
    				return -1;
    			return 1;
    		}
    		else if(this.len<other.len) {
    			if(this.isNegative) // in case both are negative
    				return 1;
    			return -1;
    		}
    		else {
    			size=this.len;
		    	for(int i=size-1;i>=0;i--) {
		    		if(this.arr[i]>other.arr[i]) {
		    			if(this.isNegative) // in case both are negative
		    				return -1;
		    			return 1;
		    		}else if (this.arr[i]<other.arr[i]) {
		    			if(this.isNegative) // in case both are negative
		    				return 1;
		    			return -1;
		    		}else
		    			continue;
		    	}
    		}
    	}
    	else {
    		if(this.isNegative)
    			return -1;
    		if(other.isNegative)
    			return 1;
    	}
	return 0;
    }
    
    // Output using the format "base: elements of list ..."
    // For example, if base=100, and the number stored corresponds to 10965,
    // then the output is "100: 65 9 1"
    public void printList() {
		System.out.print(this.base + ": ");
		if (this.isNegative) {
			System.out.print('-');
		}
		for (int i = 0; i < this.len; i++) {
			System.out.print(this.arr[i] + " ");
    		}
		System.out.println();
    }
    
    // Return number to a string in base 10
    public String toString() {
		Num base10 = truncate(this);
		String retString = "";
		try {
			if (this.isNegative) {
				retString = "-";
			}
			if (this.base != 10) {
				base10 = this.convertToBase10();
			}
			for (int i = base10.len - 1; i >= 0; i--) {
				retString += base10.arr[i] + "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retString;
    }

    public long base() { return base; }

	public Num convertToBase10() throws Exception {
		// convert to decimal - horner's method
		int size = (int) Math.ceil(((this.len + 1) / Math.log10(10)) + 1);
		long[] newNumArr = new long[size];
		Num inBase10 = new Num(newNumArr, 10);
		for (int i = 0; i <= this.len - 1; i++) {
			// in base 10 so we can call the constructor
			Num part = new Num((long) (this.arr[i] * Math.pow(this.base, i)));
			inBase10 = add(inBase10, part);
		}
		return truncate(inBase10);
	}

	// Return number equal to "this" number, in base=newBase
	public Num convertBase(int newBase) throws Exception {
		Num inBase10 = this;
		Num inNewbase = this;
		// convert to decimal
		if (this.base != 10) {
			inBase10 = this.convertToBase10();
		}
		if (newBase != 10) {
			int size = (int) Math.ceil(((this.len + 1) / Math.log10(newBase)) + 1);
			long[] newNumArr = new long[size];
			inNewbase = new Num(newNumArr, newBase);
			inNewbase.isNegative = this.isNegative;// if number is negative, converted base will also be negative
			inBase10.isNegative = false; // as mod of negative is not allowed
			Num baseObj = new Num(newBase);
			int i = 0;
			while (!isZero(inBase10)) {
				Num theMod = mod(inBase10, baseObj);
				long numInNewBase = 0;
				// get the arr after mod as one long number
				for (int j = 0; j < theMod.len; j++) {
					numInNewBase = (long) (numInNewBase + theMod.arr[j] * Math.pow(10, j));
				}
				newNumArr[i] = numInNewBase;
				inBase10 = divide(inBase10, baseObj);
				i++;
			}
			this.isNegative = inNewbase.isNegative; // preserve isNegative of original object
		}
		return truncate(inNewbase); // call truncate here
	}

	// Divide by 2, for using in binary search
	public Num by2() {
		long[] newNum;
		if (this.base == 2) { // right shift once
			newNum = new long[this.len - 1];
			for (int i = 1; i < len; i++) {
				newNum[i - 1] = this.arr[i];
			}

		} else {
			// divide each digit by 2 to get the final number divided by 2
			newNum = new long[this.len];
			long carry = 0;
			for (int i = (this.len - 1); i >= 0; i--) {
				long num = (carry * this.base + this.arr[i]);
				carry = num % 2;
				newNum[i] = num / 2;
			}
		}
		return new Num(truncate(newNum), this.base);
	}


    // Evaluate an expression in postfix and return resulting number
    // Each string is one of: "*", "+", "-", "/", "%", "^", "0", or
    // a number: [1-9][0-9]*.  There is no unary minus operator.
    public static Num evaluatePostfix(String[] expr) throws Exception {
		Deque<String> stack = new ArrayDeque<String>();
		Num res;
		int i = 0;
		while (i < expr.length) {
			if (isNumber(expr[i])) {
				stack.push(expr[i]);
				i++;
			} else {
				String op1 = stack.pop();
				String op2 = stack.pop();
				Num oper1 = new Num(op1);
				Num oper2 = new Num(op2);
				String op = expr[i];
				i++;
				//mod,subtract and divide are asymetric operations
				switch (op) {
				case "+":
					res = add(oper1,oper2);
					break;
				case "-":
					res = subtract(oper2,oper1);
					break;
				case "*":
					res = product(oper1,oper2);
					break;
				case "/":
					res = divide(oper2, oper1);
					break;
				case "%":
					res = mod(oper2, oper1);
					break;
				case "^":
					res = power(oper1, Long.parseLong(op2));
					break;
				default:
					res = null;
				}
				stack.push(res + "");
			}
		}
		return new Num(stack.pop());
    }

    // Evaluate an expression in infix and return resulting number
    // Each string is one of: "*", "+", "-", "/", "%", "^", "(", ")", "0", or
    // a number: [1-9][0-9]*.  There is no unary minus operator.
    public static Num evaluateInfix(String[] expr) throws Exception {
		String[] postFix = InfixToPostfix(expr);
		return evaluatePostfix(postFix);
    }


    private static String[] InfixToPostfix(String[] expr) {
		Stack<String> opStack = new Stack<>();
		opStack.push("$"); // base of the stack to avaid handling empty stack case
		Queue<String> outputQueue = new LinkedList<>();
		int len = expr.length;

		for (int i = 0; i < len; i++) {
			String token = expr[i];
			if (isNumber(token)) { // Number is pushed into the queue
				outputQueue.offer(token);
			} else {
				if (token.equals("("))
					opStack.push(token);
				else if (token.equals(")")) {
					while (!opStack.peek().equals("(")) {
						outputQueue.offer(opStack.pop());
					}
					opStack.pop();
				}
				else if (comparePrecedence(token, opStack.peek())) {
					// if op1 has a higher precedence than top of stack, push op1 into the stack
					opStack.push(token);
				} else {
					// if op1 has lower precedence than top of stack, pop stack until op1 gets the
					// higher precedence than the top of stack
					while (!comparePrecedence(token, opStack.peek())) {
						outputQueue.offer(opStack.pop());
					}
					opStack.push(token);
				}
			}
		}

		while (!opStack.peek().equals("$")) {
			outputQueue.offer(opStack.pop() + "");
		}

		int numElements = outputQueue.size();
		String[] postfixArr = outputQueue.toArray(new String[numElements]);
		return postfixArr;
			
	}

	// Unary - is not supported and checked, so we check if if a number is 0 or >0
	// Ex: 08 does not qualify as a number, enter 8 instead
	private static boolean isNumber(String token) {
		String regex = "[1-9][0-9]*";
		if (token.matches(regex) || token.equals("0")) {
			return true;
		}
		return false;
	}

	private static boolean comparePrecedence(String op1, String op2) {
		//if op1 has a higher precedence than op2
		if (precedenceMap.get(op1) > precedenceMap.get(op2)) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) throws Exception {

		Num x = new Num("36666669999999999999");
		Num y = new Num("272");

		System.out.println("Add");
		Num z = Num.add(x, y);
		System.out.println(z.toString());

		System.out.println("Subtract");
		Num z1 = subtract(x, y);
		System.out.println(z1.toString());

		System.out.println("Power");
		Num a = Num.power(x, 8);
		System.out.println(a.toString());

		System.out.println("Product");
		Num b = Num.product(x, y);
		b.printList();
		System.out.println(b.toString());
		
		System.out.println("Divide");
		Num m = Num.divide(x, y);
		System.out.println(m);
		

		System.out.println("by2");
		Num y1 = new Num("999");
		Num by = y1.by2();
		by.printList();

		System.out.println("power");
		Num a1 = new Num(2);
		a1 = power(a1, 3);
		a1.printList();

		System.out.println("SquareRoot");
		Num c = new Num(16);
		c = squareRoot(c);
		c.printList();

		System.out.println("Mod");
		Num d = new Num("9999999999999999999999");
		Num d1 = new Num("36666669");
		d = mod(d, d1);
		d.printList();
		System.out.println(d.toString());

		System.out.println("Convert base");
		Num e = new Num(55);
		Num e1 = e.convertBase(2);
		e1.printList();

		System.out.println("Product in base 12");
		Num f = new Num(20);
		Num f1 = f.convertBase(12);
		f1 = product(f1, f1);
		f1.printList();
		System.out.println(f1.toString());

		System.out.println("Convert base and add");
		Num g = new Num(-1000);
		Num gg = new Num(12);
		System.out.println(add(g, gg).toString());
		Num g1 = g.convertBase(60);
		g.printList();
		g1.printList();
		Num gg1 = gg.convertBase(60);
		gg1.printList();
		System.out.println(add(g, gg).toString());
		System.out.println(add(g1, gg1).toString());

		System.out.println("Convert base and sqrt");
		Num h = new Num(7328);
		Num hh = h.convertBase(16);
		System.out.println(squareRoot(hh).toString());

		System.out.println("Convert base and divide");
		Num i = new Num(7356);
		Num ii = i.convertBase(16);
		Num j = new Num(37);
		Num jj = j.convertBase(16);
		System.out.println(divide(i, j).toString());
		divide(ii, jj).printList();
		System.out.println(divide(ii, jj).toString());
		
		System.out.println("Evaluate Infix expression");
		String []expr = {"(","2","+","3","*","5","*","6",")","+","7","*","8","/","4","+","19","/","29","%","3"}; 
		System.out.println(Num.evaluateInfix(expr));

		String[] inf = { "24", "+", "30", "/", "(", "15", "%", "6", ")", "+", "20", "-", "70" };
		System.out.println(evaluateInfix(inf).toString());

		String[] inf1 = { "24", "*", "30", "/", "(", "15", "%", "6", ")", "*", "20", "-", "70" };
		System.out.println(evaluateInfix(inf1).toString());

		String[] inf2 = { "(", "24", "*", "30", ")", "/", "15", "%", "6", "*", "20", "-", "70" };
		System.out.println(evaluateInfix(inf2).toString());

		String[] inf3 = { "24", "+", "30", "/", "15", "%", "6", "*", "20", "-", "70" };
		System.out.println(evaluateInfix(inf3).toString());
		
		String[] inf4 = { "(", "5", "+", "40", ")", "/", "15", "%", "6", "*", "20", "-", "70" };
		System.out.println(evaluateInfix(inf4).toString());

		Num xx = new Num("08");
		System.out.println(divide(xx, xx).toString());

		Num xx1 = new Num("0");
		System.out.println(squareRoot(xx1).toString());

		Num xy = new Num("00008");
		System.out.println(xy.toString());

    }
}