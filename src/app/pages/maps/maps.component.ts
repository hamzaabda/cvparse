import { Component, OnInit } from '@angular/core';
import { AuthService } from '../maps/auth.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-maps',
  templateUrl: './maps.component.html',
  styleUrls: ['./maps.component.scss']
})
export class MapsComponent implements OnInit {

  offresStage: any[] = [];
  nouvelleOffre: any = {};
  offreSelectionnee: any = {};
  offreModifiee: any = {};

  constructor(private authService: AuthService) { }

  ngOnInit() {
    this.chargerOffresStage();
    this.initMap();
  }

  chargerOffresStage() {
    this.authService.getAllOffres().subscribe(
      offres => {
        this.offresStage = offres;
      },
      error => {
        console.log('Erreur lors du chargement des offres de stage : ', error);
        Swal.fire('Erreur', 'Une erreur est survenue lors du chargement des offres de stage.', 'error');
      }
    );
  }

  creerOffreStage() {
    this.authService.createOffre(this.nouvelleOffre).subscribe(
      response => {
        this.nouvelleOffre = {};
        this.chargerOffresStage(); // Rafraîchir la liste des offres après création
        Swal.fire('Succès', 'L\'offre de stage a été créée avec succès.', 'success');
      },
      error => {
        console.log('Erreur lors de la création de l\'offre de stage : ', error);
        Swal.fire('Erreur', 'Une erreur est survenue lors de la création de l\'offre de stage.', 'error');
      }
    );
  }

 
  supprimerOffreStage(id: number) {
    this.authService.deleteOffre(id).subscribe(
      response => {
        this.chargerOffresStage(); // Rafraîchir la liste des offres après suppression
        Swal.fire('Succès', 'L\'offre de stage a été supprimée avec succès.', 'success');
      },
      error => {
        console.log('Erreur lors de la suppression de l\'offre de stage : ', error);
        Swal.fire('Erreur', 'Une erreur est survenue lors de la suppression de l\'offre de stage.', 'error');
      }
    );
  }

  selectionnerOffre(offre: any) {
    this.offreSelectionnee = offre;
    // Initialisez seulement les champs nécessaires
    this.offreModifiee = {
      competencesRequises: offre.competencesRequises,
      niveauEducationRequis: offre.niveauEducationRequis
    };
  }

  initMap() {
    // Votre code Google Maps reste inchangé
  }
}
