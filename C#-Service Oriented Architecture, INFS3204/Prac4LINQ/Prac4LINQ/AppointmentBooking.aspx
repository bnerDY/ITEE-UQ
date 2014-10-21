<%@ Page Title="Appointment Booking" Language="C#" MasterPageFile="~/Site.Master" AutoEventWireup="true" CodeBehind="AppointmentBooking.aspx.cs" Inherits="Prac4LINQ.AppointmentBooking" %>
<asp:Content ID="Content1" ContentPlaceHolderID="HeadContent" runat="server">
</asp:Content>
<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">
<p>
    <asp:Label ID="Label1" runat="server" Text=" Patient's First Name"></asp:Label>
&nbsp;&nbsp;&nbsp;
    <asp:TextBox ID="txtPatientFirstName" runat="server"></asp:TextBox>
</p>
    <p>
        <asp:Label ID="Label2" runat="server" Text=" Patient's Last Name"></asp:Label>
&nbsp;&nbsp;&nbsp;
        <asp:TextBox ID="txtPatientLastName" runat="server"></asp:TextBox>
</p>
    <p>
        <asp:Label ID="Label3" runat="server" Text="Doctor's First Name"></asp:Label>
&nbsp;&nbsp;&nbsp;
        <asp:TextBox ID="txtDoctorFirstName" runat="server"></asp:TextBox>
</p>
    <p>
        <asp:Label ID="Label4" runat="server" Text=" Doctor's Last Name"></asp:Label>
&nbsp;&nbsp;&nbsp;
        <asp:TextBox ID="txtDoctorLastName" runat="server"></asp:TextBox>
</p>
    <p>
        <asp:Label ID="Label5" runat="server" Text="Appointment Date"></asp:Label>
&nbsp;&nbsp;&nbsp;
        <asp:TextBox ID="txtDate" runat="server"></asp:TextBox>
</p>
    <p>
        <asp:Label ID="Label6" runat="server" Text="Appointment Time"></asp:Label>
&nbsp;&nbsp;&nbsp;
        <asp:TextBox ID="txtTime" runat="server"></asp:TextBox>
</p>
    <p>
        <asp:Label ID="Label7" runat="server" Text="Clinic Name"></asp:Label>
&nbsp;&nbsp;&nbsp;
        <asp:TextBox ID="txtClinicName" runat="server"></asp:TextBox>
</p>
    <p>
        <asp:Button ID="btnSave" runat="server" onclick="btnSave_Click" Text="Save" />
        &nbsp;&nbsp;&nbsp;
        <asp:Label ID="lblResult" runat="server"></asp:Label>
</p>
</asp:Content>
