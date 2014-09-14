package texteditor;

import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import static java.awt.event.KeyEvent.VK_S;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;


public class TextEditor {

    UndoManager undoManager=new UndoManager();
    JFrame frame;
    JFileChooser filechooser;
    JPanel panel;
    JTextArea jta;
    JTextField tf;
    File f;
    Font f1;
    String copied;
    
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
        
        f1=new Font("Lucida Console",Font.PLAIN,15);
        jta.setFont(f1);
    
        tf=new JTextField();
           
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
    
    //Adding Menu Items to the Menu
    file.add(newMenuItem);
    file.add(openMenuItem);
    file.add(saveMenuItem);
    file.add(saveAsMenuItem);
    file.addSeparator();
    file.add(exitMenuItem);
    
    
    JMenu edit=new JMenu("Edit");
    
    JMenuItem redoMenuItem=new JMenuItem("Redo");
    redoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,InputEvent.CTRL_MASK));
    
    
    JMenuItem undoMenuItem=new JMenuItem("Undo");
    undoMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,InputEvent.CTRL_MASK));
    undoMenuItem.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent event)
         {
            if (undoManager.canUndo())
            {
               undoManager.undo();
            }
            undoMenuItem.setEnabled(undoManager.canUndo());
            redoMenuItem.setEnabled(undoManager.canRedo());
         }
      });
    
     redoMenuItem.addActionListener(new ActionListener()
      {
         public void actionPerformed(ActionEvent event)
         {
            if (undoManager.canRedo())
            {
               undoManager.redo();
            }
            undoMenuItem.setEnabled(undoManager.canUndo());
            redoMenuItem.setEnabled(undoManager.canRedo());
         }
      });
    
    
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
    
    //Adding Menu Items to the Menu
    edit.add(undoMenuItem);
    edit.add(redoMenuItem);
    edit.addSeparator();
    edit.add(cutMenuItem);
    edit.add(copyMenuItem);
    edit.add(pasteMenuItem);
    
    
    JMenu search=new JMenu("Search");
      
    JMenuItem look=new JMenuItem("Find");
    look.setMnemonic(VK_S);
    look.addActionListener(new Find(jta));
    
    search.add(look);
       
    JMenu format=new JMenu("Format");
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
    font.add(name1);
    font.add(name2);
    font.add(name3);
    font.add(name4);
    
    JMenuItem sname1=new JMenuItem("Regular");
    sname1.addActionListener(new FontMenuR());
    JMenuItem sname2=new JMenuItem("Bold");
    sname2.addActionListener(new FontMenuB());
    JMenuItem sname3=new JMenuItem("Italic");
    sname3.addActionListener(new FontMenuI());
    
    font1.add(sname1);
    font1.add(sname2);
    font1.add(sname3);
    
    JMenuItem zname1=new JMenuItem("12");
    zname1.addActionListener(new FontMenuTwelve());
    JMenuItem zname2=new JMenuItem("14");
    zname2.addActionListener(new FontMenuFourteen());
    JMenuItem zname3=new JMenuItem("18");
    zname3.addActionListener(new FontMenuEighteen());
    JMenuItem zname4=new JMenuItem("20");
    zname4.addActionListener(new FontMenuTwenty());
    
    font2.add(zname1);
    font2.add(zname2);
    font2.add(zname3);
    font2.add(zname4);
    
    format.add(font);
    format.add(font1);
    format.add(font2);
    
    menubar.add(file);
    menubar.add(edit);
    menubar.add(format);
    menubar.add(search);
    menubar.setVisible(true);
   
    frame.setJMenuBar(menubar);
    frame.getContentPane().add(panel);
    panel.setLayout(new BorderLayout());
    panel.add(jta);
    
    frame.setSize(600,600);
    
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
    class ExitMenuListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			frame.dispose();
		}
	}
    
            
     
    
    private class CutListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            copied=jta.getSelectedText();
            jta.replaceSelection("");
        }
    
    }
    
    private class CopyListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            copied=jta.getSelectedText();
        }
    
    }
    private class PasteListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            jta.insert(copied, jta.getCaretPosition());
        }
    
    }
    
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



