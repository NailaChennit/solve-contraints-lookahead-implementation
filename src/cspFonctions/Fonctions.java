package cspFonctions;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import javafx.scene.control.TextArea;

// Qd l user entrela valeur des domaine odrdonne les ordre croissant
public class Fonctions {
   	public static Matrice [][] MpTemp;
	boolean REVISE_pc(Variable xi,Variable xk,Variable xj,Matrice [][]Mp) {
		int[][] temp ;
		temp=produit(Mp[xi.nom][xk.nom].matrice,Mp[xk.nom][xk.nom].matrice,Mp[xk.nom][xj.nom].matrice);
		temp=jointure(temp,Mp[xi.nom][xj.nom].matrice);
		
		if(!areSame(Mp[xi.nom][xj.nom].matrice,temp)) {
			Mp[xi.nom][xj.nom].matrice=temp;
			return true;
		}
		return false;
	}
    
	static boolean PC2(CSP csp,Matrice [][]Mp){
		
		MpTemp=AffectMp(Mp);
		Contrainte tempContrainte;
		ArrayList<Contrainte> Q=null;
		Q=RemplirFile(csp);
		
		while(!Q.isEmpty()) {
			tempContrainte=Q.get(0); 
	    	int [][] temp;
			Q.remove(0);
			int k=0;
			while(k<csp.X.length)  {		

				if( tempContrainte.variable1.nom!=tempContrainte.variable2.nom || tempContrainte.variable1.nom!=k || tempContrainte.variable2.nom!=k) {
					
				//calcule AR1 1 entre I et K/////////////////////
				temp=calculArc1(tempContrainte,k,Mp);
              
				if(!areSame(temp,Mp[tempContrainte.variable1.nom][k].matrice)) {
					Mp[tempContrainte.variable1.nom][k].matrice=AffectMat(temp);
					Mp[k][tempContrainte.variable1.nom].matrice=AffectMat(transposer(temp));
					Variable varK=new Variable(k,csp.X[k].domaine);
					if(tempContrainte.variable1.nom<=k ) {
						Contrainte contranteNew= new Contrainte(tempContrainte.variable1,varK);
						if( isNotIn(Q,contranteNew)) Q.add(contranteNew);
						
					  }
					else {
						Contrainte contranteNew= new Contrainte(varK,tempContrainte.variable1);
						if( isNotIn(Q,contranteNew)) Q.add(contranteNew);
						
						}
			         }
				
				//ARc 2 entre K et J///////////////////////
				
				temp=calculArc2(tempContrainte,k,Mp);

				if(!areSame(temp,Mp[k][tempContrainte.variable2.nom].matrice)) {
					Mp[k][tempContrainte.variable2.nom].matrice=AffectMat(temp);
					
					Mp[tempContrainte.variable2.nom][k].matrice=AffectMat(transposer(temp));
					Variable varK=new Variable(k,csp.X[k].domaine);

					if(k<=tempContrainte.variable2.nom) {
						Contrainte contranteNew= new Contrainte(varK,tempContrainte.variable2);
						if( isNotIn(Q,contranteNew)) Q.add(contranteNew);
				}
					else {
						Contrainte contranteNew= new Contrainte(tempContrainte.variable2,varK);
						if( isNotIn(Q,contranteNew)) Q.add(contranteNew);
						}
					}
							
				}
				k++;
			}
				
		}
	
		if(Inconsistant(Mp)) return true;
		else return false;
		
			}
	
	public static void AfficherDomaine(CSP csp) {
		for(int i=0;i<csp.X.length;i++) {
			System.out.println(csp.X[i].nom +" "+csp.X[i].domaine);
			
		}
	}
	
	public static void AfficherContrainte(CSP csp) {
		for(int i=0;i<csp.C.length;i++) {
			if(csp.C[i]!=null) {
			System.out.println(csp.C[i].variable1.nom +" "+csp.C[i].variable2.nom);
			}
			else  System.out.println("null");

		}
	}
	public static ArrayList<Integer> AfficherResultat(CSP csp) {
		ArrayList<Integer> Resultat=new ArrayList<Integer>();
		for(int i=0;i<csp.X.length;i++) {
			for(int j=0;j<csp.X[i].domaine.size();j++) {
				if(csp.X[i].domaine.get(j)!=null) Resultat.add(i, csp.X[i].domaine.get(j));
			}
		}
		return Resultat;
	}
	
