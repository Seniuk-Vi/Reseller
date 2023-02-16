package com.example.reseller;

import android.net.Uri;

import java.util.Comparator;

public class Book {

    private String Name, Category, Description;
    private int Price;
    private String Thumbnail;
    private int Count;
    private String imageUrl;
    private String UserID, Orders, UserName;
    private String UserPhoto;
    private boolean privacy;
    private String status;
    private int itemOrders;
    private int itemViews;
    private boolean New;
    private String adv;
    private int complains;

    public int getComplains() {
        return complains;
    }

    public void setComplains(int complains) {
        this.complains = complains;
    }

    public boolean isNew() {
        return New;
    }

    public void setNew(boolean aNew) {
        New = aNew;
    }


    public String getAdv() {
        return adv;
    }

    public void setAdv(String adv) {
        this.adv = adv;
    }

    public int getItemOrders() {
        return itemOrders;
    }

    public void setItemOrders(int itemOrders) {
        this.itemOrders = itemOrders;
    }

    public int getItemViews() {
        return itemViews;
    }

    public void setItemViews(int itemViews) {
        this.itemViews = itemViews;
    }


    //Ordering
    private String CustomerName, CustomerLocation, CustomerPhone, CustomerID;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCustomerLocation() {
        return CustomerLocation;
    }

    public void setCustomerLocation(String customerLocation) {
        CustomerLocation = customerLocation;
    }

    public String getCustomerPhone() {
        return CustomerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        CustomerPhone = customerPhone;
    }

    public boolean isPrivacy() {
        return privacy;
    }


    public String getUserName() {
        return UserName;
    }

    public String getUserPhoto() {
        return UserPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        UserPhoto = userPhoto;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setPrivacy(boolean privacy) {
        this.privacy = privacy;
    }

    public String getOrders() {
        return Orders;
    }

    public void setOrders(String orders) {
        Orders = orders;
    }


    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public Book(String name, String categorie, String description, int common_google_signin_btn_icon_dark_focused) {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Book() {
    }

    public static Comparator<Book> ByPrice = new Comparator<Book>() {
        @Override
        public int compare(Book o1, Book o2) {
            return Integer.compare(o1.getPrice(), o2.getPrice());
        }
    };
    public static Comparator<Book> ByViews = new Comparator<Book>() {
        @Override
        public int compare(Book o1, Book o2) {
            return Integer.compare(o1.getItemViews(), o2.getItemViews());
        }
    };


//    public static Comparator<Book> ByCategory = new Comparator<Book>(String compare) {
//
//        @Override
//        public int compare(Book o1, Book o2) {
//            return o1.getCategory().equals();
//        }
//    };

    public Book(String name, String description) {
        Name = name;
        Description = description;
    }

    public void setCount(int count) {
        Count = count;
    }

    public int getCount() {
        return Count++;
    }

    public String getName() {
        return Name;
    }

    public String getCategory() {
        return Category;
    }

    public String getDescription() {
        return Description;
    }

    public int getPrice() {
        return Price;
    }

    public String getThumbnail() {
        return Thumbnail;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public void setThumbnail(String thumbnail) {
        Thumbnail = thumbnail;
    }
}
