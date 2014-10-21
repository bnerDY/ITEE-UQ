using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace Prac4LINQ
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
                LINQDatabaseService service = new LINQDatabaseService();
                Doctor d = service.GetDoctorInfo(firstName, lastName);
                if (d != null)
                {
                    txtRegistrationNo.Text = d.registrationNo;
                    txtFirstName.Text = d.firstName;
                    txtLastName.Text = d.lastName;
                    txtPhoneNumber.Text = d.phoneNumber.ToString();
                    txtProfession.Text = d.profession;
                    txtEmail.Text = d.email;
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
                d.registrationNo = txtRegistrationNo.Text;
                d.firstName = txtFirstName.Text;
                d.lastName = txtLastName.Text;
                d.phoneNumber = Convert.ToInt32(txtPhoneNumber.Text);
                d.profession = txtProfession.Text;
                d.email = txtEmail.Text;
                LINQDatabaseService service = new LINQDatabaseService();
                if (service.DoctorRegistration(d))
                {
                    lblResult.Text = "Saved successfully";
                }
                else
                {
                    lblResult.Text = "Exception happens";
                }
            }
            catch (Exception ex)
            {
                lblResult.Text = ex.Message;
            }
        }
    }
}
