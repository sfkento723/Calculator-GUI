import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Stack;

public class Calculator extends JFrame implements ActionListener, ComponentListener
{
	// Stuff to be used
	
	private CalculatorEngine engine = new CalculatorEngine();
	
	// Screen dimensions
	private Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
	private double width = size.getWidth();
	private double height = size.getHeight();
		
	private GridLayout grid = new GridLayout(5, 6, 5, 5); // Button grid
	
	// Bottom comment
	private JPanel bottom;
	private JPanel buttons; 
	private JPanel binOps; 
	
	// Top comment
	private JPanel top;
	private JPanel bases;

	private JPanel word;
	private JPanel high;
	
	// To display what has been input already
	private JTextField equation;
	
	// Essentially to track the same as above, but in various formatting
	private String hEquation = "";
	private String dEquation = "";
	private String oEquation = "";
	private String bEquation = "";

	// Text fields to hold user input
	private JTextField text;
	private JTextField displayText;
	
	// Giant PROGRAMMER text at the top of the screen
	private JTextField fullTop;
	
	// Various fonts used
	private  Font other = new Font("Segoe UI", Font.BOLD, 14);
	private  Font f = new Font("Segoe UI", Font.BOLD, 20);
	private  Font but = new Font("Arial", Font.BOLD, 20);
	private  Font gray = new Font("Segoe UI", Font.PLAIN, 12);

	// Control the 4 inputs that can be used for bases
	private JTextField[] baseText = new JTextField[4];
	private JTextField[] display = new JTextField[5];
	
	// Holds the bases and ensures only one is selected
	private ButtonGroup holder;
	
	// Arrays of buttons used (text)
	private String[] vals = {"", "Mod", "CE", "C", "", "", "A", "B",
			"7", "8", "9", "x", "C", "D", "4", "5", "6", "-",
			"E", "F", "1", "2", "3", "+", "(", ")", "",
			"0", ".", "="};
	private String[] binVals = {"Lsh", "Rsh", "Or", "Xor", "Not", "And"};
	
	private String[] changeVals = {"HEX", "DEC", "OCT", "BIN"};
		
	// Trigram text at the top left corner
	private JButton topLeft;
	
	// Arrays for the buttons themselves
	private JButton[] botP =  new JButton[30];
	private JButton[] topP = new JButton[6];
	private JButton[] modes = new JButton[5];
	
	// Counts the usage of the BYTE, DWORD, QWORD, and WORD
	private int counter = 0;
	
	// Actually changes the bases
	private JToggleButton[] change = new JToggleButton[4];
	
	// Used to run expression evaluations 
	private String eval = "  ";
	private String post = "";
	private String result;
	private Stack<String> operators = new Stack<String>();
	private Stack<Integer> operands = new Stack<Integer>();
	
	// Tracks if equals was the last pressed button
	private boolean equalsLast = false;
	
	// Constructor
	Calculator()
	{
		constructCalc();
	}
	
