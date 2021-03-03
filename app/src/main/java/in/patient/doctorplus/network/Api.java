package in.patient.doctorplus.network;

import java.util.List;

import in.patient.doctorplus.model.AppointmentModel;
import in.patient.doctorplus.model.CancelAppModel;
import in.patient.doctorplus.model.CheckPhoneModel;
import in.patient.doctorplus.model.ConsultationModel;
import in.patient.doctorplus.model.DoctorListModel;
import in.patient.doctorplus.model.GetAppointmentModel;
import in.patient.doctorplus.model.GetLoggedInPatientModel;
import in.patient.doctorplus.model.GetMedicalRecordModel;
import in.patient.doctorplus.model.GetPatientPrescriptionModel;
import in.patient.doctorplus.model.InvoiceNumberModel;
import in.patient.doctorplus.model.MobCallDoctorModel;
import in.patient.doctorplus.model.PasswordChange;
import in.patient.doctorplus.model.PatientIDModel;
import in.patient.doctorplus.model.PatientLogInModel;
import in.patient.doctorplus.model.PatientModel;
import in.patient.doctorplus.model.PatientRegModel;
import in.patient.doctorplus.model.PatientUpdateInfoModel;
import in.patient.doctorplus.model.PostMedicalRecord;
import in.patient.doctorplus.model.PromoCodeModel;
import in.patient.doctorplus.model.RescheduleAppModel;
import in.patient.doctorplus.model.SpecialitiesModel;
import in.patient.doctorplus.model.VerifyOTPModel;
import in.patient.doctorplus.model.WalletDetailsModel;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    @POST("MobCallDoctor")
    Call<MobCallDoctorModel> fetchSessionConnectionData(
            @Header("Authorization") String authToken,
            @Query("doctor") String doctor,
            @Query("app_id") String app_id
    );

    @FormUrlEncoded
    @POST("patient_check_phone/")
    Call<CheckPhoneModel> CheckPhoneNumber(
            @Field("ph_no") String ph_no
    );

    @FormUrlEncoded
    @POST("reset_forget_password/")
    Call<PasswordChange> ChangePwd(
            @Field("password") String password,
            @Field("id") String id
    );


    @GET("auth/user")
    Call<PatientModel> GetLoginDetails(@Header("Authorization") String authToken);


    @PUT("cancleAppointment/{id}/")
    Call<AppointmentModel> CancelAppointment(
            @Header("Authorization") String authToken,
            @Path("id") int itemId,
            @Body CancelAppModel body
    );

    @PUT("appointments/{id}/")
    Call<AppointmentModel> RescheduleAppointment(
            @Header("Authorization") String authToken,
            @Path("id") int itemId,
            @Body RescheduleAppModel body
    );

    @GET("https://teleduce.in/sendsms/")
    Call<String> getSMSOTP(
            @Query("key") String key,
            @Query("text") String text,
            @Query("route") String route,
            @Query("from") String from,
            @Query("to") String to,
            @Query("otp") String otp,
            @Query("otp_length") int otp_length
    );

    @GET("https://teleduce.in/sendsms/")
    Call<String> getSMS(
            @Query("key") String key,
            @Query("text") String text,
            @Query("route") String route,
            @Query("from") String from,
            @Query("to") String to
    );

    @FormUrlEncoded
    @POST("PatientRegInApp")
    Call<PatientRegModel> SendRegisterUser(
            @Field("username") String username,
            @Field("password") String password,
            @Field("email") String email,
            @Field("pat_id") String pat_id,
            @Field("full_name") String fullName,
            @Field("gender") String gender,
            @Field("age") int age,
            @Field("height") double height,
            @Field("weight") double weight,
            @Field("marital_status") String marital_status,
            @Field("blood_group") String blood_group,
            @Field("ph_no") String phoneNo

    );

    @GET("GeneratePatientID")
    Call<PatientIDModel> GeneratePatientID();

    @GET("https://teleduce.corefactors.in/validate-otp/a224db72-cafb-4cce-93ab-3d7f950c92e2/")
    Call<VerifyOTPModel> VerifyOTP(
            @Query("mobile") String mobile,
            @Query("otp") String otp
    );

    @FormUrlEncoded
    @POST("auth/login")
    Call<PatientLogInModel> SendLogInUser(
            @Field("username") String username,
            @Field("password") String password,
            @Field("user_type") String userType
    );

    @GET("getUpcomingAppoinment")
    Call<List<GetAppointmentModel>> GetAppointmentDetails(@Header("Authorization") String authToken);

    @GET("specific_doctor_available/")
    Call<List<DoctorListModel>> GetSpecificDoctorAvailable(@Header("Authorization") String authToken, @Query("date") String date, @Query("id") int id);

    @GET("specialist_types/")
    Call<List<SpecialitiesModel>> GetSpecialist();

    @GET("getLoggedInPatient")
    Call<GetLoggedInPatientModel> GetLoggedInPatient(
            @Header("Authorization") String authToken
    );

    @GET("patientAppointment/DoctorAvailable/")
    Call<List<DoctorListModel>> GetDoctorsList(
            @Query("id") int spl,
            @Query("date") String day
    );

    @FormUrlEncoded
    @POST("changePassword")
    Call<PasswordChange> SendChangePassword(
            @Header("Authorization") String authToken,
            @Field("old_password") String username,
            @Field("new_password") String email,
            @Field("confirm_new_password") String fullName
    );

    @GET("wallet_details/")
    Call<List<WalletDetailsModel>> GetWalletDetails(@Header("Authorization") String authToken);

    @GET("apply_promoCode/")
    Call<PromoCodeModel> ApplyPromoCode(@Header("Authorization") String authToken, @Query("code") String code);

    @FormUrlEncoded
    @POST("appointments/")
    Call<AppointmentModel> SendAppointmentDetails(
            @Header("Authorization") String authToken,
            @Field("appointment_date") String appointment_date,
            @Field("appointment_time") String appointment_time,
            @Field("doctor") String doctor,
            @Field("paid_amount") int paid_amount,
            @Field("payment_type") String payment_type,
            @Field("payment_id") String payment_id,
            @Field("Description") String Description,
            @Field("consultation_status") String consultation_status

    );

    @POST("PatientLogout/")
    Call<Void> PatientLogout(@Header("Authorization") String authToken);

    @Multipart
    @POST("patient-medical-records/")
    Call<PostMedicalRecord> UploadMedicalRecord(
            @Header("Authorization") String authToken,
            @Part("record_type") RequestBody record_type,
            @Part("description") RequestBody description,
            @Part("created_by") RequestBody created_by,
            @Part MultipartBody.Part record_files
    );

    @GET("patient-medical-records")
    Call<List<GetMedicalRecordModel>> GetMedicalRecordDetails(@Header("Authorization") String authToken);

    @GET("GetPatientPrescription/")
    Call<List<GetPatientPrescriptionModel>> GetPatientPrescription(@Header("Authorization") String authToken);

    @DELETE("patient-medical-records/{id}/")
    Call<Void> DeleteMedicalRecordDetails(@Header("Authorization") String authToken, @Path("id") int itemId);

    @GET("getInvoiceNumber")
    Call<InvoiceNumberModel> GetInvoiceNumber();


    @DELETE("MobVedioChatOparetion/{id}/")
    Call<Void> EndCall(@Header("Authorization") String authToken, @Path("id") int itemId);

    @Multipart
    @POST("PatientUpdateProfile")
    Call<PatientUpdateInfoModel> SendPatientUpdateInfo(
            @Header("Authorization") String authToken,
            @Part MultipartBody.Part image,
            @Part("full_name") RequestBody fullName,
            @Part("ph_no") RequestBody phoneNo,
            @Part("username") RequestBody username,
            @Part("email") RequestBody email,
            @Part("blood_group") RequestBody blood_group,
            @Part("age") RequestBody age,
            @Part("weight") RequestBody weight,
            @Part("gender") RequestBody gender,
            @Part("height") RequestBody height
    );

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("consultations/")
    Call<ConsultationModel> SendConsultationDetails(
            @Header("Authorization") String authToken,
            @Field("inv_number") String inv_number,
            @Field("video_audio_rating") int video_audio_rating,
            @Field("consultation_rating") int consultation_rating,
            @Field("overall_rating") int overall_rating,
            @Field("message") String message,
            @Field("duration") String duration,
            @Field("consultation_amt") double consultation_amt,
            @Field("doctor_id") int doctor_id
    );


}
