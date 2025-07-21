import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailAuthorityComponent } from './detail-authority.component';

describe('DetailAuthorityComponent', () => {
  let component: DetailAuthorityComponent;
  let fixture: ComponentFixture<DetailAuthorityComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DetailAuthorityComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailAuthorityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
