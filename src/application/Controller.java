package application;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Locale.Category;
import java.util.Random;

import javax.script.ScriptException;
import javax.swing.JFileChooser;

import cspFonctions.CSP;
import cspFonctions.Contrainte;
import cspFonctions.Element;
import cspFonctions.Fonctions;
import cspFonctions.Matrice;
import cspFonctions.Variable;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;

public class Controller {

       @FXML
       private TextArea AfficheCont;

	
		String Filepath;
		@FXML
		private AnchorPane anchor;

	    @FXML
	    public static RadioButton RadioAlea;

	    @FXML
	    public static RadioButton RadioMeta;
	    
	   @FXML
	    private Button lancerCons;

	    @FXML
	    private TableView<?> Resultat1;

	    @FXML
	    private TableView<?> tabMpInit;

	    @FXML
	    private TableView<?> tabMpFinale;
	    

	    @FXML
	    private TableView<?> Resultat2;

	    @FXML
	    private TableView<?> tabMpInit2;

	    @FXML
	    private TableView<?> tabMpFinale2;
	    
	    @FXML
	    private Button lancerAlea;
	    @FXML
	    private TableView<?> tabResAlea;

	    @FXML
	    private TableView<?> tabResMeta;

	    @FXML
	    private Text textimeAlea;

	    @FXML
	    private Text textimeMeta;
	    
	    @FXML
	    private TableView<?> tabResAlea1;

	    @FXML
	    private TableView<?> tabResMeta1;

	    @FXML
	    private Text textimeAlea1;

	    @FXML
	    private Text textimeMeta1;
	    
	    @FXML
	    private TextArea textNbVar;

	    @FXML
	    private LineChart<String, Number> linear_chart;

	    @FXML
	    private LineChart<String, Number> linear_chart1;
	    
  	    XYChart.Series<String,Number> seriesalea = new XYChart.Series<String,Number>();
  	    XYChart.Series<String,Number> seriesmeta = new XYChart.Series<String,Number>();
	 
  	    XYChart.Series<String,Number> seriesaleaChoisi = new XYChart.Series<String,Number>();
	    XYChart.Series<String,Number> seriesmetaChoisi = new XYChart.Series<String,Number>();
	    @FXML
	    final ToggleGroup group = new ToggleGroup();
	   // RadioAlea.setToggleGroup(group);

		private XYChart.Series<Number, Number> series;

    @FXML
    void Lancer_Consistant(ActionEvent event) {
    	tabMpInit.getItems().clear();
    	tabMpFinale.getItems().clear();
    	Resultat1.getItems().clear();
    	
    	
		ArrayList<Integer> domaine1=new ArrayList<Integer>();
		ArrayList<Integer> domaine2=new ArrayList<Integer>();
		ArrayList<Integer> domaine3=new ArrayList<Integer>();

        domaine1.add(1);
        domaine1.add(2);
        domaine1.add(3);
        domaine1.add(4);
        domaine1.add(5);
		Variable variable1=new Variable(0,domaine1); 
		
		domaine2.add(1);
        domaine2.add(2);
        domaine2.add(3);
        domaine2.add(9);
        domaine2.add(10);
		Variable variable2=new Variable(1,domaine2);
        
		domaine3.add(1);
        
        domaine3.add(2);
        domaine3.add(3);
        domaine3.add(4);
        domaine3.add(5);
		Variable variable3=new Variable(2,domaine3);
    	int [][] mat=Fonctions.Remplir();
        Variable [] X={variable1,variable2,variable3};
        
        Contrainte con1=new Contrainte(variable1,variable2,mat);
        Contrainte con2=new Contrainte(variable1,variable3,mat);
        Contrainte con3=new Contrainte(variable2,variable3,mat);
        
    	Fonctions.MpTemp=new Matrice [3][3];

        Contrainte C[]= {con1,con2,con3};
        CSP csp=new CSP(X,C);
	 	Matrice [][] Mp;
	    Mp= Fonctions.InitialiserMp(csp);
	    String [][] MpString=Matrice.MptoString(Mp); 
	    
	    
	    RemplirMpView(MpString,tabMpInit);
	    System.out.println(MpString);
	    
	    ArrayList <Element> A=new ArrayList<Element>();
	    boolean bool=  Fonctions.Look_Ahead(A,csp,Mp,2);
	    
	    
	    String [][] MpString2=Matrice.MptoString(Mp);
	  

	    //Fonctions.DisplayMp(Mp);
	   // Fonctions.AfficherDomaine(csp);
	    RemplirMpView(MpString2,tabMpFinale);
	    RemplirResultatView(csp,Resultat1);
	    
	    
	    
    }
    void RemplirResultatView(CSP csp,TableView tab) {
    		ArrayList<Integer> res=Fonctions.AfficherResultat(csp);
    	     
    		tab.getColumns().clear();
    	
             TableColumn<ObservableList<String>, String> column = new TableColumn<>("Variable");
	         column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(0)) );
	         tab.getColumns().add(column);
	         
	         column = new TableColumn<>("Valeur");
	         column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(1)) );
	         tab.getColumns().add(column);
    		
    	
    	for (int row = 0; row < res.size(); row++) {
      	    ObservableList<String> data1 = FXCollections.observableArrayList();
       	    		data1.add(0,String.valueOf("X"+row ));
       	    		data1.add(1,String.valueOf(res.get(row)) );

   		    	
       	    tab.getItems().add(data1);
       	    }
    	
    	
    }

    
    void RemplirMpView(String [][] MpString,TableView tab) {
    	//tab.getItems().clear();
    	tab.refresh();
    	 tab.getColumns().clear();
    	TableColumn<ObservableList<String>, String> column = new TableColumn<>(" ");
        column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(0)) );
        tab.getColumns().add(column);
    	
        for (int i = 1; i <= MpString.length; i++) {
             final int finalIdx = i;
             column = new TableColumn<>("X"+Integer.toString(i-1));
	         column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(finalIdx)) );
	         tab.getColumns().add(column);
   
    		 }
	       