	public static boolean Look_Ahead(ArrayList <Element> A,CSP csp,Matrice [][]Mp,int version) {
		
		Variable var=new Variable();
	 	if(PC2(csp,Mp)) {  Mp=AffectMp(MpTemp);  return false;}
    	MAJDomaine( csp, Mp);
		if(Instancié(csp)) {  return true;} 
		if(version==1) { var=InstancierVersion1(csp);}
		
		else {var=InstancierVersion2(csp);}
		ArrayList<Integer> Dprime=new ArrayList<Integer> ();
		for(int i=0;i<var.domaine.size();i++) {
			
			if(!Appartient(i,var,A) && var.domaine.get(i)!=null) {
				Dprime=RemplirDprim(i,var);
				csp.X[var.nom].setDomaine(Dprime);
				Element element=new Element(var,i); A.add(element);
				MiseAJMatrice(Mp,var,i);
				if(Look_Ahead(A,csp,Mp,version)) {  return true;}
			}
		}
						
	return false;	
	}
	
	
	static Matrice [][]AffectMp(Matrice [][] Mp)
	{
		Matrice [][] MpTemp=new Matrice [Mp.length][Mp[0].length] ;
		for(int i=0;i<Mp.length;i++) {
    		for(int j=0;j<Mp.length;j++) {
    			MpTemp[i][j]=Mp[i][j];
    			}
    		}
		return MpTemp;
	}
	static void MiseAJMatrice(Matrice [][] Mp,Variable var,int positionvaleur) {
		
		for(int j=0;j<Mp[var.nom][var.nom].matrice.length;j++) {
			for(int k=0;k<Mp[var.nom][var.nom].matrice.length;k++) {
					Mp[var.nom][var.nom].matrice[j][k]=0;
			}	
		}
		Mp[var.nom][var.nom].matrice[positionvaleur][positionvaleur]=1;
		
		
	}
	
	
	static boolean Appartient (int position ,Variable var ,ArrayList <Element> A) {
		for(int i=0;i<A.size();i++) {
			if(A.get(i).variable.nom==var.nom && A.get(i).valeurDomaine==position)  return true; 
			
		}	
		return false;
	}
	static Variable InstancierVersion1(CSP csp) {
		int conteur=0;
		ArrayList<Integer> cardinal= new ArrayList<Integer>();
    	//remplir cardinal
		for(int i=0;i<csp.X.length;i++) {
    		conteur=0;
    		for(int j=0;j<csp.X[i].domaine.size();j++) {
    			if(csp.X[i].domaine.get(j)!=null)  conteur++;
    			}
    	if(conteur>1) 	cardinal.add(csp.X[i].nom);	
    	
    	}
		
		int i =Rand(0,cardinal.size()-1);
		return csp.X[cardinal.get(i)];		
		
	}
	
	static Variable InstancierVersion2(CSP csp) {
		
		int conteur=0;
		int minValue=csp.X[0].domaine.size();
		int position=0;
    	//remplir cardinal
		for(int i=0;i<csp.X.length;i++) {
    		conteur=0;
    		for(int j=0;j<csp.X[i].domaine.size();j++) {
    			if(csp.X[i].domaine.get(j)!=null)  conteur++;
    			}
		if (conteur< minValue && conteur!=0 && conteur!=1)   { minValue =conteur; position=i;  }
	    	}
	
		return csp.X[position];
	}
	

	
	static ArrayList<Integer>  RemplirDprim(int j,Variable var) {
		ArrayList<Integer> Res=new ArrayList<Integer>() ;
		for(int i=0;i<var.domaine.size();i++) {
			if(i==j)  {Res.add(j,var.domaine.get(j));
			}
			
			else Res.add(i, null);
		}
		return Res;
	}
	
