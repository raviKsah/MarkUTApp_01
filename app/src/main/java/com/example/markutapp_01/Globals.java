package com.example.markutapp_01;

// This class is meant to store data for use throughout app.
// Access these variables from any location by instantiating this static class,
// then access variables, e.g.
//      Globals globals = Globals.getInstance();
//      globals.getUser();
public class Globals {
    // global variables below
    private static User_Details user = new User_Details();

    private static final Globals instance = new Globals();

    private Globals() {
    }

    public static Globals getInstance() {
        return instance;
    }

    public User_Details getUser() {
        return user;
    }

    public void setUser(User_Details user) {
        this.user = user;
    }

    public void clearUser() {
        this.user = null;
    }
}
