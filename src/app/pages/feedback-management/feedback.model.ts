export interface Feedback {
    id?: number;
    offreEmploi?: { id: number };  // Assurez-vous d'ajuster en fonction de votre entité
    offreStage?: { id: number };
    commentaire: string;
    rating: number;
    date: string;
  }
  