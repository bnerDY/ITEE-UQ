using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace Prac4
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
                ADODatabaseService service = new ADODatabaseService();
                Patient p = service.GetPatientInfo(firstName, lastName);
                if (p != null)
                {
                    txtInsuranceNo.Text = p.HealthInsuranceNO;
                    txtFirstName.Text = p.FirstName;
                    txtLastName.Text = p.LastName;
                    txtPhoneNumber.Text = p.PhoneNumber.ToString();
                    txtAddress.Text = p.Address;
                    txtEmail.Text = p.Email;
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
                p.HealthInsuranceNO = txtInsuranceNo.Text;
                p.FirstName = txtFirstName.Text;
                p.LastName = txtLastName.Text;
                p.PhoneNumber = Convert.ToInt32(txtPhoneNumber.Text);
                p.Address = txtAddress.Text;
                p.Email = txtEmail.Text;
                ADODatabaseService service = new ADODatabaseService();
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
