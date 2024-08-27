import { Component, OnInit, ElementRef } from '@angular/core';
import { Location } from '@angular/common';
import { Router } from '@angular/router';
import Swal from 'sweetalert2'; // Import de SweetAlert2
import { DeconnexionService } from './deconnexion.service';
import { NotificationService } from './notification.service';

// Définition des routes
const ROUTES = [
  { path: '/dashboard', title: 'Dashboard' },
  { path: '/user-profile', title: 'User Profile' },
  // Ajoutez d'autres routes ici si nécessaire
];

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss']
})
export class NavbarComponent implements OnInit {
  public focus;
  public listTitles: any[];
  public location: Location;
  public userToken: string; // Ajoutez une propriété pour stocker le token de l'utilisateur
  public notifications: any[] = []; // Array to store notifications

  constructor(
    location: Location,
    private element: ElementRef,
    private router: Router,
    private deconnexionService: DeconnexionService,
    private notificationService: NotificationService // Inject NotificationService
  ) {
    this.location = location;
  }

  ngOnInit() {
    this.listTitles = ROUTES;
    this.userToken = localStorage.getItem('token');
    this.fetchNotifications(); // Fetch notifications on component initialization
  }

  fetchNotifications(): void {
    this.notificationService.getNotifications().subscribe(
      (data: any[]) => {
        this.notifications = data;
        Swal.fire({
          title: 'Notifications Loaded',
          text: 'Notifications have been successfully fetched.',
          icon: 'success',
          confirmButtonText: 'OK'
        });
      },
      (error) => {
        console.error('Error fetching notifications', error);
        Swal.fire({
          title: 'Error',
          text: 'Unable to fetch notifications.',
          icon: 'error',
          confirmButtonText: 'OK'
        });
      }
    );
  }

  markAsRead(notificationId: number): void {
    this.notificationService.markAsRead(notificationId).subscribe(
      () => {
        this.notifications = this.notifications.filter(
          (notification) => notification.id !== notificationId
        );
        Swal.fire({
          title: 'Notification Read',
          text: 'Notification has been marked as read.',
          icon: 'success',
          confirmButtonText: 'OK'
        });
      },
      (error) => {
        console.error('Error marking notification as read', error);
        Swal.fire({
          title: 'Error',
          text: 'Unable to mark notification as read.',
          icon: 'error',
          confirmButtonText: 'OK'
        });
      }
    );
  }

  getTitle() {
    var titlee = this.location.prepareExternalUrl(this.location.path());
    if (titlee.charAt(0) === '#') {
      titlee = titlee.slice(1);
    }

    for (var item = 0; item < this.listTitles.length; item++) {
      if (this.listTitles[item].path === titlee) {
        return this.listTitles[item].title;
      }
    }
    return 'Dashboard';
  }

  logout(): void {
    Swal.fire({
      title: 'Confirmation',
      text: 'Voulez-vous vraiment vous déconnecter ?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Oui',
      cancelButtonText: 'Non'
    }).then((result) => {
      if (result.isConfirmed) {
        this.deconnexionService.deconnectAndRedirect();
      }
    });
  }
}
