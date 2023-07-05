package com.example.recyclerviewpractice;

public class ContactModel {
    int img,id;
    String name, number;

    public ContactModel(int img , String name , String number){
        this.img= img;
        this.name = name;
        this.number = number;
    }

    public ContactModel(){
        this.name= name;
        this.number = number;
    }
}