    public static Matrice [][] InitialiserMp(CSP csp){
    	Matrice [][] Mp=new Matrice [csp.X.length][csp.X.length];
    	
    	for(int i=0;i<csp.X.length;i++) {
    		for(int j=0;j<csp.X.length;j++) {
    			Mp[i][j]=new Matrice();
    		   if(i==j) Mp[i][j].matrice=MatriceVariable(csp.X[i].domaine); 
    		
    		   else {
    			   Mp[i][j].matrice=ExistContrainte(i,j,csp.C);
    			   if(Mp[i][j].matrice==null)  Mp[i][j].matrice=MatUniver(csp.X[i].domaine.size(),csp.X[j].domaine.size());

    			}
    		}
    }
    	//DisplayMp(Mp);
    return Mp;
    }
    
   static int [][] ExistContrainte(int nom1,int nom2,Contrainte[] C) {
    	for(int i=0;i<C.length;i++) {
    		if(nom1== C[i].variable1.nom && nom2== C[i].variable2.nom) return C[i].matrice;
    		if(nom2== C[i].variable1.nom && nom1== C[i].variable2.nom) return transposer(C[i].matrice);
    	}
    	return null;
    }
    
   static boolean ExistContrainte2(int nom1,int nom2,Contrainte[] C) {
   	for(int i=0;i<C.length;i++) {
   		if(C[i]!=null) {
   		if(nom1== C[i].variable1.nom && nom2== C[i].variable2.nom) return true;
   		if(nom2== C[i].variable1.nom && nom1== C[i].variable2.nom) return true;
   		}
   	}
   	return false;
   }
   

    static int[][] MatriceVariable(ArrayList<Integer> domaine){
    	int [][] mat=new int [domaine.size()][domaine.size()] ;
    	for(int i=0;i<domaine.size();i++) {
    		for(int j=0;j<domaine.size();j++) {
    			if(i==j && domaine.get(i)!=null)  mat[i][j]=1;     	

    			else  mat[i][j]=0; 
    		}
    		
    	}
        
    	return mat;    	 	
    }
    
   static int[][] MatUniver(int a, int b){
		   int [][] mat =new int[a][b];
	    	for(int i=0;i<a;i++ )	{
				for(int j=0;j<b;j++ ) {
					mat[i][j]=1;
				}
			}
	    	return mat;
		}
   
   static int[][] MatZero(int a, int b){
	   int [][] mat =new int[a][b];
    	for(int i=0;i<a;i++ )	{
			for(int j=0;j<b;j++ ) {
				mat[i][j]=0;
			}
		}
    	return mat;
	}
   public static ArrayList<Integer>  convertArray(String[] domaineString){
	   ArrayList<Integer> domaine=new ArrayList<Integer>();
	   for(int i=0;i<domaineString.length;i++) {

		  domaine.add(Integer.valueOf(domaineString[i]));
	   }
	   return domaine;
   }
   
	static Variable VarDisjonctive(CSP csp) {
		for(int i=0;i<csp.X.length;i++) {
			
			if(PasTousNull(csp.X[i].domaine)) return (csp.X[i]);
		}
		return null;
	}
	
	static boolean PasTousNull(ArrayList <Integer> domaine) {
		int conteur=0;
		for(int i=0;i<domaine.size();i++) {
			if(domaine.get(i)!=null) conteur++;
			if(conteur>=2) return true;
			
		}
		
		return false;
	}
	
	static boolean Instancié(CSP csp) {
		int conteur=0;
		for(int i=0;i<csp.X.length;i++) {
			conteur=0;
			for(int j=0;j<csp.X[i].domaine.size();j++) {
			if(csp.X[i].domaine.get(j)!=null) conteur ++;
			if(conteur>=2) return false;
			}
		}

	return true;	
	}
	
	
	
	public static void MAJDomaine(CSP csp,Matrice Mp[][]) {
		
		for(int i=0; i<Mp.length;i++) {
			for(int j=0; j<csp.X[i].domaine.size();j++) {
								
				if(Mp[i][i].matrice[j][j]==0) {csp.X[i].domaine.set(j, null); }
				if(Mp[i][i].matrice[j][j]==1) csp.X[i].domaine.set(j, csp.X[i].domaine.get(j));
				
				}	

			}	
		
		}
	
	
	static boolean Inconsistant (Matrice [][] Mp) {
		
		for(int i=0; i<Mp.length;i++) {
			
			if(MatriceVide(Mp[i][i].matrice)) return true;
			}
		return false; 
		}

	
	static boolean MatriceVide(int[][]mat) {
		int conteur=0;
		for(int i=0; i<mat.length;i++) {
			for(int j=0; j<mat[0].length;j++) {
				
				if(mat[i][j]==0)  conteur++;				
			}
		}
		if(conteur==mat.length*mat[0].length) return true;
		return false;
	}
	
