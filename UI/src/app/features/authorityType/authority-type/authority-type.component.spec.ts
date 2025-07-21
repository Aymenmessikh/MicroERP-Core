import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AuthorityTypeComponent } from './authority-type.component';

describe('AuthorityTypeComponent', () => {
  let component: AuthorityTypeComponent;
  let fixture: ComponentFixture<AuthorityTypeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AuthorityTypeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AuthorityTypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
