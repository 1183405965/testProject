package cn.hou.purcharseback.pojo;

public class QtUser {
    private String username;
    private String password;
    private String email;
    private String qq;

    public QtUser(String username, String password, String email, String qq) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.qq = qq;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }
}
