import { Component, OnInit } from '@angular/core';
import { StageService } from './stage.service';
import { OffreStage } from './offre-stage.model';

@Component({
  selector: 'app-stage-management',
  templateUrl: './stage-management.component.html',
  styleUrls: ['./stage-management.component.scss']
})
export class StageManagementComponent implements OnInit {

  offresStage: OffreStage[] = [];
  selectedFile: File | null = null;

  constructor(private stageService: StageService) { }

  ngOnInit(): void {
    this.loadOffresStage();
  }

  loadOffresStage(): void {
    this.stageService.getAllOffresStage().subscribe((data: OffreStage[]) => {
      this.offresStage = data;
    });
  }

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
  }

  onPostuler(offreStageId: number): void {
    if (this.selectedFile) {
      this.stageService.uploadCV(this.selectedFile, offreStageId).subscribe(response => {
        alert(response);
        // Rafraîchir la liste des offres de stage après avoir postulé
        this.loadOffresStage();
      }, error => {
        alert(error.error);
      });
    } else {
      alert("Veuillez sélectionner un fichier.");
    }
  }
}
