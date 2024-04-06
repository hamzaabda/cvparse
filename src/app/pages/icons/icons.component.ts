import { Component, OnInit } from '@angular/core';
import { AuthService } from '../icons/auth.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-icons',
  templateUrl: './icons.component.html',
  styleUrls: ['./icons.component.scss']
})
export class IconsComponent implements OnInit {

  public copy: string;
  public offresEmploi: any[]; 
  public nouvelleOffreEmploi: any = {};

  constructor(private authService: AuthService) { }

  ngOnInit() {
    this.getAllOffresEmploi();
  }

  createOffreEmploi(offreEmploi: any) {
    this.authService.createOffreEmploi(offreEmploi).subscribe(() => {
      Swal.fire('Succès', 'Offre d\'emploi créée avec succès', 'success').then((result) => {
        if (result.isConfirmed) {
          this.reloadPage();
        }
      });
      this.nouvelleOffreEmploi = {};
    }, error => {
      Swal.fire('Erreur', 'Erreur lors de la création de l\'offre d\'emploi', 'error');
      console.error('Erreur lors de la création de l\'offre d\'emploi : ', error);
      this.nouvelleOffreEmploi = {};
    });
  }

  deleteOffreEmploi(id: number) {
    this.authService.deleteOffreEmploi(id).subscribe(() => {
      Swal.fire('Succès', 'Offre d\'emploi supprimée avec succès', 'success').then((result) => {
        if (result.isConfirmed) {
          this.reloadPage();
        }
      });
      this.getAllOffresEmploi();
    }, error => {
      Swal.fire('Erreur', 'Erreur lors de la suppression de l\'offre d\'emploi', 'error');
      console.error('Erreur lors de la suppression de l\'offre d\'emploi : ', error);
    });
  }

  updateOffreEmploi(id: number, offreEmploi: any) {
    this.authService.updateOffreEmploi(id, offreEmploi).subscribe(() => {
      Swal.fire('Succès', 'Offre d\'emploi mise à jour avec succès', 'success').then((result) => {
        if (result.isConfirmed) {
          this.reloadPage();
        }
      });
      this.getAllOffresEmploi();
    }, error => {
      Swal.fire('Erreur', 'Erreur lors de la mise à jour de l\'offre d\'emploi', 'error');
      console.error('Erreur lors de la mise à jour de l\'offre d\'emploi : ', error);
    });
  }

  getAllOffresEmploi() {
    this.authService.getAllOffresEmploi().subscribe(data => {
      this.offresEmploi = data;
    }, error => {
      Swal.fire('Erreur', 'Erreur lors de la récupération des offres d\'emploi', 'error');
      console.error('Erreur lors de la récupération des offres d\'emploi : ', error);
    });
  }

  reloadPage() {
    window.location.reload();
  }
}
