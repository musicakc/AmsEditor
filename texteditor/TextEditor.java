
package texteditor;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;

public class TextEditor {

    JFrame frame;
    JFileChooser filechooser;
    JPanel panel;
    JTextArea jta;
    
    public static void main(String[] args) {
        TextEditor editor=new TextEditor();
        editor.run();
    }
    
    public void run()
    {
        frame=new JFrame("AmsEditor"); //name of my text editor
        panel=new JPanel();
        jta=new JTextArea();
        
        //Word Wrapping
        jta.setLineWrap(true);
        jta.setWrapStyleWord(true);
        
        try{
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    
    JMenuBar menubar=new JMenuBar();
    JMenu file=new JMenu("File");
    //Adding Menu Items
    JMenuItem newMenuItem=new JMenuItem("New");
    newMenuItem.addActionListener(new NewMenuListener());
    
    JMenuItem saveMenuItem=new JMenuItem("Save");
    saveMenuItem.addActionListener(new SaveMenuListener());
    
    JMenuItem openMenuItem=new JMenuItem("Open");
    openMenuItem.addActionListener(new OpenMenuListener());
    
    JMenuItem exitMenuItem=new JMenuItem("Exit");
    exitMenuItem.addActionListener(new ExitMenuListener());
    
    //Adding Menu Items to the Menu
    file.add(newMenuItem);
    file.add(saveMenuItem);
    file.add(openMenuItem);
    file.add(exitMenuItem);
    menubar.add(file);
    menubar.setVisible(true);
    frame.setJMenuBar(menubar);
    frame.getContentPane().add(panel);
    panel.setLayout(new BorderLayout());
    panel.add(jta);
    frame.setSize(400,600);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    
    
    class NewMenuListener implements ActionListener{

            @Override
            public void actionPerformed(ActionEvent e) {
                jta.setText("");
            }
    }
    class SaveMenuListener implements ActionListener{

                @Override
                public void actionPerformed(ActionEvent e) {
                filechooser=new JFileChooser();
                int returnVal=filechooser.showSaveDialog(frame);
                    //File selected
                if(returnVal==JFileChooser.APPROVE_OPTION)
                {
                    System.out.println("You saved: "+filechooser.getSelectedFile());
                try{
                    FileWriter writer=new FileWriter(filechooser.getSelectedFile());
                    writer.write(jta.getText()+"\n");
                }catch(IOException io)
                {
                    io.printStackTrace();
                }                
                }
                }
    }
    class OpenMenuListener implements ActionListener{

            @Override
            public void actionPerformed(ActionEvent e) {
                filechooser = new JFileChooser();
			int returnVal=filechooser.showOpenDialog(frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				System.out.println("Opening: " + filechooser.getSelectedFile());
				try {
                                    String filepath;
                                    File myFile=filechooser.getSelectedFile();
                                    filepath=myFile.getAbsolutePath();
                                    FileReader filereader=new FileReader(filepath);
                                    BufferedReader breader=new BufferedReader(filereader);
                                    String line=null;
                                    jta.setText("");
                                    
                                    while((line=breader.readLine())!=null)
                                        jta.append(line);
                                    
                                    breader.close();
                                    
            }catch(Exception ex){
            ex.printStackTrace();
            }
    
    }
    }
            
    }
    class ExitMenuListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			frame.dispose();
		}
	}
    
    
}
