package cse360project;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.util.ArrayList;

import cse360project.utils.DatabaseHelper;
import cse360project.utils.Role;

public class User {
    public int id;
    public String username;
    public String password;
    public String email;
    public String inviteCode;
    public boolean accountSetUp;
    public boolean OTP;
    public Timestamp OTP_expiration;
    public String firstName;
    public String middleName;
    public String lastName;
    public String preferredName;
    public boolean is_admin;
    public boolean is_student;
    public boolean is_instructor;

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

    public boolean hasRole(Role role) {
        if (role == Role.ADMIN)      return this.is_admin;
        if (role == Role.INSTRUCTOR) return this.is_instructor;
        if (role == Role.STUDENT)    return this.is_student;
        return false;
    }

    public void setRole(Role role, boolean hasRole) {
        if (role == Role.ADMIN)      this.is_admin = hasRole;
        if (role == Role.INSTRUCTOR) this.is_instructor = hasRole;
        if (role == Role.STUDENT)    this.is_student = hasRole;
        DatabaseHelper.updateUser(this);
    }

    public ArrayList<Role> getRoles() {
        ArrayList<Role> roles = new ArrayList<>();
        if (this.hasRole(Role.ADMIN)) {
            roles.add(Role.ADMIN);
        }
        if (this.hasRole(Role.STUDENT)) {
            roles.add(Role.STUDENT);
        }
        if (this.hasRole(Role.INSTRUCTOR)) {
            roles.add(Role.INSTRUCTOR);
        }
        return roles;
    }

    public String getFullName() {
        String fullName = "";
        if (this.firstName != null && ! this.firstName.isBlank()) {
            fullName += this.firstName + " ";
        }
        if (this.middleName != null && ! this.middleName.isBlank()) {
            fullName += this.middleName + " ";
        }
        if (this.lastName != null && ! this.lastName.isBlank()) {
            fullName += this.lastName;
        }
        return fullName;
    }

    public String getPreferredName() {
        String out;
        if (this.preferredName != null && ! this.preferredName.isBlank()) {
            out = this.preferredName;
        } else {
            out = this.firstName;
        }
        if (out == null || out.isBlank()) return "";
        return out;
    }

    public static User fromResultSet(ResultSet rs) throws SQLException {
        return new User(rs.getInt("id"), rs.getString("username"), rs.getString("password"), rs.getString("email"), rs.getString("inviteCode"), rs.getBoolean("accountSetUp"), rs.getBoolean("OTP"), rs.getTimestamp("OTP_expiration"), rs.getString("firstName"), rs.getString("middleName"), rs.getString("lastName"), rs.getString("preferredName"), rs.getBoolean("is_admin"), rs.getBoolean("is_instructor"), rs.getBoolean("is_student"));
    }

    public static User createInvitedUser(boolean is_admin, boolean is_instructor, boolean is_student) {
        return new User(-1, null, null, null, getRandomInviteCode(), false, false, null, null, null, null, null, is_admin, is_instructor, is_student);
    }

    static String getRandomInviteCode() {
        return "invitecode"; // TODO: THIS
    }

    public static String getRandomOTP() {
        return "otp"; // TODO: THIS
    }

    static final int otp_expiration_days = 30;

    /**
     * Get the timestamp of an OTP expiration that was generated now.
     * @return the timestamp of expiration
     */
    public static Timestamp getNewOTPExpirationTimestamp() {
        // get the date 30 days in the future at start of day
        LocalDate futureDate = LocalDate.now().plusDays(otp_expiration_days);
        LocalDateTime futureDateTime = futureDate.atStartOfDay();

        // convert to timestamp
        return Timestamp.valueOf(futureDateTime);
    }
}
