
// This class is meant to store data for use throughout app.
// Access these variables from any location by instantiating this static class,
// then access variables, e.g.
//      Globals globals = Globals.getInstance();
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

    public void clearUser() {
        this.user = null;
    }
}
