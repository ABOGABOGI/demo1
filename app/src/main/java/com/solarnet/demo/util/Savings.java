package com.solarnet.demo.util;

import com.google.gson.annotations.SerializedName;
import com.orhanobut.hawk.Hawk;
import com.solarnet.demo.data.contact.Contact;

public class Savings {

   public static  void saveEmail(String email){
       Hawk.put(Constant.save_email,email);
   }


    public static String getEmail(){
        return Hawk.get(Constant.save_email,"");
    }

    public static  void saveName(String name){
        Hawk.put(Constant.save_name,name);
    }

    public static String getName(){
        return Hawk.get(Constant.save_name,"");
    }

    public static  String getProfilePicture(){
       return Hawk.get(Constant.profile_picture,"");
    }

    public static void saveProfilPicture(String pic){
        Hawk.put(Constant.profile_picture,pic);
    }

    public static String getToken(){
       return Hawk.get(Constant.token,"");
    }

    public static void saveToken(String token){
       Hawk.put(Constant.token,token);
    }

}
