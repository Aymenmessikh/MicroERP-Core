import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateAuthorityComponent } from './update-authority.component';

describe('UpdateAuthorityComponent', () => {
  let component: UpdateAuthorityComponent;
  let fixture: ComponentFixture<UpdateAuthorityComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UpdateAuthorityComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateAuthorityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
