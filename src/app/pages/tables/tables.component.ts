import { Component, OnInit } from '@angular/core';
import { AuthService } from './auth.service';
import Swal from 'sweetalert2';
import jsPDF from 'jspdf'; // Importation de jsPDF
import { DomSanitizer, SafeUrl } from '@angular/platform-browser'; // Importation pour le traitement des URLs

@Component({
  selector: 'app-tables',
  templateUrl: './tables.component.html',
  styleUrls: ['./tables.component.scss']
})
export class TablesComponent implements OnInit {
  portfolioData: {
    name: string;
    email: string;
    phone: string;
    skills: string[];
    projects: string[];
    recommendations: string[];
    profilePhoto: string | ArrayBuffer;
  } = {
    name: '',
    email: '',
    phone: '',
    skills: [],
    projects: [],
    recommendations: [],
    profilePhoto: ''
  };
  newSkill: string = '';
  newProject: string = '';
  newRecommendation: string = '';
  downloadUrl: string = '';

  constructor(private portfolioService: AuthService, private sanitizer: DomSanitizer) { }

  ngOnInit(): void {
    // Initialisation si nécessaire
  }

  // Gestion du changement de fichier pour la photo de profil
  onFileChange(event: any) {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.portfolioData.profilePhoto = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  }

  generatePortfolio() {
    console.log('Portfolio data:', this.portfolioData);

    this.portfolioData.projects = Array.isArray(this.portfolioData.projects) ? this.portfolioData.projects : [this.portfolioData.projects];
    this.portfolioData.recommendations = Array.isArray(this.portfolioData.recommendations) ? this.portfolioData.recommendations : [this.portfolioData.recommendations];

    if (Array.isArray(this.portfolioData.skills) &&
        Array.isArray(this.portfolioData.projects) &&
        Array.isArray(this.portfolioData.recommendations)) {
        this.portfolioService.generatePortfolio(this.portfolioData).subscribe(
            response => {
                console.log('Portfolio generated:', response);
                this.downloadUrl = response.downloadUrl || '';
                Swal.fire('Success', 'Portfolio generated successfully!', 'success');
            },
            error => {
                console.error('Error generating portfolio:', error);
                Swal.fire('Error', 'Failed to generate portfolio', 'error');
            }
        );
    } else {
        console.error('Portfolio data is not properly formatted');
        Swal.fire('Error', 'Invalid portfolio data format', 'error');
    }
  }

  generatePDF() {
    const doc = new jsPDF();
    doc.setFontSize(16);
    doc.text('Portfolio', 10, 10);

    // Ajouter la photo de profil
    if (this.portfolioData.profilePhoto) {
      const imgData = this.portfolioData.profilePhoto.toString();
      doc.addImage(imgData, 'JPEG', 160, 10, 30, 30); // Position et taille de l'image
    }

    doc.setFontSize(12);
    doc.text(`Name: ${this.portfolioData.name}`, 10, 50);
    doc.text(`Email: ${this.portfolioData.email}`, 10, 60);
    doc.text(`Phone: ${this.portfolioData.phone}`, 10, 70);

    // Ajouter les compétences
    doc.text('Skills:', 10, 80);
    this.portfolioData.skills.forEach((skill, index) => {
      doc.text(`${index + 1}. ${skill}`, 15, 90 + (index * 10));
    });

    // Ajouter les projets
    doc.text('Projects:', 10, 100 + (this.portfolioData.skills.length * 10));
    this.portfolioData.projects.forEach((project, index) => {
      doc.text(`${index + 1}. ${project}`, 15, 110 + (this.portfolioData.skills.length * 10) + (index * 10));
    });

    // Ajouter les recommandations
    doc.text('Recommendations:', 10, 120 + (this.portfolioData.skills.length * 10) + (this.portfolioData.projects.length * 10));
    this.portfolioData.recommendations.forEach((recommendation, index) => {
      doc.text(`${index + 1}. ${recommendation}`, 15, 130 + (this.portfolioData.skills.length * 10) + (this.portfolioData.projects.length * 10) + (index * 10));
    });

    doc.save('portfolio.pdf');
  }

  addSkill() {
    if (this.newSkill.trim()) {
      this.portfolioData.skills.push(this.newSkill.trim());
      this.newSkill = '';
    }
  }

  addProject() {
    if (this.newProject.trim()) {
      this.portfolioData.projects.push(this.newProject.trim());
      this.newProject = '';
    }
  }

  addRecommendation() {
    if (this.newRecommendation.trim()) {
      this.portfolioData.recommendations.push(this.newRecommendation.trim());
      this.newRecommendation = '';
    }
  }
}
