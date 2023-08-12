package SpamFilters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class MoveMail {
	public MoveMail() {
		
	}
	public void moveMail(String filePath, String destPath)  {
		File file = new File(filePath);
		if(file.isFile() && file.exists()) {
					
				FileInputStream fis;
				FileOutputStream fos ;
				try {
					fis = new FileInputStream(file);
					fos = new FileOutputStream(destPath+"\\" +file.getName());
					byte[] buffer = new byte[1024];
					int readed;
					while((readed = fis.read(buffer)) != -1) {
						fos.write(buffer, 0, readed);
					}
					fis.close();
					fos.close();
					file.delete();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				
				}
			
		}
	}

}
