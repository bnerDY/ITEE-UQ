<%@ Page Title="Patient Registration" Language="C#" MasterPageFile="~/Site.master" AutoEventWireup="true"
    CodeBehind="PatientRegistration.aspx.cs" Inherits="Prac4LINQ._Default" %>

<asp:Content ID="HeaderContent" runat="server" ContentPlaceHolderID="HeadContent">
</asp:Content>
<asp:Content ID="BodyContent" runat="server" ContentPlaceHolderID="MainContent">
    <p>
     <asp:Label ID="Label7" runat="server" Text="First Name :   "></asp:Label><asp:TextBox ID="txtSearchFirstName" runat="server"></asp:TextBox>
</p>
    <p>
        <asp:Label ID="Label8" runat="server" Text="Last Name :   "></asp:Label>
     <asp:TextBox ID="txtSearchLastName" runat="server"></asp:TextBox>
     </p>
    <p>
     <asp:Button ID="btnSearch" runat="server" onclick="btnSearch_Click" 
         Text="Search" />
&nbsp;&nbsp;&nbsp;</p>
    <p>
     &nbsp;<asp:Label ID="lblMessage" runat="server"></asp:Label>
</p>
    <p>
    <asp:Label ID="Label1" runat="server" Text="Insurance No :   "></asp:Label>
    <asp:TextBox ID="txtInsuranceNo" runat="server"></asp:TextBox>
 </p>
 <p>
    <asp:Label ID="Label2" runat="server" Text="First Name :   "></asp:Label>
    <asp:TextBox ID="txtFirstName" runat="server"></asp:TextBox>
 </p>
 <p>
    <asp:Label ID="Label3" runat="server" Text="Last Name :   "></asp:Label>
    <asp:TextBox ID="txtLastName" runat="server"></asp:TextBox>
 </p>
 <p>
    <asp:Label ID="Label4" runat="server" Text="Phone Number :   "></asp:Label>
    <asp:TextBox ID="txtPhoneNumber" runat="server"></asp:TextBox>
 </p>
 <p>
    <asp:Label ID="Label5" runat="server" Text="Address :   "></asp:Label>
    <asp:TextBox ID="txtAddress" runat="server"></asp:TextBox>
 </p>
 <p>
    <asp:Label ID="Label6" runat="server" Text="Email :   "></asp:Label>
    <asp:TextBox ID="txtEmail" runat="server"></asp:TextBox>
 </p>
    <p>
        <asp:Button ID="btnSave" runat="server" Text="Save" onclick="btnSave_Click" />
        <asp:Label ID="lblResult" runat="server"></asp:Label>
 </p>
</asp:Content>
