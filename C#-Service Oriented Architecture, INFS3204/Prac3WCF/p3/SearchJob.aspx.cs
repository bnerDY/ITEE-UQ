using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

namespace p3
{
    public partial class SearchJob : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }
        public void search(object sender, EventArgs e)
        {
            Service2 Campany = new Service2();
            Person person = new Person();
            person.firstName = TextBox1.Text;
            person.lastName = TextBox2.Text;
            Job job = Campany.GetJobInfo(person.firstName, person.lastName);
            if (job == null)
            {
                Label1.Text = "Not found";
            }
            else
            {
                Label1.Text = job.positionNumber.ToString() + ", " + job.positionTitle.ToString() + ", " + job.positionDescription.ToString() + ", " + job.companyName.ToString();
            }

        }
    }
}