package gov.ymp.csi.review;

import javax.swing.*; import java.awt.*;

/**
* Returns a form with 5 Text Fields
* Names of the text fields are: Reviewer Name, Due Date, Position, Review Name, Contact Info
* Also adds a Submit button
*/
public class FormatTestFrame extends JFrame
{

   // first comes the constructor
    public FormatTestFrame()
   {

       Toolkit kit = Toolkit.getDefaultToolkit();
	   Dimension screenSize = kit.getScreenSize();
	   int screenHeight = screenSize.height;
	   int screenWidth = screenSize.width;

      //center frame in screen

	    setSize(screenWidth / 2, screenHeight / 2);
	    setLocation(screenWidth / 4, screenHeight / 4);

       //set frame icon and title

	   Image img = kit.getImage("Test.png");
	   setIconImage(img);
       setTitle("CenteredFrame");

       Container contentPane = getContentPane();


       JPanel buttonPanel = new JPanel();
	   JButton pressme = new JButton("Submit");
       buttonPanel.add(pressme);
       contentPane.add(buttonPanel, BorderLayout.SOUTH);


       String[] labels = {"Reviewer Name: ", "Due Date: ", "Position: ", "Review Name: ", "Contact Info: "};
	   int numPairs = labels.length;
	   //Create and populate the panel.


	   JPanel mainPanel = new JPanel();
	   mainPanel.setLayout(new BorderLayout());



	   for (int i = 0; i < numPairs; i++) {
	         JLabel l = new JLabel(labels[i], JLabel.TRAILING);
	         mainPanel.add(l);
	         JTextField textField = new JTextField(10);
	         //l.setLabelFor(textField);
	         mainPanel.add(textField);
	         final JLabel valueLabel = new JLabel();
	         mainPanel.add(valueLabel);

        }

       contentPane.add(mainPanel, BorderLayout.CENTER);



   }
   // Application's main method
   public static void main(String[] Args)
   {
	   FormatTestFrame frame = new FormatTestFrame();
	   frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   frame.show();

   }
}