	static ArrayList<Contrainte> RemplirFile(CSP csp)
	{
		ArrayList<Contrainte> Q = new ArrayList <Contrainte>();
		for(int i=0;i<csp.C.length;i++ ) {
			if(csp.C[i].variable1.nom<=csp.C[i].variable2.nom) Q.add(csp.C[i]);
			else {
				int[][] matrice= transposer(csp.C[i].matrice);
				Contrainte	tempContrainte=new Contrainte(csp.C[i].variable2,csp.C[i].variable1,matrice);
				Q.add(tempContrainte); 
			 }
			}
		return Q;
	}
	
	static int [][] calculArc1(Contrainte tempContrainte,int k,Matrice [][]Mp){

		int [][] temp ;
		temp=produit(Mp[tempContrainte.variable1.nom][tempContrainte.variable2.nom].matrice,Mp[tempContrainte.variable2.nom][tempContrainte.variable2.nom].matrice,Mp[tempContrainte.variable2.nom][k].matrice);
		temp=jointure(temp,Mp[tempContrainte.variable1.nom][k].matrice);
		return temp;
		
	}
	
	static int [][] calculArc2(Contrainte tempContrainte,int k,Matrice [][]Mp){

		int [][] temp; 
		temp=produit(Mp[k][tempContrainte.variable1.nom].matrice,Mp[tempContrainte.variable1.nom][tempContrainte.variable1.nom].matrice,Mp[tempContrainte.variable1.nom][tempContrainte.variable2.nom].matrice);
	
		temp=jointure(temp,Mp[k][tempContrainte.variable2.nom].matrice);
		return temp;
		
	}
	
    static boolean isNotIn (ArrayList<Contrainte> Q, Contrainte c) {
    	
    	for (int i=0;i<Q.size();i++) {
    		if(Q.get(i).equals(c)) return false;
    		
    	}
    	return (true);
    }
    
    
	static int[][] transposer(int mat[][]){
	int[][]temp=new int[ mat[0].length][mat.length];	
	for (int i=0; i< mat[0].length; i++) {
		for (int j=0; j< mat.length; j++) {
			temp[i][j] = mat[j][i];	
		}
	
	}
	return temp;	
	}
	
	
	static boolean areSame(int A[][], int B[][]) 
	{ 
	    int i, j; 
	    for (i = 0; i <A.length ; i++) 
	        for (j = 0; j <A[0].length ; j++) 
	            if (A[i][j] != B[i][j]) 
	                return false; 
	    return true; 
	} 
	

	static int [][] jointure(int [][] mat1,int [][]  mat2) {
		 
		int [][]  matRes=new int[mat1.length][mat2[0].length];
		for(int i=0;i<mat1.length;i++ )	{
			for(int j=0;j<mat2[0].length;j++ ) {
				if(mat1[i][j]==1 && mat2[i][j]==1 ) matRes[i][j]=1;
				if(mat1[i][j]==0 && mat2[i][j]==0 ) {matRes[i][j]=0;}
				if(mat1[i][j]==0 && mat2[i][j]==1 || mat1[i][j]==1 && mat2[i][j]==0 ) {matRes[i][j]=0;}
				
			}
		}
		
		return matRes;
	}
	
	
	static int [][] produit(int [][] mat1,int [][]  mat2, int[][] mat3) {
		int [][]  temp=new int[mat2.length][mat3[0].length];
		int var;
		for(int i=0;i<mat2.length;i++ )	{
			for(int j=0;j<mat3[0].length;j++ ) {
				 for (int k=0; k<mat2[0].length; ++k) {
					 var=mat2[i][k] * mat3[k][j];
					 if(var==1 && temp[i][j]==1 ) temp[i][j]=1;
					 if(var==0 && temp[i][j]==0 ) temp[i][j]=0;
					 else temp[i][j]=1;
				 }
			}
		} 
		int [][]  matRes=new int[mat1.length][temp[0].length];
		
		 
		 
		for(int i=0;i<mat1.length;i++ )	{
			for(int j=0;j<temp[0].length;j++ ) {
				 for (int k=0; k<mat1[0].length; k++) {
					
					 var=mat1[i][k] * temp[k][j];
					 if(var==1 && matRes[i][j]==1 ) matRes[i][j]=1;
					 if(var==0 && matRes[i][j]==0 ) matRes[i][j]=0;
					 else matRes[i][j]=1;
				 }
			}
		}
		return matRes;
	}
	
