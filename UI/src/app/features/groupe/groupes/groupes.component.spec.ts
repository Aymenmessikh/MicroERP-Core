import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GroupesComponent } from './groupes.component';

describe('GroupesComponent', () => {
  let component: GroupesComponent;
  let fixture: ComponentFixture<GroupesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GroupesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GroupesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
