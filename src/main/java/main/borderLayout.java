package main;
import java.awt.*;

 
 import java.util.*;
 import java.applet.Applet;

 public class borderLayout extends Applet {

     protected void makebutton(String name,
                               GridBagLayout gridbag,
                               GridBagConstraints c) {
         Button button = new Button(name);
         gridbag.setConstraints(button, c);
         add(button);
     }

     public void init() {
    
         GridBagLayout gridbag = new GridBagLayout();
         GridBagConstraints c = new GridBagConstraints();


         setFont(new Font("SansSerif", Font.PLAIN, 14));
         setLayout(gridbag);

         c.fill = GridBagConstraints.BOTH;
         c.weightx = 1.0;
         makebutton("Button1", gridbag, c);
         makebutton("Button2", gridbag, c);
        

         c.gridwidth = GridBagConstraints.REMAINDER; //end row
         makebutton("Button3", gridbag, c);

         c.weightx = 0.0;                //reset to the default
         makebutton("Button5", gridbag, c); //another row

     

         setSize(300, 100);
     }

     public static void main(String args[]) {
         Frame f = new Frame("GridBag Layout Example");
         borderLayout ex1 = new borderLayout();

         ex1.init();

         f.add("Center", ex1);
         f.pack();
         f.setSize(f.getPreferredSize());
         f.show();
     }
 }