
// This class is meant to store data for use throughout app.
// Access these variables from any location by instantiating this static class, e.g.
//      Globals globals = Globals.getInstance();
// then access variables, e.g.
//      globals.getUser();
public class Globals {
    // global variables below
    private static User user = new User();

    private static final Globals instance = new Globals();

    private Globals() {
    }

    public static Globals getInstance() {
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