	static void Display(int [][]mat) {
		for (int i = 0; i < mat.length; i++) {
		    for (int j = 0; j < mat[0].length; j++) {
		        System.out.print((mat[i][j])+ " "); 
		    }
		 System.out.println();
		    
		}
		 System.out.println();
	}
	
	static int[][] AffectMat(int[][] temp){
		int[][] mat=new int[temp.length][temp[0].length];
		for (int i = 0; i < temp.length; i++) {
		    for (int j = 0; j < temp[0].length; j++) {
		    	mat[i][j]=temp[i][j];
				//Display(mat);					

		    	}
		    }
		return mat;
	}
	
	static ArrayList<Integer> AffectArray(ArrayList<Integer> temp){
		ArrayList<Integer> list=new ArrayList<Integer>();
		for (int i = 0; i < temp.size(); i++) {
		   
		    	list.add(temp.get(i));
		    	
		    }
		return list;
	}
	
	public static void DisplayMp(Matrice [][]Mp) {
		
		for (int i = 0; i < Mp.length; i++) {
		    for (int j = 0; j < Mp.length; j++) {
		    	    System.out.println("i="+i+" "+"j="+j);
		    	   	Display(Mp[i][j].matrice); 
			   		    }
			 System.out.println();
		}

	}
	
	public static int [][] Remplir (){
		
		int[][] matrix = {
				  { 0, 1, 1,1,1 },
				  { 0, 0, 1,1,1 },
				  { 0, 0, 0,1,1 },
				  { 0, 0, 0,0,1 },
				  { 0, 0, 0,0,0 }
				};
		return matrix;
	}
	
	static public int Rand(int min ,int max) {
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min;
		
	}
	
	static public CSP CSPAlea(int nbContrainte,int nbVariable,int tailleDomaine,ArrayList<Integer> domaine) {
		 
	  	  Variable [] X= new Variable[nbVariable];
		  Contrainte[] C = new Contrainte[nbContrainte];
		  
		  for(int i=0;i<nbVariable;i++) {  X[i]=new Variable(i,AffectArray(domaine)); }
		  
	
	  int i=0; 	 
	  //les contraintes	
	  while(i<nbContrainte) {
		boolean bool=false;	
		while(bool==false) {
	    int var1=Rand(0,nbVariable-1);
	    int var2=Rand(0,nbVariable-1);
	    
        if(var1<=var2 && ExistContrainte2(var1,var2,C)==false) {
    		Variable variable1=new Variable(var1,X[var1].domaine); Variable variable2=new Variable(var2,X[var2].domaine);
       		 C[i]=new Contrainte(variable1,variable2);bool=true;
    		
    		} 
                
        if(var2<var1 && ExistContrainte2(var2,var1,C)==false) {
    		Variable variable1=new Variable(var1,X[var1].domaine); Variable variable2=new Variable(var2,X[var2].domaine);
    		C[i]=new Contrainte(variable2,variable1);bool=true;
    		}
	    }
		
		i++;
	  }
	       
	  	  CSP csp=new CSP(X,C);
	  	  return csp;	
	}
	
	static public  ArrayList<Integer> RemplirDomaine(int tailleDomaine){
		ArrayList<Integer> domaine=new ArrayList<Integer>();
		for(int i=0;i<tailleDomaine;i++) {
			domaine.add(Rand(1,100));
		}
		return domaine;
	}
	
	public static Matrice [][] InitMpAlea(Matrice [][] Mp,CSP csp){
		for(int i=0;i<csp.X.length;i++) {
    		for(int j=0;j<csp.X.length;j++) {
    			if(i==j) Mp[i][j].matrice=MatDiagAlea(Mp[i][j].matrice);
    			if(i<j && ExistContrainte2(i,j,csp.C)) { Mp[i][j].matrice=MatriceAlea(csp.X[i].domaine.size(),csp.X[j].domaine.size()); 
    			 Mp[j][i].matrice=transposer(Mp[i][j].matrice);
    			}
    		}
		}
		return Mp;
	}
	
