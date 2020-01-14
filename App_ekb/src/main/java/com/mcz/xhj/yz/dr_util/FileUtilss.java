package com.mcz.xhj.yz.dr_util;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

/** 
 * 类描述：FileUtil 
 *  @author hexiaoming 
 *  @version   
 */  
public class FileUtilss {  
      
    public static File updateDir = null;  
    public static File updateFile = null;  
    /***********保存升级APK的目录***********/  
    public static final String KonkaApplication = "konkaUpdateApplication";  
      
    public static boolean isCreateFileSucess;  
  
    /**  
    * 方法描述：createFile方法 
    */
    public static void createFile(String app_name) {  
          
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            isCreateFileSucess = true;  
              
            updateDir = new File(Environment.getExternalStorageDirectory()+ "/" + KonkaApplication +"/");  
            updateFile = new File(updateDir + "/" + app_name + ".apk");  
  
            if (!updateDir.exists()) {  
                updateDir.mkdirs();  
            }  
            if (!updateFile.exists()) {  
                try {  
                    updateFile.createNewFile();  
                } catch (IOException e) {  
                    isCreateFileSucess = false; 
                    e.printStackTrace();  
                }  
            }  
  
        }else{  
            isCreateFileSucess = false;  
        }  
    }  
}  
