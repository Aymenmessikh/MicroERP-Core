import { TestBed } from '@angular/core/testing';

import { GroupesService } from './groupes.service';

describe('GroupesService', () => {
  let service: GroupesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GroupesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
