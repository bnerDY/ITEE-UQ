using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace Prac4LINQ
{
    public partial class AppointmentBooking : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }

        protected void btnSave_Click(object sender, EventArgs e)
        {
            try
            {
                LINQDatabaseService service = new LINQDatabaseService();
                bool success = service.AppointmentBooking(txtPatientFirstName.Text, txtPatientLastName.Text, txtDoctorFirstName.Text, txtDoctorLastName.Text, Convert.ToDateTime(txtDate.Text + " " + txtTime.Text), txtClinicName.Text);
                if (success)
                {
                    lblResult.Text = "Save Successfully";
                }
            }
            catch (Exception ex)
            {
                lblResult.Text = ex.Message;
            }
        }
    }
}