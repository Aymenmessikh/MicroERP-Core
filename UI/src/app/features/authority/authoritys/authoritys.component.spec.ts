import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AuthoritysComponent } from './authoritys.component';

describe('AuthoritysComponent', () => {
  let component: AuthoritysComponent;
  let fixture: ComponentFixture<AuthoritysComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AuthoritysComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AuthoritysComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
