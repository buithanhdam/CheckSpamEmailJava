package RecomingMailLibrary;



import java.util.List;
import java.util.Map;
import java.util.Set;

import PretreatmentData.DataProccessing;

public class MailLibrary {
	private static MailLibrary ml;

	private MailLibrary() {

	}
	public static MailLibrary getMailLibrary() {
		
		if (ml != null) {
			ml = null;
		}
		ml = new MailLibrary();
		return ml;
	}
	
	public Map<String, Integer> getSpamLibrary(){
		Map<String, Integer> spamLibrary = DataProccessing.proccessWordFromMailServer("spam");
//		DataProccessing.proccessSpamWordFilter(spamLibrary, DataProccessing.proccessWordFromMailServer("nonspam"));
		return spamLibrary;
	}
	public Map<String, Integer> getNonSpamLibrary(){
		Map<String, Integer> nonSpamLibrary = DataProccessing.proccessWordFromMailServer("nonspam");
		return nonSpamLibrary;
	}
	public int getCountSpamFile() {
		int result = DataProccessing.countFileChild("spam");
		return result;
	}
	public int getCountNonSpamFile() {
		int result = DataProccessing.countFileChild("nonspam");
		return result;
	}
	public List<Map<String, Integer>> getListSpamFileDP(){
		return DataProccessing.listFile("spam");
	}
	public List<Map<String, Integer>> getListNonSpamFileDP(){
		return DataProccessing.listFile("nonspam");
	}
	public int getTotalSpamWord() {
		Map<String, Integer> spamLibrary = getSpamLibrary();
		int result = 0;
		Set<String >set = spamLibrary.keySet();
		for (String string : set) {
			result += spamLibrary.get(string);
		}
		return result;
	}
	public int getTotalNonSpamWord() {
		Map<String, Integer> nonSpamLibrary = getNonSpamLibrary();
		int result = 0;
		Set<String >set = nonSpamLibrary.keySet();
		for (String string : set) {
			result += nonSpamLibrary.get(string);
		}
		return result;
	}


}
