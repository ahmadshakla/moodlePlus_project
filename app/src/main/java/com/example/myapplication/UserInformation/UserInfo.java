package com.example.myapplication.UserInformation;


import java.util.List;

/**
 * second level, get user full name and unique id from the token
 */
public class UserInfo {
    private String id;
    private String username;
    private String fullname;
    private String email;
    private String department;
    private String idnumber;
    private String auth;
    private String suspended;
    private String confirmed;
    private String lang;
    private String theme;
    private String timezone;
    private String mailformat;
    private String description;
    private String descriptionformat;
    private String profileimageurlsmall;
    private String profileimageurl;
    private List<UserInfo.preferences> preferences;

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }

    public String getEmail() {
        return email;
    }

    public String getDepartment() {
        return department;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public String getAuth() {
        return auth;
    }

    public String getSuspended() {
        return suspended;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public String getLang() {
        return lang;
    }

    public String getTheme() {
        return theme;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getMailformat() {
        return mailformat;
    }

    public String getDescription() {
        return description;
    }

    public String getDescriptionformat() {
        return descriptionformat;
    }

    public String getProfileimageurlsmall() {
        return profileimageurlsmall;
    }

    public String getProfileimageurl() {
        return profileimageurl;
    }

    public List<UserInfo.preferences> getPreferences() {
        return preferences;
    }

    static class preferences {
        String name;
        String value;

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }
}