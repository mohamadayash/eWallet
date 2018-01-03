package com.example.omarsaad.appfortesting;

/**
 * Created by Omar Saad on 12/19/2015.
 */
public class LicenseType {

    public int LicenseTypeId;
    public String Type;
    public int Validity;
    public boolean Enabled;

    public int getLicenseTypeId() {
        return LicenseTypeId;
    }

    public String getType() {
        return Type;
    }

    public int getValidity() {
        return Validity;
    }

    public boolean isEnabled() {
        return Enabled;
    }

    public void setLicenseTypeId(int licenseTypeId) {
        LicenseTypeId = licenseTypeId;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setValidity(int validity) {
        Validity = validity;
    }

    public void setEnabled(boolean enabled) {
        Enabled = enabled;
    }

    @Override
    public String toString() {
        return "LicenseType{" +
                "Type='" + Type + '\'' +
                '}';
    }
}
