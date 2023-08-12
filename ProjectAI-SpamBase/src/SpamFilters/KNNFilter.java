package SpamFilters;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import PretreatmentData.DataProccessing;
import RecomingMailLibrary.MailLibrary;

public class KNNFilter implements SpamFilterInterface{
	public KNNFilter() {
		
	}
	public static boolean ValueNNSpam(String x ,int xValue) {

		int non = 0;
		int sp = 0;
		List<Map<String, Integer>> nonSpamWords = MailLibrary.getMailLibrary().getListNonSpamFileDP();
		List<Map<String, Integer>> spamWords = MailLibrary.getMailLibrary().getListSpamFileDP();
		
	      
		for (Map<String, Integer> map : nonSpamWords) {
			if (map.containsKey(x)) {
				if (map.get(x)>= xValue) {
					non ++;
					
				}
			}
		}
	    
		for (Map<String, Integer> map : spamWords) {
			if (map.containsKey(x)) {
				if (xValue>= map.get(x)) {
					sp ++;
					
				}
			}
		}
		
//	   int nSpam = sp - xValue;
//	   int nNon = non - xValue;
	   if (sp > non) {
		   
		   return true;
	   }else {
		   return  false;
		   
	   }
	   
	}
	@Override
	public String getAccuracy() {
		// TODO Auto-generated method stub

		
		 File currfile= new File("");
			String files = currfile.getAbsolutePath()+"\\src\\IncomingMailServer\\test";
			final File testFolder = new File(files);
		    for (final File testFile : testFolder.listFiles()) {	
		    	int C_NBSpam =0;
			    int C_NBNonSpam = 0;
		      Map<String, Integer> testWords = new DataProccessing().wordDF(testFile.getAbsolutePath());
		      Set<String> listWordTest = testWords.keySet();
		     for (String strTest : listWordTest) {
		    	 if (ValueNNSpam(strTest, testWords.get(strTest)) == true) {
					C_NBSpam++;
					
				}else {
					C_NBNonSpam++;
				}
//		    	 System.out.println("P(x_i=" + strTest + "|nonspam)=  " + !ValueNNSpam(strTest, testWords.get(strTest)) + "        " + "P(x_i="
//		    	            + strTest + "|spam)=  " + ValueNNSpam(strTest, testWords.get(strTest)));
		     }
		     if ( C_NBSpam > C_NBNonSpam) {
		         // Bổ sung file vừa kiểm tra vào folder spam.
		    	 new MoveMail().moveMail(testFile.getAbsolutePath(), currfile.getAbsolutePath()+"\\src\\IncomingMailServer\\spam");
		    	 return testFile.getName()+ ": Là thư rác";
		       } else {
		    	// Bổ sung file vừa kiểm tra vào folder nonspam.  
		    	   new MoveMail().moveMail(testFile.getAbsolutePath(), currfile.getAbsolutePath()+"\\src\\IncomingMailServer\\nonspam");
		         return testFile.getName()+ ": Là thư thường";
		       }
		    }// end for
		return null;
	}
	
	

}
