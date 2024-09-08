import { Component, OnInit, ViewChild, ElementRef, AfterViewInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { AuthService } from '../user-profile/auth.service';
import Swal from 'sweetalert2';
import { Chart } from 'chart.js';
import { trigger, state, style, animate, transition } from '@angular/animations';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.scss'],
  animations: [
    trigger('fadeInUp', [
      state('void', style({
        opacity: 0,
        transform: 'translateY(-20px)'
      })),
      transition(':enter', [
        animate('1s ease-out')
      ])
    ])
  ]
})
export class UserProfileComponent implements OnInit, AfterViewInit {
  token: string;
  admins: any[];
  editingAdmin: boolean = false;
  editedAdmin: any;
  usersCount: number;
  usersChart: Chart;

  @ViewChild('adminForm', { static: false }) adminForm: NgForm;
  @ViewChild('usersChartCanvas', { static: false }) usersChartCanvas: ElementRef;

  constructor(private authService: AuthService) { }

  ngOnInit() {
    this.loginAdmin();
  }

  ngAfterViewInit() {
    // Optionnel : si vous avez besoin d'animations après que la vue soit complètement initialisée
  }

  loginAdmin() {
    const email = "admin@example.com";
    const password = "password";
    this.authService.login(email, password).subscribe(
      response => {
        this.token = response.jwt;
        console.log("Login successful. Token:", this.token);
        this.getAdmins();
        this.getUsersCount();
      },
      error => {
        console.error("Login failed:", error);
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

  getUsersCount() {
    if (!this.token) {
      console.error("Token not available. Please log in first.");
      return;
    }

    this.authService.getUsersCount(this.token).subscribe(
      count => {
        this.usersCount = count;
        console.log("Total users count:", this.usersCount);
        this.renderUsersChart();
      },
      error => {
        console.error("Failed to retrieve users count:", error);
      }
    );
  }

  renderUsersChart() {
    this.usersChart = new Chart(this.usersChartCanvas.nativeElement, {
      type: 'bar',
      data: {
        labels: ['Total Users'],
        datasets: [{
          label: 'Users Count',
          data: [this.usersCount],
          backgroundColor: 'rgba(75, 192, 192, 0.2)',
          borderColor: 'rgba(75, 192, 192, 1)',
          borderWidth: 1
        }]
      },
      options: {
        scales: {
          yAxes: [{
            ticks: {
              beginAtZero: true
            }
          }]
        }
      }
    });
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
        this.getUsersCount(); // Rafraîchir le nombre d'utilisateurs après l'ajout d'un nouvel administrateur
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

  editAdmin(admin: any) {
    this.editingAdmin = true;
    this.editedAdmin = { ...admin };
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
            this.getUsersCount(); // Rafraîchir le nombre d'utilisateurs après la mise à jour de l'administrateur
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
        this.getUsersCount(); // Rafraîchir le nombre d'utilisateurs après la suppression de l'administrateur
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
}