//Remplir ligne 
    	for (int row = 0; row < MpString.length; row++) {
   	     ObservableList<String> data1 =FXCollections.observableArrayList();
 		 data1.add(0, "X"+Integer.toString(row));
 		 System.out.println(Integer.toString(row));
 		    
    	    for (int column1 = 1;column1<=MpString.length; column1++) {
    	    		data1.add(column1, (MpString[row][column1-1]));
		    	    System.out.println(MpString[row][column1-1]);
		    	    System.out.println("eee");
    	    		}
            
    	    tab.getItems().add(data1);
    	    
    	    }
         
  }
    
    
    
  

    @FXML
    void Lancer_Alea(ActionEvent event) {
    	
    	linear_chart.getData().clear();
    	tabResAlea.getItems().clear();
    	tabResMeta.getItems().clear();
    	textimeAlea.setText("");
    	textimeMeta.setText("");
    	
     	int nbVariable=Fonctions.Rand(20,40);
     	int tailleDomaine=Fonctions.Rand(10,20);
    	int nbContrainte=Fonctions.Rand(20,30);
    	
    	
    	

    	 ArrayList<Integer> domaine=Fonctions.RemplirDomaine(tailleDomaine);
         CSP csp=new CSP();
         
         csp=Fonctions.CSPAlea(nbContrainte,nbVariable,tailleDomaine,domaine);
     	 Fonctions.AfficherDomaine(csp);
    	
    	 //Version2********************

	    CSP csp2=new CSP(csp);

	    Matrice [][]MpUniv2=Fonctions.MpUniver(csp2);
    	Matrice [][]Mp2=Fonctions.InitMpAlea(MpUniv2,csp2);
    	Fonctions.MpTemp=new Matrice [nbVariable][nbVariable];
     
	    ArrayList <Element> A2=new ArrayList<Element>();
	    long startTime2 = System.currentTimeMillis();
	    boolean bool2=  Fonctions.Look_Ahead(A2,csp2,Mp2,2);
	    long endTime2 = System.currentTimeMillis();
	    System.out.println("fini2");

	    
    	
    	//VErsion 1 *********************************
    

    	CSP csp1=new CSP(csp);

    	Matrice [][]MpUniv=Fonctions.MpUniver(csp1);
    	Matrice [][]Mp=Fonctions.InitMpAlea(MpUniv,csp1);
    	Fonctions.MpTemp=new Matrice [nbVariable][nbVariable];

	    ArrayList <Element> A=new ArrayList<Element>();
	    long startTime = System.currentTimeMillis();
	    boolean bool=  Fonctions.Look_Ahead(A,csp1,Mp,1);
	    long endTime = System.currentTimeMillis();
	    
       
  
	    
	   
	    /////////////////////l affichage + courbe
	    if(bool) { RemplirResultatView(csp1,tabResAlea);  
	    textimeAlea.setText("Temps d'execution = "+(float)(endTime-startTime)+" ms");
	    seriesalea.getData().add(new XYChart.Data<String,Number>(Integer.toString(nbVariable),(float)(endTime-startTime))); 
	    }

	    else  {textimeAlea.setText("Pas de solutions"); seriesalea.getData().add(new XYChart.Data<String,Number>(Integer.toString(nbVariable),0));}
	    
	    
	   if(bool2) {RemplirResultatView(csp2,tabResMeta); textimeMeta.setText("Temps d'execution = "+(float)(endTime2-startTime2)+" ms");
	   seriesmeta.getData().add(new XYChart.Data<String,Number>(Integer.toString(nbVariable),(float)(endTime2-startTime2)));}

	    else { textimeMeta.setText("Pas de solutions");  seriesmeta.getData().add(new XYChart.Data<String,Number>(Integer.toString(nbVariable),0)); }

	    	 seriesalea.setName("Selection AléAtoire");
	    	 seriesmeta.setName("Selection avec métaheuristique");

	    	 linear_chart.getData().clear();
	         linear_chart.getData().addAll(seriesmeta,seriesalea);
	         

	   
	    }
	   
     
    
    
    @FXML
    void Selection_Cont(ActionEvent event) {
	 	FileChooser fileChooser = new FileChooser();
	 	fileChooser.setInitialFileName("C:\\Users\\naila\\Desktop\\M2\\PPC");
    	Stage stage=(Stage)anchor.getScene().getWindow();
    	File file=fileChooser.showOpenDialog(stage);
    	if(file!=null) {
    		 Filepath=file.getPath();
    	    	


    	}
    	
		
    } 
    @FXML
    void Lancer_Choisi(ActionEvent event) throws FileNotFoundException, IOException, ScriptException  {
    	linear_chart1.getData().clear();
    	tabResAlea1.getItems().clear();
    	tabResMeta1.getItems().clear();
    	textimeAlea1.setText("");
    	textimeMeta1.setText("");
    	
    	 String contString="" ;
    	 CSP csp=Fonctions.CspChoisi(Filepath,contString,AfficheCont);

    	 ///fonction alea
    	CSP csp1=new CSP(csp,0);
     	Matrice [][]MpUniv=Fonctions.MpUniver(csp1);
     	Matrice [][]Mp=Fonctions.InitMpChoisi(MpUniv,csp1);
     	//Fonctions.DisplayMp(Mp);
     	Fonctions.MpTemp=new Matrice [csp1.X.length][csp1.X.length];
     	
     	ArrayList <Element> A=new ArrayList<Element>();
 	    long startTime = System.currentTimeMillis();
 	    boolean bool=  Fonctions.Look_Ahead(A,csp1,Mp,1);
 	    long endTime = System.currentTimeMillis();
 	  
    	
      //methode heutistique
     	CSP csp2=new CSP(csp,0);
     	Matrice [][]MpUniv2=Fonctions.MpUniver(csp2);
     	Matrice [][]Mp2=Fonctions.InitMpChoisi(MpUniv2,csp1);
     	Fonctions.MpTemp=new Matrice [csp2.X.length][csp2.X.length];
     	
     	ArrayList <Element> A2=new ArrayList<Element>();
 	    long startTime2 = System.currentTimeMillis();
 	    boolean bool2=  Fonctions.Look_Ahead(A2,csp2,Mp2,2);
 	    long endTime2 = System.currentTimeMillis();
 	    int nbVariable=csp.X.length;
 	    
 	   /////////////////////l affichage + courbe
	    if(bool) { RemplirResultatView(csp1,tabResAlea1);  
	    textimeAlea1.setText("Temps d'execution = "+(float)(endTime-startTime)+" ms");
	    seriesaleaChoisi.getData().add(new XYChart.Data<String,Number>(Integer.toString(nbVariable),(float)(endTime-startTime))); 
	    }

	    else  {textimeAlea1.setText("Pas de solutions"); seriesaleaChoisi.getData().add(new XYChart.Data<String,Number>(Integer.toString(nbVariable),0));}
	    
	    
	   if(bool2) {RemplirResultatView(csp2,tabResMeta1); textimeMeta1.setText("Temps d'execution = "+(float)(endTime2-startTime2)+" ms");
	   seriesmetaChoisi.getData().add(new XYChart.Data<String,Number>(Integer.toString(nbVariable),(float)(endTime2-startTime2)));}

	    else { textimeMeta1.setText("Pas de solutions");  seriesmetaChoisi.getData().add(new XYChart.Data<String,Number>(Integer.toString(nbVariable),0)); }

	    	 seriesaleaChoisi.setName("Selection AléAtoire");
	    	 seriesmetaChoisi.setName("Selection avec métaheuristique");

	    	 linear_chart1.getData().clear();
	         linear_chart1.getData().addAll(seriesmetaChoisi,seriesaleaChoisi);
 	    
     	
    	}
    	
   
    
    
  int ChoixVersion() {
	  if(RadioAlea.isSelected()) return 1;
	  else return 2;
	  
	 
  }
    
}
