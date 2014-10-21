using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

using System.Data;
using System.Data.Sql;
using System.Data.SqlClient;
using System.Web.Configuration;

namespace Prac4
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the class name "ADODatabaseService" in code, svc and config file together.
    public class ADODatabaseService : IADODatabaseService
    {
        public bool PatientRegistration(Patient p)
        {
            SqlConnection conn = new SqlConnection(WebConfigurationManager.ConnectionStrings["Prac4"].ToString());
            conn.Open();
            try
            {
                string insert = "INSERT INTO Patient VALUES('" + p.HealthInsuranceNO + "','"
                                                            + p.FirstName + "','"
                                                            + p.LastName + "',"
                                                            + p.PhoneNumber + ",'"
                                                            + p.Address + "','"
                                                            + p.Email + "')";
                SqlCommand comm = new SqlCommand(insert, conn);
                comm.ExecuteNonQuery();
            }
            catch (SqlException e)
            {
                Console.WriteLine("An error occurred:", e);
                throw e;
            }
            catch (Exception e)
            {
                Console.WriteLine("An error occurred:", e);
                throw e;
            }
            finally
            {
                conn.Close();
            }
            return true;
        }

        public bool DoctorRegistration(Doctor d)
        {
            SqlConnection conn = new SqlConnection(WebConfigurationManager.ConnectionStrings["Prac4"].ToString());
            conn.Open();
            try
            {
                string insert = "INSERT INTO Doctor VALUES('" + d.MedicalRegistrationNO + "','"
                                                            + d.FirstName + "','"
                                                            + d.LastName + "','"
                                                            + d.HealthProfession + "',"
                                                            + d.PhoneNumber + ",'"
                                                            + d.Email + "')";
                SqlCommand comm = new SqlCommand(insert, conn);
                comm.ExecuteNonQuery();
            }
            catch (SqlException e)
            {
                Console.WriteLine("An error occurred:", e);
                throw e;
            }
            catch (Exception e)
            {
                Console.WriteLine("An error occurred:", e);
                throw e;
            }
            finally
            {
                conn.Close();
            }
            return true;
        }

        public Patient GetPatientInfo(string firstName, string lastName)
        {
            SqlConnection conn = new SqlConnection(WebConfigurationManager.ConnectionStrings["Prac4"].ToString());
            conn.Open();
            Patient patient = new Patient();
            try
            {
                string query = "SELECT * FROM Patient where firstName = '" + firstName + "' AND lastName ='"+lastName+"'";
                SqlCommand comm = new SqlCommand(query, conn);
                SqlDataReader reader = comm.ExecuteReader();
                if (reader.HasRows)
                {
                    while (reader.Read())
                    {
                        patient.HealthInsuranceNO = reader[0].ToString();
                        patient.FirstName = firstName;
                        patient.LastName = lastName;
                        patient.PhoneNumber = Convert.ToInt32(reader[3]);
                        patient.Address = reader[4].ToString();
                        patient.Email = reader[5].ToString();

                    }
                    reader.Close();
                    
                }
                else
                {
                    reader.Close();
                    return null;
                }
            }
            catch (SqlException e)
            {
                Console.WriteLine("An error occurred:", e);
                throw e;
            }
            catch (Exception e)
            {
                Console.WriteLine("An error occurred:", e);
                throw e;
            }
            finally
            {
                conn.Close();
            }
            return patient;
        }

        public Doctor GetDoctorInfo(string firstName, string lastName)
        {
            SqlConnection conn = new SqlConnection(WebConfigurationManager.ConnectionStrings["Prac4"].ToString());
            conn.Open();
            Doctor doctor = new Doctor();
            try
            {
                string query = "SELECT * FROM Doctor where firstName = '" + firstName + "' AND lastName ='" + lastName + "'";
                SqlCommand comm = new SqlCommand(query, conn);
                SqlDataReader reader = comm.ExecuteReader();
                if (reader.HasRows)
                {
                    while (reader.Read())
                    {
                        doctor.MedicalRegistrationNO = reader[0].ToString();
                        doctor.FirstName = firstName;
                        doctor.LastName = lastName;
                        doctor.HealthProfession = reader[3].ToString();
                        doctor.PhoneNumber = Convert.ToInt32(reader[4]);
                        doctor.Email = reader[5].ToString();

                    }
                    reader.Close();
                }
                else
                {
                    reader.Close();
                    return null;
                }
            }
            catch (SqlException e)
            {
                Console.WriteLine("An error occurred:", e);
                return null;
            }
            catch (Exception e)
            {
                Console.WriteLine("An error occurred:", e);
                return null;
            }
            finally
            {
                conn.Close();
            }
            return doctor;
        }

        public bool AppointmentBooking(string pfirstName, string plastName, string dfirstName, string dlastName, DateTime AppoitmentDateAndTime, string clinicName)
        {
            string insuranceNo = this.GetPatientInfo(pfirstName, plastName) == null ? "" : this.GetPatientInfo(pfirstName, plastName).HealthInsuranceNO;
            string registrationNo = this.GetDoctorInfo(dfirstName, dlastName) == null ? "" : this.GetDoctorInfo(dfirstName, dlastName).MedicalRegistrationNO;
            SqlConnection conn = new SqlConnection(WebConfigurationManager.ConnectionStrings["Prac4"].ToString());
            conn.Open();
            try
            {
                string insert = "INSERT INTO Appointment VALUES('" + insuranceNo + "','"
                                                                   + registrationNo + "','"
                                                                   + AppoitmentDateAndTime + "','"
                                                                   + clinicName + "')"; 
                SqlCommand comm = new SqlCommand(insert, conn);
                comm.ExecuteNonQuery();
            }
            catch (SqlException e)
            {
                Console.WriteLine("An error occurred:", e);
                throw e;
            }
            catch (Exception e)
            {
                Console.WriteLine("An error occurred:", e);
                throw e;
            }
            finally
            {
                conn.Close();
            }
            return true;
        }

        public Appointment GetAppointment(string pfirstName, string plastName, string dfirstName, string dlastName)
        {
            string insuranceNo = this.GetPatientInfo(pfirstName, plastName) == null ? "" : this.GetPatientInfo(pfirstName, plastName).HealthInsuranceNO;
            string registrationNo = this.GetDoctorInfo(dfirstName, dlastName) == null ? "" : this.GetDoctorInfo(dfirstName, dlastName).MedicalRegistrationNO;
            SqlConnection conn = new SqlConnection(WebConfigurationManager.ConnectionStrings["Prac4"].ToString());
            conn.Open();
            Appointment ap = new Appointment();
            try
            {
                string query = "SELECT * FROM Appointment where insuranceNo = '" + insuranceNo + "' AND registrationNo ='" + registrationNo + "'";
                SqlCommand comm = new SqlCommand(query, conn);
                SqlDataReader reader = comm.ExecuteReader();
                if (reader.HasRows)
                {
                    while (reader.Read())
                    {
                        ap.HealthInsuranceNO = insuranceNo;
                        ap.MedicalRegistrationNO = registrationNo;
                        ap.AppointmentDateAndTime = Convert.ToDateTime(reader[2].ToString());
                        ap.ClinicName = reader[3].ToString();
                    }
                    reader.Close();
                }
                else
                {
                    reader.Close();
                    return null;
                }
            }
            catch (SqlException e)
            {
                Console.WriteLine("An error occurred:", e);
                throw e;
            }
            catch (Exception e)
            {
                Console.WriteLine("An error occurred:", e);
                throw e;
            }
            finally
            {
                conn.Close();
            }
            return ap;
        }

        public bool AppointmentReschedule(string pfirstName, string plastName, string dfirstName, string dlastName, DateTime newAppointmentTime)
        {
            string insuranceNo = this.GetPatientInfo(pfirstName, plastName) == null ? "" : this.GetPatientInfo(pfirstName, plastName).HealthInsuranceNO;
            string registrationNo = this.GetDoctorInfo(dfirstName, dlastName) == null ? "" : this.GetDoctorInfo(dfirstName, dlastName).MedicalRegistrationNO;
            SqlConnection conn = new SqlConnection(WebConfigurationManager.ConnectionStrings["Prac4"].ToString());
            conn.Open();
            try
            {
                string update = "UPDATE Appointment SET datetime = '" + newAppointmentTime + "' WHERE insuranceNo = '" + insuranceNo + "' AND registrationNo = '" + registrationNo + "'";
                SqlCommand comm = new SqlCommand(update, conn);
                comm.ExecuteNonQuery();
            }
            catch (SqlException e)
            {
                Console.WriteLine("An error occurred:", e);
                throw e;
            }
            catch (Exception e)
            {
                Console.WriteLine("An error occurred:", e);
                throw e;
            }
            finally
            {
                conn.Close();
            }
            return true;
        }
    }
}
