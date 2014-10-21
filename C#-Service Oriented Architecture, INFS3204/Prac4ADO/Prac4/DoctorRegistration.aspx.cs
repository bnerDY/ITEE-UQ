using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace Prac4
{
    public partial class About : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {
            lblMessage.Text = "";
            lblResult.Text = "";
        }

        protected void btnSearch_Click(object sender, EventArgs e)
        {
            try
            {
                string firstName = txtSearchFirstName.Text;
                string lastName = txtSearchLastName.Text;
                ADODatabaseService service = new ADODatabaseService();
                Doctor d = service.GetDoctorInfo(firstName, lastName);
                if (d != null)
                {
                    txtRegistrationNo.Text = d.MedicalRegistrationNO;
                    txtFirstName.Text = d.FirstName;
                    txtLastName.Text = d.LastName;
                    txtPhoneNumber.Text = d.PhoneNumber.ToString();
                    txtProfession.Text = d.HealthProfession;
                    txtEmail.Text = d.Email;
                    lblMessage.Text = "";
                }
                else
                {
                    txtRegistrationNo.Text = "";
                    txtFirstName.Text = "";
                    txtLastName.Text = "";
                    txtPhoneNumber.Text = "";
                    txtProfession.Text = "";
                    txtEmail.Text = "";
                    lblMessage.Text = "Not found";
                }
            }
            catch (Exception ex)
            {
                lblResult.Text = ex.Message;
            }
        }

        protected void btnSave_Click(object sender, EventArgs e)
        {
            try
            {
                Doctor d = new Doctor();
                d.MedicalRegistrationNO = txtRegistrationNo.Text;
                d.FirstName = txtFirstName.Text;
                d.LastName = txtLastName.Text;
                d.PhoneNumber = Convert.ToInt32(txtPhoneNumber.Text);
                d.HealthProfession = txtProfession.Text;
                d.Email = txtEmail.Text;
                ADODatabaseService service = new ADODatabaseService();
                if (service.DoctorRegistration(d))
                {
                    lblResult.Text = "Saved successfully";
                }
            }
            catch (Exception ex)
            {
                lblResult.Text = ex.Message;
            }
        }
    }
}
