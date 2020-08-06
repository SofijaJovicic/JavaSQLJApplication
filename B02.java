// Sofija Jovicic
// 163/2010
// 4R

package sofija;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.*;
import java.lang.*;
import java.util.*;
import java.util.Date;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


//glavna javna klasa koja implementira interfejs ActionListener kako bi mogla da osluskuje
public class B02 implements ActionListener
{
	
	//pravimo staticki GUI
	static GUI gui = new GUI();
	
	//sadrzaj koji ispisujemo u izvestaj, smerove, i promenljive za naziv izlaznog fajla
	static String tekstIzvestaj="";
	static String tekstSmerovi="";
	static String oznaka_smera="zbirno";
	static String godina_upisa="zbirno";
	
	//predefinisanje apstraktnog metoda actionPerformed interfejsa ActionListener, obavezno je
	public void actionPerformed(ActionEvent e)
	{}
	
	
	static 
	{
		try 
		{
			//ukljucujem driver za db2 sistem
			Class.forName("com.ibm.db2.jcc.DB2Driver");
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	
	public static void main (String argv[])
	{	
		//prikazivanje GUI-ja
		gui.napraviGUI();
		
		//postavljanje akcije kada kliknemo na neko od dugmeta
		ActionListener al=new ActionListener()
		{
			//predefinisanje metoda
			public void actionPerformed(ActionEvent ae)
			{
				//zato sto imam vise buttona a za svaki razlicitu akciju
				Object src=ae.getSource();
				try 
				{
					//pravimo konekciju
					Connection con = null;
					String url = "jdbc:db2://localhost:50001/vstud";
					con = DriverManager.getConnection(url, "student", "abcdef");
					Statement stmt = con.createStatement();
					
					//upit za brisanje tabele kvota ako vec postoji u bazi
					try
					{
						int dropCount;
						String sql1=" drop table kvota ";
						dropCount=stmt.executeUpdate(sql1);
					}
					catch(SQLException e1)
					{
						//ignorisem, jer ne zelim da mi izbaci gresku ako tabela vec postoji, samo da nastavi
						if (e1.getErrorCode() == -911 || e1.getErrorCode() == -913)
						{
							System.out.println("Objekat je zakljucan od strane druge transakcije, sacekajte.");
							con.rollback();
							System.out.println("---Rollback---");
						}
						else
						{
							System.out.println("SQLExeption" + e1.getErrorCode());
						}
					}
					
					//upit za kreiranje tabele kvota
					//try
					//{
						int createCount;
						String sql2=" create table kvota "+
							    "( "+
							    "id_smera integer not null, "+
							    "godina integer not null, "+
							    "br_budzet integer not null, "+
							    "br_samofinansiranje integer not null, "+
							    "primary key (id_smera,godina), "+
							    "foreign key(id_smera) references smer "+
							    ") ";
						createCount = stmt.executeUpdate(sql2);
					//}
					/*catch (SQLException e)
					{
						if (e.getErrorCode() == -911 || e.getErrorCode() == -913)
						{
							System.out.println("Objekat je zakljucan od strane druge transakcije, sacekajte.");
							con.rollback();
							System.out.println("---Rollback---");
						}
						else
						{
							System.out.println("SQLExeption" + e.getErrorCode());
						}
					}*/
					
					//upit za popunjavanje tabele kvota
					try
					{
						int insertCount;
						String sql3=" insert into kvota "+
							    "with aktuelni_status as "+
							    "( "+
							      "select s.indeks,d.id_smera,s.status "+
							      "from status s join dosije d on d.indeks=s.indeks "+
							      "where not exists(select * "+
										"from status s1 "+
										"where s1.indeks=s.indeks and s1.datum<s.datum "+
										") "+
							    ") "+
							    "select a.id_smera,a.indeks/10000,sum(case "+
												  "when a.status='budzet' then 1 "+
												  "else 0 "+
												  "end), "+
											      "sum(case "+
												    "when a.status='samofinansiranje' then 1 "+
												    "else 0 "+
												    "end) "+
							    "from aktuelni_status a "+
							    "group by a.id_smera,a.indeks/10000 ";
						insertCount = stmt.executeUpdate(sql3);
					}
					catch (SQLException e)
					{
						if (e.getErrorCode() == -911 || e.getErrorCode() == -913)
						{
							System.out.println("Objekat je zakljucan od strane druge transakcije, sacekajte.");
							con.rollback();
							System.out.println("---Rollback---");
						}
						else
						{
							System.out.println("SQLExeption" + e.getErrorCode());
						}
					}
					
					//zavrseno je kreiranje i popunjavanje nove tabele kvota
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////			

					ResultSet rs = null;
					//korisnik unosi godinu,id nivoa i id smera u polja
					
					//System.out.println("unesite godinu");
					//Scanner sc=new Scanner(System.in);
					//String godina=sc.next();
					//System.out.println("unesite id_nivoa");
					//String id_nivoa=sc.next();
					//String id_smera=sc.next();
					String godina = gui.field1.getText().trim();
					String id_nivoa = gui.field2.getText().trim();
					String id_smera = gui.field4.getText().trim();
					
					//ako nije nista uneo za godinu
					if(godina.isEmpty())
					{
						//za svaki slucaj update naziva za fajl
						godina_upisa="zbirno";
						//ispisi celu tabelu kvota u izvestaju
						String sql4="select * "+
							  "from kvota ";
						rs=stmt.executeQuery(sql4);
						tekstIzvestaj="| ID_SMERA | GODINA | BR_BUDZ | BR_SAMOF |\n----------------------------------------------------------------------\n";
						while ( rs.next() )
						{
							try
							{
								tekstIzvestaj=tekstIzvestaj+"|     "+rs.getInt(1)+"     |    "+rs.getInt(2)+"    |      "+rs.getInt(3)+"      |       "+rs.getInt(4)+"       |\n";
							}
							catch (SQLException e)
							{
								if (e.getErrorCode() == -911 || e.getErrorCode() == -913)
								{
									System.out.println("Objekat je zakljucan od strane druge transakcije, sacekajte.");
									con.rollback();
									System.out.println("---Rollback---");
								}
								else
								{
									System.out.println("SQLExeption" + e.getErrorCode());
								}
							}
						}
						//System.out.println(tekstIzvestaj);
					}
					//ako je uneo godinu
					else if(!godina.isEmpty())
					{
						//ako godina nije pravilno unesena
						if(!(godina.matches("\\d{4}")))
						{
							tekstIzvestaj="Unesite pravilno godinu.";
						}
						//ako godina jeste pravilno unesena
						else if(godina.matches("\\d{4}"))
						{
							//proveri da li je u bazi ta godina tj da li je tada bio upis
							//izdvajam upitom godine kada je bio upis, iz tabele kvota
							String sql5=" select distinct godina "+
									"from kvota ";
							rs=stmt.executeQuery(sql5);
							//napravicu niz godina iz tabele
							int[] nizGodina=new int[1000];
							int i=0; //brojac kroz niz
							while ( rs.next() )
							{
								try
								{
									nizGodina[i]=rs.getInt(1);
									i++;
								}
								catch (SQLException e)
								{
									if (e.getErrorCode() == -911 || e.getErrorCode() == -913)
									{
										System.out.println("Objekat je zakljucan od strane druge transakcije, sacekajte.");
										con.rollback();
										System.out.println("---Rollback---");
									}
									else
									{
										System.out.println("SQLExeption" + e.getErrorCode());
									}
								}
							}
							//proveravam da li se godina nalazi u listi
							int j;
							for(j=0;j<i;j++)
							{
								//ako smo je nasli u listi, iskoci
								//inace trazi jos
								if(nizGodina[j]==Integer.parseInt(godina))
									break;
							}
							//ako je nismo nasli
							if(i==j)
							{
								tekstIzvestaj="Te godine nije bilo upisa i nema je u tabeli.";
							}
							//ako je ima u bazi
							else if(i!=j)
							{
								godina_upisa=godina.trim();
							
								//ako nije nista uneo za id nivoa
								if(id_nivoa.isEmpty())
								{
									//za svaki slucaj update naziva za fajl
									oznaka_smera="zbirno";
									//ispisi kvote za godinu
									String sql6=" select * "+
										  "from kvota "+
										  "where godina= "+godina+" ";
									rs=stmt.executeQuery(sql6);
									tekstIzvestaj="| ID_SMERA | GODINA | BR_BUDZ | BR_SAMOF |\n----------------------------------------------------------------------\n";
									while ( rs.next() ) 
									{
										try
										{
											tekstIzvestaj=tekstIzvestaj+"|     "+rs.getInt(1)+"     |    "+rs.getInt(2)+"    |      "+rs.getInt(3)+"      |       "+rs.getInt(4)+"       |\n";
										}
										catch (SQLException e)
										{
											if (e.getErrorCode() == -911 || e.getErrorCode() == -913)
											{
												System.out.println("Objekat je zakljucan od strane druge transakcije, sacekajte.");
												con.rollback();
												System.out.println("---Rollback---");
											}
											else
											{
												System.out.println("SQLExeption" + e.getErrorCode());
											}
										}
									}
									//System.out.println(tekstIzvestaj);
								}
								//ako jeste uneo id nivoa
								else if(!id_nivoa.isEmpty())
								{
									//ako id nivoa nije pravilno unet
									if(!(id_nivoa.matches("\\d+")))
									{
										tekstIzvestaj="Unesite pravilno id nivoa.";
										tekstSmerovi="Unesite pravilno id nivoa.";
									}
									//ako id nivoa jeste pravilno unet
									else if(id_nivoa.matches("\\d+"))
									{
										//proveri da li je u bazi taj id_nivoa tj da li postoji upis uopste za neki smer sa tog id_nivoa
										//izdvajam upitom id_nivoa iz tabele smer join kvota da izdvojim samo one za koje postoji upis
										String sql7=" select distinct s.id_nivoa "+
												"from  smer s join kvota k on k.id_smera=s.id_smera ";
										rs=stmt.executeQuery(sql7);
										//napravicu niz id_nivoa iz tabele
										int[] nizIdNivoa=new int[1000];
										int i1=0; //brojac kroz niz
										while ( rs.next() )
										{
											try
											{
												nizIdNivoa[i1]=rs.getInt(1);
												i1++;
											}
											catch (SQLException e)
											{
												if (e.getErrorCode() == -911 || e.getErrorCode() == -913)
												{
													System.out.println("Objekat je zakljucan od strane druge transakcije, sacekajte.");
													con.rollback();
													System.out.println("---Rollback---");
												}
												else
												{
													System.out.println("SQLExeption" + e.getErrorCode());
												}
											}
										}
										//proveravam da li se id_nivoa nalazi u listi
										int j1;
										for(j1=0;j1<i1;j1++)
										{
											//ako smo ga nasli u listi, iskoci
											//inace trazi jos
											if(nizIdNivoa[j1]==Integer.parseInt(id_nivoa))
												break;
										}
										//ako ga nismo nasli
										if(i1==j1)
										{
											tekstIzvestaj="Nema upisa za taj id nivoa.";
											tekstSmerovi="Nema upisanog smera za taj id nivoa.";
										}
										//ako ga ima u bazi
										else if(i1!=j1)
										{
											//prikazi id_smera za sve smerove sa tog nivoa kvalifikacije
											String sql8=" select distinct k.id_smera,s.naziv "+
													"from kvota k join smer s on k.id_smera=s.id_smera "+
													"where s.id_nivoa= "+id_nivoa+" and k.godina= "+godina+" ";
											rs=stmt.executeQuery(sql8);
											tekstSmerovi="| ID_SMERA | NAZIV |\n---------------------------------------------------------------------------------------\n";
											//napravicu niz id smerova koje ce korisnik moci da bira, i koji su bili ispisani u textfieldu, i ako ne izabere odatle, javicu gresku
											int[] nizIdSmera=new int[1000];
											int i2=0; //brojac kroz niz
											while ( rs.next() )
											{
												try
												{
													tekstSmerovi=tekstSmerovi+"|     "+rs.getInt(1)+"     | "+rs.getString(2).trim()+"\n";
													nizIdSmera[i2]=rs.getInt(1);
													i2++;
												}
												catch (SQLException e)
												{
													if (e.getErrorCode() == -911 || e.getErrorCode() == -913)
													{
														System.out.println("Objekat je zakljucan od strane druge transakcije, sacekajte.");
														con.rollback();
														System.out.println("---Rollback---");
													}
													else
													{
														System.out.println("SQLExeption" + e.getErrorCode());
													}
												}
											}
											//ako je object src prvo dugme SMEROVI, ispisi sve smerove sa tog nivoa kvalif.
											//if(src==gui.button1)
											//{
												//tekstSmerovi=tekstSmerovi.trim();
												//gui.text3.setText(tekstSmerovi);
											//}
											//tekstSmerovi=tekstSmerovi.trim();
											//gui.text3.setText(tekstSmerovi);
											//System.out.println(tekstSmerovi);
											//System.out.println("unesite id_smera od ponudjenih");
											
											//ako korisnik nije uneo id smera uopste
											if(id_smera.isEmpty())
											{
												//morate uneti smer
												tekstIzvestaj="Morate uneti i smer.";
												
											}
											//ako je korisnik uneo id smera
											if(!id_smera.isEmpty())
											{
												//ako nije pravilno unet id smera
												if(!(id_smera.matches("\\d+")))
												{
													tekstIzvestaj="Unesite pravilno id smera.";
												}
												//ako jeste pravilno unet id smera
												else if(id_smera.matches("\\d+"))
												{
													//proveravam da li se id smera nalazi u listi
													int j2;
													for(j2=0;j2<i2;j2++)
													{
														//ako smo ga nasli u listi, iskoci
														//inace trazi jos
														if(nizIdSmera[j2]==Integer.parseInt(id_smera))
															break;
													}
													//ako ga nismo nasli
													if(i2==j2)
													{
														tekstIzvestaj="Morate uneti id_smera iz liste.";
													}
													else if(i2!=j2)
													{
														//izdvoj oznaku smera zbog naziva fajla, treba nam upit
														String sql9=" select oznaka "+
																"from smer "+
																"where id_smera= "+id_smera+" ";
														rs=stmt.executeQuery(sql9);
														while( rs.next() )
														{
															try
															{
																oznaka_smera=rs.getString(1).trim();
															}
															catch (SQLException e)
															{
																if (e.getErrorCode() == -911 || e.getErrorCode() == -913)
																{
																	System.out.println("Objekat je zakljucan od strane druge transakcije, sacekajte.");
																	con.rollback();
																	System.out.println("---Rollback---");
																}
																else
																{
																	System.out.println("SQLExeption" + e.getErrorCode());
																}
															}
														}
													
														//ispisiTabeluGodineSmer
														String sql10=" select * "+
															    "from kvota "+
															    "where godina= "+godina+" and id_smera= "+id_smera+" ";
														rs=stmt.executeQuery(sql10);
														tekstIzvestaj="| ID_SMERA | GODINA | BR_BUDZ | BR_SAMOF |\n----------------------------------------------------------------------\n";
														while ( rs.next() ) 
														{
															try
															{
																tekstIzvestaj=tekstIzvestaj+"|     "+rs.getInt(1)+"     |    "+rs.getInt(2)+"    |      "+rs.getInt(3)+"      |       "+rs.getInt(4)+"       |\n";
															}
															catch (SQLException e)
															{
																if (e.getErrorCode() == -911 || e.getErrorCode() == -913)
																{
																	System.out.println("Objekat je zakljucan od strane druge transakcije, sacekajte.");
																	con.rollback();
																	System.out.println("---Rollback---");
																}
																else
																{
																	System.out.println("SQLExeption" + e.getErrorCode());
																}
															}
														}
														//System.out.println(tekstIzvestaj);
													}
												}
											}
										}
									}
									//ako je object src prvo dugme SMEROVI, ispisi sve smerove sa tog nivoa kvalif.
									if(src==gui.button1)
									{
										tekstSmerovi=tekstSmerovi.trim();
										gui.text3.setText(tekstSmerovi);
									}
								}
							}
						}
					}
					
					//ako korisnik nije uneo godinu a jeste bilo sta drugo
					if(godina.isEmpty() && (!id_nivoa.isEmpty() || !id_smera.isEmpty()))
					{
						//?????????????????????????????????????????? menjati zbirno...ma jok
						tekstIzvestaj="Morate uneti i godinu ako zelite id nivoa ili id smera.";
					}
					//ako je uneo godinu i id smera, a preskocio id nivoa
					if(!godina.isEmpty() && id_nivoa.isEmpty() && !id_smera.isEmpty())
					{
						tekstIzvestaj="Morate uneti id nivoa pa tek onda id smera.";
					}
					
					//ako je object src drugo dugme PRIKAZI, onda ispisi izvestaj u textfiledu
					if(src==gui.button2)
					{
						tekstIzvestaj=tekstIzvestaj.trim();
						gui.text5.setText(tekstIzvestaj);
					}
					//ako smo, pak, kliknuli na dugme STAMPANJE, onda pisemo u fajl izvestaj
					if(src==gui.button3)
					{
						//upisujemo formirani izvestaj u fajl kvote.oznaka_smera.godina_upisa
						try
						{
							//svaki izvestaj mora imati zaglavlje sa datumom i vremenom i Matem.fakultet
							//pravimo trenutni datum i vreme u formatu koji zelimo
							DateFormat df=new SimpleDateFormat("dd.MM.yyyy. HH:mm:ss");
							Date d=new Date();
							String datum=df.format(d);
							//formiramo string zaglavlje
							String zaglavlje="Matematicki fakultet,\n"+datum;
							//formiramo ime fajla
							String ime_fajla="kvote."+oznaka_smera+"."+godina_upisa;
							//pravimo fajl i ispisujemo u njega
							BufferedWriter fileOut = new BufferedWriter(new FileWriter(ime_fajla));
							fileOut.write(zaglavlje+"\n\n\n"+tekstIzvestaj);
							//zatvaramo fajl
							fileOut.close();
						}
						catch (IOException ioe)
						{
							ioe.printStackTrace();
						}
					}
					
					
					//zavrsili smo obradu jednog upisa, jednu transakciju i radimo commit
					con.commit();
					//zatvaramo naredbe i raskidamo konekciju
					rs.close();
					stmt.close();
					con.close();
				} //kraj najveceg try
				catch (SQLException e2) 
				{
					//mozemo da ispisemo greske
					System.out.println("SQLCODE: " +e2.getErrorCode() + " SQLSTATE: " + e2.getSQLState() + " PORUKA: " + e2.getMessage()); 
				}
				catch (Exception e3)
				{
					e3.printStackTrace();
				}
				
			} //kraj action performed
		}; //kraj action listener
		
		//svakom od buttona dodajemo osluskivac
		gui.button1.addActionListener(al);
		gui.button2.addActionListener(al);
		gui.button3.addActionListener(al);
						
		
		//dugme KRAJ, ovde cemo anonimnu klasu
		gui.button4.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae3)
			{
				gui.frame.dispose();
			}
		}
		);
	} //kraj main
}