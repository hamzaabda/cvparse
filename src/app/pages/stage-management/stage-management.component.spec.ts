import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StageManagementComponent } from './stage-management.component';

describe('StageManagementComponent', () => {
  let component: StageManagementComponent;
  let fixture: ComponentFixture<StageManagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StageManagementComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StageManagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
