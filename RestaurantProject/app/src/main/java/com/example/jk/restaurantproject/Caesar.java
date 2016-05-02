package com.example.jk.restaurantproject;

/**
 * Created by dlawk_000 on 2015-12-04.
 */
public class Caesar {

    public Caesar(){}

    public static char special1[] = {'!','@','#','$','%','^','*','(',')'};
    public static char special2[] = {'$','%','^','*','(',')','!','@','#'};

    public String crypt(String plain){
        String txt = "";
        for(int i=0;i<plain.length();i++) {
            char a = plain.charAt(i);
            //숫자부분
            if(a>=48 && a<=57){
                a+=3;
                if(a>57) a-=10;
            }
            // 영어 소문자 부분
            else if(a>=97 && a<=122){
                a+=3;
                if(a>122) a-=26;
            }
            // 영어 대문자 부분
            else if(a>=65 && a<=90){
                a+=3;
                if(a>90) a-=26;
            }
            else{
                for(int j=0;j<special1.length;j++) {
                    if (a == special1[j]){
                        a = special2[j];
                        break;
                    }
                }
            }
            txt = txt+a;
        }
        return txt;
    }

    public String decrypt(String cipher){
        String txt = "";
        for(int i=0;i<cipher.length();i++) {
            char a = cipher.charAt(i);
            //숫자부분
            if(a>=48 && a<=57){
                a-=3;
                if(a<48) a+=10;
            }
            // 영어 소문자 부분
            else if(a>=97 && a<=122){
                a-=3;
                if(a<97) a+=26;
            }
            // 영어 대문자 부분
            else if(a>=65 && a<=90){
                a-=3;
                if(a<65) a+=26;
            }
            else{
                for(int j=0;j<special1.length;j++) {
                    if (a == special2[j]){
                        a = special1[j];
                        break;
                    }
                }
            }
            txt = txt+a;
        }
        return txt;
    }
}
