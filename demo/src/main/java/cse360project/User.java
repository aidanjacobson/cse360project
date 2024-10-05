package cse360project;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.util.ArrayList;

import cse360project.utils.DatabaseHelper;
import cse360project.utils.PasswordGenerator;
import cse360project.utils.Role;

public class User {
    public int id;
    public String username;
    public char[] password;
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

    /**
     * Constructor for User
     * @param id the users id in the db, or -1 if not in db
     * @param username the username. can be null
     * @param password the password. can not be null but can be empty
     * @param email the email. can be null
     * @param inviteCode the invite code. can be null
     * @param accountSetUp whether or not the account is set up
     * @param OTP whether or not the account has an OTP
     * @param OTP_expiration the timestamp of the OTP expiration
     * @param firstName the first name. cannot be null
     * @param middleName the middle name. can be null
     * @param lastName the last name. cannot be null
     * @param preferredName the preferred name. can be null
     * @param is_admin whether or not the user is an admin
     * @param is_instructor whether or not the user is an instructor
     * @param is_student 
     */
    public User(int id, String username, char[] password, String email, String inviteCode, boolean accountSetUp, boolean OTP, Timestamp OTP_expiration, String firstName, String middleName, String lastName, String preferredName, boolean is_admin, boolean is_instructor, boolean is_student) {
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

    /**
     * Check if the user has a given role
     * @param role the role to check
     * @return true if the user has the role, false otherwise
     */
    public boolean hasRole(Role role) {
        if (role == Role.ADMIN)      return this.is_admin;
        if (role == Role.INSTRUCTOR) return this.is_instructor;
        if (role == Role.STUDENT)    return this.is_student;
        return false;
    }

    /**
     * Set a role for the user
     * @param role the role to set
     * @param hasRole whether or not the user has the role
     */
    public void setRole(Role role, boolean hasRole) {
        if (role == Role.ADMIN)      this.is_admin = hasRole;
        if (role == Role.INSTRUCTOR) this.is_instructor = hasRole;
        if (role == Role.STUDENT)    this.is_student = hasRole;
        DatabaseHelper.updateUser(this);
    }

    /**
     * Get all the roles the user has
     * @return an arraylist of roles the user has
     */
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

    /**
     * Get the full name of the user
     * @return the full name of the user
     */
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

    /**
     * Get the preferred name of the user
     * @return the preferred name of the user
     */
    public String getPreferredName() {
        String out;

        // if the preferred name is set, use it
        if (this.preferredName != null && ! this.preferredName.isBlank()) {
            out = this.preferredName;
        } else { // otherwise use the first name
            out = this.firstName;
        }
        if (out == null || out.isBlank()) return ""; // if the first name is null or empty, return empty string
        return out;
    }

    /**
     * Create a user from a result set from the database
     * @param rs the result set
     * @return the user
     * @throws SQLException
     */
    public static User fromResultSet(ResultSet rs) throws SQLException {
        // convert password bytes to char array
        byte[] passwordBytes = rs.getBytes("password");
        char[] passwordCharArray = new char[passwordBytes.length];
        for (int i = 0; i < passwordBytes.length; i++) {
            passwordCharArray[i] = (char) passwordBytes[i];
        }

        return new User(rs.getInt("id"), rs.getString("username"), passwordCharArray, rs.getString("email"), rs.getString("inviteCode"), rs.getBoolean("accountSetUp"), rs.getBoolean("OTP"), rs.getTimestamp("OTP_expiration"), rs.getString("firstName"), rs.getString("middleName"), rs.getString("lastName"), rs.getString("preferredName"), rs.getBoolean("is_admin"), rs.getBoolean("is_instructor"), rs.getBoolean("is_student"));
    }

    /**
     * Create a user that is invited with particular roles
     * @param is_admin whether or not the user should be an admin
     * @param is_instructor whether or not the user should be an instructor
     * @param is_student whether or not the user should be a student
     * @return
     */
    public static User createInvitedUser(boolean is_admin, boolean is_instructor, boolean is_student) {
        return new User(-1, null, "".toCharArray(), null, getRandomInviteCode(), false, false, null, null, null, null, null, is_admin, is_instructor, is_student);
    }

    /**
     * Get a random invite code
     * @return the invite code
     */
    static String getRandomInviteCode() {
        return String.valueOf(PasswordGenerator.generate());
    }

    /**
     * Get a random OTP
     * @return the OTP
     */
    public static char[] getRandomOTP() {
        return PasswordGenerator.generate();
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
