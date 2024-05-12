import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmploisManagementComponent } from './emplois-management.component';

describe('EmploisManagementComponent', () => {
  let component: EmploisManagementComponent;
  let fixture: ComponentFixture<EmploisManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EmploisManagementComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmploisManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
