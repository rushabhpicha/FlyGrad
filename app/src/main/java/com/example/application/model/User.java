package com.example.application.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

    public class User {

        @SerializedName("uid")
        @Expose
        private String uid;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("profileUrl")
        @Expose
        private String profileUrl;
        @SerializedName("coverUrl")
        @Expose
        private String coverUrl;
        @SerializedName("userToken")
        @Expose
        private String userToken;
        @SerializedName("gre")
        @Expose
        private String gre;
        @SerializedName("toefl")
        @Expose
        private String toefl;
        @SerializedName("workex")
        @Expose
        private String workex;
        @SerializedName("ugradcollege")
        @Expose
        private String ugradcollege;
        @SerializedName("ugradpercent")
        @Expose
        private String ugradpercent;
        @SerializedName("technicalpapers")
        @Expose
        private String technicalpapers;
        @SerializedName("extracurricular")
        @Expose
        private String extracurricular;
        @SerializedName("unione")
        @Expose
        private String unione;
        @SerializedName("unitwo")
        @Expose
        private String unitwo;
        @SerializedName("unithree")
        @Expose
        private String unithree;

        public String getUnione() {
            return unione;
        }

        public void setUnione(String unione) {
            this.unione = unione;
        }

        public String getUnitwo() {
            return unitwo;
        }

        public void setUnitwo(String unitwo) {
            this.unitwo = unitwo;
        }
        public String getUnithree() {
            return unithree;
        }

        public void setUnithree(String unithree) {
            this.unithree = unithree;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        @SerializedName("state")
        @Expose
        private String state;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getProfileUrl() {
            return profileUrl;
        }

        public void setProfileUrl(String profileUrl) {
            this.profileUrl = profileUrl;
        }

        public String getCoverUrl() {
            return coverUrl;
        }

        public void setCoverUrl(String coverUrl) {
            this.coverUrl = coverUrl;
        }

        public String getUserToken() {
            return userToken;
        }

        public void setUserToken(String userToken) {
            this.userToken = userToken;
        }

        public String getGre() {
            return gre;
        }

        public void setGre(String gre) {
            this.gre = gre;
        }

        public String getToefl() {
            return toefl;
        }

        public void setToefl(String toefl) {
            this.toefl = toefl;
        }

        public String getWorkex() {
            return workex;
        }

        public void setWorkex(String workex) {
            this.workex = workex;
        }

        public String getUgradcollege() {
            return ugradcollege;
        }

        public void setUgradcollege(String ugradcollege) {
            this.ugradcollege = ugradcollege;
        }

        public String getUgradpercent() {
            return ugradpercent;
        }

        public void setUgradpercent(String ugradpercent) {
            this.ugradpercent = ugradpercent;
        }

        public String getTechnicalpapers() {
            return technicalpapers;
        }

        public void setTechnicalpapers(String technicalpapers) {
            this.technicalpapers = technicalpapers;
        }

        public String getExtracurricular() {
            return extracurricular;
        }

        public void setExtracurricular(String extracurricular) {
            this.extracurricular = extracurricular;
        }
    }

