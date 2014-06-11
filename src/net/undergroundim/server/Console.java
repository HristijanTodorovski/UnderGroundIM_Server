package net.undergroundim.server;

import java.awt.Color;
import java.awt.Toolkit;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * 
 * @author Troy
 *
 */
public class Console extends JFrame{
	private static final long serialVersionUID = 1557048456651437971L;

	private static JTextPane log = new JTextPane();
	private JScrollPane logContainer;
	
	private static StyledDocument doc = log.getStyledDocument();
	private static Style style = log.addStyle("Server", null);
	
	static Date date;
	static SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy - HH:mm:ss");
	
	/**
	 * Construct a new console.
	 */
	public Console(){
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(Server.class.getResource("/icons/earth-icon.png")));
		this.setSize(600,300);
		this.setResizable(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setTitle("Server Console | UnderGround IM");
		
		log.setSize(600, 300);
		log.setBackground(Color.BLACK);
		log.setEditable(false);
		logContainer = new JScrollPane(log,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		logContainer.setSize(600, 300);
		logContainer.setBackground(Color.BLACK);
		log.setForeground(Color.WHITE);
		
		this.add(logContainer);
		this.setVisible(true);
	}
	
	/**
	 * Log a message with different coloured text.
	 * 
	 * @param msg
	 * @param lineColour
	 */
	public void log(String msg, Color lineColour){
		if(Constants.isGuiEnabled()){
			if(lineColour == null)
				lineColour = Color.WHITE;
			
			try{
				if(log.getText().split("\n").length > 200){
					log.setText("Amount of lines exceeded 200, cleared and starting fresh." + "\n");
				}
				
				doc.insertString(doc.getLength(), getDate() + ": ", style);
				StyleConstants.setForeground(style, lineColour);
				doc.insertString(doc.getLength(), msg + "\n", style);
				StyleConstants.setForeground(style, Color.WHITE);
			}catch (BadLocationException e){
				e.printStackTrace();
			}
			
			//Auto scroll
			log.setCaretPosition(doc.getLength());
		}else{
			System.out.println(getDate() + ": " + msg);
		}
	}
	
	/**
	 * Get the date.
	 * 
	 * @return String
	 */
	public String getDate(){
		date = new Date();
		return sdf.format(date);
	}

}
