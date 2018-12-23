public class Converter
{
	public static String hexConverter(String hex, int radix)
	{
		
		int sum = 0;
		int toAdd;
		char at = ' ';
		int mult = 0;
		
		for (int k = hex.length() - 1; k >= 0; k--)
		{
			if (radix == 10)
			{
				at = hex.charAt(k);
				switch (at)
				{
					case 'A': 
					toAdd  = (int) (10 * Math.pow(16, mult));
					mult++;
					sum += toAdd;
					break;
					case 'B': 
					toAdd  = (int) (11 * Math.pow(16, mult));
					mult++;
					sum += toAdd;
					break;
					case 'C': 
					toAdd  = (int) (12 * Math.pow(16, mult));
					mult++;
					sum += toAdd;
					break;
					case 'D': 
					toAdd  = (int) (13 * Math.pow(16, mult));
					mult++;
					sum += toAdd;
					break;
					case 'E': 
					toAdd  = (int) (14 * Math.pow(16, mult));
					mult++;
					sum += toAdd;
					break;
					case 'F': 
					toAdd  = (int) (15 * Math.pow(16, mult));
					mult++;
					sum += toAdd;
					break;
					default:
					int parse = Integer.parseInt(hex.substring(k, k + 1));
					toAdd = (int) (parse * Math.pow(16, mult));
					mult++;
					sum += toAdd;
					break;
				}
			}
		}
		String toSend = Long.toString(sum);
		return toSend;
	}
	/* Creates a octal String
	 * @param oct the string to convert
	 * @return an octal string
	 */
	
	public static String octConverter(String oct)
	{
		int sum = 0; 
		int toAdd; 
		int mult = 0;
		for (int k = oct.length() - 1; k >= 0; k--)
		{
			int parse = Integer.parseInt(oct.substring(k, k + 1));
			toAdd = (int) (parse * Math.pow(8, mult));
			mult++;
			sum += toAdd;
		}
		String toSend = Long.toString(sum);
		return toSend;
	}
	
	/* Creates a binary String 
	 * @param bin the String to convert
	 * @return the binary String
	 */
	
	public static String binConverter(String bin)
	{
		int sum = 0; 
		int toAdd; 
		int mult = 0;
		for (int k = bin.length() - 1; k >= 0; k--)
		{
			int parse = Integer.parseInt(bin.substring(k, k + 1));
			toAdd = (int) (parse * Math.pow(2, mult));
			mult++;
			sum += toAdd;
		}
		String toSend = Long.toString(sum);
		return toSend;
	}
	
	/* Used to count parenthesis (but can count other characters as well)
	 * @param count String to count
	 * @param c the character to look at
	 * @return character count in String
	 */
	
}