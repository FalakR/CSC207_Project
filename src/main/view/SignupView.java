package view;

import interface_adapters.signup.SignupController;
import interface_adapters.signup.SignupPresenter;
import interface_adapters.signup.SignupState;
import interface_adapters.signup.SignupViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class SignupView extends JFrame implements ActionListener, PropertyChangeListener {
    public final String viewName = "sign up";

    private final SignupViewModel signupViewModel;
    final JTextField nameInputField = new JTextField(15);
    final JTextField emailInputField = new JTextField(15);
    final JPasswordField passwordInputField = new JPasswordField(15);
    private final SignupController signupController;
    private final SignupPresenter signupPresenter;

    final JButton signUp;
    final JButton logIn;


    public SignupView(SignupController controller, SignupPresenter presenter, SignupViewModel signupViewModel) {

        this.signupController = controller;
        this.signupPresenter = presenter;
        this.signupViewModel = signupViewModel;
        signupViewModel.addPropertyChangeListener(this);

        JLabel title = new JLabel("Scoops Ahoy");
        title.setFont(new Font("Engravers Gothic BT", Font.BOLD, 24)); // Set font to Engravers Gothic BT

        nameInputField.setPreferredSize(new Dimension(300, 50));
        LabelTextPanel nameInfo = new LabelTextPanel(
                new JLabel("Name"), nameInputField);
        emailInputField.setPreferredSize(new Dimension(300, 50));
        LabelTextPanel emailInfo = new LabelTextPanel(
                new JLabel("Email"), emailInputField);
        passwordInputField.setPreferredSize(new Dimension(300, 50));
        LabelTextPanel passwordInfo = new LabelTextPanel(
                new JLabel("Password"), passwordInputField);


        JPanel buttons = new JPanel();
        signUp = new JButton("Sign Up");
        signUp.setPreferredSize(new Dimension(100, 50));

        buttons.add(signUp);
        logIn = new JButton("Log In");
        logIn.setPreferredSize(new Dimension(100, 50));

        buttons.add(logIn);

        this.setBackground(new Color(253, 227, 245)); // Pastel Pink
        nameInfo.setBackground(new Color(255, 218, 185)); // Peach
        emailInfo.setBackground(new Color(255, 250, 205)); // Lemon Chiffon
        passwordInfo.setBackground(new Color(240, 255, 240)); // Honeydew
        buttons.setBackground(new Color(230, 230, 250)); // Mint Green

        // Set text color
        title.setForeground(new Color(77, 77, 77)); // Dark Gray


        signUp.addActionListener(
                // This creates an anonymous subclass of ActionListener and instantiates it.
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(signUp)) {
                            signupController.execute(signupViewModel.getState().getName(),
                                    signupViewModel.getState().getEmail(),
                                    signupViewModel.getState().getPassword());
                            if (signupViewModel.getState().getEmailError() == null && signupViewModel.getState().getPasswordError() == null){
                                signupPresenter.prepareSuccessView();
                            }
                        }
                    }
                }
        );
        logIn.addActionListener((
                // This creates an anonymous subclass of ActionListener and instantiates it.
                new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                        if (evt.getSource().equals(logIn)) {
                            signupPresenter.prepareLoginView();
                        }
                    }
                }
        ));

        // This makes a new KeyListener implementing class, instantiates it, and
        // makes it listen to keystrokes in the usernameInputField.
        //
        // Notice how it has access to instance variables in the enclosing class!
        nameInputField.addKeyListener(
                new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        SignupState currentState = signupViewModel.getState();
                        currentState.setName(nameInputField.getText() + e.getKeyChar());
                        signupViewModel.setState(currentState);
                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                    }
                });

        emailInputField.addKeyListener(
                new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        SignupState currentState = signupViewModel.getState();
                        currentState.setEmail(String.valueOf(emailInputField.getText()) + e.getKeyChar());
                        signupViewModel.setState(currentState);
                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                    }
                });

        passwordInputField.addKeyListener(
                new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        SignupState currentState = signupViewModel.getState();
                        currentState.setPassword(String.valueOf(passwordInputField.getPassword()) + e.getKeyChar());
                        signupViewModel.setState(currentState);
                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {
                    }
                });
        this.setLayout(new BorderLayout());

        // Create a panel for the title with FlowLayout to center align horizontally
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.add(title);


        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.add(nameInfo);
        formPanel.add(emailInfo);
        formPanel.add(passwordInfo);

        this.add(titlePanel, BorderLayout.NORTH);
        this.add(formPanel, BorderLayout.CENTER);
        this.add(buttons, BorderLayout.SOUTH);

        // Pack the components to determine the frame size
        pack();

        // Center the window on the screen
        setLocationRelativeTo(null);

        // Set the operation to close the application when the frame is closed
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    /**
     * React to a button click that results in evt.
     */
    public void actionPerformed(ActionEvent evt) {
        System.out.println("Cancel not implemented yet.");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SignupState state = (SignupState) evt.getNewValue();

        if (state.getEmailError() != null) {
            JButton okButton = new JButton("OK");

            okButton.addActionListener(e -> {
                Container container = (Container) e.getSource();
                Frame frame = JOptionPane.getFrameForComponent(container);
                frame.dispose();  // Close the frame associated with the JOptionPane
            });

            JOptionPane.showOptionDialog(
                    this,
                    "User with this email already exists. Please try logging in, or signing up with a different email.",
                    state.getEmailError(),
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.ERROR_MESSAGE,
                    null,
                    new Object[]{okButton},
                    okButton);
        }

//            JOptionPane.showMessageDialog(this, state.getNameError());
    }
}

