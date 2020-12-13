package main;

import static javax.swing.GroupLayout.Alignment.BASELINE;
import static javax.swing.GroupLayout.Alignment.LEADING;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.BadLocationException;

import Classes.Document;
import Classes.Query;
import IndexingLucene.MyIndexReader;
import PseudoRFSearch.*;
import SearchLucene.*;


public class HW4Main  {
	
	
	public static void main(String[] args) throws Exception {
		
		/**
		 *  1. Open index, extract queries and initialize the pseudo relevance feedback retrieval model 
		 */
		new GUI().setVisible(true);

	}
}

class GUI extends JFrame implements ActionListener  {
	private JTextField textField = new JTextField();
	private String input="";
	private String time = "";
    private JLabel timeLabel = new JLabel(time);
    private JPanel panel = new JPanel();
    private JPanel northPanel = new JPanel();
    private JPanel buttonPanel = new JPanel();
    private JTextArea querytext = new JTextArea();
    private JEditorPane editorPane = new JEditorPane();
    String Text = "Welcome to Restaurant Review Search(Pittsburgh)";
    
//class GUI implements ActionListener {
    public GUI() {
    	JLabel title = new JLabel("<html><span style='font-size:25px' color='blue'>"+Text+"</span></html>");
    	
  
//    	JLabel title = new JLabel("Welcom to WW Browser");
    	JLabel labelInput = new JLabel("  Please input your keywords:   ");
    	JLabel labelResult = new JLabel(" Result:   ");
    	labelInput.setFont(new Font("Serif", Font.ITALIC, 17));
    	labelResult.setFont(new Font("Serif", Font.BOLD, 17));
    	labelResult.setForeground(Color.WHITE);
    	labelResult.setOpaque(true);
    	labelResult.setBackground(new Color(30,144,255));
        JButton cancelButton = new JButton("Cancel");
        JButton findButton = new JButton("Find");
        findButton.setPreferredSize(new Dimension(100,40));
        cancelButton.setPreferredSize(new Dimension(100,40));
       
        findButton.addActionListener(this);
        cancelButton.addActionListener(this);
        cancelButton.setActionCommand("Cancel");
      
        northPanel.setLayout(new BorderLayout());
        buttonPanel.setLayout(new FlowLayout());
        textField.setSize(300,20);
        querytext = new JTextArea(30,100); //Height:10 character, width: 20 characters
        querytext.setLineWrap(true);
//        editorPane.setContentType("text/html");
//        editorPane.setSize(30, 100); //Height:10 character, width: 20 characters
        //querytext.setEditable(false);
        // remove redundant default border of check boxes - they would hinder
        // correct spacing and aligning (maybe not needed on some look and feels)
        editorPane.setContentType("text/html");
        
//        JScrollPane scroller = new JScrollPane(querytext);
//		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
//		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        JScrollPane scroller = new JScrollPane(editorPane);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scroller.setPreferredSize(new Dimension(800, 600));
		panel.add(scroller);
		
		northPanel.add(title, BorderLayout.NORTH);
		
		getContentPane().add(BorderLayout.NORTH,northPanel);
		northPanel.add(labelInput, BorderLayout.WEST);
		northPanel.add(textField, BorderLayout.CENTER);
		northPanel.add(buttonPanel, BorderLayout.EAST);
		buttonPanel.add(findButton);
		buttonPanel.add(cancelButton);

	  
        getContentPane().add(panel,BorderLayout.CENTER);

        getContentPane().add(timeLabel,BorderLayout.SOUTH); 
        getContentPane().add(labelResult,BorderLayout.WEST); 
        setTitle("Find");
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    


@Override
public void actionPerformed(ActionEvent arg0) {
	String action = arg0.getActionCommand();
	if (action.equals("Cancel")) {
		editorPane.setText(" ");
    }else {
	
	input = textField.getText();

	try {
		MyIndexReader myireader = new MyIndexReader();
		ExtractQuery extractQuery = new ExtractQuery(input);
		
		
		PseudoRFRetrievalModel PRFSearchModel = new PseudoRFRetrievalModel(myireader);
		
		/**
		 * 2. Begin searching
		 */
		long startTime = System.currentTimeMillis();
		// Extract the query 
		Classes.Query query = extractQuery.getQuery();
		// Conduct retrieval
		List<Classes.Document> rankedList = PRFSearchModel.RetrieveQuery(query, 5, 5, 0.8);
		if(rankedList != null) {
			StringBuilder sb = new StringBuilder();
			int rank = 1;
			for(Document doc : rankedList) {				
				System.out.println(doc);
//				JLabel lab = new JLabel(doc.toString());
//				String Stringout= doc.toString();
//				panelquery.add(new JLabel(Stringout));
//				StringBuilder sb = new StringBuilder();
				String Name=doc.getRestName();
				String url = doc.getRestUrl();
				sb.append("<a href=");
				sb.append(url+'>'+Name);
				sb.append("</a>.<br>");
				String Content = doc.getOriginalContent();
				sb.append(Content);
				sb.append("<br><br>");
				
//				editorPane.setText(editorPane.getText() + Name +"\n");
//				editorPane.setText(sb.toString());
//				editorPane.setEditable(false);
//				editorPane.setText(editorPane.getText() + Content +"\n");
			
//				editorPane.setText(editorPane.getText() + "------\n");
				rank++;
			}
			editorPane.setText(sb.toString());
			editorPane.setEditable(false);
			editorPane.addHyperlinkListener(new HyperlinkListener() {
	            @Override
	            public void hyperlinkUpdate(HyperlinkEvent e) {
	                if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
//	                    System.out.println(e.getURL());
	                    Desktop desktop = Desktop.getDesktop();
	                    try {
	                        desktop.browse(e.getURL().toURI());
	                    } catch (Exception ex) {
	                        ex.printStackTrace();
	                    }
	                }
	            }
	        });
		}

		
		//querytext = new JTextArea();
		long endTime = System.currentTimeMillis(); 
		
		// 3. Output running time
		System.out.println("Queries search time: " + (endTime - startTime) / 60000.0 + " min");
		timeLabel.setText("Query search time: "+ (endTime - startTime) / 60000.0 +" min");
		myireader.close();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}


	
}
}
}
