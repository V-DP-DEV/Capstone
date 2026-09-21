public class User {
    private String name;
    private int userId;
    private String email;
    private String password;
    private String role;

    public User() {
    }

    public User(String name, int userID, String email, String password, String role) {
        this.name = name;
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}