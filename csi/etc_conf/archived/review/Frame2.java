package gov.ymp.csi.review;

import javax.swing.*; import java.awt.*;

public class Frame2 extends JFrame
{

   // first comes the constructor
    public Frame2()
   {


	   String[] labels = {"Reviewer Name: ", "Due Date: ", "Position: ", "Review Name: ", "Contact Info: "};
	   int numPairs = labels.length;

	   JButton pressme = new JButton("Submit");

	   //Create and populate the panel.
	   SpringLayout layout = new SpringLayout();
	   JPanel p = new JPanel(layout);
	   for (int i = 0; i < numPairs; i++) {
	         JLabel l = new JLabel(labels[i]);
	         p.add(l);
	         JTextField textField = new JTextField(10);
	         l.setLabelFor(textField);
	         p.add(textField);
        }


        //p.add(pressme);


		//Lay out the panel.
		 SpringUtilities.makeCompactGrid(p,
		                                 numPairs, 2, //rows, cols
		                                 6, 6,        //initX, initY
		                                 6, 6);       //xPad, yPad


           //p.add(pressme);

       	  // get screen dimensions

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

      Container con = this.getContentPane(); // inherit main frame

	  con.setLayout(layout);

      con.add(p);   // JPanel containers default to FlowLayout

      //con.add(pressme, SpringLayout.SOUTH);
      //pressme.setMnemonic('P'); // associate hotkey

   }
   // Application's main method
   public static void main(String[] Args)
   {
      Frame2 app = new Frame2();
      //app.setTitle("JPrompt Demo");
      //app.setBounds(100,100,300,200); // position frame
      app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      app.setVisible(true);
   }
}
