using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace Prac4LINQ
{
    public partial class _Default : System.Web.UI.Page
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
                Patient p = service.GetPatientInfo(firstName, lastName);
                if (p != null)
                {
                    txtInsuranceNo.Text = p.insuranceNo;
                    txtFirstName.Text = p.firstName;
                    txtLastName.Text = p.lastName;
                    txtPhoneNumber.Text = p.phoneNumber.ToString();
                    txtAddress.Text = p.address;
                    txtEmail.Text = p.email;
                    lblMessage.Text = "";
                }
                else
                {
                    txtInsuranceNo.Text = "";
                    txtFirstName.Text = "";
                    txtLastName.Text = "";
                    txtPhoneNumber.Text = "";
                    txtAddress.Text = "";
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
                Patient p = new Patient();
                p.insuranceNo = txtInsuranceNo.Text;
                p.firstName = txtFirstName.Text;
                p.lastName = txtLastName.Text;
                p.phoneNumber = Convert.ToInt32(txtPhoneNumber.Text);
                p.address = txtAddress.Text;
                p.email = txtEmail.Text;
                LINQDatabaseService service = new LINQDatabaseService();
                if (service.PatientRegistration(p))
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
