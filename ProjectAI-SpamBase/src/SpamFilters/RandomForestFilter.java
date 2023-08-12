package SpamFilters;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import PretreatmentData.DataProccessing;
import RecomingMailLibrary.MailLibrary;

public class RandomForestFilter implements SpamFilterInterface {
	
	public RandomForestFilter() {}
	public boolean getVoting(String word,List<Map<String, Integer>> spamWords,List<Map<String, Integer>> nonWords) {
		boolean result = false;
		int countSpam = 0;
		int countNonSpam = 0;
		for (Map<String, Integer> map : spamWords) {
			boolean b = getDecisionTree(word, map, true);
			if (b==true) {
				countSpam ++;
			}else {
				countNonSpam++;
			}
		} 
		
		for (Map<String, Integer> map : nonWords) {
			boolean b = getDecisionTree(word, map, false);
			if (b==true) {
				countSpam ++;
			}else {
				countNonSpam++;
			}
		} 
			
		if (countSpam>= countNonSpam) {
			result = true;
		}else {
			result =false;
		}
		return result;
		
	}
	public boolean getDecisionTree(String word,Map<String, Integer> map,boolean check) {
		boolean result = check;
		Set<String> set = map.keySet();
		Object[] setArr = set.toArray();
		if (!set.contains(word)) {
			return !result;
		}else {
			int sum = 0;
			for (String string : set) {
				sum+= map.get(string);
			}
			int center = map.size()/2;
			if (map.get(word) < map.get(setArr[center])) {
				return !result;
			}else {
				int count = 0;
				double accWord = (map.get(word)/sum)*100;
				for (String string : set) {
					double accString= (map.get(string)/sum)*100;
					if (accWord>=accString) {
						count++;
					}
				}
				if (count>= center) {
					return result;
				}else {
					return !result;
				}
			}
		}

	}
	@Override
	public String getAccuracy() {
		// TODO Auto-generated method stub
		 File currfile= new File("");
			String files = currfile.getAbsolutePath()+"\\src\\IncomingMailServer\\test";
			final File testFolder = new File(files);
			List<Map<String, Integer>> nonSpamWords = MailLibrary.getMailLibrary().getListNonSpamFileDP();
			List<Map<String, Integer>> spamWords = MailLibrary.getMailLibrary().getListSpamFileDP();
		    for (final File testFile : testFolder.listFiles()) {	
		    	int C_NBSpam =0;
			    int C_NBNonSpam = 0;
		      Map<String, Integer> testWords = new DataProccessing().wordDF(testFile.getAbsolutePath());
		      Set<String> listWordTest = testWords.keySet();
		      for (String strTest : listWordTest) {
		    	  if (getVoting(strTest, spamWords, nonSpamWords)==true) {
		    		  C_NBSpam++;
				}else {
					C_NBNonSpam++;
				}
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
		    }
		return null;
	}

}
