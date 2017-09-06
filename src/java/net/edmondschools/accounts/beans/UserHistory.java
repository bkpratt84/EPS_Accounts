package net.edmondschools.accounts.beans;

public class UserHistory {
    private int failCount;
    private String Success;
    private String BrowserType;
    private String AccessTime;
    private String ThirdParty;

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public String getSuccess() {
        return Success;
    }

    public void setSuccess(String Success) {
        this.Success = Success;
    }

    public String getBrowserType() {
        return BrowserType;
    }

    public void setBrowserType(String BrowserType) {
        this.BrowserType = BrowserType;
    }

    public String getAccessTime() {
        return AccessTime;
    }

    public void setAccessTime(String AccessTime) {
        this.AccessTime = AccessTime;
    }

    public String getThirdParty() {
        return ThirdParty;
    }

    public void setThirdParty(String ThirdParty) {
        this.ThirdParty = ThirdParty;
    }
}