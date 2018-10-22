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

    public static void clearToken(){
       Hawk.delete(Constant.token);
    }

    public static String getAccountName(){
       return  Hawk.get(Constant.account_name);
    }

    public static void saveAccountName(String name){
       Hawk.put(Constant.account_name,name);
    }

    public static String getAccountKtp(){
       return Hawk.get(Constant.account_ktp);
    }

    public static void saveAccountKtp(String ktp){
       Hawk.put(Constant.account_ktp,ktp);
    }

    public static String getAlamat(){
       return Hawk.get(Constant.account_alamat);
    }

    public static void saveAlamat(String alamat){
       Hawk.put(Constant.account_alamat,alamat);
    }

    public static String getKota(){
        return Hawk.get(Constant.account_kota);
    }

    public static void saveKota(String kota){
        Hawk.put(Constant.account_kota,kota);
    }


    public static String getProvinsi(){
        return Hawk.get(Constant.account_provinsi);
    }

    public static void saveProvinsi(String provinsi){
        Hawk.put(Constant.account_kota,provinsi);
    }

    public static String getAccountNumber(){
        return Hawk.get(Constant.account_number);
    }

    public static void saveAccountNumber(String accNumber){
        Hawk.put(Constant.account_number,accNumber);
    }

}
