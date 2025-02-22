
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class cal extends JFrame implements ActionListener {
    
    private JTextField display;
    private String currentOperator = "";
    private double firstNumber = 0;
    private boolean isNewInput = true;

    public cal() {
        setTitle("Simple Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 500);
        setLayout(new BorderLayout(10, 10));

        display = new JTextField();
        display.setFont(new Font("Arial", Font.BOLD, 30));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        add(display, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 4, 10, 10));

        String[] buttonLabels = {
                "7", "8", "9", "/", 
                "4", "5", "6", "*", 
                "1", "2", "3", "-", 
                "C", "0", "=", "+"
        };

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFont(new Font("Arial", Font.BOLD, 24));
            button.addActionListener(this);
            button.setFocusable(false);
            buttonPanel.add(button);
        }

        add(buttonPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        String buttonText = event.getActionCommand();

        if (isNumber(buttonText)) { 
            handleNumberInput(buttonText);
        } else if (buttonText.equals("C")) { 
            clearCalculator();
        } else if (buttonText.equals("=")) { 
            performCalculation();
        } else { 
            handleOperatorInput(buttonText);
        }
    }

    private boolean isNumber(String text) {
        return text.matches("\\d+"); // Now it matches multiple digits
    }

    private void handleNumberInput(String number) {
        if (isNewInput) {
            display.setText(number);
            isNewInput = false;
        } else {
            display.setText(display.getText() + number);
        }
    }

    private void handleOperatorInput(String operator) {
        if (!display.getText().isEmpty()) {
            firstNumber = Double.parseDouble(display.getText());
            currentOperator = operator;
            isNewInput = true;
        } else {
            display.setText("Enter a number first");
        }
    }

    private void performCalculation() {
        if (currentOperator.isEmpty() || display.getText().isEmpty()) return;

        try {
            double secondNumber = Double.parseDouble(display.getText());
            double result = calculateResult(firstNumber, secondNumber, currentOperator);
            
            display.setText(String.valueOf(result));
            currentOperator = "";
            isNewInput = true;
        } catch (ArithmeticException e) {
            display.setText("Cannot divide by zero");
        } catch (NumberFormatException e) {
            display.setText("Invalid input");
        }
    }

    private double calculateResult(double num1, double num2, String operator) {
        return switch (operator) {
            case "+" -> num1 + num2;
            case "-" -> num1 - num2;
            case "*" -> num1 * num2;
            case "/" -> {
                if (num2 == 0) throw new ArithmeticException("Cannot divide by zero");
                yield num1 / num2;
            }
            default -> throw new IllegalStateException("Unexpected operator: " + operator);
        };
    }

    private void clearCalculator() {
        display.setText("");
        firstNumber = 0;
        currentOperator = "";
        isNewInput = true;
    }

    public static void main(String[] args) {
        new cal();
    }
}
