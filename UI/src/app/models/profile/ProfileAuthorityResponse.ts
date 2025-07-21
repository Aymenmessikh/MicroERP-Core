import { AuthorityResponse } from '../authority/AuthorityResponse';

export interface ProfileAuthorityResponse{
    id:number;
    authorityResponse:AuthorityResponse;
    granted:boolean;
}