	public static Matrice [][] MpUniver(CSP csp){
	    Matrice [][] Mpuniver=new Matrice [csp.X.length][csp.X.length] ;
		for(int i=0;i<csp.X.length;i++) {
    		for(int j=0;j<csp.X.length;j++) {
     			Mpuniver[i][j]=new Matrice();
    			Mpuniver[i][j].matrice=MatUniver(csp.X[i].domaine.size(),csp.X[j].domaine.size());
    			}
    		}
		return Mpuniver;		
	}
	public static int [][] MatDiagAlea(int[][] mat) {
		for(int i=0;i<mat.length;i++) {
    		for(int j=0;j<mat.length;j++) {
    			if(i==j) mat[i][i]=(int) Math.round( Math.random()) ;
    			else mat[i][j]=0;
    			}
    		}
		return mat;
	}
	
	public static int[][] MatriceAlea(int a, int b){
	    int [][] mat =new int[a][b];
    	for(int i=0;i<a;i++ )	{
    		for(int j=0;j<b;j++ ) {
    			int val =(int) Math.round( Math.random()) ;
    			mat[i][j]=val;
			}
		}
    	return mat;
	}
	
	public static CSP  CspChoisi(String pathFile,String contString,TextArea AfficheCont) throws FileNotFoundException, IOException, ScriptException {
		Variable[] X=null;
		ArrayList <Variable> Xarray=new ArrayList<Variable>();
	    ArrayList <Contrainte >C=new ArrayList <Contrainte>(); 
	    ArrayList <Contrainte >CUnaire=new ArrayList <Contrainte>(); 

	    
	    FileReader fileStream = new FileReader( pathFile ); 
	    BufferedReader bufferedReader = new BufferedReader( fileStream ) ;
	    String line; String[] domaineString; 
	    try {  
	    	    ///////////////////////////read file
		     int nomVar=0;
		     line=bufferedReader.readLine();
		     while( !line.contains("**")) { 
		    	if(line!=null )
		    	{	domaineString=line.split(" ");
		    	    ArrayList<Integer> domaine=Fonctions.convertArray(domaineString);
 		    	    Variable var=new Variable(nomVar,domaine);
 		    	    Xarray.add(var);// toute les variable
		    	    nomVar++;
		    	 }
		    	line=bufferedReader.readLine();
		     }
		     //////Construire les contraintes
		     X=ConvertArraylistOfVra(Xarray);
		     line=bufferedReader.readLine();
		     contString="";
		     while(!line.contains("**")) {
	    		    if(line!=null){	
	    		    	contString=contString+line+"\n";
	    		    	Contrainte cont= ConstruireContrainte(line,X);
	    		    	if(cont.variable2!=null) {
	    		    	C.add(cont);}
	    		           
	    		    	else {
	    		    		CUnaire.add(cont);
	    		    	}
	    		    }
	   		     line=bufferedReader.readLine();

	    		 }
		     System.out.println(contString);
		     AfficheCont.setText(contString);

		}
	    catch(IOException ex){ ex.printStackTrace();  } 
		 
		 finally {
			   bufferedReader.close();
			   
		           }
		Contrainte []Cont =ArraytoMat(C);
		CSP csp=new CSP(X,Cont);
		csp.ContrainteUnaire=CUnaire;
		
		return csp; 
}
	
	  public static Variable[]  ConvertArraylistOfVra(ArrayList<Variable> Xarray){
		  Variable[] X=new Variable[Xarray.size()];
		   for(int i=0;i<Xarray.size();i++) {
			   X[i]=Xarray.get(i);
		   }
		   return X;
	   }
	
	
	
