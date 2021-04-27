package com.example.markutapp_01;

public class User_Details {
    String first_name,last_name,email_id,password,contact,type,security_question, security_answer;

    // Constructor for null User_Details object
    public User_Details() {
    }

    // Constructor for new registration or information from database
    public User_Details(String first_name, String last_name, String email_id, String password,
                        String contact, String role, String sec_question,
                        String sec_answer) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email_id = email_id;
        this.password = password;
        this.contact = contact;
        this.type=role;
        this.security_question=sec_question;
        this.security_answer=sec_answer;
    }

    public String getSecurity_question() {
        return security_question;
    }

    public String getSecurity_answer() {
        return security_answer;
    }

    public String getType() {
        return type;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getEmail_id() {
        return email_id;
    }

    public String getPassword() {
        return password;
    }

    public String getContact() {
        return contact;
    }


}
