import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './auth.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit, OnDestroy {
  email: string = '';
  password: string = '';

  carouselIndex: number = 0;

  carouselImages: string[] = [
    'assets/img/brand/smartup cover.jpg',
    'assets/img/brand/smt1.jpg',
    'assets/img/brand/smt2.jpg'
  ];

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit() {
    // Initializations if necessary
  }

  ngOnDestroy() {
    // Cleanup of subscriptions or other tasks
  }

  loginUser() {
    const userData = {
      email: this.email,
      password: this.password
    };
  
    this.authService.loginUser(userData).subscribe(
      (response) => {
        console.log('Login successful:', response);
        Swal.fire({
          icon: 'success',
          title: 'Login Successful',
          text: 'You have successfully logged in!'
        });
        this.router.navigate(['/dashboard']);
      },
      (error) => {
        console.error('Login error:', error);
        Swal.fire({
          icon: 'error',
          title: 'Login Error',
          text: 'Incorrect password.',
          confirmButtonText: 'Reset Password',
          showCancelButton: true,
          cancelButtonText: 'Close'
        }).then((result) => {
          if (result.isConfirmed) {
            this.resetPassword();
          }
        });
      }
    );
  }

  resetPassword() {
    this.authService.resetPassword(this.email).subscribe(
      (response) => {
        console.log('Password reset successful:', response);
        Swal.fire({
          icon: 'success',
          title: 'Reset Request Sent',
          text: response
        });
      },
      (error) => {
        console.error('Password reset error:', error);
        Swal.fire({
          icon: 'error',
          title: 'Password Reset Error',
          text: 'An error occurred during password reset. Please try again later.'
        });
      }
    );
  }

  nextImage() {
    // Move to the next image, loop back to the first image if at the end
    this.carouselIndex = (this.carouselIndex + 1) % this.carouselImages.length;
  }

  prevImage() {
    // Move to the previous image, loop back to the last image if at the beginning
    this.carouselIndex = (this.carouselIndex - 1 + this.carouselImages.length) % this.carouselImages.length;
  }
}
