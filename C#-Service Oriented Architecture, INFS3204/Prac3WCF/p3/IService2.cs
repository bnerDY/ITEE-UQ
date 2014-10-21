using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.Text;

namespace p3
{
    // NOTE: You can use the "Rename" command on the "Refactor" menu to change the interface name "IService2" in both code and config file together.
    [ServiceContract]
    public interface IService2
    {
        [OperationContract]
        Boolean SaveInfo(Person person, Job job);

        [OperationContract]
        Job GetJobInfo(string firstName, string lastName);

        [OperationContract]
        List<Person> GetColleagues(string firstName, string lastName);

    }

    [DataContract]
    public class Person
    {
        [DataMember]
        public string firstName { get; set; }
        [DataMember]
        public string lastName { get; set; }
        [DataMember]
        public DateTime dateOfBirth { get; set; }
        [DataMember]
        public string email { get; set; }
        [DataMember]
        public string streetAddress { get; set; }
        [DataMember]
        public string suburb { get; set; }
        [DataMember]
        public string state { get; set; }
        [DataMember]
        public int postcode { get; set; }
        [DataMember]
        public Job job { get; set; }
    }

    [DataContract]
    public class Job
    {
        [DataMember]
        public int positionNumber { get; set; }
        [DataMember]
        public string positionTitle { get; set; }
        [DataMember]
        public string positionDescription { get; set; }
        [DataMember]
        public string companyName { get; set; }
    }
}
