import { Component, OnInit } from '@angular/core';
import { DocumentService } from '../document-management/document.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-document-management',
  templateUrl: './document-management.component.html',
  styleUrls: ['./document-management.component.scss']
})
export class DocumentManagementComponent implements OnInit {
  selectedFile: File | null = null;
  documentId: number = 0;
  documentDetails: any;
  downloadUrl: string = '';
  documentsList: any[] = [];

  constructor(private documentService: DocumentService) {}

  ngOnInit(): void {
    this.loadAllDocuments(); // Load documents on component initialization
  }

  // File input change handler
  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
  }

  // Upload the selected file
  onUpload(): void {
    if (this.selectedFile) {
      const formData = new FormData();
      formData.append('nom', this.selectedFile.name);
      formData.append('type', this.selectedFile.type);
      formData.append('contenu', this.selectedFile);

      this.documentService.uploadDocument(formData).subscribe({
        next: response => {
          Swal.fire('Success', 'Document uploaded successfully!', 'success');
          this.loadAllDocuments(); // Refresh the list of documents after upload
        },
        error: () => {
          Swal.fire('Error', 'Failed to upload document!', 'error');
        }
      });
    }
  }

  // Get document by ID and prepare download URL
  onGetDocument(): void {
    if (this.documentId) {
      this.documentService.getDocument(this.documentId).subscribe({
        next: response => {
          const blob = new Blob([response], { type: response.type });
          this.downloadUrl = URL.createObjectURL(blob);
          Swal.fire('Success', 'Document retrieved successfully!', 'success');
        },
        error: () => {
          Swal.fire('Error', 'Failed to retrieve document!', 'error');
        }
      });
    } else {
      Swal.fire('Error', 'Please enter a valid document ID!', 'error');
    }
  }

  // Load all documents
  loadAllDocuments(): void {
    this.documentService.getAllDocuments().subscribe({
      next: documents => {
        this.documentsList = documents;
      },
      error: () => {
        Swal.fire('Error', 'Failed to load documents!', 'error');
      }
    });
  }

  // Delete document by ID
  onDeleteDocument(): void {
    if (this.documentId) {
      this.documentService.deleteDocument(this.documentId).subscribe({
        next: () => {
          Swal.fire('Success', 'Document deleted successfully!', 'success');
          this.loadAllDocuments(); // Refresh the document list after deletion
        },
        error: () => {
          Swal.fire('Error', 'Failed to delete document!', 'error');
        }
      });
    } else {
      Swal.fire('Error', 'Please enter a valid document ID!', 'error');
    }
  }
}
