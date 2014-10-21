<%@ Page Title="Appointment Rescheduling" Language="C#" MasterPageFile="~/Site.Master" AutoEventWireup="true" CodeBehind="AppointmentRescheduling.aspx.cs" Inherits="Prac4LINQ.AppointmentRescheduling" %>
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
        <asp:Button ID="btnSearch" runat="server" Text="Search" 
            onclick="btnSearch_Click" style="height: 26px" />
&nbsp;&nbsp;&nbsp;
        <asp:Button ID="btnReschedule" runat="server" Text="Reschedule" 
            onclick="btnReschedule_Click" />
&nbsp;&nbsp;&nbsp;
        <asp:Label ID="lblResult" runat="server"></asp:Label>
</p>
</asp:Content>
