import { GroupeResponse } from '../groupe/GroupeResponse';
import { ModuleResponse } from '../module/ModuleResponse';
import { RoleResponse } from '../role/RoleResponse';
import { ProfileAuthorityResponse } from './ProfileAuthorityResponse';

export interface ProfileResponse{
    id:number;
    libelle:string;
    actif:boolean;
    groupeResponse:GroupeResponse;
    moduleResponses:ModuleResponse[];
    roleResponses:RoleResponse[];
    profileAuthorityResponses:ProfileAuthorityResponse[];
}
