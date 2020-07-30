package server;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.vaadin.addons.locationtextfield.GeocodedLocation;

import com.vaadin.ui.Component;
import com.vaadin.ui.components.calendar.event.CalendarEvent;
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String nume;public String prenume;public String mail; public String pass;public ArrayList<Slot> sluts;
	public ArrayList<User> pretenari;ArrayList<Component> perete;public boolean poza;public Date lastSeen;
	public ArrayList<CalendarEvent>events;public String fbID;public GeocodedLocation geo;public String location;
	public boolean fb;public String urlpic;
	public User(String nume,String prenume,String mail,String pass,ArrayList<Slot> sluts,boolean poza) {
		super();
		this.nume = nume;
		this.prenume = prenume;
		this.mail = mail;
		this.pass=pass;
		this.sluts=sluts;
		this.pretenari=new ArrayList<User>();
		this.poza=false;
		this.lastSeen=new Date();
		this.poza=poza;
		this.events=new ArrayList<CalendarEvent>();
	}
	
	//public String toString() {return "nume: " + nume + ", prenume: " + prenume + ", mail: " + mail + "\n";}
	public String totalTimeSharing(){
		String how=" 0 seconds";
		long mills=0;
		for (int i=0;i<this.sluts.size();i++){
			if (this.sluts.get(i).out.after(new Date())){
			Calendar c1=Calendar.getInstance();
	 		c1.setTime(this.sluts.get(i).in);	
	 		Calendar c2=Calendar.getInstance(); 
	 		c2.setTime(this.sluts.get(i).out);
	 		long dif=(c2.getTimeInMillis()-c1.getTimeInMillis())/1000 ; 		
			mills=mills+dif;
		}}
		if(mills==0){how="0 seconds";}
		else if (mills>0&&mills<3600){how=mills/60+" minutes";}
		else if ((mills>=3600)&&(mills % 3600==0)){how=mills/3600+" hrs";}
		else{how=mills/3600+"hrs, "+(mills%3600)/60+"mins";}
		return how;
	}
//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((mail == null) ? 0 : mail.hashCode());
//		result = prime * result + ((nume == null) ? 0 : nume.hashCode());
//		result = prime * result + ((prenume == null) ? 0 : prenume.hashCode());
//		return result;
//	}
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		User other = (User) obj;
//		if (mail == null) {
//			if (other.mail != null)
//				return false;
//		} else if (!mail.equals(other.mail))
//			return false;
//		if (nume == null) {
//			if (other.nume != null)
//				return false;
//		} else if (!nume.equals(other.nume))
//			return false;
//		if (prenume == null) {
//			if (other.prenume != null)
//				return false;
//		} else if (!prenume.equals(other.prenume))
//			return false;
//		return true;
//	}
}