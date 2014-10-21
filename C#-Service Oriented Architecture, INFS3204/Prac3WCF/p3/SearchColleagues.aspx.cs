using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;
using System.Text;

namespace p3
{
    public partial class SearchColleagues : System.Web.UI.Page
    {
        protected void Page_Load(object sender, EventArgs e)
        {

        }
        public void search(object sender, EventArgs e)
        {
            Service2 Campany = new Service2();
            Person person = new Person();
            List<Person> Colleagues = new List<Person>();
            person.firstName = TextBox1.Text;
            person.lastName = TextBox2.Text;
            Colleagues = Campany.GetColleagues(person.firstName, person.lastName);
            if (Colleagues == null)
            {
                Label1.Text = "Not found";
                TextBox3.Text = "";
            }
            else
            {
                TextBox3.Text = "";
                foreach (Person p in Colleagues)
                {
                    TextBox3.Text = TextBox3.Text + p.firstName + "," + p.lastName + "," + p.dateOfBirth + "," +
                        p.email + "," + p.streetAddress + "," + p.suburb + "," + p.state + "," + p.postcode
                        + "," + p.job.positionNumber+ "," + p.job.positionTitle + "," + p.job.positionDescription
                        + "," + p.job.companyName + "\r\n";
                }
            }
        }
    }
}