	private void constructCalc()
	{
		// Builds the JFrame portion
		this.setTitle("Windows 10 Calculator Recreation");
		this.setLayout(new GridBagLayout());
		GridBagConstraints main = new GridBagConstraints();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
		this.addComponentListener(this);
		
		// Top half of the calculator (with 5 components that contain more components)
		top = new JPanel();
		top.setLayout(new GridBagLayout());
		
		// Creates the top portion of the calculator (the button and Programmer text)
		high = new JPanel(new GridBagLayout());
		high.setBackground(new Color(242, 242, 242));
		topLeft = new JButton(new ImageIcon("TopRightTrigram.png")); // Image used over text
		topLeft.setPreferredSize(new Dimension((int) (width / 40) + 10, (int) (width / 40 + 10)));
		topLeft.setBackground(new Color(242, 242, 242));
		topLeft.setBorder(BorderFactory.createEmptyBorder());
		fullTop = new JTextField("  PROGRAMMER");
		fullTop.setBackground(new Color(242, 242, 242));
		fullTop.setBorder(BorderFactory.createEmptyBorder());
		fullTop.setPreferredSize(new Dimension(200 , (int) (width / 40 + 10)));
		fullTop.setEditable(false);
		fullTop.setFont(new Font("Segoe UI", Font.BOLD, 23));
		GridBagConstraints highGbc = new GridBagConstraints();
		highGbc.gridx = 0;
		highGbc.gridy = 0;
		highGbc.weighty = 1.0;
		highGbc.weightx = .0;
		highGbc.anchor = GridBagConstraints.NORTHWEST;
		highGbc.fill = GridBagConstraints.NONE;
		high.add(topLeft, highGbc);
		highGbc.gridx = 1;
		highGbc.weightx = .02;
		high.add(fullTop, highGbc);
		
		// Creates a constraints to add the panel
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = .2;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.BOTH;
		top.add(high, gbc);
		
		// Creates the actual display to work with
		word = new JPanel(new GridBagLayout());
		GridBagConstraints characters = new GridBagConstraints();
		text = new JTextField("0");
		// text.setBackground(new Color(242, 242, 242));
		displayText = new JTextField("0");
		displayText.setBackground(new Color(242, 242, 242));
		
		// Second part that keeps track of the expression
		equation = new JTextField();
		
		equation.setEditable(false);
		equation.setHorizontalAlignment(SwingConstants.RIGHT);
		equation.setBorder(BorderFactory.createEmptyBorder());
		equation.setBackground(new Color(242, 242, 242));
		equation.setFont(gray);
		characters.gridx = 0;
		characters.gridy = 0;
		characters.weightx = 1;
		characters.weighty = .2;
		characters.fill = GridBagConstraints.BOTH;
		word.add(equation, characters);
		
		/*text.setFont(f);
		text.setEditable(false);
		text.setHorizontalAlignment(SwingConstants.RIGHT);
		text.setBorder(BorderFactory.createEmptyBorder());
		characters.gridy = 1;
		characters.weighty = .8;*/ // No longer used as it is part of background operations now
		displayText.setFont(f);
		displayText.setEditable(false);
		displayText.setHorizontalAlignment(SwingConstants.RIGHT);
		displayText.setBorder(BorderFactory.createEmptyBorder());
		characters.gridy = 1;
		characters.weighty = .8;
		word.add(displayText, characters);
		
		// Adds it on to main panel
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 1.0;
		gbc.weighty = .9;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.fill = GridBagConstraints.BOTH;
		top.add(word, gbc);
		
		// Holds the bases and the toggles
		bases = new JPanel(new GridBagLayout());
		GridBagConstraints edit = new GridBagConstraints();
		edit.gridx = 0;
		edit.gridy = 0;
		edit.weightx = .025;
		edit.weighty = .25;
		edit.insets = new Insets(0,0,0,0);
		edit.anchor = GridBagConstraints.WEST;
		holder = new ButtonGroup();
		
		UIManager.put("ToggleButton.select", new Color(242, 242, 242)); // changes the selection color of the toggle buttons
		
		// Adds all buttons w. attributes via a loop
		for (int k = 0; k < change.length; k++)
		{
			JToggleButton x = new JToggleButton(changeVals[k]);
			x.setBackground(new Color(230, 230, 230));
			x.setBorder(BorderFactory.createEmptyBorder());
			x.setMargin(new Insets(0, 0, 0, 0));
			x.setFont(new Font("Segoe UI", Font.BOLD, 15));
			holder.add(x);
			change[k] = x;
			if (k == 1)
			{
				x.setForeground(new Color(0, 128, 255));
				x.setSelected(true);
			}
			x.setBackground(new Color(242, 242, 242));
			x.addActionListener(this);
			bases.add(x, edit);
			edit.gridy = edit.gridy + 1;
		}
		
		edit.gridx = 1;
		edit.gridy = 0;
		edit.weightx = .975;
		edit.fill = GridBagConstraints.HORIZONTAL;
		
		// Adds a corresponding text field to the toggle buttons
		for (int k = 0; k < display.length; k++)
		{
			JTextField toAdd = new JTextField("0");
			toAdd.setBackground(new Color(242, 242, 242));
			toAdd.setEditable(false);
			toAdd.setBorder(BorderFactory.createEmptyBorder());
			toAdd.setFont(other);
			display[k] = toAdd;
			if (k == 1)
			{
				toAdd.setForeground(new Color(0, 128, 255));
			}
			bases.add(toAdd, edit);
			edit.gridy = edit.gridy + 1;
		}
		
		display[4].setText("");
		
		// Adds the textfields that are not formatted, but used for operational purposes
		for (int k = 0; k < baseText.length; k++)
		{
			JTextField toAdd = new JTextField("0");
			toAdd.setEditable(false);
			baseText[k] = toAdd;
		}
		 
		// Add to main
		gbc.insets = new Insets(0, 15, 0, 5);
		gbc.gridy = 2;
		gbc.weighty = .3;
		bases.setBackground(new Color(242, 242, 242));
		top.add(bases, gbc);
		
		// Adds the row of buttons at the top that use images, such as the binary toggle
		gbc.fill = GridBagConstraints.BOTH;
		bottom = new JPanel(new GridBagLayout());
		GridBagConstraints stuff = new GridBagConstraints();
		stuff.gridx = 0;
		stuff.gridy = 0;
		stuff.weightx = 0;
		stuff.weighty = .1;
		stuff.insets = new Insets(5, 3, 0, 0);
		// Assigns properties to the buttons, such as text and images
		for (int k = 0; k < modes.length; k++)
		{
			JButton x;
			if (k == 0)
			{
				ImageIcon cMode = new ImageIcon("CalcMode.png");
				x = new JButton(cMode);
			}
			else if (k == 1)
			{
				ImageIcon bMode = new ImageIcon("BinaryMode.png");
				x = new JButton(bMode);
			}
			else if (k == 2)
			{
				stuff.weightx = 0;
				x = new JButton("QWORD");
				modes[2] = x;
				x.addActionListener(this);
				x.setFont(new Font("Segoe UI", Font.BOLD, 15));
				x.setForeground(new Color(0, 128, 255));
			}
			else if (k == 3)
			{
				stuff.weightx = 0;
				x = new JButton("MS");
			}
			else
			{
				ImageIcon Ms = new ImageIcon("Ms.png");
				x = new JButton(Ms);
			}
			x.setBackground(new Color(242, 242, 242));
			x.setPreferredSize(new Dimension(60, 30));
			x.setMaximumSize(new Dimension(60, 60));
			x.setMinimumSize(new Dimension(60, 30));
			if (k == 2)
			{
				stuff.weightx = .1;
				x.setPreferredSize(new Dimension(100, 30));
				x.setMaximumSize(new Dimension(100, 60));
				x.setMinimumSize(new Dimension(100, 30));
			}
			x.setBorder(BorderFactory.createEmptyBorder());
			bottom.add(x, stuff);
			if (k == 0)
			{
				stuff.gridy = 1;
				stuff.fill = GridBagConstraints.HORIZONTAL;
				stuff.insets = new Insets(0, 5, 0, 0);
				stuff.weighty = .000001;
				JPanel blue = new JPanel();
				blue.setPreferredSize(new Dimension(60, 2));
				blue.setMaximumSize(new Dimension(60, 2));
				blue.setMinimumSize(new Dimension(60, 2));
				blue.setBackground(new Color(16, 125, 213));
				bottom.add(blue, stuff);
				stuff.gridy = 0; 
				stuff.weighty = .1;
			}
			stuff.gridx++;
			stuff.weightx = 0;
		}
				
		// Adds on the first set of buttons
		gbc.gridy = 3;
		gbc.weighty = .1;
		gbc.insets =  new Insets(0, 0, 0, 0);
		bottom.setMaximumSize(new Dimension(bottom.getWidth(), 35));
		bottom.setBackground(new Color(242, 242, 242));
		bottom.setBorder(BorderFactory.createLineBorder(new Color(228, 228, 228)));
		top.add(bottom, gbc);
		
		// Adds on binary operators (and, or, xor, etc.)
		binOps = new JPanel(new GridLayout(1, 6, 5, 10));
		binOps.setBackground(new Color(242, 242, 242));
		for (int k = 0; k < binVals.length; k++)
		{
			JButton x = new JButton(binVals[k]);
			x.setFont(f);
			x.setBackground(new Color(242, 242, 242));
			x.setBorder(BorderFactory.createEmptyBorder());
			binOps.add(x);
			topP[k] = x;
		}
		
		// Adds binary operators on
		gbc.insets = new Insets(0, 0, 0, 0);
		binOps.setBorder(BorderFactory.createLineBorder(new Color(228, 228, 228)));
		gbc.gridy = 4;
		gbc.weighty = .7;
		top.add(binOps, gbc);
		
		// Adds 1st main panel onto the frame
		main.gridx = 0;
		main.gridy = 0;
		main.fill = GridBagConstraints.BOTH;
		main.weightx = 1;
		main.weighty = .32;
		
		this.add(top, main);
		
		//Buttons on bottom half of calculator
		
		buttons = new JPanel(grid);
		buttons.setBackground(new Color(230, 230, 230));
		for (int k = 0; k < botP.length; k++)
		{
			JButton x;
			if (k == 4)
			{
				ImageIcon del = new ImageIcon("Delete.png");
				x = new JButton(del);
			}
			else if (k == 5)
			{
				ImageIcon div = new ImageIcon("Divide.png");
				x = new JButton(div);
			}
			else if (k == 26)
			{
				ImageIcon pm = new ImageIcon("PlusMinus.png");
				x = new JButton(pm);
			}
			else if (k == 0)
			{
				ImageIcon arrow = new ImageIcon("UpArrow.png");
				x = new JButton(arrow);
			}
			else
			{
				x = new JButton(vals[k]);
			}
			x.setFont(but);
			x.addActionListener(this);
			if (k == 6 || k == 7 || k == 12 || k == 13 || k == 18 || k == 19 || k == 28)
			{
				x.setEnabled(false);
			}
			if (k > 5 && k < 11 || k > 11 && k < 17 || k > 17 && k < 23
					|| k == 27)
			{
				x.setBackground(new Color(250, 250, 250));// 12 - 16, 18 - 22, 24 - 28, 33 
			}
			else
			{
				x.setBackground(new Color(240, 240, 240));
			}
			x.setBackground(new Color(230, 230, 230));
			x.setBorder(BorderFactory.createEmptyBorder());
			botP[k] = x;
			buttons.add(x);
		}
				
		
		buttons.setVisible(true);
		
		// Final addition of buttons
		main.gridy = 1;
		main.weighty = .2;
		main.insets = new Insets(0, 5, 0, 5);
		this.add(buttons, main);
		this.setMinimumSize(new Dimension((int) (width * .2175), (int) (height * .5875)));
		this.setSize((int) (width * .2175), (int) height * 2 / 3);
		
		this.setVisible(true);
	}

	
	/* Used to count parenthesis (but can count other characters as well)
	 * @param count String to count
	 * @param c the character to look at
	 * @return character count in String
	 */
	private int pCounter(String count, char c)
	{
		int counter = 0;
		for (int k = 0; k < count.length(); k++)
		{
			if (count.charAt(k) == c)
			{
				counter++;
			}
		}
		return counter;
	}
	
