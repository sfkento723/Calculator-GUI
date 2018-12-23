import java.util.Scanner;
import java.util.Stack;

// Initially the operand length was limited to 32 bits.
// However, this prevents us from calculating QWORD functions,
// so they have been replaced with long integers to allow for up to 64 bit calculation.

public class CalculatorEngine
{
	private String post = "";
	private Stack<String> operators = new Stack<String>();
	private Stack<Long> operands = new Stack<Long>();

	/* Checks if some token is an operator
	 * @param token the token to check
	 * @return if it is an operator
	 */
	private boolean operatorCheck(String token)
	{
		if (token.equals("Mod") || token.equals("x") || token.equals("/") ||
				token.equals("+") || token.equals("-"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/* Checks precedence of operators
	 * @param token the token to check
	 * @return precedence level of operator
	 */
	
	private int precedence(String token)
	{
		if (token.equals("Mod") || token.equals("x") || token.equals("/"))
		{
			return 1;
		}
		else 
		{
			return 0;
		}
	}
	
	/* Evaluates an expression via Shunting Yard and Reverse Polish Algorithms
	 * @param input the expression received
	 * @return the result of the expression
	 */
	
	public String evaluate(String input)
	{
		// No undefined allowed
		if (input.contains("/ 0") || input.contains("Mod 0"))
		{
			return "Undefined";
		}
		Scanner scan = new Scanner(input); // Scans the tokens
		// Pushes tokens to appropriate Stack
		while (scan.hasNext())
		{
			String token = scan.next();
			if (!(operatorCheck(token)))
			{
				if (token.equals("("))
				{
					operators.push(token);
				}
				else if (token.equals(")"))
				{
					if (operatorCheck(operators.peek()))
					{
						post += operators.pop() + " ";
					}
					else
					{
						operators.pop();
					}
				}
				else
				{
					post = post + token + " ";
				}
			}
			else
			{
				boolean again = true;
				while (again)
				{
					if (operators.isEmpty())
					{
						operators.push(token);
						again = false;
					}
					else if (operators.equals("("))
					{
						operators.push(token);
						again = false;
					}
					else if (precedence(token) > precedence(operators.peek()))
					{
						operators.push(token);
						again = false;
					}
					else
					{
						String pop = operators.pop();
						post = post + pop + " ";
					}
				}
			}
		}
		// Pops and empties the operator Stack and builds a String with it
		while (!(operators.isEmpty()))
		{
			if (operatorCheck(operators.peek()))
			{
				post = post + operators.pop() + " ";
			}
			else
			{
				operators.pop();
			}
		}
		
		Scanner look = new Scanner(post);
		
		for (int k = 0; k < post.length(); k++)
		{
			if (post.charAt(k) == '(')
			{
				int y = post.indexOf("(");
				post = post.substring(0, y) + post.substring(y + 1);
			}
		}
		
		look.close();
		scan.close();
		// Reverse Polish part
		Scanner scan2 = new Scanner(post);
		while (scan2.hasNext())
		{
			// Evaluate here
			String token = scan2.next();
			if (!(operatorCheck(token)))
			{
				Long toAdd = Long.parseLong(token);
				operands.push(toAdd);
			}
			else
			{
				Long op1 = operands.pop();
				Long op2 = operands.pop();
				Long toAdd;
				// Various cases to consider
				switch (token)
				{
					case "+": 
						toAdd = op1 + op2;
						operands.push(toAdd);
						break;
					case "-":
						toAdd = op2 - op1;
						operands.push(toAdd);
						break;
					case "x":
						toAdd = op1 * op2;
						operands.push(toAdd);
						break;
					case "/":
						if (op1 == 0)
						{
							return "Infinity";
						}
						else
						{
							toAdd = op2 / op1;
							operands.push(toAdd);
							break;
						}
					case "Mod":
						if (op1 == 0)
						{
							return "Infinity";
						}
						else
						{
							toAdd = op2 % op1;
							operands.push(toAdd);
							break;
						}
					default:
						break;
				}
			}
		}
		scan2.close();
		Long toSend = operands.pop();
		return Long.toString(toSend);
	}
}