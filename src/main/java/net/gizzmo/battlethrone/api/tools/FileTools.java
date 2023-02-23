package net.gizzmo.battlethrone.api.tools;

import java.io.*;

public class FileTools {
	public FileTools() {
	}
	
    public static void copy(InputStream inputStream, File file) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];

            int bytesRead;
            while((bytesRead = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }

            fileOutputStream.close();
            inputStream.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
    
    public static void mkdir(File file) {
        try {
        	file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
    
    public static void mkdirJson(File file) {
        try {
        	file.getParentFile().mkdirs();
            file.createNewFile();
            
            FileWriter writer = new FileWriter(file);
            writer.write("{}");
            writer.flush();
            writer.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
