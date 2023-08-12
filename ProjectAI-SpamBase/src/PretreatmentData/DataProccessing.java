package PretreatmentData;


import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;

import java.io.IOException;
import java.io.InputStreamReader;

import java.nio.charset.StandardCharsets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import RecomingMailLibrary.MailLibrary;



public class DataProccessing {


	public DataProccessing() {

	}

	public String[] deleteSymbol(String row) {
		String[] arrayWord;
		String result = row.replaceAll("[\\-\\+\\.\\^\\:\\,\\*\\(\\)\\{\\}\\[\\]\\?\\/\\>\\<\\;\\:\\~\\`\\!\\'\\@\\#\\$\\&\\_\\+\\=\\|\\|]"," ");
		
		row = result;
		arrayWord = row.split(" ");
		return arrayWord;
	}
//	public void deleteSymbolNGram() {
//
//		arrayWord = row.split(", ");
//		totalWord += arrayWord.length;
//	}
	public Map<String, Integer> wordDF(String filepath){ 
		 String VIETNAMESE_DIACRITIC_CHARACTERS 
	    = "ẮẰẲẴẶĂẤẦẨẪẬÂÁÀÃẢẠĐẾỀỂỄỆÊÉÈẺẼẸÍÌỈĨỊỐỒỔỖỘÔỚỜỞỠỢƠÓÒÕỎỌỨỪỬỮỰƯÚÙỦŨỤÝỲỶỸỴ";

		 Pattern p =
		Pattern.compile("(?:[" + VIETNAMESE_DIACRITIC_CHARACTERS + "]|[A-Z])++",
		                Pattern.CANON_EQ |
		                Pattern.CASE_INSENSITIVE |
		                Pattern.UNICODE_CASE);
		 String row = "";
		 String[] arrayWord;
		File conFile = new File(filepath);
	
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(conFile)));
				String sCurrentLine = "";
				
				while ((sCurrentLine = br.readLine()) != null) {
					row += sCurrentLine;				    
				}
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		arrayWord = deleteSymbol(row);
		Map<String, Integer> words =new  HashMap<String, Integer>();

		      // split each word in a string array
			// chuyển về chử thường
		      for (int i = 0; i < arrayWord.length; i++) {
		    	  if (arrayWord[i].length()>1) {
					
		    		  String first = arrayWord[i].substring(0, 1);
		    		  String rest = arrayWord[i].substring(1,arrayWord[i].length());
		    		  first = first.toLowerCase();
		    		  rest  =rest.toLowerCase();
		    		  arrayWord[i] = first+rest;				 
				}else{
					arrayWord[i] = arrayWord[i].toLowerCase();
				}
		      }
		      //đếm số từ va trả về 1 list
		      for (int i = 0; i < arrayWord.length; i++) {
		    	  
		        if (p.matcher(arrayWord[i]).find()) {// filter out symbols
		          if (!words.containsKey(arrayWord[i])) // if new word, set value to 1
		            words.put(arrayWord[i], 1);
		          else if (words.containsKey(arrayWord[i])) // if old word, add 1 to value
		            words.put(arrayWord[i], words.get(arrayWord[i]) + 1);
		        }
		      }
		     
		    
		return sortWordDF(words);
		
	}
	public static Map<String, Integer> sortWordDF(Map<String, Integer> map){
		Map<String, Integer> sortMap =new  LinkedHashMap<>();
		List<Integer> list = new ArrayList<Integer>();
		
		
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
            list.add(entry.getValue());
        }
        Collections.sort(list,new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                //Cài đặt chỉ tiêu để so sánh trong đây
                return o2.compareTo(o1);
            }
        });; 

        Set<String> set = map.keySet();
            	for (int num : list) {
            		for (String string : set) {
                if (map.get(string) == num) {
                	sortMap.put(string, num);
                }
            }
        }
		return sortMap;
	}
	public static Map<String, Integer> proccessWordFromMailServer(String explore){
		List<Map<String, Integer>> list = new ArrayList<Map<String,Integer>>();
		Map<String, Integer> result = new HashMap<>();
		File currfile= new File("");
		String files = currfile.getAbsolutePath()+"\\src\\IncomingMailServer\\"+explore;
		File IMSFile = new File(files);
		if (IMSFile.isDirectory()) {
			File[] listFile = IMSFile.listFiles();
			for (File fileChild : listFile) {			
					String fileChildPath = files+"\\"+fileChild.getName();
					Map<String, Integer> filem = new DataProccessing().wordDF(fileChildPath);
					list.add(filem);
				
			}
		}
		
		for (Map<String, Integer> map : list) {
			Set<String> set = map.keySet();
			for (String str : set) {
				if (!result.containsKey(str)) {
					result.put(str, map.get(str));
				}else {
					int oldValue = result.get(str);
					int newValue =  map.get(str);
					result.put(str,oldValue+newValue);
				}
			}
		}
		
		return sortWordDF(result);
	}
	public static List<Map<String, Integer>> listFile(String explore){
		List<Map<String, Integer>> list = new ArrayList<Map<String,Integer>>();
		Map<String, Integer> result = new HashMap<>();
		File currfile= new File("");
		String files = currfile.getAbsolutePath()+"\\src\\IncomingMailServer\\"+explore;
		File IMSFile = new File(files);
		if (IMSFile.isDirectory()) {
			File[] listFile = IMSFile.listFiles();
			for (File fileChild : listFile) {			
					String fileChildPath = files+"\\"+fileChild.getName();
					Map<String, Integer> filem = new DataProccessing().wordDF(fileChildPath);
					list.add(filem);
				
			}
		}
		return list;
	}
	public static int countFileChild(String explore) {
		int result= 0;
		File currfile= new File("");
		String files = currfile.getAbsolutePath()+"\\src\\IncomingMailServer\\"+explore;
		File IMSFile = new File(files);
		if (IMSFile.isDirectory()) {
			File[] listFile = IMSFile.listFiles();
			for (File fileChild : listFile) {			

				result+=1;
			}
		}
		return result;
	}
	
	public static void proccessSpamWordFilter(Map<String, Integer> spamL, Map<String, Integer> nonL) {
		Map<String,Integer> rs = new HashMap<>();		
		Set<String> set = nonL.keySet();
		for (String string : set) {
			if (spamL.containsKey(string)) {
				spamL.remove(string);
			}
		}		
	}
	public static void main(String[] args) {

//		Map<String, Integer> m = MailLibrary.getMailLibrary().getNGramLibrary();
//		Map<String, Integer> n= proccessWordFromMailServer("nonspam");
//		Map<String, Integer> m= proccessWordFromMailServer("spam");
//		Map<String, Integer> n = MailLibrary.getMailLibrary().getNonSpamLibrary();
		Map<String, Integer> m = MailLibrary.getMailLibrary().getSpamLibrary();

		Set<String> set = m.keySet(); 
		for (String string : set) {
			System.out.println(string+": "+m.get(string));
		}
}
	}


//hàng: 115
//và: 83
//các: 79
//khách: 75


