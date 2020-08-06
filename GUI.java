// Sofija Jovicic
// 163/2010
// 4R

package sofija;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*; 

public class GUI //implements ActionListener
{	
	//public void actionPerformed(ActionEvent e)
	//{}
	
	//deklaracija svih komponenti potrebnih za GUI, izdvojene iz funkcije napraviGUI()
	//jer nam trebaju u drugoj klasi
	
	//pravimo frame, okvir i dodajemo naslov
	JFrame frame = new JFrame("Pregled i stampanje kvota za upis");
    
    //panel u koji se smestaju komponente, stavicemo mrezni raspored
    JPanel panel = new JPanel(new GridBagLayout());

    //labela koja stoji pored textualnog polja u koje cemo uneti godinu
    JLabel label1 = new JLabel("godina: ");

    //textualno polje u koje unosimo godinu
    JTextField field1 = new JTextField(); 
    
    //labela koja stoji pored textualnog polja u koje cemo uneti id nivoa
    JLabel label2 = new JLabel("id nivoa: ");
    
    //textualno polje u koje unosimo id nivoa
    JTextField field2 = new JTextField();

    //dugme za ispis smerova
    JButton button1 = new JButton("SMEROVI");
    
    //labela koja stoji pored textualnog polja u kome ce se ispisati smerovi za unet id nivoa
    JLabel label3 = new JLabel("smerovi: ");
    
    //textualna oblast za ispis smerova
    JTextArea text3 = new JTextArea(" ",12, 12);
    
    //labela koja stoji pored textualnog polja u koje se unosi id smera
    JLabel label4 = new JLabel("id smera: ");
    
    //textualno polje u koje unosimo id smera
    JTextField field4 = new JTextField();
    
    //dugme za prikaz izvestaja u textfiledu
    JButton button2 = new JButton("PRIKAZ");
    
    //labela koja stoji pored textualnog polja u kome ce se ispisati izvestaj
    JLabel label5 = new JLabel("izvestaj: ");
    
    //textualna oblast za ispis izvestaja
    JTextArea text5 = new JTextArea(" ",12, 32);

    //dugme za stampanje izvestaja u fajl
    JButton button3 = new JButton("STAMPANJE");
    
    //dugme za izlaz iz programa
    JButton button4 = new JButton("   KRAJ   ");
   

