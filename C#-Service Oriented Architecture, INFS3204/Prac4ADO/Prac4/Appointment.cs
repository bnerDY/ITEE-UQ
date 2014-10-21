using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Runtime.Serialization;

namespace Prac4
{
    [DataContract]
    public class Appointment
    {
        String healthInsuranceNO;

        [DataMember]
        public String HealthInsuranceNO
        {
            get { return healthInsuranceNO; }
            set { healthInsuranceNO = value; }
        }
        String medicalRegistrationNO;

        [DataMember]
        public String MedicalRegistrationNO
        {
            get { return medicalRegistrationNO; }
            set { medicalRegistrationNO = value; }
        }
        DateTime appointmentDateAndTime;

        [DataMember]
        public DateTime AppointmentDateAndTime
        {
            get { return appointmentDateAndTime; }
            set { appointmentDateAndTime = value; }
        }
        String clinicName;

        [DataMember]
        public String ClinicName
        {
            get { return clinicName; }
            set { clinicName = value; }
        }
    }
}