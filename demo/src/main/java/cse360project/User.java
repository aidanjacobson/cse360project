package cse360project;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class User {
    int id;
    String username;
    String password;
    String email;
    String inviteCode;
    boolean accountSetUp;
    boolean OTP;
    Timestamp OTP_expiration;
    String firstName;
    String middleName;
    String lastName;
    String preferredName;
    boolean is_admin;
    boolean is_student;
    boolean is_instructor;

    public User(int id, String username, String password, String email, String inviteCode, boolean accountSetUp, boolean OTP, Timestamp OTP_expiration, String firstName, String middleName, String lastName, String preferredName, boolean is_admin, boolean is_instructor, boolean is_student) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.inviteCode = inviteCode;
        this.accountSetUp = accountSetUp;
        this.OTP = OTP;
        this.OTP_expiration = OTP_expiration;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.preferredName = preferredName;
        this.is_admin = is_admin;
        this.is_instructor = is_instructor;
        this.is_student = is_student;
    }

    static User fromResultSet(ResultSet rs) throws SQLException {
        return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("email"), rs.getString("inviteCode"), rs.getBoolean("accountSetUp"), rs.getBoolean("OTP"), rs.getTimestamp("OTP_expiration"), rs.getString("firstName"), rs.getString("middleName"), rs.getString("lastName"), rs.getString("preferredName"), rs.getBoolean("is_admin"), rs.getBoolean("is_instructor"), rs.getBoolean("is_student"));
    }

    static User createInvitedUser(boolean is_admin, boolean is_instructor, boolean is_student) {
        return new User(-1, null, null, null, getRandomInviteCode(), false, false, null, null, null, null, null, is_admin, is_instructor, is_student);
    }

    static String getRandomInviteCode() {
        return "invitecode";
    }
}
