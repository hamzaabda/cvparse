import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { OffreEmploiService } from './offre-emploi.service';

describe('OffreEmploiService', () => {
  let service: OffreEmploiService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    service = TestBed.inject(OffreEmploiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