	public static Matrice [][] InitMpChoisi(Matrice [][] Mp,CSP csp){
		for(int i=0;i<csp.X.length;i++) {
    		for(int j=0;j<csp.X.length;j++) {
    			if(i==j) Mp[i][j].matrice=MatDiag(csp.X[i],csp);
    			int [][]mat=new int[csp.X[i].domaine.size()][csp.X[j].domaine.size()];
    			mat=ExistContrainte(i,j,csp.C);
    			if(mat!=null)  {Mp[i][j].matrice=mat;
    					}
    		}   
		}
		
		return Mp;
	}
	
	
	public static int [][]  MatDiag(Variable var1,CSP csp) {
		int [][] mat=new int[var1.domaine.size()][var1.domaine.size()];
		
		if(ExistVarUnaire(var1,csp.ContrainteUnaire)==null) {
		for(int i=0;i<var1.domaine.size();i++) {
    		for(int j=0;j<var1.domaine.size();j++) {
    			if(i==j) mat[i][j]=1;
    			else mat[i][j]=0;
    		
    			}
    		}
		}
		else {mat=ExistVarUnaire(var1,csp.ContrainteUnaire); }
		
		return mat;
	}
	
	public static int[][] ExistVarUnaire(Variable var1,ArrayList<Contrainte> contrainteUnaire) {
		if(contrainteUnaire!=null) {
		for(int i=0;i<contrainteUnaire.size();i++) {
			if(var1.nom==contrainteUnaire.get(i).variable1.nom) return  contrainteUnaire.get(i).matrice;
			}
		}
        return null;
		
	}
	
	public static Contrainte [] ArraytoMat(ArrayList<Contrainte> Cont) {
		
		Contrainte []C=new Contrainte[Cont.size()];
		for(int i=0;i<Cont.size();i++) {
			C[i]=Cont.get(i);
		}
		
		return C;
	}
	
	public static Contrainte ConstruireContrainte(String cont, Variable [] X) throws ScriptException {
		String[] mes2Variables=new String[2];
		Pattern pattern = Pattern.compile("X\\d+");
		Matcher matcher = pattern.matcher(cont);
		int i=0;
		while (matcher.find())
		{   mes2Variables[i]=matcher.group();i++;
		}
		
		int nomvar1=Integer.valueOf(mes2Variables[0].substring(1));
		if(mes2Variables[1]!=null) {
		int nomvar2=Integer.valueOf(mes2Variables[1].substring(1));

        int [][] matcont =MatriceContrainte(X[nomvar1],X[nomvar2],cont);
	    Contrainte contrainte =new Contrainte(X[nomvar1],X[nomvar2],matcont);
	    return contrainte;

		}
		else {
			 int [][] matcont =MatriceContrainteDiag(X[nomvar1],cont);
			 Contrainte contrainte =new Contrainte(X[nomvar1],matcont);
             return contrainte;
		}
		
		}
    public static int [][] MatriceContrainteDiag(Variable variable1, String cont) throws ScriptException{
    	int [][] matcont= new int [variable1.domaine.size()][variable1.domaine.size()];

    	for(int i=0;i<variable1.domaine.size();i++) {
			String contString=cont;
			contString=contString.replaceAll("X"+String.valueOf(variable1.nom), String.valueOf(variable1.domaine.get(i)));

			matcont[i][i]=eval(contString);
		}
    
    	return matcont;
    }


    public static int [][] MatriceContrainte(Variable variable1,Variable variable2, String cont) throws ScriptException{
    	int [][] matcont= new int [variable1.domaine.size()][variable2.domaine.size()];
    	for(int i=0;i<variable1.domaine.size();i++) {
    		for(int j=0;j<variable1.domaine.size();j++) {
    			String contString=cont;
    			contString=contString.replaceAll("X"+String.valueOf(variable1.nom), String.valueOf(variable1.domaine.get(i)));
    			contString=contString.replaceAll("X"+String.valueOf(variable2.nom), String.valueOf(variable2.domaine.get(j)));

    			matcont[i][j]=eval(contString);
    		}
    		
    	}
    return matcont;
    }
    public static int eval(String contString) throws ScriptException {
    	int val;
    	ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine se = sem.getEngineByName("JavaScript");
        boolean bool= (boolean) se.eval(contString);

        if(bool==true)  val =1; 
        else  val=0;
    	return val;
    }
    		
