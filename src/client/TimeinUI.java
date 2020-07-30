package client;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.annotation.WebServlet;

import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FacebookApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import org.vaadin.addon.oauthpopup.OAuthListener;
import org.vaadin.addon.oauthpopup.OAuthPopupButton;
import org.vaadin.addon.oauthpopup.buttons.FacebookButton;
import org.vaadin.addons.locationtextfield.GeocodedLocation;
import org.vaadin.addons.locationtextfield.LocationTextField;
import org.vaadin.addons.locationtextfield.OpenStreetMapGeocoder;
import org.vaadin.teemu.switchui.Switch;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.ClassResource;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.PopupView.Content;
import com.vaadin.ui.Slider;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

import server.App;
import server.Slot;
import server.Sugestie;
import server.User;
@Push
@SuppressWarnings("serial")
@Theme("timein")
@PreserveOnRefresh
public class TimeinUI extends UI implements Serializable{
	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = TimeinUI.class, widgetset = "client.widgetset.TimeinWidgetset")	
	public static class Servlet extends VaadinServlet {}
	final VerticalLayout tot=new VerticalLayout();
	public ApiInfo FACEBOOK_API = new ApiInfo("Facebook",
			FacebookApi.class,
			"162317900811118",
			"d0285a0d25e44e85e3ba950dd28cd002",
			"https://graph.facebook.com/me");
	Embedded logo=new Embedded(null, new ClassResource("150.png"));GeocodedLocation place;
	User us3r;Image doe=new Image(); AbsoluteLayout terasa; int diff;
	protected void init(VaadinRequest request) {
		setContent(tot);if (us3r==null){ident();}else{ecran();}
	}
	 @SuppressWarnings("deprecation")
	void ident(){
		 Label copy=new Label("TIMEIN © 2016");copy.setStyleName("copy");;
		 tot.addComponent(copy);
		 Window inceput=new Window();
		 inceput.setStyleName("rotund");;
		 inceput.addStyleName("ecran1");
		 inceput.addCloseListener(new CloseListener() {	
			public void windowClose(CloseEvent e) {
				if (us3r==null){tot.removeComponent(copy);ident();}
			}
		});
			inceput.setWidth("260px");
			inceput.setResizable(false);
			VerticalLayout randuri=new VerticalLayout();
			VerticalLayout brand=new VerticalLayout();
			VerticalLayout signin=new VerticalLayout();
			VerticalLayout subsign=new VerticalLayout();
			Label whoareyou=new Label();whoareyou.setValue("Make the most of it.");whoareyou.addStyleName("motto");
			TextField userfield=new TextField();
			PasswordField passfield = new PasswordField();
			Button timein=new Button("Sign in");timein.setIcon(FontAwesome.SIGN_IN);
			Notification nue = new Notification("<h2>Sorry Cheif,<h2>",
				    "<p>Third party login is not working.</p><p> For now, just use the standard login.</p>",
				    Notification.TYPE_WARNING_MESSAGE, true);
			ApiInfo api=FACEBOOK_API;
			OAuthPopupButton facebook=new FacebookButton(api.apiKey, api.apiSecret);facebook.setCaption("Sign in");
			facebook.setStyleName("albastru");//facebook.setIcon(FontAwesome.FACEBOOK_SQUARE);
			facebook.addOAuthListener(new OAuthListener() {
				@Override
				public void authSuccessful(String accessToken, String accessTokenSecret, String oauthRawResponse) {
					OAuthRequest request=new OAuthRequest(Verb.GET, "https://graph.facebook.com/v2.5/me?fields=picture.type(large),name");				
					Token token=new Token(accessToken, accessTokenSecret);
					createOAuthService().signRequest(token, request);
					Response response = request.send();
					try {
						  JSONObject json =new org.json.JSONObject(response.getBody());
						  String urlpic = json.getJSONObject("picture").getJSONObject("data").getString("url");
						  String numetot=json.getString("name");		  
							Pattern pattern=Pattern.compile(" ");
							Matcher matcher=pattern.matcher(numetot);
							String prenume="";String familie="";
							if (matcher.find()){
								prenume=numetot.substring(0,matcher.start());
								familie=numetot.substring(matcher.end());
							}
						  String fbid=json.getString("id");
						  
						  us3r=fbsignup(prenume, familie, fbid, urlpic);
						  us3r.fb=true;us3r.fbID=fbid;us3r.urlpic=urlpic;
						  if (us3r!=null){
							  salveaza(TimeinUI.this, us3r);us3r.lastSeen=new Date();
								inceput.close();tot.removeAllComponents();ecran();						
							}
						 
						 
						 // img.setSource(new ExternalResource(urlpic));						 
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}	

					 
				}
				
				@Override
				public void authDenied(String reason) {
					Notification.show("Access denied: "+reason);
					
				}
			});
			Button google=new Button("Sign in");google.addStyleName("rosu");google.setIcon(FontAwesome.GOOGLE_PLUS_SQUARE);
			google.addClickListener(new ClickListener() {				
				public void buttonClick(ClickEvent event) {
					nue.show(getPage());		
				}
			});
			HorizontalLayout social=new HorizontalLayout();social.addComponent(facebook);//social.addComponent(google);
			userfield.setImmediate(true);passfield.setImmediate(true);
			userfield.setInputPrompt("Email");passfield.setInputPrompt("Password");
			subsign.addComponent(userfield);subsign.addComponent(passfield);
			signin.addComponent(subsign); subsign.setComponentAlignment(userfield, Alignment.TOP_CENTER);subsign.setComponentAlignment(passfield, Alignment.BOTTOM_CENTER);	
			signin.addComponent(timein);whoareyou.setWidth("74%");
			brand.addComponent(logo);brand.addComponent(whoareyou);
			brand.setMargin(isEnabled());
			brand.setComponentAlignment(whoareyou, Alignment.BOTTOM_CENTER);
			brand.setComponentAlignment(logo, Alignment.TOP_CENTER);
			randuri.addComponent(brand);
			timein.addClickListener(new ClickListener() {	
				public void buttonClick(ClickEvent event) {	
					us3r=auth(userfield.getValue(), passfield.getValue());
					if (us3r!=null){
						salveaza(TimeinUI.this, us3r);us3r.lastSeen=new Date();
					inceput.close();tot.removeAllComponents();ecran();								
					}else{Notification.show("Sorry, don't know you.");}
				}});
			randuri.setComponentAlignment(brand, Alignment.MIDDLE_RIGHT);
			randuri.addComponent(signin);
			randuri.addComponent(social);randuri.setComponentAlignment(social, Alignment.BOTTOM_CENTER);
			randuri.setComponentAlignment(signin, Alignment.MIDDLE_CENTER);
			signin.setComponentAlignment(timein, Alignment.BOTTOM_CENTER);
			signin.setSpacing(true);
			inceput.setContent(randuri);
			VerticalLayout signup=new VerticalLayout();
			VerticalLayout subup=new VerticalLayout();
			TextField prenume=new TextField();prenume.setImmediate(true);prenume.setInputPrompt("First name");
			TextField nume=new TextField();prenume.setImmediate(true);nume.setInputPrompt("Last name");
			TextField mail=new TextField();mail.setImmediate(true);mail.setInputPrompt("Email");
			PasswordField pass=new PasswordField();pass.setImmediate(true);pass.setInputPrompt("Set a password");
			Button singupbuton=new Button("Sign up");singupbuton.setIcon(FontAwesome.CLOCK_O);
			subup.addComponent(prenume);subup.addComponent(nume);subup.addComponent(mail);subup.addComponent(pass);
			subup.setComponentAlignment(prenume, Alignment.BOTTOM_CENTER);
			subup.setComponentAlignment(nume, Alignment.BOTTOM_CENTER);
			subup.setComponentAlignment(mail, Alignment.BOTTOM_CENTER);
			subup.setComponentAlignment(pass, Alignment.BOTTOM_CENTER);
			signup.addComponent(subup);
			signup.addComponent(singupbuton);
			signup.setSpacing(true);
			signup.setMargin(true);
			signup.setComponentAlignment(singupbuton, Alignment.BOTTOM_CENTER);
			signup.setComponentAlignment(subup, Alignment.BOTTOM_CENTER);	
			randuri.addComponent(signup);
			randuri.setSpacing(true);			
			randuri.setComponentAlignment(signup, Alignment.MIDDLE_CENTER);
			singupbuton.addClickListener(new ClickListener() {			
			public void buttonClick(ClickEvent event) {
				for (int i=0;i<App.userbase.size();i++){
					if (App.userbase.get(i).mail.equals(mail.getValue())){
						Notification.show("Sorry, but that email address has already been used here.", Notification.TYPE_ERROR_MESSAGE);
						return;
					}				
				}
				String crypt=encrypta();
				Trimite.trimite("itstimetotimein@gmail.com",mail.getValue(),"Welcome to TimeIn!", "Hello "+prenume.getValue()+"! Your verification code is: "+crypt, "ciocanesti");			
				VerticalLayout verif=new VerticalLayout();
				HorizontalLayout spatiu=new HorizontalLayout();
				spatiu.setHeight("38px");
				verif.setHeight("900px");
				Label paste=new Label("A verification code was sent to "+mail.getValue()+". Paste it below and you may begin.");
				paste.setStyleName("headate");
				TextField code=new TextField();code.setInputPrompt("Paste the code here");
				randuri.removeAllComponents();randuri.addComponent(brand);randuri.setComponentAlignment(brand, Alignment.MIDDLE_RIGHT);
				randuri.addComponent(paste);randuri.setComponentAlignment(paste, Alignment.MIDDLE_CENTER);
				randuri.addComponent(code);randuri.setComponentAlignment(code, Alignment.MIDDLE_CENTER);
				Button done=new Button("Verify");			
				randuri.addComponent(done);randuri.setComponentAlignment(done, Alignment.MIDDLE_CENTER);
				randuri.addComponent(spatiu);
				inceput.center();
				done.addClickListener(new ClickListener() {
					public void buttonClick(ClickEvent event) {
						if (!code.getValue().contains(crypt)){
							Notification.show("Not the right code that is.");
						}
						else{
							us3r=singup(prenume.getValue(), nume.getValue(), mail.getValue(),pass.getValue());								
							if (us3r!=null){
								salveaza(TimeinUI.this, us3r);
							inceput.close();tot.removeAllComponents();ecran();								
							}
						}
					}
				});			
				}
			}
			);
			UI.getCurrent().addWindow(inceput);
			inceput.center();
	}
	void ecran(){
		us3r.lastSeen=new Date();
		if(us3r.poza==true&&us3r.fb==false){doe.setSource(new ClassResource("pics/"+us3r.mail+".png"));}
		else if(us3r.poza==false&&us3r.fb==true){doe.setSource(new ExternalResource(us3r.urlpic));}
		else if(us3r.poza==true&&us3r.fb==true){doe.setSource(new ExternalResource(us3r.urlpic));}
		else{doe.setSource(new ClassResource("pics/a.png"));}
		terasa=new AbsoluteLayout();terasa.setHeight("56px");terasa.setWidth("100%");
		terasa.setStyleName("umbra");
		HorizontalLayout men=new HorizontalLayout();
		 MenuBar meniu=new MenuBar();meniu.setHeight("56px");meniu.setStyleName("meniu");
		 MenuItem root=meniu.addItem(us3r.prenume, null);
		 root.addItem("Settings", FontAwesome.COGS, new Command() {
			 public void menuSelected(com.vaadin.ui.MenuBar.MenuItem selectedItem) {
				 tot.removeAllComponents();
				 tot.addComponent(terasa);
					tot.addComponent(settings());
			 }});
		 root.addItem("Sign out", FontAwesome.SIGN_OUT, new Command() {
			 public void menuSelected(MenuItem selectedItem) {signout();}});
		 HorizontalLayout butoane=new HorizontalLayout();
		 HorizontalLayout cauta=new HorizontalLayout();
		 TextField search=new TextField();;search.setWidth("315px");search.setInputPrompt("Search for User, Event or Location");search.setStyleName("contur");
		 Button lupa=new Button();lupa.setIcon(FontAwesome.SEARCH);lupa.setStyleName("contur");
		 cauta.addComponent(search);cauta.addComponent(lupa);cauta.addStyleName("cauta");
		 Button events=new Button("Events");events.setHeight("56px");events.setIcon(FontAwesome.GLASS);events.setStyleName("butoane");	
		 events.addClickListener(new ClickListener() {			
			@Override
			public void buttonClick(ClickEvent event) {				
			Notification sit= new Notification("Sit tight!","Support for Events is coming soon.");
				sit.show(Page.getCurrent());
			}
		});
		 Button mysug=new Button("Suggestions");mysug.setHeight("56");mysug.setStyleName("butoane");mysug.setIcon(FontAwesome.COMMENTS_O);
		 mysug.addClickListener(new ClickListener() {			
			@Override
			public void buttonClick(ClickEvent event) {
				Notification sit= new Notification("This button doesn't do anything yet","..but it will");
				sit.show(Page.getCurrent());				
			}
		});
		 Button calendar=new Button("Calendar");calendar.setHeight("56px");calendar.setStyleName("butoane");calendar.setIcon(FontAwesome.CALENDAR);
		 butoane.addComponent(mysug);butoane.addComponent(events);butoane.addComponent(calendar);
		 Button	setari=new Button();setari.setIcon(FontAwesome.COG);
		 men.addComponent(cauta);men.setComponentAlignment(cauta, Alignment.MIDDLE_LEFT);
		 men.addComponent(doe);men.addComponent(meniu);men.setComponentAlignment(meniu, Alignment.MIDDLE_CENTER);
			logo.setHeight("50px");
			doe.setHeight("56px");
		HorizontalLayout ptLogo=new HorizontalLayout();
		ptLogo.addLayoutClickListener(new LayoutClickListener() {
			public void layoutClick(LayoutClickEvent event) {
				tot.removeAllComponents();ecran();				
			}
		});
		ptLogo.addComponent(logo);
		terasa.addComponent(ptLogo, "top:5px;left:13px");
		terasa.addComponent(butoane, "top:0px;left:150px");
		terasa.addComponent(men, "top:0px; right:0px");
		tot.addComponent(terasa);
		HorizontalLayout content=new HorizontalLayout();content.setStyleName("apa");
		content.setSpacing(true);		
		Embedded activity=new Embedded(null, new ClassResource("activitymic.png"));
		Embedded myslotz=new Embedded(null, new ClassResource("myslotsmic.png"));		
		Button plus = new Button();plus.setIcon(FontAwesome.PLUS);
		Button addTime=new Button("Add Time");
		Embedded otherppl=new Embedded(null, new ClassResource("otherpeoplemic.png"));
		HorizontalLayout myslots=new HorizontalLayout();myslots.addComponent(myslotz);myslots.addComponent(plus);myslots.setSpacing(true);
		VerticalLayout stanga=new VerticalLayout();VerticalLayout centru=new VerticalLayout(); VerticalLayout dreapta=new VerticalLayout(); 
		stanga.addComponent(activity);stanga.setComponentAlignment(activity, Alignment.TOP_CENTER);
		centru.addComponent(myslots);centru.setComponentAlignment(myslots, Alignment.TOP_CENTER);
		dreapta.addComponent(otherppl);dreapta.setComponentAlignment(otherppl, Alignment.TOP_CENTER);
		content.addComponent(stanga);content.addComponent(centru);content.addComponent(dreapta);	
		Label nosluts=new Label("You have no outstanding slots.");nosluts.setStyleName("nosluts");
		VerticalLayout bine=new VerticalLayout();bine.setHeight("250px");
		bine.addComponent(nosluts);bine.setComponentAlignment(nosluts, Alignment.BOTTOM_CENTER);
		Label click=new Label("Click \"+\" to add a new slot.");click.setStyleName("click");
		int are=0;
		for (int i=0;i<us3r.sluts.size();i++){
		if(us3r.sluts.get(i).ocupat){are++;}}		
		if (are==us3r.sluts.size()||us3r.sluts.size()==0){
			centru.addComponent(bine);centru.setComponentAlignment(bine, Alignment.MIDDLE_CENTER);
			bine.addComponent(click);bine.setComponentAlignment(click, Alignment.MIDDLE_CENTER);			
		}
		for (int i=0;i<us3r.sluts.size();i++){
			Date acuma=new Date();
			if (!us3r.sluts.get(i).ocupat&&us3r.sluts.get(i).out.after(acuma)){
			Button rade=new Button();
			Component slut=(myslot(us3r.sluts.get(i),us3r.sluts.get(i).in,us3r.sluts.get(i).out,rade));
			centru.addComponent(slut);;
			final int m=i;
			rade.addClickListener(new ClickListener() {
				public void buttonClick(ClickEvent event) {
					centru.removeComponent(slut);
					App.remSlot(us3r.sluts.get(m));
					us3r.sluts.remove(m);;
				}
			});;
		}}
		//Introducem sloturile prietenilor
		for (int i=0;i<App.userbase.size();i++){			
			User user=App.userbase.get(i);
			Date acuma=new Date();
			if (user==us3r) {continue;}
			for(int j=0;j<user.sluts.size();j++){
				if (!user.sluts.get(j).ocupat&&user.sluts.get(j).out.after(acuma)){
				Component slut=othpplslot(user.sluts.get(j) ,user);
				dreapta.addComponent(slut);	
			}}
		}
		//Bagam peretenele 
		for (int i=App.userbase.size()-1;i>=0;i--){
			User user=App.userbase.get(i);
			for (int j=user.sluts.size()-1;j>=0;j--){
				if(!user.sluts.get(j).ocupat){
				Component postare=post(user.sluts.get(j).cand,user,(byte)1,user.sluts.get(j));
				stanga.addComponent(postare);}
				else {	
				Component postare=post(user.sluts.get(j).cand,user,(byte)2,user.sluts.get(j));	
				stanga.addComponent(postare);
				}
			}
		}
		tot.addComponent(content); 
		calendar.addClickListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				tot.removeAllComponents();
				tot.addComponent(terasa);
				tot.addComponent(calendar());
			}
		});
		content.setWidth("100%");
		 Window apareLaPlus = new Window("New Slot");
		 plus.setReadOnly(true);
	        apareLaPlus.setWidth(300.0f, Unit.PIXELS);
	        final FormLayout fereastra = new FormLayout();	        
	        HorizontalLayout datele=new HorizontalLayout();
