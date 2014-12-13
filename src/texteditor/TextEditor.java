package texteditor;

import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import static java.awt.event.KeyEvent.VK_S;
import java.io.*;
import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;


public class TextEditor {

    UndoManager undoManager = new UndoManager();
    JFrame frame;
    JFileChooser filechooser;
    JPanel panel;
    JTextArea jta;
    JTextField tf;
    File f;
    Font f1;
    String copied;
    
    // Text Editor runs from here
    public static void main(String[] args) {
        TextEditor editor=new TextEditor();
        editor.run();
    }
    
    public void run()
    {
        frame=new JFrame("AmsEditor");
        panel=new JPanel();
        jta=new JTextArea();
        
        
        //Word Wrapping
        jta.setLineWrap(true);
        jta.setWrapStyleWord(true);
        
        //Initial fonts
        f1=new Font("Lucida Console",Font.PLAIN,15);
        jta.setFont(f1);
    
        tf=new JTextField();
           
        //Look and Feel of Editor
        try{
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        }catch(Exception ex)
        {
            ex.printStackTrace();
        }
    
    JMenuBar menubar=new JMenuBar();
    JMenu file=new JMenu("File");
    
    //Adding Menu Items
    
    //File Menu options
    JMenuItem newMenuItem=new JMenuItem("New");
    newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,InputEvent.CTRL_MASK));
    newMenuItem.addActionListener(new NewMenuListener());
    
    JMenuItem saveMenuItem=new JMenuItem("Save");
    saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,InputEvent.CTRL_MASK));
    saveMenuItem.addActionListener(new SaveMenuListener());
    saveMenuItem.setEnabled(false);
    
    JMenuItem saveAsMenuItem=new JMenuItem("Save As");
    filechooser=new JFileChooser();
    saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,InputEvent.CTRL_MASK));
    saveAsMenuItem.addActionListener(new SaveAsMenuListener());
    
    JMenuItem openMenuItem=new JMenuItem("Open");
    filechooser= new JFileChooser();
    openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,InputEvent.CTRL_MASK)); 
    openMenuItem.addActionListener(new OpenMenuListener());
    
    JMenuItem exitMenuItem=new JMenuItem("Exit");
    saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,InputEvent.CTRL_MASK));
    exitMenuItem.addActionListener(new ExitMenuListener());
    
    //Adding File Menu Items to the Menu
    file.add(newMenuItem);
    file.add(openMenuItem);
    file.add(saveMenuItem);
    file.add(saveAsMenuItem);
    file.addSeparator();
    file.add(exitMenuItem);
    
    
    JMenu edit=new JMenu("Edit");
    
    // Adding Edit Menu Items to Menu
    JMenuItem undoMenuItem=new JMenuItem("Undo");
    undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,InputEvent.CTRL_MASK));
    jta.getDocument().addUndoableEditListener(new UndoableEditListener() {

            @Override
            public void undoableEditHappened(UndoableEditEvent e) {
                undoManager.addEdit(e.getEdit());
            }
        });
    
    
    JMenuItem redoMenuItem=new JMenuItem("Redo");
    redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,InputEvent.CTRL_MASK));
    redoMenuItem.addActionListener(new redo());
    
    JMenuItem cutMenuItem=new JMenuItem("Cut");
    cutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,InputEvent.CTRL_MASK));
    cutMenuItem.addActionListener(new CutListener());
    
    JMenuItem copyMenuItem=new JMenuItem("Copy");
    copyMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,InputEvent.CTRL_MASK)); 
    copyMenuItem.addActionListener(new CopyListener());
    
    JMenuItem pasteMenuItem=new JMenuItem("Paste");
    pasteMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,InputEvent.CTRL_MASK));
    pasteMenuItem.addActionListener(new PasteListener());
    
    undoMenuItem.setEnabled(false);
    redoMenuItem.setEnabled(false);
    
    //Adding Edit Menu Items to the Menu
    edit.add(undoMenuItem);
    edit.add(redoMenuItem);
    edit.addSeparator();
    edit.add(cutMenuItem);
    edit.add(copyMenuItem);
    edit.add(pasteMenuItem);
    
    
    //Search Menu
    JMenu search=new JMenu("Search");
      
    //Adding Search Menu items to Menu
    JMenuItem look=new JMenuItem("Find");
    look.setMnemonic(VK_S);
    look.addActionListener(new Find(jta));
    
    search.add(look);
    
    //Format Menu
    JMenu format=new JMenu("Format");
    
    //Adding Format Menu Items to Menu
    JMenu font=new JMenu("Font");
    JMenu font1=new JMenu("Font Style");
    JMenu font2=new JMenu("Font Size");
    
    JMenuItem name1=new JMenuItem("Courier");
    name1.addActionListener(new FontMenuC());
    
    JMenuItem name2=new JMenuItem("Sans Serif");
    name2.addActionListener(new FontMenuS());
    
    JMenuItem name3=new JMenuItem("Monotype Corsiva");
    name3.addActionListener(new FontMenuM());
    
    JMenuItem name4=new JMenuItem("Arial");
    name4.addActionListener(new FontMenuA());
    
    //Adding menu items
    font.add(name1);
    font.add(name2);
    font.add(name3);
    font.add(name4);
    
    //Adding font types
    JMenuItem sname1=new JMenuItem("Regular");
    sname1.addActionListener(new FontMenuR());
    
    JMenuItem sname2=new JMenuItem("Bold");
    sname2.addActionListener(new FontMenuB());
    
    JMenuItem sname3=new JMenuItem("Italic");
    sname3.addActionListener(new FontMenuI());
    
    //Adding menu items
    font1.add(sname1);
    font1.add(sname2);
    font1.add(sname3);
    
    //Adding font sizes
    JMenuItem zname1=new JMenuItem("12");
    zname1.addActionListener(new FontMenuTwelve());
    
    JMenuItem zname2=new JMenuItem("14");
    zname2.addActionListener(new FontMenuFourteen());
    
    JMenuItem zname3=new JMenuItem("18");
    zname3.addActionListener(new FontMenuEighteen());
    
    JMenuItem zname4=new JMenuItem("20");
    zname4.addActionListener(new FontMenuTwenty());
    
    //Adding menu items
    font2.add(zname1);
    font2.add(zname2);
    font2.add(zname3);
    font2.add(zname4);
    
    
    //Adding Format Menu items to menu
    format.add(font);
    format.add(font1);
    format.add(font2);
    
    
    
    //Adding menu items to menu bar
    menubar.add(file);
    menubar.add(edit);
    menubar.add(format);
    menubar.add(search);
    menubar.setVisible(true);
   
    
    //Putting it all together
    frame.setJMenuBar(menubar);
    frame.getContentPane().add(panel);
    panel.setLayout(new BorderLayout());
    panel.add(jta);
    
    frame.setSize(600,600);
    
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    
    //Implementing New Option
    class NewMenuListener implements ActionListener{

            @Override
            public void actionPerformed(ActionEvent e) {
                jta.setText("");
            }
    }
    
    //Implementing Save Option
    class SaveMenuListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            FileWriter filew=null;
            try{
            filew=new FileWriter(f);
            
            }catch(IOException e1){
                e1.printStackTrace();
            }
            PrintWriter fw=new PrintWriter(filew);
            fw.print(jta.getText()+"\n");
        }
    
    }
    
    //Implementing Save As Option
    class SaveAsMenuListener implements ActionListener{

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
    
    //Implementing Open Option
    class OpenMenuListener implements ActionListener{

            @Override
            public void actionPerformed(ActionEvent e) {
                filechooser = new JFileChooser();
			int returnVal=filechooser.showOpenDialog(frame);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				System.out.println("Opening: " + filechooser.getSelectedFile());
                                frame.setTitle("AmsEditor-"+f);
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
    
    //Implementing Exit Option
    class ExitMenuListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			frame.dispose();
		}
	}
    
    
    ////Implementing Undo Option
     class undoManager implements ActionListener
    {
         public void actionPerformed(ActionEvent event)
         {
            try{
                undoManager.undo();
            }catch(CannotUndoException cue){
                cue.printStackTrace();
            }
         }
      }
     
     //Implementing Redo Option
    class redo implements ActionListener
    {
         public void actionPerformed(ActionEvent event)
         {
            try{
                undoManager.redo();
            }catch(CannotRedoException cre){
                cre.printStackTrace();
            }
    
         }
      }
            
     
    //Implementing Cut Option
    private class CutListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            copied=jta.getSelectedText();
            jta.replaceSelection("");
        }
    
    }
    
    //Implementing Copy Option
    private class CopyListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            copied=jta.getSelectedText();
        }
    
    }
    
    //Implementing Paste Option
    private class PasteListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            jta.insert(copied, jta.getCaretPosition());
        }
    
    }
    
    //Implementing Find Option
    class Find implements ActionListener{
        private Highlighter hilit;
        private Highlighter.HighlightPainter painter=new DefaultHighlighter.DefaultHighlightPainter(Color.MAGENTA);
        private Color HILIT_COLOR=Color.ORANGE;
        private String entry;
        private String text;
        private int index;
        private int end;
        Find(JTextArea ta)
        {jta=ta;}
        
        @Override
        public void actionPerformed(ActionEvent e) {
        
            int option=0;
            removeHighlights(jta);
            String pattern=JOptionPane.showInputDialog(null,"Enter word to be searched: ",JOptionPane.QUESTION_MESSAGE);
            
            if((pattern!=null) && (pattern!=""))
            {
                try{
                pattern=pattern.toUpperCase();
                hilit=jta.getHighlighter();
                Document doc=jta.getDocument();
                text=doc.getText(0, doc.getLength());
                text=text.toUpperCase();
                int pos=0;
            
                while((pos=text.indexOf(pattern,pos))>=0&&(option==0))
                {
                    Rectangle rect=jta.modelToView(pos);
                    jta.scrollRectToVisible(rect);
                    hilit.addHighlight(pos,pos+pattern.length(),painter);
                    option=JOptionPane.showOptionDialog(null, "Find next match", "Search",JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Next","Cancel"}, null);
                    if(option!=0)
                        break;
                    hilit.removeAllHighlights();
                    pos+=pattern.length();
                }
            
                if(option==0)
                {
                    JOptionPane.showMessageDialog(null,"No more matches found","Search",JOptionPane.PLAIN_MESSAGE );
                }
                }
                catch(BadLocationException d)
                    {
                        d.printStackTrace();
                    }
            
            }
            }
        
        public void removeHighlights(JTextComponent jtc)
        {
            Highlighter highlight=jtc.getHighlighter();
            Highlighter.Highlight hilites[]=highlight.getHighlights();
            for(int i=0;i<hilites.length;i++)
            {
                if(hilites[i].getPainter() instanceof MyHighlightPainter)
                {
                    highlight.removeHighlight(hilites[i]);
                }
            }
    
    }
        
        
    class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter
	{
		public MyHighlightPainter(Color color)
		{
                    super(color);
		}
	}
    
        
    
    }
    
    
    
    
    //Implementing Font Style Courier Option
    class FontMenuC implements ActionListener{
        
        
        @Override
        public void actionPerformed(ActionEvent e) {
            
            String fontname=f1.getName();
            String fontfamily=f1.getFamily();
            int fontstyle=f1.getStyle();
            int fontsize=f1.getSize();
            f1=new Font("Courier",fontstyle,fontsize);
            jta.setFont(f1);
            
            
        }
    }
    
    
    //Implementing Font Style Sans Serif Option
    class FontMenuS implements ActionListener{
        
        
        @Override
        public void actionPerformed(ActionEvent e) {
            
            String fontname=f1.getName();
            String fontfamily=f1.getFamily();
            int fontstyle=f1.getStyle();
            int fontsize=f1.getSize();
            f1=new Font("Sans Serif",fontstyle,fontsize);
            jta.setFont(f1);
            
            
        }
    }

    
    //Implementing Font Style Monotype Corsiva Option
    class FontMenuM implements ActionListener{
        
        
        @Override
        public void actionPerformed(ActionEvent e) {
            
            String fontname=f1.getName();
            String fontfamily=f1.getFamily();
            int fontstyle=f1.getStyle();
            int fontsize=f1.getSize();
            f1=new Font("Monotype Corsiva",fontstyle,fontsize);
            jta.setFont(f1);
            
            
        }
    }

    
    //Implementing Font Style Arial Option
class FontMenuA implements ActionListener{
        
        
        @Override
        public void actionPerformed(ActionEvent e) {
            
            String fontname=f1.getName();
            String fontfamily=f1.getFamily();
            int fontstyle=f1.getStyle();
            int fontsize=f1.getSize();
            f1=new Font("Arial",fontstyle,fontsize);
            jta.setFont(f1);
            
            
        }
    }


//Implementing Font Type Regular Option
class FontMenuR implements ActionListener{
        
        
        @Override
        public void actionPerformed(ActionEvent e) {
            
            String fontname=f1.getName();
            String fontfamily=f1.getFamily();
            int fontstyle=f1.getStyle();
            int fontsize=f1.getSize();
            f1=new Font(fontname,Font.PLAIN,fontsize);
            jta.setFont(f1);
            
            
        }
    }


//Implementing Font Type Bold Option
class FontMenuB implements ActionListener{
        
        
        @Override
        public void actionPerformed(ActionEvent e) {
            
            String fontname=f1.getName();
            String fontfamily=f1.getFamily();
            int fontstyle=f1.getStyle();
            int fontsize=f1.getSize();
            f1=new Font(fontname,Font.BOLD,fontsize);
            jta.setFont(f1);
            
            
        }
    }


//Implementing Font Type Italics Option
class FontMenuI implements ActionListener{
        
        
        @Override
        public void actionPerformed(ActionEvent e) {
            
            String fontname=f1.getName();
            String fontfamily=f1.getFamily();
            int fontstyle=f1.getStyle();
            int fontsize=f1.getSize();
            f1=new Font(fontname,Font.ITALIC,fontsize);
            jta.setFont(f1);
            
            
        }
    }



//Implementing Font Size 12 Option
class FontMenuTwelve implements ActionListener{
        
        
        @Override
        public void actionPerformed(ActionEvent e) {
            
            String fontname=f1.getName();
            String fontfamily=f1.getFamily();
            int fontstyle=f1.getStyle();
            int fontsize=f1.getSize();
            f1=new Font(fontname,fontstyle,12);
            jta.setFont(f1);
            
            
        }
    }


//Implementing Font Size 14 Option
class FontMenuFourteen implements ActionListener{
        
        
        @Override
        public void actionPerformed(ActionEvent e) {
            
            String fontname=f1.getName();
            String fontfamily=f1.getFamily();
            int fontstyle=f1.getStyle();
            int fontsize=f1.getSize();
            f1=new Font(fontname,fontstyle,14);
            jta.setFont(f1);
            
            
        }
    }


//Implementing Font Size 18 Option
class FontMenuEighteen implements ActionListener{
        
        
        @Override
        public void actionPerformed(ActionEvent e) {
            
            String fontname=f1.getName();
            String fontfamily=f1.getFamily();
            int fontstyle=f1.getStyle();
            int fontsize=f1.getSize();
            f1=new Font(fontname,fontstyle,18);
            jta.setFont(f1);
            
            
        }
    }


//Implementing Font Size 20 Option
class FontMenuTwenty implements ActionListener{
        
        
        @Override
        public void actionPerformed(ActionEvent e) {
            
            String fontname=f1.getName();
            String fontfamily=f1.getFamily();
            int fontstyle=f1.getStyle();
            int fontsize=f1.getSize();
            f1=new Font(fontname,fontstyle,20);
            jta.setFont(f1);
            
            
        }
    }

}

