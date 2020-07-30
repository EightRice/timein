package server;
import java.util.Date;
public class Sugestie {	
	public int id;
	public User autor;
	public Slot destinatar;
	public String text;
	public boolean acceptat;
	public boolean ignorat;
	public Date cand;
	public Sugestie(User autor,String text,Slot slot) {
		super();
		this.autor=autor;
		this.text=text;
		this.destinatar=slot;
		this.acceptat=false;
		this.ignorat=false;
		this.cand=new Date();
		this.id=App.maxIdSug++;
	}

}