//	        InlineDateField deLa = new InlineDateField();
//	        InlineDateField panaLa=new InlineDateField();panaLa.setResolution(Resolution.MINUTE);	  
	        PopupDateField deLa=new PopupDateField();deLa.setValue(new Date());deLa.setResolution(Resolution.MINUTE);
	        deLa.setRangeStart(new Date());;;
	        PopupDateField panaLa=new PopupDateField();
	        Label start=new Label("Start time: ");start.setStyleName("click");
	        datele.addComponent(start);datele.setComponentAlignment(start, Alignment.MIDDLE_LEFT);
	        datele.addComponent(deLa);//datele.addComponent(panaLa);
	        datele.setSpacing(true);
	        fereastra.addComponent(datele);
	        apareLaPlus.setContent(fereastra);
	        apareLaPlus.setWidth("570px");
	        HorizontalLayout durlbl=new HorizontalLayout();
	        HorizontalLayout subdate=new HorizontalLayout();
	        HorizontalLayout slide=new HorizontalLayout();
	        Label amt=new Label();amt.setImmediate(true);amt.setStyleName("click");amt.setValue("Duration: 1 hour");
	        Label space=new Label();space.setWidth("2px");
	        durlbl.addComponent(space);
	        durlbl.addComponent(amt);
	        slide.setWidth("100%");
	        Slider bara=new Slider();bara.setImmediate(true);bara.setWidth("525px");
	        bara.setStyleName("bara");
	        bara.setMin(1.0);
	        bara.setMax(44.0);
	        bara.setValue(12.0);diff=3600000;      
	        Label dif=new Label();
	        bara.addValueChangeListener(new ValueChangeListener() {				
				public void valueChange(ValueChangeEvent event) {					
					double minute=(double)event.getProperty().getValue();
					Double min=minute;
					int minu=min.intValue();
						 if (minu<12){amt.setValue("Duration: "+minu*5+" minutes");diff=minu*5*60000;}
					else if(minu==12){amt.setValue("Duration: 1 hour");diff=minu*5*60000;}
					else if(minu>12&&minu<16){amt.setValue("Duration: 1 hour and "+(minu-12)*15+" minutes");diff=3600000+(minu-12)*15*60000;}
					else if(minu==16){amt.setValue("Duration: 2 hours");diff=3600000+(minu-12)*15*60000;}
					else if(minu>16&&minu<20){amt.setValue("Duration: 2 hours and "+(minu-16)*15+" minutes");diff=7200000+(minu-16)*15*60000;}
					else if(minu==20){amt.setValue("Duration: 3 hours");diff=7200000+(minu-16)*15*60000;}
					else if(minu>20&&minu<24){amt.setValue("Duration: 3 hours and "+(minu-20)*15+" minutes");diff=10800000+(minu-20)*15*60000;}
					else if(minu==24){amt.setValue("Duration: 4 hours");diff=10800000+(minu-20)*15*60000;}
					else if(minu>24&&minu<28){amt.setValue("Duration: 4 hours and "+(minu-24)*15+" minutes");diff=14400000+(minu-24)*15*60000;}
					else if(minu==28){amt.setValue("Duration: 5 hours");diff=14400000+(minu-24)*15*60000;}
					else if(minu>28&&minu<32){amt.setValue("Duration: 5 hours and "+(minu-28)*15+" minutes");diff=18000000+(minu-28)*15*60000;}
					else if(minu==32){amt.setValue("Duration: 6 hours");diff=18000000+(minu-28)*15*60000;}
					else if(minu>32&&minu<34){amt.setValue("Duration: 6 hours and "+(minu-32)*30+" minutes");diff=21600000+(minu-32)*30*60000;}
					else if(minu==34){amt.setValue("Duration: 7 hours");diff=21600000+(minu-32)*30*60000;}
					else if(minu>34&&minu<36){amt.setValue("Duration: 7 hours and "+(minu-34)*30+" minutes");diff=25200000+(minu-34)*30*60000;}
					else if(minu==36){amt.setValue("Duration: 8 hours");diff=25200000+(minu-34)*15*60000;}
					else if(minu>36&&minu<38){amt.setValue("Duration: 8 hours and "+(minu-36)*30+" minutes");diff=28800000+(minu-36)*30*60000;}
					else if(minu==38){amt.setValue("Duration: 9 hours");diff=28800000+(minu-36)*15*60000;}
					else if(minu>38&&minu<40){amt.setValue("Duration: 9 hours and "+(minu-38)*30+" minutes");diff=32400000+(minu-38)*30*60000;}
					else if(minu==40){amt.setValue("Duration: 10 hours");diff=32400000+(minu-38)*15*60000;}
					else if(minu>40&&minu<42){amt.setValue("Duration: 10 hours and "+(minu-40)*30+" minutes");diff=36000000+(minu-40)*30*60000;}
					else if(minu==42){amt.setValue("Duration: 11 hours");diff=36000000+(minu-40)*30*60000;}
					else if(minu>42&&minu<44){amt.setValue("Duration: 11 hours and "+(minu-42)*30+" minutes");diff=39600000+(minu-42)*30*60000;}
					else if(minu==44){amt.setValue("Duration: 12 hours");diff=39600000+(minu-42)*30*60000;}
				}
			});
	        slide.addComponent(bara);
	        fereastra.addComponent(durlbl);
	        fereastra.addComponent(slide);
	        AbsoluteLayout jos=new AbsoluteLayout();jos.setSizeFull();
	        jos.setHeight("55px");jos.setWidth("100%");
	        HorizontalLayout ptloc=new HorizontalLayout();
	        OpenStreetMapGeocoder geocoder = OpenStreetMapGeocoder.getInstance();
			geocoder.setLimit(25);
			final LocationTextField<GeocodedLocation> ltf = new LocationTextField<GeocodedLocation>(geocoder, GeocodedLocation.class);
			ltf.setCaption("Where will you be?");
			if (us3r.geo!=null){
				ltf.setValue(us3r.geo.getDisplayString());
				}else {
				}
	        ltf.setWidth("100%");
	        ltf.setImmediate(true);
			ptloc.addComponent(ltf);
			Content cont=new Content() {
				@Override
				public Component getPopupComponent() {						
					return ptloc;
				}
				@Override
				public String getMinimizedValueAsHTML() {
					if (us3r.geo!=null){return us3r.geo.getDisplayString();}
					return "(not set)";
				}
			};
			ltf.addLocationValueChangeListener(new ValueChangeListener() {				
				@Override
				public void valueChange(ValueChangeEvent event) {					
					if (ltf!=null){
					//s	not.setValue("");
						us3r.geo=ltf.getLocation();
						cont.getMinimizedValueAsHTML();
					}
				}
			});			
	        PopupView loc=new PopupView(cont);
	        Label location=new Label();
	        location.setContentMode(ContentMode.HTML);
	        location.setValue(FontAwesome.LOCATION_ARROW.getHtml() + "Location: ");
	        jos.addComponent(location,"bottom:0; left:3%");
	        jos.addComponent(loc, "bottom:1px;left:16%");
	        jos.addComponent(addTime, "bottom:0;right:3%");
	        //subdate.addComponent(location);subdate.setComponentAlignment(location, Alignment.MIDDLE_LEFT);
	        //subdate.addComponent(loc);
	        //subdate.setComponentAlignment(loc, Alignment.MIDDLE_LEFT);
	        //subdate.addComponent(addTime);	
	       // subdate.setWidth("94%");
	       // subdate.setComponentAlignment(addTime, Alignment.BOTTOM_RIGHT);
	        fereastra.addComponent(jos);
	        //fereastra.addComponent(subdate);
	        apareLaPlus.center();int nare=are;
	        addTime.addClickListener(new Button.ClickListener() { 	
				public void buttonClick(ClickEvent event) {
					plus.setReadOnly(false);
					Date in=deLa.getValue();					
					Date acum=new Date();
					long millsOut=in.getTime()+diff;
					Date out=new Date();out.setTime(millsOut);			 		
					Button rade=new Button();
					Slot slt=new Slot(in, out, us3r, acum);
					App.maxIdSlot++;slt.id=App.maxIdSlot;
					App.addSlot(in, out, acum, us3r.mail);
					if (nare==us3r.sluts.size()||us3r.sluts.size()==0){centru.removeComponent(bine);}
					us3r.sluts.add(slt);
					Component slot=myslot(slt,in,out,rade);
					if(nare==us3r.sluts.size()&&us3r.sluts.size()==0){centru.removeComponent(bine);}
					centru.addComponent(slot);
					rade.addClickListener(new ClickListener(){
						public void buttonClick(ClickEvent event) {
							centru.removeComponent(slot);
							App.remSlot(slt);
							for (int i=0;i<us3r.sluts.size();i++){
								if(us3r.sluts.get(i).id==slt.id){us3r.sluts.remove(i);}
							}
						}
					});
					apareLaPlus.close();
				}
			});
		tot.setComponentAlignment(content, Alignment.TOP_CENTER);		
		plus.addClickListener(new Button.ClickListener() {
			public void buttonClick(ClickEvent event) {
				
				UI.getCurrent().addWindow(apareLaPlus);
			}
		});
	}
	User auth(String user, String pass){ 
		 for(int i=0;i<App.userbase.size();i++){
			 if (user.equals(App.userbase.get(i).mail) && pass.equals(App.userbase.get(i).pass)){
				 return App.userbase.get(i);
			 }
		 }
		 return null;
	 }
	User fbsignup(String prenume,String nume,String fbid,String urlpic){
		for (int i=0;i<App.userbase.size();i++){
			if (fbid!=null&&fbid.equals(App.userbase.get(i).fbID)){return App.userbase.get(i);}}
		User user=new User(nume, prenume, null, null, new ArrayList<Slot>(), true);
		user.fb=true;user.fbID=fbid;
		App.userbase.add(user);
		App.cine=prenume;
		App.signup(prenume, nume, null, null, fbid, urlpic);
		return user;
		}
	User singup(String prenume,String nume,String email, String pass){		 
			User user=new User(nume, prenume, email, pass, new ArrayList<Slot>(),false);
			App.userbase.add(user);
			App.cine=prenume;
			System.out.println(App.userbase);
			App.signup(prenume, nume, email, pass,null,null);
			return user;
	 }
	void signout(){
		 us3r=null;
		 tot.removeAllComponents();
		 ident();
	 }
    ComponentContainer profile(User user){
	HorizontalLayout coloane=new HorizontalLayout();coloane.setSpacing(true);coloane.setStyleName("degajat2");
	  VerticalLayout stanga=new VerticalLayout();
	  Label name=new Label(user.prenume+" "+user.nume);
	  name.setStyleName("name");
	  VerticalLayout dreapta=new VerticalLayout();dreapta.setHeight("520px");dreapta.setWidth("350px");
		  TabSheet tabs=new TabSheet();
		dreapta.addComponent(tabs);
		VerticalLayout sloturi=new VerticalLayout();
		VerticalLayout activ=new VerticalLayout();
		tabs.addTab(sloturi,user.prenume+"'s slots");
		tabs.addTab(activ,user.prenume+"'s activity");
		for (int i=user.sluts.size()-1;i>=0;i--) {
			if (!user.sluts.get(i).ocupat){
			sloturi.addComponent(pslot(user.sluts.get(i)));
		}else{			
			activ.addComponent(post(user.sluts.get(i).cand, user,(byte)3, user.sluts.get(i)));
		}}
			Image pic=new Image();
			if(user.poza==true&&user.fb==false){pic.setSource(new ClassResource("pics/"+user.mail+".png"));}
			else if(user.poza==false&&user.fb==true){pic.setSource(new ExternalResource(user.urlpic));}
			else if(user.poza==true&&user.fb==true){pic.setSource(new ExternalResource(user.urlpic));}
			else{pic.setSource(new ClassResource("pics/a.png"));}
			pic.setHeight("250px");
			stanga.addComponent(name);stanga.setComponentAlignment(name, Alignment.TOP_CENTER);
			stanga.addComponent(pic);stanga.setStyleName("forsug");dreapta.setStyleName("forsug");			
			Label last=new Label("Last seen: "+user.lastSeen.toString());
			last.setWidth("100%");
			last.setStyleName("mic");
			stanga.addComponent(last);
			OptionGroup relatie=new OptionGroup();
			relatie.addItem(1);relatie.setItemCaption(1,"Follow "+user.prenume);
			relatie.addItem(2);relatie.setItemCaption(2,"Stalk "+user.prenume);
			relatie.addItem(3);relatie.setItemCaption(3,"Hide from "+user.prenume);
			relatie.select(1);
			stanga.addComponent(relatie);
			VerticalLayout capsula=new VerticalLayout();capsula.setHeight("125px");
			stanga.addComponent(capsula);
			String ceva="You are following "+user.prenume+"'s slots and activity.";
			Label info=new Label(ceva);
			capsula.addComponent(info);
			capsula.setWidth("230px");
			relatie.addValueChangeListener(new ValueChangeListener() {
				public void valueChange(ValueChangeEvent event) {
					if (relatie.getValue().equals(1)){Label info1=new Label("You are following "+user.prenume+"'s slots and activity.");
					capsula.removeAllComponents();
					capsula.addComponent(info1);info1.setStyleName("info");}
					if (relatie.getValue().equals(2)){Label info2=new Label("You are being notified whenever "+user.prenume+" adds a new slot.");
					capsula.removeAllComponents();info2.setStyleName("info");
					capsula.addComponent(info2);}
					if (relatie.getValue().equals(3)){
					Label info3=new Label(user.prenume+ " cannot see your slots and activity.");
					capsula.removeAllComponents();info3.setStyleName("info");
					capsula.addComponent(info3);
					}
				}
			});			
			info.setStyleName("info");
			coloane.addComponent(stanga);coloane.addComponent(dreapta);
			return coloane;
		}	 
