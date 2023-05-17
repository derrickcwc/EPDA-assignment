package com.example.shopoo;

public class StaticVar {
    public enum userType{
        Customer,
        Seller,
        // Multi Roles (Customer AND Seller)
        Multi,
        Admin
    }
    public enum sellerStatus{
        Pending,
        Approved,
        Rejected
    }
    public static final String[] States = {
            "Johor",
            "Kedah",
            "Kelantan",
            "Kuala Lumpur",
            "Labuan",
            "Melaka",
            "Negeri Sembilan",
            "Pahang",
            "Perak",
            "Perlis",
            "Pulau Pinang",
            "Putrajaya",
            "Sabah",
            "Sarawak",
            "Selangor",
            "Terengganu"
    };
}
