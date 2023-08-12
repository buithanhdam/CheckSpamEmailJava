package SpamFilters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import PretreatmentData.DataProccessing;
import RecomingMailLibrary.MailLibrary;

public class NaiveBayesFilter implements SpamFilterInterface{
	 public NaiveBayesFilter() {
		// TODO Auto-generated constructor stub
	}

	// tinh xac xuat P(xi=x|nhan= nonspam)
	  public static double pNonSpam(String x) { 
	    double k = 0;
	    List<Map<String, Integer>> list = MailLibrary.getMailLibrary().getListNonSpamFileDP();
	      // moi lan x xuat hien trong 1 thu thuong thi k++
	    for (int i = 0; i < list.size(); i++) {
			
	    	if (list.get(i).containsKey(x)) {
	    		k ++;
	    	}
		}
	    return (k + 1) / (list.size() + 1);
	    // P(xi|nhan= nonspam)= (k+1)/(sothuthuong+1);
	    // trong do: k la so cac mail nonspam xuat hien xi
	    // sothuthuong la so mail nonspam
	  }
	  // tinh xac xuat P(xi=x|nhan= spam)
	  public static double pSpam(String x) {
		  List<Map<String, Integer>> list = MailLibrary.getMailLibrary().getListSpamFileDP();
	    double k = 0;
	    for (int i = 0; i < list.size(); i++) {
	      if (list.get(i).containsKey(x))
	        // moi lan x xuat hien trong 1 thu rac thi k++
	        k++;
	    }
	    return (k + 1) / (list.size() + 1);
	    // P(xi|nhan= spam)= (k+1)/(sothurac+1);
	    // trong do: k la so cac mail spam xuat hien xi
	    // sothurac la so mail spam
	  }
	@Override
	public String getAccuracy() {

	    
	 // getting test files
	    File currfile= new File("");
		String files = currfile.getAbsolutePath()+"\\src\\IncomingMailServer\\test";
		 File testFolder = new File(files);
	    for ( File testFile : testFolder.listFiles()) {	 
	    	List<Map<String, Integer>> nonSpamWords = MailLibrary.getMailLibrary().getListNonSpamFileDP();
			
			List<Map<String, Integer>> spamWords = MailLibrary.getMailLibrary().getListSpamFileDP();
		 
		 // xác xuất là thư thường. P(xi|non-spam)
		    double C_NB1 = nonSpamWords.size() / ((double) nonSpamWords.size() + spamWords.size());
		    // xác xuất là thư rác. P(xi|spam)
		    double C_NB2 = spamWords.size() / ((double) nonSpamWords.size() + spamWords.size());
		    
	      Map<String, Integer> testWords = new DataProccessing().wordDF(testFile.getAbsolutePath());
	      Set<String> listWordTest = testWords.keySet();
	     for (String strTest : listWordTest) {
	    	 if (pNonSpam(strTest) != ((double) 1 / (nonSpamWords.size() + 1))
	    	          || pSpam(strTest) != ((double) 1 / (spamWords.size() + 1))) {
//	    	        System.out.println("P(x_i=" + strTest + "|nonspam)=  " + pNonSpam(strTest) + "        " + "P(x_i="
//	    	            + strTest + "|spam)=  " + pSpam(strTest));
	    	        C_NB1 *= pNonSpam(strTest);
	    	        C_NB2 *= pSpam(strTest);
	    	      }
		}
	     if (C_NB2 > C_NB1) {
	         // Bổ sung file vừa kiểm tra vào folder spam.
	    	 new MoveMail().moveMail(testFile.getAbsolutePath(), currfile.getAbsolutePath()+"\\src\\IncomingMailServer\\spam");
	    	 return testFile.getName()+ ": Là thư rác";

	       } else{
	    	// Bổ sung file vừa kiểm tra vào folder nonspam.  
	    	   new MoveMail().moveMail(testFile.getAbsolutePath(), currfile.getAbsolutePath()+"\\src\\IncomingMailServer\\nonspam");
		         return testFile.getName()+ ": Là thư thường";
	         
	       }
	    }// end for
	   
	    return null;
	    
	}
	public static void main(String[] args) {
		NaiveBayesFilter b = new NaiveBayesFilter();
		b.getAccuracy();
	}


}
