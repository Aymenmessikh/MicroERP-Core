import { TestBed } from '@angular/core/testing';

import { AuthorityTypeService } from './authority-type.service';

describe('AuthorityTypeService', () => {
  let service: AuthorityTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuthorityTypeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
