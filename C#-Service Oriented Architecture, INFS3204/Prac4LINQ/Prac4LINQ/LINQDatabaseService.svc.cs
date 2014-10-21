using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace Prac4LINQ
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "LINQDatabaseService" in code, svc and config file together.
    public class LINQDatabaseService : ILINQDatabaseService
    {
        public bool PatientRegistration(Patient p)
        {
            ClinicalDatabaseDataContext dc = new ClinicalDatabaseDataContext();
            try
            {
                dc.Patients.InsertOnSubmit(p);
                dc.SubmitChanges();
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                throw e;
            }
            return true;
        }

        public bool DoctorRegistration(Doctor d)
        {
            ClinicalDatabaseDataContext dc = new ClinicalDatabaseDataContext();
            try
            {
                dc.Doctors.InsertOnSubmit(d);
                dc.SubmitChanges();
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                throw e;
            }
            return true;
        }

        public Patient GetPatientInfo(string firstName, string lastName)
        {
            ClinicalDatabaseDataContext dc = new ClinicalDatabaseDataContext();
            Patient patient = new Patient();
            try
            {
                var result = from p in dc.Patients
                             where p.firstName.Equals(firstName) && p.lastName.Equals(lastName)
                             select new { p.insuranceNo, p.firstName, p.lastName, p.phoneNumber, p.address, p.email };


                if (result.Any())
                {
                    foreach (var patientInfo in result)
                    {
                        patient.insuranceNo = patientInfo.insuranceNo;
                        patient.firstName = patientInfo.firstName;
                        patient.lastName = patientInfo.lastName;
                        patient.phoneNumber = patientInfo.phoneNumber;
                        patient.address = patientInfo.address;
                        patient.email = patientInfo.email;
                    }
                }
                else
                {
                    return null;
                }

            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                throw e;
            }
            return patient;
        }

        public Doctor GetDoctorInfo(string firstName, string lastName)
        {
            ClinicalDatabaseDataContext dc = new ClinicalDatabaseDataContext();
            Doctor doctor = new Doctor();
            try
            {
                var result = from d in dc.Doctors
                             where d.firstName.Equals(firstName) && d.lastName.Equals(lastName)
                             select new { d.registrationNo, d.firstName, d.lastName, d.phoneNumber, d.profession, d.email };


                if (result.Any())
                {
                    foreach (var doctorInfo in result)
                    {
                        doctor.registrationNo = doctorInfo.registrationNo;
                        doctor.firstName = doctorInfo.firstName;
                        doctor.lastName = doctorInfo.lastName;
                        doctor.phoneNumber = doctorInfo.phoneNumber;
                        doctor.profession = doctorInfo.profession;
                        doctor.email = doctorInfo.email;
                    }
                }
                else
                {
                    return null;
                }

            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                throw e;
            }
            return doctor;
        }

        public bool AppointmentBooking(string pfirstName, string plastName, string dfirstName, string dlastName, DateTime appoitmentDateAndTime, string clinicName)
        {
            ClinicalDatabaseDataContext dc = new ClinicalDatabaseDataContext();
            Appointment ap = new Appointment();
            if (this.GetPatientInfo(pfirstName, plastName) == null || this.GetDoctorInfo(dfirstName, dlastName) == null)
            {
                return false;
            }
            else
            {
                ap.insuranceNo = this.GetPatientInfo(pfirstName, plastName).insuranceNo;
                ap.registrationNo = this.GetDoctorInfo(dfirstName, dlastName).registrationNo;
                ap.datetime = appoitmentDateAndTime;
                ap.clinicname = clinicName;
                try
                {
                    dc.Appointments.InsertOnSubmit(ap);
                    dc.SubmitChanges();
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.Message);
                    throw e;
                }
                return true;
            }
        }

        public Appointment GetAppointment(string pfirstName, string plastName, string dfirstName, string dlastName)
        {
            ClinicalDatabaseDataContext dc = new ClinicalDatabaseDataContext();
            string insuranceNo = this.GetPatientInfo(pfirstName, plastName) == null ? "" : this.GetPatientInfo(pfirstName, plastName).insuranceNo;
            string registrationNo = this.GetDoctorInfo(dfirstName, dlastName) == null ? "" : this.GetDoctorInfo(dfirstName, dlastName).registrationNo;
            Appointment appointment = new Appointment();
            try
            {
                var result = from ap in dc.Appointments
                             where ap.insuranceNo.Equals(insuranceNo) && ap.registrationNo.Equals(registrationNo)
                             select new { ap.datetime, ap.clinicname };


                if (result.Any())
                {
                    foreach (var appInfo in result)
                    {
                        appointment.insuranceNo = insuranceNo;
                        appointment.registrationNo = registrationNo;
                        appointment.clinicname = appInfo.clinicname;
                        appointment.datetime = appInfo.datetime;
                    }
                }
                else
                {
                    return null;
                }

            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                throw e;
            }
            return appointment;
        }

        public bool AppointmentReschedule(string pfirstName, string plastName, string dfirstName, string dlastName, DateTime newAppointmentTime)
        {
            ClinicalDatabaseDataContext dc = new ClinicalDatabaseDataContext();
            Appointment ap = new Appointment();
            if (this.GetPatientInfo(pfirstName, plastName) == null || this.GetDoctorInfo(dfirstName, dlastName) == null)
            {
                return false;
            }
            else
            {
                try
                {
                    string insuranceNo = this.GetPatientInfo(pfirstName, plastName).insuranceNo;
                    string registrationNo = this.GetDoctorInfo(dfirstName, dlastName).registrationNo;
                    var existingApp = (from app in dc.Appointments where app.insuranceNo == insuranceNo && app.registrationNo == registrationNo select app).First();
                    existingApp.datetime = newAppointmentTime;
                    dc.SubmitChanges();
                }
                catch (Exception e)
                {
                    Console.WriteLine(e.Message);
                    throw e;
                }
                return true;
            }
        }
    }
}
