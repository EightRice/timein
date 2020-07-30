package server;
import java.util.ArrayList;
import java.util.Date;

public class Slot {
	public Date in;public Date out;public User user;public Date cand;public ArrayList<Sugestie>sugestii;
	public boolean ocupat;public User cu;public Date candSaOcupat;public int id;
	public Slot(Date in, Date out,User user,Date cand) {
		super();
		sugestii=new ArrayList<Sugestie>();
		this.in = in;
		this.out = out;
		this.user=user;
		this.cand=cand;
		this.ocupat=false;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((in == null) ? 0 : in.hashCode());
		result = prime * result + ((out == null) ? 0 : out.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Slot other = (Slot) obj;
		if (in == null) {
			if (other.in != null)
				return false;
		} else if (!in.equals(other.in))
			return false;
		if (out == null) {
			if (other.out != null)
				return false;
		} else if (!out.equals(other.out))
			return false;
		return true;
	}	
	@Override
	public String toString() {
		return "Slot [in=" + in + ", out=" + out + "]";
		
	}
	
		
}