    //fja koja pravi gui
	public void napraviGUI()
	{
	    //postavljanje izlaska iz programa na x
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    
	    //postavljanje velicine prozora
	    frame.setSize(700,600);
	    
	    //dodavanje panela na okvir severno
	    frame.getContentPane().add(panel, BorderLayout.NORTH);
	    
	    //postavljanje koordinatnog sistema pomocu kojeg cemo rasporedjivati komponente na panelu
	    GridBagConstraints xy = new GridBagConstraints();
	    
	   
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    //postavljanje koordinata labele za godinu
	    xy.gridx = 1;
	    xy.gridy = 1;
	    //definisanje rastojanja labele godina od ostalih komponenti
	    xy.insets = new Insets(5,5,5,5);
	    //dodavanje labele godina na panel
	    panel.add(label1, xy);
	
	    //postavljanje velicine tekstualnog polja u koje treba uneti godinu
	    field1.setPreferredSize(new Dimension(100,20));
	    //postavljanje pozadinske boje na belu, MADA bih ja volela da bude ROZE <3
	    field1.setBackground(Color.WHITE);
	    //postavljanje mogucnosti pisanja u polje u koje treba uneti godinu
	    field1.setEnabled(true);
	    //postavljanje koordinata za tekstualno polje u kome treba uneti godinu
	    xy.gridx = 2;
	    xy.gridy = 1;
	    //definisanje rastojanja tekstualnog polja od ostalih komponenti
	    xy.insets = new Insets(5,5,5,5);
	    //dodavanje tekstualnog polja na panel
	    panel.add(field1, xy);
	    
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    //postavljanje koordinata za labelu koja pokazuje na tekstualno polje u koje treba uneti id nivoa
	    xy.gridx = 1;
	    xy.gridy = 2;
	    //definisanje rastojanja labele od ostalih komponenti
	    xy.insets = new Insets(5,5,5,5);
	    //dodavanje labele id nivoa na panel
	    panel.add(label2,xy);  
	       
	    //postavljanje velicinje tekstualnog polja u koje treba uneti id nivoa
	    field2.setPreferredSize(new Dimension(100,20));
	    //postavljanje pozadinske boje tekstualnog polja na belu
	    field2.setBackground(Color.WHITE);
	    //postavljanje mogucnosti pisanja u polje
	    //mogucnost upisivanja u polje na true
	    field2.setEnabled(true);
	    //postavljanje koordinata za id nivoa
	    xy.gridx = 2;
	    xy.gridy = 2;
	    //rastojanje tekstualnog polja od ostalih komponenti
	    xy.insets = new Insets(5,5,5,5);
	    //dodavanje tekstualnog polja na panel
	    panel.add(field2,xy);
	    
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    //postavljanje koordinata na kojima smestamo dugme za gotov unos do smera
	    xy.gridx = 2;
	    xy.gridy = 3;
	    //definisanje rastojanja tog dugmeta od ostalih komponenti
	    xy.insets = new Insets(5,5,5,5);
	    //dodavanje dugmeta na panel
	    panel.add(button1, xy);
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    //postavljanje koordinata za labelu koja upucuje na tekstualno polje za ispis smerova
	    xy.gridx = 1;
	    xy.gridy = 4;
	    //definisanje rastojanja te labele od ostalih komponenti
	    xy.insets = new Insets(5,5,5,5);
	    //dodavanje labele na panel
	    panel.add(label3,xy);  
	    
	    //ukidanje mogucnosti pisanja u tekstualnu oblast za smerove od strane korisnika
	    text3.setEditable(false);
	    //postavljanje koordinata na koje smestamo tu tekstualnu oblast
	    xy.gridx = 2;
	    xy.gridy = 4;
	    //definisanje rastojanja tekstualne oblasti od ostalih komponenti
	    xy.insets = new Insets(5,5,5,5);
	    //dodavanje tekstualne oblasti na panel
	    panel.add(text3,xy);
	    //dodajemo scroll bar, oba
	    JScrollPane scroll1 = new JScrollPane(text3);
	    scroll1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    scroll1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
	    panel.add(scroll1,xy);
	
////////////////////////////////////////////////////////////////////////////////////////////////////////////////    
	    //postavljanje koordinata za labelu koja pokazuje na tekstualno polje u koje treba uneti id smera
	    xy.gridx = 1;
	    xy.gridy = 5;
	    //rastojanje labele od ostalih komponenti
	    xy.insets = new Insets(5,5,5,5);
	    //dodavanje labele na panel
	    panel.add(label4,xy);  
	       
	    //postavljanje velicinje tekstualnog polja u koje treba uneti id smera
	    field4.setPreferredSize(new Dimension(100,20));
	    //postavljanje pozadinske boje na belu
	    field4.setBackground(Color.WHITE);
	    //postavljanje mogucnosti pisanja u polje na true
	    field4.setEnabled(true);
	    //postavljanje koordinata za tekstualno polje
	    xy.gridx = 2;
	    xy.gridy = 5;
	    //definisanje rastojanja tekstualnog polja od ostalih komponenti
	    xy.insets = new Insets(5,5,5,5);
	    //dodavanje tekstualnog polja na panel
	    panel.add(field4,xy);
	
//////////////////////////////////////////////////////////////////////////////////////////////////////////////    
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    //postavljanje koordinata za dugme za ispis izvestaja
	    xy.gridx = 4;
	    xy.gridy = 2;
	    //definisanje rastojanja tog dugmeta od ostalih komponenti
	    xy.insets = new Insets(5,5,5,5);
	    //dodavanje dugmeta na panel
	    panel.add(button2, xy);

	    //postavljanje koordinata za labelu koja upucuje na tekstualno polje za ispis izvestaja
	    xy.gridx = 3;
	    xy.gridy = 3;
	    //definisanje rastojanja te labele od ostalih komponenti
	    xy.insets = new Insets(5,5,5,5);
	    //dodavanje labele na panel
	    panel.add(label5,xy);
	    
	    //ukidanje mogucnosti pisanja u tekstualnu oblast od strane korisnika za izvestaj
	    text5.setEditable(false);
	    //postavljanje koordinata za tu tekstualnu oblast
	    xy.gridx = 4;
	    xy.gridy = 3;
	    //definisanje rastojanja tekstualne oblasti od ostalih komponenti
	    xy.insets = new Insets(5,5,5,5);
	    //dodavanje tekstualne oblasti na panel
	    panel.add(text5,xy);
	    //dodajemo scroll bar, oba
	    JScrollPane scroll2 = new JScrollPane(text5);
	    scroll2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    scroll2.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
	    panel.add(scroll2,xy);
	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    //postavljanje koordinata za dugme za stampanje u fajl
	    xy.gridx = 4;
	    xy.gridy = 4;
	    //definisanje rastojanja tog dugmeta od ostalih komponenti
	    xy.insets = new Insets(5,5,5,5);
	    //dodavanje dugmeta za stampanje na panel
	    panel.add(button3, xy);
	    
	    //postavljanje koordinata za dugme za izlaz iz programa
	    xy.gridx = 4;
	    xy.gridy = 10;
	    //definisanje rastojanja tog dugmeta od ostalih komponenti
	    xy.insets = new Insets(5,5,5,5);
	    //dodavanje dugmeta za izlaz iz programa na panel
	    panel.add(button4, xy);

////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	    
	    //postavljamo da okvir bude vidljiv
	    frame.setVisible(true);
	}
}
