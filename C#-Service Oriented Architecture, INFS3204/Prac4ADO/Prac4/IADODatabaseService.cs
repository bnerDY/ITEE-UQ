using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace Prac4
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the interface name "IADODatabaseService" in both code and config file together.
    [ServiceContract]
    public interface IADODatabaseService
    {
        [OperationContract]
        Boolean PatientRegistration(Patient p);

        [OperationContract]
        Boolean DoctorRegistration(Doctor d);

        [OperationContract]
        Patient GetPatientInfo(String firstName, String lastName);

        [OperationContract]
        Doctor GetDoctorInfo(String firstName, String lastName);

        [OperationContract]
        Boolean AppointmentBooking(String pfirstName, String plastName, String dfirstName, String dlastName, DateTime AppoitmentDateAndTime, String clinicName);
      
        [OperationContract]
        Appointment GetAppointment(String pfirstName, String plastName, String dfirstName, String dlastName);
      
        [OperationContract]
        Boolean AppointmentReschedule(String pfirstName, String plastName, String dfirstName, String dlastName, DateTime newAppointmentTime);

    }

     [DataContract]
    public class Doctor
    {
        String medicalRegistrationNO;

        [DataMember]
        public String MedicalRegistrationNO
        {
            get { return medicalRegistrationNO; }
            set { medicalRegistrationNO = value; }
        }
        String firstName;

        [DataMember]
        public String FirstName
        {
            get { return firstName; }
            set { firstName = value; }
        }
        String lastName;

        [DataMember]
        public String LastName
        {
            get { return lastName; }
            set { lastName = value; }
        }
        String healthProfession;

        [DataMember]
        public String HealthProfession
        {
            get { return healthProfession; }
            set { healthProfession = value; }
        }
        int phoneNumber;

        [DataMember]
        public int PhoneNumber
        {
            get { return phoneNumber; }
            set { phoneNumber = value; }
        }
        String email;

        [DataMember]
        public String Email
        {
            get { return email; }
            set { email = value; }
        }
    }

    [DataContract]
    public class Patient
    {
        String healthInsuranceNO;

        [DataMember]
        public String HealthInsuranceNO
        {
            get { return healthInsuranceNO; }
            set { healthInsuranceNO = value; }
        }
        String firstName;

        [DataMember]
        public String FirstName
        {
            get { return firstName; }
            set { firstName = value; }
        }
        String lastName;

        [DataMember]
        public String LastName
        {
            get { return lastName; }
            set { lastName = value; }
        }
        int phoneNumber;

        [DataMember]
        public int PhoneNumber
        {
            get { return phoneNumber; }
            set { phoneNumber = value; }
        }
        String address;

        [DataMember]
        public String Address
        {
            get { return address; }
            set { address = value; }
        }
        String email;

        [DataMember]
        public String Email
        {
            get { return email; }
            set { email = value; }
        }
    }
}
