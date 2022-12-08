/* Name of Program: Calculator.java
 * Purpose of Program: Design a Calculator
 * Author of Program: B. Yacoob
 */

// Import of packages
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Stack;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

// Superclass that is a child of JFrame to inherit its methods and properties
public class Calculator extends JFrame implements KeyListener {
	// Declaration of variables
	private static final int ARRAY_SIZE = 20;
	private JPanel mainPanel, topOutputPanel, numsPanel; // Panels
	private JButton centerNumsBtns[] = new JButton[ARRAY_SIZE]; // Array of buttons
	private Stack<Double> stackNums = new Stack<Double>(); // Stack to hold the expression #s
	private JTextArea inputTextArea, outputTextArea; // Where the expressions will be output
	private boolean clearInput = false, equalSignClicked = false;
	private String storeExpression = "";
	private int countOperands, countOperators;
	public int lastInputLength = 0;
    private ArrayList<String> operatorArr = new ArrayList<>();	// Create an empty array list
	private final int WINDOW_WIDTH = 260; // Window width
	private final int WINDOW_HEIGHT = 335; // Window height

	public Calculator() {
		setTitle("My Calculator"); // Setting the title

		// Setting size of the window
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

		// What happens when close button is clicked
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		mainPanel = new JPanel();

		mainPanel.setLayout(new BorderLayout()); // 3 Rows - textArea, central buttons, bottom buttons

		topShowOutput(); // Shows the results of the expression(s)
		centerGridPanel(); // What holds majority of buttons

		add(mainPanel); // This adds the panel to the content frame

		// Visibility is shown for the window
		setVisible(true);
	}

	// Panel for the top that has the buttons' outputs
	private void topShowOutput() {
		topOutputPanel = new JPanel();
		topOutputPanel.setLayout(new GridLayout(2, 1)); // 2 Text Areas

		// Text area
		inputTextArea = new JTextArea();
		inputTextArea.addKeyListener(this); // Keyboard listener
		inputTextArea.setEditable(false); // Do not allow user to type into the output text area
		inputTextArea.setPreferredSize(new Dimension(130, 24)); // Manipulating the size of text area

		// Style the output display
		inputTextArea.setFont(new Font("Monospaced", Font.BOLD, 20));
		// Put the 0 on the far right side
		inputTextArea.setText("0");
		inputTextArea.setBackground(Color.BLACK);
		inputTextArea.setForeground(Color.YELLOW);

		// Text area
		outputTextArea = new JTextArea();
		outputTextArea.setEditable(false); // Do not allow user to type into the output text area
		outputTextArea.setPreferredSize(new Dimension(130, 24)); // Manipulating the size of text area

		// Style the output display
		outputTextArea.setFont(new Font("Monospaced", Font.BOLD, 15));
		// Put the result on the far right side
		outputTextArea.setBackground(Color.BLACK);
		outputTextArea.setForeground(Color.YELLOW);

		topOutputPanel.add(outputTextArea); // Add to the panel
		topOutputPanel.add(inputTextArea);

		// Adding the panel to the content pane
		mainPanel.add(topOutputPanel, BorderLayout.NORTH);
	}

	// Panel for central buttons in the calculator
	private void centerGridPanel() {
		numsPanel = new JPanel();
		numsPanel.setLayout(new GridLayout(5, 4)); // 20 buttons that will cover the majority of the calculator

		for (int k = 0; k < ARRAY_SIZE; k++) {
			centerNumsBtns[k] = new JButton();

			// Add the buttons to the panel
			numsPanel.add(centerNumsBtns[k]);

			// Register an event listener with the 20 buttons as well as a keyboard listener
			centerNumsBtns[k].addActionListener(new ButtonListener());
			centerNumsBtns[k].addKeyListener(this); // Keyboard listener

			// Make buttons colored
			centerNumsBtns[k].setBackground(Color.BLACK);
			centerNumsBtns[k].setForeground(Color.YELLOW);

			// Style the output display
			centerNumsBtns[k].setFont(new Font("Monospaced", Font.BOLD, 11));
		}

		// Top four buttons that get manually labeled
		centerNumsBtns[0].setText("<---");
		centerNumsBtns[1].setText("CE");
		centerNumsBtns[2].setText("C");
		centerNumsBtns[3].setText("=");

		// 7, 8, 9, /
		centerNumsBtns[4].setText("" + 7);
		centerNumsBtns[5].setText("" + 8);
		centerNumsBtns[6].setText("" + 9);
		centerNumsBtns[7].setText("/");

		// 4, 5, 6, *
		centerNumsBtns[8].setText("" + 4);
		centerNumsBtns[9].setText("" + 5);
		centerNumsBtns[10].setText("" + 6);
		centerNumsBtns[11].setText("*");

		// 1, 2, 3, -
		centerNumsBtns[12].setText("" + 1);
		centerNumsBtns[13].setText("" + 2);
		centerNumsBtns[14].setText("" + 3);
		centerNumsBtns[15].setText("-");

		// Manually labeling 0, ., +
		centerNumsBtns[16].setText("" + 0);
		centerNumsBtns[17].setText(".");
		centerNumsBtns[18].setText("+");
		centerNumsBtns[19].setText("EXIT");

		// Adding the panel to the content pane
		mainPanel.add(numsPanel, BorderLayout.CENTER);
	}

