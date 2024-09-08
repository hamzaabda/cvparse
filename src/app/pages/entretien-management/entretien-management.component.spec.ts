import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EntretienManagementComponent } from './entretien-management.component';

describe('EntretienManagementComponent', () => {
  let component: EntretienManagementComponent;
  let fixture: ComponentFixture<EntretienManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EntretienManagementComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EntretienManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
