package server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
@WebListener
public class App implements ServletContextListener {
		public static ArrayList<User> userbase=new ArrayList<User>();
		public static User us3r;
		public static String cine;
		public static int maxIdSlot=0;
		public static int maxIdSug=0;
		public void contextDestroyed(ServletContextEvent arg0) {
			
		}
		public void contextInitialized(ServletContextEvent arg0) {
			imprietenire(us3r);
			update();
			try {Class.forName("com.mysql.jdbc.Driver");} catch (ClassNotFoundException e1) {e1.printStackTrace();}
			Connection con;ResultSet rezultat=null;Statement afirmatie=null;
			try{
				con=DriverManager.getConnection("jdbc:mysql://localhost:3306/time","root","ciocanesti");
				afirmatie=con.createStatement();
				rezultat=afirmatie.executeQuery("select * from user");
				while (rezultat.next()){
					User user=new User(rezultat.getString("nume"), 
							rezultat.getString("prenume"), 
							rezultat.getString("mail"), 
							rezultat.getString("pass"), new ArrayList<Slot>(),
							rezultat.getBoolean("poza")	
								);		
				userbase.add(user);
				user.fbID=rezultat.getString("fbid");user.urlpic=rezultat.getString("urlpic");
				if (user.fbID!=null){user.fb=true;}
				
				}
			} catch (Exception e) {e.printStackTrace();}
			finally{try {
				rezultat.close();
			} catch (SQLException e1) {
				e1.printStackTrace();;;;;;
				
			}try {
				afirmatie.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}}
			System.out.println("Am incarcat oamenii in ram, boss!");										
			try {
				Connection con2=DriverManager.getConnection("jdbc:mysql://localhost:3306/time","root","ciocanesti");
				Statement zicala=con2.createStatement();;;;
				ResultSet rez2=zicala.executeQuery("select * from user,slot where user.mail=slot.email;");
				while(rez2.next()){
					for(int i=0;i<userbase.size();i++){
						if(rez2.getString("mail").equals(userbase.get(i).mail)){						
							Date in=new Date(rez2.getTimestamp("dela").getTime());
							Date out=new Date(rez2.getTimestamp("panala").getTime());
							Date cand=new Date(rez2.getTimestamp("cand").getTime());
							Slot slot=new Slot(in,out,userbase.get(i),cand);slot.id=rez2.getInt("id");slot.ocupat=rez2.getBoolean("ocupat");
							userbase.get(i).sluts.add(slot);
							Statement zicala2=con2.createStatement();
							ResultSet rez3=zicala2.executeQuery("select * from sugestie,slot where sugestie.idslot=slot.id;");							
							while (rez3.next()){						
									if (slot.id==rez3.getInt("id")){
										for (int j=0;j<userbase.size();j++){
											if (userbase.get(j).mail.equals(rez3.getString("mailautor"))){										
												Sugestie sug=new Sugestie(userbase.get(j), rez3.getString("porc"), slot);
												sug.acceptat=rez3.getBoolean("acceptat");sug.id=rez3.getInt("idsug");sug.cand=rez3.getTimestamp("decand");
												slot.sugestii.add(sug);
												if(slot.ocupat){
													sug.autor.events.add(new com.vaadin.ui.components.calendar.event.CalendarEvent() {
													private static final long serialVersionUID = 5538008828524132533L;
													public boolean isAllDay() {return false;}
													public String getStyleName() {return null;}
													public Date getStart() {return slot.in;}
													public Date getEnd() {return slot.out;}
													public String getDescription() {return sug.text;}
													public String getCaption() {
													return slot.user.prenume+" "+slot.user.nume;
													}
												});
													slot.cu=sug.autor;slot.candSaOcupat=new Date();
													slot.user.events.add(new com.vaadin.ui.components.calendar.event.CalendarEvent() {
													/**
														 * 
														 */
													private static final long serialVersionUID = 5538008828524132533L;
													public boolean isAllDay() {return false;}
													public String getStyleName() {return null;}
													public Date getStart() {return slot.in;}
													public Date getEnd() {return slot.out;}
													public String getDescription() {return sug.text;}
													public String getCaption() {
													return sug.autor.prenume+" "+sug.autor.nume;
													}					
												});
//													slot.cu=userbase.get(j);
													}
											}
										}
									}
								}
							}
						}
					}
				} catch (Exception e) {
				e.printStackTrace();
				}
		}
		public static void signup(String prenume,String nume,String email, String pass,String fbid,String urlpic){
			try {Class.forName("com.mysql.jdbc.Driver");} catch (ClassNotFoundException e1) {e1.printStackTrace();}
			try{
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/time","root","ciocanesti");
				Statement afirmatie=con.createStatement();
				afirmatie.executeUpdate("insert into user values ('"+prenume+"','"+nume+"','"+email+"','"+fbid+"','"+urlpic+"','"+pass+"',"+0+");");
			} catch (Exception e) {e.printStackTrace();}
			System.out.println("Adaugat user!");
		}
		public static void addSlot(java.util.Date in, java.util.Date out,java.util.Date cand, String mail){
			java.text.SimpleDateFormat sdf= new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String acum=sdf.format(cand);
			String dela=sdf.format(in);String panala=sdf.format(out);
			try {Class.forName("com.mysql.jdbc.Driver");} catch (ClassNotFoundException e1) {e1.printStackTrace();}
			try{
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/time","root","ciocanesti");
				Statement afirmatie=con.createStatement();
				afirmatie.executeUpdate("insert into slot values ('"+dela+"','"+panala+"','"+mail+"','"+acum+"',"+maxIdSlot+","+0+");");		
			} catch (Exception e) {e.printStackTrace();}
			System.out.println("Adaugat slot!");
		}
		public static void remSlot(Slot slot){
			try {Class.forName("com.mysql.jdbc.Driver");} catch (ClassNotFoundException e1) {e1.printStackTrace();}
			try{
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/time","root","ciocanesti");
				Statement afirmatie=con.createStatement();
										 //delete from slot where id=4;	
				afirmatie.executeUpdate("delete from slot where id="+slot.id+";");				
			} catch (Exception e) {e.printStackTrace();}
			System.out.println("Scos slot!");
		}
		public static void mksug(String dela, String porc, String dest,Slot slot,int sugid){	
			java.text.SimpleDateFormat sdf= new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String acum=sdf.format(new Date());
			try {Class.forName("com.mysql.jdbc.Driver");} catch (ClassNotFoundException e1) {e1.printStackTrace();}
			try{
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/time","root","ciocanesti");
				Statement afirmatie=con.createStatement();
					afirmatie.executeUpdate("insert into sugestie values ('"+dela+"','"+porc+"',"+0+",'"+dest+"',"+slot.id+","+sugid+",'"+acum+"');");
			} catch (Exception e) {e.printStackTrace();}
		}
		public static void imprietenire(User user){
			for (int i=0;i<userbase.size();i++){
				user.pretenari.add(userbase.get(i));		
			}
		}
		public static void update(){
			try {Class.forName("com.mysql.jdbc.Driver");} catch (ClassNotFoundException e1) {e1.printStackTrace();}
			Connection con;ResultSet rezultat=null;Statement afirmatie=null;
			try{
				con=DriverManager.getConnection("jdbc:mysql://localhost:3306/time","root","ciocanesti");
				afirmatie=con.createStatement();
				rezultat=afirmatie.executeQuery("select max(id), max(idsug) from slot, sugestie;");
				while (rezultat.next()){
					maxIdSug=rezultat.getInt("max(idsug)");
					maxIdSlot=rezultat.getInt("max(id)");}
				System.out.println("Maximum de sloturi a ajuns la "+maxIdSlot+";\nMaximum de sugestii e"+maxIdSug);				
			} catch (Exception e) {e.printStackTrace();}
			finally{try {
				rezultat.close();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}try {
				afirmatie.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}}
		}		
		public static void ocupa(int id){			
			try {Class.forName("com.mysql.jdbc.Driver");} catch (ClassNotFoundException e1) {e1.printStackTrace();}
			try{
				Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/time","root","ciocanesti");
				Statement afirmatie=con.createStatement();
				afirmatie.executeUpdate("update slot set ocupat=1 where id="+id+";");
			} catch (Exception e) {e.printStackTrace();}
		}
}