	@Override
	/* Performs actions based on events received
	 *  @param e the event received by the ActionListener
	 */
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		// Checks to see if it's undefined and removes if it is (no ops allowed with undefined)
		if (text.getText().equals("Undefined"))
		{
			baseText[0].setText("0");
			baseText[1].setText("0");
			baseText[2].setText("0");
			baseText[3].setText("0");
			text.setText("0");
		}
		// All the bases are checked to see which is selected
		if (e.getSource().equals(change[0]))
		{
			for (int k = 0; k < botP.length; k++)
			{
				if (k != 28)
				{
					botP[k].setEnabled(true);
				}
			}
			text.setText(baseText[0].getText());
			change[0].setForeground(new Color(0, 128, 255));
			display[0].setForeground(new Color(0, 128, 255));
			for (int k = 0; k < 4; k++)
			{
				if (k != 0)
				{
					change[k].setForeground(new Color(0, 0, 0));
					display[k].setForeground(new Color(0, 0, 0));
				}
			}
			equation.setText(hEquation);
		}
		else if (e.getSource().equals(change[1]))
		{
			for (int k = 0; k < botP.length; k++)
			{
				if (k == 6 || k == 7 || k == 12 || k == 13 || k == 18 || k == 19 || k == 28)
				{
					botP[k].setEnabled(false);
				}
				else
				{
					botP[k].setEnabled(true);
				}
			}
			change[1].setForeground(new Color(0, 128, 255));
			display[1].setForeground(new Color(0, 128, 255));
			for (int k = 0; k < 4; k++)
			{
				if (k != 1)
				{
					change[k].setForeground(new Color(0, 0, 0));
					display[k].setForeground(new Color(0, 0, 0));
				}
			}
			text.setText(baseText[1].getText());
			equation.setText(dEquation);
		}
		else if (e.getSource().equals(change[2]))
		{
			for (int k = 0; k < botP.length; k++)
			{
				if ((k >= 6 && k <= 10 && k != 8) || k == 12 || k == 13 || k == 18 || k == 19 || k == 28)
				{
					botP[k].setEnabled(false);
				}
				else
				{
					botP[k].setEnabled(true);
				}
			}
			change[2].setForeground(new Color(0, 128, 255));
			display[2].setForeground(new Color(0, 128, 255));
			for (int k = 0; k < 4; k++)
			{
				if (k != 2)
				{
					change[k].setForeground(new Color(0, 0, 0));
					display[k].setForeground(new Color(0, 0, 0));
				}
			}
			text.setText(baseText[2].getText());
			equation.setText(oEquation);
		}
		else if (e.getSource().equals(change[3]))
		{
			for (int k = 0; k < botP.length; k++)
			{
				if ((k >= 6 && k <= 10) || (k >= 12 && k <= 16) ||
						(k >= 18 && k <= 22 && k != 20) || k == 28)
				{
					botP[k].setEnabled(false);
				}
				else
				{
					botP[k].setEnabled(true);
				}
			}
			change[3].setForeground(new Color(0, 128, 255));
			display[3].setForeground(new Color(0, 128, 255));
			for (int k = 0; k < 4; k++)
			{
				if (k != 3)
				{
					change[k].setForeground(new Color(0, 0, 0));
					display[k].setForeground(new Color(0, 0, 0));
				}
			}
			text.setText(baseText[3].getText());
			equation.setText(bEquation);
		}

