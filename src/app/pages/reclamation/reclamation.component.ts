import { Component, OnInit } from '@angular/core';
import { ReclamationService } from './reclamation.service';
import { Reclamation } from './reclamation.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-reclamation',
  templateUrl: './reclamation.component.html',
  styleUrls: ['./reclamation.component.scss']
})
export class ReclamationComponent implements OnInit {

  reclamations: Reclamation[] = [];
  selectedReclamation: Reclamation | null = null;
  newReclamation: Reclamation = { id: 0, titre: '', description: '', statut: '', email: '' }; // Initialisation de newReclamation
  isFormVisible: boolean = false; // Ajout de la propriété pour gérer la visibilité du formulaire

  constructor(private reclamationService: ReclamationService) { }

  ngOnInit(): void {
    this.getAllReclamations();
  }

  getAllReclamations(): void {
    this.reclamationService.getAllReclamations().subscribe(
      (data) => {
        this.reclamations = data;
      },
      (error) => {
        console.error(error);
      }
    );
  }

  getReclamationById(id: number): void {
    this.reclamationService.getReclamationById(id).subscribe(
      (data) => {
        this.selectedReclamation = data;
      },
      (error) => {
        console.error(error);
      }
    );
  }

  createReclamation(reclamation: Reclamation): void {
    this.reclamationService.createReclamation(reclamation).subscribe(
      (data) => {
        Swal.fire('Success', 'Reclamation created successfully', 'success');
        this.getAllReclamations(); // Rafraîchir la liste
        this.newReclamation = { id: 0, titre: '', description: '', statut: '', email: '' }; // Réinitialisation de newReclamation après création
        this.isFormVisible = false; // Cacher le formulaire après création
      },
      (error) => {
        console.error(error);
        Swal.fire('Error', 'Failed to create reclamation', 'error');
      }
    );
  }

  updateReclamation(id: number, reclamation: Reclamation): void {
    this.reclamationService.updateReclamation(id, reclamation).subscribe(
      (data) => {
        Swal.fire('Success', 'Reclamation updated successfully', 'success');
        this.getAllReclamations(); // Rafraîchir la liste
        this.selectedReclamation = null; // Désélectionner après la mise à jour
      },
      (error) => {
        console.error(error);
        Swal.fire('Error', 'Failed to update reclamation', 'error');
      }
    );
  }

  deleteReclamation(id: number): void {
    this.reclamationService.deleteReclamation(id).subscribe(
      () => {
        Swal.fire('Success', 'Reclamation deleted successfully', 'success');
        this.getAllReclamations(); // Rafraîchir la liste
        this.selectedReclamation = null; // Désélectionner après la suppression
      },
      (error) => {
        console.error(error);
        Swal.fire('Error', 'Failed to delete reclamation', 'error');
      }
    );
  }

  toggleFormVisibility(): void {
    this.isFormVisible = !this.isFormVisible; // Inverse la visibilité du formulaire
  }
}
