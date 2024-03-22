import { Component, OnInit, ViewChild } from '@angular/core';
import { NgForm } from '@angular/forms';
import { AuthService } from '../user-profile/auth.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent implements OnInit {
  token: string;
  admins: any[];
  editingAdmin: boolean = false;
  editedAdmin: any;

  @ViewChild('adminForm', { static: false }) adminForm: NgForm;

  constructor(private authService: AuthService) { }

  ngOnInit() {
    this.loginAdmin();
    this.getAdmins();
  }

  loginAdmin() {
    const email = "admin@example.com";
    const password = "password";
    this.authService.login(email, password).subscribe(
      response => {
        this.token = response.jwt;
        console.log("Login successful. Token:", this.token);
        this.getAdmins();
      },
      error => {
        console.error("Login failed:", error);
      }
    );
  }

  onSubmit() {
    if (!this.token) {
      console.error("Token not available. Please log in first.");
      return;
    }
  
    const formValue = this.adminForm.value;
    const username = formValue.username;
    const password = formValue.password;
    const email = formValue.email;
  
    const adminData = {
      "username": username,
      "password": password,
      "email": email
    };
  
    this.authService.addAdmin(this.token, adminData).subscribe(
      response => {
        console.log("New admin added successfully.");
        Swal.fire({
          icon: 'success',
          title: 'Success',
          text: 'New admin added successfully!',
        });
        this.getAdmins();
      },
      error => {
        console.error("Failed to add new admin:", error);
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'Failed to add new admin. Please try again later.',
        });
      }
    );
  }

  getAdmins() {
    if (!this.token) {
      console.error("Token not available. Please log in first.");
      return;
    }

    this.authService.getAdmins(this.token).subscribe(
      response => {
        this.admins = response;
        console.log("Admins retrieved successfully:", this.admins);
      },
      error => {
        console.error("Failed to retrieve admins:", error);
      }
    );
  }

  updateAdmin(adminId: string, newData: any) {
    const parsedAdminId = parseInt(adminId);
    this.authService.updateAdmin(this.token, parsedAdminId, newData).subscribe(
        response => {
            console.log("Admin updated successfully.");
            Swal.fire({
                icon: 'success',
                title: 'Success',
                text: 'Admin updated successfully!',
            });
            this.getAdmins();
        },
        error => {
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Failed to update admin. Please try again later.',
            });
        }
    );
}


  deleteAdmin(adminId: string) {
    if (!adminId) {
      console.error("Admin ID is undefined or null.");
      return;
    }
  
    // Convertir l'ID de l'administrateur en nombre
    const adminIdNumber: number = parseInt(adminId, 10);
  
    this.authService.deleteAdmin(this.token, adminIdNumber).subscribe(
      response => {
        console.log("Admin deleted successfully.");
        Swal.fire({
          icon: 'success',
          title: 'Success',
          text: 'Admin deleted successfully!',
        });
        this.getAdmins();
      },
      error => {
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'Failed to delete admin. Please try again later.',
        });
      }
    );
  }
  

  editAdmin(admin: any) {
    this.editingAdmin = true;
    this.editedAdmin = { ...admin }; // Make a copy to avoid direct reference
  }
  
  cancelEdit() {
    this.editingAdmin = false;
    this.editedAdmin = null;
  }

  saveAdminChanges() {
    if (this.editedAdmin && this.editedAdmin._id) {
      this.updateAdmin(this.editedAdmin._id, this.editedAdmin);
      this.editingAdmin = false;
      this.editedAdmin = null;
    } else {
      console.error("Admin ID is undefined or null.");
    }
  }
}
