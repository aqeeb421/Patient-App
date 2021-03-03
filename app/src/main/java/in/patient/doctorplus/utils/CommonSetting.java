package in.patient.doctorplus.utils;

public class CommonSetting {

    private static boolean skipping;
    private static String token;
    private static String ConsultationAbout;
    private static String email;
    private static String phoneNumber;

    public static boolean getSkipping() {
        return skipping;
    }

    public static void setSkipping(boolean skipping) {
        CommonSetting.skipping = skipping;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        CommonSetting.token = token;
    }

    public static String getConsultationAbout() {
        return ConsultationAbout;
    }

    public static void setConsultationAbout(String consultationAbout) {
        ConsultationAbout = consultationAbout;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        CommonSetting.email = email;
    }

    public static String getPhoneNumber() {
        return phoneNumber;
    }

    public static void setPhoneNumber(String phoneNumber) {
        CommonSetting.phoneNumber = phoneNumber;
    }
}
