public class User {
    private String accountID;
    private final String userType = "EndUser";
    private String userName;
    private String firstName;
    private String lastName;
    private String gender;
    private String mobileNumber;
    private String email;
    private String password;

    private final String dateCreated;

    public User() {
        this.accountID = null;
        this.userName = null;
        this.firstName = null;
        this.lastName = null;
        this.gender = null;
        this.mobileNumber = null;
        this.email = null;
        this.password = null;

        long currentTimeMillis = System.currentTimeMillis();
        this.dateCreated = String.valueOf(currentTimeMillis);
    }

    public User(String accountID, String userName, String firstName, String lastName,
                String gender, String mobileNumber, String password, String email) {
        this.setAccountID(accountID);
        this.setUserName(userName);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setGender(gender);
        this.setMobileNumber(mobileNumber);
        this.setEmail(email);
        this.setPassword(password);

        long currentTimeMillis = System.currentTimeMillis();
        this.dateCreated = String.valueOf(currentTimeMillis);
    }

    public String getAccountID() {
        return accountID;
    }

    public String getUserType() {
        return userType;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setAccountID(String string) {
        this.accountID = string;
    }

    public void setUserName(String string) {
        this.userName = string;
    }

    public void setFirstName(String string) {
        this.firstName = string;
    }

    public void setLastName(String string) {
        this.lastName = string;
    }

    public void setGender(String string) {
        this.gender = string;
    }

    public void setMobileNumber(String string) {
        this.mobileNumber = string;
    }

    public void setEmail(String string) {
        this.email = string;
    }

    public void setPassword(String string) {
        this.password = string;
    }

    public boolean verifyPassword(String userNameInput, String passwordInput) {
        boolean result = false;

        if(this.userName.matches(userNameInput)) {
            if(this.password.matches(passwordInput)) {
                result = true;
            }
        }

        return result;
    }
}