    public static  String PartieString(String s) {
    	String partie=null;
    	for(int i=0;i<s.length();i++) {
    		if(!(String.valueOf(s.charAt(i))).equals("") && !(String.valueOf(s.charAt(i))).equals(" ")){ 
    			if(partie!=null) partie=partie+String.valueOf(s.charAt(i));
    			else partie=String.valueOf(s.charAt(i));
    			         }
    	}
    	return partie;
    } 
}
	/*	public static int [][] Remplir2 (){
		public static CSP  CspChoisi(String pathFile,int nbVariable) {
		
			Variable[] X=new Variable[Integer.valueOf(nbVariable)];
    	    ArrayList <Contrainte >C=new ArrayList <Contrainte>(); 
    	    
			try { FileReader fileStream = new FileReader( pathFile ); 
    		    BufferedReader bufferedReader = new BufferedReader( fileStream );
    		    String line = null; String[] domaineString; 
///////////////////////////read file
    		     int nomVar=0;
    		     while( (line = bufferedReader.readLine()) != "*******") {
    		    	if(line!=null)
    		    	{	domaineString=line.split(" ");
    		    	    ArrayList<Integer> domaine=Fonctions.convertArray(domaineString);
     		    	    Variable var=new Variable(nomVar,domaine);
     		    	    X[nomVar]=var;// toute les variable
    		    	    nomVar++;
    		    	 }
    		     }
    		     while( (line = bufferedReader.readLine()) != "*******") {
    	    		    if(line!=null){	
    	    		    	Contrainte cont= ConstruireContrainte(line,X);
    	    		    	C.add(cont);
    	    		           }
    	    		 }
    		     
			} finally {} 		
	}
		int[][] matrix = {
                     4 12  7 11	 9	
			x1    1{ 0, 0, 0, 0, 0 },
				  2{ 0, 0, 0, 0, 0 },
				  3{ 0, 0, 0, 0, 0 },
				  4{ 0, 0, 0, 0, 0},
				  5{ 1, 0, 0, 0, 0 }
				};
		return matrix;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
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
       

		int [][] mat=Remplir();
		int [][] mat2=Remplir2();
        Variable [] X={variable1,variable2,variable3};
        
        //Contrainte con1=new Contrainte(variable1,variable2,mat);
        //Contrainte con2=new Contrainte(variable1,variable3,mat2);
        //Contrainte con3=new Contrainte(variable2,variable3,mat2);

       Contrainte con1=new Contrainte(variable1,variable2);
       Contrainte con2=new Contrainte(variable1,variable3);
       Contrainte con3=new Contrainte(variable2,variable3);
        
        Contrainte C[]= {con1,con2,con3};

		CSP csp=new CSP(X,C);
	 	Matrice [][] Mp;
	    Mp= InitialiserMp(csp);
	    DisplayMp(Mp);
	    
	    
	    
	    /*
	    int[][] matrix3= {
	    		  { 0, 1, 1,1,1 },
				  { 0, 0, 1,1,1 },
				  { 0, 0, 0,1,1 },
				  { 0, 0, 0,0,1 },
				  { 0, 0, 0,0,0 }
				  };

	    int[][] matrix2 = {
	    		 { 1, 0, 0,0,0 },
				  { 0, 1, 0,0,0 },
				  { 0, 0, 1,0,0 },
				  { 0, 0, 0,1,0},
				  { 0, 0, 0,0,0 }};

	    	    int[][] matrix1 = {
	    	    	  { 0, 0, 0,0,0 },
	   				  { 0, 0, 0,0,0 },
	   				  { 1, 0, 0,0,0 },
	   				  { 1, 1, 0,0,0 },
	   				  { 1, 1, 1,0,0 }};

	    	    int[][] matrix6 = {
	    	    		  { 0, 0, 0,0,0 },
		   				  { 1, 0, 0,0,0 },
		   				  { 1, 1, 0,0,0 },
		   				  { 1, 1, 1,0,0 },
		   				  { 1, 1, 1,1,0 }};
	        int[][] matrix4=produit(matrix1,matrix2,matrix3);

	    	   	    int[][] matrix5=jointure(matrix6,matrix4);
	    
	    PC2(csp,Mp);
	    
	    ArrayList <Element> A=new ArrayList<Element>();  	
   
	    boolean bool=  Look_Ahead(A,csp,Mp);
	 
	    DisplayMp(Mp);
	    System.out.println(bool);
	    //MAJDomaine( csp, Mp);
	    AfficherDomaine(csp);
     
	}
*/