	// Allows the user to delete a single character that was inputed by them
	public void backspaceDelete() {
		// Make sure text area is not empty string
		if (inputTextArea.getText().length() > 0) {
			inputTextArea.setText(inputTextArea.getText().substring(0, inputTextArea.getText().length() - 1));
			if (storeExpression.length() != 0) {
				storeExpression = storeExpression.substring(0, storeExpression.length() - 1);
			}
		}
	}

	public void keyTyped(KeyEvent e) {
		char keyboardChar = e.getKeyChar();

		// Call method to pass the pressed keyboard character
		validateExpression("", keyboardChar);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	// Validates if the input is one of the 20 buttons or keyPress as well
	public void validateExpression(String buttonClicked, char e) {
		double result = 0, numTwo = 0, numOne = 0;

		// Check if buttonClicked is a digit. If it is, show it on the textArea
		if (buttonClicked != "" && Character.isDigit(buttonClicked.charAt(0))) {
			if (inputTextArea.getText().equals("0")) { // Prevents user from adding 0 when expression is 0
				inputTextArea.setText("");
			}

			if (clearInput == false) {
				equalSignClicked = false;
				inputTextArea.append(buttonClicked); // Output to the text area
				storeExpression += buttonClicked; // Store this output the whole expression
			} else {
				equalSignClicked = false;
				storeExpression += buttonClicked; // Store this output the whole expression
				inputTextArea.setText(buttonClicked); // Output to the text area
				clearInput = false; // Reset back to false
			}
		}

		// Check if e is a digit. If it is, show it on the textArea
		if (Character.isDigit(e)) {
			if (inputTextArea.getText().equals("0")) { // Prevents user from adding 0 when expression is 0
				inputTextArea.setText("");
			}

			if (clearInput == false) {
				equalSignClicked = false;
				inputTextArea.append("" + e); // Output to the text area
				storeExpression += e; // Store this output the whole expression
			} else {
				equalSignClicked = false;
				storeExpression += e; // Store this output the whole expression
				inputTextArea.setText("" + e); // Output to the text area
				clearInput = false; // Reset back to false
			}
		}

		// Backspace or the ASCII code of it
		if (buttonClicked.equals("<---") || e == 8) {
			// Deletes the last input number put in by user
			backspaceDelete();
		}

		// Clears the current input expression
		else if (buttonClicked.equals("CE") || e == 127) {
			lastInputLength = inputTextArea.getText().length();
			inputTextArea.setText("0");
			if (lastInputLength < storeExpression.length()) {
				storeExpression = storeExpression.substring(0, storeExpression.length() - lastInputLength);
			} else {
				storeExpression = "";
			}
		}

		// Clears everything including the result of expression on the top or the ASCII
		// code for 'c'
		else if (buttonClicked.equals("C") || e == 67 || e == 99) {
			equalSignClicked = true;	// Set this to false so that the user will be able to press '=' again
			stackNums.clear(); // The result of expression is no longer in the stack
			outputTextArea.setText("");
			countOperands = 0;
			countOperators = 0;
			storeExpression = ""; // Reset to empty string
			operatorArr.clear(); // Reset to empty string
			inputTextArea.setText("0");
		}

		// '=' operator or the ASCII code of it/LineFeed (enter)
		else if (buttonClicked.equals("=") || e == 61 || e == 10) {
			countOperands++;
			countOperators++;
			
			if (equalSignClicked == false) {
				stackNums.push(Double.parseDouble(inputTextArea.getText())); // Push to the number to the stack
				storeExpression += " = "; // Store this output the whole expression
				outputTextArea.setText(storeExpression);
				storeExpression = "";
				equalSignClicked = true;	// Set this to true so that the user will not be able to press '=' more than once
				clearInput = true; // When user clicks an operator, this passes to the top and replaces the current text in inputTextArea
			} else {
				operatorArr.clear();
				equalSignClicked = true;
				storeExpression = "";
				stackNums.clear();
				countOperands = 0;
				countOperators = 0;
			}
		}

		// '/' operator or the ASCII code of it
		else if (buttonClicked.equals("/") || e == 47) {
			stackNums.push(Double.parseDouble(inputTextArea.getText())); // Push to the number to the stack
			countOperands++;
			storeExpression += " / "; // Store this output the whole expression
			operatorArr.add("/"); // Store the operator to later be used in performing an operation
			outputTextArea.setText(storeExpression);
			countOperators++;
			clearInput = true; // When user clicks an operator, this passes to the top and replaces the current
								// text in inputTextArea
		}

		// '*' operator or the ASCII code of it
		else if (buttonClicked.equals("*") || e == 42) {
			stackNums.push(Double.parseDouble(inputTextArea.getText())); // Push to the number to the stack
			countOperands++;
			storeExpression += " * "; // Store this output the whole expression
			operatorArr.add("*"); // Store the operator to later be used in performing an operation
			outputTextArea.setText(storeExpression);
			countOperators++;
			clearInput = true; // When user clicks an operator, this passes to the top and replaces the current
								// text in inputTextArea
		}

		// '-' operator or the ASCII code of it
		else if (buttonClicked.equals("-") || e == 45) {
			stackNums.push(Double.parseDouble(inputTextArea.getText())); // Push to the number to the stack
			countOperands++;
			storeExpression += " - "; // Store this output the whole expression
			operatorArr.add("-"); // Store the operator to later be used in performing an operation
			outputTextArea.setText(storeExpression);
			countOperators++;
			clearInput = true; // When user clicks an operator, this passes to the top and replaces the current
								// text in inputTextArea
		}

		// '.' or the ASCII code of it
		else if (buttonClicked.equals(".") || e == 46) {
			// Prevents adding more than one dot on the output
			if (inputTextArea.getText().contains(".")) {
				return;
			} else {
				inputTextArea.append(".");
				storeExpression += ".";
			}
		}

		// '+' operator or the ASCII code of it
		else if (buttonClicked.equals("+") || e == 43) {
			stackNums.push(Double.parseDouble(inputTextArea.getText())); // Push to the number to the stack
			countOperands++;
			storeExpression += " + "; // Store this output the whole expression
			operatorArr.add("+"); // Store the operator to later be used in performing an operation
			outputTextArea.setText(storeExpression);
			countOperators++;
			clearInput = true; // When user clicks an operator, this passes to the top and replaces the current
								// text in inputTextArea
		}

		// 'EXIT' button or the ASCII code of 'e'
		else if (buttonClicked.equals("EXIT") || e == 69 || e == 101) {
			System.exit(0);
		}

		// Get the last pushed value, pop to get the first pushed value, then perform
		// operation to get result
		if ((countOperands >= 2 && countOperators >= 1) && stackNums.size() == 2) {
			numTwo = stackNums.peek();
			stackNums.pop();
			numOne = stackNums.peek();
			stackNums.pop();

			switch (operatorArr.get(0)) {
			case "/":
				result = numOne / numTwo;
				stackNums.push(result); // Push result to stack
				break;
			case "+":
				result = numOne + numTwo;
				stackNums.push(result); // Push result to stack
				break;
			case "-":
				result = numOne - numTwo;
				stackNums.push(result); // Push result to stack
				break;
			case "*":
				result = numOne * numTwo;
				stackNums.push(result); // Push result to stack
				break;
			default:
				break;
			}

			inputTextArea.setText(String.valueOf(stackNums.peek())); // Output the result of the expression
			countOperands = 1; // This is the result operand
			countOperators = 0;
			operatorArr.remove(0);
		}
	}

// What class implements the action listener for each button clicked
	private class ButtonListener implements ActionListener {
		// When button is clicked
		public void actionPerformed(ActionEvent e) { // Waits for the action
			JButton buttonClicked = (JButton) e.getSource();

			// Method call to pass the buttonClicked
			validateExpression(buttonClicked.getText(), ' ');
		}
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Calculator frame = new Calculator(); // Calling the class in main
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}