		// Now to check the bottom half of the display to see if it's selected (text buttons only)
		else if (e.getSource().equals(botP[6]) || e.getSource().equals(botP[7]) || e.getSource().equals(botP[8])
				|| e.getSource().equals(botP[9]) || e.getSource().equals(botP[10]) || e.getSource().equals(botP[12])
				|| e.getSource().equals(botP[13]) || e.getSource().equals(botP[14]) || e.getSource().equals(botP[15])
				|| e.getSource().equals(botP[16]) || e.getSource().equals(botP[18]) || e.getSource().equals(botP[19])
				|| e.getSource().equals(botP[20]) || e.getSource().equals(botP[21]) || e.getSource().equals(botP[22])
				|| e.getSource().equals(botP[27]))
		{
			if (change[0].isSelected())
			{
				if (equalsLast)
				{
					text.setText("0");
					baseText[0].setText("0");
					baseText[1].setText("0");
					baseText[2].setText("0");
					baseText[3].setText("0");
					equalsLast = false; // Removes padded 0s
				}
				hexOperations(e);
			}
			else if (change[1].isSelected())
			{
				if (equalsLast)
				{
					text.setText("0");
					baseText[0].setText("0");
					baseText[1].setText("0");
					baseText[2].setText("0");
					baseText[3].setText("0");
					equalsLast = false; // Removes padded 0s
				}
				decimalOperations(e);
			}
			else if (change[2].isSelected())
			{
				if (equalsLast)
				{
					text.setText("0");
					baseText[0].setText("0");
					baseText[1].setText("0");
					baseText[2].setText("0");
					baseText[3].setText("0");
					equalsLast = false; // Removes padded 0s
				}
				octOperations(e);
			}
			else
			{
				if (equalsLast)
				{
					text.setText("0");
					baseText[0].setText("0");
					baseText[1].setText("0");
					baseText[2].setText("0");
					baseText[3].setText("0");
					equalsLast = false; // Removes padded 0s
				}
				binOperations(e);
			}
		}
		// Start of operations (Clearing)
		else if (e.getSource().equals(botP[2]) || e.getSource().equals(botP[3]))
		{
			text.setText("0");
			baseText[0].setText("0");
			baseText[1].setText("0");
			baseText[2].setText("0");
			baseText[3].setText("0");
			if (e.getSource().equals(botP[3]))
			{
				equation.setText("");
				hEquation = "";
				dEquation = "";
				oEquation = "";
				bEquation = "";
				eval = "  ";
			}
		}
		// Negations
		else if (e.getSource().equals(botP[26]))
		{
			if (baseText[1].getText().contains("-"))
			{
				baseText[1].setText(baseText[1].getText().replace("-", ""));
				Long y = Long.parseLong(baseText[1].getText());
				baseText[0].setText(Long.toHexString(y).toUpperCase());
				baseText[2].setText(Long.toOctalString(y));
				baseText[3].setText(Long.toBinaryString(y));
				if (change[0].isSelected())
				{
					text.setText(baseText[0].getText());
				}
				else if (change[1].isSelected())
				{
					text.setText(baseText[1].getText());
				}
				else if (change[2].isSelected())
				{
					text.setText(baseText[2].getText());
				}
				else if (change[3].isSelected())
				{
					text.setText(baseText[3].getText());
				}
			}
			else
			{
				if (baseText[1].getText().equals("0") || baseText[1].getText().equals(""))
				{
					
				}
				else
				{
					baseText[1].setText("-" + baseText[1].getText());
					Long y = Long.parseLong(baseText[1].getText());
					baseText[0].setText(Long.toHexString(y).toUpperCase());
					baseText[2].setText(Long.toOctalString(y));
					baseText[3].setText(Long.toBinaryString(y));
				}

				if (change[0].isSelected())
				{
					text.setText(baseText[0].getText());
				}
				else if (change[1].isSelected())
				{
					text.setText(baseText[1].getText());
				}
				else if (change[2].isSelected())
				{
					text.setText(baseText[2].getText());
				}
				else if (change[3].isSelected())
				{
					text.setText(baseText[3].getText());
				}
			}
			

		}
		// Back Space and erasure 
		else if (e.getSource().equals(botP[4]))
		{
			if (text.getText().length() == 1 || (text.getText().length() == 2 && text.getText().contains("-")))
			{
				text.setText("0");
				baseText[0].setText("0");
				baseText[1].setText("0");
				baseText[2].setText("0");
				baseText[3].setText("0");
			}
			else
			{
				text.setText(text.getText().substring(0, text.getText().length() - 1));
				if (change[0].isSelected())
				{
					baseText[1].setText(Converter.hexConverter(text.getText(), 10));
					Long y = Long.parseLong(baseText[1].getText());
					baseText[0].setText(Long.toHexString(y).toUpperCase());
					baseText[2].setText(Long.toOctalString(y));
					baseText[3].setText(Long.toBinaryString(y));
				}
				else if (change[1].isSelected())
				{
					baseText[1].setText(text.getText());
					Long y = Long.parseLong(baseText[1].getText());
					baseText[0].setText(Long.toHexString(y).toUpperCase());
					baseText[2].setText(Long.toOctalString(y));
					baseText[3].setText(Long.toBinaryString(y));
				}
				else if (change[2].isSelected())
				{
					baseText[1].setText(Converter.octConverter(text.getText()));
					Long y = Long.parseLong(baseText[1].getText());
					baseText[0].setText(Long.toHexString(y).toUpperCase());
					baseText[2].setText(Long.toOctalString(y));
					baseText[3].setText(Long.toBinaryString(y));
				}
				else if (change[3].isSelected())
				{
					baseText[1].setText(Converter.binConverter(text.getText()));
					Long y = Long.parseLong(baseText[1].getText());
					baseText[0].setText(Long.toHexString(y).toUpperCase());
					baseText[2].setText(Long.toOctalString(y));
					baseText[3].setText(Long.toBinaryString(y));
				}
			}
		}
		// Shift Button
		else if (e.getSource().equals(botP[0]))
		{
			if (topP[0].getText().equals("Lsh"))
			{
				topP[0].setText("RoL");
				topP[1].setText("RoR");
			}
			else
			{
				topP[0].setText("Lsh");
				topP[1].setText("Rsh");
			}
			
		}
		// Modular arithmatic
		else if (e.getSource().equals(botP[1]))
		{
			hEquation = hEquation + display[0].getText() + " " + botP[1].getText() + " ";
			dEquation = dEquation + display[1].getText() + " " + botP[1].getText() + " ";
			oEquation = oEquation + display[2].getText() + " " + botP[1].getText() + " ";
			bEquation = bEquation + display[3].getText() + " " + botP[1].getText() + " ";
			if (change[0].isSelected())
			{
				equation.setText(hEquation);
			}
			else if (change[1].isSelected())
			{
				equation.setText(dEquation);
			}
			else if (change[2].isSelected())
			{
				equation.setText(oEquation);
			}
			else
			{
				equation.setText(bEquation);
			}
			eval = eval + " " + baseText[1].getText() + " " + botP[1].getText();
			text.setText("0");
			baseText[0].setText("0");
			baseText[1].setText("0");
			baseText[2].setText("0");
			baseText[3].setText("0");
		}
		// Division
		else if (e.getSource().equals(botP[5]))
		{
			hEquation = hEquation + display[0].getText() + " "  + "/ ";
			dEquation = dEquation + display[1].getText() + " "  + "/ ";
			oEquation = oEquation + display[2].getText() + " "  + "/ ";
			bEquation = bEquation + display[3].getText() + " "  + "/ ";
			if (change[0].isSelected())
			{
				equation.setText(hEquation);
			}
			else if (change[1].isSelected())
			{
				equation.setText(dEquation);
			}
			else if (change[2].isSelected())
			{
				equation.setText(oEquation);
			}
			else
			{
				equation.setText(bEquation);
			}
			eval = eval + " " + baseText[1].getText() + " / ";
			
			
			text.setText("0");
			baseText[0].setText("0");
			baseText[1].setText("0");
			baseText[2].setText("0");
			baseText[3].setText("0");
		}
		// Multiplication
		else if (e.getSource().equals(botP[11]))
		{
			hEquation = hEquation + display[0].getText() + " " + botP[11].getText() + " ";
			dEquation = dEquation + display[1].getText() + " " + botP[11].getText() + " ";
			oEquation = oEquation + display[2].getText() + " " + botP[11].getText() + " ";
			bEquation = bEquation + display[3].getText() + " " + botP[11].getText() + " ";
			if (change[0].isSelected())
			{
				equation.setText(hEquation);
			}
			else if (change[1].isSelected())
			{
				equation.setText(dEquation);
			}
			else if (change[2].isSelected())
			{
				equation.setText(oEquation);
			}
			else
			{
				equation.setText(bEquation);
			}
			eval = eval + " " + baseText[1].getText() + " " + botP[11].getText();
			text.setText("0");
			baseText[0].setText("0");
			baseText[1].setText("0");
			baseText[2].setText("0");
			baseText[3].setText("0");
		}
		// Subtraction
		else if (e.getSource().equals(botP[17]))
		{
			hEquation = hEquation + display[0].getText() + " " + botP[17].getText() + " ";
			dEquation = dEquation + display[1].getText() + " " + botP[17].getText() + " ";
			oEquation = oEquation + display[2].getText() + " " + botP[17].getText() + " ";
			bEquation = bEquation + display[3].getText() + " " + botP[17].getText() + " ";
			if (change[0].isSelected())
			{
				equation.setText(hEquation);
			}
			else if (change[1].isSelected())
			{
				equation.setText(dEquation);
			}
			else if (change[2].isSelected())
			{
				equation.setText(oEquation);
			}
			else
			{
				equation.setText(bEquation);
			}
			eval = eval + " " + baseText[1].getText() + " " + botP[17].getText();
			text.setText("0");
			baseText[0].setText("0");
			baseText[1].setText("0");
			baseText[2].setText("0");
			baseText[3].setText("0");
		}
		// Addition
		else if (e.getSource().equals(botP[23]))
		{
			hEquation = hEquation + display[0].getText() + " " + botP[23].getText() + " ";
			dEquation = dEquation + display[1].getText() + " " + botP[23].getText() + " ";
			oEquation = oEquation + display[2].getText() + " " + botP[23].getText() + " ";
			bEquation = bEquation + display[3].getText() + " " + botP[23].getText() + " ";
			if (change[0].isSelected())
			{
				equation.setText(hEquation);
			}
			else if (change[1].isSelected())
			{
				equation.setText(dEquation);
			}
			else if (change[2].isSelected())
			{
				equation.setText(oEquation);
			}
			else
			{
				equation.setText(bEquation);
			}
			eval = eval + " " + baseText[1].getText() + " " + botP[23].getText();
			text.setText("0");
			baseText[0].setText("0");
			baseText[1].setText("0");
			baseText[2].setText("0");
			baseText[3].setText("0");
		}
		// Left Parenthesis
		else if (e.getSource().equals(botP[24]))
		{
			String eq = equation.getText();
			if (eq.equals("") || eq.charAt(eq.length() - 2) == 'x' || eq.charAt(eq.length() - 2) == '-'
					|| eq.charAt(eq.length() - 2) == '+' || eq.charAt(eq.length() - 2) == '/' || eq.charAt(eq.length() - 2) == 'd'
					|| eq.charAt(eq.length() - 2) == '(')
			{
				hEquation = hEquation  + " " + botP[24].getText() + " ";
				dEquation = dEquation  + " " + botP[24].getText() + " ";
				oEquation = oEquation  + " " + botP[24].getText() + " ";
				bEquation = bEquation  + " " + botP[24].getText() + " ";
				if (change[0].isSelected())
				{
					equation.setText(hEquation);
				}
				else if (change[1].isSelected())
				{
					equation.setText(dEquation);
				}
				else if (change[2].isSelected())
				{
					equation.setText(oEquation);
				}
				else
				{
					equation.setText(bEquation);
				}
				eval = eval +  " " + botP[24].getText();
				text.setText("0");
				baseText[0].setText("0");
				baseText[1].setText("0");
				baseText[2].setText("0");
				baseText[3].setText("0");
			}
		}
		// Right Parenthesis
		else if (e.getSource().equals(botP[25]))
		{
			String eq = equation.getText();
			int lPCount = pCounter(eq, '(');
			int rPCount = pCounter(eq, ')');
			if (lPCount > rPCount && !(eq.equals("")) && (eq.charAt(eq.length() - 2) != 'x' | eq.charAt(eq.length() - 2) != '-'
					|| eq.charAt(eq.length() - 2) != '+' || eq.charAt(eq.length() - 2) != '/' 
					|| eq.charAt(eq.length() - 2) != 'd'))
			{
				hEquation = hEquation + display[0].getText() + " " + botP[25].getText() + " ";
				dEquation = dEquation + display[1].getText() + " " + botP[25].getText() + " ";
				oEquation = oEquation + display[2].getText() + " " + botP[25].getText() + " ";
				bEquation = bEquation + display[3].getText() + " " + botP[25].getText() + " ";
				if (change[0].isSelected())
				{
					equation.setText(hEquation);
				}
				else if (change[1].isSelected())
				{
					equation.setText(dEquation);
				}
				else if (change[2].isSelected())
				{
					equation.setText(oEquation);
				}
				else
				{
					equation.setText(bEquation);
				}
				eval = eval + " " + baseText[1].getText() + " " + botP[25].getText();
				text.setText("");
				baseText[0].setText("");
				baseText[1].setText("");
				baseText[2].setText("");
				baseText[3].setText("");
			}
		}
		// Equals
		else if (e.getSource().equals(botP[29]))
		{
			if (eval.charAt(0) == '0')
			{
				eval = eval.substring(1);
			}
			String check = "";
			
			int lP = pCounter(eval, '('); // count left parenthesis
			int rP = pCounter(eval, ')'); // count right
			
			// If the number is uneven, balance it out
			while (lP > rP)
			{
				eval += " ) ";
				rP++;
			}
			// Evaluate all parenthesis one at a time
			while (lP > 0)
			{
				if (eval.contains("("))
				{
					check = eval.substring(eval.lastIndexOf('('), (eval.indexOf(')', eval.lastIndexOf('(')) + 1));
					check = engine.evaluate(check);
					eval = eval.substring(0, eval.lastIndexOf('(')) + " " + check + " " + 
					eval.substring(eval.indexOf(')', eval.lastIndexOf('(') + 1));
					lP--;
				}
			}
			// Remove all parenthesis remaining
			while (eval.contains(")"))
			{
				eval = eval.substring(0, eval.indexOf(")")) + eval.substring(eval.indexOf(")") + 1);
			}
			
			// Check undefined
			eval = eval + " " + baseText[1].getText(); // Add on the final operand
			if (eval.contains("Undefined"))
			{
				result = "Undefined";
			}
			else
			{
				result = engine.evaluate(eval); // Evaluate fully
			}
			equation.setText("  ");
			hEquation = "";
			dEquation = "";
			oEquation = "";
			bEquation = "";
			
			// Check again for undefined
			if (result.contains("Infinity"))
			{
				result = "Undefined";
			}
			
			eval = "  "; // Reset the evaluation
			baseText[1].setText(result);
			// No parsing if it is undefined
			if (result.contains("Undefined"))
			{
				baseText[0].setText("Undefined");
				baseText[2].setText("Undefined");
				baseText[3].setText("Undefined");
			}
			else
			{
				Long y = Long.parseLong(result);
				baseText[0].setText(Long.toHexString(y).toUpperCase());
				baseText[2].setText(Long.toOctalString(y));
				baseText[3].setText(Long.toBinaryString(y));
			}
			if (change[0].isSelected())
			{
				text.setText(baseText[0].getText());
			}
			else if (change[1].isSelected())
			{
				text.setText(baseText[1].getText());
			}
			else if (change[2].isSelected())
			{
				text.setText(baseText[2].getText());
			}
			else if (change[3].isSelected())
			{
				text.setText(baseText[3].getText());
			}
			equalsLast = true;
				
		}
		// Mode changer (Essentially just caps off expressions at certain marks)
		else if (e.getSource().equals(modes[2]))
		{
			counter++;
			if (counter == 4)
			{
				counter = 0;
			}
			if (counter == 0)
			{
				modes[2].setText("QWORD");
			}
			else if (counter == 1)
			{
				modes[2].setText("DWORD");
			}
			else if (counter == 2)
			{
				modes[2].setText("WORD");
				long y = Long.parseLong(baseText[1].getText());
				if (y > 65535)
				{
					baseText[1].setText("65535");
					baseText[0].setText(Long.toHexString(y).toUpperCase());
					baseText[2].setText(Long.toOctalString(y));
					baseText[3].setText(Long.toBinaryString(y));
				}
			}
			else
			{
				modes[2].setText("BYTE");
				long y = Long.parseLong(baseText[1].getText());
				if (y > 255)
				{
					baseText[1].setText("255");
					baseText[0].setText(Long.toHexString(y).toUpperCase());
					baseText[2].setText(Long.toOctalString(y));
					baseText[3].setText(Long.toBinaryString(y));
					if (change[0].isSelected())
					{
						displayText.setText(display[0].getText());
					}
					else if (change[1].isSelected())
					{
						displayText.setText(display[1].getText());
					}
					else if (change[2].isSelected())
					{
						displayText.setText(display[2].getText());
					}
					else
					{
						displayText.setText(display[2].getText());
					}
				}
			}
		}
		// Format it to look nicer 
		StringBuilder hex = new StringBuilder(baseText[0].getText());
		StringBuilder dec = new StringBuilder(baseText[1].getText());
		StringBuilder oct = new StringBuilder(baseText[2].getText());
		StringBuilder bin = new StringBuilder(baseText[3].getText());
		
