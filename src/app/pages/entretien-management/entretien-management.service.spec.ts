import { TestBed } from '@angular/core/testing';

import { EntretienManagementService } from './entretien-management.service';

describe('EntretienManagementService', () => {
  let service: EntretienManagementService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EntretienManagementService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