@SuppressWarnings("deprecation")
	ComponentContainer myslot(Slot slot,Date in, Date out, Button but){
		VerticalLayout vl=new VerticalLayout();
		 Random rand=new Random();
		 vl.setStyleName("post"+rand.nextInt(9));
		 HorizontalLayout sus=new HorizontalLayout();		 
		 sus.addStyleName("headate");
         sus.setWidth("100%");
     	int weeknr= in.getDay();
     	int monthnr=in.getMonth();
     	String weekday;
     	String mon="Hop";
     	if (weeknr==1){weekday="Monday";}else if(weeknr==2){weekday="Tuesday";}
     	else if(weeknr==3){weekday="Wednesday";}else if(weeknr==4){weekday="Thursday";}
     	else if(weeknr==5){weekday="Friday";}else if(weeknr==6){weekday="Saturday";}else{weekday="Sunday";}
     	if(monthnr==0){mon="Jan";}else if(monthnr==1){mon="Feb";}else if(monthnr==2){mon="Mar";}
     	else if(monthnr==3){mon="Apr";} if(monthnr==4){mon="May";} else if(monthnr==5){mon="Jun";}
     	else if(monthnr==6){mon="Jul";} if(monthnr==7){mon="Aug";}else if(monthnr==8){mon="Sep";}
     	else if(monthnr==9){mon="Oct";}else if(monthnr==10){mon="Nov";}else if(monthnr==11){mon="Dec";}
     	String data="  "	+weekday+", "+mon+" "+in.getDate();
        Label datili=new Label(data);
        datili.setStyleName("data");
        datili.setWidth("130px");
        sus.addComponent(datili);
        String oraIn;
        int maiMicIn=in.getMinutes();
        if (maiMicIn<10){oraIn=in.getHours()+":0"+maiMicIn;}else{oraIn=in.getHours()+":"+maiMicIn;}     
        sus.setSpacing(true);
        String oraOut;
        int maiMicOut=out.getMinutes();
        if (maiMicOut<10){oraOut=out.getHours()+":0"+maiMicOut;}else{oraOut=out.getHours()+":"+maiMicOut;}        
        Label intt=new Label(oraIn+" - "+oraOut);intt.setStyleName("ora");
        sus.addComponent(intt);sus.setComponentAlignment(intt, Alignment.MIDDLE_LEFT);
        but.setStyleName("but");but.setHeight("22px");
        but.setIcon(FontAwesome.TRASH_O);
						        Calendar c1=Calendar.getInstance();;;;	
						 		c1.setTime(in); 		
						 		Calendar c2=Calendar.getInstance(); 
						 		c2.setTime(out);
						 		long dif=(c2.getTimeInMillis()-c1.getTimeInMillis())/1000 ;
						 		String how="";
								if (dif<3600){how=dif/60+" minutes";}
								else if ((dif>=3600)&&(dif % 3600==0)){how=dif/3600+" hrs";}
								else{how=dif/3600+"hrs, "+(dif%3600)/60+"mins";}														 		
         Label time=new Label(how);time.setStyleName("headate");
         sus.addComponent(time);sus.setComponentAlignment(time, Alignment.MIDDLE_RIGHT);
         sus.addComponent(but);sus.setComponentAlignment(but, Alignment.TOP_RIGHT);
         HorizontalLayout jeos=new HorizontalLayout();
         Label delaeicon=new Label();delaeicon.setIcon(FontAwesome.USERS);delaeicon.setStyleName("spatiu");
         jeos.addComponent(delaeicon);
         Label delaeiscris=new Label("No suggestions yet");
         int cate=slot.sugestii.size();
         if (cate==0){
        	 jeos.addComponent(delaeiscris);
             vl.addComponent(sus);
             vl.addComponent(jeos);
             return vl;
         }
         else if (cate>0){
         delaeiscris.setValue(cate+" Suggestions from friends");}
         HorizontalLayout link=new HorizontalLayout();link.setSpacing(true);
         link.addComponent(delaeiscris);link.addLayoutClickListener(new LayoutClickListener() {
			public void layoutClick(LayoutClickEvent event) {
				Window sugestii=new Window(data+", "+oraIn+"-"+oraOut);				
				sugestii.center();
				VerticalLayout vl=new VerticalLayout();vl.setStyleName("degajat");
				TabSheet tabs=new TabSheet();	tabs.setWidth("501px");						
				VerticalLayout events=new VerticalLayout();
				events.setHeight("200px");
				tabs.addTab(vl,"Suggestions from friends");
				tabs.addTab(events,"Event suggestions");
				vl.setWidth("500px");
				for(int i=0;i<slot.sugestii.size();i++){
				final int m=i;
				if(!slot.sugestii.get(i).ignorat){
					Image picture=new Image();picture.setHeight("65px");
					if(slot.sugestii.get(i).autor.poza){
					picture.setSource(new ClassResource("pics/"+slot.sugestii.get(i).autor.mail+".png"));}
					else {picture.setSource(new ClassResource("pics/a.png"));}
					Random rand=new Random();
					HorizontalLayout unu=new HorizontalLayout();unu.setStyleName("pos"+rand.nextInt(9));unu.setWidth("100%");
					unu.addComponent(picture);
					VerticalLayout una=new VerticalLayout();una.setStyleName("sug");
					Button accept=new Button("Accept");accept.setStyleName("accept");accept.setHeight("27px");
					accept.addClickListener(new ClickListener() {		
						public void buttonClick(ClickEvent event) {
							sugestii.close();
							slot.ocupat=true;
							App.ocupa(slot.id);
								slot.cu=slot.sugestii.get(m).autor;slot.candSaOcupat=new Date();
								slot.sugestii.get(m).autor.events.add(new com.vaadin.ui.components.calendar.event.CalendarEvent() {
								public boolean isAllDay() {return false;}
								public String getStyleName() {return null;}
								public Date getStart() {return slot.in;}
								public Date getEnd() {return slot.out;}
								public String getDescription() {return slot.sugestii.get(m).text;}
								public String getCaption() {									
								return us3r.prenume+" "+us3r.nume;
								}
							});
							us3r.events.add(new com.vaadin.ui.components.calendar.event.CalendarEvent() {
								public boolean isAllDay() {return false;}
								public String getStyleName() {return null;}
								public Date getStart() {return slot.in;}
								public Date getEnd() {return slot.out;}
								public String getDescription() {return slot.sugestii.get(m).text;}
								public String getCaption() {									
								return slot.sugestii.get(m).autor.prenume+" "+slot.sugestii.get(m).autor.nume;
								}
							});
							tot.removeAllComponents();
							tot.addComponent(terasa);
							tot.addComponent(calendar());
						}
					});
					Button decline=new Button("Decline");decline.setStyleName("decline");decline.setHeight("27px");
					Button ignore=new Button("Ignore");ignore.setStyleName("ignore");ignore.setHeight("27px");
					int n=i;
					ignore.addClickListener(new ClickListener() {						
						@Override
						public void buttonClick(ClickEvent event) {
														
							UI.getCurrent().removeWindow(sugestii);
							slot.sugestii.get(n).ignorat=true;
							Notification.show("Ceva s-a intamplat aici");
							UI.getCurrent().addWindow(sugestii);
						}
					});
					Button dela=new Button(slot.sugestii.get(i).autor.prenume+" "+slot.sugestii.get(i).autor.nume);dela.setStyleName("bold");					
					dela.setHeight("22px");;int t=i;
					dela.addClickListener(new ClickListener() {						
						@Override
						public void buttonClick(ClickEvent event) {
							tot.removeAllComponents();
							tot.addComponent(terasa);
							tot.addComponent(profile(slot.sugestii.get(t).autor));							
						}
					});
					HorizontalLayout sus=new HorizontalLayout();sus.setWidth("100%");
					sus.addComponent(dela);sus.setComponentAlignment(dela, Alignment.TOP_LEFT);					
					Label timeFrm=new Label(timeFrame(slot.sugestii.get(i).cand));timeFrm.setStyleName("mij");
					sus.addComponent(timeFrm);sus.setComponentAlignment(timeFrm, Alignment.TOP_RIGHT);
					una.addComponent(sus);
					Label text=new Label(slot.sugestii.get(i).text);text.setStyleName("textsug");
					una.addComponent(text);
					HorizontalLayout optiuni=new HorizontalLayout();optiuni.setStyleName("optiuni");
					optiuni.addComponent(accept);optiuni.addComponent(ignore);
					una.addComponent(optiuni);optiuni.setSpacing(true);	
					una.setComponentAlignment(optiuni, Alignment.BOTTOM_LEFT);
					unu.addComponent(una);
					unu.setExpandRatio(picture, 2);
					unu.setExpandRatio(una, 13);
					vl.addComponent(unu);
				}}
				sugestii.setContent(tabs);
				UI.getCurrent().addWindow(sugestii);
			}
		});
        jeos.addComponent(link);
        vl.addComponent(sus);
        vl.addComponent(jeos);
	    return vl;
	 }
	@SuppressWarnings("deprecation")
	ComponentContainer pslot(Slot slot){
	VerticalLayout vl=new VerticalLayout();
	 Random rand=new Random();		
	 vl.setStyleName("post"+rand.nextInt(9));
	 HorizontalLayout sus=new HorizontalLayout();		 
	 sus.addStyleName("headate");
     sus.setWidth("100%");
 	int weeknr= slot.in.getDay();
 	int monthnr=slot.in.getMonth();
 	String weekday;
 	String mon;
 	if (weeknr==1){weekday="Monday";}else if(weeknr==2){weekday="Tuesday";}
 	else if(weeknr==3){weekday="Wednesday";}else if(weeknr==4){weekday="Thursday";}
 	else if(weeknr==5){weekday="Friday";}else if(weeknr==6){weekday="Saturday";}else{weekday="Sunday";}
 	if(monthnr==0){mon="Jan";}else if(monthnr==1){mon="Feb";}else if(monthnr==2){mon="Mar";}
 	else if(monthnr==3){mon="Apr";} if(monthnr==4){mon="May";} else if(monthnr==5){mon="Jun";}
 	else if(monthnr==6){mon="Jul";} if(monthnr==7){mon="Aug";}else if(monthnr==8){mon="Sep";}
 	else if(monthnr==9){mon="Oct";}else if(monthnr==10){mon="Nov";}else{mon="Jan";}
 	String data="  "	+weekday+", "+mon+" "+slot.in.getDate();
     Label datili=new Label(data);
     datili.setStyleName("data");
     datili.setWidth("130px");
     sus.addComponent(datili);       
     String oraIn;
     int maiMicIn=slot.in.getMinutes();
     if (maiMicIn<10){oraIn=slot.in.getHours()+":0"+maiMicIn;}else{oraIn=slot.in.getHours()+":"+maiMicIn;}         
     sus.setSpacing(true);
     String oraOut;
     int maiMicOut=slot.out.getMinutes();
     if (maiMicOut<10){oraOut=slot.out.getHours()+":0"+maiMicOut;}else{oraOut=slot.out.getHours()+":"+maiMicOut;}        
     Label intt=new Label(oraIn+" - "+oraOut);intt.setStyleName("ora"); 
     sus.addComponent(intt);sus.setComponentAlignment(intt, Alignment.MIDDLE_LEFT);                    
					        Calendar c1=Calendar.getInstance(); 	
					 		c1.setTime(slot.in);
					 		Calendar c2=Calendar.getInstance(); 
					 		c2.setTime(slot.out);	
					 		long dif=(c2.getTimeInMillis()-c1.getTimeInMillis())/1000 ;
					 		String how="";
							if (dif<3600){how=dif/60+" minutes";}
							else if ((dif>=3600)&&(dif % 3600==0)){how=dif/3600+" hrs";}
							else{how=dif/3600+"hrs, "+(dif%3600)/60+"mins";}														 		
     Label time=new Label(how);time.setStyleName("headate");
     sus.addComponent(time);sus.setComponentAlignment(time, Alignment.MIDDLE_RIGHT);
     HorizontalLayout jeos=new HorizontalLayout();
     Button mkSug=new Button("MAKE SUGGESTION");mkSug.setHeight("25px");mkSug.setStyleName("mksug");
     jeos.addComponent(mkSug);
	 mkSug.addClickListener(new ClickListener() {
		public void buttonClick(ClickEvent event) {
			Window forsug=new Window();forsug.setWidth("340px");forsug.setStyleName("forsug");
			VerticalLayout vl=new VerticalLayout();
			TextArea texta=new TextArea("Make a suggestion to "+slot.user.prenume);texta.setWidth("100%");
			texta.setStyleName("texta");
			vl.addComponent(texta);
			Button send=new Button("Send");send.setWidth("100%");
			send.addClickListener(new ClickListener(){
				public void buttonClick(ClickEvent event) {
					String text=texta.getValue();
					if (text.length()>0&&text.length()<142){
					Sugestie sug=new Sugestie(us3r, text, slot);				
					forsug.close();
					App.mksug(us3r.mail, text, slot.user.mail,slot,sug.id);
					slot.sugestii.add(sug);
					Notification.show("Suggestion sent to "+slot.user.prenume);
					}else if (text.length()<0){Notification.show("Write something first.");}						
					else if(text.length()>141){Notification.show("Only 141 characters can fit in a suggestion.");}											
				}
			});
			vl.addComponent(send);
			forsug.setContent(vl);
			UI.getCurrent().addWindow(forsug);forsug.center();
		}
	});     
    vl.addComponent(sus);
    vl.addComponent(jeos);
    return vl;
 }
	 @SuppressWarnings("deprecation")
	ComponentContainer othpplslot(Slot slot,User user){
		 int weeknr= slot.in.getDay();
	     	int monthnr=slot.in.getMonth();
	     	String weekday;
	     	String mon="Hop";
	     	if (weeknr==1){weekday="Monday";}else if(weeknr==2){weekday="Tuesday";}
	     	else if(weeknr==3){weekday="Wednesday";}else if(weeknr==4){weekday="Thursday";}
	     	else if(weeknr==5){weekday="Friday";}else if(weeknr==6){weekday="Saturday";}else{weekday="Sunday";}
	     	if(monthnr==0){mon="Jan";}else if(monthnr==1){mon="Feb";}else if(monthnr==2){mon="Mar";}
	     	else if(monthnr==3){mon="Apr";} if(monthnr==4){mon="May";} else if(monthnr==5){mon="Jun";}
	     	else if(monthnr==6){mon="Jul";} if(monthnr==7){mon="Aug";}else if(monthnr==8){mon="Sep";}
	     	else if(monthnr==9){mon="Oct";}else if(monthnr==10){mon="Nov";}else if(monthnr==11){mon="Dec";}
	     	String data="  "	+weekday+", "+mon+" "+slot.in.getDate();
	     Label datili=new Label(data);	datili.setStyleName("datili");	     
		 Image pic=new Image();		 
		 if(user.poza==true&&user.fb==false){pic.setSource(new ClassResource("pics/"+user.mail+".png"));}
			else if(user.poza==false&&user.fb==true){pic.setSource(new ExternalResource(user.urlpic));}
			else if(user.poza==true&&user.fb==true){pic.setSource(new ExternalResource(user.urlpic));}
			else{pic.setSource(new ClassResource("pics/a.png"));}		 
		 pic.setHeight("61px");
		 Button mkSug=new Button("MAKE SUGGESTION");mkSug.setHeight("25px");mkSug.setStyleName("mksug");
		 mkSug.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				Window forsug=new Window();forsug.setWidth("340px");forsug.setStyleName("forsug");
				VerticalLayout vl=new VerticalLayout();
				TextArea texta=new TextArea("Make a suggestion to "+user.prenume);texta.setWidth("100%");
				texta.setStyleName("texta");
				texta.addValidator(new StringLengthValidator(
					    "Suggestions can't be longer than 141 characters.",
					    0, 141, true));
					texta.setImmediate(true);
					texta.isValidationVisible();
				vl.addComponent(texta);
				Button send=new Button("Send");send.setWidth("100%");
				send.addClickListener(new ClickListener(){
					public void buttonClick(ClickEvent event) {
						String text=texta.getValue();
						if (text.length()>0&&text.length()<142){
						Sugestie sug=new Sugestie(us3r, text, slot);				
						forsug.close();
						App.mksug(us3r.mail, text, user.mail,slot,sug.id);
						slot.sugestii.add(sug);
						Notification.show("Suggestion sent to "+user.prenume);
						}else if (text.length()<0){Notification.show("Write something first.");}						
						else if(text.length()>141){Notification.show("Only 141 characters can fit in a suggestion.");}
					}
				});
				vl.addComponent(send);
				forsug.setContent(vl);
				UI.getCurrent().addWindow(forsug);forsug.center();
			}
		});
		 HorizontalLayout hl=new HorizontalLayout();hl.setWidth("100%");hl.setHeight("61px");
		 Random rand=new Random();
		 hl.setStyleName("post"+rand.nextInt(9));
		 hl.addComponent(pic);hl.setExpandRatio(pic, 2);
		 //hl.addComponent(test);hl.setComponentAlignment(test, Alignment.MIDDLE_LEFT);test.setStyleName("test");
		 VerticalLayout vl=new VerticalLayout();vl.setWidth("100%");vl.setHeight("100%"); 
		 vl.setStyleName("trans");
		 hl.addComponent(vl); hl.setExpandRatio(vl, 13);
		 HorizontalLayout tag=new HorizontalLayout();tag.setHeight("23px");tag.setWidth("100%");tag.setStyleName("baraslot");		 
		 vl.addComponent(tag);
		 String cine=user.prenume+" "+user.nume;
		 Button vintila=new Button(cine);vintila.setStyleName("tag");vintila.setHeight("23px");
		 vintila.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				tot.removeAllComponents();
				tot.addComponent(terasa);
				tot.addComponent(profile(user));			
			}
		});
		 tag.addComponent(vintila);tag.setComponentAlignment(vintila, Alignment.TOP_LEFT);
		 tag.addComponent(datili);tag.setComponentAlignment(datili, Alignment.TOP_RIGHT);
		 String oraIn;
         int maiMicIn=slot.in.getMinutes();
         if (maiMicIn<10){oraIn=slot.in.getHours()+":0"+maiMicIn;}else{oraIn=slot.in.getHours()+":"+maiMicIn;} 
         String oraOut;
         int maiMicOut=slot.out.getMinutes();
         if (maiMicOut<10){oraOut=slot.out.getHours()+":0"+maiMicOut;}else{oraOut=slot.out.getHours()+":"+maiMicOut;}  
         Label orl=new Label(oraIn+" - "+oraOut);orl.setStyleName("orain");orl.setHeight("25px");
		 HorizontalLayout jos=new HorizontalLayout();jos.setStyleName("trans");jos.setWidth("100%");
		 vl.addComponent(jos);vl.setComponentAlignment(jos, Alignment.TOP_LEFT);
		 jos.addComponent(orl);jos.setComponentAlignment(orl, Alignment.TOP_LEFT);
		 jos.addComponent(mkSug);jos.setComponentAlignment(mkSug, Alignment.TOP_RIGHT);
 	     return hl;
	 }
	@SuppressWarnings("deprecation")
	ComponentContainer post(Date cand, User cine, byte ce, Slot slot ){		   
				        Calendar c1=Calendar.getInstance();
				 		Date d1=new Date();
				 		c1.setTime(slot.cand);
				 		Calendar acuma=Calendar.getInstance();
				 		acuma.setTime(d1);
			 			long dif=(acuma.getTimeInMillis()-c1.getTimeInMillis())/1000;
			 			String timeFrame=null;
			 			if (dif<400){timeFrame="just now";}
			 			else if (dif>=400&&dif<3600){timeFrame=dif/60 +" minutes ago";}
			 			else if (dif>=3600&&dif<7200){timeFrame= "One hour ago";}
			 			else if (dif>=7200&&dif<86400){timeFrame=dif/3600+" hours ago";}
			 			else if (dif>=86400&&dif<192000){timeFrame="One day ago";}
			 			else if (dif>=192000){timeFrame=((dif/216000)+1)+ " days ago";}		 
			 			@SuppressWarnings("unused")
						Label timp=new Label(timeFrame);
			 			Calendar cin=Calendar.getInstance();cin.setTime(slot.in);
			 			Calendar cout=Calendar.getInstance();cout.setTime(slot.out);
			 			long dif2=(cout.getTimeInMillis()-cin.getTimeInMillis())/1000;
			 			String how;
						if (dif2<3600){how=dif2/60+" minutes";}
						else if ((dif2>=3600)&&(dif2 % 3600==0)){how=dif2/3600+" hours";}
						else{how=dif2/3600+" hours and "+(dif2%3600)/60+" minutes";}
						int weeknr= slot.in.getDay();
				     	int monthnr=slot.in.getMonth();
				     	String weekday;
				     	String mon="Feb";
				     	if (weeknr==1){weekday="Monday";}else if(weeknr==2){weekday="Tuesday";}
				     	else if(weeknr==3){weekday="Wednesday";}else if(weeknr==4){weekday="Thursday";}
				     	else if(weeknr==5){weekday="Friday";}else if(weeknr==6){weekday="Saturday";}else{weekday="Sunday";}
				     	if(monthnr==0){mon="Jan";}else if(monthnr==1){mon="Feb";}else if(monthnr==2){mon="Mar";}
				     	else if(monthnr==3){mon="Apr";} if(monthnr==4){mon="May";} else if(monthnr==5){mon="Jun";}
				     	else if(monthnr==6){mon="Jul";} if(monthnr==7){mon="Aug";}else if(monthnr==8){mon="Sep";}
				     	else if(monthnr==9){mon="Oct";}else if(monthnr==10){mon="Nov";}else if(monthnr==11){mon="Dec";}
				     	String data="  "	+weekday+", "+mon+" "+slot.in.getDate();
				     	String oraIn;
				         int maiMicIn=slot.in.getMinutes();
				         if (maiMicIn<10){oraIn=slot.in.getHours()+":0"+maiMicIn;}else{oraIn=slot.in.getHours()+":"+maiMicIn;} 		 
		 VerticalLayout ml=new VerticalLayout();
		 CssLayout vl=new CssLayout();
		 ml.addComponent(vl);vl.setStyleName("paddingleft");
		 Random rand=new Random();
		 ml.setStyleName("pos"+rand.nextInt(9));
		 HorizontalLayout numele=new HorizontalLayout();
		 String vintila=cine.prenume+" "+cine.nume;
		 boolean eu=false;
		 if (cine.equals(us3r)){vintila="You ";eu=true;}	
		 Button om=new Button(vintila);om.setStyleName("tag2");om.setHeight("14px");
		 om.addClickListener(new ClickListener() {
			public void buttonClick(ClickEvent event) {
				tot.removeAllComponents();
				tot.addComponent(terasa);
				tot.addComponent(profile(cine));
			}
		});		 	 
		 numele.addComponent(om);	 
		 if (ce==1){
			 String facut=" added "+how;
			 String laData="on "+data+",";
			 String starting=" starting at ";
			 String ora=oraIn;
			 Label rasfacut=new Label(facut);rasfacut.setStyleName("postfont");
			 Label sp=new Label();sp.setWidth("5px");
			 Label space=new Label();space.setWidth("5px");
			 Label spac=new Label();spac.setWidth("5px");
			 Label rasData=new Label(laData);rasData.setStyleName("postfont");Label rastarting=new Label(starting);
			 rastarting.setStyleName("postfont");Label rasOra=new Label(ora);rasOra.setStyleName("postfont");			
			 om.setIcon(FontAwesome.PLUS_CIRCLE);
			 rasfacut.setSizeUndefined();
			 rasData.setSizeUndefined();rastarting.setSizeUndefined();rasOra.setSizeUndefined();
			 vl.addComponent(numele);vl.addComponent(rasfacut);vl.addComponent(space);space.setHeight("0px");	 
			 vl.addComponent(rasData);vl.addComponent(spac);vl.addComponent(rastarting);vl.addComponent(sp);vl.addComponent(rasOra);
			 Button makeit=new Button();makeit.setIcon(FontAwesome.COMMENT);makeit.setHeight("20px");makeit.setStyleName("makeit");				 
			if (eu==false&&slot.out.after(d1)){
			vl.addComponent(makeit);
			makeit.setDescription("Make suggestion!");
			 makeit.addClickListener(new ClickListener() {
				public void buttonClick(ClickEvent event) {
					Window forsug=new Window();forsug.setWidth("340px");forsug.setStyleName("forsug");
					VerticalLayout vl=new VerticalLayout();
					TextArea texta=new TextArea("Make a suggestion to "+cine.prenume);texta.setWidth("100%");
					texta.setStyleName("texta");
					vl.addComponent(texta);
					Button send=new Button("Send");send.setWidth("100%");
					send.addClickListener(new ClickListener(){
						public void buttonClick(ClickEvent event) {
							String text=texta.getValue();
							if (text.length()>0&&text.length()<142){
							Sugestie sug=new Sugestie(us3r, text, slot);		
							forsug.close();
							App.mksug(us3r.mail, text, cine.mail,slot,sug.id);
							slot.sugestii.add(sug);
							Notification.show("Suggestion sent to "+cine.prenume);
							}else if (text.length()<0){Notification.show("Write something first.");}
							else if(text.length()>141){Notification.show("Only 141 characters can fit in a suggestion.");}			
						}
					});
					vl.addComponent(send);
					forsug.setContent(vl);
					UI.getCurrent().addWindow(forsug);forsug.center();
				}				
			});			 
		 }}
		 if (ce==2&&slot.cu!=null){
			 String facut=" accepted ";			 
			 String cui=slot.cu.prenume+" "+slot.cu.nume+"'s";
			 String sug="suggestion for ";
			 String laData=data+".";
			 Label rasfacut=new Label(facut);rasfacut.setSizeUndefined();rasfacut.setStyleName("postfont");
			 Label rascui=new Label(cui);rascui.setSizeUndefined();rascui.setStyleName("postfont");
			 Label rasug=new Label(sug);rasug.setSizeUndefined();rasug.setStyleName("postfont");
			 Label rasdata=new Label(laData);rasdata.setSizeUndefined();rasdata.setStyleName("postfont");			 
			 Label sp=new Label();sp.setWidth("5px");
			 Label space=new Label();space.setWidth("5px");
			 Label spac=new Label();spac.setWidth("5px");
			 om.setIcon(FontAwesome.SMILE_O);			
			 vl.addComponent(numele);vl.addComponent(rasfacut);vl.addComponent(space);vl.addComponent(rascui);vl.addComponent(spac);vl.addComponent(rasug);
			 vl.addComponent(sp);vl.addComponent(rasdata);
		 }	
		 if (ce==3&&slot.cu!=null){
			 String facut=cine.prenume+" accepted "+slot.cu.prenume+" "+slot.cu.nume+"'s suggestion for "+data+".";
			 Label rasfacut=new Label(facut);rasfacut.setStyleName("belle");		 
			 om.setIcon(FontAwesome.SMILE_O);
			 Label deCand=new Label(timeFrame);deCand.setStyleName("headate");
			 ml.removeAllComponents();
			 ml.addComponent(deCand);
			 vl.addComponent(rasfacut);
			 ml.addComponent(vl);;;;
		 }	
		 return ml;
	 }
	static void salveaza(TimeinUI ui, User user){
		 ui.us3r=user;
		 ui.getSession().setAttribute("valoareaMea", user);
		 if (VaadinService.getCurrentRequest()!=null){
			 System.out.println(VaadinService.getCurrentRequest().toString());
			 System.out.println(VaadinService.getCurrentRequest().getWrappedSession());
		 VaadinService.getCurrentRequest().getWrappedSession().setAttribute("valoareaMea", user);
	 }else{System.out.println("e nul!");}}
	static String timeFrame(Date date){
		Calendar c1=Calendar.getInstance();
 		Date d1=new Date();
 		c1.setTime(date);
 		Calendar acuma=Calendar.getInstance();
 		acuma.setTime(d1);
			long dif=(acuma.getTimeInMillis()-c1.getTimeInMillis())/1000;
			String timeFrame=null;
			if (dif<400){timeFrame="just now";}
			else if (dif>=400&&dif<3600){timeFrame=dif/60 +" minutes ago";}
			else if (dif>=3600&&dif<7200){timeFrame= "One hour ago";}
			else if (dif>=7200&&dif<86400){timeFrame=dif/3600+" hours ago";}
			else if (dif>=86400&&dif<192000){timeFrame="One day ago";}
			else if (dif>=192000){timeFrame=((dif/216000)+1)+ " days ago";}		
		return timeFrame;
	} 
	ComponentContainer calendar(){
	VerticalLayout vl=new VerticalLayout();
	com.vaadin.ui.Calendar cal =new com.vaadin.ui.Calendar("");
	for (int i=0;i<us3r.events.size();i++){cal.addEvent(us3r.events.get(i));}
	cal.setWidth("100%");
	vl.addComponent(cal);
		 return vl;
	}
	ComponentContainer settings(){	
		HorizontalLayout coloane=new HorizontalLayout();coloane.setSpacing(true);coloane.setStyleName("degajat2");
		VerticalLayout stanga=new VerticalLayout();
		VerticalLayout dreapta=new VerticalLayout();dreapta.setWidth("380px");
		VerticalLayout facebook=new VerticalLayout();
		VerticalLayout fbops=new VerticalLayout();
		VerticalLayout location=new VerticalLayout();
		OpenStreetMapGeocoder geocoder = OpenStreetMapGeocoder.getInstance();
		geocoder.setLimit(25);
		final LocationTextField<GeocodedLocation> ltf = new LocationTextField<GeocodedLocation>(geocoder, GeocodedLocation.class);
		ltf.setCaption("Address: ");
        ltf.setWidth("100%");
        ltf.setImmediate(true);
        location.addComponent(ltf);
        Label locatie=new Label();location.addComponent(locatie);
        if (us3r.geo!=null){locatie.setValue(us3r.geo.getDisplayString());}
        Label lat=new Label();
        Label lon=new Label();
        GeocodedLocation hopa=new GeocodedLocation();
        hopa.setLat(47.5327387);hopa.setLon(25.8173527992344);
        Label test=new Label(hopa.getDisplayString());
        Label unu=new Label(hopa.getLocality());
        location.addComponent(test);location.addComponent(unu);
        ltf.addLocationValueChangeListener(new ValueChangeListener() {			
			public void valueChange(ValueChangeEvent event) {
				GeocodedLocation loc = (GeocodedLocation)event.getProperty().getValue();
                if (loc != null) {
                	us3r.geo=loc;
                	lat.setValue("" + us3r.geo.getLat());
                    lon.setValue("" + us3r.geo.getLon());
                    locatie.setValue(""+us3r.geo.getDisplayString());
                    location.addComponent(lat);location.addComponent(lon);
                } else {
                    lat.setValue("");
                    lon.setValue("");
                }	
			}
		});
		coloane.addComponent(stanga);coloane.addComponent(dreapta);//coloane.addComponent(location);
		Label link=new Label();
		Button linkfb=new Button();
		link.setContentMode(ContentMode.HTML);
		Switch faceSlots=new Switch();Switch faceEvents=new Switch();Switch act=new Switch();
		if (us3r.fb){
			link.setValue(FontAwesome.FACEBOOK_OFFICIAL.getHtml() + " Facebook is linked");
			linkfb.setCaption("Unlink");linkfb.setIcon(FontAwesome.UNLINK);
		}else{
			link.setValue(FontAwesome.FACEBOOK_OFFICIAL.getHtml() + " Facebook is unlinked");
			linkfb.setCaption("Link");linkfb.setIcon(FontAwesome.LINK);
			faceSlots.setValue(false);faceSlots.setReadOnly(true);
			faceEvents.setValue(false);faceEvents.setReadOnly(true);
			act.setValue(false);act.setReadOnly(true);
			fbops.addLayoutClickListener(new LayoutClickListener() {
				@Override
				public void layoutClick(LayoutClickEvent event) {
					if(!us3r.fb){Notification.show("Link your Facebook account to edit these settings.");}
				}
			});	
		}
		HorizontalLayout linkFb=new HorizontalLayout();
		HorizontalLayout postFbslt=new HorizontalLayout();postFbslt.setSpacing(true);postFbslt.setStyleName("facebook");
		HorizontalLayout postFbevt=new HorizontalLayout();postFbevt.setSpacing(true);postFbevt.setStyleName("facebook");
		HorizontalLayout activity=new HorizontalLayout();activity.setSpacing(true);activity.setStyleName("facebook");
		Label fbslots=new Label("Post my free time slots");
		Label fbevents=new Label("Post my events");
		Label activ=new Label("Post my activity");
		linkFb.addComponent(link);linkFb.addComponent(linkfb);
		linkfb.setStyleName("linkfb");linkfb.setHeight("25px");
		linkFb.setSpacing(true);
		postFbslt.addComponent(faceSlots);postFbslt.addComponent(fbslots);
		postFbevt.addComponent(faceEvents);postFbevt.addComponent(fbevents);
		activity.addComponent(act);activity.addComponent(activ);
		facebook.addComponent(linkFb);
		linkfb.addClickListener(new ClickListener() {			
			@Override
			public void buttonClick(ClickEvent event) {
				if (us3r.fb){
					faceSlots.setValue(false);faceSlots.setReadOnly(true);
					faceEvents.setValue(false);faceEvents.setReadOnly(true);
					act.setValue(false);act.setReadOnly(true);
					us3r.fb=false;
					link.setValue(FontAwesome.FACEBOOK_OFFICIAL.getHtml() + " Facebook is unlinked");
					linkfb.setCaption("Link");linkfb.setIcon(FontAwesome.LINK);
					fbops.addLayoutClickListener(new LayoutClickListener() {
						@Override
						public void layoutClick(LayoutClickEvent event) {
							if(!us3r.fb){Notification.show("Link your Facebook account to edit these settings.");}
						}
					});
				}
				else{
					faceSlots.setReadOnly(false);faceSlots.setValue(true);
					faceEvents.setReadOnly(false);	
					act.setReadOnly(false);
					us3r.fb=true;
					link.setValue(FontAwesome.FACEBOOK_OFFICIAL.getHtml() + " Facebook is 	linked");
					linkfb.setCaption("Unlink");linkfb.setIcon(FontAwesome.UNLINK);
				}
			}
		});
		facebook.setStyleName("facebook");
		facebook.setComponentAlignment(linkFb, Alignment.TOP_CENTER);
		facebook.setSpacing(true);
		fbops.addComponent(postFbslt);fbops.addComponent(postFbevt);fbops.addComponent(activity);
		facebook.addComponent(fbops);fbops.setStyleName("fbops");
		Image pic=new Image();		
		if(us3r.poza==true&&us3r.fb==false){pic.setSource(new ClassResource("pics/"+us3r.mail+".png"));}
		else if(us3r.poza==false&&us3r.fb==true){pic.setSource(new ExternalResource(us3r.urlpic));}
		else if(us3r.poza==true&&us3r.fb==true){pic.setSource(new ExternalResource(us3r.urlpic));}
		else{pic.setSource(new ClassResource("pics/a.png"));}
		pic.setHeight("250px");
		VerticalLayout poza=new VerticalLayout();
		pic.setDescription("Click to set profile pic");
		poza.addComponent(pic);
		Upload upload=new Upload();upload.setWidth("250px");			
		poza.addLayoutClickListener(new LayoutClickListener() {			
			@Override
			public void layoutClick(LayoutClickEvent event) {
				Notification.show("Dadusi click pe poza");;;					
			}
		});
		Label name=new Label(us3r.prenume+" "+us3r.nume);
		  name.setStyleName("name");
		VerticalLayout subpoza=new VerticalLayout();
		subpoza.setStyleName("facebook");
		subpoza.setSpacing(true);
		Label total=new Label("Currently sharing: "+us3r.totalTimeSharing());total.setStyleName("mij");
		stanga.addComponent(name);stanga.setComponentAlignment(name, Alignment.TOP_CENTER);
		stanga.addComponent(poza);stanga.setStyleName("forsug");
		stanga.addComponent(total);stanga.setComponentAlignment(total, Alignment.MIDDLE_CENTER);
		dreapta.addComponent(facebook);
		dreapta.setStyleName("forsug");		
		return coloane;
	}
	static String encrypta(){
		String crypt="";
		Random rand=new Random();
		for (int i=0;i<6;i++){
			crypt=crypt+rand.nextInt(10);	
		}return crypt;
	}
	OAuthService createOAuthService() {
		ServiceBuilder sb = new ServiceBuilder();
		sb.provider(FACEBOOK_API.scribeApi);
		sb.apiKey(FACEBOOK_API.apiKey);
		sb.apiSecret(FACEBOOK_API.apiSecret);
		sb.callback("http://www.google.fi");
		return sb.build();
	}	
}