		for (int k = hex.length() - 4; k > 0; k-= 4)
		{
			hex.insert(k, " ");
		}
		
		for (int k = dec.length() - 3; k > 0; k-= 3)
		{
			dec.insert(k, ",");
		}
		
		for (int k = oct.length() - 3; k > 0; k-= 3)
		{
			oct.insert(k, " ");
		}
		
		for (int k = bin.length() - 4; k > 0; k-= 4)
		{
			bin.insert(k, " ");
		}
		
		for (int i = 0; (i < 5) && (i < bin.length()); i++)
		{
			if (bin.charAt(i) == ' ')
			{
				for (int k = 0; k < 4 - i; i++)
				{
					bin.insert(0,  '0');
				}
			}
		}
		
		String h = hex.toString();
		String d = dec.toString();
		String o = oct.toString();
		String b = bin.toString();
		
		display[0].setText(h);
		display[1].setText(d);
		display[2].setText(o);
		
		if(b.length() > 39)
		{
			display[3].setText(b.substring(0, 39));
			display[4].setText(b.substring(40, b.length()));
		}
		else
		{
			display[3].setText(b);
			display[4].setText("");
		}
		
		// Setting the final box
		
		if (change[0].isSelected())
		{
			displayText.setText(display[0].getText());
			if (modes[2].getText().equalsIgnoreCase("WORD") && !(text.equals("")))
			{
				long y = Long.parseLong(baseText[1].getText());
				if (y > 65535)
				{
					displayText.setText("FFFF");
					display[0].setText("FFFF");
					display[1].setText("65535");
					display[2].setText("177 513");
					display[3].setText("1111 1111 1111 1111");
				}
			}
			else if (modes[2].getText().equalsIgnoreCase("BYTE") && !(text.equals("")))
			{
				long y = Long.parseLong(baseText[1].getText());
				if (y > 255)
				{
					displayText.setText("FF");
					display[0].setText("FF");
					display[1].setText("255");
					display[2].setText("377");
					display[3].setText("1111 1111");
				}
			}
		}
		else if (change[1].isSelected())
		{
			displayText.setText(display[1].getText());
			if (modes[2].getText().equalsIgnoreCase("WORD") && !(text.equals("")))
			{
				long y = Long.parseLong(baseText[1].getText());
				if (y > 65535)
				{
					displayText.setText("65535");
					display[0].setText("FFFF");
					display[1].setText("65535");
					display[2].setText("177 513");
					display[3].setText("1111 1111 1111 1111");
				}
			}
			else if (modes[2].getText().equalsIgnoreCase("BYTE") && !(text.equals("")))
			{
				long y = Long.parseLong(baseText[1].getText());
				if (y > 255)
				{
					displayText.setText("255");
					display[0].setText("FF");
					display[1].setText("255");
					display[2].setText("377");
					display[3].setText("1111 1111");
				}
			}
		}
		else if (change[2].isSelected())
		{
			displayText.setText(display[2].getText());
			if (modes[2].getText().equalsIgnoreCase("WORD") && !(text.equals("")))
			{
				long y = Long.parseLong(baseText[1].getText());
				if (y > 65535)
				{
					displayText.setText("177 513");
					display[0].setText("FFFF");
					display[1].setText("65535");
					display[2].setText("177 513");
					display[3].setText("1111 1111 1111 1111");
				}
			}
			else if (modes[2].getText().equalsIgnoreCase("BYTE") && !(text.equals("")))
			{
				long y = Long.parseLong(baseText[1].getText());
				if (y > 255)
				{
					displayText.setText("371");
					display[0].setText("FF");
					display[1].setText("255");
					display[2].setText("377");
					display[3].setText("1111 1111");
				}
			}
		}
		else if (change[3].isSelected())
		{
			displayText.setText(display[3].getText());
			if (modes[2].getText().equalsIgnoreCase("WORD") && !(text.equals("")))
			{
				long y = Long.parseLong(baseText[1].getText());
				if (y > 65535)
				{
					displayText.setText("1111 1111 1111 1111");
					display[0].setText("FFFF");
					display[1].setText("65535");
					display[2].setText("177 513");
					display[3].setText("1111 1111 1111 1111");
				}
			}
			else if (modes[2].getText().equalsIgnoreCase("BYTE") && !(text.equals("")))
			{
				long y = Long.parseLong(baseText[1].getText());
				if (y > 255)
				{
					displayText.setText("1111 1111");
					display[0].setText("FF");
					display[1].setText("255");
					display[2].setText("377");
					display[3].setText("1111 1111");
				}
			}
		}

		
	}
	
	
	/* Creates a binary numerical input 
	 * @param e the input received
	 */
	private void binOperations(ActionEvent e) {
		// TODO Auto-generated method stub
		if (text.getText().length() < 32 && (modes[2].getText().equalsIgnoreCase("QWORD") || modes[2].getText().equalsIgnoreCase("DWORD")))
		{
			for (int k = 0; k < botP.length; k++)
			{
				if (e.getSource().equals(botP[k]) && ((k >= 8 && k <= 10) ||
						(k >= 14 && k <= 16) || (k >= 20 && k <= 22) || k == 27))
				{
					if (text.getText().equals("0"))
					{
						try 
						{
							text.setText(botP[k].getText());
							Long y = Long.parseLong(botP[k].getText());
							baseText[0].setText(Integer.toHexString((y.intValue())).toUpperCase());
							baseText[1].setText(Integer.toString((y.intValue())));
							baseText[2].setText(Integer.toOctalString((y.intValue())));
							
							String temp = insertNewLine(Integer.toBinaryString(y.intValue()));
							
							if (temp.length() > 32)
							{
								baseText[3].setText(insertNewLine(temp.substring(0, 32)));
								baseText[4].setText(temp.substring(32, temp.length()));
							}
							break;
						}
						catch (NumberFormatException ex)
						{
							
						}
					}
					else
					{
						try
						{
							text.setText(text.getText() + botP[k].getText());
							baseText[3].setText(text.getText());
							baseText[1].setText(Converter.binConverter(text.getText()));
							Long y = Long.parseLong(baseText[1].getText());
							baseText[2].setText(Long.toOctalString(y));
							baseText[0].setText(Long.toHexString(y).toUpperCase());
						}
						catch (NumberFormatException ex)
						{
							
						}
				}
				}
			}
		}
		else if (text.getText().length() < 16 && modes[2].getText().equalsIgnoreCase("WORD"))
		{
			for (int k = 0; k < botP.length; k++)
			{
				if (e.getSource().equals(botP[k]) && ((k >= 8 && k <= 10) ||
						(k >= 14 && k <= 16) || (k >= 20 && k <= 22) || k == 27))
				{
					if (text.getText().equals("0"))
					{
						try 
						{
							text.setText(botP[k].getText());
							Long y = Long.parseLong(botP[k].getText());
							baseText[0].setText(Integer.toHexString((y.intValue())).toUpperCase());
							baseText[1].setText(Integer.toString((y.intValue())));
							baseText[2].setText(Integer.toOctalString((y.intValue())));
							baseText[3].setText(Integer.toBinaryString((y.intValue())));
							break;
						}
						catch (NumberFormatException ex)
						{
						}
					}
					else
					{
						try
						{
							text.setText(text.getText() + botP[k].getText());
							baseText[3].setText(text.getText());
							baseText[1].setText(Converter.binConverter(text.getText()));
							Long y = Long.parseLong(baseText[1].getText());
							baseText[2].setText(Long.toOctalString(y));
							baseText[0].setText(Long.toHexString(y).toUpperCase());
						}
						catch (NumberFormatException ex)
						{
						}
					}
				}
			}
		}
		else if (text.getText().length() < 8 && modes[2].getText().equalsIgnoreCase("BYTE"))
		{
			for (int k = 0; k < botP.length; k++)
			{
				if (e.getSource().equals(botP[k]) && ((k >= 8 && k <= 10) ||
						(k >= 14 && k <= 16) || (k >= 20 && k <= 22) || k == 27))
				{
					if (text.getText().equals("0"))
					{
						try 
						{
							text.setText(botP[k].getText());
							Long y = Long.parseLong(botP[k].getText());
							baseText[0].setText(Integer.toHexString((y.intValue())).toUpperCase());
							baseText[1].setText(Integer.toString((y.intValue())));
							baseText[2].setText(Integer.toOctalString((y.intValue())));
							baseText[3].setText(Integer.toBinaryString((y.intValue())));
							break;
						}
						catch (NumberFormatException ex)
						{
							
						}
					}
					else
					{
						try
						{
							text.setText(text.getText() + botP[k].getText());
							baseText[3].setText(text.getText());
							baseText[1].setText(Converter.binConverter(text.getText()));
							Long y = Long.parseLong(baseText[1].getText());
							baseText[2].setText(Long.toOctalString(y));
							baseText[0].setText(Long.toHexString(y).toUpperCase());
						}
						catch (NumberFormatException ex)
						{
							
						}
					}
				}
			}
		}
	}

	/* Creates a hex numerical input
	 * @param e the input received
	 */
	private void hexOperations(ActionEvent e)
	{
		if (text.getText().length() < 16 && modes[2].getText().equalsIgnoreCase("QWORD"))
		{
			for (int k = 0; k < botP.length; k++)
			{
				if (e.getSource().equals(botP[k]) && ((k >= 6 && k <= 10) ||
			(k >= 12 && k <= 16) || (k >= 18 && k <= 22)|| k == 27))
				{
					if (text.getText().equals("0"))
					{
						try 
						{
							text.setText(botP[k].getText());
							baseText[0].setText(text.getText().toUpperCase());
							baseText[1].setText(Converter.hexConverter(text.getText(), 10));
							Long y = Long.parseLong(baseText[1].getText());
							baseText[2].setText(Long.toOctalString(y));
							baseText[3].setText(insertNewLine(Long.toBinaryString(y)));
						}
						catch (NumberFormatException ex)
						{
							
						}
					}
					else
					{
						try
						{
							text.setText(text.getText() + botP[k].getText());
							baseText[0].setText(text.getText().toUpperCase());
							baseText[1].setText(Converter.hexConverter(text.getText(), 10));
							Long y = Long.parseLong(baseText[1].getText());
							baseText[2].setText(Long.toOctalString(y));
							baseText[3].setText(Long.toBinaryString(y));
						}
						catch (NumberFormatException ex)
						{
							
						}
					}
				}
			}
		}
		else if (text.getText().length() < 8 && modes[2].getText().equalsIgnoreCase("DWORD"))
		{
			for (int k = 0; k < botP.length; k++)
			{
				if (e.getSource().equals(botP[k]) && ((k >= 6 && k <= 10) ||
			(k >= 12 && k <= 16) || (k >= 18 && k <= 22)|| k == 27))
				{
					if (text.getText().equals("0"))
					{
						try 
						{
							text.setText(botP[k].getText());
							baseText[0].setText(text.getText().toUpperCase());
							baseText[1].setText(Converter.hexConverter(text.getText(), 10));
							Long y = Long.parseLong(baseText[1].getText());
							baseText[2].setText(Long.toOctalString(y));
							baseText[3].setText(Long.toBinaryString(y));
						}
						catch (NumberFormatException ex)
						{
							
						}
					}
					else
					{
						try
						{
							text.setText(text.getText() + botP[k].getText());
							baseText[0].setText(text.getText().toUpperCase());
							baseText[1].setText(Converter.hexConverter(text.getText(), 10));
							Long y = Long.parseLong(baseText[1].getText());
							baseText[2].setText(Long.toOctalString(y));
							baseText[3].setText(Long.toBinaryString(y));
						}
						catch (NumberFormatException ex)
						{
							
						}
				}
				}
			}
		}
		else if (text.getText().length() < 4 && modes[2].getText().equalsIgnoreCase("WORD"))
		{
			for (int k = 0; k < botP.length; k++)
			{
				if (e.getSource().equals(botP[k]) && ((k >= 6 && k <= 10) ||
			(k >= 12 && k <= 16) || (k >= 18 && k <= 22)|| k == 27))
				{
					if (text.getText().equals("0"))
					{
						try 
						{
							text.setText(botP[k].getText());
							baseText[0].setText(text.getText().toUpperCase());
							baseText[1].setText(Converter.hexConverter(text.getText(), 10));
							Long y = Long.parseLong(baseText[1].getText());
							baseText[2].setText(Long.toOctalString(y));
							baseText[3].setText(Long.toBinaryString(y));
						}
						catch (NumberFormatException ex)
						{
							
						}
					}
					else
					{
						try
						{
							text.setText(text.getText() + botP[k].getText());
							baseText[0].setText(text.getText().toUpperCase());
							baseText[1].setText(Converter.hexConverter(text.getText(), 10));
							Long y = Long.parseLong(baseText[1].getText());
							baseText[2].setText(Long.toOctalString(y));
							baseText[3].setText(Long.toBinaryString(y));
						}
						catch (NumberFormatException ex)
						{
							
						}
				}
				}
			}
		}
		else if (text.getText().length() < 2 && modes[2].getText().equalsIgnoreCase("BYTE"))
		{
			for (int k = 0; k < botP.length; k++)
			{
				if (e.getSource().equals(botP[k]) && ((k >= 6 && k <= 10) ||
			(k >= 12 && k <= 16) || (k >= 18 && k <= 22)|| k == 27))
				{
					if (text.getText().equals("0"))
					{
						try 
						{
							text.setText(botP[k].getText());
							baseText[0].setText(text.getText().toUpperCase());
							baseText[1].setText(Converter.hexConverter(text.getText(), 10));
							Long y = Long.parseLong(baseText[1].getText());
							baseText[2].setText(Long.toOctalString(y));
							baseText[3].setText(Long.toBinaryString(y));
						}
						catch (NumberFormatException ex)
						{
							
						}
					}
					else
					{
						try
						{
							text.setText(text.getText() + botP[k].getText());
							baseText[0].setText(text.getText().toUpperCase());
							baseText[1].setText(Converter.hexConverter(text.getText(), 10));
							Long y = Long.parseLong(baseText[1].getText());
							baseText[2].setText(Long.toOctalString(y));
							baseText[3].setText(Long.toBinaryString(y));
						}
						catch (NumberFormatException ex)
						{
							
						}
				}
				}
			}
		}
	}
	/* Creates a decimal representation of a number
	 * @param e the action received
	 */
	private void decimalOperations(ActionEvent e)
	{
		if (text.getText().length() < 19 && modes[2].getText().equalsIgnoreCase("QWORD"))
		{
			for (int k = 0; k < botP.length; k++)
			{
				if (e.getSource().equals(botP[k]) && ((k >= 8 && k <= 10) ||
						(k >= 14 && k <= 16) || (k >= 20 && k <= 22)|| k == 27))
				{
					if (text.getText().equals("0"))
					{
						try 
						{
							text.setText(botP[k].getText());
							Long y = Long.parseLong(botP[k].getText());
							baseText[0].setText(Integer.toHexString((y.intValue())).toUpperCase());
							baseText[1].setText(Integer.toString((y.intValue())));
							baseText[2].setText(Integer.toOctalString((y.intValue())));
							baseText[3].setText(insertNewLine(Integer.toBinaryString((y.intValue()))));
							break;
						}
						catch (NumberFormatException ex)
						{
							
						}
					}
					else
					{
						try
						{
							text.setText(text.getText() + botP[k].getText());
							Long y = Long.parseLong(text.getText());
							baseText[0].setText(Long.toHexString((y)));
							baseText[0].setText(baseText[0].getText().toUpperCase());
							baseText[1].setText(Long.toString((y)));
							baseText[2].setText(Long.toOctalString((y)));
							baseText[3].setText(Long.toBinaryString((y)));
							break;
						}
						catch (NumberFormatException ex)
						{
							
						}
				}
				}
			}
		}
		else if (text.getText().length() < 9 && modes[2].getText().equalsIgnoreCase("DWORD"))
		{
			for (int k = 0; k < botP.length; k++)
			{
				if (e.getSource().equals(botP[k]) && ((k >= 8 && k <= 10) ||
						(k >= 14 && k <= 16) || (k >= 20 && k <= 22)|| k == 27))
				{
					if (text.getText().equals("0"))
					{
						try 
						{
							text.setText(botP[k].getText());
							Long y = Long.parseLong(botP[k].getText());
							baseText[0].setText(Integer.toHexString((y.intValue())).toUpperCase());
							baseText[1].setText(Integer.toString((y.intValue())));
							baseText[2].setText(Integer.toOctalString((y.intValue())));
							baseText[3].setText(Integer.toBinaryString((y.intValue())));
							break;
						}
						catch (NumberFormatException ex)
						{
							
						}
					}
					else
					{
						try
						{
							text.setText(text.getText() + botP[k].getText());
							Long y = Long.parseLong(text.getText());
							baseText[0].setText(Long.toHexString((y)));
							baseText[0].setText(baseText[0].getText().toUpperCase());
							baseText[1].setText(Long.toString((y)));
							baseText[2].setText(Long.toOctalString((y)));
							baseText[3].setText(Long.toBinaryString((y)));
							break;
						}
						catch (NumberFormatException ex)
						{
							
						}
				}
				}
			}
		}
		else if (text.getText().length() < 5 && modes[2].getText().equalsIgnoreCase("WORD"))
		{
			for (int k = 0; k < botP.length; k++)
			{
				if (e.getSource().equals(botP[k]) && ((k >= 8 && k <= 10) ||
						(k >= 14 && k <= 16) || (k >= 20 && k <= 22)|| k == 27))
				{
					if (text.getText().equals("0"))
					{
						try 
						{
							text.setText(botP[k].getText());
							Long y = Long.parseLong(botP[k].getText());
							baseText[0].setText(Integer.toHexString((y.intValue())).toUpperCase());
							baseText[1].setText(Integer.toString((y.intValue())));
							baseText[2].setText(Integer.toOctalString((y.intValue())));
							baseText[3].setText(Integer.toBinaryString((y.intValue())));
							break;
						}
						catch (NumberFormatException ex)
						{
							
						}
					}
					else
					{
						try
						{
							text.setText(text.getText() + botP[k].getText());
							Long y = Long.parseLong(text.getText());
							baseText[0].setText(Long.toHexString((y)));
							baseText[0].setText(baseText[0].getText().toUpperCase());
							baseText[1].setText(Long.toString((y)));
							baseText[2].setText(Long.toOctalString((y)));
							baseText[3].setText(Long.toBinaryString((y)));
							break;
						}
						catch (NumberFormatException ex)
						{
							
						}
				}
				}
			}
		}
		else if (text.getText().length() < 3 && modes[2].getText().equalsIgnoreCase("BYTE"))
		{
			for (int k = 0; k < botP.length; k++)
			{
				if (e.getSource().equals(botP[k]) && ((k >= 8 && k <= 10) ||
						(k >= 14 && k <= 16) || (k >= 20 && k <= 22)|| k == 27))
				{
					if (text.getText().equals("0"))
					{
						try 
						{
							text.setText(botP[k].getText());
							Long y = Long.parseLong(botP[k].getText());
							baseText[0].setText(Integer.toHexString((y.intValue())).toUpperCase());
							baseText[1].setText(Integer.toString((y.intValue())));
							baseText[2].setText(Integer.toOctalString((y.intValue())));
							baseText[3].setText(Integer.toBinaryString((y.intValue())));
							break;
						}
						catch (NumberFormatException ex)
						{
							
						}
					}
					else
					{
						try
						{
							text.setText(text.getText() + botP[k].getText());
							Long y = Long.parseLong(text.getText());
							baseText[0].setText(Long.toHexString((y)));
							baseText[0].setText(baseText[0].getText().toUpperCase());
							baseText[1].setText(Long.toString((y)));
							baseText[2].setText(Long.toOctalString((y)));
							baseText[3].setText(Long.toBinaryString((y)));
							break;
						}
						catch (NumberFormatException ex)
						{
							
						}
				}
				}
			}
		}


	}
	/* Creates an octal numerical display
	 * @param e the input received
	 */
	private void octOperations(ActionEvent e) 
	{
		if (text.getText().length() < 22 && modes[2].getText().equalsIgnoreCase("QWORD"))
		{
			for (int k = 0; k < botP.length; k++)
			{
				if (e.getSource().equals(botP[k]) && (k == 8 ||
						(k >= 14 && k <= 16) || (k >= 20 && k <= 22)|| k == 27))
				{
					if (text.getText().equals("0"))
					{
						try 
						{
							text.setText(botP[k].getText());
							baseText[2].setText(text.getText());
							// Convert to Decimal here
							baseText[1].setText(Converter.octConverter(text.getText()));
							Long y = Long.parseLong(baseText[1].getText());
							baseText[0].setText(Long.toHexString(y).toUpperCase());
							baseText[3].setText(insertNewLine(Long.toBinaryString(y)));
							break;
						}
						catch (NumberFormatException ex)
						{
							
						}
					}
					else
					{
						try
						{
							text.setText(text.getText() + botP[k].getText());
							baseText[2].setText(text.getText());
							// Convert to Decimal
							baseText[1].setText(Converter.octConverter(text.getText()));
							Long y = Long.parseLong(baseText[1].getText());
							baseText[0].setText(Long.toHexString(y).toUpperCase());
							baseText[3].setText(Long.toBinaryString(y));
							break;
						}
						catch (NumberFormatException ex)
						{
							
						}
				}
				}
			}
		}		
		else if (text.getText().length() < 10 && modes[2].getText().equalsIgnoreCase("DWORD"))
		{
			for (int k = 0; k < botP.length; k++)
			{
				if (e.getSource().equals(botP[k]) && (k == 8 ||
						(k >= 14 && k <= 16) || (k >= 20 && k <= 22)|| k == 27))
				{
					if (text.getText().equals("0"))
					{
						try 
						{
							text.setText(botP[k].getText());
							baseText[2].setText(text.getText());
							// Convert to Decimal here
							baseText[1].setText(Converter.octConverter(text.getText()));
							Long y = Long.parseLong(baseText[1].getText());
							baseText[0].setText(Long.toHexString(y).toUpperCase());
							baseText[3].setText(Long.toBinaryString(y));
							break;
						}
						catch (NumberFormatException ex)
						{
							
						}
					}
					else
					{
						try
						{
							text.setText(text.getText() + botP[k].getText());
							baseText[2].setText(text.getText());
							// Convert to Decimal
							baseText[1].setText(Converter.octConverter(text.getText()));
							Long y = Long.parseLong(baseText[1].getText());
							baseText[0].setText(Long.toHexString(y).toUpperCase());
							baseText[3].setText(Long.toBinaryString(y));
							break;
						}
						catch (NumberFormatException ex)
						{
							
						}
				}
				}
			}
		}		
		else if (text.getText().length() < 5 && modes[2].getText().equalsIgnoreCase("WORD"))
		{
			for (int k = 0; k < botP.length; k++)
			{
				if (e.getSource().equals(botP[k]) && (k == 8 ||
						(k >= 14 && k <= 16) || (k >= 20 && k <= 22)|| k == 27))
				{
					if (text.getText().equals("0"))
					{
						try 
						{
							text.setText(botP[k].getText());
							baseText[2].setText(text.getText());
							// Convert to Decimal here
							baseText[1].setText(Converter.octConverter(text.getText()));
							Long y = Long.parseLong(baseText[1].getText());
							baseText[0].setText(Long.toHexString(y).toUpperCase());
							baseText[3].setText(Long.toBinaryString(y));
							break;
						}
						catch (NumberFormatException ex)
						{
							
						}
					}
					else
					{
						try
						{
							text.setText(text.getText() + botP[k].getText());
							baseText[2].setText(text.getText());
							// Convert to Decimal
							baseText[1].setText(Converter.octConverter(text.getText()));
							Long y = Long.parseLong(baseText[1].getText());
							baseText[0].setText(Long.toHexString(y).toUpperCase());
							baseText[3].setText(Long.toBinaryString(y));
							break;
						}
						catch (NumberFormatException ex)
						{
							
						}
				}
				}
			}
		}		
		else if (text.getText().length() < 3 && modes[2].getText().equalsIgnoreCase("BYTE"))
		{
			for (int k = 0; k < botP.length; k++)
			{
				if (e.getSource().equals(botP[k]) && (k == 8 ||
						(k >= 14 && k <= 16) || (k >= 20 && k <= 22)|| k == 27))
				{
					if (text.getText().equals("0"))
					{
						try 
						{
							text.setText(botP[k].getText());
							baseText[2].setText(text.getText());
							// Convert to Decimal here
							baseText[1].setText(Converter.octConverter(text.getText()));
							Long y = Long.parseLong(baseText[1].getText());
							baseText[0].setText(Long.toHexString(y).toUpperCase());
							baseText[3].setText(Long.toBinaryString(y));
							break;
						}
						catch (NumberFormatException ex)
						{
							
						}
					}
					else
					{
						try
						{
							text.setText(text.getText() + botP[k].getText());
							baseText[2].setText(text.getText());
							// Convert to Decimal
							baseText[1].setText(Converter.octConverter(text.getText()));
							Long y = Long.parseLong(baseText[1].getText());
							baseText[0].setText(Long.toHexString(y).toUpperCase());
							baseText[3].setText(Long.toBinaryString(y));
							break;
						}
						catch (NumberFormatException ex)
						{
							
						}
				}
				}
			}
		}		
	}

	
	
	private String insertNewLine(String str)
	{
		if (str.length() > 32)
		{
			StringBuffer reBuffer = new StringBuffer(str);
			reBuffer.insert(32,  "\n");
			return reBuffer.toString();
		}
		
		return str;
	}

	@Override
	public void componentResized(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}
}
